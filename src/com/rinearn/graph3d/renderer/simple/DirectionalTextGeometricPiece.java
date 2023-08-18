package com.rinearn.graph3d.renderer.simple;

import com.rinearn.graph3d.config.LightConfiguration;
import com.rinearn.graph3d.renderer.RinearnGraph3DDrawingParameter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;


/**
 * A geometric piece class representing a "directional" text label,
 * which is visible only from the specific direction.
 */
public final class DirectionalTextGeometricPiece extends GeometricPiece {

	/** The value of this text label. */
	private String text;

	/** The font to draw the content of this text label. */
	private Font font;

	/** The flag representing whether this text label is visible, updated in transform() method. */
	private boolean visible = false;

	/** The horizontal alignment (offset) of the rendered text on the screen. */
	private RinearnGraph3DDrawingParameter.HorizontalAlignment horizontalAlignment;

	/** The vertical alignment (offset) of the rendered text on the screen. */
	private RinearnGraph3DDrawingParameter.VerticalAlignment verticalAlignment;

	/** The vertical distance [px] from the reference point, at which position changes on "RADIAL" alignment. */
	private int verticalAlignmentThreshold;

	/** The horizontal distance [px] from the reference point, at which position changes on "RADIAL" alignment. */
	private int horizontalAlignmentThreshold;

	/** Stores X coordinate value of the screen center, updated in project() method. */
	private int screenCenterX = 0;

	/** Stores Y coordinate value of the screen center, updated in project() method. */
	private int screenCenterY = 0;


	/**
	 * Creates a new text label piece representing a point.
	 * 
	 * For the parameter "directionalVectors",
	 * specify an array storing the vectors representing the directions from which this text label is visible.
	 * Its array indices represents [vector index][dimension index], where dimension index is: 0=X, 1=Y, and 2=Z.
	 * The text label is only visible when ALL vectors are facing toward the front-side of the screen (Z > 0).
	 * 
	 * @param x The x coordinate value of the position of the text label, in the scaled space.
	 * @param y The y coordinate value of the position of the text label, in the scaled space.
	 * @param z The z coordinate value of the position of the text label, in the scaled space.
	 * @param alignmentReferenceX The x coordinate value of the reference point of "RADIAL" alignment, in the scaled space.
	 * @param alignmentReferenceY The y coordinate value of the reference point of "RADIAL" alignment, in the scaled space.
	 * @param alignmentReferenceZ The z coordinate value of the reference point of "RADIAL" alignment, in the scaled space.
	 * @param directionalVectors The vectors representing the directions from which this text label is visible. See the above description.
	 * @param verticalAlignment The vertical alignment (offset) of the rendered text on the screen.
	 * @param horizontalAlignment The horizontal alignment (offset) of the rendered text on the screen.
	 * @param verticalAlignmentThreshold The vertical distance [px] from the reference point, at which position changes on "RADIAL" alignment.
	 * @param horizontalAlignmentThreshold The horizontal distance [px] from the reference point, at which position changes on "RADIAL" alignment.
	 * @param text The displayed value of the text label.
	 * @param font The font of the text.
	 * @param color The color of the text.
	 */
	public DirectionalTextGeometricPiece(double x, double y, double z,
			double alignmentReferenceX, double alignmentReferenceY, double alignmentReferenceZ,
			double[][] directionalVectors,
			RinearnGraph3DDrawingParameter.VerticalAlignment verticalAlignment,
			RinearnGraph3DDrawingParameter.HorizontalAlignment horizontalAlignment,
			int verticalAlignmentThreshold,
			int horizontalAlignmentThreshold,
			String text, Font font, Color color) {

		// Store the rendered point, the alignment reference point, and the directional vectors as vertices.
		// So the number of vertices is as follows:
		this.vertexCount = 2 + directionalVectors.length;

		// In the vertex array,
		// store the rendered point as 0th vertex, the alignment reference point as 1st vertex,
		// and directional vectors as latter vertices, expediently.
		this.scaledVertexArray = new double[this.vertexCount][4]; // [4] is X/Y/Z/W
		this.scaledVertexArray[0][X] = x;
		this.scaledVertexArray[0][Y] = y;
		this.scaledVertexArray[0][Z] = z;
		this.scaledVertexArray[0][W] = 1.0;
		this.scaledVertexArray[1][X] = alignmentReferenceX;
		this.scaledVertexArray[1][Y] = alignmentReferenceY;
		this.scaledVertexArray[1][Z] = alignmentReferenceZ;
		this.scaledVertexArray[1][W] = 1.0;
		for (int ivec=0; ivec<directionalVectors.length; ivec++) {
			System.arraycopy(directionalVectors[ivec], 0, this.scaledVertexArray[2 + ivec], 0, 3);

			// For directional vectors, we should transform only the angle of the vectors, 
			// so we should ignore effects of the translational elements of the transformation matrix.
			// So set W to 0.
			this.scaledVertexArray[2 + ivec][W] = 0.0;
		}

		this.transformedVertexArray = new double[this.vertexCount][3]; // [3] is X/Y/Z
		this.projectedVertexArray = new int[this.vertexCount][2];      // [2] is X/Y
		this.originalColor = color;

		this.horizontalAlignment = horizontalAlignment;
		this.verticalAlignment = verticalAlignment;
		this.verticalAlignmentThreshold = verticalAlignmentThreshold;
		this.horizontalAlignmentThreshold = verticalAlignmentThreshold;

		this.text = text;
		this.font = font;
	}


