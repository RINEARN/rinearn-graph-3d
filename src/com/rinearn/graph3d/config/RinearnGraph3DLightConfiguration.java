package com.rinearn.graph3d.config;

// !!!!!
// Note:
//    Maybe we should this class to the upper hierarchy: com.rinear.graph3d.RinearnGraph3DLightingParameter?
//    Probably sometimes users want to set the lighting parameters simply
//    through the upper hierarchy (RinearnGraph3D class), without directly accessing to the renderer.
//    Considering such usage, it may be unnatural that this class belongs to renderer's package.
//!!!!!

// > あとこれ引数に詳細渡す的なやつじゃなくてずっと参照する設定値のコンテナなのでParameterって命名もなんか違う気がする。
//   OptionParametersやDrawingParameterに命名引っ張られてるけどあれらは引数が大量にできるのを包む的なやつなのでParameterなわけで。
//   こっちは上層に昇格さすなら、Set Light メニューの設定内容を包んで内部でずっと保持するようなやつになるので、役割的にParamでは済まない。
//   設定コンテナ的に Settings とかが適切では？ 他の設定メニューも似た感じのやつを量産するなら、Paramだとたぶんどっかで意味的に詰む。
//   例えば ScaleParameter とか作ると役割的にかなり違和感生じて、ScaleSettings とか or 別の命名にしたくなる気が。
// 
//   > Settingsは意味的に良いが複数形になる（単数だと単一の設定項目になる）ので、単数形で通用する Configuration の方が良いかもしれない。
//     意味的には粒度が大きすぎて少し違和感が出るけど、1インスタンスの変数名が複数形になる微妙さは避けられる。どっち優先すべきか。
//
//     > Parameter 系も、実質パラメータの集合なので本来意味的には Parameters になる所を、複数形を避けいので、
//       「まとめて1個の引数にしている」という意味付けで単数形の Parameter にした気がする。
//       なのでどうせならここでも複数形避ける事を優先した方がよさそうかも。Configuration の違和感もまあ許容範囲っちゃあ許容範囲のレベルなので
//
//   移動のタイミングまでに要件等


/**
 * <span class="lang-en">
 * The class for storing parameters related to lighting and shading
 * </span>
 * <span class="lang-ja">
 * 照明/陰影に関する設定を指定するためのパラメータオブジェクトです
 * </span>
 * .
 */
public class RinearnGraph3DLightConfiguration {

	/**
	 * <span class="lang-en">
	 * Creates a new instance for storing lighting parameters,
	 * and initializes the parameters by default values
	 * </span>
	 * <span class="lang-ja">
	 * 新しい照明設定パラメータを格納するインスタンスを作成し、
	 * 各パラメータをデフォルト値で初期化します。
	 * </span>
	 * .
	 */
	public RinearnGraph3DLightConfiguration() {
		
		// Initialize the reflection parameters by default values.
		this.ambientReflectionStrength = 0.6;
		this.diffuseReflectionStrength = 0.6;
		this.diffractiveReflectionStrength = 0.5;
		this.specularReflectionStrength = 0.5;
		this.specularReflectionAngle = 1.26;

		// Initialize the direction vector pointing to the light source, and normalize it.
		this.lightSourceDirectionVector = new double[] { -50.0, -10.0, 30.0 };
		double lightVectorLength = Math.sqrt(
				lightSourceDirectionVector[0] * lightSourceDirectionVector[0] +
				lightSourceDirectionVector[1] * lightSourceDirectionVector[1] +
				lightSourceDirectionVector[2] * lightSourceDirectionVector[2]				
		);
		lightSourceDirectionVector[0] /= lightVectorLength;
		lightSourceDirectionVector[1] /= lightVectorLength;
		lightSourceDirectionVector[2] /= lightVectorLength;
	}


	/**
	 * <span class="lang-en">Stores the strength of ambient reflection</span>
	 * <span class="lang-ja">環境光反射の強度を控えます</span>
	 * .
	 */
	private volatile double ambientReflectionStrength;

