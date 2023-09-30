package org.vcssl.nano.plugin.math.xfci1;

import org.vcssl.nano.vm.memory.DataContainer;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.vcssl.connect.ArrayDataAccessorInterface1;
import org.vcssl.connect.ConnectorException;
import org.vcssl.connect.ExternalFunctionConnectorInterface1;

public class DegXfci1PluginTest {

	private static final int RANK_OF_SCALAR = 0;
	private static final int COMPARING_PRECISION = 10;

	// 丸め誤差を吸収して比較
	private boolean compareWithRounding(Double a, Double b) {
		MathContext roundingContext = new MathContext(COMPARING_PRECISION, RoundingMode.HALF_EVEN);
		BigDecimal bigA = BigDecimal.valueOf(a).round(roundingContext);
		BigDecimal bigB = BigDecimal.valueOf(b).round(roundingContext);
		return (bigA.compareTo(bigB) == 0);
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testSettings() {
		ExternalFunctionConnectorInterface1 function = new DegXfci1Plugin();

		// Check function name
		// 関数名を検査
		assertEquals("deg", function.getFunctionName());

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
		ExternalFunctionConnectorInterface1 function = new DegXfci1Plugin();

		// Prepare input/output data
		// 入出力データを用意
		DataContainer<double[]> inputDataContainer = new DataContainer<double[]>();
		DataContainer<double[]> outputDataContainer = new DataContainer<double[]>();
		inputDataContainer.setArrayData(new double[] { Math.PI }, 0, ArrayDataAccessorInterface1.ARRAY_LENGTHS_OF_SCALAR);
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
		Double expected = Double.valueOf(180.0); // 180度はπラジアン
		Double actual = Double.valueOf(resultData[0]);
		assertTrue(compareWithRounding(expected, actual));
	}


	@Test
	public void testDoubleArray1D() throws ConnectorException {
		ExternalFunctionConnectorInterface1 function = new DegXfci1Plugin();

		// Prepare input/output data
		// 入出力データを用意
		DataContainer<double[]> inputDataContainer = new DataContainer<double[]>();
		DataContainer<double[]> outputDataContainer = new DataContainer<double[]>();
		int[] inputArrayLengths = new int[] { 3 };
		int[] outputArrayLengths = new int[] { 3 };
		inputDataContainer.setArrayData(new double[] { Math.PI, Math.PI/2, Math.PI/3 }, 0, inputArrayLengths);
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
		expected = Double.valueOf( 180.0 ); // 180度はπラジアン
		actual = Double.valueOf(resultData[0]);
		assertTrue(compareWithRounding(expected, actual));

		// Check result value[1]
		// 演算結果[1]の値を確認
		expected = Double.valueOf( 90.0 ); // 90度はπ/2ラジアン
		actual = Double.valueOf(resultData[1]);
		assertTrue(compareWithRounding(expected, actual));

		// Check result value[2]
		// 演算結果[2]の値を確認
		expected = Double.valueOf( 60.0 ); // 60度はπ/3ラジアン
		actual = Double.valueOf(resultData[2]);
		assertTrue(compareWithRounding(expected, actual));
	}


	@Test
	public void testDoubleArray2D() throws ConnectorException {
		ExternalFunctionConnectorInterface1 function = new DegXfci1Plugin();

		// Prepare input/output data
		// 入出力データを用意
		DataContainer<double[]> inputDataContainer = new DataContainer<double[]>();
		DataContainer<double[]> outputDataContainer = new DataContainer<double[]>();
		int[] inputArrayLengths = new int[] { 2, 3 };
		int[] outputArrayLengths = new int[] { 2, 3 };
		inputDataContainer.setArrayData(new double[] { 2*Math.PI, 3*Math.PI/2, Math.PI, Math.PI/2, Math.PI/3, Math.PI/6 }, 0, inputArrayLengths);
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
		expected = Double.valueOf( 360.0 ); // 360度は2πラジアン
		actual = Double.valueOf(resultData[0]);
		assertTrue(compareWithRounding(expected, actual));

		// Check result value[1]
		// 演算結果[1]の値を確認
		expected = Double.valueOf( 270.0 ); // 270度は(3/2)πラジアン
		actual = Double.valueOf(resultData[1]);
		assertTrue(compareWithRounding(expected, actual));

		// Check result value[2]
		// 演算結果[2]の値を確認
		expected = Double.valueOf( 180.0 ); // 180度はπラジアン
		actual = Double.valueOf(resultData[2]);
		assertTrue(compareWithRounding(expected, actual));

		// Check result value[3]
		// 演算結果[3]の値を確認
		expected = Double.valueOf( 90.0 ); // 90度はπ/2ラジアン
		actual = Double.valueOf(resultData[3]);
		assertTrue(compareWithRounding(expected, actual));

		// Check result value[4]
		// 演算結果[4]の値を確認
		expected = Double.valueOf( 60.0 ); // 60度はπ/3ラジアン
		actual = Double.valueOf(resultData[4]);
		assertTrue(compareWithRounding(expected, actual));

		// Check result value[5]
		// 演算結果[5]の値を確認
		expected = Double.valueOf( 30.0 ); // 30度はπ/6 ラジアン
		actual = Double.valueOf(resultData[5]);
		assertTrue(compareWithRounding(expected, actual));
	}


	@Test
	public void testDoubleArrayElement() throws ConnectorException {
		ExternalFunctionConnectorInterface1 function = new DegXfci1Plugin();

		// Prepare input/output data
		// 入出力データを用意
		DataContainer<double[]> inputDataContainer = new DataContainer<double[]>();
		DataContainer<double[]> outputDataContainer = new DataContainer<double[]>();
		int inputDataOffset = 1;
		int outputDataOffset = 2;
		inputDataContainer.setArrayData(new double[] { Math.PI, 2*Math.PI, 3*Math.PI }, inputDataOffset, ArrayDataAccessorInterface1.ARRAY_LENGTHS_OF_SCALAR);
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
		expected = Double.valueOf(360.0);
		actual = Double.valueOf(resultData[resultDataOffset]);
		assertTrue(expected.equals(actual));
	}


}
