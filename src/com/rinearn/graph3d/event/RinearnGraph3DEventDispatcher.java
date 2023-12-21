package com.rinearn.graph3d.event;

import java.util.List;
import java.util.ArrayList;


// Note: copied from the API souece of Ver.5.6, with some modifications.


/**
 * <span class="lang-ja">
 * 発生したプロットイベントをリスナーに配信するクラスです
 * （APIのユーザーがこのクラスを使用する必要はありません）
 * </span>
 * <span class="lang-en">
 * The class to dispatch plotting events to its listeners
 * (users of API should not use this class)
 * </span>
 * .
 * <div class="lang-ja">
 * このクラスの仕様は, 予告なく変更される可能性があります.
 * </div>
 * <div class="lang-en">
 * Note that, the specifications of this class may change in future without any notice.
 * </div>
 */
public class RinearnGraph3DEventDispatcher {

	/** The list of the registered plotting event listeners. */
	private List<RinearnGraph3DPlottingListener> plotListeners = new ArrayList<RinearnGraph3DPlottingListener>();

	/** Stores the RinearnGraph3D instance, to be passed to event listeners as a "source". */
	private final Object source;


	/**
	 * <span class="lang-ja">
	 * 指定された RinearnGraph3D インスタンスを source とする, 新しいイベントディスパッチャーを生成します
	 * </span>
	 * <span class="lang-en">
	 * Creates a new event dispatcher of which source is the specified RinearnGraph3D instance
	 * </span>
	 * .
	 * @param source
	 *   <span class="lang-ja">イベントの source とする RinearnGraph3D インスタンス</span>
	 *   <span class="lang-en">The RinearnGraph3D instance as the source of events</span>
	 */
	public RinearnGraph3DEventDispatcher (Object source) {
		this.source = source;
	}


	/**
	 * <span class="lang-ja">
	 * プロットイベントのリスナーを追加登録します
	 * </span>
	 * <span class="lang-en">
	 * Adds the plotting event listener
	 * </span>
	 * .
	 * @param listener
	 *   <span class="lang-ja">プロットイベントのリスナー</span>
	 *   <span class="lang-en">The plotting event listener</span>
	 */
	public void addPlottingListener(RinearnGraph3DPlottingListener listener) {
		this.plotListeners.add(listener);
	}


	/**
	 * <span class="lang-ja">
	 * 登録されている全てのプロットイベントのリスナーに対して, plottingRequested メソッドを呼び出します
	 * </span>
	 * <span class="lang-en">
	 * Calls the 'plottingRequested' method for all registered plotting event listeners
	 * </span>
	 * .
	 */
	public void firePlottingRequested () {
		RinearnGraph3DPlottingEvent event = new RinearnGraph3DPlottingEvent(this.source);
		for (RinearnGraph3DPlottingListener listener : this.plotListeners) {
			listener.plottingRequested(event);
		}
	}


	/**
	 * <span class="lang-ja">
	 * 登録されている全てのプロットイベントのリスナーに対して, plottingCanceled メソッドを呼び出します
	 * </span>
	 * <span class="lang-en">
	 * Calls the 'plottingCanceled' method for all registered plotting event listeners
	 * </span>
	 * .
	 */
	public void firePlottingCanceled () {
		RinearnGraph3DPlottingEvent event = new RinearnGraph3DPlottingEvent(this.source);
		for (RinearnGraph3DPlottingListener listener : this.plotListeners) {
			listener.plottingFinished(event);
		}
	}


	/**
	 * <span class="lang-ja">
	 * 登録されている全てのプロットイベントのリスナーに対して, plottingFinished メソッドを呼び出します
	 * </span>
	 * <span class="lang-en">
	 * Calls the 'plottingFinished' method for all registered plotting event listeners
	 * </span>
	 * .
	 */
	public void firePlottingFinished () {
		RinearnGraph3DPlottingEvent event = new RinearnGraph3DPlottingEvent(this.source);
		for (RinearnGraph3DPlottingListener listener : this.plotListeners) {
			listener.plottingFinished(event);
		}
	}
}

