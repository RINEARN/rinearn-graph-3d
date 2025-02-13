package com.rinearn.graph3d.config.frame;

import java.io.File;
import java.awt.Image;


// !!!!! NOTE !!!!!
// この設定は ScaleConfig にマージすべきか？ あえて分けるほどの規模でもないし、2Dだとどう分けるのって問題になるし。
// 実質的にこれのメインは底面の画像設定くらいな気がする。
//
// -> それじゃあ、画像設定を入れてみて、規模感が小さかったらマージを検討、結構大きかったらそのまま現状で、って感じで。
//    後でまた要検討
//
// -> 実装直後の判断、とりあえずやっぱり現状維持が良さそうかな。理由は以下：
//
//    * XY面とかのサブ要素コンテナクラス、命名的に Scale じゃ微妙で Frame の方がしっくりくる
//    * 将来的に、床面に等高線とかヒートマップとか動的描画内容とかを描きたい場合が生じた際、それらはもはや全く Scale ではないので命名が詰む。

/**
 * The class for storing configuration parameters of the graph frame.
 */
public final class FrameConfiguration {

	/**
	 * The enum representing each shape mode of the graph frame.
	 */
	public static enum ShapeMode {

		/** Represents the mode drawing no frame. */
		NONE,

		/** Represents the mode drawing a standard box-type frame. */
		BOX,

		/** Represents the mode drawing only backwalls (inside) of a box-type frame. */
		BACKWALL,

		/** Represents the mode drawing only the floor. */
		FLOOR;
	}

	/**
	 * The enum representing each direction mode of images displayed on the graph frame.
	 */
	public static enum ImageDirectionMode {

		/** Representing the mode drawing images only on the inside of the graph frame. */
		INSIDE,

		/** Representing the mode drawing images only on the outside of the graph frame. */
		OUTSIDE,

		/** Representing the mode drawing images on both the inside and outside of the graph frame. */
		BOTH;
	}


	/**
	 * Creates new configuration storing default values.
	 */
	public FrameConfiguration() {
	}


	/** Stores the shape mode of the graph frame. */
	private volatile ShapeMode shapeMode = ShapeMode.BOX;

	/** Stores the direction mode of images displayed on the graph frame. */
	private volatile ImageDirectionMode imageDirectionMode = ImageDirectionMode.INSIDE;

	/** The width of the frame lines. */
	private volatile double lineWidth = 1.0;

	/** The configuration of the lower (Z-min) side X-Y plane's frame. */
	private volatile PlaneFrameConfiguration xyLowerFrameConfiguration = new PlaneFrameConfiguration();

	/** The configuration of the upper (Z-max) side X-Y plane's frame. */
	private volatile PlaneFrameConfiguration xyUpperFrameConfiguration = new PlaneFrameConfiguration();

	/** The configuration of the lower (X-min) side Y-Z plane's frame. */
	private volatile PlaneFrameConfiguration yzLowerFrameConfiguration = new PlaneFrameConfiguration();

	/** The configuration of the upper (X-max) side Y-Z plane's frame. */
	private volatile PlaneFrameConfiguration yzUpperFrameConfiguration = new PlaneFrameConfiguration();

	/** The configuration of the lower (Y-min) side Z-X plane's frame. */
	private volatile PlaneFrameConfiguration zxLowerFrameConfiguration = new PlaneFrameConfiguration();

	/** The configuration of the upper (Y-max) side Z-X plane's frame. */
	private volatile PlaneFrameConfiguration zxUpperFrameConfiguration = new PlaneFrameConfiguration();


	/**
	 * Sets the shape mode of the graph frame.
	 *
	 * @param shapeMode The shape mode of the graph frame.
	 */
	public synchronized void setShapeMode(ShapeMode frameMode) {
		this.shapeMode = frameMode;
	}

	/**
	 * Gets the shape mode of the graph frame.
	 *
	 * @return The shape mode of the graph frame.
	 */
	public synchronized ShapeMode getShapeMode() {
		return this.shapeMode;
	}


