package com.rinearn.graph3d.config;

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
//!!! NOTE !!!


/**
 * The class representing a color gradient.
 * 
 * This class is mainly used in ColorConfiguration.
 */
public final class ColorGradient {

	/** The total number of the axes (dimensions) of this gradient. */
	private volatile int axisCount = 1;

	/** Stores an one-dimensional gradient for each axis. */
	private AxisColorGradient[] axisColorGradients = { new AxisColorGradient() };

	/** Stores a blend mode for each axis. */
	private BlendMode[] axisBlendModes = { BlendMode.ADDITION };
	// Note: About the above: the blend mode Should be contained in AxisColorGradient as a field?

	/** The background color on which the gradients of all axes are blended. */
	private Color backgroundColor = new Color(0, 0, 0, 0); // Clear black


	/**
	 * Sets the total number of the axes (dimensions) of this gradient.
	 * 
	 * @param axisCount The total number of the axes.
	 */
	public synchronized void setAxisCount(int axisCount) {
		this.axisCount = axisCount;
	}

	/**
	 * Gets the total number of the axes (dimensions) of this gradient.
	 * 
	 * @return The total number of the axes.
	 */
	public synchronized int getAxisCount() {
		return this.axisCount;
	}

	/**
	 * Sets each axis's color gradient, by an array storing an one-dimensional gradient for each axis.
	 * 
	 * @param axisColorGradients The array storing an one-dimensional gradient for each axis.
	 */
	public synchronized void setAxisColorGradients(AxisColorGradient[] axisColorGradients) {
		if (axisColorGradients.length != this.axisCount) {
			throw new IllegalArgumentException(
					"The length of the argument \"axisColorGradients\" must be the same as the value of axisCount."
			);
		}
		this.axisColorGradients = axisColorGradients;
	}

	/**
	 * Gets each axis's color gradient as an array.
	 * 
	 * @param axisColorGradients The array storing an one-dimensional gradient for each axis.
	 */
	public synchronized AxisColorGradient[] getAxisColorGradients() {
		return this.axisColorGradients;
	}

	/**
	 * Sets each axis's blend mode, by an array storing a blend mode for each axis.
	 * 
	 * @param axisBlendModes The array storing a blend mode for each axis.
	 */
	public synchronized void setAxisBlendModes(BlendMode[] axisBlendModes) {
		this.axisBlendModes = axisBlendModes;
	}

	/**
	 * Gets each axis's blend mode as an array.
	 * 
	 * @return The array storing a blend mode for each axis.
	 */
	public synchronized BlendMode[] getAxisBlendModes() {
		return this.axisBlendModes;
	}

	/**
	 * Sets the background color, on which the gradients of all axes are blended.
	 * 
	 * @param backgroundColor The background color.
	 */
	public synchronized void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * Gets the background color, on which the gradients of all axes are blended.
	 * 
	 * @return The background color.
	 */
	public synchronized Color getBackgroundColor() {
		return this.backgroundColor;
	}


	/**
	 * The enum for specifying a gradient axis.
	 */
	public static enum GradientAxis {

		/** Represents the gradient for the direction of X axis. */
		X,

		/** Represents the gradient for the direction of Y axis. */
		Y,

		/** Represents the gradient for the direction of Z axis. */
		Z,

		/** Represents the gradient by coordinate values of 4-th column in data files. */
		COLUMN_4;
	}


	/**
	 * The enum for specifying a blend mode of multiple color gradients.
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
	 * The class representing one-dimensional color gradient,
	 * composing one axis of (may be multiple dimensional) color gradient.
	 */
	public static final class AxisColorGradient {

		// Note: Should rename "boundary..." to "midPoint..." ?
		//       -> Probably No. "mid point" is used by some interpolation algorithms in the different meaning, so it may lead confusing.

		// boundaryCount みたいなやつがいるが…命名どうする？ -> ちょうど色数に一致するからそれでいい。

		/** The dimension axis of this gradient. */
		private volatile GradientAxis axis = GradientAxis.Z;

		/** The boundary mode, which determines locations of boundary points. */
		private volatile BoundaryMode boundaryMode = BoundaryMode.EQUAL_DIVISION;

		/** The interpolation mode, which determines colors between boundary points. */
		private volatile InterpolationMode interpolationMode = InterpolationMode.LINEAR;

		/** The total number of the boudnary points of this gradient. */
		private volatile int boundaryCount = 5;
		// Note: The sizes of "boundaryColors" and "boundaryCoordinates" must match with this value.

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
		public synchronized void setAxis(GradientAxis axis) {
			this.axis = axis;
		}

		/**
		 * Gets the dimension axis of this gradient.
		 * 
		 * @return The dimension axis of this gradient.
		 */
		public synchronized GradientAxis getAxis() {
			return this.axis;
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
		 * Sets the total number of the boundary points of this gradient.
		 * 
		 * @param boundaryCount The total number of the boundary points of this gradient.
		 */
		public synchronized void setBoundaryCount(int boundaryCount) {
			this.boundaryCount = boundaryCount;
		}

		/**
		 * Gets the total number of the boundary points of this gradient.
		 * 
		 * @return The total number of the boundary points of this gradient.
		 */
		public synchronized int getBoundaryCount() {
			return this.boundaryCount;
		}

		/**
		 * Sets the colors at the boundary points of this gradient.
		 * 
		 * @param boundaryColors The colors at the boundary points of this gradient.
		 */
		public synchronized void setBoundaryColors(Color[] boundaryColors) {
			if (boundaryColors.length != this.boundaryCount) {
				throw new IllegalArgumentException(
						"The length of the argument \"boundaryColors\" must be the same as the value of boundaryCount."
				);
			}
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
			if (boundaryCoordinates.length != this.boundaryCount) {
				throw new IllegalArgumentException(
						"The length of the argument \"boundaryCoordinates\" must be the same as the value of boundaryCount."
				);
			}
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
	}
}
