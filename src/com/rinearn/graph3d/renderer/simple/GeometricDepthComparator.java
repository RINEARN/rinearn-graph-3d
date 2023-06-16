package com.rinearn.graph3d.renderer.simple;

import java.util.Comparator;

/**
 * The Comparator implementation for sorting GeometricPiece instances by their 'depth' values.
 */
public final class GeometricDepthComparator implements Comparator<GeometricPiece> {

	/**
	 * Returns 1 if piece1's depth is greater than piece2's depth, or -1 if smaller.
	 * Also, if both depths are completely the same, or either one is NaN, returns 0.
	 */
	@Override
	public final int compare(GeometricPiece piece1, GeometricPiece piece2) {
		double depth1 = piece1.getDepth();
		double depth2 = piece2.getDepth();

		if (depth1 < depth2) {
			return -1;
		} else if (depth1 > depth2) {
			return 1;
		} else {
			return 0;
		}
	}
}
