package com.rinearn.graph3d.renderer.simple;

import com.rinearn.graph3d.renderer.RinearnGraph3DDrawingParameter;
import com.rinearn.graph3d.config.ColorConfiguration;
import com.rinearn.graph3d.config.ColorGradient;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.math.MathContext;


// !!! NOTE !!!
//
// 実装してて思ったけどやっぱりこれは明らかに config 管轄ではない。
// 仮にレンダラーの派生実装毎にほぼ重複する事になっても、config 管轄に移すのはまずいと思う。
// config は実質的にAPI仕様の一部になってしまうので、こういう実装ゴリゴリのやつ入れると将来互換にネックになりそうだし。
// そもそも雰囲気的にも違和感全開だし。configにあると。
// なのでレンダラー管轄のままでいい。
//
// !!! NOTE !!!

//!!! NOTE2 !!!
//
// BigDecimal使いまくってるからポリゴン細かい場合にもしかして生成重いかも。
// 同一のColorConfig下では最下層の線形補間処理以外の結果はキャッシュできるので要検討。
// 具体的には ColorConfig 更新メソッド作ってそれ呼んだタイミングでキャッシュ更新するとか。
//
//!!! NOTE2 !!!


/**
 * The class of the color mixer,
 * which generates colors of geometric pieces (points, lines, and so on),
 * when the automatic-coloring feature is enabled.
 */
public final class ColorMixer {

	/** The index representing X, in coordinate arrays. */
	private static final int X = 0;

	/** The index representing Y, in coordinate arrays. */
	private static final int Y = 1;

	/** The index representing Z, in coordinate arrays. */
	private static final int Z = 2;

	/** The index representing scalar-dimension, in coordinate arrays. */
	private static final int SCALAR = 3;


	/**
	 * Generates a color for drawing a geometric piece (a point, a line, and so on).
	 *
	 * @param coordinates The coordinate values of the representative point. The index is [0:X, 1:Y, 2:Z, 3:scalar-dimension].
	 * @param drawingParam The drawing parameter specified for drawing the geometric piece.
	 * @param colorConfig The color configuration.
	 * @return The generated color.
	 */
	public synchronized Color generateColor(
			double[] coordinates, RinearnGraph3DDrawingParameter drawingParam, ColorConfiguration colorConfig) {

		// Convert the arg "coordinates" to BigDecimal values.
		int coordinateCount = coordinates.length;
		BigDecimal[] bigDecimalCoords = new BigDecimal[coordinateCount];
		for (int icoord=0; icoord<coordinateCount; icoord++) {
			bigDecimalCoords[icoord] = new BigDecimal(coordinates[icoord]);
		}

		return this.generateColor(bigDecimalCoords, drawingParam, colorConfig);
	}


	/**
	 * Generates a color for drawing a geometric piece (a point, a line, and so on).
	 *
	 * @param coordinates The coordinate values of the representative point. The index is [0:X, 1:Y, 2:Z, 3:scalar-dimension].
	 * @param drawingParam The drawing parameter specified for drawing the geometric piece.
	 * @param colorConfig The color configuration.
	 * @return The generated color.
	 */
	public synchronized Color generateColor(
			BigDecimal[] coordinates, RinearnGraph3DDrawingParameter drawingParam, ColorConfiguration colorConfig) {
		// If the automatic-coloring feature is disabled, the color is explicitly specified in the param object.
		// So return that color.
		if (!drawingParam.isAutoColoringEnabled()) {
			return drawingParam.getColor();
		}

		// Gets the coloring modes defined in the color configuration.
		ColorConfiguration.DataColoringMode[] coloringModes = colorConfig.getDataColoringModes();
		int coloringModeCount = coloringModes.length;

		// The index of the above array is basically equals to the data series index.
		// Also, if the data series index exceeds the length of the array, regard the array as "circular array".
		int seriesIndex = drawingParam.getSeriesIndex();
		int coloringModeIndex = seriesIndex % coloringModeCount;
		ColorConfiguration.DataColoringMode coloringMode = coloringModes[coloringModeIndex];

		// Generate the color corresponding with the coloring mode.
		switch (coloringMode) {
			case SOLID : {

				// Extract the solid color defined in the color configuration.
				Color solidColor = this.extractSolidColor(seriesIndex, colorConfig);
				return solidColor;
			}
			case GRADIENT : {

				// Extract the color gradient defined in the color configuration.
				ColorGradient gradient = this.extractColorGradient(seriesIndex, colorConfig);

				// Generate the color by the color gradient.
				Color gradientColor = this.generateColorByGradient(coordinates, gradient);

				return gradientColor;
			}
			default : {
				throw new IllegalArgumentException("Unknown coloring mode: " + coloringMode);
			}
		}
	}


