package com.rinearn.graph3d.presenter;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.model.dataseries.MathDataSeries;
import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.renderer.RinearnGraph3DRenderer;
import com.rinearn.graph3d.event.RinearnGraph3DEventDispatcher;

import com.rinearn.graph3d.presenter.handler.CameraSettingHandler;
import com.rinearn.graph3d.presenter.handler.FontSettingHandler;
import com.rinearn.graph3d.presenter.handler.FrameHandler;
import com.rinearn.graph3d.presenter.handler.LabelSettingHandler;
import com.rinearn.graph3d.presenter.handler.LightSettingHandler;
import com.rinearn.graph3d.presenter.handler.MenuHandler;
import com.rinearn.graph3d.presenter.handler.RangeSettingHandler;
import com.rinearn.graph3d.presenter.handler.ScaleSettingHandler;
import com.rinearn.graph3d.presenter.handler.ScreenHandler;
import com.rinearn.graph3d.presenter.handler.ScreenSideUIHandler;
import com.rinearn.graph3d.presenter.handler.ZxyMathHandler;
import com.rinearn.graph3d.presenter.plotter.PointPlotter;
import com.rinearn.graph3d.presenter.plotter.LinePlotter;
import com.rinearn.graph3d.presenter.plotter.MeshPlotter;
import com.rinearn.graph3d.presenter.plotter.MembranePlotter;

import org.vcssl.nano.VnanoException;

import java.util.List;
import javax.swing.JOptionPane;



