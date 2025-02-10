package com.rinearn.graph3d.config.data;


/**
 * The container class for storing meta information of a data series.
 */
public class SeriesAttribute {

	/** The index of this series in all the series in the graph. */
	private volatile int globalSeriesIndex = 0;

	/** The index of this series in the source file or the source math expression. */
	private volatile int localSeriesIndex = 0;

	/** The modifiable legend of this series.*/
	private volatile String modifiableLegend = "";

	/** The unmodified (original) legend of this series.*/
	private volatile String unmodifiedLegend = "";


	/**
	 * Creates a new instance;
	 */
	public SeriesAttribute() {
	}


	/**
	 * Sets the index of this series in all the series in the graph, called "global index".
	 *
	 * @param globalSeriesIndex The global index of this series.
	 */
	public synchronized void setGlobalSeriesIndex(int globalSeriesIndex) {
		this.globalSeriesIndex = globalSeriesIndex;
	}

	/**
	 * Gets the index of this series all the series in the graph, called "global index".
	 *
	 * @return The global index of this series.
	 */
	public synchronized int getGlobalSeriesIndex() {
		return this.globalSeriesIndex;
	}


	/**
	 * Sets the index of this series in the source file or the source math expression, called "local index".
	 *
	 * @param localSeriesIndex The local index of this series.
	 */
	public synchronized void setLocalSeriesIndex(int localSeriesIndex) {
		this.localSeriesIndex = localSeriesIndex;
	}

	/**
	 * Gets the index of this series in the source file or the source math expression, called "local index".
	 *
	 * @return The local index of this series.
	 */
	public synchronized int getLocalSeriesIndex() {
		return this.localSeriesIndex;
	}


	/**
	 * Sets the modifiable legend of this series.
	 *
	 * @param modifiableLegend The modifiable legend of this series.
	 */
	public synchronized void setModifiableLegend(String modifiableLegend) {
		this.modifiableLegend = modifiableLegend;
	}

	/**
	 * Gets the modifiable legend of this series.
	 *
	 * @return The modifiable legend of this series.
	 */
	public synchronized String getModifiableLegend() {
		return this.modifiableLegend;
	}


	/**
	 * Sets the unmodified (original) legend of this series.
	 *
	 * @param unmodifiedLegend The unmodified (original) legend of this series.
	 */
	public synchronized void setUnmodifiedLegend(String unmodifiedLegend) {
		this.unmodifiedLegend = unmodifiedLegend;
	}

	/**
	 * Gets the unmodified (original) legend of this series.
	 *
	 * @return The unmodified (original) of this series.
	 */
	public synchronized String getUnmodifiedLegend() {
		return this.unmodifiedLegend;
	}
}
