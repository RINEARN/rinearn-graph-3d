package com.rinearn.graph3d.renderer.simple;

import com.rinearn.graph3d.renderer.RinearnGraph3DDrawingParameter;
import com.rinearn.graph3d.config.ScaleConfiguration;

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

	/** Stores the configuration of scales. */
	private ScaleConfiguration scaleConfig;

	/** The vertical distance [px] from the reference point, at which the alignment of tick labels change. */
	private int verticalAlignThreshold;

	/** The horizontal distance [px] from the reference point, at which the alignment of tick labels change. */
	private int horizontalAlignThreshold;

	/** The margin between axes and tick labels. */
	private volatile double tickLabelMargin;

	/** The length of tick lines. */
	private volatile double tickLineLength;

	/** The font for rendering texts of tick labels. */
	private volatile Font font;

	/** The color of tick labels and lines. */
	private volatile Color color;


	/**
	 * Create an instance for drawing scale ticks under the specified settings.
	 * 
	 * @param scaleConfig The configuration of scales.
	 * @param vertcalAlignThreshold The vertical distance [px] from the reference point, at which the alignment of tick labels change.
	 * @param horizontalAlignThreshold The horizontal distance [px] from the reference point, at which the alignment of tick labels change.
	 * @param tickLabelMargin The margin between axes and tick labels.
	 * @param tickLineLength The length of tick lines.
	 * @param font The font for rendering texts of tick labels.
	 * @param color The color of tick labels and lines.
	 */
	public ScaleTickDrawer(ScaleConfiguration scaleConfig, int verticalAlignThreshold, int horizontalAlignThreshold,
			double tickLabelMargin, double tickLineLength, Font font, Color color) {

		// Note: first four parameters should be packed into an object, e.g.:
		//     public ScaleTickDrawer(ScaleConfiguration config, Font font, Color color)

		this.scaleConfig = scaleConfig;
		this.verticalAlignThreshold = verticalAlignThreshold;
		this.horizontalAlignThreshold = horizontalAlignThreshold;
		this.tickLabelMargin = tickLabelMargin;
		this.tickLineLength = tickLineLength;
		this.font = font;
		this.color = color;
	}


	/**
	 * Sets the configuration of scales.
	 * 
	 * @param scaleConfig The configuration of scales.
	 */
	public synchronized void setScaleConfiguration(ScaleConfiguration scaleConfig) {
		this.scaleConfig = scaleConfig;
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
	 * Sets the color of tick labels and lines.
	 * 
	 * @param color The color of tick labels and lines.
	 */
	public synchronized void setColor(Color color) {
		this.color = color;
	}


	/**
	 * On all (four) X axis's scale, draws tick labels having the specified value.
	 * 
	 * @param geometricPieceList The list for storing the geometric pieces of the drawn contents by this method.
	 * @param axes The array storing X axis at [0], Y axis at [1], and Z axis at [2].
	 */
	public synchronized void drawScaleTicks(List<GeometricPiece> geometricPieceList, Axis[] axes) {

		// Draw ticks of X, Y, Z axis.
		for (int idim=0; idim<=2; idim++) {

			// Get the axis of the "idim"-th dimension, where "idim" represents: 0=X, 1=Y, 2=Z.
			Axis axis = axes[idim];

			// Get coordinates (positions) and labels (displayed values) of the ticks.
			BigDecimal[] tickCoords = axis.getTickCoordinates();
			String[] tickLabels = axis.getTickLabels();
			int tickCount = tickCoords.length;

			// Draw ticks on four axes belonging to the current dimension (X or Y or Z).
			for (int itick=0; itick<tickCount; itick++) {
				double scaledCoord = axis.scaleCoordinate(tickCoords[itick]).doubleValue();

				// X axes:
				if (idim == X) {
					this.drawXScaleTickLabels(geometricPieceList, scaledCoord, tickLabels[itick]);
					this.drawXScaleTickLines(geometricPieceList, scaledCoord);
				}

				// Y axes:
				if (idim == Y) {
					this.drawYScaleTickLabels(geometricPieceList, scaledCoord, tickLabels[itick]);
					this.drawYScaleTickLines(geometricPieceList, scaledCoord);
				}

				// Z axes:
				if (idim == Z) {
					this.drawZScaleTickLabels(geometricPieceList, scaledCoord, tickLabels[itick]);
					this.drawZScaleTickLines(geometricPieceList, scaledCoord);
				}
			}
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
		double margin = this.tickLabelMargin;

		// X axis at Y=1, Z=1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				scaledCoord, 1.0 + margin, 1.0 + margin,  // Coords of the rendered point
				0.0, 1.0, 1.0,  // Coords of the alignment reference point
				new double[][]{ {0.0, 1.0, 0.0}, {0.0, 0.0, -1.0} },  // Directional vectors
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, this.color  // Other params
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				scaledCoord, 1.0 + margin, 1.0 + margin,
				0.0, 1.0, 1.0,
				new double[][]{ {0.0, -1.0, 0.0}, {0.0, 0.0, 1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, this.color
		));

		// X axis at Y=1, Z=-1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				scaledCoord, 1.0 + margin, -1.0 - margin,
				0.0, 1.0, -1.0,
				new double[][]{ {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, this.color
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				scaledCoord, 1.0 + margin, -1.0 - margin,
				0.0, 1.0, -1.0,
				new double[][]{ {0.0, -1.0, 0.0}, {0.0, 0.0, -1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, this.color
		));

		// X axis at Y=-1, Z=1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				scaledCoord, -1.0 - margin, 1.0 + margin,
				0.0, -1.0, 1.0,
				new double[][]{ {0.0, -1.0, 0.0}, {0.0, 0.0, -1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, this.color
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				scaledCoord, -1.0 - margin, 1.0 + margin,
				0.0, -1.0, 1.0,
				new double[][]{ {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, this.color
		));

		// X axis at Y=-1, Z=-1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				scaledCoord, -1.0 - margin, -1.0 - margin,
				0.0, -1.0, -1.0,
				new double[][]{ {0.0, -1.0, 0.0}, {0.0, 0.0, 1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, this.color
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				scaledCoord, -1.0 - margin, -1.0 - margin,
				0.0, -1.0, -1.0,
				new double[][]{ {0.0, 1.0, 0.0}, {0.0, 0.0, -1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, this.color
		));
	}


	/**
	 * On all (four) X axis's scale, draws tick lines having the specified value.
	 * 
	 * @param geometricPieceList The list for storing the geometric pieces of the drawn contents by this method.
	 * @param scaledCoord The X coordinate value (position) of the tick lines.
	 */
	private void drawXScaleTickLines(List<GeometricPiece> geometricPieceList, double scaledCoord) {

		double length = tickLineLength; // Short alias of the line length
		double width = 1.0; // line width

		// X axis at Y=1, Z=1
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				scaledCoord, 1.0, 1.0,                                // Coords of the edge point A
				scaledCoord, 1.0 + length, 1.0 + length,      // Coords of the edge point B
				new double[][]{ {0.0, 1.0, 0.0}, {0.0, 0.0, -1.0} },  // Directional vectors
				width, this.color  // Other params
		));
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				scaledCoord, 1.0, 1.0,
				scaledCoord, 1.0 + length, 1.0 + length,
				new double[][]{ {0.0, -1.0, 0.0}, {0.0, 0.0, 1.0} },
				width, this.color
		));

		// X axis at Y=1, Z=-1
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				scaledCoord, 1.0, -1.0,
				scaledCoord, 1.0 + length, -1.0 - length,
				new double[][]{ {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0} },
				width, this.color
		));
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				scaledCoord, 1.0, -1.0,
				scaledCoord, 1.0 + length, -1.0 - length,
				new double[][]{ {0.0, -1.0, 0.0}, {0.0, 0.0, -1.0} },
				width, this.color
		));

		// X axis at Y=-1, Z=1
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				scaledCoord, -1.0, 1.0,
				scaledCoord, -1.0 - length, 1.0 + length,
				new double[][]{ {0.0, -1.0, 0.0}, {0.0, 0.0, -1.0} },
				width, this.color
		));
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				scaledCoord, -1.0, 1.0,
				scaledCoord, -1.0 - length, 1.0 + length,
				new double[][]{ {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0} },
				width, this.color
		));

		// X axis at Y=-1, Z=-1
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				scaledCoord, -1.0, -1.0,
				scaledCoord, -1.0 - length, -1.0 - length,
				new double[][]{ {0.0, -1.0, 0.0}, {0.0, 0.0, 1.0} },
				width, this.color
		));
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				scaledCoord, -1.0, -1.0,
				scaledCoord, -1.0 - length, -1.0 - length,
				new double[][]{ {0.0, 1.0, 0.0}, {0.0, 0.0, -1.0} },
				width, this.color
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
		double margin = this.tickLabelMargin;

		// Y axis at X=1, Z=1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				1.0 + margin, scaledCoord, 1.0 + margin,  // Coords of the rendered point
				1.0, 0.0, 1.0,  // Coords of the alignment reference point
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, 0.0, -1.0} },  // Directional vectors
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, this.color  // Other params
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				1.0 + margin, scaledCoord, 1.0 + margin,
				1.0, 0.0, 1.0,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 0.0, 1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, this.color
		));

		// Y axis at X=1, Z=-1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				1.0 + margin, scaledCoord, -1.0 - margin,
				1.0, 0.0, -1.0,
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, 0.0, 1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, this.color
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				1.0 + margin, scaledCoord, -1.0 - margin,
				1.0, 0.0, -1.0,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 0.0, -1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, this.color
		));

		// Y axis at X=-1, Z=1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				-1.0 - margin, scaledCoord, 1.0 + margin,
				-1.0, 0.0, 1.0,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 0.0, -1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, this.color
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				-1.0 - margin, scaledCoord, 1.0 + margin,
				-1.0, 0.0, 1.0,
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, 0.0, 1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, this.color
		));

		// Y axis at X=-1, Z=-1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				-1.0 - margin, scaledCoord, -1.0 - margin,
				-1.0, 0.0, -1.0,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 0.0, 1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, this.color
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				-1.0 - margin, scaledCoord, -1.0 - margin,
				-1.0, 0.0, -1.0,
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, 0.0, -1.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, this.color
		));
	}


	/**
	 * On all (four) X axis's scale, draws tick lines having the specified value.
	 * 
	 * @param geometricPieceList The list for storing the geometric pieces of the drawn contents by this method.
	 * @param scaledCoord The Y coordinate value (position) of the tick lines.
	 */
	private void drawYScaleTickLines(List<GeometricPiece> geometricPieceList, double scaledCoord) {

		double length = tickLineLength; // Short alias of the line length
		double width = 1.0; // line width

		// Y axis at X=1, Z=1
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				1.0, scaledCoord, 1.0,                                // Coords of the edge point A
				1.0 + length, scaledCoord, 1.0 + length,      // Coords of the edge point B
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, 0.0, -1.0} },  // Directional vectors
				width, this.color  // Other params
		));
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				1.0, scaledCoord, 1.0,
				1.0 + length, scaledCoord, 1.0 + length,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 0.0, 1.0} },
				width, this.color
		));

		// Y axis at X=1, Z=-1
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				1.0, scaledCoord, -1.0,
				1.0 + length, scaledCoord, -1.0 - length,
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, 0.0, 1.0} },
				width, this.color
		));
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				1.0, scaledCoord, -1.0,
				1.0 + length, scaledCoord, -1.0 - length,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 0.0, -1.0} },
				width, this.color
		));

		// Y axis at X=-1, Z=1
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				-1.0, scaledCoord, 1.0,
				-1.0 - length, scaledCoord, 1.0 + length,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 0.0, -1.0} },
				width, this.color
		));
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				-1.0, scaledCoord, 1.0,
				-1.0 - length, scaledCoord, 1.0 + length,
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, 0.0, 1.0} },
				width, this.color
		));

		// Y axis at X=-1, Z=-1
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				-1.0, scaledCoord, -1.0,
				-1.0 - length, scaledCoord, -1.0 - length,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 0.0, 1.0} },
				width, this.color
		));
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				-1.0, scaledCoord, -1.0,
				-1.0 - length, scaledCoord, -1.0 - length,
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, 0.0, -1.0} },
				width, this.color
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
		double margin = this.tickLabelMargin;

		// Z axis at Y=1, Z=1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				1.0 + margin, 1.0 + margin, scaledCoord, // Coords of the rendered point
				1.0, 1.0, 0.0,         // Coords of the alignment reference point
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, -1.0, 0.0} }, // Directional vectors
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, this.color // Other params
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				1.0 + margin, 1.0 + margin, scaledCoord,
				1.0, 1.0, 0.0,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 1.0, 0.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, this.color
		));

		// Z axis at Y=1, Z=-1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				1.0 + margin, -1.0 - margin, scaledCoord,
				1.0, -1.0, 0.0,
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, 1.0, 0.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, this.color
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				1.0 + margin, -1.0 - margin, scaledCoord,
				1.0, -1.0, 0.0,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, -1.0, 0.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, this.color
		));

		// Z axis at Y=-1, Z=1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				-1.0 - margin, 1.0 + margin, scaledCoord,
				-1.0, 1.0, 0.0,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, -1.0, 0.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, this.color
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				-1.0 - margin, 1.0 + margin, scaledCoord,
				-1.0, 1.0, 0.0,
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, 1.0, 0.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, this.color
		));

		// Z axis at Y=-1, Z=-1
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				-1.0 - margin, -1.0 - margin, scaledCoord,
				-1.0, -1.0, 0.0,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 1.0, 0.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, this.color
		));
		geometricPieceList.add(new DirectionalTextGeometricPiece(
				-1.0 - margin, -1.0 - margin, scaledCoord,
				-1.0, -1.0, 0.0,
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, -1.0, 0.0} },
				vAlign, hAlign, vThreshold, hThreshold, tickLabel, this.font, this.color
		));
	}


	/**
	 * On all (four) X axis's scale, draws tick lines having the specified value.
	 * 
	 * @param geometricPieceList The list for storing the geometric pieces of the drawn contents by this method.
	 * @param scaledCoord The Z coordinate value (position) of the tick lines.
	 */
	private void drawZScaleTickLines(List<GeometricPiece> geometricPieceList, double scaledCoord) {

		double length = tickLineLength; // Short alias of the line length
		double width = 1.0; // line width

		// Z axis at Y=1, Z=1
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				1.0, 1.0, scaledCoord,                                // Coords of the edge point A
				1.0 + length, 1.0 + length, scaledCoord,      // Coords of the edge point B
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, -1.0, 0.0} }, // Directional vectors
				width, this.color  // Other params
		));
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				1.0, 1.0, scaledCoord,
				1.0 + length, 1.0 + length, scaledCoord,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 1.0, 0.0} },
				width, this.color
		));

		// Z axis at Y=1, Z=-1
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				1.0, -1.0, scaledCoord,
				1.0 + length, -1.0 - length, scaledCoord,
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, 1.0, 0.0} },
				width, this.color
		));
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				1.0, -1.0, scaledCoord,
				1.0 + length, -1.0 - length, scaledCoord,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, -1.0, 0.0} },
				width, this.color
		));

		// Z axis at Y=-1, Z=1
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				-1.0, 1.0, scaledCoord,
				-1.0 - length, 1.0 + length, scaledCoord,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, -1.0, 0.0} },
				width, this.color
		));
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				-1.0, 1.0, scaledCoord,
				-1.0 - length, 1.0 + length, scaledCoord,
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, 1.0, 0.0} },
				width, this.color
		));

		// Z axis at Y=-1, Z=-1
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				-1.0, -1.0, scaledCoord,
				-1.0 - length, -1.0 - length, scaledCoord,
				new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 1.0, 0.0} },
				width, this.color
		));
		geometricPieceList.add(new DirectionalLineGeometricPiece(
				-1.0, -1.0, scaledCoord,
				-1.0 - length, -1.0 - length, scaledCoord,
				new double[][]{ {1.0, 0.0, 0.0}, {0.0, -1.0, 0.0} },
				width, this.color
		));
	}
}