	/**
	 * Extracts a solid color corresponding to the specified data series index, from the color configuration.
	 *
	 * @param seriesIndex The index of the data series.
	 * @param colorConfig The color configuration.
	 * @return The solid color.
	 */
	private Color extractSolidColor(int seriesIndex, ColorConfiguration colorConfig) {

		// Gets the solid colors defined in the color configuration.
		Color[] solidColors = colorConfig.getDataSolidColors();
		int solidColorCount = solidColors.length;

		// The index of the above array is basically equals to the data series index.
		// Also, if the data series index exceeds the length of the array, regard the array as "circular array".
		int solidColorIndex = seriesIndex % solidColorCount;
		Color solidColor = solidColors[solidColorIndex];
		return solidColor;
	}


	/**
	 * Extracts a color gradient corresponding to the specified data series index, from the color configuration.
	 *
	 * @param seriesIndex The index of the data series.
	 * @param colorConfig The color configuration.
	 * @return The color gradient.
	 */
	private ColorGradient extractColorGradient(int seriesIndex, ColorConfiguration colorConfig) {

		// Gets the color gradients defined in the color configuration.
		ColorGradient[] gradients = colorConfig.getDataColorGradients();
		int gradientCount = gradients.length;

		// The index of the above array is basically equals to the data series index.
		// Also, if the data series index exceeds the length of the array, regard the array as "circular array".
		int gradientIndex = seriesIndex % gradientCount;
		ColorGradient gradient = gradients[gradientIndex];
		return gradient;
	}


	/**
	 * Generates the color by the color gradient.
	 *
	 * @param coordinates
	 *     The coordinate values of the representative point to determine the color.
	 *     The array index is [0:X, 1:Y, 2:Z, 3:scalar-dimension].
	 * @param gradient The color gradient.
	 * @return The generated color.
	 */
	private Color generateColorByGradient(BigDecimal[] coordinates, ColorGradient gradient) {

		// Get the number of axes (dimensions), and gradients for the directions of them.
		int axisCount = gradient.getAxisCount();
		ColorGradient.AxisColorGradient[] axisGradients = gradient.getAxisColorGradients();

		// Generate a color for each axis by its gradient.
		Color[] axisColors = new Color[axisCount];
		ColorGradient.BlendMode[] axisBlendModes = new ColorGradient.BlendMode[axisCount];
		for (int iaxis=0; iaxis<axisCount; iaxis++) {
			axisColors[iaxis] = this.generateAxisColorByAxisGradient(coordinates, axisGradients[iaxis]);
			axisBlendModes[iaxis] = axisGradients[iaxis].getBlendMode();
		}

		// Blend the colors generated by all axes's gradients, and return it.
		Color blendedColor = this.blendAxisColors(axisColors, axisBlendModes, axisCount, gradient.getBackgroundColor());
		return blendedColor;
	}


	/**
	 * Blend the colors generated by all axes's gradients.
	 *
	 * @param axisColors The colors generated by all axes's gradients.
	 * @param axisBlendModes The blend mode for blending each axis's color into the result.
	 * @param axisCount The total number of the axes of the gradient.
	 * @param backgroundColor The background color of the blending.
	 * @return The blended color.
	 */
	private Color blendAxisColors(Color[] axisColors, ColorGradient.BlendMode[] axisBlendModes, int axisCount, Color backgroundColor) {

		// Extract Red/Green/Blue/Alpha component of the background color.
		double resultR = backgroundColor.getRed() / 255.0;
		double resultG = backgroundColor.getGreen() / 255.0;
		double resultB = backgroundColor.getBlue() / 255.0;
		double resultA = backgroundColor.getAlpha() / 255.0;

		// Blend each gradient axis's color to the above color components.
		for (int iaxis=0; iaxis<axisCount; iaxis++) {

			// Extract Red/Green/Blue/Alpha component of the color generated by the axis's gradient.
			Color axisColor = axisColors[iaxis];
			double axisR = axisColor.getRed() / 255.0;
			double axisG = axisColor.getGreen() / 255.0;
			double axisB = axisColor.getBlue() / 255.0;
			double axisA = axisColor.getAlpha() / 255.0;

			// Blend the color components, based on the axis's blend mode.
			switch (axisBlendModes[iaxis]) {
				case ADDITION : {
					resultR += axisR;
					resultG += axisG;
					resultB += axisB;
					resultA += axisA;
					break;
				}
				case MULTIPLICATION : {
					resultR *= axisR;
					resultG *= axisG;
					resultB *= axisB;
					resultA *= axisA;
					break;
				}
				default : {
					throw new IllegalArgumentException("Unknown blend mode: " + axisBlendModes[iaxis]);
				}
			}

			// The color components may exceed 1.0 by the above operations, so crop them into the range [0.0, 1.0].
			resultR = Math.max(Math.min(resultR, 1.0), 0.0);
			resultG = Math.max(Math.min(resultG, 1.0), 0.0);
			resultB = Math.max(Math.min(resultB, 1.0), 0.0);
			resultA = Math.max(Math.min(resultA, 1.0), 0.0);
		}

		// Create a Color instance having the blended color components, and return it.
		Color resultColor = new Color(
				(int)(resultR * 255),
				(int)(resultG * 255),
				(int)(resultB * 255),
				(int)(resultA * 255)
		);
		return resultColor;
	}


