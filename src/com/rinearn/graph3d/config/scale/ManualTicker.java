package com.rinearn.graph3d.config.scale;

import com.rinearn.graph3d.config.RinearnGraph3DConfigurationException;

import java.math.BigDecimal;


/**
 *
 */
public class ManualTicker extends Ticker {

	/** The coordinates (locations) of the ticks. */
	private volatile BigDecimal[] tickCoordinates = {
			BigDecimal.ONE.negate(), new BigDecimal("-0.5"), BigDecimal.ZERO, new BigDecimal("0.5"), BigDecimal.ONE
	};

	/** The texts of the tick labels. */
	private volatile String[] tickLabelTexts = { "-1.0", "-0.5", "0", "0.5", "1.0" };

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

	// !!!!! NOTE !!!!!
	// The following method was originally named as "setTickLabels", but renamed to "setTickLabelTexts"
	// for the consistency with LabelConfiguration.AxisLabelConfiguration.setText(),
	// and considering that we may add tick-label related methods, e.g.: setTickLabelAngles().
	// !!!!! NOTE !!!!!

	/**
	 * Sets the labels of the ticks.
	 *
	 * @param The labels of the ticks.
	 */
	public void setTickLabelTexts(String[] tickLabelTexts) {
		this.tickLabelTexts = tickLabelTexts;
	}

	// !!!!! NOTE !!!!!
	// The following method was originally named as "getTickLabels", but renamed to "getTickLabelTexts"
	// for the consistency with LabelConfiguration.AxisLabelConfiguration.setText(),
	// and considering that we may add tick-label related methods, e.g.: getTickLabelAngles().
	// !!!!! NOTE !!!!!

	/**
	 * Gets the texts of the tick labels.
	 *
	 * @return The texts of the tick labels.
	 */
	public String[] getTickLabelTexts() {
		return this.tickLabelTexts;
	}


	@Override
	public BigDecimal[] generateTickCoordinates(BigDecimal rangeMin, BigDecimal rangeMax, boolean isLogPlot) {
		return this.tickCoordinates;
	}

	@Override
	public String[] generateTickLabelTexts(BigDecimal[] tickCoordinates, TickLabelFormatter formatter) {
		return this.tickLabelTexts;
	}


	/**
	 * Validates correctness and consistency of parameters stored in this instance.
	 *
	 * This method is called when the ScaleConfiguration having this instance is specified to RinearnGraph3D or its renderer.
	 * If no issue is detected, nothing occurs.
	 * If any issue is detected, throws IllegalStateException.
	 *
	 * @throws RinearnGraph3DConfigurationException Thrown when incorrect or inconsistent settings are detected.
	 */
	@Override
	public void validate() throws RinearnGraph3DConfigurationException {
		if (this.tickCoordinates.length != this.tickLabelTexts.length) {
			throw new RinearnGraph3DConfigurationException("The number of the tick coordinates does not match with the number of the tick labels.");
		}
		for (BigDecimal coord: this.tickCoordinates) {
			if (coord == null) {
				throw new RinearnGraph3DConfigurationException("Null value is contained in the tick coordinates.");
			}
		}
		for (String label: this.tickLabelTexts) {
			if (label == null) {
				throw new RinearnGraph3DConfigurationException("Null label is contained in the tick labels.");
			}
		}
	}
}
