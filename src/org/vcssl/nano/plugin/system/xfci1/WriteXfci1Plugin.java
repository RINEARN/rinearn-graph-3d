/*
 * Author:  RINEARN (Fumihiro Matsui), 2022
 * License: CC0
 * Interface Specification:
 *     https://www.vcssl.org/en-us/doc/connect/ExternalFunctionConnectorInterface1_SPEC_ENGLISH
 */

package org.vcssl.nano.plugin.system.xfci1;

import org.vcssl.nano.plugin.system.file.FileIOHub;

import org.vcssl.connect.ConnectorException;
import org.vcssl.connect.ConnectorFatalException;
import org.vcssl.connect.Int64ScalarDataAccessorInterface1;
import org.vcssl.connect.ArrayDataAccessorInterface1;
import org.vcssl.connect.EngineConnectorInterface1;
import org.vcssl.connect.ExternalFunctionConnectorInterface1;


/**
 * A function plug-in providing "System.write(int fileId, string content)" function.
 */
public class WriteXfci1Plugin implements ExternalFunctionConnectorInterface1 {

	/** An object managing file I/O from/to (multiple) files. */
	protected FileIOHub fileIOHub = null;

	/** Stores the engine connector for requesting permissions. */
	protected EngineConnectorInterface1 engineConnector = null;

	/**
	 * Create a new instance of this plug-in.
	 * 
	 * @param fileIOHub The file I/O hub, through which the file I/O will be performed.
	 */
	public WriteXfci1Plugin(FileIOHub fileIOHub) {
		this.fileIOHub = fileIOHub;
	}

	@Override
	public Class<?> getEngineConnectorClass() {
		return EngineConnectorInterface1.class;
	}

	@Override
	public void initializeForConnection(Object engineConnector) throws ConnectorException { }

	@Override
	public void initializeForExecution(Object engineConnector) throws ConnectorException {
		this.engineConnector = EngineConnectorInterface1.class.cast(engineConnector);
	}

	@Override
	public void finalizeForDisconnection(Object engineConnector) throws ConnectorException { }

	@Override
	public void finalizeForTermination(Object engineConnector) throws ConnectorException { }

	@Override
	public String getFunctionName() {
		return "write";
	}

	@Override
	public Class<?>[] getParameterClasses() {
		return new Class<?>[] { long.class, Object.class };
	}

	@Override
	public Class<?>[] getParameterUnconvertedClasses() {
		return new Class<?>[] { Int64ScalarDataAccessorInterface1.class, ArrayDataAccessorInterface1.class };
	}

	@Override
	public boolean hasParameterNames() {
		return true;
	}

	@Override
	public String[] getParameterNames() {
		return new String[] { "fileId", "contents" };
	}

	@Override
	public boolean[] getParameterDataTypeArbitrarinesses() {
		return new boolean[]{ false, true };
	}

	@Override
	public boolean[] getParameterArrayRankArbitrarinesses() {
		return new boolean[]{ false, false };
	}

	@Override
	public boolean[] getParameterReferencenesses() {
		return new boolean[]{ false, true };
	}

	@Override
	public boolean[] getParameterConstantnesses() {
		return new boolean[]{ true, true };
	}

	@Override
	public boolean isParameterCountArbitrary() {
		return true;
	}

	@Override
	public boolean hasVariadicParameters() {
		return false;
	}

	@Override
	public Class<?> getReturnClass(Class<?>[] parameterClasses) {
		return void.class;
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
		return false;
	}

	@Override
	public boolean isDataConversionNecessary() {
		return false;
	}

	@Override
	public Object invoke(Object[] arguments) throws ConnectorException {
		int argLength = arguments.length;

		// Note:
		//    arguments[0] is the container for storing the return value, 
		//    argument[1] is the "fileId" arg,
		//    Others are "contents" arguments.

		// Get the value of the specified fileId.
		int fileId = (int)Int64ScalarDataAccessorInterface1.class.cast(arguments[1]).getInt64ScalarData();

		// Get the length of "contents" arguments.
		int contentLength = argLength - 2;
		String[] contents = new String[contentLength];

		// Convert each arg value to a String-type value, and store it in the "contents" an array.
		for (int icontent=0; icontent<contentLength; icontent++) {
			ArrayDataAccessorInterface1<?> container = ArrayDataAccessorInterface1.class.cast(arguments[icontent + 2]);
			contents[icontent] = this.convertContentArgToString(container);
		}

		// Write the contents to the file.
		this.performIO(fileId, contents);
		return null;
	}


	/**
	 * Write the specified contents to the specified file.
	 * (In WritelnXfci1Plugin, this method is overridden for modifying behaviour.)
	 * 
	 * @param fileId The ID of the file.
	 * @param contents The contents to be written to the file.
	 * @throws ConnectorException Thrown when any error has occurred when it is writing the contents to file.
	 */
	protected void performIO(int fileId, String[] contents) throws ConnectorException{
		this.fileIOHub.write(fileId, contents);
	}


	/**
	 * Converts data stored in the specified data-container implementing ArrayDataAccessorInterface1, to a String-type value.
	 * 
	 * @param container The data-container.
	 * @return The converted String-type value.
	 */
	protected String convertContentArgToString(ArrayDataAccessorInterface1<?> container) {
		if (container.getArrayRank() != ArrayDataAccessorInterface1.ARRAY_RANK_OF_SCALAR) {
			// The "contents" arg(s) is/are declared as a scalar(s) by getParameterClasses method,
			// so the scripting engine must store a scalar value in the (each) data container.
			throw new ConnectorFatalException("Expected a scalar but an array has been passed.");
		}

		// A scalar value is stored in arrayData[offset].
		Object arrayData = container.getArrayData();
		int offset = container.getArrayOffset();

		// Converts it to a String-type value, and return it.
		if (arrayData instanceof String[]) {
			return ((String[])arrayData)[offset];

		} else if (arrayData instanceof boolean[]) {
			boolean booelanValue = ((boolean[])arrayData)[offset];
			return Boolean.toString(booelanValue);

		} else if (arrayData instanceof long[]) {
			long longValue = ((long[])arrayData)[offset];
			return Long.toString(longValue);

		} else if (arrayData instanceof double[]) {
			double doubleValue = ((double[])arrayData)[offset];
			return Double.toString(doubleValue);

		} else {
			// Other types are not supported by Vnano.
			throw new ConnectorFatalException("Unexpected data-type: " + arrayData);
		}
	}
}
