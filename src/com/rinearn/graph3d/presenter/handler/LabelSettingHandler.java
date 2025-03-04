package com.rinearn.graph3d.presenter.handler;

import com.rinearn.graph3d.config.label.LabelConfiguration;
import com.rinearn.graph3d.config.label.AxisLabelConfiguration;
import com.rinearn.graph3d.config.label.LegendLabelConfiguration;
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

	/** The event handler of the right-click menu of the text field to input the x-label. */
	private final TextRightClickMenuHandler xLabelTextFieldMenuHandler;

	/** The event handler of the right-click menu of the text field to input the y-label. */
	private final TextRightClickMenuHandler yLabelTextFieldMenuHandler;

	/** The event handler of the right-click menu of the text field to input the z-label. */
	private final TextRightClickMenuHandler zLabelTextFieldMenuHandler;

	/** The event handler of the right-click menu of the text area to edit legends. */
	private final TextRightClickMenuHandler legendAreaMenuHandler;

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

		// Add the action listener to the check box to enable/disable the auto-legend-generation feature.
		window.autoLegendGenerationBox.addActionListener(new AutoLegendGenerationBoxSelectedEventListener());

		// Add the action listener to the check box to enable/disable the gap-removal feature.
		window.gapRemovalBox.addActionListener(new GapRemovalBoxSelectedEventListener());

		// Add the event handlers to the right-click menus of the text fields/area.
		xLabelTextFieldMenuHandler = new TextRightClickMenuHandler(window.xLabelFieldRightClickMenu, window.xLabelField);
		yLabelTextFieldMenuHandler = new TextRightClickMenuHandler(window.yLabelFieldRightClickMenu, window.yLabelField);
		zLabelTextFieldMenuHandler = new TextRightClickMenuHandler(window.zLabelFieldRightClickMenu, window.zLabelTextField);
		legendAreaMenuHandler = new TextRightClickMenuHandler(window.legendAreaRightClickMenu, window.legendArea);
	}


	/**
	 * Turns on/off the event handling feature of this instance.
	 *
	 * @param enabled Specify false for turning off the event handling feature (enabled by default).
	 */
	public synchronized void setEventHandlingEnabled(boolean enabled) {
		this.eventHandlingEnabled = enabled;
		xLabelTextFieldMenuHandler.setEventHandlingEnabled(enabled);
		yLabelTextFieldMenuHandler.setEventHandlingEnabled(enabled);
		zLabelTextFieldMenuHandler.setEventHandlingEnabled(enabled);
		legendAreaMenuHandler.setEventHandlingEnabled(enabled);
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
	 * The event listener handling the event that the auto-legend-generation check box is selected/unselected.
	 */
	private final class AutoLegendGenerationBoxSelectedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			LabelSettingWindow window = view.labelSettingWindow;
			boolean enabled = window.autoLegendGenerationBox.isSelected();

			// Update config.
			LegendLabelConfiguration legendLabelConfig = model.config.getLabelConfiguration().getLegendLabelConfiguration();
			legendLabelConfig.setAutoLegendGenerationEnabled(enabled);

			// Update UI.
			window.updateLegendSection(model.config.getLabelConfiguration().getLegendLabelConfiguration());
		}
	}


	/**
	 * The event listener handling the event that the gap-removal check box in legend section is selected/unselected.
	 */
	private final class GapRemovalBoxSelectedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			LabelSettingWindow window = view.labelSettingWindow;
			boolean enabled = window.gapRemovalBox.isSelected();

			// Update config.
			LegendLabelConfiguration legendLabelConfig = model.config.getLabelConfiguration().getLegendLabelConfiguration();
			legendLabelConfig.setEmptyLegendExclusionEnabled(enabled);

			// Update UI.
			window.updateLegendSection(model.config.getLabelConfiguration().getLegendLabelConfiguration());
		}
	}


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
			String xLabel = window.xLabelField.getText();
			String yLabel = window.yLabelField.getText();
			String zLabel = window.zLabelTextField.getText();

			// Store the above into the configuration container.
			LabelConfiguration labelConfig = model.config.getLabelConfiguration();
			labelConfig.getXLabelConfiguration().setLabelText(xLabel);
			labelConfig.getYLabelConfiguration().setLabelText(yLabel);
			labelConfig.getZLabelConfiguration().setLabelText(zLabel);

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
			AxisLabelConfiguration xLabelConfig = model.config.getLabelConfiguration().getXLabelConfiguration();
			xLabelConfig.setLabelText(this.xLabel);
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
			AxisLabelConfiguration yLabelConfig = model.config.getLabelConfiguration().getYLabelConfiguration();
			yLabelConfig.setLabelText(this.yLabel);
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
			AxisLabelConfiguration zLabelConfig = model.config.getLabelConfiguration().getZLabelConfiguration();
			zLabelConfig.setLabelText(this.zLabel);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}


	/**
	 * Set the legends of the series plotted on the graph.
	 * (API Implementation)
	 *
	 * This API method automatically disables the auto-legend-generation feature.
	 * When you want to re-enable it, use setAutoLegendGenerationEnabled(boolean) method explicitly.
	 *
	 * @param legends The legends of the series plotted on the graph.
	 */
	public void setLegends(String[] legends) {

		// Handle the API on the event-dispatcher thread.
		SetLegendsAPIListener apiListener = new SetLegendsAPIListener(legends);
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
	 * The class handling API requests from setLegends(-) method,
	 * on event-dispatcher thread.
	 *
	 * This API method automatically disables the auto-legend-generation feature.
	 * When you want to re-enable it, use setAutoLegendGenerationEnabled(boolean) method explicitly.
	 */
	private final class SetLegendsAPIListener implements Runnable {

		/** The legends. */
		private volatile String[] legends;

		/**
		 * Create an instance handling setXLabel(-) API with the specified argument.
		 *
		 * @param legends The legends.
		 */
		public SetLegendsAPIListener(String[] legends) {
			this.legends = legends;
		}

		@Override
		public void run() {
			LegendLabelConfiguration legendLabelConfig = model.config.getLabelConfiguration().getLegendLabelConfiguration();
			legendLabelConfig.setAutoLegendGenerationEnabled(false);
			legendLabelConfig.setLabelTexts(this.legends);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}


	/**
	 * Enables/disables the auto-legend-generation feature..
	 * (API Implementation)
	 *
	 * @param enabled Specify true to enable the auto-legend-generation feature.
	 */
	public void setAutoLegendGenerationEnabled(boolean enabled) {

		// Handle the API on the event-dispatcher thread.
		AutoLegendGenerationAPIListener apiListener = new AutoLegendGenerationAPIListener(enabled);
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
	 * The class handling API requests from setAutoLegendGenerationEnabled(-) method,
	 * on event-dispatcher thread.
	 */
	private final class AutoLegendGenerationAPIListener implements Runnable {

		/** The flag representing whether the auto-legend-generation feature is enabled. */
		private volatile boolean enabled;

		/**
		 * Create an instance handling setAutoLegendGenerationEnabled(-) API with the specified argument.
		 *
		 * @param legends The legends.
		 */
		public AutoLegendGenerationAPIListener(boolean enabled) {
			this.enabled = enabled;
		}

		@Override
		public void run() {
			LegendLabelConfiguration legendLabelConfig = model.config.getLabelConfiguration().getLegendLabelConfiguration();
			legendLabelConfig.setAutoLegendGenerationEnabled(this.enabled);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}
}
