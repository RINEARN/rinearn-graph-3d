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
import com.rinearn.graph3d.config.plotter.SurfacePlotterConfiguration;


/**
 * The "plotter" to plot each data series as a surface.
 *
 * A plotter is an object implementing RinearnGraph3DPlottingListener interface,
 * performs a part of plottings/re-plottings (e.g. plots points, or lines, etc),
 * in event-driven flow.
 */
public class SurfacePlotter implements RinearnGraph3DPlottingListener {

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
	public SurfacePlotter(Model model, View view, Presenter presenter, RinearnGraph3DRenderer renderer) {
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

		// Get the configuration of "With Surfaces" option.
		RinearnGraph3DConfiguration config = this.model.config;
		PlotterConfiguration plotterConfig = config.getPlotterConfiguration();
		SurfacePlotterConfiguration surfacePlotterConfig = plotterConfig.getSurfacePlotterConfiguration();
		boolean isSurfaceOptionEnabled = surfacePlotterConfig.isPlotterEnabled();

		// This plotter do nothing if "With Surfaces" option is not selected.
		if(!isSurfaceOptionEnabled) {
			return;
		}

		// Get the series filter, which filters the data series to which this option is applied.
		boolean existsSeriesFilter = surfacePlotterConfig.getSeriesFilterMode() != SeriesFilterMode.NONE;
		SeriesFilter seriesFilter = existsSeriesFilter ? surfacePlotterConfig.getSeriesFilter() : null;

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

			// Plot.
			this.plotSurface(dataAccessor, seriesIndex);
		}
	}


	/**
	 * Plots the specified data series as a surface.
	 *
	 * @param dataAccessor The data accessor.
	 * @param seriesIndex The index of the data series.
	 * @param lineWidth The width (in pixels) of lines composing a mesh.
	 */
	private void plotSurface(RinearnGraph3DPlottingDataAccessor dataAccessor, int seriesIndex) {
		RinearnGraph3DDrawingParameter drawingParameter = new RinearnGraph3DDrawingParameter();
		drawingParameter.setSeriesIndex(seriesIndex);
		drawingParameter.setAutoColoringEnabled(true);

		// Points at the corners in each cell:
		//
		// A:(iL1,iP1)   B:(iL1,iP2)
		//
		// D:(iL2,iP1)   C:(iL2,iP2)

		int dataLineCount = dataAccessor.getDataLineCount(seriesIndex);
		for (int iL1=0; iL1<dataLineCount - 1; iL1++) {
			int iL2 = iL1 + 1;

			int dataPointCount1 = dataAccessor.getDataPointCount(seriesIndex, iL1);
			int dataPointCount2 = dataAccessor.getDataPointCount(seriesIndex, iL2);
			for (int iP1=0; iP1<dataPointCount1 - 1 && iP1 < dataPointCount2 - 1; iP1++) {
				int iP2 = iP1 + 1;

				// The coordinates of the point A:
				double xA = dataAccessor.getDataPointX(seriesIndex, iL1, iP1);
				double yA = dataAccessor.getDataPointY(seriesIndex, iL1, iP1);
				double zA = dataAccessor.getDataPointZ(seriesIndex, iL1, iP1);
				boolean isAVisible = dataAccessor.isDataPointVisible(seriesIndex, iL1, iP1);

				// The coordinates of the point B:
				double xB = dataAccessor.getDataPointX(seriesIndex, iL1, iP2);
				double yB = dataAccessor.getDataPointY(seriesIndex, iL1, iP2);
				double zB = dataAccessor.getDataPointZ(seriesIndex, iL1, iP2);
				boolean isBVisible = dataAccessor.isDataPointVisible(seriesIndex, iL1, iP2);

				// The coordinates of the point C:
				double xC = dataAccessor.getDataPointX(seriesIndex, iL2, iP2);
				double yC = dataAccessor.getDataPointY(seriesIndex, iL2, iP2);
				double zC = dataAccessor.getDataPointZ(seriesIndex, iL2, iP2);
				boolean isCVisible = dataAccessor.isDataPointVisible(seriesIndex, iL2, iP1);

				// The coordinates of the point D:
				double xD = dataAccessor.getDataPointX(seriesIndex, iL2, iP1);
				double yD = dataAccessor.getDataPointY(seriesIndex, iL2, iP1);
				double zD = dataAccessor.getDataPointZ(seriesIndex, iL2, iP1);
				boolean isDVisible = dataAccessor.isDataPointVisible(seriesIndex, iL2, iP1);

				// Draw a quadrangle on the 3D graph.
				if (isAVisible && isBVisible && isCVisible && isDVisible) {
					this.renderer.drawQuadrangle(
							xA, yA, zA,
							xB, yB, zB,
							xC, yC, zC,
							xD, yD, zD,
							drawingParameter
					);
				}
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
