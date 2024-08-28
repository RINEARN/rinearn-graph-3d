package com.rinearn.graph3d.model.dataseries;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.model.ScriptEngineMount;

import java.math.BigDecimal;

import org.vcssl.nano.VnanoException;

/*
[Inheritance tree]

    AbstractDataSeries
      |
      +- ArrayDataSeries
      |
      +- MathDataSeries
          |
          +- ZxyMathDataSeries
          |
          +- XtYtZtMathDataSeries < This Class
 */


public class XtYtZtMathDataSeries extends MathDataSeries {

	/** The "engine-mount", provides a script engine for computing coordinates from math expressions. */
	private final ScriptEngineMount scriptEngineMount;

	/** The configuration container (for referring the range configuration). */
	private final RinearnGraph3DConfiguration config;


	/**
	 * Create an instance for generating data using the specified script engine, under the specified configuration.
	 *
	 * @param scrioptEngineMount The "engine-mount", provides a script engine for computing coordinates from math expressions.
	 * @param config The configuration container (for referring the range configuration).
	 */
	public XtYtZtMathDataSeries(ScriptEngineMount scriptEngineMount, RinearnGraph3DConfiguration config) {
		this.scriptEngineMount = scriptEngineMount;
		this.config = config;
	}


	/**
	 * Returns a single-line string representing the math expressions of this data series,
	 * to be displayed on UI.
	 */
	@Override
	public synchronized String getDisplayedExpression() {
		throw new RuntimeException("Unimplemented yet.");
	}


	/**
	 * Computes coordinate values from the math expressions of this data series.
	 *
	 * The computed coordinate values will be stored into the fields: xCoordinates, yCoordinates and zCoordinates.
	 *
	 * @throws VnanoException Thrown when any (typically syntactic) error has been detected for calculating the math expressions.
	 */
	@Override
	public synchronized void computeCoordinates() throws VnanoException {
		throw new RuntimeException("Unimplemented yet.");
	}


	/**
	 * Gets the X-coordinate values of the points of this data series, in double-type.
	 *
	 * @return The X-coordinate values.
	 */
	@Override
	public synchronized double[][] getXCoordinates() {
		throw new RuntimeException("Unimplemented yet.");
	}


	/**
	 * Gets the Y-coordinate values of the points of this data series, in double-type.
	 *
	 * @return The Y-coordinate values.
	 */
	@Override
	public synchronized double[][] getYCoordinates() {
		throw new RuntimeException("Unimplemented yet.");
	}


	/**
	 * Gets the Z-coordinate values of the points of this data series, in double-type.
	 *
	 * @return The Z-coordinate values.
	 */
	@Override
	public synchronized double[][] getZCoordinates() {
		throw new RuntimeException("Unimplemented yet.");
	}


	/**
	 * Gets the visibilities of the points of this data series.
	 *
	 * @return The array storing visibilities of the points of this data series.
	 */
	@Override
	public synchronized boolean[][] getVisibilities() {
		throw new RuntimeException("Unimplemented yet.");
	}


	/**
	 * Checks whether the minimum value of the X-coordinate values exists.
	 *
	 * @return Returns true if the minimum value exists.
	 */
	@Override
	public synchronized boolean hasXMin() {
		throw new RuntimeException("Unimplemented yet.");
	}

	/**
	 * Gets the minimum value of the X-coordinate values.
	 *
	 * @return The minimum value of the X-coordinate values.
	 */
	@Override
	public synchronized BigDecimal getXMin() {
		throw new RuntimeException("Unimplemented yet.");
	}

	/**
	 * Checks whether the maximum value of the X-coordinate values exists.
	 *
	 * @return Returns true if the maximum value exists.
	 */
	@Override
	public synchronized boolean hasXMax() {
		throw new RuntimeException("Unimplemented yet.");
	}

	/**
	 * Gets the maximum value of the X-coordinate values.
	 *
	 * @return The maximum value of the X-coordinate values.
	 */
	@Override
	public synchronized BigDecimal getXMax() {
		throw new RuntimeException("Unimplemented yet.");
	}


	/**
	 * Checks whether the minimum value of the Y-coordinate values exists.
	 *
	 * @return Returns true if the minimum value exists.
	 */
	@Override
	public synchronized boolean hasYMin() {
		throw new RuntimeException("Unimplemented yet.");
	}

	/**
	 * Gets the minimum value of the Y-coordinate values.
	 *
	 * @return The minimum value of the Y-coordinate values.
	 */
	@Override
	public synchronized BigDecimal getYMin() {
		throw new RuntimeException("Unimplemented yet.");
	}

	/**
	 * Checks whether the maximum value of the Y-coordinate values exists.
	 *
	 * @return Returns true if the maximum value exists.
	 */
	@Override
	public synchronized boolean hasYMax() {
		throw new RuntimeException("Unimplemented yet.");
	}

	/**
	 * Gets the maximum value of the Y-coordinate values.
	 *
	 * @return The maximum value of the Y-coordinate values.
	 */
	@Override
	public synchronized BigDecimal getYMax() {
		throw new RuntimeException("Unimplemented yet.");
	}


	/**
	 * Checks whether the minimum value of the Z-coordinate values exists.
	 *
	 * @return Returns true if the minimum value exists.
	 */
	@Override
	public synchronized boolean hasZMin() {
		throw new RuntimeException("Unimplemented yet.");
	}

	/**
	 * Gets the minimum value of the Z-coordinate values.
	 *
	 * @return The minimum value of the Z-coordinate values.
	 */
	@Override
	public synchronized BigDecimal getZMin() {
		throw new RuntimeException("Unimplemented yet.");
	}

	/**
	 * Checks whether the maximum value of the Z-coordinate values exists.
	 *
	 * @return Returns true if the maximum value exists.
	 */
	@Override
	public synchronized boolean hasZMax() {
		throw new RuntimeException("Unimplemented yet.");
	}

	/**
	 * Gets the maximum value of the Z-coordinate values.
	 *
	 * @return The maximum value of the Z-coordinate values.
	 */
	@Override
	public synchronized BigDecimal getZMax() {
		throw new RuntimeException("Unimplemented yet.");
	}

}
