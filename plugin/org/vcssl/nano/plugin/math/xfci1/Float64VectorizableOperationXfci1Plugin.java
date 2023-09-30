package org.vcssl.nano.plugin.math.xfci1;

import org.vcssl.connect.ExternalFunctionConnectorInterface1;

import org.vcssl.connect.EngineConnectorInterface1;
import org.vcssl.connect.ArrayDataAccessorInterface1;
import org.vcssl.connect.ConnectorException;

public class Float64VectorizableOperationXfci1Plugin implements ExternalFunctionConnectorInterface1 {

	@Override
	public String getFunctionName() {
		return "this_function_name_should_be_overridden";
	}

	@Override
	public Class<?>[] getParameterClasses() {
		return new Class<?>[] { double.class };
	}

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
		return new String[] { "arg" };
	}

	@Override
	public boolean[] getParameterDataTypeArbitrarinesses() {
		return new boolean[]{ false };
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
		return parameterClasses[0];
	}

	@Override
	public Class<?> getReturnUnconvertedClass(Class<?>[] parameterClasses) {
		return ArrayDataAccessorInterface1.class;
	}

	@Override
	public boolean isReturnDataTypeArbitrary() {
		return false;
	}

	@Override
	public boolean isReturnArrayRankArbitrary() {
		return true;
	}

	@Override
	public boolean isDataConversionNecessary() {
		return false;
	}

	@Override
	public Object invoke(Object[] arguments) throws ConnectorException {

		// Check types of data containers.
		if (!(arguments[0] instanceof ArrayDataAccessorInterface1) || !(arguments[1] instanceof ArrayDataAccessorInterface1)) {
			throw new ConnectorException("The type of the data container is not supported by this plug-in.");
		}

		// Check types of stored data.
		Object inputDataObject = ( (ArrayDataAccessorInterface1<?> )arguments[1]).getArrayData();
		Object outputDataObject = ( (ArrayDataAccessorInterface1<?> )arguments[0]).getArrayData(); // データ型変換無効化時は [0] が戻り値格納用
		if (!( inputDataObject instanceof double[] )) {
			throw new ConnectorException("The data type of the argument of this function should be \"float\" or \"double\".");
		}
		if (!( outputDataObject instanceof double[] || outputDataObject == null )) {
			throw new ConnectorException("The data type of the argument of this function should be \"float\" or \"double\".");
		}

		@SuppressWarnings("unchecked")
		ArrayDataAccessorInterface1<double[]> inputDataContainer = (ArrayDataAccessorInterface1<double[]>)arguments[1];
		@SuppressWarnings("unchecked")
		ArrayDataAccessorInterface1<double[]> outputDataContainer = (ArrayDataAccessorInterface1<double[]>)arguments[0];

		// Get or allocate input data
		double[] inputData = (double[])inputDataContainer.getArrayData();
		int inputDataOffset = inputDataContainer.getArrayOffset();
		int inputDataSize = inputDataContainer.getArraySize();

		// Get or allocate output data
		double[] outputData = outputDataContainer.getArrayData();
		int outputDataOffset = outputDataContainer.getArrayOffset();
		int outputDataSize = outputDataContainer.getArraySize();
		if (outputDataObject == null || outputDataSize != inputDataSize) {
			outputData = new double[ inputDataSize ];
			outputDataSize = inputDataSize;
			outputDataOffset = 0;
		}

		// Operate data
		this.operate(outputData, inputData, outputDataOffset, inputDataOffset, inputDataSize);

		// Store result data
		int[] outputDataLengths = new int[ inputDataContainer.getArrayRank() ];
		System.arraycopy(inputDataContainer.getArrayLengths(), 0, outputDataLengths, 0, inputDataContainer.getArrayRank());
		outputDataContainer.setArrayData(outputData, outputDataOffset, outputDataLengths);

		return null;
	}

	// Overridden on subclasses
	public void operate(double[] outputData, double[] inputData, int outputDataOffset, int inputDataOffset, int dataSize) {

	}

	@Override
	public Class<?> getEngineConnectorClass() {
		return EngineConnectorInterface1.class;
	}
	@Override
	public void initializeForConnection(Object engineConnector) throws ConnectorException { }
	@Override
	public void initializeForExecution(Object engineConnector) throws ConnectorException { }
	@Override
	public void finalizeForDisconnection(Object engineConnector) throws ConnectorException { }
	@Override
	public void finalizeForTermination(Object engineConnector) throws ConnectorException { }
}
