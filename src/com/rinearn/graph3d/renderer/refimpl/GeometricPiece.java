package com.rinearn.graph3d.renderer.refimpl;

import java.awt.Color;
import java.awt.Graphics2D;

import com.rinearn.graph3d.config.light.LightConfiguration;


/**
 * The base class of each piece class which represents
 * a piece of the specific geometric shape (point, line, triangle, and so on).
 */
public abstract class GeometricPiece {

	/** The array index at which a X coordinate value is stored. */
	public static final int X = 0;

	/** The array index at which a Y coordinate value is stored. */
	public static final int Y = 1;

	/** The array index at which a Z coordinate value is stored. */
	public static final int Z = 2;

	/** The array index at which a W coordinate value is stored. */
	public static final int W = 3;

	/** Represents the number of the vertices stored in the vertex array. */
	protected int vertexCount = -1;

	/** Stores the coordinate values of the vertices, in the scaled space. */
	protected double[][] scaledVertexArray = null;

	/** Stores the transformed coordinate values of the vertices. */
	protected double[][] transformedVertexArray = null;

	/** Stores the projected coordinate values of the vertices. */
	protected int[][] projectedVertexArray = null;

	/** Represents the original (unmodified) color of this piece. */
	protected Color originalColor = null;

	/** Represents the color on the screen, computed by the shading process. */
	protected Color onscreenColor = null;

	/** The square of the 'depth' value of this piece. See also the comment of the getter method. */
	protected double depthSquaredValue = Double.NaN;


	/**
	 * Transforms the coordinate values of the vertices.
	 *
	 * @param positionalTransformMatrix The matrix to transform positions, e.g.: vertex coordinates.
	 * @param directionalTransformMatrix The matrix to transform directional vectors, e.g.: normal vectors of surfaces.
	 */
	public abstract void transform(double[][] positionalTransformMatrix, double[][] directionalTransformMatrix);


	/**
	 * Shades the color.
	 *
	 * @param lightConfig The object storing parameters for lighting and shading.
	 */
	public abstract void shade(LightConfiguration lightConfig);


	/**
	 * Computes the projected screen coordinate values of the vertices.
	 *
	 * @param screenWidth The width (pixels) of the screen.
	 * @param screenHeight The height (pixels) of the screen.
	 * @param screenOffsetX The X-offset value (positive for shifting rightward) of the screen center.
	 * @param screenOffsetY The Y-offset value (positive for shifting upward) of the screen center.
	 * @param magnification The magnification of the conversion from lengths in 3D space to pixels.
	 */
	public abstract void project(int screenWidth, int screenHeight, int screenOffsetX, int screenOffsetY, double magnification);


	/**
	 * Draws this piece.
	 *
	 * @param graphics The Graphics2D instance for drawing shapes to the screen image.
	 */
	public abstract void draw(Graphics2D graphics);


	/**
	 * Updates the directional vectors, e.g. normal vectors of QuadrangleGeometricPieces.
	 *
	 * Some types of directional vectors must be re-computed when the length factors of X/Y/Z dimensions are changed,
	 * so this method is called on such time.
	 *
	 * @param xLengthFactor The length factor for X dimension.
	 * @param yLengthFactor The length factor for Y dimension.
	 * @param zLengthFactor The length factor for Z dimension.
	 */
	public void updateDirectionalVectors(double xLengthFactor, double yLengthFactor, double zLengthFactos) {
		// Do nothing if the subclass has no directional vectors (e.g. normal vectors).
	}


	/**
	 * Returns the square of the 'depth' value of this piece.
	 *
	 * The 'depth' value represents how far the geometric piece is,
	 * from the screen surface, in the depth direction (perpendicular to the screen surface).
	 *
	 * The square of the 'depth' value is computed in 'transform' method, and is stored to the field.
	 * This method returns the stored value.
	 */
	public double getDepthSquaredValue() {
		return this.depthSquaredValue;
	}


	/**
	 * Returns whether antialiasing is available for drawing this piece.
	 *
	 * Antialiasing can be cause of noise for drawing some kind of pieces, e.g.: conterminous multiple quadrangles.
	 * Such pieces returns false for the return value of this method.
	 *
	 * @return Return true if antialiasing is available.
	 */
	public boolean isAntialiasingAvailable() {
		return true;
	}
}
