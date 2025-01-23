package com.rinearn.graph3d.config;

import com.rinearn.graph3d.config.data.SeriesFilter;
import com.rinearn.graph3d.config.data.SeriesFilterMode;
import com.rinearn.graph3d.config.data.IndexSeriesFilter;

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
*/


/**
 * The class storing configuration values of plotting options.
 */
public final class OptionConfiguration {

	/**
	 * Creates a new configuration storing default values.
	 */
	public OptionConfiguration() {
	}


	/** Stores the configuration of "With Points" option. */
	private volatile PointOptionConfiguration pointOptionConfiguration = new PointOptionConfiguration();

	/** Stores the configuration of "With Lines" option. */
	private volatile LineOptionConfiguration lineOptionConfiguration = new LineOptionConfiguration();

	/** Stores the configuration of "With Meshes" option. */
	private volatile MeshOptionConfiguration meshOptionConfiguration = new MeshOptionConfiguration();

	/** Stores the configuration of "With Surfaces" option. */
	private volatile SurfaceOptionConfiguration surfaceOptionConfiguration = new SurfaceOptionConfiguration();

	/** Stores the configuration of "Gradient" option. */
	private volatile GradientOptionConfiguration gradientOptionConfiguration = new GradientOptionConfiguration();


	/**
	 * Sets the configuration of "With Points" option.
	 */
	public synchronized void getPointOptionConfiguration(PointOptionConfiguration pointOptionConfiguration) {
		this.pointOptionConfiguration = pointOptionConfiguration;
	}

	/**
	 * Gets the configuration of "With Points" option.
	 */
	public synchronized PointOptionConfiguration getPointOptionConfiguration() {
		return this.pointOptionConfiguration;
	}


	/**
	 * Sets the configuration of "With Lines" option.
	 */
	public synchronized void getLineOptionConfiguration(LineOptionConfiguration lineOptionConfiguration) {
		this.lineOptionConfiguration = lineOptionConfiguration;
	}

	/**
	 * Gets the configuration of "With Lines" option.
	 */
	public synchronized LineOptionConfiguration getLineOptionConfiguration() {
		return this.lineOptionConfiguration;
	}


	/**
	 * Sets the configuration of "With Meshes" option.
	 */
	public synchronized void getMeshOptionConfiguration(MeshOptionConfiguration meshOptionConfiguration) {
		this.meshOptionConfiguration = meshOptionConfiguration;
	}

	/**
	 * Gets the configuration of "With Meshes" option.
	 */
	public synchronized MeshOptionConfiguration getMeshOptionConfiguration() {
		return this.meshOptionConfiguration;
	}


	/**
	 * Sets the configuration of "With Surfaces" option.
	 */
	public synchronized void getSurfaceOptionConfiguration(SurfaceOptionConfiguration surfaceOptionConfiguration) {
		this.surfaceOptionConfiguration = surfaceOptionConfiguration;
	}

	/**
	 * Gets the configuration of "With Surfaces" option.
	 */
	public synchronized SurfaceOptionConfiguration getSurfaceOptionConfiguration() {
		return this.surfaceOptionConfiguration;
	}


	/**
	 * Sets the configuration of "Gradient" option.
	 */
	public synchronized void getGradientOptionConfiguration(GradientOptionConfiguration gradientOptionConfiguration) {
		this.gradientOptionConfiguration = gradientOptionConfiguration;
	}

	/**
	 * Gets the configuration of "Gradient" option.
	 */
	public synchronized GradientOptionConfiguration getGradientOptionConfiguration() {
		return this.gradientOptionConfiguration;
	}


	/**
	 * The base class of *OptionConfiguration classes.
	 */
	private static abstract class AbstractOptionConfiguration {

		/** The flag representing whether this option is enabled. */
		private volatile boolean optionEnabled = false;

		/** The mode of the series filter. */
		private volatile SeriesFilterMode seriesFilterMode = SeriesFilterMode.NONE;

		/** The index-based series filter, used in INDEX mode. */
		private volatile IndexSeriesFilter indexSeriesFilter = new IndexSeriesFilter();

		/** The custom implementation of a series filter, used in CUSTOM mode. */
		private volatile SeriesFilter customSeriesFilter = null;

		/**
		 * Enable or disable this option.
		 *
		 * @param optionEnabled Specify true to enable, false to disable.
		 */
		public synchronized final void setSelected(boolean optionEnabled) {
			this.optionEnabled = optionEnabled;
		}

		/**
		 * Checks whether this option is enabled.
		 *
		 * @return Returns true if this option is enabled.
		 */
		public synchronized final boolean isOptionEnabled() {
			return this.optionEnabled;
		}

		/**
		 * Sets the mode of the series filter.
		 *
		 * @param seriesFilterMode the mode of the series filter.
		 */
		public synchronized void setSeriesFilterMode(SeriesFilterMode seriesFilterMode) {
			this.seriesFilterMode = seriesFilterMode;
		}

		/**
		 * Gets the mode of the series filter.
		 *
		 * @param seriesFilterMode the mode of the series filter.
		 */
		public synchronized SeriesFilterMode getSeriesFilterMode() {
			return this.seriesFilterMode;
		}

