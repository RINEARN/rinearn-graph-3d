package com.rinearn.graph3d.renderer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;


/**
 * <span class="lang-en">
 * The interface for controlling the 3D rendering engine (renderer) of RINEARN Graph 3D
 * </span>
 * <span class="lang-ja">
 * RINEARN Graph 3D の3D描画エンジンを制御するためのインターフェースです
 * </span>
 * .
 */
public interface RinearnGraph3DRenderer {

	/**
	 * <span class="lang-en">
	 * Configures the state of this renderer, by setting the container storing configuration values
	 * </span>
	 * <span class="lang-ja">
	 * 設定値を格納するコンテナを渡して、このレンダラーの状態を設定します
	 * <span class="lang-en">
	 * .
	 * <span class="lang-en">
	 * Note that, the changes of the configuration (contains ranges of X/Y/Z axes)
	 * does not affect to the currently drawn contents (points, lines, quadrangles, and so on).
	 * To reflect the changes, please clear() and re-draw all contents again.
	 * <span class="lang-en">
	 * <span class="lang-ja">
	 * なお、設定の変更は、それまでに draw 系メソッドを用いて描画した内容（点や線、四角形など）
	 * には反映されない事に注意してください。
	 * 設定の変更を反映させるには、一旦 clear() メソッドでそれらをクリアして、再度描画してください。
	 * <span class="lang-en">
	 *
	 * @param configuration
	 *   <span class="lang-en">
	 *   The container storing configuration values.
	 *   </span>
	 *   <span class="lang-ja">
	 *   設定値を格納しているコンテナ
	 *   </span>
	 */
	public void configure(RinearnGraph3DConfiguration configuration);


	/**
	 * <span class="lang-en">
	 * Disposes all the disposable resources in this renderer instance
	 * </span>
	 * <span class="lang-ja">
	 * このレンダラーのインスタンス内の、破棄可能なリソースを全て破棄します
	 * </span>
	 * .
	 * <span class="lang-en">
	 * This instance is not available anyway after calling this method.
	 * When you want to reuse this renderer, create a new instance again.
	 * </span>
	 * <span class="lang-ja">
	 * このメソッドの呼び出し後は, このインスタンスは一切使用できなくなります.
	 * 再び使用したい場合は, 新しいインスタンスを生成してください.
	 * </span>
	 */
	public void dispose();


	/**
	 * <span class="lang-en">
	 * Clears all currently rendered contents of the graph
	 * </span>
	 * <span class="lang-ja">
	 * 現在のグラフ画面における描画内容を全て消去します
	 * </span>
	 * .
	 *
	 * <div class="lang-en">
	 * Different from {@link com.rinearn.graph3d.RinearnGraph3D#clear RinearnGraph3D.clear} method,
	 * this method clears not only plotted data but also axes and scales (so this method clears everything on the graph).
	 * If you want to clear only plotted data, use {@link com.rinearn.graph3d.RinearnGraph3D#clear RinearnGraph3D.clear} method instead.
	 * </div>
	 *
	 * <div class="lang-ja">
	 * {@link com.rinearn.graph3d.RinearnGraph3D#clear RinearnGraph3D.clear} とは異なり, 座標軸や目盛りなども消去され, 画面上に何も無い状態となります.
	 * それらを残したい場合は, 代わりに {@link com.rinearn.graph3d.RinearnGraph3D#clear RinearnGraph3D.clear} を使用してください.
	 * </div>
	 */
	public void clear();


	/**
	 * <span class="lang-en">
	 * Renders the graph screen
	 * </span>
	 * <span class="lang-ja">
	 * グラフ画面を描画します
	 * </span>
	 * .
	 *
	 * <div class="lang-en">
	 * Basically, the renderer does not update the graph image until this method is called.
	 * Hence, after using the drawing methods such as {@link RinearnGraph3DRenderer#drawPoint drawPoint} and {@link RinearnGraph3DRenderer#drawLine drawLine},
	 * please call this method for rendering the drawn objects to the graph.
	 * Note that, there are some certain timings that the graph image is updated automatically,
	 * e.g.: when the camera angle has changed by user's mouse dragging.
	 * </div>
	 *
	 * <div class="lang-ja">
	 * 通常は, このメソッドをコールするまでグラフ画面は更新されません.
	 * そのため, {@link RinearnGraph3DRenderer#drawPoint drawPoint} や {@link RinearnGraph3DRenderer#drawLine drawLine} 等で立体の描画を行った後, 最後にこのメソッドをコールしてください.
	 * ただし, マウス等で視点が操作された際など, 自動で再描画が行われる事もあります.
	 * </div>
	 */
	public void render();


