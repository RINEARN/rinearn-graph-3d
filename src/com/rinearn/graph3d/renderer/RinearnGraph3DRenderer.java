package com.rinearn.graph3d.renderer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;


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
	 * Sets the size of the graph screen
	 * </span>
	 * <span class="lang-ja">
	 * グラフ画面のサイズを設定します
	 * </span>
	 * .
	 * @param screenWidth
	 *   <span class="lang-en">The width (pixels) of the screen</span>
	 *   <span class="lang-ja">グラフ画面の幅（ピクセル単位）</span>
	 * @param screenHeight
	 *   <span class="lang-en">The height (pixels) of the screen</span>
	 *   <span class="lang-ja">グラフ画面の高さ（ピクセル単位）</span>
	 */
	public void setScreenSize(int screenWidth, int screenHeight);


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
