package com.rinearn.graph3d.renderer;

import java.awt.Color;


/**
 * <span class="lang-en">
 * The class for storing detailed drawing parameters
 * </span>
 * <span class="lang-ja">
 * 詳細な描画設定を指定するためのパラメータオブジェクトです
 * </span>
 * .
 * <div class="lang-en">
 * To specify the detailed drawing parameters, pass an instance of this class as an argument to the drawing methods of {@link RinearnGraph3DRenderer} class.
 * </div>
 * <div class="lang-ja">
 * このクラスのインスタンスを,  {@link RinearnGraph3DRenderer} クラス等の描画系メソッドの引数に渡す事で, 詳細な描画パラメータを制御できます.
 * </div>
 */
public final class RinearnGraph3DDrawingParameter {

	/** The enum for specifying horizontal alignment for some kinds of elements to be drawn, such as texts. */
	public static enum HorizontalAlignment {

		/**
		 * Used for specifying nothing.
		 *
		 * Some kinds of shapes (lines, triangles, and so on) don't have the horizontal alignment property,
		 * and only this value is acceptable for them.
		 *
		 * If this value is specified for shapes having the horizontal alignment property (texts and so on),
		 * its default alignment value will be applied automatically.
		 */
		NONE,

		/** Draws the piece at the left-side of the coordinate. */
		LEFT,

		/** Draws the piece at the right-side of the coordinate. */
		RIGHT,

		/** Draws the piece with aligning the center of the piece with the coordinate. */
		CENTER,

		/** Offsets the piece to the radial direction from the center of the graph frame (used for tick labels). */
		RADIAL,
	}

	/** The enum for specifying vertical alignment for some kinds of elements to be drawn, such as texts. */
	public static enum VerticalAlignment {

		/**
		 * Used for specifying nothing.
		 *
		 * Some kinds of shapes (lines, triangles, and so on) don't have the horizontal alignment property,
		 * and only this value is acceptable for them.
		 *
		 * If this value is specified for shapes having the horizontal alignment property (texts and so on),
		 * its default alignment value will be applied automatically.
		 */
		NONE,

		/** Draws the piece over the coordinate. */
		TOP,

		/** Draws the piece under the coordinate. */
		BOTTOM,

		/** Draws the piece with aligning the center of the piece with the coordinate. */
		CENTER,

		/** Offsets the piece to the radial direction from the center of the graph frame (used for tick labels). */
		RADIAL,
	}

	/**
	 * <span class="lang-en">
	 * Creates a new instance for storing drawing parameters
	 * </span>
	 * <span class="lang-ja">
	 * 新しい描画設定パラメータを格納するインスタンスを作成します
	 * </span>
	 * .
	 */
	public RinearnGraph3DDrawingParameter() {
	}


	/**
	 * <span class="lang-en">Stores the value of the series index</span>
	 * <span class="lang-ja">系列インデックスの値を控えます</span>
	 * .
	 */
	private volatile int seriesIndex = 0;

	/**
	 * <span class="lang-en">
	 * Set the series index
	 * </span>
	 * <span class="lang-ja">
	 * 系列インデックスを設定します
	 * </span>
	 * .
	 * <div class="lang-en">
	 * When the automatic coloring feature is enabled, and the 'gradation' option is disabled, the color is determined by this series index.
	 * </div>
	 * <div class="lang-ja">
	 * 自動彩色機能が有効化されていて, グラデーションオプションはOFFになっている場合には, 系列番号に応じて色が自動で塗り分けられます.
	 * </div>
	 *
	 * @param index
	 *   <span class="lang-en">The series index</span>
	 *   <span class="lang-ja">系列インデックス</span>
	 */
	public synchronized void setSeriesIndex (int index) {
		this.seriesIndex = index;
	}

	/**
	 * <span class="lang-en">
	 * Gets the series index
	 * </span>
	 * <span class="lang-ja">
	 * 系列番号を取得します
	 * </span>
	 * .
	 * @return
	 *   <span class="lang-en">The series index</span>
	 *   <span class="lang-ja">系列番号</span>
	 */
	public synchronized int getSeriesIndex () {
		return this.seriesIndex;
	}


