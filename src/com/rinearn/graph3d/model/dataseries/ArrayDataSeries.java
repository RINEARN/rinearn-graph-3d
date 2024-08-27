package com.rinearn.graph3d.model.dataseries;

import java.math.BigDecimal;

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


public final class ArrayDataSeries extends AbstractDataSeries {

	/** The X-coordinate values of the points of this data series. */
	private volatile double[][] xCoordinates = null; // [irow][icol]

	/** The Y-coordinate values of the points of this data series. */
	private volatile double[][] yCoordinates = null; // [irow][icol]

	/** The Z-coordinate values of the points of this data series. */
	private volatile double[][] zCoordinates = null; // [irow][icol]

	/** The coordinate values of the extra dimensions. */
	private volatile double[][][] extraCoordinates = null; // [idim][irow][icol]

	/** The array storing visibilities of the points of this data series. */
	private volatile boolean[][] visibilities; // [irow][icol]

	/** Stores the maximum value of the X-coordinate values. */
	private volatile BigDecimal xMin;

	/** Stores the minimum value of the X-coordinate values. */
	private volatile BigDecimal xMax;

	/** Stores the maximum value of the Y-coordinate values. */
	private volatile BigDecimal yMin;

	/** Stores the minimum value of the Y-coordinate values. */
	private volatile BigDecimal yMax;

	/** Stores the maximum value of the Z-coordinate values. */
	private volatile BigDecimal zMin;

	/** Stores the minimum value of the Z-coordinate values. */
	private volatile BigDecimal zMax;


	/**
	 * Creates a new array data series.
	 */
	public ArrayDataSeries() {
	}


	/**
	 * Creates a new array data series consisting of the specified coordinates.
	 *
	 * @param xCoordinates The X-coordinate values of the points of this data series ([irow][icol]).
	 * @param yCoordinates The Y-coordinate values of the points of this data series ([irow][icol]).
	 * @param zCoordinates The Z-coordinate values of the points of this data series ([irow][icol]).
	 */
	public ArrayDataSeries(double[][] xCoordinates, double[][] yCoordinates, double[][] zCoordinates) {
		this.setXCoordinates(xCoordinates);
		this.setYCoordinates(yCoordinates);
		this.setZCoordinates(zCoordinates);
		this.setVisibilitiesFromCoordinates();
	}


	/**
	 * Creates a new array data series consisting of the specified coordinates.
	 *
	 * @param xCoordinates The X-coordinate values of the points of this data series ([irow][icol]).
	 * @param yCoordinates The Y-coordinate values of the points of this data series ([irow][icol]).
	 * @param zCoordinates The Z-coordinate values of the points of this data series ([irow][icol]).
	 * @param extraCoordinates The coordinate values of the extra dimensions ([idim][irow][icol]).
	 */
	public ArrayDataSeries(double[][] xCoordinates, double[][] yCoordinates, double[][] zCoordinates, double[][][] extraCoordinates) {
		this.setXCoordinates(xCoordinates);
		this.setYCoordinates(yCoordinates);
		this.setZCoordinates(zCoordinates);
		this.extraCoordinates = extraCoordinates;
		this.setVisibilitiesFromCoordinates();
	}


	/**
	 * Sets the X-coordinate values of the points of this data series, in double-type.
	 *
	 * @param xCoordinate The X-coordinate values.
	 */
	public synchronized void setXCoordinates(double[][] xCoordinates) {
		this.xCoordinates = xCoordinates;
		this.detectXRange();
	}

	/**
	 * Gets the X-coordinate values of the points of this data series, in double-type.
	 *
	 * @return The X-coordinate values.
	 */
	@Override
	public synchronized double[][] getXCoordinates() {
		if (this.xCoordinates == null) {
			throw new IllegalStateException("The X-coordinate values have not been initialized yet.");
		}
		return this.xCoordinates;
	}


	/**
	 * Sets the Y-coordinate values of the points of this data series, in double-type.
	 *
	 * @param yCoordinate The Y-coordinate values.
	 */
	public synchronized void setYCoordinates(double[][] yCoordinates) {
		this.yCoordinates = yCoordinates;
		this.detectYRange();
	}

	/**
	 * Gets the Y-coordinate values of the points of this data series, in double-type.
	 *
	 * @return The Y-coordinate values.
	 */
	@Override
	public synchronized double[][] getYCoordinates() {
		if (this.yCoordinates == null) {
			throw new IllegalStateException("The Y-coordinate values have not been initialized yet.");
		}
		return this.yCoordinates;
	}

	/**
	 * Sets the Z-coordinate values of the points of this data series, in double-type.
	 *
	 * @param zCoordinate The Z-coordinate values.
	 */
	public synchronized void setZCoordinates(double[][] zCoordinates) {
		this.zCoordinates = zCoordinates;
		this.detectZRange();
	}

	/**
	 * Gets the Z-coordinate values of the points of this data series, in double-type.
	 *
	 * @return The Z-coordinate values.
	 */
	@Override
	public synchronized double[][] getZCoordinates() {
		if (this.zCoordinates == null) {
			throw new IllegalStateException("The Z-coordinate values have not been initialized yet.");
		}
		return this.zCoordinates;
	}


