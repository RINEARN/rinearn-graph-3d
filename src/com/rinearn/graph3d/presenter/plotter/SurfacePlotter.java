package com.rinearn.graph3d.presenter.plotter;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.model.data.series.AbstractDataSeries;
import com.rinearn.graph3d.model.data.series.DataSeriesGroup;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.renderer.RinearnGraph3DDrawingParameter;
import com.rinearn.graph3d.renderer.RinearnGraph3DRenderer;
import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.event.RinearnGraph3DPlottingListener;
import com.rinearn.graph3d.event.RinearnGraph3DPlottingEvent;
import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.data.SeriesAttribute;
import com.rinearn.graph3d.config.data.SeriesFilter;
import com.rinearn.graph3d.config.data.SeriesFilterMode;
import com.rinearn.graph3d.config.plotter.PlotterConfiguration;


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

		// Get the configuration of "With Membranes" option.
		RinearnGraph3DConfiguration config = this.model.config;
		PlotterConfiguration plotterConfig = config.getPlotterConfiguration();
		PlotterConfiguration.SurfacePlotterConfiguration surfacePlotterConfig = plotterConfig.getSurfacePlotterConfiguration();
		boolean isSurfaceOptionEnabled = surfacePlotterConfig.isPlotterEnabled();

		// This plotter do nothing if "With Membranes" option is not selected.
		if(!isSurfaceOptionEnabled) {
			return;
		}

		// Get the series filter, which filters the data series to which this option is applied.
		boolean existsSeriesFilter = surfacePlotterConfig.getSeriesFilterMode() != SeriesFilterMode.NONE;
		SeriesFilter seriesFilter = existsSeriesFilter ? surfacePlotterConfig.getSeriesFilter() : null;

		// Plots all data series.
		DataSeriesGroup<AbstractDataSeries> dataSeriesGroup = this.model.dataStore.getCombinedDataSeriesGroup();
		int dataSeriesCount = dataSeriesGroup.getDataSeriesCount();
		for (int dataSeriesIndex=0; dataSeriesIndex<dataSeriesCount; dataSeriesIndex++) {

			// Filter the data series.
			SeriesAttribute seriesAttribute = dataSeriesGroup.getDataSeriesAt(dataSeriesIndex).getSeriesAttribute();
			if (existsSeriesFilter && !seriesFilter.isSeriesIncluded(seriesAttribute)) {
				continue;
			}

			// Plot.
			AbstractDataSeries dataSeries = dataSeriesGroup.getDataSeriesAt(dataSeriesIndex);
			this.plotSurface(dataSeries, dataSeriesIndex);
		}
	}


	/**
	 * Plots the specified data series as a membrane.
	 *
	 * @param dataSeries The data series to be plotted.
	 * @param seriesIndex The index of the data series.
	 */
	private void plotSurface(AbstractDataSeries dataSeries, int seriesIndex) {
		RinearnGraph3DDrawingParameter drawingParameter = new RinearnGraph3DDrawingParameter();
		drawingParameter.setSeriesIndex(seriesIndex);
		drawingParameter.setAutoColoringEnabled(true);

		// Extract all coordinate points of the data series.
		double[][] xCoords = dataSeries.getXCoordinates();
		double[][] yCoords = dataSeries.getYCoordinates();
		double[][] zCoords = dataSeries.getZCoordinates();
		boolean[][] visibilities = dataSeries.getVisibilities();

		// Draw a quadrangle for each adjacent coordinate points in the above.
		int leftDimLength = xCoords.length;
		for (int iL=0; iL<leftDimLength - 1; iL++) {

			int currentRightDimLength = xCoords[iL].length;
			int nextRightDimLength = xCoords[iL + 1].length;
			for (int iR=0; iR < currentRightDimLength - 1 && iR < nextRightDimLength - 1; iR++) {

				// Draw a quadrangle only when all of its vertices are set to visible.
				boolean isQuadrangleVisible =
						visibilities[iL    ][iR    ] &&
						visibilities[iL + 1][iR    ] &&
						visibilities[iL + 1][iR + 1] &&
						visibilities[iL    ][iR + 1];
				if (!isQuadrangleVisible) {
					continue;
				}

				// Coords of the vertex A:
				double xA = xCoords[iL][iR];
				double yA = yCoords[iL][iR];
				double zA = zCoords[iL][iR];

				// Coords of the vertex B:
				double xB = xCoords[iL + 1][iR];
				double yB = yCoords[iL + 1][iR];
				double zB = zCoords[iL + 1][iR];

				// Coords of the vertex C:
				double xC = xCoords[iL + 1][iR + 1];
				double yC = yCoords[iL + 1][iR + 1];
				double zC = zCoords[iL + 1][iR + 1];

				// Coords of the vertex D:
				double xD = xCoords[iL][iR + 1];
				double yD = yCoords[iL][iR + 1];
				double zD = zCoords[iL][iR + 1];

				// Draw a quadrangle on the 3D graph.
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
