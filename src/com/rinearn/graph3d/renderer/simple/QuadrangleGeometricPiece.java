package com.rinearn.graph3d.renderer.simple;

import java.awt.Color;
import java.awt.Graphics2D;

import com.rinearn.graph3d.config.LightConfiguration;


/**
 * A geometric piece class representing a line.
 */
public final class QuadrangleGeometricPiece extends GeometricPiece {

	/** Represents the array index of the vertex point A, in vertex-related arrays. */
	private static final int A = 0;

	/** Represents the array index of the vertex point B, in vertex-related arrays. */
	private static final int B = 1;

	/** Represents the array index of the vertex point C, in vertex-related arrays. */
	private static final int C = 2;

	/** Represents the array index of the vertex point D, in vertex-related arrays. */
	private static final int D = 3;

	/** An enum for representing a set of vertices for computing the normal vector of this quadrangle. */
	private enum NormalVectorVertices {

		/** Represents the set of the vertices { A, B, C }. */
		ABC,

		/** Represents the set of the vertices { A, C, D }. */
		ACD
	}

	/** Stores a set of vertices for computing the normal vector of this quadrangle. */
	private NormalVectorVertices normalVectorVertices;


	/**
	 * Creates a new geometric piece representing a quadrangle consisting of points A, B, C and D.
	 * 
	 * @param aX The x coordinate value of the point A, in the scaled space.
	 * @param aY The y coordinate value of the point A, in the scaled space.
	 * @param aZ The z coordinate value of the point A, in the scaled space.
	 * @param bX The x coordinate value of the point B, in the scaled space.
	 * @param bY The y coordinate value of the point B, in the scaled space.
	 * @param bZ The z coordinate value of the point B, in the scaled space.
	 * @param cX The x coordinate value of the point C, in the scaled space.
	 * @param cY The y coordinate value of the point C, in the scaled space.
	 * @param cZ The z coordinate value of the point C, in the scaled space.
	 * @param dX The x coordinate value of the point D, in the scaled space.
	 * @param dY The y coordinate value of the point D, in the scaled space.
	 * @param dZ The z coordinate value of the point D, in the scaled space.
	 * @param z The z coordinate value of the center of the point, in the scaled space.
	 * @param radius The radius (pixels) of the point.
	 * @param color The color of the point.
	 */
	public QuadrangleGeometricPiece(double aX, double aY, double aZ,
			double bX, double bY, double bZ,
			double cX, double cY, double cZ,
			double dX, double dY, double dZ,
			Color color) {

		// Detect whether there is a couple of points having the same coordinate values in {A, B, C}.
		boolean existsSamePointInABC =
				(aX==bX && aY==bY && aZ==bZ) || 
				(aX==cX && aY==cY && aZ==cZ) || 
				(bX==cX && bY==cY && bZ==cZ);

		 // Basically, the normal vector will be calculated for the triangle consisting of the points A, B, and C.
		 // When there is a couple of points which have the same coordinate values in the points {A, B, C}, 
		 // the normal vector will be calculated for the triangle consisting of the points A, C, and D.		
		this.normalVectorVertices = existsSamePointInABC ? NormalVectorVertices.ACD : NormalVectorVertices.ABC;
		double[] normalVector = this.computeNormalVector(
			aX, aY, aZ , bX, bY, bZ, cX, cY, cZ, dX, dY, dZ, this.normalVectorVertices
		);

		// Store the scaled coordinate values of the points A, B, C, and D into the vertex array.
		// Also, store the normal vector as the last vertex, expediently.
		this.scaledVertexArray = new double[][] {
			{ aX, aY, aZ, 1.0 }, // The last element "1.0" is so-called W value.
			{ bX, bY, bZ, 1.0 },
			{ cX, cY, cZ, 1.0 },
			{ dX, dY, dZ, 1.0 },

			// For the normal vector, we should transform only its angle, 
			// so we should ignore effects of the translational elements of the transformation matrix.
			// So set the value of W (the last element of the following) to 0.
			{ normalVector[X], normalVector[Y], normalVector[Z], 0.0 },
		};

		// Initialize other fields.
		this.vertexCount = 5; // 4 vertex vectors + 1 normal vector
		this.transformedVertexArray = new double[this.vertexCount][3]; // [3] is X/Y/Z
		this.projectedVertexArray = new int[this.vertexCount][2];      // [2] is X/Y
		this.originalColor = color;
	}


