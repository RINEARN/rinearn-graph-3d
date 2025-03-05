package com.rinearn.graph3d.presenter.handler;

import com.rinearn.graph3d.config.range.RangeConfiguration;
import com.rinearn.graph3d.config.range.AxisRangeConfiguration;
import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.view.RangeSettingWindow;
import com.rinearn.graph3d.view.View;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;


/**
 * The class handling events and API requests for setting ranges.
 */
public final class RangeSettingHandler {

	/** The front-end class of "Model" layer, which provides internal logic procedures and so on. */
	private final Model model;

	/** The front-end class of "View" layer, which provides visible part of GUI without event handling. */
	private final View view;

	/** The front-end class of "Presenter" layer, which invokes Model's procedures triggered by user's action on GUI. */
	private final Presenter presenter;

	/** The event handler of right-click menu of x-max text field. */
	private final TextRightClickMenuHandler xMaxFieldMenuHandler;

	/** The event handler of right-click menu of x-min text field. */
	private final TextRightClickMenuHandler xMinFieldMenuHandler;

	/** The event handler of right-click menu of y-max text field. */
	private final TextRightClickMenuHandler yMaxFieldMenuHandler;

	/** The event handler of right-click menu of y-min text field. */
	private final TextRightClickMenuHandler yMinFieldMenuHandler;

	/** The event handler of right-click menu of z-max text field. */
	private final TextRightClickMenuHandler zMaxFieldMenuHandler;

	/** The event handler of right-click menu of z-min text field. */
	private final TextRightClickMenuHandler zMinFieldMenuHandler;

	/** The flag for turning on/off the event handling feature of this instance. */
	private volatile boolean eventHandlingEnabled = true;


	/**
	 * Create a new instance handling events and API requests using the specified resources.
	 *
	 * @param model The front-end class of "Model" layer, which provides internal logic procedures and so on.
	 * @param view The front-end class of "View" layer, which provides visible part of GUI without event handling.
	 * @param presenter The front-end class of "Presenter" layer, which handles events occurred on GUI, and API requests.
	 */
	public RangeSettingHandler(Model model, View view, Presenter presenter) {
		this.model = model;
		this.view = view;
		this.presenter = presenter;

		// Add the action listener defined in this class, to the OK button of label setting window.
		RangeSettingWindow window = this.view.rangeSettingWindow;
		window.okButton.addActionListener(new OkPressedEventListener());

		// Add the event listeners to auto-range check boxes.
		window.xAutoRangeBox.addActionListener(new XAutoRangeBoxClickedEventListener());
		window.yAutoRangeBox.addActionListener(new YAutoRangeBoxClickedEventListener());
		window.zAutoRangeBox.addActionListener(new ZAutoRangeBoxClickedEventListener());

		// Add the event listeners to the right-click menus.
		xMaxFieldMenuHandler = new TextRightClickMenuHandler(window.xMaxFieldRightClickMenu, window.xMaxField);
		xMinFieldMenuHandler = new TextRightClickMenuHandler(window.xMinFieldRightClickMenu, window.xMinField);
		yMaxFieldMenuHandler = new TextRightClickMenuHandler(window.yMaxFieldRightClickMenu, window.yMaxField);
		yMinFieldMenuHandler = new TextRightClickMenuHandler(window.yMinFieldRightClickMenu, window.yMinField);
		zMaxFieldMenuHandler = new TextRightClickMenuHandler(window.zMaxFieldRightClickMenu, window.zMaxField);
		zMinFieldMenuHandler = new TextRightClickMenuHandler(window.zMinFieldRightClickMenu, window.zMinField);
	}


