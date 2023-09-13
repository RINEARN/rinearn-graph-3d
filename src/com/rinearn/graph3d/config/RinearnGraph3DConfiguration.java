package com.rinearn.graph3d.config;

import com.rinearn.graph3d.config.RangeConfiguration.AxisRangeConfiguration;

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
//    ・このクラスで一階層包むんだったら、要素の ScaleConfiguration とかに
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
//                           > 作った。defaultは予約語なので createEmptyConfiguration、createDefaultConfiguration のセットで。
//                             要素 config コンテナの方はコンストラクタ―生成＆デフォルト詰まり状態のままだけど、そっちどうすべきかはまた後で。
//                             まあこのクラスが実質この設定パッケージの最上階層なので、ここだけちょっとリッチな感じでもありっちゃありだと思う。
//
//                             要素 config コンテナは実装時にフィールド宣言しながらデフォルト書いてった方がコード上は書きやすいし読みやすいし。
//                             もしあっちもstaticファクトリメソッドに切り分けるならデフォルト値をどっか別のメタ設定的なやつに定義する必要ある。
//
//      > RinearnGraph3DScaleConfiguration とかの要素 config コンテナの命名、上記が概ね着地したので RinearnGraph3D 削った。
//        この全体 config コンテナのみ残す方針。パッケージ内最上位だし、一覧の中でもこれが表層である事がわかりやすそうだし。
//        あとただの Configuration だと基底クラスっぽい雰囲気になってしまうので。
//
// !!!!!


/**
 * The class for storing configuration values of RINEARN Graph 3D.
 */
public final class RinearnGraph3DConfiguration {

	/** The configuration of the ranges of X/Y/X axes. */
	private volatile RangeConfiguration rangeConfiguration = null;

	/** The configuration of the scales of X/Y/X axes. */
	private volatile ScaleConfiguration scaleConfiguration = null;

	/** The configuration of the graph frame. */
	private volatile FrameConfiguration frameConfiguration = null;

	/** The configuration of the lighting/shading parameters. */
	private volatile LightConfiguration lightConfiguration = null;

	/** The configuration of the camera (angles, magnification, and so on). */
	private volatile CameraConfiguration cameraConfiguration = null;

	/** The configuration of colors. */
	private volatile ColorConfiguration colorConfiguration = null;


	/**
	 * Creates a new configuration storing default values.
	 */
	private RinearnGraph3DConfiguration() {
	}


	/**
	 * Creates a new configuration storing nothing.
	 * 
	 * @return The configuration storing nothing.
	 */
	public static RinearnGraph3DConfiguration createEmptyConfiguration() {
		return new RinearnGraph3DConfiguration();
	}


	/**
	 * Creates a new configuration storing default values.
	 * 
	 * @return The configuration storing default values.
	 */
	public static RinearnGraph3DConfiguration createDefaultConfiguration() {
		RinearnGraph3DConfiguration configuration = new RinearnGraph3DConfiguration();

		configuration.setRangeConfiguration(new RangeConfiguration());
		configuration.setScaleConfiguration(new ScaleConfiguration());
		configuration.setFrameConfiguration(new FrameConfiguration());
		configuration.setLightConfiguration(new LightConfiguration());
		configuration.setCameraConfiguration(new CameraConfiguration());
		configuration.setColorConfiguration(new ColorConfiguration());

		return configuration;
	}


	/**
	 * Validates correctness and consistency of configuration parameters stored in this instance.
	 * 
	 * This method is called when this configuration is specified to RinearnGraph3D or its renderer.
	 * If no issue is detected, nothing occurs.
	 * If any issue is detected, throws IllegalStateException.
	 * 
	 * @throws IllegalStateException Thrown when incorrect or inconsistent settings are detected.
	 */
	public synchronized void validate() throws IllegalStateException {

		// Validate the range configuration.
		if (this.hasRangeConfiguration()) {
			this.rangeConfiguration.validate();
		}

		// Validate the color configuration.
		if (this.hasColorConfiguration()) {
			this.colorConfiguration.validate();
		}

		// There are some dependencies between color and range configurations, so check the consistency of them.
		if (this.hasRangeConfiguration() && this.hasColorConfiguration()) {

			// Extract each axis's gradient stored in the color configuration.
			for (ColorGradient dataColorGradient: this.colorConfiguration.getDataColorGradients()) {
				for (ColorGradient.AxisColorGradient axisGradient: dataColorGradient.getAxisColorGradients()) {

					// If any gradient's axis is set to an extra dimension (e.g.: 4-th column),
					// the range of the extra dimension must be stored in that range configuration.
					ColorGradient.Axis axis = axisGradient.getAxis();
					if (axis == ColorGradient.Axis.COLUMN_4) {
						if (this.rangeConfiguration.getExtraDimensionCount() < 1) {
							throw new IllegalStateException(
								"For setting a gradient's axis to COLUMN_4," +
								"the range settings for the 4-th dimension must exist in the range configuration," +
								"but it does not exist."
							);
						}
					}
				}
			}
		}
	}


