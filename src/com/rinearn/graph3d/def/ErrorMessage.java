package com.rinearn.graph3d.def;

import java.util.Locale;

/**
 * The class which defines error messages.
 */
public final class ErrorMessage {

	/** The locale to determine which language the error message should be described in. */
	private static Locale locale = Locale.getDefault();


	/**
	 * Generates the error message corresponding with the specified error type, in the language of the specified locale.
	 *
	 * @param errorType The type of the error.
	 * @return The generated error message.
	 */
	public static String generateErrorMessage(ErrorType errorType) {
		return generateErrorMessage(errorType, new String[0]);
	}


	/**
	 * Generates the error message corresponding with the specified error type, in the language of the specified locale.
	 *
	 * @param errorType The type of the error.
	 * @param word The word to be embedded in the error message.
	 * @return The generated error message.
	 */
	public static String generateErrorMessage(ErrorType errorType, String word) {
		return generateErrorMessage(errorType, new String[] {word});
	}


	/**
	 * Generates the error message corresponding with the specified error type, in the language of the specified locale.
	 *
	 * @param errorType The type of the error.
	 * @param words The words to be embedded in the error message.
	 * @return The generated error message.
	 */
	public static String generateErrorMessage(ErrorType errorType, String[] words) {

		// Generate the error message in the language of the specified locale, and return it.
		if (   ( locale.getLanguage() != null && locale.getLanguage().toLowerCase().equals("ja") )
			   || ( locale.getCountry() != null && locale.getCountry().toLowerCase().equals("jp") )   ) {

			return generateErrorMessageJaJP(errorType, words);
		} else {
			return generateErrorMessageEnUS(errorType, words);
		}
	}


	/**
	 * Generates the error message corresponding with the specified error type, in Japanese.
	 *
	 * @param errorType The type of the error.
	 * @param words The words to be embedded in the error message (For example: the name of the variable for a "variable not found" error).
	 * @return The generated error message.
	 */
	public static String generateErrorMessageJaJP(ErrorType errorType, String[] words) {

		switch (errorType) {
			case UNSUPPORTED_DATA_FILE_FORMAT_FOR_THIS_DECODER : return "このデコーダは、指定されたデータファイル書式「" + words[0] + "」に対応していません。";
			case INCORREDCT_NUMBER_OF_COLUMNS_IN_DATA_FILE : return "このデータの列数は " + words[0] + " であるべきですが、" + words[1] + " 列の行が含まれています。（データファイル書式: " + words[2] + "）";
			case FAILED_TO_PARSE_NUMBER_IN_DATA_FILE : return "データ内に記述されている値「" + words[0] + "」を、数値として解釈できませんでした。";
			case FAILED_TO_LOAD_DATA_FILE: return "データファイル「" + words[0] + "」の読み込みに失敗しました。";
			case DATA_FILE_NOT_FOUND: return "データファイル「" + words[0] + "」が見つかりません。";
			case FAILED_TO_INFER_DELIMITER_OF_DATA_FILE: return "データファイル内の区切り文字（コンマや空白など）を自動判定できませんでした。";
			case TOO_SHORT_DATA_FILE: return "データファイルの行数が短すぎます。";
			case DEFICIENT_COLUMNS_IN_DATA_FILE: return "データファイルの列数が少なすぎます。";
			case INVARID_MATRIX_SERIES_INDEX: return "複数系列のマトリックス書式において、解釈不能の系列インデックス \"" + words[0] + "\" が使用されています。";
			case SINGLE_EMPTY_LINE_SEPARATED_MATRICES_IS_NOT_SUPPORTED : return "マトリックス書式で、空白行によって系列を区切る場合は、1連続ではなく2連続の空白で区切る必要があります。";
			case FAILED_TO_COPY_IMAGE_TO_CLIPBOARD : return "画像をコピーできませんでした。\\nコピー機能は、環境や設定などによっては使用できない場合があります。";
			case INVALID_IMAGE_FILE_QUALITY : return "画質の値が想定外です。画質は、0 ～ 100 までの値を数値で指定してください。";
			case FAILED_TO_SAVE_IMAGE: return "画像の保存に失敗しました。\n（I/Oエラー）";
			default : return "不明なエラー種類：" + errorType;
		}
	}


	/**
	 * Generates the error message corresponding with the specified error type, in US English.
	 *
	 * @param errorType The type of the error.
	 * @param words The words to be embedded in the error message (For example: the name of the variable for a "variable not found" error).
	 * @return The generated error message.
	 */
	public static String generateErrorMessageEnUS(ErrorType errorType, String[] words) {

		switch (errorType) {
			case UNSUPPORTED_DATA_FILE_FORMAT_FOR_THIS_DECODER : return "This decoder does not support the specified data file format: " + words[0] + ".";
			case INCORREDCT_NUMBER_OF_COLUMNS_IN_DATA_FILE : return "The number of the columns in this data is expected to be \"" + words[0] + "\", but a line(s) having " + words[1] + "-columns is contained. (The data file format: " + words[2] + ")";
			case FAILED_TO_PARSE_NUMBER_IN_DATA_FILE : return "Failed to parse the value \"" + words[0] + "\", which is described in the data, as a number.";
			case FAILED_TO_LOAD_DATA_FILE: return "Failed to load the data file \"" + words[0] + "\".";
			case DATA_FILE_NOT_FOUND: return "The data file \"" + words[0] + "\" is not found.";
			case FAILED_TO_INFER_DELIMITER_OF_DATA_FILE: return "Failed to infer the delimiter (comma, space, etc.) of data from the content of the file.";
			case TOO_SHORT_DATA_FILE: return "The length (line count) of the data file is too short.";
			case DEFICIENT_COLUMNS_IN_DATA_FILE: return "The number of columns of the data file is deficient.";
			case INVARID_MATRIX_SERIES_INDEX: return "Invarid data series index \"" + words[0] + "\" is used in the multi-series matrix format data file.";
			case SINGLE_EMPTY_LINE_SEPARATED_MATRICES_IS_NOT_SUPPORTED : return "To split data series in matrix format, use double empty lines, not a single empty line.";
			case FAILED_TO_COPY_IMAGE_TO_CLIPBOARD : return "Failed to copy the image to the clipboard.\nThe clipboard may be unavailable depending on your environment/settings.";
			case INVALID_IMAGE_FILE_QUALITY : return "The value of the \"Quality\" is invalid.\nPlease specify a number from 0 to 100.";
			case FAILED_TO_SAVE_IMAGE: return "Failed to save the image file.\n (I/O error)";
			default : return "Unknown Error Type: " + errorType;
		}
	}
}
