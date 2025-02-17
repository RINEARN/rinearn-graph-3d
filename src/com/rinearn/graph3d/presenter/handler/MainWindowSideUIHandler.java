package com.rinearn.graph3d.presenter.handler;

import com.rinearn.graph3d.config.frame.FrameConfiguration;
import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.view.MainWindow;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

/**
 * The class handling events of the screen-side UI on the main window.
 */
public class MainWindowSideUIHandler {

	/** The front-end class of "Model" layer, which provides internal logic procedures and so on. */
	private final Model model;

	/** The front-end class of "View" layer, which provides visible part of GUI without event handling. */
	private final View view;

	/** The front-end class of "Presenter" layer, which invokes Model's procedures triggered by user's action on GUI. */
	private final Presenter presenter;

	/** The flag for turning on/off the event handling feature of this instance. */
	private volatile boolean eventHandlingEnabled = true;


	/**
	 * Create a new instance handling events and API requests using the specified resources.
	 *
	 * @param model The front-end class of "Model" layer, which provides internal logic procedures and so on.
	 * @param view The front-end class of "View" layer, which provides visible part of GUI without event handling.
	 * @param presenter The front-end class of "Presenter" layer, which handles events occurred on GUI, and API requests.
	 */
	public MainWindowSideUIHandler(Model model, View view, Presenter presenter) {
		this.model = model;
		this.view = view;
		this.presenter = presenter;

		MainWindow window = this.view.mainWindow;

		// Add the event listeners to the scroll bars on the main window, which controls the length factors of X/Y/Z axes.
		window.xAxisLengthBar.addAdjustmentListener(new XAxisLengthBarScrolledEventListener());
		window.yAxisLengthBar.addAdjustmentListener(new YAxisLengthBarScrolledEventListener());
		window.zAxisLengthBar.addAdjustmentListener(new ZAxisLengthBarScrolledEventListener());
	}


	/**
	 * Turns on/off the event handling feature of this instance.
	 *
	 * @param enabled Specify false for turning off the event handling feature (enabled by default).
	 */
	public synchronized void setEventHandlingEnabled(boolean enabled) {
		this.eventHandlingEnabled = enabled;
	}


	/**
	 * Gets whether the event handling feature of this instance is enabled.
	 *
	 * @return Returns true if the event handling feature is enabled.
	 */
	public synchronized boolean isEventHandlingEnabled() {
		return this.eventHandlingEnabled;
	}


	/**
	 * The event listener handling the event that the scroll bar of "X Axis Bar" is moved.
	 */
	private final class XAxisLengthBarScrolledEventListener implements AdjustmentListener {
		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			MainWindow window = view.mainWindow;
			FrameConfiguration frameConfig = model.config.getFrameConfiguration();

			double sliderRatio = (double)window.xAxisLengthBar.getValue() / (double)MainWindow.AXIS_LENGTH_BAR_MAX_COUNT;
			double lengthFactor = (1.0 - sliderRatio) * 2.0;
			frameConfig.getXFrameConfiguration().setLengthFactor(lengthFactor);

			// Propagate the above update of the configuration to the entire application.
			setEventHandlingEnabled(false);
			presenter.propagateConfiguration();
			setEventHandlingEnabled(true);

			// Perform rendering on the rendering loop's thread asynchronously.
			presenter.renderingLoop.requestRendering();
		}
	}

	/**
	 * The event listener handling the event that the scroll bar of "Y Axis Length Bar" is moved.
	 */
	private final class YAxisLengthBarScrolledEventListener implements AdjustmentListener {
		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			MainWindow window = view.mainWindow;
			FrameConfiguration frameConfig = model.config.getFrameConfiguration();

			double sliderRatio = (double)window.yAxisLengthBar.getValue() / (double)MainWindow.AXIS_LENGTH_BAR_MAX_COUNT;
			double lengthFactor = (1.0 - sliderRatio) * 2.0;
			frameConfig.getYFrameConfiguration().setLengthFactor(lengthFactor);

			// Propagate the above update of the configuration to the entire application.
			setEventHandlingEnabled(false);
			presenter.propagateConfiguration();
			setEventHandlingEnabled(true);

			// Perform rendering on the rendering loop's thread asynchronously.
			presenter.renderingLoop.requestRendering();
		}
	}

	/**
	 * The event listener handling the event that the scroll bar of "Z Axis Length Bar" is moved.
	 */
	private final class ZAxisLengthBarScrolledEventListener implements AdjustmentListener {
		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			MainWindow window = view.mainWindow;
			FrameConfiguration frameConfig = model.config.getFrameConfiguration();

			double sliderRatio = (double)window.zAxisLengthBar.getValue() / (double)MainWindow.AXIS_LENGTH_BAR_MAX_COUNT;
			double lengthFactor = (1.0 - sliderRatio) * 2.0;
			frameConfig.getZFrameConfiguration().setLengthFactor(lengthFactor);

			// Propagate the above update of the configuration to the entire application.
			setEventHandlingEnabled(false);
			presenter.propagateConfiguration();
			setEventHandlingEnabled(true);

			// Perform rendering on the rendering loop's thread asynchronously.
			presenter.renderingLoop.requestRendering();
		}
	}
}
