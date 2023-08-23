package com.rinearn.graph3d;

import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.renderer.RinearnGraph3DRenderer;
import com.rinearn.graph3d.renderer.simple.SimpleRenderer;

import java.awt.Image;


/**
 * <spac lass="lang-en">
 * The class at the top layer of the implementation of RINEARN Graph 3D,
 * and also plays the role of the front-end of
 * API for controlling RINEARN Graph 3D from user's code
 * </span>
 * <span class="lang-ja">
 * リニアングラフ3Dの実装の最表層クラスであり、
 * また、コードによってリニアングラフ3Dを制御する際のAPIのフロントエンドも担います
 * </span>
 * .
 */
public class RinearnGraph3D {

	/** The front-end class of "View" layer, which provides visible part of GUI without event handling. */
	private volatile View view;

	/** The rendering engine of 3D graphs. */
	private volatile RinearnGraph3DRenderer renderer;


	/**
	 * <span class="lang-en">
	 * Launch a new RINEARN Graph 3D window
	 * </span>
	 * <span class="lang-ja">
	 * 新しいリニアングラフ3Dの画面を起動します
	 * </span>
	 */
	public RinearnGraph3D() {

		// Create "View" layer, which provides visible part of GUI without event handling.
		this.view = new View();

		// Create a rendering engine of 3D graphs.
		this.renderer = new SimpleRenderer(
				view.mainWindow.DEFAULT_SCREEN_WIDTH, view.mainWindow.DEFAULT_SCREEN_HEIGHT
		);

		// Set the reference to the rendered image of the renderer,
		// to the graph screen of the window for displaying it.
		Image screenImage = this.renderer.getScreenImage();
		this.view.mainWindow.setScreenImage(screenImage);

		// First rendering and repainting.
		this.renderer.drawScale();
		this.renderer.drawGrid();
		this.renderer.drawFrame();
		this.renderer.render();
		this.view.mainWindow.repaintScreen();

		// Show the window.
		view.mainWindow.setWindowVisible(true);
	}


	/**
	 * <span class="lang-en">
	 * Returns the 3D renderer, which is being used for rendering the graph image in this instance
	 * </span>
	 * <span class="lang-ja">
	 * グラフ画面を描画するのに使用されている3D描画エンジン（レンダラー）を返します
	 * </span>
	 * .
	 * @return
	 *   <span class="lang-en">The 3D renderer</span>
	 *   <span class="lang-ja">3D描画エンジン（レンダラー）</span>
	 */
	public synchronized RinearnGraph3DRenderer getRenderer() {
		return this.renderer;
	}
}