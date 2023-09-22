package com.rinearn.graph3d.event;

import java.util.EventObject;


//Note: copied from the API souece of Ver.5.6


/**
 * <span class="lang-ja">
 * リニアングラフ3D（RINEARN Graph3D）において、プロット/再プロットが必要になった際に発生するイベントです
 * </span>
 * <span class="lang-en">
 * The event which occurs when plotting/replotting is required on RINEARN Graph 3D
 * </span>
 * .
 * <div class="lang-ja">
 * このイベントは  RinearnGraph3DPlottingListener インタフェースを実装する事で受け取る事ができます。
 * <br />
 * 再プロットとは、グラフの3D形状の再構築処理の事で、グラフのプロット範囲の変更や、
 * プロットオプションの選択状況の切り替えなどの際に必要となります。
 * <br />
 * 再プロットが行われると、RinearnGraph3DRenderer が提供するAPI等によって描かれた3D形状は、一旦全て消去されます。
 * そのため、このイベントを受け取り、必要に応じて再描画を行ってください。
 * <br />
 * なお、このイベントは、source としてイベント発生元の RinearnGraph3D クラスのインスタンスを保持しています。
 * </div>
 * 
 * <div class="lang-en">
 * You can receive this event by implementing RinearnGraph3DPlottingListener interface.
 * <br />
 * Replotting means the process rebuilding the 3D shape of the graph.
 * RINEARN Graph 3D performs the replotting when ranges of the axes have been modified, 
 * plotting options have been changed, and so on.
 * <br />
 * When the replotting is performed, all contents drawn by the drawing methods of RinearnGraph3DRenderer API are cleared.
 * Hence, redraw them by receiving this event if necessary.
 * <br />
 * Also, this event has the instance of the RinearnGraph3D class as the 'source'.
 * </div>
 */
public class RinearnGraph3DPlottingEvent extends EventObject {
	
	/**
	 * <span class="lang-ja">
	 * 新しいプロットイベントを生成します
	 * </span>
	 * <span class="lang-en">
	 * Creates a new plotting event
	 * </span>
	 * .
	 * @param source
	 *   <span class="lang-ja">イベント発生元の RinearnGraph3D インスタンス</span>
	 *   <span class="lang-en">The RinearnGraph3D instance as the source of this event</span>
	 */
	public RinearnGraph3DPlottingEvent(Object source) {
		super(source);
	}
}
