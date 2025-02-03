package com.rinearn.graph3d.config.color;

/**
 * The enum for specifying a interpolation mode, which determines colors between boundary points.
 */
public enum GradientInterpolationMode {

	/** Interpolates colors between each pair of boundary points by the linear interpolation. */
	LINEAR, // For continuous gradation

	/** Interpolates colors between each pair of boundary points by a flat step (step function). */
	STEP;
}