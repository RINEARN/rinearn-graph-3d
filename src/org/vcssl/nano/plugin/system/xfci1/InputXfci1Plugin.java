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

import org.vcssl.connect.ArrayDataAccessorInterface1;
import org.vcssl.connect.ConnectorException;
import org.vcssl.connect.EngineConnectorInterface1;
import org.vcssl.connect.ExternalFunctionConnectorInterface1;

// Interface Specification: https://www.vcssl.org/en-us/dev/code/main-jimpl/api/org/vcssl/connect/ExternalFunctionConnectorInterface1.html
// インターフェース仕様書:  https://www.vcssl.org/ja-jp/dev/code/main-jimpl/api/org/vcssl/connect/ExternalFunctionConnectorInterface1.html

public class InputXfci1Plugin implements ExternalFunctionConnectorInterface1 {

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
		return "input";
	}

	// string型の引数を取るので String 型を返す
	@Override
	public Class<?>[] getParameterClasses() {
		return new Class<?>[] { String.class };
	}

	// デフォルト値として任意型引数を取るInputDefaultXfci1Plugin から継承する都合で、
	// データの自動変換を無効化しているので、処理系とやり取りする際に使う型を返す
	@Override
	public Class<?>[] getParameterUnconvertedClasses() {
		return new Class<?>[] { ArrayDataAccessorInterface1.class };
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

	// 戻り値は string[] 型なので String[] 型を返す
	// (現状は入力値は常に 1 つだが、将来的には区切り文字設定用の関数を追加する等した上で、入力内容を区切って受け取る機能を追加する可能性がある)
	@Override
	public Class<?> getReturnClass(Class<?>[] parameterClasses) {
		return String[].class;
	}

	// デフォルト値として任意型引数を取るInputDefaultXfci1Plugin から継承する都合で、
	// データの自動変換を無効化しているので、処理系とやり取りする際に使う型を返す
	@Override
	public Class<?> getReturnUnconvertedClass(Class<?>[] parameterClasses) {
		return ArrayDataAccessorInterface1.class;
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

	// 自動変換を介さず、処理系のデータコンテナそのものを取得したいので false を返す
	@Override
	public boolean isDataConversionNecessary() {
		return false;
	}

	// スクリプトから呼ばれた際に実行する処理
	@Override
	public Object invoke(Object[] arguments) throws ConnectorException {

		// 渡される arguments の要素数やデータ入出力インターフェースは、
		// このクラスの場合は確定しているはずで、
		// ArrayDataContainerInterface 型で要素数は 2 個、
		// その内容は [0] が戻り値格納用、[1] がスクリプトから渡された 実引数。
		if (arguments.length != 2
				|| !(arguments[0] instanceof ArrayDataAccessorInterface1)
				|| !(arguments[1] instanceof ArrayDataAccessorInterface1) ) {
			throw new ConnectorException("The number or types of arguments is/are unexpected");
		}

		// arguments[1] に表示メッセージが String[] 型（スカラなので要素数は 1 ）で格納されているので取り出す
		ArrayDataAccessorInterface1<?> messageDataContainer = (ArrayDataAccessorInterface1<?>) arguments[1];
		Object messageData = messageDataContainer.getArrayData();
		String message = ((String[])messageData)[ messageDataContainer.getArrayOffset() ];

		// ユーザーによる入力処理
		String defaultValue = "";
		String inputtedValue = this.input(message, defaultValue);

		// データ型変換を無効化している場合は arguments[0] に結果を格納する仕様なので、格納する
		@SuppressWarnings("unchecked")
		ArrayDataAccessorInterface1<String[]> returnDataContainer = (ArrayDataAccessorInterface1<String[]>)arguments[0];
		String[] returnArrayData = new String[] { inputtedValue };
		int returnArrayOffset = 0; // スカラを表す場合に配列データ内での格納位置を指すが、配列を表す場合は常に 0
		int[] returnArrayLengths = new int[] { 1 }; // 次元ごとの要素数を表す配列で、戻り値は [1] なので { 1 }
		returnDataContainer.setArrayData( returnArrayData, returnArrayOffset, returnArrayLengths );

		// データ型変換を無効化している場合は arguments[0] に結果を格納する仕様なので、このメソッドの戻り値は参照されない
		return null;
	}


	// ユーザーによる入力処理（InputDefaultXfci1Plugin からも使うので、引数にデフォルト値を指定可能になっている）
	protected String input(String message, String defaultValue) throws ConnectorException {

		String inputtedValue  = null;

		// GUIモードでは、ポップアップウィンドウ上にメッセージを表示し、入力してもらう
		if (this.isGuiMode) {

			// 以下、ユーザーによる入力
			// 結果が無効だった場合（ウィンドウが閉じられた場合など）には再入力を促すが、5回で打ち切る
			int retryCount = 5;
			while (0 <= retryCount && inputtedValue == null) {
				if (retryCount == 0) {
					if (this.isJapanese) {
						throw new ConnectorException("ユーザーによる入力処理に失敗しました。");
					} else {
						throw new ConnectorException("Failed to ask for input.");
					}
				}
				if (this.isJapanese) {
					inputtedValue = JOptionPane.showInputDialog(null, message, defaultValue);
				} else {
					inputtedValue = JOptionPane.showInputDialog(null, message, defaultValue);
				}
				--retryCount;
			}

		// CUI モードでは、標準出力にメッセージを表示し、y / n のキー入力で選択してもらう
		} else {

			// メッセージを表示
			if (this.isJapanese) {
				this.stdoutStream.println("");
				this.stdoutStream.println("スクリプトからの入力要求:");
				this.stdoutStream.println(message);
				this.stdoutStream.println("");
				if (defaultValue.isEmpty()) {
					this.stdoutStream.print("[ 入力してください ]: ");
				} else {
					this.stdoutStream.print("[ 入力してください (デフォルト値=\"" + defaultValue + "\") ]: ");
				}
			} else {
				this.stdoutStream.println("");
				this.stdoutStream.println("Script is asking for input:");
				this.stdoutStream.println(message);
				this.stdoutStream.println("");
				if (defaultValue.isEmpty()) {
					this.stdoutStream.print("[ Please input ]: ");
				} else {
					this.stdoutStream.print("[ Please input (default=\"" + defaultValue + "\") ]: ");
				}
			}

			// キー入力を受け取る
			@SuppressWarnings("resource") // 注: 標準入力を close すると次回以降読めなくなる
			Scanner scanner = new Scanner(this.stdinStream);
			inputtedValue = scanner.nextLine();
			if (inputtedValue.isEmpty()) {
				inputtedValue = defaultValue;
			}
		}

		return inputtedValue;
	}

}
