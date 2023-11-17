package com.rinearn.graph3d.presenter.plotter;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.model.dataseries.AbstractDataSeries;
import com.rinearn.graph3d.model.dataseries.MathDataSeries;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.renderer.RinearnGraph3DRenderer;
import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.event.RinearnGraph3DPlottingListener;
import com.rinearn.graph3d.event.RinearnGraph3DPlottingEvent;

import java.util.List;
import java.awt.Color;


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
	private volatile boolean plottingEnabled = true; // Temporary value

	/** The radius of points plotted by this plotter. */
	private volatile double pointRadius = 2.0; // Temporary value


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

		// Plots all math data series.
		List<MathDataSeries> mathDataSeriesList = this.model.getMathDataSeriesList();
		for (AbstractDataSeries dataSeries: mathDataSeriesList) {
			this.plotPoints(dataSeries);
		}
	}


	/**
	 * Plots points on each coordinate point of the specified data series.
	 * 
	 * @param dataSeries The data series to be plotted.
	 */
	private void plotPoints(AbstractDataSeries dataSeries) {

		// Extract all coordinate points of the data series.
		double[][] xCoords = dataSeries.getXCoordinates();
		double[][] yCoords = dataSeries.getYCoordinates();
		double[][] zCoords = dataSeries.getZCoordinates();

		// Draw a point on each coordinate point in the above.
		int leftDimLength = xCoords.length;
		for (int iL=0; iL<leftDimLength; iL++) {

			int rightDimLength = xCoords[iL].length;
			for (int iR=0; iR<rightDimLength; iR++) {

				double x = xCoords[iL][iR];
				double y = yCoords[iL][iR];
				double z = zCoords[iL][iR];

				// Prepare the color of the point.
				Color pointColor = Color.GREEN; // Temporary color

				// Draw a point on the 3D graph.
				this.renderer.drawPoint(
						x, y, z, this.pointRadius, pointColor
				);
			}
		}
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
