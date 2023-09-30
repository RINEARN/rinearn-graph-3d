package org.vcssl.nano.plugin.math.xfci1;

import org.vcssl.connect.Float64ScalarDataAccessorInterface1;
import org.vcssl.connect.ConnectorException;

public class FastScalarDegXfci1Plugin extends Float64ScalarOperationXfci1Plugin {

	@Override
	public final String getFunctionName() {
		return "deg";
	}

	@Override
	public final Object invoke(Object[] arguments) throws ConnectorException {
		double rad = ((Float64ScalarDataAccessorInterface1)arguments[1]).getFloat64ScalarData();
		double deg = 180.0 * rad / Math.PI;
		((Float64ScalarDataAccessorInterface1)arguments[0]).setFloat64ScalarData(deg);
		return null;
	}
}
