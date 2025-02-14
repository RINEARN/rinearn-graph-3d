package com.rinearn.graph3d.config.label;

import com.rinearn.graph3d.config.RinearnGraph3DConfigurationException;


/**
 * The class storing configuration values of the label of an axis(X, Y, or Z).
 */
public final class AxisLabelConfiguration {

	// To be added: margin, alignment, etc.

	/** The flag representing whether this axis's label is visible */
	private volatile boolean visible = true;

	/** The displayed text of this label. */
	private volatile String labelText = "";

	/**
	 * Sets whether this axis's label is visible.
	 *
	 * @return Specify true/false to show/hide this axis's label.
	 */
	public synchronized void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * Gets whether this axis's label is visible.
	 *
	 * @return Returns true if this axis's label is visible.
	 */
	public synchronized boolean isVisible() {
		return this.visible;
	}

	/**
	 * Sets the displayed text of this label.
	 *
	 * @param text The displayed text of this label.
	 */
	public synchronized void setLabelText(String text) {
		this.labelText = text;
	}

	/**
	 * Gets the displayed text of this label.
	 *
	 * @return The displayed text of this label.
	 */
	public synchronized String getLabelText() {
		return this.labelText;
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
		if (this.labelText == null) {
			throw new IllegalStateException("The label text is null.");
		}
	}
}