	/**
	 * Computes the normal vector of the quadrangle of which vertices is the points A, B, C, and D.
	 * 
	 * @param aX The x coordinate value of the point A.
	 * @param aY The y coordinate value of the point A.
	 * @param aZ The z coordinate value of the point A.
	 * @param bX The x coordinate value of the point B.
	 * @param bY The y coordinate value of the point B.
	 * @param bZ The z coordinate value of the point B.
	 * @param cX The x coordinate value of the point C.
	 * @param cY The y coordinate value of the point C.
	 * @param cZ The z coordinate value of the point C.
	 * @param normalVectorVertices Represents a set of vertices for computing the normal vector.
	 * @return The array storing x, y, and z coordinate values of the computed normal vector.
	 */
	private double[] computeNormalVector(double aX, double aY, double aZ,
			double bX, double bY, double bZ,
			double cX, double cY, double cZ,
			double dX, double dY, double dZ,
			NormalVectorVertices normalVectorVertices) {

		// Arrays for storing the vectors of the triangle sides.
		double[] sideVectorP = new double[3];
		double[] sideVectorQ = new double[3];

		// Calculate the coordinate values of the above 'triangle side' vectors.
		if (normalVectorVertices == NormalVectorVertices.ABC) {
			sideVectorP[X] = bX - aX;
			sideVectorP[Y] = bY - aY;
			sideVectorP[Z] = bZ - aZ;

			sideVectorQ[X] = cX - aX;
			sideVectorQ[Y] = cY - aY;
			sideVectorQ[Z] = cZ - aZ;

		} else if (normalVectorVertices == NormalVectorVertices.ACD) {
			sideVectorP[X] = cX - aX;
			sideVectorP[Y] = cY - aY;
			sideVectorP[Z] = cZ - aZ;

			sideVectorQ[X] = dX - aX;
			sideVectorQ[Y] = dY - aY;
			sideVectorQ[Z] = dZ - aZ;

		} else {
			throw new RuntimeException("Unexpected normal vector vertices: " + normalVectorVertices);
		}

		// Calculate the normal vector as the cross product of the 'triangle side' vectors.
		double[] normalVector = new double[3];
		normalVector[X] = sideVectorP[Y] * sideVectorQ[Z] - sideVectorP[Z] * sideVectorQ[Y];
		normalVector[Y] = sideVectorP[Z] * sideVectorQ[X] - sideVectorP[X] * sideVectorQ[Z];
		normalVector[Z] = sideVectorP[X] * sideVectorQ[Y] - sideVectorP[Y] * sideVectorQ[X];

		// Normalize the length of the normal vector.
		double normalVectorLengthRecip = 1.0 / Math.sqrt(
				normalVector[X] * normalVector[X] + normalVector[Y] * normalVector[Y] + normalVector[Z] * normalVector[Z]
		);
		normalVector[X] *= normalVectorLengthRecip;
		normalVector[Y] *= normalVectorLengthRecip;
		normalVector[Z] *= normalVectorLengthRecip;
		return normalVector;
	}


	/**
	 * Transforms the coordinate values of this quadrangle.
	 * 
	 * @param matrix The transformation matrix.
	 */
	@Override
	public void transform(double[][] matrix) {

		// Short aliases of the matrix and vertices arrays.
		double[][] m = matrix;
		double[][] sv = this.scaledVertexArray;
		double[][] tv = this.transformedVertexArray;

		// Transform each vertex, where X=0, Y=1, Z=2, and W=3.
		for (int ivertex=0; ivertex<this.vertexCount; ivertex++) {
			tv[ivertex][X] = m[0][0] * sv[ivertex][X] + m[0][1] * sv[ivertex][Y] + m[0][2] * sv[ivertex][Z] + m[0][3] * sv[ivertex][W];
			tv[ivertex][Y] = m[1][0] * sv[ivertex][X] + m[1][1] * sv[ivertex][Y] + m[1][2] * sv[ivertex][Z] + m[1][3] * sv[ivertex][W];
			tv[ivertex][Z] = m[2][0] * sv[ivertex][X] + m[2][1] * sv[ivertex][Y] + m[2][2] * sv[ivertex][Z] + m[2][3] * sv[ivertex][W];
		}

		// If this quadrangle faces the depth direction from the viewpoint, reverse its normal vector.
		// See also: the description of 'facesDepthDirection' method.
		if (this.facesDepthDirection(tv, this.normalVectorVertices)) {
			tv[4][X] = -tv[4][X];
			tv[4][Y] = -tv[4][Y];
			tv[4][Z] = -tv[4][Z];
		}

		// Compute the square of the 'depth' value.
		double meanZ = (tv[0][Z] + tv[1][Z] + tv[2][Z] + tv[3][Z]) * 0.25;
		this.depthSquaredValue = meanZ * meanZ;
	}


