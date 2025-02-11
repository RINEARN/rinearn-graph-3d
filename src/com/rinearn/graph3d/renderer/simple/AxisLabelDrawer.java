package com.rinearn.graph3d.renderer.simple;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.renderer.RinearnGraph3DDrawingParameter;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.List;

// !!! NOTE
//
// 内容的に ScaleTickDrawer と一体化させた方がいいかもしれない（その場合は要 rename だけど）。
// というかむしろ FrameDrawer も一緒に、グラフの骨格の描画系を一体化させるとかもありかも
//
// !!! NOTE

/**
 * The class for drawing axis labels.
 */
public class AxisLabelDrawer {

	/** The array index representing X, in three-dimensional arrays. */
	public static final int X = 0;

	/** The array index representing Y, in three-dimensional arrays. */
	public static final int Y = 1;

	/** The array index representing Z, in three-dimensional arrays. */
	public static final int Z = 2;

	/** The vertical alignment mode of labels. */
	private static final RinearnGraph3DDrawingParameter.VerticalAlignment VERTICAL_ALIGNMENT
			= RinearnGraph3DDrawingParameter.VerticalAlignment.RADIAL;

	/** The horizontal alignment mode of labels. */
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
	 * Creates a new instance for drawing graph frames under the specified configurations.
	 *
	 * @param vertcalAlignThreshold The vertical distance [px] from the reference point, at which the alignment of tick labels change.
	 * @param horizontalAlignThreshold The horizontal distance [px] from the reference point, at which the alignment of tick labels change.
	 */
	public AxisLabelDrawer(int verticalAlignThreshold, int horizontalAlignThreshold) {
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
		if (!configuration.hasLabelConfiguration()) {
			throw new IllegalArgumentException("No label configuration is stored in the specified configuration.");
		}
		if (!configuration.hasColorConfiguration()) {
			throw new IllegalArgumentException("No label configuration is stored in the specified configuration.");
		}
		this.config = configuration;
		this.scaleTicks = scaleTicks;
	}


	/**
	 * Sets the threshold distances at which the alignment of labels change.
	 *
	 * @param verticalAlignThreshold The threshold of the vertical distance [px] from the reference point.
	 * @param horizontalAlignThreshold The threshold of the horizontal distance [px] from the reference point.
	 */
	public synchronized void setAlignmentThresholds(int verticalAlignThreshold, int horizontalAlignThreshold) {
		this.verticalAlignThreshold = verticalAlignThreshold;
		this.horizontalAlignThreshold = horizontalAlignThreshold;
	}


	/**
	 * Draws axis labels on all axes.
	 *
	 * @param geometricPieceList The list for storing the geometric pieces of the drawn contents by this method.
	 * @param tickLabelFontMetrics The metrics of the font used for drawing tick labels.
	 */
	public synchronized void drawAxisLabels(List<GeometricPiece> geometricPieceList, FontMetrics tickLabelFontMetrics) {
		if (this.config == null) {
			throw new IllegalArgumentException("This drawer instance has not been configured yet.");
		}

		// Draw axis labels of X, Y, Z axis.
		for (int idim=0; idim<=2; idim++) {

			// Draw axis labels for each axis.
			switch (idim) {
				case X : {
					this.drawXAxisLabels(geometricPieceList, tickLabelFontMetrics);
					break;
				}
				case Y : {
					this.drawYAxisLabels(geometricPieceList, tickLabelFontMetrics);
					break;
				}
				case Z : {
					this.drawZAxisLabels(geometricPieceList, tickLabelFontMetrics);
					break;
				}
				default : {
					throw new UnsupportedOperationException("Incorrect dimension index: " + idim);
				}
			}
		}
	}


	/**
	 * Gets the maximum rendering width of tick labels.
	 *
	 * @param tickLabelTexts The text of the tick label.
	 * @param tickLabelFontMetrics The metrics of the font for rendering the tick labels.
	 * @return The maximum rendering width of tick labels.
	 */
	private int getTickLabelMaxWidth(String[] tickLabelTexts, FontMetrics tickLabelFontMetrics) {
		int maxWidth = 0;
		for (String tickLabelText: tickLabelTexts) {
			int width = tickLabelFontMetrics.stringWidth(tickLabelText);
			maxWidth = Math.max(width, maxWidth);
		}
		return maxWidth;
	}


