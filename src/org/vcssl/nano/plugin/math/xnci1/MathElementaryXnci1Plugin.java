package org.vcssl.nano.plugin.math.xnci1;

import java.util.LinkedList;
import java.util.List;

import org.vcssl.connect.ConnectorException;
import org.vcssl.connect.EngineConnectorInterface1;
import org.vcssl.connect.ExternalFunctionConnectorInterface1;
import org.vcssl.connect.ExternalNamespaceConnectorInterface1;
import org.vcssl.connect.ExternalStructConnectorInterface1;
import org.vcssl.connect.ExternalVariableConnectorInterface1;
import org.vcssl.nano.plugin.math.xvci1.PiXvci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.AbsXfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.AcosXfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.AsinXfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.AtanXfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.CosXfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.DegXfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.ExpXfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.LnXfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.Log10Xfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.PowXfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.RadXfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.FastScalarAbsXfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.FastScalarAcosXfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.FastScalarAsinXfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.FastScalarAtanXfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.FastScalarCosXfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.FastScalarDegXfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.FastScalarExpXfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.FastScalarLnXfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.FastScalarLog10Xfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.FastScalarPowXfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.FastScalarRadXfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.FastScalarSinXfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.FastScalarSqrtXfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.FastScalarTanXfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.SinXfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.SqrtXfci1Plugin;
import org.vcssl.nano.plugin.math.xfci1.TanXfci1Plugin;

public class MathElementaryXnci1Plugin implements ExternalNamespaceConnectorInterface1 {

	@Override
	public String getNamespaceName() {
		return "Math";
	}

	@Override
	public boolean isMandatoryToAccessMembers() {
		return false;
	}

	@Override
	public ExternalFunctionConnectorInterface1[] getFunctions() {
		List<ExternalFunctionConnectorInterface1> functionList = new LinkedList<ExternalFunctionConnectorInterface1>();

		// Fast implementations, supporting only scalar arguments
		// スカラ引数のみに対応した高速実装
		// （機能的には上の汎用実装のみで全部揃っているが、以下を接続しておけば引数がスカラの場合に高速化される）
		functionList.add(new FastScalarRadXfci1Plugin());
		functionList.add(new FastScalarDegXfci1Plugin());
		functionList.add(new FastScalarSinXfci1Plugin());
		functionList.add(new FastScalarCosXfci1Plugin());
		functionList.add(new FastScalarTanXfci1Plugin());
		functionList.add(new FastScalarAsinXfci1Plugin());
		functionList.add(new FastScalarAcosXfci1Plugin());
		functionList.add(new FastScalarAtanXfci1Plugin());
		functionList.add(new FastScalarSqrtXfci1Plugin());
		functionList.add(new FastScalarLnXfci1Plugin());
		functionList.add(new FastScalarLog10Xfci1Plugin());
		functionList.add(new FastScalarPowXfci1Plugin());
		functionList.add(new FastScalarExpXfci1Plugin());
		functionList.add(new FastScalarAbsXfci1Plugin());

		// General implementations, supporting scalar & array (any dim) arguments
		// スカラ含む任意次元配列の引数に対応した汎用実装
		// （プラグイン仕様書に記載の全機能は、一応これらの汎用実装のみで満たしている）
		functionList.add(new RadXfci1Plugin());
		functionList.add(new DegXfci1Plugin());
		functionList.add(new SinXfci1Plugin());
		functionList.add(new CosXfci1Plugin());
		functionList.add(new TanXfci1Plugin());
		functionList.add(new AsinXfci1Plugin());
		functionList.add(new AcosXfci1Plugin());
		functionList.add(new AtanXfci1Plugin());
		functionList.add(new SqrtXfci1Plugin());
		functionList.add(new LnXfci1Plugin());
		functionList.add(new Log10Xfci1Plugin());
		functionList.add(new PowXfci1Plugin());
		functionList.add(new ExpXfci1Plugin());
		functionList.add(new AbsXfci1Plugin());

		return functionList.toArray(new ExternalFunctionConnectorInterface1[0]);
	}

	@Override
	public ExternalVariableConnectorInterface1[] getVariables() {
		List<ExternalVariableConnectorInterface1> variableList = new LinkedList<ExternalVariableConnectorInterface1>();
		variableList.add(new PiXvci1Plugin());
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
