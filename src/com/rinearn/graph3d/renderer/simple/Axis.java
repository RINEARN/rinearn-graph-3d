package com.rinearn.graph3d.renderer.simple;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;


// !!! NOTE !!!
// getRangeMin() とかの命名は、他の config 類との命名と一貫性に欠ける。getMinimum とかにすべき？
// -> このクラス内に、他の種類の minimum 量が将来的に追加される可能性を考慮すると getRangeMinimum の方が良い？
//    -> setRange があるのでそれとの対応がいい: getRnageMinimum/Maximum
//    -> getRangeMinimumCoordinate は？ 長いか
//!!! NOTE !!!

//!!! NOTE2 !!!
//
// なんか config コンテナベースの方向性に収束しつつあるので、この Axis の中に色々と情報保持してる構造が微妙になってきた気がする。
// どっち参照すればいいの？ みたいになる。なので内容の同期が必要になるんだけど、そもそも常に config コンテナ参照すりゃいいのでは。
//
// つまりこの Axis ってもう無くしてもいいのでは？ 初期の土台作りに必要だった足場みたいなもんで。
// そもそも Axis ってのが初見だとなんかイメージしづらい概念だし、あっても分かりやすくなるもんでもなさそうな。
//
// -> そもそも最初はなんでこうしたんだっけ。Axisというくくりでまとめるのが凄くいい、みたいに思ったはずだけど。
//
//    -> Label とか Scale とかのカテゴリーでくくるよりも Axis でまとめた方がよさそう、みたいになったと思うが。
//       つまり当時は逆に感じた。そういう観点があったはずで。
//
//    -> 最初はとりあえず散らばっていたものをすっきりまとめる過程で↑に思ったが、
//       しかしレンダラーのステートを全部コンテナに整理して上層から降って来るようにしようとすると、
//       Axis関連だけでは済まなくなるし、それで乖離していったのかと。
//       つまり、階層を上に昇れば設定画面とか設定ファイルとかの粒度との対応も想定したコンテナ群が必要で、
//       その階層だと Axis というくくりの横断的すぎる感じが逆にまずかった。整理において。
//
//       つまりAxisは概念として横断的なので、その横断レンジでカバーしきれる情報群に注目するとすっきりまとまるけど、
//       カバーしきれない情報群に対しては中途半端なくくりになってしまう。
//       そこの境界の外側をカバーするコンテナも、その中途半端な概念形の補集合みたいになって、やっぱり中途半端になる。
//       なので概念形がなんというかスクエアな感じの方が収まりがいい。実際の収納のように。
//       それで今みたいなコンテナの切り方に落ち着いてきた。という感じか。
//       なんとなくじわじわ変形してきたのでメモ残ってないが
//
// 削るか整理する方向で検討
//
//!!! NOTE2 !!!


/**
 * A class storing/handling informations related to an axis.
 * (e.g.: Maximum and minimum values of the range of the axis, and so on.)
 */
public final class Axis {

	/** The default precision of the result value of scaleCoordinate(BigDecimal) method. */
	private static final int DEFAULT_SCALED_SPACE_PRECISION = 20;

	/** Stores the minimum value of the range of this axis. */
	private volatile BigDecimal rangeMin = BigDecimal.ONE.negate(); // -1.0

	/** Stores the maximum value of the range of this axis. */
	private volatile BigDecimal rangeMax = BigDecimal.ONE; // 1.0

	/** Stores the minimum value of the range of this axis, as the double-type value. */
	private volatile double rangeMinDoubleValue = -1.0;

	/** Stores the maximum value of the range of this axis, as the double-type value. */
	private volatile double rangeMaxDoubleValue = 1.0;

	/**
	 * The margin for comparing a double-type coordinate value and the rangeMinDoubleValue,
	 * considering tiny numerical errors of binary floating-point numbers.
	 * This value does not take negative values (the value must be positive or zero).
	 */
	private volatile double rangeMinDoubleMargin = MARGINE_RATIO;

