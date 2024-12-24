package com.rinearn.graph3d.model.io;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;


/**
 * The class that performs I/O operations on image files.
 */
public final class ImageFileIO {

	/**
	 * Saves the content of the specified Image instance as an image file.
	 *
	 * @param image The Image instance to save.
	 * @param imageFile The image file.
	 * @param quality The quality of the image file (from 0.0 to 1.0).
	 * @throws IOException Thrown when the image file format is unsupported, or any other I/O error occurred.
	 */
	public synchronized void saveImageFile(Image image, File imageFile, double quality) throws IOException {

		// Note:
		// Existence check of the image file should be performed by the called-side.
		// Don't do it here because the expected behavior differs for API/GUI.
		// (Especially, for the VCSSL API, the expected behavior depends on the VCSSL Runtime's settings.)

		// Convert the Image instance to a BufferedImage instance.
		// (Because ImageIO.write(...) is not available for Image instance. Available for BufferedImage.)
		BufferedImage bufferedImage;
		if (image instanceof BufferedImage) {
			bufferedImage = BufferedImage.class.cast(image);
		} else {

			// Get the size of the image.
			int width = image.getWidth(null);
			int height = image.getHeight(null);
			if (width == -1 || height == -1) {
				throw new IOException(
					"The image to be saved is not available. The image creation/loading process may be incomplete yet."
				);
			}

			// Copy the content of the image to the bufferedImage.
			bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D graphics = bufferedImage.createGraphics();
			graphics.drawImage(image, 0, 0, null);
			graphics.dispose();
		}

		// Check the range of the quality.
		if (quality < 0.0 || 1.0 < quality) {
			throw new IllegalArgumentException("The quality must be in the range of [0.0, 1.0].");
		}

		// Detect the image file format.
		String fileLowerName = imageFile.getName().toLowerCase();
		boolean isPNG = fileLowerName.endsWith(".png");
		boolean isBMP = fileLowerName.endsWith(".bmp");
		boolean isJPEG = fileLowerName.endsWith(".jpg") || fileLowerName.endsWith(".jpeg");

		// Save as a PNG file:
		if (isPNG) {
			ImageIO.write(bufferedImage, "png", imageFile);
			return;

		// Save as a BMP file:
		} else if (isBMP) {
			ImageIO.write(bufferedImage, "bmp", imageFile);
			return;

		// Save as a JPEG file:
		} else if (isJPEG) {
			IIOImage iioImage = new IIOImage(bufferedImage, null, null);

			// Get the image writer for JPEG.
			Iterator<ImageWriter> imageWriterIterator = ImageIO.getImageWritersByFormatName("jpg");
			ImageWriter imageWriter = imageWriterIterator.next();

			// Create the image I/O param, and set the compression ratio if it is available.
			ImageWriteParam imageWriterParam = imageWriter.getDefaultWriteParam();
			if (imageWriterParam.canWriteCompressed()){
				imageWriterParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				imageWriterParam.setCompressionQuality((float)quality);
			}

			// Save the JPEG file.
			try (ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(imageFile)) {
				imageWriter.setOutput(imageOutputStream);
				imageWriter.write(null, iioImage, imageWriterParam);

			// Catch and re-throw exceptions for closing resources.
			} catch (IOException ioe) {
				throw ioe;
			}

			// Dispose/release the resources.
			imageWriter.dispose();
			return;

		// Unsupported format:
		} else {
			throw new IOException("Unsupported image file format: " + imageFile.getPath());
		}
	}

}
