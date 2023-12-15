package com.rinearn.graph3d.model.dataseries;

/*
[Inheritance tree]

    AbstractDataSeries
      |
      +- ArrayDataSeries < This Class
      |
      +- MathDataSeries
          |
          +- ZxyMathDataSeries
          |
          +- XtYtZtMathDataSeries
 */


public class ArrayDataSeries extends AbstractDataSeries {

	/** The X-coordinate values of the points of this data series, in double-type. */
	private volatile double[][] xDoubleCoordinates = null;

	/** The Y-coordinate values of the points of this data series, in double-type. */
	private volatile double[][] yDoubleCoordinates = null;

	/** The Z-coordinate values of the points of this data series, in double-type. */
	private volatile double[][] zDoubleCoordinates = null;

	/** The array storing visibilities of the points of this data series. */
	private volatile boolean[][] visibilities;


	/**
	 * Creates a new array data series.
	 */
	public ArrayDataSeries() {
	}


	/**
	 * Sets the X-coordinate values of the points of this data series, in double-type.
	 * 
	 * @param xCoordinate The X-coordinate values.
	 */
	public synchronized void setXCoordinates(double[][] xCoordinates) {
		this.xDoubleCoordinates = xCoordinates;
	}

	/**
	 * Gets the X-coordinate values of the points of this data series, in double-type.
	 * 
	 * @return The X-coordinate values.
	 */
	@Override
	public synchronized double[][] getXCoordinates() {
		if (this.xDoubleCoordinates == null) {
			throw new IllegalStateException("The X-coordinate values have not been initialized yet.");
		}
		return this.xDoubleCoordinates;
	}


	/**
	 * Sets the Y-coordinate values of the points of this data series, in double-type.
	 * 
	 * @param yCoordinate The Y-coordinate values.
	 */
	public synchronized void setYCoordinates(double[][] yCoordinates) {
		this.yDoubleCoordinates = yCoordinates;
	}

	/**
	 * Gets the Y-coordinate values of the points of this data series, in double-type.
	 * 
	 * @return The Y-coordinate values.
	 */
	@Override
	public synchronized double[][] getYCoordinates() {
		if (this.yDoubleCoordinates == null) {
			throw new IllegalStateException("The Y-coordinate values have not been initialized yet.");
		}
		return this.yDoubleCoordinates;		
	}


	/**
	 * Sets the Z-coordinate values of the points of this data series, in double-type.
	 * 
	 * @param zCoordinate The Z-coordinate values.
	 */
	public synchronized void setZCoordinates(double[][] zCoordinates) {
		this.zDoubleCoordinates = zCoordinates;
	}

	/**
	 * Gets the Z-coordinate values of the points of this data series, in double-type.
	 * 
	 * @return The Z-coordinate values.
	 */
	@Override
	public synchronized double[][] getZCoordinates() {
		if (this.zDoubleCoordinates == null) {
			throw new IllegalStateException("The Z-coordinate values have not been initialized yet.");
		}
		return this.zDoubleCoordinates;
	}


	/**
	 * Sets the visibilities of the points of this data series.
	 * 
	 * @param visibilities The array storing visibilities of the points of this data series.
	 */
	public synchronized void setVisibilities(boolean[][] visibilities) {
		this.visibilities = visibilities;
	}

	/**
	 * Gets the visibilities of the points of this data series.
	 * 
	 * @return The array storing visibilities of the points of this data series.
	 */
	@Override
	public synchronized boolean[][] getVisibilities() {
		if(this.visibilities == null) {
			throw new IllegalStateException("The visibilities have not been initialized yet.");
		}
		return this.visibilities;		
	}
}
