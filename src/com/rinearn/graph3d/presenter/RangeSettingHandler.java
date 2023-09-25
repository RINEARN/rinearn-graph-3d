package com.rinearn.graph3d.presenter;

import com.rinearn.graph3d.config.RangeConfiguration;
import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.view.RangeSettingWindow;
import com.rinearn.graph3d.view.View;

import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	 * Sets the range of X axis.
	 * 
	 * @param min The minimum coordinate value of X axis.
	 * @param max The maximum coordinate value of X axis.
	 */
	public synchronized void setXRange(BigDecimal min, BigDecimal max) {
		RangeConfiguration.AxisRangeConfiguration xRangeConfig
			= this.model.getConfiguration().getRangeConfiguration().getXRangeConfiguration();
		xRangeConfig.setMinimum(min);
		xRangeConfig.setMaximum(max);
		this.presenter.propagateConfiguration();
		this.presenter.plot();
	}

	/**
	 * Turns on/off the auto-ranging feature for X axis.
	 * 
	 * @param enabled Specify true/false for turning on/off (the default is on).
	 */
	public synchronized void setXAutoRangingEnabled(boolean enabled) {
		RangeConfiguration.AxisRangeConfiguration xRangeConfig
			= this.model.getConfiguration().getRangeConfiguration().getXRangeConfiguration();
		xRangeConfig.setAutoRangingEnabled(enabled);
		this.presenter.propagateConfiguration();
		// this.presenter.replot(); // Not necessary.
	}


	/**
	 * Sets the range of Y axis.
	 * 
	 * @param min The minimum coordinate value of Y axis.
	 * @param max The maximum coordinate value of Y axis.
	 */
	public synchronized void setYRange(BigDecimal min, BigDecimal max) {
		RangeConfiguration.AxisRangeConfiguration yRangeConfig
			= this.model.getConfiguration().getRangeConfiguration().getYRangeConfiguration();
		yRangeConfig.setMinimum(min);
		yRangeConfig.setMaximum(max);
		this.presenter.propagateConfiguration();
		this.presenter.plot();
	}

	/**
	 * Turns on/off the auto-ranging feature for Y axis.
	 * 
	 * @param enabled Specify true/false for turning on/off (the default is on).
	 */
	public synchronized void setYAutoRangingEnabled(boolean enabled) {
		RangeConfiguration.AxisRangeConfiguration yRangeConfig
			= this.model.getConfiguration().getRangeConfiguration().getYRangeConfiguration();
		yRangeConfig.setAutoRangingEnabled(enabled);
		this.presenter.propagateConfiguration();
		// this.presenter.replot(); Not necessary.
	}


	/**
	 * Sets the range of Z axis.
	 * 
	 * @param min The minimum coordinate value of Z axis.
	 * @param max The maximum coordinate value of Z axis.
	 */
	public synchronized void setZRange(BigDecimal min, BigDecimal max) {
		RangeConfiguration.AxisRangeConfiguration zRangeConfig
			= this.model.getConfiguration().getRangeConfiguration().getZRangeConfiguration();
		zRangeConfig.setMinimum(min);
		zRangeConfig.setMaximum(max);
		this.presenter.propagateConfiguration();
		this.presenter.plot();
	}

	/**
	 * Turns on/off the auto-ranging feature for Z axis.
	 * 
	 * @param enabled Specify true/false for turning on/off (the default is on).
	 */
	public synchronized void setZAutoRangingEnabled(boolean enabled) {
		RangeConfiguration.AxisRangeConfiguration zRangeConfig
			= this.model.getConfiguration().getRangeConfiguration().getZRangeConfiguration();
		zRangeConfig.setAutoRangingEnabled(enabled);
		this.presenter.propagateConfiguration();
		// this.presenter.replot(); Not necessary.
	}


	/**
	 * The event listener handling the event that OK button is pressed.
	 */
	private final class OkPressedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			RangeSettingWindow window = view.rangeSettingWindow;

			// Get the references to the configuration containers to be modified.
			RangeConfiguration rangeConfig = model.getConfiguration().getRangeConfiguration();
			RangeConfiguration.AxisRangeConfiguration xRangeConfig = rangeConfig.getXRangeConfiguration();
			RangeConfiguration.AxisRangeConfiguration yRangeConfig = rangeConfig.getYRangeConfiguration();
			RangeConfiguration.AxisRangeConfiguration zRangeConfig = rangeConfig.getZRangeConfiguration();

			// Detect whether the UI language is set to Japanese. (Necessary for generating error messages.)
			boolean isJapanese = model.getConfiguration().getEnvironmentConfiguration().isLocaleJapanese();

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
				String yMaxString = window.yMaxField.getText();
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
				String zMaxString = window.zMaxField.getText();
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
}
