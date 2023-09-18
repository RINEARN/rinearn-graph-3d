package com.rinearn.graph3d.renderer.simple;

import com.rinearn.graph3d.renderer.RinearnGraph3DDrawingParameter;
import com.rinearn.graph3d.config.RangeConfiguration;
import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;

import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.math.BigDecimal;


/**
 * The class for drawing tick labels/lines of X/Y/Z axes's scales.
 */
public final class ScaleTickDrawer {

	/** The array index representing X, in three-dimensional arrays. */
	public static final int X = 0;

	/** The array index representing Y, in three-dimensional arrays. */
	public static final int Y = 1;

	/** The array index representing Z, in three-dimensional arrays. */
	public static final int Z = 2;

	/** The vertical alignment mode of tick labels. */
	private static final RinearnGraph3DDrawingParameter.VerticalAlignment VERTICAL_ALIGNMENT
			= RinearnGraph3DDrawingParameter.VerticalAlignment.RADIAL;

	/** The horizontal alignment mode of tick labels. */
	private static final RinearnGraph3DDrawingParameter.HorizontalAlignment HORIZONTAL_ALIGNMENT
			= RinearnGraph3DDrawingParameter.HorizontalAlignment.RADIAL;

	/** Stores the configuration of this application. */
	RinearnGraph3DConfiguration config;

	/** The vertical distance [px] from the reference point, at which the alignment of tick labels change. */
	private int verticalAlignThreshold;

	/** The horizontal distance [px] from the reference point, at which the alignment of tick labels change. */
	private int horizontalAlignThreshold;

	/** The font for rendering texts of tick labels. */
	private volatile Font font;

	/** The coordinates of the ticks on X axis. */
	private BigDecimal[] xTickCoordinates = {};

	/** The coordinates of the ticks on Y axis. */
	private BigDecimal[] yTickCoordinates = {};

	/** The coordinates of the ticks on Z axis. */
	private BigDecimal[] zTickCoordinates = {};

	/** The labels of the ticks on X axis. */
	private String[] xTickLabels = {};

	/** The labels of the ticks on Y axis. */
	private String[] yTickLabels = {};

	/** The labels of the ticks on Z axis. */
	private String[] zTickLabels = {};


	/**
	 * Create an instance for drawing scale ticks under the specified settings.
	 * 
	 * @param configuration The configuration of this application.
	 * @param vertcalAlignThreshold The vertical distance [px] from the reference point, at which the alignment of tick labels change.
	 * @param horizontalAlignThreshold The horizontal distance [px] from the reference point, at which the alignment of tick labels change.
	 * @param font The font for rendering texts of tick labels.
	 * @param color The color of tick labels and lines.
	 */
	public ScaleTickDrawer(RinearnGraph3DConfiguration configuration,
			int verticalAlignThreshold, int horizontalAlignThreshold,
			Font font) {

		// Note: first four parameters should be packed into an object, e.g.:
		//     public ScaleTickDrawer(ScaleConfiguration config, Font font, Color color)

		this.setConfiguration(configuration);
		this.verticalAlignThreshold = verticalAlignThreshold;
		this.horizontalAlignThreshold = horizontalAlignThreshold;
		this.font = font;
	}


	/**
	 * Sets the configuration.
	 * 
	 * @param config The configuration.
	 */
	public synchronized void setConfiguration(RinearnGraph3DConfiguration configuration) {
		this.config = configuration;

		if (!configuration.hasScaleConfiguration()) {
			throw new IllegalArgumentException("No scale configuration is stored in the specified configuration.");
		}
		if (!configuration.hasRangeConfiguration()) {
			throw new IllegalArgumentException("No range configuration is stored in the specified configuration.");			
		}
		if (!configuration.hasColorConfiguration()) {
			throw new IllegalArgumentException("No color configuration is stored in the specified configuration.");			
		}
	}


