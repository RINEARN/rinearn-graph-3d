package com.rinearn.graph3d.config.scale;

/**
 * The enum representing each mode for specifying alignment of ticks of a scale.
 */
public enum TickerMode {

	/** Divides an axis's range (from min to max) equally by scale ticks. */
	EQUAL_DIVISION,

	/** Align scale ticks as an arithmetic sequence. */
	ARITHMETIC_PROGRESSION,

	/** Align scale ticks as a geometric sequence. */
	GEOMETRIC_PROGRESSION,

	// !!!!! Note !!!!!  Should be renamed to 'AUTO' ?
	// -> このソフトでは setAutoRangingEnabled とか、短縮形接頭語の auto を既に結構使ってるので、auto で統一してもいいんじゃない？ ここだけ厳格にしてもなぁって感じ。要検討
	/** Align scale ticks automatically. */
	AUTOMATIC,

	/** Align scale ticks manually, and also specify arbitrary tick labels. */
	MANUAL,

	/** Uses the custom ticker implemented by users or third party developers. */
	CUSTOM,
}
