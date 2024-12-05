package com.rinearn.graph3d.renderer.simple;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.RangeConfiguration;
import com.rinearn.graph3d.config.ScaleConfiguration;
import com.rinearn.graph3d.config.scale.TickLabelFormatter;

import java.math.BigDecimal;


/**
 * The class for generating tick labels/coordinates of X/Y/Z axes's scales automatically.
 */
public final class ScaleTickGenerator {

	/** The dimension index representing X. */
	public static final int X = 0;

	/** The dimension index representing Y. */
	public static final int Y = 1;

	/** The dimension index representing Z. */
	public static final int Z = 2;

	/** Stores the configuration of this application. */
	private final RinearnGraph3DConfiguration config;

	/**
	 * Creates a new instance for generating ticks under the specified configuration.
	 *
	 * @param configuration The configuration of this application.
	 */
	public ScaleTickGenerator(RinearnGraph3DConfiguration configuration) {
		if (!configuration.hasScaleConfiguration()) {
			throw new IllegalArgumentException("No scale configuration is stored in the specified configuration container.");
		}
		if (!configuration.hasRangeConfiguration()) {
			throw new IllegalArgumentException("No range configuration is stored in the specified configuration container.");
		}
		this.config = configuration;
	}


	/**
	 * Extracts the configuration of the specified axis's scale, stored in scaleConfig field.
	 *
	 * @param dimensionIndex The index of the axis.
	 * @return The configuration of the specified axis's scale.
	 */
	private ScaleConfiguration.AxisScaleConfiguration getAxisScaleConfiguration(int dimensionIndex) {

		// Extract the configuration of the scale of the specified axis (X, Y, or Z).
		switch (dimensionIndex) {
			case X : {
				return this.config.getScaleConfiguration().getXScaleConfiguration();
			}
			case Y : {
				return this.config.getScaleConfiguration().getYScaleConfiguration();
			}
			case Z : {
				return this.config.getScaleConfiguration().getZScaleConfiguration();
			}
			default : {
				throw new RuntimeException("Unexpected dimension index: " + dimensionIndex);
			}
		}
	}


	/**
	 * Extracts the configuration of the specified axis's range, stored in scaleConfig field.
	 *
	 * @param dimensionIndex The index of the axis.
	 * s@return The configuration of the specified axis's range.
	 */
	private RangeConfiguration.AxisRangeConfiguration getAxisRangeConfiguration(int dimensionIndex) {

		// Extract the configuration of the scale of the specified axis (X, Y, or Z).
		switch (dimensionIndex) {
			case X : {
				return this.config.getRangeConfiguration().getXRangeConfiguration();
			}
			case Y : {
				return this.config.getRangeConfiguration().getYRangeConfiguration();
			}
			case Z : {
				return this.config.getRangeConfiguration().getZRangeConfiguration();
			}
			default : {
				throw new RuntimeException("Unexpected dimension index: " + dimensionIndex);
			}
		}
	}


	/**
	 * Generates coordinates (positions) of ticks.
	 *
	 * @param dimensionIndex Specify 0 for X, 1 for Y, and 2 for Z axis.
	 * @return The coordinates of the ticks.
	 */
	public BigDecimal[] generateScaleTickCoordinates(int dimensionIndex) {

		// Extract the configurations of the scale & range of the specified axis (X, Y, or Z).
		final ScaleConfiguration.AxisScaleConfiguration axisScaleConfig =
				this.getAxisScaleConfiguration(dimensionIndex);
		final RangeConfiguration.AxisRangeConfiguration axisRangeConfig =
				this.getAxisRangeConfiguration(dimensionIndex);

		// Generate tick coords and return it.
		boolean isLogPlot = false; // temporary
		return axisScaleConfig.getTicker().generateTickCoordinates(
				axisRangeConfig.getMinimum(), axisRangeConfig.getMaximum(), isLogPlot
		);
	}


	/**
	 * Generates labels of ticks.
	 *
	 * @param dimensionIndex Specify 0 for X, 1 for Y, and 2 for Z axis.
	 * @param tickCoords The coordinates (positions) of the ticks.
	 * @return The labels of the ticks.
	 */
	public String[] generateScaleTickLabels(int dimensionIndex, BigDecimal[] tickCoords) {

		// Extract the configuration of the scale of the specified axis (X, Y, or Z).
		final ScaleConfiguration.AxisScaleConfiguration axisScaleConfig =
				this.getAxisScaleConfiguration(dimensionIndex);

		// Get the formatter of the tick labels.
		TickLabelFormatter formatter = axisScaleConfig.getTickLabelFormatter();

		// Generate tick labels and return it.
		return axisScaleConfig.getTicker().generateTickLabels(tickCoords, formatter);
	}
}
