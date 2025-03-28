package com.rinearn.graph3d.renderer.refimpl;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.range.RangeConfiguration;
import com.rinearn.graph3d.config.scale.AxisScaleConfiguration;
import com.rinearn.graph3d.config.scale.ScaleConfiguration;
import com.rinearn.graph3d.config.range.AxisRangeConfiguration;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.List;


/**
 * The class for drawing an outer frame (box) of the graph.
 */
public final class FrameDrawer {

	/** The dimension index representing X. */
	public static final int X = 0;

	/** The dimension index representing Y. */
	public static final int Y = 1;

	/** The dimension index representing Z. */
	public static final int Z = 2;

	/** The number of geometric pieces composing a line. */
	public static final int LINE_PIECE_COUNT = 32;

	/** Stores the configuration of this application. */
	private volatile RinearnGraph3DConfiguration config = null;

	/** The scale ticks generated from the configuration. */
	private ScaleTickGenerator.Result scaleTicks = null;


	/**
	 * Creates a new instance for drawing graph frames under the specified configurations.
	 */
	public FrameDrawer() {
	}


	/**
	 * Sets the configuration.
	 *
	 * @param configuration The configuration.
	 * @param scaleTicks The scale ticks generated from the configuration.
	 */
	public synchronized void setConfiguration(RinearnGraph3DConfiguration configuration, ScaleTickGenerator.Result scaleTicks) {
		if (!configuration.hasFrameConfiguration()) {
			throw new IllegalArgumentException("The frame configuration is stored in the specified configuration.");
		}
		if (!configuration.hasRangeConfiguration()) {
			throw new IllegalArgumentException("The range configuration is stored in the specified configuration.");
		}
		if (!configuration.hasColorConfiguration()) {
			throw new IllegalArgumentException("No color configuration is stored in the specified configuration.");
		}
		this.config = configuration;
		this.scaleTicks = scaleTicks;
	}


	/**
	 * Draws the graph frame.
	 *
	 * @param geometricPieceList The list for storing the geometric pieces of the drawn contents by this method.
	 */
	public synchronized void drawFrame(List<GeometricPiece> geometricPieceList) {
		if (this.config == null) {
			throw new IllegalStateException("This drawer instance has not been configured yet.");
		}
		if (!this.config.getFrameConfiguration().isFrameLinesVisible()) {
			return;
		}
		this.drawFrameLines(geometricPieceList);
	}


	/**
	 * Draws the graph frame.
	 *
	 * @param geometricPieceList The list for storing the geometric pieces of the drawn contents by this method.
	 */
	private synchronized void drawFrameLines(List<GeometricPiece> geometricPieceList) {
		switch (this.config.getFrameConfiguration().getShapeMode()) {
			case NONE : {
				return;
			}
			case BOX : {
				this.drawBoxModeFrameLines(geometricPieceList);
				return;
			}
			default : {
				throw new RuntimeException("Unexpected frame mode: " + this.config.getFrameConfiguration().getShapeMode());
			}
		}
	}


	/**
	 * Draws the graph frame in BOX mode.
	 *
	 * @param geometricPieceList The list for storing the geometric pieces of the drawn contents by this method.
	 */
	private void drawBoxModeFrameLines(List<GeometricPiece> geometricPieceList) {
		Color color = this.config.getColorConfiguration().getForegroundColor();

		// Draw the floor of the outer frame.
		this.drawMultiPieceLine(
				geometricPieceList, new double[] {-1.0, -1.0, -1.0}, new double[] { 1.0, -1.0, -1.0}, // Coords of the edge points A and B.
				LINE_PIECE_COUNT, color
		);
		this.drawMultiPieceLine(
				geometricPieceList, new double[] { 1.0, -1.0, -1.0}, new double[] { 1.0,  1.0, -1.0},
				LINE_PIECE_COUNT, color
		);
		this.drawMultiPieceLine(
				geometricPieceList, new double[] { 1.0,  1.0, -1.0}, new double[] {-1.0,  1.0, -1.0},
				LINE_PIECE_COUNT, color
		);
		this.drawMultiPieceLine(
				geometricPieceList, new double[] {-1.0,  1.0, -1.0}, new double[] {-1.0, -1.0, -1.0},
				LINE_PIECE_COUNT, color
		);

		// Draw the ceil of the outer frame.
		this.drawMultiPieceLine(
				geometricPieceList, new double[] {-1.0, -1.0,  1.0}, new double[] { 1.0, -1.0,  1.0},
				LINE_PIECE_COUNT, color
		);
		this.drawMultiPieceLine(
				geometricPieceList, new double[] { 1.0, -1.0,  1.0}, new double[] { 1.0,  1.0,  1.0},
				LINE_PIECE_COUNT, color
		);
		this.drawMultiPieceLine(
				geometricPieceList, new double[] { 1.0,  1.0,  1.0}, new double[] {-1.0,  1.0,  1.0},
				LINE_PIECE_COUNT, color
		);
		this.drawMultiPieceLine(
				geometricPieceList, new double[] {-1.0,  1.0,  1.0}, new double[] {-1.0, -1.0,  1.0},
				LINE_PIECE_COUNT, color
		);

		// Draw pillars of the outer frame.
		this.drawMultiPieceLine(
				geometricPieceList, new double[] {-1.0, -1.0, -1.0}, new double[] {-1.0, -1.0,  1.0},
				LINE_PIECE_COUNT, color
		);
		this.drawMultiPieceLine(
				geometricPieceList, new double[] { 1.0, -1.0, -1.0}, new double[] { 1.0, -1.0,  1.0},
				LINE_PIECE_COUNT, color
		);
		this.drawMultiPieceLine(
				geometricPieceList, new double[] { 1.0,  1.0, -1.0}, new double[] { 1.0,  1.0,  1.0},
				LINE_PIECE_COUNT, color
		);
		this.drawMultiPieceLine(
				geometricPieceList, new double[] {-1.0,  1.0, -1.0}, new double[] {-1.0,  1.0,  1.0},
				LINE_PIECE_COUNT, color
		);
	}


