package com.rinearn.graph3d.presenter.plotter;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.renderer.RinearnGraph3DRenderer;
import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.event.RinearnGraph3DPlottingListener;
import com.rinearn.graph3d.event.RinearnGraph3DPlottingEvent;


/**
 * The "plotter" to plot a point on each coordinate point of data.
 * 
 * A plotter is an object implementing RinearnGraph3DPlottingListener interface,
 * performs a part of plottings/re-plottings (e.g. plots points, or lines, etc),
 * in event-driven flow.
 */
public class PointPlotter implements RinearnGraph3DPlottingListener {

	/** The front-end class of "Model" layer, which provides internal logic procedures and so on. */
	private final Model model;

	/** The front-end class of "View" layer, which provides visible part of GUI without event handling. */
	private final View view;

	/** The front-end class of "Presenter" layer, which invokes Model's procedures triggered by user's action on GUI. */
	private final Presenter presenter;

	/** The rendering engine of 3D graphs. */
	private final RinearnGraph3DRenderer renderer;

	/** The flag for turning on/off this plotter. */
	private volatile boolean plottingEnabled = true;


	/**
	 * Create a new instance performing plottings using the specified resources.
	 * 
	 * @param model The front-end class of "Model" layer, which provides internal logic procedures and so on.
	 * @param view The front-end class of "View" layer, which provides visible part of GUI without event handling.
	 * @param presenter The front-end class of "Presenter" layer, which handles events occurred on GUI, and API requests.
	 * @param renderer The rendering engine of 3D graphs.
	 */
	public PointPlotter(Model model, View view, Presenter presenter, RinearnGraph3DRenderer renderer) {
		this.model = model;
		this.view = view;
		this.presenter = presenter;
		this.renderer = renderer;
	}


	/**
	 * Turns on/off the plottings by this plotter.
	 * 
	 * @param enabled Specify false for turning off the plottings by this plotter.
	 */
	public synchronized void setPlottingEnabled(boolean enabled) {
		this.plottingEnabled = enabled;
	}


	/**
	 * Gets whether the plottings by this plotter is enabled.
	 * 
	 * @return Returns true if the plottings by this plotter is enabled.
	 */
	public synchronized boolean isPlottingEnabled() {
		return this.plottingEnabled;
	}


	/**
	 * Called when a plotting/re-plotting is requested.
	 * 
	 * @param event The plotting event.
	 */
	@Override
	public synchronized void plottingRequested(RinearnGraph3DPlottingEvent event) {
		if (!this.plottingEnabled) {
			return;
		}

		// --------------------------------------------------
		// TODO:
		//     plot points on each coordinate point of data.
		// --------------------------------------------------

		System.out.println("called PointPlotter.plottingRequested");
	}


	/**
	 * Called when the currently requested plotting/re-plotting has been canceled.
	 * 
	 * @param event The plotting event.
	 */
	@Override
	public synchronized void plottingCanceled(RinearnGraph3DPlottingEvent event) {
	}


	/**
	 * Called when the currently requested plotting/re-plotting has completed.
	 * 
	 * @param event The plotting event.
	 */
	@Override
	public synchronized void plottingFinished(RinearnGraph3DPlottingEvent event) {
	}
}
