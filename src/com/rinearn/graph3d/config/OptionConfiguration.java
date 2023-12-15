package com.rinearn.graph3d.config;


/*
[!!!!! NOTE !!!!!]

既に5.6から setOptionSelected(...)/setOptionParatmeter(...) API用に
RinearnGraph3DOptionItem/OptionParameter というenum/コンテナオブジェクトがあるけど、
この設定コンテナ内でも上記を使うべき？ それとも上記とは独立に実装する？

・前者の場合、上記のやつはオプション管理の基盤になって、この設定コンテナはそれを config 層用に包む感じになる。

・後者の場合、こっちの設定コンテナが基盤になって、上記のやつはその操作を簡易用途向けにラッピングするやつみたいな感じになる。

-> 後者の方が綺麗そうだけど、しかしせっかくオプション項目の enum が存在してるのに使わないっていう微妙な無駄さは生じる。

   -> でも、前者の方針で RinearnGraph3DOptionParameter を肥大化させてく方向は色々エグい気がする。
      もともと RinearnGraph3DOptionParameter って、
     「よく使う重要なやつだけAPIから直で設定可能にして、それでカバーしないような細かい設定値は設定ファイルを作って読ませる」
      っていう方針だったと思うし。
      各オプションの細かい設定を網羅的に格納するならもっと階層的な構造にしてたはずで、でも単純さ優先で表層に主要項目をベタ置きにした。

      一方、設定ファイルと同じ粒度で設定をカバーするのが Ver.6 の config 層なので、この設定コンテナは非常に細かい粒度でカバーすべき。
      そうすると必然的に RinearnGraph3DOptionParameter とは構造が色々と違ってくるのでは。

とりあえず後者の方針で実装、ほどほどに出来たタイミングで複雑さ等を振り返ってまた再検討する
*/


/**
 * The class storing configuration values of plotting options.
 */
public final class OptionConfiguration {

	/**
	 * Creates a new configuration storing default values.
	 */
	public OptionConfiguration() {
	}


	/** Stores the configuration of "With Points" option. */
	private volatile PointOptionConfiguration pointOptionConfiguration = new PointOptionConfiguration();

	/**
	 * Sets the configuration of "With Points" option.
	 */
	public synchronized void getPointOptionConfiguration(PointOptionConfiguration pointOptionConfiguration) {
		this.pointOptionConfiguration = pointOptionConfiguration;
	}

	/**
	 * Gets the configuration of "With Points" option.
	 */
	public synchronized PointOptionConfiguration getPointOptionConfiguration() {
		return this.pointOptionConfiguration;
	}

	/**
	 * The class storing configuration values of "With Points" option.
	 */
	public static final class PointOptionConfiguration {

		/** The flag representing whether this option is selected. */
		private volatile boolean selected = true;

		/** The radius (in pixels) of points plotted by this option. */
		private volatile double pointRadius = 2.0;

		/**
		 * Selects or unselects this option. 
		 * 
		 * @param selected Specify true to select, false to unselect.
		 */
		public synchronized void setSelected(boolean selected) {
			this.selected = selected;
		}

		/**
		 * Checks whether this option is selected.
		 * 
		 * @return Returns true if this option is selected.
		 */
		public synchronized boolean isSelected() {
			return this.selected;
		}

		/**
		 * Sets the radius (in pixels) of points plotted by this option.
		 * 
		 * @param pointRadius The radius (in pixels) of points plotted by this option.
		 */
		public synchronized void setPointRadius(double pointRadius) {
			this.pointRadius = pointRadius;
		}

		/**
		 * Gets the radius (in pixels) of points plotted by this option.
		 * 
		 * @return The radius (in pixels) of points plotted by this option.
		 */
		public synchronized double getPointRadius() {
			return this.pointRadius;
		}
	}


	/** Stores the configuration of "With Lines" option. */
	private volatile LineOptionConfiguration lineOptionConfiguration = new LineOptionConfiguration();

	/**
	 * Sets the configuration of "With Lines" option.
	 */
	public synchronized void getLineOptionConfiguration(LineOptionConfiguration lineOptionConfiguration) {
		this.lineOptionConfiguration = lineOptionConfiguration;
	}

