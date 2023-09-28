package com.rinearn.graph3d.presenter;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.view.CameraSettingWindow;
import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.config.CameraConfiguration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;

import static java.lang.Math.PI;


/**
 * The class handling events and API requests for setting camera-related parameters.
 */
public final class CameraSettingHandler {

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
	public CameraSettingHandler(Model model, View view, Presenter presenter) {
		this.model = model;
		this.view = view;
		this.presenter = presenter;

		// Add the action listener defined in this class, to the scroll bars in this setting window.
		CameraSettingWindow window = this.view.cameraSettingWindow;
		window.zenithAxisBox.addActionListener(new ZenithAxisSelectedEventListener());
		window.horizontalAngleBar.addAdjustmentListener(new HorizontalAngleScrolledEventListener());
		window.verticalAngleBar.addAdjustmentListener(new VerticalAngleScrolledEventListener());
		window.screwAngleBar.addAdjustmentListener(new ScrewAngleScrolledEventListener());
		window.magnificationBar.addAdjustmentListener(new MagnificationScrolledEventListener());
		window.distanceBar.addAdjustmentListener(new DistanceScrolledEventListener());
		window.horizontalCenterOffsetBar.addAdjustmentListener(new HorizontalCenterOffsetScrolledEventListener());
		window.verticalCenterOffsetBar.addAdjustmentListener(new VerticalCenterOffsetScrolledEventListener());
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
	 * The event listener handling the event that the combo box of "Zenith Axis" parameter is operated.
	 */
	private final class ZenithAxisSelectedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			CameraSettingWindow window = view.cameraSettingWindow;
			CameraConfiguration cameraConfig = model.getConfiguration().getCameraConfiguration();

			// Get the selected zenith angle.
			String zenithAngle = String.class.cast(window.zenithAxisBox.getSelectedItem());

			// Set the angle mode corresponding the above, into the configuration container.
			switch (zenithAngle) {
				case CameraSettingWindow.ZENITH_AXIS_BOX_ITEM_X : {
					cameraConfig.setAngleMode(CameraConfiguration.AngleMode.X_ZENITH);
					break;
				}
				case CameraSettingWindow.ZENITH_AXIS_BOX_ITEM_Y : {
					cameraConfig.setAngleMode(CameraConfiguration.AngleMode.Y_ZENITH);
					break;
				}
				case CameraSettingWindow.ZENITH_AXIS_BOX_ITEM_Z : {
					cameraConfig.setAngleMode(CameraConfiguration.AngleMode.Z_ZENITH);
					break;
				}
				default : {
					throw new IllegalStateException("Unexpected zenith angle: " + zenithAngle);
				}
			}

			// Propagate the above update of the configuration to the entire application.
			setEventHandlingEnabled(false);
			presenter.propagateConfiguration();
			setEventHandlingEnabled(true);

			// Perform rendering on the rendering loop's thread asynchronously.
			presenter.renderingLoop.requestRendering();
		}
	}


	/**
	 * The event listener handling the event that the scroll bar of "Horizontal Angle" parameter is moved.
	 */
	private final class HorizontalAngleScrolledEventListener implements AdjustmentListener {
		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			CameraSettingWindow window = view.cameraSettingWindow;
			CameraConfiguration cameraConfig = model.getConfiguration().getCameraConfiguration();

			// Get the modified value from the slider, and store it into the configuration container.
			double angle = (double)window.horizontalAngleBar.getValue() / (double)CameraSettingWindow.BASIC_SCROLL_BAR_MAX_COUNT;
			angle *= 2.0 * PI;
			cameraConfig.setHorizontalAngle(angle);

			// Propagate the above update of the configuration to the entire application.
			setEventHandlingEnabled(false);
			presenter.propagateConfiguration();
			setEventHandlingEnabled(true);

			// Perform rendering on the rendering loop's thread asynchronously.
			presenter.renderingLoop.requestRendering();
		}
	}


	/**
	 * The event listener handling the event that the scroll bar of "Vertical Angle" parameter is moved.
	 */
	private final class VerticalAngleScrolledEventListener implements AdjustmentListener {
		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			CameraSettingWindow window = view.cameraSettingWindow;
			CameraConfiguration cameraConfig = model.getConfiguration().getCameraConfiguration();

			// Get the modified value from the slider, and store it into the configuration container.
			double angle = (double)window.verticalAngleBar.getValue() / (double)CameraSettingWindow.BASIC_SCROLL_BAR_MAX_COUNT;
			angle *= PI;
			cameraConfig.setVerticalAngle(angle);

			// Propagate the above update of the configuration to the entire application.
			setEventHandlingEnabled(false);
			presenter.propagateConfiguration();
			setEventHandlingEnabled(true);

			// Perform rendering on the rendering loop's thread asynchronously.
			presenter.renderingLoop.requestRendering();
		}
	}


	/**
	 * The event listener handling the event that the scroll bar of "Screw Angle" parameter is moved.
	 */
	private final class ScrewAngleScrolledEventListener implements AdjustmentListener {
		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			CameraSettingWindow window = view.cameraSettingWindow;
			CameraConfiguration cameraConfig = model.getConfiguration().getCameraConfiguration();

			// Get the modified value from the slider, and store it into the configuration container.
			double angle = (double)window.screwAngleBar.getValue() / (double)CameraSettingWindow.BASIC_SCROLL_BAR_MAX_COUNT;
			angle *= 2.0 * PI;
			cameraConfig.setScrewAngle(angle);

			// Propagate the above update of the configuration to the entire application.
			setEventHandlingEnabled(false);
			presenter.propagateConfiguration();
			setEventHandlingEnabled(true);

			// Perform rendering on the rendering loop's thread asynchronously.
			presenter.renderingLoop.requestRendering();
		}
	}


	/**
	 * The event listener handling the event that the scroll bar of "Magnification" parameter is moved.
	 */
	private final class MagnificationScrolledEventListener implements AdjustmentListener {
		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			CameraSettingWindow window = view.cameraSettingWindow;
			CameraConfiguration cameraConfig = model.getConfiguration().getCameraConfiguration();

			// Get the modified value from the slider, and store it into the configuration container.
			double magnification = (double)window.magnificationBar.getValue() / (double)CameraSettingWindow.BASIC_SCROLL_BAR_MAX_COUNT;
			magnification *= CameraSettingWindow.MAX_MAGNIFICATION;
			cameraConfig.setMagnification(magnification);

			// Propagate the above update of the configuration to the entire application.
			setEventHandlingEnabled(false);
			presenter.propagateConfiguration();
			setEventHandlingEnabled(true);

			// Perform rendering on the rendering loop's thread asynchronously.
			presenter.renderingLoop.requestRendering();
		}
	}


	/**
	 * The event listener handling the event that the scroll bar of "Distance" parameter is moved.
	 */
	private final class DistanceScrolledEventListener implements AdjustmentListener {
		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			CameraSettingWindow window = view.cameraSettingWindow;
			CameraConfiguration cameraConfig = model.getConfiguration().getCameraConfiguration();

			// Get the modified value from the slider, and store it into the configuration container.
			double distance = (double)window.distanceBar.getValue() / (double)CameraSettingWindow.BASIC_SCROLL_BAR_MAX_COUNT;
			distance *= CameraSettingWindow.MAX_DISTANCE;
			cameraConfig.setDistance(distance);

			// Propagate the above update of the configuration to the entire application.
			setEventHandlingEnabled(false);
			presenter.propagateConfiguration();
			setEventHandlingEnabled(true);

			// Perform rendering on the rendering loop's thread asynchronously.
			presenter.renderingLoop.requestRendering();
		}
	}


	/**
	 * The event listener handling the event that the scroll bar of "Horizontal Center Offset" parameter is moved.
	 */
	private final class HorizontalCenterOffsetScrolledEventListener implements AdjustmentListener {
		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			CameraSettingWindow window = view.cameraSettingWindow;
			CameraConfiguration cameraConfig = model.getConfiguration().getCameraConfiguration();

			// Get the modified value from the slider, and store it into the configuration container.
			int offset = window.horizontalCenterOffsetBar.getValue();
			cameraConfig.setHorizontalCenterOffset(offset);

			// Propagate the above update of the configuration to the entire application.
			setEventHandlingEnabled(false);
			presenter.propagateConfiguration();
			setEventHandlingEnabled(true);

			// Perform rendering on the rendering loop's thread asynchronously.
			presenter.renderingLoop.requestRendering();
		}
	}


	/**
	 * The event listener handling the event that the scroll bar of "Vertical Center Offset" parameter is moved.
	 */
	private final class VerticalCenterOffsetScrolledEventListener implements AdjustmentListener {
		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			CameraSettingWindow window = view.cameraSettingWindow;
			CameraConfiguration cameraConfig = model.getConfiguration().getCameraConfiguration();

			// Get the modified value from the slider, and store it into the configuration container.
			int offset = window.verticalCenterOffsetBar.getValue();
			cameraConfig.setVerticalCenterOffset(offset);

			// Propagate the above update of the configuration to the entire application.
			setEventHandlingEnabled(false);
			presenter.propagateConfiguration();
			setEventHandlingEnabled(true);

			// Perform rendering on the rendering loop's thread asynchronously.
			presenter.renderingLoop.requestRendering();
		}
	}





	// ================================================================================
	// 
	// - API Listeners -
	//
	// ================================================================================


	/**
	 * Sets the camera angle by three angular parameters, regarding the specified axis (by "angleMode" arg) as the zenith axis.
	 * 
	 * @param horizontalAngle The horizontal angle, which is the rotation angle of the camera's location around the zenith axis.
	 * @param verticalAngle The vertical angle, which is the angle between the zenith axis and the direction toward the camera.
	 * @param screwAngle The screw angle, which is the rotation angle of the camera itself (not location) around the screen center.
	 * @param angleMode Specify X_ZENITH/Y_ZENITH/Z_ZENITH for regarding X/Y/Z axis as the zenith angle.
	 */
	public void setZenithCameraAngle(double horizontalAngle, double verticalAngle, double screwAngle,
			CameraConfiguration.AngleMode angleMode) {

		// Handle the API on the event-dispatcher thread.
		SetZenithCameraAngleAPIListener apiListener = new SetZenithCameraAngleAPIListener(
				horizontalAngle, verticalAngle, screwAngle, angleMode
		);
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
	 * The class handling API requests from setZenithCameraAngle(-) method,
	 * on event-dispatcher thread.
	 */
	private final class SetZenithCameraAngleAPIListener implements Runnable {

		/** The horizontal angle, which is the rotation angle of the camera's location around the zenith axis. */
		private volatile double horizontalAngle;

		/** The vertical angle, which is the angle between the zenith axis and the direction toward the camera. */
		private volatile double verticalAngle;

		/** The screw angle, which is the rotation angle of the camera itself (not location) around the screen center. */
		private volatile double screwAngle;

		/** The angle mode. Specify X_ZENITH/Y_ZENITH/Z_ZENITH for regarding X/Y/Z axis as the zenith angle. */
		private volatile CameraConfiguration.AngleMode angleMode;

		/**
		 * Create an instance handling setZenithCameraAngle(-) API with the specified argument.
		 * 
		 * @param horizontalAngle The horizontal angle, which is the rotation angle of the camera's location around the zenith axis.
		 * @param verticalAngle The vertical angle, which is the angle between the zenith axis and the direction toward the camera.
		 * @param screwAngle The screw angle, which is the rotation angle of the camera itself (not location) around the screen center.
		 * @param angleMode Specify X_ZENITH/Y_ZENITH/Z_ZENITH for regarding X/Y/Z axis as the zenith angle.
		 */
		public SetZenithCameraAngleAPIListener(double horizontalAngle, double verticalAngle, double screwAngle,
				CameraConfiguration.AngleMode angleMode) {

			this.horizontalAngle = horizontalAngle;
			this.verticalAngle = verticalAngle;
			this.screwAngle = screwAngle;
			this.angleMode = angleMode;
		}

		@Override
		public void run() {
			CameraConfiguration cameraConfig = model.getConfiguration().getCameraConfiguration();
			cameraConfig.setAngleMode(angleMode);
			cameraConfig.setHorizontalAngle(horizontalAngle);
			cameraConfig.setVerticalAngle(verticalAngle);
			cameraConfig.setScrewAngle(screwAngle);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}


 	/**
 	 * Sets the distance between the viewpoint and the origin of the graph.
 	 * (API Implementation)
 	 * 
 	 * @param distance The distance between the viewpoint and the origin of the graph.
 	 */
	public void setCameraDistance(double distance) {

		// Handle the API on the event-dispatcher thread.
		SetCameraDistanceAPIListener apiListener = new SetCameraDistanceAPIListener(distance);
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
	 * The class handling API requests from setCameraDistance(-) method,
	 * on event-dispatcher thread.
	 */
	private final class SetCameraDistanceAPIListener implements Runnable {

		/** The distance between the viewpoint and the origin of the graph. */
		private volatile double distance;

		/**
		 * Create an instance handling setCameraDistance(-) API with the specified argument.
		 * 
		 * @param distance The distance between the viewpoint and the origin of the graph.
		 */
		public SetCameraDistanceAPIListener(double distance) {
			this.distance = distance;
		}

		@Override
		public void run() {
			CameraConfiguration cameraConfig = model.getConfiguration().getCameraConfiguration();
			cameraConfig.setDistance(distance);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}


	/**
	 * Sets the magnification of the graph screen.
	 * 
	 * @param magnification The magnification of the graph screen.
	 */
	public void setCameraMagnification(double magnification) {

		// Handle the API on the event-dispatcher thread.
		SetCameraMagnificationAPIListener apiListener = new SetCameraMagnificationAPIListener(magnification);
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
	 * The class handling API requests from setCameraMagnification(-) method,
	 * on event-dispatcher thread.
	 */
	private final class SetCameraMagnificationAPIListener implements Runnable {

		/** The distance between the viewpoint and the origin of the graph. */
		private volatile double magnification;

		/**
		 * Create an instance handling setCameraMagnification(-) API with the specified argument.
		 * 
		 * @param magnification The magnification of the graph screen.
		 */
		public SetCameraMagnificationAPIListener(double magnification) {
			this.magnification = magnification;
		}

		@Override
		public void run() {
			CameraConfiguration cameraConfig = model.getConfiguration().getCameraConfiguration();
			cameraConfig.setMagnification(magnification);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}

}
