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
	 * Creates a new array data series consisting of the specified coordinates.
	 *
	 * @param xCoordinates The X-coordinate values of the points of this data series, in double-type.
	 * @param yCoordinates The Y-coordinate values of the points of this data series, in double-type.
	 * @param zCoordinates The Z-coordinate values of the points of this data series, in double-type.
	 */
	public ArrayDataSeries(double[][] xCoordinates, double[][] yCoordinates, double[][] zCoordinates) {
		this.xDoubleCoordinates = xCoordinates;
		this.yDoubleCoordinates = yCoordinates;
		this.zDoubleCoordinates = zCoordinates;
		this.setVisibilitiesFromCoordinates();
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
	 * Sets the visibilities from the X/Y/Z coordinate values.
	 *
	 * If X/Y/Z coordinate values of a point contains NaN, the point is regarded as invisible.
	 */
	public synchronized void setVisibilitiesFromCoordinates() {
		int leftDimLength = this.xDoubleCoordinates.length;
		int rightDimLength = (0 < leftDimLength) ? xDoubleCoordinates[0].length : 0;
		this.visibilities = new boolean[leftDimLength][rightDimLength];

		for (int iL=0; iL<leftDimLength; iL++) {
			for (int iR=0; iR<rightDimLength; iR++) {
				boolean xIsNaN = Double.isNaN(this.xDoubleCoordinates[iL][iR]);
				boolean yIsNaN = Double.isNaN(this.yDoubleCoordinates[iL][iR]);
				boolean zIsNaN = Double.isNaN(this.zDoubleCoordinates[iL][iR]);
				this.visibilities[iL][iR] = !xIsNaN && !yIsNaN && !zIsNaN;
			}
		}
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