// !!! NOTE !!!
//
// ・なんかやっぱりAPIリクエストも全てイベントディスパッチスレッド上で捌くようにした方が結局いいかも。
// 
//   APIも通常イベントも共にconfigコンテナ操作 -> 全体伝搬のフローで統一したとしても、
//   コンテナ操作から全体伝搬までがトランザクション的にアトミックでないと競合で不整合状態になるケースあり得るでしょ。
//   コンテナパラメータの操作をアトミックにしていたとしても、伝搬まで包んでないと不意のタイミングで伝搬し得るでしょ。
// 
//   んでそこまでアトミックにして大げさな更新伝搬構造を組むくらいなら
//   APIリクエストと通常イベントを同じスレッドでシリアルに処理した方が簡単だしよくあるパターンだしずっとよさそうな。
// 
//   無駄なラグは増えるけどどうせView層の更新を伴わない config 更新なんて稀なのでどうせ大抵どっかでラグ入るわけで。
//
//   -> 確かにそうすべき。↑を踏まえて ScreenHandler 改修してたらやっぱり確かにその通りだと思う。
//
//   -> あとこのPresenterと各Handlerの eventHandlingEnabled setter/getter もイベントディスパッチスレッドで処理すべきか。
//      このフラグが参照されて効くのはイベントハンドラなので、
//      操作リクエストがハンドラとシリアルに処理される事を保証できた方がたぶんいい。と思うけどどうだろ
//
//      -> 固まらん？ 固まらんなら多分そのほうがいい絶対
//
//      -> いやしかしイベントスレッドの負担を重くするってのは本来は教科書的にはNGパターンでしょ。
//
//         -> 確かにUIとイベント処理の観点だけで見たら
//            雑で重くてダメな実装例みたいになるしタイムアウト食らうプラットフォームもあるけど、
//            通常UIイベントとAPIリクエストが完全にバラバラに降って来る場合は
//            少なくともどっかでシリアル化はやっぱり必須だと思う。
//
//            んでそのための枠組みを組むのがベストなんだろうけど、
//            ほどほどの負荷のやつならイベントディスパッチスレッドのキューに詰むのが一番シンプルで。
//            あんまりそこの流れを複雑にしたくないので、軽いやつならそっちの方がバランスいい気もする。
//
//            極端に重いやつは非同期化して別スレッド処理にする必用あるけど、
//            それはそのための仕組みがAPIに既にあるので、そういうやつだけそっちのレーンで処理するとか。
//
//            -> しかし大半のイベント処理は終了を明示的に待つ必要はないので、
//               その内部の処理とAPIリクエストが一つのスレッドでシリアルに処理されていればそれでよくて、
//               それにイベントディスパッチスレッドを使うかどうかってのはまた独立な選択でしょ。
//
//               イベントディスパッチスレッドはディスパッチのためのスレッドなんだから。
//               中身の処理をシリアルにやるためのスレッドじゃないんだから。
//
//               -> まあ確かに。別スレッドとシリアル処理用のキューくらいは別途組んでもいいかもしれない。
//
//                  -> でも結局 Swing の操作の大半がイベントスレッド上でやる必用あるから
//                     そっちの方向だと至る所で SwingUtilities.invokeAndWait だらけになるよ絶対。
//                     後戻りできなくなってから要らん事せんかったらよかったって思いそう。仮に教科書的にはどうあっても。
//
//                     config コンテナのパラメータ書き換えて伝搬させるくらいのやつならイベントスレッドでいいんでは。
//                     ヘビーデータの plot とかを、setAsyncPlottingEnabled(true) 指定されてる時だけ別スレッドのキューに詰む、
//                     くらいが妥協の落とし所では。 Ver.5.6 通り。
//
//    -> 上記、とりあえずイベントディスパッチスレッド案に改修してみたのでしばらく様子を見る
//       後々でまた再検討
//
// !!! NOTE !!!


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

	/** The handler of events for setting fonts. */
	public final FontSettingHandler fontSettingHandler;

	/** The handler of events and API requests for setting camera-related parameters. */
	public final CameraSettingHandler cameraSettingHandler;

	/** The handler of events and API requests for setting scale-related parameters. */
	public final ScaleSettingHandler scaleSettingHandler;

	/** The handler of events for lighting parameters. */
	public final LightSettingHandler lightSettingHandler;

	/** The handler of events and API requests for plotting math expressions of "z(x,y)" form. */
	public final ZxyMathHandler zxyMathHandler;

	/** The flag for turning on/off the event handling feature of subcomponents in this instance. */
	private volatile boolean eventHandlingEnabled = true;


	/** The plotter to plot points. */
	public final PointPlotter pointPlotter;

	/** The plotter to plot lines. */
	public final LinePlotter linePlotter;

	/** The plotter to plot meshes. */
	public final MeshPlotter meshPlotter;

	/** The plotter to plot membranes. */
	public final MembranePlotter membranePlotter;


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
		this.screenHandler = new ScreenHandler(model, view, this);

		// Create handlers for various events and API requests.
		this.menuHandler = new MenuHandler(model, view, this);
		this.screenSideUIHandler = new ScreenSideUIHandler(model, view, this);
		this.rangeSettingHandler = new RangeSettingHandler(model, view, this);
		this.labelSettingHandler = new LabelSettingHandler(model, view, this);
		this.fontSettingHandler = new FontSettingHandler(model, view, this);
		this.cameraSettingHandler = new CameraSettingHandler(model, view, this);
		this.scaleSettingHandler = new ScaleSettingHandler(model, view, this);
		this.lightSettingHandler = new LightSettingHandler(model, view, this);
		this.zxyMathHandler = new ZxyMathHandler(model, view, this);

		// Create "plotter"s, which perform plottings/re-plottings in event-driven flow.
		this.pointPlotter = new PointPlotter(model, view, this, renderer);
		this.plottingEventDispatcher.addPlottingListener(this.pointPlotter);
		this.linePlotter = new LinePlotter(model, view, this, renderer);
		this.plottingEventDispatcher.addPlottingListener(this.linePlotter);
		this.meshPlotter = new MeshPlotter(model, view, this, renderer);
		this.plottingEventDispatcher.addPlottingListener(this.meshPlotter);
		this.membranePlotter = new MembranePlotter(model, view, this, renderer);
		this.plottingEventDispatcher.addPlottingListener(this.membranePlotter);
	}


	/**
	 * Turns on/off the GUI/API event handling feature of subcomponents in this Presenter layer.
	 * 
	 * Note that, the "plotter"s, the objects handling plotting/re-plotting events, cannot be disabled by this method.
	 * 
	 * @param enabled Specify false for turning off the event handling feature (enabled by default).
	 */
	public synchronized void setEventHandlingEnabled(boolean enabled) {
		this.eventHandlingEnabled = enabled;
		this.frameHandler.setEventHandlingEnabled(enabled);
		this.screenHandler.setEventHandlingEnabled(enabled);
		this.menuHandler.setEventHandlingEnabled(enabled);
		this.screenSideUIHandler.setEventHandlingEnabled(enabled);
		this.rangeSettingHandler.setEventHandlingEnabled(enabled);
		this.labelSettingHandler.setEventHandlingEnabled(enabled);
		this.fontSettingHandler.setEventHandlingEnabled(enabled);
		this.cameraSettingHandler.setEventHandlingEnabled(enabled);
		this.scaleSettingHandler.setEventHandlingEnabled(enabled);
		this.lightSettingHandler.setEventHandlingEnabled(enabled);
	}


	/**
	 * Gets whether the GUI/API event handling feature of this instance is enabled.
	 * 
	 * @return Returns true if the event handling feature is enabled.
	 */
	public synchronized boolean isEventHandlingEnabled() {
		return this.eventHandlingEnabled;
	}


	/**
	 * Propagates the current configuration stored in Model layer, to the entire application.
	 */
	public synchronized void propagateConfiguration() {
		boolean eventHandlingEnabledBeforeCall = this.eventHandlingEnabled;

		// To prevent infinite looping, disable the event handling feature temporary.
		// (When the view is updated in this method, some events occurs
		//  and their handlers may call this method again, if they are not disabled.)
		this.setEventHandlingEnabled(false);

		// Update the state of View layer and the renderer by the configuration.
		RinearnGraph3DConfiguration config = this.model.config;
		this.view.configure(config);
		this.renderer.configure(config);

		// Update the screen size.
		// (Because "screen-resized" event does not occurs here even if the window size is changed.)
		/*
		int screenWidth = this.view.mainWindow.screenLabel.getWidth();
		int screenHeight = this.view.mainWindow.screenLabel.getHeight();
		this.renderer.setScreenSize(screenWidth, screenHeight); // 重い、しかし控えて比較だと無反応、なんとかしたい
		*/

		// Enable the event handling feature again, if it had been enabled before calling this method.
		this.setEventHandlingEnabled(eventHandlingEnabledBeforeCall);
	}


	/**
	 * Plots all contents composing the graph again (replot).
	 */
	public synchronized void plot() {

		// Update coordinate values of math data series.
		this.updateMathDataSeriesCoordinates();

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


	/**
	 * Update coordinate values of math data series.
	 */
	private void updateMathDataSeriesCoordinates() {
		List<MathDataSeries> mathDataSeriesList = this.model.getMathDataSeriesList();
		for (MathDataSeries mathDataSeries: mathDataSeriesList) {

			// Compute coordinate values from the math expression(s), using Vnano scripting engine.
			try {
				mathDataSeries.computeCoordinates();

			// The scripting engine may throw exceptions, when the expression(s) contains syntax errors, and so on.
			} catch (VnanoException vne) {
				String expression = mathDataSeries.getDisplayedExpression();
				String errorMessage = this.model.config.getEnvironmentConfiguration().isLocaleJapanese() ?
						"数式「" + expression + "」のプロットでエラーが発生しました。\n詳細は標準エラー出力を参照してください。" :
						"An error occurred for plotting the math expression \"" + expression + "\".\nSee the standard error output for datails.";
				JOptionPane.showMessageDialog(this.view.mainWindow.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
				vne.printStackTrace();
			}
		}
	}

}
