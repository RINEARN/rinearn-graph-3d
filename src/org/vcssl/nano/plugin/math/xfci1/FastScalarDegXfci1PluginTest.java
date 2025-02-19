package org.vcssl.nano.plugin.math.xfci1;

import org.vcssl.nano.vm.accelerator.Float64ScalarCache;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.vcssl.connect.ConnectorException;
import org.vcssl.connect.ExternalFunctionConnectorInterface1;

public class FastScalarDegXfci1PluginTest {

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
	public void test() throws ConnectorException {
		ExternalFunctionConnectorInterface1 function = new FastScalarDegXfci1Plugin();

		// Check function name
		// 関数名を検査
		assertEquals("deg", function.getFunctionName());

		// Check number and types of arguments
		// 引数の個数と型を検査
		assertEquals(1, function.getParameterClasses().length);
		assertTrue(function.getParameterClasses()[0] == double.class);
		assertFalse(function.getParameterArrayRankArbitrarinesses()[0]);

		// Check type of return value
		// 戻り値の型を検査
		assertEquals(double.class, function.getReturnClass(new Class<?>[] { double.class }));
		assertFalse(function.isReturnArrayRankArbitrary());

		// Prepare input/output data
		// 入出力データを用意
		Float64ScalarCache inputDataContainer = new Float64ScalarCache();
		Float64ScalarCache outputDataContainer = new Float64ScalarCache();

		// Operate data, and check results
		// 以下、演算を実行して結果を確認

		// 30度は π/6 ラジアン
		inputDataContainer.setFloat64ScalarData(Math.PI/6.0);
		outputDataContainer.setFloat64ScalarData(0.0);
		function.invoke(new Object[] { outputDataContainer, inputDataContainer });
		assertTrue(
			this.compareWithRounding( 30.0, outputDataContainer.getFloat64ScalarData() )
		);

		// 60度は π/3 ラジアン
		inputDataContainer.setFloat64ScalarData(Math.PI/3.0);
		outputDataContainer.setFloat64ScalarData(0.0);
		function.invoke(new Object[] { outputDataContainer, inputDataContainer });
		assertTrue(
			this.compareWithRounding( 60.0, outputDataContainer.getFloat64ScalarData() )
		);

		// 90度は π/2 ラジアン
		inputDataContainer.setFloat64ScalarData(Math.PI/2.0);
		outputDataContainer.setFloat64ScalarData(0.0);
		function.invoke(new Object[] { outputDataContainer, inputDataContainer });
		assertTrue(
			this.compareWithRounding( 90.0, outputDataContainer.getFloat64ScalarData() )
		);

		// 180度は π ラジアン
		inputDataContainer.setFloat64ScalarData(Math.PI);
		outputDataContainer.setFloat64ScalarData(0.0);
		function.invoke(new Object[] { outputDataContainer, inputDataContainer });
		assertTrue(
			this.compareWithRounding( 180.0, outputDataContainer.getFloat64ScalarData() )
		);

		// 360度は 2π ラジアン
		inputDataContainer.setFloat64ScalarData(2.0*Math.PI);
		outputDataContainer.setFloat64ScalarData(0.0);
		function.invoke(new Object[] { outputDataContainer, inputDataContainer });
		assertTrue(
			this.compareWithRounding( 360.0, outputDataContainer.getFloat64ScalarData() )
		);

		// 720度は 4π ラジアン
		inputDataContainer.setFloat64ScalarData(4.0*Math.PI);
		outputDataContainer.setFloat64ScalarData(0.0);
		function.invoke(new Object[] { outputDataContainer, inputDataContainer });
		assertTrue(
			this.compareWithRounding( 720.0, outputDataContainer.getFloat64ScalarData() )
		);

		// 負の場合
		inputDataContainer.setFloat64ScalarData(-Math.PI/6.0);
		outputDataContainer.setFloat64ScalarData(0.0);
		function.invoke(new Object[] { outputDataContainer, inputDataContainer });
		assertTrue(
			this.compareWithRounding( -30.0, outputDataContainer.getFloat64ScalarData() )
		);

		// 0の場合
		inputDataContainer.setFloat64ScalarData(0.0);
		outputDataContainer.setFloat64ScalarData(0.0);
		function.invoke(new Object[] { outputDataContainer, inputDataContainer });
		assertTrue(
			this.compareWithRounding( 0.0, outputDataContainer.getFloat64ScalarData() )
		);

	}

}
