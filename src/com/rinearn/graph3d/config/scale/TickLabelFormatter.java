package com.rinearn.graph3d.config.scale;

import java.math.BigDecimal;

/**
 * The base class of the formatter of tick labels.
 */
public abstract class TickLabelFormatter {

	/**
	 * Formats the specified coordinate value into a label text,
	 *
	 * @param tickCoordinate The coordinate of the numeric tick to be formatted.
	 * @return The formatted tick label.
	 */
	public abstract String format(BigDecimal tickCoordinate);

	/**
	 * Returns whether the specified coordinate is formattable by this formatter.
	 *
	 * @param tickCoordinate Returns if the specified coordinate is formattable.
	 */
	public abstract boolean isFormattable(BigDecimal tickCoordinate);

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
