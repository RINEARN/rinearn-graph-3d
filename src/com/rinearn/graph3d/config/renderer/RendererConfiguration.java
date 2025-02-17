package com.rinearn.graph3d.config.renderer;

import com.rinearn.graph3d.config.RinearnGraph3DConfigurationException;

/**
 * The class for storing configuration parameters of the renderer.
 */
public class RendererConfiguration {

 	/** The rendering mode, */
 	private volatile RenderingMode renderingMode = RenderingMode.QUALITY;

	/**
	 * Sets the rendering mode.
	 *
	 * @param renderingMode The rendering mode.
	 */
	public synchronized void setRenderingMode(RenderingMode renderingMode) {
		this.renderingMode = renderingMode;
	}

	/**
	 * Gets the current rendering mode.
	 *
	 * @param renderingMode The current rendering mode.
	 */
	public synchronized RenderingMode getRenderingMode() {
		return this.renderingMode;
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
		if (renderingMode == null) {
			throw new RinearnGraph3DConfigurationException("The rendering mode is null.");
		}
	}
}
