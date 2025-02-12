package com.rinearn.graph3d.model.data.series;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.data.SeriesAttribute;
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


public final class XtYtZtMathDataSeries extends MathDataSeries {

	/** The containers of the attribute (meta information) of this series. */
	private volatile SeriesAttribute seriesAttribute;

	/** The "engine-mount", provides a script engine for computing coordinates from math expressions. */
	private final ScriptEngineMount scriptEngineMount;

	/** The configuration container (for referring the range configuration). */
	@SuppressWarnings("unused")
	private final RinearnGraph3DConfiguration config;

	/** The math expression of "x(t)". */
	private volatile String xtMathExpression;

	/** The math expression of "y(t)". */
	private volatile String ytMathExpression;

	/** The math expression of "z(t)". */
	private volatile String ztMathExpression;

	/** Stores the maximum value of the discretized time values. */
	private volatile BigDecimal timeMin = null;

	/** Stores the minimum value of the discretized time values. */
	private volatile BigDecimal timeMax = null;

	/** The number of discretized time points. */
	private volatile int timeDiscretizationCount;


	/** The X-coordinate values of the points of this data series. */
	protected volatile double[][] xCoordinates = null;

	/** The Y-coordinate values of the points of this data series. */
	protected volatile double[][] yCoordinates = null;

	/** The Z-coordinate values of the points of this data series. */
	protected volatile double[][] zCoordinates = null;

	/** The array storing visibilities of the points of this data series. */
	protected volatile boolean[][] visibilities = null;

	/** Stores the maximum value of the X-coordinates. */
	private volatile BigDecimal xMin = null;

	/** Stores the minimum value of the X-coordinates. */
	private volatile BigDecimal xMax = null;

	/** Stores the maximum value of the Y-coordinates. */
	private volatile BigDecimal yMin = null;

	/** Stores the minimum value of the Y-coordinates. */
	private volatile BigDecimal yMax = null;

	/** Stores the maximum value of the Z-coordinates. */
	private volatile BigDecimal zMin = null;

	/** Stores the minimum value of the Z-coordinates. */
	private volatile BigDecimal zMax = null;


	/**
	 * Create an instance for generating data using the specified script engine, under the specified configuration.
	 *
	 * @param xtExpression The math expression of "x(t)".
	 * @param ytExpression The math expression of "y(t)".
	 * @param ztExpression The math expression of "z(t)".
	 * @param timeMin The starting time.
	 * @param timeMax The ending time.
	 * @param timeDiscretizationCount The number of discretized time points.
	 * @param scrioptEngineMount The "engine-mount", provides a script engine for computing coordinates from math expressions.
	 * @param config The configuration container (for referring the range configuration).
	 */
	public XtYtZtMathDataSeries(
			String xtMathExpression, String ytMathExpression, String ztMathExpression,
			BigDecimal timeMin, BigDecimal timeMax, int timeDiscretizationCount,
			ScriptEngineMount scriptEngineMount, RinearnGraph3DConfiguration config) {

		this.xtMathExpression = xtMathExpression;
		this.ytMathExpression = ytMathExpression;
		this.ztMathExpression = ztMathExpression;
		this.timeMin = timeMin;
		this.timeMax = timeMax;
		this.timeDiscretizationCount = timeDiscretizationCount;
		this.scriptEngineMount = scriptEngineMount;
		this.config = config;

		this.seriesAttribute = new SeriesAttribute();
		this.seriesAttribute.setUnmodifiedLegend(this.getFullDisplayName());
		this.seriesAttribute.setModifiableLegend(this.getFullDisplayName());
	}



