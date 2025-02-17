package com.rinearn.graph3d.config.renderer;

// * この命名だとここにあるのがおかしい、なんか別のコンテナ作るか改名を考える。
//   そもそもこれはカメラの設定ではない可能性すらある。レンダラーの設定では。
//
//   * 確かに命名か位置のどちらかが明らかにまずいというのは明らかにそう
//
//   -> ただ、RendererConfiguration とか作ると、renderer パッケージ管轄で閉じてる config がある設計か？って雰囲気になるかも。
//      実際のところは、config.* 内の多岐に渡る configs をレンダラーは参照しながら描くので、あえて RendererConfiguration ってのを作るのもまずい。

// * そもそもここでの動名詞も避けたい気もする。

// * 確かこれってそもそも setAntialiasingEnabled みたいな案から始まって、でも単にアンチエイリアスON/OFFだけじゃなくて、品質重視か速度重視か、もしくはバランスか、
//   みたいな選択の方が将来的によさそうって考えての結果だったと思う。そこに RenderingMode みたいな仰々しい名を付けてしまったのが間違いだったか。


/**
 * The enum representing each rendering mode.
 */
public enum RenderingMode {

	/** Prioritizes quality of rendered images, than rendering speed. */
	QUALITY,

	/** Prioritizes rendering speed, than quality of rendered images. */
	SPEED;

	// CUSTOM; // For future. Refers an detailed renderer-optimization container instance.
}

