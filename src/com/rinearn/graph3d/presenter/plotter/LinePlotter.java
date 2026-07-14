package com.rinearn.graph3d.presenter.plotter;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.renderer.RinearnGraph3DDrawingParameter;
import com.rinearn.graph3d.renderer.RinearnGraph3DRenderer;
import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.event.RinearnGraph3DPlottingListener;
import com.rinearn.graph3d.event.RinearnGraph3DPlottingEvent;
import com.rinearn.graph3d.event.RinearnGraph3DPlottingDataAccessor;
import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.data.SeriesFilter;
import com.rinearn.graph3d.config.data.SeriesFilterMode;
import com.rinearn.graph3d.config.plotter.PlotterConfiguration;
import com.rinearn.graph3d.config.plotter.LinePlotterConfiguration;
import com.rinearn.graph3d.config.data.SeriesAttribute;


/**
 * The "plotter" to plot lines connecting coordinate points of data.
 *
 * A plotter is an object implementing RinearnGraph3DPlottingListener interface,
 * performs a part of plottings/re-plottings (e.g. plots points, or lines, etc),
 * in event-driven flow.
 */
public class LinePlotter implements RinearnGraph3DPlottingListener {

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
	public LinePlotter(Model model, View view, Presenter presenter, RinearnGraph3DRenderer renderer) {
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

		// Get the configuration of "With Lines" option.
		RinearnGraph3DConfiguration config = this.model.config;
		PlotterConfiguration plotterConfig = config.getPlotterConfiguration();
		LinePlotterConfiguration linePlotterConfig = plotterConfig.getLinePlotterConfiguration();
		double lineWidth = linePlotterConfig.getLineWidth();
		boolean isLineOptionEnabled = linePlotterConfig.isPlotterEnabled();

		// This plotter do nothing if "With Lines" option is not selected.
		if(!isLineOptionEnabled) {
			return;
		}

		// Get the series filter, which filters the data series to which this option is applied.
		boolean existsSeriesFilter = linePlotterConfig.getSeriesFilterMode() != SeriesFilterMode.NONE;
		SeriesFilter seriesFilter = existsSeriesFilter ? linePlotterConfig.getSeriesFilter() : null;

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

			// Plot all lines in the data series.
			int lineCount = dataAccessor.getDataLineCount(seriesIndex);
			for (int lineIndex=0; lineIndex<lineCount; lineIndex++) {
				this.plotLine(dataAccessor, seriesIndex, lineIndex, lineWidth);
			}
		}
	}


	/**
	 * Plots the specified line in data.
	 *
	 * @param dataAccessor The data accessor.
	 * @param seriesIndex The index of the data series to be plotted.
	 * @param lineIndex The index of the line to be plotted.
	 * @param lineWidth The line width.
	 */
	private void plotLine(RinearnGraph3DPlottingDataAccessor dataAccessor, int seriesIndex, int lineIndex, double lineWidth) {
		RinearnGraph3DDrawingParameter drawingParameter = new RinearnGraph3DDrawingParameter();
		drawingParameter.setSeriesIndex(seriesIndex);
		drawingParameter.setAutoColoringEnabled(true);

		int pointCount = dataAccessor.getDataPointCount(seriesIndex, lineIndex);
		for (int pointIndex=0; pointIndex<pointCount-1; pointIndex++) {

			// Draw a line element only when both of its edge points A,B are set to visible.
			boolean isPointAVisible = dataAccessor.isDataPointVisible(seriesIndex, lineIndex, pointIndex);
			boolean isPointBVisible = dataAccessor.isDataPointVisible(seriesIndex, lineIndex, pointIndex + 1);
			boolean isLineElementVisible = isPointAVisible && isPointBVisible;
			if (!isLineElementVisible) {
				continue;
			}

			// The coordinates of the edge point A:
			double xA = dataAccessor.getDataPointX(seriesIndex, lineIndex, pointIndex);
			double yA = dataAccessor.getDataPointY(seriesIndex, lineIndex, pointIndex);
			double zA = dataAccessor.getDataPointZ(seriesIndex, lineIndex, pointIndex);

			// The coordinates of the edge point B:
			double xB = dataAccessor.getDataPointX(seriesIndex, lineIndex, pointIndex + 1);
			double yB = dataAccessor.getDataPointY(seriesIndex, lineIndex, pointIndex + 1);
			double zB = dataAccessor.getDataPointZ(seriesIndex, lineIndex, pointIndex + 1);

			// Draw a line connecting the points A and B, on the 3D graph.
			this.renderer.drawLine(
					xA, yA, zA,
					xB, yB, zB,
					lineWidth, drawingParameter
			);
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
