/*
 * Author:  RINEARN (Fumihiro Matsui), 2022
 * License: CC0
 * Interface Specification:
 *     https://www.vcssl.org/en-us/doc/connect/ExternalFunctionConnectorInterface1_SPEC_ENGLISH
 */

package org.vcssl.nano.plugin.system.xfci1;

import org.vcssl.nano.plugin.system.file.FileIOHub;

import org.vcssl.connect.ConnectorException;
import org.vcssl.connect.ConnectorFatalException;
import org.vcssl.connect.Int64ScalarDataAccessorInterface1;
import org.vcssl.connect.Float64ScalarDataAccessorInterface1;
import org.vcssl.connect.ArrayDataAccessorInterface1;
import org.vcssl.connect.EngineConnectorInterface1;
import org.vcssl.connect.ExternalFunctionConnectorInterface1;

import java.math.RoundingMode;
import java.math.BigDecimal;
import java.math.MathContext;


/**
 * A function plug-in providing "System.round(double value, int digit, int mode)" function.
 */
public class FloatRoundXfci1Plugin implements ExternalFunctionConnectorInterface1 {

	/** Represents the rounding mode "UP" */
	public static final int UP = 21;

	/** Represents the rounding mode "UP_SIGNIF" */
	public static final int UP_SIGNIF = 1;

	/** Represents the rounding mode "DOWN" */
	public static final int DOWN = 22;

	/** Represents the rounding mode "DOWN_SIGNIF" */
	public static final int DOWN_SIGNIF = 2;

	/** Represents the rounding mode "HALF_UP" */
	public static final int HALF_UP = 23;

	/** Represents the rounding mode "HALF_UP_SIGNIF" */
	public static final int HALF_UP_SIGNIF = 3;

	/** Represents the rounding mode "HALF_DOWN" */
	public static final int HALF_DOWN = 24;

	/** Represents the rounding mode "HALF_DOWN_SIGNIF" */
	public static final int HALF_DOWN_SIGNIF = 4;

	/** Represents the rounding mode "HALF_TO_EVEN" */
	public static final int HALF_TO_EVEN = 25;

	/** Represents the rounding mode "HALF_TO_EVEN_SIGNIF" */
	public static final int HALF_TO_EVEN_SIGNIF = 5;

	/** Stores the engine connector for requesting permissions. */
	protected EngineConnectorInterface1 engineConnector = null;

	/**
	 * Create a new instance of this plug-in.
	 */
	public FloatRoundXfci1Plugin() {
	}

	@Override
	public Class<?> getEngineConnectorClass() {
		return EngineConnectorInterface1.class;
	}

	@Override
	public void initializeForConnection(Object engineConnector) throws ConnectorException { }

	@Override
	public void initializeForExecution(Object engineConnector) throws ConnectorException {
		this.engineConnector = EngineConnectorInterface1.class.cast(engineConnector);
	}

	@Override
	public void finalizeForDisconnection(Object engineConnector) throws ConnectorException { }

	@Override
	public void finalizeForTermination(Object engineConnector) throws ConnectorException { }

	@Override
	public String getFunctionName() {
		return "round";
	}

	@Override
	public Class<?>[] getParameterClasses() {
		return new Class<?>[] { double.class, long.class, long.class };
	}

	@Override
	public Class<?>[] getParameterUnconvertedClasses() {
		return new Class<?>[] { Float64ScalarDataAccessorInterface1.class, Int64ScalarDataAccessorInterface1.class, Int64ScalarDataAccessorInterface1.class };
	}

	@Override
	public boolean hasParameterNames() {
		return true;
	}

	@Override
	public String[] getParameterNames() {
		return new String[] { "value", "numberOfDigits", "mode" };
	}

	@Override
	public boolean[] getParameterDataTypeArbitrarinesses() {
		return new boolean[]{ false, false, false };
	}

	@Override
	public boolean[] getParameterArrayRankArbitrarinesses() {
		return new boolean[]{ false, false, false };
	}

	@Override
	public boolean[] getParameterReferencenesses() {
		return new boolean[]{ false, false, false };
	}

	@Override
	public boolean[] getParameterConstantnesses() {
		return new boolean[]{ true, true, true };
	}

