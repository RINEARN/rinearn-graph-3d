package com.rinearn.graph3d.renderer.simple;

import java.util.Comparator;

/**
 * The Comparator implementation for sorting GeometricPiece instances by their 'depth' values.
 */
public final class GeometricDepthComparator implements Comparator<GeometricPiece> {

	/**
	 * Returns 1 if piece1's depth is greater than piece2's depth, or -1 if smaller.
	 * Also, if both depths are completely the same, or either one is NaN, returns 0.
	 * 
	 * Note that, this comparator compares the square of the depths,
	 * on the premise that depths don't take negative values.
	 */
	@Override
	public final int compare(GeometricPiece piece1, GeometricPiece piece2) {
		double depthSquared1 = piece1.getDepthSquaredValue();
		double depthSquared2 = piece2.getDepthSquaredValue();

		if (depthSquared1 < depthSquared2) {
			return -1;
		} else if (depthSquared1 > depthSquared2) {
			return 1;
		} else {
			return 0;
		}
	}
}
