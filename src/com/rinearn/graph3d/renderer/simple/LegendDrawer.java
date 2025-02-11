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
	 * @param colorMixer The color mixer, which is the object to convert coordinates to gradient colors.
	 */
	public void draw(Graphics2D graphics) {
		FontConfiguration fontConfig = this.config.getFontConfiguration();
		ColorConfiguration colorConfig = this.config.getColorConfiguration();
		CameraConfiguration cameraConfig = this.config.getCameraConfiguration();
		LabelConfiguration labelConfig = this.config.getLabelConfiguration();
		LabelConfiguration.LegendLabelConfiguration legendConfig = labelConfig.getLegendLabelConfiguration();
		String[] legendTexts = legendConfig.getLabelTexts();

		// Set the color and font to render legends.
		Color forgroundColor = colorConfig.getForegroundColor();
		Font legendFont = fontConfig.getLegendLabelFont();
		graphics.setColor(forgroundColor);
		graphics.setFont(legendFont);

		// Get font information.
		FontMetrics fontMetrics = graphics.getFontMetrics(legendFont);
		int fontHeight = fontMetrics.getAscent();

		// Detect the maximum length (pixels) of the legend text on the display.
		int maxTextWidth = 0;
		for (String legendText: legendTexts) {
			int textWidth = fontMetrics.stringWidth(legendText);
			if (maxTextWidth < textWidth) {
				maxTextWidth = textWidth;
			}
		}

		// Draw legends.
		int screenWidth = cameraConfig.getScreenWidth();
		for (int ilegend=0; ilegend<legendTexts.length; ilegend++) {
			String legendText = legendTexts[ilegend];
			int textY = (int)(ilegend * fontHeight * 1.35) + 30;
			int textX = screenWidth - maxTextWidth - 30;
			graphics.drawString(legendText, textX, textY);
		}
	}
}