	/**
	 * <span class="lang-en">
	 * Gets the image of the graph screen
	 * </span>
	 * <span class="lang-ja">
	 * グラフ画面の画像を取得します
	 * </span>
	 * .
	 * @return
	 *   <span class="lang-en">The image of the graph screen</span>
	 *   <span class="lang-ja">グラフ画面の画像</span>
	 */
	public Image getScreenImage();


	/**
	 * <span class="lang-en">
	 * References the value of the flag representing whether the content of the graph screen has been updated,
	 * in addition. and performs Compare-and-Swap (CAS) operation to it
	 * </span>
	 * <span class="lang-ja">
	 * グラフ画面の内容が更新されたかどうかを表すフラグの値を参照し、それに加えて、
	 * いわゆる Compare-and-Swap (CAS) 操作を行います
	 * </span>
	 * .
	 * <span class="lang-en">
	 * Specifically, when the value of the flag equals to the value passed as "fromValue" argument,
	 * overwrite the flag by the value passed as "toValue" argument.
	 * In addition, regardless whether the above is performed,
	 * this method returns the original (non modified) value of the flag as a return value.
	 * </span>
	 * <span class="lang-ja">
	 * 具体的には、フラグの値が引数 fromValue と等しかった場合に、引数 toValue の値へと変更します。
	 * また、変更の有無に関わらず、元の値を戻り値として返します。
	 * </span>
	 *
	 * <span class="lang-en">
	 * The followings are typical examples using this method:
	 * For referring the value of the flag, and resetting it to the false, do "flag = casScreenUpdated(true, false)".
	 * For referring the value of the flag, without resetting it, do "flag = casScreenUpdated(true, true)" or "...(false, false)".
	 * For putting up the flag, do "casScreenUpdated(false, true)".
	 * </span>
	 * <span class="lang-ja">
	 * 以下はこのメソッドの典型的な使用例です:　
	 * フラグ値を参照しつつ、falseにリセットしたい場合は、"flag = casScreenUpdated(true, false)" のように行います。
	 * リセットせずに参照のみ行うには "flag = casScreenUpdated(true, true)" または "...(false, false)" とします。
	 * 外部からフラグを立てたい場合は "casScreenUpdated(false, true)" とします。
	 * </span>
	 *
	 * <span class="lang-en">
	 * An app-side thread refers this flag periodically, and updates the window if it is true, and then resets the flag to false.
	 * However, user's code running on an other thread may call render() method,
	 * and the updating of the flag caused by it may conflict to the above.
	 * Hence, operations for referencing and changing the value of this flag must be performed atomically, through this method.
	 * </span>
	 * <span class="lang-ja">
	 * このフラグはアプリ側スレッドによって定期参照され、もし true であった場合には画面が更新され、false にリセットされます。
	 * 一方、APIを通して外部から（別スレッドで）render メソッド等が呼ばれ、それによるフラグ更新が上記に割り込む可能性があります。
	 * 従って、フラグの参照とリセットの操作は、このメソッドを通してアトミック（不可分）な形で一括して行う必要があります。
	 * </span>
	 *
	 * @param fromValue
	 *   <span class="lang-en">The value to be swapped by "toValue"</span>
	 *   <span class="lang-ja">"toValue" に変更されるべき場合の値</span>
	 *
	 * @param toValue
	 *   <span class="lang-en">The swapped value</span>
	 *   <span class="lang-ja">変更後の値</span>
	 *
	 * @return
	 *   <span class="lang-en">Unmodified flag value (true if the content of the graph screen has been updated)</span>
	 *   <span class="lang-ja">未変更のフラグの値（グラフ画面の内容が更新された場合に true）</span>
	 */
	public boolean casScreenUpdated(boolean fromValue, boolean toValue);


