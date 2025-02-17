package com.rinearn.graph3d.config.frame;

import com.rinearn.graph3d.config.RinearnGraph3DConfigurationException;


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
	 * Creates new configuration storing default values.
	 */
	public FrameConfiguration() {
	}


	/** Stores the shape mode of the graph frame. */
	private volatile FrameShapeMode shapeMode = FrameShapeMode.BOX;

	/** Stores the direction mode of images displayed on the graph frame. */
	private volatile FrameImageDirectionMode imageDirectionMode = FrameImageDirectionMode.INSIDE;

	/** The width of the frame lines. */
	private volatile double lineWidth = 1.0;

	/** The configuration of the frame of X-axis. */
	private volatile AxisFrameConfiguration xFrameConfiguration = new AxisFrameConfiguration();

	/** The configuration of the frame of Y-axis. */
	private volatile AxisFrameConfiguration yFrameConfiguration = new AxisFrameConfiguration();

	/** The configuration of the frame of Z-axis. */
	private volatile AxisFrameConfiguration zFrameConfiguration = new AxisFrameConfiguration();

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
	public synchronized void setShapeMode(FrameShapeMode frameMode) {
		this.shapeMode = frameMode;
	}

	/**
	 * Gets the shape mode of the graph frame.
	 *
	 * @return The shape mode of the graph frame.
	 */
	public synchronized FrameShapeMode getShapeMode() {
		return this.shapeMode;
	}


	/**
	 * Sets the direction mode of the images displayed on the graph frame.
	 *
	 * @param shapeMode The direction mode of the images.
	 */
	public synchronized void setImageDirectionMode(FrameImageDirectionMode imageDirectionMode) {
		this.imageDirectionMode = imageDirectionMode;
	}

	/**
	 * Gets the direction mode of the images displayed on the graph frame.
	 *
	 * @return The direction mode of the images.
	 */
	public synchronized FrameImageDirectionMode getImageDirectionMode() {
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
	 * Sets the configuration of the frame of X axis.
	 *
	 * @param xFrameConfiguration The configuration of the frame of X axis.
	 */
	public synchronized void setXFrameConfiguration(AxisFrameConfiguration xFrameConfiguration) {
		this.xFrameConfiguration = xFrameConfiguration;
	}

	/**
	 * Gets the configuration of the frame of X axis.
	 *
	 * @return The configuration of the frame of X axis.
	 */
	public synchronized AxisFrameConfiguration getXFrameConfiguration() {
		return this.xFrameConfiguration;
	}


	/**
	 * Sets the configuration of the frame of Y axis.
	 *
	 * @param yFrameConfiguration The configuration of the frame of Y axis.
	 */
	public synchronized void setYFrameConfiguration(AxisFrameConfiguration yFrameConfiguration) {
		this.yFrameConfiguration = yFrameConfiguration;
	}

	/**
	 * Gets the configuration of the frame of Y axis.
	 *
	 * @return The configuration of the frame of Y axis.
	 */
	public synchronized AxisFrameConfiguration getYFrameConfiguration() {
		return this.yFrameConfiguration;
	}


	/**
	 * Sets the configuration of the frame of Z axis.
	 *
	 * @param zFrameConfiguration The configuration of the frame of Z axis.
	 */
	public synchronized void setZFrameConfiguration(AxisFrameConfiguration zFrameConfiguration) {
		this.zFrameConfiguration = zFrameConfiguration;
	}

	/**
	 * Gets the configuration of the frame of Z axis.
	 *
	 * @return The configuration of the frame of Z axis.
	 */
	public synchronized AxisFrameConfiguration getZFrameConfiguration() {
		return this.zFrameConfiguration;
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
	 * @throws RinearnGraph3DConfigurationException Thrown when incorrect or inconsistent settings are detected.
	 */
	public synchronized void validate() throws RinearnGraph3DConfigurationException {
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
}
