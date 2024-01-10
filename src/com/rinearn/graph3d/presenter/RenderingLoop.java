package com.rinearn.graph3d.presenter;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.renderer.RinearnGraph3DRenderer;
import com.rinearn.graph3d.view.View;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import java.io.File;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;


/**
 * The class which invokes rendering procedures on an independent thread when it is requested,
 * and updates the graph screen on the window.
 *
 * Also, this class handles some API requests related to rendered images, e.g.: getImage().
 */
public final class RenderingLoop implements Runnable {

	/** The interval wait [md] per one cycle of the rendering loop. */
	private static final int LOOP_WAIT = 30;

	/** The front-end class of "Model" layer, which provides internal logic procedures and so on. */
	@SuppressWarnings("unused")
	private final Model model;

	/** The front-end class of "View" layer, which provides visible part of GUI without event handling. */
	private final View view;

	/** The front-end class of "Presenter" layer, which invokes Model's procedures triggered by user's action on GUI. */
	private final Presenter presenter;

	/** The rendering engine of 3D graphs. */
	private final RinearnGraph3DRenderer renderer;

	/** The flag for continuing the loop, or exit from it. */
	private volatile boolean continuing = true;

	/** The flag representing that the loop had exited successfully. */
	private volatile boolean exitedSuccessfully = false;

	/** The flag representing that invoking render() on the loop thread has been requested. */
	private volatile boolean renderingRequested = false;

	/** The flag representing that invoking plot() on the loop thread has been requested. */
	private volatile boolean plottingRequested = false;

	/** The container class storing an buffered image and its graphics context. */
	private class BufferedResources {

		/**
		 * Creates an new instance storing the specified resources.
		 * @param image The buffered image instance.
		 * @param graphics The graphics context of the above "image".
		 */
		public BufferedResources(BufferedImage image, Graphics2D graphics) {
			this.image = image;
			this.graphics = graphics;
		}
		public final BufferedImage image;
		public final Graphics2D graphics;
	}

	/** The image/graphics buffered to be referred from the outside of the app, through getImage() API. */
	private volatile BufferedResources externBuffer;


	/**
	 * Creates new rendering loop.
	 *
	 * @param model The front-end class of "Model" layer, which provides internal logic procedures and so on.
	 * @param view The front-end class of "View" layer, which provides visible part of GUI without event handling.
	 * @param presenter The front-end class of "Presenter" layer, which handles events occurred on GUI, and API requests.
	 * @param renderer The rendering engine of 3D graphs.
	 */
	public RenderingLoop(Model model, View view, Presenter presenter, RinearnGraph3DRenderer renderer) {
		this.model = model;
		this.view = view;
		this.presenter = presenter;
		this.renderer = renderer;
	}


	/**
	 * Starts the rendering loop (on an independent thread).
	 */
	public synchronized void start() {
		Thread thread = new Thread(this);
		thread.start();
	}


	/**
	 * Request to exits the rendering loop.
	 *
	 * For checking that the rendering loop has exited successfully after this request,
	 * use isExitedSuccessfully() method.
	 */
	public synchronized void exit() {
		this.continuing = false;
	}


	/**
	 * Returns whether the rendering loop had exited successfully.
	 *
	 * @return Returns true if the rendering loop had exited successfully.
	 */
	public synchronized boolean isExitedSuccessfully() {
		return this.exitedSuccessfully;
	}


	/**
	 * Requests invoking render() method of the renderer on the thread of this rendering loop.
	 *
	 * After requesting it by this method, render() method is performed asynchronously.
	 * When the rendering is complete, the graph screen of the window is updated automatically.
	 */
	public synchronized void requestRendering() {
		this.renderingRequested = true;
	}


	/**
	 * Requests invoking plot() method of the Presenter on the thread of this rendering loop.
	 *
	 * After requesting it by this method, replot() method is performed asynchronously.
	 * When the replotting is complete, the graph screen of the window is updated automatically.
	 */
	public synchronized void requestPlotting() {
		this.plottingRequested = true;
	}


