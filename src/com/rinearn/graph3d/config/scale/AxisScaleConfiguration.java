package com.rinearn.graph3d.config.scale;


/**
 * The class storing configuration values of the scale of an axis (X, Y, or Z).
 */
public final class AxisScaleConfiguration {

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
	//
	// -> 目盛り系列の可視性を動的にコントロールするオブジェクトはさすがに大げさすぎるので、
	//    折衷案として各目盛り系列の挙動を boolean ではなく enum のモードで切り分ける事にした。
	//    それぞれのモードの挙動はグラフソフト側の実装管轄で。
	//
	//    だって、そこまで描画時のベクトルとか変換行列とかを色々意識させつつ
	//    目盛りの可視性コントロールオブジェクトを独自実装してもらうくらいなら、
	//    もう描画エンジンAPI経由で自由に目盛りを描いてもらった方が良くない？ って話になるし。
	//    目盛り描画に使えそうな文字列ポリゴンの種類を色々増やしつつ。そっちの方法の方がたぶん筋が良い。
	//
	//    その方法と比べると、目盛りの可視性管理オブジェクト用に中途半端に抽象化した概念を理解する方がむしろ面倒でしょ。
	//    そもそも機会も相当レアだし。しかも決めた仕様が将来の拡張の足も引っ張るし。なのでそんなオブジェクトは要らん。
	//
	//    以上の理由により、実装をグラフソフト側に任せてしまって enum で選択する方がたぶんいいと思う。ここはさすがに。
	//
	// 少し塩漬けにしてまた要再検討。

	/** The visibility of 'scale A' of this axis's scale. */
	private volatile ScaleVisibilityMode scaleAVisibilityMode = ScaleVisibilityMode.AUTO;
	// private volatile ScaleVisibilityMode tickSeriesAVisiblityMode = ScaleVisibilityMode.AUTO;
	// NOTE: ↑やっぱ tickSeriesA っての変だよ。scaleA の方がいいよ。scale 単独なら概念粒度的に大きすぎるけど scale 'A' なんだしいいでしょ。

	/** The visibility of 'scale B' of this axis's scale. */
	private volatile ScaleVisibilityMode scaleBVisibilityMode = ScaleVisibilityMode.AUTO;

	/** The visibility of 'scale C' of this axis's scale. */
	private volatile ScaleVisibilityMode scaleCVisibilityMode = ScaleVisibilityMode.AUTO;

	/** The visibility of 'scale D' of this axis's scale. */
	private volatile ScaleVisibilityMode scaleDVisibilityMode = ScaleVisibilityMode.AUTO;

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
	 * Sets the visibility mode of the 'scale A' of this axis.
	 *
	 * @param scaleAVisibilityMode The visibility mode of the 'scale A' of this axis.
	 */
	public synchronized void setScaleAVisibilityMode(ScaleVisibilityMode scaleAVisibilityMode) {
		this.scaleAVisibilityMode = scaleAVisibilityMode;
	}

	/**
	 * Gets the visibility mode of the 'scale A' of this axis.
	 *
	 * @return The visibility mode of the 'scale A' of this axis.
	 */
	public synchronized ScaleVisibilityMode getScaleAVisibilityMode() {
		return this.scaleAVisibilityMode;
	}


	/**
	 * Sets the visibility mode of the 'scale B' of this axis.
	 *
	 * @param scaleBVisibilityMode The visibility mode of the 'scale B' of this axis.
	 */
	public synchronized void setScaleBVisibilityMode(ScaleVisibilityMode scaleBVisibilityMode) {
		this.scaleBVisibilityMode = scaleBVisibilityMode;
	}

	/**
	 * Gets the visibility mode of the 'scale B' of this axis.
	 *
	 * @return The visibility mode of the 'scale B' of this axis.
	 */
	public synchronized ScaleVisibilityMode getScaleBVisibilityMode() {
		return this.scaleBVisibilityMode;
	}


	/**
	 * Sets the visibility mode of the 'scale C' of this axis.
	 *
	 * @param scaleBVisibilityMode The visibility mode of the 'scale C' of this axis.
	 */
	public synchronized void setScaleCVisibilityMode(ScaleVisibilityMode scaleCVisibilityMode) {
		this.scaleCVisibilityMode = scaleCVisibilityMode;
	}

	/**
	 * Gets the visibility mode of the 'scale C' of this axis.
	 *
	 * @return The visibility mode of the 'scale C' of this axis.
	 */
	public synchronized ScaleVisibilityMode getScaleCVisibilityMode() {
		return this.scaleCVisibilityMode;
	}



	/**
	 * Sets the visibility mode of the 'scale D' of this axis.
	 *
	 * @param scaleBVisibilityMode The visibility mode of the 'scale D' of this axis.
	 */
	public synchronized void setScaleDVisibilityMode(ScaleVisibilityMode scaleDVisibilityMode) {
		this.scaleDVisibilityMode = scaleDVisibilityMode;
	}

	/**
	 * Gets the visibility mode of the 'scale D' of this axis.
	 *
	 * @return The visibility mode of the 'scale D' of this axis.
	 */
	public synchronized ScaleVisibilityMode getScaleDVisibilityMode() {
		return this.scaleDVisibilityMode;
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

		// The visibility mode "AUTO" can not be used together with other modes in one axis, so check them.
		boolean containsAutoVisibilityMode = this.scaleAVisibilityMode == ScaleVisibilityMode.AUTO
				|| this.scaleBVisibilityMode == ScaleVisibilityMode.AUTO
				|| this.scaleCVisibilityMode == ScaleVisibilityMode.AUTO
				|| this.scaleDVisibilityMode == ScaleVisibilityMode.AUTO;
		boolean containsOtherVisibilityMode = this.scaleAVisibilityMode != ScaleVisibilityMode.AUTO
				|| this.scaleBVisibilityMode != ScaleVisibilityMode.AUTO
				|| this.scaleCVisibilityMode != ScaleVisibilityMode.AUTO
				|| this.scaleDVisibilityMode != ScaleVisibilityMode.AUTO;
		if (containsAutoVisibilityMode && containsOtherVisibilityMode) {
			throw new IllegalStateException(
					"On the current version, the visibility mode 'AUTO' can not be used together with the other modes, in one axis."
			);
		}
	}
}
