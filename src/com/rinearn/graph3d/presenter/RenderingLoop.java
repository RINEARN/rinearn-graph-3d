package com.rinearn.graph3d.presenter;

import com.rinearn.graph3d.renderer.RinearnGraph3DRenderer;
import com.rinearn.graph3d.view.MainWindow;

import java.awt.Image;


/**
 * The class which invokes rendering procedures on an independent thread when it is requested,
 * and updates the graph screen on the window.
 */
public final class RenderingLoop implements Runnable {

	/** The interval wait [md] per one cycle of the rendering loop. */
	private static final int LOOP_WAIT = 30;

	/** The rendering engine of 3D graphs. */
	private final RinearnGraph3DRenderer renderer;

	/** The main window of RINEARN Graph 3D. */
	private final MainWindow mainWindow;

	/** The flag for continuing the loop, or exit from it. */
	private volatile boolean continuing = true;

	/** The flag representing that the loop had exited successfully. */
	private volatile boolean exitedSuccessfully = false;

	/** The flag representing that invoking render() on the loop thread has been requested. */
	private volatile boolean renderingRequested = false;


	/**
	 * Creates new rendering loop.
	 * 
	 * @param renderer The rendering engine of 3D graphs.
	 * @param mainWindow The main window of RINEARN Graph 3D.
	 */
	public RenderingLoop(RinearnGraph3DRenderer renderer, MainWindow mainWindow) {
		this.renderer = renderer;
		this.mainWindow = mainWindow;
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
	 * The procedures of the rendering loop, which runs on an independent thread.
	 */
	@Override
	public void run() {
		while (this.continuing) {

			synchronized (this) {
				if (this.renderingRequested) {
					this.renderingRequested = false;
					this.renderer.render();
				}
			}

			if (this.renderer.casScreenResized(true, false)) {
				Image screenImage = this.renderer.getScreenImage();
				this.mainWindow.setScreenImage(screenImage);
			}

			if (this.renderer.casScreenUpdated(true, false)) {
				this.mainWindow.repaintScreen();
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
