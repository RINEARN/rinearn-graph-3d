package com.rinearn.graph3d.config.data;


/**
 * The container class for storing meta information of a data series.
 */
public class SeriesAttribute {

	/** The index of this series in all the series in the graph. */
	private volatile int globalSeriesIndex = 0;

	/** The legend of this series.*/
	private volatile String legend = "";


	/**
	 * Creates a new instance;
	 */
	public SeriesAttribute() {
	}


	/**
	 * Sets the index of this series in all the series in the graph.
	 *
	 * @param globalSeriesIndex The index of this series in all the series in the graph.
	 */
	public synchronized void setGlobalSeriesIndex(int globalSeriesIndex) {
		this.globalSeriesIndex = globalSeriesIndex;
	}

	/**
	 * Gets the index of this series in all the series in the graph.
	 *
	 * @return The index of this series in all the series in the graph.
	 */
	public synchronized int getGlobalSeriesIndex() {
		return this.globalSeriesIndex;
	}


	/**
	 * Sets the legend of this series.
	 *
	 * @param legend The legend of this series.
	 */
	public synchronized void setLegend(String lenegnd) {
		this.legend = legend;
	}

	/**
	 * Gets the legend of this series.
	 *
	 * @return The legend of this series.
	 */
	public synchronized String getLegend() {
		return this.legend;
	}
}
