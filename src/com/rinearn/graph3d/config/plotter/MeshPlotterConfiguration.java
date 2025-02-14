package com.rinearn.graph3d.config.plotter;

import com.rinearn.graph3d.config.RinearnGraph3DConfigurationException;


/**
 * The class storing configuration values of "With Meshes" option.
 */
public final class MeshPlotterConfiguration extends SeriesFilterablePlotterConfiguration {

	/** The width (in pixels) of lines composing meshes plotted by this option. */
	private volatile double lineWidth = 1.0;

	/**
	 * Creates a new instance.
	 */
	public MeshPlotterConfiguration() {
		super.setPlotterEnabled(false);
	}

	/**
	 * Sets the width (in pixels) of lines composing meshes plotted by this option.
	 *
	 * @param lineWidth The width (in pixels) of lines.
	 */
	public synchronized void setLineWidth(double lineWidth) {
		this.lineWidth = lineWidth;
	}

	/**
	 * Gets the width (in pixels) of lines composing meshes plotted by this option.
	 *
	 * @return The width (in pixels) of lines.
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
	 * @throws RinearnGraph3DConfigurationException Thrown when incorrect or inconsistent settings are detected.
	 */
	public synchronized void validate() throws RinearnGraph3DConfigurationException {
		if (this.lineWidth < 0.0) {
			throw new RinearnGraph3DConfigurationException("The line width is negative, must be zero or positive value.");
		}
	}
}
