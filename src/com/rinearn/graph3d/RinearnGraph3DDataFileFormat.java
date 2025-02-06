package com.rinearn.graph3d;

import java.util.Map;
import java.util.HashMap;

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
	 * <span class="lang-ja">ファイル内に何もデータが記載されていない事を表す, 特別な値です</span>
	 * <span class="lang-en">The meta value representing no data described in the file</span>
	 * .
	 */
	NONE,

	/**
	 * <span class="lang-ja">3カラム書式の, カンマ区切りファイルを表します</span>
	 * <span class="lang-en">Represents a comma-separated file of the three-columns format</span>
	 * .
	 */
	THREE_COLUMN_CSV,
	/**
	 * @hidden
	 */
	THREE_COLUMNS_CSV,

	/**
	 * <span class="lang-ja">3カラム書式の, タブ区切りファイルを表します</span>
	 * <span class="lang-en">Represents a tab-separated file of the three-columns format</span>
	 * .
	 */
	THREE_COLUMN_TSV,
	/**
	 * @hidden
	 */
	THREE_COLUMNS_TSV,

	/**
	 * <span class="lang-ja">3カラム書式の, タブ/スペース区切りファイルを表します</span>
	 * <span class="lang-en">Represents a space/tab-separated file of the three-columns format</span>
	 * .
	 */
	THREE_COLUMN_STSV,
	/**
	 * @hidden
	 */
	THREE_COLUMNS_STSV,

	/**
	 * <span class="lang-ja">4カラム書式の, カンマ区切りファイルを表します</span>
	 * <span class="lang-en">Represents a comma-separated file of the four-columns format</span>
	 * .
	 */
	FOUR_COLUMN_CSV,
	/**
	 * @hidden
	 */
	FOUR_COLUMNS_CSV,

	/**
	 * <span class="lang-ja">4カラム書式の, タブ区切りファイルを表します</span>
	 * <span class="lang-en">Represents a tab-separated file of the four-columns format</span>
	 * .
	 */
	FOUR_COLUMN_TSV,
	/**
	 * @hidden
	 */
	FOUR_COLUMNS_TSV,

	/**
	 * <span class="lang-ja">4カラム書式の, タブ/スペース区切りファイルを表します</span>
	 * <span class="lang-en">Represents a space/tab-separated file of the four-columns format</span>
	 * .
	 */
	FOUR_COLUMN_STSV,
	/**
	 * @hidden
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
	MATRIX_STSV;

	/**
	 * The map normalizing items containing legacy ones (supported for keeping compatibility) to the recommended items in the current version.
	 */
	private static Map<RinearnGraph3DDataFileFormat, RinearnGraph3DDataFileFormat> normalizerMap
		= new HashMap<RinearnGraph3DDataFileFormat, RinearnGraph3DDataFileFormat>();
	static {
		normalizerMap.put(AUTO, AUTO);
		normalizerMap.put(NONE, NONE);
		normalizerMap.put(THREE_COLUMN_CSV, THREE_COLUMN_CSV);
		normalizerMap.put(THREE_COLUMNS_CSV, THREE_COLUMNS_CSV);
		normalizerMap.put(THREE_COLUMN_TSV, THREE_COLUMN_TSV);
		normalizerMap.put(THREE_COLUMNS_TSV, THREE_COLUMNS_TSV);
		normalizerMap.put(THREE_COLUMN_STSV, THREE_COLUMN_STSV);
		normalizerMap.put(THREE_COLUMNS_STSV, THREE_COLUMNS_STSV);
		normalizerMap.put(FOUR_COLUMN_CSV, FOUR_COLUMN_CSV);
		normalizerMap.put(FOUR_COLUMNS_CSV, FOUR_COLUMNS_CSV);
		normalizerMap.put(FOUR_COLUMN_TSV, FOUR_COLUMN_TSV);
		normalizerMap.put(FOUR_COLUMNS_TSV, FOUR_COLUMNS_TSV);
		normalizerMap.put(FOUR_COLUMN_STSV, FOUR_COLUMN_STSV);
		normalizerMap.put(FOUR_COLUMNS_STSV, FOUR_COLUMNS_STSV);
		normalizerMap.put(MATRIX_CSV, MATRIX_CSV);
		normalizerMap.put(MATRIX_TSV, MATRIX_TSV);
		normalizerMap.put(MATRIX_STSV, MATRIX_STSV);
	}

	/**
	 * Normalizes items containing legacy ones (supported for keeping compatibility) to the recommended items in the current version.
	 *
	 * @param item The item to be normalized.
	 * @return The normalized item.
	 */
	public static RinearnGraph3DDataFileFormat normalize(RinearnGraph3DDataFileFormat item) {
		return normalizerMap.get(item);
	}
}

