package com.rinearn.graph3d.config;

import java.awt.Font;


/**
 * The class for storing configuration of fonts.
 */
public class FontConfiguration {

	/** The plain font for GUI components. */
	private volatile Font uiPlainFont = new Font("Dialog", Font.PLAIN, 16);

	/** The bold font for GUI components. */
	private volatile Font uiBoldFont = new Font("Dialog", Font.BOLD, 16);

	/** The font for rendering axis labels. */
	private volatile Font axisLabelFont = new Font("Dialog", Font.PLAIN, 30);

	/** The font for rendering tick labels. */
	private volatile Font tickLabelFont = new Font("Dialog", Font.PLAIN, 20);

	// To be added: markerFont


	/**
	 * Sets the plain font for GUI components.
	 * 
	 * @param uiPlainFont The plain font for GUI components;
	 */
	public synchronized void setUIPlainFont(Font uiPlainFont) {
		this.uiPlainFont = uiPlainFont;
	}

	/**
	 * Gets the plain font for GUI components.
	 * 
	 * @return The plain font for GUI components.
	 */
	public synchronized Font getUIPlainFont() {
		return this.uiPlainFont;
	}


	/**
	 * Sets the bold font for GUI components.
	 * 
	 * @param uiPlainFont The bold font for GUI components;
	 */
	public synchronized void setUIBoldFont(Font uiBoldFont) {
		this.uiBoldFont = uiBoldFont;
	}

	/**
	 * Gets the bold font for GUI components.
	 * 
	 * @return The bold font for GUI components.
	 */
	public synchronized Font getUIBoldFont() {
		return this.uiBoldFont;
	}


	/**
	 * Sets the font for rendering axis labels.
	 * 
	 * @param axisLabelFont The font for rendering axis labels.
	 */
	public synchronized void setAxisLabelFont(Font axisLabelFont) {
		this.axisLabelFont = axisLabelFont;
	}

	/**
	 * Gets the font for rendering axis labels.
	 * 
	 * @return The font for rendering axis labels.
	 */
	public synchronized Font getAxisLabelFont() {
		return this.axisLabelFont;
	}


	/**
	 * Sets the font for rendering tick labels.
	 * 
	 * @param tickLabelFont The font for rendering tick labels.
	 */
	public synchronized void setTickLabelFont(Font tickLabelFont) {
		this.tickLabelFont = tickLabelFont;
	}

	/**
	 * Gets the font for rendering tick labels.
	 * 
	 * @return The font for rendering tick labels.
	 */
	public synchronized Font getTickLabelFont() {
		return this.tickLabelFont;
	}


	/**
	 * Validates correctness and consistency of configuration parameters stored in this instance.
	 * 
	 * This method is called when this configuration is specified to RinearnGraph3D or its renderer.
	 * If no issue is detected, nothing occurs.
	 * If any issue is detected, throws IllegalStateException.
	 * 
	 * @throws IllegalStateException Thrown when incorrect or inconsistent settings are detected.
	 */
	public synchronized void validate() throws IllegalStateException {
		if (this.axisLabelFont == null) {
			throw new IllegalStateException("The axis label font is null.");
		}
		if (this.tickLabelFont == null) {
			throw new IllegalStateException("The tick label font is null.");
		}
	}

}
