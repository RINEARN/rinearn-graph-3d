package com.rinearn.graph3d.presenter.handler;

import com.rinearn.graph3d.config.LabelConfiguration;
import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.view.LabelSettingWindow;

import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import java.awt.event.ActionEvent;


/**
 * The class handling events and API requests for setting labels.
 */
public class LabelSettingHandler {

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
	public LabelSettingHandler(Model model, View view, Presenter presenter) {
		this.model = model;
		this.view = view;
		this.presenter = presenter;

		// Add the action listener defined in this class, to the OK button of label setting window.
		LabelSettingWindow window = this.view.labelSettingWindow;
		window.okButton.addActionListener(new OkPressedEventListener());
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





	// ================================================================================
	//
	// - Event Listeners -
	//
	// ================================================================================


	/**
	 * The event listener handling the event that OK button is pressed.
	 */
	private final class OkPressedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}

			// Get the inputted value of the labels of X/Y/Z axes.
			LabelSettingWindow window = view.labelSettingWindow;
			String xLabel = window.xLabelTextField.getText();
			String yLabel = window.yLabelTextField.getText();
			String zLabel = window.zLabelTextField.getText();

			// Store the above into the configuration container.
			LabelConfiguration labelConfig = model.config.getLabelConfiguration();
			labelConfig.getXLabelConfiguration().setText(xLabel);
			labelConfig.getYLabelConfiguration().setText(yLabel);
			labelConfig.getZLabelConfiguration().setText(zLabel);

			// Propagate the above update of the configuration to the entire application.
			presenter.propagateConfiguration();

			// Replot the graph.
			presenter.plot();
		}
	}





	// ================================================================================
	//
	// - API Listeners -
	//
	// ================================================================================


	/**
	 * Set the displayed text of X axis label.
	 * (API Implementation)
	 *
	 * @param xLabel The text of X axis label.
	 */
	public void setXLabel(String xLabel) {

		// Handle the API on the event-dispatcher thread.
		SetXLabelAPIListener apiListener = new SetXLabelAPIListener(xLabel);
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
	 * The class handling API requests from setXLabel(-) method,
	 * on event-dispatcher thread.
	 */
	private final class SetXLabelAPIListener implements Runnable {

		/** The text of X axis label. */
		private volatile String xLabel;

		/**
		 * Create an instance handling setXLabel(-) API with the specified argument.
		 *
		 * @param xLabel The text of X axis label.
		 */
		public SetXLabelAPIListener(String xLabel) {
			this.xLabel = xLabel;
		}

		@Override
		public void run() {
			LabelConfiguration.AxisLabelConfiguration xLabelConfig
					= model.config.getLabelConfiguration().getXLabelConfiguration();
			xLabelConfig.setText(xLabel);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}


	/**
	 * Set the displayed text of Y axis label.
	 * (API Implementation)
	 *
	 * @param yLabel The text of Y axis label.
	 */
	public void setYLabel(String yLabel) {

		// Handle the API on the event-dispatcher thread.
		SetYLabelAPIListener apiListener = new SetYLabelAPIListener(yLabel);
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
	 * The class handling API requests from setYLabel(-) method,
	 * on event-dispatcher thread.
	 */
	private final class SetYLabelAPIListener implements Runnable {

		/** The text of Y axis label. */
		private volatile String yLabel;

		/**
		 * Create an instance handling setYLabel(-) API with the specified argument.
		 *
		 * @param yLabel The text of Y axis label.
		 */
		public SetYLabelAPIListener(String yLabel) {
			this.yLabel = yLabel;
		}

		@Override
		public void run() {
			LabelConfiguration.AxisLabelConfiguration yLabelConfig
					= model.config.getLabelConfiguration().getYLabelConfiguration();
			yLabelConfig.setText(yLabel);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}


	/**
	 * Set the displayed text of Z axis label.
	 *
	 * @param zLabel The text of Z axis label.
	 */
	public void setZLabel(String zLabel) {

		// Handle the API on the event-dispatcher thread.
		SetZLabelAPIListener apiListener = new SetZLabelAPIListener(zLabel);
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
	 * The class handling API requests from setZLabel(-) method,
	 * on event-dispatcher thread.
	 */
	private final class SetZLabelAPIListener implements Runnable {

		/** The text of Z axis label. */
		private volatile String zLabel;

		/**
		 * Create an instance handling setZLabel(-) API with the specified argument.
		 *
		 * @param zLabel The text of Z axis label.
		 */
		public SetZLabelAPIListener(String zLabel) {
			this.zLabel = zLabel;
		}

		@Override
		public void run() {
			LabelConfiguration.AxisLabelConfiguration zLabelConfig
					= model.config.getLabelConfiguration().getZLabelConfiguration();
			zLabelConfig.setText(zLabel);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}

}
