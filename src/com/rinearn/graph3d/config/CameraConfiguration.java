package com.rinearn.graph3d.config;


/**
 * The class for storing configuration parameters of the camera.
 */
public class CameraConfiguration {

	/** The magnification of the graph screen. */
	private volatile double magnification = 700.0;

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
		double sin = Math.sin(angle);
		double cos = Math.cos(angle);

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
		double sin = Math.sin(angle);
		double cos = Math.cos(angle);

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
		double sin = Math.sin(angle);
		double cos = Math.cos(angle);

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
	}
}
