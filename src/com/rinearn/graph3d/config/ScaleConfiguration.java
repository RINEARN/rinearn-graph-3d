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

// !!!!!
// NOTE
//
//      内部クラスの NumericTickLabelFormatter は適当に改名して config パッケージ直下とかに出すべきでは？
//      もし同じような事を tick label 以外でも行いたい場面が将来的に出てきた時に、現状だと所属が違和感全開になりそう。
//      で、その時に上の階層でほぼ同機能の別名のやつ作ったら、
//      逆に ScaleConfiguration だけ内部宣言のやつ使うという違和感が残るし、そこで型を変えると互換が崩れるしで詰む。
//
//      場所については、どちらかというとロジック処理っぽいので Model 階層っぽいけど、
//      実装の外側からAPIで設定の際に使うなら Model 層のやつ参照するのも変だし（＆実装内部はあんまり互換気にしたくないし）、
//      まあ config 直下なら多少は付随ロジック的なやつあっても許容範囲だと思う。ので config 直下で。
//      一応 format とか range とか設定的な役割も含んでいるので。
//
// !!!!!

//!!!!!
//NOTE
//
// dividedSectionCount は tickCount にしてMANUALモードでの個数 getter と合流させた方がいいかも？
// でもあまり固まってない案なので、しない方がいいかも。 
// 後々で適当な時に振り返って要検討。
//
// -> そういえば、それ思った時、カラーグラデの config で section count ベースではなく境界本数ベースにしたので、
//    こっちもそれと統一したい、みたいな事を考えてた。上記はそれの整合性を綺麗にするためも兼ねてたはず。
//    UI上の使い勝手は区間数ベースの方がいいかもだけど、それはUI層でどうとでもなるので、config 層は整合性優先した方がいい、的な。
//    -> いやそもそも使い勝手も慣れたら本数ベースの方がいいでしょ。たぶん変な馴れ方してるせいな気がする、いつも区間数にしてしまうのは。
//
//NOTE
//!!!!!

/**
 * The class storing configuration values of the scales of X/Y/Z axes.
 */
public final class ScaleConfiguration {

	/** The configuration of X axis's scale. */
	private volatile AxisScaleConfiguration xScaleConfiguration = new AxisScaleConfiguration();

	/** The configuration of Y axis's scale. */
	private volatile AxisScaleConfiguration yScaleConfiguration = new AxisScaleConfiguration();

	/** The configuration of Z axis's scale. */
	private volatile AxisScaleConfiguration zScaleConfiguration = new AxisScaleConfiguration();


