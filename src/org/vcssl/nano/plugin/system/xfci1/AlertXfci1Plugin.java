/*
 * Author:  RINEARN (Fumihiro Matsui), 2021
 * License: CC0
 */

package org.vcssl.nano.plugin.system.xfci1;

import java.util.Scanner;

import javax.swing.JOptionPane;

// Interface Specification: https://www.vcssl.org/en-us/dev/code/main-jimpl/api/org/vcssl/connect/ExternalFunctionConnectorInterface1.html
// インターフェース仕様書:  https://www.vcssl.org/ja-jp/dev/code/main-jimpl/api/org/vcssl/connect/ExternalFunctionConnectorInterface1.html

public class AlertXfci1Plugin extends PopupXfci1Plugin {

	// 関数名を返す
	@Override
	public String getFunctionName() {
		return "alert";
	}

	// string型の引数を取るので String 型を返す
	@Override
	public Class<?>[] getParameterClasses() {
		return new Class<?>[] { String.class };
	}

	// 任意型の引数は取らないので false を返す
	@Override
	public boolean[] getParameterDataTypeArbitrarinesses() {
		return new boolean[]{ false };
	}


	// 関数が呼ばれた際に PopupXfci1Plugin の invoke 内で実行されるメッセージ表示処理
	// (popup 関数とはウィンドウタイトルやガイドメッセージなどが少し異なる)
	@Override
	protected void showMessage(String message) {

		// GUIモードでは、ポップアップウィンドウ上にメッセージを表示する
		if (this.isGuiMode) {
			if (this.isJapanese) {
				JOptionPane.showMessageDialog(null, message, "スクリプトからの注意メッセージ", JOptionPane.WARNING_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, message, "WARNING FROM SCRIPT", JOptionPane.WARNING_MESSAGE);
			}

		// CUI モードでは、標準出力にメッセージを表示し、Enterキーが入力されるまで一時停止する
		} else {

			// メッセージを表示
			if (this.isJapanese) {
				this.stdoutStream.println("");
				this.stdoutStream.println("スクリプトからの注意メッセージ:");
				this.stdoutStream.println(message);
				this.stdoutStream.println("");
				this.stdoutStream.print("(Enterキーを押すと続行)");
			} else {
				this.stdoutStream.println("");
				this.stdoutStream.println("WARNING FROM SCRIPT:");
				this.stdoutStream.println(message);
				this.stdoutStream.println("");
				this.stdoutStream.print("(Press the \"Enter\" key to continue)");
			}

			// Enterキー入力を待つ
			@SuppressWarnings("resource") // 注: 標準入力を close すると次回以降読めなくなる
			Scanner scanner = new Scanner(this.stdinStream);
			scanner.nextLine();
		}
	}
}