	/**
	 * <span class="lang-en">The drawing color</span>
	 * <span class="lang-ja">描画色です</span>
	 * .
	 */
	private volatile Color color = new Color(0, 0, 0, 0);

	/**
	 * <span class="lang-en">
	 * Sets the drawing color
	 * </span>
	 * <span class="lang-ja">
	 * 描画色を設定します
	 * </span>
	 * .
	 * @param color
	 *   <span class="lang-en">The drawing color</span>
	 *   <span class="lang-ja">描画色</span>
	 */
	public synchronized void setColor (Color color) {
		this.color = color;
	}

	/**
	 * <span class="lang-en">
	 * Gets the drawing color
	 * </span>
	 * <span class="lang-ja">
	 * 描画色を取得します
	 * </span>
	 * .
	 * @return
	 *   <span class="lang-en">The drawing color</span>
	 *   <span class="lang-ja">描画色</span>
	 */
	public synchronized Color getColor () {
		return this.color;
	}


	/**
	 * <span class="lang-en">The flag representing whether the automatic coloring feature is enabled</span>
	 * <span class="lang-ja">自動彩色機能が有効化されているかどうかを表すフラグです</span>
	 * .
	 */
	private volatile boolean autoColoringEnabled = true; // デフォルトだとColor未設定だし, それで引数に指定してエラーってのも不親切だから, デフォルトで自動彩色をONにしておく.

	/**
	 * <span class="lang-en">
	 * Enables/disables the automatic coloring feature
	 * </span>
	 * <span class="lang-ja">
	 * 自動彩色機能を有効化/無効化します
	 * </span>
	 * .
	 * <div class="lang-en">
	 * When this feature is enabled, colors of points/lines/polygons are determined automatically.
	 * (How their color are determined depends on whether the gradation option is enabled.)
	 * If you want to control their colors manually, disable this feature, and specify their color explicitly using setColor method.
	 * </div>
	 * <div class="lang-ja">
	 * 自動彩色機能が有効の場合, 描画色が自動で設定されます（彩色のされ方は, グラデーションオプションの有効/無効によって異なります）.
	 * 半面, 必ず特定の色で描画させたい場合は, このメソッドで自動彩色機能を無効にする必要があります.
	 * その場合は {@link RinearnGraph3DDrawingParameter#setColor} メソッドで描画色を明示指定してください.
	 * </div>
	 *
	 * @param enabled
	 *   <span class="lang-en">Specify true to enable, false to disable</span>
	 *   <span class="lang-ja">有効化する場合は true, 無効化する場合は false を指定</span>
	 */
	public synchronized void setAutoColoringEnabled (boolean enabled) {
		this.autoColoringEnabled = enabled;
	}

	/**
	 * <span class="lang-en">
	 * Returns if the automatic coloring feature is enabled
	 * </span>
	 * <span class="lang-ja">
	 * 自動彩色機能が有効化されているかどうかを取得します
	 * </span>
	 * .
	 * @return
	 *   <span class="lang-en">Returns true if the feature enabled</span>
	 *   <span class="lang-ja">有効化されていれば true</span>
	 */
	public synchronized boolean isAutoColoringEnabled () {
		return this.autoColoringEnabled;
	}


	/**
	 * <span class="lang-en">The flag representing whether the range scaling feature is enabled</span>
	 * <span class="lang-ja">グラフの範囲設定に応じた, 頂点座標の変換機能が、有効化されているかどうかを表すフラグです</span>
	 * .
	 */
	private volatile boolean rangeScalingEnabled = true;

