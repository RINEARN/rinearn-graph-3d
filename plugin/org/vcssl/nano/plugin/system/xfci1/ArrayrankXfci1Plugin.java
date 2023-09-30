package org.vcssl.nano.plugin.system.xfci1;

import java.util.Locale;

import org.vcssl.connect.EngineConnectorInterface1;
import org.vcssl.connect.ArrayDataAccessorInterface1;
import org.vcssl.connect.ConnectorException;
import org.vcssl.connect.ExternalFunctionConnectorInterface1;

public class ArrayrankXfci1Plugin implements ExternalFunctionConnectorInterface1 {

	Locale locale = null;

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


	@Override
	public final String getFunctionName() {
		return "arrayrank";
	}

	@Override
	public Class<?>[] getParameterClasses() {
		return new Class<?>[] { Object.class };
	}

	// データの自動変換を無効化しているので、処理系とやり取りする際に使う型を返す
	@Override
	public Class<?>[] getParameterUnconvertedClasses() {
		return new Class<?>[] { ArrayDataAccessorInterface1.class };
	}

	@Override
	public boolean hasParameterNames() {
		return true;
	}

	@Override
	public String[] getParameterNames() {
		return new String[] { "array" };
	}

	@Override
	public boolean[] getParameterDataTypeArbitrarinesses() {
		return new boolean[]{ true };
	}


	@Override
	public boolean[] getParameterArrayRankArbitrarinesses() {
		return new boolean[]{ true };
	}

	@Override
	public boolean[] getParameterConstantnesses() {
		return new boolean[]{ true };
	}

	@Override
	public boolean[] getParameterReferencenesses() {
		return new boolean[]{ true };
	}

	@Override
	public boolean isParameterCountArbitrary() {
		return false;
	}

	@Override
	public boolean hasVariadicParameters() {
		return false;
	}

	@Override
	public Class<?> getReturnClass(Class<?>[] parameterClasses) {
		return long.class;
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

	// データの自動変換を無効化しているので、処理系とやり取りする際に使う型を返す
	@Override
	public Class<?> getReturnUnconvertedClass(Class<?>[] parameterClasses) {
		return ArrayDataAccessorInterface1.class;
	}

	@Override
	public boolean isDataConversionNecessary() {
		return false;
	}

	@Override
	public Object invoke(Object[] arguments) throws ConnectorException {

		// Check types of data containers.
		if (!(arguments[0] instanceof ArrayDataAccessorInterface1)
				|| !(arguments[1] instanceof ArrayDataAccessorInterface1) ) {

			throw new ConnectorException("The type of the data container is not supported by this plug-in.");
		}

		// Get rank of the array argument
		ArrayDataAccessorInterface1<?> arrayArgDataContainer = (ArrayDataAccessorInterface1<?>)arguments[1];
		int rank = arrayArgDataContainer.getArrayRank();

		// Get or allocate output data
		@SuppressWarnings("unchecked")
		ArrayDataAccessorInterface1<long[]> outputContainer = (ArrayDataAccessorInterface1<long[]>)arguments[0];
		Object outputDataObject = outputContainer.getArrayData();
		long[] outputData = null;
		int outputOffset = -1;
		if (outputContainer.getArrayRank() == 0 && outputDataObject instanceof long[] && 1 <= ((long[])outputDataObject).length) {
			outputData = (long[])outputDataObject;
			outputOffset = outputContainer.getArrayOffset();
		} else {
			outputData = new long[ 1 ];
			outputOffset = 0;
		}

		// Store result data
		outputData[outputOffset] = (long)rank;

		@SuppressWarnings("unchecked")
		ArrayDataAccessorInterface1<long[]> outputDataContainer = (ArrayDataAccessorInterface1<long[]>)arguments[0];
		outputDataContainer.setArrayData(outputData, outputOffset, ArrayDataAccessorInterface1.ARRAY_LENGTHS_OF_SCALAR);

		return null;
	}
}
