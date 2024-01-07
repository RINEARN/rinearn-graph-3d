package com.rinearn.graph3d.presenter.handler;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.model.dataseries.ArrayDataSeries;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.view.View;

/**
 * The class handling API requests for plotting data stored in arrays.
 */
public class DataArrayHandler {

	/** The front-end class of "Model" layer, which provides internal logic procedures and so on. */
	private final Model model;

	/** The front-end class of "View" layer, which provides visible part of GUI without event handling. */
	private final View view;

	/** The front-end class of "Presenter" layer, which invokes Model's procedures triggered by user's action on GUI. */
	private final Presenter presenter;


	/**
	 * Create a new instance handling events and API requests using the specified resources.
	 *
	 * @param model The front-end class of "Model" layer, which provides internal logic procedures and so on.
	 * @param view The front-end class of "View" layer, which provides visible part of GUI without event handling.
	 * @param presenter The front-end class of "Presenter" layer, which handles events occurred on GUI, and API requests.
	 */
	public DataArrayHandler(Model model, View view, Presenter presenter) {
		this.model = model;
		this.view = view;
		this.presenter = presenter;
	}


	// ================================================================================
	//
	// - API Listeners -
	//
	// ================================================================================


	/**
	 * Sets the data composing a line to be plotted.
	 *
	 * Please note that, the currently registered data series are cleared.
	 * If you don't want to clear them, use appendData(...) instead.
	 *
	 * @param x
	 *     The array storing the X-coordinates of the node points of the line to be plotted,
	 *     where its index is [nodeIndex].
	 * @param y
	 *     The array storing the Y-coordinates of the node points of the line to be plotted,
	 *     where its index is [nodeIndex].
	 * @param z
	 *     The array storing the Z-coordinates of the node points of the line to be plotted,
	 *     where its index is [nodeIndex].
	 */
	public synchronized void setData(double[] x, double[] y, double[] z) {

		// Handle the API request on the event-dispatcher thread.
		boolean clearCurrentDataSeries = true;
		SetDataAndAppendDataAPIListener apiListener = new SetDataAndAppendDataAPIListener(x, y, z, clearCurrentDataSeries);
		if (SwingUtilities.isEventDispatchThread()) {
			apiListener.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(apiListener);
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}


	/**
	 * Sets the data composing a mesh to be plotted.
	 *
	 * Please note that, the currently registered data series are cleared.
	 * If you don't want to clear them, use appendData(...) instead.
	 *
	 * @param x
	 *     The array storing the X-coordinates of the grid points of the mesh to be plotted,
	 *     where its indices are [gridIndexA][gridIndexB].
	 * @param y
	 *     The array storing the Y-coordinates of the grid points of the mesh to be plotted,
	 *     where its indices are [gridIndexA][gridIndexB].
	 * @param z
	 *     The array storing the Z-coordinates of the grid points of the mesh to be plotted,
	 *     where its indices are [gridIndexA][gridIndexB].
	 */
	public synchronized void setData(double[][] x, double[][] y, double[][] z) {

		// Handle the API request on the event-dispatcher thread.
		boolean clearCurrentDataSeries = true;
		SetDataAndAppendDataAPIListener apiListener = new SetDataAndAppendDataAPIListener(x, y, z, clearCurrentDataSeries);
		if (SwingUtilities.isEventDispatchThread()) {
			apiListener.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(apiListener);
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}


	/**
	 * Sets the multiple data series (composing multiple meshes or lines) to be plotted.
	 *
	 * Please note that, the currently registered data series are cleared.
	 * If you don't want to clear them, use appendData(...) instead.
	 *
	 * @param x
	 *     The array storing the X-coordinates of the grid/node points of the multiple data series to be plotted,
	 *     where its indices are [dataSeriesIndex][gridIndexA][gridIndexB].
	 * @param y
	 *     The array storing the Y-coordinates of the grid/node points of the multiple data series to be plotted,
	 *     where its indices are [dataSeriesIndex][gridIndexA][gridIndexB].
	 * @param z
	 *     The array storing the Z-coordinates of the grid/node points of the multiple data series to be plotted,
	 *     where its indices are [dataSeriesIndex][gridIndexA][gridIndexB].
	 */
	public synchronized void setData(double[][][] x, double[][][] y, double[][][] z) {

		// Handle the API request on the event-dispatcher thread.
		boolean clearCurrentDataSeries = true;
		SetDataAndAppendDataAPIListener apiListener = new SetDataAndAppendDataAPIListener(x, y, z, clearCurrentDataSeries);
		if (SwingUtilities.isEventDispatchThread()) {
			apiListener.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(apiListener);
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}


	/**
	 * Appends the data composing a line, to the currently plotted data.
	 *
	 * @param x
	 *     The array storing the X-coordinates of the node points of the line to be plotted,
	 *     where its index is [nodeIndex].
	 * @param y
	 *     The array storing the Y-coordinates of the node points of the line to be plotted,
	 *     where its index is [nodeIndex].
	 * @param z
	 *     The array storing the Z-coordinates of the node points of the line to be plotted,
	 *     where its index is [nodeIndex].
	 */
	public synchronized void appendData(double[] x, double[] y, double[] z) {

		// Handle the API request on the event-dispatcher thread.
		boolean clearCurrentDataSeries = false;
		SetDataAndAppendDataAPIListener apiListener = new SetDataAndAppendDataAPIListener(x, y, z, clearCurrentDataSeries);
		if (SwingUtilities.isEventDispatchThread()) {
			apiListener.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(apiListener);
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}


	/**
	 * Appends the data composing a mesh, to the currently plotted data.
	 *
	 * @param x
	 *     The array storing the X-coordinates of the grid points of the mesh to be plotted,
	 *     where its indices are [gridIndexA][gridIndexB].
	 * @param y
	 *     The array storing the Y-coordinates of the grid points of the mesh to be plotted,
	 *     where its indices are [gridIndexA][gridIndexB].
	 * @param z
	 *     The array storing the Z-coordinates of the grid points of the mesh to be plotted,
	 *     where its indices are [gridIndexA][gridIndexB].
	 */
	public synchronized void appendData(double[][] x, double[][] y, double[][] z) {

		// Handle the API request on the event-dispatcher thread.
		boolean clearCurrentDataSeries = false;
		SetDataAndAppendDataAPIListener apiListener = new SetDataAndAppendDataAPIListener(x, y, z, clearCurrentDataSeries);
		if (SwingUtilities.isEventDispatchThread()) {
			apiListener.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(apiListener);
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}


	/**
	 * Appends the multiple data series (composing multiple meshes or lines), to the currently plotted data.
	 *
	 * @param x
	 *     The array storing the X-coordinates of the grid/node points of the multiple data series to be plotted,
	 *     where its indices are [dataSeriesIndex][gridIndexA][gridIndexB].
	 * @param y
	 *     The array storing the Y-coordinates of the grid/node points of the multiple data series to be plotted,
	 *     where its indices are [dataSeriesIndex][gridIndexA][gridIndexB].
	 * @param z
	 *     The array storing the Z-coordinates of the grid/node points of the multiple data series to be plotted,
	 *     where its indices are [dataSeriesIndex][gridIndexA][gridIndexB].
	 */
	public synchronized void appendData(double[][][] x, double[][][] y, double[][][] z) {

		// Handle the API request on the event-dispatcher thread.
		boolean clearCurrentDataSeries = false;
		SetDataAndAppendDataAPIListener apiListener = new SetDataAndAppendDataAPIListener(x, y, z, clearCurrentDataSeries);
		if (SwingUtilities.isEventDispatchThread()) {
			apiListener.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(apiListener);
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}


	/**
	 * The class handling API requests from setData(x,y,z) and appendData(x,y,z) methods,
	 * on event-dispatcher thread.
	 */
	private final class SetDataAndAppendDataAPIListener implements Runnable {

		/**
		 * The array storing the X-coordinates of the grid/node points of the multiple data series,
		 * where its indices are [dataSeriesIndex][gridIndexA][gridIndexB].
		 */
		private volatile double[][][] x;

		/**
		 * The array storing the Y-coordinates of the grid/node points of the multiple data series,
		 * where its indices are [dataSeriesIndex][gridIndexA][gridIndexB].
		 */
		private volatile double[][][] y;

		/**
		 * The array storing the Z-coordinates of the grid/node points of the multiple data series,
		 * where its indices are [dataSeriesIndex][gridIndexA][gridIndexB].
		 */
		private volatile double[][][] z;

		/**
		 * The flag to clear the currently registered data series before registering the specified data.
		 * Set to true for handling setData(x,y,z) API, or set to false for handling appendData(x,y,z) API.
		 */
		private volatile boolean clears;

		/**
		 * Create an instance handling setData(double[] x, ...) or appendData(double[] x, ...)
		 * API request with the specified argument.
		 *
		 * @param x
		 *     The array storing the X-coordinates of the node points of a line,
		 *     where its index is [nodeIndex].
		 * @param y
		 *     The array storing the Y-coordinates of the node points of a line,
		 *     where its index is [nodeIndex].
		 * @param z
		 *     The array storing the Z-coordinates of the node points of a line,
		 *     where its index is [nodeIndex].
		 * @param clears
		 *     The flag to clear the currently registered data series before registering the specified data.
		 *     Specify true for handling setData(x,y,z) API, or specify false for handling appendData(x,y,z) API.
		 */
		public SetDataAndAppendDataAPIListener(double[] x, double[] y, double[] z, boolean clears) {
			this.x = new double[][][] { new double[][] { x } };
			this.y = new double[][][] { new double[][] { y } };
			this.z = new double[][][] { new double[][] { z } };
			this.clears = clears;
		}

		/**
		 * Create an instance handling setData(double[][] x, ...) or appendData(double[][] x, ...)
		 * API request with the specified argument.
		 *
		 * @param x
		 *     The array storing the X-coordinates of the grid points of a mesh,
		 *     where its indices are [gridIndexA][gridIndexB].
		 * @param y
		 *     The array storing the Y-coordinates of the grid points of a mesh,
		 *     where its indices are [gridIndexA][gridIndexB].
		 * @param z
		 *     The array storing the Z-coordinates of the grid points of a mesh,
		 *     where its indices are [gridIndexA][gridIndexB].
		 * @param clears
		 *     The flag to clear the currently registered data series before registering the specified data.
		 *     Specify true for handling setData(x,y,z) API, or specify false for handling appendData(x,y,z) API.
		 */
		public SetDataAndAppendDataAPIListener(double[][] x, double[][] y, double[][] z, boolean clears) {
			this.x = new double[][][] { x };
			this.y = new double[][][] { y };
			this.z = new double[][][] { z };
			this.clears = clears;
		}

		/**
		 * Create an instance handling appendData(double[][][] x, ...) API request with the specified argument.
		 *
		 * @param x
		 *     The array storing the X-coordinates of the grid/node points of the multiple data series,
		 *     where its indices are [dataSeriesIndex][gridIndexA][gridIndexB].
		 * @param y
		 *     The array storing the Y-coordinates of the grid/node points of the multiple data series,
		 *     where its indices are [dataSeriesIndex][gridIndexA][gridIndexB].
		 * @param z
		 *     The array storing the Z-coordinates of the grid/node points of the multiple data series,
		 *     where its indices are [dataSeriesIndex][gridIndexA][gridIndexB].
		 * @param clears
		 *     The flag to clear the currently registered data series before registering the specified data.
		 *     Specify true for handling setData(x,y,z) API, or specify false for handling appendData(x,y,z) API.
		 */
		public SetDataAndAppendDataAPIListener(double[][][] x, double[][][] y, double[][][] z, boolean clears) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.clears = clears;
		}

		@Override
		public void run() {

			// When this instance handles setData(...) API, not appendData(...) API,
			// clear the currently registered data series before appending the specified data series.
			if (clears) {
				model.clearArrayDataSeries();
			}

			// Register the specified data series.
			int dataSeriesCount = x.length;
			for (int iseries=0; iseries<dataSeriesCount; iseries++) {
				ArrayDataSeries arrayDataSeries = new ArrayDataSeries(x[iseries], y[iseries], z[iseries]);
				model.addArrayDataSeries(arrayDataSeries);
			}

			// Re-plot the graph.
			presenter.plot();
		}
	}
}
