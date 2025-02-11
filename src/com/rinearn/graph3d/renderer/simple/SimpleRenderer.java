package com.rinearn.graph3d.renderer.simple;

import com.rinearn.graph3d.renderer.RinearnGraph3DRenderer;
import com.rinearn.graph3d.renderer.RinearnGraph3DDrawingParameter;
import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.RangeConfiguration;
import com.rinearn.graph3d.config.CameraConfiguration;

import java.util.List;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.RenderingHints;


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

// !!! NOTE !!!
// Zoom in/out した時にアラインメントの threshold 値を変えるやつ実装しないと。
// !!! NOTE !!!

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


	// !!! WARNING !!!
	// - About the following field "config" -
	//
	//     Don't share the same configuration container with Model layer.
	//     Because contents of the configuration container stored in this class
	//     may be modified from the outside through configure(...) API.
	//     If share the same config container with Model layer,
	//     the changes of the config through the above propagates to the config of Model layer.

	/** The container of configuration parameters (such as lighting/shading params, scales setting params, and so on). */
	private final RinearnGraph3DConfiguration config = RinearnGraph3DConfiguration.createDefaultConfiguration();

	/** The converters of coordinates from the real space to the "scaled space". The index is [0:X, 1:Y, 2:Z]. */
	private final SpaceConverter[] spaceConverters = {new SpaceConverter(), new SpaceConverter(), new SpaceConverter()};

	// Temporary settings: Should be packed into this.config.scaleConfiguration?
	//     -> No, because they vary dynamically depending on zoom in/out of magnification. They are not static settings.
	int verticalAlignThreshold = 128;
	int horizontalAlignThreshold = 32;

	/** The object to the draw scale ticks of the X/Y/Z axes. */
	private final ScaleTickDrawer scaleTickDrawer = new ScaleTickDrawer(
		this.verticalAlignThreshold, this.horizontalAlignThreshold
	);

	/** The object to draw the axis labels. */
	private final AxisLabelDrawer labelDrawer = new AxisLabelDrawer(
		this.verticalAlignThreshold, this.horizontalAlignThreshold
	);

	/** The object to draw the graph frame and the grid lines. */
	private final FrameDrawer frameDrawer = new FrameDrawer();

	/** The object to draw the legends. */
	private final LegendDrawer legendDrawer = new LegendDrawer();

	/** The object to draw the color bar. */
	private final ColorBarDrawer colorBarDrawer = new ColorBarDrawer();

	/** The color mixer, which generates colors of geometric pieces (points, lines, and so on). */
	private final ColorMixer colorMixer = new ColorMixer();


	/** The Image instance storing the rendered image of the graph screen. */
	private volatile BufferedImage screenImage = null;

	/** The Graphics2D instance to draw the graph screen. */
	private volatile Graphics2D screenGraphics = null;

	/** The flag representing whether the graph screen has been resized. */
	private volatile boolean screenUpdated = false;

	/** The flag representing whether the content of the graph screen has been updated. */
	private volatile boolean screenResized = false;

	/** The list storing geometric pieces to be rendered. */
	private volatile List<GeometricPiece> geometricPieceList = new ArrayList<GeometricPiece>();

	/** The transformation matrix from the graph coordinate system to the view coordinate system. */
	private volatile double[][] transformationMatrix = {
		{ 1.0, 0.0, 0.0, 0.0 },
		{ 0.0, 1.0, 0.0, 0.0 },
		{ 0.0, 0.0, 1.0, 0.0 },
		{ 0.0, 0.0, 0.0, 1.0 }
	};


	/**
	 * Creates a new renderer.
	 */
	public SimpleRenderer() {
		this.configure(this.config);
		this.clear();
	}


	/**
	 * Configures the state of this renderer, by parameters stored in the specified configuration container.
	 *
	 * Note that, the changes of the configuration (contains ranges of X/Y/Z axes)
	 * does not affect to the currently drawn contents (points, lines, quadrangles, and so on).
	 * To reflect the changes, please clear() and re-draw all contents again.
	 *
	 * @param configuration The container storing configuration parameters.
	 * @throws IllegalArgumentException
	 *     Thrown when incorrect or inconsistent settings are detected in the specified configuration.
	 */
	public synchronized void configure(RinearnGraph3DConfiguration configuration) {

		// Don't replace the reference of "config" field of this instance.
		// Only modify its contents.

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

		// Set the ranges of X/Y/Z axes, to the range converter for each axis.
		RangeConfiguration rangeConfig = this.config.getRangeConfiguration();
		RangeConfiguration.AxisRangeConfiguration xRangeConfig = rangeConfig.getXRangeConfiguration();
		RangeConfiguration.AxisRangeConfiguration yRangeConfig = rangeConfig.getYRangeConfiguration();
		RangeConfiguration.AxisRangeConfiguration zRangeConfig = rangeConfig.getZRangeConfiguration();
		this.spaceConverters[X].setRange(xRangeConfig.getMinimum(), xRangeConfig.getMaximum());
		this.spaceConverters[Y].setRange(yRangeConfig.getMinimum(), yRangeConfig.getMaximum());
		this.spaceConverters[Z].setRange(zRangeConfig.getMinimum(), zRangeConfig.getMaximum());

		// Generate tick coordinates and tick labels from the configuration.
		ScaleTickGenerator.Result scaleTicks = ScaleTickGenerator.generateTicks(this.config);

		// Sets the configuration for drawing scales, frames, etc.
		this.scaleTickDrawer.setConfiguration(this.config, scaleTicks);
		this.frameDrawer.setConfiguration(this.config, scaleTicks);
		this.labelDrawer.setConfiguration(this.config, scaleTicks);
		this.colorBarDrawer.setConfiguration(this.config, scaleTicks);
		this.legendDrawer.setConfiguration(this.config);

		// Update the camera angles and parameters.
		this.updateCamera();
	}


	/**
	 * Updates the camera angle and parameters, from the current camera configuration.
	 */
	private void updateCamera() {
		CameraConfiguration cameraConfig = this.config.getCameraConfiguration();
		double[][] rotationMatrix = cameraConfig.getRotationMatrix();

		// Resets the rotation-related elements of the transformation matrix.
		double dx = this.transformationMatrix[0][3];
		double dy = this.transformationMatrix[1][3];
		double distance = cameraConfig.getDistance();
		this.transformationMatrix[0] = new double[] { 1.0, 0.0, 0.0, dx };
		this.transformationMatrix[1] = new double[] { 0.0, 1.0, 0.0, dy };
		this.transformationMatrix[2] = new double[] { 0.0, 0.0, 1.0, -distance }; // Z takes a negative value for the depth direction.
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

		// Update the size of the screen.
		int updatedScreenWidth = cameraConfig.getScreenWidth();
		int updatedScreenHeight = cameraConfig.getScreenHeight();
		if (this.screenImage == null ||
				this.screenImage.getWidth() != updatedScreenWidth ||
				this.screenImage.getHeight() != updatedScreenHeight) {

			this.setScreenSize(updatedScreenWidth, updatedScreenHeight);
		}
	}


	/**
	 * Performs something temporary for the development and the debuggings.
	 */
	public synchronized void temporaryExam() {
	}


	/**
	 * Disposes all the disposable resources in this renderer instance.
	 */
	@Override
	public synchronized void dispose() {
		this.screenImage = null;
		this.screenGraphics.dispose();
		this.geometricPieceList.clear();
		this.transformationMatrix = null;

		System.gc();
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
		this.screenGraphics.setBackground(this.config.getColorConfiguration().getBackgroundColor());
		this.screenGraphics.clearRect(0, 0, this.screenImage.getWidth(), this.screenImage.getHeight());

		// Turn on the flag for detecting that the content of the graph screen has been updated.
		this.screenUpdated = true;
	}


	/**
	 * Renders the graph on the screen.
	 */
	@Override
	public synchronized void render() {

		// Gets the quality mode, and turn on/off text-antialiasing option.
		CameraConfiguration.RenderingMode renderingMode = this.config.getCameraConfiguration().getRenderingMode();
		boolean isQualityMode = renderingMode == CameraConfiguration.RenderingMode.QUALITY;
		if (isQualityMode) {
			this.screenGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		} else {
			this.screenGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		}

		// Update the screen dimension.
		int screenWidth = this.screenImage.getWidth();
		int screenHeight = this.screenImage.getHeight();
		int screenOffsetX = this.config.getCameraConfiguration().getHorizontalCenterOffset();
		int screenOffsetY = this.config.getCameraConfiguration().getVerticalCenterOffset();
		double magnification = this.config.getCameraConfiguration().getMagnification();

		// Clear the graph screen.
		this.screenGraphics.setBackground(this.config.getColorConfiguration().getBackgroundColor());
		this.screenGraphics.clearRect(0, 0, screenWidth, screenHeight);

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

			// Turn on/off antialiasing option for drawing geometric shape, depending on the kind of the piece.
			if (isQualityMode && piece.isAntialiasingAvailable()) {
				this.screenGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			} else {
				this.screenGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
			}
			piece.project(screenWidth, screenHeight, screenOffsetX, screenOffsetY, magnification);
			piece.draw(this.screenGraphics);
		}

		// Draw legends.
		if (this.config.getLabelConfiguration().getLegendLabelConfiguration().isVisible()) {
			this.legendDrawer.draw(this.screenGraphics);
		}

		// Draw the color bar.
		if (this.config.getColorConfiguration().isDataGradientColorEnabled()) {
			this.colorBarDrawer.draw(this.screenGraphics, this.colorMixer);
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
					this.spaceConverters[X].containsInRange(x, true) &&
					this.spaceConverters[Y].containsInRange(y, true) &&
					this.spaceConverters[Z].containsInRange(z, true);

			if (!isInRange) {
				return;
			}
		}

		// Generates the color based on the current color configuration.
		double[] colorRepresentCoords = {x, y, z};
		Color color = this.colorMixer.generateColor(colorRepresentCoords, parameter, this.config.getColorConfiguration());

		// Scale X/Y/Z coordinate values into the range [-1.0, 1.0] (= scaled space).
		if (parameter.isRangeScalingEnabled()) {
			x = this.spaceConverters[X].toScaledSpaceCoordinate(x);
			y = this.spaceConverters[Y].toScaledSpaceCoordinate(y);
			z = this.spaceConverters[Z].toScaledSpaceCoordinate(z);
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
					this.spaceConverters[X].containsInRange(aX, true) &&
					this.spaceConverters[Y].containsInRange(aY, true) &&
					this.spaceConverters[Z].containsInRange(aZ, true) &&

					this.spaceConverters[X].containsInRange(bX, true) &&
					this.spaceConverters[Y].containsInRange(bY, true) &&
					this.spaceConverters[Z].containsInRange(bZ, true);

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
			aX = this.spaceConverters[X].toScaledSpaceCoordinate(aX);
			aY = this.spaceConverters[Y].toScaledSpaceCoordinate(aY);
			aZ = this.spaceConverters[Z].toScaledSpaceCoordinate(aZ);

			bX = this.spaceConverters[X].toScaledSpaceCoordinate(bX);
			bY = this.spaceConverters[Y].toScaledSpaceCoordinate(bY);
			bZ = this.spaceConverters[Z].toScaledSpaceCoordinate(bZ);
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
					this.spaceConverters[X].containsInRange(aX, true) &&
					this.spaceConverters[Y].containsInRange(aY, true) &&
					this.spaceConverters[Z].containsInRange(aZ, true) &&

					this.spaceConverters[X].containsInRange(bX, true) &&
					this.spaceConverters[Y].containsInRange(bY, true) &&
					this.spaceConverters[Z].containsInRange(bZ, true) &&

					this.spaceConverters[X].containsInRange(cX, true) &&
					this.spaceConverters[Y].containsInRange(cY, true) &&
					this.spaceConverters[Z].containsInRange(cZ, true) &&

					this.spaceConverters[X].containsInRange(dX, true) &&
					this.spaceConverters[Y].containsInRange(dY, true) &&
					this.spaceConverters[Z].containsInRange(dZ, true);

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
			aX = this.spaceConverters[X].toScaledSpaceCoordinate(aX);
			aY = this.spaceConverters[Y].toScaledSpaceCoordinate(aY);
			aZ = this.spaceConverters[Z].toScaledSpaceCoordinate(aZ);

			bX = this.spaceConverters[X].toScaledSpaceCoordinate(bX);
			bY = this.spaceConverters[Y].toScaledSpaceCoordinate(bY);
			bZ = this.spaceConverters[Z].toScaledSpaceCoordinate(bZ);

			cX = this.spaceConverters[X].toScaledSpaceCoordinate(cX);
			cY = this.spaceConverters[Y].toScaledSpaceCoordinate(cY);
			cZ = this.spaceConverters[Z].toScaledSpaceCoordinate(cZ);

			dX = this.spaceConverters[X].toScaledSpaceCoordinate(dX);
			dY = this.spaceConverters[Y].toScaledSpaceCoordinate(dY);
			dZ = this.spaceConverters[Z].toScaledSpaceCoordinate(dZ);
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
		this.scaleTickDrawer.drawScaleTicks(this.geometricPieceList);
	}


	/**
	 * Draws the grid lines on the backwalls of the graph.
	 */
	@Override
	public synchronized void drawGrid() {
		this.frameDrawer.drawGridLines(this.geometricPieceList);
	}


	@Override
	public synchronized void drawLabel() {
		this.screenGraphics.setFont(this.config.getFontConfiguration().getTickLabelFont());
		FontMetrics tickLabelFontMetrics = this.screenGraphics.getFontMetrics();
		this.labelDrawer.drawAxisLabels(this.geometricPieceList, tickLabelFontMetrics);
	}


	/**
	 * Sets the size of the graph screen.
	 *
	 * @param screenWidth The width (pixels) of the screen.
	 * @param screenHeight The height (pixels) of the screen.
	 */
	private synchronized void setScreenSize(int screenWidth, int screenHeight) {

		// If the image/graphics instances are already allocated, release them.
		if (this.screenGraphics != null) {
			this.screenGraphics.dispose();
			this.screenImage = null;
			System.gc();
		}

		// Allocate the image/graphics instances.
		this.screenImage = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
		this.screenGraphics = this.screenImage.createGraphics();

		// Clear the screen image by the background color.
		this.screenGraphics.setBackground(this.config.getColorConfiguration().getBackgroundColor());
		this.screenGraphics.clearRect(0, 0, screenWidth, screenHeight);

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
	 * Creates a deep copy of the current image of the graph screen.
	 *
	 * This method allocates a buffer (BufferedImage), and copies the content of the graph screen to it, and returns it.
	 * The allocation of the buffer requires a certain overhead cost, so if frequently copy the graph screen,
	 * consider using {SimpleRenderer.copyScreenImage(BufferedImage,Graphics2D) copyScreenImage(BufferedImage,Graphics2D)}
	 * method instead.
	 *
	 * Please note that,
	 * if the caller-side thread is interrupted (Thread#interrupt() is called) during the image is being copied,
	 * the copying process will be terminated prematurely, so the copied image may be imperfect in such case.
	 *
	 * @param bufferedImageType The type of the BufferedImage to be returned (e.g.: BufferedImage.TYPE_INT_ARGB, TYPE_INT_RGB, etc.)
	 * @return The deep copy of the current image of the graph screen.
	 */
	public synchronized BufferedImage copyScreenImage(int bufferedImageType) {

		// Gets the current size of the screen.
		int screenWidth = this.screenImage.getWidth();
		int screenHeight = this.screenImage.getHeight();

		// Create the buffer to store copied image, and the Graphics2D object to draw the image to the buffer.
		BufferedImage buffer = new BufferedImage(screenWidth, screenHeight, bufferedImageType);
		Graphics2D graphics = buffer.createGraphics();

		// Clear by the background color.
		graphics.setBackground(this.config.getColorConfiguration().getBackgroundColor());
		graphics.clearRect(0, 0, screenWidth, screenHeight);

		// Copy the image to the buffer.
		this.copyScreenImage(buffer, graphics);

		// Dispose the resources and return the buffer storing the copied screen image.
		graphics.dispose();
		return buffer;
	}


	/**
	 * Copies the content of the current image of the graph screen to the specified buffer, allocated by the caller-side.
	 *
	 * When the width/height of the buffer and the graph screen are different,
	 * the copy process will be performed between their overwrapping area, without raising any error.
	 *
	 * Please note that,
	 * if the caller-side thread is interrupted (Thread#interrupt() is called) during the image is being copied,
	 * the copying process may be terminated prematurely, so the copied image may be imperfect in such case.
	 *
	 * Also, this method does not clear the buffer automatically before copying the image,
	 * considering the use case that the overlay the graph on the other contents (which are drawn on the buffer beforehand).
	 * Hence, clear the buffer at the caller-side beforehand if necessary, using Graphics2D.clearRect() etc.
	 *
	 * @param buffer The buffer to which the current image of the screen will be copied.
	 * @param graphics The Graphics2D object to draw contents to the buffer.
	 */
	public synchronized void copyScreenImage(BufferedImage buffer, Graphics2D graphics) {

		// NOTE:
		//   This implementation does not use the argument "buffer" explicitly
		//   (because draws the image via the argument "graphics"),
		//   but don't remove the argument "buffer" from the arguments of this method of RinearnGraph3DRenderer interface.
		//   Other renderer implementations may use the "buffer", and may not use "graphics".
		//   It depends on the internal architecture of each implementation that which way is better.

		boolean completed = false;
		while (!completed) {
			completed = graphics.drawImage(this.screenImage, 0, 0, null);
			try {
				Thread.sleep(10);
			} catch (InterruptedException nfe) {
				break;
			}
		}
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
}
