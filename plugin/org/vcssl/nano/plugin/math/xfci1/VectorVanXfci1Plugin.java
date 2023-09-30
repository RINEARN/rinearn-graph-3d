package org.vcssl.nano.plugin.math.xfci1;

public class VectorVanXfci1Plugin extends Float64VectorToScalarOperationXfci1Plugin {

	@Override
	public final String getFunctionName() {
		return "van";
	}

	@Override
	public final double operate(double[] inputData, int inputDataOffset, int inputDataSize) {
		int n = inputDataSize;
		int endIndex = inputDataOffset + inputDataSize - 1;

		double sum = 0.0;
		for (int i=inputDataOffset; i<=endIndex; i++) {
			sum += inputData[i];
		}
		double mean = sum / n;

		double squareDiffSum = 0.0;
		for (int i=inputDataOffset; i<=endIndex; i++) {
			double diff = inputData[i] - mean;
			squareDiffSum += diff * diff;
		}

		double van = squareDiffSum / n;

		return van;
	}
}
