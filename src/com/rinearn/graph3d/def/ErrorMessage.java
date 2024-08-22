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
			default : return "Unknown Error Type: " + errorType;
		}
	}
}
