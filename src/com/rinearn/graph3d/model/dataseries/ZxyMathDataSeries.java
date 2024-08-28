package com.rinearn.graph3d.model.dataseries;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.model.ScriptEngineMount;

import org.vcssl.nano.VnanoException;

import java.math.BigDecimal;


/*
[Inheritance tree]

    AbstractDataSeries
      |
      +- ArrayDataSeries
      |
      +- MathDataSeries
          |
          +- ZxyMathDataSeries < This Class
          |
          +- XtYtZtMathDataSeries
 */


public class ZxyMathDataSeries extends MathDataSeries {

	/** The "engine-mount", provides a script engine for computing coordinates from math expressions. */
	private final ScriptEngineMount scriptEngineMount;

	/** The configuration container (for referring the range configuration). */
	private final RinearnGraph3DConfiguration config;

	/** The math expression of "z(x,y)". */
	private final String zMathExpression;

	/** The number of discretized X-coordinates. */
	private final int xDiscretizationCount;

	/** The number of discretized Y-coordinates. */
	private final int yDiscretizationCount;

	/** The X-coordinate values of the points of this data series. */
	protected volatile double[][] xCoordinates = null;

	/** The Y-coordinate values of the points of this data series. */
	protected volatile double[][] yCoordinates = null;

	/** The Z-coordinate values of the points of this data series. */
	protected volatile double[][] zCoordinates = null;

	/** The array storing visibilities of the points of this data series. */
	protected volatile boolean[][] visibilities;

	/** Stores the maximum value of the Z-coordinate values. */
	private volatile BigDecimal zMin;

	/** Stores the minimum value of the Z-coordinate values. */
	private volatile BigDecimal zMax;


	/**
	 * Create an instance for generating data using the specified script engine, under the specified configuration.
	 *
	 * @param zExpressionThe math expression of "z(x,y)".
	 * @param xDiscretizationCount The number of discretized X-coordinates.
	 * @param yDiscretizationCount The number of discretized Y-coordinates.
	 * @param scrioptEngineMount The "engine-mount", provides a script engine for computing coordinates from math expressions.
	 * @param config The configuration container (for referring the range configuration).
	 */
	public ZxyMathDataSeries(
			String zMathExpression, int xDiscretizationCount, int yDiscretizationCount,
			ScriptEngineMount scriptEngineMount, RinearnGraph3DConfiguration config) {

		this.zMathExpression = zMathExpression;
		this.xDiscretizationCount = xDiscretizationCount;
		this.yDiscretizationCount = yDiscretizationCount;
		this.scriptEngineMount = scriptEngineMount;
		this.config = config;
	}


	/**
	 * Returns a single-line string representing the math expression of this data series,
	 * to be displayed on UI.
	 */
	@Override
	public synchronized String getDisplayedExpression() {
		String displayedExpression = "z(x,y)=" + this.zMathExpression;
		return displayedExpression;
	}


