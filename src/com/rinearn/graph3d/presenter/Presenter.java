package com.rinearn.graph3d.presenter;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.color.AxisGradientColor;
import com.rinearn.graph3d.config.color.ColorConfiguration;
import com.rinearn.graph3d.config.color.GradientColor;
import com.rinearn.graph3d.config.data.DataConfiguration;
import com.rinearn.graph3d.config.data.SeriesAttribute;
import com.rinearn.graph3d.config.label.LabelConfiguration;
import com.rinearn.graph3d.config.label.LegendLabelConfiguration;
import com.rinearn.graph3d.config.range.RangeConfiguration;
import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.model.data.series.AbstractDataSeries;
import com.rinearn.graph3d.model.data.series.MathDataSeries;
import com.rinearn.graph3d.model.data.series.DataSeriesGroup;
import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.renderer.RinearnGraph3DRenderer;
import com.rinearn.graph3d.event.RinearnGraph3DEventDispatcher;

import com.rinearn.graph3d.presenter.handler.CameraSettingHandler;
import com.rinearn.graph3d.presenter.handler.FontSettingHandler;
import com.rinearn.graph3d.presenter.handler.GradientOptionHandler;
import com.rinearn.graph3d.presenter.handler.MainWindowFrameHandler;
import com.rinearn.graph3d.presenter.handler.LabelSettingHandler;
import com.rinearn.graph3d.presenter.handler.LightSettingHandler;
import com.rinearn.graph3d.presenter.handler.MainMenuHandler;
import com.rinearn.graph3d.presenter.handler.RangeSettingHandler;
import com.rinearn.graph3d.presenter.handler.ScaleSettingHandler;
import com.rinearn.graph3d.presenter.handler.ScreenHandler;
import com.rinearn.graph3d.presenter.handler.ScreenRightClickMenuHandler;
import com.rinearn.graph3d.presenter.handler.ScreenSideUIHandler;
import com.rinearn.graph3d.presenter.handler.ZxyMathHandler;
import com.rinearn.graph3d.presenter.handler.XtYtZtMathHandler;
import com.rinearn.graph3d.presenter.handler.DataFileIOHandler;
import com.rinearn.graph3d.presenter.handler.DataTextIOHandler;
import com.rinearn.graph3d.presenter.handler.DataArrayIOHandler;
import com.rinearn.graph3d.presenter.handler.ImageIOHandler;
import com.rinearn.graph3d.presenter.handler.PointOptionHandler;
import com.rinearn.graph3d.presenter.handler.LineOptionHandler;
import com.rinearn.graph3d.presenter.handler.MeshOptionHandler;
import com.rinearn.graph3d.presenter.handler.SurfaceOptionHandler;
import com.rinearn.graph3d.presenter.plotter.PointPlotter;
import com.rinearn.graph3d.presenter.plotter.LinePlotter;
import com.rinearn.graph3d.presenter.plotter.MeshPlotter;
import com.rinearn.graph3d.presenter.plotter.SurfacePlotter;

