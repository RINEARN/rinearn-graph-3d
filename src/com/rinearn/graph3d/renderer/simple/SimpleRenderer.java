package com.rinearn.graph3d.renderer.simple;

import com.rinearn.graph3d.renderer.RinearnGraph3DDrawingParameter;
import com.rinearn.graph3d.renderer.RinearnGraph3DRenderer;

import java.util.List;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;


/**
 * The class providing a simple implementation of 
 * the rendering engine (renderer) of RINEARN Graph 3D.
 */
public class SimpleRenderer implements RinearnGraph3DRenderer {

	/** The default value of the distance between the viewpoint and the origin of the graph. */
	private static final double DEFAULT_DISTANCE = 3.0;

	/** The Image instance storing the rendered image of the graph screen. */
	private volatile BufferedImage screenImage = null;

	/** The Graphics2D instance to draw the graph screen. */
	private volatile Graphics2D screenGraphics = null;

	/** The background color of the graph screen. */
	private volatile Color screenBackgroundColor = Color.BLACK;

	/** The list storing geometric pieces to be rendered. */
	private volatile List<GeometricPiece> geometricPieceList = null;

	// The transformation matrix from the graph coordinate system to the view coordinate system.
	private volatile double[][] transformationMatrix = {
		{ 1.0, 0.0, 0.0, 0.0 },
		{ 0.0, 1.0, 0.0, 0.0 },
		{ 0.0, 0.0, 1.0, -DEFAULT_DISTANCE }, // Z takes a negative value for the depth direction.
		{ 0.0, 0.0, 0.0, 1.0 }
	};


	/**
	 * Creates a new renderer.
	 * 
	 * @param screenWidth The width (pixels) of the screen.
	 * @param screenHeight The height (pixels) of the screen.
	 */
	public SimpleRenderer(int screenWidth, int screenHeight) {
		this.geometricPieceList = new ArrayList<GeometricPiece>();
		this.setScreenSize(screenWidth, screenHeight);
		this.clear();
	}


	/**
	 * Clears all currently rendered contents.
	 */
	@Override
	public synchronized void clear() {

		// Remove all geometric pieces registered by the drawer methods.
		this.geometricPieceList.clear();
		System.gc();

		// Clear the content of the graph screen.
		this.screenGraphics.setColor(this.screenBackgroundColor);
		this.screenGraphics.fillRect(0, 0, this.screenImage.getWidth(), this.screenImage.getHeight());
	}


	/**
	 * Renders the graph on the screen.
	 */
	@Override
	public synchronized void render() {
		int screenWidth = this.screenImage.getWidth();
		int screenHeight = this.screenImage.getHeight();
		int screenOffsetX = 0;
		int screenOffsetY = 0;
		double magnification = 700.0;

		// Clear the graph screen.
		this.screenGraphics.setColor(this.screenBackgroundColor);
		this.screenGraphics.fillRect(0, 0, screenWidth, screenHeight);

		// Transform each geometric piece.
		for (GeometricPiece piece: this.geometricPieceList) {
			piece.transform(transformationMatrix);
		}

		// Sort the geometric pieces in descending order of their 'depth' values.
		GeometricDepthComparator comparator = new GeometricDepthComparator();
		this.geometricPieceList.sort(comparator);

		// Shades the color of each geometric piece.
		for (GeometricPiece piece: this.geometricPieceList) {
			piece.shade();
		}

		// Draw each geometric piece on the screen.
		for (GeometricPiece piece: this.geometricPieceList) {
			piece.project(screenWidth, screenHeight, screenOffsetX, screenOffsetY, magnification);
			piece.draw(this.screenGraphics);
		}
	}


