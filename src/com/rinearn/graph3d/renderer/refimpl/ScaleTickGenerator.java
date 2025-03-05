package com.rinearn.graph3d.renderer.refimpl;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.color.AxisGradientColor;
import com.rinearn.graph3d.config.color.ColorConfiguration;
import com.rinearn.graph3d.config.color.GradientColor;
import com.rinearn.graph3d.config.scale.ScaleConfiguration;
import com.rinearn.graph3d.config.scale.AxisScaleConfiguration;
import com.rinearn.graph3d.config.range.RangeConfiguration;
import com.rinearn.graph3d.config.range.AxisRangeConfiguration;

import java.math.BigDecimal;


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

		RangeConfiguration rangeConfig = config.getRangeConfiguration();
		AxisRangeConfiguration xRangeConfig = rangeConfig.getXRangeConfiguration();
		AxisRangeConfiguration yRangeConfig = rangeConfig.getYRangeConfiguration();
		AxisRangeConfiguration zRangeConfig = rangeConfig.getZRangeConfiguration();
		// RangeConfiguration.AxisRangeConfiguration cRangeConfig = zRangeConfig; // Temporary

		ScaleConfiguration scaleConfig = config.getScaleConfiguration();
		AxisScaleConfiguration xScaleConfig = scaleConfig.getXScaleConfiguration();
		AxisScaleConfiguration yScaleConfig = scaleConfig.getYScaleConfiguration();
		AxisScaleConfiguration zScaleConfig = scaleConfig.getZScaleConfiguration();
		AxisScaleConfiguration cScaleConfig = scaleConfig.getColorBarScaleConfiguration();

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
				xRangeConfig.getMinimumCoordinate(), xRangeConfig.getMaximumCoordinate(), xScaleConfig.isLogScaleEnabled()
		);
		result.yTickCoordinates = yScaleConfig.getTicker().generateTickCoordinates(
				yRangeConfig.getMinimumCoordinate(), yRangeConfig.getMaximumCoordinate(), yScaleConfig.isLogScaleEnabled()
		);
		result.zTickCoordinates = zScaleConfig.getTicker().generateTickCoordinates(
				zRangeConfig.getMinimumCoordinate(), zRangeConfig.getMaximumCoordinate(), zScaleConfig.isLogScaleEnabled()
		);
		result.colorBarTickCoordinates = cScaleConfig.getTicker().generateTickCoordinates(
				axisGradientColor.getMinimumBoundaryCoordinate(), axisGradientColor.getMaximumBoundaryCoordinate(),
				zScaleConfig.isLogScaleEnabled()
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