	/**
	 * <span class="lang-en">
	 * References the value of the flag representing whether the graph screen has been resized,
	 * in addition. and performs Compare-and-Swap (CAS) operation to it
	 * </span>
	 * <span class="lang-ja">
	 * グラフ画面がリサイズされたかどうかを表すフラグの値を参照し、それに加えて、
	 * いわゆる Compare-and-Swap (CAS) 操作を行います
	 * </span>
	 * .
	 * <span class="lang-en">
	 * Specifically, when the value of the flag equals to the value passed as "fromValue" argument,
	 * overwrite the flag by the value passed as "toValue" argument.
	 * In addition, regardless whether the above is performed,
	 * this method returns the original (non modified) value of the flag as a return value.
	 * </span>
	 * <span class="lang-ja">
	 * 具体的には、フラグの値が引数 fromValue と等しかった場合に、引数 toValue の値へと変更します。
	 * また、変更の有無に関わらず、元の値を戻り値として返します。
	 * </span>
	 *
	 * <span class="lang-en">
	 * For usage examples, and why we design this flag's operations as a CAS operation,
	 * see the description of "casScreenUpdated()" method.
	 * </span>
	 * <span class="lang-ja">
	 * 使用例や、このフラグの操作をCAS操作としている理由については、casScreenUpdated() メソッドの説明を参照してください。
	 * </span>
	 *
	 * @param fromValue
	 *   <span class="lang-en">The value to be swapped by "toValue"</span>
	 *   <span class="lang-ja">"toValue" に変更されるべき場合の値</span>
	 *
	 * @param toValue
	 *   <span class="lang-en">The swapped value</span>
	 *   <span class="lang-ja">変更後の値</span>
	 *
	 * @return
	 *   <span class="lang-en">Unmodified flag value (true if the graph screen has been resized)</span>
	 *   <span class="lang-ja">未変更のフラグの値（グラフ画面がリサイズされた場合に true）</span>
	 */
	public boolean casScreenResized(boolean fromValue, boolean toValue);


	/**
	 * <span class="lang-en">
	 * Draws a point
	 * </span>
	 * <span class="lang-ja">
	 * 点を描画します
	 * </span>
	 * .
	 * @param x
	 *   <span class="lang-en">The X coordinate of the point</span>
	 *   <span class="lang-ja">点のX座標</span>
	 * @param y
	 *   <span class="lang-en">The Y coordinate of the point</span>
	 *   <span class="lang-ja">点のY座標</span>
	 * @param z
	 *   <span class="lang-en">The Z coordinate of the point</span>
	 *   <span class="lang-ja">点のZ座標</span>
	 * @param radius
	 *   <span class="lang-en">The radius of the point (in pixels)</span>
	 *   <span class="lang-ja">点の半径（ピクセル単位）</span>
	 */
	public void drawPoint(double x, double y, double z, double radius);


	/**
	 * <span class="lang-en">
	 * Draws a point of the specified color
	 * </span>
	 * <span class="lang-ja">
	 * 指定された色で, 点を描画します
	 * </span>
	 * .
	 * @param x
	 *   <span class="lang-en">The X coordinate of the point</span>
	 *   <span class="lang-ja">点のX座標</span>
	 * @param y
	 *   <span class="lang-en">The Y coordinate of the point</span>
	 *   <span class="lang-ja">点のY座標</span>
	 * @param z
	 *   <span class="lang-en">The Z coordinate of the point</span>
	 *   <span class="lang-ja">点のZ座標</span>
	 * @param radius
	 *   <span class="lang-en">The radius of the point (in pixels)</span>
	 *   <span class="lang-ja">点の半径（ピクセル単位）</span>
	 * @param color
	 *   <span class="lang-en">The color of the point</span>
	 *   <span class="lang-ja">点の色</span>
	 */
	public void drawPoint(double x, double y, double z, double radius, Color color);


	/**
	 * <span class="lang-en">
	 * Draws a point with the specified parameter settings
	 * </span>
	 * <span class="lang-ja">
	 * 詳細な設定に基づいて, 点を描画します
	 * </span>
	 * .
	 * @param x
	 *   <span class="lang-en">The X coordinate of the point</span>
	 *   <span class="lang-ja">点のX座標</span>
	 * @param y
	 *   <span class="lang-en">The Y coordinate of the point</span>
	 *   <span class="lang-ja">点のY座標</span>
	 * @param z
	 *   <span class="lang-en">The Z coordinate of the point</span>
	 *   <span class="lang-ja">点のZ座標</span>
	 * @param radius
	 *   <span class="lang-en">The radius of the point (in pixels)</span>
	 *   <span class="lang-ja">点の半径（ピクセル単位）</span>
	 * @param parameter
	 *   <span class="lang-en">The object storing the drawing parameters</span>
	 *   <span class="lang-ja">描画パラメーターを格納しているオブジェクト</span>
	 */
	public void drawPoint(double x, double y, double z, double radius, RinearnGraph3DDrawingParameter parameter);


