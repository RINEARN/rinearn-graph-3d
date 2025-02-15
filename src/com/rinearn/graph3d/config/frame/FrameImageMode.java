package com.rinearn.graph3d.config.frame;


/**
 * The enum representing each image displaying mode on the frame of a plane.
 */
public enum FrameImageMode {

	/** Represents the mode displaying no image. */
	NONE,

	/** Represents the mode displaying the image loaded from a file. */
	FILE,

	// SUBPLOT, // Maybe supported in future.

	/** Represents the mode displaying the dynamically generated image passed through API, etc. */
	CUSTOM,
}
