package com.rinearn.graph3d.config.scale;

import java.math.BigDecimal;

/**
 * The base class of tickers,
 * which are classes to generates tick coordinates and tick labels.
 */
public abstract class Ticker {

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
	public abstract BigDecimal[] generateTickLabels(BigDecimal[] tickCoordinates, TickLabelFormatter formatter);
}