	/**
	 * Determines whether the normal vector of the specified quadrangle faces the depth direction,
	 * CONSIDERING THE PERSPECTIVE EFFECT.
	 * 
	 * In the simplest way, you can determine whether a quadrangle faces the depth direction, 
	 * by checking the sign of the Z component of the transformed normal vector (without perspective effect).
	 * 
	 * However, some error occures between directions of normal vectors with/without the perspective effect.
	 * Hence, when we are required to determine precisely whether the quadrangle faces to the depth direction 
	 * from the viewpoint, it is necessary to consider the perspective effect.
	 * This method is useful for such cases.
	 * 
	 * @param vertexArray The array storing the transformed coordinates of the vertices of the quadrangle.
	 * @param normalVertices Represents which vertices should be used for computing the normal vector.
	 * @return Returns true if the normal vector computed from the specified array 
	 *          (with considering the perspective effect) faces the depth direction.
	 */
	private boolean facesDepthDirection(double[][] vertexArray, NormalVectorVertices normalVertices) {
		double[][] v = vertexArray;

		if (normalVertices == NormalVectorVertices.ABC) {

			// Coefficients to apply the perspective effect to X and Y coordinate values of the vertices A, B, and C.
			double azRecip = 1.0 / -v[A][Z];
			double bzRecip = 1.0 / -v[B][Z];
			double czRecip = 1.0 / -v[C][Z];

			// Calculate the X and Y coordinate values of the above 'triangle side' vectors, with the perspective effect.
			double sideVectorPX = v[B][X] * bzRecip - v[A][X] * azRecip;
			double sideVectorPY = v[B][Y] * bzRecip - v[A][Y] * azRecip;
			double sideVectorQX = v[C][X] * czRecip - v[A][X] * azRecip;
			double sideVectorQY = v[C][Y] * czRecip - v[A][Y] * azRecip;

			// Calculate the cross product of the above 'triangle side' vectors, and determine the result from its sign.
			double crossProductZ = sideVectorPX * sideVectorQY - sideVectorPY * sideVectorQX;
			return (crossProductZ < 0);

		} else if (normalVertices == NormalVectorVertices.ACD) {

			// Coefficients to apply the perspective effect to X and Y coordinate values of the vertices A, C, and D.
			double azRecip = 1.0 / -v[A][Z];
			double czRecip = 1.0 / -v[C][Z];
			double dzRecip = 1.0 / -v[D][Z];

			// Calculate the X and Y coordinate values of the above 'triangle side' vectors, with the perspective effect.
			double sideVectorPX = v[C][X] * czRecip - v[A][X] * azRecip;
			double sideVectorPY = v[C][Y] * czRecip - v[A][Y] * azRecip;
			double sideVectorQX = v[D][X] * dzRecip - v[A][X] * azRecip;
			double sideVectorQY = v[D][Y] * dzRecip - v[A][Y] * azRecip;

			// Calculate the cross product of the above 'triangle side' vectors, and determine the result from its sign.
			double crossProductZ = sideVectorPX * sideVectorQY - sideVectorPY * sideVectorQX;
			return (crossProductZ < 0);

		} else {
			throw new RuntimeException("Unexpected normal vector vertices: " + normalVectorVertices);
		}
	}


