package com.rinearn.graph3d.renderer.simple;

import com.rinearn.graph3d.config.CameraConfiguration;
import com.rinearn.graph3d.config.ColorConfiguration;
import com.rinearn.graph3d.config.OptionConfiguration;
import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.color.AxisGradientColor;
import com.rinearn.graph3d.config.color.GradientColor;
import com.rinearn.graph3d.config.data.SeriesFilterMode;
import com.rinearn.graph3d.config.data.IndexSeriesFilter;
import com.rinearn.graph3d.config.data.SeriesAttribute;

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
	 * Draws the color bar on the graph screen image.
	 *
	 * @param graphics The Graphics2D object to draw contents on the graph screen image.
	 */
	public void draw(Graphics2D graphics) {
		OptionConfiguration optionConfig = this.config.getOptionConfiguration();
		boolean pointOptionEnabled = optionConfig.getPointOptionConfiguration().isOptionEnabled();
		boolean lineOptionEnabled = optionConfig.getLineOptionConfiguration().isOptionEnabled();
		boolean meshOptionEnabled = optionConfig.getMeshOptionConfiguration().isOptionEnabled();
		boolean surfaceOptionEnabled = optionConfig.getSurfaceOptionConfiguration().isOptionEnabled();
		boolean contourOptionEnabled = false; // Temporary.

		boolean lineGroupOptionEnabled = lineOptionEnabled || contourOptionEnabled;
		boolean tileGroupOptionEnabled = surfaceOptionEnabled || meshOptionEnabled;

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

		// Draw point marker icons.
		if (pointOptionEnabled && !tileGroupOptionEnabled) {
			int markerOffsetX = lineGroupOptionEnabled ? -20 : -6;
			this.drawPointMarkerIcons(graphics, legendTextAreaPosition, legendTextLineHeight, markerOffsetX, gradientTargetFlags);
		}

		// Draw tiles icons.
		if (tileGroupOptionEnabled) {
			int tileOffsetX = -8;
			this.drawTileIcons(graphics, legendTextAreaPosition, legendTextLineHeight, tileOffsetX, gradientTargetFlags);
		}
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
	 * Draws point-marker type icons, at the left-side of legend texts.
	 *
	 * @param graphics The Graphics2D object to draw contents on the graph screen image.
	 * @param legendTextAreaPosition The array storing x (at [0]) and y (at [1]) of the left-top point of the legend text area (not including markers).
	 * @param legendTextLineHeight The line-height of the legend texts.
	 * @param tileOffsetX The horizontal offset amount of marker's positions.
	 * @param gradientTargetFlags The flag array representing whether each data series is drawn by gradient colors.
	 */
	private void drawPointMarkerIcons(Graphics2D graphics,
			int[] legendTextAreaPosition, int legendTextLineHeight, int markerOffsetX,
			boolean[] gradientTargetFlags) {

		int legendCount = this.config.getLabelConfiguration().getLegendLabelConfiguration().getLabelTexts().length;

		// Extract point/marker settings.
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

			System.out.println("TODO: 後でここ系列フィルター居る: " + this);
			boolean isSeriesIncluded = true;
			if (!isSeriesIncluded) {
				continue;
			}

			// If this data series is drawn by gradient colors.
			if (gradientTargetFlags[iseries]) {
				graphics.setColor(foregroundColor);

			// If this data series is drawn by a solid color.
			} else {
				int solidColorIndex = iseries % solidColors.length;
				graphics.setColor(solidColors[solidColorIndex]);
			}

			// Calculate the position of the marker.
			int markerX = legendTextAreaPosition[0] + markerOffsetX;
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


	/**
	 * Draws tile-type icons, at the left-side of legend texts.
	 *
	 * @param graphics The Graphics2D object to draw contents on the graph screen image.
	 * @param legendTextAreaPosition The array storing x (at [0]) and y (at [1]) of the left-top point of the legend text area (not including markers).
	 * @param legendTextLineHeight The line-height of the legend texts.
	 * @param tileOffsetX The horizontal offset amount of tile's positions.
	 * @param gradientTargetFlags The flag array representing whether each data series is drawn by gradient colors.
	 */
	private void drawTileIcons(Graphics2D graphics,
			int[] legendTextAreaPosition, int legendTextLineHeight, int tileOffsetX,
			boolean[] gradientTargetFlags) {

		int legendCount = this.config.getLabelConfiguration().getLegendLabelConfiguration().getLabelTexts().length;
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
		for (int iseries=0; iseries<legendCount; iseries++) {

			System.out.println("TODO: 後でここ系列フィルター居る: " + this);
			boolean isSeriesIncluded = true;
			if (!isSeriesIncluded) {
				continue;
			}

			// Calculate the position of the marker.
			int tileWidth = (int)(textFontHeight * 0.8);
			int tileHeight = (int)(textFontHeight * 0.8);
			int tileX = legendTextAreaPosition[0] + tileOffsetX - tileWidth;
			int tileY = legendTextAreaPosition[1] + (iseries * legendTextLineHeight) - tileHeight;

			// Draw the tile by the gradient color.
			if (gradientTargetFlags[iseries]) {
				graphics.setColor(foregroundColor);
				graphics.drawRect(tileX, tileY, tileWidth, tileHeight);

				for (int iline=1; iline<=tileHeight-1; iline++) {
					int lineY = tileY + iline;
					double representCoord = (double)(iline - 1) / (double)(tileHeight - 2);
					Color lineColor = colorMixer.generateColorFromAxisGradientColor(new BigDecimal(representCoord), axisGradientColor);
					graphics.setColor(lineColor);
					graphics.drawLine(tileX + 1, lineY, tileX + tileWidth - 1, lineY);
				}

			// Draw the tile by a solid color.
			} else {
				int solidColorIndex = iseries % solidColors.length;
				graphics.setColor(solidColors[solidColorIndex]);
				graphics.fillRect(tileX, tileY, tileWidth, tileHeight);
			}
		}
	}

}