	/**
	 * <span class="lang-en">
	 * Draws a line
	 * </span>
	 * <span class="lang-ja">
	 * 直線を描画します
	 * </span>
	 * .
	 * @param aX
	 *   <span class="lang-en">The X coordinate of the point A</span>
	 *   <span class="lang-ja">点AのX座標</span>
	 * @param aY
	 *   <span class="lang-en">The Y coordinate of the point A</span>
	 *   <span class="lang-ja">点AのY座標</span>
	 * @param aZ
	 *   <span class="lang-en">The Z coordinate of the point A</span>
	 *   <span class="lang-ja">点AのZ座標</span>
	 * @param bX
	 *   <span class="lang-en">The X coordinate of the point B</span>
	 *   <span class="lang-ja">点BのX座標</span>
	 * @param bY
	 *   <span class="lang-en">The Y coordinate of the point B</span>
	 *   <span class="lang-ja">点BのY座標</span>
	 * @param bZ
	 *   <span class="lang-en">The Z coordinate of the point B</span>
	 *   <span class="lang-ja">点BのZ座標</span>
	 * @param width
	 *   <span class="lang-en">The width of the line (in pixels)</span>
	 *   <span class="lang-ja">直線の太さ（ピクセル単位）</span>
	 */
	public void drawLine(double aX, double aY, double aZ, double bX, double bY, double bZ, double width);


	/**
	 * <span class="lang-en">
	 * Draws a line of the specified color
	 * </span>
	 * <span class="lang-ja">
	 * 指定された色の直線を描画します
	 * </span>
	 * .
	 * @param aX
	 *   <span class="lang-en">The X coordinate of the point A</span>
	 *   <span class="lang-ja">点AのX座標</span>
	 * @param aY
	 *   <span class="lang-en">The Y coordinate of the point A</span>
	 *   <span class="lang-ja">点AのY座標</span>
	 * @param aZ
	 *   <span class="lang-en">The Z coordinate of the point A</span>
	 *   <span class="lang-ja">点AのZ座標</span>
	 * @param bX
	 *   <span class="lang-en">The X coordinate of the point B</span>
	 *   <span class="lang-ja">点BのX座標</span>
	 * @param bY
	 *   <span class="lang-en">The Y coordinate of the point B</span>
	 *   <span class="lang-ja">点BのY座標</span>
	 * @param bZ
	 *   <span class="lang-en">The Z coordinate of the point B</span>
	 *   <span class="lang-ja">点BのZ座標</span>
	 * @param width
	 *   <span class="lang-en">The width of the line (in pixels)</span>
	 *   <span class="lang-ja">直線の太さ（ピクセル単位）</span>
	 * @param color
	 *   <span class="lang-en">The color of the line</span>
	 *   <span class="lang-ja">直線の色</span>
	 */
	public void drawLine(double aX, double aY, double aZ, double bX, double bY, double bZ, double width, Color color);


	/**
	 * <span class="lang-en">
	 * Draws a line with the specified parameter settings
	 * </span>
	 * <span class="lang-ja">
	 * 詳細な設定に基づいて, 直線を描画します
	 * </span>
	 * .
	 * @param aX
	 *   <span class="lang-en">The X coordinate of the point A</span>
	 *   <span class="lang-ja">点AのX座標</span>
	 * @param aY
	 *   <span class="lang-en">The Y coordinate of the point A</span>
	 *   <span class="lang-ja">点AのY座標</span>
	 * @param aZ
	 *   <span class="lang-en">The Z coordinate of the point A</span>
	 *   <span class="lang-ja">点AのZ座標</span>
	 * @param bX
	 *   <span class="lang-en">The X coordinate of the point B</span>
	 *   <span class="lang-ja">点BのX座標</span>
	 * @param bY
	 *   <span class="lang-en">The Y coordinate of the point B</span>
	 *   <span class="lang-ja">点BのY座標</span>
	 * @param bZ
	 *   <span class="lang-en">The Z coordinate of the point B</span>
	 *   <span class="lang-ja">点BのZ座標</span>
	 * @param width
	 *   <span class="lang-en">The width of the line (in pixels)</span>
	 *   <span class="lang-ja">直線の太さ（ピクセル単位）</span>
	 * @param parameter
	 *   <span class="lang-en">The object storing the drawing parameters</span>
	 *   <span class="lang-ja">描画パラメーターを格納しているオブジェクト</span>
	 */
	public void drawLine(double aX, double aY, double aZ, double bX, double bY, double bZ, double width, RinearnGraph3DDrawingParameter parameter);


