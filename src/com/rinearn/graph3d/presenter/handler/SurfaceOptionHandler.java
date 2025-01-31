package com.rinearn.graph3d.presenter.handler;

import com.rinearn.graph3d.config.OptionConfiguration;
import com.rinearn.graph3d.config.data.IndexSeriesFilter;
import com.rinearn.graph3d.config.data.SeriesFilterMode;
import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.view.SurfaceOptionWindow;
import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.def.ErrorMessage;
import com.rinearn.graph3d.def.ErrorType;

import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * The class handling events and API requests related to "With Surfaces" option.
 */
public final class SurfaceOptionHandler {

	/** The front-end class of "Model" layer, which provides internal logic procedures and so on. */
	private final Model model;

	/** The front-end class of "View" layer, which provides visible part of GUI without event handling. */
	private final View view;

	/** The front-end class of "Presenter" layer, which invokes Model's procedures triggered by user's action on GUI. */
	private final Presenter presenter;

	/** The event handler of UI components for series filter settings. */
	private final SeriesFilterHandler seriesFilterHandler;

	/** The flag for turning on/off the event handling feature of this instance. */
	private volatile boolean eventHandlingEnabled = true;


	/** The getter class to get the series filters from the configuration of this option. */
	private final class SeriesFilterGetter implements SeriesFilterHandler.SeriesFilterGetterInterface {
		@Override
		public IndexSeriesFilter getIndexSeriesFilter() {
			return model.config.getOptionConfiguration().getSurfaceOptionConfiguration().getIndexSeriesFilter();
		}
	}


	/**
	 * Create a new instance handling events and API requests using the specified resources.
	 *
	 * @param model The front-end class of "Model" layer, which provides internal logic procedures and so on.
	 * @param view The front-end class of "View" layer, which provides visible part of GUI without event handling.
	 * @param presenter The front-end class of "Presenter" layer, which handles events occurred on GUI, and API requests.
	 */
	public SurfaceOptionHandler(Model model, View view, Presenter presenter) {
		this.model = model;
		this.view = view;
		this.presenter = presenter;

		// Add the action listener defined in this class, to the SET button of label setting window.
		SurfaceOptionWindow window = this.view.surfaceOptionWindow;
		window.setButton.addActionListener(new SetPressedEventListener());

		// Add the event handler to UI components for series filter settings.
		seriesFilterHandler = new SeriesFilterHandler(window.seriesFilterComponents, new SeriesFilterGetter());
	}


	/**
	 * Turns on/off the event handling feature of this instance.
	 *
	 * @param enabled Specify false for turning off the event handling feature (enabled by default).
	 */
	public synchronized void setEventHandlingEnabled(boolean enabled) {
		this.eventHandlingEnabled = enabled;
		this.seriesFilterHandler.setEventHandlingEnabled(enabled);
	}


	/**
	 * Gets whether the event handling feature of this instance is enabled.
	 *
	 * @return Returns true if the event handling feature is enabled.
	 */
	public synchronized boolean isEventHandlingEnabled() {
		return this.eventHandlingEnabled;
	}





	// ================================================================================
	//
	// - Event Listeners -
	//
	// ================================================================================


	/**
	 * The event listener handling the event that OK button is pressed.
	 */
	private final class SetPressedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			SurfaceOptionWindow window = view.surfaceOptionWindow;
			OptionConfiguration optionConfig = model.config.getOptionConfiguration();
			OptionConfiguration.SurfaceOptionConfiguration surfaceOptionConfig = optionConfig.getSurfaceOptionConfiguration();
			boolean isJapanese = model.config.getEnvironmentConfiguration().isLocaleJapanese();

			// Series filter:
			{
				boolean seriesFilterEnabled = window.seriesFilterComponents.enabledBox.isSelected();
				if (seriesFilterEnabled) {
					surfaceOptionConfig.setSeriesFilterMode(SeriesFilterMode.INDEX);

					String[] seriesIndexTexts = window.seriesFilterComponents.indexField.getText().trim().split(",");
					int seriesIndexCount = seriesIndexTexts.length;
					int[] seriesIndices = new int[seriesIndexCount];

					for (int iSeriesIndex=0; iSeriesIndex<seriesIndexCount; iSeriesIndex++) {
						try {
							seriesIndices[iSeriesIndex] = Integer.parseInt(seriesIndexTexts[iSeriesIndex].trim());
						} catch (NumberFormatException nfe) {
							String[] errorWords = isJapanese ? new String[]{ "系列番号" } : new String[]{ "Series Indices" };
							String errorMessage = ErrorMessage.generateErrorMessage(ErrorType.COMMA_SEPARATED_INT_PARAMETER_PARSING_FAILED, errorWords);
							JOptionPane.showMessageDialog(window.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
							return;
						}
						if (seriesIndices[iSeriesIndex] < 1 || Integer.MAX_VALUE < seriesIndices[iSeriesIndex]) {
							String[] errorWords = isJapanese ?
									new String[]{ "系列番号", "1", Integer.toString(Integer.MAX_VALUE) } :
									new String[]{ "Series Indices", "1", Integer.toString(Integer.MAX_VALUE) };
							String errorMessage = ErrorMessage.generateErrorMessage(ErrorType.COMMA_SEPARATED_INT_PARAMETER_OUT_OF_RANGE, errorWords);
							JOptionPane.showMessageDialog(window.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
							return;
						}

						// The series index "1" on UI corresponds to the internal series index "0" . So offset the index.
						seriesIndices[iSeriesIndex]--;
					}
					IndexSeriesFilter indexFilter = surfaceOptionConfig.getIndexSeriesFilter();
					indexFilter.setIncludedSeriesIndices(seriesIndices);

				} else {
					surfaceOptionConfig.setSeriesFilterMode(SeriesFilterMode.NONE);
				}
			}

			// Propagate the above update of the configuration to the entire application.
			presenter.propagateConfiguration();

			// Replot the graph.
			presenter.plot();
		}
	}
}
