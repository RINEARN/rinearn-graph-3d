package org.vcssl.nano.plugin.math.xfci1;

import org.vcssl.nano.vm.memory.DataContainer;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.vcssl.connect.ArrayDataAccessorInterface1;
import org.vcssl.connect.ConnectorException;
import org.vcssl.connect.ExternalFunctionConnectorInterface1;

public class SqrtXfci1PluginTest {

	private static final int RANK_OF_SCALAR = 0;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testSettings() {
		ExternalFunctionConnectorInterface1 function = new SqrtXfci1Plugin();

		// Check function name
		// 関数名を検査
		assertEquals("sqrt", function.getFunctionName());

		// Check number and types of arguments
		// 引数の個数と型を検査
		assertEquals(1, function.getParameterClasses().length);
		assertTrue(function.getParameterClasses()[0] == double.class);

		// Check type of return value
		// 戻り値の型を検査
		assertEquals(double.class, function.getReturnClass(new Class<?>[] { double.class }));
	}


	@Test
	public void testDoubleScalar() throws ConnectorException {
		ExternalFunctionConnectorInterface1 function = new SqrtXfci1Plugin();

		// Prepare input/output data
		// 入出力データを用意
		DataContainer<double[]> inputDataContainer = new DataContainer<double[]>();
		DataContainer<double[]> outputDataContainer = new DataContainer<double[]>();
		inputDataContainer.setArrayData(new double[] { 1.0 }, 0, ArrayDataAccessorInterface1.ARRAY_LENGTHS_OF_SCALAR);
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
		Double expected = Double.valueOf(Math.sqrt(1.0));
		Double actual = Double.valueOf(resultData[0]);
		assertTrue(expected.equals(actual));
	}


	@Test
	public void testDoubleArray1D() throws ConnectorException {
		ExternalFunctionConnectorInterface1 function = new SqrtXfci1Plugin();

		// Prepare input/output data
		// 入出力データを用意
		DataContainer<double[]> inputDataContainer = new DataContainer<double[]>();
		DataContainer<double[]> outputDataContainer = new DataContainer<double[]>();
		int[] inputArrayLengths = new int[] { 3 };
		int[] outputArrayLengths = new int[] { 3 };
		inputDataContainer.setArrayData(new double[] { 1.0, 2.0, 3.0 }, 0, inputArrayLengths);
		outputDataContainer.setArrayData(new double[] { 0.0, 0.0, 0.0 }, 0, outputArrayLengths);

		// Operate data
		// 演算を実行
		function.invoke(new Object[]{ outputDataContainer, inputDataContainer });

		// Check dimensions of the operation result
		// 演算結果の次元を確認
		assertEquals(1, outputDataContainer.getArrayRank());
		assertEquals(1, outputDataContainer.getArrayLengths().length);
		assertEquals(3, outputDataContainer.getArrayLengths()[0]);

		// Get result data in data container, and check its length
		// 演算結果のデータを取り出し、データ長を確認
		double[] resultData = outputDataContainer.getArrayData();
		assertEquals(3, resultData.length);

		Double expected;
		Double actual;

		// Check result value[0]
		// 演算結果[0]の値を確認
		expected = Double.valueOf(Math.sqrt(1.0));
		actual = Double.valueOf(resultData[0]);
		assertTrue(expected.equals(actual));

		// Check result value[1]
		// 演算結果[1]の値を確認
		expected = Double.valueOf(Math.sqrt(2.0));
		actual = Double.valueOf(resultData[1]);
		assertTrue(expected.equals(actual));

		// Check result value[2]
		// 演算結果[2]の値を確認
		expected = Double.valueOf(Math.sqrt(3.0));
		actual = Double.valueOf(resultData[2]);
		assertTrue(expected.equals(actual));
	}


