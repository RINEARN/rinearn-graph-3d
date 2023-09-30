/*
 * Author:  RINEARN (Fumihiro Matsui), 2020
 * License: CC0
 */

package org.vcssl.nano.plugin.system.terminal;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultEditorKit;

import org.vcssl.connect.ConnectorException;

/**
 * A window to display outputs of print functions etc. on GUI mode
 * GUIモード時に print 関数などの出力を表示するためのウィンドウです
 */
public class TerminalWindow {

	private static final int DEFAULT_WINDOW_X = 100;
	private static final int DEFAULT_WINDOW_Y = 100;
	private static final int DEFAULT_WINDOW_WIDTH = 600;
	private static final int DEFAULT_WINDOW_HEIGHT = 400;
	private static final String DEFAULT_WINDOW_TITLE = "Terminal";
	private static final Color DEFAULT_BACKGROUND_COLOR = Color.BLACK;
	private static final Color DEFAULT_FOREGROUND_COLOR = Color.WHITE;
	private static final Font DEFAULT_FONT = new Font("Dialog", Font.PLAIN, 16);

	private JFrame frame = null;
	private JTextArea textArea = null;
	private JScrollPane scrollPane = null;
	private JPopupMenu textAreaPopupMenu = null;

	// ウィンドウが表示OFFの状態でも、次に print された時点で表示ONにするためのフラグ。
	// 何も print しないスクリプトでも常にウィンドウが表示されると、アプリケーションの種類によっては嫌かもしれないので、
	// 初期状態では表示されないようになっている。
	// そしてスクリプト開始後に、print が呼ばれた時点でこのフラグが true なら、その時にウィンドウが表示され、フラグは false に切り替わる。
	// ただし、スクリプト内で hide 関数が呼ばれた場合、それはウィンドウの表示が不要という意思表示なので、その時点でフラグを false にする。
	private volatile boolean autoShowing = true;

	/**
	 * A class to construct components of the window on the UI thread
	 * UIスレッドで画面構築を行うためのクラスです
	 */
	private class InitRunner implements Runnable {
		@Override
		public void run() {

			// ウィンドウを生成
			TerminalWindow.this.frame = new JFrame();
			TerminalWindow.this.frame.setBounds(
				TerminalWindow.DEFAULT_WINDOW_X,
				TerminalWindow.DEFAULT_WINDOW_Y,
				TerminalWindow.DEFAULT_WINDOW_WIDTH,
				TerminalWindow.DEFAULT_WINDOW_HEIGHT
			);
			TerminalWindow.this.frame.setTitle(TerminalWindow.DEFAULT_WINDOW_TITLE);

			// テキストエリアを生成
			TerminalWindow.this.textArea = new JTextArea();
			TerminalWindow.this.textArea.setBackground(TerminalWindow.DEFAULT_BACKGROUND_COLOR);
			TerminalWindow.this.textArea.setForeground(TerminalWindow.DEFAULT_FOREGROUND_COLOR);
			TerminalWindow.this.textArea.setFont(TerminalWindow.DEFAULT_FONT);

			// テキストエリアにスクロールバーを付けてウィンドウ上に配置
			TerminalWindow.this.scrollPane = new JScrollPane(
				TerminalWindow.this.textArea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
			);
			TerminalWindow.this.frame.getContentPane().add(TerminalWindow.this.scrollPane);

			// テキストエリアの右クリックメニューを生成
			TerminalWindow.this.textAreaPopupMenu = new JPopupMenu();
			TerminalWindow.this.textAreaPopupMenu.add(new DefaultEditorKit.CutAction()).setText("Cut");
			TerminalWindow.this.textAreaPopupMenu.add(new DefaultEditorKit.CopyAction()).setText("Copy");
			TerminalWindow.this.textAreaPopupMenu.add(new DefaultEditorKit.PasteAction()).setText("Paste");

			// テキストエリアの右クリックイベントをハンドルするリスナーを生成して登録
			TerminalWindow.this.textArea.addMouseListener(new TextAreaRightClickListener());

			// テキストエリアを表示（ウィンドウは print するまで表示したくない用途もあるのでここではまだ表示しない）
			TerminalWindow.this.textArea.setVisible(true);
			TerminalWindow.this.scrollPane.setVisible(true);
		}
	}

	/**
	 * A class to make the window visible on the UI thread
	 * UIスレッドでウィンドウの表示をONにするためのクラスです
	 */
	private class ShowRunner implements Runnable {
		@Override
		public void run() {
			TerminalWindow.this.frame.setVisible(true);
		}
	}

	/**
	 * A class to check whether the window is visible or not, on the UI thread
	 * UIスレッドでウィンドウの表示/非表示状態を取得するためのクラスです
	 */
	private class VisiblityCheckRunner implements Runnable {
		boolean visiblity = false;

		@Override
		public void run() {
			this.visiblity = TerminalWindow.this.frame.isVisible();
		}

		public boolean isVisible() {
			return this.visiblity;
		}
	}

	/**
	 * A class to make the window invisible on the UI thread
	 * UIスレッドでウィンドウの表示をOFFにするためのクラスです
	 */
	private class HideRunner implements Runnable {
		@Override
		public void run() {
			TerminalWindow.this.frame.setVisible(false);
		}
	}

	/**
	 * A class to print a string to the text field on the window, on the UI thread
	 * UIスレッドでウィンドウに print するためのクラスです
	 */
	private class PrintRunner implements Runnable {
		String printContent = null;
		public PrintRunner(String printContent) {
			this.printContent = printContent;
		}