	/**
	 * Generate a color of one axis (dimension) of the gradient (which may have multiple axes).
	 *
	 * @param coordinates
	 *     The coordinate values of the representative point to determine the color.
	 *     The array index is [0:X, 1:Y, 2:Z, 3:scalar-dimension].
	 * @param axisGradient The color gradient of the axis.
	 * @return The generated color.
	 */
	private Color generateAxisColorByAxisGradient(BigDecimal[] coordinates, ColorGradient.AxisColorGradient axisGradient) {

		// Get the colors at the boundary points.
		int boundaryCount = axisGradient.getBoundaryCount();
		Color[] boundaryColors = axisGradient.getBoundaryColors();

		// Get/generate the coordinate values of the boundary points.
		BigDecimal[] boundaryCoords = this.getOrGenerateBoundaryCoordinates(axisGradient);

		// Extract the coordinate value of the representative point, on the gradient axis.
		BigDecimal representCoord = this.extractCoordinateOnAxis(coordinates, axisGradient.getAxis());

		// If the representative coord's  is smaller than (or equals to) the minimum coord,
		// return the color of the boundary point of which coord is minimum.
		if (representCoord.compareTo(boundaryCoords[0]) <= 0) {
			return boundaryColors[0];
		}

		// If the representative point's coord is larger than (or equals to) the maximum coord,
		// return the color of the boundary point of which coordinate is maximum.
		if (0 <= representCoord.compareTo(boundaryCoords[boundaryCount - 1])) {
			return boundaryColors[boundaryCount - 1];
		}

		// Detect the neighbor boundary point of the representative point.
		int upperBoundaryIndex = -1;
		int lowerBoundaryIndex = -1;
		for (int ibound=0; ibound<boundaryCount; ibound++) {
			if (0 <= representCoord.compareTo(boundaryCoords[ibound])) {
				lowerBoundaryIndex = ibound;
				upperBoundaryIndex = ibound + 1;
			}
		}

		// Generate the color, by the algorithm specified as the interpolation mode.
		switch (axisGradient.getInterpolationMode()) {
			case STEP : {
				return boundaryColors[lowerBoundaryIndex];
			}
			case LINEAR : {
				return this.generateColorByLinearInterpolation(
						representCoord,
						boundaryCoords[lowerBoundaryIndex], boundaryCoords[upperBoundaryIndex],
						boundaryColors[lowerBoundaryIndex], boundaryColors[upperBoundaryIndex]
				);
			}
			default : {
				throw new IllegalArgumentException("Unknown interpolation mode: " + axisGradient.getInterpolationMode());
			}
		}
	}


