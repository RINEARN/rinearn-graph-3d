package com.rinearn.graph3d.model.io;

import com.rinearn.graph3d.RinearnGraph3DDataFileFormat;
import com.rinearn.graph3d.def.ErrorType;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;


/**
 * The class to infer data file formats from the contents of data files.
 */
public class DataFileFormatInferencer {

	/**
	 * The enum representing delimiters, which are separators of columns in data files.
	 */
	private enum Delimiter {

		/** The delimiter of comma-separated files (= comma). */
		CSV,

		/** The delimiter of tab-separated files (= tab). */
		TSV,

		/** The delimiter of space-or-tab-separated files (sequential spaces and tabs). */
		STSV;
	}

	/**
	 * The enum representing alignments of data values in data files.
	 */
	private enum Alignment {

		/** The alignment of the 3-columns format. */
		THREE_COLUMNS,

		/** The alignment of the 4-columns format. */
		FOUR_COLUMNS,

		/** The alignment of the matrix format. */
		MATRIX;
	}


	/**
	 * Creates a new inferencer instance.
	 *
	 * @return The created inferencer instance.
	 */
	public DataFileFormatInferencer() {
	}


	/**
	 * Infers the data file format of the specified file.
	 *
	 * @param file The file for which the data file format is to be inferred.
	 * @return The inferred data file format.
	 * @throws IOException Thrown if it failed to read the contents of the file by any I/O error.
	 * @throws DataFileFormatException Thrown if the content of the file is syntactically incorrect.
	 */
	public RinearnGraph3DDataFileFormat infer(File file) throws IOException, DataFileFormatException {
		try (FileReader fileReader = new FileReader(file);
				BufferedReader bufferedReader = new BufferedReader(fileReader)) {

			return this.infer(bufferedReader);

		} catch (IOException ioe) {
			throw ioe;
		}
	}


	/**
	 * Infers the data file format from the content of the data file.
	 *
	 * @param bufferedReader The reader for reading the content from the data file.
	 * @return The inferred data file format.
	 * @throws IOException Thrown if it failed to read the contents of the file by any I/O error.
	 * @throws DataFileFormatException Thrown if the content of the file is syntactically incorrect.
	 */
	public RinearnGraph3DDataFileFormat infer(BufferedReader bufferedReader)
			throws IOException, DataFileFormatException {

		// Extract the first line in the data file, excluding empty lines and comment lines.
		String effectiveFirstLine = null;
		String currentReadingLine = null;
		while ((currentReadingLine = bufferedReader.readLine()) != null) {
			currentReadingLine = currentReadingLine.trim();
			if (currentReadingLine.isEmpty()) {
				continue;
			}
			if (currentReadingLine.startsWith("#")) {
				continue;
			}
			effectiveFirstLine = currentReadingLine;
			break;
		}

		// Infer the delimiter, which is the separator of the columns in the data file.
		Delimiter delimiter = this.inferDelimiter(effectiveFirstLine);

		// Infer the alignment of the data values.
		Alignment alignment = this.inferAlignment(effectiveFirstLine, delimiter);

		// The strict TSV mode has not been implemented yet in decoders, so handle it as STSV mode.
		if (delimiter == Delimiter.TSV) {
			delimiter = Delimiter.STSV;
		}

		// Determine the data file format corresponding the above delimiter and alignment,
		// and returns it.
		switch (alignment) {
			case THREE_COLUMNS : {
				if (delimiter == Delimiter.CSV) {
					return RinearnGraph3DDataFileFormat.THREE_COLUMNS_CSV;
				} else if (delimiter == Delimiter.STSV) {
					return RinearnGraph3DDataFileFormat.THREE_COLUMNS_STSV;
				} else {
					throw new IllegalArgumentException("Unexpected delimiter: " + delimiter);
				}
			}
			case FOUR_COLUMNS : {
				if (delimiter == Delimiter.CSV) {
					return RinearnGraph3DDataFileFormat.FOUR_COLUMNS_CSV;
				} else if (delimiter == Delimiter.STSV) {
					return RinearnGraph3DDataFileFormat.FOUR_COLUMNS_STSV;
				} else {
					throw new IllegalArgumentException("Unexpected delimiter: " + delimiter);
				}
			}
			case MATRIX : {
				if (delimiter == Delimiter.CSV) {
					return RinearnGraph3DDataFileFormat.MATRIX_CSV;
				} else if (delimiter == Delimiter.STSV) {
					return RinearnGraph3DDataFileFormat.MATRIX_STSV;
				} else {
					throw new IllegalArgumentException("Unexpected delimiter: " + delimiter);
				}
			}
			default : {
				throw new IllegalArgumentException("Unexpected alignment: " + alignment);
			}
		}
	}


