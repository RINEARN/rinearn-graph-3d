package com.rinearn.graph3d.presenter.handler;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.view.View;

import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;

/**
 * The class handling events and API requests related to UI on the screen side panel.
 */
public final class ScreenSideUIHandler {

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
	public ScreenSideUIHandler(Model model, View view, Presenter presenter) {
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
	 * Sets the visibility of the UI panel at the screen side.
	 *
	 * @param visible Specify true for showing the UI panel at the screen side.
	 */
	public void setScreenSideUIVisible(boolean visible) {

		// Handle the API on the event-dispatcher thread.
		SetScreenSideUIVisibleAPIListener apiListener = new SetScreenSideUIVisibleAPIListener(visible);
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
	 * The class handling API requests from setScreenSideUIVisible(-) method,
	 * on event-dispatcher thread.
	 */
	private final class SetScreenSideUIVisibleAPIListener implements Runnable {

		/** The flag for switching the visibility of the screen-side UI panel. */
		private final boolean visible;

		/**
		 * Create an instance handling setScreenSideUIVisible(-) API with the specified argument.
		 *
		 * @param visible Specify true for showing the UI panel at the screen side.
		 */
		public SetScreenSideUIVisibleAPIListener(boolean visible) {
			this.visible = visible;
		}

		@Override
		public void run() {
			view.mainWindow.setScreenSideUIVisible(visible);
			view.mainWindow.forceUpdateWindowLayout();
			presenter.renderingLoop.requestRendering();
		}
	}
}
