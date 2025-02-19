package org.vcssl.nano.plugin.system.xvci1;

import org.vcssl.connect.ConnectorException;
import org.vcssl.connect.EngineConnectorInterface1;
import org.vcssl.connect.ExternalVariableConnectorInterface1;

public class AppendXvci1Plugin implements ExternalVariableConnectorInterface1 {

	@Override
	public String getVariableName() { return "APPEND"; }
	@Override
	public Class<?> getDataClass() { return String.class; }
	@Override
	public Class<?> getDataUnconvertedClass() { return null; }
	@Override
	public boolean isDataTypeArbitrary() { return false; }
	@Override
	public boolean isArrayRankArbitrary() { return false; }
	@Override
	public boolean isConstant() { return true; }
	@Override
	public boolean isReference() { return false; }
	@Override
	public Class<?> getEngineConnectorClass() { return EngineConnectorInterface1.class; }
	@Override
	public void initializeForConnection(Object engineConnector) { }
	@Override
	public void finalizeForDisconnection(Object engineConnector) { }
	@Override
	public void initializeForExecution(Object engineConnector) { }
	@Override
	public void finalizeForTermination(Object engineConnector) { }

	@Override
	public boolean isDataConversionNecessary() { return true; }

	@Override
	public Object getData() throws ConnectorException {
		return "a";
	}

	@Override
	public void getData(Object dataContainer) throws ConnectorException {
		// This method is for the case of the data conversion is disabled.
	}

	@Override
	public void setData(Object data) throws ConnectorException {
		throw new ConnectorException("\"" + getVariableName() + "\" is a constant, so this value shoud not be changed.");
	}
}
