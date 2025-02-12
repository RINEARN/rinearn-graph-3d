package com.rinearn.graph3d.config;

import com.rinearn.graph3d.config.data.SeriesFilterHub;
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

	/** Stores the configuration of "With Contours" option. */
	private volatile ContourOptionConfiguration contourOptionConfiguration = new ContourOptionConfiguration();

	/** Stores the configuration of "Gradient" option. */
	private volatile GradientOptionConfiguration gradientOptionConfiguration = new GradientOptionConfiguration();


	/**
	 * Sets the configuration of "With Points" option.
	 */
	public synchronized void setPointOptionConfiguration(PointOptionConfiguration pointOptionConfiguration) {
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
	public synchronized void setLineOptionConfiguration(LineOptionConfiguration lineOptionConfiguration) {
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
	public synchronized void setMeshOptionConfiguration(MeshOptionConfiguration meshOptionConfiguration) {
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
	public synchronized void setSurfaceOptionConfiguration(SurfaceOptionConfiguration surfaceOptionConfiguration) {
		this.surfaceOptionConfiguration = surfaceOptionConfiguration;
	}

	/**
	 * Gets the configuration of "With Surfaces" option.
	 */
	public synchronized SurfaceOptionConfiguration getSurfaceOptionConfiguration() {
		return this.surfaceOptionConfiguration;
	}


	/**
	 * Sets the configuration of "With Contours" option.
	 */
	public synchronized void setContourOptionConfiguration(ContourOptionConfiguration contourOptionConfiguration) {
		this.contourOptionConfiguration = contourOptionConfiguration;
	}

	/**
	 * Gets the configuration of "With Contours" option.
	 */
	public synchronized ContourOptionConfiguration getContourOptionConfiguration() {
		return this.contourOptionConfiguration;
	}


	/**
	 * Sets the configuration of "Gradient" option.
	 */
	public synchronized void setGradientOptionConfiguration(GradientOptionConfiguration gradientOptionConfiguration) {
		this.gradientOptionConfiguration = gradientOptionConfiguration;
	}

	/**
	 * Gets the configuration of "Gradient" option.
	 */
	public synchronized GradientOptionConfiguration getGradientOptionConfiguration() {
		return this.gradientOptionConfiguration;
	}


	/**
	 * The base class of *OptionConfiguration classes supporting the series filter feature.
	 */
	private static abstract class SeriesFilterableOptionConfiguration {

		/** The flag representing whether this option is enabled. */
		private volatile boolean optionEnabled = false;

		/** The object which stores multiple kinds of SeriesFilter instances, and provides their setters and getters. */
		private volatile SeriesFilterHub seriesFilterHub = new SeriesFilterHub();

		/**
		 * Enable or disable this option.
		 *
		 * @param optionEnabled Specify true to enable, false to disable.
		 */
		public synchronized final void setOptionEnabled(boolean optionEnabled) {
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
			this.seriesFilterHub.setSeriesFilterMode(seriesFilterMode);
		}

		/**
		 * Gets the mode of the series filter.
		 *
		 * @param seriesFilterMode the mode of the series filter.
		 */
		public synchronized SeriesFilterMode getSeriesFilterMode() {
			return this.seriesFilterHub.getSeriesFilterMode();
		}

		/**
		 * Gets the series filter corresponding to the current mode.
		 *
		 * @return The series filter corresponding to the current mode.
		 */
		public synchronized SeriesFilter getSeriesFilter() {
			return this.seriesFilterHub.getSeriesFilter();
		}

		/**
		 * Sets the index-based series filter, used in INDEX mode.
		 *
		 * @param indexSeriesFilter The index-based series filter, used in INDEX mode.
		 */
		public synchronized void setIndexSeriesFilter(IndexSeriesFilter indexSeriesFilter) {
			this.seriesFilterHub.setIndexSeriesFilter(indexSeriesFilter);
		}

		/**
		 * Gets the index-based series filter, used in INDEX mode.
		 *
		 * @return The index-based series filter, used in INDEX mode.
		 */
		public synchronized IndexSeriesFilter getIndexSeriesFilter() {
			return this.seriesFilterHub.getIndexSeriesFilter();
		}

		/**
		 * Sets the custom implementation of a filter, used in CUSTOM mode.
		 *
		 * @param customSeriesFilter The custom implementation of a  series filter, used in CUSTOM mode.
		 */
		public synchronized void setCustomSeriesFilter(SeriesFilter customSeriesFilter) {
			this.seriesFilterHub.setCustomSeriesFilter(customSeriesFilter);
		}

		/**
		 * Gets the custom implementation of a filter, used in CUSTOM mode.
		 *
		 * @return The custom implementation of a  series filter, used in CUSTOM mode.
		 */
		public synchronized SeriesFilter getCustomSeriesFilter() {
			return this.seriesFilterHub.getCustomSeriesFilter();
		}
	}


	/** The enum representing each style mode of "With Points" option. */
	public enum PointStyleMode {

		/** Draws coordinate points by circles. */
		CIRCLE,

		/** Draws coordinate points by markers. */
		MARKER;
	}

	/**
	 * The class storing configuration values of "With Points" option.
	 */
	public static final class PointOptionConfiguration extends SeriesFilterableOptionConfiguration {

		/** The style mode for drawing points. */
		private volatile PointStyleMode pointStyleMode = PointStyleMode.CIRCLE;

		/** The radius (in pixels) of points in CIRCLE mode. */
		private volatile double circleRadius = 2.0;

		/** The font size of markers in MARKER mode. */
		private volatile double markerSize = 10.0;

		/** The ratio to correct the vartical position of markers. */
		private volatile double markerVerticalOffsetRatio = 0.2;

		/** The texts (of the symbols) of the markers. */
		private volatile String[] markerTexts = { "●", "■", "▲", "▼", "〇", "□", "△", "▽", "◇", "×" };

		/** The flag to whether draw markers in bold fonts. */
		private volatile boolean markerBold = false;

		/**
		 * Creates a new instance.
		 */
		public PointOptionConfiguration() {
			super.setOptionEnabled(true);
		}

		/**
		 * Sets the style mode for drawing points.
		 *
		 * @param pointStyleMode The style mode for drawing points.
		 */
		public synchronized void setPointStyleMode(PointStyleMode pointStyleMode) {
			this.pointStyleMode = pointStyleMode;
		}

		/**
		 * Gets the style mode for drawing points.
		 *
		 * @return The style mode for drawing points.
		 */
		public synchronized PointStyleMode getPointStyleMode() {
			return this.pointStyleMode;
		}

		/**
		 * Sets the radius (in pixels) of points in CIRCLE mode.
		 *
		 * @param pointRadius The radius (in pixels) of points plotted by this option.
		 */
		public synchronized void setCircleRadius(double circleRadius) {
			this.circleRadius = circleRadius;
		}

		/**
		 * Gets the radius (in pixels) of points in CIRCLE mode.
		 *
		 * @return The radius (in pixels) of points plotted by this option.
		 */
		public synchronized double getCircleRadius() {
			return this.circleRadius;
		}

		/**
		 * Sets the font size of markers.
		 *
		 * @param markerSize The font size of markers.
		 */
		public synchronized void setMarkerSize(double markerSize) {
			this.markerSize = markerSize;
		}

		/**
		 * Gets the radius (in pixels) of points plotted by this option.
		 *
		 * @return The font size of markers.
		 */
		public synchronized double getMarkerSize() {
			return this.markerSize;
		}

		/**
		 * Sets the ratio to correct the vartical position of markers.
		 *
		 * @param markerVerticalOffsetRatio The ratio to correct the vartical position of markers.
		 */
		public void setMarkerVerticalOffsetRatio(double markerVerticalOffsetRatio) {
			this.markerVerticalOffsetRatio = markerVerticalOffsetRatio;
		}

		/**
		 * Gets the ratio to correct the vartical position of markers.
		 *
		 * @return markerVerticalOffsetRatio The ratio to correct the vartical position of markers.
		 */
		public double getMarkerVerticalOffsetRatio() {
			return this.markerVerticalOffsetRatio;
		}

		/**
		 * Sets the texts (of the symbols) of the markers.
		 *
		 * @param markerTexts The texts (of the symbols) of the markers.
		 */
		public void setMarkerTexts(String[] markerTexts) {
			this.markerTexts = markerTexts;
		}

		/**
		 * Gets the texts (of the symbols) of the markers.
		 *
		 * @return The texts (of the symbols) of the markers.
		 */
		public String[] getMarkerTexts() {
			return this.markerTexts;
		}

		/**
		 * Sets the flag to whether draw markers in bold fonts.
		 *
		 * @param markerBold The flag to whether draw markers in bold fonts.
		 */
		public void setMarkerBold(boolean markerBold) {
			this.markerBold = markerBold;
		}

		/**
		 * Gets the flag to whether draw markers in bold fonts.
		 *
		 * @return The flag to whether draw markers in bold fonts.
		 */
		public boolean isMarkerBold() {
			return this.markerBold;
		}
	}


	/**
	 * The class storing configuration values of "With Lines" option.
	 */
	public static final class LineOptionConfiguration extends SeriesFilterableOptionConfiguration {

		/** The width (in pixels) of lines plotted by this option. */
		private volatile double lineWidth = 1.0;

		/**
		 * Creates a new instance.
		 */
		public LineOptionConfiguration() {
			super.setOptionEnabled(false);
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
	public static final class MeshOptionConfiguration extends SeriesFilterableOptionConfiguration {

		/** The width (in pixels) of lines composing meshes plotted by this option. */
		private volatile double lineWidth = 1.0;

		/**
		 * Creates a new instance.
		 */
		public MeshOptionConfiguration() {
			super.setOptionEnabled(false);
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
	public static final class SurfaceOptionConfiguration extends SeriesFilterableOptionConfiguration {

		/**
		 * Creates a new instance.
		 */
		public SurfaceOptionConfiguration() {
			super.setOptionEnabled(false);
		}
	}


	/**
	 * The class storing configuration values of "With Contours" option.
	 */
	public static final class ContourOptionConfiguration extends SeriesFilterableOptionConfiguration {

		/**
		 * Creates a new instance.
		 */
		public ContourOptionConfiguration() {
			super.setOptionEnabled(false);
		}
	}


	/**
	 * The class storing configuration values of "Gradient" option.
	 */
	public static final class GradientOptionConfiguration extends SeriesFilterableOptionConfiguration {

		/**
		 * Creates a new instance.
		 */
		public GradientOptionConfiguration() {
			super.setOptionEnabled(false);
		}

		// !!! NOTE !!!
		// On Ver.6, this "Gradient" option works as a short-cut UI
		// for switching the current coloring mode optionEnabled in "Settings" > "Set Colors" menu.
		// So we don't define detailed parameters here. They are defined in "ColorConfiguration" class.
	}
}
