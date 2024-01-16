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
import org.vcssl.connect.StringScalarDataAccessorInterface1;
import org.vcssl.connect.ArrayDataAccessorInterface1;
import org.vcssl.connect.EngineConnectorInterface1;
import org.vcssl.connect.ExternalFunctionConnectorInterface1;

import java.math.RoundingMode;
import java.math.BigDecimal;
import java.math.MathContext;


/**
 * A function plug-in providing "System.round(string value, int digit, int mode)" function.
 */
public class StringRoundXfci1Plugin implements ExternalFunctionConnectorInterface1 {

	/** Represents the rounding mode "UP" */
	private static final int UP = FloatRoundXfci1Plugin.UP;

	/** Represents the rounding mode "UP_SIGNIF" */
	private static final int UP_SIGNIF = FloatRoundXfci1Plugin.UP_SIGNIF;

	/** Represents the rounding mode "DOWN" */
	private static final int DOWN = FloatRoundXfci1Plugin.DOWN;

	/** Represents the rounding mode "DOWN_SIGNIF" */
	private static final int DOWN_SIGNIF = FloatRoundXfci1Plugin.DOWN_SIGNIF;

	/** Represents the rounding mode "HALF_UP" */
	private static final int HALF_UP = FloatRoundXfci1Plugin.HALF_UP;

	/** Represents the rounding mode "HALF_UP_SIGNIF" */
	private static final int HALF_UP_SIGNIF = FloatRoundXfci1Plugin.HALF_UP_SIGNIF;

	/** Represents the rounding mode "HALF_DOWN" */
	private static final int HALF_DOWN = FloatRoundXfci1Plugin.HALF_DOWN;

	/** Represents the rounding mode "HALF_DOWN_SIGNIF" */
	private static final int HALF_DOWN_SIGNIF = FloatRoundXfci1Plugin.HALF_DOWN_SIGNIF;

	/** Represents the rounding mode "HALF_TO_EVEN" */
	private static final int HALF_TO_EVEN = FloatRoundXfci1Plugin.HALF_TO_EVEN;

	/** Represents the rounding mode "HALF_TO_EVEN_SIGNIF" */
	private static final int HALF_TO_EVEN_SIGNIF = FloatRoundXfci1Plugin.HALF_TO_EVEN_SIGNIF;

	/** Stores the engine connector for requesting permissions. */
	private EngineConnectorInterface1 engineConnector = null;

	/**
	 * Create a new instance of this plug-in.
	 */
	public StringRoundXfci1Plugin() {
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
		return new Class<?>[] { String.class, long.class, long.class };
	}

	@Override
	public Class<?>[] getParameterUnconvertedClasses() {
		return new Class<?>[] { StringScalarDataAccessorInterface1.class, Int64ScalarDataAccessorInterface1.class, Int64ScalarDataAccessorInterface1.class };
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
		return String.class;
	}

	@Override
	public Class<?> getReturnUnconvertedClass(Class<?>[] parameterClasses) {
		return StringScalarDataAccessorInterface1.class;
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

		StringScalarDataAccessorInterface1 returnContainer = StringScalarDataAccessorInterface1.class.cast(arguments[0]);
		String value = StringScalarDataAccessorInterface1.class.cast(arguments[1]).getStringScalarData();
		long numberOfDigits = Int64ScalarDataAccessorInterface1.class.cast(arguments[2]).getInt64ScalarData();
		long mode = Int64ScalarDataAccessorInterface1.class.cast(arguments[3]).getInt64ScalarData();

		String roundedValue = this.round(value, (int)numberOfDigits, (int)mode);
		returnContainer.setStringScalarData(roundedValue);
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
	private String round(String value, int numberOfDigits, int mode) {
		BigDecimal rounder = new BigDecimal(value);
		RoundingMode roundingMode = this.toRoundingMode(mode);

		if (this.roundsAfterRadixPoint(mode)) {
			rounder = rounder.setScale(numberOfDigits, roundingMode);
		} else {
			MathContext mathContext = new MathContext(numberOfDigits, roundingMode);
			rounder = rounder.round(mathContext);
		}

		String roundedValue = rounder.toString();
		if (roundedValue.toUpperCase().contains("E+")) {
			roundedValue = roundedValue.replace("E+", "E");
			roundedValue = roundedValue.replace("e+", "e");
		}
		return roundedValue;
	}

	/**
	 * Converts the rounding mode index to RoundingMode object.
	 * 
	 * @param mode The rounding mode index.
	 * @return The RoundingMode object.
	 */
	private RoundingMode toRoundingMode(int mode) {
		if (mode == StringRoundXfci1Plugin.UP || mode == StringRoundXfci1Plugin.UP_SIGNIF) {
			return RoundingMode.UP;

		} else if (mode == StringRoundXfci1Plugin.DOWN || mode == StringRoundXfci1Plugin.DOWN_SIGNIF) {
			return RoundingMode.DOWN;

		} else if (mode == StringRoundXfci1Plugin.HALF_UP || mode == StringRoundXfci1Plugin.HALF_UP_SIGNIF) {
			return RoundingMode.HALF_UP;

		} else if (mode == StringRoundXfci1Plugin.HALF_DOWN || mode == StringRoundXfci1Plugin.HALF_DOWN_SIGNIF) {
			return RoundingMode.HALF_DOWN;

		} else if (mode == StringRoundXfci1Plugin.HALF_TO_EVEN || mode == StringRoundXfci1Plugin.HALF_TO_EVEN_SIGNIF) {
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
		if(mode == StringRoundXfci1Plugin.UP
				|| mode == StringRoundXfci1Plugin.DOWN
				|| mode == StringRoundXfci1Plugin.HALF_UP
				|| mode == StringRoundXfci1Plugin.HALF_DOWN
				|| mode == StringRoundXfci1Plugin.HALF_TO_EVEN) {
			return true;

		} else if(mode == StringRoundXfci1Plugin.UP_SIGNIF
				|| mode == StringRoundXfci1Plugin.DOWN_SIGNIF
				|| mode == StringRoundXfci1Plugin.HALF_UP_SIGNIF
				|| mode == StringRoundXfci1Plugin.HALF_DOWN_SIGNIF
				|| mode == StringRoundXfci1Plugin.HALF_TO_EVEN_SIGNIF) {
			return false;

		} else {
			throw new ConnectorFatalException("Unknown rounding mode index: " + mode);
		}
	}
}