	/**
	 * Sets the coordinate values of the extra dimensions.
	 *
	 * @param extraCoordinate The coordinate values of the extra dimensions ([idim][irow][icol]).
	 */
	public synchronized void setExtraCoordinates(double[][][] extraCoordinates) {
		this.extraCoordinates = extraCoordinates;
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
		int leftDimLength = this.xCoordinates.length;
		int rightDimLength = (0 < leftDimLength) ? xCoordinates[0].length : 0;
		this.visibilities = new boolean[leftDimLength][rightDimLength];

		for (int iL=0; iL<leftDimLength; iL++) {
			for (int iR=0; iR<rightDimLength; iR++) {
				boolean xIsNaN = Double.isNaN(this.xCoordinates[iL][iR]);
				boolean yIsNaN = Double.isNaN(this.yCoordinates[iL][iR]);
				boolean zIsNaN = Double.isNaN(this.zCoordinates[iL][iR]);
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


	/**
	 * Detects the minimum and the maximum value of the x coordinate values,
	 * and stores them to "xMin" and "xMax" fields.
	 */
	private void detectXRange() {
		this.xMin = null;
		this.xMax = null;

		for (double[] xCoordsOfLine: xCoordinates) {
			for (double x: xCoordsOfLine) {

				if (Double.isNaN(x)) {
					continue;
				}
				BigDecimal xBD = new BigDecimal(x);

				if (xMin == null || xBD.compareTo(xMin) < 0) {
					xMin = xBD;
				}
				if (xMax == null || 0 < xBD.compareTo(xMax)) {
					xMax = xBD;
				}
			}
		}
	}

	/**
	 * Checks whether the minimum value of the X-coordinate values exists.
	 *
	 * @return Returns true if the minimum value exists.
	 */
	public synchronized boolean haxXMin() {
		return this.xMin != null;
	}

	/**
	 * Gets the minimum value of the X-coordinate values.
	 *
	 * @return The minimum value of the X-coordinate values.
	 */
	public synchronized BigDecimal getXMin() {
		return this.xMin;
	}

	/**
	 * Checks whether the maximum value of the X-coordinate values exists.
	 *
	 * @return Returns true if the maximum value exists.
	 */
	public synchronized boolean haxXMax() {
		return this.xMax != null;
	}

	/**
	 * Gets the maximum value of the X-coordinate values.
	 *
	 * @return The maximum value of the X-coordinate values.
	 */
	public synchronized BigDecimal getXMax() {
		return this.xMax;
	}


	/**
	 * Detects the minimum and the maximum values of the y coordinate values,
	 * and stores them to "yMin" and "yMax" fields.
	 */
	private void detectYRange() {
		this.yMin = null;
		this.yMax = null;

		for (double[] yCoordsOfLine: yCoordinates) {
			for (double y: yCoordsOfLine) {

				if (Double.isNaN(y)) {
					continue;
				}
				BigDecimal yBD = new BigDecimal(y);

				if (yMin == null || yBD.compareTo(yMin) < 0) {
					yMin = yBD;
				}
				if (yMax == null || 0 < yBD.compareTo(yMax)) {
					yMax = yBD;
				}
			}
		}
	}

	/**
	 * Checks whether the minimum value of the Y-coordinate values exists.
	 *
	 * @return Returns true if the minimum value exists.
	 */
	public synchronized boolean haxYMin() {
		return this.yMin != null;
	}

	/**
	 * Gets the minimum value of the Y-coordinate values.
	 *
	 * @return The minimum value of the Y-coordinate values.
	 */
	public synchronized BigDecimal getYMin() {
		return this.yMin;
	}

	/**
	 * Checks whether the maximum value of the Y-coordinate values exists.
	 *
	 * @return Returns true if the maximum value exists.
	 */
	public synchronized boolean haxYMax() {
		return this.yMax != null;
	}

	/**
	 * Gets the maximum value of the Y-coordinate values.
	 *
	 * @return The maximum value of the Y-coordinate values.
	 */
	public synchronized BigDecimal getYMax() {
		return this.yMax;
	}


	/**
	 * Detects the minimum and the maximum values of the z coordinate values,
	 * and stores them to "zMin" and "zMax" fields.
	 */
	private void detectZRange() {
		this.zMin = null;
		this.zMax = null;

		for (double[] zCoordsOfLine: zCoordinates) {
			for (double z: zCoordsOfLine) {

				if (Double.isNaN(z)) {
					continue;
				}
				BigDecimal zBD = new BigDecimal(z);

				if (zMin == null || zBD.compareTo(zMin) < 0) {
					zMin = zBD;
				}
				if (zMax == null || 0 < zBD.compareTo(zMax)) {
					zMax = zBD;
				}
			}
		}
	}

	/**
	 * Checks whether the minimum value of the Z-coordinate values exists.
	 *
	 * @return Returns true if the minimum value exists.
	 */
	public synchronized boolean haxZMin() {
		return this.zMin != null;
	}

	/**
	 * Gets the minimum value of the Z-coordinate values.
	 *
	 * @return The minimum value of the Z-coordinate values.
	 */
	public synchronized BigDecimal getZMin() {
		return this.zMin;
	}

	/**
	 * Checks whether the maximum value of the Z-coordinate values exists.
	 *
	 * @return Returns true if the maximum value exists.
	 */
	public synchronized boolean haxZMax() {
		return this.zMax != null;
	}

	/**
	 * Gets the maximum value of the Z-coordinate values.
	 *
	 * @return The maximum value of the Z-coordinate values.
	 */
	public synchronized BigDecimal getZMax() {
		return this.zMax;
	}

}
