package org.vcssl.nano.plugin.system.xvci1;

import org.vcssl.connect.ConnectorException;
import org.vcssl.connect.EngineConnectorInterface1;
import org.vcssl.connect.ExternalVariableConnectorInterface1;

public class EolXvci1Plugin implements ExternalVariableConnectorInterface1 {

	private String eol = System.getProperty("line.separator");

	@Override
	public String getVariableName() { return "EOL"; }
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
	public void initializeForExecution(Object engineConnector) throws ConnectorException {

		// 処理系の情報を取得するコネクタ（処理系依存）の互換性を検査
		if (!(engineConnector instanceof EngineConnectorInterface1)) {
			throw new ConnectorException(
				"The type of the engine connector \"" +
				engineConnector.getClass().getCanonicalName() +
				"\" is not supported by this plug-in."
			);
		}
		EngineConnectorInterface1 eci1Connector = (EngineConnectorInterface1)engineConnector;

		// 処理系のオプションから、環境におけるデフォルトの改行コードを取得
		if (eci1Connector.hasOptionValue("ENVIRONMENT_EOL")) {
			this.eol = (String)eci1Connector.getOptionValue("ENVIRONMENT_EOL");
		}
	}

	@Override
	public void finalizeForTermination(Object engineConnector) { }

	@Override
	public boolean isDataConversionNecessary() { return true; }

	@Override
	public Object getData() throws ConnectorException {
		return this.eol;
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
