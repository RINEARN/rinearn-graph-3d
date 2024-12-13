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

	/** The visibility of the grid. */
	private volatile boolean gridVisible;

	/** The visibility of the tick lines and tick labels. */
	private volatile boolean tickVisible;

	/** The flag to set the direction of the ticks inward. */
	private volatile boolean tickInward;

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
	 * Sets the visibility of the grid.
	 *
	 * @param gridVisible Specify true for setting the grid visible.
	 */
	public synchronized void setGridVisible(boolean gridVisible) {
		this.gridVisible = gridVisible;
	}

	/**
	 * Gets the visibility of the grid.
	 *
	 * @return Returns true if the grid is visible.
	 */
	public synchronized boolean isGridVisible() {
		return this.gridVisible;
	}


	/**
	 * Sets the visibility of the tick lines and tick labels.
	 *
	 * @param tickVisible Specify true for setting the tick lines/labels visible.
	 */
	public synchronized void setTickVisible(boolean tickVisible) {
		this.tickVisible = tickVisible;
	}

	/**
	 * Gets the visibility of the tick lines and tick labels.
	 *
	 * @return Returns true if the tick lines/labels are visible.
	 */
	public synchronized boolean isTickVisible() {
		return this.tickVisible;
	}


	/**
	 * Sets the value of the flag to draw the ticks inward.
	 *
	 * @param tickInward Specify true to set the ticks inward.
	 */
	public synchronized void setTickInward(boolean tickInward) {
		this.tickInward = tickInward;
	}

	/**
	 * Gets the value of the flag to draw the ticks inward.
	 *
	 * @return Returns true if the ticks is set inward.
	 */
	public synchronized boolean isTickInward() {
		return this.tickInward;
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

		/** Uses the custom ticker implemented by users or third party developers. */
		CUSTOM,
	}

	/**
	 * The enum representing each mode to format tick labels.
	 */
	public static enum TickLabelFormatterMode {

		/** Formats the tick coordinates into the numeric labels in the default format.  */
		AUTO,

		/** Formats the tick coordinates into the numeric labels in the user-specified format.  */
		NUMERIC,

		/** Uses the custom formmatter implemented by users or third party developers. */
		CUSTOM,
	}

	/**
	 * The class storing configuration values of the scale of an axis (X, Y, or Z).
	 */
	public static class AxisScaleConfiguration {

		// !!!!! NOTE:
		// 以下の Visibility 制御フラグ、将来の事を考えたら、TickVisibilityController みたいなのを作ってその Manual 版として実装すべきでは？
		// だって他にも少なくとも Auto 時の挙動が別枠で必用になるわけで、それを auto かどうかのフラグでやるくらいなら、
		// モードとして AUTO と MANUAL を用意して、他も拡張できる余地も作っておいたほうがよくない？
		//
		// -> しかし、判定メソッドの引数に何を取らせるかを、汎用性を予想して決めるのはなかなかむずい。
		//    AUTO挙動はフレーム面の法線ベクトルでやってるので、少なくともそれは要るが、しかし角度パラメータの方が本質的な気がする。
		//    一方毎回角度から法線算出するのも無駄だし、法線を渡したいが、それは実装に直結し過ぎなような気もするし。
		//
		//    -> いや Ticker だって Range 渡してるんだし、可視性コントローラに法線渡しても別にそこまで変じゃない気がするが。
		//
		//       -> しかし2本の法線をベースに可視性を決める現状のアルゴリズムのは結構独特なものでしょ？ 他の情報も参照しつつ微妙な加減で調整してたし。
		//
		//       ・ ああ、というか、AUTO時の目盛りの可視性制御はポリゴン登録時じゃなくて描画時にレンダラーの特例措置でやってる（特殊ポリゴン扱い）
		//          ので結構面倒だ、その挙動を上層に持ってくるのは。というかそもそもできるのか？
		//          もしできたらそれはそれでメンテ性の観点で嬉しい気もするが
		//
		//    -> ↑ができるかどうかは置いといて、判断材料についてはコンテナクラスを作ってそこに必用に応じてsetter/getter生やしていけば済む。というかたぶん必須。
		//
		//       ・確かに。むしろ Ticker もそう改修すべきかも。確か range 以外にも対数プロットかどうかのフラグを単発でとってるけど、粒度的にそのへん微妙だし。

		/** The visibility of 'tick series A' of this axis's scale. */
		private volatile boolean tickSeriesAVisible;

		/** The visibility of 'tick series B' of this axis's scale. */
		private volatile boolean tickSeriesBVisible;

		/** The visibility of 'tick series C' of this axis's scale. */
		private volatile boolean tickSeriesCVisible;

		/** The visibility of 'tick series D' of this axis's scale. */
		private volatile boolean tickSeriesDVisible;

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

		/** The ticker implemented by users or third party developers, used in CUSTOM mode. */
		private volatile Ticker customTicker = null;

		/** The precision of the internal calculation of the scale's coordinates. */
		private volatile int calculationPrecision = 128;

		/** The mode of the formatter, which formats tick coordinates to tick labels.  */
		private volatile TickLabelFormatterMode tickLabelFormatterMode = TickLabelFormatterMode.AUTO;

		/** The formatter of tick labels, used in AUTO mode. */
		private final TickLabelFormatter autoTickLabelFormatter = new NumericTickLabelFormatter().createUnmodifiableClone();

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
		 * Sets the visibility of 'tick series A' of this axis.
		 *
		 * @param gridVisible Specify true for setting 'tick series A' visible.
		 */
		public synchronized void setTickSeriesAVisible(boolean tickSeriesAVisible) {
			this.tickSeriesAVisible = tickSeriesAVisible;
		}

		/**
		 * Gets the visibility of 'tick series A' of this axis.
		 *
		 * @return Returns true if 'tick series A'is visible.
		 */
		public synchronized boolean isTickSeriesAVisible() {
			return this.tickSeriesAVisible;
		}


		/**
		 * Sets the visibility of 'tick series B' of this axis.
		 *
		 * @param gridVisible Specify true for setting 'tick series B' visible.
		 */
		public synchronized void setTickSeriesBVisible(boolean tickSeriesBVisible) {
			this.tickSeriesBVisible = tickSeriesBVisible;
		}

		/**
		 * Gets the visibility of 'tick series B' of this axis.
		 *
		 * @return Returns true if 'tick series B'is visible.
		 */
		public synchronized boolean isTickSeriesBVisible() {
			return this.tickSeriesBVisible;
		}


		/**
		 * Sets the visibility of 'tick series C' of this axis.
		 *
		 * @param gridVisible Specify true for setting 'tick series C' visible.
		 */
		public synchronized void setTickSeriesCVisible(boolean tickSeriesCVisible) {
			this.tickSeriesCVisible = tickSeriesCVisible;
		}

		/**
		 * Gets the visibility of 'tick series C' of this axis.
		 *
		 * @return Returns true if 'tick series C'is visible.
		 */
		public synchronized boolean isTickSeriesCVisible() {
			return this.tickSeriesCVisible;
		}


		/**
		 * Sets the visibility of 'tick series D' of this axis.
		 *
		 * @param gridVisible Specify true for setting 'tick series D' visible.
		 */
		public synchronized void setTickSeriesDVisible(boolean tickSeriesDVisible) {
			this.tickSeriesDVisible = tickSeriesDVisible;
		}

		/**
		 * Gets the visibility of 'tick series D' of this axis.
		 *
		 * @return Returns true if 'tick series D'is visible.
		 */
		public synchronized boolean isTickSeriesDVisible() {
			return this.tickSeriesDVisible;
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
					return this.manualTicker;
				}
				case CUSTOM: {
					return this.customTicker;
				}
				default: {
					throw new IllegalStateException("Unexpected ticker mode: " + this.tickerMode);
				}
			}
		}


		/**
		 * Sets the ticker to generate ticks at the N-equal-division points of the range of the axis, used in EQUAL_DIVISION mode.
		 *
		 * @param equalDivisionTicker The ticker for EQUAL_DIVISION mode.
		 */
		public synchronized void setEqualDivisionTicker(EqualDivisionTicker equalDivisionTicker) {
			this.equalDivisionTicker = equalDivisionTicker;
		}

		/**
		 * Gets the ticker to generate ticks at the N-equal-division points of the range of the axis, used in EQUAL_DIVISION mode.
		 *
		 * @return The ticker for EQUAL_DIVISION mode.
		 */
		public synchronized EqualDivisionTicker getEqualDivisionTicker() {
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
		 * Sets the ticker implemented by users or third party developers, used in CUSTOM mode.
		 *
		 * @param manualTicker The ticker for CUSTOM mode.
		 */
		public synchronized void setCustomTicker(Ticker customTicker) {
			this.customTicker = customTicker;
		}

		/**
		 * Sets the ticker implemented by users or third party developers, used in CUSTOM mode.
		 *
		 * @return  The ticker for CUSTOM mode.
		 */
		public synchronized Ticker getCustomTicker() {
			return this.customTicker;
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
				case AUTO: {
					return autoTickLabelFormatter;
				}
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

			// Validate parameters of tickers and formatters.
			this.manualTicker.validate();
			this.equalDivisionTicker.validate();
			this.numericTickLabelFormatter.validate();
			if (this.tickerMode == TickerMode.CUSTOM) {
				if (this.customTicker == null) {
					throw new IllegalStateException("The custom ticker is null although the ticker mode is set to CUSTOM.");
				}
				this.customTickLabelFormatter.validate();
			}
			if (this.tickLabelFormatterMode == TickLabelFormatterMode.CUSTOM) {
				if (this.customTickLabelFormatter == null) {
					throw new IllegalStateException("The custom tick label formatter is null although the formatter mode is set to CUSTOM.");
				}
				this.customTickLabelFormatter.validate();
			}
		}
	}
}