	/**
	 * The margin for comparing a double-type coordinate value and the rangeMaxDoubleValue,
	 * considering tiny numerical errors of binary floating-point numbers.
	 * This value does not take negative values (the value must be positive or zero).
	 */
	private volatile double rangeMaxDoubleMargin = MARGINE_RATIO;

	/** The ratio of rangeMinDoubleMargin (or rangeMaxDoubleMargin) to rangeMinDoubleValue (or rangeMaxDoubleValue). */
	private static final double MARGINE_RATIO = 1.0E-12;

	/** Stores the coordinate values of the ticks of this axis. */
	//private volatile BigDecimal[] tickCoordinates;

	/** Stores the displayed texts of the ticks of this axis. */
	//private volatile String[] tickLabels;

	/** Stores the axis labels. */
	//private volatile String[] axisLabels;


	/**
	 * Sets the range of this axis.
	 * 
	 * @param min The minimum value of the range of this axis.
	 * @param max The maximum value of the range of this axis.
	 */
	public synchronized void setRange(BigDecimal min, BigDecimal max) {

		// Update precise values of the range.
		this.rangeMin = min;
		this.rangeMax = max;

		// Update values which will be used for double-double comparisons.
		this.rangeMinDoubleValue = min.doubleValue();
		this.rangeMaxDoubleValue = max.doubleValue();

		// Update margins of double-double comparisons.
		this.rangeMinDoubleMargin = Math.abs(this.rangeMinDoubleValue * MARGINE_RATIO);
		this.rangeMaxDoubleMargin = Math.abs(this.rangeMaxDoubleValue * MARGINE_RATIO);
	}


	/**
	 * Gets the minimum value of the range of this axis.
	 * 
	 * @return The minimum value of the range of this axis.
	 */
	public synchronized BigDecimal getRangeMin() {
		return this.rangeMin;
	}


	/**
	 * Gets the maximum value of the range of this axis.
	 * 
	 * @return The maximum value of the range of this axis.
	 */
	public synchronized BigDecimal getRangeMax() {
		return this.rangeMax;
	}


	/**
	 * Sets the axis labels.
	 * 
	 * @param axisLabels the axis labels.
	 */
	/*
	public synchronized void setAxisLabels(String[] axisLabels) {
		this.axisLabels = axisLabels;
	}
	*/

	/**
	 * Gets the axis labels.
	 * 
	 * @return the axis labels.
	 */
	/*
	public synchronized String[] getAxisLabels() {
		return this.axisLabels;
	}
	*/


	/**
	 * Set the coordinate values (positions) and the labels (displayed values) of the ticks of this axis.
	 * 
	 * @param tickCoordinates The coordinate values of the ticks.
	 * @param tickLabels The labels of the ticks
	 */
	/*
	public synchronized void setTicks(BigDecimal[] tickCoordinates, String[] tickLabels) {
		this.tickCoordinates = tickCoordinates;
		this.tickLabels = tickLabels;
	}
	*/


	/**
	 * Gets the coordinate values (positions) of the ticks of this axis.
	 * 
	 * @return The coordinate values of this ticks.
	 */
	/*
	public synchronized BigDecimal[] getTickCoordinates() {
		return this.tickCoordinates;
	}
	*/


	/**
	 * Gets the labels (displayed values) of the ticks of this axis.
	 * 
	 * @return The labels of the ticks.
	 */
	/*
	public synchronized String[] getTickLabels() {
		return this.tickLabels;
	}
	*/


