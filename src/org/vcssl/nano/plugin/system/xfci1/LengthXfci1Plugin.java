package org.vcssl.nano.plugin.system.xfci1;

import java.util.Locale;

import org.vcssl.connect.ArrayDataAccessorInterface1;
import org.vcssl.connect.ConnectorException;
import org.vcssl.connect.EngineConnectorInterface1;
import org.vcssl.connect.ExternalFunctionConnectorInterface1;

public class LengthXfci1Plugin implements ExternalFunctionConnectorInterface1 {

	Locale locale = Locale.getDefault();

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

		// 言語ロケール情報を取得（エラーメッセージの言語を変えるため）
		if (eci1Connector.hasOptionValue("LOCALE")) {
			this.locale = (Locale)eci1Connector.getOptionValue("LOCALE");
		}
	}

	// スクリプト実行後の終了時処理
	@Override
	public void finalizeForDisconnection(Object engineConnector) throws ConnectorException { }

	// 接続解除時の終了時処理
	@Override
	public void finalizeForTermination(Object engineConnector) throws ConnectorException { }


	@Override
	public final String getFunctionName() {
		return "length";
	}

	@Override
	public Class<?>[] getParameterClasses() {
		return new Class<?>[] { Object.class, long.class };
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
		return new String[] { "array", "dimIndex" };
	}

	@Override
	public boolean[] getParameterDataTypeArbitrarinesses() {
		return new boolean[]{ true, false };
	}


	@Override
	public boolean[] getParameterArrayRankArbitrarinesses() {
		return new boolean[]{ true, false };
	}

	@Override
	public boolean[] getParameterConstantnesses() {
		return new boolean[]{ true, true };
	}

	@Override
	public boolean[] getParameterReferencenesses() {
		return new boolean[]{ true, true };
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

	@Override
	public boolean isDataConversionNecessary() {
		return false;
	}

	@Override
	public Object invoke(Object[] arguments) throws ConnectorException {

		// Check types of data containers.
		if (!(arguments[0] instanceof ArrayDataAccessorInterface1)
				|| !(arguments[1] instanceof ArrayDataAccessorInterface1)
				|| !(arguments[2] instanceof ArrayDataAccessorInterface1) ) {

			throw new ConnectorException("The type of the data container is not supported by this plug-in.");
		}

		// Get rank and lengths of the array argument
		ArrayDataAccessorInterface1<?> arrayArgDataContainer = (ArrayDataAccessorInterface1<?>)arguments[1];
		int rank = arrayArgDataContainer.getArrayRank();
		int[] lengths = arrayArgDataContainer.getArrayLengths();
		if (rank == 0) {

			if (    ( this.locale.getLanguage()!=null && locale.getLanguage().equals("ja") )
				     || ( this.locale.getCountry()!=null && locale.getCountry().equals("JP")   )   ) {

				throw new ConnectorException("「 length 」関数は、配列ではない引数に対しては使用できません。");
			} else {
				throw new ConnectorException("\"length\" function is not available for non-array argument.");
			}
		}

		// Get value of the dim-index argument
		@SuppressWarnings("unchecked")
		ArrayDataAccessorInterface1<long[]> dimIndexArgDataContainer = (ArrayDataAccessorInterface1<long[]>)arguments[2];
		long dimIndex = dimIndexArgDataContainer.getArrayData()[ dimIndexArgDataContainer.getArrayOffset() ];

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
		outputData[outputOffset] = (long)lengths[ (int)dimIndex ];

		@SuppressWarnings("unchecked")
		ArrayDataAccessorInterface1<long[]> outputDataContainer = (ArrayDataAccessorInterface1<long[]>)arguments[0];
		outputDataContainer.setArrayData(outputData, outputOffset, ArrayDataAccessorInterface1.ARRAY_LENGTHS_OF_SCALAR);

		return null;
	}
}
