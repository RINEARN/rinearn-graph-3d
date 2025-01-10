package com.rinearn.graph3d.renderer.simple;

import com.rinearn.graph3d.config.ColorConfiguration;
import com.rinearn.graph3d.config.RangeConfiguration;
import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.color.GradientColor;
import com.rinearn.graph3d.renderer.RinearnGraph3DDrawingParameter;

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
	 * @param colorMixer The color mixer, which is the object to convert coordinates to gradient colors.
	 */
	public void draw(Graphics2D graphics, ColorMixer colorMixer) {

		// Determine the range of the color bar from RangeConfiguration.
		RangeConfiguration rangeConfig = this.config.getRangeConfiguration();
		RangeConfiguration.AxisRangeConfiguration colorBarRangeConfig = rangeConfig.getZRangeConfiguration(); // Temporary
		BigDecimal min = colorBarRangeConfig.getMinimum();
		BigDecimal max = colorBarRangeConfig.getMaximum();

		// Extract the gradient colors defined in ColorConfiguration.
		ColorConfiguration colorConfig = this.config.getColorConfiguration();
		GradientColor[] gradientColors = colorConfig.getDataGradientColors();
		if (gradientColors.length == 0) {
			throw new IllegalStateException("No gradient color is defined in ColorConfiguration. At least one gradient color is required for gradient coloring mode.");
		}

		// As the temporary specification, draw the color bar for only the first gradient color,
		// until we support multiple gradient colors in Ver.6.x (probably we can not support it on 6.0 yet).
		// Note that, the specification of ColorConfiguration is defined with consideration for
		// keeping compatibility with future versions, so it is redundant for the currently developing version.
		// Some features defined in ColorConfiguration are not available in the current version.
		GradientColor gradientColor = gradientColors[0];

		// For same reason as the above, extract the first AxisGradientColor from the ColorGradient instance.
		// ColorGradient supports multiple-axes gradients with considering future compatibility, but probably we can not support it on 6.0 yet.
		GradientColor.AxisGradientColor[] axisGradientColors = gradientColor.getAxisGradientColors();
		if (axisGradientColors.length == 0) {
			throw new IllegalStateException("No AxisGradientColor is defined in GradientColor instance. At least one axis is required for using gradient coloring mode.");
		}
		GradientColor.AxisGradientColor axisGradientColor = axisGradientColors[0];

		/** Convert the dimension of the gradient's axis to the index in a coordinate array. */
		int gradientAxisIndex = -1;
		switch (axisGradientColor.getAxis()) {
			case X :
				gradientAxisIndex = 0;
				break;
			case Y :
				gradientAxisIndex = 1;
				break;
			case Z :
				gradientAxisIndex = 2;
				break;
			case COLUMN_4 :
				gradientAxisIndex = 3;
				break;
			default:
				throw new IllegalStateException("Unexpected gradient axis: " + axisGradientColor.getAxis());
		}

		// Draw the color bar by drawing horizontal lines repeatedly, from the top to the bottom.
		for (int iline=0; iline<this.colorBarHeight; iline++) {

			// Convert the position of the line (iline) in the color bar to the corresponding 3D coordinate in the graph space.
			double gradientRatio = (double)(this.colorBarHeight - iline) / (double)this.colorBarHeight;
			double gradientAxisCoord = min.doubleValue() + (max.doubleValue() - min.doubleValue()) * gradientRatio;
			double[] coordsIn3D = new double[3];
			coordsIn3D[gradientAxisIndex] = gradientAxisCoord;

			// Generate the color of the line.
			RinearnGraph3DDrawingParameter drawingParam = new RinearnGraph3DDrawingParameter();
			drawingParam.setAutoColoringEnabled(true);
			Color lineColor = colorMixer.generateColor(coordsIn3D, drawingParam, colorConfig);

			// Draw the line
			graphics.setColor(lineColor);
			graphics.drawLine(this.colorBarX, this.colorBarY + iline, this.colorBarX + this.colorBarWidth, this.colorBarY + iline);
		}
	}
}
