package org.vcssl.nano.plugin.math.xnci1;

import java.util.LinkedList;
import java.util.List;

import org.vcssl.connect.ConnectorException;
import org.vcssl.connect.EngineConnectorInterface1;
import org.vcssl.connect.ExternalFunctionConnectorInterface1;
import org.vcssl.connect.ExternalNamespaceConnectorInterface1;
import org.vcssl.connect.ExternalStructConnectorInterface1;
import org.vcssl.connect.ExternalVariableConnectorInterface1;

import org.vcssl.nano.plugin.math.xfci1.MeanXfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.Sdn1Xfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.SdnXfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.SumXfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.Van1Xfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.VanXfci1Plugin;

import org.vcssl.nano.plugin.math.xfci1.VectorSumXfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.VectorMeanXfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.VectorVanXfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.VectorVan1Xfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.VectorSdnXfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.VectorSdn1Xfci1Plugin;

public class MathStatisticalXnci1Plugin implements ExternalNamespaceConnectorInterface1 {

	@Override
	public String getNamespaceName() {
		return "math.StatisticalFunction";
	}

	@Override
	public boolean isMandatoryToAccessMembers() {
		return false;
	}

	@Override
	public ExternalFunctionConnectorInterface1[] getFunctions() {
		List<ExternalFunctionConnectorInterface1> functionList = new LinkedList<ExternalFunctionConnectorInterface1>();

		functionList.add(new SumXfci1Plugin());
		functionList.add(new MeanXfci1Plugin());
		functionList.add(new VanXfci1Plugin());
		functionList.add(new Van1Xfci1Plugin());
		functionList.add(new SdnXfci1Plugin());
		functionList.add(new Sdn1Xfci1Plugin());

		functionList.add(new VectorSumXfci1Plugin());
		functionList.add(new VectorMeanXfci1Plugin());
		functionList.add(new VectorVanXfci1Plugin());
		functionList.add(new VectorVan1Xfci1Plugin());
		functionList.add(new VectorSdnXfci1Plugin());
		functionList.add(new VectorSdn1Xfci1Plugin());

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
