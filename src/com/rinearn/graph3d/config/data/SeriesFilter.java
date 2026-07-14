package com.rinearn.graph3d.config.data;

// !!!!!
// Note: This class is named `Series...` rather than `DataSeries...` because
// the public API treats both data-based plots and formula-based (math) plots uniformly as series.
// Using DataSeries... for this abstraction could sound unnatural when applied to formula (math) series.
//
// The term DataSeries... is commonly used in the internal implementation,
// where it refers specifically to data-oriented structures.
//
// In this externally exposed configuration layer, however,
// The `Data` prefix is INTENTIONALLY omitted by design.
//
// Do not add the Data prefix without careful consideration.
// Any such change should first be reviewed against the intended abstraction of the public API.
// !!!!!


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