	/**
	 * <span class="lang-en">
	 * Draws a triangle polygon
	 * </span>
	 * <span class="lang-ja">
	 * 三角形ポリゴンを描画します
	 * </span>
	 * .
	 * @param aX
	 *   <span class="lang-en">The X coordinate of the point A</span>
	 *   <span class="lang-ja">点AのX座標</span>
	 * @param aY
	 *   <span class="lang-en">The Y coordinate of the point A</span>
	 *   <span class="lang-ja">点AのY座標</span>
	 * @param aZ
	 *   <span class="lang-en">The Z coordinate of the point A</span>
	 *   <span class="lang-ja">点AのZ座標</span>
	 * @param bX
	 *   <span class="lang-en">The X coordinate of the point B</span>
	 *   <span class="lang-ja">点BのX座標</span>
	 * @param bY
	 *   <span class="lang-en">The Y coordinate of the point B</span>
	 *   <span class="lang-ja">点BのY座標</span>
	 * @param bZ
	 *   <span class="lang-en">The Z coordinate of the point B</span>
	 *   <span class="lang-ja">点BのZ座標</span>
	 * @param cX
	 *   <span class="lang-en">The X coordinate of the point C</span>
	 *   <span class="lang-ja">点CのX座標</span>
	 * @param cY
	 *   <span class="lang-en">The Y coordinate of the point C</span>
	 *   <span class="lang-ja">点CのY座標</span>
	 * @param cZ
	 *   <span class="lang-en">The Z coordinate of the point C</span>
	 *   <span class="lang-ja">点CのZ座標</span>
	 */
	public void drawTriangle(double aX, double aY, double aZ, double bX, double bY, double bZ, double cX, double cY, double cZ);


	/**
	 * <span class="lang-en">
	 * Draws a triangle polygon of the specified color
	 * </span>
	 * <span class="lang-ja">
	 * 指定された色の三角形ポリゴンを描画します
	 * </span>
	 * .
	 * @param aX
	 *   <span class="lang-en">The X coordinate of the point A</span>
	 *   <span class="lang-ja">点AのX座標</span>
	 * @param aY
	 *   <span class="lang-en">The Y coordinate of the point A</span>
	 *   <span class="lang-ja">点AのY座標</span>
	 * @param aZ
	 *   <span class="lang-en">The Z coordinate of the point A</span>
	 *   <span class="lang-ja">点AのZ座標</span>
	 * @param bX
	 *   <span class="lang-en">The X coordinate of the point B</span>
	 *   <span class="lang-ja">点BのX座標</span>
	 * @param bY
	 *   <span class="lang-en">The Y coordinate of the point B</span>
	 *   <span class="lang-ja">点BのY座標</span>
	 * @param bZ
	 *   <span class="lang-en">The Z coordinate of the point B</span>
	 *   <span class="lang-ja">点BのZ座標</span>
	 * @param cX
	 *   <span class="lang-en">The X coordinate of the point C</span>
	 *   <span class="lang-ja">点CのX座標</span>
	 * @param cY
	 *   <span class="lang-en">The Y coordinate of the point C</span>
	 *   <span class="lang-ja">点CのY座標</span>
	 * @param cZ
	 *   <span class="lang-en">The Z coordinate of the point C</span>
	 *   <span class="lang-ja">点CのZ座標</span>
	 * @param color
	 *   <span class="lang-en">The color of the triangle polygon</span>
	 *   <span class="lang-ja">三角形ポリゴンの色</span>
	 */
	public void drawTriangle(double aX, double aY, double aZ, double bX, double bY, double bZ, double cX, double cY, double cZ, Color color);


