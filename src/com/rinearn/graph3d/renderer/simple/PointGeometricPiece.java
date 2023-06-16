package com.rinearn.graph3d.renderer.simple;

import java.awt.Graphics2D;


/**
 * A geometric piece class representing a point.
 */
public class PointGeometricPiece extends GeometricPiece {

	/**
	 * Transforms the coordinate values of this point.
	 * 
	 * @param matrix The transformation matrix.
	 */
	@Override
	public void transform(double[][] matrix) {

		// Short aliases of the matrix and vertices arrays.
		double[][] m = matrix;
		double[] sv = this.scaledVertexArray[0];
		double[] tv = this.transformedVertexArray[0];

		// Transform vertices, where X=0, Y=1, and Z=2.
		tv[X] = m[0][0] * sv[X] + m[0][1] * sv[Y] + m[0][2] * sv[Z] + m[0][3];
		tv[Y] = m[1][0] * sv[X] + m[1][1] * sv[Y] + m[1][2] * sv[Z] + m[1][3];
		tv[Z] = m[2][0] * sv[X] + m[2][1] * sv[Y] + m[2][2] * sv[Z] + m[2][3];

		// Compute the square of the 'depth' value.
		this.depthSquaredValue = tv[Z] * tv[Z];
	}


	/**
	 * Shades the color.
	 */
	@Override
	public void shade() {

		// Points have no shades, so simply copy the original color as it is.
		this.onscreenColor = this.originalColor;
	}


	/**
	 * Computes the projected screen coordinate values of this point.
	 * 
	 * @param screenWidth The width (pixels) of the screen.
	 * @param screenHeight The height (pixels) of the screen.
	 * @param screenOffsetX The X-offset value (positive for shifting rightward) of the screen center.
	 * @param screenOffsetY The Y-offset value (positive for shifting upward) of the screen center.
	 */
	@Override
	public void project(int screenWidth, int screenHeight, int screenOffsetX, int screenOffsetY) {

		// Short aliases of the vertices arrays.
		double[] tv = this.transformedVertexArray[0];
		int[] pv = this.projectedVertexArray[0];

		// Compute the project coordinates on the screen.
		// (The origin is the left-top edge of the screen.)
		int screenCenterX = screenWidth >> 1 + screenOffsetX; // bit-shifting instead of dividing by 2.
		int screenCenterY = screenHeight >> 1 - screenOffsetY;
		double recipZ = 1.0 / tv[Z];
		pv[X] = screenCenterX + (int)(tv[X] * recipZ);
		pv[Y] = screenCenterY - (int)(tv[Y] * recipZ);
	}


	/**
	 * Draws this point.
	 * 
	 * @param graphics The Graphics2D instance for drawing shapes to the screen image.
	 */
	@Override
	public void draw(Graphics2D graphics) {
		throw new RuntimeException("Unimplemented");
	}
}