	/**
	 * Rotates the graph around the X-axis.
	 * 
	 * For the sign of the rotation angle, 
	 * we define it as positive when a right-hand screw advances 
	 * towards the positive direction of the X-axis.
	 * 
	 * @param angle The rotation angle.
	 */
	public void rotateX(double angle) {
		double sin = Math.sin(angle);
		double cos = Math.cos(angle);

		// Create the rotation matrix.
		double[][] r = new double[3][];
		r[0] = new double[] { 1.0, 0.0, 0.0 };
		r[1] = new double[] { 0.0, cos,-sin };
		r[2] = new double[] { 0.0, sin, cos };

		// Act the rotation matrix to the transformation matrix.
		double[][] m = this.transformationMatrix;
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				m[i][j] = r[i][0] * m[0][j] + r[i][1] * m[1][j] + r[i][2] * m[2][j];
			}
		}
	}


	/**
	 * Rotates the graph around the Y-axis.
	 * 
	 * For the sign of the rotation angle, 
	 * we define it as positive when a right-hand screw advances 
	 * towards the positive direction of the Y-axis.
	 * 
	 * @param angle The rotation angle.
	 */
	public void rotateY(double angle) {
		double sin = Math.sin(angle);
		double cos = Math.cos(angle);

		// Create the rotation matrix.
		double[][] r = new double[3][];
		r[0] = new double[] { cos, 0.0, sin };
		r[1] = new double[] { 0.0, 1.0, 0.0 };
		r[2] = new double[] { -sin,0.0, cos };

		// Act the rotation matrix to the transformation matrix.
		double[][] m = this.transformationMatrix;
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				m[i][j] = r[i][0] * m[0][j] + r[i][1] * m[1][j] + r[i][2] * m[2][j];
			}
		}
	}


	/**
	 * Rotates the graph around the Z-axis.
	 * 
	 * For the sign of the rotation angle, 
	 * we define it as positive when a right-hand screw advances 
	 * towards the positive direction of the Z-axis.
	 * 
	 * @param angle The rotation angle.
	 */
	public void rotateZ(double angle) {
		double sin = Math.sin(angle);
		double cos = Math.cos(angle);

		// Create the rotation matrix.
		double[][] r = new double[3][];
		r[0] = new double[] { cos,-sin, 0.0 };
		r[1] = new double[] { sin, cos, 0.0 };
		r[2] = new double[] { 0.0, 0.0, 1.0 };

		// Act the rotation matrix to the transformation matrix.
		double[][] m = this.transformationMatrix;
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				m[i][j] = r[i][0] * m[0][j] + r[i][1] * m[1][j] + r[i][2] * m[2][j];
			}
		}
	}


	/**
	 * Cancels the effects of the rotations performed by rotateX/Y/Z methods.
	 */
	public void cancelRotations() {
		double dx = this.transformationMatrix[0][3];
		double dy = this.transformationMatrix[1][3];
		double distance = this.transformationMatrix[2][3];
		
		this.transformationMatrix[0] = new double[] { 1.0, 0.0, 0.0, dx };
		this.transformationMatrix[1] = new double[] { 0.0, 1.0, 0.0, dy };
		this.transformationMatrix[2] = new double[] { 0.0, 0.0, 1.0, distance };
		this.transformationMatrix[3] = new double[] { 0.0, 0.0, 0.0, 1.0 };
	}


	@Override
	public synchronized void drawPoint(double x, double y, double z, 
			double radius) {

		throw new RuntimeException("Unimplemented yet");
	}


	@Override
	public synchronized void drawPoint(double x, double y, double z, 
			double radius, Color color) {

		throw new RuntimeException("Unimplemented yet");
	}


	/**
	 * Draws a point with the specified parameter settings.
	 * 
	 * @param x The X coordinate of the point.
	 * @param y The Y coordinate of the point.
	 * @param z The Z coordinate of the point.
	 * @param radius The radius of the point (in pixels).
	 * @param parameter The object storing the drawing parameters.
	 */
	@Override
	public synchronized void drawPoint(double x, double y, double z, 
			double radius, RinearnGraph3DDrawingParameter parameter) {

		if (parameter.isRangeScalingEnabled()) {
			throw new RuntimeException("Unimplemented yet");			
		}
		if (parameter.isAutoColoringEnabled()) {
			throw new RuntimeException("Unimplemented yet");			
		}

		Color color = parameter.getColor();
		PointGeometricPiece point = new PointGeometricPiece(x, y, z, radius, color);
		this.geometricPieceList.add(point);
	}


	@Override
	public synchronized void drawLine(double aX, double aY, double aZ, 
			double bX, double bY, double bZ, 
			double width) {

		throw new RuntimeException("Unimplemented yet");
	}


	@Override
	public synchronized void drawLine(double aX, double aY, double aZ, 
			double bX, double bY, double bZ, 
			double width, Color color) {

		throw new RuntimeException("Unimplemented yet");
	}


	@Override
	public synchronized void drawLine(double aX, double aY, double aZ, 
			double bX, double bY, double bZ, 
			double width, RinearnGraph3DDrawingParameter parameter) {

		if (parameter.isRangeScalingEnabled()) {
			throw new RuntimeException("Unimplemented yet");			
		}
		if (parameter.isAutoColoringEnabled()) {
			throw new RuntimeException("Unimplemented yet");			
		}

		Color color = parameter.getColor();
		LineGeometricPiece line = new LineGeometricPiece(aX, aY, aZ, bX, bY, bZ, width, color);
		this.geometricPieceList.add(line);
	}


	@Override
	public synchronized void drawTriangle(double aX, double aY, double aZ, 
			double bX, double bY, double bZ, 
			double cX, double cY, double cZ) {

		throw new RuntimeException("Unimplemented yet");
	}


	@Override
	public synchronized void drawTriangle(double aX, double aY, double aZ, 
			double bX, double bY, double bZ, 
			double cX, double cY, double cZ, 
			Color color) {

		throw new RuntimeException("Unimplemented yet");
	}


	@Override
	public synchronized void drawTriangle(double aX, double aY, double aZ, 
			double bX, double bY, double bZ, 
			double cX, double cY, double cZ, 
			RinearnGraph3DDrawingParameter parameter) {

		throw new RuntimeException("Unimplemented yet");
	}


	@Override
	public synchronized void drawQuadrangle(double aX, double aY, double aZ, 
			double bX, double bY, double bZ, 
			double cX, double cY, double cZ, 
			double dX, double dY, double dZ) {

		throw new RuntimeException("Unimplemented yet");
	}


	@Override
	public synchronized void drawQuadrangle(double aX, double aY, double aZ, 
			double bX, double bY, double bZ, 
			double cX, double cY, double cZ, 
			double dX, double dY, double dZ, 
			Color color) {

		throw new RuntimeException("Unimplemented yet");
	}


	@Override
	public synchronized void drawQuadrangle(double aX, double aY, double aZ, 
			double bX, double bY, double bZ, 
			double cX, double cY, double cZ, 
			double dX, double dY, double dZ, 
			RinearnGraph3DDrawingParameter parameter) {

		throw new RuntimeException("Unimplemented yet");
	}


	@Override
	public synchronized void drawText(double x, double y, double z, 
			String text, Font font, Color color) {

		throw new RuntimeException("Unimplemented yet");
	}


	@Override
	public synchronized void drawText(double x, double y, double z, 
			String text, Font font, RinearnGraph3DDrawingParameter parameter) {

		throw new RuntimeException("Unimplemented yet");
	}


	@Override
	public synchronized void drawFrame() {
		throw new RuntimeException("Unimplemented yet");
	}


	@Override
	public synchronized void drawScale() {
		throw new RuntimeException("Unimplemented yet");
	}


	@Override
	public synchronized void drawGrid() {
		throw new RuntimeException("Unimplemented yet");
	}


	@Override
	public synchronized void drawLabel() {
		throw new RuntimeException("Unimplemented yet");
	}


	/**
	 * Sets the size of the graph screen.
	 * 
	 * @param screenWidth The width (pixels) of the screen.
	 * @param screenHeight The height (pixels) of the screen.
	 */
	public synchronized void setScreenSize(int screenWidth, int screenHeight) {

		// If the image/graphics instances are already allocated, release them.
		if (this.screenGraphics != null) {
			this.screenGraphics.dispose();
			this.screenImage = null;
			System.gc();
		}

		// Allocate the image/graphics instances.
		this.screenImage = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
		this.screenGraphics = this.screenImage.createGraphics();
	}


	/**
	 * Returns the Image instance storing the rendered image of the graph screen.
	 * 
	 * @return The rendered image of the graph screen.
	 */
	public synchronized Image getScreenImage() {
		return this.screenImage;
	}


	/**
	 * Sets the background color of the graph screen.
	 * 
	 * @param screenBackgroundColor The background color of the graph screen.
	 */
	public synchronized void setScreenBackgroundColor(Color screenBackgroundColor) {
		this.screenBackgroundColor = screenBackgroundColor;
	}
}
