package com.rinearn.graph3d.presenter.handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.rinearn.graph3d.RinearnGraph3DDataFileFormat;
import com.rinearn.graph3d.def.ErrorMessage;
import com.rinearn.graph3d.def.ErrorType;
import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.model.data.series.ArrayDataSeries;
import com.rinearn.graph3d.model.data.series.DataSeriesGroup;
import com.rinearn.graph3d.model.io.DataFileIO;
import com.rinearn.graph3d.model.io.DataFileFormatException;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.view.DataTextPastingWindow;


/**
 * The class handling events and API requests for plotting data texts.
 */
public final class DataTextIOHandler {

	/** The front-end class of "Model" layer, which provides internal logic procedures and so on. */
	private final Model model;

	/** The front-end class of "View" layer, which provides visible part of GUI without event handling. */
	private final View view;

	/** The front-end class of "Presenter" layer, which invokes Model's procedures triggered by user's action on GUI. */
	private final Presenter presenter;

	/** The event handler of right-click menu of data pasting text area.  */
	private volatile TextRightClickMenuHandler dataTextAreaMenuHandler;

	/** The flag for turning on/off the event handling feature of this instance. */
	private volatile boolean eventHandlingEnabled = true;


	/**
	 * Create a new instance handling events and API requests using the specified resources.
	 *
	 * @param model The front-end class of "Model" layer, which provides internal logic procedures and so on.
	 * @param view The front-end class of "View" layer, which provides visible part of GUI without event handling.
	 * @param presenter The front-end class of "Presenter" layer, which handles events occurred on GUI, and API requests.
	 */
	public DataTextIOHandler(Model model, View view, Presenter presenter) {
		this.model = model;
		this.view = view;
		this.presenter = presenter;

		DataTextPastingWindow window = this.view.dataTextPastingWindow;

		// Add the action listener to the PLOT/CLEAR button.
		window.plotButton.addActionListener(new PlotButtonEventListener());
		window.clearButton.addActionListener(new ClearButtonEventListener());

		// Add the event listeners to the right-click menu.
		this.dataTextAreaMenuHandler = new TextRightClickMenuHandler(window.dataTextAreaRightClickMenu, window.dataTextArea);
	}


	/**
	 * Turns on/off the event handling feature of this instance.
	 *
	 * @param enabled Specify false for turning off the event handling feature (enabled by default).
	 */
	public synchronized void setEventHandlingEnabled(boolean enabled) {
		this.eventHandlingEnabled = enabled;
		this.dataTextAreaMenuHandler.setEventHandlingEnabled(enabled);
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
	 * The event listener handling the event that PLOT button is pressed.
	 */
	private final class PlotButtonEventListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			DataTextPastingWindow window = view.dataTextPastingWindow;
			String dataText = window.dataTextArea.getText();
			RinearnGraph3DDataFileFormat format = window.getSelectedDataFileFormat();

			// Parse the data text.
			DataSeriesGroup<ArrayDataSeries> dataSeriesGroup = null;
			try {
				DataFileIO dataFileIO = new DataFileIO();
				dataSeriesGroup = dataFileIO.parseDataFileContent(dataText, format);

			// If any error occurred, display it to users as a pop-up message.
			} catch (DataFileFormatException dffe) {
				String errorMessage = ErrorMessage.generateErrorMessage(ErrorType.FAILED_TO_PARSE_DATA_TEXT);
				boolean isJapanese = model.config.getEnvironmentConfiguration().isLocaleJapanese();
				if (isJapanese) {
					errorMessage += "\n\n[詳細]\n" + dffe.getMessage();
				} else {
					errorMessage += "\n\n[Details]\n" + dffe.getMessage();
				}
				JOptionPane.showMessageDialog(view.mainWindow.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
			}

			// Set the loaded data to Model.
			model.dataStore.setArrayDataSeriesGroup(dataSeriesGroup);

			// Don't do the following. We must register the multiple data series by an "atomic operation".
			// (because the data series registered in the Model may be accessed from another thread asynchronously.)
			// ---
			// for (ArrayDataSeries dataSeries) {
			//     model.addArrayDataSeries(dataSeries);
			// }

			// Re-plot the graph.
			presenter.plot();
		}
	}



	/**
	 * The event listener handling the event that CLEAR button is pressed.
	 */
	private final class ClearButtonEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}

			// Clear the data text pasting area.
			view.dataTextPastingWindow.dataTextArea.setText("");

			// Cleare all currently-registered array data series.
			model.dataStore.clearArrayDataSeries();

			// Replot the graph.
			presenter.plot();
		}
	}

}
