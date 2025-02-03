package com.rinearn.graph3d.config.color;

import java.awt.Color;


// !!! NOTE !!!
//
// 合成方法を加算とか乗算とか enum で指定できたほうがいいかも。
// まあそもそも加算でも乗算でもなさそうだけど。
// -> いや、加算と乗算の組み合わせで大抵いける気がする。一方のみで考えるとできない例は出てくるが。
//
//    軸Aと軸Bは加算、軸Bと軸Cは乗算、みたいなパターンはどうする？
//    -> そんな場合はもう除外しても良い気がするが。しかし透明度だけ乗算、みたいな場合はあるかな。
//       実際、赤系統と青系統の2軸加算グラデに、透明度を乗算したい、みたいな場合は実際ありそう。
//
// いや、そもそも思ったんだけど、アルファチャンネルでのグラデって、
// 透明色との普通のグラデとは処理が違う気がする。現行版でどう実装してたっけか？
// 透明とのグラデって、「RGB成分が透明と不透明側と同じで、アルファが0と255との間のグラデ」と等価では？ あれは処理的に。
// なので黒ベースや白ベースのα=255透明色は、普通に任意の色との間でグラデかけても、綺麗な透明グラデにはならんでしょ。
// たぶん。直感的に。
// -> 確かに、仮にαをRGBと等価に扱うとすれば、透明側のRGBの違いに結果が依存しないはずはないので、違いが出る事は確実なはず。
//
// ↑を受けて、それならどう設定として扱うべきか？
//   1次元なら、例えば青と透明の2色グラデなら、青不透明から青透明のグラデに設定すれば済むので、セット方法は工夫しなくてもいい。
//   しかし、2次元のうちの1方向を透明度とするなら、様々な色相に対して透明を作用させる必要があるので、
//   透明側にダイレクトに色をセットする方法は向かない。
//
// -> ならやっぱモードで「色配列の補完グラデ」とか「透明へのグラデ」とか選べるべきか。
//    -> なんかモードや階層だらけになるが… ColorConfig の GRADIENT モードの特定Axisの「透明グラデモード」とか、ちょっと深すぎかも。
//
// -> 上記の透明へのグラデ、透明軸を「白不透明～白透明」へのグラデにして、色彩側の軸と乗算すれば等価になる気がする。直感的に。
//    -> 等価になるなら色配列による設定で兼ねられるので、別途モードを設ける必要はない。階層数増やしたくないし。本当に兼ねられるなら。
//
//    -> とりあえず上記もろもろを踏まえて、ブレンドモード（乗算 or 加算）ベースで実装した。
//       またミキサーあたり実装して動くようになったくらいのタイミングで振り返って要検討/検証。この仕様でいけそうかどうか。
//
//    -> 上記の「白不透明～白透明へのグラデ」を「色彩グラデ」に乗算するやつ、実際にやってみたけど期待通りになった。
//       X軸方向に色彩変化、Y軸方向に透明度変化みたいにちゃんとなる。のでこれでいい。透明グラデを特別扱いするモードとかは要らない。
//       要は「透明変化マスクは白色のαのみ変化させたグラデを乗算すべし」ってのをどっかに書いておけばいい。使い方の一つとして。
//
//!!! NOTE !!!

// !!! NOTE2 !!!
//
// getMinimumBoundaryCoordinate とかの命名、むしろ getBoundaryCoordinateMinimum とかでは？ getRangeMinimum とかとの対応的に。
//
// !!! NOTE2 !!!

// !!! NOTE3 !!!
//
// * ColorConfig 内部クラスから color パッケージ内の単体クラスに昇格したことだし、
//   AxisGradientColor も1つのクラスに分割して、このクラスはそれを部品として使う、みたいに改めるべき？
//
// !!! NOTE3 !!!

/**
 * The class representing a color gradient.
 *
 * This class is mainly used in ColorConfiguration.
 */
public final class GradientColor {

	/** Stores an one-dimensional gradient color for each axis. */
	private volatile AxisGradientColor[] axisGradientColors = { new AxisGradientColor() };

	/** The background color on which the gradients of all axes are blended. */
	private volatile Color backgroundColor = new Color(0, 0, 0, 0); // Clear black

	/**
	 * Gets the total number of the axes (dimensions) of this gradient color.
	 *
	 * @return The total number of the axes.
	 */
	public synchronized int getAxisCount() {
		return this.axisGradientColors.length;
	}

	/**
	 * Sets each axis's gradient color, by an array storing an one-dimensional gradient color for each axis.
	 *
	 * @param axisGradientColors The array storing an one-dimensional gradient color for each axis.
	 */
	public synchronized void setAxisGradientColors(AxisGradientColor[] axisGradientColors) {
		this.axisGradientColors = axisGradientColors;
	}

	/**
	 * Gets each axis's color gradient color as an array.
	 *
	 * @param axisGradientColors The array storing an one-dimensional gradient color for each axis.
	 */
	public synchronized AxisGradientColor[] getAxisGradientColors() {
		return this.axisGradientColors;
	}

	/**
	 * Sets the background color, on which the gradient colors of all axes are blended.
	 *
	 * @param backgroundColor The background color.
	 */
	public synchronized void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * Gets the background color, on which the gradient colors of all axes are blended.
	 *
	 * @return The background color.
	 */
	public synchronized Color getBackgroundColor() {
		return this.backgroundColor;
	}

	/**
	 * Validates correctness and consistency of parameters stored in this instance.
	 *
	 * This method is called when a color configuration is specified to RinearnGraph3D or its renderer.
	 * If no issue is detected, nothing occurs.
	 * If any issue is detected, throws IllegalStateException.
	 *
	 * @throws IllegalStateException Thrown when incorrect or inconsistent settings are detected.
	 */
	public synchronized void validate() throws IllegalStateException {

		// Validate axes's gradient colors.
		if (this.axisGradientColors == null) {
			throw new IllegalStateException("The axes's gradient colors are null");
		} else {
			for (AxisGradientColor axisGradientColor: this.axisGradientColors) {
				if (axisGradientColor == null) {
					throw new IllegalStateException("There is a null element in the axes's color gradients.");
				}
				axisGradientColor.validate();
			}
		}

		// Validate the background color.
		if (this.backgroundColor == null) {
			throw new IllegalStateException("The background color is null.");
		}
	}


}
