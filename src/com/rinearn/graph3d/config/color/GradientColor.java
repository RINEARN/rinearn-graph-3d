package com.rinearn.graph3d.config.color;

import java.awt.Color;
import java.math.BigDecimal;


// !!! NOTE !!!
//
// 合成方法を加算とか乗算とか enum で指定できたほうがいいかも。
// まあそもそも加算でも乗算でもなさそうだけど。
// -> いや、加算と乗算の組み合わせで大抵いける気がする。一方のみで考えるとできない例は出てくるが。
//
//    軸Aと軸Bは加算、軸Bと軸Cは乗算、みたいなパターンはどうする？
//    -> そんな場合はもう除外しても良い気がするが。しかし透明度だけ乗算、みたいな場合はあるかな。
//       実際、赤系統と青系統の2軸加算グラデに、透明度を乗算したい、みたいな場合は実際ありそう。
//
// いや、そもそも思ったんだけど、アルファチャンネルでのグラデって、
// 透明色との普通のグラデとは処理が違う気がする。現行版でどう実装してたっけか？
// 透明とのグラデって、「RGB成分が透明と不透明側と同じで、アルファが0と255との間のグラデ」と等価では？ あれは処理的に。
// なので黒ベースや白ベースのα=255透明色は、普通に任意の色との間でグラデかけても、綺麗な透明グラデにはならんでしょ。
// たぶん。直感的に。
// -> 確かに、仮にαをRGBと等価に扱うとすれば、透明側のRGBの違いに結果が依存しないはずはないので、違いが出る事は確実なはず。
//
// ↑を受けて、それならどう設定として扱うべきか？
//   1次元なら、例えば青と透明の2色グラデなら、青不透明から青透明のグラデに設定すれば済むので、セット方法は工夫しなくてもいい。
//   しかし、2次元のうちの1方向を透明度とするなら、様々な色相に対して透明を作用させる必要があるので、
//   透明側にダイレクトに色をセットする方法は向かない。
//
// -> ならやっぱモードで「色配列の補完グラデ」とか「透明へのグラデ」とか選べるべきか。
//    -> なんかモードや階層だらけになるが… ColorConfig の GRADIENT モードの特定Axisの「透明グラデモード」とか、ちょっと深すぎかも。
//
// -> 上記の透明へのグラデ、透明軸を「白不透明～白透明」へのグラデにして、色彩側の軸と乗算すれば等価になる気がする。直感的に。
//    -> 等価になるなら色配列による設定で兼ねられるので、別途モードを設ける必要はない。階層数増やしたくないし。本当に兼ねられるなら。
//
//    -> とりあえず上記もろもろを踏まえて、ブレンドモード（乗算 or 加算）ベースで実装した。
//       またミキサーあたり実装して動くようになったくらいのタイミングで振り返って要検討/検証。この仕様でいけそうかどうか。
//
//    -> 上記の「白不透明～白透明へのグラデ」を「色彩グラデ」に乗算するやつ、実際にやってみたけど期待通りになった。
//       X軸方向に色彩変化、Y軸方向に透明度変化みたいにちゃんとなる。のでこれでいい。透明グラデを特別扱いするモードとかは要らない。
//       要は「透明変化マスクは白色のαのみ変化させたグラデを乗算すべし」ってのをどっかに書いておけばいい。使い方の一つとして。
//
//!!! NOTE !!!

// !!! NOTE2 !!!
//
// getMinimumBoundaryCoordinate とかの命名、むしろ getBoundaryCoordinateMinimum とかでは？ getRangeMinimum とかとの対応的に。
//
// !!! NOTE2 !!!

// !!! NOTE3 !!!
//
// * ColorConfig 内部クラスから color パッケージ内の単体クラスに昇格したことだし、
//   AxisGradientColor も1つのクラスに分割して、このクラスはそれを部品として使う、みたいに改めるべき？
//
// !!! NOTE3 !!!

/**
 * The class representing a color gradient.
 *
 * This class is mainly used in ColorConfiguration.
 */
public final class GradientColor {

	/** Stores an one-dimensional gradient color for each axis. */
	private AxisGradientColor[] axisGradientColors = { new AxisGradientColor() };

	/** The background color on which the gradients of all axes are blended. */
	private Color backgroundColor = new Color(0, 0, 0, 0); // Clear black


	/**
	 * Gets the total number of the axes (dimensions) of this gradient color.
	 *
	 * @return The total number of the axes.
	 */
	public synchronized int getAxisCount() {
		return this.axisGradientColors.length;
	}

