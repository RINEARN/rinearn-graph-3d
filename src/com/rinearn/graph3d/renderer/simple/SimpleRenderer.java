package com.rinearn.graph3d.renderer.simple;

import com.rinearn.graph3d.renderer.RinearnGraph3DDrawingParameter;
import com.rinearn.graph3d.renderer.RinearnGraph3DRenderer;

import java.awt.Color;
import java.awt.Font;


/**
 * The class providing a simple implementation of 
 * the rendering engine (renderer) of RINEARN Graph 3D.
 */
public class SimpleRenderer implements RinearnGraph3DRenderer {

	/**
	 * Creates a new renderer.
	 */
	public SimpleRenderer() {
	}


	@Override
	public void clear() {
		throw new RuntimeException("Unimplemented yet");
	}


	@Override
	public void render() {
		throw new RuntimeException("Unimplemented yet");
	}


	@Override
	public void drawPoint(double x, double y, double z, 
			double radius) {

		throw new RuntimeException("Unimplemented yet");
	}


	@Override
	public void drawPoint(double x, double y, double z, 
			double radius, Color color) {

		throw new RuntimeException("Unimplemented yet");
	}


	@Override
	public void drawPoint(double x, double y, double z, 
			double radius, RinearnGraph3DDrawingParameter parameter) {

		throw new RuntimeException("Unimplemented yet");
	}


	@Override
	public void drawLine(double aX, double aY, double aZ, 
			double bX, double bY, double bZ, 
			double width) {

		throw new RuntimeException("Unimplemented yet");
	}


	@Override
	public void drawLine(double aX, double aY, double aZ, 
			double bX, double bY, double bZ, 
			double width, Color color) {

		throw new RuntimeException("Unimplemented yet");
	}


	@Override
	public void drawLine(double aX, double aY, double aZ, 
			double bX, double bY, double bZ, 
			double width, RinearnGraph3DDrawingParameter parameter) {

		throw new RuntimeException("Unimplemented yet");
	}


	@Override
	public void drawTriangle(double aX, double aY, double aZ, 
			double bX, double bY, double bZ, 
			double cX, double cY, double cZ) {

		throw new RuntimeException("Unimplemented yet");
	}


	@Override
	public void drawTriangle(double aX, double aY, double aZ, 
			double bX, double bY, double bZ, 
			double cX, double cY, double cZ, 
			Color color) {

		throw new RuntimeException("Unimplemented yet");
	}


	@Override
	public void drawTriangle(double aX, double aY, double aZ, 
			double bX, double bY, double bZ, 
			double cX, double cY, double cZ, 
			RinearnGraph3DDrawingParameter parameter) {

		throw new RuntimeException("Unimplemented yet");
	}


	@Override
	public void drawQuadrangle(double aX, double aY, double aZ, 
			double bX, double bY, double bZ, 
			double cX, double cY, double cZ, 
			double dX, double dY, double dZ) {

		throw new RuntimeException("Unimplemented yet");
	}


	@Override
	public void drawQuadrangle(double aX, double aY, double aZ, 
			double bX, double bY, double bZ, 
			double cX, double cY, double cZ, 
			double dX, double dY, double dZ, 
			Color color) {

		throw new RuntimeException("Unimplemented yet");
	}


	@Override
	public void drawQuadrangle(double aX, double aY, double aZ, 
			double bX, double bY, double bZ, 
			double cX, double cY, double cZ, 
			double dX, double dY, double dZ, 
			RinearnGraph3DDrawingParameter parameter) {

		throw new RuntimeException("Unimplemented yet");
	}


	@Override
	public void drawText(double x, double y, double z, 
			String text, Font font, Color color) {

		throw new RuntimeException("Unimplemented yet");
	}


	@Override
	public void drawText(double x, double y, double z, 
			String text, Font font, RinearnGraph3DDrawingParameter parameter) {

		throw new RuntimeException("Unimplemented yet");
	}


	@Override
	public void drawFrame() {
		throw new RuntimeException("Unimplemented yet");
	}


	@Override
	public void drawScale() {
		throw new RuntimeException("Unimplemented yet");
	}


	@Override
	public void drawGrid() {
		throw new RuntimeException("Unimplemented yet");
	}


	@Override
	public void drawLabel() {
		throw new RuntimeException("Unimplemented yet");
	}
}
