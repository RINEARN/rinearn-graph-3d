package com.rinearn.graph3d;


/**
 * <span class="lang-ja">
 * リニアングラフ3D（RINEARN Graph 3D）のプロットオプションを表す列挙型です
 * </span>
 * <span class="lang-en">
 * The enum representing the plotting options of RINEARN Graph 3D
 * </span>
 * .
 * <div class="lang-ja">
 * この列挙型は、主にデータファイルを開く
 * {@link com.rinearn.graph3d.RinearnGraph3D#setOptionSelected RinearnGraph3D.setOptionSelected} メソッドの引数として使用します。
 * </div>
 *
 * <div class="lang-en">
 * This enum is mainly used as an argument of {@link com.rinearn.graph3d.RinearnGraph3D#setOptionSelected RinearnGraph3D.setOptionSelected} method.
 * </div>
 */
public enum RinearnGraph3DOptionItem {

	/**
	 * <span class="lang-ja">「線プロット（With Lines）」オプションです</span>
	 * <span class="lang-en">Represents "With Lines" option</span>
	 * .
	 */
	LINE,

	/**
	 * <span class="lang-ja">「点プロット（With Points）」オプションです</span>
	 * <span class="lang-en">Represents "With Points" option</span>
	 * .
	 */
	POINT,

	/**
	 * <span class="lang-ja">「ドットプロット（With Dots）」オプションです</span>
	 * <span class="lang-en">Represents "With Dots" option</span>
	 * .
	 */
	DOT,

	/**
	 * <span class="lang-ja">「メッシュプロット（With Meshes）」オプションです</span>
	 * <span class="lang-en">Represents "With Meshes" option</span>
	 * .
	 */
	MESH,

	/**
	 * <span class="lang-ja">「曲面プロット（With Meshes）」オプションです</span>
	 * <span class="lang-en">Represents "With Membranes" option</span>
	 * .
	 */
	MEMBRANE,

	/**
	 * <span class="lang-ja">「等高線プロット（With Contours）」オプションです</span>
	 * <span class="lang-en">Represents "With Contours" option</span>
	 * .
	 */
	CONTOUR,

	/**
	 * <span class="lang-ja">「対数軸 X（Log X）」オプションです</span>
	 * <span class="lang-en">Represents "Log X" option</span>
	 * .
	 */
	LOG_X,

	/**
	 * <span class="lang-ja">「対数軸 Y（Log Y）」オプションです</span>
	 * <span class="lang-en">Represents "Log Y" option</span>
	 * .
	 */
	LOG_Y,

	/**
	 * <span class="lang-ja">「対数軸 Z（Log Z）」オプションです</span>
	 * <span class="lang-en">Represents "Log Z" option</span>
	 * .
	 */
	LOG_Z,

	/**
	 * <span class="lang-ja">「軸反転 X（Reverse X）」オプションです</span>
	 * <span class="lang-en">Represents "Reverse X" option</span>
	 * .
	 */
	REVERSE_X,

	/**
	 * <span class="lang-ja">「軸反転 Y（Reverse Y）」オプションです</span>
	 * <span class="lang-en">Represents "Reverse Y" option</span>
	 * .
	 */
	REVERSE_Y,

	/**
	 * <span class="lang-ja">「軸反転 Z（Reverse Z）」オプションです</span>
	 * <span class="lang-en">Represents "Reverse Z" option</span>
	 * .
	 */
	REVERSE_Z,

	/**
	 * <span class="lang-ja">「平面化（Flat）」オプションです</span>
	 * <span class="lang-en">Represents "Flat" option</span>
	 * .
	 */
	FLAT,

	/**
	 * <span class="lang-ja">「ブラックスクリーン（Black Screen）」オプションです</span>
	 * <span class="lang-en">Represents "Black Screen" option</span>
	 * .
	 */
	BLACK_SCREEN,

	/**
	 * <span class="lang-ja">「フレーム（Frame）」オプションです</span>
	 * <span class="lang-en">Represents "Frame" option</span>
	 * .
	 */
	FRAME,

	/**
	 * <span class="lang-ja">「グリッド（Grid）」オプションです</span>
	 * <span class="lang-en">Represents "Grid" option</span>
	 * .
	 */
	GRID,

	/**
	 * <span class="lang-ja">「目盛り（Scale）」オプションです</span>
	 * <span class="lang-en">Represents "Scale" option</span>
	 * .
	 */
	SCALE,

	/**
	 * <span class="lang-ja">「グラデーション（Gradation）」オプションです</span>
	 * <span class="lang-en">Represents "Gradation" option</span>
	 * .
	 */
	GRADATION,
}

