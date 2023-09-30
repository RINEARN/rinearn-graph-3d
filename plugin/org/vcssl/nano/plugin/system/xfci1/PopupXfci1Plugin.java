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

public class PopupXfci1Plugin implements ExternalFunctionConnectorInterface1 {

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
		return "popup";
	}

	// 任意型の引数を取るので Object 型を返す
	@Override
	public Class<?>[] getParameterClasses() {
		return new Class<?>[] { Object.class };
	}

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

	// 任意型の引数を取るので true を返す
	@Override
	public boolean[] getParameterDataTypeArbitrarinesses() {
		return new boolean[]{ true };
	}

	// 任意次元の引数は取らないので false を返す
	@Override
	public boolean[] getParameterArrayRankArbitrarinesses() {
		return new boolean[]{ false };
	}

	// 配列が渡された際のコピーオーバーヘッドを削るため、引数をコピーせず参照渡ししてほしいので true を返す
	@Override
	public boolean[] getParameterReferencenesses() {
		return new boolean[]{ true };
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

	// 戻り値は無いので void 型を返す
	@Override
	public Class<?> getReturnClass(Class<?>[] parameterClasses) {
		return void.class;
	}

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
		// その内容は [0] が戻り値格納用、スクリプトから渡された [1] が実引数。
		if (arguments.length != 2
				|| !(arguments[0] instanceof ArrayDataAccessorInterface1)
				|| !(arguments[1] instanceof ArrayDataAccessorInterface1) ) {
			throw new ConnectorException("The number or types of arguments is/are unexpected");
		}

		// 実引数のデータコンテナ: 表示すべきメッセージが格納されている
		ArrayDataAccessorInterface1<?> messageDataContainer = (ArrayDataAccessorInterface1<?>) arguments[1];

		// 実引数の格納データを、型に応じて文字列に変換する
		Object messageData = messageDataContainer.getArrayData(); // 引数はスカラと宣言しているので要素数は1
		String messageString = null;
		if (messageData instanceof long[]){
			messageString = Long.toString( ((long[])messageData)[ messageDataContainer.getArrayOffset() ] );
		} else if (messageData instanceof double[]){
			messageString = Double.toString( ((double[])messageData)[ messageDataContainer.getArrayOffset() ] );
		} else if (messageData instanceof boolean[]){
			messageString = Boolean.toString( ((boolean[])messageData)[ messageDataContainer.getArrayOffset() ] );
		} else if (messageData instanceof String[]){
			messageString = ((String[])messageData)[ messageDataContainer.getArrayOffset() ];
		} else {
			throw new ConnectorException(
				"Unsupported internal data type: " + messageData.getClass().getCanonicalName()
			);
		}

		// メッセージを表示
		this.showMessage(messageString);

		// データ型変換を無効化している場合は arguments[0] に結果を格納する仕様なので、このメソッドの戻り値は参照されない
		return null;
	}


	protected void showMessage(String message) {

		// GUIモードでは、ポップアップウィンドウ上にメッセージを表示する
		if (this.isGuiMode) {
			if (this.isJapanese) {
				JOptionPane.showMessageDialog(null, message, "スクリプトからのメッセージ", JOptionPane.PLAIN_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, message, "Message from script", JOptionPane.PLAIN_MESSAGE);
			}

		// CUI モードでは、標準出力にメッセージを表示し、Enterキーが入力されるまで一時停止する
		} else {

			// メッセージを表示
			if (this.isJapanese) {
				this.stdoutStream.println("");
				this.stdoutStream.println("スクリプトからのメッセージ:");
				this.stdoutStream.println(message);
				this.stdoutStream.println("");
				this.stdoutStream.print("(Enterキーを押すと続行)");
			} else {
				this.stdoutStream.println("");
				this.stdoutStream.println("Message from script:");
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
