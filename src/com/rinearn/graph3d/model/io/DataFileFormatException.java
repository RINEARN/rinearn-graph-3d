package com.rinearn.graph3d.model.io;

import com.rinearn.graph3d.def.ErrorType;
import com.rinearn.graph3d.def.ErrorMessage;

/**
 * The Exception thrown when syntactic errors are detected for contents of data files.
 */
public class DataFileFormatException extends Exception {

	/** The type of the error cause.  */
	private final ErrorType errorType;

	/**
	 * Creates a new exception corresponding to the specified error type.
	 *
	 * @param errorType The type of the error cause.
	 */
	public DataFileFormatException(ErrorType errorType) {
		this.errorType = errorType;
	}

	/**
	 * Generates the error message.
	 *
	 * @return The error message.
	 */
	@Override
	public String getMessage() {
		return ErrorMessage.generateErrorMessage(this.errorType);
	}
}