	/**
	 * Sets the direction mode of the images displayed on the graph frame.
	 *
	 * @param shapeMode The direction mode of the images.
	 */
	public synchronized void setImageDirectionMode(ImageDirectionMode imageDirectionMode) {
		this.imageDirectionMode = imageDirectionMode;
	}

	/**
	 * Gets the direction mode of the images displayed on the graph frame.
	 *
	 * @return The direction mode of the images.
	 */
	public synchronized ImageDirectionMode getImageDirectionMode() {
		return this.imageDirectionMode;
	}


	/**
	 * Sets the width of the frame lines.
	 *
	 * @param lineWidth The width of the frame lines.
	 */
	public synchronized void setLineWidth(double lineWidth) {
		this.lineWidth = lineWidth;
	}

	/**
	 * Gets the width of the frame lines.
	 *
	 * @return The width of the frame lines.
	 */
	public synchronized double getLineWidth() {
		return this.lineWidth;
	}


	/**
	 * Sets the configuration of the lower (Z-min) side X-Y plane's frame.
	 *
	 * @param xyLowerFrameConfiguration The configuration of the lower (Z-min) side X-Y plane's frame.
	 */
	public synchronized void setXyLowerFrameConfiguration(PlaneFrameConfiguration xyLowerFrameConfiguration) {
		this.xyLowerFrameConfiguration = xyLowerFrameConfiguration;
	}

	/**
	 * Gets the configuration of the lower (Z-min) side X-Y plane's frame.
	 *
	 * @return The configuration of the lower (Z-min) side X-Y plane's frame.
	 */
	public synchronized PlaneFrameConfiguration getXyLowerFrameConfiguration() {
		return this.xyLowerFrameConfiguration;
	}


	/**
	 * Sets the configuration of the upper (Z-min) side X-Y plane's frame.
	 *
	 * @param xyUpperFrameConfiguration The configuration of the upper (Z-min) side X-Y plane's frame.
	 */
	public synchronized void setXyUpperFrameConfiguration(PlaneFrameConfiguration xyUpperFrameConfiguration) {
		this.xyUpperFrameConfiguration = xyUpperFrameConfiguration;
	}

	/**
	 * Gets the configuration of the upper (Z-max) side X-Y plane's frame.
	 *
	 * @return The configuration of the upper (Z-max) side X-Y plane's frame.
	 */
	public synchronized PlaneFrameConfiguration getXyUpperFrameConfiguration() {
		return this.xyUpperFrameConfiguration;
	}


	/**
	 * Sets the configuration of the lower (X-min) side Y-Z plane's frame.
	 *
	 * @param yzLowerFrameConfiguration The configuration of the lower (X-min) side Y-Z plane's frame.
	 */
	public synchronized void setYzLowerFrameConfiguration(PlaneFrameConfiguration yzLowerFrameConfiguration) {
		this.yzLowerFrameConfiguration = yzLowerFrameConfiguration;
	}

	/**
	 * Gets the configuration of the lower (X-min) side Y-Z plane's frame.
	 *
	 * @return The configuration of the lower (X-min) side Y-Z plane's frame.
	 */
	public synchronized PlaneFrameConfiguration getYzLowerFrameConfiguration() {
		return this.yzLowerFrameConfiguration;
	}


	/**
	 * Sets the configuration of the upper (X-min) side Y-Z plane's frame.
	 *
	 * @param yzUpperFrameConfiguration The configuration of the upper (X-min) side Y-Z plane's frame.
	 */
	public synchronized void setYzUpperFrameConfiguration(PlaneFrameConfiguration yzUpperFrameConfiguration) {
		this.yzUpperFrameConfiguration = yzUpperFrameConfiguration;
	}

	/**
	 * Gets the configuration of the upper (X-max) side Y-Z plane's frame.
	 *
	 * @return The configuration of the upper (X-max) side Y-Z plane's frame.
	 */
	public synchronized PlaneFrameConfiguration getYzUpperFrameConfiguration() {
		return this.yzUpperFrameConfiguration;
	}