	/**
	 * On all (four) X axis's scale, draws tick labels having the specified value.
	 *
	 * @param geometricPieceList The list for storing the geometric pieces of the drawn contents by this method.
	 * @param tickLabelFontMetrics The metrics of the font used for drawing tick labels.
	 */
	private void drawXAxisLabels(List<GeometricPiece> geometricPieceList, FontMetrics tickLabelFontMetrics) {

		// Define short aliases of some fields:
		RinearnGraph3DDrawingParameter.VerticalAlignment vAlign   = VERTICAL_ALIGNMENT;
		RinearnGraph3DDrawingParameter.HorizontalAlignment hAlign = HORIZONTAL_ALIGNMENT;
		int vThreshold   = this.verticalAlignThreshold;
		int hThreshold = this.horizontalAlignThreshold;
		double tickLabelMargin = this.config.getScaleConfiguration().getXScaleConfiguration().getTickLabelMargin();
		String[] tickLabelTexts = this.scaleTicks.xTickLabelTexts;
		String axisLabel = this.config.getLabelConfiguration().getXLabelConfiguration().getLabelText();

		int hOffset = this.getTickLabelMaxWidth(tickLabelTexts, tickLabelFontMetrics);
		int vOffset = tickLabelFontMetrics.getHeight();
		hOffset = (int)(hOffset * 1.5);
		vOffset = (int)(vOffset * 1.5);
		Color color = this.config.getColorConfiguration().getForegroundColor();
		Font font = this.config.getFontConfiguration().getAxisLabelFont();

		DirectionalTextGeometricPiece piece = null;

		// X axis at Y=1, Z=1
		{
			piece = new DirectionalTextGeometricPiece(
					0.0, 1.0 + tickLabelMargin, 1.0 + tickLabelMargin,  // Coords of the rendered point
					0.0, 1.0, 1.0,                            // Coords of the alignment reference point
					new double[][]{ {0.0, 1.0, 0.0}, {0.0, 0.0, -1.0} },  // Directional vectors
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, font, color  // Other params
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);

			piece = new DirectionalTextGeometricPiece(
					0.0, 1.0 + tickLabelMargin, 1.0 + tickLabelMargin,
					0.0, 1.0, 1.0,
					new double[][]{ {0.0, -1.0, 0.0}, {0.0, 0.0, 1.0} },
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, font, color
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);

		}

		// X axis at Y=1, Z=-1
		{
			piece = new DirectionalTextGeometricPiece(
					0.0, 1.0 + tickLabelMargin, -1.0 - tickLabelMargin,
					0.0, 1.0, -1.0,
					new double[][]{ {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0} },
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, font, color
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);

			piece = new DirectionalTextGeometricPiece(
					0.0, 1.0 + tickLabelMargin, -1.0 - tickLabelMargin,
					0.0, 1.0, -1.0,
					new double[][]{ {0.0, -1.0, 0.0}, {0.0, 0.0, -1.0} },
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, font, color
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);

		}

		// X axis at Y=-1, Z=1
		{
			piece = new DirectionalTextGeometricPiece(
					0.0, -1.0 - tickLabelMargin, 1.0 + tickLabelMargin,
					0.0, -1.0, 1.0,
					new double[][]{ {0.0, -1.0, 0.0}, {0.0, 0.0, -1.0} },
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, font, color
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);

			piece = new DirectionalTextGeometricPiece(
					0.0, -1.0 - tickLabelMargin, 1.0 + tickLabelMargin,
					0.0, -1.0, 1.0,
					new double[][]{ {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0} },
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, font, color
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);

		}

		// X axis at Y=-1, Z=-1
		{
			piece = new DirectionalTextGeometricPiece(
					0.0, -1.0 - tickLabelMargin, -1.0 - tickLabelMargin,
					0.0, -1.0, -1.0,
					new double[][]{ {0.0, -1.0, 0.0}, {0.0, 0.0, 1.0} },
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, font, color
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);

			piece = new DirectionalTextGeometricPiece(
					0.0, -1.0 - tickLabelMargin, -1.0 - tickLabelMargin,
					0.0, -1.0, -1.0,
					new double[][]{ {0.0, 1.0, 0.0}, {0.0, 0.0, -1.0} },
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, font, color
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);
		}
	}


