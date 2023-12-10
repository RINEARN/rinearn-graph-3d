package com.rinearn.graph3d.presenter.handler;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.config.ScaleConfiguration;

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
	// - API Listeners -
	//
	// ================================================================================


	/**
	 * Sets the coordinates (locations) and the labels (displayed texts) of the scale ticks on X axis.
	 * 
	 * @param tickCoordinates The coordinates of the scale ticks.
	 * @param tickLabels The labels of the scale ticks.
	 */
	public void setXTicks(BigDecimal[] tickCoordinates, String[] tickLabels) {

		// Handle the API on the event-dispatcher thread.
		SetXTicksAPIListener apiListener = new SetXTicksAPIListener(tickCoordinates, tickLabels);
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

		/** The labels of the scale ticks. */
		private final String[] tickLabels;

		/**
		 * Create an instance handling setXTicks(-) API with the specified argument.
		 * 
		 * @param tickCoordinates The coordinates of the scale ticks.
		 * @param tickLabels The labels of the scale ticks.
		 */
		public SetXTicksAPIListener(BigDecimal[] tickCoordinates, String[] tickLabels) {
			this.tickCoordinates = tickCoordinates;
			this.tickLabels = tickLabels;
		}

		@Override
		public void run() {
			ScaleConfiguration.AxisScaleConfiguration xScaleConfig
					= model.config.getScaleConfiguration().getXScaleConfiguration();

			xScaleConfig.setTickMode(ScaleConfiguration.TickMode.MANUAL);
			xScaleConfig.setTickCoordinates(tickCoordinates);
			xScaleConfig.setTickLabels(tickLabels);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}


	/**
	 * Sets the coordinates (locations) and the labels (displayed texts) of the scale ticks on Y axis.
	 * 
	 * @param tickCoordinates The coordinates of the scale ticks.
	 * @param tickLabels The labels of the scale ticks.
	 */
	public void setYTicks(BigDecimal[] tickCoordinates, String[] tickLabels) {

		// Handle the API on the event-dispatcher thread.
		SetYTicksAPIListener apiListener = new SetYTicksAPIListener(tickCoordinates, tickLabels);
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

		/** The labels of the scale ticks. */
		private final String[] tickLabels;

		/**
		 * Create an instance handling setYTicks(-) API with the specified argument.
		 * 
		 * @param tickCoordinates The coordinates of the scale ticks.
		 * @param tickLabels The labels of the scale ticks.
		 */
		public SetYTicksAPIListener(BigDecimal[] tickCoordinates, String[] tickLabels) {
			this.tickCoordinates = tickCoordinates;
			this.tickLabels = tickLabels;
		}

		@Override
		public void run() {
			ScaleConfiguration.AxisScaleConfiguration yScaleConfig
					= model.config.getScaleConfiguration().getYScaleConfiguration();

			yScaleConfig.setTickMode(ScaleConfiguration.TickMode.MANUAL);
			yScaleConfig.setTickCoordinates(tickCoordinates);
			yScaleConfig.setTickLabels(tickLabels);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}


	/**
	 * Sets the coordinates (locations) and the labels (displayed texts) of the scale ticks on Y axis.
	 * 
	 * @param tickCoordinates The coordinates of the scale ticks.
	 * @param tickLabels The labels of the scale ticks.
	 */
	public void setZTicks(BigDecimal[] tickCoordinates, String[] tickLabels) {

		// Handle the API on the event-dispatcher thread.
		SetZTicksAPIListener apiListener = new SetZTicksAPIListener(tickCoordinates, tickLabels);
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

		/** The labels of the scale ticks. */
		private final String[] tickLabels;

		/**
		 * Create an instance handling setZTicks(-) API with the specified argument.
		 * 
		 * @param tickCoordinates The coordinates of the scale ticks.
		 * @param tickLabels The labels of the scale ticks.
		 */
		public SetZTicksAPIListener(BigDecimal[] tickCoordinates, String[] tickLabels) {
			this.tickCoordinates = tickCoordinates;
			this.tickLabels = tickLabels;
		}

		@Override
		public void run() {
			ScaleConfiguration.AxisScaleConfiguration zScaleConfig
					= model.config.getScaleConfiguration().getZScaleConfiguration();

			zScaleConfig.setTickMode(ScaleConfiguration.TickMode.MANUAL);
			zScaleConfig.setTickCoordinates(tickCoordinates);
			zScaleConfig.setTickLabels(tickLabels);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}

}
