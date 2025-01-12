package com.rinearn.graph3d.config.scale;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * The ticker to generate ticks at the N-equal-division points of the range of the axis.
 */
public class EqualDivisionTicker extends Ticker {

	/** The number of the divided sections. */
	private int dividedSectionCount = 4;


	/**
	 * Create a new instance.
	 */
	public EqualDivisionTicker() {
	}


	/**
	 * Sets the number of the divided sections.
	 *
	 * @param dividedSectionCount The number of the divided sections.
	 */
	public void setDividedSectionCount(int dividedSectionCount) {
		this.dividedSectionCount = dividedSectionCount;
	}

	/**
	 * Gets the number of the divided sections.
	 *
	 * @return The number of the divided section.
	 */
	public int getDividedSectionCount() {
		return this.dividedSectionCount;
	}


	/**
	 * Generates the coordinates (locations) of the ticks.
	 *
	 * @param rangeMin The minimum value of the range of the target axis.
	 * @param rangeMax The maximum value of the range of the target axis.
	 * @param isLogPlot If the logarithmic plot option is selected, passed true.
	 * @return The generated tick coordinates.
	 */
	@Override
	public synchronized BigDecimal[] generateTickCoordinates(BigDecimal rangeMin, BigDecimal rangeMax, boolean isLogPlot) {

		// We can return the result without calculations if the number of sections is smaller than 3.
		if (this.dividedSectionCount == 1) {
			return new BigDecimal[] { rangeMin, rangeMax };
		}

		// Creates a MathContext instance for specifying the precision and rounding mode of the calculations.
		MathContext mathContext = new MathContext(super.calculationPrecision, RoundingMode.HALF_UP);

		// Convert the min and max values to log scale before compute N-equal-division, if necessary.
		BigDecimal convertedMin = rangeMin;
		BigDecimal convertedMax = rangeMax;
		if (isLogPlot) {
			convertedMin = new BigDecimal(StrictMath.log(rangeMin.doubleValue()));
			convertedMax = new BigDecimal(StrictMath.log(rangeMax.doubleValue()));
		}

		// Don't calculate the interval as follows and calculate the tick coordinates using it.
		//
		//     BigDecimal interval = convertedMax.subtract(convertedMin).divide(new BigDecimal(this.dividedSectionCount), mathContext);
		//
		// If do it, the tick label of "0" can contain tiny error,
		// when "interval" is smaller a little than the theoretical value of (max-min)/N, where N is dividedSectionCount.
		//
		// For example, it occurs when N = 6 and the range is [-1, 1] (so interval is 0.33333... < 1/3),
		// leading that the tick label of "0" is displayed as "-1.0E-{mathContextForDivision}".

		// Calculate coordinates of the ticks at the equally divided point on the axis, and return it.
		BigDecimal[] tickCoords = new BigDecimal[this.dividedSectionCount + 1];
		tickCoords[0] = convertedMin;
		tickCoords[this.dividedSectionCount] = convertedMax;
		for (int itick=1; itick<this.dividedSectionCount; itick++) {

			// Calculate the value of itick-th tick coordinate. Be careful of the tiny calculation error. See the above comments.
			BigDecimal normalizedCoord = new BigDecimal(itick).divide(new BigDecimal(this.dividedSectionCount), mathContext); // r: i/N
			BigDecimal distanceFromMin = convertedMax.subtract(convertedMin).multiply(normalizedCoord); // d: (max-min) * r
			tickCoords[itick] = convertedMin.add(distanceFromMin); // result: min + d

			// If isLogPlot is true, the min and max were converted to log scale,
			// so N-equal-division coords between them, tickCoords[itick] in here, are also log scale.
			// They will be the displayed label values (through formattera) on the graph,
			// so we should recover them to the real scale value.
			if (isLogPlot) {
				// Recover log()-ed in real scale ussing exp().
				tickCoords[itick] = new BigDecimal(StrictMath.exp(tickCoords[itick].doubleValue()));
			}
		}
		return tickCoords;
	}


	/**
	 * Validates correctness and consistency of parameters stored in this instance.
	 *
	 * This method is called when the ScaleConfiguration having this instance is specified to RinearnGraph3D or its renderer.
	 * If no issue is detected, nothing occurs.
	 * If any issue is detected, throws IllegalStateException.
	 *
	 * @throws IllegalStateException Thrown when incorrect or inconsistent settings are detected.
	 */
	@Override
	public void validate() throws IllegalStateException {
		if (this.getDividedSectionCount() < 1) {
			throw new IllegalStateException("The length of tick lines must be greater than 1.");
		}
	}
}
