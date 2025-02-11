package com.rinearn.graph3d.config;

// !!! NOTE !!!
//
// なんかこれ書いてて、あちこちの set...Labels 的なやつ（setTickLabelsとか）の命名を
// set...LabelTexts とかに直したほうがいいような気がしてきたけど、
// 一方で直さない方が良い気もする。
//
// ・直した方がいいと思う理由は、Label そのものには margin とかの要素もイメージ上の階層構造内に含んでて、
// 　なので実際 set...LabelMargin とかが存在するので、
// 　ラベルの表示文字列を設定するのは Text を付けないと曖昧な気がしてきたため。
//
// 　実際、このクラス内の AxisLabelConfiguration みたいにイメージ上の構造をクラスの構造に射影した際に、
// 　ラベルの値の setter の命名とかで結局 setText とかになるので、やっぱそうだよなあ、という感じがして。
//
// ・そのままでいいと思う理由は、例えば普通に「 setXLabel 」とか見るとラベル文字列をセットする事は十分に明快で、
// 　RinearnGraph3D のAPI上でも既にそうなってるし、たぶんそれで曖昧さを感じて混乱する事は実際には無さそうなので。
//
// 　とすると Config 類のラベル的なもののあちこちに Text が付くのは、イメージ上の階層構造では綺麗になるが、
// 　そのために実用上は（微妙に面倒くさいという）不利益を生み出すだけになるのって方針としてどうなの？という。
//
//  -> 以上の議論から一時期は AxisLabelConfiguration.setText 等にしてたが、結局なんのテキストなのか微妙さが残るので
//     冗長さを承知で setLabelText にした。これでまあ確実に伝わるのは伝わるし、別のクラスとの命名ブレもなくなる。
//
// また後々で要検討
//
//!!! NOTE !!!


/**
 * The class storing configuration values of labels.
 */
public final class LabelConfiguration {

	/** The configuration of the X axis label. */
	public volatile AxisLabelConfiguration xLabelConfiguration = new AxisLabelConfiguration();

	/** The configuration of the Y axis label. */
	public volatile AxisLabelConfiguration yLabelConfiguration = new AxisLabelConfiguration();;

	/** The configuration of the Z axis label. */
	public volatile AxisLabelConfiguration zLabelConfiguration = new AxisLabelConfiguration();;


	/**
	 * Creates a new configuration storing default values.
	 */
	public LabelConfiguration() {
		this.xLabelConfiguration.setLabelText("X");
		this.yLabelConfiguration.setLabelText("Y");
		this.zLabelConfiguration.setLabelText("Z");
	}


	/**
	 * Set the configuration of X axis label.
	 *
	 * @param xLabelConfiguration The configuration of X axis label.
	 */
	public synchronized void setXLabelConfiguration(AxisLabelConfiguration xLabelConfiguration) {
		this.xLabelConfiguration = xLabelConfiguration;
	}

	/**
	 * Get the configuration of X axis label.
	 *
	 * @return The configuration of X axis label.
	 */
	public synchronized AxisLabelConfiguration getXLabelConfiguration() {
		return this.xLabelConfiguration;
	}


	/**
	 * Set the configuration of Y axis label.
	 *
	 * @param yLabelConfiguration The configuration of Y axis label.
	 */
	public synchronized void setYLabelConfiguration(AxisLabelConfiguration yLabelConfiguration) {
		this.yLabelConfiguration = yLabelConfiguration;
	}

	/**
	 * Get the configuration of Y axis label.
	 *
	 * @return The configuration of Y axis label.
	 */
	public synchronized AxisLabelConfiguration getYLabelConfiguration() {
		return this.yLabelConfiguration;
	}


	/**
	 * Set the configuration of Z axis label.
	 *
	 * @param zLabelConfiguration The configuration of Z axis label.
	 */
	public synchronized void setZLabelConfiguration(AxisLabelConfiguration zLabelConfiguration) {
		this.zLabelConfiguration = zLabelConfiguration;
	}

	/**
	 * Get the configuration of Z axis label.
	 *
	 * @return The configuration of Z axis label.
	 */
	public synchronized AxisLabelConfiguration getZLabelConfiguration() {
		return this.zLabelConfiguration;
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

		// Validate configs of X/Y/Z axes.
		this.xLabelConfiguration.validate();
		this.yLabelConfiguration.validate();
		this.zLabelConfiguration.validate();
	}


	/**
	 * The class storing configuration values of the label of an axis(X, Y, or Z).
	 */
	public final class AxisLabelConfiguration {

		// To be added: margin, alignment, etc.

		/** The flag representing whether this axis's label is visible */
		private volatile boolean visible = true;

		/** The displayed text of this label. */
		private volatile String labelText = "";

		/**
		 * Sets whether this axis's label is visible.
		 *
		 * @return Specify true/false to show/hide this axis's label.
		 */
		public synchronized void setVisible(boolean visible) {
			this.visible = visible;
		}

		/**
		 * Gets whether this axis's label is visible.
		 *
		 * @return Returns true if this axis's label is visible.
		 */
		public synchronized boolean isVisible() {
			return this.visible;
		}

		/**
		 * Sets the displayed text of this label.
		 *
		 * @param text The displayed text of this label.
		 */
		public synchronized void setLabelText(String text) {
			this.labelText = text;
		}

		/**
		 * Gets the displayed text of this label.
		 *
		 * @return The displayed text of this label.
		 */
		public synchronized String getLabelText() {
			return this.labelText;
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
			if (this.labelText == null) {
				throw new IllegalStateException("The label text is null.");
			}
		}
	}


	/**
	 * The class storing configuration values of the legend labels.
	 */
	public final class LegendLabelConfiguration {

		/** The flag representing whether the legends display is visible. */
		private volatile boolean visible = true;

		/** The flag representing whether the auto-legend-generation feature is enabled. */
		private volatile boolean autoLegendGenerationEnabled = true;

		/** The texts of the legend labels. */
		private volatile String[] labelTexts = { };

		/**
		 * Sets whether the legends display is visible.
		 *
		 * @return Specify true/false to show/hide the legends display.
		 */
		public synchronized void setVisible(boolean visible) {
			this.visible = visible;
		}

		/**
		 * Gets whether the legends display is visible.
		 *
		 * @return Returns true if the legend display is visible.
		 */
		public synchronized boolean isVisible() {
			return this.visible;
		}

		/**
		 * Enables/disables the auto-legend-generation feature.
		 *
		 * @return Specify true to enable the auto-legend-generation feature.
		 */
		public synchronized void setAutoLegendGenerationEnabled(boolean autoLegendGenerationEnabled) {
			this.autoLegendGenerationEnabled = autoLegendGenerationEnabled;
		}

		/**
		 * Gets whether the auto-legend-generation feature is enabled.
		 *
		 * @return Returns true if the auto-legend-generation feature is enabled.
		 */
		public synchronized boolean isAutoLegendGenerationEnabled() {
			return this.autoLegendGenerationEnabled;
		}

		/**
		 * Sets the texts of the legend labels.
		 *
		 * @param texts The texts of the legend labels.
		 */
		public synchronized void setLabelTexts(String[] texts) {
			this.labelTexts = texts;
		}

		/**
		 * Gets the texts of the legend labels.
		 *
		 * @return The texts of the legend labels.
		 */
		public synchronized String[] getLabelTexts() {
			return this.labelTexts;
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
			if (this.labelTexts == null) {
				throw new IllegalStateException("The legend label texts are null.");
			}
			for (String text: this.labelTexts) {
				if (text == null) {
					throw new IllegalStateException("The legend label texts contains null as an element.");
				}
			}
		}
	}
}
