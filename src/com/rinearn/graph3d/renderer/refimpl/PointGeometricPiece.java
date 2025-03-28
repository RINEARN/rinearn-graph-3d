package com.rinearn.graph3d.renderer.refimpl;

import java.awt.Color;
import java.awt.Graphics2D;

import com.rinearn.graph3d.config.light.LightConfiguration;


/**
 * A geometric piece class representing a point.
 */
public final class PointGeometricPiece extends GeometricPiece {

	/** The radius of this point, in the unit of pixels. */
	private int radius;

	/**
	 * The diameter (pixels) of this point, in the unit of pixels.
	 * May not match with 2 * radius, because the original radius is the floating point number.
	 */
	private int diameter;


	/**
	 * Creates a new geometric piece representing a point.
	 *
	 * @param x The x coordinate value of the center of the point, in the scaled space.
	 * @param y The y coordinate value of the center of the point, in the scaled space.
	 * @param z The z coordinate value of the center of the point, in the scaled space.
	 * @param radius The radius (pixels) of the point.
	 * @param color The color of the point.
	 */
	public PointGeometricPiece(double x, double y, double z, double radius, Color color) {
		this.vertexCount = 1;
		this.scaledVertexArray = new double[][] {{ x, y, z }};
		this.transformedVertexArray = new double[this.vertexCount][3]; // [3] is X/Y/Z
		this.projectedVertexArray = new int[this.vertexCount][2];      // [2] is X/Y
		this.originalColor = color;

		this.radius = (int)Math.round(radius);
		this.diameter = (int)Math.round(radius * 2.0);
	}


	/**
	 * Transforms the coordinate values of this point.
	 *
	 * @param positionalTransformMatrix The matrix to transform positions, e.g.: vertex coordinates.
	 * @param directionalTransformMatrix The matrix to transform directional vectors, e.g.: normal vectors of surfaces.
	 */
	@Override
	public void transform(double[][] positionalTransformMatrix, double[][] directionalTransformMatrix) {

		// Short aliases of the matrix and vertices arrays.
		double[][] m = positionalTransformMatrix;
		double[] sv = this.scaledVertexArray[0];
		double[] tv = this.transformedVertexArray[0];

		// Transform the coordinate values, where X=0, Y=1, and Z=2.
		tv[X] = m[0][0] * sv[X] + m[0][1] * sv[Y] + m[0][2] * sv[Z] + m[0][3];
		tv[Y] = m[1][0] * sv[X] + m[1][1] * sv[Y] + m[1][2] * sv[Z] + m[1][3];
		tv[Z] = m[2][0] * sv[X] + m[2][1] * sv[Y] + m[2][2] * sv[Z] + m[2][3];

		// Compute the square of the 'depth' value.
		this.depthSquaredValue = tv[Z] * tv[Z];
	}


	/**
	 * Shades the color.
	 *
	 * @param lightConfig The object storing parameters for lighting and shading.
	 */
	@Override
	public void shade(LightConfiguration lightConfig) {

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
	 * @param magnification The magnification of the conversion from lengths in 3D space to pixels.
	 */
	@Override
	public void project(int screenWidth, int screenHeight, int screenOffsetX, int screenOffsetY, double magnification) {

		// Compute the project coordinates on the screen.
		// (The origin is the left-top edge of the screen.)
		int screenCenterX = (screenWidth >> 1) + screenOffsetX; // bit-shifting instead of dividing by 2.
		int screenCenterY = (screenHeight >> 1) - screenOffsetY;

		// Short aliases of the vertices arrays.
		double[] tv = this.transformedVertexArray[0];
		int[] pv = this.projectedVertexArray[0];

		double projectionRatio = magnification / -tv[Z]; // Z takes a negative value for the depth direction.
		pv[X] = screenCenterX + (int)(tv[X] * projectionRatio);
		pv[Y] = screenCenterY - (int)(tv[Y] * projectionRatio);
	}


	/**
	 * Draws this point.
	 *
	 * @param graphics The Graphics2D instance for drawing shapes to the screen image.
	 */
	@Override
	public void draw(Graphics2D graphics) {
		graphics.setColor(this.onscreenColor);

		int[] pv = this.projectedVertexArray[0];
		graphics.fillOval(pv[X] - this.radius, pv[Y] - this.radius, this.diameter, this.diameter);
	}
}
