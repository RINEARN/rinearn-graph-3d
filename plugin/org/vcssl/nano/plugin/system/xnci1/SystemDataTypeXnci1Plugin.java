/*
 * Author:  RINEARN (Fumihiro Matsui), 2020-2022
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
import org.vcssl.nano.plugin.system.xfci1.InfXfci1Plugin;
import org.vcssl.nano.plugin.system.xfci1.LengthXfci1Plugin;
import org.vcssl.nano.plugin.system.xfci1.NanXfci1Plugin;
import org.vcssl.nano.plugin.system.xfci1.ArrayrankXfci1Plugin;
import org.vcssl.nano.plugin.system.xfci1.StringRoundXfci1Plugin;
import org.vcssl.nano.plugin.system.xfci1.FloatRoundXfci1Plugin;
import org.vcssl.nano.plugin.system.xvci1.FloatMaxXvci1Plugin;
import org.vcssl.nano.plugin.system.xvci1.FloatMinAbsDenormalXvci1Plugin;
import org.vcssl.nano.plugin.system.xvci1.FloatMinAbsNormalXvci1Plugin;
import org.vcssl.nano.plugin.system.xvci1.InfXvci1Plugin;
import org.vcssl.nano.plugin.system.xvci1.IntMaxXvci1Plugin;
import org.vcssl.nano.plugin.system.xvci1.IntMinXvci1Plugin;
import org.vcssl.nano.plugin.system.xvci1.NanXvci1Plugin;
import org.vcssl.nano.plugin.system.xvci1.UpXvci1Plugin;
import org.vcssl.nano.plugin.system.xvci1.UpSignifXvci1Plugin;
import org.vcssl.nano.plugin.system.xvci1.DownXvci1Plugin;
import org.vcssl.nano.plugin.system.xvci1.DownSignifXvci1Plugin;
import org.vcssl.nano.plugin.system.xvci1.HalfUpXvci1Plugin;
import org.vcssl.nano.plugin.system.xvci1.HalfUpSignifXvci1Plugin;
import org.vcssl.nano.plugin.system.xvci1.HalfDownXvci1Plugin;
import org.vcssl.nano.plugin.system.xvci1.HalfDownSignifXvci1Plugin;
import org.vcssl.nano.plugin.system.xvci1.HalfToEvenXvci1Plugin;
import org.vcssl.nano.plugin.system.xvci1.HalfToEvenSignifXvci1Plugin;

public class SystemDataTypeXnci1Plugin implements ExternalNamespaceConnectorInterface1 {

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
		functionList.add(new NanXfci1Plugin());
		functionList.add(new InfXfci1Plugin());
		functionList.add(new LengthXfci1Plugin());
		functionList.add(new ArrayrankXfci1Plugin());
		functionList.add(new FloatRoundXfci1Plugin());
		functionList.add(new StringRoundXfci1Plugin());
		return functionList.toArray(new ExternalFunctionConnectorInterface1[0]);
	}

	@Override
	public ExternalVariableConnectorInterface1[] getVariables() {
		List<ExternalVariableConnectorInterface1> variableList = new LinkedList<ExternalVariableConnectorInterface1>();
		variableList.add(new NanXvci1Plugin());
		variableList.add(new InfXvci1Plugin());
		variableList.add(new IntMaxXvci1Plugin());
		variableList.add(new IntMinXvci1Plugin());
		variableList.add(new FloatMaxXvci1Plugin());
		variableList.add(new FloatMinAbsNormalXvci1Plugin());
		variableList.add(new FloatMinAbsDenormalXvci1Plugin());
		variableList.add(new UpXvci1Plugin());
		variableList.add(new UpSignifXvci1Plugin());
		variableList.add(new DownXvci1Plugin());
		variableList.add(new DownSignifXvci1Plugin());
		variableList.add(new HalfUpXvci1Plugin());
		variableList.add(new HalfUpSignifXvci1Plugin());
		variableList.add(new HalfDownXvci1Plugin());
		variableList.add(new HalfDownSignifXvci1Plugin());
		variableList.add(new HalfToEvenXvci1Plugin());
		variableList.add(new HalfToEvenSignifXvci1Plugin());
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