	/**
	 * Creates a new configuration storing default values.
	 */
	public ScaleConfiguration() {
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
	 * Validates correctness and consistency of configuration parameters stored in this instance.
	 * 
	 * This method is called when this configuration is specified to RinearnGraph3D or its renderer.
	 * If no issue is detected, nothing occurs.
	 * If any issue is detected, throws IllegalStateException.
	 * 
	 * @throws IllegalStateException Thrown when incorrect or inconsistent settings are detected.
	 */
	public synchronized void validate() {
		this.xScaleConfiguration.validate();
		this.yScaleConfiguration.validate();
		this.zScaleConfiguration.validate();
	}


	/**
	 * The class storing configuration values of the scale of an axis (X, Y, or Z).
	 */
	public static class AxisScaleConfiguration {

		// To be added: tickLineWidth, tickLabelMargin, etc.

		/** The length of tick lines. */
		private volatile double tickLineLength = 0.05;

		/** The margin between axes and tick labels. */
		private volatile double tickLabelMargin = 0.06;

		/** Represents the mode for specifying alignment of ticks of a scale. */
		private volatile TickMode tickMode = TickMode.EQUAL_DIVISION; // Temporary setting to this, but will set to AUTOMATIC in future.

		/** The coordinates (locations) of ticks, in MANUAL mode. */
		private volatile BigDecimal[] tickCoordinates = new BigDecimal[0];

		/** The labels (displayed texts) of ticks, in MANUAL mode. */
		private volatile String[] tickLabels = new String[0];

		/** Number of sections between ticks, in EQUAL_DIVISION mode. */
		private volatile int dividedSectionCount = 4;
		// ↑ 区間の訳の命名、interval は長さ値的なニュアンスを含むので、個数に注目しているここでは section の方がいい。
		//    前者のニュアンスを含む個所では interval を使うよう注意。

		// -> MANUAL モードとかとの統一性を考えたら tickCount の方がいいのでは？
		//    Color gradient の方でも boundaryCount にしてるし、そっちとの対応的にも。
		//    利用上は sectionCount ベースの方が便利かもだが、それはUI層でどうとでもなるし、
		//    config 層では内部処理的に綺麗な方がしておいた方がいいかも。


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
		 * Sets the length of tick lines.
		 * 
		 * The value is regarded as a projected length to a plane of the graph frame (X-Y plane, Y-Z plane, or Z-X plane).
		 * Hence, if a tick line is not parallel with any above planes,
		 * actual length of the line is longer than the specified value a little.
		 * 
		 * Also, the unit of the specified length is regarded as "length in scaled space".
		 * In this unit, the length of a edge line (e.g.: X axis line) of the graph frame is just 2.0.
		 * 
		 * @param tickLineLength The length of tick lines.
		 */
		public synchronized void setTickLineLength(double tickLineLength) {
			this.tickLineLength = tickLineLength;
		}

		/**
		 * Gets the length of tick lines.
		 * 
		 * About details of the value, see the description of setTickLineLength(double) method.
		 * 
		 * @return The length of tick lines.
		 */
		public synchronized double getTickLineLength() {
			return this.tickLineLength;
		}


		/**
		 * Sets the margin between axes and tick labels.
		 * 
		 * The value is regarded as a projected length of the margin to a plane of the graph frame 
		 * (X-Y plane, Y-Z plane, or Z-X plane), as same as the setter of tick line length.
		 * 
		 * The unit of the specified value is regarded as "length in scaled space".
		 * In this unit, the length of a edge line (e.g.: X axis line) of the graph frame is just 2.0.
		 * 
		 * @param tickLabelMargin The margin between axes and tick labels
		 */
		public synchronized void setTickLabelMargin(double tickLabelMargin) {
			this.tickLabelMargin = tickLabelMargin;
		}

		/**
		 * Gets the margin between axes and tick labels.
		 * 
		 * About details of the value, see the description of setTickLabelMargin(double) method.
		 * 
		 * @return The margin between axes and tick labels.
		 */
		public synchronized double getTickLabelMargin() {
			return this.tickLabelMargin;
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
		 * Sets the coordinates (locations) of the ticks, in MANUAL mode.
		 * 
		 * @param tickCoordinates The coordinates of the ticks.
		 */
		public synchronized void setTickCoordinates(BigDecimal[] tickCoordinates) {
			this.tickCoordinates = tickCoordinates;
		}

		/**
		 * Gets the coordinates (locations) of the ticks, in MANUAL mode.
		 * 
		 * @return The coordinates of the ticks.
		 */
		public synchronized BigDecimal[] getTickCoordinates() {
			return this.tickCoordinates;
		}


		/**
		 * Sets the labels (displayed texts) of the ticks, in MANUAL mode.
		 * 
		 * @param tickCoordinates The labels of the ticks.
		 */
		public synchronized void setTickLabels(String[] tickLabels) {
			this.tickLabels = tickLabels;
		}

		/**
		 * Gets the labels (displayed texts) of the ticks, in MANUAL mode.
		 * 
		 * @return The labels of the ticks.
		 */
		public synchronized String[] getTickLabels() {
			return this.tickLabels;
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


		/**
		 * Validates correctness and consistency of configuration parameters stored in this instance.
		 * 
		 * This method is called when this configuration is specified to RinearnGraph3D or its renderer.
		 * If no issue is detected, nothing occurs.
		 * If any issue is detected, throws IllegalStateException.
		 * 
		 * @throws IllegalStateException Thrown when incorrect or inconsistent settings are detected.
		 */
		public synchronized void validate() {
			if (this.tickLineLength < 0.0) {
				throw new IllegalStateException("The length of tick lines must be a positive value.");
			}
			if (this.tickLabelMargin < 0.0) {
				throw new IllegalStateException("The margin of tick labels must be a positive value.");
			}
			if (this.tickMode == null) {
				throw new IllegalStateException("The tick mode is null.");				
			}
			if (this.calculationPrecision < 1) {
				throw new IllegalStateException("The calculation precision must be greater than 1.");
			}

			// Validate parameters for each mode.
			switch (this.tickMode) {
				case MANUAL : {
					if (this.tickCoordinates == null) {
						throw new IllegalStateException("The tick coordinates are null (mandatory in MANUAL mode).");
					}
					if (this.tickLabels == null) {
						throw new IllegalStateException("The tick labels are null (mandatory in MANUAL mode).");
					}
					if (this.tickCoordinates.length != this.tickLabels.length) {
						throw new IllegalStateException("The number of the tick coordinates does not match with the number of the tick labels.");
					}
					break;
				}
				case EQUAL_DIVISION : {
					if (dividedSectionCount < 1) {
						throw new IllegalStateException("The length of tick lines must be greater than 1.");
					}
					break;
				}
				default : {
					throw new UnsupportedOperationException("Unknown tick mode: " + this.tickMode);
				}
			}
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


	// !!! NOTE !!!
	//
	// ↓これやっぱ外にあった方がいいなあ、どう考えても
	//
	// !!! NOTE !!!

	/**
	 * The formatter of displayed values of numeric tick labels.
	 */
	public static class NumericTickLabelFormatter {

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
