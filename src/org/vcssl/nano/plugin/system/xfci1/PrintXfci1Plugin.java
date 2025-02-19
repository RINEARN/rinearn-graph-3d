/*
 * Author:  RINEARN (Fumihiro Matsui), 2020
 * License: CC0
 */

package org.vcssl.nano.plugin.system.xfci1;

import java.io.PrintStream;

import org.vcssl.connect.ArrayDataAccessorInterface1;
import org.vcssl.connect.ConnectorException;
import org.vcssl.connect.EngineConnectorInterface1;
import org.vcssl.connect.ExternalFunctionConnectorInterface1;
import org.vcssl.nano.plugin.system.terminal.TerminalWindow;

// Interface Specification: https://www.vcssl.org/en-us/dev/code/main-jimpl/api/org/vcssl/connect/ExternalFunctionConnectorInterface1.html
// インターフェース仕様書:  https://www.vcssl.org/ja-jp/dev/code/main-jimpl/api/org/vcssl/connect/ExternalFunctionConnectorInterface1.html

public class PrintXfci1Plugin implements ExternalFunctionConnectorInterface1 {

	protected String delimiter = null;
	protected String printEnd = null;

	private PrintStream stdoutStream = null;
	private TerminalWindow terminalWindow = null;
	private boolean isGuiMode = false;

	public PrintXfci1Plugin(boolean isGuiMode, TerminalWindow terminalWindow) {
		this.printEnd = "";
		this.delimiter = "\t";
		this.isGuiMode = isGuiMode;
		this.terminalWindow = terminalWindow;
	}

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

		// 処理系のオプションから、標準出力用のストリームを取得
		if (eci1Connector.hasOptionValue("STDOUT_STREAM")) {
			this.stdoutStream = (PrintStream)eci1Connector.getOptionValue("STDOUT_STREAM");
		} else {
			throw new ConnectorException("The option \"STDOUT_STREAM\" is required for using \"print\" function, but it is not set.");
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
		return "print";
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
		return new String[] { "value" };
	}

	// 任意型の引数を取るので true を返す
	@Override
	public boolean[] getParameterDataTypeArbitrarinesses() {
		return new boolean[]{ true };
	}

	// 任意次元の引数を取るので true を返す
	@Override
	public boolean[] getParameterArrayRankArbitrarinesses() {
		return new boolean[]{ true };
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

	// 任意個の引数を取るので true を返す
	@Override
	public boolean isParameterCountArbitrary() {
		return true;
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

		int argLength = arguments.length;

		// データ変換を無効化しているため、処理系依存のデータコンテナそのものを扱う必要があるため、まずその互換性を検査
		for (int argIndex=1; argIndex<argLength; argIndex++) { // データ変換を無効化している場合、[0] は戻り値格納用
			if (!(arguments[argIndex] instanceof ArrayDataAccessorInterface1)) {
				throw new ConnectorException(
					"The type of the data container \"" +
					arguments[argIndex].getClass().getCanonicalName() +
					"\" is not supported by this plug-in."
				);
			}
		}

		// print 内容を控えるバッファ
		StringBuilder printContentBuilder = new StringBuilder();

		// 引数のデータを上記バッファに print していく
		for (int argIndex=1; argIndex<argLength; argIndex++) { // データ変換を無効化している場合、[0] は戻り値格納用

			ArrayDataAccessorInterface1<?> inputDataContainer = (ArrayDataAccessorInterface1<?>) arguments[argIndex];

			// データコンテナの格納データ、要素数、要素格納位置を取得
			Object inputDataObject = ( (ArrayDataAccessorInterface1<?>)arguments[argIndex] ).getArrayData();
			int inputDataSize = inputDataContainer.getArraySize();
			int inputDataOffset = inputDataContainer.getArrayOffset();

			// ArrayDataContainerInterface1 型のデータコンテナでは、
			// スクリプト上での配列次元数に関わらず、全てのデータは1次元配列として保持される。
			// スクリプト上での次元数やスカラかどうかは、
			// ランク値（getRank()で取得）や次元長配列（getLengths()で取得）で表現される。

			// このプラグインが提供する print 関数では、配列次元数やスカラかどうかに関わらず、
			// 単純に全ての値をデリミタ区切りで print する。　

			// スクリプト内で int 型データが渡された場合: 内部表現は long 型の1次元配列
			if (inputDataObject instanceof long[]) {
				long[] inputData = (long[])inputDataObject;
				for (int i=0; i<inputDataSize; i++) {
					printContentBuilder.append( inputData[ inputDataOffset + i ] );
					if (i != inputDataSize - 1) {
						printContentBuilder.append(this.delimiter); // 最後の要素値でなければデリミタを挟む
					}
				}

			// スクリプト内で float 型データが渡された場合: 内部表現は double 型の1次元配列
			} else if (inputDataObject instanceof double[]) {
				double[] inputData = (double[])inputDataObject;
				for (int i=0; i<inputDataSize; i++) {
					printContentBuilder.append( inputData[ inputDataOffset + i ] );
					if (i != inputDataSize - 1) {
						printContentBuilder.append(this.delimiter); // 最後の要素値でなければデリミタを挟む
					}
				}

			// スクリプト内で bool 型データが渡された場合: 内部表現は boolean 型の1次元配列
			} else if (inputDataObject instanceof boolean[]) {
				boolean[] inputData = (boolean[])inputDataObject;
				for (int i=0; i<inputDataSize; i++) {
					printContentBuilder.append( inputData[ inputDataOffset + i ] );
					if (i != inputDataSize - 1) {
						printContentBuilder.append(this.delimiter); // 最後の要素値でなければデリミタを挟む
					}
				}

			// スクリプト内で string 型データが渡された場合: 内部表現は String 型の1次元配列
			} else if (inputDataObject instanceof String[]) {
				String[] inputData = (String[])inputDataObject;
				for (int i=0; i<inputDataSize; i++) {
					printContentBuilder.append( inputData[ inputDataOffset + i ] );
					if (i != inputDataSize - 1) {
						printContentBuilder.append(this.delimiter); // 最後の要素値でなければデリミタを挟む
					}
				}

			// 未対応の型の場合
			} else {
				throw new ConnectorException(
					"Unsupported internal data type: " + inputDataObject.getClass().getCanonicalName()
				);
			}

			// 最後の引数でなければデリミタを挟む
			if (argIndex != arguments.length - 1) {
				printContentBuilder.append(this.delimiter);
			}
		}

		// 全引数の print が終わったら、設定された print 終端文字を出力（デフォルトでは空文字）
		printContentBuilder.append(this.printEnd);

		// バッファに溜めた内容を print する
		String printContent = printContentBuilder.toString();
		if (this.isGuiMode) {
			this.terminalWindow.print(printContent);
		} else {
			this.stdoutStream.print(printContent);
		}

		// データ型変換を無効化している場合は arguments[0] に結果を格納する仕様なので、このメソッドの戻り値は参照されない
		return null;
	}
}
