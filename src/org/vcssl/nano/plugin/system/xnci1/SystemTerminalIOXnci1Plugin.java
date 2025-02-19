/*
 * Author:  RINEARN (Fumihiro Matsui), 2020-2021
 * License: CC0
 */

package org.vcssl.nano.plugin.system.xnci1;

import java.util.LinkedList;
import java.util.List;

import org.vcssl.connect.ConnectorException;
import org.vcssl.connect.EngineConnectorInterface1;
import org.vcssl.connect.ExternalFunctionConnectorInterface1;
import org.vcssl.connect.ExternalNamespaceConnectorInterface1;
import org.vcssl.connect.ExternalStructConnectorInterface1;
import org.vcssl.connect.ExternalVariableConnectorInterface1;
import org.vcssl.nano.plugin.system.terminal.TerminalWindow;
import org.vcssl.nano.plugin.system.xfci1.PrintXfci1Plugin;
import org.vcssl.nano.plugin.system.xfci1.PrintlnXfci1Plugin;

//Interface Specification: https://www.vcssl.org/en-us/dev/code/main-jimpl/api/org/vcssl/connect/ExternalNamespaceConnectorInterface1.html
//インターフェース仕様書:  https://www.vcssl.org/ja-jp/dev/code/main-jimpl/api/org/vcssl/connect/ExternalNamespaceConnectorInterface1.html

public class SystemTerminalIOXnci1Plugin implements ExternalNamespaceConnectorInterface1 {

	private TerminalWindow terminalWindow = null;
	private boolean isGuiMode = true;

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
		functionList.add( new PrintXfci1Plugin(this.isGuiMode, this.terminalWindow) );
		functionList.add( new PrintlnXfci1Plugin(this.isGuiMode, this.terminalWindow) );
		return functionList.toArray(new ExternalFunctionConnectorInterface1[0]);
	}

	@Override
	public ExternalVariableConnectorInterface1[] getVariables() {
		List<ExternalVariableConnectorInterface1> variableList = new LinkedList<ExternalVariableConnectorInterface1>();
		// variableList.add(...);
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

		// 処理系の情報を取得するコネクタ（処理系依存）の互換性を検査
		if (!(engineConnector instanceof EngineConnectorInterface1)) {
			throw new ConnectorException(
				"The type of the engine connector \"" +
				engineConnector.getClass().getCanonicalName() +
				"\" is not supported by this plug-in."
			);
		}
		EngineConnectorInterface1 eci1Connector = (EngineConnectorInterface1)engineConnector;

		// 処理系のUI設定がGUIかどうかを取得
		if (eci1Connector.hasOptionValue("UI_MODE")) {
			this.isGuiMode = ( (String)eci1Connector.getOptionValue("UI_MODE") ).equals("GUI");
		}

		// GUIモードならウィンドウを生成
		if (this.isGuiMode) {
			this.terminalWindow = new TerminalWindow();
			this.terminalWindow.init();
		}
	}

	@Override
	public void postInitializeForConnection(Object engineConnector) throws ConnectorException {
	}

	@Override
	public void preInitializeForExecution(Object engineConnector) throws ConnectorException {
		if (this.isGuiMode) {
			this.terminalWindow.reset();
		}
	}

	@Override
	public void postInitializeForExecution(Object engineConnector) throws ConnectorException {
	}

	@Override
	public void preFinalizeForTermination(Object engineConnector) throws ConnectorException {
	}

	@Override
	public void postFinalizeForTermination(Object engineConnector) throws ConnectorException {
	}

	@Override
	public void preFinalizeForDisconnection(Object engineConnector) throws ConnectorException {
	}

	@Override
	public void postFinalizeForDisconnection(Object engineConnector) throws ConnectorException {
		if (this.isGuiMode) {
			this.terminalWindow.dispose(true);
			this.terminalWindow = null;
		}
	}
}
