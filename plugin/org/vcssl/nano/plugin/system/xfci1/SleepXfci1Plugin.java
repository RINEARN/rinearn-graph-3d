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

public class SleepXfci1Plugin implements ExternalFunctionConnectorInterface1 {

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
	public void initializeForExecution(Object engineConnector) throws ConnectorException { }

	// スクリプト実行後の終了時処理
	@Override
	public void finalizeForDisconnection(Object engineConnector) throws ConnectorException { }

	// 接続解除時の終了時処理
	@Override
	public void finalizeForTermination(Object engineConnector) throws ConnectorException { }


	// 関数名を返す
	@Override
	public String getFunctionName() {
		return "sleep";
	}

	// int 型の引数を取るので、その内部表現の long 型を返す
	@Override
	public Class<?>[] getParameterClasses() {
		return new Class<?>[] { long.class };
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
		return new String[] { "sleepMilliseconds" };
	}

	// 任意型の引数は取らないので false を返す
	@Override
	public boolean[] getParameterDataTypeArbitrarinesses() {
		return new boolean[]{ false };
	}

	// 任意次元の引数は取らないので false を返す
	@Override
	public boolean[] getParameterArrayRankArbitrarinesses() {
		return new boolean[]{ true };
	}

	// 参照渡しする必要はないので false を返す
	@Override
	public boolean[] getParameterReferencenesses() {
		return new boolean[]{ false };
	}

	// 引数の中身を書き変えないので true を返す（そう宣言しておくと最適化で少し有利になる可能性がある）
	@Override
	public boolean[] getParameterConstantnesses() {
		return new boolean[]{ true };
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

	// 戻り値は無いので、void 型を返す
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

		// ※ データ変換を無効化している場合、arguments[0] は戻り値格納用で、arguments[1] が最初の引数

		// データ変換を無効化しているため、処理系依存のデータコンテナそのものを扱う必要があるため、まずその互換性を検査
		if (!(arguments[1] instanceof ArrayDataAccessorInterface1)) {
			throw new ConnectorException(
				"The type of the data container \"" +
				arguments[1].getClass().getCanonicalName() +
				"\" is not supported by this plug-in."
			);
		}

		// データコンテナの型に変換（型パラメータは、このクラスの型宣言メソッドが正しく認識されていれば合っているはず）
		@SuppressWarnings("unchecked")
		ArrayDataAccessorInterface1<long[]> inputDataContainer = (ArrayDataAccessorInterface1<long[]>)arguments[1];

		// 引数データコンテナから値を取り出す（ getData() の戻り値は常に1次元配列で、スカラの場合は getOffset() 番目に値が格納されている ）
		long inputValue = inputDataContainer.getArrayData()[ inputDataContainer.getArrayOffset() ];

		// 引数に指定されたミリ秒だけ、呼び出し元スレッド（＝スクリプト実行スレッド）を停止
		try {
			Thread.sleep(inputValue);
		} catch (InterruptedException ie) {
			// 途中で割り込まれた場合は待機を終了する
		}

		// void 型関数の実装なので戻り値は無い（あっても、データ型変換を無効にしている場合は argument[0] に格納するため、ここでは何も返さない）
		return null;
	}
}
