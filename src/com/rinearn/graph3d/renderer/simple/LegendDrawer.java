package com.rinearn.graph3d.renderer.simple;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.screen.ScreenConfiguration;
import com.rinearn.graph3d.config.label.LegendLabelConfiguration;
import com.rinearn.graph3d.config.color.AxisGradientColor;
import com.rinearn.graph3d.config.color.ColorConfiguration;
import com.rinearn.graph3d.config.color.GradientColor;
import com.rinearn.graph3d.config.data.SeriesFilterMode;
import com.rinearn.graph3d.config.data.DataConfiguration;
import com.rinearn.graph3d.config.data.IndexSeriesFilter;
import com.rinearn.graph3d.config.data.SeriesAttribute;
import com.rinearn.graph3d.config.data.SeriesFilter;
import com.rinearn.graph3d.config.plotter.PlotterConfiguration;
import com.rinearn.graph3d.config.plotter.PointPlotterConfiguration;
import com.rinearn.graph3d.config.plotter.LinePlotterConfiguration;
import com.rinearn.graph3d.config.plotter.MeshPlotterConfiguration;
import com.rinearn.graph3d.config.plotter.SurfacePlotterConfiguration;
import com.rinearn.graph3d.config.plotter.ContourPlotterConfiguration;
import com.rinearn.graph3d.config.plotter.PointStyleMode;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.math.BigDecimal;

/*
	Note:
	calculateGradientTargetFlags() の実装、現状だと系列フィルタが IndexSeriesFilter しかないから算出できてるけど、
	別種の系列フィルタができたら無理になる。この描画エンジン層では、データ系列の素の SeriesAttribute にアクセスできないので。
	どうする？

	* 案1: config にデータ自身が含まれない事は変えようがないので、上層からフィルタ済みの色分け情報を描画エンジンに渡す。
	* 案2: フィルタに SeriesAttribute[] を事前設定するようにして、判定時は index のみで判定できるようにす。

	の2択か。案2だと SeriesFilter の仕様を変える必要があるので、リリース前に決める必要がある。

	-> 「Index-based かどうかをモード検査して、SeriesAttribute を生成してインデックス詰めて、フィルタに渡して判定」が
	    イディオムみたいになってて複数の場面で登場し、ちょっと煩雑なので、案2の方がいい気もする。

    -> いや、「config の中のフィルタが状態を控えてて、それがプロット＆描画フローの中でバッファ的に使われる」ってのは
       そもそも設計として悪手なような。
       そういうのを許すと、「どの状態で config を参照するかによって内容が変わる」とかになりかねない。
       避けた方がいいような。

       * 確かに、案1が設計としては本来あるべき形で、案2は利便性のためにちょっとトリッキーな事をやってる感はある。プロット中に状態変わるのなら。

       -> しかし凡例のマーカー描く際にも各オプションのフィルタ要る事にいま気付いた。
          案1じゃないと無理かもしれん。
          だって全プロットオプションの対象系列インデックスを上層から渡すのは非現実的でしょ。さすがに。
          ここはもう案1にするしかしょうがないんじゃないか。

          -> そもそもオプションや系列フィルタを勘案の上で、描画エンジンに点や線とかの描画処理を発行してるのって上層の Handlers の役割だし、
             この凡例だけ内部でそんな事をやる必用が生じる設計がそもそもまずい、という観点もある。

             * 確かに、ここでそれをやるんだったら Handlers もエンジン内にあるのが妥当で、ちぐはぐか。

             -> でもわざわざ凡例描くために上層で大量のコマンド発行したくないなあ。

             -> 上層で Handlers という枠組みを用意して、低抽象度な描画コマンドを使って描いてるのは、カスタマイズ性を上げるための構造でもある。
                つまり PlottingListeners を implements した Handler クラスが、Renderer のインターフェースを使って描く、という制約を入れる事で、
                プロットオプションを自作可能にしている。

                で、果たして凡例でそんな独自クラス実装するレベルのカスタマイズ性が要るか？っていう。

                -> いや、あったら普通に欲しいなそれは。

                   -> そんなんもう画面の Graphics2D に描けばええやんって思う。そこゴツくするメリットに釣り合わないでしょ。学習コストも。

                      もう案1でええやん。考えすぎやって。悪い癖や。案1で全て解決するんや。実装もシンプルに。
                      プロット中に config がバッファ的に状態持つとか、こっち側で気を付ければいいだけで、ユーザー側をややこしくするわけではないし。

	上の話の妥協案として新案が浮かんだ：

	* 案3: どうせデータ変換の設定とかで DataConfiguration とかできるだろうし、そこに getGlobalSeriesAttributes() とか作れば？
	        config に状態を持たせるのが妥協点なら、せめて Filter に持たせずにデータ管轄のやつに持たせればマシかも、みたいな感じ。

			-> 少し考えてみたけど、いいかも。
			   データのプロット時に DataConfiguration の一部が変わるならまあまだ納得はできる（マシという意味で）。
			   RangeConfiguration のレンジが、AutoRange が有効の場合にプロット時に変わるのと似たようなニュアンスで。
			   LegendConfiguration の凡例テキストも AutoLegendGenration が有効の時はプロット時に変わるし。

			   結局、「系列フィルタの状態」がプロット時に変わるという事が気持ち悪かったわけで、
			   データ関連情報格納の枠に入ってる系列情報が変わるのはそこまで違和感ない。そういう事か。

	また要検討。
*/


