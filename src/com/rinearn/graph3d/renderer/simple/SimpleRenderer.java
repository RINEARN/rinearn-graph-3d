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

	/** The Image instance storing the rendered image of the graph screen. */
	private volatile BufferedImage screenImage = null;

	/** The Graphics2D instance to draw the graph screen. */
	private volatile Graphics2D screenGraphics = null;

	/** The background color of the graph screen. */
	private volatile Color screenBackgroundColor = Color.BLACK;

	/** The list storing geometric pieces to be rendered. */
	private volatile List<GeometricPiece> geometricPieceList = null;


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

		// Clear the graph screen.
		this.screenGraphics.setColor(this.screenBackgroundColor);
		this.screenGraphics.fillRect(0, 0, this.screenImage.getWidth(), this.screenImage.getHeight());

		/*
		 * TODO:
		 * Create the transformation matrix here,
		 * from the camera angles and other related parameters.
		 */
		double[][] transformationMatrix = this.createTemporaryTransformationMatrix();

		// Transform each geometric piece.
		for (GeometricPiece piece: this.geometricPieceList) {
			piece.transform(transformationMatrix);
		}

		// Sort the geometric pieces by their 'depth' values.
		GeometricDepthComparator comparator = new GeometricDepthComparator();
		this.geometricPieceList.sort(comparator);

		// Shades the color of each geometric piece.
		for (GeometricPiece piece: this.geometricPieceList) {
			piece.shade();
		}

		// Draw each geometric piece on the screen.
		for (GeometricPiece piece: this.geometricPieceList) {
			piece.draw(this.screenGraphics);
		}
	}

	// Temporary method for developing/debugging.
	private double[][] createTemporaryTransformationMatrix() {
		double magnification = 700.0;
		double distance = 6.0;
		
		double[][] matrix = new double[4][];
		matrix[0] = new double[] { magnification, 0.0, 0.0, 0.0 };
		matrix[1] = new double[] { 0.0, magnification, 0.0, 0.0 };
		matrix[2] = new double[] { 0.0, 0.0, magnification, distance };
		matrix[3] = new double[] { 0.0, 0.0, 0.0, 1.0 };
		
		return matrix;
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


	@Override
	public synchronized void drawPoint(double x, double y, double z, 
			double radius, RinearnGraph3DDrawingParameter parameter) {

		throw new RuntimeException("Unimplemented yet");
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

		throw new RuntimeException("Unimplemented yet");
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
