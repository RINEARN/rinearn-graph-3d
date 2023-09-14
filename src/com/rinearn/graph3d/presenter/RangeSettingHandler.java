package com.rinearn.graph3d.presenter;

import java.math.BigDecimal;

import com.rinearn.graph3d.config.RangeConfiguration;
import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.renderer.RinearnGraph3DRenderer;
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
}
