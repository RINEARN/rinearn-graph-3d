package com.rinearn.graph3d.model.dataseries;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.model.ScriptEngineMount;

/*
[Inheritance tree]

    AbstractDataSeries
      |
      +- ArrayDataSeries
      |
      +- ExpressionDataSeries < This Class
          |
          +- ZxyExpressionDataSeries
          |
          +- XtYtZtExpressionDataSeries
 */


public abstract class ExpressionDataSeries extends AbstractDataSeries {

	/** The "engine-mount", provides a script engine for computing coordinates from math expressions. */
	private final ScriptEngineMount scriptEngineMount;

	/** The configuration container (for referring the range configuration). */
	private final RinearnGraph3DConfiguration config;

	/** The X-coordinate values of the points of this data series. */
	private volatile double[][] xCoordinates = null;

	/** The Y-coordinate values of the points of this data series. */
	private volatile double[][] yCoordinates = null;

	/** The Z-coordinate values of the points of this data series. */
	private volatile double[][] zCoordinates = null;


	/**
	 * Create an instance for generating data using the specified script engine, under the specified configuration.
	 * 
	 * @param scrioptEngineMount The "engine-mount", provides a script engine for computing coordinates from math expressions.
	 * @param config The configuration container (for referring the range configuration).
	 */
	protected ExpressionDataSeries(ScriptEngineMount scriptEngineMount, RinearnGraph3DConfiguration config) {
		this.scriptEngineMount = scriptEngineMount;
		this.config = config;
	}


	/**
	 * Computes coordinate values from math expression(s) of this data series.
	 * 
	 * The computed coordinate values will be stored into the fields: xCoordinates, yCoordinates and zCoordinates.
	 * This abstract method is implemented by subclasses: ZxyExpressionDataSeries and XtYtZtExpressionDataSeries.
	 */
	public abstract void computeCoordinates();


	/**
	 * Gets the X-coordinate values of the points of this data series, in double-type.
	 * 
	 * @return The X-coordinate values.
	 */
	@Override
	public synchronized double[][] getXCoordinates() {
		if (this.xCoordinates == null) {
			throw new IllegalStateException("The X-coordinate values has not been generated yet.");
		}
		return this.xCoordinates;
	}


	/**
	 * Gets the Y-coordinate values of the points of this data series, in double-type.
	 * 
	 * @return The Y-coordinate values.
	 */
	@Override
	public synchronized double[][] getYCoordinates() {
		if (this.yCoordinates == null) {
			throw new IllegalStateException("The Y-coordinate values has not been generated yet.");
		}
		return this.yCoordinates;		
	}


	/**
	 * Gets the Z-coordinate values of the points of this data series, in double-type.
	 * 
	 * @return The Z-coordinate values.
	 */
	@Override
	public synchronized double[][] getZCoordinates() {
		if (this.zCoordinates == null) {
			throw new IllegalStateException("The Z-coordinate values has not been generated yet.");
		}
		return this.zCoordinates;
	}
}
