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
import org.vcssl.nano.plugin.system.xvci1.CrXvci1Plugin;
import org.vcssl.nano.plugin.system.xvci1.EolXvci1Plugin;
import org.vcssl.nano.plugin.system.xvci1.LfXvci1Plugin;

//Interface Specification: https://www.vcssl.org/en-us/dev/code/main-jimpl/api/org/vcssl/connect/ExternalNamespaceConnectorInterface1.html
//インターフェース仕様書:  https://www.vcssl.org/ja-jp/dev/code/main-jimpl/api/org/vcssl/connect/ExternalNamespaceConnectorInterface1.html

public class SystemEnvironmentXnci1Plugin implements ExternalNamespaceConnectorInterface1 {

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
		// functionList.add(...);
		return functionList.toArray(new ExternalFunctionConnectorInterface1[0]);
	}

	@Override
	public ExternalVariableConnectorInterface1[] getVariables() {
		List<ExternalVariableConnectorInterface1> variableList = new LinkedList<ExternalVariableConnectorInterface1>();
		variableList.add(new EolXvci1Plugin());
		variableList.add(new LfXvci1Plugin());
		variableList.add(new CrXvci1Plugin());
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
	public void preInitializeForConnection(Object engineConnector) throws ConnectorException { }

	@Override
	public void postInitializeForConnection(Object engineConnector) throws ConnectorException { }

	@Override
	public void preInitializeForExecution(Object engineConnector) throws ConnectorException { }

	@Override
	public void postInitializeForExecution(Object engineConnector) throws ConnectorException { }

	@Override
	public void preFinalizeForTermination(Object engineConnector) throws ConnectorException { }

	@Override
	public void postFinalizeForTermination(Object engineConnector) throws ConnectorException { }

	@Override
	public void preFinalizeForDisconnection(Object engineConnector) throws ConnectorException { }

	@Override
	public void postFinalizeForDisconnection(Object engineConnector) throws ConnectorException { }
}
