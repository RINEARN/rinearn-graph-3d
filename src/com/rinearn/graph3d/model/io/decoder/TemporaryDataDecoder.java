package com.rinearn.graph3d.model.io.decoder;

import com.rinearn.graph3d.model.io.DataFileFormatException;
import com.rinearn.graph3d.model.dataseries.DataSeriesGroup;
import com.rinearn.graph3d.model.dataseries.ArrayDataSeries;
import com.rinearn.graph3d.def.ErrorType;
import com.rinearn.graph3d.RinearnGraph3DDataFileFormat;

import java.io.StringReader;
import java.io.BufferedReader;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;


/**
 * The temporary implementation of a decoder, decoding strings loaded from data files.
 * This decoder currently supports only three-columns and four-columns data file format.
 */
public final class TemporaryDataDecoder {

	/**
	 * Stores the parameters depending on data file formats.
	 */
	private final class DecodingParam {

		/** The delimiter of columns, depending the format. */
		public final String delimiter;

		/** The expected number of the columns. */
		public final int expectedColumnCount;

		/**
		 * Creates an instance storing the specified parameters.
		 *
		 * @param delimiter The delimiter of columns, depending the format.
		 * @param expectedColumnCount The expected number of the columns.
		 */
		public DecodingParam(String delimiter, int expectedColumnCount) {
			this.delimiter = delimiter;
			this.expectedColumnCount = expectedColumnCount;
		}
	}


	/**
	 * Stores the contents of a 'sub data series', which is separated by single empty data lines in a data file
	 * ('data series' are separated by double empty data lines).
	 */
	private final class SubDataSeries {

		/** Stores the values of the columns of all vertices composing this sub data series. */
		public final List<String[]> columnsList;

		/**
		 * Creates an instance storing the specified data values.
		 *
		 * @param columnsList The values of the columns of all vertices composing this sub data series.
		 */
		public SubDataSeries(List<String[]> columnsList) {
			this.columnsList = columnsList;
		}
	}


	/**
	 * Creates a new decoder.
	 */
	public TemporaryDataDecoder() {
	}


	/**
	 * Decodes the string loaded from a data file.
	 *
	 * @param dataString The string loaded from the data file.
	 * @param format The format of the data file.
	 * @return The DataSeriesGroup instance storing all data series contained in the data file.
	 * @throw DataFileFormatException
	 *   Thrown if the specified format is unsupported, or the contents is syntactically incorrect.
	 */
	public DataSeriesGroup decode(String dataString, RinearnGraph3DDataFileFormat format)
			throws DataFileFormatException {

		try (StringReader stringReader = new StringReader(dataString);
				BufferedReader bufferedReader = new BufferedReader(stringReader)) {

			DataSeriesGroup result = this.decode(bufferedReader, format);
			return result;

		} catch (IOException ioe) {
			throw new IllegalStateException(ioe);
		}
	}


	/**
	 * Decodes the content of a data file.
	 *
	 * @param bufferedReader The stream reading the content of the data file
	 * @param format The format of the data file.
	 * @return The DataSeriesGroup instance storing all data series contained in the data file.
	 * @throw DataFileFormatException
	 *   Thrown if the specified format is unsupported, or the contents is syntactically incorrect.
	 */
	public DataSeriesGroup decode(BufferedReader bufferedReader, RinearnGraph3DDataFileFormat format)
			throws DataFileFormatException, IOException {

		// Stores the decoded result to be returned.
		DataSeriesGroup dataSeriesGroup = new DataSeriesGroup();

		// Define the decoding parameters depending on the data file format.
		// If the format is unsupported by this decoder, DataFileFormatException occurs here.
		DecodingParam decodingParam = this.determineDecodingParam(format);

		// The list of the "sub data series", in the currently read "data series".
		// (A data series is composed of multiple sub data series.)
		List<SubDataSeries> subDataSeriesList = new ArrayList<SubDataSeries>();

		// The list of the value of the columns, in the currently read "sub data series".
		List<String[]> columnsList = new ArrayList<String[]>();

		// The counter which is incremented if the currently reading line is an empty data line,
		// and is reset to zero if it is not an empty data line.
		int emptyDataLineCount = 0;

		// The flag to switch continuing or breaking from the following reading loop.
		boolean continuesReading = true;

		// Read the all lines in the file.
		while (continuesReading) {
			String line = bufferedReader.readLine().trim();

			// The end of the file.
			if (line == null) {
				break;
			}

			// An empty data line: represents a separator of multiple "data series" or "sub data series".
			if (this.isEmptyDataLine(line, decodingParam)) {
				emptyDataLineCount++;

				// Single empty data lines: a separator of "sub data series".
				if (emptyDataLineCount == 1) {
					SubDataSeries subDataSeries = new SubDataSeries(columnsList);
					subDataSeriesList.add(subDataSeries);
					columnsList = new ArrayList<String[]>();

				// Double empty data lines: a separator of "data series".
				} else if (emptyDataLineCount == 2) {
					ArrayDataSeries dataSeries = this.parseAndPackIntoDataSeries(subDataSeriesList);
					dataSeriesGroup.addDataSeries(dataSeries);
					subDataSeriesList = new ArrayList<SubDataSeries>();
					columnsList = new ArrayList<String[]>();

				// Triple or more empty data lines:
				} else {
					// Ignore.
				}
				continue;

			} else {
				emptyDataLineCount = 0;
			}

			// Split the line into the columns, check the number of them, and store them into the List.
			String[] columns = line.split(decodingParam.delimiter, -1);
			int columnCount = columns.length;
			if (columnCount != decodingParam.expectedColumnCount) {
				throw new DataFileFormatException(
						ErrorType.INCORREDCT_NUMBER_OF_COLUMNS_IN_DATA_FILE,
						Integer.toString(decodingParam.expectedColumnCount), Integer.toString(columnCount), format.toString()
				);
			}
			columnsList.add(columns);

		} // The end of the reading loop.

		return dataSeriesGroup;
	}


