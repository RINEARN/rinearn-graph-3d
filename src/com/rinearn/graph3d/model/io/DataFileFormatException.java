package com.rinearn.graph3d.model.io;

import com.rinearn.graph3d.def.ErrorType;
import com.rinearn.graph3d.def.ErrorMessage;

/**
 * The Exception thrown when syntactic errors are detected for contents of data files.
 */
public class DataFileFormatException extends Exception {

	private static final long serialVersionUID = 8061758703491486580L;

	/**
	 * Creates a new exception corresponding to the specified error type.
	 *
	 * @param errorType The type of the error cause.
	 * @param errorWords The words to be embedded into the error message.
	 */
	public DataFileFormatException(ErrorType errorType, String ... errorWords) {
		super(ErrorMessage.generateErrorMessage(errorType, errorWords));
	}
}
