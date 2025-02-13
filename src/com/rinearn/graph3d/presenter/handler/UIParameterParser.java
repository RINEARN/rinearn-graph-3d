package com.rinearn.graph3d.presenter.handler;

import com.rinearn.graph3d.config.environment.EnvironmentConfiguration;
import com.rinearn.graph3d.def.ErrorMessage;
import com.rinearn.graph3d.def.ErrorType;

import java.math.BigDecimal;

import javax.swing.JOptionPane;


/**
 * The parser of parameters which are input to UI component.
 */
public final class UIParameterParser {

	/**
	 * The exception thrown when the parsing has failed.
	 */
	public static class ParsingException extends Exception {
		private static final long serialVersionUID = 2231490134110394239L;

		/**
		 * Creates a new instance having the specified error message.
		 *
		 * @param errorMessage The error message.
		 */
		public ParsingException(String errorMessage) {
			super(errorMessage);
		}
	}


	/**
	 * Parses the specified text parameter as a double-type scalar value.
	 *
	 * @param text The text to be parsed.
	 * @param parameterNameEn The parameter name in Japanese.
	 * @param parameterNameJa The parameter name in English.
	 * @param min The minimum acceptable value of the parameter.
	 * @param max The maximum acceptable value of the parameter.
	 * @param envConfig The environment configuration, used for detecting the language of the environment's locale.
	 * @return The parsed result.
	 * @throws ParsingException Thrown if the parsing failed (thrown after showing the pop-up error message to the user).
	 */
	public static double parseDoubleParameter(
			String text, String parameterNameEn, String parameterNameJa, double min, double max, EnvironmentConfiguration envConfig)
					throws ParsingException {

		boolean isJapanese = envConfig.isLocaleJapanese();
		double result = Double.NaN;

		// Parse:
		try {
			result = Double.parseDouble(text);
		} catch (NumberFormatException nfe) {
			String[] errorWords = isJapanese ? new String[]{ parameterNameJa } : new String[]{ parameterNameEn };
			String errorMessage = ErrorMessage.generateErrorMessage(ErrorType.DOUBLE_PARAMETER_PARSING_FAILED, errorWords);
			JOptionPane.showMessageDialog(null, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
			throw new ParsingException(errorMessage);
		}

		// Check range:
		if (result < min || max < result) {
			String[] errorWords = isJapanese ?
					new String[]{ parameterNameEn, Double.toString(min), Double.toString(max) } :
					new String[]{ parameterNameEn, Double.toString(min), Double.toString(max) };
			String errorMessage = ErrorMessage.generateErrorMessage(ErrorType.DOUBLE_PARAMETER_OUT_OF_RANGE, errorWords);
			JOptionPane.showMessageDialog(null, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
			throw new ParsingException(errorMessage);
		}
		return result;
	}


	/**
	 * Parses the specified text parameter as a BigDecimal-type scalar value.
	 *
	 * @param text The text to be parsed.
	 * @param parameterNameEn The parameter name in Japanese.
	 * @param parameterNameJa The parameter name in English.
	 * @param min The minimum acceptable value of the parameter.
	 * @param max The maximum acceptable value of the parameter.
	 * @param envConfig The environment configuration, used for detecting the language of the environment's locale.
	 * @return The parsed result.
	 * @throws ParsingException Thrown if the parsing failed (thrown after showing the pop-up error message to the user).
	 */
	public static BigDecimal parseBigDecimalParameter(
			String text, String parameterNameEn, String parameterNameJa, boolean checkRange, BigDecimal min, BigDecimal max, EnvironmentConfiguration envConfig)
					throws ParsingException {

		boolean isJapanese = envConfig.isLocaleJapanese();
		BigDecimal result = null;

		// Parse:
		try {
			result = new BigDecimal(text);
		} catch (NumberFormatException nfe) {
			String[] errorWords = isJapanese ? new String[]{ parameterNameJa } : new String[]{ parameterNameEn };
			String errorMessage = ErrorMessage.generateErrorMessage(ErrorType.DOUBLE_PARAMETER_PARSING_FAILED, errorWords);
			JOptionPane.showMessageDialog(null, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
			throw new ParsingException(errorMessage);
		}

		// Check range:
		if (checkRange) {
			if (result.compareTo(min) < 0 || 0 < result.compareTo(max)) {
				String[] errorWords = isJapanese ?
						new String[]{ parameterNameEn, min.toString(), max.toString() } :
						new String[]{ parameterNameEn, min.toString(), max.toString() };
				String errorMessage = ErrorMessage.generateErrorMessage(ErrorType.DOUBLE_PARAMETER_OUT_OF_RANGE, errorWords);
				JOptionPane.showMessageDialog(null, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
				throw new ParsingException(errorMessage);
			}
		}
		return result;
	}


	/**
	 * Parses the specified text parameter as a int-type scalar value.
	 *
	 * @param text The text to be parsed.
	 * @param parameterNameEn The parameter name in Japanese.
	 * @param parameterNameJa The parameter name in English.
	 * @param min The minimum acceptable value of the parameter.
	 * @param max The maximum acceptable value of the parameter.
	 * @param envConfig The environment configuration, used for detecting the language of the environment's locale.
	 * @return The parsed result.
	 * @throws ParsingException Thrown if the parsing failed (thrown after showing the pop-up error message to the user).
	 */
	public static int[] parseCommaSeparatedIntParameters(
			String text, String parameterNameEn, String parameterNameJa, int min, int max, EnvironmentConfiguration envConfig)
					throws ParsingException {

		String[] valueTexts = text.trim().split(",");
		int valueCount = valueTexts.length;
		int[] values = new int[valueCount];
		boolean isJapanese = envConfig.isLocaleJapanese();

		for (int ivalue=0; ivalue<valueCount; ivalue++) {

			// Parse:
			try {
				values[ivalue] = Integer.parseInt(valueTexts[ivalue].trim());
			} catch (NumberFormatException nfe) {
				String[] errorWords = isJapanese ? new String[]{ parameterNameJa } : new String[]{ parameterNameEn };
				String errorMessage = ErrorMessage.generateErrorMessage(ErrorType.COMMA_SEPARATED_INT_PARAMETER_PARSING_FAILED, errorWords);
				JOptionPane.showMessageDialog(null, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
				throw new ParsingException(errorMessage);
			}

			// Check range:
			if (values[ivalue] < min || max < values[ivalue]) {
				String[] errorWords = isJapanese ?
						new String[]{ parameterNameJa, Integer.toString(min), Integer.toString(max) } :
						new String[]{ parameterNameEn, Integer.toString(min), Integer.toString(max) };
				String errorMessage = ErrorMessage.generateErrorMessage(ErrorType.COMMA_SEPARATED_INT_PARAMETER_OUT_OF_RANGE, errorWords);
				JOptionPane.showMessageDialog(null, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
				throw new ParsingException(errorMessage);
			}
		}
		return values;
	}


	/**
	 * Parses the specified text parameter as comma-separated String values.
	 *
	 * @param text The text to be parsed.
	 * @param parameterNameEn The parameter name in Japanese.
	 * @param parameterNameJa The parameter name in English.
	 * @param envConfig The environment configuration, used for detecting the language of the environment's locale.
	 * @return The parsed result.
	 * @throws ParsingException Thrown if the parsing failed (thrown after showing the pop-up error message to the user).
	 */
	public static String[] parseCommaSeparatedStringParameters(
			String text, String parameterNameEn, String parameterNameJa, EnvironmentConfiguration envConfig)
					throws ParsingException {

		// Replace the escaped comma "\," to "___ESCAPED_COMMA___".
		String escapedCommaReplacedText = text.trim().replaceAll("\\\\,", "___ESCAPED_COMMA___");

		// Split the text by commas ",".
		String[] values = escapedCommaReplacedText.split(",");
		int valueCount = values.length;

		// Recover the replaced escaped commas.
		for (int ivalue=0; ivalue<valueCount; ivalue++) {
			values[ivalue] = values[ivalue].replaceAll("___ESCAPED_COMMA___", "\\,").trim();
		}
		return values;
	}
}

