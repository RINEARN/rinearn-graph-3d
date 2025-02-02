package com.rinearn.graph3d.config.scale;

/**
 * The enum representing each mode to format tick labels.
 */
public enum TickLabelFormatterMode {

	// !!!!! Note !!!!!  Should be renamed to 'AUTOMATIC' ?
	// -> 上の enum の AUTOMATIC のコメント参照。少なくともどっちかに統一はしておきたい。今のところは AUTO が有力かな。要検討。
	/** Formats the tick coordinates into the numeric labels in the default format.  */
	AUTO,

	/** Formats the tick coordinates into the numeric labels in the user-specified format.  */
	NUMERIC,

	/** Uses the custom formmatter implemented by users or third party developers. */
	CUSTOM,
}
