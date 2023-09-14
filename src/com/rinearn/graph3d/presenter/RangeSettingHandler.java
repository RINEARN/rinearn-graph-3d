package com.rinearn.graph3d.presenter;

import java.math.BigDecimal;

import com.rinearn.graph3d.config.RangeConfiguration;
import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.view.View;

/**
 * The class handling events and API requests for setting ranges.
 */
public class RangeSettingHandler {

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
	 * @param renderer 
	 */
	public RangeSettingHandler(Model model, View view, Presenter presenter) {
		this.model = model;
		this.view = view;
		this.presenter = presenter;
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
		this.presenter.reflectUpdatedConfiguration();
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
		this.presenter.reflectUpdatedConfiguration();
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
		this.presenter.reflectUpdatedConfiguration();
	}

}
