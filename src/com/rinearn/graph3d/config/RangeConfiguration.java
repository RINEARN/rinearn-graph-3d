package com.rinearn.graph3d.config;

import java.math.BigDecimal;

/**
 * The class storing configuration values of the ranges of X/Y/Z axes.
 */
public final class RangeConfiguration {

	/** The configuration of the range of X axis. */
	private volatile AxisRangeConfiguration xRangeConfiguration = new AxisRangeConfiguration();

	/** The configuration of the range of Y axis. */
	private volatile AxisRangeConfiguration yRangeConfiguration = new AxisRangeConfiguration();

	/** The configuration of the range of Z axis. */
	private volatile AxisRangeConfiguration zRangeConfiguration = new AxisRangeConfiguration();

	/** The configurations of the ranges of the extra dimensions. */
	private volatile AxisRangeConfiguration[] extraDimensionRangeConfigurations = new AxisRangeConfiguration[0];


	/**
	 * Creates a new configuration storing default values.
	 */
	public RangeConfiguration() {
	}


	/**
	 * Sets the configuration of the range of X axis.
	 * 
	 * @param xRangeConfiguration The configuration of the range of X axis.
	 */
	public synchronized void setXRangeConfiguration(AxisRangeConfiguration xRangeConfiguration) {
		this.xRangeConfiguration = xRangeConfiguration;
	}

	/**
	 * Gets the configuration of the range of X axis.
	 * 
	 * @return The configuration of the range of X axis.
	 */
	public synchronized AxisRangeConfiguration getXRangeConfiguration() {
		return this.xRangeConfiguration;
	}

	/**
	 * Sets the configuration of the range of Y axis.
	 * 
	 * @param yRangeConfiguration The configuration of the range of Y axis.
	 */
	public synchronized void setYRangeConfiguration(AxisRangeConfiguration yRangeConfiguration) {
		this.yRangeConfiguration = yRangeConfiguration;
	}

	/**
	 * Gets the configuration of the range of Y axis.
	 * 
	 * @return The configuration of the range of Y axis.
	 */
	public synchronized AxisRangeConfiguration getYRangeConfiguration() {
		return this.yRangeConfiguration;
	}

	/**
	 * Sets the configuration of the range of Z axis.
	 * 
	 * @param zRangeConfiguration The configuration of the range of Z axis.
	 */
	public synchronized void setZRangeConfiguration(AxisRangeConfiguration zRangeConfiguration) {
		this.zRangeConfiguration = zRangeConfiguration;
	}

	/**
	 * Gets the configuration of the range of Z axis.
	 * 
	 * @return The configuration of the range of Z axis.
	 */
	public synchronized AxisRangeConfiguration getZRangeConfiguration() {
		return this.zRangeConfiguration;
	}

	/**
	 * Gets the total number of the extra dimensions.
	 * 
	 * @return The total number of the extra dimensions.
	 */
	public synchronized int getExtraDimensionCount() {
		return this.extraDimensionRangeConfigurations.length;
	}

	/**
	 * Sets the configurations of the ranges of the extra dimensions.
	 * 
	 * @param extraDimensionRangeConfigurations The configurations of the extra dimensions.
	 */
	public synchronized void setExtraDimensionRangeConfigurations(
			AxisRangeConfiguration[] extraDimensionRangeConfigurations) {
		this.extraDimensionRangeConfigurations = extraDimensionRangeConfigurations;
	}


	/**
	 * The class storing configuration values of the range of an axis (X, Y, or Z).
	 */
	public static class AxisRangeConfiguration {

		/** The minimum value of this range. */
		private volatile BigDecimal min = BigDecimal.ONE.negate();

		/** The maximum value of this range. */
		private volatile BigDecimal max = BigDecimal.ONE;

		/**
		 * Sets the minimum value of this range.
		 * 
		 * @param min The minimum value of this range.
		 */
		public synchronized void setMinimum(BigDecimal min) {
			this.min = min;
		}

		/**
		 * Gets the minimum value of this range.
		 * 
		 * @return The minimum value of this range.
		 */
		public synchronized BigDecimal getMinimum() {
			return this.min;
		}

		/**
		 * Sets the maximum value of this range.
		 * 
		 * @param max The maximum value of this range.
		 */
		public synchronized void setMaximum(BigDecimal max) {
			this.max = max;
		}

		/**
		 * Gets the maximum value of this range.
		 * 
		 * @return The maximum value of this range.
		 */
		public synchronized BigDecimal getMaximum() {
			return this.max;
		}
	}
}
