package com.rinearn.graph3d.def;

import java.util.Locale;

/**
 * The class which defines communication messages.
 */
public final class CommunicationMessage {

	/** The locale to determine which language the communication message should be described in. */
	private static Locale locale = Locale.getDefault();


	/**
	 * Generates the communication message corresponding with the specified confirmation type, in the language of the specified locale.
	 *
	 * @param communicationType The type of the communication.
	 * @return The generated communication message.
	 */
	public static String generateCommunicationMessage(CommunicationType communicationType) {
		return generateCommunicationMessage(communicationType, new String[0]);
	}


	/**
	 * Generates the communication message corresponding with the specified communication type, in the language of the specified locale.
	 *
	 * @param communicationType The type of the communication.
	 * @param word The word to be embedded in the communication message.
	 * @return The generated communication message.
	 */
	public static String generateCommunicationMessage(CommunicationType communicationType, String word) {
		return generateCommunicationMessage(communicationType, new String[] {word});
	}


	/**
	 * Generates the communication message corresponding with the specified error type, in the language of the specified locale.
	 *
	 * @param communicationType The type of the confirmation.
	 * @param words The words to be embedded in the communication message.
	 * @return The generated communication message.
	 */
	public static String generateCommunicationMessage(CommunicationType communicationType, String[] words) {

		// Generate the communication message in the language of the specified locale, and return it.
		if (   ( locale.getLanguage() != null && locale.getLanguage().toLowerCase().equals("ja") )
			   || ( locale.getCountry() != null && locale.getCountry().toLowerCase().equals("jp") )   ) {

			return generateCommunicationMessageJaJP(communicationType, words);
		} else {
			return generateCommunicationMessageEnUS(communicationType, words);
		}
	}


	/**
	 * Generates the communication message corresponding with the specified error type, in Japanese.
	 *
	 * @param communicationType The type of the communication.
	 * @param words The words to be embedded in the communication message.
	 * @return The generated communication message.
	 */
	public static String generateCommunicationMessageJaJP(CommunicationType communicationType, String[] words) {

		switch (communicationType) {
			case CHOOSE_DATA_FILES : return "データファイルを選択:（※ Ctrlキーを押しながら複数選択できます）";
			case SPECIFY_IMAGE_FILE : return "保存する画像ファイルを指定";
			case CHANGE_DATA_FORMATS_OF_ALL_LISTED_FILES : return "現在リストにある、全ファイルのデータ書式もまとめて変更しますか？";
			default : return "不明な種類のメッセージ：" + communicationType;
		}
	}


	/**
	 * Generates the error message corresponding with the specified error type, in US English.
	 *
	 * @param communicationType The type of the communication.
	 * @param words The words to be embedded in the communication message.
	 * @return The generated communication message.
	 */
	public static String generateCommunicationMessageEnUS(CommunicationType communicationType, String[] words) {

		switch (communicationType) {
			case CHOOSE_DATA_FILES : return "Choose the Data Files to Open: (Hold down the \"Ctrl\" key to select multiple files.)";
			case SPECIFY_IMAGE_FILE : return "Specify the Image File to Save";
			case CHANGE_DATA_FORMATS_OF_ALL_LISTED_FILES : return "Do you want to change the data formats of all the files in the list at once?";
			default : return "Unknown Communication Message Type: " + communicationType;
		}
	}
}
