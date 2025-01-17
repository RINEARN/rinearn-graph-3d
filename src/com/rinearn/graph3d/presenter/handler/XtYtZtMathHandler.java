package com.rinearn.graph3d.presenter.handler;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.model.data.series.XtYtZtMathDataSeries;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.view.XtYtZtMathWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

import java.math.BigDecimal;


/**
 * The class handling events and API requests for plotting math expressions of "x(t),y(t),z(t)" form.
 */
public class XtYtZtMathHandler {

	/** The front-end class of "Model" layer, which provides internal logic procedures and so on. */
	private final Model model;

	/** The front-end class of "View" layer, which provides visible part of GUI without event handling. */
	private final View view;

	/** The front-end class of "Presenter" layer, which invokes Model's procedures triggered by user's action on GUI. */
	private final Presenter presenter;

	/** The flag for turning on/off the event handling feature of this instance. */
	private volatile boolean eventHandlingEnabled = true;

	/** The event handler of the right-click menu of the text field to input x(t) math expression. */
	private volatile TextRightClickMenuHandler xtMathExpressionFieldMenuHandler = null;

	/** The event handler of the right-click menu of the text field to input y(t) math expression. */
	private volatile TextRightClickMenuHandler ytMathExpressionFieldMenuHandler = null;

	/** The event handler of the right-click menu of the text field to input z(t) math expression. */
	private volatile TextRightClickMenuHandler ztMathExpressionFieldMenuHandler = null;

	/** The event handler of the right-click menu of the text field to input the starting time. */
	private volatile TextRightClickMenuHandler timeMinFieldMenuHandler = null;

	/** The event handler of the right-click menu of the text field to input the ending time. */
	private volatile TextRightClickMenuHandler timeMaxFieldMenuHandler = null;

	/** The event handler of the right-click menu of the text field to input the time resolution. */
	private volatile TextRightClickMenuHandler timeResolutionFieldMenuHandler = null;

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
	private volatile XtYtZtMathDataSeries updateTargetMathDataSeries = null;


	/**
	 * Create a new instance handling events and API requests using the specified resources.
	 *
	 * @param model The front-end class of "Model" layer, which provides internal logic procedures and so on.
	 * @param view The front-end class of "View" layer, which provides visible part of GUI without event handling.
	 * @param presenter The front-end class of "Presenter" layer, which handles events occurred on GUI, and API requests.
	 */
	public XtYtZtMathHandler(Model model, View view, Presenter presenter) {
		this.model = model;
		this.view = view;
		this.presenter = presenter;
		XtYtZtMathWindow window = this.view.xtYtZtMathWindow;

		// Add the action listener defined in this class, to the UI components in the window of "z(x,y)" plot.
		window.plotButton.addActionListener(new PlotButtonPressedEventListener());

		// Add the event listeners of the right click menus of the text fields.
		this.xtMathExpressionFieldMenuHandler = new TextRightClickMenuHandler(
				window.xtMathExpressionFieldRightClickMenu, window.ztMathExpressionField
		);
		this.ytMathExpressionFieldMenuHandler = new TextRightClickMenuHandler(
				window.ytMathExpressionFieldRightClickMenu, window.ytMathExpressionField
		);
		this.ztMathExpressionFieldMenuHandler = new TextRightClickMenuHandler(
				window.ztMathExpressionFieldRightClickMenu, window.ztMathExpressionField
		);
		this.timeMinFieldMenuHandler = new TextRightClickMenuHandler(
				window.timeMinFieldRightClickMenu, window.timeMinField
		);
		this.timeMaxFieldMenuHandler = new TextRightClickMenuHandler(
				window.timeMaxFieldRightClickMenu, window.timeMaxField
		);
		this.timeResolutionFieldMenuHandler = new TextRightClickMenuHandler(
				window.timeResolutionFieldRightClickMenu, window.timeResolutionField
		);
	}


