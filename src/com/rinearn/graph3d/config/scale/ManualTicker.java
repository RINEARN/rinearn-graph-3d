package com.rinearn.graph3d.config.scale;

import java.math.BigDecimal;


/**
 *
 */
public class ManualTicker extends Ticker {

	/** The coordinates (locations) of the ticks. */
	private volatile BigDecimal[] tickCoordinates = {
			BigDecimal.ONE.negate(), new BigDecimal("-0.5"), BigDecimal.ZERO, new BigDecimal("0.5"), BigDecimal.ONE
	};

	/** The labels of the ticks. */
	private volatile String[] tickLabels = { "-1.0", "-0.5", "0", "0.5", "1.0" };

	/**
	 * Create a new instance.
	 */
	public ManualTicker() {
	}

	/**
	 * Sets the coordinates (locations) of the ticks.
	 *
	 * @param tickCoordinates The coordinates of the ticks.
	 */
	public void setTickCoordinates(BigDecimal[] tickCoordinates) {
		this.tickCoordinates = tickCoordinates;
	}

	/**
	 * Gets the coordinates (locations) of the ticks.
	 *
	 * @return The coordinates (locations) of the ticks.
	 */
	public BigDecimal[] getTickCoordinates() {
		return this.tickCoordinates;
	}

	/**
	 * Sets the labels of the ticks.
	 *
	 * @param The labels of the ticks.
	 */
	public void setTickLabels(String[] tickLabels) {
		this.tickLabels = tickLabels;
	}

	/**
	 * Gets the labels of the ticks.
	 *
	 * @return The labels of the ticks.
	 */
	public String[] getTickLabels() {
		return this.tickLabels;
	}


	@Override
	public BigDecimal[] generateTickCoordinates(BigDecimal rangeMin, BigDecimal rangeMax, boolean isLogPlot) {
		return this.tickCoordinates;
	}

	@Override
	public String[] generateTickLabels(BigDecimal[] tickCoordinates, TickLabelFormatter formatter) {
		return this.tickLabels;
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
		if (this.tickCoordinates.length != this.tickLabels.length) {
			throw new IllegalStateException("The number of the tick coordinates does not match with the number of the tick labels.");
		}
		for (BigDecimal coord: this.tickCoordinates) {
			if (coord == null) {
				throw new IllegalStateException("Null value is contained in the tick coordinates.");
			}
		}
		for (String label: this.tickLabels) {
			if (label == null) {
				throw new IllegalStateException("Null label is contained in the tick labels.");
			}
		}
	}
}
