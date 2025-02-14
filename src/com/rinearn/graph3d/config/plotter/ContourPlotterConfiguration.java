package com.rinearn.graph3d.config.plotter;


/**
 * The class storing configuration values of "With Contours" option.
 */
public final class ContourPlotterConfiguration extends SeriesFilterablePlotterConfiguration {

	/**
	 * Creates a new instance.
	 */
	public ContourPlotterConfiguration() {
		super.setPlotterEnabled(false);
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
	}
}