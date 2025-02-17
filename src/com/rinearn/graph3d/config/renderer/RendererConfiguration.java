package com.rinearn.graph3d.config.renderer;

import com.rinearn.graph3d.config.RinearnGraph3DConfigurationException;

/**
 * The class for storing configuration parameters of the renderer.
 */
public class RendererConfiguration {

	/** The flag representing whether the anti-aliasing feature is enabled. */
	private volatile boolean antialiasingEnabled = true;


	/**
	 * Enables/disables the anti-aliasing feature.
	 *
	 * @param antialiasingEnabled Specify true to enable the anti-aliasing feature.
	 */
	public synchronized void setAntialiasingEnabled(boolean antialiasingEnabled) {
		this.antialiasingEnabled = antialiasingEnabled;
	}

	/**
	 * Gets whether the anti-aliasing feature is enabled.
	 *
	 * @return Returns true if the anti-aliasing feature is enabled.
	 */
	public synchronized boolean isAntialiasingEnabled() {
		return this.antialiasingEnabled;
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
	}
}
