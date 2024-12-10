package com.rinearn.graph3d.config;

import com.rinearn.graph3d.config.scale.TickLabelFormatter;
import com.rinearn.graph3d.config.scale.NumericTickLabelFormatter;
import com.rinearn.graph3d.config.scale.Ticker;
import com.rinearn.graph3d.config.scale.EqualDivisionTicker;
import com.rinearn.graph3d.config.scale.ManualTicker;

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

// !!! NOTE !!!
// tickLabel は tickLabelTexts にすべきでは？
// tickLabel 自体には tickLabelMargin とかのテキスト以外の属性があるし、
// LabelConfig での軸ラベルも setText にしてるし。
// !!! NOTE !!!


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

	/** The configuration of the color-bar's scale. */
	private volatile AxisScaleConfiguration colorBarScaleConfiguration = new AxisScaleConfiguration();


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
	 * Sets the configuration of the color-bat's scale.
	 *
	 * @param colorBarAxisScaleConfiguration The configuration of Z axis's scale.
	 */
	public synchronized void setColorBarScaleConfiguration(AxisScaleConfiguration colorBarAxisScaleConfiguration) {
		this.colorBarScaleConfiguration = colorBarAxisScaleConfiguration;
	}

	/**
	 * Gets the configuration of the color-bat's scale.
	 *
	 * @return The configuration of the color-bar's scale.
	 */
	public synchronized AxisScaleConfiguration getColorBarScaleConfiguration() {
		return this.colorBarScaleConfiguration;
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
		this.colorBarScaleConfiguration.validate();
	}

	/**
	 * The class storing configuration values of the scale of an axis (X, Y, or Z).
	 */
	public static class AxisScaleConfiguration {

		/** The length of tick lines. */
		private volatile double tickLineLength = 0.05;

		/** The margin between axes and tick labels. */
		private volatile double tickLabelMargin = 0.06;

		/** Represents the mode for specifying alignment of ticks of a scale. */
		private volatile TickerMode tickerMode = TickerMode.EQUAL_DIVISION; // Temporary setting to this, but will set to AUTOMATIC in future.

		/** The ticker to generate ticks at the N-equal-division points of the range of the axis, used in EQUAL_DIVISION mode. */
		private volatile EqualDivisionTicker equalDivisionTicker = new EqualDivisionTicker();

		/** The ticker to render the tick coordinates and the tick labels specified directly by the user, used in MANUAL mode. */
		private volatile ManualTicker manualTicker = new ManualTicker();

		/** The precision of the internal calculation of the scale's coordinates. */
		private volatile int calculationPrecision = 128;

		/** The mode of the formatter, which formats tick coordinates to tick labels.  */
		private volatile TickLabelFormatterMode tickLabelFormatterMode = TickLabelFormatterMode.NUMERIC;

		/** The formatter of tick labels, used in NUMERIC mode. */
		private volatile NumericTickLabelFormatter numericTickLabelFormatter = new NumericTickLabelFormatter();

		/** The formmatter of tick labels implemented by users or third party developers, used in CUSTOM mode. */
		private volatile TickLabelFormatter customTickLabelFormatter = null;


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
		 * Sets the ticker mode, which determines the alignment of ticks, of this axis's scale.
		 *
		 * @param tickerMode The tick mode of this axis's scale.
		 */
		public synchronized void setTickerMode(TickerMode tickMode) {
			this.tickerMode = tickMode;
		}

		/**
		 * Gets the ticker mode, which determines the alignment of ticks, of this axis's scale.
		 *
		 * @return The tick mode of this axis's scale.
		 */
		public synchronized TickerMode getTickerMode() {
			return this.tickerMode;
		}


		/**
		 * Gets the Ticker instance corresponding to the current ticker mode.
		 *
		 * @return The Ticker instance corresponding to the current ticker mode.
		 */
		public synchronized Ticker getTicker() {
			switch (this.tickerMode) {
				case EQUAL_DIVISION: {
					return this.equalDivisionTicker;
				}
				case MANUAL: {
					return null; // To be impled
				}
				default: {
					throw new IllegalStateException("Unexpected ticker mode: " + this.tickerMode);
				}
			}
		}


		/**
		 * Sets the ticker to generate ticks at the N-equal-division points of the range of the axis, used in EQUAL_DIVISION mode.
		 *
		 * @param equalDivisionTicker Sets the ticker for EQUAL_DIVISION mode.
		 */
		public synchronized void setDividedSectionTicker(EqualDivisionTicker equalDivisionTicker) {
			this.equalDivisionTicker = equalDivisionTicker;
		}

		/**
		 * Gets the ticker to generate ticks at the N-equal-division points of the range of the axis, used in EQUAL_DIVISION mode.
		 *
		 * @return The ticker for EQUAL_DIVISION mode.
		 */
		public synchronized EqualDivisionTicker getDividedSectionTicker() {
			return this.equalDivisionTicker;
		}


		/**
		 * Sets the ticker to render the tick coordinates and the tick labels specified directly by the user, used in MANUAL mode.
		 *
		 * @param manualTicker The ticker for MANUAL mode.
		 */
		public synchronized void setManualTicker(ManualTicker manualTicker) {
			this.manualTicker = manualTicker;
		}

		/**
		 * Sets the ticker to render the tick coordinates and the tick labels specified directly by the use, used in MANUAL mode.
		 *
		 * @return  The ticker for MANUAL mode.
		 */
		public synchronized ManualTicker getManualTicker() {
			return this.manualTicker;
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
		 * Sets the tick-label-formatter mode, which determines how the format of the tick labels of this axis's scale.
		 *
		 * @param tickerMode The tick-label-formatter mode of this axis's scale.
		 */
		public synchronized void setTickLabelFormatterMode(TickLabelFormatterMode tickLabelFormatterMode) {
			this.tickLabelFormatterMode = tickLabelFormatterMode;
		}

		/**
		 * Gets the tick-label-formatter mode, which determines how the format of the tick labels of this axis's scale.
		 *
		 * @return The tick-label-formatter mode of this axis's scale.
		 */
		public synchronized TickLabelFormatterMode getTickLabelFormatterMode() {
			return this.tickLabelFormatterMode;
		}


		/**
		 * Gets the TickLabelFormatter instance corresponding to the current tick-label-formatter mode.
		 *
		 * @return The TickLabelFormatter instance corresponding to the current ticker mode.
		 */
		public synchronized TickLabelFormatter getTickLabelFormatter() {
			switch (this.tickLabelFormatterMode) {
				case NUMERIC: {
					return this.numericTickLabelFormatter;
				}
				case CUSTOM: {
					return this.customTickLabelFormatter;
				}
				default: {
					throw new IllegalStateException("Unexpected tick-label-formatter mode: " + this.tickLabelFormatterMode);
				}
			}
		}


		/**
		 * Sets the formatters of tick labels, applied when the tick-label-formatter mode is NUMERIC.
		 *
		 * @param numericTickLabelFormatters The formatters of numeric tick labels.
		 */
		public synchronized void setNumericTickLabelFormatter(NumericTickLabelFormatter numericTickLabelFormatter) {
			this.numericTickLabelFormatter = numericTickLabelFormatter;
		}

		/**
		 * Gets the formatters of tick labels, applied when the tick-label-formatter mode is NUMERIC.
		 *
		 * @return The formatters of numeric tick labels.
		 */
		public synchronized NumericTickLabelFormatter getNumericTickLabelFormatter() {
			return this.numericTickLabelFormatter;
		}


		/**
		 * Sets the formatters of tick labels, applied when the tick-label-formatter mode is CUSTOM.
		 *
		 * @param customTickLabelFormatters The formatters of tick labels.
		 */
		public synchronized void setCustomTickLabelFormatter(TickLabelFormatter customTickLabelFormatter) {
			this.customTickLabelFormatter = customTickLabelFormatter;
		}

		/**
		 * Gets the formatters of tick labels, applied when the tick-label-formatter mode is CUSTOM.
		 *
		 * @return The formatters of tick labels.
		 */
		public synchronized TickLabelFormatter getCustomTickLabelFormatter() {
			return this.customTickLabelFormatter;
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
			if (this.tickerMode == null) {
				throw new IllegalStateException("The tick mode is null.");
			}
			if (this.calculationPrecision < 1) {
				throw new IllegalStateException("The calculation precision must be greater than 1.");
			}

			// Validate parameters of tickers.
			this.manualTicker.validate();
			this.equalDivisionTicker.validate();
		}
	}


	/**
	 * The enum representing each mode for specifying alignment of ticks of a scale.
	 */
	public static enum TickerMode {

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
	 * The enum representing each mode to format tick labels.
	 */
	public static enum TickLabelFormatterMode {

		/** Formats the coordinates of the ticks into the numeric labels.  */
		NUMERIC,

		/** Uses the custom formmatter implemented by users or third party developers. */
		CUSTOM,
	}
}