	/**
	 * The procedures of the rendering loop, which runs on an independent thread.
	 */
	@Override
	public void run() {
		while (this.continuing) {

			synchronized (this) {
				if (this.plottingRequested) {
					this.plottingRequested = false;
					this.presenter.plot();
				}
			}

			synchronized (this) {
				if (this.renderingRequested) {
					this.renderingRequested = false;
					this.renderer.render();
				}
			}

			if (this.renderer.casScreenResized(true, false)) {
				Image screenImage = this.renderer.getScreenImage();
				view.mainWindow.setScreenImage(screenImage);
			}

			if (this.renderer.casScreenUpdated(true, false)) {
				view.mainWindow.repaintScreen();
			}

			try {
				Thread.sleep(LOOP_WAIT);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
				break;
			}
		}

		synchronized (this) {
			this.exitedSuccessfully = true;
		}
	}





	// ================================================================================
	//
	// - API Listeners -
	//
	// ================================================================================


	/**
	 * Gets an Image instance storing the current screen image.
	 *
	 * This method copies the current screen image to a buffer, and returns the reference to the buffer.
	 * Hence, the content of the returned Image instance is NOT updated automatically when the screen is re-rendered.
	 * To update the content of the image, re-call this method.
	 *
	 * Please note that, the returned Image instance changes when the screen is resized,
	 * because it requires to reallocate the buffer.
	 *
	 * @return The Image instance storing the current screen image.
	 */
	public Image getImage() {

		// There is no need to add "synchronized" here.
		// The timing-critical processing to copy the screen image to the buffer
		// is implemented as a "synchronized" method: bufferCurrentScreenImage(-).

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
	private class GetImageAPIListener implements Runnable {

		/** The Image instance of the graph screen. */
		private volatile Image image;

		/**
		 * Gets the Image instance of the graph screen,
		 * gotten from the renderer in run() method.
		 *
		 * @return The Image instance of the graph screen.
		 */
		public Image getImage() {
			return this.image;
		}

		/**
		 * Gets the Image instance from the renderer, on the event-dispatcher thread.
		 */
		@Override
		public void run() {

			// Copy the current screen image to the buffer, and store the result to the "image" field.
			externBuffer = bufferCurrentScreenImage(externBuffer, true);
			this.image = externBuffer.image;
		}
	}


	/**
	 * Exports the current screen image to a image file.
	 *
	 * @param file The file to be written.
	 * @param quality The quality of the image file (from 0.0 to 1.0, or from 1.0 to 100.0).
	 * @throws IOException Thrown if any error occurred for writing the image file.
	 */
	public void exportImageFile(File file, double quality) throws IOException {

		// There is no need to add "synchronized" here.
		// The timing-critical processing to copy the screen image to the buffer
		// is implemented as a "synchronized" method: bufferCurrentScreenImage(-).

		// Handle the API on the event-dispatcher thread.
		ExportImageAPIListener apiListener;
		try {
			apiListener = new ExportImageAPIListener(file, quality);
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
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
		if (apiListener.hasIOException()) {
			throw apiListener.getIOException();
		}
	}


	/**
	 * The class handling API requests from exportImage(-) method,
	 * on the event-dispatcher thread.
	 */
	private class ExportImageAPIListener implements Runnable {

		/** The file to be written. */
		private final File file;

		/** The name of the image format. */
		private final String formatName;

		/** The quality of the image file (from 0.0 to 1.0, or from 1.0 to 100.0). */
		private final double quality;

		/** Stores the IOException occurred when writing a image file. */
		private volatile IOException ioException = null;

		/**
		 * Create a new instance writing the specified image file with the specified quality.
		 *
		 * @param file The file to be written.
		 * @param quality The quality of the image file (from 0.0 to 1.0, or from 1.0 to 100.0).
		 * @throws IllegalArgumentException Thrown if the specified format is unsupported, or the quality is out of range.
		 */
		public ExportImageAPIListener(File file, double quality) throws IllegalArgumentException {
			this.file = file;
			String fileName = file.getName().toLowerCase();

			if (fileName.endsWith(".png")) {
				this.formatName = "png";
			} else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
				this.formatName = "jpg";
			} else if (fileName.endsWith(".bmp")) {
				this.formatName = "bmp";
			} else {
				throw new IllegalArgumentException("Unsupported Image Format: " + fileName);
			}

			if (quality < 0.0 || 100.0 < quality) {
				throw new IllegalArgumentException("The specified quality is out of range: " + quality);
			}
			this.quality = (quality <= 1.0) ? quality : (quality / 100.0);
		}

		/**
		 * Checks whether any IOException occurred when this instance wrote the image file.
		 *
		 * @return Returns true if any IOException occurred.
		 */
		public boolean hasIOException() {
			return this.ioException != null;
		}

		/**
		 * Gets the IOException which occurred when this instance wrote the image file.
		 *
		 * @return The IOException which occurred when this instance wrote the image file.
		 */
		public IOException getIOException() {
			return this.ioException;
		}

		/**
		 * Gets the Image instance from the renderer, on the event-dispatcher thread.
		 */
		@Override
		public void run() {

			// Check whether alpha-channel is available on the specified image format.
			boolean usesAlphaChannel = this.formatName.equals("png");

			// Copy the current screen image to a buffer, and store the result to the "image" field.
			BufferedResources buffer = bufferCurrentScreenImage(null, usesAlphaChannel);

			if (this.quality < 1.0 && this.formatName.equals("jpg")) {
				boolean wrote = false;

				// Get all the "ImageWriter"s which can write a JPEG image.
				Iterator<ImageWriter> imageWriterIterator = ImageIO.getImageWritersByFormatName(this.formatName);
				while (!wrote && imageWriterIterator.hasNext()) {
					ImageWriter imageWriter = imageWriterIterator.next();
					ImageWriteParam imageWriterParam = imageWriter.getDefaultWriteParam();

					// If the above ImageWrite can write a JPEG image with the specified quality,
					// write it and break from this loop.
					if (imageWriterParam.canWriteCompressed()) {
						imageWriterParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
						imageWriterParam.setCompressionQuality((float)this.quality);
						IIOImage iioImage = new IIOImage(
								(BufferedImage)buffer.image,
								null, // A List of thumbnails: we can specify null.
								null // IIOMetadata: we can specify null.
						);
						try (ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(this.file)) {
							imageWriter.setOutput(imageOutputStream);
							imageWriter.write(
									null, // IIOMetadata: we can specify null, to use the default one.
									iioImage,
									imageWriterParam
							);
							wrote = true;
						} catch (IOException e) {
							this.ioException = e;
						}
					}
					imageWriter.dispose();
				}

				// If no ImageWriter available for writing the JPEG image with the specified quality has been found:
				if (!wrote && this.ioException == null) {
					this.ioException = new IOException(
							"Can not write the specified image file with the specified quality in this environment."
					);
				}
				return;

			// For other formats, we can simply write the image file by ImageIO.write(-).
			} else {
				try {
					ImageIO.write(buffer.image, this.formatName, this.file);
				} catch (IOException ioe) {
					this.ioException = ioe;
					return;
				}
			}
		}
	}


	/**
	 * Buffers (copies) the current screen image.
	 *
	 * @param lastBuffer The buffer used last time by this method (reused if possible), or specify null to allocate a new buffer.
	 * @param usesAlphaChannel Specify true if the alpha channel is necessary (for expressing transparent colors).
	 * @return The buffered image of the current screen image, and its graphics context.
	 */
	private synchronized BufferedResources bufferCurrentScreenImage(
			BufferedResources lastBuffer, boolean usesAlphaChannel) {

		// Get the current screen image.
		Image screenImage = this.renderer.getScreenImage();
		ImageIcon screenIcon = new ImageIcon(screenImage); // Having an ImageObserver internally.
		int screenWidth = screenIcon.getIconWidth();
		int screenHeight = screenIcon.getIconHeight();

		// Reallocate the buffer, if its size does not match with the current screen size.
		BufferedResources buffer = lastBuffer;
		if (buffer == null || buffer.image.getWidth() != screenWidth || buffer.image.getHeight() != screenHeight) {
			if (buffer != null) {
				buffer.graphics.dispose();
			}
			BufferedImage newImage = usesAlphaChannel ?
					new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB):
					new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D newGraphics = newImage.createGraphics();
			buffer = new BufferedResources(newImage, newGraphics);
		}

		// Set all the pixels of the buffer to the clear color (of which alpha-component is zero).
		// (Necessary because the background color of the screen can be transparent.)
		buffer.graphics.clearRect(0, 0, screenWidth, screenHeight);

		// Copy the current screen image to the buffer.
		boolean drawImageCompleted = false;
		while (!drawImageCompleted) {
			drawImageCompleted = buffer.graphics.drawImage(screenImage, 0, 0, screenWidth, screenHeight, null);
			try {
				Thread.sleep(10);
			} catch (InterruptedException ie) {
				throw new RuntimeException(ie);
			}
		}
		return buffer;
	}
}