	/**
	 * Sets each axis's gradient color, by an array storing an one-dimensional gradient color for each axis.
	 *
	 * @param axisGradientColors The array storing an one-dimensional gradient color for each axis.
	 */
	public synchronized void setAxisGradientColors(AxisGradientColor[] axisGradientColors) {
		this.axisGradientColors = axisGradientColors;
	}

	/**
	 * Gets each axis's color gradient color as an array.
	 *
	 * @param axisGradientColors The array storing an one-dimensional gradient color for each axis.
	 */
	public synchronized AxisGradientColor[] getAxisGradientColors() {
		return this.axisGradientColors;
	}

	/**
	 * Sets the background color, on which the gradient colors of all axes are blended.
	 *
	 * @param backgroundColor The background color.
	 */
	public synchronized void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * Gets the background color, on which the gradient colors of all axes are blended.
	 *
	 * @return The background color.
	 */
	public synchronized Color getBackgroundColor() {
		return this.backgroundColor;
	}

	/**
	 * Validates correctness and consistency of parameters stored in this instance.
	 *
	 * This method is called when a color configuration is specified to RinearnGraph3D or its renderer.
	 * If no issue is detected, nothing occurs.
	 * If any issue is detected, throws IllegalStateException.
	 *
	 * @throws IllegalStateException Thrown when incorrect or inconsistent settings are detected.
	 */
	public synchronized void validate() throws IllegalStateException {

		// Validate axes's gradient colors.
		if (this.axisGradientColors == null) {
			throw new IllegalStateException("The axes's gradient colors are null");
		} else {
			for (GradientColor.AxisGradientColor axisGradientColor: this.axisGradientColors) {
				if (axisGradientColor == null) {
					throw new IllegalStateException("There is a null element in the axes's color gradients.");
				}
				axisGradientColor.validate();
			}
		}

		// Validate the background color.
		if (this.backgroundColor == null) {
			throw new IllegalStateException("The background color is null.");
		}
	}


	/**
	 * The enum for specifying a gradient's axis.
	 */
	public static enum Axis {

		/** Represents the gradient for the direction of X axis. */
		X,

		/** Represents the gradient for the direction of Y axis. */
		Y,

		/** Represents the gradient for the direction of Z axis. */
		Z,

		/** Represents the gradient by coordinate values of 4-th column in data files. */
		COLUMN_4; // Note: Should rename to more abstract one?
	}


	/**
	 * The enum for specifying a blend mode of multiple gradient colors.
	 */
	public static enum BlendMode {

		/** Adds a gradient's color components (R/G/B/A) to the corresponding color component of the background. */
		ADDITION,

		/** Multiplies a gradient's color components (R/G/B/A) to the corresponding color component of the background. */
		MULTIPLICATION;
	}

	/**
	 * The enum for specifying a interpolation mode, which determines colors between boundary points.
	 */
	public static enum InterpolationMode {

		/** Interpolates colors between each pair of boundary points by the linear interpolation. */
		LINEAR, // For continuous gradation

		/** Interpolates colors between each pair of boundary points by a flat step (step function). */
		STEP;
	}

	// Should rename to "BoundaryLayoutMode" or something else?
	/**
	 * The enum for specifying a boundary mode, which determines locations of boundary points.
	 */
	public static enum BoundaryMode {

		/** Divides the range from min to max coordinates equally by boundary points. */
		EQUAL_DIVISION,

		/** Specify coordinate values of the boundary points manually. */
		MANUAL;
	}


	/**
	 * The class representing one-dimensional gradient color,
	 * composing one axis of (may be multiple dimensional) gradient color.
	 */
	public static final class AxisGradientColor {

		// Note: Should rename "boundary..." to "midPoint..." ?
		//       -> Probably No. "mid point" is used by some interpolation algorithms in the different meaning, so it may lead confusing.

		// boundaryCount みたいなやつがいるが…命名どうする？ -> ちょうど色数に一致するからそれでいい。

		/** The dimension axis of this gradient. */
		private volatile Axis axis = Axis.Z;

		/** Stores a blend mode, for blending this gradient to the background color or an other gradient. */
		private BlendMode blendMode = BlendMode.ADDITION;

		/** The boundary mode, which determines locations of boundary points. */
		private volatile BoundaryMode boundaryMode = BoundaryMode.EQUAL_DIVISION;

		/** The interpolation mode, which determines colors between boundary points. */
		private volatile InterpolationMode interpolationMode = InterpolationMode.LINEAR;