	/**
	 * Turns on/off the event handling feature of this instance.
	 *
	 * @param enabled Specify false for turning off the event handling feature (enabled by default).
	 */
	public synchronized void setEventHandlingEnabled(boolean enabled) {
		this.eventHandlingEnabled = enabled;
		xMaxFieldMenuHandler.setEventHandlingEnabled(enabled);
		xMinFieldMenuHandler.setEventHandlingEnabled(enabled);
		yMaxFieldMenuHandler.setEventHandlingEnabled(enabled);
		yMinFieldMenuHandler.setEventHandlingEnabled(enabled);
		zMaxFieldMenuHandler.setEventHandlingEnabled(enabled);
		zMinFieldMenuHandler.setEventHandlingEnabled(enabled);
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
	 * The event listener handling the event that the X-Auto-Range check box is selected/unselected.
	 */
	private final class XAutoRangeBoxClickedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			RangeSettingWindow window = view.rangeSettingWindow;
			boolean enabled = window.xAutoRangeBox.isSelected();

			// Update the config and reflect to the window,
			// to update editable/non-editable states of min/max text fields.
			RangeConfiguration rangeConfig = model.config.getRangeConfiguration();
			rangeConfig.getXRangeConfiguration().setAutoRangeEnabled(enabled);
			window.configure(model.config);
		}
	}


	/**
	 * The event listener handling the event that the Y-Auto-Range check box is selected/unselected.
	 */
	private final class YAutoRangeBoxClickedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			RangeSettingWindow window = view.rangeSettingWindow;
			boolean enabled = window.yAutoRangeBox.isSelected();

			// Update the config and reflect to the window,
			// to update editable/non-editable states of min/max text fields.
			RangeConfiguration rangeConfig = model.config.getRangeConfiguration();
			rangeConfig.getYRangeConfiguration().setAutoRangeEnabled(enabled);
			window.configure(model.config);
		}
	}


	/**
	 * The event listener handling the event that the Z-Auto-Range check box is selected/unselected.
	 */
	private final class ZAutoRangeBoxClickedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			RangeSettingWindow window = view.rangeSettingWindow;
			boolean enabled = window.zAutoRangeBox.isSelected();

			// Update the config and reflect to the window,
			// to update editable/non-editable states of min/max text fields.
			RangeConfiguration rangeConfig = model.config.getRangeConfiguration();
			rangeConfig.getZRangeConfiguration().setAutoRangeEnabled(enabled);
			window.configure(model.config);
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
			RangeSettingWindow window = view.rangeSettingWindow;

			// Get the references to the configuration containers to be modified.
			RangeConfiguration rangeConfig = model.config.getRangeConfiguration();
			AxisRangeConfiguration xRangeConfig = rangeConfig.getXRangeConfiguration();
			AxisRangeConfiguration yRangeConfig = rangeConfig.getYRangeConfiguration();
			AxisRangeConfiguration zRangeConfig = rangeConfig.getZRangeConfiguration();

			// Detect whether the UI language is set to Japanese. (Necessary for generating error messages.)
			boolean isJapanese = model.config.getEnvironmentConfiguration().isLocaleJapanese();

			// X range:
			{
				// Get the inputted values and check them.
				boolean xAutoRangeEnabled = window.xAutoRangeBox.isSelected();
				BigDecimal xMax = null;
				BigDecimal xMin = null;
				try {
					xMax = new BigDecimal(window.xMaxField.getText());
				} catch (NumberFormatException nfe) {
					String errorMessage = isJapanese ?
							"X軸の最大値を解釈できません。\n入力値が正しい数値か、確認してください。" :
							"Can not parse \"Max\" value of X axis.\nPlease check that input value is a correct numeric value.";
					JOptionPane.showMessageDialog(window.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					xMin = new BigDecimal(window.xMinField.getText());
				} catch (NumberFormatException nfe) {
					String errorMessage = isJapanese ?
							"X軸の最小値を解釈できません。\n入力値が正しい数値か、確認してください。" :
							"Can not parse \"Min\" value of X axis.\nPlease check that input value is a correct numeric value.";
					JOptionPane.showMessageDialog(window.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
					return;
				}

				// Store the above into the configuration container.
				xRangeConfig.setAutoRangeEnabled(xAutoRangeEnabled);
				xRangeConfig.setMaximumCoordinate(xMax);
				xRangeConfig.setMinimumCoordinate(xMin);
			}

			// Y range:
			{
				// Get the inputted values and check them.
				boolean yAutoRangeEnabled = window.yAutoRangeBox.isSelected();
				BigDecimal yMax = null;
				BigDecimal yMin = null;
				try {
					yMax = new BigDecimal(window.yMaxField.getText());
				} catch (NumberFormatException nfe) {
					String errorMessage = isJapanese ?
							"Y軸の最大値を解釈できません。\n入力値が正しい数値か、確認してください。" :
							"Can not parse \"Max\" value of Y axis.\nPlease check that input value is a correct numeric value.";
					JOptionPane.showMessageDialog(window.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					yMin = new BigDecimal(window.yMinField.getText());
				} catch (NumberFormatException nfe) {
					String errorMessage = isJapanese ?
							"Y軸の最小値を解釈できません。\n入力値が正しい数値か、確認してください。" :
							"Can not parse \"Min\" value of Y axis.\nPlease check that input value is a correct numeric value.";
					JOptionPane.showMessageDialog(window.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
					return;
				}

				// Store the above into the configuration container.
				yRangeConfig.setAutoRangeEnabled(yAutoRangeEnabled);
				yRangeConfig.setMaximumCoordinate(yMax);
				yRangeConfig.setMinimumCoordinate(yMin);
			}

			// Z range:
			{
				// Get the inputted values and check them.
				boolean zAutoRangeEnabled = window.zAutoRangeBox.isSelected();
				BigDecimal zMax = null;
				BigDecimal zMin = null;
				try {
					zMax = new BigDecimal(window.zMaxField.getText());
				} catch (NumberFormatException nfe) {
					String errorMessage = isJapanese ?
							"Z軸の最大値を解釈できません。\n入力値が正しい数値か、確認してください。" :
							"Can not parse \"Max\" value of Z axis.\nPlease check that input value is a correct numeric value.";
					JOptionPane.showMessageDialog(window.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					zMin = new BigDecimal(window.zMinField.getText());
				} catch (NumberFormatException nfe) {
					String errorMessage = isJapanese ?
							"Z軸の最小値を解釈できません。\n入力値が正しい数値か、確認してください。" :
							"Can not parse \"Min\" value of Z axis.\nPlease check that input value is a correct numeric value.";
					JOptionPane.showMessageDialog(window.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
					return;
				}

				// Store the above into the configuration container.
				zRangeConfig.setAutoRangeEnabled(zAutoRangeEnabled);
				zRangeConfig.setMaximumCoordinate(zMax);
				zRangeConfig.setMinimumCoordinate(zMin);
			}

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
	 * Sets the range of X axis.
	 * (API Implementation)
	 *
	 * Please note that, this API automatically disables the auto-range feature for X axis.
	 *
	 * @param min The minimum coordinate value of X axis.
	 * @param max The maximum coordinate value of X axis.
	 */
	public void setXRange(BigDecimal min, BigDecimal max) {

		// Handle the API on the event-dispatcher thread.
		SetXRangeAPIListener apiListener = new SetXRangeAPIListener(min, max);
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
	 * The class handling API requests from setXRange(-) method,
	 * on event-dispatcher thread.
	 *
	 * Please note that, this API automatically disables the auto-range feature for X axis.
	 */
	private final class SetXRangeAPIListener implements Runnable {

		/** The minimum coordinate value of X axis. */
		private final BigDecimal min;

		/** The maximum coordinate value of X axis. */
		private final BigDecimal max;

		/**
		 * Create an instance handling setXRange(-) API with the specified argument.
		 *
		 * @param min The minimum coordinate value of X axis.
		 * @param max The maximum coordinate value of X axis.
		 */
		public SetXRangeAPIListener(BigDecimal min, BigDecimal max) {
			this.min = min;
			this.max = max;
		}

		@Override
		public void run() {
			AxisRangeConfiguration xRangeConfig = model.config.getRangeConfiguration().getXRangeConfiguration();
			xRangeConfig.setAutoRangeEnabled(false);
			xRangeConfig.setMinimumCoordinate(min);
			xRangeConfig.setMaximumCoordinate(max);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}


	/**
	 * Turns on/off the auto-range feature for X axis.
	 * (API Implementation)
	 *
	 * @param enabled Specify true/false for turning on/off (the default is on).
	 */
	public void setXAutoRangeEnabled(boolean enabled) {

		// Handle the API on the event-dispatcher thread.
		SetXAutoRangeEnabledAPIListener apiListener = new SetXAutoRangeEnabledAPIListener(enabled);
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
	 * The class handling API requests from setXAutoRangeEnabled(-) method,
	 * on event-dispatcher thread.
	 */
	private final class SetXAutoRangeEnabledAPIListener implements Runnable {

		/** The flag representing whether the auto ranging feature is enabled. */
		private final boolean enabled;

		/**
		 * Create an instance handling setXAutoRangeEnabled(-) API with the specified argument.
		 *
		 * @param enabled Specify true/false for turning on/off (the default is on).
		 */
		public SetXAutoRangeEnabledAPIListener(boolean enabled) {
			this.enabled = enabled;
		}

		@Override
		public void run() {
			AxisRangeConfiguration xRangeConfig = model.config.getRangeConfiguration().getXRangeConfiguration();
			xRangeConfig.setAutoRangeEnabled(enabled);
			presenter.propagateConfiguration();
			// presenter.replot(); // Not necessary.
		}
	}


	/**
	 * Sets the range of Y axis.
	 * (API Implementation)
	 *
	 * Please note that, this API automatically disables the auto-range feature for Y axis.
	 *
	 * @param min The minimum coordinate value of Y axis.
	 * @param max The maximum coordinate value of Y axis.
	 */
	public void setYRange(BigDecimal min, BigDecimal max) {

		// Handle the API on the event-dispatcher thread.
		SetYRangeAPIListener apiListener = new SetYRangeAPIListener(min, max);
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
	 * The class handling API requests from setYRange(-) method,
	 * on event-dispatcher thread.
	 *
	 * Please note that, this API automatically disables the auto-range feature for Y axis.
	 */
	private final class SetYRangeAPIListener implements Runnable {

		/** The minimum coordinate value of X axis. */
		private final BigDecimal min;

		/** The maximum coordinate value of X axis. */
		private final BigDecimal max;

		/**
		 * Create an instance handling setYRange(-) API with the specified argument.
		 *
		 * @param min The minimum coordinate value of Y axis.
		 * @param max The maximum coordinate value of Y axis.
		 */
		public SetYRangeAPIListener(BigDecimal min, BigDecimal max) {
			this.min = min;
			this.max = max;
		}

		@Override
		public void run() {
			AxisRangeConfiguration yRangeConfig = model.config.getRangeConfiguration().getYRangeConfiguration();
			yRangeConfig.setAutoRangeEnabled(false);
			yRangeConfig.setMinimumCoordinate(min);
			yRangeConfig.setMaximumCoordinate(max);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}

	/**
	 * Turns on/off the auto-range feature for Y axis.
	 * (API Implementation)
	 *
	 * @param enabled Specify true/false for turning on/off (the default is on).
	 */
	public void setYAutoRangeEnabled(boolean enabled) {

		// Handle the API on the event-dispatcher thread.
		SetYAutoRangeEnabledAPIListener apiListener = new SetYAutoRangeEnabledAPIListener(enabled);
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
	 * The class handling API requests from setYAutoRangeEnabled(-) method,
	 * on event-dispatcher thread.
	 */
	private final class SetYAutoRangeEnabledAPIListener implements Runnable {

		/** The flag representing whether the auto ranging feature is enabled. */
		private final boolean enabled;

		/**
		 * Create an instance handling setYAutoRangeEnabled(-) API with the specified argument.
		 *
		 * @param enabled Specify true/false for turning on/off (the default is on).
		 */
		public SetYAutoRangeEnabledAPIListener(boolean enabled) {
			this.enabled = enabled;
		}

		@Override
		public void run() {
			AxisRangeConfiguration yRangeConfig = model.config.getRangeConfiguration().getYRangeConfiguration();
			yRangeConfig.setAutoRangeEnabled(enabled);
			presenter.propagateConfiguration();
			// presenter.replot(); Not necessary.
		}
	}


	/**
	 * Sets the range of Z axis.
	 * (API Implementation)
	 *
	 * Please note that, this API automatically disables the auto-range feature for Z axis.
	 *
	 * @param min The minimum coordinate value of Z axis.
	 * @param max The maximum coordinate value of Z axis.
	 */
	public void setZRange(BigDecimal min, BigDecimal max) {

		// Handle the API on the event-dispatcher thread.
		SetZRangeAPIListener apiListener = new SetZRangeAPIListener(min, max);
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
	 * The class handling API requests from setZRange(-) method,
	 * on event-dispatcher thread.
	 *
	 * Please note that, this API automatically disables the auto-range feature for Z axis.
	 */
	private final class SetZRangeAPIListener implements Runnable {

		/** The minimum coordinate value of Z axis. */
		private final BigDecimal min;

		/** The maximum coordinate value of Z axis. */
		private final BigDecimal max;

		/**
		 * Create an instance handling setZRange(-) API with the specified argument.
		 *
		 * @param min The minimum coordinate value of Z axis.
		 * @param max The maximum coordinate value of Z axis.
		 */
		public SetZRangeAPIListener(BigDecimal min, BigDecimal max) {
			this.min = min;
			this.max = max;
		}

		@Override
		public void run() {
			AxisRangeConfiguration zRangeConfig = model.config.getRangeConfiguration().getZRangeConfiguration();
			zRangeConfig.setAutoRangeEnabled(false);
			zRangeConfig.setMinimumCoordinate(min);
			zRangeConfig.setMaximumCoordinate(max);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}


	/**
	 * Turns on/off the auto-ranging feature for Z axis.
	 * (API Implementation)
	 *
	 * @param enabled Specify true/false for turning on/off (the default is on).
	 */
	public void setZAutoRangeEnabled(boolean enabled) {

		// Handle the API on the event-dispatcher thread.
		SetZAutoRangeEnabledAPIListener apiListener = new SetZAutoRangeEnabledAPIListener(enabled);
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
	 * The class handling API requests from setZAutoRangeEnabled(-) method,
	 * on event-dispatcher thread.
	 */
	private final class SetZAutoRangeEnabledAPIListener implements Runnable {

		/** The flag representing whether the auto ranging feature is enabled. */
		private final boolean enabled;

		/**
		 * Create an instance handling setZAutoRangeEnabled(-) API with the specified argument.
		 *
		 * @param enabled Specify true/false for turning on/off (the default is on).
		 */
		public SetZAutoRangeEnabledAPIListener(boolean enabled) {
			this.enabled = enabled;
		}

		@Override
		public void run() {
			AxisRangeConfiguration zRangeConfig = model.config.getRangeConfiguration().getZRangeConfiguration();
			zRangeConfig.setAutoRangeEnabled(enabled);
			presenter.propagateConfiguration();
			// presenter.replot(); Not necessary.
		}
	}

}
