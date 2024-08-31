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
			default : return "Unknown Error Type: " + errorType;
		}
	}
}
