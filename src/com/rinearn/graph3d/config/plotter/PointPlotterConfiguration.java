package com.rinearn.graph3d.config.plotter;

import com.rinearn.graph3d.config.RinearnGraph3DConfigurationException;


/**
 * The class storing configuration values of "With Points" option.
 */
public final class PointPlotterConfiguration extends SeriesFilterablePlotterConfiguration {

	/** The style mode for drawing points. */
	private volatile PointStyleMode pointStyleMode = PointStyleMode.CIRCLE;

	/** The radius (in pixels) of points in CIRCLE mode. */
	private volatile double circleRadius = 2.0;

	/** The font size of markers in MARKER mode. */
	private volatile double markerSize = 10.0;

	/** The ratio to correct the vartical position of markers. */
	private volatile double markerVerticalOffsetRatio = 0.2;

	/** The texts (of the symbols) of the markers. */
	private volatile String[] markerTexts = { "●", "■", "▲", "▼", "〇", "□", "△", "▽", "◇", "×" };

	/** The flag to whether draw markers in bold fonts. */
	private volatile boolean markerBold = false;

	/**
	 * Creates a new instance.
	 */
	public PointPlotterConfiguration() {
		super.setPlotterEnabled(true);
	}

	/**
	 * Sets the style mode for drawing points.
	 *
	 * @param pointStyleMode The style mode for drawing points.
	 */
	public synchronized void setPointStyleMode(PointStyleMode pointStyleMode) {
		this.pointStyleMode = pointStyleMode;
	}

	/**
	 * Gets the style mode for drawing points.
	 *
	 * @return The style mode for drawing points.
	 */
	public synchronized PointStyleMode getPointStyleMode() {
		return this.pointStyleMode;
	}

	/**
	 * Sets the radius (in pixels) of points in CIRCLE mode.
	 *
	 * @param pointRadius The radius (in pixels) of points plotted by this option.
	 */
	public synchronized void setCircleRadius(double circleRadius) {
		this.circleRadius = circleRadius;
	}

	/**
	 * Gets the radius (in pixels) of points in CIRCLE mode.
	 *
	 * @return The radius (in pixels) of points plotted by this option.
	 */
	public synchronized double getCircleRadius() {
		return this.circleRadius;
	}

	/**
	 * Sets the font size of markers.
	 *
	 * @param markerSize The font size of markers.
	 */
	public synchronized void setMarkerSize(double markerSize) {
		this.markerSize = markerSize;
	}

	/**
	 * Gets the radius (in pixels) of points plotted by this option.
	 *
	 * @return The font size of markers.
	 */
	public synchronized double getMarkerSize() {
		return this.markerSize;
	}

	/**
	 * Sets the ratio to correct the vartical position of markers.
	 *
	 * @param markerVerticalOffsetRatio The ratio to correct the vartical position of markers.
	 */
	public void setMarkerVerticalOffsetRatio(double markerVerticalOffsetRatio) {
		this.markerVerticalOffsetRatio = markerVerticalOffsetRatio;
	}

	/**
	 * Gets the ratio to correct the vartical position of markers.
	 *
	 * @return markerVerticalOffsetRatio The ratio to correct the vartical position of markers.
	 */
	public double getMarkerVerticalOffsetRatio() {
		return this.markerVerticalOffsetRatio;
	}

	/**
	 * Sets the texts (of the symbols) of the markers.
	 *
	 * @param markerTexts The texts (of the symbols) of the markers.
	 */
	public void setMarkerTexts(String[] markerTexts) {
		this.markerTexts = markerTexts;
	}

	/**
	 * Gets the texts (of the symbols) of the markers.
	 *
	 * @return The texts (of the symbols) of the markers.
	 */
	public String[] getMarkerTexts() {
		return this.markerTexts;
	}

	/**
	 * Sets the flag to whether draw markers in bold fonts.
	 *
	 * @param markerBold The flag to whether draw markers in bold fonts.
	 */
	public void setMarkerBold(boolean markerBold) {
		this.markerBold = markerBold;
	}

	/**
	 * Gets the flag to whether draw markers in bold fonts.
	 *
	 * @return The flag to whether draw markers in bold fonts.
	 */
	public boolean isMarkerBold() {
		return this.markerBold;
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
		if (this.pointStyleMode == null) {
			throw new RinearnGraph3DConfigurationException("The point style mode is null.");
		}
		if (this.circleRadius < 0.0) {
			throw new RinearnGraph3DConfigurationException("The circle radius is negative, must be zero or positive value.");
		}
		if (this.markerSize < 0.0) {
			throw new RinearnGraph3DConfigurationException("The marker size is negative, must be zero or positive value.");
		}
		if (this.markerTexts == null) {
			throw new RinearnGraph3DConfigurationException("The marker texts is null.");
		}
		if (this.markerTexts.length == 0) {
			throw new RinearnGraph3DConfigurationException("The number of marker texts is 0, but at least 1 element is required.");
		}
	}
}
