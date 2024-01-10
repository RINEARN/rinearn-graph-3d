package com.rinearn.graph3d;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.renderer.RinearnGraph3DRenderer;
import com.rinearn.graph3d.renderer.simple.SimpleRenderer;
import com.rinearn.graph3d.event.RinearnGraph3DEventDispatcher;
import com.rinearn.graph3d.event.RinearnGraph3DPlottingListener;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.EnvironmentConfiguration;
import com.rinearn.graph3d.config.CameraConfiguration;

import java.awt.Image;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import java.math.BigDecimal;


/**
 * <spac lass="lang-en">
 * The class at the top layer of the implementation of RINEARN Graph 3D,
 * and also plays the role of the front-end of
 * API for controlling RINEARN Graph 3D from user's code
 * </span>
 * <span class="lang-ja">
 * リニアングラフ3Dの実装の最表層クラスであり、
 * また、コードによってリニアングラフ3Dを制御する際のAPIのフロントエンドも担います
 * </span>
 * .
 */
public class RinearnGraph3D {

	/** The front-end class of "Model" layer, which provides internal logic procedures and so on. */
	private final Model model;

	/** The front-end class of "View" layer, which provides visible part of GUI without event handling. */
	private final View view;

	/** The front-end class of "Presenter" layer, which invokes Model's procedures triggered by user's action on GUI. */
	private final Presenter presenter;

	/** The rendering engine of 3D graphs. */
	private final RinearnGraph3DRenderer renderer;


	/**
	 * The entry-point when RINEARN Graph 3D is executed as an application (not a library).
	 *
	 * @param args The command-line arguments.
	 */
	public static void main(String[] args) {
		RinearnGraph3D graph3D = new RinearnGraph3D();

		// When instantiated as a stand-alone application, not a library,
		// enable the feature to exit the entire application when the graph window is closed.
		graph3D.setAutoExitingEnabled(true);
	}


	/**
	 * <span class="lang-en">
	 * Launch a new RINEARN Graph 3D window
	 * </span>
	 * <span class="lang-ja">
	 * 新しいリニアングラフ3Dの画面を起動します
	 * </span>
	 */
	public RinearnGraph3D() {

		// Create the configuration container storing the default values for all configuration parameters.
		RinearnGraph3DConfiguration configuration = RinearnGraph3DConfiguration.createDefaultConfiguration();

		// Check that dependencies (libraries) are available.
		boolean allDependenciesAreAvailable = this.checkDependencies(configuration);
		if (!allDependenciesAreAvailable) {
			throw new IllegalStateException("Unavailable dependency has been detected.");
		}

		// !!! NOTE !!!
		//
		// ↑で config 生成して全体で参照共有しようとしたけど、良く考えたらレンダラーでも共有するのはまずい。
		// RinearnGraph3D と RinearnGraph3DRenderer はAPIを提供していて、それぞれ独立に外部から conifigure され得るが、
		// それらは渡されたconfigコンテナの中身を、自信の保持しているコンテナにマージする挙動になっている。
		// （利便性から、APIでは部分設定だけが入ったconfigコンテナを渡して反映できるようにしたいため。）
		//
		// で、それぞれの内部に保持している config コンテナの参照が同じだと、
		// 例えば RinearnGraph3DRenderer にAPI経由でマージされた部分設定が、上層の RinearnGraph3D にも反映されてしまう。
		// それは明らかに意図と異なるし、直感的な階層構造にも反する。
		//
		// つまりレンダラーはレンダラーのみで独立に config コンテナを持っているべき。
		// コンテナの参照を統一してはいけない。
		//
		// -> じゃあ数個前のコミットは結局元の方が合ってたわけなので、後で部分的に戻さないといけない。
		//    また後々で戻すべき
		//
		// -> とりあえずレンダラーとの共有はやめた。でもまだレンダラーの中のほうでリファクタが要りそう。
		//
		// !!! NOTE !!!

		// Create "Model" layer, which provides internal logic procedures and so on.
		this.model = new Model(configuration);

		// !!! NOTE !!!
		// 結局それなら ↑ も内部で生成してればよかったのでは？ ここで生成して渡さんでも。一昨日のままでよかった気が。
		//
		// -> これはまあ、「アプリのconfigはModelが保持/管理しますよ」っていう役割を考えたら、
		//    際表層で生成してコンストラクタで渡してても変ではないと思う。
		//    両者の違いは、「生成までがModelの役割に含まれるか」どうかというデザイン上の違いで。
		//
		//    例えば、仮にこの RinearnGraph3D のコンストラクタに、引数として config 渡せるようなやつがあったら、
		//    生成は Model の役割にまとめ込めなくなるし、上のような感じで投げるのが自然だし。
		//    それ考えたら上のデザインでもまあいい。
		//
		// !!! NOTE !!!

		// Create "View" layer, which provides visible part of GUI without event handling.
		this.view = new View();

		// Create a rendering engine of 3D graphs.
		this.renderer = new SimpleRenderer();

		// !!! NOTE !!!
		// ↑ここで引数で渡して参照共有してるのは確実にやめるべき。
		//   -> やめた。
		// !!! NOTE !!!


		// Creates the event dispatcher, which manages listeners of RinearnGraph3DPlottingEvent and dispatches fired events.
		// It is managed in Presenter, but instantiate here to specify this RinearnGraph3D instance as the event source.
		// And then, pass it to the argument of the Presenter's constructor.
		RinearnGraph3DEventDispatcher plottingEventDispatcher = new RinearnGraph3DEventDispatcher(this);

		// Create "Presenter" layer which invokes Model's procedures triggered by user's action on GUI.
		// (The rendering loop is also running in this Presenter layer.)
		this.presenter = new Presenter(this.model, this.view, this.renderer, plottingEventDispatcher);


		// Update some parameters in the configuration.
		Dimension screenSize = view.mainWindow.getScreenSize(); // The screen size depends on the default window size.
		configuration.getCameraConfiguration().setScreenSize((int)screenSize.getWidth(), (int)screenSize.getHeight());

		// !!! NOTE !!!
		// ↑これ、というか逆に、config 内のスクリーンサイズに合わせてデフォルトウィンドウが決まるべきか？
		//   起動後の挙動だと、config 内スクリーンサイズを呼んでリサイズするし。
		//   現状のように MainWindow に定数としてデフォルトウィンドウサイズを決めてる事がむしろだめ？
		//   そのせいで起動直後の初回レンダリングだけ、その後と非対称な特例処理になってるわけで。
		//
		// また要検討
		//
		// !!! NOTE !!!


		// Propagate the configuration stored in Model, to the entire application.
		this.presenter.propagateConfiguration();
		this.presenter.plot();

		// Show the window.
		this.view.mainWindow.setWindowVisible(true);

		// !!! TEMPORARY !!!
		//
		// Perform temporary code for development and debugging.
		this.model.temporaryExam();
		//
		// !!! TEMPORARY !!!
	}


