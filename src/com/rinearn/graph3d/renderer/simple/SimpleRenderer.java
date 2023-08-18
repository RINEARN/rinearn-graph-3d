package com.rinearn.graph3d.renderer.simple;

import com.rinearn.graph3d.renderer.RinearnGraph3DRenderer;
import com.rinearn.graph3d.renderer.RinearnGraph3DDrawingParameter;
import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;

import java.util.List;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.math.BigDecimal;


/**
 * The class providing a simple implementation of 
 * the rendering engine (renderer) of RINEARN Graph 3D.
 */
public final class SimpleRenderer implements RinearnGraph3DRenderer {

	/** The array index representing X, in some array fields. */
	public static final int X = 0;

	/** The array index representing Y, in some array fields. */
	public static final int Y = 1;

	/** The array index representing Z, in some array fields. */
	public static final int Z = 2;

	/** The default value of the distance between the viewpoint and the origin of the graph. */
	private static final double DEFAULT_DISTANCE = 4.0; // Should be belong to "camera configuration" ?

	/** The object storing configuration values of scales, frames, lighting/shading, and so on. */
	private volatile RinearnGraph3DConfiguration config = RinearnGraph3DConfiguration.createDefaultConfiguration();

	/** The Image instance storing the rendered image of the graph screen. */
	private volatile BufferedImage screenImage = null;

	/** The Graphics2D instance to draw the graph screen. */
	private volatile Graphics2D screenGraphics = null;

	/** The background color of the graph screen. */
	private volatile Color screenBackgroundColor = Color.BLACK; // Should be belong to "color configuration" ?

	/** The color of the outer frame of the graph. */
	private volatile Color frameColor = Color.WHITE; // Should be belong to "color configuration" ?

	/** The color of the grid lines frame of the graph. */
	private volatile Color gridColor = Color.DARK_GRAY; // Should be belong to "color configuration" ?

	/** The font for rendering tick labels. */
	private volatile Font tickLabelFont = new Font("Dialog", Font.PLAIN, 20); // Should be belong to "font configuration" ?

	/** The array storing X, Y, and Z-axis. Each element stores values related to an axis (e.g.: min/max value of the range). */
	private volatile Axis[] axes = { new Axis(), new Axis(), new Axis() };

	/** The list storing geometric pieces to be rendered. */
	private volatile List<GeometricPiece> geometricPieceList = new ArrayList<GeometricPiece>();

	/** The transformation matrix from the graph coordinate system to the view coordinate system. */
	private volatile double[][] transformationMatrix = {
		{ 1.0, 0.0, 0.0, 0.0 },
		{ 0.0, 1.0, 0.0, 0.0 },
		{ 0.0, 0.0, 1.0, -DEFAULT_DISTANCE }, // Z takes a negative value for the depth direction.
		{ 0.0, 0.0, 0.0, 1.0 }
	};


	// Temporary settings. Should be packed into this.config.scaleConfiguration.
	int verticalAlignThreshold = 128;
	int horizontalAlignThreshold = 32;
	double tickLabelMargin = 0.06;
	double tickLineLength = 0.05;

	/** The object providing drawing process of scale ticks of X/Y/Z axes. */
	private volatile ScaleTickDrawer scaleTickDrawer = new ScaleTickDrawer(
		verticalAlignThreshold, horizontalAlignThreshold,
		tickLabelMargin, tickLineLength,
		tickLabelFont, frameColor
	);

	/** The object providing drawing process of graph frames and grid lines. */
	private volatile FrameDrawer frameDrawer = new FrameDrawer(this.config.getFrameConfiguration(), this.frameColor, this.gridColor);


	/**
	 * Creates a new renderer.
	 * 
	 * @param screenWidth The width (pixels) of the screen.
	 * @param screenHeight The height (pixels) of the screen.
	 */
	public SimpleRenderer(int screenWidth, int screenHeight) {
		this.setScreenSize(screenWidth, screenHeight);
		this.clear();
	}


	/**
	 * Performs something temporary for the development and the debuggings.
	 */
	public synchronized void temporaryExam() {
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
			piece.shade(this.config.getLightConfiguration());
		}

