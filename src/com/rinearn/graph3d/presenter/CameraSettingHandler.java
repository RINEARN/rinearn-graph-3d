package com.rinearn.graph3d.presenter;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.view.View;
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
	 * Sets the camera angle by three angular parameters, regarding the specified axis (by "angleMode" arg) as the zenith axis.
	 * 
	 * @param horizontalAngle The horizontal angle, which is the rotation angle of the camera's location around the zenith axis.
	 * @param verticalAngle The vertical angle, which is the angle between the zenith axis and the direction toward the camera.
	 * @param screwAngle The screw angle, which is the rotation angle of the camera itself (not location) around the screen center.
	 * @param angleMode Specify X_ZENITH/Y_ZENITH/Z_ZENITH for regarding X/Y/Z axis as the zenith angle.
	 */
	public void setZenithCameraAngle(double horizontalAngle, double verticalAngle, double screwAngle,
			CameraConfiguration.AngleMode angleMode) {

		CameraConfiguration cameraConfig = this.model.getConfiguration().getCameraConfiguration();
		cameraConfig.setAngleMode(angleMode);
		cameraConfig.setHorizontalAngle(horizontalAngle);
		cameraConfig.setVerticalAngle(verticalAngle);
		cameraConfig.setScrewAngle(screwAngle);
		this.presenter.propagateConfiguration();
		this.presenter.replot();
	}


 	/**
 	 * Sets the distance between the viewpoint and the origin of the graph.
 	 * 
 	 * @param distance The distance between the viewpoint and the origin of the graph.
 	 */
	public void setCameraDistance(double distance) {
		CameraConfiguration cameraConfig = this.model.getConfiguration().getCameraConfiguration();
		cameraConfig.setDistance(distance);
		this.presenter.propagateConfiguration();
		this.presenter.replot();
	}

	/**
	 * Sets the magnification of the graph screen.
	 * 
	 * @param magnification The magnification of the graph screen.
	 */
	public void setCameraMagnification(double magnification) {
		CameraConfiguration cameraConfig = this.model.getConfiguration().getCameraConfiguration();
		cameraConfig.setMagnification(magnification);
		this.presenter.propagateConfiguration();
		this.presenter.replot();
	}
}
