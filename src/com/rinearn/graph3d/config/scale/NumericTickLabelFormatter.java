package com.rinearn.graph3d.config.scale;

import java.math.BigDecimal;
import java.text.NumberFormat;


/**
 * The formatter of displayed values of numeric tick labels.
 */
public final class NumericTickLabelFormatter {

	/** The displayed format of numeric tick labels. */
	private final NumberFormat format;
	// Note: Why we don't declare the above as DecimalFormat-type field is,
	//       some people may want to implement original format by extending NumberFormat.
	//       (NumberFormat is an abstract class, and DecimalFormat is one of its implementation.)

	/** The flag representing whether the applicable range of this formatter is defined. */
	private final boolean hasRange;

	/** The minimum value of the range to which apply this formatter. */
	private final BigDecimal minCoordinate;

	/** The maximum value of the range to which apply this formatter. */
	private final BigDecimal maxCoordinate;

	/**
	 * The flag representing whether apply this formatter to a label
	 * of which coordinate is just the same as minimum value of the range.
	 */
	private final boolean containsMinCoordinate;

	/**
	 * The flag representing whether apply this formatter to a label
	 * of which coordinate is just the same as maximum value of the range.
	 */
	private final boolean containsMaxCoordinate;


	/**
	 * Creates a new formatter for formatting any labels of ticks.
	 *
	 * @param format The format of labels.
	 */
	public NumericTickLabelFormatter(NumberFormat format) {
		this.format = format;
		this.hasRange = false;
		this.minCoordinate = null;
		this.maxCoordinate = null;
		this.containsMinCoordinate = false;
		this.containsMaxCoordinate = false;
	}

	/**
	 * Creates a new formatter for formatting only labels of ticks in the specific range.
	 *
	 * @param format The displayed format of numeric labels.
	 * @param minCoordinate The minimum coordinate of the range to which apply this formatter.
	 * @param maxCoordinate The minimum coordinate of the range to which apply this formatter.
	 * @param containsMinCoordinate Specify true if apply this formatter to a label of which coordinate is just the same as minCoordinate.
	 * @param containsMaxCoordinate Specify true if apply this formatter to a label of which coordinate is just the same as maxCoordinate.
	 */
	public NumericTickLabelFormatter(NumberFormat format,
			BigDecimal minCoordinate, BigDecimal maxCoordinate,
			boolean containsMinCoordinate, boolean containsMaxCoordinate) {

		this.format = format;
		this.hasRange = true;
		this.minCoordinate = minCoordinate;
		this.maxCoordinate = maxCoordinate;
		this.containsMinCoordinate = containsMinCoordinate;
		this.containsMaxCoordinate = containsMaxCoordinate;
	}


	/**
	 * Formats the specified coordinate value into a label text,
	 *
	 * @param tickCoordinate The coordinate of the numeric tick to be formatted.
	 * @return The formatted tick label (or the specified label as it is).
	 */
	public synchronized String format(BigDecimal tickCoordinate) {
		return this.format.format(tickCoordinate);
	}

	// !!!!! NOTE:
	//   この層でこういうのが必用になるのは、管轄が切り方がおかしい。
	//   日付とかその他文字列とか色々なフォーマット実装を考えると、
	//   レンジ分岐が要るなら、それは複数レンジをこのクラス内で持って内部でやるべき。
	//   もっと一般化して、特定座標がフォーマット可能かどうかを検査するなら、名前を isFormattable とかにすべき。
	/**
	 * Returns whether the specified coordinate is contained in the applicable range of this formatter.
	 * If no applicable range is defined, always returns true.
	 *
	 * @param coordinate Returns if the specified coordinate is contained in the range.
	 */
	public synchronized boolean isFormattable(BigDecimal coordinate) {
		if (!this.hasRange) {
			return true;
		}

		// Firstly, compare the specified coordinate to the minimum value of the range.
		int minCompared = coordinate.compareTo(this.minCoordinate);

		// The case that we allow the completely same value as the min value.
		if (this.containsMinCoordinate) {

			// Decline if the specified coordinate is LESS THAN the min value.
			if (minCompared < 0) {
				return false;
			}

		} else {

			// Decline if the specified coordinate is LESS THAN OR EQUALS TO the min value.
			if (minCompared <= 0) {
				return false;
			}
		}

		// Next, compare the specified coordinate to the maximum value of the range.
		int maxCompared = coordinate.compareTo(this.maxCoordinate);

		// The case that we allow the completely same value as the max value.
		if (this.containsMaxCoordinate) {

			// Decline if the specified coordinate is GREATER THAN the min value.
			if (0 < maxCompared) {
				return false;
			}

		} else {

			// Decline if the specified coordinate is GREATER THAN OR EQUALS TO the min value.
			if (0 <= maxCompared) {
				return false;
			}
		}

		// If all tests have passed, the specified coordinate is contained in the range.
		return true;
	}
}
