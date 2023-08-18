package com.rinearn.graph3d.config;

// !!!!!
// NOTE
//
//    ・サブ設定ごとの反映ON/OFF制御フラグとかあった方がいい？
//
//     > 設定I/Oの実装ではどっかに要るけどこの層で保持するべきかどうか自信が持てない。
//        まあ後で追加しても互換は保てるしとりあえず保留でいいんじゃないの
//
//        そのうち要検討
//
//    ・このクラスで一階層包むんだったら、要素の RinearnGraph3DScaleConfiguration とかに
//      いちいち RinearnGraph3D が付いてるのは冗長過ぎる気がする。
//
//      > 外から（API経由で）は常に包んで渡すんだったら削ってもいいけど、
//        単体で渡したりとかする場面があると RinearnGraph3DDrawingParameter とかある状態との対応が微妙なような。
//        そもそもあっちがどうなのって話だけど後の祭りだし。
//
//        > 仮に RinearnGraph3D クラスとかのAPIフロントエンドに要素 config コンテナを単体で渡せるようにしたら、
//          最初はよくても長期的に setter がどんどん生えていくし、そもそも全体 config コンテナ渡すやつは必須だし、
//          無駄に分散するだけでそもそも悪手じゃない？
//
//          要素 config コンテナを直接渡すのは実装内部に限った方が良い気がする。
//          setXRange とか、いちいち config 作るのが面倒すぎる重要パラメータ類だけ直接設定メソッドがあればいい。
//          ちょっと混み入った設定は、どうせ config コンテナ作らないといけないなら（面倒不可避だし）全体 config か設定ファイル経由でいいかと。
//          要素 config を渡すってのは粒度が微妙で、API肥大化と利便性のトレードオフ的にあんまりよくないと思う。表層でやり取りすべきじゃない。
//
//          > そんならやっぱここで反映ON/OFFフラグ要るのでは？
//            あとフラグだけでは済まない気もする。
//            全部反映させる（デフォルトtrueと見なす）のか、選んだやつだけ反映させる（デフォルトfalseと見なす）のか、
//            の指定手段もないと、新しい要素 config が追加された時に互換が壊れる。
//            （何かのお知らせメール配信のオプトアウトで全OFFしても新カテゴリー増設された際にまた届き始めるアレみたいな現象が生じてしまう。）
//
//            > それなら生成時点で全部の要素コンテナが含まれててデフォルト値が詰まってる挙動を再考すべき？
//              このインスタンスに要素コンテナを set して、そうしたやつだけ反映されるとか？
//
//              > それもなんか別の面倒さある気がするけど、でもまあそれが一番現実的にストンと着地するかなあ...
//
//                 > 要素configコンテナは生成時点でデフォルト詰まってるのに、全体configコンテナは空なんだ、的な微妙さはあるけど。
//
//                   > ああでも、設定ファイル保存画面の選択状態はデフォルト全OFFだし、
//                     このコンテナのデフォルトを空にするのはそれとの対応という点ではすっきりするか。
//                     じゃないと画面とAPIでなんか方向性が逆だなってなるし。
//                     あの画面のデフォルトを全OFFにするのは結果的にベストっぽかったのでたぶん将来も覆らないし。
//
//                     確かに設定I/Oがファイル読んでこのコンテナを返すとすると、デフォルトは何も読んでない状態で、空が直感に合うかも。
//
//                     > そんなら要素 config コンテナもデフォルト空にして、デフォルト詰めてくれる役を担うやつを別に作る？
//
//                       > しかしその階層だと「空」の状態がない値もたくさんあって、それに何詰めるのって話になる。
//                         コンストラクタ指定必須化は互換的に無理だし、
//                         コンストラクタ呼べなくしてstaticなファクトリメソッド作る感じにすると使うの面倒かと。
//
//                         > あーでもstaticファクトリメソッド、それでもいい気がしてきた。
//                           というかこの全体 config コンテナを empty() か default() でインスタンス化するようにすればそれで解決では？
//                           むしろ要素 config コンテナのほうは空状態が無い値もあるから empty() 作れないし、default() 一択になるし。
//
//              後々で要検討
//
// !!!!!


/**
 * The class for storing configuration values of RINEARN Graph 3D.
 */
public final class RinearnGraph3DConfiguration {

	/** The configuration of the scales of X/Y/X axes. */
	private volatile RinearnGraph3DScaleConfiguration scaleConfiguration = new RinearnGraph3DScaleConfiguration();

	/** The configuration of the graph frame. */
	private volatile RinearnGraph3DFrameConfiguration frameConfiguration = new RinearnGraph3DFrameConfiguration();

	/** The configuration of the lighting/shading parameters. */
	private volatile RinearnGraph3DLightConfiguration lightConfiguration = new RinearnGraph3DLightConfiguration();


	/**
	 * Creates a new configuration storing default values.
	 */
	public RinearnGraph3DConfiguration() {
	}


	/**
	 * Sets the configuration of the scales of X/Y/Z axes.
	 * 
	 * @param scaleConfiguration The configuration of the scales of X/Y/Z axes.
	 */
	public synchronized void setScaleConfiguration(RinearnGraph3DScaleConfiguration scaleConfiguration) {
		this.scaleConfiguration = scaleConfiguration;
	}

	/**
	 * Gets the configuration of the scales of X/Y/Z axes.
	 * 
	 * @return The configuration of the scales of X/Y/Z axes.
	 */
	public synchronized RinearnGraph3DScaleConfiguration getScaleConfiguration() {
		return this.scaleConfiguration;
	}

	/**
	 * Sets the configuration of the graph frame.
	 * 
	 * @param frameConfiguration The configuration of the graph frame.
	 */
	public synchronized void setFrameConfiguration(RinearnGraph3DFrameConfiguration frameConfiguration) {
		this.frameConfiguration = frameConfiguration;
	}

	/**
	 * Gets the configuration of the graph frame.
	 * 
	 * @return The configuration of the graph frame.
	 */
	public synchronized RinearnGraph3DFrameConfiguration getFrameConfiguration() {
		return this.frameConfiguration;
	}

	/**
	 * Sets the configuration of the lighting/shading parameters.
	 * 
	 * @param frameConfiguration The configuration of the lighting/shading parameters.
	 */
	public synchronized void setFrameConfiguration(RinearnGraph3DLightConfiguration lightConfiguration) {
		this.lightConfiguration = lightConfiguration;
	}

	/**
	 * Gets the configuration of the lighting/shading parameters.
	 * 
	 * @return The configuration of the lighting/shading parameters.
	 */
	public synchronized RinearnGraph3DLightConfiguration getLightConfiguration() {
		return this.lightConfiguration;
	}

}
