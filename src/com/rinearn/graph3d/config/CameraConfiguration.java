package com.rinearn.graph3d.config;

import static java.lang.Math.PI;
import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.acos;
import static java.lang.Math.atan2;


/**
 * The class for storing configuration parameters of the camera.
 */
public class CameraConfiguration {

	/** The array index representing X coordinate, in 3-D vectors. */
	private static final int X = 0;

	/** The array index representing Y coordinate, in 3-D vectors. */
	private static final int Y = 1;

	/** The array index representing Z coordinate, in 3-D vectors. */
	private static final int Z = 2;

	/** The magnification of the graph screen. */
	private volatile double magnification = 700.0;

	/** The distance between the viewpoint and the origin of the graph. */
	private volatile double distance = 4.0;

	/** The horizontal offset amount of the graph center (= rotation center), from the screen center. */
	private volatile int horizontalCenterOffset = 0;

	/** The vertical offset amount of the graph center (= rotation center), from the screen center. */
	private volatile int verticalCenterOffset = 0;

	/** The width (pixels) of the screen. */
	private volatile int screenWidth = 500;

	/** The height (pixels) of the screen. */
	private volatile int screenHeight = 500;

	/** The enum representing each angle mode, for switching how specify the camera angle(s). */
	public static enum AngleMode {

		/** Specifies vertical/horizontal/screw angles of the camera's position, regarding X axis as the zenith axis. */
		X_ZENITH,

		/** Specifies vertical/horizontal/screw angles of the camera's position, regarding Y axis as the zenith axis. */
		Y_ZENITH,

		/** Specifies vertical/horizontal/screw angles of the camera's position, regarding Z axis as the zenith axis. */
		Z_ZENITH;
	}

	/** The angle mode, for switching how specify the camera angle(s). */
	private volatile AngleMode angleMode = AngleMode.Z_ZENITH;

	/** The vertical angle, which is the angle between the zenith axis and the direction toward the camera. */
	private volatile double verticalAngle = 0.0;

	/** The horizontal angle, which is the rotation angle of the camera's location around the zenith axis. */
	private volatile double horizontalAngle = 0.0;

	/** The screw angle, which is the rotation angle of the camera itself (not location) around the screen center. */
	private volatile double screwAngle = 0.0;

	/** The matrix representing the rotation of the graph to the current state from the initial state. */
 	private volatile double[][] rotationMatrix = {
 		{ 1.0, 0.0, 0.0 },
 		{ 0.0, 1.0, 0.0 },
 		{ 0.0, 0.0, 1.0 }
 	};

 
 	/**
 	 * Sets the magnification of the graph screen.
 	 * 
 	 * @param magnification The magnification of the graph screen.
 	 */
 	public synchronized void setMagnification(double magnification) {
		this.magnification = magnification;
 	}

 
 	/**
 	 * Gets the magnification of the graph screen.
 	 * 
 	 * @return The magnification of the graph screen.
 	 */
 	public synchronized double getMagnification() {
		return this.magnification;
 	}


 	/**
 	 * Sets the distance between the viewpoint and the origin of the graph.
 	 * 
 	 * @param distance The distance between the viewpoint and the origin of the graph.
 	 */
 	public synchronized void setDistance(double distance) {
 		this.distance = distance;
 	}

 
 	/**
 	 * Gets the distance between the viewpoint and the origin of the graph.
 	 * 
 	 * @return The distance between the viewpoint and the origin of the graph.
 	 */
 	public synchronized double getDistance() {
 		return this.distance;
 	}


