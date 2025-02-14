package com.rinearn.graph3d.config.plotter;


/**
 * The class storing configuration values of "With Lines" option.
 */
public final class LinePlotterConfiguration extends SeriesFilterablePlotterConfiguration {

	/** The width (in pixels) of lines plotted by this option. */
	private volatile double lineWidth = 1.0;

	/**
	 * Creates a new instance.
	 */
	public LinePlotterConfiguration() {
		super.setPlotterEnabled(false);
	}

	/**
	 * Sets the width (in pixels) of lines plotted by this option.
	 *
	 * @param lineWidth The width (in pixels) of lines plotted by this option.
	 */
	public synchronized void setLineWidth(double lineWidth) {
		this.lineWidth = lineWidth;
	}

	/**
	 * Gets the width (in pixels) of lines plotted by this option.
	 *
	 * @return The width (in pixels) of lines plotted by this option.
	 */
	public synchronized double getLineWidth() {
		return this.lineWidth;
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
		if (this.lineWidth < 0.0) {
			throw new IllegalStateException("The line width is negative, must be zero or positive value.");
		}
	}
}
