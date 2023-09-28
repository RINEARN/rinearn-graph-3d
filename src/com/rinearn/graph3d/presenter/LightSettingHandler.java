package com.rinearn.graph3d.presenter;

import com.rinearn.graph3d.config.LightConfiguration;
import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.view.LightSettingWindow;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;


/**
 * The class handling events for setting light-related parameters.
 */
public class LightSettingHandler {

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
	public LightSettingHandler(Model model, View view, Presenter presenter) {
		this.model = model;
		this.view = view;
		this.presenter = presenter;

		// Add the action listener defined in this class, to the scroll bars in this setting window.
		LightSettingWindow window = this.view.lightSettingWindow;
		window.ambientBar.addAdjustmentListener(new AmbientScrolledEventListener());
		window.diffuseBar.addAdjustmentListener(new DiffuseScrolledEventListener());
		window.diffractiveBar.addAdjustmentListener(new DiffractiveScrolledEventListener());
		window.specularStrengthBar.addAdjustmentListener(new SpecularStrengthScrolledEventListener());
		window.specularAngleBar.addAdjustmentListener(new SpecularAngleScrolledEventListener());
		window.horizontalAngleBar.addAdjustmentListener(new LightAngleScrolledEventListener());
		window.verticalAngleBar.addAdjustmentListener(new LightAngleScrolledEventListener());
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
	 * The event listener handling the event that the scroll bar of "Ambient" parameter is moved.
	 */
	private final class AmbientScrolledEventListener implements AdjustmentListener {
		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			LightSettingWindow window = view.lightSettingWindow;
			LightConfiguration lightConfig = model.getConfiguration().getLightConfiguration();

			// Get the modified value from the slider, and store it into the configuration container.
			double strength = (double)window.ambientBar.getValue() / (double)LightSettingWindow.SCROLL_BAR_MAX_COUNT;
			lightConfig.setAmbientReflectionStrength(strength);

			// Propagate the above update of the configuration to the entire application.
			setEventHandlingEnabled(false);
			presenter.propagateConfiguration();
			setEventHandlingEnabled(true);

			// Perform rendering on the rendering loop's thread asynchronously.
			presenter.renderingLoop.requestRendering();
		}
	}


	/**
	 * The event listener handling the event that the scroll bar of "Diffuse" parameter is moved.
	 */
	private final class DiffuseScrolledEventListener implements AdjustmentListener {
		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			LightSettingWindow window = view.lightSettingWindow;
			LightConfiguration lightConfig = model.getConfiguration().getLightConfiguration();

			// Get the modified value from the slider, and store it into the configuration container.
			double strength = (double)window.diffuseBar.getValue() / (double)LightSettingWindow.SCROLL_BAR_MAX_COUNT;
			lightConfig.setDiffuseReflectionStrength(strength);

			// Propagate the above update of the configuration to the entire application.
			setEventHandlingEnabled(false);
			presenter.propagateConfiguration();
			setEventHandlingEnabled(true);