	/**
	 * <span class="lang-en">
	 * Enables/disables the range scaling feature, which is the feature for converting coordinates automatically depends on the range settings of the graph
	 * </span>
	 * <span class="lang-ja">
	 * グラフの範囲設定に応じた, 頂点座標の変換機能の有効/無効を設定します
	 * </span>
	 * .
	 * <div class="lang-en">
	 * For example, if you draw a point at the coordinates (1.0, 2.0, 3.0) using drawPoint method when this feature is enabled,
	 * the point is plotted to the location at which the X-axis scale is 1.0, the Y-axis scale is 2.0, and the Z-axis scale is 3.0.
	 * In this case, the absolute location of the point in the 3D space depends on the range settings of the graph.
	 * <br />
	 * On the other hand, when this feature is disabled, the coordinates of the point (1.0, 2.0, 3.0) are regarded as the "unscaled" coordinates in the 3D space.
	 * Unscaled coordinates are coordinate values based on the "unscaled coordinate system",
	 * which regards the coordinate value at the positive/negative edge of each axis as 1.0/-1.0, independent of the range settings of the graph.
	 * <br />
	 * Unscaled coordinates are useful when you want to customize the grid lines, ticks, and so on of the graph.
	 * </div>
	 *
	 * <div class="lang-ja">
	 * 例えば {@link RinearnGraph3DRenderer#drawPoint(double,double,double,double,RinearnGraph3DDrawingParameter) RinearnGraph3DRenderer#drawPoint} メソッドによって座標（1.0, 2.0, 3.0）に点を描画しようとした場合,
	 * この機能が有効の場合は, グラフのX軸の目盛りが1.0, Y軸の目盛りが2.0, Z軸の目盛りが3.0の位置に点が描画されます. この点の3D空間における位置は, プロット範囲の設定などに依存して変化します.
	 * <br />
	 * それに対して, この機能が無効の場合は, 座標値（1.0, 2.0, 3.0）は「 Unscaled 座標系 」における座標値と見なされます.
	 * Unscaled 座標系では, グラフの各軸の両端を -1.0 から 1.0 に対応付けた座標系で, プロット範囲の設定には全く依存しません.
	 * <br />
	 * Unscaled 座標系は, 例えばグラフの目盛りやグリッド線の描画をカスタマイズしたい場合などに有用です.
	 * </div>
	 *
	 * @param enabled
	 *   <span class="lang-en">Specify true to enable, false to disable</span>
	 *   <span class="lang-ja">有効化する場合は true, 無効化する場合は false を指定</span>
	 */
	public synchronized void setRangeScalingEnabled (boolean enabled) {
		this.rangeScalingEnabled = enabled;
	}

	/**
	 * <span class="lang-en">
	 * Returns if the range scaling feature is enabled
	 * </span>
	 * <span class="lang-ja">
	 * グラフの範囲設定に応じた, 頂点座標の変換機能が有効化されているかどうかを取得します
	 * </span>
	 * .
	 * @return
	 *   <span class="lang-en">Returns true if the feature enabled</span>
	 *   <span class="lang-ja">有効化されていれば true</span>
	 */
	public synchronized boolean isRangeScalingEnabled () {
		return this.rangeScalingEnabled;
	}


	/**
	 * <span class="lang-en">The flag representing whether the range clipping feature is enabled</span>
	 * <span class="lang-ja">グラフ範囲外にはみ出した部分を, クリッピングする機能が、有効化されているかどうかを表すフラグです</span>
	 * .
	 */
	private volatile boolean rangeClippingEnabled = true;

	/**
	 * <span class="lang-en">
	 * Enable/disable the range clipping feature, which is the feature for clipping a part of a line/polygon protruding out of the range of the graph
	 * </span>
	 * <span class="lang-ja">
	 * グラフ範囲外にはみ出した部分を, クリッピングする機能の有効/無効を設定します
	 * </span>
	 * .
	 * @param enabled
	 *   <span class="lang-en">Specify true to enable, false to disable</span>
	 *   <span class="lang-ja">有効化する場合は true, 無効化する場合は false を指定</span>
	 */
	public synchronized void setRangeClippingEnabled (boolean enabled) {
		this.rangeClippingEnabled = enabled;
	}

