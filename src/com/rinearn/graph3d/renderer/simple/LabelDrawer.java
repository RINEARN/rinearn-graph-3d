package com.rinearn.graph3d.renderer.simple;

import com.rinearn.graph3d.config.LabelConfiguration;
import com.rinearn.graph3d.config.ScaleConfiguration;
import com.rinearn.graph3d.renderer.RinearnGraph3DDrawingParameter;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.math.BigDecimal;
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
public class LabelDrawer {

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

	/** The vertical distance [px] from the reference point, at which the alignment of tick labels change. */
	private int verticalAlignThreshold;

	/** The horizontal distance [px] from the reference point, at which the alignment of tick labels change. */
	private int horizontalAlignThreshold;

	/** Stores the configuration of labels. */
	LabelConfiguration labelConfig;

	/** Stores the configuration of scales. */
	ScaleConfiguration scaleConfig;

	/** The color of labels. */
	private volatile Color color;

	/** The font for rendering texts of axis labels. */
	private volatile Font axisLabelFont;

	/** The font for rendering texts of tick labels. */
	private volatile Font tickLabelFont;



	/**
	 * Creates a new instance for drawing graph frames under the specified configurations.
	 * 
	 * @param labelConfig The configuration of axis labels.
	 * @param scaleConfig The configuration of scales.
	 * @param vertcalAlignThreshold The vertical distance [px] from the reference point, at which the alignment of tick labels change.
	 * @param horizontalAlignThreshold The horizontal distance [px] from the reference point, at which the alignment of tick labels change.
	 * @param axisLabelFont The font for rendering texts of axis labels.
	 * @param color The color of labels.
	 */
	public LabelDrawer(LabelConfiguration labelConfig, ScaleConfiguration scaleConfig, 
			int verticalAlignThreshold, int horizontalAlignThreshold,
			Font axisLabelFont, Font tickLabelFont, Color color) {

		this.verticalAlignThreshold = verticalAlignThreshold;
		this.horizontalAlignThreshold = horizontalAlignThreshold;
		this.labelConfig = labelConfig;
		this.scaleConfig = scaleConfig;
		this.axisLabelFont = axisLabelFont;
		this.tickLabelFont = tickLabelFont;
		this.color = color;
	}


