package com.rinearn.graph3d.presenter.handler;

import com.rinearn.graph3d.config.renderer.RendererConfiguration;
import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.view.RendererSettingWindow;
import com.rinearn.graph3d.view.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * The class handling events and API requests for renderer settings.
 */
public class RendererSettingHandler {

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
	public RendererSettingHandler(Model model, View view, Presenter presenter) {
		this.model = model;
		this.view = view;
		this.presenter = presenter;

		// Add the action listener to the UI components.
		RendererSettingWindow window = view.rendererSettingWindow;
		window.okButton.addActionListener(new OkButtonPressedEventListener());
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
	 * The event listener handling the event that the OK button is pressed.
	 */
	private final class OkButtonPressedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			RendererSettingWindow window = view.rendererSettingWindow;
			RendererConfiguration rendererConfig = model.config.getRendererConfiguration();

			// Enable/disable the anti-aliasing feature.
			rendererConfig.setAntialiasingEnabled(window.antialiasingBox.isSelected());

			// Propagate the above update of the configuration to the entire application.
			// In addition, request the MainWindowFrameHandler to resize the main window,
			// into the size corresponding the screen size.
			setEventHandlingEnabled(false);
			presenter.propagateConfiguration();
			setEventHandlingEnabled(true);

			// Perform rendering on the rendering loop's thread asynchronously.
			presenter.renderingLoop.requestRendering();
		}
	}
}
