package org.vcssl.nano.plugin.math.xfci1;

public class SumXfci1Plugin extends Float64VariadicScalarOperationXfci1Plugin {

	@Override
	public final String getFunctionName() {
		return "sum";
	}

	@Override
	public final void operate(double[] outputData, double[] inputData, int outputDataOffset) {
		double sum = 0.0;
		for (double v: inputData) {
			sum += v;
		}
		outputData[ outputDataOffset ] = sum;
	}
}
