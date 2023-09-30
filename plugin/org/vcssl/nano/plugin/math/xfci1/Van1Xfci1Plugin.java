package org.vcssl.nano.plugin.math.xfci1;

public class Van1Xfci1Plugin extends Float64VariadicScalarOperationXfci1Plugin {

	@Override
	public final String getFunctionName() {
		return "van1";
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

		double van1 = squareDiffSum / (n-1);

		outputData[ outputDataOffset ] = van1;
	}
}
