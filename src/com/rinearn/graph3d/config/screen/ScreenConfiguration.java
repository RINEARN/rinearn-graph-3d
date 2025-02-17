package com.rinearn.graph3d.config.screen;

import com.rinearn.graph3d.config.RinearnGraph3DConfigurationException;


/**
 * The class for storing configuration parameters of the screen.
 */
public class ScreenConfiguration {

	/** The width (pixels) of the screen. */
	private volatile int screenWidth = 500;

	/** The height (pixels) of the screen. */
	private volatile int screenHeight = 500;


 	/**
 	 * Creates a new configuration storing default values.
 	 */
 	public ScreenConfiguration() {
 	}


 	/**
 	 * Sets the width of the screen.
 	 *
 	 * @param screenWidth The width (pixels) of the screen.
 	 */
 	public synchronized void setScreenWidth(int screenWidth) {
 		this.screenWidth = screenWidth;
 	}

 	/**
 	 * Gets the width (pixels) of the screen.
 	 *
 	 * @return The width (pixels) of the screen.
 	 */
 	public synchronized int getScreenWidth() {
 		return this.screenWidth;
 	}


 	/**
 	 * Sets the height of the screen.
 	 *
 	 * @param screenHeight The height (pixels) of the screen.
 	 */
 	public synchronized void setScreenHeight(int screenHeight) {
 		this.screenHeight = screenHeight;
 	}

 	/**
 	 * Gets the height (pixels) of the screen.
 	 *
 	 * @return The height (pixels) of the screen.
 	 */
	public synchronized int getScreenHeight() {
 		return this.screenHeight;
 	}


	/**
	 * Validates correctness and consistency of configuration parameters stored in this instance.
	 *
	 * This method is called when this configuration is specified to RinearnGraph3D or its renderer.
	 * If no issue is detected, nothing occurs.
	 * If any issue is detected, throws IllegalStateException.
	 *
	 * @throws RinearnGraph3DConfigurationException Thrown when incorrect or inconsistent settings are detected.
	 */
	public synchronized void validate() throws RinearnGraph3DConfigurationException {
		if (screenWidth < 0) {
			throw new RinearnGraph3DConfigurationException("The value of the screen width is negative.");
		}
		if (screenHeight < 0) {
			throw new RinearnGraph3DConfigurationException("The value of the screen height is negative.");
		}
	}
}
