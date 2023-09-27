package com.rinearn.graph3d;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.renderer.RinearnGraph3DRenderer;
import com.rinearn.graph3d.renderer.simple.SimpleRenderer;
import com.rinearn.graph3d.event.RinearnGraph3DEventDispatcher;
import com.rinearn.graph3d.event.RinearnGraph3DPlottingListener;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.CameraConfiguration;

import java.awt.Image;
import java.awt.Dimension;
import java.math.BigDecimal;


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

	/** The front-end class of "Model" layer, which provides internal logic procedures and so on. */
	private final Model model;

	/** The front-end class of "View" layer, which provides visible part of GUI without event handling. */
	private final View view;

	/** The front-end class of "Presenter" layer, which invokes Model's procedures triggered by user's action on GUI. */
	private final Presenter presenter;

	/** The rendering engine of 3D graphs. */
	private final RinearnGraph3DRenderer renderer;


	/**
	 * <span class="lang-en">
	 * Launch a new RINEARN Graph 3D window
	 * </span>
	 * <span class="lang-ja">
	 * 新しいリニアングラフ3Dの画面を起動します
	 * </span>
	 */
	public RinearnGraph3D() {

		// Create the configuration container storing the default values for all configuration parameters.
		RinearnGraph3DConfiguration configuration = RinearnGraph3DConfiguration.createDefaultConfiguration();

		// !!! NOTE !!!
		//
		// ↑で config 生成して全体で参照共有しようとしたけど、良く考えたらレンダラーでも共有するのはまずい。
		// RinearnGraph3D と RinearnGraph3DRenderer はAPIを提供していて、それぞれ独立に外部から conifigure され得るが、
		// それらは渡されたconfigコンテナの中身を、自信の保持しているコンテナにマージする挙動になっている。
		// （利便性から、APIでは部分設定だけが入ったconfigコンテナを渡して反映できるようにしたいため。）
		//
		// で、それぞれの内部に保持している config コンテナの参照が同じだと、
		// 例えば RinearnGraph3DRenderer にAPI経由でマージされた部分設定が、上層の RinearnGraph3D にも反映されてしまう。
		// それは明らかに意図と異なるし、直感的な階層構造にも反する。
		//
		// つまりレンダラーはレンダラーのみで独立に config コンテナを持っているべき。
		// コンテナの参照を統一してはいけない。
		//
		// -> じゃあ数個前のコミットは結局元の方が合ってたわけなので、後で部分的に戻さないといけない。
		//    また後々で戻すべき
		//
		// -> とりあえずレンダラーとの共有はやめた。でもまだレンダラーの中のほうでリファクタが要りそう。
		//
		// !!! NOTE !!!

		// Create "Model" layer, which provides internal logic procedures and so on.
		this.model = new Model(configuration);

		// !!! NOTE !!!
		// 結局それなら ↑ も内部で生成してればよかったのでは？ ここで生成して渡さんでも。一昨日のままでよかった気が。
		//
		// -> これはまあ、「アプリのconfigはModelが保持/管理しますよ」っていう役割を考えたら、
		//    際表層で生成してコンストラクタで渡してても変ではないと思う。
		//    両者の違いは、「生成までがModelの役割に含まれるか」どうかというデザイン上の違いで。
		//
		//    例えば、仮にこの RinearnGraph3D のコンストラクタに、引数として config 渡せるようなやつがあったら、
		//    生成は Model の役割にまとめ込めなくなるし、上のような感じで投げるのが自然だし。
		//    それ考えたら上のデザインでもまあいい。
		//
		// !!! NOTE !!!

		// Create "View" layer, which provides visible part of GUI without event handling.
		this.view = new View();

		// Create a rendering engine of 3D graphs.
		this.renderer = new SimpleRenderer();

		// !!! NOTE !!!
		// ↑ここで引数で渡して参照共有してるのは確実にやめるべき。
		//   -> やめた。
		// !!! NOTE !!!


		// Set the reference to the rendered image of the renderer,
		// to the graph screen of the window for displaying it.
		Image screenImage = this.renderer.getScreenImage();
		this.view.mainWindow.setScreenImage(screenImage);

		// First rendering and repainting.
		this.renderer.drawScale();
		this.renderer.drawLabel();
		this.renderer.drawGrid();
		this.renderer.drawFrame();
		this.renderer.render();
		//this.view.mainWindow.repaintScreen();

		// Show the window.
		this.view.mainWindow.setWindowVisible(true);

		// Creates the event dispatcher, which manages listeners of RinearnGraph3DPlottingEvent and dispatches fired events.
		// It is managed in Presenter, but instantiate here to specify this RinearnGraph3D instance as the event source.
		// And then, pass it to the argument of the Presenter's constructor.
		RinearnGraph3DEventDispatcher plottingEventDispatcher = new RinearnGraph3DEventDispatcher(this);

		// Create "Presenter" layer which invokes Model's procedures triggered by user's action on GUI.
		// (The rendering loop is also running in this Presenter layer.)
		this.presenter = new Presenter(this.model, this.view, this.renderer, plottingEventDispatcher);

		// Propagate the configuration stored in Model, to the entire application.
		this.presenter.propagateConfiguration();
	}


	/**
	 * <span class="lang-en">
	 * Configures detailed setting parameters, by the container storing them
	 * </span>
	 * <span class="lang-ja">
	 * 設定値を格納するコンテナを渡して、詳細な設定パラメータを設定します
	 * <span class="lang-en">
	 * .
	 * @param configuration
	 *   <span class="lang-en">
	 *   The container storing configuration values.
	 *   </span>
	 *   <span class="lang-ja">
	 *   設定値を格納しているコンテナ
	 *   </span>
	 */
	public synchronized void configure(RinearnGraph3DConfiguration configuration) {

		// RinearnGraph3DConfiguration is a container of subpart configurations.
		// Some of them are set and others are not set,
		// so extract only stored subpart configurations in the argument "configuration"
		// and merge them to the configuration of this application, which is stored in Model.
		this.model.getConfiguration().merge(configuration);

		// Propagate the above modified configuration (stored in Model) to the entire application.
		this.presenter.propagateConfiguration();
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


	/**
	 * <span class="lang-ja">
	 * グラフ表示ウィンドウの位置とサイズを設定します
	 * </span>
	 * <span class="lang-en">
	 * Sets the location and the size of the graph window
	 * </span>
	 * .
	 * <span class="lang-ja">
	 * 変更後のウィンドウのサイズに合わせて, グラフスクリーンのサイズも変更されます. 
	 * なお, グラフスクリーンのサイズは, ウィンドウのサイズよりも若干小さくなり, 
	 * その差はバージョンや環境に依存する事に注意してください. 
	 * 画像出力用途などで, グラフスクリーンのサイズを明示的に指定したい場合は, 代わりに
	 * {@link RinearnGraph3D#setScreenSize setScreenSize} メソッドを使用してください. 
	 * </span>
	 * 
	 * <span class="lang-en">
	 * Note that, the graph screen will be resized automatically, 
	 * corresponding with the size of the modified graph window.
	 * The size of the graph screen will be little smaller than the size of the graph window, 
	 * and the difference between them depends on your environment and the version of RINEARN Graph 3D.
	 * When you want to set the size of the graph screen precisely, 
	 * use {@link RinearnGraph3D#setScreenSize setScreenSize} method instead.
	 * </span>
	 *
	 * @param x
	 *   <span class="lang-ja">グラフウィンドウ左上のX座標</span>
	 *   <span class="lang-en">The X-coordinate of the left-top edge of the graph window</span>
	 * @param y
	 *   <span class="lang-ja">グラフウィンドウ左上のY座標</span>
	 *   <span class="lang-en">The Y-coordinate of the left-top edge of the graph window</span>
	 * @param width
	 *   <span class="lang-ja">グラフウィンドウの幅</span>
	 *   <span class="lang-en">The width the graph window</span>
	 * @param height
	 *   <span class="lang-ja">グラフウィンドウの高さ</span>
	 *   <span class="lang-en">The height the graph window</span>
	 */
	public synchronized void setWindowBounds(int x, int y, int width, int height) {
		this.presenter.frameHandler.setWindowBounds(x, y, width, height);
		this.presenter.screenHandler.updateScreenSize();
	}


	/**
	 * <span class="lang-ja">
	 * グラフスクリーンのサイズを設定します
	 * </span>
	 * <span class="lang-en">
	 * Sets the size of the graph screen
	 * </span>
	 * .
	 * <span class="lang-ja">
	 * 変更後のスクリーンサイズに合わせて, グラフウィンドウのサイズも自動で変更されます.
	 * グラフウィンドウのサイズを明示的に指定したい場合は, 代わりに
	 * {@link RinearnGraph3D#setWindowBounds setWindowBounds} メソッドを使用してください. 
	 * </span>
	 * 
	 * <span class="lang-en">
	 * Note that, the graph window will be resized automatically, 
	 * corresponding with the size of the modified graph screen.
	 * When you want to set the size of the graph window explicitly, 
	 * use {@link RinearnGraph3D#setWindowBounds setWindowBounds} method instead.
	 * </span>
	 *
	 * @param width
	 *   <span class="lang-ja">グラフスクリーンの幅</span>
	 *   <span class="lang-en">The width the graph screen</span>
	 * @param height
	 *   <span class="lang-ja">グラフスクリーンの高さ</span>
	 *   <span class="lang-en">The height the graph screen</span>
	 */
	public synchronized void setScreenSize (int width, int height) {
		this.presenter.frameHandler.setScreenSize(width, height);
		this.presenter.screenHandler.setScreenSize(width, height);
	}


	/**
	 * Sets the range of X axis.
	 * 
	 * @param min The minimum coordinate value of X axis.
	 * @param max The maximum coordinate value of X axis.
	 */
	public synchronized void setXRange(double min, double max) {
		this.presenter.rangeSettingHandler.setXRange(new BigDecimal(min), new BigDecimal(max));
	}

	/**
	 * Sets the range of X axis.
	 * 
	 * @param min The minimum coordinate value of X axis.
	 * @param max The maximum coordinate value of X axis.
	 */
	public synchronized void setXRange(BigDecimal min, BigDecimal max) {
		this.presenter.rangeSettingHandler.setXRange(min, max);
	}

	/**
	 * Turns on/off the auto-ranging feature for X axis.
	 * 
	 * @param enabled Specify true/false for turning on/off (the default is on).
	 */
	public synchronized void setXAutoRangingEnabled(boolean enabled) {
		this.presenter.rangeSettingHandler.setXAutoRangingEnabled(enabled);
	}


	/**
	 * Sets the range of Y axis.
	 * 
	 * @param min The minimum coordinate value of Y axis.
	 * @param max The maximum coordinate value of Y axis.
	 */
	public synchronized void setYRange(double min, double max) {
		this.presenter.rangeSettingHandler.setYRange(new BigDecimal(min), new BigDecimal(max));
	}

	/**
	 * Sets the range of Y axis.
	 * 
	 * @param min The minimum coordinate value of Y axis.
	 * @param max The maximum coordinate value of Y axis.
	 */
	public synchronized void setYRange(BigDecimal min, BigDecimal max) {
		this.presenter.rangeSettingHandler.setYRange(min, max);
	}

	/**
	 * Turns on/off the auto-ranging feature for Y axis.
	 * 
	 * @param enabled Specify true/false for turning on/off (the default is on).
	 */
	public synchronized void setYAutoRangingEnabled(boolean enabled) {
		this.presenter.rangeSettingHandler.setYAutoRangingEnabled(enabled);
	}


	/**
	 * Sets the range of Z axis.
	 * 
	 * @param min The minimum coordinate value of Z axis.
	 * @param max The maximum coordinate value of Z axis.
	 */
	public synchronized void setZRange(double min, double max) {
		this.presenter.rangeSettingHandler.setZRange(new BigDecimal(min), new BigDecimal(max));
	}

	/**
	 * Sets the range of Z axis.
	 * 
	 * @param min The minimum coordinate value of Z axis.
	 * @param max The maximum coordinate value of Z axis.
	 */
	public synchronized void setZRange(BigDecimal min, BigDecimal max) {
		this.presenter.rangeSettingHandler.setZRange(min, max);
	}

	/**
	 * Turns on/off the auto-ranging feature for Z axis.
	 * 
	 * @param enabled Specify true/false for turning on/off (the default is on).
	 */
	public synchronized void setZAutoRangingEnabled(boolean enabled) {
		this.presenter.rangeSettingHandler.setZAutoRangingEnabled(enabled);
	}


	/**
	 * Set the displayed text of X axis label.
	 * 
	 * @param xLabel The text of X axis label.
	 */
	public synchronized void setXLabel(String xLabel) {
		this.presenter.labelSettingHandler.setXLabel(xLabel);
	}

	/**
	 * Set the displayed text of Y axis label.
	 * 
	 * @param yLabel The text of Y axis label.
	 */
	public synchronized void setYLabel(String yLabel) {
		this.presenter.labelSettingHandler.setYLabel(yLabel);
	}

	/**
	 * Set the displayed text of Z axis label.
	 * 
	 * @param zLabel The text of Z axis label.
	 */
	public synchronized void setZLabel(String zLabel) {
		this.presenter.labelSettingHandler.setZLabel(zLabel);
	}


 	/**
 	 * Sets the distance between the viewpoint and the origin of the graph.
 	 * 
 	 * @param distance The distance between the viewpoint and the origin of the graph.
 	 */
	public void setCameraDistance(double distance) {
		this.presenter.cameraSettingHandler.setCameraDistance(distance);
	}

	/**
	 * Sets the magnification of the graph screen.
	 * 
	 * @param magnification The magnification of the graph screen.
	 */
	public void setCameraMagnification(double magnification) {
		this.presenter.cameraSettingHandler.setCameraMagnification(magnification);
	}


	/**
	 * Sets the camera angle by two angular parameters, regarding X axis as the zenith axis.
	 * 
	 * The "screw angle" is always set to zero by this method.
	 * To set the screw angle together, use the overload of this method, having 3-arguments instead.
	 * 
	 * @param horizontalAngle The horizontal angle, which is the rotation angle of the camera's location around the zenith axis.
	 * @param verticalAngle The vertical angle, which is the angle between the zenith axis and the direction toward the camera.
	 */
	public synchronized void setXZenithCameraAngle(double horizontalAngle, double verticalAngle) {
		this.presenter.cameraSettingHandler.setZenithCameraAngle(
				horizontalAngle, verticalAngle, 0.0, CameraConfiguration.AngleMode.X_ZENITH
		);
	}

	/**
	 * Sets the camera angle by three angular parameters, regarding X axis as the zenith axis.
	 * 
	 * @param horizontalAngle The horizontal angle, which is the rotation angle of the camera's location around the zenith axis.
	 * @param verticalAngle The vertical angle, which is the angle between the zenith axis and the direction toward the camera.
	 * @param screwAngle The screw angle, which is the rotation angle of the camera itself (not location) around the screen center.
	 */
	public synchronized void setXZenithCameraAngle(double horizontalAngle, double verticalAngle, double screwAngle) {
		this.presenter.cameraSettingHandler.setZenithCameraAngle(
				horizontalAngle, verticalAngle, screwAngle, CameraConfiguration.AngleMode.X_ZENITH
		);
	}


	/**
	 * Sets the camera angle by two angular parameters, regarding Y axis as the zenith axis.
	 * 
	 * The "screw angle" is always set to zero by this method.
	 * To set the screw angle together, use the overload of this method, having 3-arguments instead.
	 * 
	 * @param horizontalAngle The horizontal angle, which is the rotation angle of the camera's location around the zenith axis.
	 * @param verticalAngle The vertical angle, which is the angle between the zenith axis and the direction toward the camera.
	 */
	public synchronized void setYZenithCameraAngle(double horizontalAngle, double verticalAngle) {
		this.presenter.cameraSettingHandler.setZenithCameraAngle(
				horizontalAngle, verticalAngle, 0.0, CameraConfiguration.AngleMode.Y_ZENITH
		);
	}

	/**
	 * Sets the camera angle by three angular parameters, regarding Y axis as the zenith axis.
	 * 
	 * @param horizontalAngle The horizontal angle, which is the rotation angle of the camera's location around the zenith axis.
	 * @param verticalAngle The vertical angle, which is the angle between the zenith axis and the direction toward the camera.
	 * @param screwAngle The screw angle, which is the rotation angle of the camera itself (not location) around the screen center.
	 */
	public synchronized void setYZenithCameraAngle(double horizontalAngle, double verticalAngle, double screwAngle) {
		this.presenter.cameraSettingHandler.setZenithCameraAngle(
				horizontalAngle, verticalAngle, screwAngle, CameraConfiguration.AngleMode.Y_ZENITH
		);
	}


	/**
	 * Sets the camera angle by two angular parameters, regarding Z axis as the zenith axis.
	 * 
	 * The "screw angle" is always set to zero by this method.
	 * To set the screw angle together, use the overload of this method, having 3-arguments instead.
	 * 
	 * @param horizontalAngle The horizontal angle, which is the rotation angle of the camera's location around the zenith axis.
	 * @param verticalAngle The vertical angle, which is the angle between the zenith axis and the direction toward the camera.
	 */
	public synchronized void setZZenithCameraAngle(double horizontalAngle, double verticalAngle) {
		this.presenter.cameraSettingHandler.setZenithCameraAngle(
				horizontalAngle, verticalAngle, 0.0, CameraConfiguration.AngleMode.Z_ZENITH
		);
	}

	/**
	 * Sets the camera angle by three angular parameters, regarding Z axis as the zenith axis.
	 * 
	 * @param horizontalAngle The horizontal angle, which is the rotation angle of the camera's location around the zenith axis.
	 * @param verticalAngle The vertical angle, which is the angle between the zenith axis and the direction toward the camera.
	 * @param screwAngle The screw angle, which is the rotation angle of the camera itself (not location) around the screen center.
	 */
	public synchronized void setZZenithCameraAngle(double horizontalAngle, double verticalAngle, double screwAngle) {
		this.presenter.cameraSettingHandler.setZenithCameraAngle(
				horizontalAngle, verticalAngle, screwAngle, CameraConfiguration.AngleMode.Z_ZENITH
		);
	}


	/**
	 * Sets the coordinates (locations) and the labels (displayed text) of the scale ticks on X axis.
	 * 
	 * @param tickCoordinates The coordinates of the scale ticks.
	 * @param tickLabels The labels of the scale ticks.
	 */
	public synchronized void setXTicks(BigDecimal[] tickCoordinates, String[] tickLabels) {
		this.presenter.scaleSettingHandler.setXTicks(tickCoordinates, tickLabels);
	}

	/**
	 * Sets the coordinates (locations) and the labels (displayed text) of the scale ticks on X axis.
	 * 
	 * @param tickCoordinates The coordinates of the scale ticks.
	 * @param tickLabels The labels of the scale ticks.
	 */
	public synchronized void setXTicks(double[] tickCoordinates, String[] tickLabels) {
		BigDecimal[] bdTickCoordinates = new BigDecimal[tickCoordinates.length];
		for (int itick=0; itick<tickCoordinates.length; itick++) {
			bdTickCoordinates[itick] = new BigDecimal(tickCoordinates[itick]);
		}
		this.presenter.scaleSettingHandler.setXTicks(bdTickCoordinates, tickLabels);
	}


	/**
	 * Sets the coordinates (locations) and the labels (displayed text) of the scale ticks on Y axis.
	 * 
	 * @param tickCoordinates The coordinates of the scale ticks.
	 * @param tickLabels The labels of the scale ticks.
	 */
	public synchronized void setYTicks(BigDecimal[] tickCoordinates, String[] tickLabels) {
		this.presenter.scaleSettingHandler.setYTicks(tickCoordinates, tickLabels);
	}

	/**
	 * Sets the coordinates (locations) and the labels (displayed text) of the scale ticks on Y axis.
	 * 
	 * @param tickCoordinates The coordinates of the scale ticks.
	 * @param tickLabels The labels of the scale ticks.
	 */
	public synchronized void setYTicks(double[] tickCoordinates, String[] tickLabels) {
		BigDecimal[] bdTickCoordinates = new BigDecimal[tickCoordinates.length];
		for (int itick=0; itick<tickCoordinates.length; itick++) {
			bdTickCoordinates[itick] = new BigDecimal(tickCoordinates[itick]);
		}
		this.presenter.scaleSettingHandler.setYTicks(bdTickCoordinates, tickLabels);
	}


	/**
	 * Sets the coordinates (locations) and the labels (displayed text) of the scale ticks on Z axis.
	 * 
	 * @param tickCoordinates The coordinates of the scale ticks.
	 * @param tickLabels The labels of the scale ticks.
	 */
	public synchronized void setZTicks(BigDecimal[] tickCoordinates, String[] tickLabels) {
		this.presenter.scaleSettingHandler.setZTicks(tickCoordinates, tickLabels);
	}

	/**
	 * Sets the coordinates (locations) and the labels (displayed text) of the scale ticks on Z axis.
	 * 
	 * @param tickCoordinates The coordinates of the scale ticks.
	 * @param tickLabels The labels of the scale ticks.
	 */
	public synchronized void setZTicks(double[] tickCoordinates, String[] tickLabels) {
		BigDecimal[] bdTickCoordinates = new BigDecimal[tickCoordinates.length];
		for (int itick=0; itick<tickCoordinates.length; itick++) {
			bdTickCoordinates[itick] = new BigDecimal(tickCoordinates[itick]);
		}
		this.presenter.scaleSettingHandler.setZTicks(bdTickCoordinates, tickLabels);
	}


	/**
	 * Sets the visibility of the menu bar and the right click menus.
	 * 
	 * @param visible Specify true for showing the menu bar and the right click menus.
	 */
	public synchronized void setMenuVisible(boolean visible) {
		presenter.menuHandler.setMenuVisible(visible);
	}


	/**
	 * Sets the visibility of the UI-panel at the left side of the screen.
	 * 
	 * @param visible Specify true for showing the UI-panel at the left side of the screen.
	 */
	public synchronized void setScreenSideUIVisible(boolean visible) {
		presenter.screenSideUIHandler.setScreenSideUIVisible(visible);
	}


	/**
	 * <span class="lang-ja">
	 * 再プロットが必要になった際に発行される RinearnGraph3DPlottingEvent を受け取る, 
	 * RinearnGraph3DPlottingListener インタフェースを実装したイベントリスナーを追加登録します
	 * </span>
	 * <span class="lang-en">
	 * Adds the event listener implementing RinearnGraph3DPlottingListener, 
	 * for receiving RinearnGraph3DPlottingEvent which occurs when plotting/replotting is required.
	 * </span>
	 * .
	 * @param plottingListener 
	 *   <span class="lang-ja">登録するイベントリスナー</span>
	 *   <span class="lang-en">The event listener to be added</span>
	 */
	public void addPlottingListener(RinearnGraph3DPlottingListener plottingListener) {
		this.presenter.plottingEventDispatcher.addPlottingListener(plottingListener);
	}


	// ↓これ外部APIとして出す必用ある？ 出したら戻せないが。
	//    このアプリは設定更新したら内部で replot される挙動なので、不要かもしれん。
	//    要るようになった時に追加してもいいような。要検討
	//
	//    -> 思いついた。PlottingEvent のリスナーを作って登録した時、
	//       そのままでは次の再プロットタイミングまで呼ばれないから、
	//       初回描画的な意味で今すぐ実行させたい時とか。
	//
	//       現状はそのために最初の一回（イベント発動とは別に）描画処理を走らせるコードを書く必用があるが、
	//       その代わりにplot呼べばイベントとして勝手に走って描かれるので、そうした方がたぶん読みやすくなる。
	//
	//       つまりリスナー登録＆plot()で初回描画のコンボで使う。
	/**
	 * Plots all contents composing the graph again (replot).
	 */
	public synchronized void plot() {
		this.presenter.plot();
	}
}