/*
	Note:

	凡例テキストの左のアイコン、点でも線でも、グラデのやつはタイルでいいんじゃない？ もう。

	-> しかし、グラデで点プロットでマーカーを使った場合、どれがどれか分からなくなる。
	   ので、マーカーはやっぱりグラデON時は白でマーカー記号を描くのが妥当かと。
	   そういう利用シーンを考えたら、やっぱりそれが一番見やすいでしょ。

	   * 確かに。で、それをそうすべきなら、線も線で描かなきゃ変か。

	   * 逆にメッシュは面っぽいのでタイルでいい。surface と併用もするだろうし。

	   * 等高線は線だな。-> いや、カラーマップっぽいからタイルじゃないの？ -> いや、やっぱり線な気がする。

	   * 面とマーカーの併用とか、面と線の併用とかは？ -> それはタイルでいいと思う。変に重ねるとややこしい。

*/


/**
 * The class to draw legends over the 3D-rendered graph screen.
 */
public final class LegendDrawer {

	/** Stores the configuration of this application. */
	private volatile RinearnGraph3DConfiguration config = null;

	/**
	 * Creates a new instance.
	 */
	public LegendDrawer() {
	}


	/**
	 * Sets the configuration.
	 *
	 * @param configuration The configuration.
	 */
	public synchronized void setConfiguration(RinearnGraph3DConfiguration configuration) {
		this.config = configuration;
	}


	/**
	 * The container storing the enabled/disabled states of plot options for each data series after filtering,
	 * called "filtered result flags".
	 */
	private static class FilteredResultFlags {
		private int seriesCount;
		public boolean[] legendTextExistFlags;
		public boolean[] pointPlotterTargetFlags;
		public boolean[] linePlotterTargetFlags;
		public boolean[] meshPlotterTargetFlags;
		public boolean[] surfacePlotterTargetFlags;
		public boolean[] contourPlotterTargetFlags;
		public boolean[] lineGroupPlotterTargetFlags;
		public boolean[] tileGroupPlotterTargetFlags;

		/**
		 * Creates a new instance for storing results of data series of which number is seriesCount.
		 *
		 * @param length The number of the data series.
		 */
		public FilteredResultFlags(int seriesCount) {
			this.seriesCount = seriesCount;
			this.legendTextExistFlags = new boolean[seriesCount];
			this.pointPlotterTargetFlags = new boolean[seriesCount];
			this.linePlotterTargetFlags = new boolean[seriesCount];
			this.meshPlotterTargetFlags = new boolean[seriesCount];
			this.surfacePlotterTargetFlags = new boolean[seriesCount];
			this.contourPlotterTargetFlags = new boolean[seriesCount];
			this.lineGroupPlotterTargetFlags = new boolean[seriesCount];
			this.tileGroupPlotterTargetFlags = new boolean[seriesCount];
		}
	}