	/**
	 * Gets the configuration of "With Lines" option.
	 */
	public synchronized LineOptionConfiguration getLineOptionConfiguration() {
		return this.lineOptionConfiguration;
	}

	/**
	 * The class storing configuration values of "With Lines" option.
	 */
	public static final class LineOptionConfiguration {

		/** The flag representing whether this option is selected. */
		private volatile boolean selected = false;

		/** The width (in pixels) of lines plotted by this option. */
		private volatile double lineWidth = 1.0;

		/**
		 * Selects or unselects this option. 
		 * 
		 * @param selected Specify true to select, false to unselect.
		 */
		public synchronized void setSelected(boolean selected) {
			this.selected = selected;
		}

		/**
		 * Checks whether this option is selected.
		 * 
		 * @return Returns true if this option is selected.
		 */
		public synchronized boolean isSelected() {
			return this.selected;
		}

		/**
		 * Sets the width (in pixels) of lines plotted by this option.
		 * 
		 * @param lineWidth The width (in pixels) of lines plotted by this option.
		 */
		public synchronized void setLineWidth(double lineWidth) {
			this.lineWidth = lineWidth;
		}

		/**
		 * Gets the width (in pixels) of lines plotted by this option.
		 * 
		 * @return The width (in pixels) of lines plotted by this option.
		 */
		public synchronized double getLineWidth() {
			return this.lineWidth;
		}
	}


	/** Stores the configuration of "With Membranes" option. */
	private volatile MembraneOptionConfiguration membraneOptionConfiguration = new MembraneOptionConfiguration();

	/**
	 * Sets the configuration of "With Membranes" option.
	 */
	public synchronized void getMembraneOptionConfiguration(MembraneOptionConfiguration membraneOptionConfiguration) {
		this.membraneOptionConfiguration = membraneOptionConfiguration;
	}

	/**
	 * Gets the configuration of "With Membranes" option.
	 */
	public synchronized MembraneOptionConfiguration getMembraneOptionConfiguration() {
		return this.membraneOptionConfiguration;
	}

	/**
	 * The class storing configuration values of "With Membranes" option.
	 */
	public static final class MembraneOptionConfiguration {

		/** The flag representing whether this option is selected. */
		private volatile boolean selected = false;

		/**
		 * Selects or unselects this option. 
		 * 
		 * @param selected Specify true to select, false to unselect.
		 */
		public synchronized void setSelected(boolean selected) {
			this.selected = selected;
		}

		/**
		 * Checks whether this option is selected.
		 * 
		 * @return Returns true if this option is selected.
		 */
		public synchronized boolean isSelected() {
			return this.selected;
		}
	}


	/** Stores the configuration of "Gradient" option. */
	private volatile GradientOptionConfiguration gradientOptionConfiguration = new GradientOptionConfiguration();

	/**
	 * Sets the configuration of "Gradient" option.
	 */
	public synchronized void getGradientOptionConfiguration(GradientOptionConfiguration gradientOptionConfiguration) {
		this.gradientOptionConfiguration = gradientOptionConfiguration;
	}

	/**
	 * Gets the configuration of "Gradient" option.
	 */
	public synchronized GradientOptionConfiguration getGradientOptionConfiguration() {
		return this.gradientOptionConfiguration;
	}

	/**
	 * The class storing configuration values of "Gradient" option.
	 */
	public static final class GradientOptionConfiguration {

		// !!! NOTE !!!
		// On Ver.6, this "Gradient" option works as a short-cut UI
		// for switching the current coloring mode selected in "Settings" > "Set Colors" menu.
		// So we don't define detailed parameters here. They are defined in "ColorConfiguration" class.

		/** The flag representing whether this option is selected. */
		private volatile boolean selected = true;

		/**
		 * Selects or unselects this option. 
		 * 
		 * @param selected Specify true to select, false to unselect.
		 */
		public synchronized void setSelected(boolean selected) {
			this.selected = selected;
		}

		/**
		 * Checks whether this option is selected.
		 * 
		 * @return Returns true if this option is selected.
		 */
		public synchronized boolean isSelected() {
			return this.selected;
		}
	}
}
