package com.rinearn.graph3d.view;

import com.rinearn.graph3d.RinearnGraph3DDataFileFormat;

/**
 * The class defining the display names of data file formats on UI,
 * used in DataFileOpeningWindow and DataTextPastingWindow.
 */
public class FormatDisplayNames {

	/** The display name in English of: AUTO */
	public static final String AUTO_EN = "AUTO";

	/** The display name in English of: AUTO */
	public static final String AUTO_JA = "自動識別";

	/** The display name in English of: SPREADSHEET */
	public static final String SPREADSHEET_EN = "Copied from Spreadsheets";

	/** The display name in English of: SPREADSHEET */
	public static final String SPREADSHEET_JA = "表計算ソフトからのコピー";

	/** The display name in English of: 3-COLUMNS CSV */
	public static final String THREE_COLUMN_CSV_EN = "3-COLUMN CSV (Comma Separated)";

	/** The display name in Japanese of: 3-COLUMNS CSV */
	public static final String THREE_COLUMN_CSV_JA = "3カラム CSV (カンマ区切り)";

	/** The display name in English of: 3-COLUMNS STSV.  */
	public static final String THREE_COLUMN_STSV_EN = "3-COLUMN STSV (Space/Tab Separated)";

	/** The display name in Japanese of: 3-COLUMNS STSV.  */
	public static final String THREE_COLUMN_STSV_JA = "3カラム STSV (スペース/タブ区切り)";

	/** The display name in English of: 3-COLUMNS TSV.  */
	public static final String THREE_COLUMN_TSV_EN = "3-COLUMN TSV (Tab Separated)";

	/** The display name in Japanese of: 3-COLUMNS TSV.  */
	public static final String THREE_COLUMN_TSV_JA = "3カラム TSV (厳密なタブ区切り)";

	/** The display name in English of: 4-COLUMNS CSV */
	public static final String FOUR_COLUMN_CSV_EN = "4-COLUMN CSV (Comma Separated)";

	/** The display name in Japanese of: 4-COLUMNS CSV */
	public static final String FOUR_COLUMN_CSV_JA = "4カラム CSV (カンマ区切り)";

	/** The display name in English of: 4-COLUMNS STSV.  */
	public static final String FOUR_COLUMN_STSV_EN = "4-COLUMN STSV (Space/Tab Separated)";

	/** The display name in Japanese of: 4-COLUMNS STSV.  */
	public static final String FOUR_COLUMN_STSV_JA = "4カラム STSV (スペース/タブ区切り)";

	/** The display name in English of: 4-COLUMNS TSV.  */
	public static final String FOUR_COLUMN_TSV_EN = "4-COLUMN TSV (Tab Separated)";

	/** The display name in Japanese of: 4-COLUMNS TSV.  */
	public static final String FOUR_COLUMN_TSV_JA = "4カラム TSV (厳密なタブ区切り)";

	/** The display name in English of: MATRIX CSV */
	public static final String MATRIX_CSV_EN = "MATRIX CSV (Comma Separated)";

	/** The display name in Japanese of: MATRIX CSV */
	public static final String MATRIX_CSV_JA = "マトリックス CSV (カンマ区切り)";

	/** The display name in English of: MATRIX STSV.  */
	public static final String MATRIX_STSV_EN = "MATRIX STSV (Space/Tab Separated)";

	/** The display name in Japanese of: MATRIX STSV.  */
	public static final String MATRIX_STSV_JA = "マトリックス STSV (スペース/タブ区切り)";

	/** The display name in English of: MATRIX TSV.  */
	public static final String MATRIX_TSV_EN = "MATRIX TSV (Tab Separated)";

	/** The display name in English of: MATRIX TSV.  */
	public static final String MATRIX_TSV_JA = "マトリックス TSV (厳密なタブ区切り)";


	/**
	 * Converts the specified display name to the corresponding element of RinearnGraph3DDataFileFormat enum.
	 *
	 * @param displayName The display name of the data file format to be converted.
	 * @return The corresponding element of RinearnGraph3DDataFileFormat enum.
	 */
	public static final RinearnGraph3DDataFileFormat toRinearnGraph3DDataFileFormat(String displayName) {
		switch (displayName) {
			case FormatDisplayNames.AUTO_EN:
			case FormatDisplayNames.AUTO_JA: {
				return RinearnGraph3DDataFileFormat.AUTO;
			}
			case FormatDisplayNames.SPREADSHEET_EN:
			case FormatDisplayNames.SPREADSHEET_JA: {
				return RinearnGraph3DDataFileFormat.MATRIX_TSV;
			}
			case FormatDisplayNames.THREE_COLUMN_CSV_EN:
			case FormatDisplayNames.THREE_COLUMN_CSV_JA: {
				return RinearnGraph3DDataFileFormat.THREE_COLUMN_CSV;
			}
			case FormatDisplayNames.THREE_COLUMN_STSV_EN:
			case FormatDisplayNames.THREE_COLUMN_STSV_JA: {
				return RinearnGraph3DDataFileFormat.THREE_COLUMN_STSV;
			}
			case FormatDisplayNames.THREE_COLUMN_TSV_EN:
			case FormatDisplayNames.THREE_COLUMN_TSV_JA: {
				return RinearnGraph3DDataFileFormat.THREE_COLUMN_TSV;
			}
			case FormatDisplayNames.FOUR_COLUMN_CSV_EN:
			case FormatDisplayNames.FOUR_COLUMN_CSV_JA: {
				return RinearnGraph3DDataFileFormat.FOUR_COLUMN_CSV;
			}
			case FormatDisplayNames.FOUR_COLUMN_STSV_EN:
			case FormatDisplayNames.FOUR_COLUMN_STSV_JA: {
				return RinearnGraph3DDataFileFormat.FOUR_COLUMN_STSV;
			}
			case FormatDisplayNames.FOUR_COLUMN_TSV_EN:
			case FormatDisplayNames.FOUR_COLUMN_TSV_JA: {
				return RinearnGraph3DDataFileFormat.FOUR_COLUMN_TSV;
			}
			case FormatDisplayNames.MATRIX_CSV_EN:
			case FormatDisplayNames.MATRIX_CSV_JA: {
				return RinearnGraph3DDataFileFormat.MATRIX_CSV;
			}
			case FormatDisplayNames.MATRIX_STSV_EN:
			case FormatDisplayNames.MATRIX_STSV_JA: {
				return RinearnGraph3DDataFileFormat.MATRIX_STSV;
			}
			case FormatDisplayNames.MATRIX_TSV_EN:
			case FormatDisplayNames.MATRIX_TSV_JA: {
				return RinearnGraph3DDataFileFormat.MATRIX_TSV;
			}
			default : {
				throw new IllegalStateException("Unexpected data file format: " + displayName);
			}
		}
	}
}
