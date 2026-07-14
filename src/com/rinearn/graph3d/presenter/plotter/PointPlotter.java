package com.rinearn.graph3d.presenter.plotter;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.renderer.RinearnGraph3DDrawingParameter;
import com.rinearn.graph3d.renderer.RinearnGraph3DRenderer;
import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.event.RinearnGraph3DPlottingListener;
import com.rinearn.graph3d.event.RinearnGraph3DPlottingDataAccessor;
import com.rinearn.graph3d.event.RinearnGraph3DPlottingEvent;
import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.data.SeriesAttribute;
import com.rinearn.graph3d.config.data.SeriesFilter;
import com.rinearn.graph3d.config.data.SeriesFilterMode;
import com.rinearn.graph3d.config.plotter.PlotterConfiguration;
import com.rinearn.graph3d.config.plotter.PointPlotterConfiguration;


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
	@SuppressWarnings("unused")
	private final View view;

	/** The front-end class of "Presenter" layer, which invokes Model's procedures triggered by user's action on GUI. */
	@SuppressWarnings("unused")
	private final Presenter presenter;

	/** The rendering engine of 3D graphs. */
	private final RinearnGraph3DRenderer renderer;


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
	 * Called when a plotting/re-plotting is requested.
	 *
	 * @param event The plotting event.
	 */
	@Override
	public synchronized void plottingRequested(RinearnGraph3DPlottingEvent event) {

		// Get the configuration of "With Points" option.
		RinearnGraph3DConfiguration config = this.model.config;
		PlotterConfiguration plotterConfig = config.getPlotterConfiguration();
		PointPlotterConfiguration pointPlotterConfig = plotterConfig.getPointPlotterConfiguration();
		double pointRadius = pointPlotterConfig.getCircleRadius();
		boolean isPointOptionEnabled = pointPlotterConfig.isPlotterEnabled();

		// This plotter do nothing if "With Points" option is not selected.
		if(!isPointOptionEnabled) {
			return;
		}

		// Get the series filter, which filters the data series to which this option is applied.
		boolean existsSeriesFilter = pointPlotterConfig.getSeriesFilterMode() != SeriesFilterMode.NONE;
		SeriesFilter seriesFilter = existsSeriesFilter ? pointPlotterConfig.getSeriesFilter() : null;

		// Get the data accessor, which is an object for accessing data to be plotted.
		RinearnGraph3DPlottingDataAccessor dataAccessor = event.getPlottingDataAccessor();

		// Plots all data series.
		int seriesCount = dataAccessor.getDataSeriesCount();
		for (int seriesIndex=0; seriesIndex<seriesCount; seriesIndex++) {

			// Filter the data series.
			SeriesAttribute seriesAttribute = dataAccessor.getDataSeriesAttribute(seriesIndex);
			if (existsSeriesFilter && !seriesFilter.isSeriesIncluded(seriesAttribute)) {
				continue;
			}

			// Plot all points in the data series.
			int lineCount = dataAccessor.getDataLineCount(seriesIndex);
			for (int lineIndex=0; lineIndex<lineCount; lineIndex++) {
				int pointCount = dataAccessor.getDataPointCount(seriesIndex, lineIndex);
				for (int pointIndex=0; pointIndex<pointCount; pointIndex++) {
					this.plotPoint(dataAccessor, seriesIndex, lineIndex, pointIndex, pointRadius);
				}
			}
		}
	}


	/**
	 * Plots a point on each coordinate point of the specified data series.
	 *
	 * @param dataAccessor The data accessor.
	 * @param seriesIndex The index of the data series.
	 * @param lineIndex The index of the data line to which the data point belongs.
	 * @param pointIndex The index of the data point to be plotted.
	 * @param pointRadius The radius (in pixels) of points.
	 */
	private void plotPoint(RinearnGraph3DPlottingDataAccessor dataAccessor, int seriesIndex, int lineIndex, int pointIndex, double pointRadius) {
		RinearnGraph3DDrawingParameter drawingParameter = new RinearnGraph3DDrawingParameter();
		drawingParameter.setSeriesIndex(seriesIndex);
		drawingParameter.setAutoColoringEnabled(true);

		// If the data point is set to invisible, draw nothing.
		if (!dataAccessor.isDataPointVisible(seriesIndex, lineIndex, pointIndex)) {
			return;
		}

		// Get the coordinate values of the data point to be plotted.
		double x = dataAccessor.getDataPointX(seriesIndex, lineIndex, pointIndex);
		double y = dataAccessor.getDataPointY(seriesIndex, lineIndex, pointIndex);
		double z = dataAccessor.getDataPointZ(seriesIndex, lineIndex, pointIndex);

		// Draw a point on the 3D graph.
		this.renderer.drawPoint(
				x, y, z, pointRadius, drawingParameter
		);
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