	/**
	 * Generates the container storing the enabled/disabled states of plot options for each data series after filtering,
	 * called "filtered result flags".
	 *
	 * @return The filtered result flags.
	 */
	private FilteredResultFlags generateFilteredResultFlags() {

		// Get series attributes of all the registered data series.
		DataConfiguration dataConfig = this.config.getDataConfiguration();
		SeriesAttribute[] seriesAttributes = dataConfig.getGlobalSeriesAttributes();
		int seriesCount = seriesAttributes.length;

		// Create the container of the flags.
		FilteredResultFlags flags = new FilteredResultFlags(seriesCount);

		// Get configurations of plot options.
		PlotterConfiguration optionConfig = this.config.getPlotterConfiguration();
		PointPlotterConfiguration pointPlotterConfig = optionConfig.getPointPlotterConfiguration();
		LinePlotterConfiguration linePlotterConfig = optionConfig.getLinePlotterConfiguration();
		MeshPlotterConfiguration meshPlotterConfig = optionConfig.getMeshPlotterConfiguration();
		SurfacePlotterConfiguration surfacePlotterConfig = optionConfig.getSurfacePlotterConfiguration();
		ContourPlotterConfiguration contourPlotterConfig = optionConfig.getContourPlotterConfiguration();

		// Get enabled/disabled states for each plot option.
		boolean pointPlotterEnabled = pointPlotterConfig.isPlotterEnabled();
		boolean linePlotterEnabled = linePlotterConfig.isPlotterEnabled();
		boolean meshPlotterEnabled = meshPlotterConfig.isPlotterEnabled();
		boolean surfacePlotterEnabled = surfacePlotterConfig.isPlotterEnabled();
		boolean contourPlotterEnabled = contourPlotterConfig.isPlotterEnabled();

		// Extract series filters for each plot option.
		boolean pointFilterEnabled = pointPlotterConfig.getSeriesFilterMode() != SeriesFilterMode.NONE;
		boolean lineFilterEnabled = linePlotterConfig.getSeriesFilterMode() != SeriesFilterMode.NONE;
		boolean meshFilterEnabled = meshPlotterConfig.getSeriesFilterMode() != SeriesFilterMode.NONE;
		boolean surfacePlotterFilterEnabled = surfacePlotterConfig.getSeriesFilterMode() != SeriesFilterMode.NONE;
		boolean contourFilterEnabled = contourPlotterConfig.getSeriesFilterMode() != SeriesFilterMode.NONE;
		SeriesFilter pointFilter = pointFilterEnabled ? pointPlotterConfig.getSeriesFilter() : null;
		SeriesFilter lineFilter = lineFilterEnabled ? linePlotterConfig.getSeriesFilter() : null;
		SeriesFilter meshFilter = meshFilterEnabled ? meshPlotterConfig.getSeriesFilter() : null;
		SeriesFilter surfaceFilter = surfacePlotterFilterEnabled ? surfacePlotterConfig.getSeriesFilter() : null;
		SeriesFilter contourFilter = contourFilterEnabled ? contourPlotterConfig.getSeriesFilter() : null;

		// Calculate and set values of the results.
		for (int iseries=0; iseries<seriesCount; iseries++) {
			SeriesAttribute seriesAttribute = seriesAttributes[iseries];

			// Set whether the legend is not empty.
			// (We regard that legends consisting of white-spaces are NOT empty here, so don't use trim() or isEmpty())
			flags.legendTextExistFlags[iseries] = seriesAttribute.getModifiableLegend().length() != 0;

			// Set the enabled/disabled state of "With Points" option for this series.
			if (pointPlotterEnabled) {
				if (pointFilterEnabled) {
					flags.pointPlotterTargetFlags[iseries] = pointFilter.isSeriesIncluded(seriesAttribute);
				} else {
					flags.pointPlotterTargetFlags[iseries] = true;
				}
			} else {
				flags.pointPlotterTargetFlags[iseries] = false;
			}

			// Set the enabled/disabled state of "With Lines" option for this series.
			if (linePlotterEnabled) {
				if (lineFilterEnabled) {
					flags.linePlotterTargetFlags[iseries] = lineFilter.isSeriesIncluded(seriesAttribute);
				} else {
					flags.linePlotterTargetFlags[iseries] = true;
				}
			} else {
				flags.linePlotterTargetFlags[iseries] = false;
			}

			// Set the enabled/disabled state of "With Meshes" option for this series.
			if (meshPlotterEnabled) {
				if (meshFilterEnabled) {
					flags.meshPlotterTargetFlags[iseries] = meshFilter.isSeriesIncluded(seriesAttribute);
				} else {
					flags.meshPlotterTargetFlags[iseries] = true;
				}
			} else {
				flags.meshPlotterTargetFlags[iseries] = false;
			}

			// Set the enabled/disabled state of "With Surfaces" option for this series.
			if (surfacePlotterEnabled) {
				if (surfacePlotterFilterEnabled) {
					flags.surfacePlotterTargetFlags[iseries] = surfaceFilter.isSeriesIncluded(seriesAttribute);
				} else {
					flags.surfacePlotterTargetFlags[iseries] = true;
				}
			} else {
				flags.surfacePlotterTargetFlags[iseries] = false;
			}

			// Set the enabled/disabled state of "With Contours" option for this series.
			if (contourPlotterEnabled) {
				if (contourFilterEnabled) {
					flags.contourPlotterTargetFlags[iseries] = contourFilter.isSeriesIncluded(seriesAttribute);
				} else {
					flags.contourPlotterTargetFlags[iseries] = true;
				}
			} else {
				flags.contourPlotterTargetFlags[iseries] = false;
			}

			// Calculate the values of the compound flags.
			flags.lineGroupPlotterTargetFlags[iseries] = flags.linePlotterTargetFlags[iseries] || flags.contourPlotterTargetFlags[iseries];
			flags.tileGroupPlotterTargetFlags[iseries] = flags.surfacePlotterTargetFlags[iseries] || flags.meshPlotterTargetFlags[iseries];
		}
		return flags;
	}


