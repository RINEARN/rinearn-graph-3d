/*
 * Author:  RINEARN (Fumihiro Matsui), 2022
 * License: CC0
 * Interface Specification:
 *     https://www.vcssl.org/en-us/doc/connect/ExternalFunctionConnectorInterface1_SPEC_ENGLISH
 */

package org.vcssl.nano.plugin.system.xfci1;

import org.vcssl.connect.StringScalarDataAccessorInterface1;
import org.vcssl.connect.BoolScalarDataAccessorInterface1;
import org.vcssl.connect.ConnectorException;
import org.vcssl.connect.ConnectorFatalException;
import org.vcssl.connect.ConnectorPermissionName;
import org.vcssl.connect.EngineConnectorInterface1;
import org.vcssl.connect.ExternalFunctionConnectorInterface1;

import java.io.File;
import java.io.IOException;
import java.util.Locale;


/**
 * A function plug-in providing "System.mkdir(string directoryPath)" function.
 */
public class MkdirXfci1Plugin implements ExternalFunctionConnectorInterface1 {

	/** Stores the engine connector for requesting permissions. */
	protected EngineConnectorInterface1 engineConnector = null;

	/** Stores the directory in which the main script is. */
	private File mainDirectory;


	/**
	 * Create a new instance of this plug-in.
	 */
	public MkdirXfci1Plugin() {
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
		return "mkdir";
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
		return void.class;
	}

	@Override
	public Class<?> getReturnUnconvertedClass(Class<?>[] parameterClasses) {
		return BoolScalarDataAccessorInterface1.class;
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
		//    argument[1] is the "directoryPath" arg.

		// Get the contaners of the arg values.
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

		File parentDir = directoryFile.getParentFile();

		// If the directory already exists, do nothing.
		if (directoryFile.exists()) {
			return null;
		}

		// If the parent directory does not exist: error.
		if (!parentDir.exists()) {

			// Detect the language for displaying the error message.
			Locale locale = Locale.getDefault();
			if (this.engineConnector.hasOptionValue("LOCALE")) {
				locale = Locale.class.cast(this.engineConnector.getOptionValue("LOCALE"));
			}
			boolean isJapanese =
				( locale.getLanguage()!=null && locale.getLanguage().equals("ja") )
				||
				( locale.getCountry()!=null && locale.getCountry().equals("JP") );

			if (isJapanese) {
				throw new ConnectorException("フォルダを作成しようとしましたが、親フォルダが存在しないため、作成できません： " + directoryFile.getPath());
			} else {
				throw new ConnectorException("Tried to create the directory, but failed to create it because the parent directory does not exist: " + directoryFile.getPath());
			}
		}

		// Request the permission for creating the directory.
		this.engineConnector.requestPermission(ConnectorPermissionName.DIRECTORY_CREATE, this, parentDir.getPath());		

		// Create the directory.
		directoryFile.mkdir();

		return null;
	}
}
