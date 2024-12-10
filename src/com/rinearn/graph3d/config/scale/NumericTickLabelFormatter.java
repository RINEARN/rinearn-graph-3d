package com.rinearn.graph3d.config.scale;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;


/**
 * The formatter of displayed values of numeric tick labels.
 */
public final class NumericTickLabelFormatter extends TickLabelFormatter {

	/** The format of numeric tick labels, applied when |coordinate| = 0. */
	private volatile NumberFormat zeroRangeFormat = new DecimalFormat("0");
	// Note: Why we don't declare the above as DecimalFormat-type field is,
	//       some people may want to implement original format by extending NumberFormat.
	//       (NumberFormat is an abstract class, and DecimalFormat is one of its implementation.)

	/** The format of numeric tick labels, applied when |coordinate| < 0.1. */
	private volatile NumberFormat shortRangeFormat = new DecimalFormat("0.0#E0");

	/** The format of numeric tick labels, applied when 0.1 <= |coordinate| <= 10. */
	private volatile NumberFormat mediumRangeFormat = new DecimalFormat("0.0#");

	/** The format of numeric tick labels, applied when 10 < |coordinate|. */
	private volatile NumberFormat longRangeFormat = new DecimalFormat("0.0#E0");

	/** The threshold value between the 'short' range and the 'medium' range. */
	private volatile BigDecimal shortMediumThreshold = new BigDecimal("0.1");

	/** The threshold value between the 'medium' range and the 'long' range. */
	private volatile BigDecimal mediumLongThreshold = BigDecimal.TEN;


	/**
	 * Creates a new formatter for formatting tick coordinates into numeric tick labels.
	 */
	public NumericTickLabelFormatter() {
	}


	/**
	 * Sets the format for the coordinate zero (0).
	 *
	 * @param zeroRangeFormat The format for the coordinate zero (0).
	 */
	public void setZeroRangeFormat(NumberFormat zeroRangeFormat) {
		this.zeroRangeFormat = zeroRangeFormat;
	}

	/**
	 * Gets the format for the coordinate zero (0).
	 *
	 * @return The format for the coordinate zero (0).
	 */
	public NumberFormat getZeroRangeFormat() {
		return this.zeroRangeFormat;
	}


	/**
	 * Sets the format for coordinates in the short-range (|coordinate| < 0.1, by default).
	 *
	 * @param shortRangeFormat The format for coordinates in the short-range.
	 */
	public void setShorRangeFormat(NumberFormat shortRangeFormat) {
		this.shortRangeFormat = shortRangeFormat;
	}

	/**
	 * Gets the format for coordinates in the short-range (|coordinate| < 0.1, by default).
	 *
	 * @return The format for coordinates in the short-range.
	 */
	public NumberFormat getShorRangeFormat() {
		return this.shortRangeFormat;
	}


	/**
	 * Sets the format for coordinates in the medium-range (0.1 <= |coordinate| <= 10, by default).
	 *
	 * @param shortRangeFormat The format for coordinates in the medium-range.
	 */
	public void setMediumRangeFormat(NumberFormat mediumRangeFormat) {
		this.mediumRangeFormat = mediumRangeFormat;
	}

	/**
	 * Gets the format for coordinates in the medium-range (0.1 <= |coordinate| <= 10, by default).
	 *
	 * @return The format for coordinates in the medium-range.
	 */
	public NumberFormat getMediumRangeFormat() {
		return this.mediumRangeFormat;
	}



	/**
	 * Sets the format for coordinates in the long-range (10 < |coordinate|, by default).
	 *
	 * @param longRangeFormat The format for coordinates in the long-range.
	 */
	public void setLongRangeFormat(NumberFormat longRangeFormat) {
		this.longRangeFormat = longRangeFormat;
	}

	/**
	 * Gets the format for coordinates in the long-range (10 < |coordinate|, by default).
	 *
	 * @return The format for coordinates in the long-range.
	 */
	public NumberFormat getLongRangeFormat() {
		return this.longRangeFormat;
	}


	/**
	 * Sets the threshold value between the 'short' range and the 'medium' range.
	 *
	 * @param shortMediumThreshold The threshold value between the 'short' range and the 'medium' range.
	 */
	public void setShortMediumThreshold(BigDecimal shortMediumThreshold) {
		this.shortMediumThreshold = shortMediumThreshold;
	}

	/**
	 * Gets the threshold value between the 'short' range and the 'medium' range.
	 *
	 * @return shortMediumThreshold The threshold value between the 'short' range and the 'medium' range.
	 */
	public BigDecimal getShortMediumThreshold() {
		return this.shortMediumThreshold;
	}


	/**
	 * Sets the threshold value between the 'medium' range and the 'long' range.
	 *
	 * @param mediumLongThreshold The threshold value between the 'medium' range and the 'long' range.
	 */
	public void setMediumLongThreshold(BigDecimal mediumLongThreshold) {
		this.mediumLongThreshold = mediumLongThreshold;
	}

	/**
	 * Gets the threshold value between the 'medium' range and the 'long' range.
	 *
	 * @return shortMediumThreshold The threshold value between the 'medium' range and the 'long' range.
	 */
	public BigDecimal getMediumLongThreshold() {
		return this.mediumLongThreshold;
	}


	/**
	 * Formats the specified coordinate value into a label text,
	 *
	 * @param tickCoordinate The coordinate of the numeric tick to be formatted.
	 * @return The formatted tick label.
	 */
	@Override
	public synchronized String format(BigDecimal tickCoordinate) {
		BigDecimal absCoordinate = tickCoordinate.abs();

		if (absCoordinate.compareTo(BigDecimal.ZERO) == 0) {
			return this.zeroRangeFormat.format(tickCoordinate);
		}
		if (absCoordinate.compareTo(this.shortMediumThreshold) < 0) {
			return this.shortRangeFormat.format(tickCoordinate);
		}
		if (0 < absCoordinate.compareTo(this.mediumLongThreshold)) {
			return this.longRangeFormat.format(tickCoordinate);
		}
		return this.mediumRangeFormat.format(tickCoordinate);
	}

	/**
	 * Returns whether the specified coordinate is formattable by this formatter.
	 *
	 * @param tickCoordinate Returns if the specified coordinate is formattable.
	 */
	@Override
	public synchronized boolean isFormattable(BigDecimal coordinate) {
		return true;
	}
}