			// Perform rendering on the rendering loop's thread asynchronously.
			presenter.renderingLoop.requestRendering();
		}
	}


	/**
	 * The event listener handling the event that the scroll bar of "Diffractive" parameter is moved.
	 */
	private final class DiffractiveScrolledEventListener implements AdjustmentListener {
		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			LightSettingWindow window = view.lightSettingWindow;
			LightConfiguration lightConfig = model.getConfiguration().getLightConfiguration();

			// Get the modified value from the slider, and store it into the configuration container.
			double strength = (double)window.diffractiveBar.getValue() / (double)LightSettingWindow.SCROLL_BAR_MAX_COUNT;
			lightConfig.setDiffractiveReflectionStrength(strength);

			// Propagate the above update of the configuration to the entire application.
			setEventHandlingEnabled(false);
			presenter.propagateConfiguration();
			setEventHandlingEnabled(true);

			// Perform rendering on the rendering loop's thread asynchronously.
			presenter.renderingLoop.requestRendering();
		}
	}


	/**
	 * The event listener handling the event that the scroll bar of "Specular Strength" parameter is moved.
	 */
	private final class SpecularStrengthScrolledEventListener implements AdjustmentListener {
		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			LightSettingWindow window = view.lightSettingWindow;
			LightConfiguration lightConfig = model.getConfiguration().getLightConfiguration();

			// Get the modified value from the slider, and store it into the configuration container.
			double strength = (double)window.specularStrengthBar.getValue() / (double)LightSettingWindow.SCROLL_BAR_MAX_COUNT;
			lightConfig.setSpecularReflectionStrength(strength);

			// Propagate the above update of the configuration to the entire application.
			setEventHandlingEnabled(false);
			presenter.propagateConfiguration();
			setEventHandlingEnabled(true);

			// Perform rendering on the rendering loop's thread asynchronously.
			presenter.renderingLoop.requestRendering();
		}
	}


	/**
	 * The event listener handling the event that the scroll bar of "Specular Angle" parameter is moved.
	 */
	private final class SpecularAngleScrolledEventListener implements AdjustmentListener {
		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			LightSettingWindow window = view.lightSettingWindow;
			LightConfiguration lightConfig = model.getConfiguration().getLightConfiguration();

			// Get the modified value from the slider, and store it into the configuration container.
			double angle = (double)window.specularAngleBar.getValue() / (double)LightSettingWindow.SCROLL_BAR_MAX_COUNT;
			angle *= 0.5 * Math.PI; // Map the maximum spread angle to pi/2.
			lightConfig.setSpecularReflectionAngle(angle);

			// Propagate the above update of the configuration to the entire application.
			setEventHandlingEnabled(false);
			presenter.propagateConfiguration();
			setEventHandlingEnabled(true);

			// Perform rendering on the rendering loop's thread asynchronously.
			presenter.renderingLoop.requestRendering();
		}
	}


	/**
	 * The event listener handling the event that the scroll bar
	 * of the light souce's "Horizontal/Horizontal Angle" parameters is moved.
	 */
	private final class LightAngleScrolledEventListener implements AdjustmentListener {
		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			LightSettingWindow window = view.lightSettingWindow;
			LightConfiguration lightConfig = model.getConfiguration().getLightConfiguration();

			// Get the modified values from the slider.
			double horizontalAngle = (double)window.horizontalAngleBar.getValue() / (double)LightSettingWindow.SCROLL_BAR_MAX_COUNT;
			double verticalAngle = (double)window.verticalAngleBar.getValue() / (double)LightSettingWindow.SCROLL_BAR_MAX_COUNT;
			horizontalAngle *= 2.0 * Math.PI; // Map the maximum horizontal angle to 2pi.
			verticalAngle *= Math.PI; // Map the maximum vertical angle to pi.

			// Compute the light direction vector (facing to the light source).
			// Note that, on this application, the light direction vector is defined on the view coordinate system.
			// So the X axis faces to the right side of the screen, Y faces to the upper side, and Z faces to the front side.
			// The vertical angle is the angle between the vector and Y (NOT Z) axis.
			// The horizontal angle is the angle between Z axis and the projected vector to the X-Z plane.
			double lightY = Math.cos(verticalAngle);
			double lightZ = Math.cos(horizontalAngle) * Math.sin(verticalAngle);
			double lightX = Math.sin(horizontalAngle) * Math.sin(verticalAngle);

			// Set the updated vector to the light configuration.
			lightConfig.setLightSourceDirection(lightX, lightY, lightZ);

			// Propagate the above update of the configuration to the entire application.
			setEventHandlingEnabled(false);
			presenter.propagateConfiguration();
			setEventHandlingEnabled(true);

			// Perform rendering on the rendering loop's thread asynchronously.
			presenter.renderingLoop.requestRendering();
		}
	}
}
