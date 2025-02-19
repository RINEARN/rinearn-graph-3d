package com.rinearn.graph3d.model.data.series;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.data.SeriesAttribute;
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

/*
  - How This Class Is Used -

  1. "PLOT" button on the z(x,y) math tool is pressed.

  2. This class is instantiated, storing the z(x,y) and x/y discretization counts.
     The 3D mesh data is not generated yet for this step.

  3. The above instance is registered to the DataStore.

  4. When the graph is re-plotted, "computeCoordinates()" of the above instance is called.

  5. The above method generates the 3D mesh data using VnanoEngine based on the current configuration.
     (Note that, the 3D shape of a z(x,y) expression on the graph depends on the range of X/Y/Z axes,
      so we should re-generate it when the graph is re-plotted.)

  6. The generated 3D mesh data is drawn by an array of polygons, in 3D graph space.
 */

public final class ZxyMathDataSeries extends MathDataSeries {

	/** The containers of the attribute (meta information) of this series. */
	private volatile SeriesAttribute seriesAttribute;

	/** The "engine-mount", provides a script engine for computing coordinates from math expressions. */
	private final ScriptEngineMount scriptEngineMount;

	/** The configuration container (for referring the range configuration). */
	private final RinearnGraph3DConfiguration config;

	/** The math expression of "z(x,y)". */
	private volatile String zMathExpression;

	/** The number of discretized X-coordinates. */
	private volatile int xDiscretizationCount;

	/** The number of discretized Y-coordinates. */
	private volatile int yDiscretizationCount;

	/** The X-coordinate values of the points of this data series. */
	protected volatile double[][] xCoordinates = null;

	/** The Y-coordinate values of the points of this data series. */
	protected volatile double[][] yCoordinates = null;

	/** The Z-coordinate values of the points of this data series. */
	protected volatile double[][] zCoordinates = null;

	/** The array storing visibilities of the points of this data series. */
	protected volatile boolean[][] visibilities = null;

	/** Stores the maximum value of the Z-coordinate values. */
	private volatile BigDecimal zMin = null;

	/** Stores the minimum value of the Z-coordinate values. */
	private volatile BigDecimal zMax = null;


	/**
	 * Create an instance for generating data using the specified script engine, under the specified configuration.
	 *
	 * @param zExpression The math expression of "z(x,y)".
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

		this.seriesAttribute = new SeriesAttribute();
		this.seriesAttribute.setUnmodifiedLegend(this.getShortDisplayName());
		this.seriesAttribute.setModifiableLegend(this.getShortDisplayName());
	}


	/**
	 * Updates the expression and the parameters of this instance.
	 *
	 * @param zExpressionThe math expression of "z(x,y)".
	 * @param xDiscretizationCount The number of discretized X-coordinates.
	 * @param yDiscretizationCount The number of discretized Y-coordinates.
	 */
	public synchronized void update(String zMathExpression, int xDiscretizationCount, int yDiscretizationCount) {
		this.zMathExpression = zMathExpression;
		this.xDiscretizationCount = xDiscretizationCount;
		this.yDiscretizationCount = yDiscretizationCount;
		this.seriesAttribute.setUnmodifiedLegend(this.getShortDisplayName());
		this.seriesAttribute.setModifiableLegend(this.getShortDisplayName());
	}


