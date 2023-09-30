/*
 * Author:  RINEARN (Fumihiro Matsui), 2022
 * License: CC0
 * Interface Specification:
 *     https://www.vcssl.org/en-us/doc/connect/ExternalFunctionConnectorInterface1_SPEC_ENGLISH
 */

package org.vcssl.nano.plugin.system.xfci1;

import org.vcssl.nano.plugin.system.file.FileIOHub;

import org.vcssl.connect.ConnectorException;
import org.vcssl.connect.Int64ScalarDataAccessorInterface1;
import org.vcssl.connect.ArrayDataAccessorInterface1;
import org.vcssl.connect.EngineConnectorInterface1;
import org.vcssl.connect.ExternalFunctionConnectorInterface1;


/**
 * A function plug-in providing "System.read(int fileId)" function.
 */
public class ReadXfci1Plugin implements ExternalFunctionConnectorInterface1 {

	/** An object managing file I/O from/to (multiple) files. */
	protected FileIOHub fileIOHub = null;

	/** Stores the engine connector for requesting permissions. */
	protected EngineConnectorInterface1 engineConnector = null;

	/**
	 * Create a new instance of this plug-in.
	 * 
	 * @param fileIOHub The file I/O hub, through which the file I/O will be performed.
	 */
	public ReadXfci1Plugin(FileIOHub fileIOHub) {
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
		return "read";
	}

	@Override
	public Class<?>[] getParameterClasses() {
		return new Class<?>[] { long.class };
	}

	@Override
	public Class<?>[] getParameterUnconvertedClasses() {
		return new Class<?>[] { Int64ScalarDataAccessorInterface1.class };
	}

	@Override
	public boolean hasParameterNames() {
		return true;
	}

	@Override
	public String[] getParameterNames() {
		return new String[] { "fileId" };
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
	public boolean[] getParameterReferencenesses() {
		return new boolean[]{ false };
	}

	@Override
	public boolean[] getParameterConstantnesses() {
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
		return String[].class;
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

		// Note:
		//    arguments[0] is the container for storing the return value, 
		//    argument[1] is the "fileId" arg.

		// Get the value of the specified fileId.
		int fileId = (int)Int64ScalarDataAccessorInterface1.class.cast(arguments[1]).getInt64ScalarData();

		// Read the contents of one line, from the file.
		String[] contents = this.performIO(fileId);

		// Get the container for storing the return value.
		@SuppressWarnings("unchecked")
		ArrayDataAccessorInterface1<String[]> returnContainer = (ArrayDataAccessorInterface1<String[]>)arguments[0];

		// Store the read contents to the above container.
		int offset = 0;
		int[] lengths = new int[]{ contents.length };
		returnContainer.setArrayData((String[])contents, offset, lengths);
		return null;
	}


	/**
	 * Read the specified contents to the specified file.
	 * (In ReadlnXfci1Plugin, this method is overridden for modifying behaviour.)
	 * 
	 * @param fileId The ID of the file.
	 * @return The contents read from the file.
	 * @throws ConnectorException Thrown when any error has occurred when it is reading the contents to file.
	 */
	protected String[] performIO(int fileId) throws ConnectorException{
		return this.fileIOHub.read(fileId);
	}
}
