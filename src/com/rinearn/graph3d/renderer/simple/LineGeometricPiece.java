package com.rinearn.graph3d.renderer.simple;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.BasicStroke;

/**
 * A geometric piece class representing a line.
 */
public class LineGeometricPiece extends GeometricPiece {

	/** The stroke for drawing this line. */
	private Stroke stroke;

	/**
	 * Creates a new geometric piece representing a line between point A and point B.
	 * 
	 * @param aX The x coordinate value of the point A, in the scaled space.
	 * @param aY The y coordinate value of the point A, in the scaled space.
	 * @param aZ The z coordinate value of the point A, in the scaled space.
	 * @param bX The x coordinate value of the point B, in the scaled space.
	 * @param bY The y coordinate value of the point B, in the scaled space.
	 * @param bZ The z coordinate value of the point B, in the scaled space.
	 * @param z The z coordinate value of the center of the point, in the scaled space.
	 * @param radius The radius (pixels) of the point.
	 * @param color The color of the point.
	 */
	public LineGeometricPiece(double aX, double aY, double aZ, double bX, double bY, double bZ, double width, Color color) {
		this.vertexCount = 2;
		this.scaledVertexArray = new double[][] {{ aX, aY, aZ }, {bX, bY, bZ}};
		this.transformedVertexArray = new double[this.vertexCount][3]; // [3] is X/Y/Z
		this.projectedVertexArray = new int[this.vertexCount][2];      // [2] is X/Y
		this.stroke = new BasicStroke((float)width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
		this.originalColor = color;
	}


	/**
	 * Transforms the coordinate values of this point.
	 * 
	 * @param matrix The transformation matrix.
	 */
	@Override
	public void transform(double[][] matrix) {

		// Short aliases of the matrix and vertices arrays.
		double[][] m = matrix;
		double[][] sv = this.scaledVertexArray;
		double[][] tv = this.transformedVertexArray;

		// Transform each vertex.
		for (int ivertex=0; ivertex<this.vertexCount; ivertex++) {

			// Transform coordinate values of the vertex, where X=0, Y=1, and Z=2.
			tv[ivertex][X] = m[0][0] * sv[ivertex][X] + m[0][1] * sv[ivertex][Y] + m[0][2] * sv[ivertex][Z] + m[0][3];
			tv[ivertex][Y] = m[1][0] * sv[ivertex][X] + m[1][1] * sv[ivertex][Y] + m[1][2] * sv[ivertex][Z] + m[1][3];
			tv[ivertex][Z] = m[2][0] * sv[ivertex][X] + m[2][1] * sv[ivertex][Y] + m[2][2] * sv[ivertex][Z] + m[2][3];
		}

		// Compute the square of the 'depth' value.
		double meanZ = (tv[0][Z] + tv[1][Z]) * 0.5;
		this.depthSquaredValue = meanZ * meanZ;
	}


	/**
	 * Shades the color.
	 */
	@Override
	public void shade() {

		// Lines have no shades, so simply copy the original color as it is.
		this.onscreenColor = this.originalColor;
	}


	/**
	 * Computes the projected screen coordinate values of this point.
	 * 
	 * @param screenWidth The width (pixels) of the screen.
	 * @param screenHeight The height (pixels) of the screen.
	 * @param screenOffsetX The X-offset value (positive for shifting rightward) of the screen center.
	 * @param screenOffsetY The Y-offset value (positive for shifting upward) of the screen center.
	 * @param magnification The magnification of the conversion from lengths in 3D space to pixels.
	 */
	@Override
	public void project(int screenWidth, int screenHeight, int screenOffsetX, int screenOffsetY, double magnification) {

		// Compute the project coordinates on the screen.
		// (The origin is the left-top edge of the screen.)
		int screenCenterX = screenWidth >> 1 + screenOffsetX; // bit-shifting instead of dividing by 2.
		int screenCenterY = screenHeight >> 1 - screenOffsetY;

		// Short aliases of the vertices arrays.
		double[][] tv = this.transformedVertexArray;
		int[][] pv = this.projectedVertexArray;

		// Project each vertex.
		for (int ivertex=0; ivertex<this.vertexCount; ivertex++) {
			double projectionRatio = magnification / -tv[ivertex][Z]; // Z takes a negative value for the depth direction.
			pv[ivertex][X] = screenCenterX + (int)(tv[ivertex][X] * projectionRatio);
			pv[ivertex][Y] = screenCenterY - (int)(tv[ivertex][Y] * projectionRatio);
		}
	}


	/**
	 * Draws this line.
	 * 
	 * @param graphics The Graphics2D instance for drawing shapes to the screen image.
	 */
	@Override
	public void draw(Graphics2D graphics) {
		graphics.setColor(this.onscreenColor);
		graphics.setStroke(this.stroke);

		int[][] pv = this.projectedVertexArray;
		graphics.drawLine(pv[0][X], pv[0][Y], pv[1][X], pv[1][Y]);
	}
}
