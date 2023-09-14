package com.rinearn.graph3d.renderer.simple;

import com.rinearn.graph3d.renderer.RinearnGraph3DRenderer;
import com.rinearn.graph3d.renderer.RinearnGraph3DDrawingParameter;
import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.RangeConfiguration;
import com.rinearn.graph3d.config.CameraConfiguration;
import com.rinearn.graph3d.config.ColorConfiguration;
import com.rinearn.graph3d.config.ColorGradient;

import java.util.List;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.math.BigDecimal;


// !!!!!
// NOTE
//
//    rotateAroundX とかの引数、BigDecimal にすべき？ range とかは ticks とかは BigDecimal なので。
//
//    > range や ticks は完全精度で指定できないと困る場面があり得るが、
//      rotateAroundX とかは回転でしかも追加的な振る舞いなので、double でいいでしょ。内部でどうせ sin & cos にかけるし。
//
//      > 外から叩くAPIだけ念のため BigDecimal にしておいても損はないような。使う手間は少し増えるけどそれ以外に。
//
//        > いやしかし得も絶対ない気がする。だって単位ラジアンだと、引数渡す側が円周率をかけたりする必要が常にあるけど、
//          その円周率の値をどっから取ってくんのっていう。普通は double の精度しか得られないでしょ。しかも完全精度は理論上無理で。
//          なので角度に関しては、0以外の値は本質的に精度落ちしてるので、BigDecimal にしても誤差ゼロにはできない。
//          API叩く側が、ラジアンのBigDecimal値というものを作った時点で、既に精度が落ちてる。ので何も嬉しくない。手間だけが増える。
//
//    ならどういうパラメータが double であるべきで、どういうパラメータは BigDecimal であるべきか？
//
//    > 「APIは全部念のためBigDecimal」みたいな案はとりあえず除外すべきだと思う。
//       誤差の存在が非常にクリティカル＆可視な形に効いてくる可能性があり得るパラメータのみ絞ってBigDecimal、とかが一つの基準か。
//       そういうのが全く効いてこない、例えば極端なものでは ticks の線の長さとかは double でいいと思う。逆変換も丸めりゃ済むやつ。
//
//       というかBigDecimalのやつに関しても簡易版の double による setter 並存してもいいくらいだと思う。むしろ常に並存すべきか。
//
//    基準要検討、また後々にパラメータ整理する際まで
//
// !!!!!

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


	// Temporary settings: Should be packed into this.config.scaleConfiguration?
	//     -> No, because they vary dynamically depending on zoom in/out of magnification. They are not static settings.
	int verticalAlignThreshold = 128;
	int horizontalAlignThreshold = 32;

	/** The object providing drawing process of scale ticks of X/Y/Z axes. */
	private volatile ScaleTickDrawer scaleTickDrawer = new ScaleTickDrawer(
		this.config.getScaleConfiguration(),
		verticalAlignThreshold, horizontalAlignThreshold,
		tickLabelFont, frameColor
	);

	/** The object providing drawing process of graph frames and grid lines. */
	private volatile FrameDrawer frameDrawer = new FrameDrawer(this.config.getFrameConfiguration(), this.frameColor, this.gridColor);

	/** The color mixer, which generates colors of geometric pieces (points, lines, and so on). */
	private volatile ColorMixer colorMixer = new ColorMixer();

	/** The flag representing whether the graph screen has been resized. */
	private volatile boolean screenUpdated = false;

	/** The flag representing whether the content of the graph screen has been updated. */
	private volatile boolean screenResized = false;


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


	// !!!!!
	// NOTE:
	//
	//   Should rename to "configure(...)" ?
	//
	// !!!!!

	/**
	 * Sets the container storing configuration values.
	 * 
	 * Note that, the changes of the configuration (contains ranges of X/Y/Z axes)
	 * does not affect to the currently drawn contents (points, lines, quadrangles, and so on).
	 * To reflect the changes, please clear() and re-draw all contents again.
	 * 
	 * @param configuration The container storing configuration values.
	 * @throws IllegalArgumentException
	 *     Thrown when incorrect or inconsistent settings are detected in the specified configuration.
	 */
	public synchronized void setConfiguration(RinearnGraph3DConfiguration configuration) {

		// Validate the specified (may be partial) configuration at first.
		try {
			configuration.validate();
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}

		// RinearnGraph3DConfiguration is a container of subpart configurations.
		// Some of them are set and others are not set,
		// so extract only stored subpart configurations in the argument "configuration"
		// and merge them to the "config" field of this instance.
		this.config.merge(configuration);

		// Validate the merged full "config" (field of this instance).
		// Note that, even when the validation of the specified "configuration" argument has passed,
		// the validation of the full "config" may fail.
		// It is because there are some dependencies between subpart configurations.
		try {
			this.config.validate();
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}

		// Set the ranges of X/Y/Z axes.
		RangeConfiguration rangeConfig = this.config.getRangeConfiguration();
		RangeConfiguration.AxisRangeConfiguration xRangeConfig = rangeConfig.getXRangeConfiguration();
		RangeConfiguration.AxisRangeConfiguration yRangeConfig = rangeConfig.getYRangeConfiguration();
		RangeConfiguration.AxisRangeConfiguration zRangeConfig = rangeConfig.getZRangeConfiguration();
		this.axes[X].setRange(xRangeConfig.getMinimum(), xRangeConfig.getMaximum());
		this.axes[Y].setRange(yRangeConfig.getMinimum(), yRangeConfig.getMaximum());
		this.axes[Z].setRange(zRangeConfig.getMinimum(), zRangeConfig.getMaximum());

		// Sets the configuration for drawing scales and frames.
		this.scaleTickDrawer.setScaleConfiguration(this.config.getScaleConfiguration());
		this.frameDrawer.setFrameConfiguration(this.config.getFrameConfiguration());

		// Update the camera angle(s).
		CameraConfiguration cameraConfig = this.config.getCameraConfiguration();
		this.updateCameraAngle(cameraConfig.getRotationMatrix());

		// Update the range of each axis (dimension) of color gradients, if its auto-ranging feature is enabled.
		ColorConfiguration colorConfig = this.config.getColorConfiguration();
		for (ColorGradient gradient: colorConfig.getDataColorGradients()) {
			for (ColorGradient.AxisColorGradient axisGradient: gradient.getAxisColorGradients()) {
				if (!axisGradient.isAutoBoundaryRangingEnabled()) {
					continue;
				}
				switch (axisGradient.getAxis()) {
					case X : {
						axisGradient.setMinimumBoundaryCoordinate(xRangeConfig.getMinimum());
						axisGradient.setMaximumBoundaryCoordinate(xRangeConfig.getMaximum());
						break;
					}
					case Y : {
						axisGradient.setMinimumBoundaryCoordinate(yRangeConfig.getMinimum());
						axisGradient.setMaximumBoundaryCoordinate(yRangeConfig.getMaximum());
						break;
					}
					case Z : {
						axisGradient.setMinimumBoundaryCoordinate(zRangeConfig.getMinimum());
						axisGradient.setMaximumBoundaryCoordinate(zRangeConfig.getMaximum());
						break;
					}
					case COLUMN_4 : {
						RangeConfiguration.AxisRangeConfiguration[] extraRangeConfig = rangeConfig.getExtraDimensionRangeConfigurations();
						axisGradient.setMinimumBoundaryCoordinate(extraRangeConfig[0].getMinimum());
						axisGradient.setMaximumBoundaryCoordinate(extraRangeConfig[0].getMaximum());
						// Existence of extraRangeConfig[0] has been checked in the validation.
						break;
					}
					default : {
						throw new UnsupportedOperationException("Unknown gradient axis: " + axisGradient.getAxis());
					}
				}
			}
		}
	}


	/**
	 * Updates the camera angle of the graph,
	 * by acting the specified rotation matrix to the initial state (default angle).
	 * 
	 * @param rotationMatrix The matrix representing the rotation of the graph from the initial state.
	 */
	private void updateCameraAngle(double[][] rotationMatrix) {

		// Resets the rotation-related elements of the transformation matrix.
		double dx = this.transformationMatrix[0][3];
		double dy = this.transformationMatrix[1][3];
		double distance = this.transformationMatrix[2][3];
		this.transformationMatrix[0] = new double[] { 1.0, 0.0, 0.0, dx };
		this.transformationMatrix[1] = new double[] { 0.0, 1.0, 0.0, dy };
		this.transformationMatrix[2] = new double[] { 0.0, 0.0, 1.0, distance };
		this.transformationMatrix[3] = new double[] { 0.0, 0.0, 0.0, 1.0 };

		// Create a matrix for temporary storing updated values of the transformation matrix.
		double[][] updatedMatrix = new double[3][3];

		// Act the rotation matrix to the transformation matrix.
		double[][] r = rotationMatrix;
		double[][] m = this.transformationMatrix;
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				updatedMatrix[i][j] = r[i][0] * m[0][j] + r[i][1] * m[1][j] + r[i][2] * m[2][j];
			}
		}
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				this.transformationMatrix[i][j] = updatedMatrix[i][j];
			}
		}
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

		// Turn on the flag for detecting that the content of the graph screen has been updated.
		this.screenUpdated = true;
	}


	/**
	 * Renders the graph on the screen.
	 */
	@Override
	public synchronized void render() {

		// Update the screen dimension.
		int screenWidth = this.screenImage.getWidth();
		int screenHeight = this.screenImage.getHeight();
		int screenOffsetX = this.config.getCameraConfiguration().getHorizontalCenterOffset();
		int screenOffsetY = this.config.getCameraConfiguration().getVerticalCenterOffset();
		double magnification = this.config.getCameraConfiguration().getMagnification();

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

		// Turn on the flag for detecting that the content of the graph screen has been updated.
		this.screenUpdated = true;
	}


	@Override
	public synchronized void drawPoint(double x, double y, double z, 
			double radius) {

		RinearnGraph3DDrawingParameter parameter = new RinearnGraph3DDrawingParameter();
		parameter.setAutoColoringEnabled(true);
		parameter.setSeriesIndex(0);
		this.drawPoint(x, y, z, radius, parameter);
	}


	@Override
	public synchronized void drawPoint(double x, double y, double z, 
			double radius, Color color) {

		RinearnGraph3DDrawingParameter parameter = new RinearnGraph3DDrawingParameter();
		parameter.setAutoColoringEnabled(false);
		parameter.setColor(color);
		this.drawPoint(x, y, z, radius, parameter);
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

		// Generates the color based on the current color configuration.
		double[] colorRepresentCoords = {x, y, z};
		Color color = this.colorMixer.generateColor(colorRepresentCoords, parameter, this.config.getColorConfiguration());

		// Scale X/Y/Z coordinate values into the range [-1.0, 1.0] (= scaled space).
		if (parameter.isRangeScalingEnabled()) {
			x = this.axes[X].scaleCoordinate(x);
			y = this.axes[Y].scaleCoordinate(y);
			z = this.axes[Z].scaleCoordinate(z);
		}

		// Create a point piece and register to the list.
		PointGeometricPiece point = new PointGeometricPiece(x, y, z, radius, color);
		this.geometricPieceList.add(point);
	}


	@Override
	public synchronized void drawLine(double aX, double aY, double aZ, 
			double bX, double bY, double bZ, 
			double width) {

		RinearnGraph3DDrawingParameter parameter = new RinearnGraph3DDrawingParameter();
		parameter.setAutoColoringEnabled(true);
		parameter.setSeriesIndex(0);
		this.drawLine(aX, aY, aZ, bX, bY, bZ, width, parameter);
	}


	@Override
	public synchronized void drawLine(double aX, double aY, double aZ, 
			double bX, double bY, double bZ, 
			double width, Color color) {

		RinearnGraph3DDrawingParameter parameter = new RinearnGraph3DDrawingParameter();
		parameter.setAutoColoringEnabled(false);
		parameter.setColor(color);
		this.drawLine(aX, aY, aZ, bX, bY, bZ, width, parameter);
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

		// Generates the color based on the current color configuration.
		double[] colorRepresentCoords = {
				(aX + bX) / 2.0,
				(aY + bY) / 2.0,
				(aZ + bZ) / 2.0
		};
		Color color = this.colorMixer.generateColor(colorRepresentCoords, parameter, this.config.getColorConfiguration());

		// Scale X/Y/Z coordinate values into the range [-1.0, 1.0] (= scaled space).
		if (parameter.isRangeScalingEnabled()) {
			aX = this.axes[X].scaleCoordinate(aX);
			aY = this.axes[Y].scaleCoordinate(aY);
			aZ = this.axes[Z].scaleCoordinate(aZ);

			bX = this.axes[X].scaleCoordinate(bX);
			bY = this.axes[Y].scaleCoordinate(bY);
			bZ = this.axes[Z].scaleCoordinate(bZ);
		}

		// Create a line piece and register to the list.
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

		RinearnGraph3DDrawingParameter parameter = new RinearnGraph3DDrawingParameter();
		parameter.setAutoColoringEnabled(true);
		parameter.setSeriesIndex(0);
		this.drawQuadrangle(aX, aY, aZ, bX, bY, bZ, cX, cY, cZ, dX, dY, dZ, parameter);
	}


	@Override
	public synchronized void drawQuadrangle(double aX, double aY, double aZ, 
			double bX, double bY, double bZ, 
			double cX, double cY, double cZ, 
			double dX, double dY, double dZ, 
			Color color) {

		RinearnGraph3DDrawingParameter parameter = new RinearnGraph3DDrawingParameter();
		parameter.setAutoColoringEnabled(false);
		parameter.setColor(color);
		this.drawQuadrangle(aX, aY, aZ, bX, bY, bZ, cX, cY, cZ, dX, dY, dZ, parameter);
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

		// Generates the color based on the current color configuration.
		double[] colorRepresentCoords = {
				(aX + bX + cX + dX) / 4.0,
				(aY + bY + cY + dY) / 4.0,
				(aZ + bZ + cZ + dZ) / 4.0
		};
		Color color = this.colorMixer.generateColor(colorRepresentCoords, parameter, this.config.getColorConfiguration());

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

		// Create a quadrangle piece and register to the list.
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
		this.axes[Z].setTicks(zTickCoords, zTickLabels);

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
	@Override
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

		// Turn on the flag for detecting that the graph screen has been resized.
		this.screenResized = true;
	}


	/**
	 * Returns the Image instance storing the rendered image of the graph screen.
	 * 
	 * @return The rendered image of the graph screen.
	 */
	@Override
	public synchronized Image getScreenImage() {
		return this.screenImage;
	}


	/**
	 * References the value of the flag representing whether the content of the graph screen has been updated,
	 * in addition. and performs Compare-and-Swap (CAS) operation to it.
	 * 
	 * Specifically, when the value of the flag equals to the value passed as "fromValue" argument,
	 * overwrite the flag by the value passed as "toValue" argument.
	 * In addition, regardless whether the above is performed,
	 * this method returns the original (non modified) value of the flag as a return value.
	 * 
	 * The followings are typical examples using this method:
	 * For referring the value of the flag, and resetting it to the false, do "flag = casScreenUpdated(true, false)".
	 * For referring the value of the flag, without resetting it, do "flag = casScreenUpdated(true, true)" or "...(false, false)".
	 * For putting up the flag, do "casScreenUpdated(false, true)".
	 * 
	 * An app-side thread refers this flag periodically, and updates the window if it is true, and then resets the flag to false.
	 * However, user's code running on an other thread may call render() method,
	 * and the updating of the flag caused by it may conflict to the above.
	 * Hence, operations for referencing and changing the value of this flag must be performed atomically, through this method.
	 * 
	 * @param fromValue The value to be swapped by "toValue"
	 * @param toValue The swapped value
	 * @return Unmodified flag value (true if the content of the graph screen has been updated)
	 */
	@Override
	public synchronized boolean casScreenUpdated(boolean fromValue, boolean toValue) {
		boolean unmodifiedValue = this.screenUpdated;
		if (this.screenUpdated == fromValue) {
			this.screenUpdated = toValue;
		}
		return unmodifiedValue;
	}


	/**
	 * <span class="lang-en">
	 * References the value of the flag representing whether the graph screen has been resized,
	 * in addition. and performs Compare-and-Swap (CAS) operation to it.
	 * 
	 * Specifically, when the value of the flag equals to the value passed as "fromValue" argument,
	 * overwrite the flag by the value passed as "toValue" argument.
	 * In addition, regardless whether the above is performed,
	 * this method returns the original (non modified) value of the flag as a return value.
	 * 
	 * For usage examples, and why we design this flag's operations as a CAS operation,
	 * see the description of "casScreenUpdated()" method.
	 * 
	 * @param fromValue The value to be swapped by "toValue"
	 * @param toValue The swapped value
	 * @return Unmodified flag value (true if the graph screen has been resized)
	 */
	@Override
	public synchronized boolean casScreenResized(boolean fromValue, boolean toValue) {
		boolean unmodifiedValue = this.screenResized;
		if (this.screenResized == fromValue) {
			this.screenResized = toValue;
		}
		return unmodifiedValue;
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
