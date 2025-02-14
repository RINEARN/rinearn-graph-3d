package com.rinearn.graph3d.config.label;


/**
 * The class storing configuration values of the legend labels.
 */
public final class LegendLabelConfiguration {

	/** The flag representing whether the legends display is visible. */
	private volatile boolean visible = false;

	/** The flag representing whether the auto-legend-generation feature is enabled. */
	private volatile boolean autoLegendGenerationEnabled = true;

	/** The texts of the legend labels. */
	private volatile String[] labelTexts = { };

	/**
	 * Sets whether the legends display is visible.
	 *
	 * @return Specify true/false to show/hide the legends display.
	 */
	public synchronized void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * Gets whether the legends display is visible.
	 *
	 * @return Returns true if the legend display is visible.
	 */
	public synchronized boolean isVisible() {
		return this.visible;
	}

	/**
	 * Enables/disables the auto-legend-generation feature.
	 *
	 * @return Specify true to enable the auto-legend-generation feature.
	 */
	public synchronized void setAutoLegendGenerationEnabled(boolean autoLegendGenerationEnabled) {
		this.autoLegendGenerationEnabled = autoLegendGenerationEnabled;
	}

	/**
	 * Gets whether the auto-legend-generation feature is enabled.
	 *
	 * @return Returns true if the auto-legend-generation feature is enabled.
	 */
	public synchronized boolean isAutoLegendGenerationEnabled() {
		return this.autoLegendGenerationEnabled;
	}

	/**
	 * Sets the texts of the legend labels.
	 *
	 * @param texts The texts of the legend labels.
	 */
	public synchronized void setLabelTexts(String[] texts) {
		this.labelTexts = texts;
	}

	/**
	 * Gets the texts of the legend labels.
	 *
	 * @return The texts of the legend labels.
	 */
	public synchronized String[] getLabelTexts() {
		return this.labelTexts;
	}

	/**
	 * Validates correctness and consistency of configuration parameters stored in this instance.
	 *
	 * This method is called when this configuration is specified to RinearnGraph3D or its renderer.
	 * If no issue is detected, nothing occurs.
	 * If any issue is detected, throws IllegalStateException.
	 *
	 * @throws IllegalStateException Thrown when incorrect or inconsistent settings are detected.
	 */
	public synchronized void validate() throws IllegalStateException {
		if (this.labelTexts == null) {
			throw new IllegalStateException("The legend label texts are null.");
		}
		for (String text: this.labelTexts) {
			if (text == null) {
				throw new IllegalStateException("The legend label texts contains null as an element.");
			}
		}
	}
}