package com.rinearn.graph3d.presenter;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.renderer.RinearnGraph3DRenderer;
import com.rinearn.graph3d.view.View;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;
import javax.swing.ImageIcon;


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
			externBuffer = bufferCurrentScreenImage(externBuffer);
			this.image = externBuffer.image;
		}
	}


	/**
	 * Buffers (copies) the current screen image.
	 *
	 * @param lastBuffer The buffer used last time by this method (reused if possible).
	 * @return The buffered image of the current screen image, and its graphics context.
	 */
	private synchronized BufferedResources bufferCurrentScreenImage(BufferedResources lastBuffer) {

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
			BufferedImage newImage = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
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