	/**
	 * <span class="lang-en">
	 * Draws a triangle polygon with the specified parameter settings
	 * </span>
	 * <span class="lang-ja">
	 * 詳細な設定に基づいて, 三角形ポリゴンを描画します
	 * </span>
	 * .
	 * @param aX
	 *   <span class="lang-en">The X coordinate of the point A</span>
	 *   <span class="lang-ja">点AのX座標</span>
	 * @param aY
	 *   <span class="lang-en">The Y coordinate of the point A</span>
	 *   <span class="lang-ja">点AのY座標</span>
	 * @param aZ
	 *   <span class="lang-en">The Z coordinate of the point A</span>
	 *   <span class="lang-ja">点AのZ座標</span>
	 * @param bX
	 *   <span class="lang-en">The X coordinate of the point B</span>
	 *   <span class="lang-ja">点BのX座標</span>
	 * @param bY
	 *   <span class="lang-en">The Y coordinate of the point B</span>
	 *   <span class="lang-ja">点BのY座標</span>
	 * @param bZ
	 *   <span class="lang-en">The Z coordinate of the point B</span>
	 *   <span class="lang-ja">点BのZ座標</span>
	 * @param cX
	 *   <span class="lang-en">The X coordinate of the point C</span>
	 *   <span class="lang-ja">点CのX座標</span>
	 * @param cY
	 *   <span class="lang-en">The Y coordinate of the point C</span>
	 *   <span class="lang-ja">点CのY座標</span>
	 * @param cZ
	 *   <span class="lang-en">The Z coordinate of the point C</span>
	 *   <span class="lang-ja">点CのZ座標</span>
	 * @param parameter
	 *   <span class="lang-en">The object storing the drawing parameters</span>
	 *   <span class="lang-ja">描画パラメーターを格納しているオブジェクト</span>
	 */
	public void drawTriangle(double aX, double aY, double aZ, double bX, double bY, double bZ, double cX, double cY, double cZ, RinearnGraph3DDrawingParameter parameter);


	/**
	 * <span class="lang-en">
	 * Draws a quadrangle polygon
	 * </span>
	 * <span class="lang-ja">
	 * 四角形ポリゴンを描画します
	 * </span>
	 * .
	 * @param aX
	 *   <span class="lang-en">The X coordinate of the point A</span>
	 *   <span class="lang-ja">点AのX座標</span>
	 * @param aY
	 *   <span class="lang-en">The Y coordinate of the point A</span>
	 *   <span class="lang-ja">点AのY座標</span>
	 * @param aZ
	 *   <span class="lang-en">The Z coordinate of the point A</span>
	 *   <span class="lang-ja">点AのZ座標</span>
	 * @param bX
	 *   <span class="lang-en">The X coordinate of the point B</span>
	 *   <span class="lang-ja">点BのX座標</span>
	 * @param bY
	 *   <span class="lang-en">The Y coordinate of the point B</span>
	 *   <span class="lang-ja">点BのY座標</span>
	 * @param bZ
	 *   <span class="lang-en">The Z coordinate of the point B</span>
	 *   <span class="lang-ja">点BのZ座標</span>
	 * @param cX
	 *   <span class="lang-en">The X coordinate of the point C</span>
	 *   <span class="lang-ja">点CのX座標</span>
	 * @param cY
	 *   <span class="lang-en">The Y coordinate of the point C</span>
	 *   <span class="lang-ja">点CのY座標</span>
	 * @param cZ
	 *   <span class="lang-en">The Z coordinate of the point C</span>
	 *   <span class="lang-ja">点CのZ座標</span>
	 * @param dX
	 *   <span class="lang-en">The X coordinate of the point D</span>
	 *   <span class="lang-ja">点DのX座標</span>
	 * @param dY
	 *   <span class="lang-en">The Y coordinate of the point D</span>
	 *   <span class="lang-ja">点DのY座標</span>
	 * @param dZ
	 *   <span class="lang-en">The Z coordinate of the point D</span>
	 *   <span class="lang-ja">点DのZ座標</span>
	 */
	public void drawQuadrangle(double aX, double aY, double aZ, double bX, double bY, double bZ, double cX, double cY, double cZ, double dX, double dY, double dZ);