	/**
	 * Generates a color of the representation point, by linear interpolation from the colors of its neighbor boundary points.
	 *
	 * @param representCoord The coordinate value of the representation point to determine the color.
	 * @param lowerBoundaryCoord The coordinate value of the lower-side neighbor boundary point.
	 * @param upperBoundaryCoord The coordinate value of the upper-side neighbor boundary point.
	 * @param lowerBoundaryColor The color of the lower-side neighbor boundary point.
	 * @param upperBoundaryColor The color of the upper-side neighbor boundary point.
	 * @return The generated color.
	 */
	private Color generateColorByLinearInterpolation(BigDecimal representCoord,
			BigDecimal lowerBoundaryCoord, BigDecimal upperBoundaryCoord,
			Color lowerBoundaryColor, Color upperBoundaryColor) {

		// In this step, no equality-comparisons are necessary any more.
		// (They are already done in upper hierarchy methods.)

		// With considering the precision of the color component,
		// compute values using double-values instead of BigDecimal values here.

		// Compute the length between the upper boundary's coord and the lower boundary's coord.
		double sectionLength = upperBoundaryCoord.doubleValue() - lowerBoundaryCoord.doubleValue();

		// Compute the "normalized level" of the representation point.
		// If the point equals to the lower boundary, the level is 0.0.
		// If equals to the upper boundary, the level is 1.0.
		double normalizedLevel = (representCoord.doubleValue() - lowerBoundaryCoord.doubleValue()) / sectionLength;

		// Extract the values of the color components of the upper/lower boundary color.
		int lowerBoundaryR = lowerBoundaryColor.getRed();
		int lowerBoundaryG = lowerBoundaryColor.getGreen();
		int lowerBoundaryB = lowerBoundaryColor.getBlue();
		int lowerBoundaryA = lowerBoundaryColor.getAlpha();
		int upperBoundaryR = upperBoundaryColor.getRed();
		int upperBoundaryG = upperBoundaryColor.getGreen();
		int upperBoundaryB = upperBoundaryColor.getBlue();
		int upperBoundaryA = upperBoundaryColor.getAlpha();

		// Blend the above color component by linear interpolation.
		int interpolationR = (int)(normalizedLevel * upperBoundaryR + (1.0 - normalizedLevel) * lowerBoundaryR);
		int interpolationG = (int)(normalizedLevel * upperBoundaryG + (1.0 - normalizedLevel) * lowerBoundaryG);
		int interpolationB = (int)(normalizedLevel * upperBoundaryB + (1.0 - normalizedLevel) * lowerBoundaryB);
		int interpolationA = (int)(normalizedLevel * upperBoundaryA + (1.0 - normalizedLevel) * lowerBoundaryA);

		// If any color component exceed the range [0, 255], crop into the range.
		interpolationR = Math.max(Math.min(interpolationR, 255), 0);
		interpolationG = Math.max(Math.min(interpolationG, 255), 0);
		interpolationB = Math.max(Math.min(interpolationB, 255), 0);
		interpolationA = Math.max(Math.min(interpolationA, 255), 0);

		// Create a Color instance from the computed color components, and return it.
		Color interpolationColor = new Color(interpolationR, interpolationG, interpolationB, interpolationA);
		return interpolationColor;
	}


	/**
	 * Extract the coordinate value corresponding on the specified axis.
	 *
	 * @param coordinates The array storing the coordinate values, where the index is [0:X, 1:Y, 2:Z, 3:scalar-dimension].
	 * @param axis The axis.
	 * @return The coordinate value corresponding to the specified axis.
	 */
	private BigDecimal extractCoordinateOnAxis(BigDecimal[] coordinates, ColorGradient.Axis axis) {
		switch (axis) {
			case X : return coordinates[X];
			case Y : return coordinates[Y];
			case Z : return coordinates[Z];
			case COLUMN_4 : return coordinates[SCALAR];
			default : throw new IllegalArgumentException("Unknown axis: " + axis);
		}
	}


	/**
	 * Gets or generates the coordinate values of the boundary points.
	 *
	 * @param axisGradient The color gradient of the axis.
	 * @return The coordinate values of the boundary points.
	 */
	private BigDecimal[] getOrGenerateBoundaryCoordinates(ColorGradient.AxisColorGradient axisGradient) {
		switch (axisGradient.getBoundaryMode()) {
			case MANUAL : {
				return axisGradient.getBoundaryCoordinates();
			}
			case EQUAL_DIVISION : {

				// Prepare precision-related settings
				int roundingPrecision = 30; // Sufficient for computing the color component.
				MathContext mathContext = new MathContext(roundingPrecision, RoundingMode.HALF_EVEN);

				// Compute the value of "delta", which is the length of a section between two boundary points.
				int boundaryCount = axisGradient.getBoundaryCount();
				int sectionCount = boundaryCount - 1;
				BigDecimal minCoord = axisGradient.getMinimumBoundaryCoordinate();
				BigDecimal maxCoord = axisGradient.getMaximumBoundaryCoordinate();
				BigDecimal delta = maxCoord.subtract(minCoord).divide(new BigDecimal(sectionCount), mathContext);

				// Create an array for storing results, and store min/max coords at the top/end of it.
				BigDecimal[] coords = new BigDecimal[boundaryCount];
				coords[0] = minCoord;
				coords[boundaryCount - 1] = maxCoord;

				// Compute the coordinates at equally divided boundary points, and store them into the array.
				for (int ibound=1; ibound<boundaryCount-1; ibound++) {
					coords[ibound] = minCoord.add(delta.multiply(new BigDecimal(ibound), mathContext));
				}
				return coords;
			}
			default : {
				throw new IllegalArgumentException("Unknown boundary mode: " + axisGradient.getBoundaryMode());
			}
		}
	}

}
