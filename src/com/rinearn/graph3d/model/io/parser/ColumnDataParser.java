package com.rinearn.graph3d.model.io.parser;

import com.rinearn.graph3d.model.data.series.ArrayDataSeries;
import com.rinearn.graph3d.model.data.series.DataSeriesGroup;
import com.rinearn.graph3d.model.io.DataFileFormatException;
import com.rinearn.graph3d.def.ErrorType;
import com.rinearn.graph3d.RinearnGraph3DDataFileFormat;

import java.io.StringReader;
import java.io.BufferedReader;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;


/**
 * The parser for decoding strings loaded from n-columns-format data files.
 */
public final class ColumnDataParser {

	/**
	 * Stores the parameters depending on a data file.
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
	 * Creates a new parser.
	 */
	public ColumnDataParser() {
	}


	/**
	 * Parses the string loaded from a data file.
	 *
	 * @param dataString The string loaded from the data file.
	 * @param format The format of the data file.
	 * @param legend The legend of the parsed data series.
	 * @return The DataSeriesGroup instance storing all data series contained in the data file.
	 * @throw DataFileFormatException
	 *   Thrown if the specified format is unsupported, or the contents is syntactically incorrect.
	 */
	public DataSeriesGroup<ArrayDataSeries> parse(String dataString, RinearnGraph3DDataFileFormat format, String legend)
			throws DataFileFormatException {

		try (StringReader stringReader = new StringReader(dataString);
				BufferedReader bufferedReader = new BufferedReader(stringReader)) {

			DataSeriesGroup<ArrayDataSeries> result = this.parse(bufferedReader, format, legend);
			return result;

		} catch (IOException ioe) {
			throw new IllegalStateException(ioe);
		}
	}


	/**
	 * Parses the content of a data file.
	 *
	 * @param bufferedReader The stream reading the content of the data file
	 * @param format The format of the data file.
	 * @param legend The legend of the parsed data series.
	 * @return The DataSeriesGroup instance storing all data series contained in the data file.
	 * @throw DataFileFormatException
	 *   Thrown if the specified format is unsupported, or the contents is syntactically incorrect.
	 */
	public DataSeriesGroup<ArrayDataSeries> parse(BufferedReader bufferedReader, RinearnGraph3DDataFileFormat format, String legend)
			throws DataFileFormatException, IOException {

		// Stores the decoded result to be returned.
		DataSeriesGroup<ArrayDataSeries> dataSeriesGroup = new DataSeriesGroup<ArrayDataSeries>();

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

		// Read the all lines in the file.
		while (true) {

			// Read the next line.
			String line = bufferedReader.readLine();
			boolean isEndOfFile = (line == null);

			// An empty data line or EOF: represents a separator of multiple "data series" or "sub data series".
			if (isEndOfFile || this.isEmptyDataLine(line, decodingParam)) {
				emptyDataLineCount++;

				// Single empty data lines or EOL: a separator of "sub data series".
				if (emptyDataLineCount == 1 || isEndOfFile) {
					if (columnsList.size() != 0) {
						SubDataSeries subDataSeries = new SubDataSeries(columnsList);
						subDataSeriesList.add(subDataSeries);
						columnsList = new ArrayList<String[]>();
					}
				}

				// Double empty data lines or EOL: a separator of "data series".
				if (emptyDataLineCount == 2 || isEndOfFile) {
					if (subDataSeriesList.size() != 0) {
						ArrayDataSeries dataSeries = this.parseAndPackSubDataSeries(subDataSeriesList, legend);
						dataSeriesGroup.addDataSeries(dataSeries);
						subDataSeriesList = new ArrayList<SubDataSeries>();
						columnsList = new ArrayList<String[]>();
					}
				}

				// If the stream has reached to EOL, end reading. Otherwise continue reading.
				if (isEndOfFile) {
					break;
				} else {
					continue;
				}

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

		// Supplements the series attributes of all the data series in the group.
		dataSeriesGroup.supplementSeriesAttributes();

		return dataSeriesGroup;
	}


	/**
	 * Parses the columns of multiple sub data series, and packs the results into an ArrayDataSeries instance.
	 *
	 * @param subDataSeriesList The list of the multiple sub data series to be packed.
	 * @param legend The legend of the parsed data series.
	 * @return The ArrayDataSeries instance storing the parsed result.
	 * @throw DataFileFormatException
	 *     Thrown if any non-number value is detected.
	 */
	private ArrayDataSeries parseAndPackSubDataSeries(List<SubDataSeries> subDataSeriesList, String legend)
			throws DataFileFormatException {

		int subSeriesCount = subDataSeriesList.size();
		int columnCount = subDataSeriesList.get(0).columnsList.get(0).length;
		double[][][] coords = new double[columnCount][subSeriesCount][]; // [icolumn][isub][ivertex]
		boolean[][] visibilities = new boolean[subSeriesCount][];

		// Parse all sub series.
		for (int isub=0; isub<subSeriesCount; isub++) {
			SubDataSeries subSeries = subDataSeriesList.get(isub);

			int vertexCount = subSeries.columnsList.size();
			for (int icolumn=0; icolumn<columnCount; icolumn++) {
				coords[icolumn][isub] = new double[vertexCount];
				visibilities[isub] = new boolean[vertexCount];
			}

			// Parse all the vertex coordinates.
			for (int ivertex=0; ivertex<vertexCount; ivertex++) {
				String[] columns = subSeries.columnsList.get(ivertex);
				visibilities[isub][ivertex] = true;

				// Parse each column's coordinate value of this vertex.
				for (int icolumn=0; icolumn<columnCount; icolumn++) {
					try {
						double value = Double.parseDouble(columns[icolumn]);
						coords[icolumn][isub][ivertex] = value;
						visibilities[isub][ivertex] &= !Double.isNaN(value);

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
			// NOTE: Should we make invisible this point if its extraCoords[iexdim] is NaN? Or not?
			//       -> Probably it should be decided in plotter-side, because it depends on how this dimension is visualized.
		}

		// Pack the parsed result into an ArrayDataSeries instance.
		ArrayDataSeries dataSeries;
		if (columnCount == 3) {
			dataSeries = new ArrayDataSeries(xCoords, yCoords, zCoords, visibilities, legend);
		} else {
			dataSeries = new ArrayDataSeries(xCoords, yCoords, zCoords, extraCoords, visibilities, legend);
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
			case THREE_COLUMN_CSV:
			case THREE_COLUMNS_CSV:
			case FOUR_COLUMN_CSV:
			case FOUR_COLUMNS_CSV: {
				delimiter = ",";
				break;
			}
			case THREE_COLUMN_STSV:
			case THREE_COLUMNS_STSV:
			case FOUR_COLUMN_STSV:
			case FOUR_COLUMNS_STSV: {
				delimiter = "\\s";
				break;
			}
			default: {
				throw new DataFileFormatException(
						ErrorType.UNSUPPORTED_DATA_FILE_FORMAT_FOR_THIS_PARSER, format.toString()
				);
			}
		}

		// Define the expected number of the columns.
		int expectedColumnCount = -1;
		switch(format) {
			case THREE_COLUMN_CSV:
			case THREE_COLUMNS_CSV:
			case THREE_COLUMN_STSV:
			case THREE_COLUMNS_STSV: {
				expectedColumnCount = 3;
				break;
			}
			case FOUR_COLUMN_CSV:
			case FOUR_COLUMNS_CSV:
			case FOUR_COLUMN_STSV:
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