		/**
		 * Gets the series filter corresponding to the current mode.
		 *
		 * @return The series filter corresponding to the current mode.
		 */
		public synchronized SeriesFilter getSeriesFilter() {
			switch (this.seriesFilterMode) {
				case INDEX: return this.indexSeriesFilter;
				case CUSTOM: return this.customSeriesFilter;
				default: throw new IllegalStateException("Unexpected series filter mode: " + this.seriesFilterMode);
			}
		}

		/**
		 * Sets the index-based series filter, used in INDEX mode.
		 *
		 * @param indexSeriesFilter The index-based series filter, used in INDEX mode.
		 */
		public synchronized void setIndexSeriesFilter(IndexSeriesFilter indexSeriesFilter) {
			this.indexSeriesFilter = indexSeriesFilter;
		}

		/**
		 * Gets the index-based series filter, used in INDEX mode.
		 *
		 * @return The index-based series filter, used in INDEX mode.
		 */
		public synchronized IndexSeriesFilter getIndexSeriesFilter() {
			return this.indexSeriesFilter;
		}

		/**
		 * Sets the custom implementation of a filter, used in CUSTOM mode.
		 *
		 * @param customSeriesFilter The custom implementation of a  series filter, used in CUSTOM mode.
		 */
		public synchronized void setCustomSeriesFilter(SeriesFilter customSeriesFilter) {
			this.customSeriesFilter = customSeriesFilter;
		}

		/**
		 * Gets the custom implementation of a filter, used in CUSTOM mode.
		 *
		 * @return The custom implementation of a  series filter, used in CUSTOM mode.
		 */
		public synchronized SeriesFilter getCustomSeriesFilter() {
			return this.customSeriesFilter;
		}
	}


	/**
	 * The class storing configuration values of "With Points" option.
	 */
	public static final class PointOptionConfiguration extends AbstractOptionConfiguration {

		/** The radius (in pixels) of points plotted by this option. */
		private volatile double pointRadius = 2.0;

		/**
		 * Creates a new instance.
		 */
		public PointOptionConfiguration() {
			super.setSelected(true);
		}

		/**
		 * Sets the radius (in pixels) of points plotted by this option.
		 *
		 * @param pointRadius The radius (in pixels) of points plotted by this option.
		 */
		public synchronized void setPointRadius(double pointRadius) {
			this.pointRadius = pointRadius;
		}

		/**
		 * Gets the radius (in pixels) of points plotted by this option.
		 *
		 * @return The radius (in pixels) of points plotted by this option.
		 */
		public synchronized double getPointRadius() {
			return this.pointRadius;
		}
	}


	/**
	 * The class storing configuration values of "With Lines" option.
	 */
	public static final class LineOptionConfiguration extends AbstractOptionConfiguration {

		/** The width (in pixels) of lines plotted by this option. */
		private volatile double lineWidth = 1.0;

		/**
		 * Creates a new instance.
		 */
		public LineOptionConfiguration() {
			super.setSelected(false);
		}

		/**
		 * Sets the width (in pixels) of lines plotted by this option.
		 *
		 * @param lineWidth The width (in pixels) of lines plotted by this option.
		 */
		public synchronized void setLineWidth(double lineWidth) {
			this.lineWidth = lineWidth;
		}

		/**
		 * Gets the width (in pixels) of lines plotted by this option.
		 *
		 * @return The width (in pixels) of lines plotted by this option.
		 */
		public synchronized double getLineWidth() {
			return this.lineWidth;
		}
	}


	/**
	 * The class storing configuration values of "With Meshes" option.
	 */
	public static final class MeshOptionConfiguration extends AbstractOptionConfiguration {

		/** The width (in pixels) of lines composing meshes plotted by this option. */
		private volatile double lineWidth = 1.0;

		/**
		 * Creates a new instance.
		 */
		public MeshOptionConfiguration() {
			super.setSelected(false);
		}

		/**
		 * Sets the width (in pixels) of lines composing meshes plotted by this option.
		 *
		 * @param lineWidth The width (in pixels) of lines.
		 */
		public synchronized void setLineWidth(double lineWidth) {
			this.lineWidth = lineWidth;
		}

		/**
		 * Gets the width (in pixels) of lines composing meshes plotted by this option.
		 *
		 * @return The width (in pixels) of lines.
		 */
		public synchronized double getLineWidth() {
			return this.lineWidth;
		}
	}


	/**
	 * The class storing configuration values of "With Surfaces" option.
	 */
	public static final class SurfaceOptionConfiguration extends AbstractOptionConfiguration {

		/**
		 * Creates a new instance.
		 */
		public SurfaceOptionConfiguration() {
			super.setSelected(false);
		}
	}


	/**
	 * The class storing configuration values of "Gradient" option.
	 */
	public static final class GradientOptionConfiguration extends AbstractOptionConfiguration {

		/**
		 * Creates a new instance.
		 */
		public GradientOptionConfiguration() {
			super.setSelected(false);
		}

		// !!! NOTE !!!
		// On Ver.6, this "Gradient" option works as a short-cut UI
		// for switching the current coloring mode optionEnabled in "Settings" > "Set Colors" menu.
		// So we don't define detailed parameters here. They are defined in "ColorConfiguration" class.
	}
}
