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
	 * Appends the data composing a line, to the currently plotted data
	 *
	 * @param x The array storing the X-coordinates of the node points of the line.
	 * @param y The array storing the Y-coordinates of the node points of the line.
	 * @param z The array storing the Z-coordinates of the node points of the line.
	 */
	public synchronized void appendData(double[] x, double[] y, double[] z) {

		// Handle the API request on the event-dispatcher thread.
		AppendData1RankAPIListener apiListener = new AppendData1RankAPIListener(x, y, z);
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
	 * The class handling API requests from appendData(double[] x, ...) method,
	 * on event-dispatcher thread.
	 */
	private final class AppendData1RankAPIListener implements Runnable {

		// The array storing the X-coordinates of the node points of the line.
		private volatile double[] x;

		// The array storing the Y-coordinates of the node points of the line.
		private volatile double[] y;

		// The array storing the Z-coordinates of the node points of the line.
		private volatile double[] z;

		/**
		 * Create an instance handling appendData(double[][]x, ...) API request with the specified argument.
		 *
		 * @param x The array storing the X-coordinates of the node points of the line.
		 * @param y The array storing the Y-coordinates of the node points of the line.
		 * @param z The array storing the Z-coordinates of the node points of the line.
		 */
		public AppendData1RankAPIListener(double[] x, double[] y, double[] z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		@Override
		public void run() {
			ArrayDataSeries arrayDataSeries = new ArrayDataSeries(
					new double[][] { x },
					new double[][] { y },
					new double[][] { z }
			);
			model.addArrayDataSeries(arrayDataSeries);
			presenter.plot();
		}
	}


	/**
	 * Appends the data composing a mesh, to the currently plotted data
	 *
	 * @param x The array storing the X-coordinates of the grid points of the mesh.
	 * @param y The array storing the Y-coordinates of the grid points of the mesh.
	 * @param z The array storing the Z-coordinates of the grid points of the mesh.
	 */
	public synchronized void appendData(double[][] x, double[][] y, double[][] z) {

		// Handle the API request on the event-dispatcher thread.
		AppendData2RanksAPIListener apiListener = new AppendData2RanksAPIListener(x, y, z);
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
	 * The class handling API requests from appendData(double[][] x, ...) method,
	 * on event-dispatcher thread.
	 */
	private final class AppendData2RanksAPIListener implements Runnable {

		// The array storing the X-coordinates of the grid points of the mesh.
		private volatile double[][] x;

		// The array storing the Y-coordinates of the grid points of the mesh.
		private volatile double[][] y;

		// The array storing the Z-coordinates of the grid points of the mesh.
		private volatile double[][] z;

		/**
		 * Create an instance handling appendData(double[][]x, ...) API request with the specified argument.
		 *
		 * @param x The array storing the X-coordinates of the grid points of the mesh.
		 * @param y The array storing the Y-coordinates of the grid points of the mesh.
		 * @param z The array storing the Z-coordinates of the grid points of the mesh.
		 */
		public AppendData2RanksAPIListener(double[][] x, double[][] y, double[][] z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		@Override
		public void run() {
			ArrayDataSeries arrayDataSeries = new ArrayDataSeries(x, y, z);
			model.addArrayDataSeries(arrayDataSeries);
			presenter.plot();
		}
	}
}
