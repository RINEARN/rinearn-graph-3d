package com.rinearn.graph3d.config;

import com.rinearn.graph3d.config.data.SeriesAttribute;


/**
 * The class for storing data-related configuration.
 *
 * Please note that, some fields of this class's instance may be modified automatically when plotting data.
 */
public class DataConfiguration {

	/** The array storing series attributes of all the data series. */
	private volatile SeriesAttribute[] globalSeriesAttributes = new SeriesAttribute[0];


	/**
	 * Sets the series attributes of all the data series.
	 *
	 * Please note that, the series attributes are modified automatically when plotting data.
	 *
	 * @param globalSeriesAttributes The array storing series attributes of all the data series.
	 */
	public synchronized void setGlobalSeriesAttributes(SeriesAttribute[] globalSeriesAttributes) {
		this.globalSeriesAttributes = globalSeriesAttributes;
	}

	/**
	 * Gets the series attributes of all the data series.
	 *
	 * Please note that, the series attributes are modified automatically when plotting data.
	 *
	 * @return The array storing series attributes of all the data series.
	 */
	public synchronized SeriesAttribute[] getGlobalSeriesAttribute() {
		return this.globalSeriesAttributes;
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
		if (this.globalSeriesAttributes == null) {
			throw new IllegalStateException("The series attributes is null.");
		}
	}
}
