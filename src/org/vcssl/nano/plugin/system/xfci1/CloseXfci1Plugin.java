/*
 * Author:  RINEARN (Fumihiro Matsui), 2022
 * License: CC0
 * Interface Specification:
 *     https://www.vcssl.org/en-us/doc/connect/ExternalFunctionConnectorInterface1_SPEC_ENGLISH
 */

package org.vcssl.nano.plugin.system.xfci1;

import org.vcssl.nano.plugin.system.file.FileIOHub;
// import org.vcssl.nano.plugin.system.file.FileIOMode;

import org.vcssl.connect.ConnectorException;
// import org.vcssl.connect.ConnectorPermissionName;
import org.vcssl.connect.EngineConnectorInterface1;
import org.vcssl.connect.ExternalFunctionConnectorInterface1;

// import java.io.File;
// import java.io.IOException;
// import java.rmi.ConnectIOException;
// import java.util.Locale;


/**
 * A function plug-in providing "System.close(int fileId)" function.
 */
public class CloseXfci1Plugin implements ExternalFunctionConnectorInterface1 {

	/** An object managing file I/O from/to (multiple) files. */
	protected FileIOHub fileIOHub = null;

	/** Stores the engine connector for requesting permissions. */
	protected EngineConnectorInterface1 engineConnector = null;

	/**
	 * Create a new instance of this plug-in.
	 *
	 * @param fileIOHub The file I/O hub, through which the file I/O will be performed.
	 */
	public CloseXfci1Plugin(FileIOHub fileIOHub) {
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
		return "close";
	}

	@Override
	public Class<?>[] getParameterClasses() {
		return new Class<?>[] { long.class };
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
		return true;
	}

	@Override
	public Object invoke(Object[] arguments) throws ConnectorException {
		int fileId = Long.class.cast(arguments[0]).intValue();
		this.fileIOHub.close(fileId);
		return null;
	}
}
