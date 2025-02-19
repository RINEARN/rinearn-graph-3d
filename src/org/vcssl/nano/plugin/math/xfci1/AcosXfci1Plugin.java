package org.vcssl.nano.plugin.math.xfci1;

public class AcosXfci1Plugin extends Float64VectorizableOperationXfci1Plugin {

	@Override
	public final String getFunctionName() {
		return "acos";
	}

	@Override
	public final void operate(double[] outputData, double[] inputData, int outputDataOffset, int inputDataOffset, int dataSize) {
		for (int i=0; i<dataSize; i++) {
			outputData[ i + outputDataOffset ] = Math.acos( inputData[ i + inputDataOffset ] );
		}
	}
}