	/**
	 * Draws the color bar on the graph screen image.
	 *
	 * @param graphics The Graphics2D object to draw contents on the graph screen image.
	 */
	public void draw(Graphics2D graphics) {
		if (!this.config.getLabelConfiguration().isLegendLabelsVisible()) {
			return;
		}

		// Extract/define position parameters.
		ScreenConfiguration screenConfig = this.config.getScreenConfiguration();
		int screenWidth = screenConfig.getScreenWidth();
		int screenHeight = screenConfig.getScreenHeight();
		int legendOffsetX = 30;
		int legendOffsetY = 30;

		// Calculate the position of legends.
		int[] legendTextAreaPosition = this.calculateLegendTextAraaPosition(graphics, screenWidth, screenHeight, legendOffsetX, legendOffsetY);
		int legendTextLineHeight = this.calculateLegendTextLineHeight(graphics);

		// Calculate whether each data series is drawn by gradient colors, and store the results into an array.
		boolean[] gradientTargetFlags = this.calculateGradientTargetFlags();

		// Generates the container storing the enabled/disabled states of plot options for each data series after filtering,
		// called "filtered result flags".
		FilteredResultFlags filteredResultFlags = generateFilteredResultFlags();

		// Draw legend texts.
		this.drawLegendTexts(graphics, legendTextAreaPosition, legendTextLineHeight, filteredResultFlags);

		// Draw line icons.
		int lineOffsetX = -6;
		this.drawLineIcons(
				graphics, legendTextAreaPosition, legendTextLineHeight, lineOffsetX, filteredResultFlags, gradientTargetFlags
		);

		// Draw point marker icons.
		// boolean useForegroundColorInsteadOfGradient = !lineGroupOptionEnabled;
		boolean useForegroundColorInsteadOfGradient = true;
		int markerOffsetXWithLine = -20;
		int markerOffsetXWithoutLine = -6;
		this.drawPointMarkerIcons(
				graphics, legendTextAreaPosition, legendTextLineHeight, markerOffsetXWithLine, markerOffsetXWithoutLine,
				filteredResultFlags, gradientTargetFlags, useForegroundColorInsteadOfGradient
		);

		// Draw tiles icons.
		int tileOffsetX = -8;
		this.drawTileIcons(
				graphics, legendTextAreaPosition, legendTextLineHeight, tileOffsetX, filteredResultFlags, gradientTargetFlags
		);
	}


	/**
	 * Calculates the position of the left-top point of the text area of legends (not including markers).
	 *
	 * @param graphics The Graphics2D object to draw contents on the graph screen image.
	 * @param screenWidth The width of the graph screen.
	 * @param screenHeight The height of the graph screen.
	 * @param legendOffsetX The offset amount [pixels] of the X-position of the legends.
	 * @param legendOffsetY The offset amount [pixels] of the Y-position of the legends.
	 * @return The array storing x (at [0]) and y (at [1]) of the left-top point of the legend text area (not including markers).
	 */
	private int[] calculateLegendTextAraaPosition(Graphics2D graphics, int screenWidth, int screenHeight, int legendOffsetX, int legendOffsetY) {

		// Extract legend settings.
		String[] legendTexts = this.config.getLabelConfiguration().getLegendLabelConfiguration().getLabelTexts();
		Font legendFont = this.config.getFontConfiguration().getLegendLabelFont();

		FontMetrics fontMetrics = graphics.getFontMetrics(legendFont);

		// Detect the maximum length (pixels) of the legend text on the display.
		int maxTextWidth = 0;
		for (String legendText: legendTexts) {
			int textWidth = fontMetrics.stringWidth(legendText);
			if (maxTextWidth < textWidth) {
				maxTextWidth = textWidth;
			}
		}

		int legendAreaX = screenWidth - maxTextWidth - legendOffsetX;
		int legendAreaY = legendOffsetY;
		return new int[] { legendAreaX, legendAreaY };
	}


	/**
	 * Calculates the line-height of the legend texts.
	 *
	 * @param graphics The Graphics2D object to draw contents on the graph screen image.
	 * @return The line-height of the legend texts.
	 */
	private int calculateLegendTextLineHeight(Graphics2D graphics) {
		Font legendFont = this.config.getFontConfiguration().getLegendLabelFont();
		FontMetrics fontMetrics = graphics.getFontMetrics(legendFont);
		int fontHeight = fontMetrics.getAscent();
		return (int)(fontHeight * 1.35);
	}