	/**
	 * Turns on/off the event handling feature of this instance.
	 *
	 * @param enabled Specify false for turning off the event handling feature (enabled by default).
	 */
	public synchronized void setEventHandlingEnabled(boolean enabled) {
		this.eventHandlingEnabled = enabled;
		this.xtMathExpressionFieldMenuHandler.setEventHandlingEnabled(enabled);
		this.ytMathExpressionFieldMenuHandler.setEventHandlingEnabled(enabled);
		this.ztMathExpressionFieldMenuHandler.setEventHandlingEnabled(enabled);
		this.timeMinFieldMenuHandler.setEventHandlingEnabled(enabled);
		this.timeMaxFieldMenuHandler.setEventHandlingEnabled(enabled);
		this.timeResolutionFieldMenuHandler.setEventHandlingEnabled(enabled);
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
		XtYtZtMathWindow.Mode uiMode = null;
		switch (mode) {
			case PLOT:   uiMode = XtYtZtMathWindow.Mode.PLOT; break;
			case UPDATE: uiMode = XtYtZtMathWindow.Mode.UPDATE; break;
			case UNSET:  uiMode = XtYtZtMathWindow.Mode.UNSET; break;
			default: throw new IllegalStateException("Unexpected mode: " + mode);
		}
		this.view.xtYtZtMathWindow.updateUIForMode(uiMode, this.model.config.getEnvironmentConfiguration());
	}


	/**
	 * Sets the math data series to be updated in UPDATE currentMode.
	 *
	 * @param updateTargetMathDataSeries The math data series to be updated.
	 */
	public synchronized void setUpdateTargetMathDataSeries(XtYtZtMathDataSeries updateTargetMathDataSeries) {
		this.updateTargetMathDataSeries = updateTargetMathDataSeries;

		// Update the input values in the text fields on the window.
		XtYtZtMathWindow window = this.view.xtYtZtMathWindow;
		window.xtMathExpressionField.setText(this.updateTargetMathDataSeries.getXtMathExpression());
		window.ytMathExpressionField.setText(this.updateTargetMathDataSeries.getYtMathExpression());
		window.ztMathExpressionField.setText(this.updateTargetMathDataSeries.getZtMathExpression());
		window.timeMinField.setText(this.updateTargetMathDataSeries.getTimeMin().toString());
		window.timeMaxField.setText(this.updateTargetMathDataSeries.getTimeMax().toString());
		window.timeResolutionField.setText(Integer.toString(this.updateTargetMathDataSeries.getTimeDiscretizationCount()));
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
			XtYtZtMathWindow window = view.xtYtZtMathWindow;

			// Detect whether the UI language is set to Japanese. (Necessary for generating error messages.)
			boolean isJapanese = model.config.getEnvironmentConfiguration().isLocaleJapanese();

			// Get the inputted math expressions.
			String xtMathExpression = window.xtMathExpressionField.getText();
			String ytMathExpression = window.ytMathExpressionField.getText();
			String ztMathExpression = window.ztMathExpressionField.getText();

			// Parse and check the values of resolutions (= numbers of discretization points of time).
			int timeDiscretizationCount = -1;
			try {
				timeDiscretizationCount = Integer.parseInt(window.timeResolutionField.getText());
			} catch (NumberFormatException nfe) {
				String errorMessage = isJapanese ?
						"「時刻点数」の入力値を解釈できません。\n正しい数値が入力されているか、確認してください。" :
						"Can not parse the values of \"Time Points\".\nPlease check that input value is a correct numeric value.";
				JOptionPane.showMessageDialog(window.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (timeDiscretizationCount < 2 || 10000*10000 < timeDiscretizationCount) {

				String errorMessage = isJapanese ?
						"解像度の入力値が、許容範囲外です。2 から 10000 0000 までの範囲で入力してください。" :
						"The values of resolutions are out of acceptable range.\nPlease input a number from 2 to 100 000 000.";
				JOptionPane.showMessageDialog(window.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Parse and check the values of the range of time.
			BigDecimal timeMin = null;
			BigDecimal timeMax = null;
			try {
				timeMin = new BigDecimal(window.timeMinField.getText());
				timeMax = new BigDecimal(window.timeMaxField.getText());
			} catch (NumberFormatException nfe) {
				String errorMessage = isJapanese ?
						"始点/終点時刻の入力値を解釈できません。\n正しい数値が入力されているか、確認してください。" :
						"Can not parse the values of \"Starting/Ending Time\".\nPlease check that input value is a correct numeric value.";
				JOptionPane.showMessageDialog(window.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
				return;
			}

			switch (currentMode) {

				// Plot the math expression as a new data series.
				case PLOT: {

					// Create a new data series representing the inputted math expression.
					XtYtZtMathDataSeries mathDataSeries = new XtYtZtMathDataSeries(
							xtMathExpression, ytMathExpression, ztMathExpression, timeMin, timeMax, timeDiscretizationCount,
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
					updateTargetMathDataSeries.update(xtMathExpression, ytMathExpression, ztMathExpression, timeMin, timeMax, timeDiscretizationCount);
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
