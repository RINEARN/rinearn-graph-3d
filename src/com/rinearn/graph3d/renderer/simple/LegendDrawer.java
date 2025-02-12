package com.rinearn.graph3d.renderer.simple;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import com.rinearn.graph3d.config.CameraConfiguration;
import com.rinearn.graph3d.config.ColorConfiguration;
import com.rinearn.graph3d.config.FontConfiguration;
import com.rinearn.graph3d.config.LabelConfiguration;
import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.color.GradientColor;

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
		int legendOffsetY = -30;

		// Calculate the position of legends.
		int[] legendTextAreaPosition = this.calculateLegendTextAraaPosition(graphics, screenWidth, screenHeight, legendOffsetX, legendOffsetY);
		int legendTextLineHeight = this.calculateLegendTextLineHeight(graphics);

		// Draw legend texts.
		this.drawLegendTexts(graphics, legendTextAreaPosition, legendTextLineHeight);
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
		Color forgroundColor = this.config.getColorConfiguration().getForegroundColor();

		graphics.setColor(forgroundColor);
		graphics.setFont(legendFont);

		// Draw legend texts.
		for (int ilegend=0; ilegend<legendTexts.length; ilegend++) {
			String legendText = legendTexts[ilegend];
			int textX = legendTextAreaPosition[0];
			int textY = legendTextAreaPosition[1] + (ilegend * legendTextLineHeight);
			graphics.drawString(legendText, textX, textY);
		}
	}
}
