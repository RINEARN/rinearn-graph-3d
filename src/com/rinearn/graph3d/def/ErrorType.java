package com.rinearn.graph3d.def;


/**
 * The enum to distinguish types of errors.
 */
public enum ErrorType {
	UNSUPPORTED_DATA_FILE_FORMAT_FOR_THIS_DECODER,
	INCORREDCT_NUMBER_OF_COLUMNS_IN_DATA_FILE,
	FAILED_TO_PARSE_NUMBER_IN_DATA_FILE,
	FAILED_TO_LOAD_DATA_FILE,
	DATA_FILE_NOT_FOUND,
	FAILED_TO_INFER_DELIMITER_OF_DATA_FILE,
	TOO_SHORT_DATA_FILE,
	DEFICIENT_COLUMNS_IN_DATA_FILE,
	INVARID_MATRIX_SERIES_INDEX,
	SINGLE_EMPTY_LINE_SEPARATED_MATRICES_IS_NOT_SUPPORTED,
	FAILED_TO_COPY_IMAGE_TO_CLIPBOARD,
	INVALID_IMAGE_FILE_QUALITY,
	FAILED_TO_SAVE_IMAGE,
}