	/**
	 * Draws the grid lines on the backwalls of the graph.
	 *
	 * @param geometricPieceList The list for storing the geometric pieces of the drawn contents by this method.
	 * @param axes The array storing X axis at [0], Y axis at [1], and Z axis at [2].
	 * @param xTickCoordinates The coordinates of the ticks on X axis.
	 * @param yTickCoordinates The coordinates of the ticks on Y axis.
	 * @param zTickCoordinates The coordinates of the ticks on Z axis.
	 */
	public synchronized void drawGridLines(List<GeometricPiece> geometricPieceList) {
		if (this.config == null) {
			throw new IllegalArgumentException("This drawer instance has not been configured yet.");
		}
		if (!this.config.getScaleConfiguration().isGridLinesVisible()) {
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
				xRangeConfig.getMinimumCoordinate(), xRangeConfig.getMaximumCoordinate(), xScaleConfig.isLogScaleEnabled()
		);
		SpaceConverter ySpaceConverter = new SpaceConverter(
				yRangeConfig.getMinimumCoordinate(), yRangeConfig.getMaximumCoordinate(), yScaleConfig.isLogScaleEnabled()
		);
		SpaceConverter zSpaceConverter = new SpaceConverter(
				zRangeConfig.getMinimumCoordinate(), zRangeConfig.getMaximumCoordinate(), zScaleConfig.isLogScaleEnabled()
		);

		// Draw ticks on X axis.
		for (BigDecimal tickCoord: this.scaleTicks.xTickCoordinates) {
			double scaledCoord = xSpaceConverter.toScaledSpaceCoordinate(tickCoord.doubleValue());
			this.drawXGridLines(geometricPieceList, scaledCoord);
		}

		// Draw ticks on Y axis.
		for (BigDecimal tickCoord: this.scaleTicks.yTickCoordinates) {
			double scaledCoord = ySpaceConverter.toScaledSpaceCoordinate(tickCoord.doubleValue());
			this.drawYGridLines(geometricPieceList, scaledCoord);
		}

		// Draw ticks on Z axis.
		for (BigDecimal tickCoord: this.scaleTicks.zTickCoordinates) {
			double scaledCoord = zSpaceConverter.toScaledSpaceCoordinate(tickCoord.doubleValue());
			this.drawZGridLines(geometricPieceList, scaledCoord);
		}
	}


	/**
	 * Draw grid lines connecting specified ticks on four X axes.
	 *
	 * @param geometricPieceList The list for storing the geometric pieces of the drawn contents by this method.
	 * @param xCoord The scaled coordinate value of the tick to be connected by the grid line.
	 */
	private void drawXGridLines(List<GeometricPiece> geometricPieceList, double xScaledCoord) {
		Color color = this.config.getColorConfiguration().getGridColor();

		// (Y=-1, Z=-1) to (Y=1, Z=-1), visible from positive direction of Z axis.
		this.drawMultiPieceDirectionalLine(
				geometricPieceList,
				new double[] {xScaledCoord, -1.0, -1.0}, new double[] {xScaledCoord, 1.0, -1.0}, // Edge points A and B.
				new double[] {0.0, 0.0, 1.0}, // The vector of the direction from which the line is visible.
				LINE_PIECE_COUNT, color
		);

		// (Y=1, Z=-1) to (Y=1, Z=1), visible from negative direction of Y axis.
		this.drawMultiPieceDirectionalLine(
				geometricPieceList,
				new double[] {xScaledCoord, 1.0, -1.0}, new double[] {xScaledCoord, 1.0, 1.0},
				new double[] {0.0, -1.0, 0.0},
				LINE_PIECE_COUNT, color
		);

		// (Y=1, Z=1) to (Y=-1, Z=1), visible from negative direction of Z axis.
		this.drawMultiPieceDirectionalLine(
				geometricPieceList,
				new double[] {xScaledCoord, 1.0, 1.0}, new double[] {xScaledCoord, -1.0, 1.0},
				new double[] {0.0, 0.0, -1.0},
				LINE_PIECE_COUNT, color
		);

		// (Y=-1, Z=1) to (Y=-1, Z=-1), visible from positive direction of Y axis.
		this.drawMultiPieceDirectionalLine(
				geometricPieceList,
				new double[] {xScaledCoord, -1.0, 1.0}, new double[] {xScaledCoord, -1.0, -1.0},
				new double[] {0.0, 1.0, 0.0},
				LINE_PIECE_COUNT, color
		);
	}


