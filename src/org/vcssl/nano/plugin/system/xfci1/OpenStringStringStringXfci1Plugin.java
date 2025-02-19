/*
 * Author:  RINEARN (Fumihiro Matsui), 2022
 * License: CC0
 * Interface Specification:
 *     https://www.vcssl.org/en-us/doc/connect/ExternalFunctionConnectorInterface1_SPEC_ENGLISH
 */

package org.vcssl.nano.plugin.system.xfci1;

import org.vcssl.nano.plugin.system.file.FileIOHub;
import org.vcssl.nano.plugin.system.file.FileIOMode;

import org.vcssl.connect.ConnectorException;
import org.vcssl.connect.ConnectorFatalException;
import org.vcssl.connect.ConnectorPermissionName;
import org.vcssl.connect.EngineConnectorInterface1;
import org.vcssl.connect.ExternalFunctionConnectorInterface1;

import java.io.File;
import java.io.IOException;
import java.util.Locale;


/**
 * A function plug-in providing "System.open(string fileName, string mode, string encoding)" function.
 */
public class OpenStringStringStringXfci1Plugin implements ExternalFunctionConnectorInterface1 {

	/** An object managing file I/O from/to (multiple) files. */
	protected FileIOHub fileIOHub = null;

	/** Stores the engine connector for requesting permissions. */
	protected EngineConnectorInterface1 engineConnector = null;

	/** Stores the directory in which the main script is. */
	private File mainDirectory;


	/**
	 * Create a new instance of this plug-in.
	 * 
	 * @param fileIOHub The file I/O hub, through which the file I/O will be performed.
	 */
	public OpenStringStringStringXfci1Plugin(FileIOHub fileIOHub) {
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
		return "open";
	}

	@Override
	public Class<?>[] getParameterClasses() {
		return new Class<?>[] { String.class, String.class, String.class };
	}

	@Override
	public Class<?>[] getParameterUnconvertedClasses() {
		return null;
	}

	@Override
	public boolean hasParameterNames() {
		return true;
	}

	@Override
	public String[] getParameterNames() {
		return new String[] { "fileName", "fileIOMode", "encodingName" };
	}

	@Override
	public boolean[] getParameterDataTypeArbitrarinesses() {
		return new boolean[]{ false, false, false };
	}

	@Override
	public boolean[] getParameterArrayRankArbitrarinesses() {
		return new boolean[]{ false, false, false };
	}

	@Override
	public boolean[] getParameterReferencenesses() {
		return new boolean[]{ false, false, false };
	}

	@Override
	public boolean[] getParameterConstantnesses() {
		return new boolean[]{ true, true, true };
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
		return true;
	}

	@Override
	public Object invoke(Object[] arguments) throws ConnectorException {
		String fileName = String.class.cast(arguments[0]);
		String fileIOModeSpecifier = String.class.cast(arguments[1]);
		String encodingName = String.class.cast(arguments[2]);

		// Resolve the specified file name/path to the absolute path.
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

		// Open the file, and return the file ID assigned to it.
		int fileId = this.open(filePath, fileIOModeSpecifier, encodingName);
		return Long.valueOf(fileId);
	}


	/**
	 * Open the specified file, and returns the file ID assigned to it.
	 * 
	 * @param fileName The file to be opened.
	 * @param fileIOModeSpecifier The string for specifying the file I/O mode (e.g.: "w" for WRITE, "r" for READ, and so on).
	 * @param encodingName The name of the character encoding of the file.
	 * @return The file ID assigned to the opened file.
	 * @throws ConnectorException Thrown when any error has occurred for the file opening process.
	 */
	protected int open(String fileName, String fileIOModeSpecifier, String encodingName) throws ConnectorException {

		// Check that the specified file I/O mode is valid, and then convert it to an element of FileIOMode enum.
		FileIOMode fileIOMode = null;
		if (FileIOMode.SPECIFIER_ENUM_MAP.containsKey(fileIOModeSpecifier)) {
			fileIOMode = FileIOMode.SPECIFIER_ENUM_MAP.get(fileIOModeSpecifier);

		// If invalid file I/O mode is specified, throw an exception.
		} else {

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
				throw new ConnectorException("指定されたファイルI/Oモードはサポートされていません： " + fileIOModeSpecifier);
			} else {
				throw new ConnectorException("The specified file I/O mode is unsupported: " + fileIOModeSpecifier);
			}
		}

		// Get the line-feed code from the option.
		String lineFeedCode = System.getProperty("line.separator");
		if (this.engineConnector.hasOptionValue("FILE_IO_EOL")) {
			lineFeedCode = String.class.cast(this.engineConnector.getOptionValue("FILE_IO_EOL"));
		}

		// Resolve the specified file name/path to the absolute path.
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

		// Request permissions for reading contents from the file.
		if (FileIOMode.READ_MODE_SET.contains(fileIOMode)) {
			this.engineConnector.requestPermission(ConnectorPermissionName.FILE_READ, this, filePath);

		// Request permissions for writing/appending contents to the file.
		} else if (FileIOMode.WRITE_MODE_SET.contains(fileIOMode) || FileIOMode.APPEND_MODE_SET.contains(fileIOMode)) {
			this.engineConnector.requestPermission(ConnectorPermissionName.FILE_WRITE, this, filePath);
			if (file.exists()) {
				this.engineConnector.requestPermission(ConnectorPermissionName.FILE_OVERWRITE, this, filePath);
			} else {
				this.engineConnector.requestPermission(ConnectorPermissionName.FILE_CREATE, this, filePath);
			}

		// Throw an exception when an unexpected file I/O mode is specified.
		} else {

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
				throw new ConnectorException("指定されたファイルI/Oモードはサポートされていません： " + fileIOMode);
			} else {
				throw new ConnectorException("The specified file I/O mode is unsupported: " + fileIOMode);
			}
		}

		// Open the file, and return the file ID assigned to it.
		int fileId = this.fileIOHub.open(filePath, fileIOModeSpecifier, encodingName, lineFeedCode);
		return fileId;
	}
}