	/**
	 * <span class="lang-en">
	 * Draws a quadrangle polygon of the specified color
	 * </span>
	 * <span class="lang-ja">
	 * 指定された色の四角形ポリゴンを描画します
	 * </span>
	 * .
	 * @param aX
	 *   <span class="lang-en">The X coordinate of the point A</span>
	 *   <span class="lang-ja">点AのX座標</span>
	 * @param aY
	 *   <span class="lang-en">The Y coordinate of the point A</span>
	 *   <span class="lang-ja">点AのY座標</span>
	 * @param aZ
	 *   <span class="lang-en">The Z coordinate of the point A</span>
	 *   <span class="lang-ja">点AのZ座標</span>
	 * @param bX
	 *   <span class="lang-en">The X coordinate of the point B</span>
	 *   <span class="lang-ja">点BのX座標</span>
	 * @param bY
	 *   <span class="lang-en">The Y coordinate of the point B</span>
	 *   <span class="lang-ja">点BのY座標</span>
	 * @param bZ
	 *   <span class="lang-en">The Z coordinate of the point B</span>
	 *   <span class="lang-ja">点BのZ座標</span>
	 * @param cX
	 *   <span class="lang-en">The X coordinate of the point C</span>
	 *   <span class="lang-ja">点CのX座標</span>
	 * @param cY
	 *   <span class="lang-en">The Y coordinate of the point C</span>
	 *   <span class="lang-ja">点CのY座標</span>
	 * @param cZ
	 *   <span class="lang-en">The Z coordinate of the point C</span>
	 *   <span class="lang-ja">点CのZ座標</span>
	 * @param dX
	 *   <span class="lang-en">The X coordinate of the point D</span>
	 *   <span class="lang-ja">点DのX座標</span>
	 * @param dY
	 *   <span class="lang-en">The Y coordinate of the point D</span>
	 *   <span class="lang-ja">点DのY座標</span>
	 * @param dZ
	 *   <span class="lang-en">The Z coordinate of the point D</span>
	 *   <span class="lang-ja">点DのZ座標</span>
	 * @param color
	 *   <span class="lang-en">The color of the quadrangle polygon</span>
	 *   <span class="lang-ja">四角形ポリゴンの色</span>
	 */
	public void drawQuadrangle(double aX, double aY, double aZ, double bX, double bY, double bZ, double cX, double cY, double cZ, double dX, double dY, double dZ, Color color);


	/**
	 * <span class="lang-en">
	 * Draws a quadrangle polygon with the specified parameter settings
	 * </span>
	 * <span class="lang-ja">
	 * 詳細な設定に基づいて, 四角形ポリゴンを描画します
	 * </span>
	 * .
	 * @param aX
	 *   <span class="lang-en">The X coordinate of the point A</span>
	 *   <span class="lang-ja">点AのX座標</span>
	 * @param aY
	 *   <span class="lang-en">The Y coordinate of the point A</span>
	 *   <span class="lang-ja">点AのY座標</span>
	 * @param aZ
	 *   <span class="lang-en">The Z coordinate of the point A</span>
	 *   <span class="lang-ja">点AのZ座標</span>
	 * @param bX
	 *   <span class="lang-en">The X coordinate of the point B</span>
	 *   <span class="lang-ja">点BのX座標</span>
	 * @param bY
	 *   <span class="lang-en">The Y coordinate of the point B</span>
	 *   <span class="lang-ja">点BのY座標</span>
	 * @param bZ
	 *   <span class="lang-en">The Z coordinate of the point B</span>
	 *   <span class="lang-ja">点BのZ座標</span>
	 * @param cX
	 *   <span class="lang-en">The X coordinate of the point C</span>
	 *   <span class="lang-ja">点CのX座標</span>
	 * @param cY
	 *   <span class="lang-en">The Y coordinate of the point C</span>
	 *   <span class="lang-ja">点CのY座標</span>
	 * @param cZ
	 *   <span class="lang-en">The Z coordinate of the point C</span>
	 *   <span class="lang-ja">点CのZ座標</span>
	 * @param dX
	 *   <span class="lang-en">The X coordinate of the point D</span>
	 *   <span class="lang-ja">点DのX座標</span>
	 * @param dY
	 *   <span class="lang-en">The Y coordinate of the point D</span>
	 *   <span class="lang-ja">点DのY座標</span>
	 * @param dZ
	 *   <span class="lang-en">The Z coordinate of the point D</span>
	 *   <span class="lang-ja">点DのZ座標</span>
	 * @param parameter
	 *   <span class="lang-en">The object storing the drawing parameters</span>
	 *   <span class="lang-ja">描画パラメーターを格納しているオブジェクト</span>
	 */
	public void drawQuadrangle(double aX, double aY, double aZ, double bX, double bY, double bZ, double cX, double cY, double cZ, double dX, double dY, double dZ, RinearnGraph3DDrawingParameter parameter);


