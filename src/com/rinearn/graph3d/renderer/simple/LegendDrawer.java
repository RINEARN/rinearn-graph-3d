package com.rinearn.graph3d.renderer.simple;

import com.rinearn.graph3d.config.CameraConfiguration;
import com.rinearn.graph3d.config.ColorConfiguration;
import com.rinearn.graph3d.config.OptionConfiguration;
import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.color.GradientColor;
import com.rinearn.graph3d.config.data.SeriesFilterMode;
import com.rinearn.graph3d.config.data.IndexSeriesFilter;
import com.rinearn.graph3d.config.data.SeriesAttribute;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

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

	また要検討。
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
	 * Draws the color bar on the graph screen image.
	 *
	 * @param graphics The Graphics2D object to draw contents on the graph screen image.
	 */
	public void draw(Graphics2D graphics) {

		// Extract/define position parameters.
		CameraConfiguration cameraConfig = this.config.getCameraConfiguration();
		int screenWidth = cameraConfig.getScreenWidth();
		int screenHeight = cameraConfig.getScreenHeight();
		int legendOffsetX = 30;
		int legendOffsetY = 30;

		// Calculate the position of legends.
		int[] legendTextAreaPosition = this.calculateLegendTextAraaPosition(graphics, screenWidth, screenHeight, legendOffsetX, legendOffsetY);
		int legendTextLineHeight = this.calculateLegendTextLineHeight(graphics);

		// Calculate whether each data series is drawn by gradient colors, and store the results into an array.
		boolean[] gradientTargetFlags = this.calculateGradientTargetFlags();

		// Draw legend texts.
		this.drawLegendTexts(graphics, legendTextAreaPosition, legendTextLineHeight);

		// Draw point markers.
		this.drawLegendPointMarkers(graphics, legendTextAreaPosition, legendTextLineHeight, gradientTargetFlags);
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
		boolean isIndexGradientFilter = gradientColor.getSeriesFilterMode() != SeriesFilterMode.INDEX;
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
	 */
	private void drawLegendTexts(Graphics2D graphics, int[] legendTextAreaPosition, int legendTextLineHeight) {

		// Extract legend settings.
		String[] legendTexts = this.config.getLabelConfiguration().getLegendLabelConfiguration().getLabelTexts();
		Font legendFont = this.config.getFontConfiguration().getLegendLabelFont();

		// Extract color settings.
		Color foregroundColor = this.config.getColorConfiguration().getForegroundColor();

		graphics.setColor(foregroundColor);
		graphics.setFont(legendFont);

		// Draw legend texts.
		for (int iseries=0; iseries<legendTexts.length; iseries++) {
			String legendText = legendTexts[iseries];
			int textX = legendTextAreaPosition[0];
			int textY = legendTextAreaPosition[1] + (iseries * legendTextLineHeight);
			graphics.drawString(legendText, textX, textY);
		}
	}


	/**
	 * Draws legend markers.
	 *
	 * @param graphics The Graphics2D object to draw contents on the graph screen image.
	 * @param legendTextAreaPosition The array storing x (at [0]) and y (at [1]) of the left-top point of the legend text area (not including markers).
	 * @param legendTextLineHeight The line-height of the legend texts.
	 * @param gradientTargetFlags The flag array representing whether each data series is drawn by gradient colors.
	 */
	private void drawLegendPointMarkers(Graphics2D graphics, int[] legendTextAreaPosition, int legendTextLineHeight,
			boolean[] gradientTargetFlags) {

		int legendCount = this.config.getLabelConfiguration().getLegendLabelConfiguration().getLabelTexts().length;

		// Extract marger settings.
		OptionConfiguration optionConfig = this.config.getOptionConfiguration();
		OptionConfiguration.PointOptionConfiguration pointOptionConfig = optionConfig.getPointOptionConfiguration();
		boolean isMarkerEnabled = pointOptionConfig.getPointStyleMode() == OptionConfiguration.PointStyleMode.MARKER;
		String[] markerTexts = pointOptionConfig.getMarkerTexts();

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

		// Draw markers.
		for (int iseries=0; iseries<legendCount; iseries++) {

			// If this data series is drawn by gradient colors.
			if (gradientTargetFlags[iseries]) {
				graphics.setColor(foregroundColor);

			// If this data series is drawn by a solid color.
			} else {
				int solidColorIndex = iseries % solidColors.length;
				graphics.setColor(solidColors[solidColorIndex]);
			}

			// Calculate the position of the marker.
			int markerX = legendTextAreaPosition[0] - 15;
			int markerY = legendTextAreaPosition[1] + (iseries * legendTextLineHeight);

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
		}
	}
}
