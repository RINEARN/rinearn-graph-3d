package com.rinearn.graph3d.config.scale;

import com.rinearn.graph3d.config.RinearnGraph3DConfigurationException;

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

	// !!!!! NOTE !!!!!
	// The following method was originally named as "generateTickLabels", but renamed to "generateTickLabelTexts"
	// for the consistency with LabelConfiguration.AxisLabelConfiguration.setText(),
	// and considering that we may add tick-label related methods, e.g.: generateTickLabelAngles().
	// !!!!! NOTE !!!!!

	/**
	 * Generates the texts of the tick labels.
	 *
	 * @param tickCoordinates The coordinates of the ticks.
	 * @param formatter The formatter of the tick labels.
	 * @return The generated texts of the tick labels.
	 */
	public synchronized String[] generateTickLabelTexts(BigDecimal[] tickCoordinates, TickLabelFormatter formatter) {
		int tickCount = tickCoordinates.length;
		String[] tickLabelTexts = new String[tickCount];
		for (int itick=0; itick<tickCount; itick++) {
			if (formatter.isFormattable(tickCoordinates[itick])) {
				tickLabelTexts[itick] = formatter.format(tickCoordinates[itick]);
			} else {
				tickLabelTexts[itick] = "";
			}
		}
		return tickLabelTexts;
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
	public abstract void validate() throws RinearnGraph3DConfigurationException;
}
