package com.rinearn.graph3d.presenter.handler;

import com.rinearn.graph3d.config.OptionConfiguration;
import com.rinearn.graph3d.config.data.IndexSeriesFilter;
import com.rinearn.graph3d.config.data.SeriesFilterMode;
import com.rinearn.graph3d.def.ErrorMessage;
import com.rinearn.graph3d.def.ErrorType;
import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.view.MeshOptionWindow;
import com.rinearn.graph3d.view.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;


/**
 * The class handling events and API requests related to "With Meshes" option.
 */
public final class MeshOptionHandler {

	/** The front-end class of "Model" layer, which provides internal logic procedures and so on. */
	private final Model model;

	/** The front-end class of "View" layer, which provides visible part of GUI without event handling. */
	private final View view;

	/** The front-end class of "Presenter" layer, which invokes Model's procedures triggered by user's action on GUI. */
	private final Presenter presenter;

	/** The event handler of the right-click menu of the text field to input the line width. */
	private final TextRightClickMenuHandler lineWidthFieldMenuHandler;

	/** The event handler of UI components for series filter settings. */
	private final SeriesFilterHandler seriesFilterHandler;

	/** The flag for turning on/off the event handling feature of this instance. */
	private volatile boolean eventHandlingEnabled = true;


	/** The accessor class to get the series filters from the configuration of this option. */
	private final class SeriesFilterAccessor implements SeriesFilterHandler.SeriesFilterAccessorInterface {
		@Override
		public void setSeriesFilterMode(SeriesFilterMode seriesFilterMode) {
			model.config.getOptionConfiguration().getMeshOptionConfiguration().setSeriesFilterMode(seriesFilterMode);
		}
		@Override
		public SeriesFilterMode getSeriesFilterMode() {
			return model.config.getOptionConfiguration().getMeshOptionConfiguration().getSeriesFilterMode();
		}
		@Override
		public void setIndexSeriesFilter(IndexSeriesFilter indexSeriesFilter) {
			model.config.getOptionConfiguration().getMeshOptionConfiguration().setIndexSeriesFilter(indexSeriesFilter);
		}
		@Override
		public IndexSeriesFilter getIndexSeriesFilter() {
			return model.config.getOptionConfiguration().getMeshOptionConfiguration().getIndexSeriesFilter();
		}
	}


	/**
	 * Create a new instance handling events and API requests using the specified resources.
	 *
	 * @param model The front-end class of "Model" layer, which provides internal logic procedures and so on.
	 * @param view The front-end class of "View" layer, which provides visible part of GUI without event handling.
	 * @param presenter The front-end class of "Presenter" layer, which handles events occurred on GUI, and API requests.
	 */
	public MeshOptionHandler(Model model, View view, Presenter presenter) {
		this.model = model;
		this.view = view;
		this.presenter = presenter;

		// Add the action listener defined in this class, to the SET button of label setting window.
		MeshOptionWindow window = this.view.meshOptionWindow;
		window.setButton.addActionListener(new SetPressedEventListener());

		// Add the event handler of the right-click menu of the text field to input the line width.
		this.lineWidthFieldMenuHandler = new TextRightClickMenuHandler(window.lineWidthFieldRightClickMenu, window.lineWidthField);

		// Add the event handler to UI components for series filter settings.
		this.seriesFilterHandler = new SeriesFilterHandler(window.seriesFilterComponents, new SeriesFilterAccessor());
	}


	/**
	 * Turns on/off the event handling feature of this instance.
	 *
	 * @param enabled Specify false for turning off the event handling feature (enabled by default).
	 */
	public synchronized void setEventHandlingEnabled(boolean enabled) {
		this.eventHandlingEnabled = enabled;
		this.lineWidthFieldMenuHandler.setEventHandlingEnabled(enabled);
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
			MeshOptionWindow window = view.meshOptionWindow;
			OptionConfiguration optionConfig = model.config.getOptionConfiguration();
			OptionConfiguration.MeshOptionConfiguration meshOptionConfig = optionConfig.getMeshOptionConfiguration();
			boolean isJapanese = model.config.getEnvironmentConfiguration().isLocaleJapanese();

			// Line width:
			{
				String lineWidthText = window.lineWidthField.getText();
				double lineWidth = Double.NaN;
				try {
					lineWidth = Double.parseDouble(lineWidthText);
				} catch (NumberFormatException nfe) {
					String[] errorWords = isJapanese ? new String[]{ "線の幅" } : new String[]{ "Line Width" };
					String errorMessage = ErrorMessage.generateErrorMessage(ErrorType.DOUBLE_PARAMETER_PARSING_FAILED, errorWords);
					JOptionPane.showMessageDialog(window.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (lineWidth < 0.0 || 10000.0 < lineWidth) {
					String[] errorWords = isJapanese ? new String[]{ "線の幅", "0.0", "10000.0" } : new String[]{ "Line Width", "0.0", "10000.0" };
					String errorMessage = ErrorMessage.generateErrorMessage(ErrorType.DOUBLE_PARAMETER_OUT_OF_RANGE, errorWords);
					JOptionPane.showMessageDialog(window.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				meshOptionConfig.setLineWidth(lineWidth);
			}

			// Update the series filter from the current state of filer-settings UI.
			seriesFilterHandler.updateFilterFromUI(model.config.getEnvironmentConfiguration());

			// Propagate the above update of the configuration to the entire application.
			presenter.propagateConfiguration();

			// Replot the graph.
			presenter.plot();
		}
	}
}