 	/**
 	 * Sets the horizontal offset amount of the graph center, from the screen center.
 	 * 
 	 * When the graph center matches with the screen center, the offset amount is 0.
 	 * If specify a positive value, the position of the graph is shifted to the right, from the screen center.
 	 * Also, the center of rotation always matches with the (shifted) graph center.
 	 * 
 	 * @param horizontalCenterOffset The horizontal offset amount of the graph center (in pixels).
 	 */
 	public synchronized void setHorizontalCenterOffset(int horizontalCenterOffset) {
 		this.horizontalCenterOffset = horizontalCenterOffset;
 	}
 
 
 	/**
 	 * Gets the horizontal offset amount of the graph center, on the screen, from the screen center.
 	 * 
 	 * @param horizontalCenterOffset The horizontal offset amount of the graph center (in pixels).
 	 */
 	public synchronized int getHorizontalCenterOffset() {
 		return this.horizontalCenterOffset;
 	}

 
 	/**
 	 * Sets the vertical offset amount of the graph center, on the screen, from the screen center.
 	 * 
 	 * When the graph center matches with the screen center, the offset amount is 0.
 	 * If specify a positive value, the position of the graph is shifted up, from the screen center.
 	 * Also, the center of rotation always matches with the (shifted) graph center.
 	 * 
 	 * @param verticalCenterOffset The vertical offset amount of the graph center (in pixels).
 	 */
	public synchronized void setVerticalCenterOffset(int verticalScreenCenterOffset) {
 		this.verticalCenterOffset = verticalScreenCenterOffset;
 	}


 	/**
 	 * Gets the vertical offset amount of the graph center, on the screen, from the screen center.
 	 * 
 	 * @param verticalCenterOffset The vertical offset amount of the graph center (in pixels).
 	 */
 	public synchronized int getVerticalCenterOffset() {
 		return this.verticalCenterOffset;
 	}

 
 	/**
 	 * Sets the size of the screen.
 	 * 
 	 * @param screenWidth The width (pixels) of the screen.
 	 * @param screenHeight The height (pixels) of the screen.
 	 */
 	public synchronized void setScreenSize(int screenWidth, int screenHeight) {
 		this.screenWidth = screenWidth;
 		this.screenHeight = screenHeight;
 	}

 
 	/**
 	 * Gets the width (pixels) of the screen.
 	 * 
 	 * @return The width (pixels) of the screen.
 	 */
 	public synchronized int getScreenWidth() {
 		return this.screenWidth;
 	}
 
 
 	/**
 	 * Gets the height (pixels) of the screen.
 	 * 
 	 * @return The height (pixels) of the screen.
 	 */
	public synchronized int getScreenHeight() {
 		return this.screenHeight;
 	}


 	/**
 	 * Gets the matrix representing the rotation of the graph to the current state from the initial state.
 	 * 
 	 * @return The matrix representing the rotation of the graph.
 	 */
	public synchronized double[][] getRotationMatrix() {

		// Note: Probably we should not provide the setter for the rotation matrix.
		return this.rotationMatrix;
	}


	// !!! NOTE !!!
	//   このメソッド、renderer にあった時に rotateX/Y/Z との対応で命名したけど、見直すべきかも。
	//   現状だと「これまでの追加回転をキャンセルする」というニュアンスだけど、
	//   それは renderer の層には追加回転メソッドしかなかったためで、
	//   こっちではカメラアングルベースの setter とかも生えてるので、
	//  「絶対的に角度をゼロに戻す」的な名前の方がしっくりくる気が。後々で要検討。
 	/**
 	 * Resets vertical/horizontal/screw angles to 0,
 	 * and resets the rotation of the graph to the state corresponding to the above angles.
 	 * 
 	 * Please note that, the state corresponding the above angles depends on the angle mode.
 	 */
	public synchronized void cancelRotaion() {

		// Reset the rotation matrix of the graph.
		this.resetMatrix();

		// Reset the values of camera angles.
	 	this.verticalAngle = 0.0;
	 	this.horizontalAngle = 0.0;
	 	this.screwAngle = 0.0;
	}


