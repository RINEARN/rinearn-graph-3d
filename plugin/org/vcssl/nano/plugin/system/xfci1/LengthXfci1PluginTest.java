package org.vcssl.nano.plugin.system.xfci1;

import org.vcssl.nano.vm.memory.DataContainer;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.vcssl.connect.ArrayDataAccessorInterface1;
import org.vcssl.connect.ConnectorException;
import org.vcssl.connect.ExternalFunctionConnectorInterface1;

public class LengthXfci1PluginTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testSettings() {
		ExternalFunctionConnectorInterface1 function = new LengthXfci1Plugin();

		// Check function name
		// 関数名を検査
		assertEquals("length", function.getFunctionName());

		// 引数 [0] は任意型・任意次元を許容する事を検査
		assertTrue(function.getParameterDataTypeArbitrarinesses()[0]);
		assertTrue(function.getParameterArrayRankArbitrarinesses()[0]);

		// Check number and types of arguments
		// 引数 [1] の個数と型を検査
		assertEquals(2, function.getParameterClasses().length);
		assertTrue(function.getParameterClasses()[1] == long.class);

		// Check type of return value
		// 戻り値の型を検査
		assertEquals(long.class, function.getReturnClass(new Class<?>[] { double[].class, long.class }));
	}


	@Test
	public void testDoubleScalar() {
		ExternalFunctionConnectorInterface1 function = new LengthXfci1Plugin();

		// Prepare input/output data
		// 入出力データを用意
		DataContainer<double[]> inputDataContainer = new DataContainer<double[]>();
		DataContainer<long[]> dimDataContainer = new DataContainer<long[]>();
		DataContainer<long[]> outputDataContainer = new DataContainer<long[]>();
		inputDataContainer.setArrayData(new double[] { 1.0 }, 0, ArrayDataAccessorInterface1.ARRAY_LENGTHS_OF_SCALAR);
		dimDataContainer.setArrayData(new long[] { 0L }, 0, ArrayDataAccessorInterface1.ARRAY_LENGTHS_OF_SCALAR);
		outputDataContainer.setArrayData(new long[] { 0L }, 0, ArrayDataAccessorInterface1.ARRAY_LENGTHS_OF_SCALAR);

		// Operate data
		// 演算を実行
		try {
			function.invoke(new Object[]{ outputDataContainer, inputDataContainer, dimDataContainer });
			fail("Expected exception has not occured");
		} catch (ConnectorException e) {
			// スカラに対する呼び出しでは、例外が発生するのが正しい挙動
		}
	}


	@Test
	public void testDoubleArray1D() throws ConnectorException {
		ExternalFunctionConnectorInterface1 function = new LengthXfci1Plugin();

		// Prepare input/output data
		// 入出力データを用意
		DataContainer<double[]> inputDataContainer = new DataContainer<double[]>();
		DataContainer<long[]> dimDataContainer = new DataContainer<long[]>();
		DataContainer<long[]> outputDataContainer = new DataContainer<long[]>();
		int[] inputArrayLengths = new int[] { 3 };
		int[] outputArrayLengths = new int[] { 3 };
		inputDataContainer.setArrayData(new double[] { 1.0, 2.0, 3.0 }, 0, inputArrayLengths);
		dimDataContainer.setArrayData(new long[] { 0L }, 0, ArrayDataAccessorInterface1.ARRAY_LENGTHS_OF_SCALAR);
		outputDataContainer.setArrayData(new long[] { 0L, 0L, 0L }, 0, outputArrayLengths);

		// Operate data
		// 演算を実行
		function.invoke(new Object[]{ outputDataContainer, inputDataContainer, dimDataContainer });

		// Check dimensions of the operation result
		// 演算結果の次元と要素数を確認
		assertEquals(0, outputDataContainer.getArrayRank());
		assertEquals(0, outputDataContainer.getArrayLengths().length);

		// Get result data in data container, and check its length
		// 演算結果のデータを取り出し、データ長を確認
		long[] resultData = outputDataContainer.getArrayData();
		assertEquals(1, resultData.length);

		// Check result value
		// 演算結果の値を確認
		assertEquals(3L, resultData[0]);
	}


	@Test
	public void testDoubleArray2D() throws ConnectorException {
		ExternalFunctionConnectorInterface1 function = new LengthXfci1Plugin();

		// Prepare input/output data
		// 入出力データを用意
		DataContainer<double[]> inputDataContainer = new DataContainer<double[]>();
		DataContainer<long[]> dimDataContainer = new DataContainer<long[]>();
		DataContainer<long[]> outputDataContainer = new DataContainer<long[]>();
		int[] inputArrayLengths = new int[] { 2, 3 };
		inputDataContainer.setArrayData(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 }, 0, inputArrayLengths);
		outputDataContainer.setArrayData(new long[] { 0L }, 0, ArrayDataAccessorInterface1.ARRAY_LENGTHS_OF_SCALAR);
		long[] resultData = null;


		// Operate data (dimIndex: 0)
		// 演算を実行（次元インデックス: 0）
		dimDataContainer.setArrayData(new long[] { 0L }, 0, ArrayDataAccessorInterface1.ARRAY_LENGTHS_OF_SCALAR);
		function.invoke(new Object[]{ outputDataContainer, inputDataContainer, dimDataContainer });

		// Check dimensions of the operation result
		// 演算結果の次元を確認
		assertEquals(0, outputDataContainer.getArrayRank());

		// Get result data in data container, and check its length
		// 演算結果のデータを取り出し、データ長を確認
		resultData = outputDataContainer.getArrayData();
		assertEquals(1, resultData.length);

		// Check result data
		// 演算結果の値を確認
		assertEquals(2L, resultData[0]);


		// Operate data (dimIndex: 1)
		// 演算を実行（次元インデックス: 1）
		dimDataContainer.setArrayData(new long[] { 1L }, 0, ArrayDataAccessorInterface1.ARRAY_LENGTHS_OF_SCALAR);
		function.invoke(new Object[]{ outputDataContainer, inputDataContainer, dimDataContainer });

		// Check dimensions of the operation result
		// 演算結果の次元を確認
		assertEquals(0, outputDataContainer.getArrayRank());

		// Get result data in data container, and check its length
		// 演算結果のデータを取り出し、データ長を確認
		resultData = outputDataContainer.getArrayData();
		assertEquals(1, resultData.length);

		// Check result data
		// 演算結果の値を確認
		assertEquals(3L, resultData[0]);
	}

}