	/**
	 * Computes coordinate values from the math expression of this data series.
	 *
	 * The computed coordinate values will be stored into the fields: xCoordinates, yCoordinates and zCoordinates.
	 *
	 * @throws VnanoException Thrown when any (typically syntactic) error has been detected for calculating the math expression.
	 */
	@Override
	public synchronized void computeCoordinates() throws VnanoException {

		// Prepare some values to discretize X and Y coordinates.
		int xN = this.xDiscretizationCount;
		int yN = this.yDiscretizationCount;
		double xMax = this.config.getRangeConfiguration().getXRangeConfiguration().getMaximum().doubleValue();
		double xMin = this.config.getRangeConfiguration().getXRangeConfiguration().getMinimum().doubleValue();
		double yMax = this.config.getRangeConfiguration().getYRangeConfiguration().getMaximum().doubleValue();
		double yMin = this.config.getRangeConfiguration().getYRangeConfiguration().getMinimum().doubleValue();
		double xDelta = (xMax - xMin) / (xN - 1);
		double yDelta = (yMax - yMin) / (yN - 1);
		double zMinTentative = Double.POSITIVE_INFINITY;
		double zMaxTentative = Double.NEGATIVE_INFINITY;

		// Allocate coordinate arrays.
		this.xCoordinates = new double[xN][yN];
		this.yCoordinates = new double[xN][yN];
		this.zCoordinates = new double[xN][yN];
		this.visibilities = new boolean[xN][yN];

		// Activate the script engine (initialization procedures of all connected plug-ins are invoked).
		this.scriptEngineMount.activateMathExpressionEngine();

		// Compute coordinate values, and store them into the above coordinate arrays.
		for (int ix=0; ix<xN; ix++) {
			for (int iy=0; iy<yN; iy++) {

				// Compute coordinates.
				double x = (ix == xN - 1) ? xMax : (xMin + xDelta * ix);
				double y = (iy == yN - 1) ? yMax : (yMin + yDelta * iy);
				double z = this.scriptEngineMount.calculateMathExpression(this.zMathExpression, x, y);

				// Store the computed coordinates.
				this.xCoordinates[ix][iy] = x;
				this.yCoordinates[ix][iy] = y;
				this.zCoordinates[ix][iy] = z;
				this.visibilities[ix][iy] = true;

				// Update Z range.
				if (this.visibilities[ix][iy]) {
					if (z < zMinTentative) {
						zMinTentative = z;
					}
					if (zMaxTentative < z) {
						zMaxTentative = z;
					}
				}
			}
		}

		// Stores the minimum and maximum Z-coordinates to the fields.
		this.zMin = null;
		if (zMinTentative != Double.POSITIVE_INFINITY) {
			this.zMin = new BigDecimal(zMinTentative);
		}
		this.zMax = null;
		if (zMaxTentative != Double.NEGATIVE_INFINITY) {
			this.zMax = new BigDecimal(zMaxTentative);
		}

		// Deactivate the script engine (finalization procedures of all connected plug-ins are invoked).
		this.scriptEngineMount.deactivateMathExpressionEngine();
	}


	/**
	 * Gets the X-coordinate values of the points of this data series, in double-type.
	 *
	 * @return The X-coordinate values.
	 */
	@Override
	public synchronized double[][] getXCoordinates() {
		if (this.xCoordinates == null) {
			throw new IllegalStateException("The X-coordinate values have not been generated yet.");
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
			throw new IllegalStateException("The Y-coordinate values have not been generated yet.");
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
			throw new IllegalStateException("The Z-coordinate values have not been generated yet.");
		}
		return this.zCoordinates;
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
	 * Checks whether the minimum value of the X-coordinate values exists.
	 *
	 * @return Returns true if the minimum value exists.
	 */
	@Override
	public synchronized boolean hasXMin() {
		return true;
	}

	/**
	 * Gets the minimum value of the X-coordinate values.
	 *
	 * @return The minimum value of the X-coordinate values.
	 */
	@Override
	public synchronized BigDecimal getXMin() {
		return this.config.getRangeConfiguration().getXRangeConfiguration().getMinimum();
	}

	/**
	 * Checks whether the maximum value of the X-coordinate values exists.
	 *
	 * @return Returns true if the maximum value exists.
	 */
	@Override
	public synchronized boolean hasXMax() {
		return true;
	}

	/**
	 * Gets the maximum value of the X-coordinate values.
	 *
	 * @return The maximum value of the X-coordinate values.
	 */
	@Override
	public synchronized BigDecimal getXMax() {
		return this.config.getRangeConfiguration().getXRangeConfiguration().getMaximum();
	}


	/**
	 * Checks whether the minimum value of the Y-coordinate values exists.
	 *
	 * @return Returns true if the minimum value exists.
	 */
	@Override
	public synchronized boolean hasYMin() {
		return true;
	}

	/**
	 * Gets the minimum value of the Y-coordinate values.
	 *
	 * @return The minimum value of the Y-coordinate values.
	 */
	@Override
	public synchronized BigDecimal getYMin() {
		return this.config.getRangeConfiguration().getYRangeConfiguration().getMinimum();
	}

	/**
	 * Checks whether the maximum value of the Y-coordinate values exists.
	 *
	 * @return Returns true if the maximum value exists.
	 */
	@Override
	public synchronized boolean hasYMax() {
		return true;
	}

	/**
	 * Gets the maximum value of the Y-coordinate values.
	 *
	 * @return The maximum value of the Y-coordinate values.
	 */
	@Override
	public synchronized BigDecimal getYMax() {
		return this.config.getRangeConfiguration().getYRangeConfiguration().getMaximum();
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
