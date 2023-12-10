package com.rinearn.graph3d.presenter.handler;

import com.rinearn.graph3d.config.RangeConfiguration;
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
			RangeSettingWindow window = view.rangeSettingWindow;

			// Get the references to the configuration containers to be modified.
			RangeConfiguration rangeConfig = model.config.getRangeConfiguration();
			RangeConfiguration.AxisRangeConfiguration xRangeConfig = rangeConfig.getXRangeConfiguration();
			RangeConfiguration.AxisRangeConfiguration yRangeConfig = rangeConfig.getYRangeConfiguration();
			RangeConfiguration.AxisRangeConfiguration zRangeConfig = rangeConfig.getZRangeConfiguration();

			// Detect whether the UI language is set to Japanese. (Necessary for generating error messages.)
			boolean isJapanese = model.config.getEnvironmentConfiguration().isLocaleJapanese();

			// X range:
			{
				// Get the inputted values and check them.
				boolean xAutoRangingEnabled = window.xAutoRangingBox.isSelected();
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
				xRangeConfig.setAutoRangingEnabled(xAutoRangingEnabled);
				xRangeConfig.setMaximum(xMax);
				xRangeConfig.setMinimum(xMin);
			}

			// Y range:
			{
				// Get the inputted values and check them.
				boolean yAutoRangingEnabled = window.yAutoRangingBox.isSelected();
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
				yRangeConfig.setAutoRangingEnabled(yAutoRangingEnabled);
				yRangeConfig.setMaximum(yMax);
				yRangeConfig.setMinimum(yMin);
			}

			// Z range:
			{
				// Get the inputted values and check them.
				boolean zAutoRangingEnabled = window.zAutoRangingBox.isSelected();
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
				zRangeConfig.setAutoRangingEnabled(zAutoRangingEnabled);
				zRangeConfig.setMaximum(zMax);
				zRangeConfig.setMinimum(zMin);
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
			RangeConfiguration.AxisRangeConfiguration xRangeConfig
					= model.config.getRangeConfiguration().getXRangeConfiguration();
			xRangeConfig.setMinimum(min);
			xRangeConfig.setMaximum(max);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}


	/**
	 * Turns on/off the auto-ranging feature for X axis.
	 * (API Implementation)
	 * 
	 * @param enabled Specify true/false for turning on/off (the default is on).
	 */
	public void setXAutoRangingEnabled(boolean enabled) {

		// Handle the API on the event-dispatcher thread.
		SetXAutoRangingEnabledAPIListener apiListener = new SetXAutoRangingEnabledAPIListener(enabled);
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
	 * The class handling API requests from setXAutoRangingEnabled(-) method,
	 * on event-dispatcher thread.
	 */
	private final class SetXAutoRangingEnabledAPIListener implements Runnable {

		/** The flag representing whether the auto ranging feature is enabled. */
		private final boolean enabled;

		/**
		 * Create an instance handling setXAutoRangingEnabled(-) API with the specified argument.
		 * 
		 * @param enabled Specify true/false for turning on/off (the default is on).
		 */
		public SetXAutoRangingEnabledAPIListener(boolean enabled) {
			this.enabled = enabled;
		}

		@Override
		public void run() {
			RangeConfiguration.AxisRangeConfiguration xRangeConfig
					= model.config.getRangeConfiguration().getXRangeConfiguration();
			xRangeConfig.setAutoRangingEnabled(enabled);
			presenter.propagateConfiguration();
			// presenter.replot(); // Not necessary.
		}
	}


	/**
	 * Sets the range of Y axis.
	 * (API Implementation)
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
			RangeConfiguration.AxisRangeConfiguration yRangeConfig
					= model.config.getRangeConfiguration().getYRangeConfiguration();
			yRangeConfig.setMinimum(min);
			yRangeConfig.setMaximum(max);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}

	/**
	 * Turns on/off the auto-ranging feature for Y axis.
	 * (API Implementation)
	 * 
	 * @param enabled Specify true/false for turning on/off (the default is on).
	 */
	public void setYAutoRangingEnabled(boolean enabled) {

		// Handle the API on the event-dispatcher thread.
		SetYAutoRangingEnabledAPIListener apiListener = new SetYAutoRangingEnabledAPIListener(enabled);
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
	 * The class handling API requests from setYAutoRangingEnabled(-) method,
	 * on event-dispatcher thread.
	 */
	private final class SetYAutoRangingEnabledAPIListener implements Runnable {

		/** The flag representing whether the auto ranging feature is enabled. */
		private final boolean enabled;

		/**
		 * Create an instance handling setYAutoRangingEnabled(-) API with the specified argument.
		 * 
		 * @param enabled Specify true/false for turning on/off (the default is on).
		 */
		public SetYAutoRangingEnabledAPIListener(boolean enabled) {
			this.enabled = enabled;
		}

		@Override
		public void run() {
			RangeConfiguration.AxisRangeConfiguration yRangeConfig
					= model.config.getRangeConfiguration().getYRangeConfiguration();
			yRangeConfig.setAutoRangingEnabled(enabled);
			presenter.propagateConfiguration();
			// presenter.replot(); Not necessary.
		}
	}


	/**
	 * Sets the range of Z axis.
	 * (API Implementation)
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
			RangeConfiguration.AxisRangeConfiguration zRangeConfig
					= model.config.getRangeConfiguration().getZRangeConfiguration();
			zRangeConfig.setMinimum(min);
			zRangeConfig.setMaximum(max);
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
	public void setZAutoRangingEnabled(boolean enabled) {

		// Handle the API on the event-dispatcher thread.
		SetZAutoRangingEnabledAPIListener apiListener = new SetZAutoRangingEnabledAPIListener(enabled);
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
	 * The class handling API requests from setZAutoRangingEnabled(-) method,
	 * on event-dispatcher thread.
	 */
	private final class SetZAutoRangingEnabledAPIListener implements Runnable {

		/** The flag representing whether the auto ranging feature is enabled. */
		private final boolean enabled;

		/**
		 * Create an instance handling setZAutoRangingEnabled(-) API with the specified argument.
		 * 
		 * @param enabled Specify true/false for turning on/off (the default is on).
		 */
		public SetZAutoRangingEnabledAPIListener(boolean enabled) {
			this.enabled = enabled;
		}

		@Override
		public void run() {
			RangeConfiguration.AxisRangeConfiguration zRangeConfig
					= model.config.getRangeConfiguration().getZRangeConfiguration();
			zRangeConfig.setAutoRangingEnabled(enabled);
			presenter.propagateConfiguration();
			// presenter.replot(); Not necessary.
		}
	}

}
