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
		if (this.dividedSectionCount == 0) {
			return new BigDecimal[0];
		} else if (this.dividedSectionCount == 1) {
			return new BigDecimal[] { rangeMin };
		} else if (this.dividedSectionCount == 2) {
			return new BigDecimal[] { rangeMin, rangeMax };
		}

		// Creates a MathContext instance for specifying the precision and rounding mode of the calculations.
		MathContext mathContext = new MathContext(super.calculationPrecision, RoundingMode.HALF_EVEN);

		// Convert the min and max values to log scale before compute N-equal-division, if necessary.
		BigDecimal convertedMin = rangeMin;
		BigDecimal convertedMax = rangeMax;
		if (isLogPlot) {
			convertedMin = new BigDecimal(StrictMath.log(rangeMin.doubleValue()));
			convertedMax = new BigDecimal(StrictMath.log(rangeMax.doubleValue()));
		}

		// Calculate the interval between the ticks to be generated.
		BigDecimal interval = convertedMax.subtract(convertedMin).divide(new BigDecimal(this.dividedSectionCount), mathContext);

		// Calculate coordinates of the ticks at the equally divided point on the axis, and return it.
		BigDecimal[] tickCoords = new BigDecimal[this.dividedSectionCount + 1];
		tickCoords[0] = convertedMin;
		tickCoords[this.dividedSectionCount] = convertedMax;
		for (int itick=1; itick<this.dividedSectionCount; itick++) {
			BigDecimal distanceFromMin = interval.multiply(new BigDecimal(itick));
			tickCoords[itick] = convertedMin.add(distanceFromMin);

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
}
