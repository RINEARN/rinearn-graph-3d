/*
 * Author:  RINEARN (Fumihiro Matsui), 2020
 * License: CC0
 */

package org.vcssl.nano.plugin.system.xfci1;

import org.vcssl.connect.EngineConnectorInterface1;
import org.vcssl.connect.ArrayDataAccessorInterface1;
import org.vcssl.connect.ConnectorException;
import org.vcssl.connect.ExternalFunctionConnectorInterface1;

// Interface Specification: https://www.vcssl.org/en-us/dev/code/main-jimpl/api/org/vcssl/connect/ExternalFunctionConnectorInterface1.html
// インターフェース仕様書:  https://www.vcssl.org/ja-jp/dev/code/main-jimpl/api/org/vcssl/connect/ExternalFunctionConnectorInterface1.html

public class TimeXfci1Plugin implements ExternalFunctionConnectorInterface1 {

	private long initialNanoTime = 0;

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
		this.initialNanoTime = System.nanoTime();
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
		return "time";
	}

	// 引数は取らないので空配列を返す
	@Override
	public Class<?>[] getParameterClasses() {
		return new Class<?>[0];
	}

	// データの自動変換を無効化しているので、処理系とやり取りする際に使う型を返す
	@Override
	public Class<?>[] getParameterUnconvertedClasses() {
		return new Class<?>[] { ArrayDataAccessorInterface1.class };
	}

	// 引数名は定義されていないので false を返す
	@Override
	public boolean hasParameterNames() {
		return false;
	}

	// 引数名を返すが、引数が無いので空配列を返す
	@Override
	public String[] getParameterNames() {
		return new String[0];
	}

	// 任意型の引数を取る場合に true を返すが、引数が無いので空配列を返す
	@Override
	public boolean[] getParameterDataTypeArbitrarinesses() {
		return new boolean[0];
	}

	// 任意次元の引数を取る場合に true を返すが、引数が無いので空配列を返す
	@Override
	public boolean[] getParameterArrayRankArbitrarinesses() {
		return new boolean[0];
	}

	// 参照渡しする必要がある場合に true を返すが、引数が無いので空配列を返す
	@Override
	public boolean[] getParameterReferencenesses() {
		return new boolean[0];
	}

	// 引数の中身を書き変えない場合に true を返すが、引数が無いので空配列を返す
	@Override
	public boolean[] getParameterConstantnesses() {
		return new boolean[0];
	}

	// 任意個の引数は取らないので false を返す
	@Override
	public boolean isParameterCountArbitrary() {
		return false;
	}

	// 可変長引数は取らないので false を返す
	@Override
	public boolean hasVariadicParameters() {
		return false;
	}

	// 戻り値は int 型なので、その内部表現の long 型を返す
	@Override
	public Class<?> getReturnClass(Class<?>[] parameterClasses) {
		return long.class;
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

		// ※ データ変換を無効化している場合、arguments[0] は戻り値格納用で、arguments[1] が最初の引数

		// データ変換を無効化しているため、処理系依存のデータコンテナそのものを扱う必要があるため、まずその互換性を検査
		if (!(arguments[0] instanceof ArrayDataAccessorInterface1)) {
			throw new ConnectorException(
				"The type of the data container \"" +
				arguments[0].getClass().getCanonicalName() +
				"\" is not supported by this plug-in."
			);
		}

		// データコンテナの型に変換（型パラメータは、このクラスの型宣言メソッドが正しく認識されていれば合っているはず）
		@SuppressWarnings("unchecked")
		ArrayDataAccessorInterface1<long[]> outputDataContainer = (ArrayDataAccessorInterface1<long[]>)arguments[0];

		// 戻り値格納用データコンテナのデータ領域が未確保なら確保する
		if (outputDataContainer.getArrayData() == null) {
			outputDataContainer.setArrayData(new long[1], 0, ArrayDataAccessorInterface1.ARRAY_LENGTHS_OF_SCALAR);
		}

		// 現在の時刻を取得し、ミリ秒に変換
		long currentMilliTime = (System.nanoTime() - this.initialNanoTime) / 1000000L;

		// 結果を戻り値データコンテナに格納する
		outputDataContainer.getArrayData()[ outputDataContainer.getArrayOffset() ] = currentMilliTime;

		// 自動データ型変換を無効化している場合は、戻り値は arguments[0] に格納するため、メソッドの戻り値としては何も返さない
		return null;
	}
}