	/**
	 * Parses the columns of multiple sub data series, and packs the results into an ArrayDataSeries instance.
	 *
	 * @param subDataSeriesList The list of the multiple sub data series to be packed.
	 * @return The ArrayDataSeries instance storing the parsed result.
	 * @throw DataFileFormatException
	 *     Thrown if any non-number value is detected.
	 */
	private ArrayDataSeries parseAndPackIntoDataSeries(List<SubDataSeries> subDataSeriesList)
			throws DataFileFormatException {

		int subSeriesCount = subDataSeriesList.size();
		int columnCount = subDataSeriesList.get(0).columnsList.get(0).length;
		double[][][] coords = new double[columnCount][subSeriesCount][]; // [icolumn][isub][ivertex]

		// Parse all sub series.
		for (int isub=0; isub<subSeriesCount; isub++) {
			SubDataSeries subSeries = subDataSeriesList.get(isub);

			int vertexCount = subSeries.columnsList.size();
			for (int icolumn=0; icolumn<columnCount; icolumn++) {
				coords[icolumn][isub] = new double[vertexCount];
			}

			// Parse all the vertex coordinates.
			for (int ivertex=0; ivertex<vertexCount; ivertex++) {
				String[] columns = subSeries.columnsList.get(ivertex);

				// Parse each column's coordinate value of this vertex.
				for (int icolumn=0; icolumn<columnCount; icolumn++) {
					try {
						coords[icolumn][isub][ivertex] = Double.parseDouble(columns[0]);
					} catch (NumberFormatException nfe) {
						throw new DataFileFormatException(ErrorType.FAILED_TO_PARSE_NUMBER_IN_DATA_FILE, columns[0]);
					}
				}
			}
		}

		// The first three dimensions of "coords" array represent X, Y, and Z dimensions.
		// So rename them as:
		double[][] xCoords = coords[0];
		double[][] yCoords = coords[1];
		double[][] zCoords = coords[2];

		// Pack coords of extra dimensions into an array.
		int extraDimCount = columnCount - 3;
		double[][][] extraCoords = new double[extraDimCount][][];
		for (int iexdim=0; iexdim<extraDimCount; iexdim++) {
			extraCoords[iexdim] = coords[iexdim + 3];
		}

		// Pack the parsed result into an ArrayDataSeries instance.
		ArrayDataSeries dataSeries;
		if (columnCount == 3) {
			dataSeries = new ArrayDataSeries(xCoords, yCoords, zCoords);
		} else {
			dataSeries = new ArrayDataSeries(xCoords, yCoords, zCoords, extraCoords);
		}
		return dataSeries;
	}


	/**
	 * Checks whether the specified line represents an empty data line.
	 *
	 * A empty data line is an empty line, or a line consists of only delimiters.
	 *
	 * Single empty data lines represents a separator between multiple "sub data series" in a data file.
	 * Double empty data lines represents a separator between multiple "data series", composing of multiple sub data series.
	 *
	 * @param line
	 * @param decodingParam
	 * @return Returns true if the specified line is an empty data line.
	 */
	private boolean isEmptyDataLine(String line, DecodingParam decodingParam) {
		if (decodingParam.delimiter.equals(",")) {
			line = line.replaceAll(",", "");
		}
		return line.trim().isEmpty();
	}


	/**
	 * Determines decoding parameters depending on the data file format.
	 *
	 * @param format The format of the data file.
	 * @return The container storing the determined decoding parameters.
	 * @throw DataFileFormatException
	 *     Thrown if the specified format is unsupported.
	 */
	private DecodingParam determineDecodingParam(RinearnGraph3DDataFileFormat format)
			throws DataFileFormatException {

		// Define the delimiter of columns, depending the format.
		String delimiter = null;
		switch(format) {
			case THREE_COLUMNS_CSV:
			case FOUR_COLUMNS_CSV: {
				delimiter = ",";
				break;
			}
			case THREE_COLUMNS_STSV:
			case FOUR_COLUMNS_STSV: {
				delimiter = "\\s";
				break;
			}
			default: {
				throw new DataFileFormatException(
						ErrorType.UNSUPPORTED_DATA_FILE_FORMAT_FOR_THIS_DECODER, format.toString()
				);
			}
		}

		// Define the expected number of the columns.
		int expectedColumnCount = -1;
		switch(format) {
			case THREE_COLUMNS_CSV:
			case THREE_COLUMNS_STSV: {
				expectedColumnCount = 3;
				break;
			}
			case FOUR_COLUMNS_CSV:
			case FOUR_COLUMNS_STSV: {
				expectedColumnCount = 4;
				break;
			}
			default: {
				// This case must be detected in the default case of the previous switch statements.
				throw new IllegalStateException();
			}
		}

		return new DecodingParam(delimiter, expectedColumnCount);
	}

}
