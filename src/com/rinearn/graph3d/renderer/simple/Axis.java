package com.rinearn.graph3d.renderer.simple;

import java.math.BigDecimal;

/**
 * A class storing/handling informations related to an axis.
 * (e.g.: Maximum and minimum values of the range of the axis, and so on.)
 */
public class Axis {

	/** Stores the minimum value of the range of this axis. */
	private volatile BigDecimal rangeMin;

	/** Stores the maximum value of the range of this axis. */
	private volatile BigDecimal rangeMax;

	/** Stores the minimum value of the range of this axis, as the double-type value. */
	private volatile double rangeMinDoubleValue;

	/** Stores the maximum value of the range of this axis, as the double-type value. */
	private volatile double rangeMaxDoubleValue;

	/**
	 * The margin for comparing a double-type coordinate value and the rangeMinDoubleValue,
	 * considering tiny numerical errors of binary floating-point numbers.
	 */
	private volatile double rangeMinDoubleMargin;

	/**
	 * The margin for comparing a double-type coordinate value and the rangeMaxDoubleValue,
	 * considering tiny numerical errors of binary floating-point numbers.
	 */
	private volatile double rangeMaxDoubleMargin;

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
}
