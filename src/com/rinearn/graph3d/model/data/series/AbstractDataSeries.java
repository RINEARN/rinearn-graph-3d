package com.rinearn.graph3d.model.data.series;

import java.math.BigDecimal;

/*
[Inheritance tree]

    AbstractDataSeries < This Class
      |
      +- ArrayDataSeries
      |
      +- MathDataSeries
          |
          +- ZxyMathDataSeries
          |
          +- XtYtZtMathDataSeries
 */


public abstract class AbstractDataSeries {

	// !!!!! NOTE & TODO !!!!!
	// ・double型ではなくBigDecimal型で座標値を持っている系列はどう扱うべき？
	//   -> とりあえず double ベースである程度実装してみて、
	//      上から下まで処理がつながって見通しが付いたあたりで、拡張するか書き直すか考える。
	//      最初から分派させると工程が難解になり過ぎるし、たぶん結果も無駄に冗長気味になりそうなので。
	//
	// ・宣言するのすっかり忘れてたけど各点ごとの可視性の配列要るでしょ。

	/**
	 * Gets the X-coordinate values of the points of this data series.
	 *
	 * @return The X-coordinate values.
	 */
	public abstract double[][] getXCoordinates();


	/**
	 * Gets the Y-coordinate values of the points of this data series.
	 *
	 * @return The Y-coordinate values.
	 */
	public abstract double[][] getYCoordinates();


	/**
	 * Gets the Z-coordinate values of the points of this data series.
	 *
	 * @return The Z-coordinate values.
	 */
	public abstract double[][] getZCoordinates();


	/**
	 * Gets the array storing visibilities of the points of this data series.
	 */
	public abstract boolean[][] getVisibilities();


	/**
	 * Checks whether the minimum value of the X-coordinate values exists.
	 *
	 * @return Returns true if the minimum value exists.
	 */
	public abstract boolean hasXMin();

	/**
	 * Gets the minimum value of the X-coordinate values.
	 *
	 * @return The minimum value of the X-coordinate values.
	 */
	public abstract BigDecimal getXMin();

	/**
	 * Checks whether the maximum value of the X-coordinate values exists.
	 *
	 * @return Returns true if the maximum value exists.
	 */
	public abstract boolean hasXMax();

	/**
	 * Gets the maximum value of the X-coordinate values.
	 *
	 * @return The maximum value of the X-coordinate values.
	 */
	public abstract BigDecimal getXMax();


	/**
	 * Checks whether the minimum value of the Y-coordinate values exists.
	 *
	 * @return Returns true if the minimum value exists.
	 */
	public abstract boolean hasYMin();

	/**
	 * Gets the minimum value of the Y-coordinate values.
	 *
	 * @return The minimum value of the Y-coordinate values.
	 */
	public abstract BigDecimal getYMin();

	/**
	 * Checks whether the maximum value of the Y-coordinate values exists.
	 *
	 * @return Returns true if the maximum value exists.
	 */
	public abstract boolean hasYMax();

	/**
	 * Gets the maximum value of the Y-coordinate values.
	 *
	 * @return The maximum value of the Y-coordinate values.
	 */
	public abstract BigDecimal getYMax();


	/**
	 * Checks whether the minimum value of the Z-coordinate values exists.
	 *
	 * @return Returns true if the minimum value exists.
	 */
	public abstract boolean hasZMin();

	/**
	 * Gets the minimum value of the Z-coordinate values.
	 *
	 * @return The minimum value of the Z-coordinate values.
	 */
	public abstract BigDecimal getZMin();

	/**
	 * Checks whether the maximum value of the Z-coordinate values exists.
	 *
	 * @return Returns true if the maximum value exists.
	 */
	public abstract boolean hasZMax();

	/**
	 * Gets the maximum value of the Z-coordinate values.
	 *
	 * @return The maximum value of the Z-coordinate values.
	 */
	public abstract BigDecimal getZMax();

}
