package com.rinearn.graph3d.presenter;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.view.View;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import com.rinearn.graph3d.config.CameraConfiguration;


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
