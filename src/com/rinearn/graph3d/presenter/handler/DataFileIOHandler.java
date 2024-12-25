package com.rinearn.graph3d.presenter.handler;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.model.data.series.ArrayDataSeries;
import com.rinearn.graph3d.model.data.series.DataSeriesGroup;
import com.rinearn.graph3d.presenter.Presenter;

import com.rinearn.graph3d.view.DataFileOpeningWindow;
import com.rinearn.graph3d.view.View;

import com.rinearn.graph3d.RinearnGraph3DDataFileFormat;
import com.rinearn.graph3d.model.io.DataFileIO;
import com.rinearn.graph3d.model.io.DataFileFormatException;
import com.rinearn.graph3d.def.ErrorType;
import com.rinearn.graph3d.def.ErrorMessage;
import com.rinearn.graph3d.def.CommunicationType;
import com.rinearn.graph3d.def.CommunicationMessage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FileDialog;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import javax.swing.SwingUtilities;

import javax.swing.JOptionPane;


/**
 * The class handling events and API requests for plotting data files.
 */
public final class DataFileIOHandler {

	/** The front-end class of "Model" layer, which provides internal logic procedures and so on. */
	private final Model model;

	/** The front-end class of "View" layer, which provides visible part of GUI without event handling. */
	private final View view;

	/** The front-end class of "Presenter" layer, which invokes Model's procedures triggered by user's action on GUI. */
	private final Presenter presenter;

	/** The flag for turning on/off the event handling feature of this instance. */
	private volatile boolean eventHandlingEnabled = true;


	/**
	 * Create a new instance handling events and API requests using the specified resources.
	 *
	 * @param model The front-end class of "Model" layer, which provides internal logic procedures and so on.
	 * @param view The front-end class of "View" layer, which provides visible part of GUI without event handling.
	 * @param presenter The front-end class of "Presenter" layer, which handles events occurred on GUI, and API requests.
	 */
	public DataFileIOHandler(Model model, View view, Presenter presenter) {
		this.model = model;
		this.view = view;
		this.presenter = presenter;

		DataFileOpeningWindow window = this.view.dataFileOpeningWindow;

		// Add the action listener to the OPEN/PLOT/CLEAR button.
		window.openButton.addActionListener(new OpenButtonEventListener());
		window.plotButton.addActionListener(new PlotButtonEventListener());
		window.clearButton.addActionListener(new ClearButtonEventListener());

		// Add the action listener to the "Data Format" combo box.
		window.dataFormatBox.addActionListener(new DataFormatBoxEventListener());
	}


