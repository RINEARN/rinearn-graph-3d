package com.rinearn.graph3d.presenter;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.renderer.RinearnGraph3DRenderer;


/**
 * The front-end class of "Presenter" layer (com.rinearn.graph3d.presenter package)
 * of RINEARN Graph 3D.
 * 
 * Components in Presenter layer invokes "Model" layer's procedures triggered by user's action on GUI,
 * and updates the graph screen depending on the result.
 * 
 * Also, in addition to the above normal events, this presenter layer handles API requests,
 * through wrapper method defined in RinearnGraph3D class.
 */
public final class Presenter {

	/** The front-end class of "Model" layer, which provides internal logic procedures and so on. */
	private final Model model;

	/** The front-end class of "View" layer, which provides visible part of GUI without event handling. */
	private final View view;

	/** The rendering engine of 3D graphs. */
	private final RinearnGraph3DRenderer renderer;

	/** The loop which performs rendering and updates the screen, on an independent thread. */
	public final RenderingLoop renderingLoop;

	/** The handler of events of the frame of the main window. */
	public final FrameHandler frameHandler;

	/** The handler of events of the graph screen, such as mouse-dragging events for rotate a graph, etc. */
	public final ScreenHandler screenHandler;

	/** The handler of events and API requests for setting ranges. */
	public final RangeSettingHandler rangeSettingHandler;

	/** The handler of events and API requests for setting labels. */
	public final LabelSettingHandler labelSettingHandler;

	/** The handler of events and API requests for setting camera-related parameters. */
	public final CameraSettingHandler cameraSettingHandler;

	/** The handler of events and API requests for setting scale-related parameters. */
	public final ScaleSettingHandler scaleSettingHandler;


	/**
	 * Creates new Presenter layer of RINEARN Graph 3D.
	 * 
	 * @param model The front-end class of Model layer, which provides internal logic procedures and so on.
	 * @param view The front-end class of View layer, which provides visible part of GUI without event handling.
	 * @param renderer The rendering engine of 3D graphs.
	 */
	public Presenter(Model model, View view, RinearnGraph3DRenderer renderer) {
		this.model = model;
		this.view = view;
		this.renderer = renderer;

		// Create a rendering loop/thread, and start it.
		this.renderingLoop = new RenderingLoop(renderer, view.mainWindow);
		this.renderingLoop.start();

		// Create a handler of events of the frame of the main window.
		this.frameHandler = new FrameHandler(model, view, this);

		// Create a handler of events of the graph screen, handling mouse-dragging events for rotate a graph, etc.
		this.screenHandler = new ScreenHandler(model, view, renderer, renderingLoop);

		// Create handlers for various events and API requests.
		this.rangeSettingHandler = new RangeSettingHandler(model, view, this);
		this.labelSettingHandler = new LabelSettingHandler(model, view, this);
		this.cameraSettingHandler = new CameraSettingHandler(model, view, this);
		this.scaleSettingHandler = new ScaleSettingHandler(model, view, this);
	}


	/**
	 * Reflects the current configuration to the graph.
	 * 
	 * @param requestsReplot Specify true if re-plotting is necessary.
	 */
	public synchronized void reflectUpdatedConfiguration(boolean requestsReplot) {
		RinearnGraph3DConfiguration config = this.model.getConfiguration();
		this.renderer.setConfiguration(config);
		if (requestsReplot) {
			this.replot();			
		}
	}


	/**
	 * Re-plots the contents composing the graph.
	 */
	public synchronized void replot() {

		// Clear all currently drawn contents registered to the renderer.
		this.renderer.clear();

		// Draw basic components (outer frame, scale ticks, etc.) of the graph.
		this.renderer.drawScale();
		this.renderer.drawLabel();
		this.renderer.drawGrid();
		this.renderer.drawFrame();

		// -----
		// Future: Draw other elements here
		// -----

		// Render the re-plotted contents on the screen.
		this.renderer.render();
	}
}
