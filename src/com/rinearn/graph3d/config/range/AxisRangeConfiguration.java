package com.rinearn.graph3d.config.range;

import com.rinearn.graph3d.config.RinearnGraph3DConfigurationException;

import java.math.BigDecimal;


/**
 * The class storing configuration values of the range of an axis (X, Y, or Z).
 */
public final class AxisRangeConfiguration {

	/** The minimum value of this range. */
	private volatile BigDecimal min = BigDecimal.ONE.negate();

	/** The maximum value of this range. */
	private volatile BigDecimal max = BigDecimal.ONE;

	/** The flag representing whether the auto-range feature is enabled. For details, see the setter method. */
	private volatile boolean autoRangeEnabled = true;

	/**
	 * Sets the minimum coordinate of this range.
	 *
	 * @param min The minimum coordinate of this range.
	 */
	public synchronized void setMinimumCoordinate(BigDecimal min) {
		this.min = min;
	}

	/**
	 * Gets the minimum coordinate of this range.
	 *
	 * @return The minimum coordinate of this range.
	 */
	public synchronized BigDecimal getMinimumCoordinate() {
		return this.min;
	}

	/**
	 * Sets the maximum coordinate of this range.
	 *
	 * @param max The maximum coordinate of this range.
	 */
	public synchronized void setMaximumCoordinate(BigDecimal max) {
		this.max = max;
	}

	/**
	 * Gets the maximum coordinate of this range.
	 *
	 * @return The maximum coordinate of this range.
	 */
	public synchronized BigDecimal getMaximumCoordinate() {
		return this.max;
	}

	/**
	 * Turns on/off the auto-range feature.
	 *
	 * When this feature is enabled, the range are set automatically
	 * from the minimum and the maximum coordinate values in the data, when plot it.
	 *
	 * @param enabled Specify true/false for turning on/off the auto-ranging feature (the default is on).
	 */
	public synchronized void setAutoRangeEnabled(boolean enabled) {
		this.autoRangeEnabled = enabled;
	}

	/**
	 * Gets whether the auto-range feature is enabled.
	 *
	 * @return Returns true if the auto-ranging feature is enabled.
	 */
	public synchronized boolean isAutoRangeEnabled() {
		return this.autoRangeEnabled;
	}

	/**
	 * Validates correctness and consistency of configuration parameters stored in this instance.
	 *
	 * This method is called when this configuration is specified to RinearnGraph3D or its renderer.
	 * If no issue is detected, nothing occurs.
	 * If any issue is detected, throws IllegalStateException.
	 *
	 * @throws IllegalStateException Thrown when incorrect or inconsistent settings are detected.
	 */
	public synchronized void validate() throws RinearnGraph3DConfigurationException {
		if (this.min == null) {
			throw new RinearnGraph3DConfigurationException("The minimum value of the range is null.");
		}
		if (this.max == null) {
			throw new RinearnGraph3DConfigurationException("The maximum value of the range is null.");
		}
	}
}