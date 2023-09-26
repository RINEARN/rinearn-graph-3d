package com.rinearn.graph3d.presenter;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.view.View;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;


/**
 * The class handling events of the frame of the main window.
 */
public final class FrameHandler {

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
	public FrameHandler(Model model, View view, Presenter presenter) {
		this.model = model;
		this.view = view;
		this.presenter = presenter;
		JFrame frame = this.view.mainWindow.frame;

		// Add the event listener handling resizing events of the frame.
		ResizeEventListener resizeListener = new ResizeEventListener();
		frame.addComponentListener(resizeListener);

		// Add the event listener handling resizing events of the frame.
		CloseEventListener closeListener = new CloseEventListener();
		frame.addWindowListener(closeListener);
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
	 * The event listener handling resizing events of the frame.
	 */
	private class ResizeEventListener extends ComponentAdapter {
		@Override
		public void componentResized(ComponentEvent ce) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			view.mainWindow.resize();
		}
	}


	/**
	 * The event listener handling resizing events of the frame.
	 */
	private class CloseEventListener extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent we) {
			if (!isEventHandlingEnabled()) {
				return;
			}

			// Temporary
			System.exit(0);
		}
	}
}