	/**
	 * Returns whether this math expression is bounded for the directions of X and Y axes.
	 * If this method return true, we can define X- and Y- ranges of this math expression.
	 *
	 * @return Returns true if this math expression is XY-bounded.
	 */
	public synchronized boolean isXYBounded() {
		return false; // Z(x,y) is unbounded for X and Y directions.
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
	 * Gets the math expression of z(x,y).
	 *
	 * @return The math expression of z(x,y).
	 */
	public synchronized String getZMathExpression() {
		return this.zMathExpression;
	}


	/**
	 * Gets the number of discretized X-coordinates.
	 *
	 * @return The number of discretized X-coordinates.
	 */
	public synchronized int getXDiscretizationCount() {
		return this.xDiscretizationCount;
	}


	/**
	 * Gets the number of discretized Y-coordinates.
	 *
	 * @return The number of discretized Y-coordinates.
	 */
	public synchronized int getYDiscretizationCount() {
		return this.yDiscretizationCount;
	}


	/**
	 * Returns a single-line full name of this data series, to be displayed on UI, etc.
	 */
	@Override
	public synchronized String getFullDisplayName() {
		String displayedExpression = "z(x,y)=" + this.zMathExpression.replaceAll(" ", "");
		return displayedExpression;
	}


	/**
	 * Returns a single-line short name of this data series, to be displayed on UI, etc.
	 */
	@Override
	public synchronized String getShortDisplayName() {
		String displayedExpression = "z=" + this.zMathExpression.replaceAll(" ", "");
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

				// Compute X-coordinate.
				double x;
				if (ix == xN - 1) { // Branching to avoid the degradation of xMax.
					x = xMax;
				} else {
					x = xMin + xDelta * ix;
				}

				// Compute Y-coordinate.
				double y;
				if (iy == yN - 1) { // Branching to avoid the degradation of yMax.
					y = yMax;
				} else {
					y = yMin + yDelta * iy;
				}

				// Compute Z-coordinate.
				double z = this.scriptEngineMount.calculateMathExpression(this.zMathExpression, x, y);

				// Store the computed coordinates.
				this.xCoordinates[ix][iy] = x;
				this.yCoordinates[ix][iy] = y;
				this.zCoordinates[ix][iy] = z;
				this.visibilities[ix][iy] = !Double.isNaN(z);

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

		// Don't return true.
		// Mathematically, the X/Y ranges of z(x,y) plotting is "unbounded",
		// so it should not affect to the range detection of X/Y axes.
		// See also the comment in the next method.
		return false;
	}

	/**
	 * Gets the minimum value of the X-coordinate values.
	 *
	 * @return The minimum value of the X-coordinate values.
	 */
	@Override
	public synchronized BigDecimal getXMin() {
		return null;

		// Don't return the following.
		// Otherwise, when we want to fits the X range to the currently plotted data (containing other data series),
		// the range can not shrink smaller than the currently configured range.
		//
		// return this.config.getRangeConfiguration().getXRangeConfiguration().getMinimum();
	}

	/**
	 * Checks whether the maximum value of the X-coordinate values exists.
	 *
	 * @return Returns true if the maximum value exists.
	 */
	@Override
	public synchronized boolean hasXMax() {

		// Don't return true.
		// Mathematically, the X/Y ranges of z(x,y) plotting is "unbounded",
		// so it should not affect to the range detection of X/Y axes.
		// See also the comment in the next method.
		return false;
	}

	/**
	 * Gets the maximum value of the X-coordinate values.
	 *
	 * @return The maximum value of the X-coordinate values.
	 */
	@Override
	public synchronized BigDecimal getXMax() {
		return null;

		// Don't return the following.
		// Otherwise, when we want to fits the X range to the currently plotted data (containing other data series),
		// the range can not shrink smaller than the currently configured range.
		//
		// return this.config.getRangeConfiguration().getXRangeConfiguration().getMaximum();
	}


	/**
	 * Checks whether the minimum value of the Y-coordinate values exists.
	 *
	 * @return Returns true if the minimum value exists.
	 */
	@Override
	public synchronized boolean hasYMin() {

		// Don't return true.
		// Mathematically, the X/Y ranges of z(x,y) plotting is "unbounded",
		// so it should not affect to the range detection of X/Y axes.
		// See also the comment in the next method.
		return false;
	}

	/**
	 * Gets the minimum value of the Y-coordinate values.
	 *
	 * @return The minimum value of the Y-coordinate values.
	 */
	@Override
	public synchronized BigDecimal getYMin() {
		return null;

		// Don't return the following.
		// Otherwise, when we want to fits the Y range to the currently plotted data (containing other data series),
		// the range can not shrink smaller than the currently configured range.
		//
		// return this.config.getRangeConfiguration().getYRangeConfiguration().getMinimum();
	}

	/**
	 * Checks whether the maximum value of the Y-coordinate values exists.
	 *
	 * @return Returns true if the maximum value exists.
	 */
	@Override
	public synchronized boolean hasYMax() {

		// Don't return true.
		// Mathematically, the X/Y ranges of z(x,y) plotting is "unbounded",
		// so it should not affect to the range detection of X/Y axes.
		// See also the comment in the next method.
		return false;
	}

	/**
	 * Gets the maximum value of the Y-coordinate values.
	 *
	 * @return The maximum value of the Y-coordinate values.
	 */
	@Override
	public synchronized BigDecimal getYMax() {
		return null;

		// Don't return the following.
		// Otherwise, when we want to fits the Y range to the currently plotted data (containing other data series),
		// the range can not shrink smaller than the currently configured range.
		//
		// return this.config.getRangeConfiguration().getYRangeConfiguration().getMaximum();
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
