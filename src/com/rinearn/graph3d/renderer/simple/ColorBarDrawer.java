package com.rinearn.graph3d.renderer.simple;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;

import java.awt.Graphics2D;
import java.awt.Color;
import java.math.BigDecimal;


/**
 * The class to draw the color bar, over the 3D-rendered graph screen.
 */
public class ColorBarDrawer {

	/** The default value of the X-coordinate (px) of the left-top edge of the color bar. */
	public final int DEFAULT_COLOR_BAR_X = 20;

	/** The default value of the Y-coordinate (px) of the left-top edge of the color bar. */
	public final int DEFAULT_COLOR_BAR_Y = 30;

	/** The default value of the width (px) of the left-top edge of the color bar. */
	public final int DEFAULT_COLOR_BAR_WIDTH = 22;

	/** The default value of the height (px) of the left-top edge of the color bar. */
	public final int DEFAULT_COLOR_BAR_HEIGHT = 300;

	/** The X-coordinate (px) of the left-top edge of the color bar. */
	private volatile int colorBarX = DEFAULT_COLOR_BAR_X;

	/** The Y-coordinate (px) of the left-top edge of the color bar. */
	private volatile int colorBarY = DEFAULT_COLOR_BAR_Y;

	/** The width (px) of the left-top edge of the color bar. */
	private volatile int colorBarWidth = DEFAULT_COLOR_BAR_WIDTH;

	/** The height (px) of the left-top edge of the color bar. */
	private volatile int colorBarHeight = DEFAULT_COLOR_BAR_HEIGHT;

	/** The coordinates (locations) of the ticks. */
	private volatile BigDecimal[] tickCoordinates;

	/** The labels of the ticks. */
	private volatile String[] tickLabels;

	/** Stores the configuration of this application. */
	private volatile RinearnGraph3DConfiguration config;


	/**
	 * Creates a new instance for drawing graph frames under the specified configurations.
	 */
	public ColorBarDrawer() {
	}


	/**
	 * Sets the configuration.
	 *
	 * @param config The configuration.
	 */
	public synchronized void setConfiguration(RinearnGraph3DConfiguration configuration) {
		if (!configuration.hasRangeConfiguration()) {
			throw new IllegalArgumentException("The range configuration is stored in the specified configuration.");
		}
		if (!configuration.hasColorConfiguration()) {
			throw new IllegalArgumentException("No color configuration is stored in the specified configuration.");
		}
		this.config = configuration;
	}


	/**
	 * Sets the coordinates (locations) of the ticks.
	 *
	 * @param tickCoordinates The coordinates of the ticks.
	 */
	public synchronized void setTickCoordinates(BigDecimal[] tickCoordinates) {
		this.tickCoordinates = tickCoordinates;
	}

	/**
	 * Sets the labels of the ticks.
	 *
	 * @param tickLabels The labels of the ticks.
	 */
	public synchronized void setTickLabelTexts(String[] tickLabels) {
		this.tickLabels = tickLabels;
	}


	/**
	 * Draws the color bar on the graph screen image.
	 *
	 * @param graphics The Graphics2D object to draw contents on the graph screen image.
	 */
	public void draw(Graphics2D graphics) {
		graphics.setColor(Color.CYAN);
		graphics.fillRect(this.colorBarX, this.colorBarY, this.colorBarWidth, this.colorBarHeight);
	}
}