	/**
	 * On all (four) Y axis's scale, draws tick labels having the specified value.
	 *
	 * @param geometricPieceList The list for storing the geometric pieces of the drawn contents by this method.
	 * @param tickLabelFontMetrics The metrics of the font used for drawing tick labels.
	 */
	private void drawYAxisLabels(List<GeometricPiece> geometricPieceList, FontMetrics tickLabelFontMetrics) {

		// Define short aliases of some fields:
		RinearnGraph3DDrawingParameter.VerticalAlignment vAlign   = VERTICAL_ALIGNMENT;
		RinearnGraph3DDrawingParameter.HorizontalAlignment hAlign = HORIZONTAL_ALIGNMENT;
		int vThreshold   = this.verticalAlignThreshold;
		int hThreshold = this.horizontalAlignThreshold;
		double tickLabelMargin = this.config.getScaleConfiguration().getYScaleConfiguration().getTickLabelMargin();
		String[] tickLabels = this.scaleTicks.yTickLabelTexts;
		String axisLabel = this.config.getLabelConfiguration().getYLabelConfiguration().getLabelText();

		int hOffset = this.getTickLabelMaxWidth(tickLabels, tickLabelFontMetrics);
		int vOffset = tickLabelFontMetrics.getHeight();
		hOffset = (int)(hOffset * 1.5);
		vOffset = (int)(vOffset * 1.5);
		Color color = this.config.getColorConfiguration().getForegroundColor();
		Font font = this.config.getFontConfiguration().getAxisLabelFont();

		DirectionalTextGeometricPiece piece = null;

		// Y axis at X=1, Z=1
		{
			piece = new DirectionalTextGeometricPiece(
					1.0 + tickLabelMargin, 0.0, 1.0 + tickLabelMargin,  // Coords of the rendered point
					1.0, 0.0, 1.0,                            // Coords of the alignment reference point
					new double[][]{ {1.0, 0.0, 0.0}, {0.0, 0.0, -1.0} },  // Directional vectors
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, font, color  // Other params
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);

			piece = new DirectionalTextGeometricPiece(
					1.0 + tickLabelMargin, 0.0, 1.0 + tickLabelMargin,
					1.0, 0.0, 1.0,
					new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 0.0, 1.0} },
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, font, color
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);

		}

		// Y axis at X=1, Z=-1
		{
			piece = new DirectionalTextGeometricPiece(
					1.0 + tickLabelMargin, 0.0, -1.0 - tickLabelMargin,
					1.0, 0.0, -1.0,
					new double[][]{ {1.0, 0.0, 0.0}, {0.0, 0.0, 1.0} },
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, font, color
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);

			piece = new DirectionalTextGeometricPiece(
					1.0 + tickLabelMargin, 0.0, -1.0 - tickLabelMargin,
					1.0, 0.0, -1.0,
					new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 0.0, -1.0} },
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, font, color
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);
		}

		// Y axis at X=-1, Z=1
		{
			piece = new DirectionalTextGeometricPiece(
					-1.0 - tickLabelMargin, 0.0, 1.0 + tickLabelMargin,
					-1.0, 0.0, 1.0,
					new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 0.0, -1.0} },
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, font, color
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);

			piece = new DirectionalTextGeometricPiece(
					-1.0 - tickLabelMargin, 0.0, 1.0 + tickLabelMargin,
					-1.0, 0.0, 1.0,
					new double[][]{ {1.0, 0.0, 0.0}, {0.0, 0.0, 1.0} },
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, font, color
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);
		}

		// Y axis at X=-1, Z=-1
		{
			piece = new DirectionalTextGeometricPiece(
					-1.0 - tickLabelMargin, 0.0, -1.0 - tickLabelMargin,
					-1.0, 0.0, -1.0,
					new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 0.0, 1.0} },
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, font, color
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);

			piece = new DirectionalTextGeometricPiece(
					-1.0 - tickLabelMargin, 0.0, -1.0 - tickLabelMargin,
					-1.0, 0.0, -1.0,
					new double[][]{ {1.0, 0.0, 0.0}, {0.0, 0.0, -1.0} },
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, font, color
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);
		}
	}


	/**
	 * On all (four) Z axis's scale, draws tick labels having the specified value.
	 *
	 * @param geometricPieceList The list for storing the geometric pieces of the drawn contents by this method.
	 * @param tickLabelFontMetrics The metrics of the font used for drawing tick labels.
	 */
	private void drawZAxisLabels(List<GeometricPiece> geometricPieceList, FontMetrics tickLabelFontMetrics) {

		// Define short aliases of some fields:
		RinearnGraph3DDrawingParameter.VerticalAlignment vAlign   = VERTICAL_ALIGNMENT;
		RinearnGraph3DDrawingParameter.HorizontalAlignment hAlign = HORIZONTAL_ALIGNMENT;
		int vThreshold   = this.verticalAlignThreshold;
		int hThreshold = this.horizontalAlignThreshold;
		double tickLabelMargin = this.config.getScaleConfiguration().getZScaleConfiguration().getTickLabelMargin();
		String[] tickLabels = this.scaleTicks.zTickLabelTexts;
		String axisLabel = this.config.getLabelConfiguration().getZLabelConfiguration().getLabelText();

		int hOffset = this.getTickLabelMaxWidth(tickLabels, tickLabelFontMetrics);
		int vOffset = tickLabelFontMetrics.getHeight();
		hOffset = (int)(hOffset * 1.5);
		vOffset = (int)(vOffset * 1.5);
		Color color = this.config.getColorConfiguration().getForegroundColor();
		Font font = this.config.getFontConfiguration().getAxisLabelFont();

		DirectionalTextGeometricPiece piece = null;

		// Z axis at Y=1, Z=1
		{
			piece = new DirectionalTextGeometricPiece(
					1.0 + tickLabelMargin, 1.0 + tickLabelMargin, 0.0, // Coords of the rendered point
					1.0, 1.0, 0.0,                           // Coords of the alignment reference point
					new double[][]{ {1.0, 0.0, 0.0}, {0.0, -1.0, 0.0} }, // Directional vectors
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, font, color // Other params
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);

			piece = new DirectionalTextGeometricPiece(
					1.0 + tickLabelMargin, 1.0 + tickLabelMargin, 0.0,
					1.0, 1.0, 0.0,
					new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 1.0, 0.0} },
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, font, color
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);
		}

		// Z axis at Y=1, Z=-1
		{
			piece = new DirectionalTextGeometricPiece(
					1.0 + tickLabelMargin, -1.0 - tickLabelMargin, 0.0,
					1.0, -1.0, 0.0,
					new double[][]{ {1.0, 0.0, 0.0}, {0.0, 1.0, 0.0} },
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, font, color
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);

			piece = new DirectionalTextGeometricPiece(
					1.0 + tickLabelMargin, -1.0 - tickLabelMargin, 0.0,
					1.0, -1.0, 0.0,
					new double[][]{ {-1.0, 0.0, 0.0}, {0.0, -1.0, 0.0} },
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, font, color
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);
		}

		// Z axis at Y=-1, Z=1
		{
			piece = new DirectionalTextGeometricPiece(
					-1.0 - tickLabelMargin, 1.0 + tickLabelMargin, 0.0,
					-1.0, 1.0, 0.0,
					new double[][]{ {-1.0, 0.0, 0.0}, {0.0, -1.0, 0.0} },
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, font, color
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);

			piece = new DirectionalTextGeometricPiece(
					-1.0 - tickLabelMargin, 1.0 + tickLabelMargin, 0.0,
					-1.0, 1.0, 0.0,
					new double[][]{ {1.0, 0.0, 0.0}, {0.0, 1.0, 0.0} },
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, font, color
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);
		}

		// Z axis at Y=-1, Z=-1
		{
			piece = new DirectionalTextGeometricPiece(
					-1.0 - tickLabelMargin, -1.0 - tickLabelMargin, 0.0,
					-1.0, -1.0, 0.0,
					new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 1.0, 0.0} },
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, font, color
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);

			piece = new DirectionalTextGeometricPiece(
					-1.0 - tickLabelMargin, -1.0 - tickLabelMargin, 0.0,
					-1.0, -1.0, 0.0,
					new double[][]{ {1.0, 0.0, 0.0}, {0.0, -1.0, 0.0} },
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, font, color
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);
		}
	}
}
