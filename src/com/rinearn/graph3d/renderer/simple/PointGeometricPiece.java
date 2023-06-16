package com.rinearn.graph3d.renderer.simple;

import java.awt.Graphics2D;


/**
 * A geometric piece class representing a point.
 */
public class PointGeometricPiece extends GeometricPiece {

	/**
	 * Transforms the coordinate values of this point.
	 * 
	 * @param matrix The transformation matrix.
	 */
	@Override
	public void transform(double[][] matrix) {
		throw new RuntimeException("Unimplemented");
	}


	/**
	 * Shades the color.
	 */
	@Override
	public void shade() {
		throw new RuntimeException("Unimplemented");
	}


	/**
	 * Draws this point.
	 * 
	 * @param graphics The Graphics2D instance for drawing shapes to the screen image.
	 */
	@Override
	public void draw(Graphics2D graphics) {
		throw new RuntimeException("Unimplemented");
	}
}