	/**
	 * Reset only the rotation matrix to the default state of the current angle mode
	 * (corresponding to the state under the all angles are 0),
	 * with keeping the values of vertical/horizontal/screw as they are.
	 */
	private void resetMatrix() {

		// Reset the rotation matrix of the graph.
		// (Don't call cancelRotation() here, because it resets vertical/horizontal/screw angles to 0.0.)
		switch (this.angleMode) {

			// Under X_ZENITH mode, X axis faces to the user,
			// Y axis faces to the right, and Z axis faces to the upside on the screen.
			// So its matrix is:
			case X_ZENITH : {
				this.rotationMatrix = new double[][] {
					{ 0.0, 1.0, 0.0 }, // ex_x  ey_x  ez_x
					{ 0.0, 0.0, 1.0 }, // ex_y  ey_y  ez_y
					{ 1.0, 0.0, 0.0 }  // ex_z  ey_z  ez_z
				};
				break;
			}

			// Under Y_ZENITH mode, Y axis faces to the user,
			// Z axis faces to the right, and X axis faces to the upside on the screen.
			// So its matrix is:
			case Y_ZENITH : {
				this.rotationMatrix = new double[][] {
					{ 0.0, 0.0, 1.0 }, // ex_x  ey_x  ez_x
					{ 1.0, 0.0, 0.0 }, // ex_y  ey_y  ez_y
					{ 0.0, 1.0, 0.0 }  // ex_z  ey_z  ez_z
				};
				break;
			}

			// Under Z_ZENITH mode, Z axis faces to the user,
			// X axis faces to the right, and Z axis faces to the upside on the screen.
			// So its matrix is:
			case Z_ZENITH : {
				this.rotationMatrix = new double[][] {
					{ 1.0, 0.0, 0.0 }, // ex_x  ey_x  ez_x
					{ 0.0, 1.0, 0.0 }, // ex_y  ey_y  ez_y
					{ 0.0, 0.0, 1.0 }  // ex_z  ey_z  ez_z
				};
				break;
			}
		}
	}