	/**
	 * Sets the coordinates of the ticks on X, Y, and Z axes.
	 * 
	 * @param xTickCoordinates The coordinates of the ticks on X axis.
	 * @param yTickCoordinates The coordinates of the ticks on Y axis.
	 * @param zTickCoordinates The coordinates of the ticks on Z axis.
	 */
	public synchronized void setTickCoordinates(
			BigDecimal[] xTickCoordinates, BigDecimal[] yTickCoordinates, BigDecimal[] zTickCoordinates) {

		this.xTickCoordinates = xTickCoordinates;
		this.yTickCoordinates = yTickCoordinates;
		this.zTickCoordinates = zTickCoordinates;
	}


	/**
	 * Sets the labels of the ticks on X, Y, and Z axes.
	 * 
	 * @param xTickLabels The labels of the ticks on X axis.
	 * @param yTickLabels The labels of the ticks on Y axis.
	 * @param zTickLabels The labels of the ticks on Z axis.
	 */
	public synchronized void setTickLabels(
			String[] xTickLabels, String[] yTickLabels, String[] zTickLabels) {

		this.xTickLabels = xTickLabels;
		this.yTickLabels = yTickLabels;
		this.zTickLabels = zTickLabels;
	}


	/**
	 * Sets the threshold distances at which the alignment of tick labels change.
	 * 
	 * @param verticalAlignThreshold The threshold of the vertical distance [px] from the reference point.
	 * @param horizontalAlignThreshold The threshold of the horizontal distance [px] from the reference point.
	 */
	public synchronized void setAlignmentThresholds(int verticalAlignThreshold, int horizontalAlignThreshold) {
		this.verticalAlignThreshold = verticalAlignThreshold;
		this.horizontalAlignThreshold = horizontalAlignThreshold;
	}

	/**
	 * Sets the font for rendering texts of tick labels.
	 * 
	 * @param font The font for rendering texts of tick labels.
	 */
	public synchronized void setFont(Font font) {
		this.font = font;
	}


	/**
	 * Draws tick lines and tick labels on all axes.
	 * 
	 * @param geometricPieceList The list for storing the geometric pieces of the drawn contents by this method.
	 * @param axes The array storing X axis at [0], Y axis at [1], and Z axis at [2].
	 */
	public synchronized void drawScaleTicks(List<GeometricPiece> geometricPieceList) {
		RangeConfiguration rangeConfig = this.config.getRangeConfiguration();
		RangeConfiguration.AxisRangeConfiguration xRangeConfig = rangeConfig.getXRangeConfiguration();
		RangeConfiguration.AxisRangeConfiguration yRangeConfig = rangeConfig.getYRangeConfiguration();
		RangeConfiguration.AxisRangeConfiguration zRangeConfig = rangeConfig.getZRangeConfiguration();

		SpaceConverter xSpaceConverter = new SpaceConverter(xRangeConfig.getMinimum(), xRangeConfig.getMaximum());
		SpaceConverter ySpaceConverter = new SpaceConverter(yRangeConfig.getMinimum(), yRangeConfig.getMaximum());
		SpaceConverter zSpaceConverter = new SpaceConverter(zRangeConfig.getMinimum(), zRangeConfig.getMaximum());

		// Draw ticks on X axis.
		int xTickCount = this.xTickCoordinates.length;
		for (int itick=0; itick<xTickCount; itick++) {
			double scaledCoord = xSpaceConverter.toScaledSpaceCoordinate(this.xTickCoordinates[itick]).doubleValue();
			this.drawXScaleTickLabels(geometricPieceList, scaledCoord, this.xTickLabels[itick]);
			this.drawXScaleTickLines(geometricPieceList, scaledCoord);
		}

		// Draw ticks on Y axis.
		int yTickCount = this.yTickCoordinates.length;
		for (int itick=0; itick<yTickCount; itick++) {
			double scaledCoord = ySpaceConverter.toScaledSpaceCoordinate(this.yTickCoordinates[itick]).doubleValue();
			this.drawYScaleTickLabels(geometricPieceList, scaledCoord, this.yTickLabels[itick]);
			this.drawYScaleTickLines(geometricPieceList, scaledCoord);
		}

		// Draw ticks on Z axis.
		int zTickCount = this.zTickCoordinates.length;
		for (int itick=0; itick<zTickCount; itick++) {
			double scaledCoord = zSpaceConverter.toScaledSpaceCoordinate(this.zTickCoordinates[itick]).doubleValue();
			this.drawZScaleTickLabels(geometricPieceList, scaledCoord, this.zTickLabels[itick]);
			this.drawZScaleTickLines(geometricPieceList, scaledCoord);
		}
	}


