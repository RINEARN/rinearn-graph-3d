package org.vcssl.nano.plugin.math.xfci1;

public class MeanXfci1Plugin extends Float64VariadicScalarOperationXfci1Plugin {

	@Override
	public final String getFunctionName() {
		return "mean";
	}

	@Override
	public final void operate(double[] outputData, double[] inputData, int outputDataOffset) {
		int n = inputData.length;

		double sum = 0.0;
		for (double v: inputData) {
			sum += v;
		}

		double mean = sum / n;

		outputData[ outputDataOffset ] = mean;
	}
}
