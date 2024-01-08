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

			// ↑これ、非同期プロット時、add の複数連続コールの狭間にわずかな synchronized の切れ目ができるので、
			//   その瞬間に Plotter が全系列データ get し得るのでまずい。やっぱ全系列を一気に置き換えるメソッドが無いと。
			//   たぶん「高速アニメーションしてる時に、稀に5系列中の3系列しかプロットされてない絵が生成される」とか発生する。
			//
			//   -> 確かに setData をハンドルする場合はそう。
			//      しかし appendData をハンドルする場合は本質的に add 挙動だし、
			//      タイミングによって系列が追加されたか or まだされてないかが未知で、系列数が半端になるのは避けられない。
			//
			//      -> 非同期プロットで appendData する場合は、それはユーザー側が当然想定すべきで。
			//         だってそれは「 系列追加の反映を非同期にやって」という事に他ならないので。
			//
			//         一方、非同期プロットで setData する場合、
			//         それは「全系列をまとめてセットしたから、その反映を非同期にやって」という事になるので、
			//         その描画結果のフレームで系列が半端に欠け得るのは許されないでしょ。
			//         それは操作から自然に想定される事では全くない。むしろ実装知らなければ完全に謎挙動。防ぐべき。
			//
			//         -> じゃあもっと set と append とでごそっと分岐した処理にすべき？
			//            clears でクリアするかどうかを制御するだけではなく。
			//            それとも、そもそもやっぱりクラスも別で分けるべき？
			//
			//            -> やっぱり分けた方がいい気がする。そこまで分岐するなら。
			//
			//    要検討


			// Re-plot the graph.
			if (asynchronousPlottingEnabled) {
				presenter.renderingLoop.requestPlotting();
			} else {
				presenter.plot();
			}
		}
	}
}
