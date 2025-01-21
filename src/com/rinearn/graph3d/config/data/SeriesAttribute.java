package com.rinearn.graph3d.config.data;


/**
 * The container class for storing meta information of a data series.
 */
public class SeriesAttribute {

	/** The index of this series in all the series in the graph. */
	private volatile int globalSeriesIndex;

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
}
