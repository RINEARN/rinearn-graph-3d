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
	@SuppressWarnings("unused")
	private final View view;

	/** The front-end class of "Presenter" layer, which invokes Model's procedures triggered by user's action on GUI. */
	private final Presenter presenter;

	/**
	 * The flag to perform the re-plotting asynchronously on rendering-loop thread, after updating data.
	 * When this flag is set to false, the re-plotting is performed synchronously on the thread
	 * on which setData(...) or appendData(...) is called.
	 */
	private volatile boolean asynchronousPlottingEnabled = false;


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


	/**
	 * Enables/disables the asynchronous-plotting feature.
	 *
	 * When this feature is enabled,
	 * the re-plotting after updating data is performed asynchronously on rendering-loop thread.
	 *
	 * When this feature is disabled,
	 * the re-plotting is performed synchronously on the thread on which setData(...) or appendData(...) is called.
	 *
	 * @param enabled Specify true to enable, false to disable.
	 */
	public synchronized void setAsynchronousPlottingEnabled(boolean enabled) {
		this.asynchronousPlottingEnabled = enabled;
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
	public void setData(double[] x, double[] y, double[] z) {

		// Handle the API request on the event-dispatcher thread.
		DataAPIListener apiListener = new DataAPIListener(x, y, z, DataAPIListenerMode.SET);
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
	public void setData(double[][] x, double[][] y, double[][] z) {

		// Handle the API request on the event-dispatcher thread.
		DataAPIListener apiListener = new DataAPIListener(x, y, z, DataAPIListenerMode.SET);
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
	public void setData(double[][][] x, double[][][] y, double[][][] z) {

		// Handle the API request on the event-dispatcher thread.
		DataAPIListener apiListener = new DataAPIListener(x, y, z, DataAPIListenerMode.SET);
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
	public void appendData(double[] x, double[] y, double[] z) {

		// Handle the API request on the event-dispatcher thread.
		DataAPIListener apiListener = new DataAPIListener(x, y, z, DataAPIListenerMode.APPEND);
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
	public void appendData(double[][] x, double[][] y, double[][] z) {

		// Handle the API request on the event-dispatcher thread.
		DataAPIListener apiListener = new DataAPIListener(x, y, z, DataAPIListenerMode.APPEND);
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
	public void appendData(double[][][] x, double[][][] y, double[][][] z) {

		// Handle the API request on the event-dispatcher thread.
		DataAPIListener apiListener = new DataAPIListener(x, y, z, DataAPIListenerMode.APPEND);
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
	 * The enum for specifying the mode of DataAPIListener.
	 */
	private enum DataAPIListenerMode {

		/** The mode for handling setData(x,y,z) API. */
		SET,

		/** The mode for handling appendData(x,y,z) API. */
		APPEND;
	}


	/**
	 * The class handling API requests from setData(x,y,z) and appendData(x,y,z) methods,
	 * on event-dispatcher thread.
	 */
	private final class DataAPIListener implements Runnable {

		/**
		 * The array storing the X-coordinates of the grid/node points of the multiple data series,
		 * where its indices are [dataSeriesIndex][gridIndexA][gridIndexB].
		 */
		private final double[][][] x;

		/**
		 * The array storing the Y-coordinates of the grid/node points of the multiple data series,
		 * where its indices are [dataSeriesIndex][gridIndexA][gridIndexB].
		 */
		private final double[][][] y;

		/**
		 * The array storing the Z-coordinates of the grid/node points of the multiple data series,
		 * where its indices are [dataSeriesIndex][gridIndexA][gridIndexB].
		 */
		private final double[][][] z;

		/**
		 * The mode of this listener,
		 * specifying the API handled by this listener from setData(x,y,z) and appendData(x,y,z).
		 */
		private final DataAPIListenerMode mode;

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
		 * @param mode
		 *     Specify SET for handling setData(x,y,z) API, or APPEND for handling appendData(x,y,z) API.
		 */
		public DataAPIListener(double[] x, double[] y, double[] z, DataAPIListenerMode mode) {
			this.x = new double[][][] { new double[][] { x } };
			this.y = new double[][][] { new double[][] { y } };
			this.z = new double[][][] { new double[][] { z } };
			this.mode = mode;
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
		 * @param mode
		 *     Specify SET for handling setData(x,y,z) API, or APPEND for handling appendData(x,y,z) API.
		 */
		public DataAPIListener(double[][] x, double[][] y, double[][] z, DataAPIListenerMode mode) {
			this.x = new double[][][] { x };
			this.y = new double[][][] { y };
			this.z = new double[][][] { z };
			this.mode = mode;
		}

		/**
		 * Create an instance handling setData(double[][][] x, ...) or appendData(double[][][] x, ...)
		 * API request with the specified argument.
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
		 * @param mode
		 *     Specify SET for handling setData(x,y,z) API, or APPEND for handling appendData(x,y,z) API.
		 */
		public DataAPIListener(double[][][] x, double[][][] y, double[][][] z, DataAPIListenerMode mode) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.mode = mode;
		}

		@Override
		public void run() {

			// Stores the specified (multiple) data series into an array.
			int dataSeriesCount = x.length;
			ArrayDataSeries[] multipleArrayDataSeries = new ArrayDataSeries[dataSeriesCount];
			for (int iseries=0; iseries<dataSeriesCount; iseries++) {
				ArrayDataSeries arrayDataSeries = new ArrayDataSeries(x[iseries], y[iseries], z[iseries]);
				multipleArrayDataSeries[iseries] = arrayDataSeries;

				// Don't do the following. We must register the multiple data series by an "atomic operation".
				// (because the data series registered in the Model may be accessed from another thread asynchronously.)
				// ---
				// model.addArrayDataSeries(arrayDataSeries);
			}

			// Set/add the above (multiple) data series to the Model layer.
			switch (this.mode) {
				case SET : {
					model.setArrayDataSeries(multipleArrayDataSeries);
					break;
				}
				case APPEND : {
					model.addArrayDataSeries(multipleArrayDataSeries);
					break;
				}
				default : {
					throw new IllegalStateException("Unexpected mode: " + this.mode);
				}
			}

			// Re-plot the graph.
			if (asynchronousPlottingEnabled) {
				presenter.renderingLoop.requestPlotting();
			} else {
				presenter.plot();
			}
		}
	}
}
