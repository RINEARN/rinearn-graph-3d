package org.vcssl.nano.plugin.math.xfci1;

public class SdnXfci1Plugin extends Float64VariadicScalarOperationXfci1Plugin {

	@Override
	public final String getFunctionName() {
		return "sdn";
	}

	@Override
	public final void operate(double[] outputData, double[] inputData, int outputDataOffset) {
		int n = inputData.length;

		double sum = 0.0;
		for (double v: inputData) {
			sum += v;
		}

		double mean = sum / n;

		double squareDiffSum = 0.0;
		for (double v: inputData) {
			double diff = v - mean;
			squareDiffSum += diff * diff;
		}

		double van = squareDiffSum / n;
		double sdn = Math.sqrt(van);

		outputData[ outputDataOffset ] = sdn;
	}
}