	/**
	 * Sets the configuration of labels.
	 * 
	 * @param labelConfig The configuration of labels.
	 */
	public synchronized void setLabelConfiguration(LabelConfiguration labelConfig) {
		this.labelConfig = labelConfig;
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
	 * Sets the font for rendering texts of axis labels.
	 * 
	 * @param axisLabelFont The font for rendering texts of axis labels.
	 */
	public synchronized void setAxisLabelFont(Font axisLabelFont) {
		this.axisLabelFont = axisLabelFont;
	}

	/**
	 * Sets the font for rendering texts of tick labels.
	 * 
	 * @param axisLabelFont The font for rendering texts of tick labels.
	 */
	public synchronized void setTickLabelFont(Font tickLabelFont) {
		this.tickLabelFont = tickLabelFont;
	}

	/**
	 * Sets the color of labels.
	 * 
	 * @param color The color of labels.
	 */
	public synchronized void setColor(Color color) {
		this.color = color;
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
	 * @param axes The array storing X axis at [0], Y axis at [1], and Z axis at [2].
	 * @param tickLabelFontMetrics The metrics of the font used for drawing tick labels.
	 */
	public synchronized void drawAxisLabels(List<GeometricPiece> geometricPieceList, Axis[] axes, FontMetrics tickLabelFontMetrics) {

		// Draw axis labels of X, Y, Z axis.
		for (int idim=0; idim<=2; idim++) {

			// Get the axis of the "idim"-th dimension, where "idim" represents: 0=X, 1=Y, 2=Z.
			Axis axis = axes[idim];

			// Draw axis labels for each axis.
			switch (idim) {
				case X : {
					this.drawXAxisLabels(geometricPieceList, axis, tickLabelFontMetrics);
					break;
				}
				case Y : {
					this.drawYAxisLabels(geometricPieceList, axis, tickLabelFontMetrics);
					break;
				}
				case Z : {
					this.drawZAxisLabels(geometricPieceList, axis, tickLabelFontMetrics);
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
	 * @param tickLabels The tick label.
	 * @param tickLabelFontMetrics The metrics of the font for rendering the tick labels.
	 * @return The maximum rendering width of tick labels.
	 */
	private int getTickLabelMaxWidth(String[] tickLabels, FontMetrics tickLabelFontMetrics) {
		int maxWidth = 0;
		for (String tickLabel: tickLabels) {
			int width = tickLabelFontMetrics.stringWidth(tickLabel);
			maxWidth = Math.max(width, maxWidth);
		}
		return maxWidth;
	}


	/**
	 * On all (four) X axis's scale, draws tick labels having the specified value.
	 * 
	 * @param geometricPieceList The list for storing the geometric pieces of the drawn contents by this method.
	 * @param axisLabel The label of X axis.
	 */
	private void drawXAxisLabels(List<GeometricPiece> geometricPieceList, Axis axis, FontMetrics tickLabelFontMetrics) {

		// Define short aliases of some fields:
		RinearnGraph3DDrawingParameter.VerticalAlignment vAlign   = VERTICAL_ALIGNMENT;
		RinearnGraph3DDrawingParameter.HorizontalAlignment hAlign = HORIZONTAL_ALIGNMENT;
		int vThreshold   = this.verticalAlignThreshold;
		int hThreshold = this.horizontalAlignThreshold;
		double tickLabelMargin = this.scaleConfig.getXScaleConfiguration().getTickLabelMargin();
		String[] tickLabels = axis.getTickLabels();
		String axisLabel = this.labelConfig.getXLabelConfiguration().getText();

		int hOffset = this.getTickLabelMaxWidth(tickLabels, tickLabelFontMetrics);
		int vOffset = tickLabelFontMetrics.getHeight();
		hOffset = (int)(hOffset * 1.5);
		vOffset = (int)(vOffset * 1.5);
		DirectionalTextGeometricPiece piece = null;

		// X axis at Y=1, Z=1
		{
			piece = new DirectionalTextGeometricPiece(
					0.0, 1.0 + tickLabelMargin, 1.0 + tickLabelMargin,  // Coords of the rendered point
					0.0, 1.0, 1.0,                            // Coords of the alignment reference point
					new double[][]{ {0.0, 1.0, 0.0}, {0.0, 0.0, -1.0} },  // Directional vectors
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, this.axisLabelFont, this.color  // Other params
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);

			piece = new DirectionalTextGeometricPiece(
					0.0, 1.0 + tickLabelMargin, 1.0 + tickLabelMargin,
					0.0, 1.0, 1.0,
					new double[][]{ {0.0, -1.0, 0.0}, {0.0, 0.0, 1.0} },
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, this.axisLabelFont, this.color
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
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, this.axisLabelFont, this.color
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);

			piece = new DirectionalTextGeometricPiece(
					0.0, 1.0 + tickLabelMargin, -1.0 - tickLabelMargin,
					0.0, 1.0, -1.0,
					new double[][]{ {0.0, -1.0, 0.0}, {0.0, 0.0, -1.0} },
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, this.axisLabelFont, this.color
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
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, this.axisLabelFont, this.color
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);

			piece = new DirectionalTextGeometricPiece(
					0.0, -1.0 - tickLabelMargin, 1.0 + tickLabelMargin,
					0.0, -1.0, 1.0,
					new double[][]{ {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0} },
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, this.axisLabelFont, this.color
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
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, this.axisLabelFont, this.color
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);

			piece = new DirectionalTextGeometricPiece(
					0.0, -1.0 - tickLabelMargin, -1.0 - tickLabelMargin,
					0.0, -1.0, -1.0,
					new double[][]{ {0.0, 1.0, 0.0}, {0.0, 0.0, -1.0} },
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, this.axisLabelFont, this.color
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);
		}
	}


	/**
	 * On all (four) Y axis's scale, draws tick labels having the specified value.
	 * 
	 * @param geometricPieceList The list for storing the geometric pieces of the drawn contents by this method.
	 * @param axisLabel The label of Y axis.
	 */
	private void drawYAxisLabels(List<GeometricPiece> geometricPieceList, Axis axis, FontMetrics tickLabelFontMetrics) {

		// Define short aliases of some fields:
		RinearnGraph3DDrawingParameter.VerticalAlignment vAlign   = VERTICAL_ALIGNMENT;
		RinearnGraph3DDrawingParameter.HorizontalAlignment hAlign = HORIZONTAL_ALIGNMENT;
		int vThreshold   = this.verticalAlignThreshold;
		int hThreshold = this.horizontalAlignThreshold;
		double tickLabelMargin = this.scaleConfig.getYScaleConfiguration().getTickLabelMargin();
		String[] tickLabels = axis.getTickLabels();
		String axisLabel = this.labelConfig.getYLabelConfiguration().getText();

		int hOffset = this.getTickLabelMaxWidth(tickLabels, tickLabelFontMetrics);
		int vOffset = tickLabelFontMetrics.getHeight();
		hOffset = (int)(hOffset * 1.5);
		vOffset = (int)(vOffset * 1.5);
		DirectionalTextGeometricPiece piece = null;

		// Y axis at X=1, Z=1
		{
			piece = new DirectionalTextGeometricPiece(
					1.0 + tickLabelMargin, 0.0, 1.0 + tickLabelMargin,  // Coords of the rendered point
					1.0, 0.0, 1.0,                            // Coords of the alignment reference point
					new double[][]{ {1.0, 0.0, 0.0}, {0.0, 0.0, -1.0} },  // Directional vectors
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, this.axisLabelFont, this.color  // Other params
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);

			piece = new DirectionalTextGeometricPiece(
					1.0 + tickLabelMargin, 0.0, 1.0 + tickLabelMargin,
					1.0, 0.0, 1.0,
					new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 0.0, 1.0} },
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, this.axisLabelFont, this.color
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
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, this.axisLabelFont, this.color
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);

			piece = new DirectionalTextGeometricPiece(
					1.0 + tickLabelMargin, 0.0, -1.0 - tickLabelMargin,
					1.0, 0.0, -1.0,
					new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 0.0, -1.0} },
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, this.axisLabelFont, this.color
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
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, this.axisLabelFont, this.color
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);

			piece = new DirectionalTextGeometricPiece(
					-1.0 - tickLabelMargin, 0.0, 1.0 + tickLabelMargin,
					-1.0, 0.0, 1.0,
					new double[][]{ {1.0, 0.0, 0.0}, {0.0, 0.0, 1.0} },
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, this.axisLabelFont, this.color
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
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, this.axisLabelFont, this.color
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);

			piece = new DirectionalTextGeometricPiece(
					-1.0 - tickLabelMargin, 0.0, -1.0 - tickLabelMargin,
					-1.0, 0.0, -1.0,
					new double[][]{ {1.0, 0.0, 0.0}, {0.0, 0.0, -1.0} },
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, this.axisLabelFont, this.color
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);
		}
	}


	/**
	 * On all (four) Z axis's scale, draws tick labels having the specified value.
	 * 
	 * @param geometricPieceList The list for storing the geometric pieces of the drawn contents by this method.
	 * @param axisLabel The label of Z axis.
	 */
	private void drawZAxisLabels(List<GeometricPiece> geometricPieceList, Axis axis, FontMetrics tickLabelFontMetrics) {

		// Define short aliases of some fields:
		RinearnGraph3DDrawingParameter.VerticalAlignment vAlign   = VERTICAL_ALIGNMENT;
		RinearnGraph3DDrawingParameter.HorizontalAlignment hAlign = HORIZONTAL_ALIGNMENT;
		int vThreshold   = this.verticalAlignThreshold;
		int hThreshold = this.horizontalAlignThreshold;
		double tickLabelMargin = this.scaleConfig.getZScaleConfiguration().getTickLabelMargin();
		String[] tickLabels = axis.getTickLabels();
		String axisLabel = this.labelConfig.getZLabelConfiguration().getText();

		int hOffset = this.getTickLabelMaxWidth(tickLabels, tickLabelFontMetrics);
		int vOffset = tickLabelFontMetrics.getHeight();
		hOffset = (int)(hOffset * 1.5);
		vOffset = (int)(vOffset * 1.5);
		DirectionalTextGeometricPiece piece = null;

		// Z axis at Y=1, Z=1
		{
			piece = new DirectionalTextGeometricPiece(
					1.0 + tickLabelMargin, 1.0 + tickLabelMargin, 0.0, // Coords of the rendered point
					1.0, 1.0, 0.0,                           // Coords of the alignment reference point
					new double[][]{ {1.0, 0.0, 0.0}, {0.0, -1.0, 0.0} }, // Directional vectors
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, this.axisLabelFont, this.color // Other params
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);
			
			piece = new DirectionalTextGeometricPiece(
					1.0 + tickLabelMargin, 1.0 + tickLabelMargin, 0.0,
					1.0, 1.0, 0.0,
					new double[][]{ {-1.0, 0.0, 0.0}, {0.0, 1.0, 0.0} },
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, this.axisLabelFont, this.color
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
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, this.axisLabelFont, this.color
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);

			piece = new DirectionalTextGeometricPiece(
					1.0 + tickLabelMargin, -1.0 - tickLabelMargin, 0.0,
					1.0, -1.0, 0.0,
					new double[][]{ {-1.0, 0.0, 0.0}, {0.0, -1.0, 0.0} },
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, this.axisLabelFont, this.color
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
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, this.axisLabelFont, this.color
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);

			piece = new DirectionalTextGeometricPiece(
					-1.0 - tickLabelMargin, 1.0 + tickLabelMargin, 0.0,
					-1.0, 1.0, 0.0,
					new double[][]{ {1.0, 0.0, 0.0}, {0.0, 1.0, 0.0} },
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, this.axisLabelFont, this.color
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
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, this.axisLabelFont, this.color
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);

			piece = new DirectionalTextGeometricPiece(
					-1.0 - tickLabelMargin, -1.0 - tickLabelMargin, 0.0,
					-1.0, -1.0, 0.0,
					new double[][]{ {1.0, 0.0, 0.0}, {0.0, -1.0, 0.0} },
					vAlign, hAlign, vThreshold, hThreshold, axisLabel, this.axisLabelFont, this.color
			);
			piece.setAlignmentOffsets(vOffset, hOffset);
			geometricPieceList.add(piece);
		}
	}
}
