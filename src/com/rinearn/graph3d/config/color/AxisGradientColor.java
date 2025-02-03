package com.rinearn.graph3d.config.color;

import java.awt.Color;
import java.math.BigDecimal;


/**
 * The class representing one-dimensional gradient color,
 * composing one axis of (may be multiple dimensional) gradient color.
 */
public final class AxisGradientColor {

	// Note: Should rename "boundary..." to "midPoint..." ?
	//       -> Probably No. "mid point" is used by some interpolation algorithms in the different meaning, so it may lead confusing.

	// boundaryCount みたいなやつがいるが…命名どうする？ -> ちょうど色数に一致するからそれでいい。

	/** The dimension axis of this gradient. */
	private volatile GradientAxis axis = GradientAxis.Z;

	/** Stores a blend mode, for blending this gradient to the background color or an other gradient. */
	private ColorBlendMode blendMode = ColorBlendMode.ADDITION;

	/** The boundary mode, which determines locations of boundary points. */
	private volatile GradientBoundaryMode boundaryMode = GradientBoundaryMode.EQUAL_DIVISION;

	/** The interpolation mode, which determines colors between boundary points. */
	private volatile GradientInterpolationMode interpolationMode = GradientInterpolationMode.LINEAR;

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
	 * Sets the blend mode, for blending this gradient to the background color or an other gradient.
	 *
	 * @param blendMode The blend mode.
	 */
	public synchronized void setBlendMode(ColorBlendMode blendMode) {
		this.blendMode = blendMode;
	}

	/**
	 * Gets the blend mode, for blending this gradient to the background color or an other gradient.
	 *
	 * @return The blend mode.
	 */
	public synchronized ColorBlendMode getBlendMode() {
		return this.blendMode;
	}

	/**
	 * Sets the interpolation mode, which determines colors between boundary points.
	 *
	 * @param interpolationMode The interpolation mode.
	 */
	public synchronized void setInterpolationMode(GradientInterpolationMode interpolationMode) {
		this.interpolationMode = interpolationMode;
	}

	/**
	 * Gets the interpolation mode, which determines colors between boundary points.
	 *
	 * @return The interpolation mode.
	 */
	public synchronized GradientInterpolationMode getInterpolationMode() {
		return this.interpolationMode;
	}

	/**
	 * Sets the boundary mode, which determines locations of boundary points.
	 *
	 * @param boundaryMode The boundary mode.
	 */
	public synchronized void setBoundaryMode(GradientBoundaryMode boundaryMode) {
		this.boundaryMode = boundaryMode;
	}

	/**
	 * Gets the boundary mode, which determines locations of boundary points.
	 *
	 * @return The boundary mode.
	 */
	public synchronized GradientBoundaryMode getBoundaryMode() {
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
		if (this.boundaryMode == GradientBoundaryMode.MANUAL) {
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
		if (this.boundaryMode == GradientBoundaryMode.EQUAL_DIVISION && !this.autoBoundaryRangingEnabled) {
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