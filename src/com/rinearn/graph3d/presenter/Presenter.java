package com.rinearn.graph3d.presenter;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.renderer.RinearnGraph3DRenderer;
import com.rinearn.graph3d.event.RinearnGraph3DEventDispatcher;


/**
 * The front-end class of "Presenter" layer (com.rinearn.graph3d.presenter package)
 * of RINEARN Graph 3D.
 * 
 * Components in Presenter layer invokes "Model" layer's procedures triggered by user's action on GUI,
 * and updates the graph screen depending on the result.
 * 
 * Also, in addition to the above normal events, this presenter layer handles API requests,
 * through wrapper method defined in RinearnGraph3D class.
 */
public final class Presenter {

	/** The front-end class of "Model" layer, which provides internal logic procedures and so on. */
	private final Model model;

	/** The front-end class of "View" layer, which provides visible part of GUI without event handling. */
	private final View view;

	/** The rendering engine of 3D graphs. */
	private final RinearnGraph3DRenderer renderer;


	/** The event dispatcher, which manages listeners of RinearnGraph3DPlottingEvent and dispatches fired events to them. */
	public final RinearnGraph3DEventDispatcher plottingEventDispatcher;

	/** The loop which performs rendering and updates the screen, on an independent thread. */
	public final RenderingLoop renderingLoop;

	/** The handler of events of the frame of the main window. */
	public final FrameHandler frameHandler;

	/** The handler of events of the graph screen, such as mouse-dragging events for rotate a graph, etc. */
	public final ScreenHandler screenHandler;

	/** The handler of events and API requests related to the menu bar and right click menus. */
	public final MenuHandler menuHandler;

	/** The handler of events and API requests related to UI on the panel at the left side of the screen. */
	public final ScreenSideUIHandler screenSideUIHandler;

	/** The handler of events and API requests for setting ranges. */
	public final RangeSettingHandler rangeSettingHandler;

	/** The handler of events and API requests for setting labels. */
	public final LabelSettingHandler labelSettingHandler;

	/** The handler of events and API requests for setting camera-related parameters. */
	public final CameraSettingHandler cameraSettingHandler;

	/** The handler of events and API requests for setting scale-related parameters. */
	public final ScaleSettingHandler scaleSettingHandler;

	/** The handler of events for lighting parameters. */
	public final LightSettingHandler lightSettingHandler;


	/**
	 * Creates new Presenter layer of RINEARN Graph 3D.
	 * 
	 * @param model The front-end class of Model layer, which provides internal logic procedures and so on.
	 * @param view The front-end class of View layer, which provides visible part of GUI without event handling.
	 * @param renderer The rendering engine of 3D graphs.
	 * @param plottingEventDispatcher The event dispatcher of RinearnGraph3DPlottingEvent.
	 */
	public Presenter(Model model, View view,
			RinearnGraph3DRenderer renderer, RinearnGraph3DEventDispatcher plottingEventDispatcher) {

		this.model = model;
		this.view = view;
		this.renderer = renderer;
		this.plottingEventDispatcher = plottingEventDispatcher;

		// Create a rendering loop/thread, and start it.
		this.renderingLoop = new RenderingLoop(model, view, this, renderer);
		this.renderingLoop.start();

		// Create a handler of events of the frame of the main window.
		this.frameHandler = new FrameHandler(model, view, this);

		// Create a handler of events of the graph screen, handling mouse-dragging events for rotate a graph, etc.
		this.screenHandler = new ScreenHandler(model, view, this, renderer);

		// Create handlers for various events and API requests.
		this.menuHandler = new MenuHandler(model, view, this);
		this.screenSideUIHandler = new ScreenSideUIHandler(model, view, this);
		this.rangeSettingHandler = new RangeSettingHandler(model, view, this);
		this.labelSettingHandler = new LabelSettingHandler(model, view, this);
		this.cameraSettingHandler = new CameraSettingHandler(model, view, this);
		this.scaleSettingHandler = new ScaleSettingHandler(model, view, this);
		this.lightSettingHandler = new LightSettingHandler(model, view, this);
	}


