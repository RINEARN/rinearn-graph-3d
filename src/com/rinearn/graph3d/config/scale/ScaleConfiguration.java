package com.rinearn.graph3d.config.scale;

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
}
