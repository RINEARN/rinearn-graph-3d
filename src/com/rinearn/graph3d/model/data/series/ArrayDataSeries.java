package com.rinearn.graph3d.model.data.series;

import com.rinearn.graph3d.config.data.SeriesAttribute;

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

	/** The containers of the attribute (meta information) of this series. */
	private volatile SeriesAttribute seriesAttribute;

	/** The X-coordinate values of the points of this data series. */
	private volatile double[][] xCoordinates = null; // [irow][icol]

	/** The Y-coordinate values of the points of this data series. */
	private volatile double[][] yCoordinates = null; // [irow][icol]

	/** The Z-coordinate values of the points of this data series. */
	private volatile double[][] zCoordinates = null; // [irow][icol]

	/** The coordinate values of the extra dimensions. */
	@SuppressWarnings("unused")
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
	 * @param legend The legend of this data series.
	 */
	public ArrayDataSeries(double[][] xCoordinates, double[][] yCoordinates, double[][] zCoordinates,
			boolean[][] visibilities, String legend) {

		this.xCoordinates = xCoordinates;
		this.yCoordinates = yCoordinates;
		this.zCoordinates = zCoordinates;
		this.visibilities = visibilities;

		this.detectXRange();
		this.detectYRange();
		this.detectZRange();

		this.seriesAttribute = new SeriesAttribute();
	}


	/**
	 * Creates a new array data series consisting of the specified coordinates.
	 *
	 * @param xCoordinates The X-coordinate values of the points of this data series ([irow][icol]).
	 * @param yCoordinates The Y-coordinate values of the points of this data series ([irow][icol]).
	 * @param zCoordinates The Z-coordinate values of the points of this data series ([irow][icol]).
	 * @param extraCoordinates The coordinate values of the extra dimensions ([idim][irow][icol]).
	 * @param legend The legend of this data series.
	 */
	public ArrayDataSeries(double[][] xCoordinates, double[][] yCoordinates, double[][] zCoordinates, double[][][] extraCoordinates,
			boolean[][] visibilities, String legend) {

		this.xCoordinates = xCoordinates;
		this.yCoordinates = yCoordinates;
		this.zCoordinates = zCoordinates;
		this.extraCoordinates = extraCoordinates;
		this.visibilities = visibilities;

		this.detectXRange();
		this.detectYRange();
		this.detectZRange();

		this.seriesAttribute = new SeriesAttribute();
	}


	/**
	 * Gets the container of the attribute (meta information) of this series.
	 *
	 * @return The container of the attribute of this series.
	 */
	@Override
	public synchronized SeriesAttribute getSeriesAttribute() {
		return this.seriesAttribute;
	}

	/**
	 * Sets the container of the attribute (meta information) of this series.
	 *
	 * @param The container of the attribute of this series.
	 */
	@Override
	public synchronized void setSeriesAttribute(SeriesAttribute seriesAttribute) {
		this.seriesAttribute = seriesAttribute;
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
		double xMinTentative = Double.POSITIVE_INFINITY;
		double xMaxTentative = Double.NEGATIVE_INFINITY;

		// Find the minimum and maximum coordinates.
		for (int ixL=0; ixL<this.xCoordinates.length; ixL++) {
			for (int ixR=0; ixR<this.xCoordinates[ixL].length; ixR++) {
				if (this.visibilities[ixL][ixR]) {
					double x = this.xCoordinates[ixL][ixR];
					if (x < xMinTentative) {
						xMinTentative = x;
					}
					if (xMaxTentative < x) {
						xMaxTentative = x;
					}
				}
			}
		}

		// Stores the minimum and maximum coordinates to the fields.
		this.xMin = null;
		if (xMinTentative != Double.POSITIVE_INFINITY) {
			this.xMin = new BigDecimal(xMinTentative);
		}
		this.xMax = null;
		if (xMaxTentative != Double.NEGATIVE_INFINITY) {
			this.xMax = new BigDecimal(xMaxTentative);
		}
	}

	/**
	 * Checks whether the minimum value of the X-coordinate values exists.
	 *
	 * @return Returns true if the minimum value exists.
	 */
	@Override
	public synchronized boolean hasXMin() {
		return this.xMin != null;
	}

	/**
	 * Gets the minimum value of the X-coordinate values.
	 *
	 * @return The minimum value of the X-coordinate values.
	 */
	@Override
	public synchronized BigDecimal getXMin() {
		return this.xMin;
	}

	/**
	 * Checks whether the maximum value of the X-coordinate values exists.
	 *
	 * @return Returns true if the maximum value exists.
	 */
	@Override
	public synchronized boolean hasXMax() {
		return this.xMax != null;
	}

	/**
	 * Gets the maximum value of the X-coordinate values.
	 *
	 * @return The maximum value of the X-coordinate values.
	 */
	@Override
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
		double yMinTentative = Double.POSITIVE_INFINITY;
		double yMaxTentative = Double.NEGATIVE_INFINITY;

		// Find the minimum and maximum coordinates.
		for (int iyL=0; iyL<this.yCoordinates.length; iyL++) {
			for (int iyR=0; iyR<this.yCoordinates[iyL].length; iyR++) {
				if (this.visibilities[iyL][iyR]) {
					double y = this.yCoordinates[iyL][iyR];
					if (y < yMinTentative) {
						yMinTentative = y;
					}
					if (yMaxTentative < y) {
						yMaxTentative = y;
					}
				}
			}
		}

		// Stores the minimum and maximum coordinates to the fields.
		this.yMin = null;
		if (yMinTentative != Double.POSITIVE_INFINITY) {
			this.yMin = new BigDecimal(yMinTentative);
		}
		this.yMax = null;
		if (yMaxTentative != Double.NEGATIVE_INFINITY) {
			this.yMax = new BigDecimal(yMaxTentative);
		}
	}

	/**
	 * Checks whether the minimum value of the Y-coordinate values exists.
	 *
	 * @return Returns true if the minimum value exists.
	 */
	@Override
	public synchronized boolean hasYMin() {
		return this.yMin != null;
	}

	/**
	 * Gets the minimum value of the Y-coordinate values.
	 *
	 * @return The minimum value of the Y-coordinate values.
	 */
	@Override
	public synchronized BigDecimal getYMin() {
		return this.yMin;
	}

	/**
	 * Checks whether the maximum value of the Y-coordinate values exists.
	 *
	 * @return Returns true if the maximum value exists.
	 */
	@Override
	public synchronized boolean hasYMax() {
		return this.yMax != null;
	}

	/**
	 * Gets the maximum value of the Y-coordinate values.
	 *
	 * @return The maximum value of the Y-coordinate values.
	 */
	@Override
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
		double zMinTentative = Double.POSITIVE_INFINITY;
		double zMaxTentative = Double.NEGATIVE_INFINITY;

		// Find the minimum and maximum coordinates.
		for (int izL=0; izL<this.zCoordinates.length; izL++) {
			for (int izR=0; izR<this.zCoordinates[izL].length; izR++) {
				if (this.visibilities[izL][izR]) {
					double z = this.zCoordinates[izL][izR];
					if (z < zMinTentative) {
						zMinTentative = z;
					}
					if (zMaxTentative < z) {
						zMaxTentative = z;
					}
				}
			}
		}

		// Stores the minimum and maximum coordinates to the fields.
		this.zMin = null;
		if (zMinTentative != Double.POSITIVE_INFINITY) {
			this.zMin = new BigDecimal(zMinTentative);
		}
		this.zMax = null;
		if (zMaxTentative != Double.NEGATIVE_INFINITY) {
			this.zMax = new BigDecimal(zMaxTentative);
		}
	}

	/**
	 * Checks whether the minimum value of the Z-coordinate values exists.
	 *
	 * @return Returns true if the minimum value exists.
	 */
	@Override
	public synchronized boolean hasZMin() {
		return this.zMin != null;
	}

	/**
	 * Gets the minimum value of the Z-coordinate values.
	 *
	 * @return The minimum value of the Z-coordinate values.
	 */
	@Override
	public synchronized BigDecimal getZMin() {
		return this.zMin;
	}

	/**
	 * Checks whether the maximum value of the Z-coordinate values exists.
	 *
	 * @return Returns true if the maximum value exists.
	 */
	@Override
	public synchronized boolean hasZMax() {
		return this.zMax != null;
	}

	/**
	 * Gets the maximum value of the Z-coordinate values.
	 *
	 * @return The maximum value of the Z-coordinate values.
	 */
	@Override
	public synchronized BigDecimal getZMax() {
		return this.zMax;
	}

}
