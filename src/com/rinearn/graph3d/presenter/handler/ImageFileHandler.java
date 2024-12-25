                                                  package com.rinearn.graph3d.presenter.handler;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.model.io.ImageFileIO;

import java.awt.Image;
import javax.swing.SwingUtilities;

import java.io.File;
import java.io.IOException;

import java.lang.reflect.InvocationTargetException;


/**
 * The class handling events and API requests related to image file I/O.
 */
public final class ImageFileHandler {

	/** The front-end class of "Model" layer, which provides internal logic procedures and so on. */
	@SuppressWarnings("unused")
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
	public ImageFileHandler(Model model, View view, Presenter presenter) {
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
	 * Exports (saves) the current graph screen to an image file .
	 * The image file format is automatically determined from the extension of the file to be saved.
	 * In this version, JPEG (.jpg), PNG (.png), and BMP (.bmp) formats are available.
	 *
	 * @param file The image file to be saved.
	 * @param quality The quality of the image file (from 0.0 to 1.0).
	 * @throw IOException Thrown if it failed to save the image file.
	 */
	public void exportImageFile(File file, double quality) throws IOException {

		// Handle the API request on the event-dispatcher thread.
		ExportImageFileAPIListener apiListener = new ExportImageFileAPIListener(file, quality);
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
	 * The class handling API requests from exportImageFile(...) method,
	 * on event-dispatcher thread.
	 */
	private final class ExportImageFileAPIListener implements Runnable {

		/** The image file to be saved. */
		private final File imageFile;

		/** The quality of the image file. */
		private final double quality;

		/** The exception which was occurred when the image file was saved last time. */
		private volatile IOException occurredException = null;

		/**
		 * Creates a new instance handling exportImageFile(File, double) method
		 * on event-dispatcher thread.
		 *
		 * @param imageFile The image file to be saved.
		 * @param quality The quality of the image file.
		 */
		public ExportImageFileAPIListener(File imageFile, double quality) {
			this.imageFile = imageFile;
			this.quality = quality;
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
		 * Saves the image file.
		 */
		@Override
		public synchronized void run() {

			// Copy the current image of the graph screen.
			Image screenImage = presenter.renderingLoop.copyScreenImage();

			// Save the above image as the specified image file.
			ImageFileIO imageFileIO = new ImageFileIO();
			try {
				imageFileIO.saveImageFile(screenImage, this.imageFile, this.quality);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

}
