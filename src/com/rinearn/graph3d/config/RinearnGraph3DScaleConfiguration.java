package com.rinearn.graph3d.config;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.DecimalFormat;

// !!!!!
// NOTE
//
//     RinearnGraph3D側での設定メソッドを configureScale(...) にする予定なら、
//     このクラスの setXAxis... とかも configureXAxis... とかにすべき？ 統一性のために。
//
//     > いや、設定読んで反映させる処理側で getXAxis... するの必須だし、
//       getter があるならむしろ対応のために普通の setter 存在してほしいような。
//
//     > それと、これに configureXAxis... してもその瞬間にRG3D側に反映されるわけではないので、
//       役割的にもここは set が合ってる気がする。
//       むしろRG3D側も set も可で configure とどっちか迷うレベルだけど、仮にあっち側がどうであっても
//       こっちが configure になるのは意味的におかしい。のでむしろ統一性はあっち側をどうするかの問題。
//
// !!!!!

// !!!!!
// NOTE
//
//     命名について：
//     config パッケージ内で、X/Y/Z軸それぞれに同じような設定を行うConfigurationコンテナは、このクラスのように
//     「AxisScale... のようなサブコンテナを作って、それを xScale... のようなフィールドとして持たせる」
//     というの方針を暫定案で決めた。
//     ここで初採用なので、後々でなんかまずい点/他の個所ですっきりしなくなる点等が浮上したら再考する。
//
//     Range とか Label とかの設定も、RG3D側で setXRange とかの直接setterはあるけど、
//     自動調整のON/OFFとか細かい挙動の表現を含めたら
//    （APIはともかく実装としては）Configurationコンテナ要るし、その実装の際も暫定的には上記の方針で。
//
//     なお、サブコンテナの命名は Axis... だけど、setter を setXAxis... にはせずに setX... にするので、
//     うっかり setXAxis... 的なやつを（他クラス含めて）作ってしまわないように要注意。
//     これは、Axis付けると全部のX/Y/Zに付ける必要が生じて冗長過ぎる（＆既にいくつか略してるので手遅れ）なのと、
//     そもそも Axis という抽象概念が使用者視点において少し難しいため（実装面ではくくり方として色々整って良いけれど）。
// 
// !!!!!

/**
 * The class storing configuration values of the scales of X/Y/Z axes.
 */
public class RinearnGraph3DScaleConfiguration {

	/** The configuration of X axis's scale. */
	private volatile AxisScaleConfiguration xScaleConfiguration = new AxisScaleConfiguration();

	/** The configuration of Y axis's scale. */
	private volatile AxisScaleConfiguration yScaleConfiguration = new AxisScaleConfiguration();

	/** The configuration of Z axis's scale. */
	private volatile AxisScaleConfiguration zScaleConfiguration = new AxisScaleConfiguration();


	/**
	 * Creates a new configuration storing default values.
	 */
	public RinearnGraph3DScaleConfiguration() {
	}


	/**
	 * Sets the configuration of X axis's scale.
	 * 
	 * @param xAxisScaleConfiguration The configuration of X axis's scale.
	 */
	public synchronized void setXScaleConfiguration(AxisScaleConfiguration xAxisScaleConfiguration) {
		this.xScaleConfiguration = xAxisScaleConfiguration;
	}

	/**
	 * Gets the configuration of X axis's scale.
	 * 
	 * @return The configuration of X axis's scale.
	 */
	public synchronized AxisScaleConfiguration getXScaleConfiguration() {
		return this.xScaleConfiguration;
	}

	/**
	 * Sets the configuration of Y axis's scale.
	 * 
	 * @param yAxisScaleConfiguration The configuration of Y axis's scale.
	 */
	public synchronized void setYScaleConfiguration(AxisScaleConfiguration yAxisScaleConfiguration) {
		this.yScaleConfiguration = yAxisScaleConfiguration;
	}

	/**
	 * Gets the configuration of Y axis's scale.
	 * 
	 * @return The configuration of Y axis's scale.
	 */
	public synchronized AxisScaleConfiguration getYScaleConfiguration() {
		return this.yScaleConfiguration;
	}

	/**
	 * Sets the configuration of Z axis's scale.
	 * 
	 * @param zAxisScaleConfiguration The configuration of Z axis's scale.
	 */
	public synchronized void setZScaleConfiguration(AxisScaleConfiguration zAxisScaleConfiguration) {
		this.zScaleConfiguration = zAxisScaleConfiguration;
	}

	/**
	 * Gets the configuration of Z axis's scale.
	 * 
	 * @return The configuration of Z axis's scale.
	 */
	public synchronized AxisScaleConfiguration getZScaleConfiguration() {
		return this.zScaleConfiguration;
	}


	/**
	 * The class storing configuration values of the scale of an axis (X, Y, or Z).
	 */
	public static class AxisScaleConfiguration {

		// To be added: tickLineWidth, tickLabelMargin, etc.

		/** Represents the mode for specifying alignment of ticks of a scale. */
		private volatile TickMode tickMode = TickMode.EQUAL_DIVISION; // Temporary setting to this, but will set to AUTOMATIC in future.