	/**
	 * Determines whether the specified coordinate value is in the range of this axis.
	 * 
	 * If true is specified to considersMargins,
	 * this method regards a double-type coordinate value is in the range, 
	 * if it is greater than or equals to rangeMinDoubleValue - rangeMinDoubleMargin, 
	 * and is less than or equals to rangneMaxDoubleValue + rangeMaxDoubleMargin.
	 * This is the behavior to address tiny errors contained in double-type values.
	 * 
	 * @param coordinate The coordinate value to be determined.
	 * @param considersMargins Specify true if you enable margins to address tiny errors of coordinate values.
	 * @return Returns true if the specified coordinate value is in the range.
	 */
	public synchronized boolean containsCoordinate(double coordinate, boolean considersMargins) {

		// If considersMargins is true, use margins set to the fields. Otherwise use 0 (= no margins).
		double minMargin = considersMargins ? this.rangeMinDoubleMargin : 0.0;
		double maxMargin = considersMargins ? this.rangeMaxDoubleMargin : 0.0;

		// Compare the specified coordinate value to the min/max values (with margins, if required).
		boolean greaterThanOrEqualsToMin = this.rangeMinDoubleValue - minMargin <= coordinate;
		boolean lessThanOrEqualsToMax = coordinate <= this.rangeMaxDoubleValue + maxMargin;
		boolean isInRange = greaterThanOrEqualsToMin && lessThanOrEqualsToMax;
		return isInRange;
	}


	/**
	 * Scales the specified coordinate values, into the coordinate values in the "scaled space".
	 * 
	 * In the scaled space,
	 * the maximum value of the range of the axis is mapped to 1.0,
	 * and the minimum value is mapped to -1.0.
	 * 
	 * @param rawCoordinateValue The coordinate value to be scaled.
	 * @return The scaled coordinate value.
	 */
	public synchronized double scaleCoordinate(double rawCoordinate) {
		
		// Firstly, scale into the range [0.0, 1.0].
		double axisLength = this.rangeMaxDoubleValue - this.rangeMinDoubleValue;
		double scaledInto01 = (rawCoordinate - this.rangeMinDoubleValue) / axisLength;

		// Then, scale into the range [-1.0, 1.0].
		double scaledInto11 = scaledInto01 * 2.0 - 1.0;
		return scaledInto11;
	}


	/**
	 * Scales the specified coordinate values, into the coordinate values in the "scaled space".
	 * 
	 * In the scaled space,
	 * the maximum value of the range of the axis is mapped to 1.0,
	 * and the minimum value is mapped to -1.0.
	 * 
	 * @param rawCoordinate The coordinate value to be scaled.
	 * @return The scaled coordinate value.
	 */
	public synchronized BigDecimal scaleCoordinate(BigDecimal rawCoordinate) {
		return this.scaleCoordinate(rawCoordinate, DEFAULT_SCALED_SPACE_PRECISION);
	}


	/**
	 * Scales the specified coordinate values, into the coordinate values in the "scaled space".
	 * 
	 * In the scaled space,
	 * the maximum value of the range of the axis is mapped to 1.0,
	 * and the minimum value is mapped to -1.0.
	 * 
	 * @param rawCoordinate The coordinate value to be scaled.
	 * @param precision The precision of the result.
	 * @return The scaled coordinate value.
	 */
	public synchronized BigDecimal scaleCoordinate(BigDecimal rawCoordinate, int precision) {

		// Create the math context for specifying the rounding behavior.
		MathContext precisionContext = new MathContext(precision, RoundingMode.HALF_EVEN);
		MathContext redundantPrecisionContext = new MathContext(precision * 3, RoundingMode.HALF_EVEN);

		// Firstly, scale into the range [0.0, 1.0].
		BigDecimal axisLength = this.rangeMax.subtract(this.rangeMin);
		BigDecimal scaledInto01 = rawCoordinate.subtract(this.rangeMin).divide(axisLength, redundantPrecisionContext);

		// Then, scale into the range [-1.0, 1.0].
		BigDecimal scaledInto11 = scaledInto01.multiply(new BigDecimal(2)).subtract(BigDecimal.ONE);
		scaledInto11 = scaledInto11.add(BigDecimal.ZERO, precisionContext);
		return scaledInto11;
	}
}
