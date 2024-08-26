package com.rinearn.graph3d.presenter.handler;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.view.View;

import com.rinearn.graph3d.RinearnGraph3DDataFileFormat;
import com.rinearn.graph3d.model.io.DataFileIO;
import com.rinearn.graph3d.model.io.DataFileFormatException;
import com.rinearn.graph3d.model.dataseries.DataSeriesGroup;
import com.rinearn.graph3d.model.dataseries.ArrayDataSeries;
import com.rinearn.graph3d.def.ErrorType;
import com.rinearn.graph3d.def.ErrorMessage;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;


/**
 * The class handling events and API requests for plotting data files.
 */
public final class DataFileHandler {

	/** The front-end class of "Model" layer, which provides internal logic procedures and so on. */
	private final Model model;

	/** The front-end class of "View" layer, which provides visible part of GUI without event handling. */
	@SuppressWarnings("unused")
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
	public DataFileHandler(Model model, View view, Presenter presenter) {
		this.model = model;
		this.view = view;
		this.presenter = presenter;
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
	// - API Listeners -
	//
	// ================================================================================


	/**
	 * Opens and plots the data file, by inferring the data file format from the file content.
	 *
	 * @param file The data file to be plotted.
	 * @throw IOException Thrown if it failed to load the data file, due to I/O errors, syntax errors, etc.
	 */
	public void openDataFile(File file) throws IOException {

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
	 * @throw IOException Thrown if it failed to load the data file, due to I/O errors, syntax errors, etc.
	 */
	public void openDataFile(File file, RinearnGraph3DDataFileFormat format) throws IOException {

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
	 * @throw IOException Thrown if it failed to load the data files, due to I/O errors, syntax errors, etc.
	 */
	public void openDataFile(File[] files) throws IOException {

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
	 * @throw IOException Thrown if it failed to load the data files, due to I/O errors, syntax errors, etc.
	 */
	public void openDataFile(File[] files, RinearnGraph3DDataFileFormat[] formats) throws IOException {

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

			// Stores the all data series loaded from the multiple data files into the following data series group.
			DataSeriesGroup<ArrayDataSeries> allDataSeriesGroup = new DataSeriesGroup<ArrayDataSeries>();

			// Load the data files.
			for (int ifile=0; ifile<dataFileCount; ifile++) {
				try {

					// Load the ifile-th data file, and store the result into "allDataSeriesGroup".
					DataSeriesGroup<ArrayDataSeries> loadedDataFromFile = dataFileIO.loadDataFile(dataFiles[ifile], formats[ifile]);
					for (ArrayDataSeries dataSeries: loadedDataFromFile.getDataSeriesList()) {
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
			model.setArrayDataSeriesGroup(allDataSeriesGroup);

			// Re-plot the graph.
			presenter.plot();
		}
	}

}
