/*
 * Author:  RINEARN (Fumihiro Matsui), 2022
 * License: CC0
 * Interface Specification:
 *     https://www.vcssl.org/en-us/doc/connect/ExternalFunctionConnectorInterface1_SPEC_ENGLISH
 */

package org.vcssl.nano.plugin.system.xfci1;

import org.vcssl.nano.plugin.system.file.FileIOHub;

import org.vcssl.connect.ConnectorException;


/**
 * A function plug-in providing "System.readln(int fileId)" function.
 */
public class ReadlnXfci1Plugin extends ReadXfci1Plugin {

	/**
	 * Create a new instance of this plug-in.
	 * 
	 * @param fileIOHub The file I/O hub, through which the file I/O will be performed.
	 */
	public ReadlnXfci1Plugin(FileIOHub fileIOHub) {
		super(fileIOHub);
	}


	@Override
	public String getFunctionName() {
		return "readln";
	}


	/**
	 * Read the specified contents to the specified file.
	 * (In ReadlnXfci1Plugin, this method is overridden for modifying behaviour.)
	 * 
	 * @param fileId The ID of the file.
	 * @return The contents read from the file.
	 * @throws ConnectorException Thrown when any error has occurred when it is reading the contents to file.
	 */
	@Override
	protected String[] performIO(int fileId) throws ConnectorException{
		return this.fileIOHub.readln(fileId);
	}
}