	/**
	 * <span class="lang-en">
	 * Returns if the range clipping feature is enabled
	 * </span>
	 * <span class="lang-ja">
	 * グラフ範囲外にはみ出した部分をクリッピングする機能が, 有効化されているかどうかを返します
	 * </span>
	 * .
	 * @return
	 *   <span class="lang-en">Returns true if the feature enabled</span>
	 *   <span class="lang-ja">有効化されていれば true</span>
	 */
	public synchronized boolean isRangeClippingEnabled () {
		return this.rangeClippingEnabled;
	}


	/**
	 * <span class="lang-en">Stores the X component of the depth-offset</span>
	 * <span class="lang-ja">深度オフセット量のX成分を控えます</span>
	 * .
	 */
	private volatile double offsetX = 0.0;

	/**
	 * <span class="lang-en">Stores the Y component of the depth-offset</span>
	 * <span class="lang-ja">深度オフセット量のY成分を控えます</span>
	 * .
	 */
	private volatile double offsetY = 0.0;

	/**
	 * <span class="lang-en">Stores the Z component of the depth-offset</span>
	 * <span class="lang-ja">深度オフセット量のZ成分を控えます</span>
	 * .
	 */
	private volatile double offsetZ = 0.0;

	/**
	 * <span class="lang-en">
	 * Sets the depth-offset values, for tuning near-far relationship between polygons
	 * </span>
	 * <span class="lang-ja">
	 * ポリゴンの遠近判定を微調整するための, 深度のオフセット量を設定します
	 * </span>
	 * .
	 * <div class="lang-en">
	 * In this context, "depth" means the degree representing how the object is far from the camera.
	 * For example, a polygin is located on the line of sight, and the distance of it from the camera is 3, then the depth of the polygon is 3.
	 * Note that, the depth of the polygon depends on the camera angles, so it may varies dynamically.
	 * <br />
	 * Near-far relationship between polygons are determined based on the depths values of the polygons.
	 * However, sometimes, you may want to render the specific polygon at the nearer-side than other polygons.
	 * In such case, set the depth-offset value to the polygon, using this method.
	 * <br />
	 * If a polygon has a positive depth-offset value, the polygon is regarded to be farther from the camera than the actual distance,
	 * on the near-far determination between polygons.
	 * In the contrast, if a polygon has a negative depth-offset value, the polygon is regarded to be nearer than actually it is.
	 * <br />
	 * Depth offset values are scaled as same as coordinate values of polygons.
	 * Generally, ranges of axes of the graph are not the same, so you can set depth-offset values independently for X, Y, and Z-axis.
	 * <br />
	 * For example, when the camera looks to the direction of the X-axis, and a polygon is located on the sight line, the argument offsetX is regarded as the offset value of the polygon.
	 * If there is no axis parallel with the sight line, the depth-offset value is computed by ellipsoid interpolation.
	 * </div>
	 *
	 * <div class="lang-ja">
	 * ここでの深度とは, ポリゴンがカメラからどれだけ遠くに離れているかを表す値です.
	 * 例えば, ちょうど視点の中心線上に, カメラから距離 3 の位置にポリゴンがある場合, そのポリゴンの深度は 3 です
	 * なお, 深度は, カメラアングルに依存して動的に変化する事に留意が必要です.
	 * <br />
	 * 複数のポリゴンが近距離で重なっている場合, どちらが手前に描かれるかは, 深度の大小によって決定されます.
	 * しかし, 場面によっては, 特定のポリゴンを, 優先的に手前に描画させたい場合なども存在します.
	 * そのような場合は, このメソッドにより, ポリゴンに深度のオフセット量を設定して調整します.
	 * <br />
	 * オフセット量に正の値を設定すると, そのポリゴンは, 本来の位置からその量だけ奥にあるものとして描画されます. 逆に, 負のオフセット量を設定すると, その量だけ手前に描画されます.
	 * <br />
	 * オフセット量の大きさは, ポリゴンの頂点座標値と同じようにスケール変換された後で, 遠近判定に使用されます.
	 * グラフのX/Y/Z軸は, 一般にそれぞれ長さの単位が異なる非等方座標系となっているため,
	 * オフセット量もX/Y/Z軸の単位で表したものを3つ独立に設定する事が可能です.
	 * <br />
	 * 例えば, 画面の奥方向がX軸に一致している場合は引数 offsetX がオフセット量となり,
	 * Z軸に一致している場合は引数 offsetY がオフセット量となります.
	 * X/Y/Z軸のいずれも画面奥方向に一致しない（斜めの方向を向いている）場合は, 楕円体補間によって計算された適切なオフセット量が用いられます.
	 * </div>
	 *
	 * @param offsetX
	 *   <span class="lang-en">The depth-offset value for the X-axis</span>
	 *   <span class="lang-ja">X軸での深度オフセット値</span>
	 * @param offsetY
	 *   <span class="lang-en">The depth-offset value for the Y-axis</span>
	 *   <span class="lang-ja">Y軸での深度オフセット値</span>
	 * @param offsetZ
	 *   <span class="lang-en">The depth-offset value for the Z-axis</span>
	 *   <span class="lang-ja">Z軸での深度オフセット値</span>
	 */
	public synchronized void setDepthOffsetAmounts(double offsetX, double offsetY, double offsetZ) {
		this.offsetX = -offsetX;
		this.offsetY = -offsetY;
		this.offsetZ = -offsetZ;
	}

