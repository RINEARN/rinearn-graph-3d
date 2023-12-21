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
 * The "plotter" to plot each data series as a membrane.
 *
 * A plotter is an object implementing RinearnGraph3DPlottingListener interface,
 * performs a part of plottings/re-plottings (e.g. plots points, or lines, etc),
 * in event-driven flow.
 */
public class MembranePlotter implements RinearnGraph3DPlottingListener {

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
	public MembranePlotter(Model model, View view, Presenter presenter, RinearnGraph3DRenderer renderer) {
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
		OptionConfiguration optionConfig = config.getOptionConfiguration();
		OptionConfiguration.MembraneOptionConfiguration membraneOptionConfig = optionConfig.getMembraneOptionConfiguration();
		boolean isMembraneOptionSelected = membraneOptionConfig.isSelected();

		// This plotter do nothing if "With Membranes" option is not selected.
		if(!isMembraneOptionSelected) {
			return;
		}

		// Plots all math data series.
		List<MathDataSeries> mathSeriesList = this.model.getMathDataSeriesList();
		int mathSeriesCount = mathSeriesList.size();
		for (int mathSeriesIndex=0; mathSeriesIndex<mathSeriesCount; mathSeriesIndex++) {
			AbstractDataSeries mathSeries = mathSeriesList.get(mathSeriesIndex);
			this.plotMembrane(mathSeries, mathSeriesIndex);
		}
	}


	/**
	 * Plots the specified data series as a membrane.
	 *
	 * @param dataSeries The data series to be plotted.
	 * @param seriesIndex The index of the data series.
	 */
	private void plotMembrane(AbstractDataSeries dataSeries, int seriesIndex) {
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

			int rightDimLength = xCoords[iL].length;
			for (int iR=0; iR<rightDimLength - 1; iR++) {

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
