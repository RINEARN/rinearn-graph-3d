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

	/** Stores the configuration of this application. */
	RinearnGraph3DConfiguration config;

	/** The vertical distance [px] from the reference point, at which the alignment of tick labels change. */
	private int verticalAlignThreshold;

	/** The horizontal distance [px] from the reference point, at which the alignment of tick labels change. */
	private int horizontalAlignThreshold;

	/** The color of labels. */
	private volatile Color color;

	/** The font for rendering texts of axis labels. */
	private volatile Font axisLabelFont;

	/** The labels of the ticks on X axis. */
	private String[] xTickLabels = {};

	/** The labels of the ticks on Y axis. */
	private String[] yTickLabels = {};

	/** The labels of the ticks on Z axis. */
	private String[] zTickLabels = {};


	/**
	 * Creates a new instance for drawing graph frames under the specified configurations.
	 * 
	 * @param configuration The configuration of this application.
	 * @param vertcalAlignThreshold The vertical distance [px] from the reference point, at which the alignment of tick labels change.
	 * @param horizontalAlignThreshold The horizontal distance [px] from the reference point, at which the alignment of tick labels change.
	 * @param axisLabelFont The font for rendering texts of axis labels.
	 * @param color The color of labels.
	 */
	public LabelDrawer(RinearnGraph3DConfiguration configuration, 
			int verticalAlignThreshold, int horizontalAlignThreshold,
			Font axisLabelFont, Font tickLabelFont, Color color) {

		this.setConfiguration(configuration);
		this.verticalAlignThreshold = verticalAlignThreshold;
		this.horizontalAlignThreshold = horizontalAlignThreshold;
		this.axisLabelFont = axisLabelFont;
		this.color = color;
	}


	/**
	 * Sets the configuration.
	 * 
	 * @param config The configuration.
	 */
	public synchronized void setConfiguration(RinearnGraph3DConfiguration configuration) {
		if (!configuration.hasScaleConfiguration()) {
			throw new IllegalArgumentException("The scale configuration is not stored in the specified configuration.");
		}
		if (!configuration.hasLabelConfiguration()) {
			throw new IllegalArgumentException("The label configuration is not stored in the specified configuration.");			
		}
		this.config = configuration;
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
	 * Sets the font for rendering texts of axis labels.
	 * 
	 * @param axisLabelFont The font for rendering texts of axis labels.
	 */
	public synchronized void setAxisLabelFont(Font axisLabelFont) {
		this.axisLabelFont = axisLabelFont;
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
	 * @param tickLabelFontMetrics The metrics of the font used for drawing tick labels.
	 */
	public synchronized void drawAxisLabels(List<GeometricPiece> geometricPieceList, FontMetrics tickLabelFontMetrics) {

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
	 * @param tickLabelFontMetrics The metrics of the font used for drawing tick labels.
	 */
	private void drawXAxisLabels(List<GeometricPiece> geometricPieceList, FontMetrics tickLabelFontMetrics) {

		// Define short aliases of some fields:
		RinearnGraph3DDrawingParameter.VerticalAlignment vAlign   = VERTICAL_ALIGNMENT;
		RinearnGraph3DDrawingParameter.HorizontalAlignment hAlign = HORIZONTAL_ALIGNMENT;
		int vThreshold   = this.verticalAlignThreshold;
		int hThreshold = this.horizontalAlignThreshold;
		double tickLabelMargin = this.config.getScaleConfiguration().getXScaleConfiguration().getTickLabelMargin();
		String[] tickLabels = this.xTickLabels;
		String axisLabel = this.config.getLabelConfiguration().getXLabelConfiguration().getText();

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
	 * @param tickLabelFontMetrics The metrics of the font used for drawing tick labels.
	 */
	private void drawYAxisLabels(List<GeometricPiece> geometricPieceList, FontMetrics tickLabelFontMetrics) {

		// Define short aliases of some fields:
		RinearnGraph3DDrawingParameter.VerticalAlignment vAlign   = VERTICAL_ALIGNMENT;
		RinearnGraph3DDrawingParameter.HorizontalAlignment hAlign = HORIZONTAL_ALIGNMENT;
		int vThreshold   = this.verticalAlignThreshold;
		int hThreshold = this.horizontalAlignThreshold;
		double tickLabelMargin = this.config.getScaleConfiguration().getYScaleConfiguration().getTickLabelMargin();
		String[] tickLabels = this.yTickLabels;
		String axisLabel = this.config.getLabelConfiguration().getYLabelConfiguration().getText();

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
	 * @param tickLabelFontMetrics The metrics of the font used for drawing tick labels.
	 */
	private void drawZAxisLabels(List<GeometricPiece> geometricPieceList, FontMetrics tickLabelFontMetrics) {

		// Define short aliases of some fields:
		RinearnGraph3DDrawingParameter.VerticalAlignment vAlign   = VERTICAL_ALIGNMENT;
		RinearnGraph3DDrawingParameter.HorizontalAlignment hAlign = HORIZONTAL_ALIGNMENT;
		int vThreshold   = this.verticalAlignThreshold;
		int hThreshold = this.horizontalAlignThreshold;
		double tickLabelMargin = this.config.getScaleConfiguration().getZScaleConfiguration().getTickLabelMargin();
		String[] tickLabels = this.zTickLabels;
		String axisLabel = this.config.getLabelConfiguration().getZLabelConfiguration().getText();

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
