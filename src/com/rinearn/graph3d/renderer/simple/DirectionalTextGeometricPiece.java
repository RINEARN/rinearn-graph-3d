package com.rinearn.graph3d.renderer.simple;

import com.rinearn.graph3d.renderer.RinearnGraph3DLightingParameter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Font;


/**
 * A geometric piece class representing a "directional" text label,
 * which is visible only from the specific direction.
 */
public class DirectionalTextGeometricPiece extends GeometricPiece {

	/** The value of this text label. */
	private String text;

	/** The font to draw the content of this text label. */
	private Font font;

	/** The flag representing whether this text label is visible, updated in transform() method. */
	private boolean visible;


	/**
	 * Creates a new text label piece representing a point.
	 * 
	 * For the parameter "directionalVectors",
	 * specify an array storing the vectors representing the directions from which this text label is visible.
	 * Its array indices represents [vector index][dimension index], where dimension index is: 0=X, 1=Y, and 2=Z.
	 * The text label is only visible when ALL vectors are facing toward the front-side of the screen (Z > 0).
	 * 
	 * @param x The x coordinate value of the center of the text label, in the scaled space.
	 * @param y The y coordinate value of the center of the text label, in the scaled space.
	 * @param z The z coordinate value of the center of the text label, in the scaled space.
	 * @param directionalVectors The vectors representing the directions from which this text label is visible. See the above description.
	 * @param text The displayed value of the text label.
	 * @param font The font of the text.
	 * @param color The color of the text.
	 */
	public DirectionalTextGeometricPiece(double x, double y, double z, double[][] directionalVectors, String text, Font font, Color color) {
		this.vertexCount = 1 + directionalVectors.length;

		// In the vertex array, store X/Y/Z coordinates as 0-th vertex, and directional vectors as latter vertices, expediently.
		this.scaledVertexArray = new double[this.vertexCount][3]; // [3] is X/Y/Z
		this.scaledVertexArray[0][X] = x;
		this.scaledVertexArray[0][Y] = y;
		this.scaledVertexArray[0][Z] = z;
		for (int ivec=0; ivec<directionalVectors.length; ivec++) {
			System.arraycopy(directionalVectors[ivec], 0, this.scaledVertexArray[1 + ivec], 0, 3);
		}

		this.transformedVertexArray = new double[this.vertexCount][3]; // [3] is X/Y/Z
		this.projectedVertexArray = new int[this.vertexCount][2];      // [2] is X/Y
		this.originalColor = color;

		this.text = text;
		this.font = font;
		this.visible = false; // Updated in transform() method.
	}


	/**
	 * Transforms the coordinate values of this text label.
	 * 
	 * @param matrix The transformation matrix.
	 */
	@Override
	public void transform(double[][] matrix) {
/*
		// Short aliases of the matrix and vertices arrays.
		double[][] m = matrix;
		double[] sv = this.scaledVertexArray[0];
		double[] tv = this.transformedVertexArray[0];

		// Transform the coordinate values, where X=0, Y=1, and Z=2.
		tv[X] = m[0][0] * sv[X] + m[0][1] * sv[Y] + m[0][2] * sv[Z] + m[0][3];
		tv[Y] = m[1][0] * sv[X] + m[1][1] * sv[Y] + m[1][2] * sv[Z] + m[1][3];
		tv[Z] = m[2][0] * sv[X] + m[2][1] * sv[Y] + m[2][2] * sv[Z] + m[2][3];

		// Compute the square of the 'depth' value.
		this.depthSquaredValue = tv[Z] * tv[Z];
*/
		// ---

		// Reset the visibility of this text label (updated later if it should be invisible).
		this.visible = true;

		// Short aliases of the matrix and vertices arrays.
		double[][] m = matrix;
		double[][] sv = this.scaledVertexArray;
		double[][] tv = this.transformedVertexArray;

		// Transform the coordinate values (stores as 0-th vertex), where X=0, Y=1, and Z=2.
		tv[0][X] = m[0][0] * sv[0][X] + m[0][1] * sv[0][Y] + m[0][2] * sv[0][Z] + m[0][3];
		tv[0][Y] = m[1][0] * sv[0][X] + m[1][1] * sv[0][Y] + m[1][2] * sv[0][Z] + m[1][3];
		tv[0][Z] = m[2][0] * sv[0][X] + m[2][1] * sv[0][Y] + m[2][2] * sv[0][Z] + m[2][3];

		// Transform directional vectors (handled as 1-th to the last vertices, expediently).
		for (int ivertex=1; ivertex<this.vertexCount; ivertex++) {

			// We use only Z element, so omit calculating X and Y elements.
			// Note that, for the directional vectors, transform only its direction, ignoring m[*][3].

			//tv[ivertex][X] = m[0][0] * sv[ivertex][X] + m[0][1] * sv[ivertex][Y] + m[0][2] * sv[ivertex][Z];
			//tv[ivertex][Y] = m[1][0] * sv[ivertex][X] + m[1][1] * sv[ivertex][Y] + m[1][2] * sv[ivertex][Z];
			tv[ivertex][Z] = m[2][0] * sv[ivertex][X] + m[2][1] * sv[ivertex][Y] + m[2][2] * sv[ivertex][Z];

			// If any vector faces the depth direction from the viewpoint, set this text label invisible.
			if (tv[ivertex][Z] < 0) {
				this.visible = false;
			}
		}

		// Compute the square of the 'depth' value.
		this.depthSquaredValue = tv[0][Z] * tv[0][Z];
	}


	/**
	 * Shades the color.
	 * 
	 * @param lightingParameter The object storing parameters for lighting and shading.
	 */
	@Override
	public void shade(RinearnGraph3DLightingParameter lightingParameter) {

		// Text labels have no shades, so simply copy the original color as it is.
		this.onscreenColor = this.originalColor;
	}


	/**
	 * Computes the projected screen coordinate values of this text labels.
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
		if (!visible) {
			return;
		}

		graphics.setColor(this.onscreenColor);
		graphics.setFont(this.font);

		int[] pv = this.projectedVertexArray[0];
		graphics.drawString(this.text, pv[X], pv[Y]);
	}
}