	/**
	 * Sets the configuration of the lower (Y-min) side Z-X plane's frame.
	 *
	 * @param zxLowerFrameConfiguration The configuration of the lower (Y-min) side Z-X plane's frame.
	 */
	public synchronized void setZxLowerFrameConfiguration(PlaneFrameConfiguration zxLowerFrameConfiguration) {
		this.zxLowerFrameConfiguration = zxLowerFrameConfiguration;
	}

	/**
	 * Gets the configuration of the lower (Y-min) side Z-X plane's frame.
	 *
	 * @return The configuration of the lower (Y-min) side Z-X plane's frame.
	 */
	public synchronized PlaneFrameConfiguration getZxLowerFrameConfiguration() {
		return this.zxLowerFrameConfiguration;
	}


	/**
	 * Sets the configuration of the upper (Y-min) side Z-X plane's frame.
	 *
	 * @param zxUpperFrameConfiguration The configuration of the upper (Y-min) side Z-X plane's frame.
	 */
	public synchronized void setZxUpperFrameConfiguration(PlaneFrameConfiguration zxUpperFrameConfiguration) {
		this.zxUpperFrameConfiguration = zxUpperFrameConfiguration;
	}

	/**
	 * Gets the configuration of the upper (Y-max) side Z-X plane's frame.
	 *
	 * @return The configuration of the upper (Y-max) side Z-X plane's frame.
	 */
	public synchronized PlaneFrameConfiguration getZxUpperFrameConfiguration() {
		return this.zxUpperFrameConfiguration;
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
		if (this.lineWidth < 0.0) {
			throw new IllegalStateException("The width of the frame lines must be a positive value.");
		}

		// Validate each plane's configurations.
		this.xyLowerFrameConfiguration.validate();
		this.xyUpperFrameConfiguration.validate();
		this.yzLowerFrameConfiguration.validate();
		this.yzUpperFrameConfiguration.validate();
		this.zxLowerFrameConfiguration.validate();
		this.zxUpperFrameConfiguration.validate();
	}


	/**
	 * The enum representing each image displaying mode on the frame of a plane.
	 */
	public enum ImageMode {

		/** Represents the mode displaying no image. */
		NONE,

		/** Represents the mode displaying the image loaded from a file. */
		FILE,

		// SUBPLOT, // Maybe supported in future.

		/** Represents the mode displaying the dynamically generated image passed through API, etc. */
		CUSTOM,
	}


	/**
	 * The class storing configuration values of a plane.
	 */
	public class PlaneFrameConfiguration {

		/** The image displaying mode on this plane's frame. */
		private ImageMode imageMode = ImageMode.NONE;

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
		public synchronized void setImageMode(ImageMode imageMode) {
			this.imageMode = imageMode;
		}

		/**
		 * Gets the image displaying mode on this plane's frame.
		 *
		 * @return The image displaying mode on this plane's frame.
		 */
		public synchronized ImageMode getImageMode() {
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
		 * @throws IllegalStateException Thrown when incorrect or inconsistent settings are detected.
		 */
		public synchronized void validate() throws IllegalStateException {

			// Check the image file/instance depending the specified image mode.
			if (this.imageMode == ImageMode.FILE) {
				if (this.imageFile == null) {
					throw new IllegalStateException("The image mode is set to FILE but the image file has not been set.");
				}
			} else if (this.imageMode == ImageMode.CUSTOM) {
				if (this.imageFile == null) {
					throw new IllegalStateException("The image mode is set to CUSTOM but the custom Image instance has not been set.");
				}
			}

			// Check the resolution conversion size.
			if (this.imageResolutionConversionWidth < 0) {
				throw new IllegalStateException("The width of the resolution-conversion must be a positive value, excluding zero.");
			}
			if (this.imageResolutionConversionHeight < 0) {
				throw new IllegalStateException("The height of the resolution-conversion must be a positive value, excluding zero.");
			}
			if (10000 < this.imageResolutionConversionWidth) {
				throw new IllegalStateException("The width of the resolution-conversion is too large (must be <= 10000).");
			}
			if (10000 < this.imageResolutionConversionHeight) {
				throw new IllegalStateException("The height of the resolution-conversion is too large (must be <= 10000).");
			}
		}
	}

}
