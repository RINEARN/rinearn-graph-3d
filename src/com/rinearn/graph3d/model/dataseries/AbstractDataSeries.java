package com.rinearn.graph3d.model.dataseries;

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
}
