package com.rinearn.graph3d.presenter.handler;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.view.ScaleSettingWindow;
import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.config.ScaleConfiguration;
import com.rinearn.graph3d.config.FrameConfiguration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import javax.swing.SwingUtilities;


/**
 * The class handling events and API requests for setting scale-related parameters.
 */
public class ScaleSettingHandler {

	/** The front-end class of "Model" layer, which provides internal logic procedures and so on. */
	private final Model model;

	/** The front-end class of "View" layer, which provides visible part of GUI without event handling. */
	@SuppressWarnings("unused")
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
	public ScaleSettingHandler(Model model, View view, Presenter presenter) {
		this.model = model;
		this.view = view;
		this.presenter = presenter;

		// Add the action listener defined in this class, to the OK button of label setting window.
		ScaleSettingWindow window = this.view.scaleSettingWindow;
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

			// Get the configuration containers.
			ScaleConfiguration scaleConfig = model.config.getScaleConfiguration();
			FrameConfiguration frameConfig = model.config.getFrameConfiguration();

			// Get the inputteds, and store them into the configuration containers.
			ScaleSettingWindow window = view.scaleSettingWindow;

			// "Frame" tab:
			{
				// Frame shape mode
				FrameConfiguration.ShapeMode frameShapeMode = window.designTabItems.getSelectedFrameShapeMode();
				frameConfig.setShapeMode(frameShapeMode);

				// Frame line length
				String frameLineWidthText = window.designTabItems.frameLineWidthField.getText();
				try {
					double frameLineWidth = Double.parseDouble(frameLineWidthText);
					frameConfig.setLineWidth(frameLineWidth);
				} catch (NumberFormatException nfe) {
				}

				// Tick label margin
				String tickLabelMarginText = window.designTabItems.tickLabelMarginField.getText();
				try {
					double tickLabelMargin = Double.parseDouble(tickLabelMarginText);
					scaleConfig.getXScaleConfiguration().setTickLabelMargin(tickLabelMargin);
					scaleConfig.getYScaleConfiguration().setTickLabelMargin(tickLabelMargin);
					scaleConfig.getZScaleConfiguration().setTickLabelMargin(tickLabelMargin);
				} catch (NumberFormatException nfe) {
				}

				// Tick line length
				String tickLineLengthText = window.designTabItems.tickLineLengthField.getText();
				try {
					double tickLineLength = Double.parseDouble(tickLineLengthText);
					scaleConfig.getXScaleConfiguration().setTickLineLength(tickLineLength);
					scaleConfig.getYScaleConfiguration().setTickLineLength(tickLineLength);
					scaleConfig.getZScaleConfiguration().setTickLineLength(tickLineLength);
				} catch (NumberFormatException nfe) {
				}

				// "Inward" box
				boolean isTickInward = window.designTabItems.tickInwardBox.isSelected();
				scaleConfig.setTickInward(isTickInward);
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
	 * Sets the coordinates (locations) and the labels (displayed texts) of the scale ticks on X axis.
	 *
	 * @param tickCoordinates The coordinates of the scale ticks.
	 * @param tickLabelTexts The label texts of the scale ticks.
	 */
	public void setXTicks(BigDecimal[] tickCoordinates, String[] tickLabelTexts) {

		// Handle the API on the event-dispatcher thread.
		SetXTicksAPIListener apiListener = new SetXTicksAPIListener(tickCoordinates, tickLabelTexts);
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
	 * The class handling API requests from setXTicks(-) method,
	 * on event-dispatcher thread.
	 */
	private final class SetXTicksAPIListener implements Runnable {

		/** The coordinates of the scale ticks. */
		private final BigDecimal[] tickCoordinates;

		/** The label texts of the scale ticks. */
		private final String[] tickLabelTexts;

		/**
		 * Create an instance handling setXTicks(-) API with the specified argument.
		 *
		 * @param tickCoordinates The coordinates of the scale ticks.
		 * @param tickLabelTexts The label texts of the scale ticks.
		 */
		public SetXTicksAPIListener(BigDecimal[] tickCoordinates, String[] tickLabelTexts) {
			this.tickCoordinates = tickCoordinates;
			this.tickLabelTexts = tickLabelTexts;
		}

		@Override
		public void run() {
			ScaleConfiguration.AxisScaleConfiguration xScaleConfig
					= model.config.getScaleConfiguration().getXScaleConfiguration();

			xScaleConfig.setTickerMode(ScaleConfiguration.TickerMode.MANUAL);
			xScaleConfig.getManualTicker().setTickCoordinates(tickCoordinates);
			xScaleConfig.getManualTicker().setTickLabelTexts(tickLabelTexts);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}


	/**
	 * Sets the coordinates (locations) and the labels (displayed texts) of the scale ticks on Y axis.
	 *
	 * @param tickCoordinates The coordinates of the scale ticks.
	 * @param tickLabelTexts The label texts of the scale ticks.
	 */
	public void setYTicks(BigDecimal[] tickCoordinates, String[] tickLabelTexts) {

		// Handle the API on the event-dispatcher thread.
		SetYTicksAPIListener apiListener = new SetYTicksAPIListener(tickCoordinates, tickLabelTexts);
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
	 * The class handling API requests from setYTicks(-) method,
	 * on event-dispatcher thread.
	 */
	private final class SetYTicksAPIListener implements Runnable {

		/** The coordinates of the scale ticks. */
		private final BigDecimal[] tickCoordinates;

		/** The label texts of the scale ticks. */
		private final String[] tickLabelTexts;

		/**
		 * Create an instance handling setYTicks(-) API with the specified argument.
		 *
		 * @param tickCoordinates The coordinates of the scale ticks.
		 * @param tickLabelTexts The labels of the scale ticks.
		 */
		public SetYTicksAPIListener(BigDecimal[] tickCoordinates, String[] tickLabelTexts) {
			this.tickCoordinates = tickCoordinates;
			this.tickLabelTexts = tickLabelTexts;
		}

		@Override
		public void run() {
			ScaleConfiguration.AxisScaleConfiguration yScaleConfig
					= model.config.getScaleConfiguration().getYScaleConfiguration();

			yScaleConfig.setTickerMode(ScaleConfiguration.TickerMode.MANUAL);
			yScaleConfig.getManualTicker().setTickCoordinates(tickCoordinates);
			yScaleConfig.getManualTicker().setTickLabelTexts(tickLabelTexts);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}


	/**
	 * Sets the coordinates (locations) and the labels (displayed texts) of the scale ticks on Y axis.
	 *
	 * @param tickCoordinates The coordinates of the scale ticks.
	 * @param tickLabelTexts The label texts of the scale ticks.
	 */
	public void setZTicks(BigDecimal[] tickCoordinates, String[] tickLabelTexts) {

		// Handle the API on the event-dispatcher thread.
		SetZTicksAPIListener apiListener = new SetZTicksAPIListener(tickCoordinates, tickLabelTexts);
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
	 * The class handling API requests from setZTicks(-) method,
	 * on event-dispatcher thread.
	 */
	private final class SetZTicksAPIListener implements Runnable {

		/** The coordinates of the scale ticks. */
		private final BigDecimal[] tickCoordinates;

		/** The label texts of the scale ticks. */
		private final String[] tickLabelTexts;

		/**
		 * Create an instance handling setZTicks(-) API with the specified argument.
		 *
		 * @param tickCoordinates The coordinates of the scale ticks.
		 * @param tickLabelTexts The label texts of the scale ticks.
		 */
		public SetZTicksAPIListener(BigDecimal[] tickCoordinates, String[] tickLabelTexts) {
			this.tickCoordinates = tickCoordinates;
			this.tickLabelTexts = tickLabelTexts;
		}

		@Override
		public void run() {
			ScaleConfiguration.AxisScaleConfiguration zScaleConfig
					= model.config.getScaleConfiguration().getZScaleConfiguration();

			zScaleConfig.setTickerMode(ScaleConfiguration.TickerMode.MANUAL);
			zScaleConfig.getManualTicker().setTickCoordinates(tickCoordinates);
			zScaleConfig.getManualTicker().setTickLabelTexts(tickLabelTexts);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}

}