	/**
	 * Calculate whether each data series is drawn by gradient colors, and store the results into an array.
	 *
	 * @return The array storing the result.
	 */
	private boolean[] calculateGradientTargetFlags() {
		int legendCount = this.config.getLabelConfiguration().getLegendLabelConfiguration().getLabelTexts().length;

		// Extract color settings.
		ColorConfiguration colorConfig = this.config.getColorConfiguration();
		GradientColor[] gradientColors = colorConfig.getDataGradientColors();
		boolean gradientEnabled = colorConfig.isDataGradientColorEnabled();
		if (gradientColors.length != 1) {
			throw new IllegalStateException("This version does not support multiple gradient colors yet.");
		}
		GradientColor gradientColor = gradientColors[0];

		// Extract gradient filter settings.
		boolean gradientFilterEnabled = gradientColor.getSeriesFilterMode() != SeriesFilterMode.NONE;
		boolean isIndexGradientFilter = gradientColor.getSeriesFilterMode() == SeriesFilterMode.INDEX;
		if (gradientFilterEnabled && !isIndexGradientFilter) {
			throw new IllegalStateException("Only index-based series filter is supported in this version.");
		}
		IndexSeriesFilter gradientSeriesFilter = gradientFilterEnabled ? gradientColor.getIndexSeriesFilter() : null;

		// Check whether each data series is drawn by the gradient color, and store the result into the following array.
		boolean[] gradientTargetFlags = new boolean[legendCount];
		for (int iseries=0; iseries<legendCount; iseries++) {
			boolean isGradientTargetSeries = false;

			// If the gradient is enabled, all the data series are drawn by the gradient color by default.
			if (gradientEnabled) {
				isGradientTargetSeries = true;

				// If the gradient filter is defined and the series index is not included,
				// the series is not a target of the gradient.
				if (gradientFilterEnabled) {
					SeriesAttribute seriesAttribute = new SeriesAttribute();
					seriesAttribute.setGlobalSeriesIndex(iseries);
					if (!gradientSeriesFilter.isSeriesIncluded(seriesAttribute)) {
						isGradientTargetSeries = false;
					}
				}
			}
			gradientTargetFlags[iseries] = isGradientTargetSeries;
		}

		return gradientTargetFlags;
	}


	/**
	 * Draws legend texts.
	 *
	 * @param graphics The Graphics2D object to draw contents on the graph screen image.
	 * @param legendTextAreaPosition The array storing x (at [0]) and y (at [1]) of the left-top point of the legend text area (not including markers).
	 * @param legendTextLineHeight The line-height of the legend texts.
	 * @param filteredResultFlags The container storing the enabled/disabled states of plot options for each data series after filtering.
	 */
	private void drawLegendTexts(Graphics2D graphics, int[] legendTextAreaPosition, int legendTextLineHeight,
			FilteredResultFlags filteredResultFlags) {

		// Extract legend settings.
		LegendLabelConfiguration legendConfig = this.config.getLabelConfiguration().getLegendLabelConfiguration();
		String[] legendTexts = legendConfig.getLabelTexts();
		int legendCount = legendTexts.length;
		Font legendFont = this.config.getFontConfiguration().getLegendLabelFont();

		// Extract color settings.
		Color foregroundColor = this.config.getColorConfiguration().getForegroundColor();

		graphics.setColor(foregroundColor);
		graphics.setFont(legendFont);

		// Draw legend texts.
		int legendDrawableSeriesCount = Math.min(filteredResultFlags.seriesCount, legendCount);
		int drawnLegendTextCount = 0;
		for (int iseries=0; iseries<legendDrawableSeriesCount; iseries++) {
			if (!filteredResultFlags.legendTextExistFlags[iseries]) {
				continue;
			}
			String legendText = legendTexts[iseries];
			int textX = legendTextAreaPosition[0];
			int textY = legendTextAreaPosition[1] + (iseries * legendTextLineHeight);
			if (legendConfig.isEmptyLegendExclusionEnabled()) {
				textY = legendTextAreaPosition[1] + (drawnLegendTextCount * legendTextLineHeight);
			}
			graphics.drawString(legendText, textX, textY);
			drawnLegendTextCount++;
		}
	}


