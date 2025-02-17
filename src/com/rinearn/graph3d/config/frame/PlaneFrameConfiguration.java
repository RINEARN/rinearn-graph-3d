package com.rinearn.graph3d.config.frame;

import com.rinearn.graph3d.config.RinearnGraph3DConfigurationException;

import java.awt.Image;
import java.io.File;


/**
 * The class storing configuration values of a plane of the graph frame.
 */
public class PlaneFrameConfiguration {

	/** The image displaying mode on this plane's frame. */
	private FrameImageMode imageMode = FrameImageMode.NONE;

	/** The file from which the image to be displayed on this plane's frame is loaded. */
	private volatile File imageFile = null; // The loaded image will be drawn to the buffer in Renderer, so no Image instance here.

	// private volatile int subplotIndex; // Maybe supported in future. Probably we should have subplot index here, not Image instance.

	/** The custom image to be displayed on this plane's frame. */
	private volatile Image customImage = null;

	/** The width (pixel count) of the rasterized image rendered on the plane's frame. */
	private volatile int imageResolutionConversionWidth = 140;
	// private volatile int imageRasterizationWidth;

	/** The height (pixel count) of the rasterized image rendered on the plane's frame. */
	private volatile int imageResolutionConversionHeight = 140;
	// private volatile int imageRasterizationHeight;


	/**
	 * Sets the image displaying mode on this plane's frame.
	 *
	 * @param imageMode The image displaying mode on this plane's frame.
	 */
	public synchronized void setImageMode(FrameImageMode imageMode) {
		this.imageMode = imageMode;
	}

	/**
	 * Gets the image displaying mode on this plane's frame.
	 *
	 * @return The image displaying mode on this plane's frame.
	 */
	public synchronized FrameImageMode getImageMode() {
		return this.imageMode;
	}


	/**
	 * Sets the file from which the image to be displayed on this plane's frame is loaded, in FILE mode.
	 *
	 * @param imageFile The file of the image to be displayed on this plane's frame.
	 */
	public synchronized void setImageFile(File imageFile) {
		this.imageFile = imageFile;
	}

	/**
	 * Gets the file from which the image to be displayed on this plane's frame is loaded in FILE mode.
	 *
	 * @return The file of the image to be displayed on this plane's frame.
	 */
	public synchronized File getImageFile() {
		return this.imageFile;
	}


	/**
	 * Sets the image to be displayed on this plane's frame, in CUSTOM mode.
	 *
	 * @param customImage The image to be displayed on this plane's frame.
	 */
	public synchronized void setCustomImage(Image customImage) {
		this.customImage = customImage;
	}

	/**
	 * Gets the image to be displayed on this plane's frame, in CUSTOM mode.
	 *
	 * @return The image to be displayed on this plane's frame.
	 */
	public synchronized Image getCustomImage() {
		return this.customImage;
	}


	/**
	 * Sets the with (pixel count) of the re-rasterized image rendered on the plane's frame.
	 *
	 * @param width The width (pixel count) of the re-rasterized image rendered on the plane's frame.
	 */
	public synchronized void setImageResolutionConversionWidth(int width) {
		this.imageResolutionConversionWidth = width;
	}

	/**
	 * Gets the with (pixel count) of the re-rasterized image rendered on the plane's frame.
	 *
	 * @return The width (pixel count) of the re-rasterized image rendered on the plane's frame.
	 */
	public synchronized int getImageResolutionConversionWidth() {
		return this.imageResolutionConversionWidth;
	}


	/**
	 * Sets the height (pixel count) of the re-rasterized image rendered on the plane's frame.
	 *
	 * @param width The height (pixel count) of the re-rasterized image rendered on the plane's frame.
	 */
	public synchronized void setImageResolutionConversionHeight(int height) {
		this.imageResolutionConversionHeight = height;
	}

	/**
	 * Gets the height (pixel count) of the re-rasterized image rendered on the plane's frame.
	 *
	 * @return The height (pixel count) of the re-rasterized image rendered on the plane's frame.
	 */
	public synchronized int getImageResolutionConversionHeight() {
		return this.imageResolutionConversionHeight;
	}


	/**
	 * Validates correctness and consistency of configuration parameters stored in this instance.
	 *
	 * This method is called when this configuration is specified to RinearnGraph3D or its renderer.
	 * If no issue is detected, nothing occurs.
	 * If any issue is detected, throws IllegalStateException.
	 *
	 * @throws RinearnGraph3DConfigurationException Thrown when incorrect or inconsistent settings are detected.
	 */
	public synchronized void validate() throws RinearnGraph3DConfigurationException {

		// Check the image file/instance depending the specified image mode.
		if (this.imageMode == FrameImageMode.FILE) {
			if (this.imageFile == null) {
				throw new RinearnGraph3DConfigurationException("The image mode is set to FILE but the image file has not been set.");
			}
		} else if (this.imageMode == FrameImageMode.CUSTOM) {
			if (this.imageFile == null) {
				throw new RinearnGraph3DConfigurationException("The image mode is set to CUSTOM but the custom Image instance has not been set.");
			}
		}

		// Check the resolution conversion size.
		if (this.imageResolutionConversionWidth < 0) {
			throw new RinearnGraph3DConfigurationException("The width of the resolution-conversion must be a positive value, excluding zero.");
		}
		if (this.imageResolutionConversionHeight < 0) {
			throw new RinearnGraph3DConfigurationException("The height of the resolution-conversion must be a positive value, excluding zero.");
		}
		if (10000 < this.imageResolutionConversionWidth) {
			throw new RinearnGraph3DConfigurationException("The width of the resolution-conversion is too large (must be <= 10000).");
		}
		if (10000 < this.imageResolutionConversionHeight) {
			throw new RinearnGraph3DConfigurationException("The height of the resolution-conversion is too large (must be <= 10000).");
		}
	}
}
