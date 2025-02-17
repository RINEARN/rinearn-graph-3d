package com.rinearn.graph3d.config.label;

import com.rinearn.graph3d.config.RinearnGraph3DConfigurationException;


/**
 * The class storing configuration values of the legend labels.
 */
public final class LegendLabelConfiguration {

	/** The flag representing whether the auto-legend-generation feature is enabled. */
	private volatile boolean autoLegendGenerationEnabled = true;

	/** The flag representing whether the empty-legend-exclusion feature, which omit to draw empty legends, is enabled. */
	private volatile boolean emptyLegendExclusionEnabled = false; // to be renamed: emptyLegendExclusionEnabled


	/** The texts of the legend labels. */
	private volatile String[] labelTexts = { };


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
	 * Sets whether the empty-legend-exclusion feature, which omit to draw empty legends, is enabled.
	 *
	 * @param emptyLegendExclusionEnabled Specify true to enable the empty-legend-exclusion feature.
	 */
	public synchronized void setEmptyLegendExclusionEnabled(boolean emptyLegendExclusionEnabled) {
		this.emptyLegendExclusionEnabled = emptyLegendExclusionEnabled;
	}

	/**
	 * Gets whether the empty-legend-exclusion feature, which removes empty lines in legends, is enabled.
	 *
	 * @return Returns true to enable the empty-legend-exclusion feature.
	 */
	public synchronized boolean isEmptyLegendExclusionEnabled() {
		return this.emptyLegendExclusionEnabled;
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
	 * @throws RinearnGraph3DConfigurationException Thrown when incorrect or inconsistent settings are detected.
	 */
	public synchronized void validate() throws RinearnGraph3DConfigurationException {
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