	/**
	 * Turns on/off the event handling feature of this instance.
	 *
	 * @param enabled Specify false for turning off the event handling feature (enabled by default).
	 */
	public synchronized void setEventHandlingEnabled(boolean enabled) {
		this.eventHandlingEnabled = enabled;
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
	 * The container of the pair of a file and its data format, used in PlotButtonEventListener.
	 */
	private final class FileFormatPair {

		/** The file. */
		public final File file;

		/** The data format of the file. */
		public final RinearnGraph3DDataFileFormat format;

		/**
		 * Creates a new container instance having the specified values.
		 *
		 * @param file The file.
		 * @param format The data format of the file.
		 */
		public FileFormatPair(File file, RinearnGraph3DDataFileFormat format) {
			this.file = file;
			this.format = format;
		}
	}

	/**
	 * Parses a line in the content of fileListArea.
	 *
	 * @param line A line in the content of fileListArea.
	 * @return The parsed result, the container of the file-path part and data-format part.
	 */
	private FileFormatPair parseFileFormatLine(String line) {

		// The line contains the file-path part and data-format part, separated by colon:
		//
		//   <file-path part>: <data-format part>
		//
		// Note that, the file-path part may contains colons, e.g.:
		//
		//   C:\aaa\bbb\file.csv: AUTO

		// Gets the index of the colon at the last-side in the line.
		int lastColonIndex = line.lastIndexOf(":");

		// If the line contains no colons.
		if (lastColonIndex == -1) {
			return new FileFormatPair(new File(line), RinearnGraph3DDataFileFormat.AUTO);
		}

		// Split by the last colon.
		String filePathPart = line.substring(0, lastColonIndex).trim();
		String formatPart = line.substring(lastColonIndex + 1, line.length()).trim().toUpperCase();

		// Convert the data-format part to a value of the RinearnGraph3DDataFileFormat enum.
		RinearnGraph3DDataFileFormat format;
		try {
			format = RinearnGraph3DDataFileFormat.valueOf(formatPart);

		// If the conversion failed, it means that the data-format part is misspelled,
		// or we should regard it as a part of the file-path part, not data-format.
		} catch (IllegalArgumentException iae) {
			return new FileFormatPair(new File(line), RinearnGraph3DDataFileFormat.AUTO);
		}

		return new FileFormatPair(new File(filePathPart), format);
	}

	/**
	 * Parses the content (multiple lines) of fileListArea.
	 *
	 * @param multiLineContent Parses the content (multiple lines) of fileListArea.
	 * @return The parsed result, the array of the container of the file-path part and data-format part.
	 */
	private FileFormatPair[] parseFileFormatLines(String multiLineContent) {

		// Replace line-feed \r\n & \r codes to \n.
		multiLineContent.replaceAll("\\r\n", "\n");
		multiLineContent.replaceAll("\\r", "\n");

		// Replace continuous line-feeds codes to a single line-feed.
		while (multiLineContent.contains("\n\n")) {
			multiLineContent = multiLineContent.replaceAll("\\n\\n", "\n");
		}
		if (multiLineContent.trim().isEmpty()) {
			return new FileFormatPair[0];
		}

		// Splits the content into lines.
		String[] lines = multiLineContent.split("\\n");
		int lineCount = lines.length;

		// Parse each line, and store result into the following array.
		FileFormatPair[] parsedResults = new FileFormatPair[lineCount];
		for (int iline=0; iline<lineCount; iline++) {
			parsedResults[iline] = this.parseFileFormatLine(lines[iline].trim());
		}
		return parsedResults;
	}


	/**
	 * The event listener handling the event that OPEN button is pressed.
	 */
	private final class OpenButtonEventListener implements ActionListener {

		/** Stores the directory in which the last opened file is contained. */
		private volatile File lastDirectory = new File(".");

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			DataFileOpeningWindow window = view.dataFileOpeningWindow;

			// Prepare message (window title) of the file-chooser window.
			String message = CommunicationMessage.generateCommunicationMessage(CommunicationType.CHOOSE_DATA_FILES);

			// Choose the files to be opened.
			FileDialog fileDialog = new FileDialog(view.dataFileOpeningWindow.frame, message, FileDialog.LOAD);
			fileDialog.setDirectory(this.lastDirectory.getPath());
			fileDialog.setMultipleMode(true);
			fileDialog.setVisible(true);
			File[] files = fileDialog.getFiles();
			int fileCount = files.length;

			// If canceled without choosing any file.
			if (fileCount == 0) {
				return;
			}

			// Get the selected data file format.
			RinearnGraph3DDataFileFormat dataFormat = window.getSelectedDataFileFormat();

			// Get the current list of the files, and replace line feed codes to "\n".
			String currentListText = window.fileListArea.getText();
			currentListText.replaceAll("\\r\\n", "\n");
			currentListText.replaceAll("\\r", "\n");
			if (!currentListText.endsWith("\n") && !currentListText.trim().isEmpty()) {
				currentListText += "\n";
			}

			// Append the selected files to the content of the file list.
			for (int ifile=0; ifile<fileCount; ifile++) {
				String filePath = files[ifile].getAbsolutePath() + ": " + dataFormat.toString();
				currentListText += filePath;
				currentListText += "\n";
			}

			// Update the text area of the file list.
			window.fileListArea.setText(currentListText);
		}
	}


	/**
	 * The event listener handling the event that PLOT button is pressed.
	 */
	private final class PlotButtonEventListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			DataFileOpeningWindow window = view.dataFileOpeningWindow;

			// Gets the content of the file list.
			String fileListText = window.fileListArea.getText().trim();

			// Parse each line into the file-path part and the data-format part.
			FileFormatPair[] parsedResults = parseFileFormatLines(fileListText);
			int fileCount = parsedResults.length;

			// Separate the parsed results into a file-part array and a format-part array.
			File[] files = new File[fileCount];
			RinearnGraph3DDataFileFormat[] formats = new RinearnGraph3DDataFileFormat[fileCount];
			for (int ifile=0; ifile<fileCount; ifile++) {
				files[ifile] = parsedResults[ifile].file;
				formats[ifile] = parsedResults[ifile].format;
			}