	/**
	 * Draws point-marker type icons, at the left-side of legend texts.
	 *
	 * @param graphics The Graphics2D object to draw contents on the graph screen image.
	 * @param legendTextAreaPosition The array storing x (at [0]) and y (at [1]) of the left-top point of the legend text area (not including markers).
	 * @param legendTextLineHeight The line-height of the legend texts.
	 * @param markerOffsetXWithLine
	 * @param markerOffsetXWithoutLine
	 * @param filteredResultFlags The container storing the enabled/disabled states of plot options for each data series after filtering.
	 * @param gradientTargetFlags The flag array representing whether each data series is drawn by gradient colors.
	 * @param useForegroundColorInsteadOfGradient The flag to draw markers by the foreground color, instead of gradient colors.
	 */
	private void drawPointMarkerIcons(Graphics2D graphics,
			int[] legendTextAreaPosition, int legendTextLineHeight, int markerOffsetXWithLine, int markerOffsetXWithoutLine,
			FilteredResultFlags filteredResultFlags, boolean[] gradientTargetFlags, boolean useForegroundColorInsteadOfGradient) {

		LegendLabelConfiguration legendConfig = this.config.getLabelConfiguration().getLegendLabelConfiguration();
		int legendCount = legendConfig.getLabelTexts().length;
		ColorMixer colorMixer = new ColorMixer();

		// Extract point/marker settings.
		PlotterConfiguration plotterConfig = this.config.getPlotterConfiguration();
		PointPlotterConfiguration pointPlotterConfig = plotterConfig.getPointPlotterConfiguration();
		boolean isMarkerEnabled = pointPlotterConfig.getPointStyleMode() == PointStyleMode.MARKER;
		String[] markerTexts = pointPlotterConfig.getMarkerTexts();

		// Get font information.
		Font markerFont = this.config.getFontConfiguration().getPointMarkerFont();
		Font textFont = this.config.getFontConfiguration().getLegendLabelFont();
		int legendFontSize = this.config.getFontConfiguration().getLegendLabelFont().getSize();
		markerFont = new Font(markerFont.getName(), markerFont.getStyle(), legendFontSize); // Resizing to adjust marker heights to text heights.
		FontMetrics markerFontMetrics = graphics.getFontMetrics(markerFont);
		FontMetrics textFontMetrics = graphics.getFontMetrics(textFont);
		int textFontHeight = textFontMetrics.getAscent();

		// Extract color settings.
		ColorConfiguration colorConfig = this.config.getColorConfiguration();
		Color foregroundColor = colorConfig.getForegroundColor();
		Color[] solidColors = colorConfig.getDataSolidColors();

		GradientColor[] gradientColors = colorConfig.getDataGradientColors();
		if (gradientColors.length != 1) {
			throw new IllegalStateException("This version does not support multiple gradient colors yet.");
		}
		GradientColor gradientColor = gradientColors[0];
		AxisGradientColor[] axisGradientColors = gradientColor.getAxisGradientColors();
		if (axisGradientColors.length != 1) {
			throw new IllegalStateException("This version does not support multiple axis gradient colors yet.");
		}
		AxisGradientColor axisGradientColor = axisGradientColors[0];

		// Draw markers.
		int legendDrawableSeriesCount = Math.min(filteredResultFlags.seriesCount, legendCount);
		int drawnLegendTextCount = 0;
		for (int iseries=0; iseries<legendDrawableSeriesCount; iseries++) {

			// Determine whether we should draw a line for this series.
			boolean pointPlotterFlags = filteredResultFlags.pointPlotterTargetFlags[iseries];
			boolean tileGroupPlotterFlags = filteredResultFlags.tileGroupPlotterTargetFlags[iseries];
			boolean shouldDrawThisSeries = pointPlotterFlags && !tileGroupPlotterFlags;
			if (!shouldDrawThisSeries) {
				continue;
			}

			int markerOffsetX = filteredResultFlags.lineGroupPlotterTargetFlags[iseries] ? markerOffsetXWithLine : markerOffsetXWithoutLine;

			// If this data series is drawn by gradient colors.
			if (gradientTargetFlags[iseries]) {
				if (useForegroundColorInsteadOfGradient) {
					graphics.setColor(foregroundColor);
				} else {
					boolean isCoordNormalized = true;
					boolean isLogScaleEnabled = false;
					Color gradientCenterColor = colorMixer.generateColorFromAxisGradientColor(
							new BigDecimal(0.5), axisGradientColor, isCoordNormalized, isLogScaleEnabled
					);
					graphics.setColor(gradientCenterColor);
				}

			// If this data series is drawn by a solid color.
			} else {
				int solidColorIndex = iseries % solidColors.length;
				graphics.setColor(solidColors[solidColorIndex]);
			}

			// Calculate the position of the marker.
			int markerX = legendTextAreaPosition[0] + markerOffsetX;
			int markerY = legendTextAreaPosition[1] + (iseries * legendTextLineHeight);
			if (legendConfig.isEmptyLegendExclusionEnabled()) {
				markerY = legendTextAreaPosition[1] + (drawnLegendTextCount * legendTextLineHeight);
			}

			// Draw a marker.
			if (isMarkerEnabled) {
				int markerWidth = markerFontMetrics.stringWidth(markerTexts[iseries]);
				markerX -= markerWidth;
				graphics.setFont(markerFont);
				graphics.drawString(markerTexts[iseries], markerX, markerY);

			// Draw a point (filled circle).
			} else {
				int pointSize = 10;
				markerX -= pointSize;
				markerY -= (textFontHeight * 0.6);
				graphics.fillOval(markerX, markerY, pointSize, pointSize);
			}

			// Count-up the drawn legend text.
			if (filteredResultFlags.legendTextExistFlags[iseries]) {
				drawnLegendTextCount++;
			}
		}
	}


