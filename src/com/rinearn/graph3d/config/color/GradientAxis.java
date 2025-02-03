package com.rinearn.graph3d.config.color;

/**
 * The enum for specifying a gradient's axis.
 */
public enum GradientAxis {

	/** Represents the gradient for the direction of X axis. */
	X,

	/** Represents the gradient for the direction of Y axis. */
	Y,

	/** Represents the gradient for the direction of Z axis. */
	Z,

	/** Represents the gradient by coordinate values of 4-th column in data files. */
	COLUMN_4; // Note: Should rename to more abstract one?
}