import org.vcssl.nano.VnanoException;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;



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

	/** The handler of events of (the frame of) the main window. */
	public final MainWindowFrameHandler mainWindowFrameHandler;

	/** The handler of events and API requests related to the menu bar and right click menus. */
	public final MainMenuHandler mainMenuHandler;

	/** The handler of events of the graph screen, such as mouse-dragging events for rotate a graph, etc. */
	public final ScreenHandler screenHandler;

	/** The handler of events of the right-click menu of the graph screen. */
	public final ScreenRightClickMenuHandler screenRightClickMenuHandler;

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

	/** The handler of events and API requests for plotting math expressions of "x(t),y(t),z(t)" form. */
	public final XtYtZtMathHandler xtYtZtMathHandler;

	/** The handler of events and API requests for plotting data files. */
	public final DataFileIOHandler dataFileIOHandler;

	/** The handler of events for plotting data texts. */
	public final DataTextIOHandler dataTextIOHandler;

	/** The handler of API requests for plotting data stored in arrays. */
	public final DataArrayIOHandler dataArrayIOHandler;

	/** The handler of events and API requests related to image file I/O. */
	public final ImageIOHandler imageIOHandler;

	/** The flag for turning on/off the event handling feature of subcomponents in this instance. */
	private volatile boolean eventHandlingEnabled = true;

	/** The handler of "With Poinst" option window. */
	public final PointOptionHandler pointOptionHandler;

	/** The handler of "With Lines" option window. */
	public final LineOptionHandler lineOptionHandler;

	/** The handler of "With Meshes" option window. */
	public final MeshOptionHandler meshOptionHandler;

	/** The handler of "With Surfaces" option window. */
	public final SurfaceOptionHandler surfaceOptionHandler;

	/** The handler of "Gradient Coloring" option window. */
	public final GradientOptionHandler gradientOptionHandler;

	/** The plotter to plot points. */
	public final PointPlotter pointPlotter;

	/** The plotter to plot lines. */
	public final LinePlotter linePlotter;

	/** The plotter to plot meshes. */
	public final MeshPlotter meshPlotter;

	/** The plotter to plot surfaces. */
	public final SurfacePlotter surfacePlotter;


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
		this.mainWindowFrameHandler = new MainWindowFrameHandler(model, view, this);

		// Create a handler of events of the graph screen, handling mouse-dragging events for rotate a graph, etc.
		this.screenHandler = new ScreenHandler(model, view, this);

		// Create handlers for various events and API requests.
		this.mainMenuHandler = new MainMenuHandler(model, view, this);
		this.screenSideUIHandler = new ScreenSideUIHandler(model, view, this);
		this.screenRightClickMenuHandler = new ScreenRightClickMenuHandler(model, view, this);

		this.rangeSettingHandler = new RangeSettingHandler(model, view, this);
		this.labelSettingHandler = new LabelSettingHandler(model, view, this);
		this.fontSettingHandler = new FontSettingHandler(model, view, this);
		this.cameraSettingHandler = new CameraSettingHandler(model, view, this);
		this.scaleSettingHandler = new ScaleSettingHandler(model, view, this);
		this.lightSettingHandler = new LightSettingHandler(model, view, this);

		this.zxyMathHandler = new ZxyMathHandler(model, view, this);
		this.xtYtZtMathHandler = new XtYtZtMathHandler(model, view, this);

		this.dataFileIOHandler = new DataFileIOHandler(model, view, this);
		this.dataTextIOHandler = new DataTextIOHandler(model, view, this);
		this.dataArrayIOHandler = new DataArrayIOHandler(model, view, this);
		this.imageIOHandler = new ImageIOHandler(model, view, this);

		this.pointOptionHandler = new PointOptionHandler(model, view, this);
		this.lineOptionHandler = new LineOptionHandler(model, view, this);
		this.meshOptionHandler = new MeshOptionHandler(model, view, this);
		this.surfaceOptionHandler = new SurfaceOptionHandler(model, view, this);
		this.gradientOptionHandler = new GradientOptionHandler(model, view, this);

		// Create "plotter"s, which perform plottings/re-plottings in event-driven flow.
		this.pointPlotter = new PointPlotter(model, view, this, renderer);
		this.plottingEventDispatcher.addPlottingListener(this.pointPlotter);
		this.linePlotter = new LinePlotter(model, view, this, renderer);
		this.plottingEventDispatcher.addPlottingListener(this.linePlotter);
		this.meshPlotter = new MeshPlotter(model, view, this, renderer);
		this.plottingEventDispatcher.addPlottingListener(this.meshPlotter);
		this.surfacePlotter = new SurfacePlotter(model, view, this, renderer);
		this.plottingEventDispatcher.addPlottingListener(this.surfacePlotter);
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
		this.mainWindowFrameHandler.setEventHandlingEnabled(enabled);
		this.screenHandler.setEventHandlingEnabled(enabled);
		this.mainMenuHandler.setEventHandlingEnabled(enabled);
		this.screenSideUIHandler.setEventHandlingEnabled(enabled);
		this.screenRightClickMenuHandler.setEventHandlingEnabled(enabled);

		this.rangeSettingHandler.setEventHandlingEnabled(enabled);
		this.labelSettingHandler.setEventHandlingEnabled(enabled);
		this.fontSettingHandler.setEventHandlingEnabled(enabled);
		this.cameraSettingHandler.setEventHandlingEnabled(enabled);
		this.scaleSettingHandler.setEventHandlingEnabled(enabled);
		this.lightSettingHandler.setEventHandlingEnabled(enabled);

		this.zxyMathHandler.setEventHandlingEnabled(enabled);
		this.xtYtZtMathHandler.setEventHandlingEnabled(enabled);

		this.dataFileIOHandler.setEventHandlingEnabled(enabled);
		this.dataTextIOHandler.setEventHandlingEnabled(enabled);
		this.dataArrayIOHandler.setEventHandlingEnabled(enabled);
		this.imageIOHandler.setEventHandlingEnabled(enabled);

		this.pointOptionHandler.setEventHandlingEnabled(enabled);
		this.lineOptionHandler.setEventHandlingEnabled(enabled);
		this.meshOptionHandler.setEventHandlingEnabled(enabled);
		this.surfaceOptionHandler.setEventHandlingEnabled(enabled);
		this.gradientOptionHandler.setEventHandlingEnabled(enabled);
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

		// Update the data configuration from the currently registered data.
		this.updateDataConfiguration();

		// Update the legend configuration from the currently registered data.
		this.updateLegends();

		// Adjust the ranges to fit to the currently registered data.
		this.adjustRanges();

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
	 * Updates coordinate values of math data series.
	 */
	private void updateMathDataSeriesCoordinates() {
		DataSeriesGroup<MathDataSeries> mathDataSeriesGroup = this.model.dataStore.getMathDataSeriesGroup();
		for (MathDataSeries mathDataSeries: mathDataSeriesGroup) {

			// Compute coordinate values from the math expression(s), using Vnano scripting engine.
			try {
				mathDataSeries.computeCoordinates();

			// The scripting engine may throw exceptions, when the expression(s) contains syntax errors, and so on.
			} catch (VnanoException vne) {
				String expression = mathDataSeries.getFullDisplayName();
				String errorMessage = this.model.config.getEnvironmentConfiguration().isLocaleJapanese() ?
						"数式「" + expression + "」のプロットでエラーが発生しました。\n詳細は標準エラー出力を参照してください。" :
						"An error occurred for plotting the math expression \"" + expression + "\".\nSee the standard error output for datails.";
				JOptionPane.showMessageDialog(this.view.mainWindow.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
				vne.printStackTrace();
			}
		}
	}


	/**
	 * Updates the data configuration from the currently registered data.
	 */
	private void updateDataConfiguration() {
		DataConfiguration dataConfig = model.config.getDataConfiguration();

		// Get the group of all the registered data series.
		DataSeriesGroup<AbstractDataSeries> dataSeriesGroup = model.dataStore.getCombinedDataSeriesGroup();
		int seriesCount = dataSeriesGroup.getDataSeriesCount();

		// Store the series atttributes of all the data series into an array.
		List<SeriesAttribute> seriesAttributeList = new ArrayList<SeriesAttribute>();
		for (int iseries=0; iseries<seriesCount; iseries++) {
			AbstractDataSeries dataSeries = dataSeriesGroup.getDataSeriesAt(iseries);
			SeriesAttribute attribute = dataSeries.getSeriesAttribute();
			attribute.setGlobalSeriesIndex(iseries);
			seriesAttributeList.add(dataSeries.getSeriesAttribute());
		}
		SeriesAttribute[] seriesAttributes = new SeriesAttribute[seriesAttributeList.size()];
		seriesAttributes = seriesAttributeList.toArray(seriesAttributes);

		// Store the above to the configuration container.
		dataConfig.setGlobalSeriesAttributes(seriesAttributes);
	}


	/**
	 * Updates the legend configuration from the currently registered data.
	 */
	private void updateLegends() {
		LabelConfiguration labelConfig = model.config.getLabelConfiguration();
		LegendLabelConfiguration legendLabelConfig = labelConfig.getLegendLabelConfiguration();

		// Get the group of all the registered data series.
		DataSeriesGroup<AbstractDataSeries> dataSeriesGroup = model.dataStore.getCombinedDataSeriesGroup();
		int dataSeriesCount = dataSeriesGroup.getDataSeriesCount();

		// If the auto-legend-generation feature is disabled is enabled:
		// Extract the unmodified (original) legend from each data series's attribute,
		// and set it to the legend configuration.
		if (legendLabelConfig.isAutoLegendGenerationEnabled()) {

			// Stores the legend to be displayed.
			List<String> legendList = new ArrayList<String>();

			// Get the unmodified (original) legend from each series's attribute, and set it to the legend list to be displayed.
			// Also, set it to the modifiable legend of the series's attribute.
			for (AbstractDataSeries dataSeries: dataSeriesGroup) {
				SeriesAttribute attribute = dataSeries.getSeriesAttribute();
				String unmodifiedLegend = attribute.getUnmodifiedLegend();
				legendList.add(unmodifiedLegend);

				attribute.setModifiableLegend(unmodifiedLegend);
			}

			// Set to the legend label configuration to display.
			String[] legendTexts = new String[ legendList.size() ];
			legendTexts = legendList.toArray(legendTexts);
			legendLabelConfig.setLabelTexts(legendTexts);

		// If the auto-legend-generation feature is disabled is disabled:
		// Update the "modifiable legend" of the data series's atttribute.
		} else {
			String[] currentLegends = legendLabelConfig.getLabelTexts();
			int legendCount = currentLegends.length;
			for (int ilegend=0; ilegend<legendCount; ilegend++) {
				if (ilegend < dataSeriesCount) {
					AbstractDataSeries dataSeries = dataSeriesGroup.getDataSeriesAt(ilegend);
					SeriesAttribute attribute = dataSeries.getSeriesAttribute();
					attribute.setModifiableLegend(currentLegends[ilegend]);
				}
			}
		}
	}


	/**
	 * Adjusts the ranges of axes and gradient colors to fit to the currently registered data.
	 */
	private synchronized void adjustRanges() {

		// Get the group of all the registered data series.
		DataSeriesGroup<AbstractDataSeries> dataSeriesGroup = model.dataStore.getCombinedDataSeriesGroup();

		// Get the configurations of each axis's range.
		RangeConfiguration rangeConfig = model.config.getRangeConfiguration();
		RangeConfiguration.AxisRangeConfiguration xRangeConfig = rangeConfig.getXRangeConfiguration();
		RangeConfiguration.AxisRangeConfiguration yRangeConfig = rangeConfig.getYRangeConfiguration();
		RangeConfiguration.AxisRangeConfiguration zRangeConfig = rangeConfig.getZRangeConfiguration();

		// Auto-adjust the X range to fit to the data, if enabled.
		if (xRangeConfig.isAutoRangeEnabled()) {
			if (dataSeriesGroup.hasXMin()) {
				xRangeConfig.setMinimum(dataSeriesGroup.getXMin());
			}
			if (dataSeriesGroup.hasXMax()) {
				xRangeConfig.setMaximum(dataSeriesGroup.getXMax());
			}
		}

		// Auto-adjust the Y range to fit to the data, if enabled.
		if (yRangeConfig.isAutoRangeEnabled()) {
			if (dataSeriesGroup.hasYMin()) {
				yRangeConfig.setMinimum(dataSeriesGroup.getYMin());
			}
			if (dataSeriesGroup.hasYMax()) {
				yRangeConfig.setMaximum(dataSeriesGroup.getYMax());
			}
		}

		// Auto-adjust the Z range to fit to the data, if enabled.
		if (zRangeConfig.isAutoRangeEnabled()) {
			if (dataSeriesGroup.hasZMin()) {
				zRangeConfig.setMinimum(dataSeriesGroup.getZMin());
			}
			if (dataSeriesGroup.hasZMax()) {
				zRangeConfig.setMaximum(dataSeriesGroup.getZMax());
			}
		}

		// Auto-adjust the range of the gradient colors.
		ColorConfiguration colorConfig = this.model.config.getColorConfiguration();
		for (GradientColor gradientColor: colorConfig.getDataGradientColors()) {
			for (AxisGradientColor axisGradientColor: gradientColor.getAxisGradientColors()) {
				if (!axisGradientColor.isBoundaryAutoRangeEnabled()) {
					continue;
				}
				switch (axisGradientColor.getAxis()) {
					case X : {
						axisGradientColor.setMinimumBoundaryCoordinate(xRangeConfig.getMinimum());
						axisGradientColor.setMaximumBoundaryCoordinate(xRangeConfig.getMaximum());
						break;
					}
					case Y : {
						axisGradientColor.setMinimumBoundaryCoordinate(yRangeConfig.getMinimum());
						axisGradientColor.setMaximumBoundaryCoordinate(yRangeConfig.getMaximum());
						break;
					}
					case Z : {
						axisGradientColor.setMinimumBoundaryCoordinate(zRangeConfig.getMinimum());
						axisGradientColor.setMaximumBoundaryCoordinate(zRangeConfig.getMaximum());
						break;
					}
					case SCALAR : {
						RangeConfiguration.AxisRangeConfiguration[] extraRangeConfig = rangeConfig.getExtraDimensionRangeConfigurations();
						axisGradientColor.setMinimumBoundaryCoordinate(extraRangeConfig[0].getMinimum());
						axisGradientColor.setMaximumBoundaryCoordinate(extraRangeConfig[0].getMaximum());
						// Existence of extraRangeConfig[0] has been checked in the validation.
						break;
					}
					default : {
						throw new UnsupportedOperationException("Unknown gradient axis: " + axisGradientColor.getAxis());
					}
				}
			}
		}

		// Propagates the updated range configurations to the entire application.
		this.propagateConfiguration();
	}


	/**
	 * Disposes all the disposable resources of this application.
	 *
	 * This processing is performed on the event-dispatcher thread,
	 * because threre may be some reserved event/API processings on the event queue when this method is called.
	 * In such situation,
	 * this application will be disposed after when all the reserved event/API processings are completed.
	 */
	public synchronized void dispose() {

		// The above "synchronized" modifier is necessary for avoiding conflicts with the processing of plot() method,
		// because plot() may be run on threads different with the event-dispatcher thread,
		// e.g.: the thread of the host application calling plot(),
		// or screen-updater thread when asynchronous-plotting feature is enabled.

		Disposer disposer = new Disposer();
		if (SwingUtilities.isEventDispatchThread()) {
			disposer.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(disposer);
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * The Runnable implementation to perform internal processing of dispose() method on the event-dispatcher thread.
	 */
	private final class Disposer implements Runnable {
		@Override
		public void run() {

			// Terminate the rendering loop.
			try {
				renderingLoop.exit();

				// The loop is running on another thread, so we must wait for termination of it.
				while(!renderingLoop.isExitedSuccessfully()) {
					Thread.sleep(100);
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}

			// Dispose all the windows of View layer.
			view.dispose();

			// Dispose the resources in the renderer.
			renderer.dispose();

			// Some subcomponents in Presenter layer having disposable resources (image buffers, etc.),
			// so dispose them.
			renderingLoop.dispose();
		}
	}

}
