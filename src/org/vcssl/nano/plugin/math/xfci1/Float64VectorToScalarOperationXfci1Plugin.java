package org.vcssl.nano.plugin.math.xfci1;

import org.vcssl.connect.ExternalFunctionConnectorInterface1;

import org.vcssl.connect.EngineConnectorInterface1;
import org.vcssl.connect.ArrayDataAccessorInterface1;
import org.vcssl.connect.Float64ScalarDataAccessorInterface1;
import org.vcssl.connect.ConnectorException;

public class Float64VectorToScalarOperationXfci1Plugin implements ExternalFunctionConnectorInterface1 {

	@Override
	public String getFunctionName() {
		return "this_function_name_should_be_overridden";
	}

	@Override
	public Class<?>[] getParameterClasses() {
		return new Class<?>[] { double[].class };
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
		return new String[] { "args" };
	}

	@Override
	public Class<?> getReturnClass(Class<?>[] parameterClasses) {
		return double.class;
	}

	@Override
	public Class<?> getReturnUnconvertedClass(Class<?>[] parameterClasses) {
		return Float64ScalarDataAccessorInterface1.class;
	}

	@Override
	public boolean isReturnDataTypeArbitrary() {
		return false;
	}

	@Override
	public boolean isReturnArrayRankArbitrary() {
		return false;
	}

	@Override
	public boolean[] getParameterDataTypeArbitrarinesses() {
		return new boolean[]{ false };
	}


	@Override
	public boolean[] getParameterArrayRankArbitrarinesses() {
		return new boolean[]{ false };
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
	public boolean isDataConversionNecessary() {
		return false;
	}

	@Override
	public Object invoke(Object[] arguments) throws ConnectorException {

		// Check types of stored data.
		Object inputDataObject = ( (ArrayDataAccessorInterface1<?> )arguments[1]).getArrayData();
		if (!( inputDataObject instanceof double[] )) {
			throw new ConnectorException("The data type of the argument of this function should be \"float\" or \"double\".");
		}

		@SuppressWarnings("unchecked")
		ArrayDataAccessorInterface1<double[]> inputDataContainer = (ArrayDataAccessorInterface1<double[]>)arguments[1];
		Float64ScalarDataAccessorInterface1 outputDataContainer = Float64ScalarDataAccessorInterface1.class.cast(arguments[0]);

		// Get input data
		double[] inputData = inputDataContainer.getArrayData();
		int inputDataOffset = inputDataContainer.getArrayOffset();
		int inputDataSize = inputDataContainer.getArraySize();

		// Operate data
		double result = this.operate(inputData, inputDataOffset, inputDataSize);

		// Store result data
		outputDataContainer.setFloat64ScalarData(result);

		return null;
	}

	// Overridden on subclasses
	public double operate(double[] inputData, int inputDataOffset, int inputDataSize) {
		return Double.NaN;
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