	/**
	 * <span class="lang-en">
	 * Sets the strength of ambient reflection
	 * </span>
	 * <span class="lang-ja">
	 * 環境光の反射強度を設定します
	 * </span>
	 * .
	 * @param strength
	 *   <span class="lang-en">The strength of ambient reflection</span>
	 *   <span class="lang-ja">環境光の反射強度</span>
	 */
	public synchronized void setAmbientReflectionStrength (double strength) {
		this.ambientReflectionStrength = strength;
	}

	/**
	 * <span class="lang-en">
	 * Gets the strength of ambient reflection
	 * </span>
	 * <span class="lang-ja">
	 * 環境光の反射強度を取得します
	 * </span>
	 * .
	 * @return
	 *   <span class="lang-en">The strength of ambient reflection</span>
	 *   <span class="lang-ja">環境光の反射強度</span>
	 */
	public synchronized double getAmbientReflectionStrength () {
		return this.ambientReflectionStrength;
	}


	/**
	 * <span class="lang-en">Stores the strength of diffuse reflection</span>
	 * <span class="lang-ja">拡散反射の強度を控えます</span>
	 * .
	 */
	private volatile double diffuseReflectionStrength;

	/**
	 * <span class="lang-en">
	 * Sets the strength of diffuse reflection
	 * </span>
	 * <span class="lang-ja">
	 * 拡散反射の強度を設定します
	 * </span>
	 * .
	 * @param strength
	 *   <span class="lang-en">The strength of diffuse reflection</span>
	 *   <span class="lang-ja">拡散反射の強度</span>
	 */
	public synchronized void setDiffuseReflectionStrength (double strength) {
		this.diffuseReflectionStrength = strength;
	}

	/**
	 * <span class="lang-en">
	 * Gets the strength of diffuse reflection
	 * </span>
	 * <span class="lang-ja">
	 * 拡散反射の強度を取得します
	 * </span>
	 * .
	 * @return
	 *   <span class="lang-en">The strength of diffuse reflection</span>
	 *   <span class="lang-ja">拡散反射の強度</span>
	 */
	public synchronized double getDiffuseReflectionStrength () {
		return this.diffuseReflectionStrength;
	}


	/**
	 * <span class="lang-en">Stores the strength of diffractive reflection</span>
	 * <span class="lang-ja">回折性反射の強度を控えます</span>
	 * .
	 */
	private volatile double diffractiveReflectionStrength;

	/**
	 * <span class="lang-en">
	 * Sets the strength of diffractive reflection
	 * </span>
	 * <span class="lang-ja">
	 * 回折性反射の強度を設定します
	 * </span>
	 * .
	 * @param strength
	 *   <span class="lang-en">The strength of diffractive reflection</span>
	 *   <span class="lang-ja">回折性反射の強度</span>
	 */
	public synchronized void setDiffractiveReflectionStrength (double strength) {
		this.diffractiveReflectionStrength = strength;
	}

	/**
	 * <span class="lang-en">
	 * Gets the strength of diffractive reflection
	 * </span>
	 * <span class="lang-ja">
	 * 回折性反射の強度を取得します
	 * </span>
	 * .
	 * @return
	 *   <span class="lang-en">The strength of diffractive reflection</span>
	 *   <span class="lang-ja">回折性反射の強度</span>
	 */
	public synchronized double getDiffractiveReflectionStrength () {
		return this.diffractiveReflectionStrength;
	}


	/**
	 * <span class="lang-en">Stores the strength of specular reflection</span>
	 * <span class="lang-ja">鏡面反射の強度を控えます</span>
	 * .
	 */
	private volatile double specularReflectionStrength;

	/**
	 * <span class="lang-en">
	 * Sets the strength of specular reflection
	 * </span>
	 * <span class="lang-ja">
	 * 鏡面反射の強度を設定します
	 * </span>
	 * .
	 * @param strength
	 *   <span class="lang-en">The strength of specular reflection</span>
	 *   <span class="lang-ja">鏡面反射の強度</span>
	 */
	public synchronized void setSpecularReflectionStrength (double strength) {
		this.specularReflectionStrength = strength;
	}

	/**
	 * <span class="lang-en">
	 * Gets the strength of specular reflection
	 * </span>
	 * <span class="lang-ja">
	 * 鏡面反射の強度を取得します
	 * </span>
	 * .
	 * @return
	 *   <span class="lang-en">The strength of specular reflection</span>
	 *   <span class="lang-ja">鏡面反射の強度</span>
	 */
	public synchronized double getSpecularReflectionStrength () {
		return this.specularReflectionStrength;
	}