	@Override
	public boolean isParameterCountArbitrary() {
		return false;
	}

	@Override
	public boolean hasVariadicParameters() {
		return false;
	}

	@Override
	public Class<?> getReturnClass(Class<?>[] parameterClasses) {
		return double.class;
	}

	@Override
	public Class<?> getReturnUnconvertedClass(Class<?>[] parameterClasses) {
		return Float64ScalarDataAccessorInterface1.class;
	}

	@Override
	public boolean isReturnDataTypeArbitrary() {
		return false;
	}

	@Override
	public boolean isReturnArrayRankArbitrary() {
		return false;
	}

	@Override
	public boolean isDataConversionNecessary() {
		return false;
	}

	@Override
	public Object invoke(Object[] arguments) throws ConnectorException {
		int argLength = arguments.length;

		Float64ScalarDataAccessorInterface1 returnContainer = Float64ScalarDataAccessorInterface1.class.cast(arguments[0]);
		double value = (double)Float64ScalarDataAccessorInterface1.class.cast(arguments[1]).getFloat64ScalarData();
		long numberOfDigits = (long)Int64ScalarDataAccessorInterface1.class.cast(arguments[2]).getInt64ScalarData();
		long mode = (long)Int64ScalarDataAccessorInterface1.class.cast(arguments[3]).getInt64ScalarData();

		double roundedValue = this.round(value, (int)numberOfDigits, (int)mode);
		returnContainer.setFloat64ScalarData(roundedValue);
		return null;
	}

	/**
	 * Rounds the specifed value with the specified settings.
	 * 
	 * @param value The value to be rounded.
	 * @param numberOfDigits The number of digits of the part to be rounded.
	 * @param mode The rounding mode index.
	 * @return The rounded value.
	 */
	private double round(double value, int numberOfDigits, int mode) {
		BigDecimal rounder = new BigDecimal(value);
		RoundingMode roundingMode = this.toRoundingMode(mode);

		if (this.roundsAfterRadixPoint(mode)) {
			rounder = rounder.setScale(numberOfDigits, roundingMode);
		} else {
			MathContext mathContext = new MathContext(numberOfDigits, roundingMode);
			rounder = rounder.round(mathContext);
		}

		double roundedValue = rounder.doubleValue();
		return roundedValue;
	}

	/**
	 * Converts the rounding mode index to RoundingMode object.
	 * 
	 * @param mode The rounding mode index.
	 * @return The RoundingMode object.
	 */
	private RoundingMode toRoundingMode(int mode) {
		if (mode == this.UP || mode == this.UP_SIGNIF) {
			return RoundingMode.UP;

		} else if (mode == this.DOWN || mode == this.DOWN_SIGNIF) {
			return RoundingMode.DOWN;

		} else if (mode == this.HALF_UP || mode == this.HALF_UP_SIGNIF) {
			return RoundingMode.HALF_UP;

		} else if (mode == this.HALF_DOWN || mode == this.HALF_DOWN_SIGNIF) {
			return RoundingMode.HALF_DOWN;

		} else if (mode == this.HALF_TO_EVEN || mode == this.HALF_TO_EVEN_SIGNIF) {
			return RoundingMode.HALF_EVEN;

		} else {
			throw new ConnectorFatalException("Unknown rounding mode index: " + mode);
		}
	}

	/**
	 * Returns whether the specified mode is for rounding a part after the radix point.
	 * If this method returns false, it means that the specified mode is for rounding a significand part.
	 * 
	 * @param mode The rounding mode index.
	 * @return Returns true if the specified mode is for rounding a part after the radix point.
	 */
	private boolean roundsAfterRadixPoint(int mode) {
		if(mode == this.UP
				|| mode == this.DOWN
				|| mode == this.HALF_UP
				|| mode == this.HALF_DOWN
				|| mode == this.HALF_TO_EVEN) {
			return true;

		} else if(mode == this.UP_SIGNIF
				|| mode == this.DOWN_SIGNIF
				|| mode == this.HALF_UP_SIGNIF
				|| mode == this.HALF_DOWN_SIGNIF
				|| mode == this.HALF_TO_EVEN_SIGNIF) {
			return false;

		} else {
			throw new ConnectorFatalException("Unknown rounding mode index: " + mode);
		}
	}
}
