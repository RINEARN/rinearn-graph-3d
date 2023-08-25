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
 	 * Gets the matrix representing the rotation of the graph to the current state from the initial state.
 	 * 
 	 * @return The matrix representing the rotation of the graph.
 	 */
	public synchronized double[][] getRotationMatrix() {

		// Note: Probably we should not provide the setter for the rotation matrix.
		return this.rotationMatrix;
	}
 

 	/**
 	 * Resets the angle of the graph to the initial state.
 	 */
	public synchronized void cancelRotaion() {
	 	this.rotationMatrix = new double[][] {
	 		{ 1.0, 0.0, 0.0 },
	 		{ 0.0, 1.0, 0.0 },
	 		{ 0.0, 0.0, 1.0 }
	 	};
	 	this.verticalAngle = 0.0;
	 	this.horizontalAngle = 0.0;
	 	this.screwAngle = 0.0;
	}


	/**
	 * Rotates the graph around the X-axis.
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
	 * Rotates the graph around the Y-axis.
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
	 * Rotates the graph around the Z-axis.
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
	 * @param angleMode The angle mode.
	 */
	public synchronized void setAngleMode(AngleMode angleMode) {
		this.angleMode = angleMode;
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

		// Update the rotation matrix.
		switch (this.angleMode) {
			case Z_ZENITH: {
				// this.computeMatrixFromZZenithAngles(); // To be implemented
				return;
			}
			default: {
				throw new RuntimeException("Unexpected angle mode: " + this.angleMode);
			}
		}
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

		// Update the rotation matrix.
		switch (this.angleMode) {
			case Z_ZENITH: {
				// this.computeMatrixFromZZenithAngles(); // To be implemented
				return;
			}
			default: {
				throw new RuntimeException("Unexpected angle mode: " + this.angleMode);
			}
		}
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

		// Update the rotation matrix.
		switch (this.angleMode) {
			case Z_ZENITH: {
				// this.computeMatrixFromZZenithAngles(); // To be implemented
				return;
			}
			default: {
				throw new RuntimeException("Unexpected angle mode: " + this.angleMode);
			}
		}
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
	 * Dumps the values of vertical/horizontal/screw angles, for debugging.
	 */
	public synchronized void dumpCameraAngles() {
		System.out.println("----- DUMP CAMERA ANGLES -----");
		System.out.println(" mode       = " + this.angleMode);
		System.out.println(" vertical   = " + this.verticalAngle);
		System.out.println(" horizontal = " + this.horizontalAngle);
		System.out.println(" screw      = " + this.screwAngle);
		System.out.println("------------------------------");
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
				this.horizontalAngle = atan2(graphYAxis[Y], graphYAxis[X]) - 1.5 * Math.PI;
				this.horizontalAngle = this.shiftAngleFrom0To2Pi(this.horizontalAngle); // Don't use X axis here. Otherwise it does not work under the situation that vertical angle = pi.
				
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
		double[] graphXDash = new double[] { Math.cos(this.screwAngle), Math.sin(this.screwAngle), 0.0 };

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
}
