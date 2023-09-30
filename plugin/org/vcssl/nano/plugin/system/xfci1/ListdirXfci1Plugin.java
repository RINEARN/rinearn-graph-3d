/*
 * Author:  RINEARN (Fumihiro Matsui), 2022
 * License: CC0
 * Interface Specification:
 *     https://www.vcssl.org/en-us/doc/connect/ExternalFunctionConnectorInterface1_SPEC_ENGLISH
 */

package org.vcssl.nano.plugin.system.xfci1;

import org.vcssl.connect.StringScalarDataAccessorInterface1;
import org.vcssl.connect.ArrayDataAccessorInterface1;
import org.vcssl.connect.EngineConnectorInterface1;
import org.vcssl.connect.ExternalFunctionConnectorInterface1;
import org.vcssl.connect.ConnectorPermissionName;
import org.vcssl.connect.ConnectorFatalException;
import org.vcssl.connect.ConnectorException;

import java.io.File;
import java.io.IOException;
import java.util.Locale;


/**
 * A function plug-in providing "System.listdir(string directoryPath)" function.
 */
public class ListdirXfci1Plugin implements ExternalFunctionConnectorInterface1 {

	/** Stores the engine connector for requesting permissions. */
	protected EngineConnectorInterface1 engineConnector = null;

	/** Stores the directory in which the main script is. */
	private File mainDirectory;


	/**
	 * Create a new instance of this plug-in.
	 */
	public ListdirXfci1Plugin() {
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
		return "listdir";
	}

	@Override
	public Class<?>[] getParameterClasses() {
		return new Class<?>[] { String.class };
	}

	@Override
	public Class<?>[] getParameterUnconvertedClasses() {
		return new Class<?>[] { StringScalarDataAccessorInterface1.class };
	}

	@Override
	public boolean hasParameterNames() {
		return true;
	}

	@Override
	public String[] getParameterNames() {
		return new String[] { "directoryPath" };
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
		//    argument[1] is the "directoryPath" arg.

		// Get the contaners of the arg/return values.
		@SuppressWarnings("unchecked")
		ArrayDataAccessorInterface1<String[]> returnContainer = (ArrayDataAccessorInterface1<String[]>)arguments[0];
		StringScalarDataAccessorInterface1 directoryPathContainer = StringScalarDataAccessorInterface1.class.cast(arguments[1]);

		// Get the file specified as the argument, and its parent directory.
		String directoryPath = directoryPathContainer.getStringScalarData();
		File directoryFile = new File(directoryPath);
		if (!directoryFile.isAbsolute()) {
			directoryFile = new File(this.mainDirectory, directoryPath);
			try {
				directoryFile = directoryFile.getCanonicalFile();
			} catch (IOException ioe) {
				throw new ConnectorFatalException(ioe);
			}
			directoryPath = directoryFile.getPath();
		}

		// Request the permission for getting the list of files in the specified directory.
		this.engineConnector.requestPermission(ConnectorPermissionName.DIRECTORY_LIST, this, directoryFile.getPath());

		// If no directory exists at the specified path: error.
		if (!directoryFile.exists() || !directoryFile.isDirectory()) {

			// Detect the language for displaying the error message.
			Locale locale = Locale.getDefault();
			if (this.engineConnector.hasOptionValue("LOCALE")) {
				locale = Locale.class.cast(this.engineConnector.getOptionValue("LOCALE"));
			}
			boolean isJapanese =
				( locale.getLanguage()!=null && locale.getLanguage().equals("ja") )
				||
				( locale.getCountry()!=null && locale.getCountry().equals("JP") );

			if (!directoryFile.exists()) {
				if (isJapanese) {
					throw new ConnectorException("指定されたパスに、フォルダやファイルが存在しません： " + directoryFile.getPath());
				} else {
					throw new ConnectorException("No directory or file exists at the specified path: " + directoryFile.getPath());
				}

			} else if (!directoryFile.isDirectory()) {
				if (isJapanese) {
					throw new ConnectorException("指定されたパスにあるファイルは、フォルダではありません： " + directoryFile.getPath());
				} else {
					throw new ConnectorException("The file at the specified path is not a directory: " + directoryFile.getPath());
				}
			}
		}

		// Get the list of files in the directory.
		String[] elementFileNames = directoryFile.list();
		if (elementFileNames == null) {
			throw new ConnectorFatalException("Failed to get the list of files in the directory: " + directoryFile.getPath());
		}

		// Store the read contents to the above container.
		int offset = 0;
		int[] lengths = new int[]{ elementFileNames.length };
		returnContainer.setArrayData(elementFileNames, offset, lengths);
		return null;
	}
}
