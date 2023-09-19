package com.rinearn.graph3d;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.view.MainWindow;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.renderer.RinearnGraph3DRenderer;
import com.rinearn.graph3d.renderer.simple.SimpleRenderer;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.CameraConfiguration;
import com.rinearn.graph3d.config.RangeConfiguration;

import java.awt.Image;
import java.math.BigDecimal;


/**
 * <spac lass="lang-en">
 * The class at the top layer of the implementation of RINEARN Graph 3D,
 * and also plays the role of the front-end of
 * API for controlling RINEARN Graph 3D from user's code
 * </span>
 * <span class="lang-ja">
 * リニアングラフ3Dの実装の最表層クラスであり、
 * また、コードによってリニアングラフ3Dを制御する際のAPIのフロントエンドも担います
 * </span>
 * .
 */
public class RinearnGraph3D {

	/** The front-end class of "Model" layer, which provides internal logic procedures and so on. */
	private final Model model;

	/** The front-end class of "View" layer, which provides visible part of GUI without event handling. */
	private final View view;

	/** The front-end class of "Presenter" layer, which invokes Model's procedures triggered by user's action on GUI. */
	private final Presenter presenter;

	/** The rendering engine of 3D graphs. */
	private final RinearnGraph3DRenderer renderer;


	/**
	 * <span class="lang-en">
	 * Launch a new RINEARN Graph 3D window
	 * </span>
	 * <span class="lang-ja">
	 * 新しいリニアングラフ3Dの画面を起動します
	 * </span>
	 */
	public RinearnGraph3D() {

		// Create "Model" layer, which provides internal logic procedures and so on.
		this.model = new Model();

		// Create "View" layer, which provides visible part of GUI without event handling.
		this.view = new View();

		// Create a rendering engine of 3D graphs.
		this.renderer = new SimpleRenderer(
				MainWindow.DEFAULT_SCREEN_WIDTH, MainWindow.DEFAULT_SCREEN_HEIGHT
		);

		// Set the reference to the rendered image of the renderer,
		// to the graph screen of the window for displaying it.
		Image screenImage = this.renderer.getScreenImage();
		this.view.mainWindow.setScreenImage(screenImage);

		// First rendering and repainting.
		this.renderer.drawScale();
		this.renderer.drawLabel();
		this.renderer.drawGrid();
		this.renderer.drawFrame();
		this.renderer.render();
		//this.view.mainWindow.repaintScreen();

		// Show the window.
		this.view.mainWindow.setWindowVisible(true);

		// Create "Presenter" layer which invokes Model's procedures triggered by user's action on GUI.
		// (The rendering loop is also running in this Presenter layer.)
		this.presenter = new Presenter(this.model, this.view, this.renderer);
	}


	/**
	 * <span class="lang-en">
	 * Returns the 3D renderer, which is being used for rendering the graph image in this instance
	 * </span>
	 * <span class="lang-ja">
	 * グラフ画面を描画するのに使用されている3D描画エンジン（レンダラー）を返します
	 * </span>
	 * .
	 * @return
	 *   <span class="lang-en">The 3D renderer</span>
	 *   <span class="lang-ja">3D描画エンジン（レンダラー）</span>
	 */
	public synchronized RinearnGraph3DRenderer getRenderer() {
		return this.renderer;
	}


	/**
	 * Sets the range of X axis.
	 * 
	 * @param min The minimum coordinate value of X axis.
	 * @param max The maximum coordinate value of X axis.
	 */
	public synchronized void setXRange(double min, double max) {
		this.presenter.rangeSettingHandler.setXRange(new BigDecimal(min), new BigDecimal(max));
	}

	/**
	 * Sets the range of X axis.
	 * 
	 * @param min The minimum coordinate value of X axis.
	 * @param max The maximum coordinate value of X axis.
	 */
	public synchronized void setXRange(BigDecimal min, BigDecimal max) {
		this.presenter.rangeSettingHandler.setXRange(min, max);
	}

