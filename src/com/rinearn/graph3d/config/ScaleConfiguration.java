package com.rinearn.graph3d.config;

import com.rinearn.graph3d.config.scale.TickLabelFormatter;
import com.rinearn.graph3d.config.scale.NumericTickLabelFormatter;
import com.rinearn.graph3d.config.scale.Ticker;
import com.rinearn.graph3d.config.scale.TickerMode;
import com.rinearn.graph3d.config.scale.EqualDivisionTicker;
import com.rinearn.graph3d.config.scale.ManualTicker;
import com.rinearn.graph3d.config.scale.TickLabelFormatterMode;
import com.rinearn.graph3d.config.scale.ScaleVisibilityMode;

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
//
// -> しかしあっちは AxisLabelConfiguration って粒度階層を一枚かませてるからなあ。だからこその .setText & .getText で。
//    tickLabel も、たとえば単一の TickLabel をクラスに包んで、それに .setMargin とかするなら .setText になると思う。
//    が、それはあまりにも冗長過ぎるし…
//    しかし確かに tickLabelText にすべきってのは一利あるか。要検討。
//
//    ・ 標準GUIでも setLabel は非推奨化して setText に置き換えられてたりするし、やっぱ setLabel でテキスト設定というのは概念枠が狭すぎそう。
//
//    -> ところでそれは今の実装だとこのクラスじゃなくて Ticker の管轄か。あっちに書かんと。でもあっちだと読まんだろうしこっちに残しとくか。
//
//    -> 一応 Ticker 側は改名した。しかしAPI側は既に setXLabel とかある事に気づいた。
//       -> それなら API は API で簡潔さ重視でいいのでは。APIでカバーしきれない細かい設定を config コンテナでカバーしようってコンセプトだし、
//          API はそれくらいの省略はほどほどにした方がよくて、config コンテナはなるべく将来微妙化しないように慎重めに命名しとく、って感じで。
//
//    -> 将来的に setTickLabelAngles とか追加する可能性がある。めっちゃありそう。なのでやっぱ setTickLabelTexts にしとくのは無難で妥当。
//
// しばらく経った頃にまた俯瞰して要検討、反対方向のブレなければそのまま確定方向で。
//
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
}
