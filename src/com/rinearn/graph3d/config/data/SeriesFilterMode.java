package com.rinearn.graph3d.config.data;

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