	/**
	 * <span class="lang-en">Stores the spread angle of specular reflection</span>
	 * <span class="lang-ja">鏡面反射の拡がり角を控えます</span>
	 * .
	 */
	private volatile double specularReflectionAngle;

	/**
	 * <span class="lang-en">
	 * Sets the spread angle of specular reflection
	 * </span>
	 * <span class="lang-ja">
	 * 鏡面反射の拡がり角を設定します
	 * </span>
	 * .
	 * @param strength
	 *   <span class="lang-en">The spread angle (in radians) of specular reflection</span>
	 *   <span class="lang-ja">鏡面反射の拡がり角（ラジアン単位）</span>
	 */
	public synchronized void setSpecularReflectionAngle (double angle) {
		this.specularReflectionAngle = angle;
	}

	/**
	 * <span class="lang-en">
	 * Gets the spread angle of specular reflection
	 * </span>
	 * <span class="lang-ja">
	 * 鏡面反射の強度を取得します
	 * </span>
	 * .
	 * @return
	 *   <span class="lang-en">The spread angle (in radians) of specular reflection</span>
	 *   <span class="lang-ja">鏡面反射の拡がり角（ラジアン単位）</span>
	 */
	public synchronized double getSpecularReflectionAngle () {
		return this.specularReflectionAngle;
	}


	/**
	 * <span class="lang-en">Stores the direction vector pointing to the light source</span>
	 * <span class="lang-ja">光源の方向を指すベクトルを控えます</span>
	 * .
	 */
	private volatile double[] lightSourceDirectionVector;

	/**
	 * <span class="lang-en">Sets the direction of the light source</span>
	 * <span class="lang-ja">光源の方向を設定します</span>
	 * .
	 * @param x
	 *   <span class="lang-en">The X component of the direction vector pointing to the light source</span>
	 *   <span class="lang-ja">光源の方向を指すベクトルのX成分</span>
	 * @param y
	 *   <span class="lang-en">The Y component of the direction vector pointing to the light source</span>
	 *   <span class="lang-ja">光源の方向を指すベクトルのY成分</span>
	 * @param z
	 *   <span class="lang-en">The Z component of the direction vector pointing to the light source</span>
	 *   <span class="lang-ja">光源の方向を指すベクトルのZ成分</span>
	 */
	public synchronized void setLightSourceDirection(double x, double y, double z) {
		this.lightSourceDirectionVector = new double[] { x, y, z };
	}

	/**
	 * <span class="lang-en">Gets the X component of the direction vector pointing to the light source</span>
	 * <span class="lang-ja">光源の方向を指すベクトルのX成分を取得します</span>
	 * .
	 * @return
	 *   <span class="lang-en">The X component of the direction vector pointing to the light source</span>
	 *   <span class="lang-ja">光源の方向を指すベクトルのX成分</span>
	 */
	public synchronized double getLightSourceDirectionX() {
		return this.lightSourceDirectionVector[0];
	}

	/**
	 * <span class="lang-en">Gets the Y component of the direction vector pointing to the light source</span>
	 * <span class="lang-ja">光源の方向を指すベクトルのY成分を取得します</span>
	 * .
	 * @return
	 *   <span class="lang-en">The Y component of the direction vector pointing to the light source</span>
	 *   <span class="lang-ja">光源の方向を指すベクトルのY成分</span>
	 */
	public synchronized double getLightSourceDirectionY() {
		return this.lightSourceDirectionVector[1];
	}

	/**
	 * <span class="lang-en">Gets the Z component of the direction vector pointing to the light source</span>
	 * <span class="lang-ja">光源の方向を指すベクトルのZ成分を取得します</span>
	 * .
	 * @return
	 *   <span class="lang-en">The Z component of the direction vector pointing to the light source</span>
	 *   <span class="lang-ja">光源の方向を指すベクトルのZ成分</span>
	 */
	public synchronized double getLightSourceDirectionZ() {
		return this.lightSourceDirectionVector[2];
	}
}
