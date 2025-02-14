package com.rinearn.graph3d.renderer.simple;

import java.math.BigDecimal;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.color.AxisGradientColor;
import com.rinearn.graph3d.config.color.ColorConfiguration;
import com.rinearn.graph3d.config.color.GradientColor;
import com.rinearn.graph3d.config.scale.ScaleConfiguration;
import com.rinearn.graph3d.config.range.RangeConfiguration;
import com.rinearn.graph3d.config.range.AxisRangeConfiguration;

/**
 * The class to generate scale ticks from configuration,
 * used in FrameDrawer, AxisLabelDrawer, ScaleTickDrawer, ColorBarDrawer, ColorBarDrawer, etc.
 */
public final class ScaleTickGenerator {

	/**
	 * The container storing the result of generateTicks() method.
	 */
	public static class Result {

		/** The coordinates of the ticks on X axis. */
		public volatile BigDecimal[] xTickCoordinates;

		/** The coordinates of the ticks on Y axis. */
		public volatile BigDecimal[] yTickCoordinates;

		/** The coordinates of the ticks on Z axis. */
		public volatile BigDecimal[] zTickCoordinates;

		/** The coordinates of the ticks on the color bar. */
		public volatile BigDecimal[] colorBarTickCoordinates;

		/** The labels of the ticks on X axis. */
		public volatile String[] xTickLabelTexts;

		/** The labels of the ticks on Y axis. */
		public volatile String[] yTickLabelTexts;

		/** The labels of the ticks on Z axis. */
		public volatile String[] zTickLabelTexts;

		/** The labels of the ticks on the color bar. */
		public volatile String[] colorBarTickLabelTexts;
	}

	/**
	 * Generates/updates ticks from the specified configuration.
	 * This method does not return the result.
	 * To get the result, use getResult() method instead.
	 *
	 * @param config The configuration.
	 * @return The container storing the result of generateTicks() method.
	 */
	public static Result generateTicks(RinearnGraph3DConfiguration config) {
		Result result = new Result();

		boolean isLogPlot = false; // Temporary

		RangeConfiguration rangeConfig = config.getRangeConfiguration();
		AxisRangeConfiguration xRangeConfig = rangeConfig.getXRangeConfiguration();
		AxisRangeConfiguration yRangeConfig = rangeConfig.getYRangeConfiguration();
		AxisRangeConfiguration zRangeConfig = rangeConfig.getZRangeConfiguration();
		// RangeConfiguration.AxisRangeConfiguration cRangeConfig = zRangeConfig; // Temporary

		ScaleConfiguration scaleConfig = config.getScaleConfiguration();
		ScaleConfiguration.AxisScaleConfiguration xScaleConfig = scaleConfig.getXScaleConfiguration();
		ScaleConfiguration.AxisScaleConfiguration yScaleConfig = scaleConfig.getYScaleConfiguration();
		ScaleConfiguration.AxisScaleConfiguration zScaleConfig = scaleConfig.getZScaleConfiguration();
		ScaleConfiguration.AxisScaleConfiguration cScaleConfig = scaleConfig.getColorBarScaleConfiguration();

		// Extract an axis's gradient color.
		ColorConfiguration colorConfig = config.getColorConfiguration();
		GradientColor[] gradientColors = colorConfig.getDataGradientColors();
		if (gradientColors.length != 1) {
			throw new IllegalStateException("Multiple gradient coloring is not supported on this version yet.");
		}
		GradientColor gradientColor = gradientColors[0];
		AxisGradientColor[] axisGradientColors = gradientColor.getAxisGradientColors();
		if (axisGradientColors.length != 1) {
			throw new IllegalStateException("Multi-axis gradient color is not supported on this version yet.");
		}
		AxisGradientColor axisGradientColor = axisGradientColors[0];

		result.xTickCoordinates = xScaleConfig.getTicker().generateTickCoordinates(
				xRangeConfig.getMinimum(), xRangeConfig.getMaximum(), isLogPlot
		);
		result.yTickCoordinates = yScaleConfig.getTicker().generateTickCoordinates(
				yRangeConfig.getMinimum(), yRangeConfig.getMaximum(), isLogPlot
		);
		result.zTickCoordinates = zScaleConfig.getTicker().generateTickCoordinates(
				zRangeConfig.getMinimum(), zRangeConfig.getMaximum(), isLogPlot
		);
		result.colorBarTickCoordinates = cScaleConfig.getTicker().generateTickCoordinates(
				axisGradientColor.getMinimumBoundaryCoordinate(), axisGradientColor.getMaximumBoundaryCoordinate(), isLogPlot
		);

		result.xTickLabelTexts = xScaleConfig.getTicker().generateTickLabelTexts(
				result.xTickCoordinates, xScaleConfig.getTickLabelFormatter()
		);
		result.yTickLabelTexts = yScaleConfig.getTicker().generateTickLabelTexts(
				result.yTickCoordinates, yScaleConfig.getTickLabelFormatter()
		);
		result.zTickLabelTexts = zScaleConfig.getTicker().generateTickLabelTexts(
				result.zTickCoordinates, zScaleConfig.getTickLabelFormatter()
		);
		result.colorBarTickLabelTexts = cScaleConfig.getTicker().generateTickLabelTexts(
				result.colorBarTickCoordinates, cScaleConfig.getTickLabelFormatter()
		);
		return result;
	}
}


