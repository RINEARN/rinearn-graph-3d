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
import com.rinearn.graph3d.config.plotter.MeshPlotterConfiguration;


/**
 * The "plotter" to plot each data series as meshes.
 *
 * A plotter is an object implementing RinearnGraph3DPlottingListener interface,
 * performs a part of plottings/re-plottings (e.g. plots points, or lines, etc),
 * in event-driven flow.
 */
public class MeshPlotter implements RinearnGraph3DPlottingListener {

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
	public MeshPlotter(Model model, View view, Presenter presenter, RinearnGraph3DRenderer renderer) {
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

		// Get the configuration of "With Meshes" option.
		RinearnGraph3DConfiguration config = this.model.config;
		PlotterConfiguration plotterConfig = config.getPlotterConfiguration();
		MeshPlotterConfiguration meshPlotterConfig = plotterConfig.getMeshPlotterConfiguration();
		double lineWidth = meshPlotterConfig.getLineWidth();
		boolean isMeshOptionEnabled = meshPlotterConfig.isPlotterEnabled();

		// Plots all data series.
		// This plotter do nothing if "With Meshes" option is not selected.
		if(!isMeshOptionEnabled) {
			return;
		}

		// Get the series filter, which filters the data series to which this option is applied.
		boolean existsSeriesFilter = meshPlotterConfig.getSeriesFilterMode() != SeriesFilterMode.NONE;
		SeriesFilter seriesFilter = existsSeriesFilter ? meshPlotterConfig.getSeriesFilter() : null;

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
			this.plotMesh(dataAccessor, seriesIndex, lineWidth);
		}
	}


	/**
	 * Plots the specified data series as a mesh.
	 *
	 * @param dataAccessor The data accessor.
	 * @param seriesIndex The index of the data series.
	 * @param lineWidth The width (in pixels) of lines composing a mesh.
	 */
	private void plotMesh(RinearnGraph3DPlottingDataAccessor dataAccessor, int seriesIndex, double lineWidth) {
		RinearnGraph3DDrawingParameter drawingParameter = new RinearnGraph3DDrawingParameter();
		drawingParameter.setSeriesIndex(seriesIndex);
		drawingParameter.setAutoColoringEnabled(true);

		// Draw lines parallel to the data lines.
		int maxDataPointCountInAllLines = 0;
		int dataLineCount = dataAccessor.getDataLineCount(seriesIndex);
		for (int iL=0; iL<dataLineCount; iL++) {

			int dataPointCount = dataAccessor.getDataPointCount(seriesIndex, iL);
			maxDataPointCountInAllLines = Math.max(dataPointCount, maxDataPointCountInAllLines);
			for (int iP1=0; iP1<dataPointCount - 1; iP1++) {
				int iP2 = iP1 + 1;

				// The coordinates of the edge point A:
				double xA = dataAccessor.getDataPointX(seriesIndex, iL, iP1);
				double yA = dataAccessor.getDataPointY(seriesIndex, iL, iP1);
				double zA = dataAccessor.getDataPointZ(seriesIndex, iL, iP1);
				boolean isAVisible = dataAccessor.isDataPointVisible(seriesIndex, iL, iP1);

				// The coordinates of the edge point B:
				double xB = dataAccessor.getDataPointX(seriesIndex, iL, iP2);
				double yB = dataAccessor.getDataPointY(seriesIndex, iL, iP2);
				double zB = dataAccessor.getDataPointZ(seriesIndex, iL, iP2);
				boolean isBVisible = dataAccessor.isDataPointVisible(seriesIndex, iL, iP2);

				// Draw a line connecting the points A and B, on the 3D graph.
				if (isAVisible && isBVisible) {
					this.renderer.drawLine(
							xA, yA, zA,
							xB, yB, zB,
							lineWidth, drawingParameter
					);
				}
			}
		}

		// Draw lines in a direction that intersects the data lines.
		for (int iP=0; iP<maxDataPointCountInAllLines; iP++) {
			for (int iL1=0; iL1<dataLineCount - 1; iL1++) {
				int iL2 = iL1 + 1;

				int dataPointCount1 = dataAccessor.getDataPointCount(seriesIndex, iL1);
				int dataPointCount2 = dataAccessor.getDataPointCount(seriesIndex, iL2);
				if (dataPointCount1 <= iP || dataPointCount2 <= iP) {
					continue;
				}

				// The coordinates of the edge point A:
				double xA = dataAccessor.getDataPointX(seriesIndex, iL1, iP);
				double yA = dataAccessor.getDataPointY(seriesIndex, iL1, iP);
				double zA = dataAccessor.getDataPointZ(seriesIndex, iL1, iP);
				boolean isAVisible = dataAccessor.isDataPointVisible(seriesIndex, iL1, iP);

				// The coordinates of the edge point B:
				double xB = dataAccessor.getDataPointX(seriesIndex, iL2, iP);
				double yB = dataAccessor.getDataPointY(seriesIndex, iL2, iP);
				double zB = dataAccessor.getDataPointZ(seriesIndex, iL2, iP);
				boolean isBVisible = dataAccessor.isDataPointVisible(seriesIndex, iL2, iP);

				// Draw a line connecting the points A and B, on the 3D graph.
				if (isAVisible && isBVisible) {
					this.renderer.drawLine(
							xA, yA, zA,
							xB, yB, zB,
							lineWidth, drawingParameter
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
