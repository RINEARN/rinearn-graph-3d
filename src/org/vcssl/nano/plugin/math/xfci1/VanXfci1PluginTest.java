package org.vcssl.nano.plugin.math.xfci1;

import org.vcssl.nano.vm.memory.DataContainer;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.vcssl.connect.ArrayDataAccessorInterface1;
import org.vcssl.connect.ConnectorException;
import org.vcssl.connect.ExternalFunctionConnectorInterface1;

public class VanXfci1PluginTest {

	private static final int RANK_OF_SCALAR = 0;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testSettings() {
		ExternalFunctionConnectorInterface1 function = new VanXfci1Plugin();

		// Check function name
		// 関数名を検査
		assertEquals("van", function.getFunctionName());

		// Check number and types of arguments
		// 引数の型を検査
		assertEquals(1, function.getParameterClasses().length);
		assertTrue(function.getParameterClasses()[0] == double.class);

		// Check number of parameters arbitrary
		// 引数の個数が任意に設定されている事を検査
		assertTrue(function.isParameterCountArbitrary());

		// Check type of return value
		// 戻り値の型を検査
		assertEquals(double.class, function.getReturnClass(new Class<?>[] { double.class }));
	}


	@Test
	public void testDoubleSingleArg() throws ConnectorException {
		ExternalFunctionConnectorInterface1 function = new VanXfci1Plugin();

		// Prepare input/output data
		// 入出力データを用意
		DataContainer<double[]> inputDataContainer = new DataContainer<double[]>();
		DataContainer<double[]> outputDataContainer = new DataContainer<double[]>();
		inputDataContainer.setArrayData(new double[] { 1.23 }, 0, ArrayDataAccessorInterface1.ARRAY_LENGTHS_OF_SCALAR);
		outputDataContainer.setArrayData(new double[] { 0.0 }, 0, ArrayDataAccessorInterface1.ARRAY_LENGTHS_OF_SCALAR);

		// Operate data
		// 演算を実行
		function.invoke(new Object[]{ outputDataContainer, inputDataContainer });

		// Check dimensions of the operation result
		// 演算結果の次元を確認
		assertEquals(RANK_OF_SCALAR, outputDataContainer.getArrayRank());
		assertEquals(0, outputDataContainer.getArrayLengths().length);

		// Get result data in data container, and check its length
		// 演算結果のデータを取り出し、データ長を確認
		double[] resultData = outputDataContainer.getArrayData();
		assertEquals(1, resultData.length);

		// Check result value
		// 演算結果の値を確認
		Double expected = Double.valueOf(0.0); // 数値1個の分散は 0
		Double actual = Double.valueOf(resultData[0]);
		assertTrue(expected.equals(actual));
	}

	@Test
	public void testDoubleDoubleArgs() throws ConnectorException {
		ExternalFunctionConnectorInterface1 function = new VanXfci1Plugin();

		// Prepare input/output data
		// 入出力データを用意
		DataContainer<double[]> inputDataContainer1 = new DataContainer<double[]>();
		DataContainer<double[]> inputDataContainer2 = new DataContainer<double[]>();
		DataContainer<double[]> outputDataContainer = new DataContainer<double[]>();
		inputDataContainer1.setArrayData(new double[] { 1.23 }, 0, ArrayDataAccessorInterface1.ARRAY_LENGTHS_OF_SCALAR);
		inputDataContainer2.setArrayData(new double[] { 4.56 }, 0, ArrayDataAccessorInterface1.ARRAY_LENGTHS_OF_SCALAR);
		outputDataContainer.setArrayData(new double[] { 0.0 }, 0, ArrayDataAccessorInterface1.ARRAY_LENGTHS_OF_SCALAR);

		// Operate data
		// 演算を実行
		function.invoke(new Object[]{ outputDataContainer, inputDataContainer1, inputDataContainer2 });

		// Check dimensions of the operation result
		// 演算結果の次元を確認
		assertEquals(RANK_OF_SCALAR, outputDataContainer.getArrayRank());
		assertEquals(0, outputDataContainer.getArrayLengths().length);

		// Get result data in data container, and check its length
		// 演算結果のデータを取り出し、データ長を確認
		double[] resultData = outputDataContainer.getArrayData();
		assertEquals(1, resultData.length);

		// Check result value
		// 以下、演算結果の値を確認

		double mean = (1.23 + 4.56) / 2;
		double squareDiffSum = (1.23-mean)*(1.23-mean) + (4.56-mean)*(4.56-mean);
		double van = squareDiffSum / 2;

		Double expected = Double.valueOf(van);
		Double actual = Double.valueOf(resultData[0]);
		assertTrue(expected.equals(actual));
	}


	@Test
	public void testArryElementArgs() throws ConnectorException {
		ExternalFunctionConnectorInterface1 function = new VanXfci1Plugin();

		// Prepare input/output data
		// 入出力データを用意
		DataContainer<double[]> inputDataContainer1 = new DataContainer<double[]>();
		DataContainer<double[]> inputDataContainer2 = new DataContainer<double[]>();
		DataContainer<double[]> inputDataContainer3 = new DataContainer<double[]>();
		DataContainer<double[]> outputDataContainer = new DataContainer<double[]>();
		int inputDataOffset1 = 0;
		int inputDataOffset2 = 1;
		int inputDataOffset3 = 2;
		int outputDataOffset = 2;
		inputDataContainer1.setArrayData(new double[] { 1.23, 4.56, 7.89 }, inputDataOffset1, ArrayDataAccessorInterface1.ARRAY_LENGTHS_OF_SCALAR);
		inputDataContainer2.setArrayData(new double[] { 1.23, 4.56, 7.89 }, inputDataOffset2, ArrayDataAccessorInterface1.ARRAY_LENGTHS_OF_SCALAR);
		inputDataContainer3.setArrayData(new double[] { 1.23, 4.56, 7.89 }, inputDataOffset3, ArrayDataAccessorInterface1.ARRAY_LENGTHS_OF_SCALAR);
		outputDataContainer.setArrayData(new double[] { 0.0, 0.0, 0.0 }, outputDataOffset, ArrayDataAccessorInterface1.ARRAY_LENGTHS_OF_SCALAR);

		// Operate data
		// 演算を実行
		function.invoke(new Object[]{ outputDataContainer, inputDataContainer1, inputDataContainer2, inputDataContainer3 });

		// Check dimensions, size, offset, and so on of the operation result
		// 演算結果のデータを取り出し、データ長や使用サイズ、格納位置などを確認
		double[] resultData = outputDataContainer.getArrayData();
		assertEquals(RANK_OF_SCALAR, outputDataContainer.getArrayRank());
		assertEquals(3, resultData.length);
		assertEquals(1, outputDataContainer.getArraySize());
		assertEquals(2, outputDataContainer.getArrayOffset());

		// Check result value
		// 以下、演算結果の値を確認

		double mean = (1.23 + 4.56 + 7.89) / 3;
		double squareDiffSum = (1.23-mean)*(1.23-mean) + (4.56-mean)*(4.56-mean) + (7.89-mean)*(7.89-mean);
		double van = squareDiffSum / 3;
		Double expected = Double.valueOf(van);
		Double actual = Double.valueOf(resultData[outputDataOffset]);
		assertTrue(expected.equals(actual));
	}

}
