/*
 * Author:  RINEARN (Fumihiro Matsui), 2022
 * License: CC0
 * Interface Specification:
 *     https://www.vcssl.org/en-us/doc/connect/ExternalFunctionConnectorInterface1_SPEC_ENGLISH
 */

package org.vcssl.nano.plugin.system.xfci1;

import org.vcssl.nano.plugin.system.file.FileIOUnit;

import org.vcssl.connect.ConnectorException;
import org.vcssl.connect.ConnectorFatalException;
import org.vcssl.connect.ConnectorPermissionName;
import org.vcssl.connect.EngineConnectorInterface1;
import org.vcssl.connect.ExternalFunctionConnectorInterface1;
import org.vcssl.connect.ArrayDataAccessorInterface1;
import org.vcssl.connect.StringScalarDataAccessorInterface1;

import java.io.File;
import java.io.IOException;


/**
 * A function plug-in providing "System.save(string fileName, string value)" function.
 */
public class SaveXfci1Plugin implements ExternalFunctionConnectorInterface1 {

	/** Stores the engine connector for requesting permissions. */
	protected EngineConnectorInterface1 engineConnector = null;

	/** Stores the name of the default encoding. */
	private String defaultEncodingName = null;

	/** Stores the line feed code. */
	private String defaultLineFeedCode = null;

	/** Stores the directory in which the main script is. */
	private File mainDirectory;


	/**
	 * Create a new instance of this plug-in.
	 */
	public SaveXfci1Plugin() {
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
		
		// Get the character encoding from the option settings.
		if (this.engineConnector.hasOptionValue("FILE_IO_ENCODING")) {
			this.defaultEncodingName = String.class.cast(this.engineConnector.getOptionValue("FILE_IO_ENCODING"));
		} else {
			this.defaultEncodingName = "UTF-8";
		}

		// Get the line feed code from the option settings.
		if (this.engineConnector.hasOptionValue("FILE_IO_EOL")) {
			this.defaultLineFeedCode = String.class.cast(this.engineConnector.getOptionValue("FILE_IO_EOL"));
		} else {
			this.defaultLineFeedCode = System.getProperty("line.separator");
		}

		// Get the main directory from the option settings.
		String mainDirectoryPath = ".";
		if (this.engineConnector.hasOptionValue("MAIN_SCRIPT_DIRECTORY")) {
			mainDirectoryPath = String.class.cast(this.engineConnector.getOptionValue("MAIN_SCRIPT_DIRECTORY"));
		}
		this.mainDirectory = new File(mainDirectoryPath);
		try {
			this.mainDirectory = this.mainDirectory.getCanonicalFile();
		} catch (IOException ioe) {
		}
	}

	@Override
	public void finalizeForDisconnection(Object engineConnector) throws ConnectorException { }

	@Override
	public void finalizeForTermination(Object engineConnector) throws ConnectorException { }

	@Override
	public String getFunctionName() {
		return "save";
	}

	@Override
	public Class<?>[] getParameterClasses() {
		return new Class<?>[] { String.class, Object.class };
	}

	@Override
	public Class<?>[] getParameterUnconvertedClasses() {
		return new Class<?>[]{ StringScalarDataAccessorInterface1.class, ArrayDataAccessorInterface1.class };
	}

	@Override
	public boolean hasParameterNames() {
		return true;
	}

	@Override
	public String[] getParameterNames() {
		return new String[] { "fileName", "value" };
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
		return new boolean[]{ false, false };
	}

	@Override
	public boolean[] getParameterConstantnesses() {
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
		return void.class;
	}

	@Override
	public Class<?> getReturnUnconvertedClass(Class<?>[] parameterClasses) {
		return null;
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
		//    arguments[0] is the container for storing the return value (unused), 
		//    argument[1] is the "fileName" arg,
		//    argument[2] is the "value" arg.

		// Get the value of the specified file name or path.
		String fileName = StringScalarDataAccessorInterface1.class.cast(arguments[1]).getStringScalarData();
		String filePath = fileName;
		File file = new File(filePath);
		if (!file.isAbsolute()) {
			file = new File(this.mainDirectory, fileName);
			try {
				file = file.getCanonicalFile();
			} catch (IOException ioe) {
				throw new ConnectorFatalException(ioe);
			}
			filePath = file.getPath();
		}

		// Request permissions.
		this.engineConnector.requestPermission(ConnectorPermissionName.FILE_WRITE, this, filePath);
		if (file.exists()) {
			this.engineConnector.requestPermission(ConnectorPermissionName.FILE_OVERWRITE, this, filePath);
		} else {
			this.engineConnector.requestPermission(ConnectorPermissionName.FILE_CREATE, this, filePath);
		}

		// Convert the value of "value" arg to a String-type value.
		ArrayDataAccessorInterface1<?> valueContainer = ArrayDataAccessorInterface1.class.cast(arguments[2]);
		String valueString = this.convertContentArgToString(valueContainer);

		// Write the value to the specified file.
		FileIOUnit ioUnit = new FileIOUnit();
		ioUnit.open(filePath, "w", this.defaultEncodingName, this.defaultLineFeedCode);
		ioUnit.write(new String[]{ valueString });
		ioUnit.close();

		return null;
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
