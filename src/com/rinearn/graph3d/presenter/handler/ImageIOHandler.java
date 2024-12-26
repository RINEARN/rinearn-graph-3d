package com.rinearn.graph3d.presenter.handler;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.view.ImageSavingWindow;
import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.model.io.ImageFileIO;
import com.rinearn.graph3d.def.ErrorType;
import com.rinearn.graph3d.def.CommunicationMessage;
import com.rinearn.graph3d.def.CommunicationType;
import com.rinearn.graph3d.def.ErrorMessage;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.JFileChooser;

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
	private final View view;

	/** The front-end class of "Presenter" layer, which invokes Model's procedures triggered by user's action on GUI. */
	private final Presenter presenter;

	/** The flag for turning on/off the event handling feature of this instance. */
	private volatile boolean eventHandlingEnabled = true;

	/** The path of the last selected directory to save the image file. */
	private volatile String lastDirectoryPath = ".";


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

		// Add the action listeners to RightClick > "Copy Image" menu item.
		view.mainWindow.copyImageMenuItem.addActionListener(new CopyImageMenuEventListener());

		// Add the action listener to the "Open" button on "File" > "Save Image" window is clicked.
		view.imageSavingWindow.fileLocationButton.addActionListener(new FileLocationButtonEventListener());

		// Add the action listener to the "SAVE" button on "File" > "Save Image" window is clicked.
		view.imageSavingWindow.saveButton.addActionListener(new SaveButtonEventListener());
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
	 * The event listener handling the event that the Right-click > "Copy Image" menu is clicked.
	 */
	private final class CopyImageMenuEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}

			// Call the implementation of copyImage(int,boolean) API, provided by this class.
			// (It is processed on the event-dispatcher thread,
			//  so there is no need to wrap the followings by SwingUtilities.invokeAndWait(...))
			try {
				boolean transfersToClipboard = true;
				int bufferedImageType = BufferedImage.TYPE_INT_RGB;

				// Depending on the environment, if the copied image has the alpha-channel,
				// a warning (not exception) occurs, and we can not catch and handle it.
				//
				//   int bufferedImageType = BufferedImage.TYPE_INT_ARGB;

				copyImage(bufferedImageType, transfersToClipboard);

			} catch (IOException ioe) {
				ioe.printStackTrace();
				String errorMessage = ErrorMessage.generateErrorMessage(ErrorType.FAILED_TO_COPY_IMAGE_TO_CLIPBOARD);
				JOptionPane.showMessageDialog(view.mainWindow.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
			}
		}
	}


	/**
	 * The event listener handling the event that the "Open" button on "File" > "Save Image" window is clicked.
	 */
	private final class FileLocationButtonEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}

			// Choose the directory to save the image file.
			// NOTE: Using JFileChooser instead of FileDialog, because the latter does not support DIRECTORIES_ONLY mode.
			JFileChooser fileChooser = new JFileChooser(lastDirectoryPath);
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileChooser.showOpenDialog(view.imageSavingWindow.frame);
			File file = fileChooser.getSelectedFile();
			if (file == null) {
				return;
			}

			// Set the path of the selected directory to the text field on the window.
			String selectedDirectoryPath = file.getAbsolutePath();
			view.imageSavingWindow.fileLocationField.setText(selectedDirectoryPath);
			lastDirectoryPath = selectedDirectoryPath;
		}
	}


	/**
	 * The event listener handling the event that the "SAVE" button on "File" > "Save Image" window is clicked.
	 */
	private final class SaveButtonEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			ImageSavingWindow window = view.imageSavingWindow;

			// Gets the name, location, and format of the image file to be saved.
			String fileName = window.fileNameField.getText();
			String fileLocation = window.fileLocationField.getText();
			String format = window.fileFormatBox.getSelectedItem().toString();

			// Append the extension depending on the file format, if necessary.
			// Additionally, decide the suitable type of the BufferedImage for the format.
			int bufferedImageType;
			switch (format) {
				case ImageSavingWindow.FILE_FORMAT_PNG : {
					if (!fileName.toLowerCase().endsWith(".png")) {
						fileName += ".png";
					}
					bufferedImageType = BufferedImage.TYPE_INT_ARGB;
					break;
				}
				case ImageSavingWindow.FILE_FORMAT_BMP : {
					if (!fileName.toLowerCase().endsWith(".bmp")) {
						fileName += ".bmp";
					}
					bufferedImageType = BufferedImage.TYPE_INT_RGB;
					break;
				}
				case ImageSavingWindow.FILE_FORMAT_JPEG : {
					if (!fileName.toLowerCase().endsWith(".jpg") && !fileName.toLowerCase().endsWith(".jpeg")) {
						fileName += ".jpg";
					}
					bufferedImageType = BufferedImage.TYPE_INT_RGB;
					break;
				}
				default : {
					throw new IllegalStateException("Unexpected image file format: " + format);
				}
			}

			// Get the quality of the image file.
			double quality = 100.0;
			try {
				// Convert the input text to double-type value.
				quality = Double.parseDouble(window.qualityField.getText());

				// Convert the range of the value from [0.0, 100.0] to [0.0, 1.0].
				quality /= 100.0;
				if (quality < 0.0 || 1.0 < quality) {
					String errorMessage = ErrorMessage.generateErrorMessage(ErrorType.INVALID_IMAGE_FILE_QUALITY);
					JOptionPane.showMessageDialog(view.mainWindow.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
					window.qualityField.setText("100");
					return;
				}

			} catch (NumberFormatException nfe) {
				String errorMessage = ErrorMessage.generateErrorMessage(ErrorType.INVALID_IMAGE_FILE_QUALITY);
				JOptionPane.showMessageDialog(view.mainWindow.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
				window.qualityField.setText("100");
				return;
			}

			// Concatenate the file location and the file name, and convert its path to the absolute path.
			File file = new File(fileLocation, fileName);
			try {
				file = new File(file.getCanonicalPath());
			} catch (IOException ioe) {
				file = new File(file.getAbsolutePath());
			}

			// If the file already exists, confirm whether overwrite it.
			if (file.exists()) {
				String confirmationMessage = CommunicationMessage.generateCommunicationMessage(
						CommunicationType.DO_YOU_WANT_TO_OVERWRITE_IMAGE, file.getPath()
				);
				int option = JOptionPane.showConfirmDialog(
						view.mainWindow.frame, confirmationMessage, "!", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE
				);
				if (option != JOptionPane.YES_OPTION) {
					return;
				}
			}

			// Copy the current image of the graph screen.
			Image screenImage = presenter.renderingLoop.copyScreenImage(bufferedImageType);

			// Save the image file.
			ImageFileIO imageFileIO = new ImageFileIO();
			try {
				imageFileIO.saveImageFile(screenImage, file, quality);

			} catch (IOException ioe) {
				String errorMessage = ErrorMessage.generateErrorMessage(ErrorType.FAILED_TO_SAVE_IMAGE);
				JOptionPane.showMessageDialog(view.mainWindow.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
				ioe.printStackTrace();
				return;
			}

			// Show the saved file path to the user.
			String succeededMessage = CommunicationMessage.generateCommunicationMessage(
					CommunicationType.SUCCEEDED_TO_SAVE_IMAGE, file.getPath()
			);
			JOptionPane.showMessageDialog(view.mainWindow.frame, succeededMessage, "", JOptionPane.PLAIN_MESSAGE);
		}
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
	 * @param bufferedImageType The type of the buffered image to be returned by the API (e.g.: BufferedImage.TYPE_INT_ARGB, TYPE_INT_RGB, etc.)
	 * @param transfersToClipboard Specify true for transferring the copied image to the clipboard.
	 * @return The Image instance storing the copy of the current screen image
	 * @throws IOException Thrown if any error occurred for transferring the copied image to the clipboard.
	 */
	public BufferedImage copyImage(int bufferedImageType, boolean transfersToClipboard) throws IOException {

		// Copy and transfer the screen image on the event-dispatcher thread.
		CopyImageAPIListener apiListener = new CopyImageAPIListener(bufferedImageType, transfersToClipboard);
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
		private volatile BufferedImage copiedImage = null;

		/** The type of the buffered image to be returned by the API (e.g.: BufferedImage.TYPE_INT_ARGB, TYPE_INT_RGB, etc.) */
		private final int bufferedImageType;

		/** The flag to transfer the copied image to the clipboard. */
		private final boolean transfersToClipboard;

		/** The exception which was occurred when the copied image was transferred last time. */
		private volatile IOException occurredException = null;

		/**
		 * Create a new instance to copy/transfer the screen image.
		 *
		 * @param bufferedImageType The type of the buffered image to be returned by the API (e.g.: BufferedImage.TYPE_INT_ARGB, TYPE_INT_RGB, etc.)
		 * @param transfersToClipboard Specify true for transferring the copied image to the clipboard.
		 */
		public CopyImageAPIListener(int bufferedImageType, boolean transfersToClipboard) {
			this.bufferedImageType = bufferedImageType;
			this.transfersToClipboard = transfersToClipboard;
		}

		/**
		 * Gets the copied Image instance of the graph screen,
		 * gotten from the renderer in run() method.
		 *
		 * @return The Image instance of the graph screen.
		 */
		public synchronized BufferedImage getCopiedImage() {
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
			this.copiedImage = presenter.renderingLoop.copyScreenImage(this.bufferedImageType);

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
			BufferedImage screenImage = presenter.renderingLoop.copyScreenImage(BufferedImage.TYPE_INT_ARGB);

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
