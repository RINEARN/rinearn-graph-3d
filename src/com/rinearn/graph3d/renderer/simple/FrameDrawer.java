package com.rinearn.graph3d.renderer.simple;

import com.rinearn.graph3d.config.RinearnGraph3DFrameConfiguration;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.List;


/**
 * The class for drawing an outer frame (box) of the graph.
 */
public class FrameDrawer {

	/** The dimension index representing X. */
	public static final int X = 0;

	/** The dimension index representing Y. */
	public static final int Y = 1;

	/** The dimension index representing Z. */
	public static final int Z = 2;

	/** The number of geometric pieces composing a line. */
	public static final int LINE_PIECE_COUNT = 32;

	/** Stores the configuration of graph frames. */
	private volatile RinearnGraph3DFrameConfiguration frameConfig;

	/** The color of lines composing outer frames. */
	private volatile Color outerFrameColor;

	/** The color of grid lines. */
	private volatile Color gridLineColor;


	/**
	 * Creates a new instance for drawing graph frames under the specified configurations.
	 * 
	 * @param frameConfig The configuration of graph frames.
	 * @param color The color of outer frames.
	 * @param gridLineColor The color of grid lines.
	 */
	public FrameDrawer(RinearnGraph3DFrameConfiguration frameConfig, Color outerFrameColor, Color gridLineColor) {
		this.frameConfig = frameConfig;
		this.outerFrameColor = outerFrameColor;
		this.gridLineColor = gridLineColor;
	}


	/**
	 * Sets the configuration of graph frames.
	 * 
	 * @param frameConfig The configuration of graph frames.
	 */
	public synchronized void setFrameConfiguration(RinearnGraph3DFrameConfiguration frameConfig) {
		this.frameConfig = frameConfig;
	}

	/**
	 * Sets the color of lines composing outer frames.
	 * 
	 * @param color The color of outer frames.
	 */
	public synchronized void setOuterFrameColor(Color outerFrameColor) {
		this.outerFrameColor = outerFrameColor;
	}

	/**
	 * Sets the color of grid lines.
	 * 
	 * @param gridLineColor The color of grid lines.
	 */
	public synchronized void setGridLineColor(Color gridLineColor) {
		this.gridLineColor = gridLineColor;
	}


	/**
	 * Draws the graph frame.
	 * 
	 * @param geometricPieceList The list for storing the geometric pieces of the drawn contents by this method.
	 */
	public synchronized void drawFrame(List<GeometricPiece> geometricPieceList) {
		switch (this.frameConfig.getFrameMode()) {
			case NONE : {
				return;
			}
			case BOX : {
				this.drawBoxModeFrame(geometricPieceList);
				return;
			}
			default : {
				throw new RuntimeException("Unexpected frame mode: " + this.frameConfig.getFrameMode());
			}
		}
	}


	/**
	 * Draws the graph frame in BOX mode.
	 * 
	 * @param geometricPieceList The list for storing the geometric pieces of the drawn contents by this method.
	 */
	private void drawBoxModeFrame(List<GeometricPiece> geometricPieceList) {

		// Draw the floor of the outer frame.
		this.drawMultiPieceLine(
				geometricPieceList, new double[] {-1.0, -1.0, -1.0}, new double[] { 1.0, -1.0, -1.0}, // Coords of the edge points A and B.
				LINE_PIECE_COUNT, this.outerFrameColor
		);
		this.drawMultiPieceLine(
				geometricPieceList, new double[] { 1.0, -1.0, -1.0}, new double[] { 1.0,  1.0, -1.0},
				LINE_PIECE_COUNT, this.outerFrameColor
		);
		this.drawMultiPieceLine(
				geometricPieceList, new double[] { 1.0,  1.0, -1.0}, new double[] {-1.0,  1.0, -1.0},
				LINE_PIECE_COUNT, this.outerFrameColor
		);
		this.drawMultiPieceLine(
				geometricPieceList, new double[] {-1.0,  1.0, -1.0}, new double[] {-1.0, -1.0, -1.0},
				LINE_PIECE_COUNT, this.outerFrameColor
		);

		// Draw the ceil of the outer frame.
		this.drawMultiPieceLine(
				geometricPieceList, new double[] {-1.0, -1.0,  1.0}, new double[] { 1.0, -1.0,  1.0},
				LINE_PIECE_COUNT, this.outerFrameColor
		);
		this.drawMultiPieceLine(
				geometricPieceList, new double[] { 1.0, -1.0,  1.0}, new double[] { 1.0,  1.0,  1.0},
				LINE_PIECE_COUNT, this.outerFrameColor
		);
		this.drawMultiPieceLine(
				geometricPieceList, new double[] { 1.0,  1.0,  1.0}, new double[] {-1.0,  1.0,  1.0},
				LINE_PIECE_COUNT, this.outerFrameColor
		);
		this.drawMultiPieceLine(
				geometricPieceList, new double[] {-1.0,  1.0,  1.0}, new double[] {-1.0, -1.0,  1.0},
				LINE_PIECE_COUNT, this.outerFrameColor
		);

		// Draw pillars of the outer frame.
		this.drawMultiPieceLine(
				geometricPieceList, new double[] {-1.0, -1.0, -1.0}, new double[] {-1.0, -1.0,  1.0},
				LINE_PIECE_COUNT, this.outerFrameColor
		);
		this.drawMultiPieceLine(
				geometricPieceList, new double[] { 1.0, -1.0, -1.0}, new double[] { 1.0, -1.0,  1.0},
				LINE_PIECE_COUNT, this.outerFrameColor
		);
		this.drawMultiPieceLine(
				geometricPieceList, new double[] { 1.0,  1.0, -1.0}, new double[] { 1.0,  1.0,  1.0},
				LINE_PIECE_COUNT, this.outerFrameColor
		);
		this.drawMultiPieceLine(
				geometricPieceList, new double[] {-1.0,  1.0, -1.0}, new double[] {-1.0,  1.0,  1.0},
				LINE_PIECE_COUNT, this.outerFrameColor
		);
	}


