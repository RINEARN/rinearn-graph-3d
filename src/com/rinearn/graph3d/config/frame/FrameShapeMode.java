package com.rinearn.graph3d.config.frame;


/**
 * The enum representing each shape mode of the graph frame.
 */
public enum FrameShapeMode {

	/** Represents the mode drawing no frame. */
	NONE,

	/** Represents the mode drawing a standard box-type frame. */
	BOX,

	/** Represents the mode drawing only backwalls (inside) of a box-type frame. */
	BACKWALL,

	/** Represents the mode drawing only the floor. */
	FLOOR;
}