	/**
	 * <span class="lang-en">
	 * Returns the depth-offset values, for tuning near-far relationship between polygons
	 * </span>
	 * <span class="lang-ja">
	 * ポリゴンの遠近判定を微調整するための, 深度のオフセット量を取得します
	 * </span>
	 * .
	 * @return
	 *   <span class="lang-en">The array storing the depth-offset values (the index is: 0 represents X, 1 represents Y, and 2 represents Z)</span>
	 *   <span class="lang-ja">深度オフセット値を格納する配列（インデックス: 0がX, 1がY, 2がZ）</span>
	 */
	public synchronized double[] getDepthOffsetAmounts() { // get が配列を返すので複数形の Amounts を付けている
		return new double[]{ -this.offsetX, -this.offsetY, -this.offsetZ };
	}


	/** Stores the horizontal alignment. */
	private volatile HorizontalAlignment horizontalAlignment = HorizontalAlignment.NONE;

	/**
	 * Sets the horizontal alignment for the element to be drawn, such as texts.
	 *
	 * Note that, whether the drawn element has the horizontal alignment property depends on the kind of the element.
	 * If it doesn't have, only the value NONE is acceptable.
	 *
	 * @param horizontalAlignment The horizontal alignment.
	 */
	public synchronized void setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
		this.horizontalAlignment = horizontalAlignment;
	}

	/**
	 * Sets the horizontal alignment for the element to be drawn, such as texts.
	 *
	 * Note that, whether the drawn element has the horizontal alignment property depends on the kind of the element.
	 * If it doesn't have, only the value NONE is acceptable.
	 *
	 * @param horizontalAlignment The horizontal alignment.
	 */
	public synchronized HorizontalAlignment getHorizontalAlignment() {
		return this.horizontalAlignment;
	}


	/** Stores the vertical alignment. */
	private volatile VerticalAlignment verticalAlignment = VerticalAlignment.NONE;

	/**
	 * Sets the vertical alignment for the element to be drawn, such as texts.
	 *
	 * Note that, whether the drawn element has the vertical alignment property depends on the kind of the element.
	 * If it doesn't have, only the value NONE is acceptable.
	 *
	 * @param vertocalAlignment The vertical alignment.
	 */
	public synchronized void setVertocalAlignment(VerticalAlignment vertocalAlignment) {
		this.verticalAlignment = verticalAlignment;
	}

	/**
	 * Sets the vertical alignment for the element to be drawn, such as texts.
	 *
	 * Note that, whether the drawn element has the vertical alignment property depends on the kind of the element.
	 * If it doesn't have, only the value NONE is acceptable.
	 *
	 * @param verticalAlignment The vertical alignment.
	 */
	public synchronized VerticalAlignment getVerticalAlignment() {
		return this.verticalAlignment;
	}
}