		// Draw each geometric piece on the screen.
		for (GeometricPiece piece: this.geometricPieceList) {
			piece.project(screenWidth, screenHeight, screenOffsetX, screenOffsetY, magnification);
			piece.draw(this.screenGraphics);
		}
	}


	/**
	 * Sets the range of the X-axis.
	 * 
	 * The change of the range does not affect to the currently drawn contents
	 * (points, lines, quadrangles, and so on).
	 * To reflect the change of the range, please clear() and re-draw all contents again.
	 * 
	 * @param min The minimum value of the range.
	 * @param max The maximum value of the range.
	 */
	public synchronized void setXRange(BigDecimal min, BigDecimal max) {
		this.axes[X].setRange(min, max);
	}


	/**
	 * Sets the range of the Y-axis.
	 * 
	 * The change of the range does not affect to the currently drawn contents
	 * (points, lines, quadrangles, and so on).
	 * To reflect the change of the range, please clear() and re-draw all contents again.
	 * 
	 * @param min The minimum value of the range.
	 * @param max The maximum value of the range.
	 */
	public synchronized void setYRange(BigDecimal min, BigDecimal max) {
		this.axes[Y].setRange(min, max);
	}


	/**
	 * Sets the range of the Z-axis.
	 * 
	 * The change of the range does not affect to the currently drawn contents
	 * (points, lines, quadrangles, and so on).
	 * To reflect the change of the range, please clear() and re-draw all contents again.
	 * 
	 * @param min The minimum value of the range.
	 * @param max The maximum value of the range.
	 */
	public synchronized void setZRange(BigDecimal min, BigDecimal max) {
		this.axes[Z].setRange(min, max);
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
	public synchronized void rotateAroundX(double angle) {
		double sin = Math.sin(angle);
		double cos = Math.cos(angle);

		// Create the rotation matrix.
		double[][] r = new double[3][];
		r[0] = new double[] { 1.0, 0.0, 0.0 };
		r[1] = new double[] { 0.0, cos,-sin };
		r[2] = new double[] { 0.0, sin, cos };

		// Create a matrix for temporary storing updated values of the transformation matrix.
		double[][] updated = new double[3][3];

		// Act the rotation matrix to the transformation matrix.
		double[][] m = this.transformationMatrix;
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				updated[i][j] = r[i][0] * m[0][j] + r[i][1] * m[1][j] + r[i][2] * m[2][j];
			}
		}
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				m[i][j] = updated[i][j];
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
	public synchronized void rotateAroundY(double angle) {
		double sin = Math.sin(angle);
		double cos = Math.cos(angle);

		// Create the rotation matrix.
		double[][] r = new double[3][];
		r[0] = new double[] { cos, 0.0, sin };
		r[1] = new double[] { 0.0, 1.0, 0.0 };
		r[2] = new double[] { -sin,0.0, cos };

		// Create a matrix for temporary storing updated values of the transformation matrix.
		double[][] updated = new double[3][3];

		// Act the rotation matrix to the transformation matrix.
		double[][] m = this.transformationMatrix;
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				updated[i][j] = r[i][0] * m[0][j] + r[i][1] * m[1][j] + r[i][2] * m[2][j];
			}
		}
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				m[i][j] = updated[i][j];
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
	public synchronized void rotateAroundZ(double angle) {
		double sin = Math.sin(angle);
		double cos = Math.cos(angle);

		// Create the rotation matrix.
		double[][] r = new double[3][];
		r[0] = new double[] { cos,-sin, 0.0 };
		r[1] = new double[] { sin, cos, 0.0 };
		r[2] = new double[] { 0.0, 0.0, 1.0 };

		// Create a matrix for temporary storing updated values of the transformation matrix.
		double[][] updated = new double[3][3];

		// Act the rotation matrix to the transformation matrix.
		double[][] m = this.transformationMatrix;
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				updated[i][j] = r[i][0] * m[0][j] + r[i][1] * m[1][j] + r[i][2] * m[2][j];
			}
		}
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				m[i][j] = updated[i][j];
			}
		}
	}


	/**
	 * Cancels the effects of the rotations performed by rotateX/Y/Z methods.
	 */
	public synchronized void cancelRotations() {
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

		// Check whether the point is in ranges of X/Y/Z axes. If no, draw nothing.
		if (parameter.isRangeClippingEnabled()) {
			boolean isInRange =
					this.axes[X].containsCoordinate(x, true) &&
					this.axes[Y].containsCoordinate(y, true) &&
					this.axes[Z].containsCoordinate(z, true);
			
			if (!isInRange) {
				return;
			}
		}

		// Scale X/Y/Z coordinate values into the range [-1.0, 1.0] (= scaled space).
		if (parameter.isRangeScalingEnabled()) {
			x = this.axes[X].scaleCoordinate(x);
			y = this.axes[Y].scaleCoordinate(y);
			z = this.axes[Z].scaleCoordinate(z);
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

		// Check whether the line is in ranges of X/Y/Z axes. If no, draw nothing.
		if (parameter.isRangeClippingEnabled()) {
			boolean isInRange =
					this.axes[X].containsCoordinate(aX, true) &&
					this.axes[Y].containsCoordinate(aY, true) &&
					this.axes[Z].containsCoordinate(aZ, true) &&

					this.axes[X].containsCoordinate(bX, true) &&
					this.axes[Y].containsCoordinate(bY, true) &&
					this.axes[Z].containsCoordinate(bZ, true);

			if (!isInRange) {
				return;
			}
		}

		// Scale X/Y/Z coordinate values into the range [-1.0, 1.0] (= scaled space).
		if (parameter.isRangeScalingEnabled()) {
			aX = this.axes[X].scaleCoordinate(aX);
			aY = this.axes[Y].scaleCoordinate(aY);
			aZ = this.axes[Z].scaleCoordinate(aZ);

			bX = this.axes[X].scaleCoordinate(bX);
			bY = this.axes[Y].scaleCoordinate(bY);
			bZ = this.axes[Z].scaleCoordinate(bZ);
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

		// Check whether the line is in ranges of X/Y/Z axes. If no, draw nothing.
		if (parameter.isRangeClippingEnabled()) {
			boolean isInRange =
					this.axes[X].containsCoordinate(aX, true) &&
					this.axes[Y].containsCoordinate(aY, true) &&
					this.axes[Z].containsCoordinate(aZ, true) &&

					this.axes[X].containsCoordinate(bX, true) &&
					this.axes[Y].containsCoordinate(bY, true) &&
					this.axes[Z].containsCoordinate(bZ, true) &&

					this.axes[X].containsCoordinate(cX, true) &&
					this.axes[Y].containsCoordinate(cY, true) &&
					this.axes[Z].containsCoordinate(cZ, true) &&

					this.axes[X].containsCoordinate(dX, true) &&
					this.axes[Y].containsCoordinate(dY, true) &&
					this.axes[Z].containsCoordinate(dZ, true);

			if (!isInRange) {
				return;
			}
		}

		// Scale X/Y/Z coordinate values into the range [-1.0, 1.0] (= scaled space).
		if (parameter.isRangeScalingEnabled()) {
			aX = this.axes[X].scaleCoordinate(aX);
			aY = this.axes[Y].scaleCoordinate(aY);
			aZ = this.axes[Z].scaleCoordinate(aZ);

			bX = this.axes[X].scaleCoordinate(bX);
			bY = this.axes[Y].scaleCoordinate(bY);
			bZ = this.axes[Z].scaleCoordinate(bZ);

			cX = this.axes[X].scaleCoordinate(cX);
			cY = this.axes[Y].scaleCoordinate(cY);
			cZ = this.axes[Z].scaleCoordinate(cZ);

			dX = this.axes[X].scaleCoordinate(dX);
			dY = this.axes[Y].scaleCoordinate(dY);
			dZ = this.axes[Z].scaleCoordinate(dZ);
		}

		if (parameter.isAutoColoringEnabled()) {
			throw new RuntimeException("Unimplemented yet");			
		}

		Color color = parameter.getColor();
		QuadrangleGeometricPiece quad = new QuadrangleGeometricPiece(aX, aY, aZ, bX, bY, bZ, cX, cY, cZ, dX, dY, dZ, color);
		this.geometricPieceList.add(quad);
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


	/**
	 * Draws the outer frame of the graph.
	 */
	@Override
	public synchronized void drawFrame() {
		this.frameDrawer.drawFrame(this.geometricPieceList);
	}


	/**
	 * Draws the scale ticks of X/Y/Z axes.
	 */
	@Override
	public synchronized void drawScale() {

		// Generate coordinates and labels of ticks, based on the scale configuration.
		ScaleTickGenerator scaleTickGenerator = new ScaleTickGenerator(this.config.getScaleConfiguration());
		BigDecimal[] xTickCoords = scaleTickGenerator.generateScaleTickCoordinates(X, this.axes[X]);
		BigDecimal[] yTickCoords = scaleTickGenerator.generateScaleTickCoordinates(Y, this.axes[Y]);
		BigDecimal[] zTickCoords = scaleTickGenerator.generateScaleTickCoordinates(Z, this.axes[Z]);
		String[] xTickLabels = scaleTickGenerator.generateScaleTickLabels(X, this.axes[X], xTickCoords);
		String[] yTickLabels = scaleTickGenerator.generateScaleTickLabels(Y, this.axes[Y], yTickCoords);
		String[] zTickLabels = scaleTickGenerator.generateScaleTickLabels(Z, this.axes[Z], zTickCoords);

		// Set the generated ticks to the axes.
		this.axes[X].setTicks(xTickCoords, xTickLabels);
		this.axes[Y].setTicks(yTickCoords, yTickLabels);
		this.axes[Z].setTicks(xTickCoords, zTickLabels);

		// Draw tick labels/lines.
		this.scaleTickDrawer.drawScaleTicks(this.geometricPieceList, this.axes);
	}


	/**
	 * Draws the grid lines on the backwalls of the graph.
	 */
	@Override
	public synchronized void drawGrid() {
		this.frameDrawer.drawGridLines(this.geometricPieceList, this.axes);
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
