package com.rinearn.graph3d.config.plotter;

import com.rinearn.graph3d.config.data.IndexSeriesFilter;
import com.rinearn.graph3d.config.data.SeriesFilter;
import com.rinearn.graph3d.config.data.SeriesFilterHub;
import com.rinearn.graph3d.config.data.SeriesFilterMode;


/**
 * The base class of *PlotterConfiguration classes supporting the series filter feature.
 */
public abstract class SeriesFilterablePlotterConfiguration {

	/** The flag representing whether this option is enabled. */
	private volatile boolean plotterEnabled = false;

	/** The object which stores multiple kinds of SeriesFilter instances, and provides their setters and getters. */
	private volatile SeriesFilterHub seriesFilterHub = new SeriesFilterHub();

	/**
	 * Enable or disable this plotter.
	 *
	 * @param optionEnabled Specify true to enable, false to disable.
	 */
	public synchronized final void setPlotterEnabled(boolean plotterEnabled) {
		this.plotterEnabled = plotterEnabled;
	}

	/**
	 * Checks whether this plotter is enabled.
	 *
	 * @return Returns true if this option is enabled.
	 */
	public synchronized final boolean isPlotterEnabled() {
		return this.plotterEnabled;
	}

	/**
	 * Sets the mode of the series filter.
	 *
	 * @param seriesFilterMode the mode of the series filter.
	 */
	public synchronized void setSeriesFilterMode(SeriesFilterMode seriesFilterMode) {
		this.seriesFilterHub.setSeriesFilterMode(seriesFilterMode);
	}

	/**
	 * Gets the mode of the series filter.
	 *
	 * @param seriesFilterMode the mode of the series filter.
	 */
	public synchronized SeriesFilterMode getSeriesFilterMode() {
		return this.seriesFilterHub.getSeriesFilterMode();
	}

	/**
	 * Gets the series filter corresponding to the current mode.
	 *
	 * @return The series filter corresponding to the current mode.
	 */
	public synchronized SeriesFilter getSeriesFilter() {
		return this.seriesFilterHub.getSeriesFilter();
	}

	/**
	 * Sets the index-based series filter, used in INDEX mode.
	 *
	 * @param indexSeriesFilter The index-based series filter, used in INDEX mode.
	 */
	public synchronized void setIndexSeriesFilter(IndexSeriesFilter indexSeriesFilter) {
		this.seriesFilterHub.setIndexSeriesFilter(indexSeriesFilter);
	}

	/**
	 * Gets the index-based series filter, used in INDEX mode.
	 *
	 * @return The index-based series filter, used in INDEX mode.
	 */
	public synchronized IndexSeriesFilter getIndexSeriesFilter() {
		return this.seriesFilterHub.getIndexSeriesFilter();
	}

	/**
	 * Sets the custom implementation of a filter, used in CUSTOM mode.
	 *
	 * @param customSeriesFilter The custom implementation of a  series filter, used in CUSTOM mode.
	 */
	public synchronized void setCustomSeriesFilter(SeriesFilter customSeriesFilter) {
		this.seriesFilterHub.setCustomSeriesFilter(customSeriesFilter);
	}

	/**
	 * Gets the custom implementation of a filter, used in CUSTOM mode.
	 *
	 * @return The custom implementation of a  series filter, used in CUSTOM mode.
	 */
	public synchronized SeriesFilter getCustomSeriesFilter() {
		return this.seriesFilterHub.getCustomSeriesFilter();
	}
}