			// Plots the files.
			try {
				openDataFiles(files, formats);

			// If any error occurred, display it to users as a pop-up message.
			} catch (IOException ioe) {
				String errorMessage = ioe.getMessage();
				Throwable cause = ioe.getCause();
				if (cause != null) {
					boolean isJapanese = model.config.getEnvironmentConfiguration().isLocaleJapanese();
					if (isJapanese) {
						errorMessage += "\n\n[詳細]\n" + cause.getMessage();
					} else {
						errorMessage += "\n\n[Details]\n" + cause.getMessage();
					}
				}
				JOptionPane.showMessageDialog(view.mainWindow.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
			}

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
			view.dataFileOpeningWindow.fileListArea.setText("");
		}
	}


	/**
	 * The event listener handling the event that the selected item of the "Data Format" combo box is changed.
	 */
	private final class DataFormatBoxEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}

			// Ask the user whether change the deta formats of all the files in the list at once.
			String message = CommunicationMessage.generateCommunicationMessage(CommunicationType.CHANGE_DATA_FORMATS_OF_ALL_LISTED_FILES);
			int decision = JOptionPane.showConfirmDialog(
					view.dataFileOpeningWindow.frame, message, "?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE
			);

			// If the selected option is not "YES", do nothing.
			if (decision != JOptionPane.YES_OPTION) {
				return;
			}

			// Gets the selected data formats of the combo box, and the current file list.
			RinearnGraph3DDataFileFormat selectedFormat = view.dataFileOpeningWindow.getSelectedDataFileFormat();
			String fileListText = view.dataFileOpeningWindow.fileListArea.getText();

			// Parse the current file list (containing the data formats).
			FileFormatPair[] parsedResults = parseFileFormatLines(fileListText);

			// Update the file list with swapping the data-format part.
			fileListText = "";
			for (FileFormatPair parsedResult: parsedResults) {
				fileListText += parsedResult.file + ": " + selectedFormat + "\n";
			}
			view.dataFileOpeningWindow.fileListArea.setText(fileListText);
		}
	}





	// ================================================================================
	//
	// - API Listeners -
	//
	// ================================================================================


	/**
	 * Opens and plots the data file, by inferring the data file format from the file content.
	 *
	 * @param file The data file to be plotted.
	 * @throw FileNotFoundException Thrown if the data file does not exist.
	 * @throw IOException Thrown if it failed to load the data file, due to I/O errors, syntax errors, etc.
	 */
	public void openDataFile(File file) throws FileNotFoundException, IOException {

		// Handle the API request on the event-dispatcher thread.
		DataFileAPIListener apiListener = new DataFileAPIListener(file);
		if (SwingUtilities.isEventDispatchThread()) {
			apiListener.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(apiListener);
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		// If any error has occurred while loading the file, re-throw it to the caller side.
		if (apiListener.hasOccurredException()) {
			throw apiListener.getOccurredException();
		}
	}


	/**
	 * Opens and plots the data file.
	 *
	 * @param file The data file to be plotted.
	 * @param format The format of the data file.
	 * @throw FileNotFoundException Thrown if the data file does not exist.
	 * @throw IOException Thrown if it failed to load the data file, due to I/O errors, syntax errors, etc.
	 */
	public void openDataFile(File file, RinearnGraph3DDataFileFormat format) throws FileNotFoundException, IOException {

		// Handle the API request on the event-dispatcher thread.
		DataFileAPIListener apiListener = new DataFileAPIListener(file, format);
		if (SwingUtilities.isEventDispatchThread()) {
			apiListener.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(apiListener);
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		// If any error has occurred while loading the file, re-throw it to the caller side.
		if (apiListener.hasOccurredException()) {
			throw apiListener.getOccurredException();
		}
	}


	/**
	 * Opens and plots the multiple data files, by inferring the data file format from the file contents.
	 *
	 * @param file The data files to be plotted.
	 * @throw FileNotFoundException Thrown if the data file(s) does not exist.
	 * @throw IOException Thrown if it failed to load the data files, due to I/O errors, syntax errors, etc.
	 */
	public void openDataFiles(File[] files) throws FileNotFoundException, IOException {

		// Handle the API request on the event-dispatcher thread.
		DataFileAPIListener apiListener = new DataFileAPIListener(files);
		if (SwingUtilities.isEventDispatchThread()) {
			apiListener.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(apiListener);
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		// If any error has occurred while loading the file, re-throw it to the caller side.
		if (apiListener.hasOccurredException()) {
			throw apiListener.getOccurredException();
		}
	}


	/**
	 * Opens and plots the multiple data files.
	 *
	 * @param file The data files to be plotted.
	 * @param format The formats of the data files.
	 * @throw FileNotFoundException Thrown if the data file(s) does not exist.
	 * @throw IOException Thrown if it failed to load the data files, due to I/O errors, syntax errors, etc.
	 */
	public void openDataFiles(File[] files, RinearnGraph3DDataFileFormat[] formats) throws FileNotFoundException, IOException {

		// Handle the API request on the event-dispatcher thread.
		DataFileAPIListener apiListener = new DataFileAPIListener(files, formats);
		if (SwingUtilities.isEventDispatchThread()) {
			apiListener.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(apiListener);
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		// If any error has occurred while loading the file, re-throw it to the caller side.
		if (apiListener.hasOccurredException()) {
			throw apiListener.getOccurredException();
		}
	}


	/**
	 * The class handling API requests from openDataFile(...) and openDataFiles(...) methods,
	 * on event-dispatcher thread.
	 */
	private final class DataFileAPIListener implements Runnable {

		/** The data file to be plotted. */
		private final File[] dataFiles;

		/** The format of the data file. */
		private final RinearnGraph3DDataFileFormat[] formats;

		/** The exception which was occurred when the data was loaded last time. */
		private volatile IOException occurredException = null;

		/**
		 * Creates a new instance handling openDataFile(File) method
		 * on event-dispatcher thread.
		 *
		 * @param dataFile The data file to be plotted.
		 */
		public DataFileAPIListener(File dataFile) {
			this(new File[] {dataFile});
		}

		/**
		 * Creates a new instance handling openDataFile(File, RinearnGraph3DDataFileFormat) method
		 * on event-dispatcher thread.
		 *
		 * @param dataFile The data file to be plotted.
		 * @param format The format of the data file.
		 */
		public DataFileAPIListener(File dataFile, RinearnGraph3DDataFileFormat format) {
			this(new File[] {dataFile}, new RinearnGraph3DDataFileFormat[] {format});
		}

		/**
		 * Creates a new instance handling openDataFiles(File[]) method
		 * on event-dispatcher thread.
		 *
		 * @param dataFiles The data files to be plotted.
		 */
		public DataFileAPIListener(File[] dataFiles) {
			int dataFileCount = dataFiles.length;
			this.dataFiles = dataFiles;
			this.formats = new RinearnGraph3DDataFileFormat[dataFileCount];
			Arrays.fill(formats, RinearnGraph3DDataFileFormat.AUTO);
		}

		/**
		 * Creates a new instance handling openDataFiles(File[], RinearnGraph3DDataFileFormat[]) method
		 * on event-dispatcher thread.
		 *
		 * @param dataFiles The data files to be plotted.
		 * @param formats The formats of the data file.
		 */
		public DataFileAPIListener(File[] dataFiles, RinearnGraph3DDataFileFormat[] formats) {
			if (dataFiles.length != formats.length) {
				throw new IllegalArgumentException("The lengths of dataFiles[] and formats[] must be the same.");
			}
			this.dataFiles = dataFiles;
			this.formats = formats;
		}

		/**
		 * Checks whether any exception occurred when loading the data last time.
		 *
		 * @return Return true if any exception occurred.
		 */
		public synchronized boolean hasOccurredException() {
			return this.occurredException != null;
		}

		/**
		 * Gets the exception which occurred when loading the data last time.
		 *
		 * @return Return the exception which occurred when loading the data last time.
		 */
		public synchronized IOException getOccurredException() {
			return this.occurredException;
		}

		/**
		 * Loads the data file.
		 */
		@Override
		public synchronized void run() {
			DataFileIO dataFileIO = new DataFileIO();
			int dataFileCount = this.dataFiles.length;

			// Clear the error info.
			this.occurredException = null;

			// Check that the data files exist.
			for (File file: this.dataFiles) {
				if (!file.exists()) {
					String errorMessage = ErrorMessage.generateErrorMessage(ErrorType.DATA_FILE_NOT_FOUND, file.getName());
					this.occurredException = new FileNotFoundException(errorMessage);
					return;
				}
			}

			// Stores the all data series loaded from the multiple data files into the following data series group.
			DataSeriesGroup<ArrayDataSeries> allDataSeriesGroup = new DataSeriesGroup<ArrayDataSeries>();

			// Load the data files.
			for (int ifile=0; ifile<dataFileCount; ifile++) {
				try {

					// Load the ifile-th data file, and store the result into "allDataSeriesGroup".
					DataSeriesGroup<ArrayDataSeries> loadedDataFromFile = dataFileIO.loadDataFile(dataFiles[ifile], formats[ifile]);
					for (ArrayDataSeries dataSeries: loadedDataFromFile) {
						allDataSeriesGroup.addDataSeries(dataSeries);
					}

				} catch (IOException ioe) {
					String errorMessage = ErrorMessage.generateErrorMessage(ErrorType.FAILED_TO_LOAD_DATA_FILE, this.dataFiles[ifile].getPath());
					this.occurredException = new IOException(errorMessage, ioe);
					return;

				} catch (DataFileFormatException dffe) {
					String errorMessage = ErrorMessage.generateErrorMessage(ErrorType.FAILED_TO_LOAD_DATA_FILE, this.dataFiles[ifile].getPath());
					this.occurredException = new IOException(errorMessage, dffe);
					return;
				}
			}

			// Set the loaded data to Model.
			model.dataStore.setArrayDataSeriesGroup(allDataSeriesGroup);

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

}
