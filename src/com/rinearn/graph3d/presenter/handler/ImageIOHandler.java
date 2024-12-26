package com.rinearn.graph3d.presenter.handler;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.model.io.ImageFileIO;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import javax.swing.SwingUtilities;

import java.io.File;
import java.io.IOException;

import java.lang.reflect.InvocationTargetException;


/**
 * The class handling events and API requests related to image file I/O.
 */
public final class ImageIOHandler {

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
	public ImageIOHandler(Model model, View view, Presenter presenter) {
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
	 * Gets an Image instance of the screen image.
	 *
	 * The content of the returned Image instance may vary by the change of the content of the graph screen,
	 * depending on the implementation of the renderer currently used.
	 * Hence, the returned Image instance is not suitable for the purpose of editing the rendered graph image on the caller-side.
	 * Its intended purpose is to be displayed on UI components.
	 *
	 * @return The Image instance of the screen image
	 */
	public Image getImage() {

		// Handle the API on the event-dispatcher thread.
		GetImageAPIListener apiListener = new GetImageAPIListener();
		if (SwingUtilities.isEventDispatchThread()) {
			apiListener.run();
			return apiListener.getImage();
		} else {
			try {
				SwingUtilities.invokeAndWait(apiListener);
				return apiListener.getImage();
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}


	/**
	 * The class handling API requests from getImage() method,
	 * on the event-dispatcher thread.
	 */
	private final class GetImageAPIListener implements Runnable {

		/** The Image instance of the graph screen. */
		private volatile Image image;

		/**
		 * Gets the Image instance of the graph screen,
		 * gotten from the renderer in run() method.
		 *
		 * @return The Image instance of the graph screen.
		 */
		public synchronized Image getImage() {
			return this.image;
		}

		/**
		 * Gets the screen's Image instance from the renderer, on the event-dispatcher thread.
		 */
		@Override
		public synchronized void run() {
			this.image = presenter.renderingLoop.getScreenImage();
		}
	}


	/**
	 * Copies an Image instance of the current screen image, and returns it or transfers it to the clipboard.
	 *
	 * This method allocates a buffer, and copies the current screen image to the buffer, and returns its reference.
	 * Hence, the content of the returned Image instance is NOT updated automatically when the screen is re-rendered.
	 *
	 * @param transfersToClipboard Specify true for transferring the copied image to the clipboard.
	 * @return The Image instance storing the copy of the current screen image
	 * @throws IOException Thrown if any error occurred for transferring the copied image to the clipboard.
	 */
	public Image copyImage(boolean transfersToClipboard) throws IOException {

		// Copy and transfer the screen image on the event-dispatcher thread.
		CopyImageAPIListener apiListener = new CopyImageAPIListener(transfersToClipboard);
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

		// If any error has occurred while transferring the copied image, throw it to the caller side.
		if (apiListener.hasOccurredException()) {
			throw apiListener.getOccurredException();
		}
		return apiListener.getCopiedImage();
	}


	/**
	 * The class handling API requests from copyImage(boolean) method,
	 * on the event-dispatcher thread.
	 */
	private final class CopyImageAPIListener implements Runnable, Transferable, ClipboardOwner {

		/** The supported flavor. This method copies images to the clipboard, so DataFlavor.imageFlavor is necessary. */
		private static final DataFlavor SUPPORTED_DATA_FLAVER = DataFlavor.imageFlavor;

		/** The array storing the supported flavor(s), to be returned by getTransferDataFlavors() method.  */
		private static final DataFlavor[] SUPPORTED_DATA_FLAVERS = { SUPPORTED_DATA_FLAVER };

		/** The Image instance of the copy of the graph screen. */
		private volatile Image copiedImage = null;

		/** The flag to transfer the copied image to the clipboard. */
		private volatile boolean transfersToClipboard;

		/** The exception which was occurred when the copied image was transferred last time. */
		private volatile IOException occurredException = null;

		/**
		 * Create a new instance to copy/transfer the screen image.
		 *
		 * @param transfersToClipboard Specify true for transferring the copied image to the clipboard.
		 */
		public CopyImageAPIListener(boolean transfersToClipboard) {
			this.transfersToClipboard = transfersToClipboard;
		}

		/**
		 * Gets the copied Image instance of the graph screen,
		 * gotten from the renderer in run() method.
		 *
		 * @return The Image instance of the graph screen.
		 */
		public synchronized Image getCopiedImage() {
			return this.copiedImage;
		}

		/**
		 * Checks whether any exception occurred when transferring the copied image last time.
		 *
		 * @return Return true if any exception occurred.
		 */
		public synchronized boolean hasOccurredException() {
			return this.occurredException != null;
		}

		/**
		 * Gets the exception which occurred when transferring the copied image last time.
		 *
		 * @return Return the exception which occurred when loading the data last time.
		 */
		public synchronized IOException getOccurredException() {
			return this.occurredException;
		}

		/**
		 * Copies the screen's Image instance of the renderer, on the event-dispatcher thread.
		 *
		 * @throws IllegalStateException Thrown if the clipboard is not available.
		 */
		@Override
		public synchronized void run() {

			// Copy the current screen image.
			this.copiedImage = presenter.renderingLoop.copyScreenImage();

			// If required, transfer the copied image to the clipboard.
			if (this.transfersToClipboard) {

				try {
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(this, this); // Transferable, ClipboardOwner

				// If the above failed to transfer the image to the clipboard, it throws IllegalStateException.
				// So catch it, and wrap it by IOException.
				} catch (IllegalStateException ise) {
					this.occurredException = new IOException("Failed to transfer the copied image to the clipboard.", ise);
				}
			}
		}

		/**
		 * A method of Transferable, copying the screen image and return it to be transferred to the clipboard.
		 */
		@Override
		public Object getTransferData(DataFlavor flavor) throws IOException, UnsupportedFlavorException {
			if (!this.isDataFlavorSupported(flavor)) {
				throw new UnsupportedFlavorException(flavor);
			}

			// CAUTION:
			//     The image has already been copied in run() method, and then,
			//     the subsequent code in the run() method requests transferring the copied image to the clipboard.
			//     So don't copy the image again here as follows:
			// this.copiedImage = presenter.renderingLoop.copyScreenImage();

			return this.copiedImage;
		}

		/**
		 * A method of Transferable, returning the array of available data flavors.
		 */
		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return SUPPORTED_DATA_FLAVERS;
		}

		/**
		 * A method of Transferable, returns whether the specified data flavors is available.
		 */
		@Override
		public boolean isDataFlavorSupported(DataFlavor dataFlavorToBeCheced) {
			boolean isSupportedFlavor = dataFlavorToBeCheced.equals(SUPPORTED_DATA_FLAVER);
			return isSupportedFlavor;
		}

		/**
		 * A method of ClipboardOwner.
		 */
		@Override
		public void lostOwnership(Clipboard clipboard, Transferable contents) {
		}
	}



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
