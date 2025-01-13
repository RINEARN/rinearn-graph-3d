package com.rinearn.graph3d.model.data.series;

import org.vcssl.nano.VnanoException;

/*
[Inheritance tree]

    AbstractDataSeries
      |
      +- ArrayDataSeries
      |
      +- MathDataSeries < This Class
          |
          +- ZxyMathDataSeries
          |
          +- XtYtZtMathDataSeries
 */


public abstract class MathDataSeries extends AbstractDataSeries {

	// !!! Don't move fields and their getter methods here from the subclasses to avoid code duplication.
	// !!! Firstly we did it, but then we felt that it is not readable, so moved them to subclasses.
	// !!! We think it is not readable that the abstract class has many implementational code.
    // !!! It should be a simple, "abstract concept of subclasses".


	/**
	 * Returns a single-line name of this data series, to be displayed on UI.
	 */
	public abstract String getDisplayName();


	/**
	 * Computes coordinate values from math expression(s) of this data series.
	 *
	 * The computed coordinate values will be stored into the fields: xCoordinates, yCoordinates and zCoordinates.
	 * This abstract method is implemented by subclasses: ZxyMathDataSeries and XtYtZtMathDataSeries.
	 *
	 * @throws VnanoException Thrown when any (typically syntactic) error has been detected for calculating math expression(s).
	 */
	public abstract void computeCoordinates() throws VnanoException;

}