	/**
	 * Draws line-type icons, at the left-side of legend texts.
	 *
	 * @param graphics The Graphics2D object to draw contents on the graph screen image.
	 * @param legendTextAreaPosition The array storing x (at [0]) and y (at [1]) of the left-top point of the legend text area (not including markers).
	 * @param legendTextLineHeight The line-height of the legend texts.
	 * @param filteredResultFlags The container storing the enabled/disabled states of plot options for each data series after filtering.
	 * @param gradientTargetFlags The flag array representing whether each data series is drawn by gradient colors.
	 */
	private void drawLineIcons(Graphics2D graphics,
			int[] legendTextAreaPosition, int legendTextLineHeight, int lineOffsetX,
			FilteredResultFlags filteredResultFlags, boolean[] gradientTargetFlags) {

		LegendLabelConfiguration legendConfig = this.config.getLabelConfiguration().getLegendLabelConfiguration();
		int legendCount = legendConfig.getLabelTexts().length;
		ColorMixer colorMixer = new ColorMixer();

		// Get font information.
		Font markerFont = this.config.getFontConfiguration().getPointMarkerFont();
		Font textFont = this.config.getFontConfiguration().getLegendLabelFont();
		int legendFontSize = this.config.getFontConfiguration().getLegendLabelFont().getSize();
		markerFont = new Font(markerFont.getName(), markerFont.getStyle(), legendFontSize); // Resizing to adjust marker heights to text heights.
		FontMetrics textFontMetrics = graphics.getFontMetrics(textFont);
		int textFontHeight = textFontMetrics.getAscent();

		// Extract color settings.
		ColorConfiguration colorConfig = this.config.getColorConfiguration();
		Color[] solidColors = colorConfig.getDataSolidColors();

		GradientColor[] gradientColors = colorConfig.getDataGradientColors();
		if (gradientColors.length != 1) {
			throw new IllegalStateException("This version does not support multiple gradient colors yet.");
		}
		GradientColor gradientColor = gradientColors[0];
		AxisGradientColor[] axisGradientColors = gradientColor.getAxisGradientColors();
		if (axisGradientColors.length != 1) {
			throw new IllegalStateException("This version does not support multiple axis gradient colors yet.");
		}
		AxisGradientColor axisGradientColor = axisGradientColors[0];

		// Draw surface tiles.
		int legendDrawableSeriesCount = Math.min(filteredResultFlags.seriesCount, legendCount);
		int drawnLegendTextCount = 0;
		for (int iseries=0; iseries<legendDrawableSeriesCount; iseries++) {

			// Determine whether we should draw a line for this series.
			boolean lineGroupPlotterFlags = filteredResultFlags.lineGroupPlotterTargetFlags[iseries];
			boolean tileGroupPlotterFlags = filteredResultFlags.tileGroupPlotterTargetFlags[iseries];
			boolean shouldDrawThisSeries = lineGroupPlotterFlags && !tileGroupPlotterFlags;
			if (!shouldDrawThisSeries) {
				continue;
			}

			// Calculate the position of the marker.
			int lineIconWidth = 40;
			int lineIconHeight = 2;
			int lineX = legendTextAreaPosition[0] + lineOffsetX - lineIconWidth;
			int lineY = legendTextAreaPosition[1] + (iseries * legendTextLineHeight) - (int)(textFontHeight * 0.4);
			if (legendConfig.isEmptyLegendExclusionEnabled()) {
				lineY = legendTextAreaPosition[1] + (drawnLegendTextCount * legendTextLineHeight) - (int)(textFontHeight * 0.4);
			}

			// Draw the line by the gradient color.
			if (gradientTargetFlags[iseries]) {
				for (int ix=0; ix<lineIconWidth; ix++) {
					double representCoord = (double)ix / (double)lineIconWidth;
					boolean isCoordNormalized = true;
					boolean isLogScaleEnabled = false;
					Color lineColor = colorMixer.generateColorFromAxisGradientColor(
							new BigDecimal(representCoord), axisGradientColor, isCoordNormalized, isLogScaleEnabled
					);
					graphics.setColor(lineColor);
					graphics.drawLine(lineX + ix, lineY, lineX + ix, lineY + lineIconHeight - 1);
				}

			// Draw the line by a solid color.
			} else {
				int solidColorIndex = iseries % solidColors.length;
				graphics.setColor(solidColors[solidColorIndex]);
				for (int iline=0; iline<lineIconHeight; iline++) {
					graphics.drawLine(lineX, lineY + iline, lineX + lineIconWidth, lineY + iline);
				}
			}

			// Count-up the drawn legend text.
			if (filteredResultFlags.legendTextExistFlags[iseries]) {
				drawnLegendTextCount++;
			}
		}
	}


