package com.rinearn.graph3d.config.plotter;

import java.math.BigDecimal;

import com.rinearn.graph3d.config.RinearnGraph3DConfigurationException;


/**
 * The class storing configuration values of "With Contours" option.
 */
public final class ContourPlotterConfiguration extends SeriesFilterablePlotterConfiguration {

	/** The width (in pixels) of contour lines. */
	private volatile double lineWidth = 1.0;

	/** The number of the sections between contour lines. */
	private volatile int sectionCount = 10;

	/** The flag representing whether the auto-range feature is enabled. */
	private volatile boolean autoRangeEnabled = true;

	/** The minimum coordinate of the contours. */
	private volatile BigDecimal minimumCoordinate = BigDecimal.ONE.negate();

	/** The maximum coordinate of the contours. */
	private volatile BigDecimal maximumCoordinate = BigDecimal.ONE;


	/**
	 * Creates a new instance.
	 */
	public ContourPlotterConfiguration() {
		super.setPlotterEnabled(false);
	}


	/**
	 * Sets the width (in pixels) of contour lines.
	 *
	 * @param lineWidth The width (in pixels) of contour lines.
	 */
	public synchronized void setLineWidth(double lineWidth) {
		this.lineWidth = lineWidth;
	}

	/**
	 * Gets the width (in pixels) of contour lines.
	 *
	 * @return The width (in pixels) of contour lines.
	 */
	public synchronized double getLineWidth() {
		return this.lineWidth;
	}


	/**
	 * Sets the number of the sections between contour lines.
	 *
	 * @param sectionCount The number of the sections between contour lines.
	 */
	public synchronized void setSectionCount(int sectionCount) {
		this.sectionCount = sectionCount;
	}

	/**
	 * Gets the number of the sections between contour lines.
	 *
	 * @return The number of the sections between contour lines.
	 */
	public synchronized int getSectionCount() {
		return this.sectionCount;
	}


	/**
	 * Enables/disables the auto-range feature.
	 *
	 * @param enabled Specify true to enable the auto-range feature.
	 */
	public synchronized void setAutoRangeEnabled(boolean enabled) {
		this.autoRangeEnabled = enabled;
	}

	/**
	 * Gets whether the auto-range feature is enabled.
	 *
	 * @return Returns true if the auto-range feature is enabled.
	 */
	public synchronized boolean isAutoRangeEnabled() {
		return this.autoRangeEnabled;
	}


	/**
	 * Sets the minimum coordinate of contours.
	 *
	 * @param minimumCoordinate The minimum coordinate of contours.
	 */
	public synchronized void setMinimumCoordinate(BigDecimal minimumCoordinate) {
		this.minimumCoordinate = minimumCoordinate;
	}

	/**
	 * Gets the minimum coordinate of contours.
	 *
	 * @return The minimum coordinate of contours.
	 */
	public synchronized BigDecimal getMinimumCoordinate() {
		return this.minimumCoordinate;
	}


	/**
	 * Sets the maximum coordinate of contours.
	 *
	 * @param maximumCoordinate The maximum coordinate of contours.
	 */
	public synchronized void setMaximumCoordinate(BigDecimal maximumCoordinate) {
		this.maximumCoordinate = maximumCoordinate;
	}

	/**
	 * Gets the maximum coordinate of contours.
	 *
	 * @return The maximum coordinate of contours.
	 */
	public synchronized BigDecimal getMaximumCoordinate() {
		return this.maximumCoordinate;
	}


	/**
	 * Validates correctness and consistency of configuration parameters stored in this instance.
	 *
	 * This method is called when this configuration is specified to RinearnGraph3D or its renderer.
	 * If no issue is detected, nothing occurs.
	 * If any issue is detected, throws IllegalStateException.
	 *
	 * @throws RinearnGraph3DConfigurationException Thrown when incorrect or inconsistent settings are detected.
	 */
	public synchronized void validate() throws RinearnGraph3DConfigurationException {
	}
}