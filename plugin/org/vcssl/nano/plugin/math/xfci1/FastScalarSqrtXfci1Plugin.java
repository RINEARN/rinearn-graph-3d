package org.vcssl.nano.plugin.math.xfci1;

import org.vcssl.connect.Float64ScalarDataAccessorInterface1;
import org.vcssl.connect.ConnectorException;

public class FastScalarSqrtXfci1Plugin extends Float64ScalarOperationXfci1Plugin {

	@Override
	public final String getFunctionName() {
		return "sqrt";
	}

	@Override
	public final Object invoke(Object[] arguments) throws ConnectorException {
		((Float64ScalarDataAccessorInterface1)arguments[0]).setFloat64ScalarData(
			Math.sqrt(
				((Float64ScalarDataAccessorInterface1)arguments[1]).getFloat64ScalarData()
			)
		);
		return null;
	}
}
