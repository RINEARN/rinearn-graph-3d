package com.rinearn.graph3d.presenter;

import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.renderer.RinearnGraph3DRenderer;


/**
 * The front-end class of "Presenter" layer (com.rinearn.graph3d.presenter package)
 * of RINEARN Graph 3D.
 * 
 * Components in Presenter layer invokes "Model" layer's procedures triggered by user's action on GUI,
 * and updates the graph screen depending on the result.
 */
public class Presenter {

	/** The front-end class of "View" layer, which provides visible part of GUI without event handling. */
	private final View view;

	/** The rendering engine of 3D graphs. */
	private final RinearnGraph3DRenderer renderer;

	/** The loop which performs rendering and updates the screen, on an independent thread. */
	public final RenderingLoop renderingLoop;


	/**
	 * Creates new Presenter layer of RINEARN Graph 3D.
	 * 
	 * @param view
	 * @param renderer
	 */
	public Presenter(View view, RinearnGraph3DRenderer renderer) {
		this.view = view;
		this.renderer = renderer;

		// Create a rendering loop/thread, and start it.
		this.renderingLoop = new RenderingLoop(renderer, view.mainWindow);
		this.renderingLoop.start();
	}
}
