package com.rinearn.graph3d.presenter;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.renderer.RinearnGraph3DRenderer;
import com.rinearn.graph3d.view.View;

import java.awt.Image;
import java.awt.image.BufferedImage;


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
	 * Disposes the resources.
	 */
	public synchronized void dispose() {
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
	 * Gets the screen image (to be displayed on the window, may vary in real-time).
	 *
	 * @return The screen image.
	 */
	public synchronized Image getScreenImage() {
		return this.renderer.getScreenImage();
	}


	/**
	 * Creates a deep-copy of the current screen image.
	 *
	 * This method allocates a buffer (BufferedImage), and copies the content of the graph screen to it, and returns it.
	 * The allocation of the buffer requires a certain overhead cost, so if frequently copy the graph screen, consider using
	 * {com.rinearn.graph3d.renderer.RinearnGraph3DRenderer.copyScreenImage(BufferedImage,Graphics2D) RinearnGraph3DRenderer.copyScreenImage(BufferedImage,Graphics2D)}
	 * method instead.
	 *
	 * @param bufferedImageType The type of the BufferedImage to be returned (e.g.: BufferedImage.TYPE_INT_ARGB, TYPE_INT_RGB, etc.)
	 * @return The created deep-copy of the current screen image.
	 */
	public synchronized BufferedImage copyScreenImage(int bufferedImageType) {

		// Note:
		// Should we implement the deep-copying process here rather than in the renderer?
		// And then, should we remove copyScreenImage() from the renderer because it is redundant?
		//
		// -> No.
		//
		//    The renderer can be accessed directly via API from other threads, not only via this rendering loop/thread.
		//    So, to keep the deep-copying process of the screen thread-safe,
		//    the current design (the renderer provides copyScreenImage()) is correct.
		//
		//    Otherwise, if the screen image is copied while an external program is drawing something via renderer's API,
		//    the image may be imperfect.

		return this.renderer.copyScreenImage(bufferedImageType);
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

				// 2D drawn contents lost when the screen is resized, so re-draw them.
				this.renderer.drawColorBar();
				this.renderer.drawLegendLabels();
				this.renderer.compositeLayers();

				// ↑ これ、ユーザーが独自に前景2Dコンテンツを取捨選択して描きたい場合に、画面サイズを変えたら強制的にカラーバーと凡例が描かれてしまう。
				//    なんか新しい枠組みが必用。
				//
				//    -> それはもうユーザー描きこみ用の新しいバッファを1枚入れたら？
				//       -> 仮にそうしても、「clear() したのにリサイズでカラーバーや凡例が復活してしまう」の解決にはならない。別の話だ。

				// Update the reference to the screen image, and repaint the screen.
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
