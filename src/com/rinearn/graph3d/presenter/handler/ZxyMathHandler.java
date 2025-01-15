package com.rinearn.graph3d.presenter.handler;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.model.data.series.ZxyMathDataSeries;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.view.ZxyMathWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;


/**
 * The class handling events and API requests for plotting math expressions of "z(x,y)" form.
 */
public class ZxyMathHandler {

	/** The front-end class of "Model" layer, which provides internal logic procedures and so on. */
	private final Model model;

	/** The front-end class of "View" layer, which provides visible part of GUI without event handling. */
	private final View view;

	/** The front-end class of "Presenter" layer, which invokes Model's procedures triggered by user's action on GUI. */
	private final Presenter presenter;

	/** The flag for turning on/off the event handling feature of this instance. */
	private volatile boolean eventHandlingEnabled = true;

	/** The event handler of the right-click menu of the text field to input z(x,y) math expression. */
	private volatile TextRightClickMenuHandler zMathExpressionFieldMenuHandler = null;

	/** The event handler of the right-click menu of the text field to input x-resolution. */
	private volatile TextRightClickMenuHandler xResolutionFieldMenuHandler = null;

	/** The event handler of the right-click menu of the text field to input y-resolution. */
	private volatile TextRightClickMenuHandler yResolutionFieldMenuHandler = null;

	/** The currentMode of z(x,y) math tool. */
	public enum Mode {

		/** The currentMode to plot a new expression. */
		PLOT,

		/** The currentMode to update an existing expression. */
		UPDATE,

		/** Represents the currentMode is not set yet, or has been unset. */
		UNSET;
	}

	/** The current currentMode of z(x,y) math tool. */
	private volatile Mode currentMode = Mode.UNSET;

	/** The math data series to be updated in UPDATE currentMode. */
	private volatile ZxyMathDataSeries updateTargetMathDataSeries = null;


	/**
	 * Create a new instance handling events and API requests using the specified resources.
	 *
	 * @param model The front-end class of "Model" layer, which provides internal logic procedures and so on.
	 * @param view The front-end class of "View" layer, which provides visible part of GUI without event handling.
	 * @param presenter The front-end class of "Presenter" layer, which handles events occurred on GUI, and API requests.
	 */
	public ZxyMathHandler(Model model, View view, Presenter presenter) {
		this.model = model;
		this.view = view;
		this.presenter = presenter;
		ZxyMathWindow window = this.view.zxyMathWindow;

		// Add the action listener defined in this class, to the UI components in the window of "z(x,y)" plot.
		window.plotButton.addActionListener(new PlotButtonPressedEventListener());

		// Add the event listeners of the right click menus of the text fields.
		this.zMathExpressionFieldMenuHandler = new TextRightClickMenuHandler(
				window.zMathExpressionFieldRightClickMenu, window.zMathExpressionField
		);
		this.xResolutionFieldMenuHandler = new TextRightClickMenuHandler(
				window.xResolutionFieldRightClickMenu, window.xResolutionField
		);
		this.yResolutionFieldMenuHandler = new TextRightClickMenuHandler(
				window.yResolutionFieldRightClickMenu, window.yResolutionField
		);
	}


