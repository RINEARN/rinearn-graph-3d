package com.rinearn.graph3d.presenter.handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.rinearn.graph3d.config.EnvironmentConfiguration;
import com.rinearn.graph3d.config.data.IndexSeriesFilter;
import com.rinearn.graph3d.config.data.SeriesFilterMode;
import com.rinearn.graph3d.def.ErrorMessage;
import com.rinearn.graph3d.def.ErrorType;
import com.rinearn.graph3d.view.SeriesFilterComponents;


public final class SeriesFilterHandler {

	/** The container of UI components for series filter settings. */
	private final SeriesFilterComponents seriesFilterComponents;

	/** The event handler of the right-click menu of the text field for inputting the series indices. */
	private final TextRightClickMenuHandler indexFieldMenuHandler;

	/** The flag for turning on/off the event handling feature of this instance. */
	private volatile boolean eventHandlingEnabled = true;

	/** The interface to get series filters from the configuration of the target option. */
	public interface SeriesFilterAccessorInterface {
		public abstract void setSeriesFilterMode(SeriesFilterMode seriesFilterMode);
		public abstract SeriesFilterMode getSeriesFilterMode();
		public abstract void setIndexSeriesFilter(IndexSeriesFilter indexSeriesFilter);
		public abstract IndexSeriesFilter getIndexSeriesFilter();
	}

	/** The getter of the series filters from the configuration of the target option. */
	private final SeriesFilterAccessorInterface seriesFilterAccessor;


	/**
	 * Creates a new instance.
	 *
	 * @param seriesFilterComponents The container of UI components for series filter settings.
	 * @param seriesFilterAccessor The accessor of the series filters from the configuration of the target option.
	 */
	public SeriesFilterHandler(SeriesFilterComponents seriesFilterComponents, SeriesFilterAccessorInterface seriesFilterGetter) {
		this.seriesFilterComponents = seriesFilterComponents;
		this.seriesFilterAccessor = seriesFilterGetter;

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
				seriesFilterComponents.setSeriesFilterMode(SeriesFilterMode.INDEX, seriesFilterAccessor.getIndexSeriesFilter());
			} else {
				seriesFilterComponents.setSeriesFilterMode(SeriesFilterMode.NONE, seriesFilterAccessor.getIndexSeriesFilter());
			}
		}
	}


	/**
	 * Updates the series filter from the current state of UI.
	 * This method is invokable only from the event-dispatch thread.
	 *
	 * @param envConfig The environment configuration, used for detecting the language of the locale.
	 */
	public void updateFilterFromUI(EnvironmentConfiguration envConfig) {
		if (!SwingUtilities.isEventDispatchThread()) {
			throw new IllegalStateException("This method is invokable only from the event-dispatch thread.");
		}
		boolean isJapanese = envConfig.isLocaleJapanese();

		if (this.seriesFilterComponents.enabledBox.isSelected()) {
			this.seriesFilterAccessor.setSeriesFilterMode(SeriesFilterMode.INDEX);

			String[] seriesIndexTexts = this.seriesFilterComponents.indexField.getText().trim().split(",");
			int seriesIndexCount = seriesIndexTexts.length;
			int[] seriesIndices = new int[seriesIndexCount];

			for (int iSeriesIndex=0; iSeriesIndex<seriesIndexCount; iSeriesIndex++) {
				try {
					seriesIndices[iSeriesIndex] = Integer.parseInt(seriesIndexTexts[iSeriesIndex].trim());
				} catch (NumberFormatException nfe) {
					String[] errorWords = isJapanese ? new String[]{ "系列番号" } : new String[]{ "Series Indices" };
					String errorMessage = ErrorMessage.generateErrorMessage(ErrorType.COMMA_SEPARATED_INT_PARAMETER_PARSING_FAILED, errorWords);
					JOptionPane.showMessageDialog(null, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (seriesIndices[iSeriesIndex] < 1 || Integer.MAX_VALUE < seriesIndices[iSeriesIndex]) {
					String[] errorWords = isJapanese ?
							new String[]{ "系列番号", "1", Integer.toString(Integer.MAX_VALUE) } :
							new String[]{ "Series Indices", "1", Integer.toString(Integer.MAX_VALUE) };
					String errorMessage = ErrorMessage.generateErrorMessage(ErrorType.COMMA_SEPARATED_INT_PARAMETER_OUT_OF_RANGE, errorWords);
					JOptionPane.showMessageDialog(null, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
					return;
				}

				// The series index "1" on UI corresponds to the internal series index "0" . So offset the index.
				seriesIndices[iSeriesIndex]--;
			}
			IndexSeriesFilter indexFilter = seriesFilterAccessor.getIndexSeriesFilter();
			indexFilter.setIncludedSeriesIndices(seriesIndices);

		} else {
			seriesFilterAccessor.setSeriesFilterMode(SeriesFilterMode.NONE);
		}
	}
}