	/**
	 * <span class="lang-en">
	 * Draws a text string in the 3D space, with the specified color
	 * </span>
	 * <span class="lang-ja">
	 * 指定された色で, 3D空間中に文字列を描画します
	 * </span>
	 * .
	 * @param x
	 *   <span class="lang-en">The X coordinate of the left edge of the base line</span>
	 *   <span class="lang-ja">ベースライン左端のX座標</span>
	 * @param y
	 *   <span class="lang-en">The Y coordinate of the left edge of the base line</span>
	 *   <span class="lang-ja">ベースライン左端のY座標</span>
	 * @param z
	 *   <span class="lang-en">The Z coordinate of the left edge of the base line</span>
	 *   <span class="lang-ja">ベースライン左端のZ座標</span>
	 * @param text
	 *   <span class="lang-en">The content of the text</span>
	 *   <span class="lang-ja">文字列の内容</span>
	 * @param font
	 *   <span class="lang-en">The font of the text</span>
	 *   <span class="lang-ja">文字列のフォント</span>
	 * @param color
	 *   <span class="lang-en">The color of the text</span>
	 *   <span class="lang-ja">文字列の描画色</span>
	 */
	public void drawText(double x, double y, double z, String text, Font font, Color color);


	/**
	 * <span class="lang-en">
	 * Draws a text string in the 3D space, with the specified parameter settings
	 * </span>
	 * <span class="lang-ja">
	 * 詳細な設定に基づいて, 3D空間中に文字列を描画します
	 * </span>
	 * .
	 * @param x
	 *   <span class="lang-en">The X coordinate of the left edge of the base line</span>
	 *   <span class="lang-ja">ベースライン左端のX座標</span>
	 * @param y
	 *   <span class="lang-en">The Y coordinate of the left edge of the base line</span>
	 *   <span class="lang-ja">ベースライン左端のY座標</span>
	 * @param z
	 *   <span class="lang-en">The Z coordinate of the left edge of the base line</span>
	 *   <span class="lang-ja">ベースライン左端のZ座標</span>
	 * @param text
	 *   <span class="lang-en">The content of the text</span>
	 *   <span class="lang-ja">文字列の内容</span>
	 * @param font
	 *   <span class="lang-en">The font of the text</span>
	 *   <span class="lang-ja">文字列のフォント</span>
	 * @param parameter
	 *   <span class="lang-en">The object storing the drawing parameters</span>
	 *   <span class="lang-ja">描画パラメーターを格納しているオブジェクト</span>
	 */
	public void drawText(double x, double y, double z, String text, Font font, RinearnGraph3DDrawingParameter parameter);


	/**
	 * <span class="lang-en">
	 * Draws the box frame of the graph
	 * </span>
	 * <span class="lang-ja">
	 * グラフのフレーム（外枠）を描画します
	 * </span>
	 * .
	 */
	public void drawFrame();


	/**
	 * <span class="lang-en">
	 * Draws the scale (ticks) of the graph
	 * </span>
	 * <span class="lang-ja">
	 * グラフの目盛りを描画します
	 * </span>
	 * .
	 */
	public void drawScale();


	/**
	 * <span class="lang-en">
	 * Draws the grid lines of the graph
	 * </span>
	 * <span class="lang-ja">
	 * グラフのグリッド線を描画します
	 * </span>
	 * .
	 */
	public void drawGrid();


	/**
	 * <span class="lang-en">
	 * Draws the labels of the axes of the graph
	 * </span>
	 * <span class="lang-ja">
	 * グラフの座標軸ラベルを描画します
	 * </span>
	 * .
	 */
	public void drawLabel();
}
