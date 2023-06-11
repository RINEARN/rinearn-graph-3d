package com.rinearn.graph3d.renderer;

import java.awt.Color;
import java.awt.Graphics2D;


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

	/** Represents the number of the vertices stored in the vertex array. */
	protected int vertexCount;

	/** Stores the coordinate values of the vertices, in the scaled space. */
	protected double[][] scaledVertexArray;

	/** Stores the transformed coordinate values of the vertices. */
	protected double[][] transformedVertexArray;

	/** Represents the original (unmodified) color of this piece. */
	protected Color originalColor;

	/** Represents the color on the screen, computed by the shading process. */
	protected Color screenColor;


	/**
	 * Transforms the coordinate values of the vertices.
	 * 
	 * @param matrix The transformation matrix.
	 */
	public abstract void transform(double[][] matrix);


	/**
	 * Shades the color.
	 */
	public abstract void shade();


	/**
	 * Draws this piece.
	 * 
	 * @param graphics The Graphics2D instance for drawing shapes to the screen image.
	 */
	public abstract void draw(Graphics2D graphics);
}
