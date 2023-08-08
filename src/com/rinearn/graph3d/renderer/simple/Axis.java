package com.rinearn.graph3d.renderer.simple;

import java.math.BigDecimal;

/**
 * A class storing/handling informations related to an axis.
 * (e.g.: Maximum and minimum values of the range of the axis, and so on.)
 */
public class Axis {

	/** Stores the minimum value of the range of this axis. */
	private volatile BigDecimal rangeMin = BigDecimal.ONE.negate(); // -1.0

	/** Stores the maximum value of the range of this axis. */
	private volatile BigDecimal rangeMax = BigDecimal.ONE; // 1.0

	/** Stores the minimum value of the range of this axis, as the double-type value. */
	private volatile double rangeMinDoubleValue = -1.0;

	/** Stores the maximum value of the range of this axis, as the double-type value. */
	private volatile double rangeMaxDoubleValue = 1.0;

	/**
	 * The margin for comparing a double-type coordinate value and the rangeMinDoubleValue,
	 * considering tiny numerical errors of binary floating-point numbers.
	 * This value does not take negative values (the value must be positive or zero).
	 */
	private volatile double rangeMinDoubleMargin = MARGINE_RATIO;

	/**
	 * The margin for comparing a double-type coordinate value and the rangeMaxDoubleValue,
	 * considering tiny numerical errors of binary floating-point numbers.
	 * This value does not take negative values (the value must be positive or zero).
	 */
	private volatile double rangeMaxDoubleMargin = MARGINE_RATIO;

	/** The ratio of rangeMinDoubleMargin (or rangeMaxDoubleMargin) to rangeMinDoubleValue (or rangeMaxDoubleValue). */
	private static final double MARGINE_RATIO = 1.0E-12;


	/**
	 * Sets the range of this axis.
	 * 
	 * @param min The minimum value of the range of this axis.
	 * @param max The maximum value of the range of this axis.
	 */
	public synchronized void setRange(BigDecimal min, BigDecimal max) {

		// Update precise values of the range.
		this.rangeMin = min;
		this.rangeMax = max;

		// Update values which will be used for double-double comparisons.
		this.rangeMinDoubleValue = min.doubleValue();
		this.rangeMaxDoubleValue = max.doubleValue();

		// Update margins of double-double comparisons.
		this.rangeMinDoubleMargin = Math.abs(this.rangeMinDoubleValue * MARGINE_RATIO);
		this.rangeMaxDoubleMargin = Math.abs(this.rangeMaxDoubleValue * MARGINE_RATIO);
	}


	/**
	 * Gets the minimum value of the range of this axis.
	 * 
	 * @return The minimum value of the range of this axis.
	 */
	public synchronized BigDecimal getRangeMin() {
		return this.rangeMin;
	}


	/**
	 * Gets the maximum value of the range of this axis.
	 * 
	 * @return The maximum value of the range of this axis.
	 */
	public synchronized BigDecimal getRangeMax() {
		return this.rangeMax;
	}


	/**
	 * Determines whether the specified coordinate value is in the range of this axis.
	 * 
	 * If true is specified to considersMargins,
	 * this method regards a double-type coordinate value is in the range, 
	 * if it is greater than or equals to rangeMinDoubleValue - rangeMinDoubleMargin, 
	 * and is less than or equals to rangneMaxDoubleValue + rangeMaxDoubleMargin.
	 * This is the behavior to address tiny errors contained in double-type values.
	 * 
	 * @param coordinateValue The coordinate value to be determined.
	 * @param considersMargins Specify true if you enable margins to address tiny errors of coordinate values.
	 * @return Returns true if the specified coordinate value is in the range.
	 */
	public synchronized boolean containsCoordinate(double coordinateValue, boolean considersMargins) {

		// If considersMargins is true, use margins set to the fields. Otherwise use 0 (= no margins).
		double minMargin = considersMargins ? this.rangeMinDoubleMargin : 0.0;
		double maxMargin = considersMargins ? this.rangeMaxDoubleMargin : 0.0;

		// Compare the specified coordinate value to the min/max values (with margins, if required).
		boolean greaterThanOrEqualsToMin = this.rangeMinDoubleValue - minMargin <= coordinateValue;
		boolean lessThanOrEqualsToMax = coordinateValue <= this.rangeMaxDoubleValue + maxMargin;
		boolean isInRange = greaterThanOrEqualsToMin && lessThanOrEqualsToMax;
		return isInRange;
	}


	/**
	 * Scales the specified coordinate values, into the coordinate values in the "scaled space".
	 * 
	 * In the scaled space,
	 * the maximum value of the range of the axis is mapped to 1.0,
	 * and the minimum value is mapped to -1.0.
	 * 
	 * @param rawCoordinateValue The coordinate value to be scaled.
	 * @return The scaled coordinate value.
	 */
	public synchronized double scaleCoordinate(double rawCoordinateValue) {
		
		// Firstly, scale into the range [0.0, 1.0].
		double axisLength = this.rangeMaxDoubleValue - this.rangeMinDoubleValue;
		double scaledInto01 = (rawCoordinateValue - this.rangeMinDoubleValue) / axisLength;

		// Then, scale into the range [-1.0, 1.0].
		double scaledInto11 = scaledInto01 * 2.0 - 1.0;
		return scaledInto11;
	}
}
