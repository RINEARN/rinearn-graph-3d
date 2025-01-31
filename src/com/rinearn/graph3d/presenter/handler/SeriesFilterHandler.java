package com.rinearn.graph3d.presenter.handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.rinearn.graph3d.config.data.IndexSeriesFilter;
import com.rinearn.graph3d.config.data.SeriesFilterMode;
import com.rinearn.graph3d.view.SeriesFilterComponents;


public final class SeriesFilterHandler {

	/** The container of UI components for series filter settings. */
	private final SeriesFilterComponents seriesFilterComponents;

	/** The event handler of the right-click menu of the text field for inputting the series indices. */
	private final TextRightClickMenuHandler indexFieldMenuHandler;

	/** The flag for turning on/off the event handling feature of this instance. */
	private volatile boolean eventHandlingEnabled = true;

	/** The interface to get series filters from the configuration of the target option. */
	public interface SeriesFilterGetterInterface {
		public abstract IndexSeriesFilter getIndexSeriesFilter();
	}

	/** The getter of the series filters from the configuration of the target option. */
	private final SeriesFilterGetterInterface seriesFilterGetter;


	/**
	 * Creates a new instance.
	 *
	 * @param seriesFilterComponents The container of UI components for series filter settings.
	 * @param seriesFilterGetter The getter of the series filters from the configuration of the target option.
	 */
	public SeriesFilterHandler(SeriesFilterComponents seriesFilterComponents, SeriesFilterGetterInterface seriesFilterGetter) {
		this.seriesFilterComponents = seriesFilterComponents;
		this.seriesFilterGetter = seriesFilterGetter;

		// Add the event handler of the right-click menu of seriesFilterComponents.indexField.
		this.indexFieldMenuHandler = new TextRightClickMenuHandler(
				this.seriesFilterComponents.indexFieldRightClickMenu,
				this.seriesFilterComponents.indexField
		);

		// Add the event listener handling the event that the series filter is enabled/disabled.
		this.seriesFilterComponents.enabledBox.addActionListener(new SeriesFilterBoxSelectedEventListener());
	}


	/**
	 * Turns on/off the event handling feature of this instance.
	 *
	 * @param enabled Specify false for turning off the event handling feature (enabled by default).
	 */
	public synchronized void setEventHandlingEnabled(boolean enabled) {
		this.eventHandlingEnabled = enabled;
		this.indexFieldMenuHandler.setEventHandlingEnabled(enabled);
	}


	/**
	 * Gets whether the event handling feature of this instance is enabled.
	 *
	 * @return Returns true if the event handling feature is enabled.
	 */
	public synchronized boolean isEventHandlingEnabled() {
		return this.eventHandlingEnabled;
	}


	/**
	 * The event listener handling the event that the series filter is enabled/disabled.
	 */
	private final class SeriesFilterBoxSelectedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			if (seriesFilterComponents.enabledBox.isSelected()) {
				seriesFilterComponents.setSeriesFilterMode(SeriesFilterMode.INDEX, seriesFilterGetter.getIndexSeriesFilter());
			} else {
				seriesFilterComponents.setSeriesFilterMode(SeriesFilterMode.NONE, seriesFilterGetter.getIndexSeriesFilter());
			}
		}
	}
}