	/**
	 * On all (four) X axis's scale, draws tick labels having the specified value.
	 * 
	 * @param geometricPieceList The list for storing the geometric pieces of the drawn contents by this method.
	 * @param scaledCoord The X coordinate value (position) of the tick labels.
	 * @param tickLabel The label (displayed value) of the tick labels.
	 */
	private void drawXScaleTickLabels(List<GeometricPiece> geometricPieceList, double scaledCoord, String tickLabel) {

		// Define short aliases of some fields:
		RinearnGraph3DDrawingParameter.VerticalAlignment vAlign   = VERTICAL_ALIGNMENT;
		RinearnGraph3DDrawingParameter.HorizontalAlignment hAlign = HORIZONTAL_ALIGNMENT;
		int vThreshold   = this.verticalAlignThreshold;
		int hThreshold = this.horizontalAlignThreshold;
		double margin = this.config.getScaleConfiguration().getXScaleConfiguration().getTickLabelMargin();
		Color color = this.config.getColorConfiguration().getForegroundColor();

		// X axis at Y=1, Z=1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				scaledCoord, 1.0 + margin, 1.0 + margin,  // Coords of the rendered point
				0.0, 1.0, 1.0,                            // Coords of the alignment reference point
				new double[][]{ {0.0, 1.0, 0.0}, {0.0, 0.0, -1.0} },  // Directional vectors
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, color  // Other params
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				scaledCoord, 1.0 + margin, 1.0 + margin,
				0.0, 1.0, 1.0,
				new double[][]{ {0.0, -1.0, 0.0}, {0.0, 0.0, 1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, color
		));

		// X axis at Y=1, Z=-1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				scaledCoord, 1.0 + margin, -1.0 - margin,
				0.0, 1.0, -1.0,
				new double[][]{ {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, color
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				scaledCoord, 1.0 + margin, -1.0 - margin,
				0.0, 1.0, -1.0,
				new double[][]{ {0.0, -1.0, 0.0}, {0.0, 0.0, -1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, color
		));

		// X axis at Y=-1, Z=1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				scaledCoord, -1.0 - margin, 1.0 + margin,
				0.0, -1.0, 1.0,
				new double[][]{ {0.0, -1.0, 0.0}, {0.0, 0.0, -1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, color
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				scaledCoord, -1.0 - margin, 1.0 + margin,
				0.0, -1.0, 1.0,
				new double[][]{ {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, color
		));

		// X axis at Y=-1, Z=-1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				scaledCoord, -1.0 - margin, -1.0 - margin,
				0.0, -1.0, -1.0,
				new double[][]{ {0.0, -1.0, 0.0}, {0.0, 0.0, 1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, color
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				scaledCoord, -1.0 - margin, -1.0 - margin,
				0.0, -1.0, -1.0,
				new double[][]{ {0.0, 1.0, 0.0}, {0.0, 0.0, -1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, color
		));
	}


	/**
	 * On all (four) X axis's scale, draws tick lines having the specified value.
	 * 
	 * @param geometricPieceList The list for storing the geometric pieces of the drawn contents by this method.
	 * @param scaledCoord The X coordinate value (position) of the tick lines.
	 */
	private void drawXScaleTickLines(List<GeometricPiece> geometricPieceList, double scaledCoord) {

		double length = this.config.getScaleConfiguration().getXScaleConfiguration().getTickLineLength();
		double lineWidth = 1.0;
		Color color = this.config.getColorConfiguration().getForegroundColor();

		// X axis at Y=1, Z=1
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				scaledCoord, 1.0, 1.0,                                // Coords of the edge point A
				scaledCoord, 1.0 + length, 1.0 + length,              // Coords of the edge point B
				new double[][]{ {0.0, 1.0, 0.0}, {0.0, 0.0, -1.0} },  // Directional vectors
				lineWidth, color  // Other params
		));
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				scaledCoord, 1.0, 1.0,
				scaledCoord, 1.0 + length, 1.0 + length,
				new double[][]{ {0.0, -1.0, 0.0}, {0.0, 0.0, 1.0} },
				lineWidth, color
		));

		// X axis at Y=1, Z=-1
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				scaledCoord, 1.0, -1.0,
				scaledCoord, 1.0 + length, -1.0 - length,
				new double[][]{ {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0} },
				lineWidth, color
		));
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				scaledCoord, 1.0, -1.0,
				scaledCoord, 1.0 + length, -1.0 - length,
				new double[][]{ {0.0, -1.0, 0.0}, {0.0, 0.0, -1.0} },
				lineWidth, color
		));

		// X axis at Y=-1, Z=1
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				scaledCoord, -1.0, 1.0,
				scaledCoord, -1.0 - length, 1.0 + length,
				new double[][]{ {0.0, -1.0, 0.0}, {0.0, 0.0, -1.0} },
				lineWidth, color
		));
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				scaledCoord, -1.0, 1.0,
				scaledCoord, -1.0 - length, 1.0 + length,
				new double[][]{ {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0} },
				lineWidth, color
		));

		// X axis at Y=-1, Z=-1
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				scaledCoord, -1.0, -1.0,
				scaledCoord, -1.0 - length, -1.0 - length,
				new double[][]{ {0.0, -1.0, 0.0}, {0.0, 0.0, 1.0} },
				lineWidth, color
		));
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				scaledCoord, -1.0, -1.0,
				scaledCoord, -1.0 - length, -1.0 - length,
				new double[][]{ {0.0, 1.0, 0.0}, {0.0, 0.0, -1.0} },
				lineWidth, color
		));
	}


	/**
	 * On all (four) Y axis's scale, draws tick labels having the specified value.
	 * 
	 * @param geometricPieceList The list for storing the geometric pieces of the drawn contents by this method.
	 * @param scaledCoord The Y coordinate value (position) of the tick labels.
	 * @param tickLabel The label (displayed value) of the tick labels.
	 */
	private void drawYScaleTickLabels(List<GeometricPiece> geometricPieceList, double scaledCoord, String tickLabel) {

		// Define short aliases of some fields:
		RinearnGraph3DDrawingParameter.VerticalAlignment vAlign   = VERTICAL_ALIGNMENT;
		RinearnGraph3DDrawingParameter.HorizontalAlignment hAlign = HORIZONTAL_ALIGNMENT;
		int vThreshold   = this.verticalAlignThreshold;
		int hThreshold = this.horizontalAlignThreshold;
		double margin = this.config.getScaleConfiguration().getYScaleConfiguration().getTickLabelMargin();
		Color color = this.config.getColorConfiguration().getForegroundColor();

		// Y axis at X=1, Z=1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				1.0 + margin, scaledCoord, 1.0 + margin,  // Coords of the rendered point
				1.0, 0.0, 1.0,                            // Coords of the alignment reference point
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, 0.0, -1.0} },  // Directional vectors
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, color  // Other params
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				1.0 + margin, scaledCoord, 1.0 + margin,
				1.0, 0.0, 1.0,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 0.0, 1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, color
		));

		// Y axis at X=1, Z=-1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				1.0 + margin, scaledCoord, -1.0 - margin,
				1.0, 0.0, -1.0,
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, 0.0, 1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, color
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				1.0 + margin, scaledCoord, -1.0 - margin,
				1.0, 0.0, -1.0,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 0.0, -1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, color
		));

		// Y axis at X=-1, Z=1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				-1.0 - margin, scaledCoord, 1.0 + margin,
				-1.0, 0.0, 1.0,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 0.0, -1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, color
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				-1.0 - margin, scaledCoord, 1.0 + margin,
				-1.0, 0.0, 1.0,
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, 0.0, 1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, color
		));

		// Y axis at X=-1, Z=-1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				-1.0 - margin, scaledCoord, -1.0 - margin,
				-1.0, 0.0, -1.0,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 0.0, 1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, color
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				-1.0 - margin, scaledCoord, -1.0 - margin,
				-1.0, 0.0, -1.0,
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, 0.0, -1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, color
		));
	}


	/**
	 * On all (four) X axis's scale, draws tick lines having the specified value.
	 * 
	 * @param geometricPieceList The list for storing the geometric pieces of the drawn contents by this method.
	 * @param scaledCoord The Y coordinate value (position) of the tick lines.
	 */
	private void drawYScaleTickLines(List<GeometricPiece> geometricPieceList, double scaledCoord) {

		double length = this.config.getScaleConfiguration().getYScaleConfiguration().getTickLineLength();
		double lineWidth = 1.0;
		Color color = this.config.getColorConfiguration().getForegroundColor();

		// Y axis at X=1, Z=1
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				1.0, scaledCoord, 1.0,                                // Coords of the edge point A
				1.0 + length, scaledCoord, 1.0 + length,              // Coords of the edge point B
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, 0.0, -1.0} },  // Directional vectors
				lineWidth, color  // Other params
		));
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				1.0, scaledCoord, 1.0,
				1.0 + length, scaledCoord, 1.0 + length,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 0.0, 1.0} },
				lineWidth, color
		));

		// Y axis at X=1, Z=-1
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				1.0, scaledCoord, -1.0,
				1.0 + length, scaledCoord, -1.0 - length,
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, 0.0, 1.0} },
				lineWidth, color
		));
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				1.0, scaledCoord, -1.0,
				1.0 + length, scaledCoord, -1.0 - length,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 0.0, -1.0} },
				lineWidth, color
		));

		// Y axis at X=-1, Z=1
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				-1.0, scaledCoord, 1.0,
				-1.0 - length, scaledCoord, 1.0 + length,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 0.0, -1.0} },
				lineWidth, color
		));
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				-1.0, scaledCoord, 1.0,
				-1.0 - length, scaledCoord, 1.0 + length,
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, 0.0, 1.0} },
				lineWidth, color
		));

		// Y axis at X=-1, Z=-1
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				-1.0, scaledCoord, -1.0,
				-1.0 - length, scaledCoord, -1.0 - length,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 0.0, 1.0} },
				lineWidth, color
		));
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				-1.0, scaledCoord, -1.0,
				-1.0 - length, scaledCoord, -1.0 - length,
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, 0.0, -1.0} },
				lineWidth, color
		));
	}


	/**
	 * On all (four) Z axis's scale, draws tick labels having the specified value.
	 * 
	 * @param geometricPieceList The list for storing the geometric pieces of the drawn contents by this method.
	 * @param scaledCoord The Z coordinate value (position) of the tick labels.
	 * @param tickLabel The label (displayed value) of the tick labels.
	 */
	private void drawZScaleTickLabels(List<GeometricPiece> geometricPieceList, double scaledCoord, String tickLabel) {

		// Define short aliases of some fields:
		RinearnGraph3DDrawingParameter.VerticalAlignment vAlign   = VERTICAL_ALIGNMENT;
		RinearnGraph3DDrawingParameter.HorizontalAlignment hAlign = HORIZONTAL_ALIGNMENT;
		int vThreshold   = this.verticalAlignThreshold;
		int hThreshold = this.horizontalAlignThreshold;
		double margin = this.config.getScaleConfiguration().getZScaleConfiguration().getTickLabelMargin();
		Color color = this.config.getColorConfiguration().getForegroundColor();

		// Z axis at Y=1, Z=1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				1.0 + margin, 1.0 + margin, scaledCoord, // Coords of the rendered point
				1.0, 1.0, 0.0,                           // Coords of the alignment reference point
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, -1.0, 0.0} }, // Directional vectors
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, color // Other params
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				1.0 + margin, 1.0 + margin, scaledCoord,
				1.0, 1.0, 0.0,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 1.0, 0.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, color
		));

		// Z axis at Y=1, Z=-1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				1.0 + margin, -1.0 - margin, scaledCoord,
				1.0, -1.0, 0.0,
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, 1.0, 0.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, color
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				1.0 + margin, -1.0 - margin, scaledCoord,
				1.0, -1.0, 0.0,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, -1.0, 0.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, color
		));

		// Z axis at Y=-1, Z=1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				-1.0 - margin, 1.0 + margin, scaledCoord,
				-1.0, 1.0, 0.0,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, -1.0, 0.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, color
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				-1.0 - margin, 1.0 + margin, scaledCoord,
				-1.0, 1.0, 0.0,
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, 1.0, 0.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, color
		));

		// Z axis at Y=-1, Z=-1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				-1.0 - margin, -1.0 - margin, scaledCoord,
				-1.0, -1.0, 0.0,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 1.0, 0.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, color
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				-1.0 - margin, -1.0 - margin, scaledCoord,
				-1.0, -1.0, 0.0,
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, -1.0, 0.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, color
		));
	}


	/**
	 * On all (four) X axis's scale, draws tick lines having the specified value.
	 * 
	 * @param geometricPieceList The list for storing the geometric pieces of the drawn contents by this method.
	 * @param scaledCoord The Z coordinate value (position) of the tick lines.
	 */
	private void drawZScaleTickLines(List<GeometricPiece> geometricPieceList, double scaledCoord) {

		double length = this.config.getScaleConfiguration().getZScaleConfiguration().getTickLineLength();
		double lineWidth = 1.0;
		Color color = this.config.getColorConfiguration().getForegroundColor();

		// Z axis at Y=1, Z=1
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				1.0, 1.0, scaledCoord,                               // Coords of the edge point A
				1.0 + length, 1.0 + length, scaledCoord,             // Coords of the edge point B
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, -1.0, 0.0} }, // Directional vectors
				lineWidth, color  // Other params
		));
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				1.0, 1.0, scaledCoord,
				1.0 + length, 1.0 + length, scaledCoord,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 1.0, 0.0} },
				lineWidth, color
		));

		// Z axis at Y=1, Z=-1
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				1.0, -1.0, scaledCoord,
				1.0 + length, -1.0 - length, scaledCoord,
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, 1.0, 0.0} },
				lineWidth, color
		));
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				1.0, -1.0, scaledCoord,
				1.0 + length, -1.0 - length, scaledCoord,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, -1.0, 0.0} },
				lineWidth, color
		));

		// Z axis at Y=-1, Z=1
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				-1.0, 1.0, scaledCoord,
				-1.0 - length, 1.0 + length, scaledCoord,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, -1.0, 0.0} },
				lineWidth, color
		));
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				-1.0, 1.0, scaledCoord,
				-1.0 - length, 1.0 + length, scaledCoord,
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, 1.0, 0.0} },
				lineWidth, color
		));

		// Z axis at Y=-1, Z=-1
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				-1.0, -1.0, scaledCoord,
				-1.0 - length, -1.0 - length, scaledCoord,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 1.0, 0.0} },
				lineWidth, color
		));
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				-1.0, -1.0, scaledCoord,
				-1.0 - length, -1.0 - length, scaledCoord,
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, -1.0, 0.0} },
				lineWidth, color
		));
	}
}