		/** The colors at the boundary points of this gradient. */
		private volatile Color[] boundaryColors = {
				Color.BLUE,
				Color.CYAN,
				Color.GREEN,
				Color.YELLOW,
				Color.RED
		};

		/** The coordinate values at the boundary points of this gradient, in MANUAL mode. */
		private volatile BigDecimal[] boundaryCoordinates = null;

		/** The minimum coordinate value in boundary points, in EQUAL_DIVISION mode. */
		private volatile BigDecimal minBoundaryCoordinate = BigDecimal.ONE.negate();

		/** The maximum coordinate value in boundary points, in EQUAL_DIVISION mode. */
		private volatile BigDecimal maxBoundaryCoordinate = BigDecimal.ONE;

		/** The flag to detect the min/max coords of boundary points automatically from the data, in EQUAL_DIVISION mode. */
		private volatile boolean autoBoundaryRangingEnabled = true;


		/**
		 * Sets the dimension axis of this gradient.
		 *
		 * @param axis The dimension axis of this gradient.
		 */
		public synchronized void setAxis(Axis axis) {
			this.axis = axis;
		}

		/**
		 * Gets the dimension axis of this gradient.
		 *
		 * @return The dimension axis of this gradient.
		 */
		public synchronized Axis getAxis() {
			return this.axis;
		}

		/**
		 * Sets the blend mode, for blending this gradient to the background color or an other gradient.
		 *
		 * @param blendMode The blend mode.
		 */
		public synchronized void setBlendMode(BlendMode blendMode) {
			this.blendMode = blendMode;
		}

		/**
		 * Gets the blend mode, for blending this gradient to the background color or an other gradient.
		 *
		 * @return The blend mode.
		 */
		public synchronized BlendMode getBlendMode() {
			return this.blendMode;
		}

		/**
		 * Sets the interpolation mode, which determines colors between boundary points.
		 *
		 * @param interpolationMode The interpolation mode.
		 */
		public synchronized void setInterpolationMode(InterpolationMode interpolationMode) {
			this.interpolationMode = interpolationMode;
		}

		/**
		 * Gets the interpolation mode, which determines colors between boundary points.
		 *
		 * @return The interpolation mode.
		 */
		public synchronized InterpolationMode getInterpolationMode() {
			return this.interpolationMode;
		}

		/**
		 * Sets the boundary mode, which determines locations of boundary points.
		 *
		 * @param boundaryMode The boundary mode.
		 */
		public synchronized void setBoundaryMode(BoundaryMode boundaryMode) {
			this.boundaryMode = boundaryMode;
		}

		/**
		 * Gets the boundary mode, which determines locations of boundary points.
		 *
		 * @return The boundary mode.
		 */
		public synchronized BoundaryMode getBoundaryMode() {
			return this.boundaryMode;
		}

		/**
		 * Gets the total number of the boundary points of this gradient.
		 *
		 * @return The total number of the boundary points of this gradient.
		 */
		public synchronized int getBoundaryCount() {
			return this.boundaryColors.length;
		}

		/**
		 * Sets the colors at the boundary points of this gradient.
		 *
		 * @param boundaryColors The colors at the boundary points of this gradient.
		 */
		public synchronized void setBoundaryColors(Color[] boundaryColors) {
			this.boundaryColors = boundaryColors;
		}

		/**
		 * Gets the colors at the boundary points of this gradient.
		 *
		 * @return The colors at the boundary points of this gradient.
		 */
		public synchronized Color[] getBoundaryColors() {
			return this.boundaryColors;
		}

		/**
		 * Sets the coordinate values of the boundary points of this gradient.
		 *
		 * @param boundaryCoordinates The coordinate values of the boundary points of this gradient.
		 */
		public synchronized void setBoundaryCoordinates(BigDecimal[] boundaryCoordinates) {
			this.boundaryCoordinates = boundaryCoordinates;
		}

		/**
		 * Gets the coordinate values of the boundary points of this gradient.
		 *
		 * @return The coordinate values of the boundary points of this gradient.
		 */
		public synchronized BigDecimal[] getBoundaryCoordinates() {
			return this.boundaryCoordinates;
		}

		/**
		 * Sets the minimum coordinate value of the boundary points, in EQUAL_DIVISION mode.
		 *
		 * @param minBoundaryCoordinate The minimum coordinate value of the boundary points.
		 */
		public synchronized void setMinimumBoundaryCoordinate(BigDecimal minBoundaryCoordinate) {
			this.minBoundaryCoordinate = minBoundaryCoordinate;
		}

