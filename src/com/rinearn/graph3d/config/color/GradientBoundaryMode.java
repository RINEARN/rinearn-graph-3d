package com.rinearn.graph3d.config.color;

// Should rename to "BoundaryLayoutMode" or something else?
/**
 * The enum for specifying a boundary mode, which determines locations of boundary points.
 */
public enum GradientBoundaryMode {

	/** Divides the range from min to max coordinates equally by boundary points. */
	EQUAL_DIVISION,

	/** Specify coordinate values of the boundary points manually. */
	MANUAL;
}