	/**
	 * Shades the color.
	 * 
	 * @param lightConfig The object storing parameters for lighting and shading.
	 */
	@Override
	public void shade(LightConfiguration lightConfig) {

		// Prepare aliases of the normal vector,
		// and the direction vector pointing to the light source (hereinafter referred to as 'light vector').
		double[] normalVector = this.transformedVertexArray[4];
		double[] lightVector = {
				lightConfig.getLightSourceDirectionX(),
				lightConfig.getLightSourceDirectionY(),
				lightConfig.getLightSourceDirectionZ()
		};

		// Calculate the value of 'directional product',
		// which is the inner product between the normal vector and the light vector.
		double directionalProduct =
				normalVector[X] * lightVector[X] + 
				normalVector[Y] * lightVector[Y] + 
				normalVector[Z] * lightVector[Z];

		// Calculate the angle between the normal vector and the light vector,
		// and normalize it into the range [0.0, 1.0].
		double directionalAngle = Math.acos(directionalProduct);
		double normalizedDirectionalAngle = directionalAngle / Math.PI;

		// If the value of 'directional product' is negative, replace it by 0.
		double directionalProductPositive = (0 <= directionalProduct) ? directionalProduct : 0.0;

		// Calculate the brightness contributed by ambient, diffuse, and diffractive reflections.
		double baseBrightness = 
				lightConfig.getAmbientReflectionStrength() +
				lightConfig.getDiffuseReflectionStrength() * directionalProductPositive +
				lightConfig.getDiffractiveReflectionStrength() * normalizedDirectionalAngle;

		// Calculate the vector of the light reflected by specular reflection.
		double[] specularReflectionVector = {
				2.0 * directionalProduct * normalVector[X] - lightVector[X],
				2.0 * directionalProduct * normalVector[Y] - lightVector[Y],
				2.0 * directionalProduct * normalVector[Z] - lightVector[Z],
		};
		double specularReflectionVectorLength = Math.sqrt(
				specularReflectionVector[X] * specularReflectionVector[X] +
				specularReflectionVector[Y] * specularReflectionVector[Y] +
				specularReflectionVector[Z] * specularReflectionVector[Z]
		);
		
		// Calculate the angle between the above vector and the Z-axis (= direction of the user's gaze).
		double specularReflectionVectorAngle = Math.acos(specularReflectionVector[Z] / specularReflectionVectorLength);

		// Calculate the brightness contributed by specular reflection.
		double specularBrightness = 0.0;
		double specularReflectionSpreadAngle = lightConfig.getSpecularReflectionAngle();
		if (specularReflectionVectorAngle < specularReflectionSpreadAngle) {
			specularBrightness = lightConfig.getSpecularReflectionStrength() *
					Math.cos(0.5 * Math.PI * specularReflectionVectorAngle / specularReflectionSpreadAngle);
		}

		// Convert the RGBA components of the original color to double-type values, in range [0.0, 1.0].
		double recip255 = 1.0 / 255.0;
		double r = this.originalColor.getRed() * recip255;
		double g = this.originalColor.getGreen() * recip255;
		double b = this.originalColor.getBlue() * recip255;
		double a = this.originalColor.getAlpha() * recip255;

		// Blend the RGBA components based on the calculated brightnesses.
		r = r * baseBrightness * (1.0 - specularBrightness) + specularBrightness;
		g = g * baseBrightness * (1.0 - specularBrightness) + specularBrightness;
		b = b * baseBrightness * (1.0 - specularBrightness) + specularBrightness;
		a = a + specularBrightness;

		// Calculated RGBA components may exceed the range [0.0, 1.0], so crop values.
		r = Math.min(Math.max(r, 0.0), 1.0);
		g = Math.min(Math.max(g, 0.0), 1.0);
		b = Math.min(Math.max(b, 0.0), 1.0);
		a = Math.min(Math.max(a, 0.0), 1.0);
		this.onscreenColor = new Color((float)r, (float)g, (float)b, (float)a);
	}


	/**
	 * Computes the projected screen coordinate values of this quadrangle.
	 * 
	 * @param screenWidth The width (pixels) of the screen.
	 * @param screenHeight The height (pixels) of the screen.
	 * @param screenOffsetX The X-offset value (positive for shifting rightward) of the screen center.
	 * @param screenOffsetY The Y-offset value (positive for shifting upward) of the screen center.
	 * @param magnification The magnification of the conversion from lengths in 3D space to pixels.
	 */
	@Override
	public void project(int screenWidth, int screenHeight, int screenOffsetX, int screenOffsetY, double magnification) {

		// Compute the project coordinates on the screen.
		// (The origin is the left-top edge of the screen.)
		int screenCenterX = (screenWidth >> 1) + screenOffsetX; // bit-shifting instead of dividing by 2.
		int screenCenterY = (screenHeight >> 1) - screenOffsetY;

		// Short aliases of the vertices arrays.
		double[][] tv = this.transformedVertexArray;
		int[][] pv = this.projectedVertexArray;

		// Project each vertex.
		for (int ivertex=0; ivertex<this.vertexCount; ivertex++) {
			double projectionRatio = magnification / -tv[ivertex][Z]; // Z takes a negative value for the depth direction.
			pv[ivertex][X] = screenCenterX + (int)(tv[ivertex][X] * projectionRatio);
			pv[ivertex][Y] = screenCenterY - (int)(tv[ivertex][Y] * projectionRatio);
		}
	}


	/**
	 * Draws this quadrangle.
	 * 
	 * @param graphics The Graphics2D instance for drawing shapes to the screen image.
	 */
	@Override
	public void draw(Graphics2D graphics) {
		graphics.setColor(this.onscreenColor);

		int[][] pv = this.projectedVertexArray;
		int[] xArray = { pv[0][X], pv[1][X], pv[2][X], pv[3][X] };
		int[] yArray = { pv[0][Y], pv[1][Y], pv[2][Y], pv[3][Y] };
		graphics.fillPolygon(xArray, yArray, 4);
	}
}
