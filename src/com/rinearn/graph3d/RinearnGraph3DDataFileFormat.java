package com.rinearn.graph3d;


/**
 * <span class="lang-ja">
 * リニアングラフ3D（RINEARN Graph 3D）でサポートされている, データファイルの書式を表す列挙型です
 * </span>
 * <span class="lang-en">
 * The enum representing the data file formats supported on RINEARN Graph 3D
 * </span>
 * .
 * <div class="lang-ja">
 * この列挙型は, 主にデータファイルを開く
 * {@link com.rinearn.graph3d.RinearnGraph3D#openDataFile RinearnGraph3D.openDataFile} メソッドの引数として, 書式を指定するために使用します.
 * それぞれの書式の詳細については, リニアングラフ3Dのユーザーガイドにおける
 * <a href="https://www.rinearn.com/ja-jp/graph3d/guide/file">座標値ファイル書式</a> の説明をご参照ください.
 * </div>
 *
 * <div class="lang-en">
 * This enum is mainly used as an argument of {@link com.rinearn.graph3d.RinearnGraph3D#openDataFile RinearnGraph3D.openDataFile} method,
 * for specifying the format of the data file to be opened.
 * For datails of each data file format,
 * see the following page in the user's guide of the RINEARN Graph 3D: '<a href="https://www.rinearn.com/en-us/graph3d/guide/file">Data File Formats</a>'.
 * </div>
 */
public enum RinearnGraph3DDataFileFormat {

	/**
	 * <span class="lang-ja">自動で書式を判定するための, 特別な値です</span>
	 * <span class="lang-en">The meta value for detecting the file format automatically</span>
	 * .
	 */
	AUTO,

	/**
	 * <span class="lang-ja">3カラム書式の, カンマ区切りファイルを表します</span>
	 * <span class="lang-en">Represents a comma-separated file of the three-columns format</span>
	 * .
	 */
	THREE_COLUMNS_CSV,

	/**
	 * <span class="lang-ja">3カラム書式の, タブ区切りファイルを表します</span>
	 * <span class="lang-en">Represents a tab-separated file of the three-columns format</span>
	 * .
	 */
	THREE_COLUMNS_TSV,

	/**
	 * <span class="lang-ja">3カラム書式の, タブ/スペース区切りファイルを表します</span>
	 * <span class="lang-en">Represents a space/tab-separated file of the three-columns format</span>
	 * .
	 */
	THREE_COLUMNS_STSV,

	/**
	 * <span class="lang-ja">4カラム書式の, カンマ区切りファイルを表します</span>
	 * <span class="lang-en">Represents a comma-separated file of the four-columns format</span>
	 * .
	 */
	FOUR_COLUMNS_CSV,

	/**
	 * <span class="lang-ja">4カラム書式の, タブ区切りファイルを表します</span>
	 * <span class="lang-en">Represents a tab-separated file of the four-columns format</span>
	 * .
	 */
	FOUR_COLUMNS_TSV,

	/**
	 * <span class="lang-ja">4カラム書式の, タブ/スペース区切りファイルを表します</span>
	 * <span class="lang-en">Represents a space/tab-separated file of the four-columns format</span>
	 * .
	 */
	FOUR_COLUMNS_STSV,

	/**
	 * <span class="lang-ja">マトリックス書式の, カンマ区切りファイルを表します</span>
	 * <span class="lang-en">Represents a comma-separated file of the matrix format</span>
	 * .
	 */
	MATRIX_CSV,

	/**
	 * <span class="lang-ja">マトリックス書式の, タブ区切りファイルを表します</span>
	 * <span class="lang-en">Represents a tab-separated file of the matrix format</span>
	 * .
	 */
	MATRIX_TSV,

	/**
	 * <span class="lang-ja">マトリックス書式の, タブ/スペース区切りファイルを表します</span>
	 * <span class="lang-en">Represents a space/tab-separated file of the matrix format</span>
	 * .
	 */
	MATRIX_STSV,
}


