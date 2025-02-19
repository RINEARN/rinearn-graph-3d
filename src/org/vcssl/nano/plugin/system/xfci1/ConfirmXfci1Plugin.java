/*
 * Author:  RINEARN (Fumihiro Matsui), 2021
 * License: CC0
 */

package org.vcssl.nano.plugin.system.xfci1;

import java.io.PrintStream;
import java.io.InputStream;
import java.util.Locale;
import java.util.Scanner;

import javax.swing.JOptionPane;

import org.vcssl.connect.ConnectorException;
import org.vcssl.connect.EngineConnectorInterface1;
import org.vcssl.connect.ExternalFunctionConnectorInterface1;

// Interface Specification: https://www.vcssl.org/en-us/dev/code/main-jimpl/api/org/vcssl/connect/ExternalFunctionConnectorInterface1.html
// インターフェース仕様書:  https://www.vcssl.org/ja-jp/dev/code/main-jimpl/api/org/vcssl/connect/ExternalFunctionConnectorInterface1.html

public class ConfirmXfci1Plugin implements ExternalFunctionConnectorInterface1 {

	protected PrintStream stdoutStream = null;
	protected InputStream stdinStream = null;
	protected boolean isGuiMode = false;
	protected Locale locale = null;
	protected boolean isJapanese = false;

	// 初期化/終了時処理の引数に渡される、スクリプトエンジンと情報をやり取りするインターフェースの指定
	@Override
	public Class<?> getEngineConnectorClass() {
		return EngineConnectorInterface1.class;
	}

	// 接続時の初期化
	@Override
	public void initializeForConnection(Object engineConnector) throws ConnectorException { }

	// スクリプト実行前の初期化
	@Override
	public void initializeForExecution(Object engineConnector) throws ConnectorException {

		// 処理系の情報を取得するコネクタ（処理系依存）の互換性を検査
		if (!(engineConnector instanceof EngineConnectorInterface1)) {
			throw new ConnectorException(
				"The type of the engine connector \"" +
				engineConnector.getClass().getCanonicalName() +
				"\" is not supported by this plug-in."
			);
		}
		EngineConnectorInterface1 eci1Connector = (EngineConnectorInterface1)engineConnector;

		// 処理系のオプションから、GUIモードかどうかの設定を取得
		if (eci1Connector.hasOptionValue("UI_MODE")) {
			this.isGuiMode = ( (String)eci1Connector.getOptionValue("UI_MODE") ).equals("GUI");
		} else {
			// 未設定ならGUIモードにしておく
			// (コマンドライン端末が表示されない環境ではユーザーが返答リクエストに気付かないかもしれないため)
			this.isGuiMode = true;
		}

		// 処理系のオプションから、標準入出力用のストリームを取得
		if (eci1Connector.hasOptionValue("STDOUT_STREAM")) {
			this.stdoutStream = (PrintStream)eci1Connector.getOptionValue("STDOUT_STREAM");
		} else {
			throw new ConnectorException(
				"The option \"STDOUT_STREAM\" is required for using \""
				+ this.getClass().getCanonicalName() +
				"\" function on CUI mode, but it is not set."
			);
		}
		if (eci1Connector.hasOptionValue("STDIN_STREAM")) {
			this.stdinStream = (InputStream)eci1Connector.getOptionValue("STDIN_STREAM");
		} else {
			throw new ConnectorException(
				"The option \"STDIN_STREAM\" is required for using \""
				+ this.getClass().getCanonicalName() +
				"\" function on CUI mode, but it is not set."
			);
		}

		// 言語ロケール情報を取得（CUIモードでのガイドメッセージの言語を変えるため）
		if (eci1Connector.hasOptionValue("LOCALE")) {
			this.locale = (Locale)eci1Connector.getOptionValue("LOCALE");
		} else {
			this.locale = Locale.getDefault();
		}
		if (    ( this.locale.getLanguage()!=null && this.locale.getLanguage().equals("ja") )
		     || ( this.locale.getCountry()!=null && this.locale.getCountry().equals("JP")   )   ) {
			this.isJapanese = true;
		}

	}

	// スクリプト実行後の終了時処理
	@Override
	public void finalizeForDisconnection(Object engineConnector) throws ConnectorException { }

	// 接続解除時の終了時処理
	@Override
	public void finalizeForTermination(Object engineConnector) throws ConnectorException { }


	// 関数名を返す
	@Override
	public String getFunctionName() {
		return "confirm";
	}

	// string型の引数を取るので String 型を返す
	@Override
	public Class<?>[] getParameterClasses() {
		return new Class<?>[] { String.class };
	}

	// データの自動変換を有効化しているので参照されない
	@Override
	public Class<?>[] getParameterUnconvertedClasses() {
		return new Class<?>[0];
	}

	// 引数名が定義されているので true を返す
	@Override
	public boolean hasParameterNames() {
		return true;
	}

	// 引数名を返す
	@Override
	public String[] getParameterNames() {
		return new String[] { "message" };
	}