	/**
	 * Checks that dependencies (libraries) are available.
	 *
	 * @param configuration The container storing configuration values.
	 * @return Returns true if all dependencies are available.
	 */
	private boolean checkDependencies(RinearnGraph3DConfiguration configuration) {
		boolean isJapanese = new EnvironmentConfiguration().isLocaleJapanese();

		// Check that Vnano Engine is available.
		try {
			new org.vcssl.nano.VnanoEngine();
		} catch (NoClassDefFoundError e) {
			e.printStackTrace();
			String errorMessage = isJapanese ?
					"Vnano Engine が見つかりません。\n\n「lib > app-dipendencies > vnano-engine」フォルダの中に\n「Vnano.jar」があるかどうかご確認ください。" :
					"Vnano Engine was not found.\n\nPlease check that Vnano.jar is located in \"lib > app-dependencies > vnano-engine\" folder.";
			JOptionPane.showMessageDialog(null, errorMessage, "RINEARN Graph 3D", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}


	/**
	 * <span class="lang-en">
	 * Configures detailed setting parameters, by the container storing them
	 * </span>
	 * <span class="lang-ja">
	 * 設定値を格納するコンテナを渡して、詳細な設定パラメータを設定します
	 * <span class="lang-en">
	 * .
	 * @param configuration
	 *   <span class="lang-en">
	 *   The container storing configuration values.
	 *   </span>
	 *   <span class="lang-ja">
	 *   設定値を格納しているコンテナ
	 *   </span>
	 */
	public synchronized void configure(RinearnGraph3DConfiguration configuration) {

		// RinearnGraph3DConfiguration is a container of subpart configurations.
		// Some of them are set and others are not set,
		// so extract only stored subpart configurations in the argument "configuration"
		// and merge them to the configuration of this application, which is stored in Model.
		this.model.config.merge(configuration);

		// Propagate the above modified configuration (stored in Model) to the entire application.
		this.presenter.propagateConfiguration();
	}


	/**
	 * <span class="lang-en">
	 * Disposes all the disposable resources of this instance
	 * </span>
	 * <span class="lang-ja">
	 * このインスタンス内の, 破棄可能なリソースを全て破棄します
	 * </span>
	 * .
	 * <span class="lang-en">
	 * This instance is not available anyway after calling this method.
	 * When you want to reuse it, create a new instance again.
	 * </span>
	 * <span class="lang-ja">
	 * このメソッドの呼び出し後は, このインスタンスは一切使用できなくなります.
	 * 再び使用したい場合は, 新しいインスタンスを生成してください.
	 * </span>
	 */
	public synchronized void dispose() {
		this.presenter.dispose();
	}


	/**
	 * <span class="lang-en">
	 * Enables/disables the automatic resource disposal feature performed when the graph window is closed (enabled by default)
	 * </span>
	 * <span class="lang-ja">
	 * グラフウィンドウを閉じた際に, 自動でリソースを破棄する機能の有効/無効を設定します（デフォルトで有効）
	 * </span>
	 * .
	 * @param enabled Specify true to enable, or false to disable.
	 */
	public void setAutoDisposingEnabled(boolean enabled) {
		this.presenter.frameHandler.setAutoDisposingEnabled(enabled);
	}


	/**
	 * <span class="lang-en">
	 * Enables/disables the feature to exit the entier application automatically,
	 * performed when the graph window is closed (disabled by default)
	 * </span>
	 * <span class="lang-ja">
	 * グラフウィンドウを閉じた際に,
	 * 自動でアプリケーション全体を終了させる機能の有効/無効を設定します（デフォルトで無効）
	 * </span>
	 * .
	 * @param enabled
	 *   <span class="lang-en">
	 *   Specify true to enable, false to disable
	 *   </span>
	 *   <span class="lang-ja">
	 *   有効化する場合にtrue, 無効化する場合に false を指定
	 *   </span>
	 */
	public synchronized void setAutoExitingEnabled(boolean enabled) {
		this.presenter.frameHandler.setAutoExitingEnabled(enabled);
	}

	/**
	 * <span class="lang-en">
	 * This method name contains a mis-spelling, so use setAutoExitingEnabled(boolean enabled) instead
	 * </span>
	 * <span class="lang-ja">
	 * このメソッド名にはスペルミスが含まれているため, 代わりに setAutoExitingEnabled(boolean enabled) を使用してください
	 * </span>
	 * .
	 * <span class="lang-en">
	 * This method is supported only for keeping compatibility of old code depending on this method.
	 * </span>
	 * <span class="lang-ja">
	 * このメソッドは, このメソッドに依存している古いコードの互換性を保つためだけにサポートされています.
	 * </span>
	 *
	 * @param enabled
	 *   <span class="lang-en">
	 *   Same as setAutoExitingEnabled(boolean enabled)
	 *   </span>
	 *   <span class="lang-ja">
	 *   setAutoExitingEnabled(boolean enabled) と同様
	 *   </span>
	 */
	public void setAutoExittingEnabled(boolean enabled) {
		this.setAutoExitingEnabled(enabled);
	}


	/**
	 * <span class="lang-en">
	 * Gets an Image instance storing the current screen image
	 * </span>
	 * <span class="lang-ja">
	 * 現在のスクリーンの内容を保持している Image インスタンスを取得します
	 * </span>
	 * .
	 *
	 * <span class="lang-en">
	 * This method copies the current screen image to a buffer, and returns the reference to the buffer.
	 * Hence, the content of the returned Image instance is NOT updated automatically when the screen is re-rendered.
	 * To update the content of the image, re-call this method.
	 * </span>
	 * <span class="lang-ja">
	 * このメソッドは、現在のスクリーンの内容をバッファにコピーし, そのバッファの参照を返します.
	 * 従って, 返される Image インスタンスの内容は, スクリーンが再描画されても, 自動で更新はされません.
	 * 内容を更新するには, このメソッドを再度使用してください.
	 * </span>
	 *
	 * <span class="lang-en">
	 * Please note that, the returned Image instance changes when the screen is resized,
	 * because it requires to reallocate the buffer.
	 * </span>
	 * <span class="lang-ja">
	 * なお, スクリーンサイズが変わった際には, バッファ領域が再確保されるため,
	 * このメソッドが返す Image インスタンスも変わる事に注意が必要です.
	 * </span>
	 *
	 * @return
	 *   <span class="lang-en">
	 *   The Image instance storing the current screen image
	 *   </span>
	 *   <span class="lang-ja">
	 *   現在のスクリーンの内容を保持している Image インスタンス
	 *   </span>
	 */
	public synchronized Image getImage() {
		return this.presenter.renderingLoop.getImage();
	}


	/**
	 * <span class="lang-en">
	 * Exports the current screen image to a image file
	 * </span>
	 * <span class="lang-ja">
	 * 現在のスクリーンの内容を, 画像ファイルとして保存します
	 * </span>
	 * .
	 * @param file
	 *   <span class="lang-en">
	 *   The file to be written
	 *   </span>
	 *   <span class="lang-ja">
	 *   保存するファイル
	 *   </span>
	 *
	 * @param quality
	 *   <span class="lang-en">
	 *   The quality of the image file (from 0.0 to 1.0, or from 1.0 to 100.0)
	 *   </span>
	 *   <span class="lang-ja">
	 *   画像ファイルの品質 (0.0 から 1.0, または 1.0から 100.0 の範囲で指定します)
	 *   </span>
	 *
	 * @throws IOException
	 *   <span class="lang-en">
	 *   Thrown if any error occurred for writing the image file
	 *   </span>
	 *   <span class="lang-ja">
	 *   画像ファイルの出力処理で何らかのエラーが発生した際にスローされます
	 *   </span>
	 */
	public synchronized void exportImageFile(File file, double quality) throws IOException {
		this.presenter.renderingLoop.exportImageFile(file, quality);
	}


	/**
	 * <span class="lang-en">
	 * Clears all the currently plotted data and math expressions
	 * </span>
	 * <span class="lang-ja">
	 * 現在プロットされているデータや数式を、全てクリアします
	 * </span>
	 * .
	 */
	public synchronized void clear() {
		this.presenter.menuHandler.clear();
	}


	/**
	 * <span class="lang-en">
	 * Sets the data composing a line to be plotted
	 * </span>
	 * <span class="lang-ja">
	 * プロット対象として, 線状のデータをセットします
	 * </span>
	 * .
	 * <span class="lang-en">
	 * Please note that, the currently registered data series are cleared.
	 * If you don't want to clear them, use appendData(...) instead.
	 * </span>
	 * <span class="lang-ja">
	 * なお, 現時点で登録されているデータ系列は, 全てクリアされる事にご注意ください.
	 * クリアしたくない場合は, 代わりに appendData(...) を使用してください.
	 * </span>
	 *
	 * @param x
	 *   <span class="lang-en">
	 *   The array storing the X-coordinates of the node points of a line,
	 *   where its index is [nodeIndex]
	 *   </span>
	 *   <span class="lang-ja">
	 *   線の節点における, X値を格納する配列
	 *   （インデックスは [節点のインデックス]）
	 *   </span>
	 * @param y
	 *   <span class="lang-en">
	 *   The array storing the Y-coordinates of the node points of the line,
	 *   where its index is [nodeIndex]
	 *   </span>
	 *   <span class="lang-ja">
	 *   線の節点における, Y値を格納する配列
	 *   （インデックスは [節点のインデックス]）
	 *   </span>
	 * @param z
	 *   <span class="lang-en">
	 *   The array storing the Z-coordinates of the node points of the line,
	 *   where its index is [nodeIndex]
	 *   </span>
	 *   <span class="lang-ja">
	 *   線の節点における, Z値を格納する配列
	 *   （インデックスは [節点のインデックス]）
	 *   </span>
	 */
	public synchronized void setData(double[] x, double[] y, double[] z) {
		this.presenter.dataArrayHandler.setData(x, y, z);
	}


	/**
	 * <span class="lang-en">
	 * Sets the data composing a mesh to be plotted
	 * </span>
	 * <span class="lang-ja">
	 * プロット対象として, メッシュ状のデータをセットします
	 * </span>
	 * .
	 * <span class="lang-en">
	 * Please note that, the currently registered data series are cleared.
	 * If you don't want to clear them, use appendData(...) instead.
	 * </span>
	 * <span class="lang-ja">
	 * なお, 現時点で登録されているデータ系列は, 全てクリアされる事にご注意ください.
	 * クリアしたくない場合は, 代わりに appendData(...) を使用してください.
	 * </span>
	 *
	 * @param x
	 *   <span class="lang-en">
	 *   The array storing the X-coordinates of the grid points of the mesh to be plotted,
	 *   where its indices are [gridIndexA][gridIndexB]
	 *   </span>
	 *   <span class="lang-ja">
	 *   メッシュ格子点における, X値を格納する配列
	 *   （インデックスは [格子方向Aのインデックス][格子方向Bのインデックス]）
	 *   </span>
	 * @param y
	 *   <span class="lang-en">
	 *   The array storing the Y-coordinates of the grid points of the mesh to be plotted,
	 *   where its indices are [gridIndexA][gridIndexB]
	 *   </span>
	 *   <span class="lang-ja">
	 *   メッシュ格子点における, Y値を格納する配列
	 *   （インデックスは [格子方向Aのインデックス][格子方向Bのインデックス]）
	 *   </span>
	 * @param z
	 *   <span class="lang-en">
	 *   The array storing the Z-coordinates of the grid points of the mesh to be plotted,
	 *   where its indices are [gridIndexA][gridIndexB]
	 *   </span>
	 *   <span class="lang-ja">
	 *   メッシュ格子点における, Z値を格納する配列
	 *   （インデックスは [格子方向Aのインデックス][格子方向Bのインデックス]）
	 *   </span>
	 */
	public synchronized void setData(double[][] x, double[][] y, double[][] z) {
		this.presenter.dataArrayHandler.setData(x, y, z);
	}


	/**
	 * <span class="lang-en">
	 * Sets the multiple data series (composing multiple meshes or lines) to be plotted
	 * </span>
	 * <span class="lang-ja">
	 * プロット対象として, 複数系列のデータ（複数のメッシュや線を構成）をセットします
	 * </span>
	 * .
	 * <span class="lang-en">
	 * Please note that, the currently registered data series are cleared.
	 * If you don't want to clear them, use appendData(...) instead.
	 * </span>
	 * <span class="lang-ja">
	 * なお, 現時点で登録されているデータ系列は, 全てクリアされる事にご注意ください.
	 * クリアしたくない場合は, 代わりに appendData(...) を使用してください.
	 * </span>
	 *
	 * @param x
	 *   <span class="lang-en">
	 *   The array storing the X-coordinates of the grid/node points of the multiple data series to be plotted,
	 *   where its indices are [dataSeriesIndex][gridIndexA][gridIndexB]
	 *   </span>
	 *   <span class="lang-ja">
	 *   各系列の、メッシュ格子点や線の節点における, X値を格納する配列
	 *   （インデックスは [系列インデックス][格子方向Aのインデックス][格子方向Bのインデックス]）
	 *   </span>
	 * @param y
	 *   <span class="lang-en">
	 *   The array storing the Y-coordinates of the grid/node points of the multiple data series to be plotted,
	 *   where its indices are [dataSeriesIndex][gridIndexA][gridIndexB]
	 *   </span>
	 *   <span class="lang-ja">
	 *   各系列の、メッシュ格子点や線の節点における, Y値を格納する配列
	 *   （インデックスは [系列インデックス][格子方向Aのインデックス][格子方向Bのインデックス]）
	 *   </span>
	 * @param z
	 *   <span class="lang-en">
	 *   The array storing the Z-coordinates of the grid/node points of the multiple data series to be plotted,
	 *   where its indices are [dataSeriesIndex][gridIndexA][gridIndexB]
	 *   </span>
	 *   <span class="lang-ja">
	 *   各系列の、メッシュ格子点や線の節点における, Z値を格納する配列
	 *   （インデックスは [系列インデックス][格子方向Aのインデックス][格子方向Bのインデックス]）
	 *   </span>
	 */
	public synchronized void setData(double[][][] x, double[][][] y, double[][][] z) {
		this.presenter.dataArrayHandler.setData(x, y, z);
	}


	/**
	 * <span class="lang-en">
	 * Appends the data composing a line, to the currently plotted data
	 * </span>
	 * <span class="lang-ja">
	 * 現在プロットされている内容に, 線状のデータを追加します
	 * </span>
	 * .
	 * @param x
	 *   <span class="lang-en">
	 *   The array storing the X-coordinates of the node points of a line,
	 *   where its index is [nodeIndex]
	 *   </span>
	 *   <span class="lang-ja">
	 *   線の節点における, X値を格納する配列
	 *   （インデックスは [節点のインデックス]）
	 *   </span>
	 * @param y
	 *   <span class="lang-en">
	 *   The array storing the Y-coordinates of the node points of the line,
	 *   where its index is [nodeIndex]
	 *   </span>
	 *   <span class="lang-ja">
	 *   線の節点における, Y値を格納する配列
	 *   （インデックスは [節点のインデックス]）
	 *   </span>
	 * @param z
	 *   <span class="lang-en">
	 *   The array storing the Z-coordinates of the node points of the line,
	 *   where its index is [nodeIndex]
	 *   </span>
	 *   <span class="lang-ja">
	 *   線の節点における, Z値を格納する配列
	 *   （インデックスは [節点のインデックス]）
	 *   </span>
	 */
	public synchronized void appendData(double[] x, double[] y, double[] z) {
		this.presenter.dataArrayHandler.appendData(x, y, z);
	}


	/**
	 * <span class="lang-en">
	 * Appends the data composing a mesh, to the currently plotted data
	 * </span>
	 * <span class="lang-ja">
	 * 現在プロットされている内容に、メッシュ状のデータを追加します
	 * </span>
	 * .
	 * @param x
	 *   <span class="lang-en">
	 *   The array storing the X-coordinates of the grid points of the mesh to be plotted,
	 *   where its indices are [gridIndexA][gridIndexB]
	 *   </span>
	 *   <span class="lang-ja">
	 *   メッシュ格子点における, X値を格納する配列
	 *   （インデックスは [格子方向Aのインデックス][格子方向Bのインデックス]）
	 *   </span>
	 * @param y
	 *   <span class="lang-en">
	 *   The array storing the Y-coordinates of the grid points of the mesh to be plotted,
	 *   where its indices are [gridIndexA][gridIndexB]
	 *   </span>
	 *   <span class="lang-ja">
	 *   メッシュ格子点における, Y値を格納する配列
	 *   （インデックスは [格子方向Aのインデックス][格子方向Bのインデックス]）
	 *   </span>
	 * @param z
	 *   <span class="lang-en">
	 *   The array storing the Z-coordinates of the grid points of the mesh to be plotted,
	 *   where its indices are [gridIndexA][gridIndexB]
	 *   </span>
	 *   <span class="lang-ja">
	 *   メッシュ格子点における, Z値を格納する配列
	 *   （インデックスは [格子方向Aのインデックス][格子方向Bのインデックス]）
	 *   </span>
	 */
	public synchronized void appendData(double[][] x, double[][] y, double[][] z) {
		this.presenter.dataArrayHandler.appendData(x, y, z);
	}


	/**
	 * <span class="lang-en">
	 * Appends the multiple data series (composing multiple meshes or lines), to the currently plotted data
	 * </span>
	 * <span class="lang-ja">
	 * 現在プロットされている内容に、複数系列のデータ（複数のメッシュや線を構成）を追加します
	 * </span>
	 * .
	 * @param x
	 *   <span class="lang-en">
	 *   The array storing the X-coordinates of the grid/node points of the multiple data series to be plotted,
	 *   where its indices are [dataSeriesIndex][gridIndexA][gridIndexB]
	 *   </span>
	 *   <span class="lang-ja">
	 *   各系列の、メッシュ格子点や線の節点における, X値を格納する配列
	 *   （インデックスは [系列インデックス][格子方向Aのインデックス][格子方向Bのインデックス]）
	 *   </span>
	 * @param y
	 *   <span class="lang-en">
	 *   The array storing the Y-coordinates of the grid/node points of the multiple data series to be plotted,
	 *   where its indices are [dataSeriesIndex][gridIndexA][gridIndexB]
	 *   </span>
	 *   <span class="lang-ja">
	 *   各系列の、メッシュ格子点や線の節点における, Y値を格納する配列
	 *   （インデックスは [系列インデックス][格子方向Aのインデックス][格子方向Bのインデックス]）
	 *   </span>
	 * @param z
	 *   <span class="lang-en">
	 *   The array storing the Z-coordinates of the grid/node points of the multiple data series to be plotted,
	 *   where its indices are [dataSeriesIndex][gridIndexA][gridIndexB]
	 *   </span>
	 *   <span class="lang-ja">
	 *   各系列の、メッシュ格子点や線の節点における, Z値を格納する配列
	 *   （インデックスは [系列インデックス][格子方向Aのインデックス][格子方向Bのインデックス]）
	 *   </span>
	 */
	public synchronized void appendData(double[][][] x, double[][][] y, double[][][] z) {
		this.presenter.dataArrayHandler.appendData(x, y, z);
	}


	/**
	 * <span class="lang-en">
	 * Enables/disables the asynchronous plotting feature
	 * </span>
	 * <span class="lang-ja">
	 * 非同期プロット機能の有効・無効を設定します
	 * </span>
	 * .
	 * <span class="lang-en">
	 * If this feature is disabled (default),
	 * when you plot the data by calling setData(double[], double[], double[]) method,
	 * the processing flow returns to the called-side after when the plotting has competed.
	 * In the contrast, if this feature is enabled, the processing flow returns from
	 * setData(double[], double[], double[]) method to the caller-side immediately with storing the data to the buffer.
	 * Then, the plotting will be performed asynchronously on the other thread, at a suitable timing.
	 * This feature is useful when you want to animate the graph by calling setData method repeatedly in high frequency.
	 * In such case, with enabling this feature, you can reduce the load of the main thread, and also can make the animation smooth.
	 * </span>
	 * <span class="lang-ja">
	 * この機能の有効・無効は, setData(double[], double[], double[]) メソッドなどを連続的に呼び出し続けて,
	 * 大量の座標値データをグラフにプロットさせ続ける場合の振る舞いに影響します.
	 * この機能が無効の場合は, 座標値データを渡した際, それがグラフにプロットされた後に, 呼び出し元に処理が戻ります.
	 * 渡したデータが確実にプロットされるため, プロット結果を画像ファイルに出力するような場合に適しています.
	 * それに対して, この機能が有効の場合は, 座標値データは一時的なバッファ領域に控えられるだけで, すぐに呼び出し元に処理が戻ります.
	 * そして, バッファされたデータは, 後の適当なタイミングでグラフにプロットされます.
	 * そのため, データを繰り返し高頻度で渡して, グラフをアニメーションさせてたいような場合には, この機能を有効化するのが適しています.
	 * そうする事で, メインスレッドの負荷を低減させると共に, アニメーションをスムーズにする事ができます.
	 * </span>
	 *
	 * @param enabled
	 *   <span class="lang-en">
	 *   Specify true to enable, false to disable
	 *   </span>
	 *   <span class="lang-ja">
	 *   有効化する場合に true, 無効化する場合に false を指定
	 *   </span>
	 */
	public synchronized void setAsynchronousPlottingEnabled(boolean enabled) {
		this.presenter.dataArrayHandler.setAsynchronousPlottingEnabled(enabled);
	}


	/**
	 * <span class="lang-en">
	 * Returns the 3D renderer, which is being used for rendering the graph image in this instance
	 * </span>
	 * <span class="lang-ja">
	 * グラフ画面を描画するのに使用されている3D描画エンジン（レンダラー）を返します
	 * </span>
	 * .
	 * @return
	 *   <span class="lang-en">The 3D renderer</span>
	 *   <span class="lang-ja">3D描画エンジン（レンダラー）</span>
	 */
	public synchronized RinearnGraph3DRenderer getRenderer() {
		return this.renderer;
	}


	/**
	 * <span class="lang-ja">
	 * グラフ表示ウィンドウの位置とサイズを設定します
	 * </span>
	 * <span class="lang-en">
	 * Sets the location and the size of the graph window
	 * </span>
	 * .
	 * <span class="lang-ja">
	 * 変更後のウィンドウのサイズに合わせて, グラフスクリーンのサイズも変更されます.
	 * なお, グラフスクリーンのサイズは, ウィンドウのサイズよりも若干小さくなり,
	 * その差はバージョンや環境に依存する事に注意してください.
	 * 画像出力用途などで, グラフスクリーンのサイズを明示的に指定したい場合は, 代わりに
	 * {@link RinearnGraph3D#setScreenSize setScreenSize} メソッドを使用してください.
	 * </span>
	 *
	 * <span class="lang-en">
	 * Note that, the graph screen will be resized automatically,
	 * corresponding with the size of the modified graph window.
	 * The size of the graph screen will be little smaller than the size of the graph window,
	 * and the difference between them depends on your environment and the version of RINEARN Graph 3D.
	 * When you want to set the size of the graph screen precisely,
	 * use {@link RinearnGraph3D#setScreenSize setScreenSize} method instead.
	 * </span>
	 *
	 * @param x
	 *   <span class="lang-ja">グラフウィンドウ左上のX座標</span>
	 *   <span class="lang-en">The X-coordinate of the left-top edge of the graph window</span>
	 * @param y
	 *   <span class="lang-ja">グラフウィンドウ左上のY座標</span>
	 *   <span class="lang-en">The Y-coordinate of the left-top edge of the graph window</span>
	 * @param width
	 *   <span class="lang-ja">グラフウィンドウの幅</span>
	 *   <span class="lang-en">The width the graph window</span>
	 * @param height
	 *   <span class="lang-ja">グラフウィンドウの高さ</span>
	 *   <span class="lang-en">The height the graph window</span>
	 */
	public synchronized void setWindowBounds(int x, int y, int width, int height) {
		this.presenter.frameHandler.setWindowBounds(x, y, width, height);
		this.presenter.screenHandler.updateScreenSize();
	}


	/**
	 * <span class="lang-ja">
	 * グラフスクリーンのサイズを設定します
	 * </span>
	 * <span class="lang-en">
	 * Sets the size of the graph screen
	 * </span>
	 * .
	 * <span class="lang-ja">
	 * 変更後のスクリーンサイズに合わせて, グラフウィンドウのサイズも自動で変更されます.
	 * グラフウィンドウのサイズを明示的に指定したい場合は, 代わりに
	 * {@link RinearnGraph3D#setWindowBounds setWindowBounds} メソッドを使用してください.
	 * </span>
	 *
	 * <span class="lang-en">
	 * Note that, the graph window will be resized automatically,
	 * corresponding with the size of the modified graph screen.
	 * When you want to set the size of the graph window explicitly,
	 * use {@link RinearnGraph3D#setWindowBounds setWindowBounds} method instead.
	 * </span>
	 *
	 * @param width
	 *   <span class="lang-ja">グラフスクリーンの幅</span>
	 *   <span class="lang-en">The width the graph screen</span>
	 * @param height
	 *   <span class="lang-ja">グラフスクリーンの高さ</span>
	 *   <span class="lang-en">The height the graph screen</span>
	 */
	public synchronized void setScreenSize (int width, int height) {
		this.presenter.frameHandler.setScreenSize(width, height);
		this.presenter.screenHandler.setScreenSize(width, height);
	}


	/**
	 * Sets the range of X axis.
	 *
	 * @param min The minimum coordinate value of X axis.
	 * @param max The maximum coordinate value of X axis.
	 */
	public synchronized void setXRange(double min, double max) {
		this.presenter.rangeSettingHandler.setXRange(new BigDecimal(min), new BigDecimal(max));
	}

	/**
	 * Sets the range of X axis.
	 *
	 * @param min The minimum coordinate value of X axis.
	 * @param max The maximum coordinate value of X axis.
	 */
	public synchronized void setXRange(BigDecimal min, BigDecimal max) {
		this.presenter.rangeSettingHandler.setXRange(min, max);
	}

	/**
	 * Turns on/off the auto-ranging feature for X axis.
	 *
	 * @param enabled Specify true/false for turning on/off (the default is on).
	 */
	public synchronized void setXAutoRangingEnabled(boolean enabled) {
		this.presenter.rangeSettingHandler.setXAutoRangingEnabled(enabled);
	}


	/**
	 * Sets the range of Y axis.
	 *
	 * @param min The minimum coordinate value of Y axis.
	 * @param max The maximum coordinate value of Y axis.
	 */
	public synchronized void setYRange(double min, double max) {
		this.presenter.rangeSettingHandler.setYRange(new BigDecimal(min), new BigDecimal(max));
	}

	/**
	 * Sets the range of Y axis.
	 *
	 * @param min The minimum coordinate value of Y axis.
	 * @param max The maximum coordinate value of Y axis.
	 */
	public synchronized void setYRange(BigDecimal min, BigDecimal max) {
		this.presenter.rangeSettingHandler.setYRange(min, max);
	}

	/**
	 * Turns on/off the auto-ranging feature for Y axis.
	 *
	 * @param enabled Specify true/false for turning on/off (the default is on).
	 */
	public synchronized void setYAutoRangingEnabled(boolean enabled) {
		this.presenter.rangeSettingHandler.setYAutoRangingEnabled(enabled);
	}


	/**
	 * Sets the range of Z axis.
	 *
	 * @param min The minimum coordinate value of Z axis.
	 * @param max The maximum coordinate value of Z axis.
	 */
	public synchronized void setZRange(double min, double max) {
		this.presenter.rangeSettingHandler.setZRange(new BigDecimal(min), new BigDecimal(max));
	}

	/**
	 * Sets the range of Z axis.
	 *
	 * @param min The minimum coordinate value of Z axis.
	 * @param max The maximum coordinate value of Z axis.
	 */
	public synchronized void setZRange(BigDecimal min, BigDecimal max) {
		this.presenter.rangeSettingHandler.setZRange(min, max);
	}

	/**
	 * Turns on/off the auto-ranging feature for Z axis.
	 *
	 * @param enabled Specify true/false for turning on/off (the default is on).
	 */
	public synchronized void setZAutoRangingEnabled(boolean enabled) {
		this.presenter.rangeSettingHandler.setZAutoRangingEnabled(enabled);
	}


	/**
	 * Set the displayed text of X axis label.
	 *
	 * @param xLabel The text of X axis label.
	 */
	public synchronized void setXLabel(String xLabel) {
		this.presenter.labelSettingHandler.setXLabel(xLabel);
	}

	/**
	 * Set the displayed text of Y axis label.
	 *
	 * @param yLabel The text of Y axis label.
	 */
	public synchronized void setYLabel(String yLabel) {
		this.presenter.labelSettingHandler.setYLabel(yLabel);
	}

	/**
	 * Set the displayed text of Z axis label.
	 *
	 * @param zLabel The text of Z axis label.
	 */
	public synchronized void setZLabel(String zLabel) {
		this.presenter.labelSettingHandler.setZLabel(zLabel);
	}


 	/**
 	 * Sets the distance between the viewpoint and the origin of the graph.
 	 *
 	 * @param distance The distance between the viewpoint and the origin of the graph.
 	 */
	public void setCameraDistance(double distance) {
		this.presenter.cameraSettingHandler.setCameraDistance(distance);
	}

	/**
	 * Sets the magnification of the graph screen.
	 *
	 * @param magnification The magnification of the graph screen.
	 */
	public void setCameraMagnification(double magnification) {
		this.presenter.cameraSettingHandler.setCameraMagnification(magnification);
	}


	/**
	 * Sets the camera angle by two angular parameters, regarding X axis as the zenith axis.
	 *
	 * The "screw angle" is always set to zero by this method.
	 * To set the screw angle together, use the overload of this method, having 3-arguments instead.
	 *
	 * @param horizontalAngle The horizontal angle, which is the rotation angle of the camera's location around the zenith axis.
	 * @param verticalAngle The vertical angle, which is the angle between the zenith axis and the direction toward the camera.
	 */
	public synchronized void setXZenithCameraAngle(double horizontalAngle, double verticalAngle) {
		this.presenter.cameraSettingHandler.setZenithCameraAngle(
				horizontalAngle, verticalAngle, 0.0, CameraConfiguration.AngleMode.X_ZENITH
		);
	}

	/**
	 * Sets the camera angle by three angular parameters, regarding X axis as the zenith axis.
	 *
	 * @param horizontalAngle The horizontal angle, which is the rotation angle of the camera's location around the zenith axis.
	 * @param verticalAngle The vertical angle, which is the angle between the zenith axis and the direction toward the camera.
	 * @param screwAngle The screw angle, which is the rotation angle of the camera itself (not location) around the screen center.
	 */
	public synchronized void setXZenithCameraAngle(double horizontalAngle, double verticalAngle, double screwAngle) {
		this.presenter.cameraSettingHandler.setZenithCameraAngle(
				horizontalAngle, verticalAngle, screwAngle, CameraConfiguration.AngleMode.X_ZENITH
		);
	}


	/**
	 * Sets the camera angle by two angular parameters, regarding Y axis as the zenith axis.
	 *
	 * The "screw angle" is always set to zero by this method.
	 * To set the screw angle together, use the overload of this method, having 3-arguments instead.
	 *
	 * @param horizontalAngle The horizontal angle, which is the rotation angle of the camera's location around the zenith axis.
	 * @param verticalAngle The vertical angle, which is the angle between the zenith axis and the direction toward the camera.
	 */
	public synchronized void setYZenithCameraAngle(double horizontalAngle, double verticalAngle) {
		this.presenter.cameraSettingHandler.setZenithCameraAngle(
				horizontalAngle, verticalAngle, 0.0, CameraConfiguration.AngleMode.Y_ZENITH
		);
	}

	/**
	 * Sets the camera angle by three angular parameters, regarding Y axis as the zenith axis.
	 *
	 * @param horizontalAngle The horizontal angle, which is the rotation angle of the camera's location around the zenith axis.
	 * @param verticalAngle The vertical angle, which is the angle between the zenith axis and the direction toward the camera.
	 * @param screwAngle The screw angle, which is the rotation angle of the camera itself (not location) around the screen center.
	 */
	public synchronized void setYZenithCameraAngle(double horizontalAngle, double verticalAngle, double screwAngle) {
		this.presenter.cameraSettingHandler.setZenithCameraAngle(
				horizontalAngle, verticalAngle, screwAngle, CameraConfiguration.AngleMode.Y_ZENITH
		);
	}


	/**
	 * Sets the camera angle by two angular parameters, regarding Z axis as the zenith axis.
	 *
	 * The "screw angle" is always set to zero by this method.
	 * To set the screw angle together, use the overload of this method, having 3-arguments instead.
	 *
	 * @param horizontalAngle The horizontal angle, which is the rotation angle of the camera's location around the zenith axis.
	 * @param verticalAngle The vertical angle, which is the angle between the zenith axis and the direction toward the camera.
	 */
	public synchronized void setZZenithCameraAngle(double horizontalAngle, double verticalAngle) {
		this.presenter.cameraSettingHandler.setZenithCameraAngle(
				horizontalAngle, verticalAngle, 0.0, CameraConfiguration.AngleMode.Z_ZENITH
		);
	}

	/**
	 * Sets the camera angle by three angular parameters, regarding Z axis as the zenith axis.
	 *
	 * @param horizontalAngle The horizontal angle, which is the rotation angle of the camera's location around the zenith axis.
	 * @param verticalAngle The vertical angle, which is the angle between the zenith axis and the direction toward the camera.
	 * @param screwAngle The screw angle, which is the rotation angle of the camera itself (not location) around the screen center.
	 */
	public synchronized void setZZenithCameraAngle(double horizontalAngle, double verticalAngle, double screwAngle) {
		this.presenter.cameraSettingHandler.setZenithCameraAngle(
				horizontalAngle, verticalAngle, screwAngle, CameraConfiguration.AngleMode.Z_ZENITH
		);
	}


	/**
	 * Sets the coordinates (locations) and the labels (displayed text) of the scale ticks on X axis.
	 *
	 * @param tickCoordinates The coordinates of the scale ticks.
	 * @param tickLabels The labels of the scale ticks.
	 */
	public synchronized void setXTicks(BigDecimal[] tickCoordinates, String[] tickLabels) {
		this.presenter.scaleSettingHandler.setXTicks(tickCoordinates, tickLabels);
	}

	/**
	 * Sets the coordinates (locations) and the labels (displayed text) of the scale ticks on X axis.
	 *
	 * @param tickCoordinates The coordinates of the scale ticks.
	 * @param tickLabels The labels of the scale ticks.
	 */
	public synchronized void setXTicks(double[] tickCoordinates, String[] tickLabels) {
		BigDecimal[] bdTickCoordinates = new BigDecimal[tickCoordinates.length];
		for (int itick=0; itick<tickCoordinates.length; itick++) {
			bdTickCoordinates[itick] = new BigDecimal(tickCoordinates[itick]);
		}
		this.presenter.scaleSettingHandler.setXTicks(bdTickCoordinates, tickLabels);
	}


	/**
	 * Sets the coordinates (locations) and the labels (displayed text) of the scale ticks on Y axis.
	 *
	 * @param tickCoordinates The coordinates of the scale ticks.
	 * @param tickLabels The labels of the scale ticks.
	 */
	public synchronized void setYTicks(BigDecimal[] tickCoordinates, String[] tickLabels) {
		this.presenter.scaleSettingHandler.setYTicks(tickCoordinates, tickLabels);
	}

	/**
	 * Sets the coordinates (locations) and the labels (displayed text) of the scale ticks on Y axis.
	 *
	 * @param tickCoordinates The coordinates of the scale ticks.
	 * @param tickLabels The labels of the scale ticks.
	 */
	public synchronized void setYTicks(double[] tickCoordinates, String[] tickLabels) {
		BigDecimal[] bdTickCoordinates = new BigDecimal[tickCoordinates.length];
		for (int itick=0; itick<tickCoordinates.length; itick++) {
			bdTickCoordinates[itick] = new BigDecimal(tickCoordinates[itick]);
		}
		this.presenter.scaleSettingHandler.setYTicks(bdTickCoordinates, tickLabels);
	}


	/**
	 * Sets the coordinates (locations) and the labels (displayed text) of the scale ticks on Z axis.
	 *
	 * @param tickCoordinates The coordinates of the scale ticks.
	 * @param tickLabels The labels of the scale ticks.
	 */
	public synchronized void setZTicks(BigDecimal[] tickCoordinates, String[] tickLabels) {
		this.presenter.scaleSettingHandler.setZTicks(tickCoordinates, tickLabels);
	}

	/**
	 * Sets the coordinates (locations) and the labels (displayed text) of the scale ticks on Z axis.
	 *
	 * @param tickCoordinates The coordinates of the scale ticks.
	 * @param tickLabels The labels of the scale ticks.
	 */
	public synchronized void setZTicks(double[] tickCoordinates, String[] tickLabels) {
		BigDecimal[] bdTickCoordinates = new BigDecimal[tickCoordinates.length];
		for (int itick=0; itick<tickCoordinates.length; itick++) {
			bdTickCoordinates[itick] = new BigDecimal(tickCoordinates[itick]);
		}
		this.presenter.scaleSettingHandler.setZTicks(bdTickCoordinates, tickLabels);
	}


	/**
	 * Sets the visibility of the menu bar and the right click menus.
	 *
	 * @param visible Specify true for showing the menu bar and the right click menus.
	 */
	public synchronized void setMenuVisible(boolean visible) {
		presenter.menuHandler.setMenuVisible(visible);
	}


	/**
	 * Sets the visibility of the UI-panel at the left side of the screen.
	 *
	 * @param visible Specify true for showing the UI-panel at the left side of the screen.
	 */
	public synchronized void setScreenSideUIVisible(boolean visible) {
		presenter.screenSideUIHandler.setScreenSideUIVisible(visible);
	}


	/**
	 * <span class="lang-en">
	 * Enables/disables the default event listener handling key events on the graph window
	 * </span>
	 * <span class="lang-ja">
	 * グラフウィンドウ上でのキーイベントを処理する, デフォルトのイベントリスナーの有効/無効を切り替えます
	 * </span>
	 *
	 * @param enabled
	 *   <span class="lang-en">Specify true to enable, or false to disable</span>
	 *   <span class="lang-ja">有効化する場合は true, 無効化する場合は false を指定</span>
	 */
	public synchronized void setDefaultKeyListenerEnabled(boolean enabled) {
		this.presenter.screenHandler.setDefaultKeyListenerEnabled(enabled);
	}


	/**
	 * <span class="lang-en">
	 * Enables/disables the default event listener handling mouse events on the graph screen
	 * </span>
	 * <span class="lang-ja">
	 * グラフスクリーン上でのマウスイベントを処理する, デフォルトのイベントリスナーの有効/無効を切り替えます
	 * </span>
	 *
	 * @param enabled
	 *   <span class="lang-en">Specify true to enable, or false to disable</span>
	 *   <span class="lang-ja">有効化する場合は true, 無効化する場合は false を指定</span>
	 */
	public synchronized void setDefaultMouseListenerEnabled(boolean enabled) {
		this.presenter.screenHandler.setDefaultMouseListenerEnabled(enabled);
	}


	/**
	 * <span class="lang-en">
	 * Enables/disables the default event listener handling mouse-motion events on the graph screen
	 * </span>
	 * <span class="lang-ja">
	 * グラフスクリーン上でのマウスモーションイベントを処理する, デフォルトのイベントリスナーの有効/無効を切り替えます
	 * </span>
	 *
	 * @param enabled
	 *   <span class="lang-en">Specify true to enable, or false to disable</span>
	 *   <span class="lang-ja">有効化する場合は true, 無効化する場合は false を指定</span>
	 */
	public synchronized void setDefaultMouseMotionListenerEnabled(boolean enabled) {
		this.presenter.screenHandler.setDefaultMouseMotionListenerEnabled(enabled);
	}


	/**
	 * <span class="lang-en">
	 * Enables/disables the default event listener handling mouse-wheel events on the graph screen
	 * </span>
	 * <span class="lang-ja">
	 * グラフスクリーン上でのマウスホイールイベントを処理する, デフォルトのイベントリスナーの有効/無効を切り替えます
	 * </span>
	 *
	 * @param enabled
	 *   <span class="lang-en">Specify true to enable, or false to disable</span>
	 *   <span class="lang-ja">有効化する場合は true, 無効化する場合は false を指定</span>
	 */
	public synchronized void setDefaultMouseWheelListenerEnabled(boolean enabled) {
		this.presenter.screenHandler.setDefaultMouseWheelListenerEnabled(enabled);
	}


	/**
	 * <span class="lang-en">
	 * Adds the event listener for handling key events on the graph window
	 * </span>
	 * <span class="lang-ja">
	 * グラフウィンドウ上でのキーイベントを処理する, イベントリスナーを追加します
	 * </span>
	 *
	 * @param enabled
	 *   <span class="lang-en">The event listener to be added</span>
	 *   <span class="lang-ja">追加するイベントリスナー</span>
	 */
	public synchronized void addKeyListener(KeyListener listener) {
		this.presenter.screenHandler.addKeyListener(listener);
	}


	/**
	 * <span class="lang-en">
	 * Adds the event listener for handling mouse events on the graph screen
	 * </span>
	 * <span class="lang-ja">
	 * グラフスクリーン上でのマウスイベントを処理する, イベントリスナーを追加します
	 * </span>
	 *
	 * @param enabled
	 *   <span class="lang-en">The event listener to be added</span>
	 *   <span class="lang-ja">追加するイベントリスナー</span>
	 */
	public synchronized void addMouseListener(MouseListener listener) {
		this.presenter.screenHandler.addMouseListener(listener);
	}


	/**
	 * <span class="lang-en">
	 * Adds the event listener for handling mouse-motion events on the graph screen
	 * </span>
	 * <span class="lang-ja">
	 * グラフスクリーン上でのマウスモーションイベントを処理する, イベントリスナーを追加します
	 * </span>
	 *
	 * @param enabled
	 *   <span class="lang-en">The event listener to be added</span>
	 *   <span class="lang-ja">追加するイベントリスナー</span>
	 */
	public synchronized void addMouseMotionListener(MouseMotionListener listener) {
		this.presenter.screenHandler.addMouseMotionListener(listener);
	}


	/**
	 * <span class="lang-en">
	 * Adds the event listener for handling mouse-wheel events on the graph screen
	 * </span>
	 * <span class="lang-ja">
	 * グラフスクリーン上でのマウスホイールイベントを処理する, イベントリスナーを追加します
	 * </span>
	 *
	 * @param enabled
	 *   <span class="lang-en">The event listener to be added</span>
	 *   <span class="lang-ja">追加するイベントリスナー</span>
	 */
	public synchronized void addMouseWheelListener(MouseWheelListener listener) {
		this.presenter.screenHandler.addMouseWheelListener(listener);
	}


	/**
	 * <span class="lang-ja">
	 * 再プロットが必要になった際に発行される RinearnGraph3DPlottingEvent を受け取る,
	 * RinearnGraph3DPlottingListener インタフェースを実装したイベントリスナーを追加登録します
	 * </span>
	 * <span class="lang-en">
	 * Adds the event listener implementing RinearnGraph3DPlottingListener,
	 * for receiving RinearnGraph3DPlottingEvent which occurs when plotting/replotting is required.
	 * </span>
	 * .
	 * @param plottingListener
	 *   <span class="lang-ja">登録するイベントリスナー</span>
	 *   <span class="lang-en">The event listener to be added</span>
	 */
	public synchronized void addPlottingListener(RinearnGraph3DPlottingListener plottingListener) {
		this.presenter.plottingEventDispatcher.addPlottingListener(plottingListener);
	}


	// ↓これ外部APIとして出す必用ある？ 出したら戻せないが。
	//    このアプリは設定更新したら内部で replot される挙動なので、不要かもしれん。
	//    要るようになった時に追加してもいいような。要検討
	//
	//    -> 思いついた。PlottingEvent のリスナーを作って登録した時、
	//       そのままでは次の再プロットタイミングまで呼ばれないから、
	//       初回描画的な意味で今すぐ実行させたい時とか。
	//
	//       現状はそのために最初の一回（イベント発動とは別に）描画処理を走らせるコードを書く必用があるが、
	//       その代わりにplot呼べばイベントとして勝手に走って描かれるので、そうした方がたぶん読みやすくなる。
	//
	//       つまりリスナー登録＆plot()で初回描画のコンボで使う。
	/**
	 * Plots all contents composing the graph again (replot).
	 */
	public synchronized void plot() {

		// これイベントスレッドでやるよう包むべきでは？今は他のAPIリクエストがイベントキューに積まれてシリアルに実行されるので。
		// 下記をダイレクトに呼んでしまうと、先に記述している設定操作がイベントスレッド上で終わる前にプロットされる事が生じ得ない？
		//
		// -> 設定操作は invokeLater じゃなく invokeAndWait してるから順序は問題ないと思うが、まあ要検討かも。
		//    全APIリクエストが一旦イベントキューに積まれてからシリアルに捌かれるよう統一した方が、確かに分かりやすいっちゃ分かりやすいし。
		//
		//    たぶん別に必須ではないので追い追い検討
		//
		//    （Presenter.plot 自体はイベントスレッド化すると他の依存場面でフロー変わるので、やるなら別メソッドを用意する方向で）

		this.presenter.plot();
	}
}
