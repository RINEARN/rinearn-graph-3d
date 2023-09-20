package com.rinearn.graph3d.presenter;

import com.rinearn.graph3d.config.LabelConfiguration;
import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.view.View;

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
	}


	/**
	 * Set the displayed text of X axis label.
	 * 
	 * @param xLabel The text of X axis label.
	 */
	public synchronized void setXLabel(String xLabel) {
		LabelConfiguration.AxisLabelConfiguration xLabelConfig
			= this.model.getConfiguration().getLabelConfiguration().getXLabelConfiguration();
		xLabelConfig.setText(xLabel);
		this.presenter.reflectUpdatedConfiguration(true);
	}


	/**
	 * Set the displayed text of Y axis label.
	 * 
	 * @param yLabel The text of Y axis label.
	 */
	public synchronized void setYLabel(String yLabel) {
		LabelConfiguration.AxisLabelConfiguration yLabelConfig
			= this.model.getConfiguration().getLabelConfiguration().getYLabelConfiguration();
		yLabelConfig.setText(yLabel);
		this.presenter.reflectUpdatedConfiguration(true);
	}


	/**
	 * Set the displayed text of Z axis label.
	 * 
	 * @param zLabel The text of Z axis label.
	 */
	public synchronized void setZLabel(String zLabel) {
		LabelConfiguration.AxisLabelConfiguration zLabelConfig
			= this.model.getConfiguration().getLabelConfiguration().getZLabelConfiguration();
		zLabelConfig.setText(zLabel);
		this.presenter.reflectUpdatedConfiguration(true);
	}

}