	/**
	 * Turns on/off the auto-ranging feature for X axis.
	 * 
	 * @param enabled Specify true/false for turning on/off (the default is on).
	 */
	public synchronized void setXAutoRangingEnabled(boolean enabled) {
		this.presenter.rangeSettingHandler.setXAutoRangingEnabled(enabled);
	}


	/**
	 * Sets the range of Y axis.
	 * 
	 * @param min The minimum coordinate value of Y axis.
	 * @param max The maximum coordinate value of Y axis.
	 */
	public synchronized void setYRange(double min, double max) {
		this.presenter.rangeSettingHandler.setYRange(new BigDecimal(min), new BigDecimal(max));
	}

	/**
	 * Sets the range of Y axis.
	 * 
	 * @param min The minimum coordinate value of Y axis.
	 * @param max The maximum coordinate value of Y axis.
	 */
	public synchronized void setYRange(BigDecimal min, BigDecimal max) {
		this.presenter.rangeSettingHandler.setYRange(min, max);
	}

	/**
	 * Turns on/off the auto-ranging feature for Y axis.
	 * 
	 * @param enabled Specify true/false for turning on/off (the default is on).
	 */
	public synchronized void setYAutoRangingEnabled(boolean enabled) {
		this.presenter.rangeSettingHandler.setYAutoRangingEnabled(enabled);
	}


	/**
	 * Sets the range of Z axis.
	 * 
	 * @param min The minimum coordinate value of Z axis.
	 * @param max The maximum coordinate value of Z axis.
	 */
	public synchronized void setZRange(double min, double max) {
		this.presenter.rangeSettingHandler.setZRange(new BigDecimal(min), new BigDecimal(max));
	}

	/**
	 * Sets the range of Z axis.
	 * 
	 * @param min The minimum coordinate value of Z axis.
	 * @param max The maximum coordinate value of Z axis.
	 */
	public synchronized void setZRange(BigDecimal min, BigDecimal max) {
		this.presenter.rangeSettingHandler.setZRange(min, max);
	}

	/**
	 * Turns on/off the auto-ranging feature for Z axis.
	 * 
	 * @param enabled Specify true/false for turning on/off (the default is on).
	 */
	public synchronized void setZAutoRangingEnabled(boolean enabled) {
		this.presenter.rangeSettingHandler.setZAutoRangingEnabled(enabled);
	}


	/**
	 * Set the displayed text of X axis label.
	 * 
	 * @param xLabel The text of X axis label.
	 */
	public synchronized void setXLabel(String xLabel) {
		this.presenter.labelSettingHandler.setXLabel(xLabel);
	}

	/**
	 * Set the displayed text of Y axis label.
	 * 
	 * @param yLabel The text of Y axis label.
	 */
	public synchronized void setYLabel(String yLabel) {
		this.presenter.labelSettingHandler.setYLabel(yLabel);
	}

	/**
	 * Set the displayed text of Z axis label.
	 * 
	 * @param zLabel The text of Z axis label.
	 */
	public synchronized void setZLabel(String zLabel) {
		this.presenter.labelSettingHandler.setZLabel(zLabel);
	}


 	/**
 	 * Sets the distance between the viewpoint and the origin of the graph.
 	 * 
 	 * @param distance The distance between the viewpoint and the origin of the graph.
 	 */
	public void setCameraDistance(double distance) {
		this.presenter.cameraSettingHandler.setCameraDistance(distance);
	}

	/**
	 * Sets the magnification of the graph screen.
	 * 
	 * @param magnification The magnification of the graph screen.
	 */
	public void setCameraMagnification(double magnification) {
		this.presenter.cameraSettingHandler.setCameraMagnification(magnification);
	}