		/**
		 * Gets the minimum coordinate value of the boundary points, in EQUAL_DIVISION mode.
		 *
		 * @return The minimum coordinate value of the boundary points.
		 */
		public synchronized BigDecimal getMinimumBoundaryCoordinate() {
			return this.minBoundaryCoordinate;
		}

		/**
		 * Sets the maximum coordinate value of the boundary points, in EQUAL_DIVISION mode.
		 *
		 * @param maxBoundaryCoordinate The maximum coordinate value of the boundary points.
		 */
		public synchronized void setMaximumBoundaryCoordinate(BigDecimal maxBoundaryCoordinate) {
			this.maxBoundaryCoordinate = maxBoundaryCoordinate;
		}

		/**
		 * Gets the maximum coordinate value of the boundary points, in EQUAL_DIVISION mode.
		 *
		 * @return The maximum coordinate value of the boundary points.
		 */
		public synchronized BigDecimal getMaximumBoundaryCoordinate() {
			return this.maxBoundaryCoordinate;
		}

		/**
		 * Sets whether detect the min/max coordinates of the boundary points automatically from the data, in EQUAL_DIVISION mode.
		 *
		 * @param autoRangingEnabled Specify true for detecting the min/max coordinates automatically.
		 */
		public synchronized void setAutoBoundaryRangingEnabled(boolean autoBoundaryRangingEnabled) {
			this.autoBoundaryRangingEnabled = autoBoundaryRangingEnabled;
		}

		/**
		 * Gets whether the auto boundary ranging feature,
		 * which detects the min/max coordinates of the boundary points automatically from the data in EQUAL_DIVISION mode,
		 * is enabled.
		 *
		 * @return Returns true if the auto boundary ranging feature is enabled.
		 */
		public synchronized boolean isAutoBoundaryRangingEnabled() {
			return this.autoBoundaryRangingEnabled;
		}

		/**
		 * Validates correctness and consistency of parameters stored in this instance.
		 *
		 * This method is called when a color configuration is specified to RinearnGraph3D or its renderer.
		 * If no issue is detected, nothing occurs.
		 * If any issue is detected, throws IllegalStateException.
		 *
		 * @throws IllegalStateException Thrown when incorrect or inconsistent settings are detected.
		 */
		public synchronized void validate() throws IllegalStateException {

			// Validate the axis and the modes.
			if (this.axis == null) {
				throw new IllegalStateException("The axis is null.");
			}
			if (this.blendMode == null) {
				throw new IllegalStateException("The blend mode is null.");
			}
			if (this.boundaryMode == null) {
				throw new IllegalStateException("The boundary mode is null.");
			}
			if (this.interpolationMode == null) {
				throw new IllegalStateException("The interpolation mode is null.");
			}

			// Validate boundary colors.
			if (this.boundaryColors == null) {
				throw new IllegalStateException("The boundary colors are null.");
			} else {
				for (Color color: this.boundaryColors) {
					if (color == null) {
						throw new IllegalStateException("There is a null element in boundary colors.");
					}
				}
			}

			// Validate boundary colors.
			if (this.boundaryMode == BoundaryMode.MANUAL) {
				if (this.boundaryColors == null) {
					throw new IllegalStateException("The boundary coordinates is null. In MANUAL mode, they are mandatory.");
				} else {
					for (BigDecimal coord: this.boundaryCoordinates) {
						if (coord == null) {
							throw new IllegalStateException("There is a null element in boundary coordinates.");
						}
					}
					if (this.boundaryCoordinates.length != this.boundaryColors.length) {
						throw new IllegalStateException(
							"The total number of the boundary coordinates does not match with the number of the boundary colors."
						);
					}
				}
			}

			// Validate min/max coordinates.
			if (this.boundaryMode == BoundaryMode.EQUAL_DIVISION && !this.autoBoundaryRangingEnabled) {
				if (this.minBoundaryCoordinate == null) {
					throw new IllegalStateException(
						"The minimum boundary coordinate is null. In EQUAL_DIVISION mode, it is mandatory when the auto-ranging is disabled."
					);
				}
				if (this.maxBoundaryCoordinate == null) {
					throw new IllegalStateException(
						"The maximum boundary coordinate is null. In EQUAL_DIVISION mode, it is mandatory when the auto-ranging is disabled."
					);
				}
			}
		}
	}
}
