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
 * A function plug-in providing "System.writeln(int fileId, string content)" function.
 */
public class WritelnXfci1Plugin extends WriteXfci1Plugin {

	/**
	 * Create a new instance of this plug-in.
	 * 
	 * @param fileIOHub The file I/O hub, through which the file I/O will be performed.
	 */
	public WritelnXfci1Plugin(FileIOHub fileIOHub) {
		super(fileIOHub);
	}


	@Override
	public String getFunctionName() {
		return "writeln";
	}


	/**
	 * Write the specified contents to the specified file, and go to the next line.
	 * (This method called from WriteXfci1Plugin.invoke(...) method.)
	 * 
	 * @param fileId The ID of the file.
	 * @param contents The contents to be written to the file.
	 * @throws ConnectorException Thrown when any error has occurred when it writing the contents to file.
	 */
	@Override
	protected void performIO(int fileId, String[] contents) throws ConnectorException{
		this.fileIOHub.writeln(fileId, contents);
	}
}