	@Test
	public void testDoubleArray2D() throws ConnectorException {
		ExternalFunctionConnectorInterface1 function = new SqrtXfci1Plugin();

		// Prepare input/output data
		// 入出力データを用意
		DataContainer<double[]> inputDataContainer = new DataContainer<double[]>();
		DataContainer<double[]> outputDataContainer = new DataContainer<double[]>();
		int[] inputArrayLengths = new int[] { 2, 3 };
		int[] outputArrayLengths = new int[] { 2, 3 };
		inputDataContainer.setArrayData(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 }, 0, inputArrayLengths);
		outputDataContainer.setArrayData(new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 }, 0, outputArrayLengths);

		// Operate data
		// 演算を実行
		function.invoke(new Object[]{ outputDataContainer, inputDataContainer });

		// Check dimensions of the operation result
		// 演算結果の次元を確認
		assertEquals(2, outputDataContainer.getArrayRank());
		assertEquals(2, outputDataContainer.getArrayLengths().length);
		assertEquals(2, outputDataContainer.getArrayLengths()[0]);
		assertEquals(3, outputDataContainer.getArrayLengths()[1]);

		// Get result data in data container, and check its length
		// 演算結果のデータを取り出し、データ長を確認
		double[] resultData = outputDataContainer.getArrayData();
		assertEquals(2*3, resultData.length);

		Double expected;
		Double actual;

		// Check result value[0]
		// 演算結果[0]の値を確認
		expected = Double.valueOf(Math.sqrt(1.0));
		actual = Double.valueOf(resultData[0]);
		assertTrue(expected.equals(actual));

		// Check result value[1]
		// 演算結果[1]の値を確認
		expected = Double.valueOf(Math.sqrt(2.0));
		actual = Double.valueOf(resultData[1]);
		assertTrue(expected.equals(actual));

		// Check result value[2]
		// 演算結果[2]の値を確認
		expected = Double.valueOf(Math.sqrt(3.0));
		actual = Double.valueOf(resultData[2]);
		assertTrue(expected.equals(actual));

		// Check result value[3]
		// 演算結果[3]の値を確認
		expected = Double.valueOf(Math.sqrt(4.0));
		actual = Double.valueOf(resultData[3]);
		assertTrue(expected.equals(actual));

		// Check result value[4]
		// 演算結果[4]の値を確認
		expected = Double.valueOf(Math.sqrt(5.0));
		actual = Double.valueOf(resultData[4]);
		assertTrue(expected.equals(actual));

		// Check result value[5]
		// 演算結果[5]の値を確認
		expected = Double.valueOf(Math.sqrt(6.0));
		actual = Double.valueOf(resultData[5]);
		assertTrue(expected.equals(actual));
	}


	@Test
	public void testDoubleArrayElement() throws ConnectorException {
		ExternalFunctionConnectorInterface1 function = new SqrtXfci1Plugin();

		// Prepare input/output data
		// 入出力データを用意
		DataContainer<double[]> inputDataContainer = new DataContainer<double[]>();
		DataContainer<double[]> outputDataContainer = new DataContainer<double[]>();
		int inputDataOffset = 1;
		int outputDataOffset = 2;
		inputDataContainer.setArrayData(new double[] { 1.0, 2.0, 3.0 }, inputDataOffset, ArrayDataAccessorInterface1.ARRAY_LENGTHS_OF_SCALAR);
		outputDataContainer.setArrayData(new double[] { 0.0, 0.0, 0.0 }, outputDataOffset, ArrayDataAccessorInterface1.ARRAY_LENGTHS_OF_SCALAR);

		// Operate data
		// 演算を実行
		function.invoke(new Object[]{ outputDataContainer, inputDataContainer });

		// Check dimensions of the operation result
		// 演算結果の次元を確認
		assertEquals(0, outputDataContainer.getArrayRank());
		assertEquals(0, outputDataContainer.getArrayLengths().length);

		// Get result data in data container, and check its length
		// 演算結果のデータを取り出し、データ長や使用サイズ、格納位置などを確認
		double[] resultData = outputDataContainer.getArrayData();
		int resultDataSize = outputDataContainer.getArraySize();
		int resultDataOffset = outputDataContainer.getArrayOffset();
		int resultDataRank = outputDataContainer.getArrayRank();
		assertEquals(3, resultData.length); // 変わっていないはず
		assertEquals(1, resultDataSize); // スカラなので1のはず
		assertEquals(0, resultDataRank); // スカラなので0のはず
		assertEquals(outputDataOffset, resultDataOffset);

		Double expected;
		Double actual;

		// Check the result value
		// 演算結果の値を確認
		expected = Double.valueOf(Math.sqrt(2.0));
		actual = Double.valueOf(resultData[resultDataOffset]);
		assertTrue(expected.equals(actual));
	}

}