	/**
	 * Draws a straight line consisting of multiple geometric pieces.
	 * 
	 * @param geometricPieceList The list for storing the geometric pieces of the drawn contents by this method.
	 * @param aCoords The coordinate values of the edge point A, in the scaled space.
	 * @param bCoords The coordinate values of the edge point B, in the scaled space.
	 * @param pieceCount The number of piece consisting of the line.
	 * @param color The color of the line.
	 */
	private void drawMultiPieceLine(List<GeometricPiece> geometricPieceList,
			double[] aCoords, double[] bCoords, int pieceCount, Color color) {

		this.drawMultiPieceDirectionalLine(geometricPieceList, aCoords, bCoords, null, pieceCount, color);
	}


	/**
	 * Draws a straight line visible only from the specific direction, consisting of multiple geometric pieces.
	 * 
	 * @param geometricPieceList The list for storing the geometric pieces of the drawn contents by this method.
	 * @param aCoords The coordinate values of the edge point A, in the scaled space.
	 * @param bCoords The coordinate values of the edge point B, in the scaled space.
	 * @param visibleDirectionVector The vector facing the direction from which the line is visible.
	 * @param pieceCount The number of piece consisting of the line.
	 * @param color The color of the line.
	 */
	private void drawMultiPieceDirectionalLine(List<GeometricPiece> geometricPieceList,
			double[] aCoords, double[] bCoords, double[] visibleDirectionVector, int pieceCount, Color color) {

		// Allocate an array for storing coordinates of nodes, 
		// which are equally N-divided points between the points A and B, where N is the number of the pieces.
		int nodeCount = pieceCount + 1;
		double[][] nodeCoords = new double[nodeCount][3]; // 3 is X/Y/Z

		// Calculate the vector from the point A to the first node.
		double[] deltaVector = new double[3]; // 3 is X/Y/Z
		deltaVector[X] = (bCoords[X] - aCoords[X]) / pieceCount;
		deltaVector[Y] = (bCoords[Y] - aCoords[Y]) / pieceCount;
		deltaVector[Z] = (bCoords[Z] - aCoords[Z]) / pieceCount;

		// Calculate the coordinates of the nodes.
		nodeCoords[0] = aCoords;
		nodeCoords[nodeCount - 1] = bCoords;
		for (int inode=1; inode<nodeCount - 1; inode++) {
			nodeCoords[inode][X] = aCoords[X] + deltaVector[X] * inode;
			nodeCoords[inode][Y] = aCoords[Y] + deltaVector[Y] * inode;
			nodeCoords[inode][Z] = aCoords[Z] + deltaVector[Z] * inode;
		}

		// Draw lines between all nodes.
		for (int ipiece=0; ipiece<pieceCount; ipiece++) {
			if (visibleDirectionVector == null) {
				LineGeometricPiece piece = new LineGeometricPiece(
						nodeCoords[ipiece    ][X], nodeCoords[ipiece    ][Y], nodeCoords[ipiece    ][Z],
						nodeCoords[ipiece + 1][X], nodeCoords[ipiece + 1][Y], nodeCoords[ipiece + 1][Z],
						1.0, color
				);
				geometricPieceList.add(piece);
			} else {
				DirectionalLineGeometricPiece piece = new DirectionalLineGeometricPiece(
						nodeCoords[ipiece    ][X], nodeCoords[ipiece    ][Y], nodeCoords[ipiece    ][Z],
						nodeCoords[ipiece + 1][X], nodeCoords[ipiece + 1][Y], nodeCoords[ipiece + 1][Z],
						new double[][] { visibleDirectionVector },
						1.0, color
				);
				geometricPieceList.add(piece);
			}
		}
	}
}
