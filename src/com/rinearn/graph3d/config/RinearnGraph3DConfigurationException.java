package com.rinearn.graph3d.config;


/**
 * The exception thrown when validations of various configuration containers fail.
 */
public class RinearnGraph3DConfigurationException extends Exception {
	private static final long serialVersionUID = -1713268452536417636L;

	/**
	 * Creates a new exception having the specified error message.
	 *
	 * @param message The error message of this exception.
	 */
	public RinearnGraph3DConfigurationException(String errorMmessage) {
		super(errorMmessage);
	}
}