	/**
	 * Transforms the coordinate values of this text label.
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
		this.depthSquaredValue = tv[0][Z] * tv[0][Z];
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
		this.screenCenterX = screenWidth >> 1 + screenOffsetX; // bit-shifting instead of dividing by 2.
		this.screenCenterY = screenHeight >> 1 - screenOffsetY;

		// Short aliases of the vertices arrays.
		double[][] tv = this.transformedVertexArray;
		int[][] pv = this.projectedVertexArray;

		// Project the rendered point, which is stored as 0th vertex.
		double projectionRatio = magnification / -tv[0][Z]; // Z takes a negative value for the depth direction.
		pv[0][X] = screenCenterX + (int)(tv[0][X] * projectionRatio);
		pv[0][Y] = screenCenterY - (int)(tv[0][Y] * projectionRatio);

		// Project the alignment reference point, which is stored as 1st vertex.
		projectionRatio = magnification / -tv[1][Z];
		pv[1][X] = screenCenterX + (int)(tv[1][X] * projectionRatio);
		pv[1][Y] = screenCenterY - (int)(tv[1][Y] * projectionRatio);
	}


	/**
	 * Draws this text label.
	 * 
	 * @param graphics The Graphics2D instance for drawing shapes to the screen image.
	 */
	@Override
	public void draw(Graphics2D graphics) {
		if (!visible) {
			return;
		}
		int[][] pv = this.projectedVertexArray;

		// Compute the width/height of the rendered text with the specified font.
		FontMetrics metrics = graphics.getFontMetrics(this.font);
		int width = metrics.stringWidth(this.text);
		int height = metrics.getAscent();

		// Determine the offset value of X coordinate of the rendering position, based on the alignment mode.
		int coordOffsetX = Integer.MAX_VALUE;
		switch (this.horizontalAlignment) {
			case NONE : {
				coordOffsetX = 0;
				break;
			}
			case LEFT : {
				coordOffsetX = -width;
				break;
			}
			case RIGHT : {
				coordOffsetX = 0;
				break;
			}
			case CENTER : {
				coordOffsetX = -width/2;
				break;
			}
			case RADIAL : {
				int referencePointX = pv[1][X];
				if (referencePointX < this.screenCenterX - this.horizontalAlignmentThreshold) {
					coordOffsetX = -width; // LEFT
				} else if (this.screenCenterX + this.horizontalAlignmentThreshold < referencePointX) {
					coordOffsetX = 0; // RIGHT
				} else {
					coordOffsetX = -width/2; // CENTER
				}
				break;
			}
			default : {
				throw new RuntimeException("Unexpected horizontal alignment: " + this.horizontalAlignment);
			}
		}

		// Determine the offset value of Y coordinate of the rendering position, based on the alignment mode.
		int coordOffsetY = 0;
		switch (this.verticalAlignment) {
			case NONE : {
				coordOffsetY = 0;
				break;
			}
			case TOP : {
				coordOffsetY = 0;
				break;
			}
			case BOTTOM : {
				coordOffsetY = height;
				break;
			}
			case CENTER : {
				coordOffsetY = height/2;
				break;
			}
			case RADIAL : {
				int referencePointY = pv[1][Y];
				if (referencePointY < this.screenCenterY - this.verticalAlignmentThreshold) {
					coordOffsetY = 0; // TOP
				} else if (this.screenCenterY + this.verticalAlignmentThreshold < referencePointY) {
					coordOffsetY = height; // BOTTOM
				} else {
					coordOffsetY = height/2; // CENTER
				}
				break;
			}
			default : {
				throw new RuntimeException("Unexpected vertical alignment: " + this.verticalAlignment);
			}
		}

		// Draw the text.
		int textBaseLineX = pv[0][X] + coordOffsetX;
		int textBaseLineY = pv[0][Y] + coordOffsetY;
		graphics.setColor(this.onscreenColor);
		graphics.setFont(this.font);
		graphics.drawString(this.text, textBaseLineX, textBaseLineY);
	}
}
