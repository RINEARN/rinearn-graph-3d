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

	/** The event handler of right-click menu of the series indices text field. */
	private final TextRightClickMenuHandler seriesFilterMenuHandler;

	/** The flag for turning on/off the event handling feature of this instance. */
	private volatile boolean eventHandlingEnabled = true;


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

		// Add the event listener handling the event that the series filter is enabled/disabled.
		window.seriesFilterBox.addActionListener(new SeriesFilterBoxSelectedEventListener());

		// Add the event listeners to the right-click menus.
		seriesFilterMenuHandler = new TextRightClickMenuHandler(window.seriesFilterFieldRightClickMenu, window.seriesFilterField);
	}


	/**
	 * Turns on/off the event handling feature of this instance.
	 *
	 * @param enabled Specify false for turning off the event handling feature (enabled by default).
	 */
	public synchronized void setEventHandlingEnabled(boolean enabled) {
		this.eventHandlingEnabled = enabled;
		seriesFilterMenuHandler.setEventHandlingEnabled(enabled);
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
				boolean seriesFilterEnabled = window.seriesFilterBox.isSelected();
				if (seriesFilterEnabled) {
					surfaceOptionConfig.setSeriesFilterMode(SeriesFilterMode.INDEX);

					String[] seriesIndexTexts = window.seriesFilterField.getText().trim().split(",");
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


	/**
	 * The event listener handling the event that the series filter is enabled/disabled.
	 */
	private final class SeriesFilterBoxSelectedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			SurfaceOptionWindow window = view.surfaceOptionWindow;
			OptionConfiguration optionConfig = model.config.getOptionConfiguration();
			OptionConfiguration.PointOptionConfiguration pointOptionConfig = optionConfig.getPointOptionConfiguration();

			if (window.seriesFilterBox.isSelected()) {
				window.setSeriesFilterMode(SeriesFilterMode.INDEX, pointOptionConfig.getIndexSeriesFilter());
			} else {
				window.setSeriesFilterMode(SeriesFilterMode.NONE, pointOptionConfig.getIndexSeriesFilter());
			}
		}
	}
}
