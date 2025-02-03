package com.rinearn.graph3d.config.data;

/**
 * The object which stores multiple kinds of SeriesFilter instances, and provides their setters and getters.
 */
public final class SeriesFilterHub {

	/** The mode of the series filter. */
	private volatile SeriesFilterMode seriesFilterMode = SeriesFilterMode.NONE;

	/** The index-based series filter, used in INDEX mode. */
	private volatile IndexSeriesFilter indexSeriesFilter = new IndexSeriesFilter(new int[] { 0, 1, 2 });

	/** The custom implementation of a series filter, used in CUSTOM mode. */
	private volatile SeriesFilter customSeriesFilter = null;

	/**
	 * Sets the mode of the series filter.
	 *
	 * @param seriesFilterMode the mode of the series filter.
	 */
	public synchronized void setSeriesFilterMode(SeriesFilterMode seriesFilterMode) {
		this.seriesFilterMode = seriesFilterMode;
	}

	/**
	 * Gets the mode of the series filter.
	 *
	 * @param seriesFilterMode the mode of the series filter.
	 */
	public synchronized SeriesFilterMode getSeriesFilterMode() {
		return this.seriesFilterMode;
	}

	/**
	 * Gets the series filter corresponding to the current mode.
	 *
	 * @return The series filter corresponding to the current mode.
	 */
	public synchronized SeriesFilter getSeriesFilter() {
		switch (this.seriesFilterMode) {
			case INDEX: return this.indexSeriesFilter;
			case CUSTOM: return this.customSeriesFilter;
			default: throw new IllegalStateException("Unexpected series filter mode: " + this.seriesFilterMode);
		}
	}

	/**
	 * Sets the index-based series filter, used in INDEX mode.
	 *
	 * @param indexSeriesFilter The index-based series filter, used in INDEX mode.
	 */
	public synchronized void setIndexSeriesFilter(IndexSeriesFilter indexSeriesFilter) {
		this.indexSeriesFilter = indexSeriesFilter;
	}

	/**
	 * Gets the index-based series filter, used in INDEX mode.
	 *
	 * @return The index-based series filter, used in INDEX mode.
	 */
	public synchronized IndexSeriesFilter getIndexSeriesFilter() {
		return this.indexSeriesFilter;
	}

	/**
	 * Sets the custom implementation of a filter, used in CUSTOM mode.
	 *
	 * @param customSeriesFilter The custom implementation of a  series filter, used in CUSTOM mode.
	 */
	public synchronized void setCustomSeriesFilter(SeriesFilter customSeriesFilter) {
		this.customSeriesFilter = customSeriesFilter;
	}

	/**
	 * Gets the custom implementation of a filter, used in CUSTOM mode.
	 *
	 * @return The custom implementation of a  series filter, used in CUSTOM mode.
	 */
	public synchronized SeriesFilter getCustomSeriesFilter() {
		return this.customSeriesFilter;
	}
}