	/**
	 * Infers the delimiter, which is the separator of the columns, in the data file.
	 *
	 * @param effectiveFirstLine The first line in the data file, excluding empty/comment lines.
	 * @return The inferred delimiter.]
	 * @throw DataFileFormatException Thrown if it can not infer the delimiter.
	 */
	private Delimiter inferDelimiter(String effectiveFirstLine) throws DataFileFormatException {

		// If the number of columns separated by comma is 3 or more,
		// and the first three column's values don't contain either tabs or spaces,
		// the result is CSV.
		String[] commaSeparatedColumns = effectiveFirstLine.split(",", -1);
		if (3 <= commaSeparatedColumns.length) {
			boolean containsTabsOrSpaces
					=  commaSeparatedColumns[0].contains(" ") || commaSeparatedColumns[0].contains("\t")
					|| commaSeparatedColumns[1].contains(" ") || commaSeparatedColumns[1].contains("\t")
					|| commaSeparatedColumns[2].contains(" ") || commaSeparatedColumns[2].contains("\t");

			if (containsTabsOrSpaces) {
				return Delimiter.CSV;
			}
		}

		// If the number of columns separated by comma is 3 or more,
		// and the first three column's values are empty (= sequential tabs exist) or contain spaces,
		// the result is STSV. Otherwise, the result is TSV.
		String tabSeparatedColumns[] = effectiveFirstLine.split("\t", -1);
		if (3 <= tabSeparatedColumns.length) {
			boolean containsSpaces
					=  tabSeparatedColumns[0].contains(" ")
					|| tabSeparatedColumns[1].contains(" ")
					|| tabSeparatedColumns[2].contains(" ");

			boolean containsEmpty
					=  tabSeparatedColumns[0].isEmpty()
					|| tabSeparatedColumns[1].isEmpty()
					|| tabSeparatedColumns[2].isEmpty();

			if (containsSpaces || containsEmpty) {
				return Delimiter.STSV;
			} else {
				return Delimiter.TSV;
			}
		}

		throw new DataFileFormatException(ErrorType.FAILED_TO_INFER_DELIMITER_OF_DATA_FILE);
	}


	/**
	 * Infers the alignment of the data values in the data file.
	 *
	 * @param effectiveFirstLine The first line in the data file, excluding empty/comment lines.
	 * @return The inferred alignment.
	 */
	private Alignment inferAlignment(String effectiveFirstLine, Delimiter delimiter) {
		String[] columns;
		if (delimiter == Delimiter.CSV) {
			columns = effectiveFirstLine.split(",", -1);
		} else if (delimiter == Delimiter.TSV) {
			columns = effectiveFirstLine.split("\t", -1);
		} else if (delimiter == Delimiter.STSV) {
			columns = effectiveFirstLine.split("\\s", -1);
		} else {
			throw new IllegalArgumentException("Unexpected delimiter: " + delimiter);
		}

		int columnCount = columns.length;

		if (columnCount == 3) {
			return Alignment.THREE_COLUMNS;
		} else if (columnCount == 4) {
			return Alignment.FOUR_COLUMNS;
		} else {
			return Alignment.MATRIX;
		}
	}
}
