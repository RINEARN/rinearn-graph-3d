package com.rinearn.graph3d.event;

import java.util.EventListener;


//Note: copied from the API souece of Ver.5.6


/**
 * <span class="lang-ja">
 * リニアングラフ3D（RINEARN Graph3D）において, プロット/再プロットが必要になった際に発生する,
 * RinearnGraph3DPlottingEvent のリスナーです
 * </span>
 * <span class="lang-en">
 * The listener of RinearnGraph3DPlottingEvent which occurs when plotting/replotting is required on RINEARN Graph 3D
 * </span>
 * .
 * <div class="lang-ja">
 * 再プロットとは, グラフの3D形状の再構築処理の事で, グラフのプロット範囲の変更や, 
 * プロットオプションの選択状況の切り替えなどの際に必要となります. 
 * <br />
 * 再プロットが行われると, RinearnGraph3DRenderer が提供するAPIによって描かれた3D形状は, 一旦全て消去されます. 
 * そのため, このリスナーを実装して再プロットイベントを受け取り, 必要に応じて再描画を行ってください. 
 * </div>
 * 
 * <div class="lang-en">
 * Replotting means the process rebuilding the 3D shape of the graph.
 * RINEARN Graph 3D performs the replotting when ranges of the axes have been modified, 
 * plotting options have been changed, and so on.
 * <br />
 * When the replotting is performed, all contents drawn by the drawing methods of RinearnGraph3DRenderer are cleared.
 * Hence, redraw them by implementing the replotting event listener, if necessary.
 * </div>
 */
public interface RinearnGraph3DPlottingListener extends EventListener {

	/**
	 * <span class="lang-ja">
	 * 再プロットが要求されたタイミングでコールされます
	 * </span>
	 * <span class="lang-en">
	 * Called when replotting is requested
	 * </span>
	 * .
	 * @param event
	 *   <span class="lang-ja">プロットイベント</span>
	 *   <span class="lang-en">The plotting event</span>
	 */
	public void plottingRequested(RinearnGraph3DPlottingEvent event);

	/**
	 * <span class="lang-ja">
	 * 要求された再プロットが, キャンセルされたタイミングでコールされます
	 * </span>
	 * <span class="lang-en">
	 * Called when the requested replotting has been canceled
	 * </span>
	 * .
	 * @param event
	 *   <span class="lang-ja">プロットイベント</span>
	 *   <span class="lang-en">The plotting event</span>
	 */
	public void plottingCanceled(RinearnGraph3DPlottingEvent event);

	/**
	 * <span class="lang-ja">
	 * 要求された再プロットが, 完了したタイミングでコールされます
	 * </span>
	 * <span class="lang-en">
	 * Called when the requested replotting has completed
	 * </span>
	 * .
	 * @param event
	 *   <span class="lang-ja">プロットイベント</span>
	 *   <span class="lang-en">The plotting event</span>
	 */
	public void plottingFinished(RinearnGraph3DPlottingEvent event);
}