	/**
	 * Turns on/off the event handling feature of this instance.
	 *
	 * @param enabled Specify false for turning off the event handling feature (enabled by default).
	 */
	public synchronized void setEventHandlingEnabled(boolean enabled) {
		this.eventHandlingEnabled = enabled;
		this.zMathExpressionFieldMenuHandler.setEventHandlingEnabled(enabled);
		this.xResolutionFieldMenuHandler.setEventHandlingEnabled(enabled);
		this.yResolutionFieldMenuHandler.setEventHandlingEnabled(enabled);
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
	 * Sets the currentMode of z(x,y) math tool.
	 *
	 * @param currentMode The currentMode to be set (PLOT or UPDATE).
	 */
	public synchronized void setMode(Mode mode) {
		this.currentMode = mode;

		// Unset the target math data series if it is set.
		if (mode != Mode.UPDATE) {
			this.updateTargetMathDataSeries = null;
		}

		// Update the UI of z(x,y) math tool.
		ZxyMathWindow.Mode uiMode = null;
		switch (mode) {
			case PLOT:   uiMode = ZxyMathWindow.Mode.PLOT; break;
			case UPDATE: uiMode = ZxyMathWindow.Mode.UPDATE; break;
			case UNSET:  uiMode = ZxyMathWindow.Mode.UNSET; break;
			default: throw new IllegalStateException("Unexpected mode: " + mode);
		}
		this.view.zxyMathWindow.updateUIForMode(uiMode, this.model.config.getEnvironmentConfiguration());
	}


	/**
	 * Sets the math data series to be updated in UPDATE currentMode.
	 *
	 * @param updateTargetMathDataSeries The math data series to be updated.
	 */
	public synchronized void setUpdateTargetMathDataSeries(ZxyMathDataSeries updateTargetMathDataSeries) {
		this.updateTargetMathDataSeries = updateTargetMathDataSeries;

		// Update the input values in the text fields on the window.
		ZxyMathWindow window = this.view.zxyMathWindow;
		window.zMathExpressionField.setText(this.updateTargetMathDataSeries.getZMathExpression());
		window.xResolutionField.setText(Integer.toString(this.updateTargetMathDataSeries.getXDiscretizationCount()));
		window.yResolutionField.setText(Integer.toString(this.updateTargetMathDataSeries.getYDiscretizationCount()));
	}


	/**
	 * The event listener handling the event that PLOT/UPDATE button is pressed.
	 */
	private final class PlotButtonPressedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			ZxyMathWindow window = view.zxyMathWindow;

			// Detect whether the UI language is set to Japanese. (Necessary for generating error messages.)
			boolean isJapanese = model.config.getEnvironmentConfiguration().isLocaleJapanese();

			// Get the inputted math expression of "z(x,y)" form.
			String zMathExpression = window.zMathExpressionField.getText();

			// Parse and check the values of resolutions (= numbers of discretization points for X/Y directions).
			int xDiscretizationCount = -1;
			int yDiscretizationCount = -1;
			try {
				xDiscretizationCount = Integer.parseInt(window.xResolutionField.getText());
				yDiscretizationCount = Integer.parseInt(window.yResolutionField.getText());
			} catch (NumberFormatException nfe) {
				String errorMessage = isJapanese ?
						"解像度の入力値を解釈できません。\n正しい数値が入力されているか、確認してください。" :
						"Can not parse the values of resolutions.\nPlease check that input value is a correct numeric value.";
				JOptionPane.showMessageDialog(window.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (xDiscretizationCount < 2 || 10000 < xDiscretizationCount
					|| yDiscretizationCount < 2 || 10000 < yDiscretizationCount) {

				String errorMessage = isJapanese ?
						"解像度の入力値が、許容範囲外です。2 から 10000 までの範囲で入力してください。" :
						"The values of resolutions are out of acceptable range.\nPlease input a number from 2 to 10000.";
				JOptionPane.showMessageDialog(window.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
				return;
			}

			switch (currentMode) {

				// Plot the math expression as a new data series.
				case PLOT: {

					// Create a new data series representing the inputted math expression.
					ZxyMathDataSeries mathDataSeries = new ZxyMathDataSeries(
							zMathExpression, xDiscretizationCount, yDiscretizationCount,
							model.scriptEngineMount, model.config
					);
					model.dataStore.addMathDataSeries(mathDataSeries);

					// Switches to UPDATE mode to update the above data series.
					setMode(Mode.UPDATE);
					updateTargetMathDataSeries = mathDataSeries;
					break;
				}

				// Update the math expression of the existing data series.
				case UPDATE: {
					updateTargetMathDataSeries.update(zMathExpression, xDiscretizationCount, yDiscretizationCount);
					break;
				}
				default: {
					throw new IllegalStateException("Unexpected mode: " + currentMode);
				}
			}

			// Replot the graph.
			presenter.plot();
		}
	}

}
