package com.rinearn.graph3d.config;

// !!!!! NOTE !!!!!
// この設定は ScaleConfig にマージすべきか？ あえて分けるほどの規模でもないし、2Dだとどう分けるのって問題になるし。
// 実質的にこれのメインは底面の画像設定くらいな気がする。
//
// -> それじゃあ、画像設定を入れてみて、規模感が小さかったらマージを検討、結構大きかったらそのまま現状で、って感じで。
//    後でまた要検討


/**
 * The class for storing configuration parameters of the graph frame.
 */
public final class FrameConfiguration {

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
	public FrameConfiguration() {
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
