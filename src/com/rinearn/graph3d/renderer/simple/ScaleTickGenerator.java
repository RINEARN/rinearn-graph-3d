package com.rinearn.graph3d.renderer.simple;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.RangeConfiguration;
import com.rinearn.graph3d.config.ScaleConfiguration;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;


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

		// Generate coordinates of ticks for the specified mode.
		switch (axisScaleConfig.getTickMode()) {
			case EQUAL_DIVISION : {
				return this.generateScaleTickCoordsByEqualDivision(axisScaleConfig, axisRangeConfig);
			}
			case MANUAL : {
				return axisScaleConfig.getTickCoordinates();
			}
			default : {
				throw new RuntimeException("The specified tick mode is unimplemented yet: " + axisScaleConfig.getTickMode());
			}
		}
	}


	/**
	 * Generates coordinates (positions) of ticks for EQUAL_DIVISION mode.
	 * 
	 * @param axisScaleConfig The configuration of the axis's scale.
	 * @param axisRangeConfig The configuration of the axis's range.
	 * @return The coordinates of the ticks.
	 */
	private BigDecimal[] generateScaleTickCoordsByEqualDivision(
			ScaleConfiguration.AxisScaleConfiguration axisScaleConfig,
			RangeConfiguration.AxisRangeConfiguration axisRangeConfig) {

		// Creates a MathContext instance for specifying the precision and rounding mode of the calculations.
		int precision = axisScaleConfig.getCalculationPrecision();
		MathContext mathContext = new MathContext(precision, RoundingMode.HALF_EVEN);

		// Get the number of sections between the ticks to be generated.
		int sectionCount = axisScaleConfig.getDividedSectionCount();
		BigDecimal sectionCountBD = new BigDecimal(sectionCount);

		// Calculate the interval between the ticks to be generated.
		BigDecimal min = axisRangeConfig.getMinimum();
		BigDecimal max = axisRangeConfig.getMaximum();
		BigDecimal interval = max.subtract(min).divide(sectionCountBD, mathContext);

		// We can return the result without calculations if the number of sections is smaller than 3.
		if (sectionCount == 0) {
			return new BigDecimal[0];
		} else if (sectionCount == 1) {
			return new BigDecimal[] { min };
		} else if (sectionCount == 2) {
			return new BigDecimal[] { min, max };
		}

		// Calculate coordinates of the ticks at the equally divided point on the axis, and return it.
		BigDecimal[] tickCoords = new BigDecimal[sectionCount + 1];
		tickCoords[0] = min;
		tickCoords[sectionCount] = max;
		for (int itick=1; itick<sectionCount; itick++) {
			BigDecimal distanceFromMin = interval.multiply(new BigDecimal(itick));
			tickCoords[itick] = min.add(distanceFromMin);
		}
		return tickCoords;
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

		// Generate the labels based on the tick mode.
		switch (axisScaleConfig.getTickMode()) {
			case MANUAL : {
				return axisScaleConfig.getTickLabels();
			}
			case EQUAL_DIVISION : {
				int tickCount = tickCoords.length;
				String[] tickLabels = new String[tickCount];

				// Get the formatters of the tick labels.
				ScaleConfiguration.NumericTickLabelFormatter[] formatters =
						axisScaleConfig.getNumericTickLabelFormatters();

				// Generate tick labels from their coordinate values, and format them.
				for (int itick=0; itick<tickCount; itick++) {

					// Firstly, define the label text of the tick,
					// as String representation of its coordinate value in full precision.
					tickLabels[itick] = tickCoords[itick].toString();

					// Next, replace the label by the text formatted by the formatter
					// of which range contains the coordinate, if it exists.
					int formatterCount = formatters.length;
					for (int iformatter=0; iformatter<formatterCount; iformatter++) {

						if (formatters[iformatter].contains(tickCoords[itick])) {
							tickLabels[itick] = formatters[iformatter].format(tickCoords[itick]);
						}
					}
				}
				return tickLabels;				
			}
			default : {
				throw new UnsupportedOperationException("Unknown tick mode: " + axisScaleConfig.getTickMode());
			}
		}
	}
}
