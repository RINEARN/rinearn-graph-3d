package com.rinearn.graph3d.config.data;

//!!!!!
//Note: This enum is named `Series...` rather than `DataSeries...` because
//the public API treats both data-based plots and formula-based (math) plots uniformly as series.
//Using DataSeries... for this abstraction could sound unnatural when applied to formula (math) series.
//
//The term DataSeries... is commonly used in the internal implementation,
//where it refers specifically to data-oriented structures.
//
//In this externally exposed configuration layer, however,
//The `Data` prefix is INTENTIONALLY omitted by design.
//
//Do not add the Data prefix without careful consideration.
//Any such change should first be reviewed against the intended abstraction of the public API.
//!!!!!

/**
 * The enum to specify behavior of a series filter, used in OptionConfiguration, etc.
 */
public enum SeriesFilterMode {

	/** Represents no series filter is specified. */
	NONE,

	/** Uses the index-based series filter (IndexSeriesFilter class). */
	INDEX,

	/** Uses a custom implementation (a subclass) of SeriesFilter class. */
	CUSTOM,
}
