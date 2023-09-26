package com.rinearn.graph3d.presenter;

import com.rinearn.graph3d.model.Model;
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

		// Add the action listener to sub menu items in "Settings" menu.
		window.rangeSettingMenuItem.addActionListener(new RangeSettingItemClickedEventListener());
		window.labelSettingMenuItem.addActionListener(new LabelSettingItemClickedEventListener());
		window.lightSettingMenuItem.addActionListener(new LightSettingParametersItemClickedEventListener());
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
	 * The listener handling the event that "Settings" > "Set Lighting Parameters" menu item is clicked.
	 */
	private final class LightSettingParametersItemClickedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			view.lightSettingWindow.setWindowVisible(true);
		}
	}
}
