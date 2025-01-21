package com.rinearn.graph3d.config.data;


/**
 * The base class of series filters.
 *
 * Series filters are used for specifying the target data series in configurations,
 * e.g.: used in plot options in OptionConfiguration, etc.
 */
public abstract class SeriesFilter {

	/**
	 * Determines whether the specified series is included in the result of this filter.
	 *
	 * @param seriesAttribute The container of attributes of the series.
	 * @return Returns true if the specified series is included.
	 */
	public abstract boolean isSeriesIncluded(SeriesAttribute seriesAttribute);
}