	/**
	 * Checks whether any range configuration is set to this instance.
	 * 
	 * @return Returns true if any range configuration is set to this instance.
	 */
	public synchronized boolean hasRangeConfiguration() {
		return this.rangeConfiguration != null;
	}

	/**
	 * Sets the configuration of the ranges of X/Y/Z axes.
	 * 
	 * @param scaleConfiguration The configuration of the ranges of X/Y/Z axes.
	 */
	public synchronized void setRangeConfiguration(RangeConfiguration rangeConfiguration) {
		this.rangeConfiguration = rangeConfiguration;
	}

	/**
	 * Gets the configuration of the ranges of X/Y/Z axes.
	 * 
	 * @return The configuration of the ranges of X/Y/Z axes.
	 */
	public synchronized RangeConfiguration getRangeConfiguration() {
		return this.rangeConfiguration;
	}


	/**
	 * Checks whether any scale configuration is set to this instance.
	 * 
	 * @return Returns true if any scale configuration is set to this instance.
	 */
	public synchronized boolean hasScaleConfiguration() {
		return this.scaleConfiguration != null;
	}

	/**
	 * Sets the configuration of the scales of X/Y/Z axes.
	 * 
	 * @param scaleConfiguration The configuration of the scales of X/Y/Z axes.
	 */
	public synchronized void setScaleConfiguration(ScaleConfiguration scaleConfiguration) {
		this.scaleConfiguration = scaleConfiguration;
	}

	/**
	 * Gets the configuration of the scales of X/Y/Z axes.
	 * 
	 * @return The configuration of the scales of X/Y/Z axes.
	 */
	public synchronized ScaleConfiguration getScaleConfiguration() {
		return this.scaleConfiguration;
	}


	/**
	 * Checks whether any frame configuration is set to this instance.
	 * 
	 * @return Returns true if any frame configuration is set to this instance.
	 */
	public synchronized boolean hasFrameConfiguration() {
		return this.frameConfiguration != null;
	}

	/**
	 * Sets the configuration of the graph frame.
	 * 
	 * @param frameConfiguration The configuration of the graph frame.
	 */
	public synchronized void setFrameConfiguration(FrameConfiguration frameConfiguration) {
		this.frameConfiguration = frameConfiguration;
	}

	/**
	 * Gets the configuration of the graph frame.
	 * 
	 * @return The configuration of the graph frame.
	 */
	public synchronized FrameConfiguration getFrameConfiguration() {
		return this.frameConfiguration;
	}


	/**
	 * Checks whether any light configuration is set to this instance.
	 * 
	 * @return Returns true if any light configuration is set to this instance.
	 */
	public synchronized boolean hasLightConfiguration() {
		return this.lightConfiguration != null;
	}

	/**
	 * Sets the configuration of the lighting/shading parameters.
	 * 
	 * @param frameConfiguration The configuration of the lighting/shading parameters.
	 */
	public synchronized void setLightConfiguration(LightConfiguration lightConfiguration) {
		this.lightConfiguration = lightConfiguration;
	}

	/**
	 * Gets the configuration of the lighting/shading parameters.
	 * 
	 * @return The configuration of the lighting/shading parameters.
	 */
	public synchronized LightConfiguration getLightConfiguration() {
		return this.lightConfiguration;
	}


	/**
	 * Checks whether any camera configuration is set to this instance.
	 * 
	 * @return Returns true if any camera configuration is set to this instance.
	 */
	public synchronized boolean hasCameraConfiguration() {
		return this.cameraConfiguration != null;
	}

	/**
	 * Sets the configuration of the camera (angles, magnification, and so on).
	 * 
	 * @param frameConfiguration The configuration of the camera.
	 */
	public synchronized void setCameraConfiguration(CameraConfiguration cameraConfiguration) {
		this.cameraConfiguration = cameraConfiguration;
	}

	/**
	 * Gets the configuration of the camera (angles, magnification, and so on).
	 * 
	 * @return The configuration of the camera.
	 */
	public synchronized CameraConfiguration getCameraConfiguration() {
		return this.cameraConfiguration;
	}


	/**
	 * Checks whether any color configuration is set to this instance.
	 * 
	 * @return Returns true if any color configuration is set to this instance.
	 */
	public synchronized boolean hasColorConfiguration() {
		return this.colorConfiguration != null;
	}

	/**
	 * Sets the configuration of colors.
	 * 
	 * @param colorConfiguration The configuration of colors.
	 */
	public synchronized void setColorConfiguration(ColorConfiguration colorConfiguration) {
		this.colorConfiguration = colorConfiguration;
	}

	/**
	 * Gets the configuration of colors.
	 * 
	 * @return The configuration of colors.
	 */
	public synchronized ColorConfiguration getColorConfiguration() {
		return this.colorConfiguration;
	}
}