	/**
	 * Propagates the current configuration stored in Model layer, to the entire application.
	 */
	public synchronized void propagateConfiguration() {
		RinearnGraph3DConfiguration config = this.model.getConfiguration();
		this.view.configure(config);
		this.renderer.configure(config);
	}


	/**
	 * Plots all contents composing the graph again (replot).
	 */
	public synchronized void plot() {

		// Clear all currently drawn contents registered to the renderer.
		this.renderer.clear();

		// Draw basic components (outer frame, scale ticks, etc.) of the graph.
		this.renderer.drawScale();
		this.renderer.drawLabel();
		this.renderer.drawGrid();
		this.renderer.drawFrame();

		// -----
		// Future: Draw other elements here
		// -----

		// Call "plottingRequested" methods of the registered event listeners of RinearnGraph3DPlottingEvent.
		this.plottingEventDispatcher.firePlottingRequested();

		// Call "plottingFinished" methods of the registered event listeners of RinearnGraph3DPlottingEvent.
		this.plottingEventDispatcher.firePlottingFinished();
		// ↑ これ render 後に呼ぶべき？ 前に呼ぶべき？
		//
		//    -> render まで全て終わった後にスクリーン上に（2Dで）何か描きたいみたいな場面を考えたら、後で呼ぶべきでは？
		//
		//       -> でも Ver.5.6 だと前に呼んでる。まあそっちはスクリーン上に2Dで何か描く機能無いので気にしなくていいかもだけど。
		//
		//           -> そういう処理は2D描画の foregroundRenderer とか backgroundRenderer とか用意して前景/背景レイヤーを追加する可能性がある。
		//              もし将来的にそうした場合、合成はたぶん render 時に行うので、render 後だと逆に描けなくなってしまう。
		//              そこまで拡張考えたら render 前の方がいいかと思う。
		//
		//              -> render前に行うやつなら firePlottingRequested 一本でいいんじゃないの？
		//
		//                 -> 他のリスナーの firePlottingRequested が終わったのを待ってから行いたい処理とかがなんかあるかも、
		//                    …と思って作った記憶があるが、その時点で具体的に何かは想定していなかった記憶もある。なんか後々でありそうみたいな。
		//                    インターフェース決めると後で追加したら互換崩れるし、念のため宣言しといたという感じだったかと。
		//
		//                    -> まあ replot は頻繁に発生するわけではないので、requested の時点でなんかリソース確保して描いて、
		//                       後続のリスナーもそのリソース共有して使って、
		//                       んで finished の時点で解放する、とかの使い道が全く無いわけではなさそうな。
		//
		//                       -> ああなんか機器やネットワーク上のレイテンシ大きいやつとかに connection する場合はありそう、いかにも
		//
		//              -> そもそも仮に render 後にスクリーンのグラフィックスコンテキスト引っ張ってきて画面上に何か描いたところで
		//                 画面更新ループが察知できないから表示はされないし意味ないような
		//
		//                 -> いや、レンダラーの casScreenUpdated で画面更新フラグ立てれば拾われる。
		//                    むしろ要らんかもと思いつつ念のため CAS 操作可能なAPIにした意味が生じる珍しい例かも。
		//
		//           -> ああそうだ、仮に render 後の2D描画が可能で便利でも、画面をマウス操作した瞬間に再び render が走って描画内容がすぐ消えるんだ。
		//              Ver.5.6 で最初にそう実装してすぐ描画内容消えて、意味ないよなあってなって結局 render 前にしたんだ。思い出した。
		//
		//              で、そういうのをサポートするなら render のタイミングをイベントとして拾う RinearnGraph3DRenderingListener を作って
		//              そっちのメソッドを fire すべきで、でもそれはスクリーン前景/背景への2D描画の仕様を固めないといけないから未実装、
		//              みたいな。そういう感じだったかと。
		//
		// とりあえず render 前に行って、後でまた再検討する（RenderingListener とか作る方向も含めて）


		// Render the re-plotted contents on the screen.
		this.renderer.render();
	}
}