	/**
	 * Draw grid lines connecting specified ticks on four Y axes.
	 *
	 * @param geometricPieceList The list for storing the geometric pieces of the drawn contents by this method.
	 * @param xCoord The scaled coordinate value of the tick to be connected by the grid line.
	 */
	private void drawYGridLines(List<GeometricPiece> geometricPieceList, double yScaledCoord) {
		Color color = this.config.getColorConfiguration().getGridColor();

		// (X=-1, Z=-1) to (X=1, Z=-1), visible from positive direction of Z axis.
		this.drawMultiPieceDirectionalLine(
				geometricPieceList,
				new double[] {-1.0, yScaledCoord, -1.0}, new double[] {1.0, yScaledCoord, -1.0}, // Edge points A and B.
				new double[] {0.0, 0.0, 1.0}, // The vector of the direction from which the line is visible.
				LINE_PIECE_COUNT, color
		);

		// (X=1, Z=-1) to (X=1, Z=1), visible from negative direction of X axis.
		this.drawMultiPieceDirectionalLine(
				geometricPieceList,
				new double[] {1.0, yScaledCoord, -1.0}, new double[] {1.0, yScaledCoord, 1.0},
				new double[] {-1.0, 0.0, 0.0},
				LINE_PIECE_COUNT, color
		);

		// (X=1, Z=1) to (X=-1, Z=1), visible from negative direction of Z axis.
		this.drawMultiPieceDirectionalLine(
				geometricPieceList,
				new double[] {1.0, yScaledCoord, 1.0}, new double[] {-1.0, yScaledCoord, 1.0},
				new double[] {0.0, 0.0, -1.0},
				LINE_PIECE_COUNT, color
		);

		// (X=-1, Z=1) to (X=-1, Z=-1), visible from positive direction of X axis.
		this.drawMultiPieceDirectionalLine(
				geometricPieceList,
				new double[] {-1.0, yScaledCoord, 1.0}, new double[] {-1.0, yScaledCoord, -1.0},
				new double[] {1.0, 0.0, 0.0},
				LINE_PIECE_COUNT, color
		);
	}


	/**
	 * Draw grid lines connecting specified ticks on four Z axes.
	 *
	 * @param geometricPieceList The list for storing the geometric pieces of the drawn contents by this method.
	 * @param xCoord The scaled coordinate value of the tick to be connected by the grid line.
	 */
	private void drawZGridLines(List<GeometricPiece> geometricPieceList, double zScaledCoord) {
		Color color = this.config.getColorConfiguration().getGridColor();

		// (X=-1, Y=-1) to (X=1, Y=-1), visible from positive direction of Y axis.
		this.drawMultiPieceDirectionalLine(
				geometricPieceList,
				new double[] {-1.0, -1.0, zScaledCoord}, new double[] {1.0, -1.0, zScaledCoord}, // Edge points A and B.
				new double[] {0.0, 1.0, 0.0}, // The vector of the direction from which the line is visible.
				LINE_PIECE_COUNT, color
		);

		// (X=1, Y=-1) to (X=1, Y=1), visible from negative direction of X axis.
		this.drawMultiPieceDirectionalLine(
				geometricPieceList,
				new double[] {1.0, -1.0, zScaledCoord}, new double[] {1.0, 1.0, zScaledCoord},
				new double[] {-1.0, 0.0, 0.0},
				LINE_PIECE_COUNT, color
		);

		// (X=1, Y=1) to (X=-1, Y=1), visible from negative direction of Y axis.
		this.drawMultiPieceDirectionalLine(
				geometricPieceList,
				new double[] {1.0, 1.0, zScaledCoord}, new double[] {-1.0, 1.0, zScaledCoord},
				new double[] {0.0, -1.0, 0.0},
				LINE_PIECE_COUNT, color
		);

		// (X=-1, Y=1) to (X=-1, Y=-1), visible from positive direction of X axis.
		this.drawMultiPieceDirectionalLine(
				geometricPieceList,
				new double[] {-1.0, 1.0, zScaledCoord}, new double[] {-1.0, -1.0, zScaledCoord},
				new double[] {1.0, 0.0, 0.0},
				LINE_PIECE_COUNT, color
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