	/**
	 * Rotates the graph around the X-axis of the camera (view) coordinate.
	 * 
	 * For the sign of the rotation angle, 
	 * we define it as positive when a right-hand screw advances 
	 * towards the positive direction of the X-axis.
	 * The unit of the angle is radian.
	 * 
	 * @param angle The rotation angle.
	 */
	public synchronized void rotateAroundX(double angle) {
		double sin = sin(angle);
		double cos = cos(angle);

		// Create the rotation matrix around X axis: Rx.
		double[][] rx = {
				{ 1.0, 0.0, 0.0 },
				{ 0.0, cos,-sin },
				{ 0.0, sin, cos }
		};

		// Create a temporary matrix, for storing updated values of the rotation matrix of the graph.
		double[][] updatedMatrix = new double[3][3];

		// Act Rx to the rotation matrix of the graph.
		double[][] m = this.rotationMatrix;
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				updatedMatrix[i][j] = rx[i][0] * m[0][j] + rx[i][1] * m[1][j] + rx[i][2] * m[2][j];
			}
		}
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				this.rotationMatrix[i][j] = updatedMatrix[i][j];
			}
		}

		// Update values of camera angles.
		switch (this.angleMode) {
			case X_ZENITH: {
				this.computeXZenithAnglesFromMatrix();
				return;
			}
			case Y_ZENITH: {
				this.computeYZenithAnglesFromMatrix();
				return;
			}
			case Z_ZENITH: {
				this.computeZZenithAnglesFromMatrix();
				return;
			}
			default: {
				throw new RuntimeException("Unexpected angle mode: " + this.angleMode);
			}
		}
	}


	/**
	 * Rotates the graph around the Y-axis of the camera (view) coordinate.
	 * 
	 * For the sign of the rotation angle, 
	 * we define it as positive when a right-hand screw advances 
	 * towards the positive direction of the Y-axis.
	 * The unit of the angle is radian.
	 * 
	 * @param angle The rotation angle.
	 */
	public synchronized void rotateAroundY(double angle) {
		double sin = sin(angle);
		double cos = cos(angle);

		// Create the rotation matrix around Y axis: Ry.
		double[][] ry = {
				{ cos, 0.0, sin },
				{ 0.0, 1.0, 0.0 },
				{ -sin,0.0, cos }
		};

		// Create a matrix for temporary storing updated values of the transformation matrix.
		double[][] updatedMatrix = new double[3][3];

		// Act Ry to the rotation matrix of the graph.
		double[][] m = this.rotationMatrix;
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				updatedMatrix[i][j] = ry[i][0] * m[0][j] + ry[i][1] * m[1][j] + ry[i][2] * m[2][j];
			}
		}
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				this.rotationMatrix[i][j] = updatedMatrix[i][j];
			}
		}

		// Update values of camera angles.
		switch (this.angleMode) {
			case X_ZENITH: {
				this.computeXZenithAnglesFromMatrix();
				return;
			}
			case Y_ZENITH: {
				this.computeYZenithAnglesFromMatrix();
				return;
			}
			case Z_ZENITH: {
				this.computeZZenithAnglesFromMatrix();
				return;
			}
			default: {
				throw new RuntimeException("Unexpected angle mode: " + this.angleMode);
			}
		}
	}


	/**
	 * Rotates the graph around the Z-axis of the camera (view) coordinate.
	 * 
	 * For the sign of the rotation angle, 
	 * we define it as positive when a right-hand screw advances 
	 * towards the positive direction of the Z-axis.
	 * The unit of the angle is radian.
	 * 
	 * @param angle The rotation angle.
	 */
	public synchronized void rotateAroundZ(double angle) {
		double sin = sin(angle);
		double cos = cos(angle);

		// Create the rotation matrix around Z axis: Rz.
		double[][] rz = {
				{ cos,-sin, 0.0 },
				{ sin, cos, 0.0 },
				{ 0.0, 0.0, 1.0 }
		};

		// Create a matrix for temporary storing updated values of the transformation matrix.
		double[][] updatedMatrix = new double[3][3];

		// Act Rz to the rotation matrix of the graph.
		double[][] m = this.rotationMatrix;
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				updatedMatrix[i][j] = rz[i][0] * m[0][j] + rz[i][1] * m[1][j] + rz[i][2] * m[2][j];
			}
		}
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				this.rotationMatrix[i][j] = updatedMatrix[i][j];
			}
		}

		// Update values of camera angles.
		switch (this.angleMode) {
			case X_ZENITH: {
				this.computeXZenithAnglesFromMatrix();
				return;
			}
			case Y_ZENITH: {
				this.computeYZenithAnglesFromMatrix();
				return;
			}
			case Z_ZENITH: {
				this.computeZZenithAnglesFromMatrix();
				return;
			}
			default: {
				throw new RuntimeException("Unexpected angle mode: " + this.angleMode);
			}
		}
	}


	/**
	 * Sets the angle mode, for switching how specify the camera angle(s).
	 * 
	 * Note that, when the angle mode is changed (by calling this method),
	 * all camera angles are reset to 0.
	 * 
	 * @param angleMode The angle mode.
	 */
	public synchronized void setAngleMode(AngleMode angleMode) {
		this.angleMode = angleMode;
		this.cancelRotaion();
	}


	/**
	 * Gets the angle mode.
	 * 
	 * @return The angle mode.
	 */
	public synchronized AngleMode getAngleMode() {
		return this.angleMode;
	}


	/**
	 * Sets the vertical angle,
	 * which is the angle between the zenith axis and the direction toward the camera.
	 * 
	 * @param verticalAngle The vertical angle.
	 */
	public synchronized void setVerticalAngle(double verticalAngle) {
		this.verticalAngle = verticalAngle;

		// Update the rotation matrix from vertical/horizontal/screw angles.
		this.computeMatrixFromZenithAngles();
	}


	/**
	 * Gets the vertical angle,
	 * which is the angle between the zenith axis and the direction toward the camera.
	 * 
	 * @return The verticcal angle.
	 */
	public synchronized double getVerticalAngle() {
		return this.verticalAngle;
	}


	/**
	 * Sets the horizontal angle,
	 * which is the rotation angle of the camera's location around the zenith axis.
	 * 
	 * @param horizontalAngle The horizontal angle.
	 */
	public synchronized void setHorizontalAngle(double horizontalAngle) {
		this.horizontalAngle = horizontalAngle;

		// Update the rotation matrix from vertical/horizontal/screw angles.
		this.computeMatrixFromZenithAngles();
	}


	/**
	 * Gets the horizontal angle,
	 * which is the rotation angle of the camera's location around the zenith axis.
	 * 
	 * @return The horizontal angle.
	 */
	public synchronized double getHorizontalAngle() {
		return this.horizontalAngle;
	}


	/**
	 * Sets the screw angle,
	 * which is the rotation angle of the camera itself (not location) around the screen center.
	 * 
	 * @param screwAngle The screw angle.
	 */
	public synchronized void setScrewAngle(double screwAngle) {
		this.screwAngle = screwAngle;

		// Update the rotation matrix from vertical/horizontal/screw angles.
		this.computeMatrixFromZenithAngles();
	}


	/**
	 * Gets the screw angle,
	 * which is the rotation angle of the camera itself (not location) around the screen center.
	 * 
	 * @return The screw angle.
	 */
	public synchronized double getScrewAngle() {
		return this.screwAngle;
	}


	/**
	 * Shifts the value of the specified angle, into the range [0, 2pi].
	 * 
	 * @param angle The angle to be shifted.
	 * @return The shifted angle.
	 */
	private double shiftAngleFrom0To2Pi(double angle) {
		double shiftedAngle = angle;
		double pi2 = 2.0 * PI;
		while (shiftedAngle < 0) {
			shiftedAngle += pi2;
		}
		while (pi2 < shiftedAngle) {
			shiftedAngle -= pi2;
		}
		return shiftedAngle;
	}


	/**
	 * From the current rotation matrix,
	 * computes the values of vertical/horizontal/screw angles, regarding X axis as the zenith axis.
	 */
	private void computeXZenithAnglesFromMatrix() {

		// From the rotation matrix, extract vectors of X/Y/Z axes of the graph.
		double[][] m = this.rotationMatrix;
		double[] graphXAxis = new double[] { m[X][X], m[Y][X], m[Z][X] };
		double[] graphYAxis = new double[] { m[X][Y], m[Y][Y], m[Z][Y] };
		double[] graphZAxis = new double[] { m[X][Z], m[Y][Z], m[Z][Z] };

		// If X axis of the graph is parallel with the Z axis of the camera, we can compute angles easily.
		if (Math.abs(graphXAxis[X]) < 1.0E-10 && Math.abs(graphXAxis[Y]) < 1.0E-10) {

			// When X axis faces to the user.
			if (0 < graphXAxis[Z]) {
				this.verticalAngle = 0.0;
				this.screwAngle = 0.0;
				this.horizontalAngle = 0.5 * PI - atan2(graphZAxis[Y], graphZAxis[X]); // Don't use Y axis here. Otherwise it does not work under the situation that vertical angle = pi.
				this.horizontalAngle = this.shiftAngleFrom0To2Pi(this.horizontalAngle);

			// When X axis faces to the depth-direction of the screen.
			} else {
				this.verticalAngle = PI;
				this.screwAngle = 0.0;
				this.horizontalAngle = atan2(graphZAxis[Y], graphZAxis[X]) - 1.5 * Math.PI; // Don't use Y axis here. Otherwise it does not work under the situation that vertical angle = pi.
				this.horizontalAngle = this.shiftAngleFrom0To2Pi(this.horizontalAngle); 
			}
			return;
		}

		// The vertical angle, which is the angle between two X axes of the camera and the graph,
		// is independent of the horizontal angle and the screw angle.
		// So determine the vertical angle at first.
		double innerProductBetweenCameraXandGraphX = graphXAxis[Z];
		this.verticalAngle = acos(innerProductBetweenCameraXandGraphX);

		// Next, consider about the projected vector of the graph's X axis to the screen.
		// Its direction is independent of the horizontal angle, but dependent on the screw angle.
		// So we can determine the screw angle from the projected vector.
		double[] graphXAxisOnScreen = new double[] { graphXAxis[X], graphXAxis[Y] };
		this.screwAngle = 0.5 * PI - atan2(graphXAxisOnScreen[Y], graphXAxisOnScreen[X]);
		this.screwAngle = this.shiftAngleFrom0To2Pi(this.screwAngle);

		// If the horizontal angle is 0, Y axis of the graph is parallel with the screen.
		// Call it as "graphYDash". We can compute its vector component from the screw angle, as follows:
		double[] graphYDash = new double[] { cos(this.screwAngle), -sin(this.screwAngle), 0.0 };

		// Call the Z axis of the graph under the same situation as "graphZDash".
		// We can compute it as the cross product vector between graphXAxis and graphYDash:
		double[] graphZDash = new double[] {
				graphXAxis[Y] * graphYDash[Z] - graphXAxis[Z] * graphYDash[Y],
				graphXAxis[Z] * graphYDash[X] - graphXAxis[X] * graphYDash[Z],
				graphXAxis[X] * graphYDash[Y] - graphXAxis[Y] * graphYDash[X]
		};

		// Then, consider the 2-D coordinate system of which X and Y axes is graphYDash and graphZDash.
		// On the above coordinate, the vector components (ux, uy) of actual Y axis (not dash) of the graph
		// can be expressed as inner products with graphYDash for ux, and graphZDash for uy:
		double ux = graphYAxis[X] * graphYDash[X] + graphYAxis[Y] * graphYDash[Y] + graphYAxis[Z] * graphYDash[Z];
		double uy = graphYAxis[X] * graphZDash[X] + graphYAxis[Y] * graphZDash[Y] + graphYAxis[Z] * graphZDash[Z];

		// Finally, we can determine horizontal value from ux and uy:
		//this.horizontalAngle = atan2(ux, uy);
		this.horizontalAngle = -atan2(uy, ux);
		this.horizontalAngle = this.shiftAngleFrom0To2Pi(this.horizontalAngle);
	}


	/**
	 * From the current rotation matrix,
	 * computes the values of vertical/horizontal/screw angles, regarding X axis as the zenith axis.
	 */
	private void computeYZenithAnglesFromMatrix() {

		// From the rotation matrix, extract vectors of X/Y/Z axes of the graph.
		double[][] m = this.rotationMatrix;
		double[] graphXAxis = new double[] { m[X][X], m[Y][X], m[Z][X] };
		double[] graphYAxis = new double[] { m[X][Y], m[Y][Y], m[Z][Y] };
		double[] graphZAxis = new double[] { m[X][Z], m[Y][Z], m[Z][Z] };

		// If Y axis of the graph is parallel with the Z axis of the camera, we can compute angles easily.
		if (Math.abs(graphYAxis[X]) < 1.0E-10 && Math.abs(graphYAxis[Y]) < 1.0E-10) {

			// When Y axis faces to the user.
			if (0 < graphYAxis[Z]) {
				this.verticalAngle = 0.0;
				this.screwAngle = 0.0;
				// Don't use Z axis here. Otherwise it does not work under the situation that vertical angle = pi.
				this.horizontalAngle = 0.5 * PI - atan2(graphXAxis[Y], graphXAxis[X]);
				this.horizontalAngle = this.shiftAngleFrom0To2Pi(this.horizontalAngle);

			// When Y axis faces to the depth-direction of the screen.
			} else {
				this.verticalAngle = PI;
				this.screwAngle = 0.0;
				// Don't use Z axis here. Otherwise it does not work under the situation that vertical angle = pi.
				this.horizontalAngle = atan2(graphXAxis[Y], graphXAxis[X]) - 1.5 * Math.PI;
				this.horizontalAngle = this.shiftAngleFrom0To2Pi(this.horizontalAngle); 
			}
			return;
		}

		// The vertical angle, which is the angle between two Y axes of the camera and the graph,
		// is independent of the horizontal angle and the screw angle.
		// So determine the vertical angle at first.
		double innerProductBetweenCameraYandGraphY = graphYAxis[Z];
		this.verticalAngle = acos(innerProductBetweenCameraYandGraphY);

		// Next, consider about the projected vector of the graph's Y axis to the screen.
		// Its direction is independent of the horizontal angle, but dependent on the screw angle.
		// So we can determine the screw angle from the projected vector.
		double[] graphYAxisOnScreen = new double[] { graphYAxis[X], graphYAxis[Y] };
		this.screwAngle = 0.5 * PI - atan2(graphYAxisOnScreen[Y], graphYAxisOnScreen[X]);
		this.screwAngle = this.shiftAngleFrom0To2Pi(this.screwAngle);

		// If the horizontal angle is 0, Z axis of the graph is parallel with the screen.
		// Call it as "graphZDash". We can compute its vector component from the screw angle, as follows:
		double[] graphZDash = new double[] { cos(this.screwAngle), -sin(this.screwAngle), 0.0 };

		// Call the X axis of the graph under the same situation as "graphXDash".
		// We can compute it as the cross product vector between graphYAxis and graphZDash:
		double[] graphXDash = new double[] {
				graphYAxis[Y] * graphZDash[Z] - graphYAxis[Z] * graphZDash[Y],
				graphYAxis[Z] * graphZDash[X] - graphYAxis[X] * graphZDash[Z],
				graphYAxis[X] * graphZDash[Y] - graphYAxis[Y] * graphZDash[X]
		};

		// Then, consider the 2-D coordinate system of which X and Y axes is graphYDash and graphZDash.
		// On the above coordinate, the vector components (ux, uy) of actual Z axis (not dash) of the graph
		// can be expressed as inner products with graphZDash for ux, and graphXDash for uy:
		double ux = graphZAxis[X] * graphZDash[X] + graphZAxis[Y] * graphZDash[Y] + graphZAxis[Z] * graphZDash[Z];
		double uy = graphZAxis[X] * graphXDash[X] + graphZAxis[Y] * graphXDash[Y] + graphZAxis[Z] * graphXDash[Z];

		// Finally, we can determine horizontal value from ux and uy:
		//this.horizontalAngle = atan2(ux, uy);
		this.horizontalAngle = -atan2(uy, ux);
		this.horizontalAngle = this.shiftAngleFrom0To2Pi(this.horizontalAngle);
	}


	/**
	 * From the current rotation matrix,
	 * computes the values of vertical/horizontal/screw angles, regarding Z axis as the zenith axis.
	 */
	private void computeZZenithAnglesFromMatrix() {

		// From the rotation matrix, extract vectors of X/Y/Z axes of the graph.
		double[][] m = this.rotationMatrix;
		double[] graphXAxis = new double[] { m[X][X], m[Y][X], m[Z][X] };
		double[] graphYAxis = new double[] { m[X][Y], m[Y][Y], m[Z][Y] };
		double[] graphZAxis = new double[] { m[X][Z], m[Y][Z], m[Z][Z] };

		// If Z axis of the graph is parallel with the Z axis of the camera, we can compute angles easily.
		if (Math.abs(graphZAxis[X]) < 1.0E-10 && Math.abs(graphZAxis[Y]) < 1.0E-10) {

			// When Z axis faces to the user.
			if (0 < graphZAxis[Z]) {
				this.verticalAngle = 0.0;
				this.screwAngle = 0.0;
				this.horizontalAngle = 0.5 * PI - atan2(graphYAxis[Y], graphYAxis[X]); // Don't use X axis here. Otherwise it does not work under the situation that vertical angle = pi.
				this.horizontalAngle = this.shiftAngleFrom0To2Pi(this.horizontalAngle);

			// When Z axis faces to the depth-direction of the screen.
			} else {
				this.verticalAngle = PI;
				this.screwAngle = 0.0;
				this.horizontalAngle = atan2(graphYAxis[Y], graphYAxis[X]) - 1.5 * Math.PI; // Don't use X axis here. Otherwise it does not work under the situation that vertical angle = pi.
				this.horizontalAngle = this.shiftAngleFrom0To2Pi(this.horizontalAngle);
				
			}
			return;
		}

		// The vertical angle, which is the angle between two Z axes of the camera and the graph,
		// is independent of the horizontal angle and the screw angle.
		// So determine the vertical angle at first.
		double innerProductBetweenCameraZandGraphZ = graphZAxis[Z];
		this.verticalAngle = acos(innerProductBetweenCameraZandGraphZ);

		// Next, consider about the projected vector of the graph's Z axis to the screen.
		// Its direction is independent of the horizontal angle, but dependent on the screw angle.
		// So we can determine the screw angle from the projected vector.
		double[] graphZAxisOnScreen = new double[] { graphZAxis[X], graphZAxis[Y] };
		this.screwAngle = 0.5 * PI - atan2(graphZAxisOnScreen[Y], graphZAxisOnScreen[X]);
		this.screwAngle = this.shiftAngleFrom0To2Pi(this.screwAngle);

		// If the horizontal angle is 0, X axis of the graph is parallel with the screen.
		// Call it as "graphXDash". We can compute its vector component from the screw angle, as follows:
		double[] graphXDash = new double[] { cos(this.screwAngle), -sin(this.screwAngle), 0.0 };

		// Call the Y axis of the graph under the same situation as "graphYDash".
		// We can compute it as the cross product vector between graphZAxis and graphXDash:
		double[] graphYDash = new double[] {
				graphZAxis[Y] * graphXDash[Z] - graphZAxis[Z] * graphXDash[Y],
				graphZAxis[Z] * graphXDash[X] - graphZAxis[X] * graphXDash[Z],
				graphZAxis[X] * graphXDash[Y] - graphZAxis[Y] * graphXDash[X]
		};

		// Then, consider the 2-D coordinate system of which X and Y axes is graphXDash and graphYDash.
		// On the above coordinate, the vector components (ux, uy) of actual X axis (not dash) of the graph
		// can be expressed as inner products with graphXDash for ux, and graphYDash for uy:
		double ux = graphXAxis[X] * graphXDash[X] + graphXAxis[Y] * graphXDash[Y] + graphXAxis[Z] * graphXDash[Z];
		double uy = graphXAxis[X] * graphYDash[X] + graphXAxis[Y] * graphYDash[Y] + graphXAxis[Z] * graphYDash[Z];

		// Finally, we can determine horizontal value from ux and uy:
		//this.horizontalAngle = atan2(ux, uy);
		this.horizontalAngle = -atan2(uy, ux);
		this.horizontalAngle = this.shiftAngleFrom0To2Pi(this.horizontalAngle);
	}


	/**
	 * Computes the rotation matrix from the values of vertical/horizontal/screw angles,
	 * regarding X/Y/Z axis as the zenith axis, specified to the "angleMode" field.
	 */
	private void computeMatrixFromZenithAngles() {

		// Variables for storing values of sin(angle) and cos(angle) functions temporary.
		double sin;
		double cos;

		// Reset the rotation matrix of the graph.
		this.resetMatrix();

	 	// Define a short alias of the rotation matrix.
	 	double[][] m = this.rotationMatrix;

		// Create a temporary matrix, for storing updated values of the rotation matrix of the graph.
		double[][] updatedMatrix = new double[3][3];

		// The rotation by horizontal angle can be expressed as
		// rotation around Z axis OF CAMERA/VIEW COORDINATE SYSTEM (not the graph's axis).
		// Call its matrix as "Rh":
		sin = sin(-this.horizontalAngle); // Be careful of the direction of the rotation.
		cos = cos(-this.horizontalAngle);
		double[][] rh = {
				{ cos,-sin, 0.0 },
				{ sin, cos, 0.0 },
				{ 0.0, 0.0, 1.0 }
		};

		// Act Rh to the rotation matrix of the graph.
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				updatedMatrix[i][j] = rh[i][0] * m[0][j] + rh[i][1] * m[1][j] + rh[i][2] * m[2][j];
			}
		}
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				m[i][j] = updatedMatrix[i][j];
			}
		}

		// The rotation by vertical angle can be expressed as
		// rotation around X axis OF CAMERA/VIEW COORDINATE SYSTEM (not the graph's axis).
		// Call its matrix as "Rh":
		sin = sin(-this.verticalAngle); // Be careful of the direction of the rotation.
		cos = cos(-this.verticalAngle);
		double[][] rv = {
				{ 1.0, 0.0, 0.0 },
				{ 0.0, cos,-sin },
				{ 0.0, sin, cos }
		};

		// Act Rv to the rotation matrix of the graph.
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				updatedMatrix[i][j] = rv[i][0] * m[0][j] + rv[i][1] * m[1][j] + rv[i][2] * m[2][j];
			}
		}
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				m[i][j] = updatedMatrix[i][j];
			}
		}

		// The rotation by screw angle can be expressed as
		// re-rotation around Z axis OF CAMERA/VIEW COORDINATE SYSTEM (not the graph's axis).
		// Call its matrix as "Rh":
		sin = sin(-this.screwAngle); // Be careful of the direction of the rotation.
		cos = cos(-this.screwAngle);
		double[][] rs = {
				{ cos,-sin, 0.0 },
				{ sin, cos, 0.0 },
				{ 0.0, 0.0, 1.0 }
		};

		// Act Rs to the rotation matrix of the graph.
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				updatedMatrix[i][j] = rs[i][0] * m[0][j] + rs[i][1] * m[1][j] + rs[i][2] * m[2][j];
			}
		}
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				m[i][j] = updatedMatrix[i][j];
			}
		}
	}
}
