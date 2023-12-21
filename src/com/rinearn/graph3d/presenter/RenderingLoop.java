package com.rinearn.graph3d.presenter;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.renderer.RinearnGraph3DRenderer;
import com.rinearn.graph3d.view.View;

import java.awt.Image;


/**
 * The class which invokes rendering procedures on an independent thread when it is requested,
 * and updates the graph screen on the window.
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

	/** The flag representing that invoking replot() on the loop thread has been requested. */
	private volatile boolean replottingRequested = false;


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
	 * Requests invoking replot() method of the Presenter on the thread of this rendering loop.
	 *
	 * After requesting it by this method, replot() method is performed asynchronously.
	 * When the replotting is complete, the graph screen of the window is updated automatically.
	 */
	public synchronized void requestReplotting() {
		this.replottingRequested = true;
	}


	/**
	 * The procedures of the rendering loop, which runs on an independent thread.
	 */
	@Override
	public void run() {
		while (this.continuing) {

			synchronized (this) {
				if (this.replottingRequested) {
					this.replottingRequested = false;
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
}
