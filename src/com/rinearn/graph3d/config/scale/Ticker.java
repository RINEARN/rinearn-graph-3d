package com.rinearn.graph3d.config.scale;

import java.math.BigDecimal;

/**
 * The base class of tickers,
 * which are classes to generates tick coordinates and tick labels.
 */
public abstract class Ticker {

	/** The precision of the internal calculation of the tick coordinates. **/
	protected volatile int calculationPrecision = 128;

	/**
	 * Sets the precision of internal calculations of the tick coordinates.
	 *
	 * @param calculationPrecision The precision of internal calculations of the tick coordinates.
	 */
	public synchronized void setCalculationPrecision(int calculationPrecision) {
		this.calculationPrecision = calculationPrecision;
	}

	/**
	 * Gets the precision of internal calculations of the tick coordinates.
	 *
	 * @return The precision of internal calculations of the tick coordinates.
	 */
	public synchronized int getCalculationPrecision() {
		return this.calculationPrecision;
	}

	/**
	 * Generates the coordinates (locations) of the ticks.
	 *
	 * @param rangeMin The minimum value of the range of the target axis.
	 * @param rangeMax The maximum value of the range of the target axis.
	 * @param isLogPlot If the logarithmic plot option is selected, passed true.
	 * @return The generated tick coordinates.
	 */
	public abstract BigDecimal[] generateTickCoordinates(BigDecimal rangeMin, BigDecimal rangeMax, boolean isLogPlot);

	/**
	 * Generates the tick labels.
	 *
	 * @param tickCoordinates
	 * @param formatter
	 * @return The generated tick labels.
	 */
	public synchronized String[] generateTickLabels(BigDecimal[] tickCoordinates, TickLabelFormatter formatter) {
		int tickCount = tickCoordinates.length;
		String[] tickLabels = new String[tickCount];
		for (int itick=0; itick<tickCount; itick++) {
			if (formatter.isFormattable(tickCoordinates[itick])) {
				tickLabels[itick] = formatter.format(tickCoordinates[itick]);
			} else {
				tickLabels[itick] = "";
			}
		}
		return tickLabels;
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
	public abstract void validate() throws IllegalStateException;
}
