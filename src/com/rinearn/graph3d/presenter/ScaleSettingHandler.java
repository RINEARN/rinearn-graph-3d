package com.rinearn.graph3d.presenter;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.config.ScaleConfiguration;

import java.math.BigDecimal;


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
	 * Sets the coordinates (locations) and the labels (displayed texts) of the scale ticks on X axis.
	 * 
	 * @param tickCoordinates The coordinates of the scale ticks.
	 * @param tickLabels The labels of the scale ticks.
	 */
	public synchronized void setXTicks(BigDecimal[] tickCoordinates, String[] tickLabels) {
		ScaleConfiguration.AxisScaleConfiguration xScaleConfig
			= this.model.getConfiguration().getScaleConfiguration().getXScaleConfiguration();

		xScaleConfig.setTickMode(ScaleConfiguration.TickMode.MANUAL);
		xScaleConfig.setTickCoordinates(tickCoordinates);
		xScaleConfig.setTickLabels(tickLabels);
		this.presenter.reflectUpdatedConfiguration(true);
	}


	/**
	 * Sets the coordinates (locations) and the labels (displayed texts) of the scale ticks on Y axis.
	 * 
	 * @param tickCoordinates The coordinates of the scale ticks.
	 * @param tickLabels The labels of the scale ticks.
	 */
	public synchronized void setYTicks(BigDecimal[] tickCoordinates, String[] tickLabels) {
		ScaleConfiguration.AxisScaleConfiguration yScaleConfig
			= this.model.getConfiguration().getScaleConfiguration().getXScaleConfiguration();

		yScaleConfig.setTickMode(ScaleConfiguration.TickMode.MANUAL);
		yScaleConfig.setTickCoordinates(tickCoordinates);
		yScaleConfig.setTickLabels(tickLabels);
		this.presenter.reflectUpdatedConfiguration(true);
	}


	/**
	 * Sets the coordinates (locations) and the labels (displayed texts) of the scale ticks on Y axis.
	 * 
	 * @param tickCoordinates The coordinates of the scale ticks.
	 * @param tickLabels The labels of the scale ticks.
	 */
	public synchronized void setZTicks(BigDecimal[] tickCoordinates, String[] tickLabels) {
		ScaleConfiguration.AxisScaleConfiguration zScaleConfig
			= this.model.getConfiguration().getScaleConfiguration().getXScaleConfiguration();

		zScaleConfig.setTickMode(ScaleConfiguration.TickMode.MANUAL);
		zScaleConfig.setTickCoordinates(tickCoordinates);
		zScaleConfig.setTickLabels(tickLabels);
		this.presenter.reflectUpdatedConfiguration(true);
	}
}
