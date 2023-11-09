package com.rinearn.graph3d.model.dataseries;

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


public class ExpressionDataSeries extends AbstractDataSeries {

	/** The X-coordinate values of the points of this data series. */
	private volatile double[][] xCoordinates = null;

	/** The Y-coordinate values of the points of this data series. */
	private volatile double[][] yCoordinates = null;

	/** The Z-coordinate values of the points of this data series. */
	private volatile double[][] zCoordinates = null;


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
