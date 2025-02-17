package com.rinearn.graph3d.config.frame;

import com.rinearn.graph3d.config.RinearnGraph3DConfigurationException;


/**
 * The class storing configuration values of an axis of the graph frame.
 */
public class AxisFrameConfiguration {

	/** The length factor of this axis, where the default length is 1.0. */
	private volatile double lengthFactor = 1.0;


	/**
	 * Sets the length factor of this axis, where the default length is 1.0.
	 *
	 * @param lengthFactor The length factor of this axis.
	 */
	public synchronized void setLengthFactor(double lengthFactor) {
		this.lengthFactor = lengthFactor;
	}

	/**
	 * Gets the length factor of this axis, where the default length is 1.0.
	 *
	 * @return The length factor of this axis.
	 */
	public synchronized double getLengthFactor() {
		return this.lengthFactor;
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
		if (this.lengthFactor < 0.0) {
			throw new IllegalStateException("The axis's length factor must be a positive value.");
		}
	}
}
