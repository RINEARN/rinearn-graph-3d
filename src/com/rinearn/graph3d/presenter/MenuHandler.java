package com.rinearn.graph3d.presenter;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.model.dataseries.ZxyMathDataSeries;
import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.view.MainWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * The class handling events and API requests related to the menu bar and right-click menus.
 */
public final class MenuHandler {

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
	public MenuHandler(Model model, View view, Presenter presenter) {
		this.model = model;
		this.view = view;
		this.presenter = presenter;

		MainWindow window = this.view.mainWindow;

		// Add the action listener to sub menu items in "Math" menu.
		window.zxyMathMenuItem.addActionListener(new ZxyMathItemClickedEventListener());
		window.removeLastMathMenuItem.addActionListener(new RemoveLastMathItemClickedEventListener());
		window.clearMathMenuItem.addActionListener(new ClearMathItemClickedEventListener());

		// Add the action listener to sub menu items in "Settings" menu.
		window.rangeSettingMenuItem.addActionListener(new RangeSettingItemClickedEventListener());
		window.labelSettingMenuItem.addActionListener(new LabelSettingItemClickedEventListener());
		window.fontSettingMenuItem.addActionListener(new FontSettingItemClickedEventListener());
		window.cameraSettingMenuItem.addActionListener(new CameraSettingItemClickedEventListener());
		window.lightSettingMenuItem.addActionListener(new LightSettingItemClickedEventListener());
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
	 * Sets the visibility of the menu bar and the right click menus.
	 * 
	 * @param visible Specify true for showing the menu bar and the right click menus.
	 */
	public synchronized void setMenuVisible(boolean visible) {
		view.mainWindow.setMenuVisible(visible);
		view.mainWindow.forceUpdateWindowLayout();
		presenter.renderingLoop.requestRendering();
	}


	/**
	 * The listener handling the event that "Math" > "z(x,y)" menu item is clicked.
	 */
	private final class ZxyMathItemClickedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}

			// !!!!!
			// TEMPORARY IMPLEMENTATION
			// !!!!!

			// Create temporary math data series, and register it.
			String mathExpression = "x*x - y*y";
			int xDiscretizationCount = 100;
			int yDiscretizationCount = 100;
			ZxyMathDataSeries mathDataSeries = new ZxyMathDataSeries(
					mathExpression, xDiscretizationCount, yDiscretizationCount,
					model.scriptEngineMount, model.config
			);
			model.addMathDataSeries(mathDataSeries);

			// !!!!!
			// TEMPORARY IMPLEMENTATION
			// !!!!!

			// Replot the graph.
			presenter.plot();
		}
	}


	/**
	 * The listener handling the event that "Math" > "Remove Last" menu item is clicked.
	 */
	private final class RemoveLastMathItemClickedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}

			// Cleare the lastly-registered math data series.
			model.removeLastMathDataSeries();

			// Replot the graph.
			presenter.plot();
		}
	}


	/**
	 * The listener handling the event that "Math" > "Clear" menu item is clicked.
	 */
	private final class ClearMathItemClickedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}

			// Cleare all currently-registered math data series.
			model.clearMathDataSeries();

			// Replot the graph.
			presenter.plot();
		}
	}


	/**
	 * The listener handling the event that "Settings" > "Set Ranges" menu item is clicked.
	 */
	private final class RangeSettingItemClickedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			view.rangeSettingWindow.setWindowVisible(true);
		}
	}


	/**
	 * The listener handling the event that "Settings" > "Set Labels" menu item is clicked.
	 */
	private final class LabelSettingItemClickedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			view.labelSettingWindow.setWindowVisible(true);
		}
	}


	/**
	 * The listener handling the event that "Settings" > "Set Fonts" menu item is clicked.
	 */
	private final class FontSettingItemClickedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			view.fontSettingWindow.setWindowVisible(true);
		}
	}


	/**
	 * The listener handling the event that "Settings" > "Set Camera" menu item is clicked.
	 */
	private final class CameraSettingItemClickedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			view.cameraSettingWindow.setWindowVisible(true);
		}
	}


	/**
	 * The listener handling the event that "Settings" > "Set Camera" menu item is clicked.
	 */
	private final class LightSettingItemClickedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			view.lightSettingWindow.setWindowVisible(true);
		}
	}
}