		/** Number of sections between ticks, in EQUAL_DIVISION mode. */
		private volatile int dividedSectionCount = 4;
		// ↑ 区間の訳の命名、interval は長さ値的なニュアンスを含むので、個数に注目しているここでは section の方がいい。
		//    前者のニュアンスを含む個所では interval を使うよう注意。

		/** The precision of the internal calculation of the scale's coordinates. */
		private volatile int calculationPrecision = 128;

		/**
		 * The formatters of tick labels, applied when they are numeric values.
		 */
		private volatile NumericTickLabelFormatter[] numericTickLabelFormatters = {

				// The base format, applied to any ticks
				// excluding if their coordinates are contained in the range of formatters defined latter.
				new NumericTickLabelFormatter(new DecimalFormat("0.0#E0")),

				// The format applied to only for the range (0.1, 10), which does not contain 0.1 and 10.
				new NumericTickLabelFormatter(new DecimalFormat("0.0#"), new BigDecimal("0.1"), BigDecimal.TEN, false, false),

				// The format applied to only for the range (-10, 0.1), which does not contain -10 and -0.1.
				new NumericTickLabelFormatter(new DecimalFormat("0.0#"), BigDecimal.TEN.negate(), new BigDecimal("0.1").negate(), false, false),

				// The format applied to only zero.
				new NumericTickLabelFormatter(new DecimalFormat("0"), BigDecimal.ZERO, BigDecimal.ZERO, true, true)
		};


		/**
		 * Creates a new configuration storing default values.
		 */
		public AxisScaleConfiguration() {
		}


		/**
		 * Sets the tick mode, which determines the alignment of ticks, of this axis's scale.
		 * 
		 * @param tickMode The tick mode of this axis's scale.
		 */
		public synchronized void setTickMode(TickMode tickMode) {
			this.tickMode = tickMode;
		}

		/**
		 * Gets the tick mode, which determines the alignment of ticks, of this axis's scale.
		 * 
		 * @return The tick mode of this axis's scale.
		 */
		public synchronized TickMode getTickMode() {
			return this.tickMode;
		}


		/**
		 * Sets the number of sections between ticks, in EQUAL_DIVISION mode.
		 * 
		 * @param dividedSectionCount The number of sections between ticks.
		 */
		public synchronized void setDividedSectionCount(int dividedSectionCount) {
			this.dividedSectionCount = dividedSectionCount;
		}

		/**
		 * Gets the number of sections between ticks, in EQUAL_DIVISION mode.
		 * 
		 * @return The number of sections between ticks.
		 */
		public synchronized int getDividedSectionCount() {
			return this.dividedSectionCount;
		}


		/**
		 * Sets the precision of internal calculations of the scale's coordinates.
		 * 
		 * @param calculationPrecision The precision of internal calculations of the scale's coordinates.
		 */
		public synchronized void setCalculationPrecision(int calculationPrecision) {
			this.calculationPrecision = calculationPrecision;
		}

		/**
		 * Gets the precision of internal calculations of the scale's coordinates.
		 * 
		 * @return The precision of internal calculations of the scale's coordinates.
		 */
		public synchronized int getCalculationPrecision() {
			return this.calculationPrecision;
		}


		/**
		 * Sets the formatters of tick labels, applied when they are numeric values.
		 * 
		 * To apply different format for different ranges on the axis, you can specify multiple formatters as an array.
		 * When ranges of multiple formatter are overlapping,
		 * the formatter stored at latter side in the array (having greater index) will be applied preferentially.
		 * 
		 * @param numericTickLabelFormatters The formatters of numeric tick labels.
		 */
		public synchronized void setNumericTickLabelFormatters(NumericTickLabelFormatter[] numericTickLabelFormatters) {
			this.numericTickLabelFormatters = numericTickLabelFormatters;
		}

		/**
		 * Gets the formatters of tick labels, applied when they are numeric values.
		 * 
		 * @return The formatters of numeric tick labels.
		 */
		public synchronized NumericTickLabelFormatter[] getNumericTickLabelFormatters() {
			return this.numericTickLabelFormatters;
		}
	}


	/**
	 * The enum representing each mode for specifying alignment of ticks of a scale.
	 */
	public static enum TickMode {

		/** Divides an axis's range (from min to max) equally by scale ticks. */
		EQUAL_DIVISION,

		/** Align scale ticks as an arithmetic sequence. */
		ARITHMETIC_PROGRESSION,

		/** Align scale ticks as a geometric sequence. */
		GEOMETRIC_PROGRESSION,

		/** Align scale ticks automatically. */
		AUTOMATIC,

		/** Align scale ticks manually, and also specify arbitrary tick labels. */
		MANUAL,
	}


	/**
	 * The formatter of displayed values of numeric tick labels.
	 */
	public static class NumericTickLabelFormatter {

		/** The displayed format of numeric tick labels. */
		private final NumberFormat format;

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
		 * @param format
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

		/**
		 * Returns whether the specified coordinate is contained in the applicable range of this formatter.
		 * If no applicable range is defined, always returns true.
		 * 
		 * @param coordinate Returns if the specified coordinate is contained in the range.
		 */
		public synchronized boolean contains(BigDecimal coordinate) {
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

}