	/**
	 * Updates the expression and the parameters of this instance.
	 *
	 * @param xtExpression The math expression of "x(t)".
	 * @param ytExpression The math expression of "y(t)".
	 * @param ztExpression The math expression of "z(t)".
	 * @param timeMin The starting time.
	 * @param timeMax The ending time.
	 * @param timeDiscretizationCount The number of discretized time points.
	 */
	public synchronized void update(
			String xtMathExpression, String ytMathExpression, String ztMathExpression,
			BigDecimal timeMin, BigDecimal timeMax, int timeDiscretizationCount) {

		this.xtMathExpression = xtMathExpression;
		this.ytMathExpression = ytMathExpression;
		this.ztMathExpression = ztMathExpression;
		this.timeMin = timeMin;
		this.timeMax = timeMax;
		this.timeDiscretizationCount = timeDiscretizationCount;
		this.seriesAttribute.setUnmodifiedLegend(this.getShortDisplayName());
		this.seriesAttribute.setModifiableLegend(this.getShortDisplayName());
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
	 * Gets the math expression of x(t).
	 */
	public synchronized String getXtMathExpression() {
		return this.xtMathExpression;
	}


	/**
	 * Gets the math expression of y(t).
	 */
	public synchronized String getYtMathExpression() {
		return this.ytMathExpression;
	}


	/**
	 * Gets the math expression of z(t).
	 */
	public synchronized String getZtMathExpression() {
		return this.ztMathExpression;
	}


	/**
	 * Gets the number of discretized time points.
	 *
	 * @return The number of discretized time points.
	 */
	public synchronized int getTimeDiscretizationCount() {
		return this.timeDiscretizationCount;
	}


	/**
	 * Gets the starting time.
	 *
	 * @return The starting time.
	 */
	public synchronized BigDecimal getTimeMin() {
		return this.timeMin;
	}


	/**
	 * Gets the ending time.
	 *
	 * @return The ending time.
	 */
	public synchronized BigDecimal getTimeMax() {
		return this.timeMax;
	}


	/**
	 * Returns a single-line full name of this data series, to be displayed on UI.
	 */
	@Override
	public synchronized String getFullDisplayName() {
		String displayedExpression
				=  "x(t)=" + this.xtMathExpression.replaceAll(" ", "")
				+ ", y(t)=" + this.ytMathExpression.replaceAll(" ", "")
				+ ", z(t)=" + this.ztMathExpression.replaceAll(" ", "");

		return displayedExpression;
	}


	/**
	 * Returns a single-line short name of this data series, to be displayed on UI, etc.
	 */
	@Override
	public synchronized String getShortDisplayName() {
		String displayedExpression
		=  "x=" + this.xtMathExpression.replaceAll(" ", "")
		+ ", y=" + this.ytMathExpression.replaceAll(" ", "")
		+ ", z=" + this.ztMathExpression.replaceAll(" ", "");
		return displayedExpression;
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

		// Allocate coordinate arrays.
		int tN = this.timeDiscretizationCount;
		this.xCoordinates = new double[1][tN];
		this.yCoordinates = new double[1][tN];
		this.zCoordinates = new double[1][tN];
		this.visibilities = new boolean[1][tN];

		// Activate the script engine (initialization procedures of all connected plug-ins are invoked).
		this.scriptEngineMount.activateMathExpressionEngine();

		double tMaxDouble = timeMax.doubleValue();
		double tMinDouble = timeMin.doubleValue();
		double tDelta = (tMaxDouble - tMinDouble) / (tN - 1);

		// Stores temporary min/max values of x, y, and t for updating the range of the graph.
		double xMinTentative = Double.POSITIVE_INFINITY;
		double xMaxTentative = Double.NEGATIVE_INFINITY;
		double yMinTentative = Double.POSITIVE_INFINITY;
		double yMaxTentative = Double.NEGATIVE_INFINITY;
		double zMinTentative = Double.POSITIVE_INFINITY;
		double zMaxTentative = Double.NEGATIVE_INFINITY;

		// Compute coordinate values, and store them into the above coordinate arrays.
		for (int it=0; it<tN; it++) {
			double t;
			if (it == tN - 1) { // Branching to avoid the degradation of timeMax.
				t = tMaxDouble;
			} else {
				t = tMinDouble + tDelta * it;
			}

			// Compute X/Y/Z coordinates.
			double x = this.scriptEngineMount.calculateMathExpression(this.xtMathExpression, t);
			double y = this.scriptEngineMount.calculateMathExpression(this.ytMathExpression, t);
			double z = this.scriptEngineMount.calculateMathExpression(this.ztMathExpression, t);

			// Store the computed coordinates.
			this.xCoordinates[0][it] = x;
			this.yCoordinates[0][it] = y;
			this.zCoordinates[0][it] = z;
			this.visibilities[0][it] = !Double.isNaN(x) && !Double.isNaN(y) && !Double.isNaN(z);

			// Update X range.
			if (this.visibilities[0][it]) {
				if (x < xMinTentative) {
					xMinTentative = x;
				}
				if (xMaxTentative < x) {
					xMaxTentative = x;
				}
				if (y < yMinTentative) {
					yMinTentative = y;
				}
				if (yMaxTentative < y) {
					yMaxTentative = y;
				}
				if (z < zMinTentative) {
					zMinTentative = z;
				}
				if (zMaxTentative < z) {
					zMaxTentative = z;
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
		this.yMin = null;
		if (yMinTentative != Double.POSITIVE_INFINITY) {
			this.yMin = new BigDecimal(yMinTentative);
		}
		this.yMax = null;
		if (yMaxTentative != Double.NEGATIVE_INFINITY) {
			this.yMax = new BigDecimal(yMaxTentative);
		}
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
		return this.xCoordinates;
	}


	/**
	 * Gets the Y-coordinate values of the points of this data series, in double-type.
	 *
	 * @return The Y-coordinate values.
	 */
	@Override
	public synchronized double[][] getYCoordinates() {
		return this.yCoordinates;
	}


	/**
	 * Gets the Z-coordinate values of the points of this data series, in double-type.
	 *
	 * @return The Z-coordinate values.
	 */
	@Override
	public synchronized double[][] getZCoordinates() {
		return this.zCoordinates;
	}


	/**
	 * Gets the visibilities of the points of this data series.
	 *
	 * @return The array storing visibilities of the points of this data series.
	 */
	@Override
	public synchronized boolean[][] getVisibilities() {
		return this.visibilities;
	}


	/**
	 * Checks whether the minimum value of the X-coordinate values exists.
	 *
	 * @return Returns true if the minimum value exists.
	 */
	@Override
	public synchronized boolean hasXMin() {
		return (this.xMin != null);
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
		return (this.xMax != null);
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
	 * Checks whether the minimum value of the Y-coordinate values exists.
	 *
	 * @return Returns true if the minimum value exists.
	 */
	@Override
	public synchronized boolean hasYMin() {
		return (this.yMin != null);
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
		return (this.yMax != null);
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
	 * Checks whether the minimum value of the Z-coordinate values exists.
	 *
	 * @return Returns true if the minimum value exists.
	 */
	@Override
	public synchronized boolean hasZMin() {
		return (this.zMin != null);
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
		return (this.zMax != null);
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
