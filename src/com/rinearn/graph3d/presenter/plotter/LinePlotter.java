package com.rinearn.graph3d.presenter.plotter;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.model.dataseries.AbstractDataSeries;
import com.rinearn.graph3d.model.dataseries.MathDataSeries;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.renderer.RinearnGraph3DDrawingParameter;
import com.rinearn.graph3d.renderer.RinearnGraph3DRenderer;
import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.event.RinearnGraph3DPlottingListener;
import com.rinearn.graph3d.event.RinearnGraph3DPlottingEvent;
import com.rinearn.graph3d.config.OptionConfiguration;
import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;

import java.util.List;


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
		OptionConfiguration optionConfig = config.getOptionConfiguration();
		OptionConfiguration.LineOptionConfiguration lineOptionConfig = optionConfig.getLineOptionConfiguration();
		double lineWidth = lineOptionConfig.getLineWidth();
		boolean isLineOptionSelected = lineOptionConfig.isSelected();

		// This plotter do nothing if "With Lines" option is not selected.
		if(!isLineOptionSelected) {
			return;
		}

		// Plots all math data series.
		List<MathDataSeries> mathSeriesList = this.model.getMathDataSeriesList();
		int mathSeriesCount = mathSeriesList.size();
		for (int mathSeriesIndex=0; mathSeriesIndex<mathSeriesCount; mathSeriesIndex++) {
			AbstractDataSeries mathSeries = mathSeriesList.get(mathSeriesIndex);
			this.plotLines(mathSeries, mathSeriesIndex, lineWidth);
		}
	}


	/**
	 * Plots lines connecting coordinate points of the specified data series.
	 *
	 * @param dataSeries The data series to be plotted.
	 * @param seriesIndex The index of the data series.
	 * @param lineWidth The width (in pixels) of lines.
	 */
	private void plotLines(AbstractDataSeries dataSeries, int seriesIndex, double lineWidth) {
		RinearnGraph3DDrawingParameter drawingParameter = new RinearnGraph3DDrawingParameter();
		drawingParameter.setSeriesIndex(seriesIndex);
		drawingParameter.setAutoColoringEnabled(true);

		// Extract all coordinate points of the data series.
		double[][] xCoords = dataSeries.getXCoordinates();
		double[][] yCoords = dataSeries.getYCoordinates();
		double[][] zCoords = dataSeries.getZCoordinates();
		boolean[][] visibilities = dataSeries.getVisibilities();

		// Draw a line between each pair of adjacent points in the above.
		int leftDimLength = xCoords.length;
		for (int iL=0; iL<leftDimLength; iL++) {

			int rightDimLength = xCoords[iL].length;
			for (int iR=0; iR<rightDimLength - 1; iR++) {

				// Draw a line only when both of its edge points are set to visible.
				boolean isLineVisible = visibilities[iL][iR] && visibilities[iL][iR + 1];
				if (!isLineVisible) {
					continue;
				}

				// The coordinates of the edge point A:
				double xA = xCoords[iL][iR];
				double yA = yCoords[iL][iR];
				double zA = zCoords[iL][iR];

				// The coordinates of the edge point B:
				double xB = xCoords[iL][iR + 1];
				double yB = yCoords[iL][iR + 1];
				double zB = zCoords[iL][iR + 1];

				// Draw a line connecting the points A and B, on the 3D graph.
				this.renderer.drawLine(
						xA, yA, zA,
						xB, yB, zB,
						lineWidth, drawingParameter
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