		@Override
		public void run() {
			TerminalWindow.this.textArea.append(this.printContent);
		}
	}

	/**
	 * A class to clear contents of the text field on the window, on the UI thread
	 * UIスレッドでウィンドウの print 内容をクリアするためのクラスです
	 */
	private class ClearRunner implements Runnable {
		@Override
		public void run() {
			TerminalWindow.this.textArea.setText("");
		}
	}

	/**
	 * A class to dispose the window on the UI thread
	 * UIスレッドでウィンドウを破棄するためのクラスです
	 */
	private class DisposeRunner implements Runnable {

		// disposesNow に true が指定されていれば、有無を言わさず今すぐに破棄する
		// false が指定されている場合は、画面が表示中ならすぐに破棄はせず、
		// ユーザーがウィンドウを閉じた時点で破棄されるように設定する

		boolean disposesNow = false;
		public DisposeRunner(boolean disposesNow) {
			this.disposesNow = disposesNow;
		}

		@Override
		public void run() {
			if (TerminalWindow.this.frame == null) {
				return;
			}

			// disposesNow に true が指定されている場合や、
			// false でも画面が非表示になっている場合は、今すぐに破棄する
			if (disposesNow || !TerminalWindow.this.frame.isVisible()) {
				TerminalWindow.this.frame.setVisible(false);
				TerminalWindow.this.frame.dispose();

			// そうでなければ、ユーザーにとって不都合にならないタイミングで破棄するように設定
			} else {
				TerminalWindow.this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}

			// どちらにしても参照はこの時点で解除しておく
			TerminalWindow.this.textArea = null;
			TerminalWindow.this.scrollPane = null;
			TerminalWindow.this.textAreaPopupMenu = null;
			TerminalWindow.this.frame = null;
		}
	}

	/**
	 * An event listener class to pop-up right-click menu
	 * 右クリックメニューを表示するためのイベントリスナークラスです
	 */
	private final class TextAreaRightClickListener implements MouseListener {
		@Override
		public final void mouseClicked(MouseEvent e) {
			if(javax.swing.SwingUtilities.isRightMouseButton(e)){
				TerminalWindow.this.textAreaPopupMenu.show(
					TerminalWindow.this.textArea, e.getX(), e.getY()
				);
			}
		}
		@Override
		public final void mousePressed(MouseEvent e) {
		}
		@Override
		public final void mouseReleased(MouseEvent e) {
		}
		@Override
		public final void mouseEntered(MouseEvent e) {
		}
		@Override
		public final void mouseExited(MouseEvent e) {
		}
	}

	/**
	 * Constructs the window
	 * ウィンドウを構築します
	 */
	public void init() {
		if (SwingUtilities.isEventDispatchThread()) {
			new InitRunner().run();
		} else {
			SwingUtilities.invokeLater(new InitRunner());
		}
	}

	/**
	 * Resets state for re-executing a script (or executing other script)
	 * スクリプトを再実行（または別のスクリプトを実行）するために、初期状態に戻します
	 */
	public void reset() {
		this.autoShowing = true;
		this.clear();
	}

	/**
	 * Makes the window visible
	 * ウィンドウを表示します
	 */
	public void show() {
		if (SwingUtilities.isEventDispatchThread()) {
			new ShowRunner().run();
		} else {
			SwingUtilities.invokeLater(new ShowRunner());
		}
	}

	/**
	 * Makes the window invisible
	 * ウィンドウを非表示にします
	 */
	public void hide() {
		this.autoShowing = false;
		if (SwingUtilities.isEventDispatchThread()) {
			new HideRunner().run();
		} else {
			SwingUtilities.invokeLater(new HideRunner());
		}
	}

	/**
	 * Prints a string to the text area on the window
	 * ウィンドウ上のテキストエリアに文字列を print します
	 * @throws ConnectorException
	 */
	public void print(String printContent) throws ConnectorException {

		// ウィンドウが表示状態かどうかを取得
		boolean isFrameVisible = false;
		if (SwingUtilities.isEventDispatchThread()) {
			isFrameVisible = this.frame.isVisible();
		} else {
			VisiblityCheckRunner visibleChecker = new VisiblityCheckRunner();
			try {
				SwingUtilities.invokeAndWait(visibleChecker);
			} catch (InvocationTargetException | InterruptedException e) {
				throw new ConnectorException(e);
			}
			isFrameVisible = visibleChecker.isVisible();
		}

		if (this.autoShowing && !isFrameVisible) {
			this.autoShowing = false;
			this.show();
		}

		if (SwingUtilities.isEventDispatchThread()) {
			new PrintRunner(printContent).run();
		} else {
			SwingUtilities.invokeLater(new PrintRunner(printContent));
		}
	}

	/**
	 * Clears contents of the text area on the window
	 * ウィンドウ上のテキストエリアの内容をクリアします
	 */
	public void clear() {
		if (SwingUtilities.isEventDispatchThread()) {
			new ClearRunner().run();
		} else {
			SwingUtilities.invokeLater(new ClearRunner());
		}
	}

	/**
	 * Disposes the window
	 * ウィンドウを破棄します
	 */
	public void dispose(boolean disposesNow) {

		// disposesNow に true が指定されていれば、有無を言わさず今すぐに破棄する
		// false が指定されている場合は、画面が表示中ならすぐに破棄はせず、
		// ユーザーがウィンドウを閉じた時点で破棄されるように設定する

		if (SwingUtilities.isEventDispatchThread()) {
			new DisposeRunner(disposesNow).run();
		} else {
			SwingUtilities.invokeLater(new DisposeRunner(disposesNow));
		}
	}
}
