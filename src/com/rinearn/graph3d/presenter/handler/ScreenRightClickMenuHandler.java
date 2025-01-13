package com.rinearn.graph3d.presenter.handler;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.view.MainWindow;
import com.rinearn.graph3d.view.View;


/**
 * The class handling events of the right click menu of the graph screen (Copy Image, Paste Data, and so on).
 */
public final class ScreenRightClickMenuHandler {

	/** The front-end class of "Model" layer, which provides internal logic procedures and so on. */
	@SuppressWarnings("unused")
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
	public ScreenRightClickMenuHandler(Model model, View view, Presenter presenter) {
		this.model = model;
		this.view = view;
		this.presenter = presenter;

		MainWindow window = this.view.mainWindow;
		MainMenuHandler mainMenuHandler = this.presenter.mainMenuHandler;
		if (mainMenuHandler == null) {
			throw new IllegalStateException(
					"ScreenRightClickMenuHandler depends on MainMenuHandler, so the former must be instantiated AFTER the latter."
			);
		}

		// Add the action listeners to the right-click menu.
		window.screenRightClickMenu.copyImageRightClickMenuItem.addActionListener(mainMenuHandler.new CopyImageItemClickedEventListener());
		window.screenRightClickMenu.pasteDataTextRightClickMenuItem.addActionListener(mainMenuHandler.new PasteDataTextItemClickedEventListener());
	}


	/**
	 * Turns on/off the event handling feature of this instance.
	 *
	 * @param enabled Specify false for turning off the event handling feature (enabled by default).
	 */
	public synchronized void setEventHandlingEnabled(boolean enabled) {
		this.eventHandlingEnabled = enabled;

		// For the current implementation,
		// the event handlers of the right-click menu items "Copy Image" and "Paste Data"
		// are diverted from them of the corresponding main menu items as they are.
		// Please not that the above field "this.eventHandlingEnabled" doesn't affect their behavior.
	}


	/**
	 * Gets whether the event handling feature of this instance is enabled.
	 *
	 * @return Returns true if the event handling feature is enabled.
	 */
	public synchronized boolean isEventHandlingEnabled() {
		return this.eventHandlingEnabled;
	}

}