	// 任意型の引数は取らないので false を返す
	@Override
	public boolean[] getParameterDataTypeArbitrarinesses() {
		return new boolean[]{ false };
	}

	// 任意次元の引数は取らないので false を返す
	@Override
	public boolean[] getParameterArrayRankArbitrarinesses() {
		return new boolean[]{ false };
	}

	// 引数が参照渡しされる必要はないので false を返す
	@Override
	public boolean[] getParameterReferencenesses() {
		return new boolean[]{ false };
	}

	// 引数の中身を書き変えないので true を返す（参照渡しの場合はそう宣言しないと、リテラル等を引数に取れない上に、最適化でも少し不利になる）
	@Override
	public boolean[] getParameterConstantnesses() {
		return new boolean[]{ true };
	}

	// 任意個の引数は取らないので false を返す
	@Override
	public boolean isParameterCountArbitrary() {
		return false;
	}

	// 可変長引数は上記の任意個引数とは少し仕様が異なり、このプラグインでは使わない（Vnanoでは未対応）ので false を返す
	@Override
	public boolean hasVariadicParameters() {
		return false;
	}

	// 戻り値は bool 型なので boolean 型を返す
	@Override
	public Class<?> getReturnClass(Class<?>[] parameterClasses) {
		return boolean.class;
	}

	// データの自動変換を有効化しているので参照されない
	@Override
	public Class<?> getReturnUnconvertedClass(Class<?>[] parameterClasses) {
		return null;
	}

	// 戻り値のデータ型は固定なので false
	@Override
	public boolean isReturnDataTypeArbitrary() {
		return false;
	}

	// 戻り値の配列次元数は固定なので false
	@Override
	public boolean isReturnArrayRankArbitrary() {
		return false;
	}

	// データ型を自動変換してほしいので true を返す
	@Override
	public boolean isDataConversionNecessary() {
		return true;
	}

	// スクリプトから呼ばれた際に実行する処理
	@Override
	public Object invoke(Object[] arguments) throws ConnectorException {

		if (arguments.length != 1 || !(arguments[0] instanceof String)) {
			throw new ConnectorException("The number or types of arguments is/are unexpected");
		}
		String message = (String)arguments[0]; // 引数: 表示するメッセージ
		boolean result = false;                // 戻り値: ユーザーによる選択結果

		// GUIモードでは、ポップアップウィンドウ上にメッセージを表示し、選択してもらう
		if (this.isGuiMode) {
			int resultInt = JOptionPane.CANCEL_OPTION;

			// 以下、ユーザーによる選択
			// 結果無効だった場合（ウィンドウが閉じられた場合など）には再選択を促すが、5回で打ち切る
			int retryCount = 5;
			while (0 <= retryCount && resultInt != JOptionPane.YES_OPTION && resultInt != JOptionPane.NO_OPTION) {
				if (retryCount == 0) {
					if (this.isJapanese) {
						throw new ConnectorException("ユーザーによる確認処理に失敗しました。");
					} else {
						throw new ConnectorException("Failed to confirm by the user.");
					}
				}
				if (this.isJapanese) {
					resultInt = JOptionPane.showConfirmDialog(null, message, "スクリプトからの確認メッセージ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				} else {
					resultInt = JOptionPane.showConfirmDialog(null, message, "Confirmation from script", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				}
				--retryCount;
			}
			result = resultInt == JOptionPane.YES_OPTION;

		// CUI モードでは、標準出力にメッセージを表示し、y / n のキー入力で選択してもらう
		} else {

			// メッセージを表示
			if (this.isJapanese) {
				this.stdoutStream.println("");
				this.stdoutStream.println("スクリプトからの確認メッセージ:");
				this.stdoutStream.println(message);
				this.stdoutStream.println("");
			} else {
				this.stdoutStream.println("");
				this.stdoutStream.println("Confirmation from script:");
				this.stdoutStream.println(message);
				this.stdoutStream.println("");
			}

			// 以下、キー入力から選択結果を判定する

			@SuppressWarnings("resource") // 注: 標準入力を close すると次回以降読めなくなる
			Scanner scanner = new Scanner(this.stdinStream);

			// ユーザーによる入力結果が無効だった場合に備えて、無効なら再入力を促すが、4回で打ち切る
			int retryCount = 5;
			String line = null;
			boolean inputIsValid = false;
			while (!inputIsValid && 0<=retryCount) {
				if (retryCount == 0) {
					if (this.isJapanese) {
						throw new ConnectorException("ユーザーによる確認処理に失敗しました。");
					} else {
						throw new ConnectorException("Failed to confirm by the user.");
					}
				}
				if (this.isJapanese) {
					this.stdoutStream.print("[ y (yes) / n (no) で返答]: ");
				} else {
					this.stdoutStream.print("[ Input y (yes) / n (no) ]: ");
				}
				line = scanner.nextLine().toLowerCase();
				inputIsValid = line.equals("y") || line.equals("n") || line.equals("yes") || line.equals("no");
				retryCount--;
			}

			// y か yes が選択されれば true を返す
			result = line.equals("y") || line.equals("yes");
		}

		return result;
	}
}
