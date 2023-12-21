package com.rinearn.graph3d.renderer.simple;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import com.rinearn.graph3d.config.LightConfiguration;


/**
 * A geometric piece class representing a "directional" line,
 * which is visible only from the specific direction.
 */
public final class DirectionalLineGeometricPiece extends GeometricPiece {

	/** The stroke for drawing this line. */
	private Stroke stroke;

	/** The flag representing whether this text label is visible, updated in transform() method. */
	private boolean visible = false;

	/**
	 * Creates a new geometric piece representing a line between point A and point B.
	 *
	 * For the parameter "directionalVectors",
	 * specify an array storing the vectors representing the directions from which this line is visible.
	 * Its array indices represents [vector index][dimension index], where dimension index is: 0=X, 1=Y, and 2=Z.
	 * The text label is only visible when ALL vectors are facing toward the front-side of the screen (Z > 0).
	 *
	 * @param aX The x coordinate value of the point A, in the scaled space.
	 * @param aY The y coordinate value of the point A, in the scaled space.
	 * @param aZ The z coordinate value of the point A, in the scaled space.
	 * @param bX The x coordinate value of the point B, in the scaled space.
	 * @param bY The y coordinate value of the point B, in the scaled space.
	 * @param bZ The z coordinate value of the point B, in the scaled space.
	 * @param directionalVectors The vectors representing the directions from which this text label is visible. See the above description.
	 * @param width The width of the line.
	 * @param color The color of the line.
	 */
	public DirectionalLineGeometricPiece(double aX, double aY, double aZ, double bX, double bY, double bZ,
			double[][] directionalVectors, double width, Color color) {

		// Store the two edge points and the directional vectors as vertices.
		// So the number of vertices is as follows:
		this.vertexCount = 2 + directionalVectors.length;

		// In the vertex array,
		// store the edge point of the line as 0th and 1st vertex,
		// and directional vectors as latter vertices, expediently.
		this.scaledVertexArray = new double[this.vertexCount][4];
		this.scaledVertexArray[0][X] = aX;
		this.scaledVertexArray[0][Y] = aY;
		this.scaledVertexArray[0][Z] = aZ;
		this.scaledVertexArray[0][W] = 1.0;
		this.scaledVertexArray[1][X] = bX;
		this.scaledVertexArray[1][Y] = bY;
		this.scaledVertexArray[1][Z] = bZ;
		this.scaledVertexArray[1][W] = 1.0;
		for (int ivec=0; ivec<directionalVectors.length; ivec++) {
			System.arraycopy(directionalVectors[ivec], 0, this.scaledVertexArray[2 + ivec], 0, 3);

			// For directional vectors, we should transform only the angle of the vectors,
			// so we should ignore effects of the translational elements of the transformation matrix.
			// So set W to 0.
			this.scaledVertexArray[2 + ivec][W] = 0.0;
		}

		// Initialize other fields.
		this.transformedVertexArray = new double[this.vertexCount][3]; // [3] is X/Y/Z
		this.projectedVertexArray = new int[this.vertexCount][2];      // [2] is X/Y
		this.stroke = new BasicStroke((float)width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
		this.originalColor = color;
	}


	/**
	 * Transforms the coordinate values of this line.
	 *
	 * @param matrix The transformation matrix.
	 */
	@Override
	public void transform(double[][] matrix) {

		// Reset the visibility of this text label (updated later if it should be invisible).
		this.visible = true;

		// Short aliases of the matrix and vertices arrays.
		double[][] m = matrix;
		double[][] sv = this.scaledVertexArray;
		double[][] tv = this.transformedVertexArray;

		// Transform the coordinate values of the rendered point([0]), alignment reference point(1), and directional vectors([2],...).
		for (int ivertex=0; ivertex<this.vertexCount; ivertex++) {
			tv[ivertex][X] = m[0][0] * sv[ivertex][X] + m[0][1] * sv[ivertex][Y] + m[0][2] * sv[ivertex][Z] + m[0][3] * sv[ivertex][W];
			tv[ivertex][Y] = m[1][0] * sv[ivertex][X] + m[1][1] * sv[ivertex][Y] + m[1][2] * sv[ivertex][Z] + m[1][3] * sv[ivertex][W];
			tv[ivertex][Z] = m[2][0] * sv[ivertex][X] + m[2][1] * sv[ivertex][Y] + m[2][2] * sv[ivertex][Z] + m[2][3] * sv[ivertex][W];
		}

		// If any directional vector faces the depth direction from the viewpoint, set this text label invisible.
		for (int ivertex=2; ivertex<this.vertexCount; ivertex++) {
			if (tv[ivertex][Z] < 0) {
				this.visible = false;
			}
		}

		// Compute the square of the 'depth' value.
		double meanDepth = (tv[0][Z] + tv[1][Z]) * 0.5;
		this.depthSquaredValue = meanDepth * meanDepth;
	}


	/**
	 * Shades the color.
	 *
	 * @param lightConfig The object storing parameters for lighting and shading.
	 */
	@Override
	public void shade(LightConfiguration lightConfig) {

		// Text labels have no shades, so simply copy the original color as it is.
		this.onscreenColor = this.originalColor;
	}


	/**
	 * Computes the projected screen coordinate values of this line.
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
		double[][] tv = this.transformedVertexArray;
		int[][] pv = this.projectedVertexArray;

		// Project the edge points of the line, which is stored as 0th and 1st vertex.
		double projectionRatio = magnification / -tv[0][Z]; // Z takes a negative value for the depth direction.
		pv[0][X] = screenCenterX + (int)(tv[0][X] * projectionRatio);
		pv[0][Y] = screenCenterY - (int)(tv[0][Y] * projectionRatio);
		projectionRatio = magnification / -tv[1][Z];
		pv[1][X] = screenCenterX + (int)(tv[1][X] * projectionRatio);
		pv[1][Y] = screenCenterY - (int)(tv[1][Y] * projectionRatio);
	}


	/**
	 * Draws this line.
	 *
	 * @param graphics The Graphics2D instance for drawing shapes to the screen image.
	 */
	@Override
	public void draw(Graphics2D graphics) {
		if (!visible) {
			return;
		}
		graphics.setColor(this.onscreenColor);
		graphics.setStroke(this.stroke);

		int[][] pv = this.projectedVertexArray;
		graphics.drawLine(pv[0][X], pv[0][Y], pv[1][X], pv[1][Y]);
	}
}
