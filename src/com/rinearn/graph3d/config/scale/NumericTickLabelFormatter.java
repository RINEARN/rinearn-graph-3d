package com.rinearn.graph3d.config.scale;

import java.math.BigDecimal;
import java.text.NumberFormat;


/**
 * The formatter of displayed values of numeric tick labels.
 */
public final class NumericTickLabelFormatter extends TickLabelFormatter {

	/** The format of numeric tick labels, applied when |coordinate| = 0. */
	private final NumberFormat zeroRangeFormat;
	// Note: Why we don't declare the above as DecimalFormat-type field is,
	//       some people may want to implement original format by extending NumberFormat.
	//       (NumberFormat is an abstract class, and DecimalFormat is one of its implementation.)

	/** The format of numeric tick labels, applied when |coordinate| < 0.1. */
	private final NumberFormat shortRangeFormat;

	/** The format of numeric tick labels, applied when 0.1 <= |coordinate| <= 10. */
	private final NumberFormat mediumRangeFormat;

	/** The format of numeric tick labels, applied when 10 < |coordinate|. */
	private final NumberFormat longRangeFormat;

	/** The threshold value between the 'short' range and the 'medium' range. */
	private final BigDecimal shortMediumThreshold;

	/** The threshold value between the 'medium' range and the 'long' range. */
	private final BigDecimal mediumLongThreshold;


	/**
	 * Creates a new formatter for formatting tick coordinates into numeric tick labels.
	 *
	 * @param format The format of numeric tick labels.
	 */
	public NumericTickLabelFormatter(NumberFormat format) {
		this(format, format, format, format);
	}


	/**
	 * Creates a new formatter for formatting tick coordinates into numeric tick labels,
	 * using multiple formats defined for different coordinate ranges (zero, short, medium, long).
	 *
	 * @param shortRangeFormat The format of numeric tick labels, applied when |coordinate| < 0.1.
	 * @param mediumRangeFormat The format of numeric tick labels, applied when 0.1 <= |coordinate| <= 10.
	 * @param longRangeFormat The format of numeric tick labels, applied when 10 < |coordinate|.
	 */
	public NumericTickLabelFormatter(
			NumberFormat zeroRangeFormat, NumberFormat shortRangeFormat, NumberFormat mediumRangeFormat, NumberFormat longRangeFormat) {

		this.zeroRangeFormat = zeroRangeFormat;
		this.shortRangeFormat = shortRangeFormat;
		this.mediumRangeFormat = mediumRangeFormat;
		this.longRangeFormat = longRangeFormat;
		this.shortMediumThreshold = new BigDecimal(0.1);
		this.mediumLongThreshold = new BigDecimal(10);
	}

	/**
	 * Creates a new formatter for formatting tick coordinates into numeric tick labels,
	 * using multiple formats defined for different coordinate ranges (zero, short, medium, long),
	 * separated by the specified threshold coordinates.
	 *
	 * @param shortRangeFormat The format of numeric tick labels, applied when |coordinate| < shortMediumThreshold.
	 * @param mediumRangeFormat The format of numeric tick labels, applied when shortMediumThreshold <= |coordinate| <= mediumLongThreshold.
	 * @param longRangeFormat The format of numeric tick labels, applied when mediumLongThreshold < |coordinate|.
	 */
	public NumericTickLabelFormatter(
			NumberFormat zeroRangeFormat, NumberFormat shortRangeFormat, NumberFormat mediumRangeFormat, NumberFormat longRangeFormat,
				BigDecimal shortMediumThreshold, BigDecimal mediumLongThreshold) {

		this.zeroRangeFormat = zeroRangeFormat;
		this.shortRangeFormat = shortRangeFormat;
		this.mediumRangeFormat = mediumRangeFormat;
		this.longRangeFormat = longRangeFormat;
		this.shortMediumThreshold = shortMediumThreshold;
		this.mediumLongThreshold = mediumLongThreshold;
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
