package com.rinearn.graph3d.config;


/**
 * The class storing configuration parameters of the graph frame.
 */
public final class RinearnGraph3DFrameConfiguration {

	// To be added: grid-lines, images, etc.

	/**
	 * The enum representing each mode of the graph frame.
	 */
	public static enum FrameMode {

		/** Represents the mode drawing no frame. */
		NONE,

		/** Represents the mode drawing a standard box-type frame. */
		BOX,

		/** Represents the mode drawing only backwalls (inside) of a box-type frame. */
		BACKWALL,

		/** Represents the mode drawing only the floor. */
		FLOOR;
	}


	/**
	 * Creates new configuration storing default values.
	 */
	public RinearnGraph3DFrameConfiguration() {
	}


	/** Stores the mode of the graph frame. */
	private volatile FrameMode frameMode = FrameMode.BOX;

	/**
	 * Sets the mode of the graph frame.
	 * 
	 * @param frameMode The mode of the graph frame.
	 */
	public synchronized void setFrameMode(FrameMode frameMode) {
		this.frameMode = frameMode;
	}

	/**
	 * Gets the mode of the graph frame.
	 * 
	 * @return The mode of the graph frame.
	 */
	public synchronized FrameMode getFrameMode() {
		return this.frameMode;
	}
}