	/**
	 * Draws tile-type icons, at the left-side of legend texts.
	 *
	 * @param graphics The Graphics2D object to draw contents on the graph screen image.
	 * @param legendTextAreaPosition The array storing x (at [0]) and y (at [1]) of the left-top point of the legend text area (not including markers).
	 * @param legendTextLineHeight The line-height of the legend texts.
	 * @param filteredResultFlags The container storing the enabled/disabled states of plot options for each data series after filtering.
	 * @param gradientTargetFlags The flag array representing whether each data series is drawn by gradient colors.
	 */
	private void drawTileIcons(Graphics2D graphics,
			int[] legendTextAreaPosition, int legendTextLineHeight, int tileOffsetX,
			FilteredResultFlags filteredResultFlags, boolean[] gradientTargetFlags) {

		LegendLabelConfiguration legendConfig = this.config.getLabelConfiguration().getLegendLabelConfiguration();
		int legendCount = legendConfig.getLabelTexts().length;
		ColorMixer colorMixer = new ColorMixer();

		// Get font information.
		Font markerFont = this.config.getFontConfiguration().getPointMarkerFont();
		Font textFont = this.config.getFontConfiguration().getLegendLabelFont();
		int legendFontSize = this.config.getFontConfiguration().getLegendLabelFont().getSize();
		markerFont = new Font(markerFont.getName(), markerFont.getStyle(), legendFontSize); // Resizing to adjust marker heights to text heights.
		FontMetrics textFontMetrics = graphics.getFontMetrics(textFont);
		int textFontHeight = textFontMetrics.getAscent();

		// Extract color settings.
		ColorConfiguration colorConfig = this.config.getColorConfiguration();
		Color foregroundColor = colorConfig.getForegroundColor();
		Color[] solidColors = colorConfig.getDataSolidColors();

		GradientColor[] gradientColors = colorConfig.getDataGradientColors();
		if (gradientColors.length != 1) {
			throw new IllegalStateException("This version does not support multiple gradient colors yet.");
		}
		GradientColor gradientColor = gradientColors[0];
		AxisGradientColor[] axisGradientColors = gradientColor.getAxisGradientColors();
		if (axisGradientColors.length != 1) {
			throw new IllegalStateException("This version does not support multiple axis gradient colors yet.");
		}
		AxisGradientColor axisGradientColor = axisGradientColors[0];

		// Draw surface tiles.
		int legendDrawableSeriesCount = Math.min(filteredResultFlags.seriesCount, legendCount);
		int drawnLegendTextCount = 0;
		for (int iseries=0; iseries<legendDrawableSeriesCount; iseries++) {

			// Determine whether we should draw a line for this series.
			boolean tileGroupPlotterFlags = filteredResultFlags.tileGroupPlotterTargetFlags[iseries];
			boolean shouldDrawThisSeries = tileGroupPlotterFlags;
			if (!shouldDrawThisSeries) {
				continue;
			}

			// Calculate the position of the marker.
			int tileWidth = (int)(textFontHeight * 0.8);
			int tileHeight = (int)(textFontHeight * 0.8);
			int tileX = legendTextAreaPosition[0] + tileOffsetX - tileWidth;
			int tileY = legendTextAreaPosition[1] + (iseries * legendTextLineHeight) - tileHeight;
			if (legendConfig.isEmptyLegendExclusionEnabled()) {
				tileY = legendTextAreaPosition[1] + (drawnLegendTextCount * legendTextLineHeight) - tileHeight;
			}

			// Draw the tile by the gradient color.
			if (gradientTargetFlags[iseries]) {
				graphics.setColor(foregroundColor);
				graphics.drawRect(tileX, tileY, tileWidth, tileHeight);

				for (int iline=1; iline<=tileHeight-1; iline++) {
					int lineY = tileY + iline;
					double representCoord = (double)(iline - 1) / (double)(tileHeight - 2);
					boolean isCoordNormalized = true;
					boolean isLogScaleEnabled = false;
					Color lineColor = colorMixer.generateColorFromAxisGradientColor(
							new BigDecimal(representCoord), axisGradientColor, isCoordNormalized, isLogScaleEnabled
					);
					graphics.setColor(lineColor);
					graphics.drawLine(tileX + 1, lineY, tileX + tileWidth - 1, lineY);
				}

			// Draw the tile by a solid color.
			} else {
				int solidColorIndex = iseries % solidColors.length;
				graphics.setColor(solidColors[solidColorIndex]);
				graphics.fillRect(tileX, tileY, tileWidth, tileHeight);
			}

			// Count-up the drawn legend text.
			if (filteredResultFlags.legendTextExistFlags[iseries]) {
				drawnLegendTextCount++;
			}
		}
	}

}
