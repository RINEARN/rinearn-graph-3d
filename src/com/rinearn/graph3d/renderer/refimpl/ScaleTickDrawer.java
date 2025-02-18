package com.rinearn.graph3d.renderer.refimpl;

import com.rinearn.graph3d.renderer.RinearnGraph3DDrawingParameter;
import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.range.RangeConfiguration;
import com.rinearn.graph3d.config.range.AxisRangeConfiguration;
import com.rinearn.graph3d.config.scale.ScaleConfiguration;
import com.rinearn.graph3d.config.scale.AxisScaleConfiguration;

import java.awt.Color;
import java.awt.Font;
import java.util.List;


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
	RinearnGraph3DConfiguration config = null;

	/** The scale ticks generated from the configuration. */
	private ScaleTickGenerator.Result scaleTicks = null;

	/** The vertical distance [px] from the reference point, at which the alignment of tick labels change. */
	private int verticalAlignThreshold;

	/** The horizontal distance [px] from the reference point, at which the alignment of tick labels change. */
	private int horizontalAlignThreshold;


	/**
	 * Create an instance for drawing scale ticks under the specified settings.
	 *
	 * @param vertcalAlignThreshold The vertical distance [px] from the reference point, at which the alignment of tick labels change.
	 * @param horizontalAlignThreshold The horizontal distance [px] from the reference point, at which the alignment of tick labels change.
	 */
	public ScaleTickDrawer(int verticalAlignThreshold, int horizontalAlignThreshold) {

		// Note: first four parameters should be packed into an object, e.g.:
		//     public ScaleTickDrawer(ScaleConfiguration config, Font font, Color color)

		this.verticalAlignThreshold = verticalAlignThreshold;
		this.horizontalAlignThreshold = horizontalAlignThreshold;
	}


	/**
	 * Sets the configuration.
	 *
	 * @param configuration The configuration.
	 * @param scaleTicks The scale ticks generated from the configuration.
	 */
	public synchronized void setConfiguration(RinearnGraph3DConfiguration configuration, ScaleTickGenerator.Result scaleTicks) {
		if (!configuration.hasScaleConfiguration()) {
			throw new IllegalArgumentException("No scale configuration is stored in the specified configuration.");
		}
		if (!configuration.hasRangeConfiguration()) {
			throw new IllegalArgumentException("No range configuration is stored in the specified configuration.");
		}
		if (!configuration.hasColorConfiguration()) {
			throw new IllegalArgumentException("No color configuration is stored in the specified configuration.");
		}
		this.config = configuration;
		this.scaleTicks = scaleTicks;
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
	 * Draws tick lines and tick labels on all axes.
	 *
	 * @param geometricPieceList The list for storing the geometric pieces of the drawn contents by this method.
	 * @param axes The array storing X axis at [0], Y axis at [1], and Z axis at [2].
	 */
	public synchronized void drawScaleTicks(List<GeometricPiece> geometricPieceList) {
		if (this.config == null) {
			throw new IllegalArgumentException("This drawer instance has not been configured yet.");
		}
		if (!this.config.getScaleConfiguration().isTicksVisible()) {
			return;
		}
		RangeConfiguration rangeConfig = this.config.getRangeConfiguration();
		AxisRangeConfiguration xRangeConfig = rangeConfig.getXRangeConfiguration();
		AxisRangeConfiguration yRangeConfig = rangeConfig.getYRangeConfiguration();
		AxisRangeConfiguration zRangeConfig = rangeConfig.getZRangeConfiguration();

		ScaleConfiguration scaleConfig = this.config.getScaleConfiguration();
		AxisScaleConfiguration xScaleConfig = scaleConfig.getXScaleConfiguration();
		AxisScaleConfiguration yScaleConfig = scaleConfig.getYScaleConfiguration();
		AxisScaleConfiguration zScaleConfig = scaleConfig.getZScaleConfiguration();

		SpaceConverter xSpaceConverter = new SpaceConverter(
				xRangeConfig.getMinimum(), xRangeConfig.getMaximum(), xScaleConfig.isLogScaleEnabled()
		);
		SpaceConverter ySpaceConverter = new SpaceConverter(
				yRangeConfig.getMinimum(), yRangeConfig.getMaximum(), yScaleConfig.isLogScaleEnabled()
		);
		SpaceConverter zSpaceConverter = new SpaceConverter(
				zRangeConfig.getMinimum(), zRangeConfig.getMaximum(), zScaleConfig.isLogScaleEnabled()
		);

		// Draw ticks on X axis.
		int xTickCount = this.scaleTicks.xTickCoordinates.length;
		for (int itick=0; itick<xTickCount; itick++) {
			double scaledCoord = xSpaceConverter.toScaledSpaceCoordinate(this.scaleTicks.xTickCoordinates[itick]).doubleValue();
			this.drawXScaleTickLabels(geometricPieceList, scaledCoord, this.scaleTicks.xTickLabelTexts[itick]);
			this.drawXScaleTickLines(geometricPieceList, scaledCoord);
		}

		// Draw ticks on Y axis.
		int yTickCount = this.scaleTicks.yTickCoordinates.length;
		for (int itick=0; itick<yTickCount; itick++) {
			double scaledCoord = ySpaceConverter.toScaledSpaceCoordinate(this.scaleTicks.yTickCoordinates[itick]).doubleValue();
			this.drawYScaleTickLabels(geometricPieceList, scaledCoord, this.scaleTicks.yTickLabelTexts[itick]);
			this.drawYScaleTickLines(geometricPieceList, scaledCoord);
		}

		// Draw ticks on Z axis.
		int zTickCount = this.scaleTicks.zTickCoordinates.length;
		for (int itick=0; itick<zTickCount; itick++) {
			double scaledCoord = zSpaceConverter.toScaledSpaceCoordinate(this.scaleTicks.zTickCoordinates[itick]).doubleValue();
			this.drawZScaleTickLabels(geometricPieceList, scaledCoord, this.scaleTicks.zTickLabelTexts[itick]);
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
		Font font = this.config.getFontConfiguration().getTickLabelFont();

		// X axis at Y=1, Z=1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				scaledCoord, 1.0 + margin, 1.0 + margin,  // Coords of the rendered point
				0.0, 1.0, 1.0,                            // Coords of the alignment reference point
				new double[][]{ {0.0, 1.0, 0.0}, {0.0, 0.0, -1.0} },  // Directional vectors
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, font, color  // Other params
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				scaledCoord, 1.0 + margin, 1.0 + margin,
				0.0, 1.0, 1.0,
				new double[][]{ {0.0, -1.0, 0.0}, {0.0, 0.0, 1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, font, color
		));

		// X axis at Y=1, Z=-1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				scaledCoord, 1.0 + margin, -1.0 - margin,
				0.0, 1.0, -1.0,
				new double[][]{ {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, font, color
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				scaledCoord, 1.0 + margin, -1.0 - margin,
				0.0, 1.0, -1.0,
				new double[][]{ {0.0, -1.0, 0.0}, {0.0, 0.0, -1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, font, color
		));

		// X axis at Y=-1, Z=1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				scaledCoord, -1.0 - margin, 1.0 + margin,
				0.0, -1.0, 1.0,
				new double[][]{ {0.0, -1.0, 0.0}, {0.0, 0.0, -1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, font, color
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				scaledCoord, -1.0 - margin, 1.0 + margin,
				0.0, -1.0, 1.0,
				new double[][]{ {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, font, color
		));

		// X axis at Y=-1, Z=-1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				scaledCoord, -1.0 - margin, -1.0 - margin,
				0.0, -1.0, -1.0,
				new double[][]{ {0.0, -1.0, 0.0}, {0.0, 0.0, 1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, font, color
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				scaledCoord, -1.0 - margin, -1.0 - margin,
				0.0, -1.0, -1.0,
				new double[][]{ {0.0, 1.0, 0.0}, {0.0, 0.0, -1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, font, color
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
		Font font = this.config.getFontConfiguration().getTickLabelFont();

		// Y axis at X=1, Z=1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				1.0 + margin, scaledCoord, 1.0 + margin,  // Coords of the rendered point
				1.0, 0.0, 1.0,                            // Coords of the alignment reference point
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, 0.0, -1.0} },  // Directional vectors
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, font, color  // Other params
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				1.0 + margin, scaledCoord, 1.0 + margin,
				1.0, 0.0, 1.0,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 0.0, 1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, font, color
		));

		// Y axis at X=1, Z=-1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				1.0 + margin, scaledCoord, -1.0 - margin,
				1.0, 0.0, -1.0,
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, 0.0, 1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, font, color
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				1.0 + margin, scaledCoord, -1.0 - margin,
				1.0, 0.0, -1.0,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 0.0, -1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, font, color
		));

		// Y axis at X=-1, Z=1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				-1.0 - margin, scaledCoord, 1.0 + margin,
				-1.0, 0.0, 1.0,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 0.0, -1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, font, color
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				-1.0 - margin, scaledCoord, 1.0 + margin,
				-1.0, 0.0, 1.0,
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, 0.0, 1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, font, color
		));

		// Y axis at X=-1, Z=-1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				-1.0 - margin, scaledCoord, -1.0 - margin,
				-1.0, 0.0, -1.0,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 0.0, 1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, font, color
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				-1.0 - margin, scaledCoord, -1.0 - margin,
				-1.0, 0.0, -1.0,
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, 0.0, -1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, font, color
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
		Font font = this.config.getFontConfiguration().getTickLabelFont();

		// Z axis at Y=1, Z=1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				1.0 + margin, 1.0 + margin, scaledCoord, // Coords of the rendered point
				1.0, 1.0, 0.0,                           // Coords of the alignment reference point
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, -1.0, 0.0} }, // Directional vectors
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, font, color // Other params
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				1.0 + margin, 1.0 + margin, scaledCoord,
				1.0, 1.0, 0.0,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 1.0, 0.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, font, color
		));

		// Z axis at Y=1, Z=-1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				1.0 + margin, -1.0 - margin, scaledCoord,
				1.0, -1.0, 0.0,
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, 1.0, 0.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, font, color
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				1.0 + margin, -1.0 - margin, scaledCoord,
				1.0, -1.0, 0.0,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, -1.0, 0.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, font, color
		));

		// Z axis at Y=-1, Z=1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				-1.0 - margin, 1.0 + margin, scaledCoord,
				-1.0, 1.0, 0.0,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, -1.0, 0.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, font, color
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				-1.0 - margin, 1.0 + margin, scaledCoord,
				-1.0, 1.0, 0.0,
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, 1.0, 0.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, font, color
		));

		// Z axis at Y=-1, Z=-1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				-1.0 - margin, -1.0 - margin, scaledCoord,
				-1.0, -1.0, 0.0,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 1.0, 0.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, font, color
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				-1.0 - margin, -1.0 - margin, scaledCoord,
				-1.0, -1.0, 0.0,
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, -1.0, 0.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, font, color
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