	/**
	 * Sets the camera angle by two angular parameters, regarding X axis as the zenith axis.
	 * 
	 * The "screw angle" is always set to zero by this method.
	 * To set the screw angle together, use the overload of this method, having 3-arguments instead.
	 * 
	 * @param horizontalAngle The horizontal angle, which is the rotation angle of the camera's location around the zenith axis.
	 * @param verticalAngle The vertical angle, which is the angle between the zenith axis and the direction toward the camera.
	 */
	public synchronized void setXZenithCameraAngle(double horizontalAngle, double verticalAngle) {
		this.presenter.cameraSettingHandler.setZenithCameraAngle(
				horizontalAngle, verticalAngle, 0.0, CameraConfiguration.AngleMode.X_ZENITH
		);
	}

	/**
	 * Sets the camera angle by three angular parameters, regarding X axis as the zenith axis.
	 * 
	 * @param horizontalAngle The horizontal angle, which is the rotation angle of the camera's location around the zenith axis.
	 * @param verticalAngle The vertical angle, which is the angle between the zenith axis and the direction toward the camera.
	 * @param screwAngle The screw angle, which is the rotation angle of the camera itself (not location) around the screen center.
	 */
	public synchronized void setXZenithCameraAngle(double horizontalAngle, double verticalAngle, double screwAngle) {
		this.presenter.cameraSettingHandler.setZenithCameraAngle(
				horizontalAngle, verticalAngle, screwAngle, CameraConfiguration.AngleMode.X_ZENITH
		);
	}


	/**
	 * Sets the camera angle by two angular parameters, regarding Y axis as the zenith axis.
	 * 
	 * The "screw angle" is always set to zero by this method.
	 * To set the screw angle together, use the overload of this method, having 3-arguments instead.
	 * 
	 * @param horizontalAngle The horizontal angle, which is the rotation angle of the camera's location around the zenith axis.
	 * @param verticalAngle The vertical angle, which is the angle between the zenith axis and the direction toward the camera.
	 */
	public synchronized void setYZenithCameraAngle(double horizontalAngle, double verticalAngle) {
		this.presenter.cameraSettingHandler.setZenithCameraAngle(
				horizontalAngle, verticalAngle, 0.0, CameraConfiguration.AngleMode.Y_ZENITH
		);
	}

	/**
	 * Sets the camera angle by three angular parameters, regarding Y axis as the zenith axis.
	 * 
	 * @param horizontalAngle The horizontal angle, which is the rotation angle of the camera's location around the zenith axis.
	 * @param verticalAngle The vertical angle, which is the angle between the zenith axis and the direction toward the camera.
	 * @param screwAngle The screw angle, which is the rotation angle of the camera itself (not location) around the screen center.
	 */
	public synchronized void setYZenithCameraAngle(double horizontalAngle, double verticalAngle, double screwAngle) {
		this.presenter.cameraSettingHandler.setZenithCameraAngle(
				horizontalAngle, verticalAngle, screwAngle, CameraConfiguration.AngleMode.Y_ZENITH
		);
	}


	/**
	 * Sets the camera angle by two angular parameters, regarding Z axis as the zenith axis.
	 * 
	 * The "screw angle" is always set to zero by this method.
	 * To set the screw angle together, use the overload of this method, having 3-arguments instead.
	 * 
	 * @param horizontalAngle The horizontal angle, which is the rotation angle of the camera's location around the zenith axis.
	 * @param verticalAngle The vertical angle, which is the angle between the zenith axis and the direction toward the camera.
	 */
	public synchronized void setZZenithCameraAngle(double horizontalAngle, double verticalAngle) {
		this.presenter.cameraSettingHandler.setZenithCameraAngle(
				horizontalAngle, verticalAngle, 0.0, CameraConfiguration.AngleMode.Z_ZENITH
		);
	}

	/**
	 * Sets the camera angle by three angular parameters, regarding Z axis as the zenith axis.
	 * 
	 * @param horizontalAngle The horizontal angle, which is the rotation angle of the camera's location around the zenith axis.
	 * @param verticalAngle The vertical angle, which is the angle between the zenith axis and the direction toward the camera.
	 * @param screwAngle The screw angle, which is the rotation angle of the camera itself (not location) around the screen center.
	 */
	public synchronized void setZZenithCameraAngle(double horizontalAngle, double verticalAngle, double screwAngle) {
		this.presenter.cameraSettingHandler.setZenithCameraAngle(
				horizontalAngle, verticalAngle, screwAngle, CameraConfiguration.AngleMode.Z_ZENITH
		);
	}


