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
	 * Gets the configurations of the ranges of the extra dimensions.
	 *
	 * @return The configurations of the extra dimensions.
	 */
	public synchronized AxisRangeConfiguration[] getExtraDimensionRangeConfigurations() {
		return this.extraDimensionRangeConfigurations;
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
	public synchronized void validate() throws IllegalStateException {

		// Validate configs of X/Y/Z axes.
		this.xRangeConfiguration.validate();
		this.yRangeConfiguration.validate();
		this.zRangeConfiguration.validate();

		// Validate configs of extra dimensions.
		if (this.extraDimensionRangeConfigurations == null) {
			throw new IllegalStateException("The extra-dimension's range configurations are null.");
		}
		for (AxisRangeConfiguration extraDimConfig: this.extraDimensionRangeConfigurations) {
			if (extraDimConfig == null) {
				throw new IllegalStateException("There is a null element in the extra dimension's range configurations.");
			}
			extraDimConfig.validate();
		}
	}

	/**
	 * The class storing configuration values of the range of an axis (X, Y, or Z).
	 */
	public static class AxisRangeConfiguration {

		/** The minimum value of this range. */
		private volatile BigDecimal min = BigDecimal.ONE.negate();

		/** The maximum value of this range. */
		private volatile BigDecimal max = BigDecimal.ONE;

		/** The flag representing whether the auto-ranging feature is enabled. For details, see the setter method. */
		private volatile boolean autoRangingEnabled = true;

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

		/**
		 * Turns on/off the auto-ranging feature.
		 *
		 * When this feature is enabled, the range are set automatically
		 * from the minimum and the maximum coordinate values in the data, when plot it.
		 *
		 * @param enabled Specify true/false for turning on/off the auto-ranging feature (the default is on).
		 */
		public synchronized void setAutoRangingEnabled(boolean enabled) {
			this.autoRangingEnabled = enabled;
		}

		/**
		 * Gets whether the auto-ranging feature is enabled.
		 *
		 * @return Returns true if the auto-ranging feature is enabled.
		 */
		public synchronized boolean isAutoRangingEnabled() {
			return this.autoRangingEnabled;
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
		public synchronized void validate() throws IllegalStateException {
			if (this.min == null) {
				throw new IllegalStateException("The minimum value of the range is null.");
			}
			if (this.max == null) {
				throw new IllegalStateException("The maximum value of the range is null.");
			}
		}
	}
}
