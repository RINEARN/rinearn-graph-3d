/*
 * Author:  RINEARN (Fumihiro Matsui), 2022
 * License: CC0
 * Interface Specification:
 *     https://www.vcssl.org/en-us/doc/connect/ExternalNamespaceConnectorInterface1_SPEC_ENGLISH
 */

package org.vcssl.nano.plugin.system.xnci1;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.vcssl.connect.ConnectorException;
import org.vcssl.connect.EngineConnectorInterface1;
import org.vcssl.connect.ExternalFunctionConnectorInterface1;
import org.vcssl.connect.ExternalNamespaceConnectorInterface1;
import org.vcssl.connect.ExternalStructConnectorInterface1;
import org.vcssl.connect.ExternalVariableConnectorInterface1;

import org.vcssl.nano.plugin.system.file.FileIOHub;

import org.vcssl.nano.plugin.system.xvci1.WriteXvci1Plugin;
import org.vcssl.nano.plugin.system.xvci1.ReadXvci1Plugin;
import org.vcssl.nano.plugin.system.xvci1.AppendXvci1Plugin;
import org.vcssl.nano.plugin.system.xvci1.WriteTsvXvci1Plugin;
import org.vcssl.nano.plugin.system.xvci1.ReadTsvXvci1Plugin;
import org.vcssl.nano.plugin.system.xvci1.AppendTsvXvci1Plugin;
import org.vcssl.nano.plugin.system.xvci1.WriteCsvXvci1Plugin;
import org.vcssl.nano.plugin.system.xvci1.ReadCsvXvci1Plugin;
import org.vcssl.nano.plugin.system.xvci1.AppendCsvXvci1Plugin;
import org.vcssl.nano.plugin.system.xvci1.ReadStsvXvci1Plugin;

import org.vcssl.nano.plugin.system.xfci1.OpenStringStringXfci1Plugin;
import org.vcssl.nano.plugin.system.xfci1.OpenStringStringStringXfci1Plugin;
import org.vcssl.nano.plugin.system.xfci1.CloseXfci1Plugin;
import org.vcssl.nano.plugin.system.xfci1.WriteXfci1Plugin;
import org.vcssl.nano.plugin.system.xfci1.WritelnXfci1Plugin;
import org.vcssl.nano.plugin.system.xfci1.FlushXfci1Plugin;
import org.vcssl.nano.plugin.system.xfci1.ReadXfci1Plugin;
import org.vcssl.nano.plugin.system.xfci1.ReadlnXfci1Plugin;
import org.vcssl.nano.plugin.system.xfci1.CountlnXfci1Plugin;
import org.vcssl.nano.plugin.system.xfci1.ExistsXfci1Plugin;
import org.vcssl.nano.plugin.system.xfci1.IsdirXfci1Plugin;
import org.vcssl.nano.plugin.system.xfci1.ListdirXfci1Plugin;
import org.vcssl.nano.plugin.system.xfci1.MkdirXfci1Plugin;
import org.vcssl.nano.plugin.system.xfci1.SaveXfci1Plugin;
import org.vcssl.nano.plugin.system.xfci1.LoadXfci1Plugin;


/**
 * A namespace plug-in providing file I/O functions, and parameter variables related to file I/O.
 */
public class SystemFileIOXnci1Plugin implements ExternalNamespaceConnectorInterface1 {

	/** An object managing file I/O from/to (multiple) files. */
	private FileIOHub fileIOHub = null;


	/**
	 * Create a new instance of this plug-in.
	 */
	public SystemFileIOXnci1Plugin() {
		
		// Create an object managing file I/O from/to (multiple) files: File I/O hub.
		this.fileIOHub = new FileIOHub();
	}


	@Override
	public String getNamespaceName() {
		return "System";
	}

	@Override
	public boolean isMandatoryToAccessMembers() {
		return false;
	}

	@Override
	public ExternalFunctionConnectorInterface1[] getFunctions() {
		List<ExternalFunctionConnectorInterface1> functionList = new LinkedList<ExternalFunctionConnectorInterface1>();
		functionList.add(new OpenStringStringXfci1Plugin(this.fileIOHub));
		functionList.add(new OpenStringStringStringXfci1Plugin(this.fileIOHub));
		functionList.add(new CloseXfci1Plugin(this.fileIOHub));
		functionList.add(new WriteXfci1Plugin(this.fileIOHub));
		functionList.add(new WritelnXfci1Plugin(this.fileIOHub));
		functionList.add(new FlushXfci1Plugin(this.fileIOHub));
		functionList.add(new ReadXfci1Plugin(this.fileIOHub));
		functionList.add(new ReadlnXfci1Plugin(this.fileIOHub));
		functionList.add(new CountlnXfci1Plugin(this.fileIOHub));
		functionList.add(new ExistsXfci1Plugin());
		functionList.add(new IsdirXfci1Plugin());
		functionList.add(new ListdirXfci1Plugin());
		functionList.add(new MkdirXfci1Plugin());
		functionList.add(new SaveXfci1Plugin());
		functionList.add(new LoadXfci1Plugin());
		return functionList.toArray(new ExternalFunctionConnectorInterface1[0]);
	}

	@Override
	public ExternalVariableConnectorInterface1[] getVariables() {
		List<ExternalVariableConnectorInterface1> variableList = new LinkedList<ExternalVariableConnectorInterface1>();
		variableList.add(new WriteXvci1Plugin());
		variableList.add(new ReadXvci1Plugin());
		variableList.add(new AppendXvci1Plugin());
		variableList.add(new WriteTsvXvci1Plugin());
		variableList.add(new ReadTsvXvci1Plugin());
		variableList.add(new AppendTsvXvci1Plugin());
		variableList.add(new WriteCsvXvci1Plugin());
		variableList.add(new ReadCsvXvci1Plugin());
		variableList.add(new AppendCsvXvci1Plugin());
		variableList.add(new ReadStsvXvci1Plugin());
		return variableList.toArray(new ExternalVariableConnectorInterface1[0]);
	}

	@Override
	public ExternalStructConnectorInterface1[] getStructs() {
		return new ExternalStructConnectorInterface1[0];
	}

	@Override
	public Class<?> getEngineConnectorClass() {
		return EngineConnectorInterface1.class;
	}

	@Override
	public void preInitializeForConnection(Object engineConnector) throws ConnectorException {
	}

	@Override
	public void postInitializeForConnection(Object engineConnector) throws ConnectorException {
	}

	@Override
	public void preInitializeForExecution(Object engineConnector) throws ConnectorException {
		
		// Set the language of error messages.
		EngineConnectorInterface1 eci1EngineConnector = EngineConnectorInterface1.class.cast(engineConnector);
		if (eci1EngineConnector.hasOptionValue("LOCALE")) {
			Locale locale = Locale.class.cast(eci1EngineConnector.getOptionValue("LOCALE"));
			this.fileIOHub.setLocale(locale);
		}

		// Initialize resources for performing file I/O.
		this.fileIOHub.initializeResources();
	}

	@Override
	public void postInitializeForExecution(Object engineConnector) throws ConnectorException {
	}

	@Override
	public void preFinalizeForTermination(Object engineConnector) throws ConnectorException {
	}

	@Override
	public void postFinalizeForTermination(Object engineConnector) throws ConnectorException {

		// Dispose resources for performing file I/O.
		this.fileIOHub.disposeResources();
	}

	@Override
	public void preFinalizeForDisconnection(Object engineConnector) throws ConnectorException {
	}

	@Override
	public void postFinalizeForDisconnection(Object engineConnector) throws ConnectorException {
	}
}