	/**
	 * Sets the coordinates (locations) and the labels (displayed text) of the scale ticks on X axis.
	 * 
	 * @param tickCoordinates The coordinates of the scale ticks.
	 * @param tickLabels The labels of the scale ticks.
	 */
	public synchronized void setXTicks(BigDecimal[] tickCoordinates, String[] tickLabels) {
		this.presenter.scaleSettingHandler.setXTicks(tickCoordinates, tickLabels);
	}

	/**
	 * Sets the coordinates (locations) and the labels (displayed text) of the scale ticks on X axis.
	 * 
	 * @param tickCoordinates The coordinates of the scale ticks.
	 * @param tickLabels The labels of the scale ticks.
	 */
	public synchronized void setXTicks(double[] tickCoordinates, String[] tickLabels) {
		BigDecimal[] bdTickCoordinates = new BigDecimal[tickCoordinates.length];
		for (int itick=0; itick<tickCoordinates.length; itick++) {
			bdTickCoordinates[itick] = new BigDecimal(tickCoordinates[itick]);
		}
		this.presenter.scaleSettingHandler.setXTicks(bdTickCoordinates, tickLabels);
	}


	/**
	 * Sets the coordinates (locations) and the labels (displayed text) of the scale ticks on Y axis.
	 * 
	 * @param tickCoordinates The coordinates of the scale ticks.
	 * @param tickLabels The labels of the scale ticks.
	 */
	public synchronized void setYTicks(BigDecimal[] tickCoordinates, String[] tickLabels) {
		this.presenter.scaleSettingHandler.setYTicks(tickCoordinates, tickLabels);
	}

	/**
	 * Sets the coordinates (locations) and the labels (displayed text) of the scale ticks on Y axis.
	 * 
	 * @param tickCoordinates The coordinates of the scale ticks.
	 * @param tickLabels The labels of the scale ticks.
	 */
	public synchronized void setYTicks(double[] tickCoordinates, String[] tickLabels) {
		BigDecimal[] bdTickCoordinates = new BigDecimal[tickCoordinates.length];
		for (int itick=0; itick<tickCoordinates.length; itick++) {
			bdTickCoordinates[itick] = new BigDecimal(tickCoordinates[itick]);
		}
		this.presenter.scaleSettingHandler.setYTicks(bdTickCoordinates, tickLabels);
	}


	/**
	 * Sets the coordinates (locations) and the labels (displayed text) of the scale ticks on Z axis.
	 * 
	 * @param tickCoordinates The coordinates of the scale ticks.
	 * @param tickLabels The labels of the scale ticks.
	 */
	public synchronized void setZTicks(BigDecimal[] tickCoordinates, String[] tickLabels) {
		this.presenter.scaleSettingHandler.setZTicks(tickCoordinates, tickLabels);
	}

	/**
	 * Sets the coordinates (locations) and the labels (displayed text) of the scale ticks on Z axis.
	 * 
	 * @param tickCoordinates The coordinates of the scale ticks.
	 * @param tickLabels The labels of the scale ticks.
	 */
	public synchronized void setZTicks(double[] tickCoordinates, String[] tickLabels) {
		BigDecimal[] bdTickCoordinates = new BigDecimal[tickCoordinates.length];
		for (int itick=0; itick<tickCoordinates.length; itick++) {
			bdTickCoordinates[itick] = new BigDecimal(tickCoordinates[itick]);
		}
		this.presenter.scaleSettingHandler.setZTicks(bdTickCoordinates, tickLabels);
	}


	/**
	 * Re-plots the contents composing the graph.
	 */
	public synchronized void replot() {
		this.presenter.replot();
	}
}
