package com.rinearn.graph3d.config.plotter;


/*
[!!!!! NOTE !!!!!]

既に5.6から setOptionSelected(...)/setOptionParatmeter(...) API用に
RinearnGraph3DOptionItem/OptionParameter というenum/コンテナオブジェクトがあるけど、
この設定コンテナ内でも上記を使うべき？ それとも上記とは独立に実装する？

・前者の場合、上記のやつはオプション管理の基盤になって、この設定コンテナはそれを config 層用に包む感じになる。

・後者の場合、こっちの設定コンテナが基盤になって、上記のやつはその操作を簡易用途向けにラッピングするやつみたいな感じになる。

-> 後者の方が綺麗そうだけど、しかしせっかくオプション項目の enum が存在してるのに使わないっていう微妙な無駄さは生じる。

   -> でも、前者の方針で RinearnGraph3DOptionParameter を肥大化させてく方向は色々エグい気がする。
      もともと RinearnGraph3DOptionParameter って、
     「よく使う重要なやつだけAPIから直で設定可能にして、それでカバーしないような細かい設定値は設定ファイルを作って読ませる」
      っていう方針だったと思うし。
      各オプションの細かい設定を網羅的に格納するならもっと階層的な構造にしてたはずで、でも単純さ優先で表層に主要項目をベタ置きにした。

      一方、設定ファイルと同じ粒度で設定をカバーするのが Ver.6 の config 層なので、この設定コンテナは非常に細かい粒度でカバーすべき。
      そうすると必然的に RinearnGraph3DOptionParameter とは構造が色々と違ってくるのでは。

とりあえず後者の方針で実装、ほどほどに出来たタイミングで複雑さ等を振り返ってまた再検討する

-> だいぶ間を置いて再考したが、やっぱり後者一択で決着した。
   というのも、対象系列指定をオプション項目ごとにできるようにしたりとかで、そのフィルタモード指定とかフィルタクラスとかが必用になったので、
   もはやそれを前者のようにフラットシンプルな構造に収めるのは無理がある。
   なので、OptionParameter はせっかくあるので簡易利用時のシンプルで分かりやすいやつとして使う。
   そもそも他のAPIもほぼそういう役割になりつつあるし。
   本気で細かい所まで制御するには粒度不足で config 作って投げるしかない。極論 config と同じ粒度で細かいAPI生やすとどえらい事になる。
   APIの表層は「ざっと docs 読んで使ってみるかー」ってなる粒度であるべき。OptionParameter はそれ用。

-> さらにTODOノートでの考察の結果、OptionConfiguration から PlotterConfiguration に改名した。
   なのでここで RinearnGraph3DOptionItem は使わなくていい。概念整理の粒度や世代が完全に異なる。
   RinearnGraph3DOptionItem はGUIに対応した簡易設定用で確定。

*/


/**
 * The class storing configuration values of plotting options.
 */
public final class PlotterConfiguration {

	/**
	 * Creates a new configuration storing default values.
	 */
	public PlotterConfiguration() {
	}


	/** Stores the configuration of "With Points" option. */
	private volatile PointPlotterConfiguration pointPlotterConfiguration = new PointPlotterConfiguration();

	/** Stores the configuration of "With Lines" option. */
	private volatile LinePlotterConfiguration linePlotterConfiguration = new LinePlotterConfiguration();

	/** Stores the configuration of "With Meshes" option. */
	private volatile MeshPlotterConfiguration meshPlotterConfiguration = new MeshPlotterConfiguration();

	/** Stores the configuration of "With Surfaces" option. */
	private volatile SurfacePlotterConfiguration surfacePlotterConfiguration = new SurfacePlotterConfiguration();

	/** Stores the configuration of "With Contours" option. */
	private volatile ContourPlotterConfiguration contourPlotterConfiguration = new ContourPlotterConfiguration();


	/**
	 * Sets the configuration of "With Points" option.
	 */
	public synchronized void setPointPlotterConfiguration(PointPlotterConfiguration pointPlotterConfiguration) {
		this.pointPlotterConfiguration = pointPlotterConfiguration;
	}

	/**
	 * Gets the configuration of "With Points" option.
	 */
	public synchronized PointPlotterConfiguration getPointPlotterConfiguration() {
		return this.pointPlotterConfiguration;
	}


	/**
	 * Sets the configuration of "With Lines" option.
	 */
	public synchronized void setLinePlotterConfiguration(LinePlotterConfiguration linePlotterConfiguration) {
		this.linePlotterConfiguration = linePlotterConfiguration;
	}

	/**
	 * Gets the configuration of "With Lines" option.
	 */
	public synchronized LinePlotterConfiguration getLinePlotterConfiguration() {
		return this.linePlotterConfiguration;
	}


	/**
	 * Sets the configuration of "With Meshes" option.
	 */
	public synchronized void setMeshPlotterConfiguration(MeshPlotterConfiguration meshPlotterConfiguration) {
		this.meshPlotterConfiguration = meshPlotterConfiguration;
	}

	/**
	 * Gets the configuration of "With Meshes" option.
	 */
	public synchronized MeshPlotterConfiguration getMeshPlotterConfiguration() {
		return this.meshPlotterConfiguration;
	}


	/**
	 * Sets the configuration of "With Surfaces" option.
	 */
	public synchronized void setSurfacePlotterConfiguration(SurfacePlotterConfiguration surfaceOptionConfiguration) {
		this.surfacePlotterConfiguration = surfaceOptionConfiguration;
	}

	/**
	 * Gets the configuration of "With Surfaces" option.
	 */
	public synchronized SurfacePlotterConfiguration getSurfacePlotterConfiguration() {
		return this.surfacePlotterConfiguration;
	}


	/**
	 * Sets the configuration of "With Contours" option.
	 */
	public synchronized void setContourPlotterConfiguration(ContourPlotterConfiguration contourPlotterConfiguration) {
		this.contourPlotterConfiguration = contourPlotterConfiguration;
	}

	/**
	 * Gets the configuration of "With Contours" option.
	 */
	public synchronized ContourPlotterConfiguration getContourPlotterConfiguration() {
		return this.contourPlotterConfiguration;
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
	public synchronized void validate() throws IllegalStateException {
		this.pointPlotterConfiguration.validate();
		this.linePlotterConfiguration.validate();
		this.meshPlotterConfiguration.validate();
		this.surfacePlotterConfiguration.validate();
		this.contourPlotterConfiguration.validate();
	}
}
