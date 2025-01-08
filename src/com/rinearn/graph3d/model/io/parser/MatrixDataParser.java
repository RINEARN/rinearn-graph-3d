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
import java.util.Map;
import java.util.HashMap;


/**
 * The parser for decoding strings loaded matrix-format data files.
 */
public final class MatrixDataParser {

	private static final String OPTION_VERTICAL_SPLIT = "VERTICAL_SPLIT";
	@SuppressWarnings("unused")
	private static final String OPTION_HORIZONTAL_SPLIT = "HORIZONTAL_SPLIT";
	@SuppressWarnings("unused")
	private static final String OPTION_LEVEL_SPLIT = "LEVEL_SPLIT";

	private static final String OPTION_HORIZONTAL_INDEX = "HORIZONTAL_INDEX";

	private static final String OPTION_LEVEL_INDEX = "LEVEL_INDEX";

	@SuppressWarnings("unused")
	private static final String OPTION_HORIZONTAL_X = "HORIZONTAL_X";

	private static final String OPTION_HORIZONTAL_Y = "HORIZONTAL_Y";


	/**
	 * Stores the parameters depending on a data file.
	 */
	private final class DecodingParam {

		/** The delimiter of columns, depending the format. */
		public final String delimiter;

		/**
		 * Creates an instance storing the specified parameters.
		 *
		 * @param delimiter The delimiter of columns, depending the format.
		 */
		public DecodingParam(String delimiter) {
			this.delimiter = delimiter;
		}
	}


	/**
	 * Stores the intermediate data structure of the matrix to be parsed.
	 */
	private final class Matrix {
		public final List<String> horizontalCoordList = new ArrayList<String>();
		public final List<String> verticalCoordList = new ArrayList<String>();
		public final List<List<String>> zLineList = new ArrayList<List<String>>();
		public final List<String> optionList = new ArrayList<String>();

		@SuppressWarnings("unused")
		public void dump() {
			System.out.print("optionList: ");
			for (int i=0; i<this.optionList.size(); i++) {
				System.out.print(this.optionList.get(i) + ",");
			}
			System.out.println("");

			System.out.print("horizontalCoordList: ");
			for (int i=0; i<this.horizontalCoordList.size(); i++) {
				System.out.print(this.horizontalCoordList.get(i) + ",");
			}
			System.out.println("");

			System.out.print("verticalCoordList: ");
			for (int i=0; i<this.verticalCoordList.size(); i++) {
				System.out.print(this.verticalCoordList.get(i) + ",");
			}
			System.out.println("");

			for (int i=0; i<this.zLineList.size(); i++) {
				System.out.print("zLineList[" + i + "][]: ");
				List<String> zList = this.zLineList.get(i);
				for (int j=0; j<zList.size(); j++) {
					System.out.print(zList.get(j) + ",");
				}
				System.out.println("");
			}
			System.out.println("");
		}
	}


	/**
	 * Creates a new parser.
	 */
	public MatrixDataParser() {
	}


	private List<String[]> tokenizeLines(List<String> lineList, DecodingParam decodingParam) {
		List<String[]> tokenizedLineList = new ArrayList<String[]>();
		for (String line: lineList) {
			String[] columns = line.split(decodingParam.delimiter, -1);
			tokenizedLineList.add(columns);
		}
		return tokenizedLineList;
	}

	private List<Matrix> parseMatrix(List<String[]> tokenizedLineList, DecodingParam decodingParam)
			throws DataFileFormatException {

		if (tokenizedLineList.size() < 2) {
			throw new DataFileFormatException(ErrorType.TOO_SHORT_DATA_FILE);
		}

		// Extract the specified options.
		String[] firstLineColumns = tokenizedLineList.get(0);
		if (firstLineColumns.length < 2) {
			throw new DataFileFormatException(ErrorType.TOO_SHORT_DATA_FILE);
		}
		String firstCell = firstLineColumns[0];
		String[] options = firstCell.split("&", -1);

		// Detect the options to declare multi-series.
		boolean isMultiSeries = false;
		for (String option: options) {
			if (option.equals(OPTION_HORIZONTAL_INDEX.toUpperCase()) || option.equals(OPTION_LEVEL_INDEX.toUpperCase())) {
				isMultiSeries = true;
				break;
			}
		}

		// If the data is single-series matrix, simply parse it and return.
		if (!isMultiSeries) {
			List<Matrix> seriesList = new ArrayList<Matrix>();
			seriesList.add(this.parseSingleSerirsMatrix(tokenizedLineList, decodingParam));
			return seriesList;
		}

		// Store the pair of (icol, iseries) into a map.
		Map<Integer, Integer> icolIseriesMap = new HashMap<Integer, Integer>();
		int maxIseries = -1;
		for (int icol=1; icol<firstLineColumns.length; icol++) {
			int iseries = -1;
			try {
				iseries = Integer.parseInt(firstLineColumns[icol]);
			} catch (NumberFormatException nfe) {
				throw new DataFileFormatException(ErrorType.INVARID_MATRIX_SERIES_INDEX, firstLineColumns[icol]);
			}
			if (iseries < 1 || 1000000 < iseries) {
				throw new DataFileFormatException(ErrorType.INVARID_MATRIX_SERIES_INDEX, firstLineColumns[icol]);
			}
			if (maxIseries < iseries) {
				maxIseries = iseries;
			}
			icolIseriesMap.put(icol, iseries);
		}
		int seriesCount = maxIseries + 1;

		// Store the values at the left-edge column.
		List<String> leftEdgeColumnList = new ArrayList<String>(tokenizedLineList.size() - 1);
		for (int iline=1; iline<tokenizedLineList.size(); iline++) {
			int irow = iline - 1;
			leftEdgeColumnList.set(irow, tokenizedLineList.get(iline)[0]);
		}

		// Create the nested Lists, to store values of [iseries][iline][icolumn].
		List<List<List<String>>> seriesContentList = new ArrayList<List<List<String>>>(seriesCount);
		for (int iseries = 0; iseries < seriesCount; iseries++) {
			seriesContentList.set(iseries, new ArrayList<List<String>>(tokenizedLineList.size() - 1));
			for (int iline=1; iline < tokenizedLineList.size(); iline++) {
				int irow = iline - 1;
				seriesContentList.get(iseries).set(irow, new ArrayList<String>());
			}
		}

		// Store the left-edge column values into matrices of all series.
		for (int irow=0; irow<tokenizedLineList.size() - 1; irow++) {
			for (int iseries = 0; iseries < seriesCount; iseries++) {
				List<List<String>> seriesContent = seriesContentList.get(iseries);
				seriesContent.get(irow).add(leftEdgeColumnList.get(irow));
			}
		}

		// Store the other column values into matrices.
		for (int iline=1; iline<tokenizedLineList.size(); iline++) {
			for (int icol=1; icol<firstLineColumns.length; icol++) {
				int iseries = icolIseriesMap.get(icol);
				int irow = iline - 1;
				List<List<String>> seriesContent = seriesContentList.get(iseries);
				String columnValue = tokenizedLineList.get(iline)[icol];
				seriesContent.get(irow).add(columnValue);
			}
		}

		// Parse the above results to Matrix instances, and return them.
		List<Matrix> resultMatrixList = new ArrayList<Matrix>();
		//for (int iseries=0; iseries<seriesCount; iseries++) { // The series index "0" is invalid for this format, so omit it from the result, though we handled it above for readability.
		for (int iseries=1; iseries<seriesCount; iseries++) {

			// Conveer List of List<String> to List of String[], sttoring all the column values of all the lines.
			List<List<String>> seriesContent = seriesContentList.get(iseries);
			int rowCount = seriesContent.size();
			List<String[]> seriesTokenizedLineList = new ArrayList<String[]>(rowCount);
			for (int irow = 0; irow<rowCount; irow++) {
				seriesTokenizedLineList.add(seriesContent.get(irow).toArray(new String[0]));
			}
			Matrix matrix = this.parseSingleSerirsMatrix(seriesTokenizedLineList, decodingParam);
			resultMatrixList.add(matrix);
		}
		return resultMatrixList;
	}


	private Matrix parseSingleSerirsMatrix(List<String[]> tokenizedLineList, DecodingParam decodingParam) throws DataFileFormatException {
		if (tokenizedLineList.size() < 2) {
			throw new DataFileFormatException(ErrorType.TOO_SHORT_DATA_FILE);
		}

		Matrix matrix = new Matrix();

		String[] firstLineColumns = tokenizedLineList.get(0);
		if (tokenizedLineList.size() < 2) {
			throw new DataFileFormatException(ErrorType.TOO_SHORT_DATA_FILE);
		}
		String firstCell = firstLineColumns[0];

		// Determine whether the first cell is a numeric cell, or is storing annotation information.
		boolean isFirstCellNumeric;
		try {
			@SuppressWarnings("unused")
			double d = Double.parseDouble(firstCell);
			isFirstCellNumeric = true;
		} catch (NumberFormatException nfe) {
			isFirstCellNumeric = false;
		}

		// If the value of the first cell is NOT numeric, it specifies options, so read and store them.
		if (!isFirstCellNumeric) {
			String[] options = firstCell.split("&", -1);
			for (String option: options) {
				matrix.optionList.add(option.toUpperCase().trim());
			}
		}

		// Read horizontal coordinates.
		boolean hasHorizontalVerticalCoordLines = !isFirstCellNumeric;
		if (hasHorizontalVerticalCoordLines) {
			for (int icol=1; icol<firstLineColumns.length; icol++) {
				matrix.horizontalCoordList.add(firstLineColumns[icol]);
			}
		// If the horizontal/vertical coordinates are omitted, assign the column indices.
		} else {
			for (int icol=0; icol<firstLineColumns.length; icol++) {
				matrix.horizontalCoordList.add(Integer.toString(icol));
			}
		}

		for (int irow=0; irow<tokenizedLineList.size(); irow++) {
			String[] columns = tokenizedLineList.get(irow);

			if (hasHorizontalVerticalCoordLines) {
				if (irow == 0) {
					continue;
				}

				// Read the row-index as the vertical coordinate.
				matrix.verticalCoordList.add(columns[0]);

				// Store values of column representing Z values.
				List<String> zColumnList = new ArrayList<String>();
				for (int icol=1; icol<columns.length; icol++) { // CAUTION: icol begins with 1 for this case.
					zColumnList.add(columns[icol]);
				}
				matrix.zLineList.add(zColumnList);

			} else {

				// Assign the row-index as the vertical coordinate.
				matrix.verticalCoordList.add(Integer.toString(irow));

				// Store values of column representing Z values.
				List<String> zColumnList = new ArrayList<String>();
				for (int icol=0; icol<columns.length; icol++) { // CAUTION: icol begins with 0 for this case.
					zColumnList.add(columns[icol]);
				}
				matrix.zLineList.add(zColumnList);
			}
		}

		//matrix.dump();

		return matrix;
	}



	private class LineSeries {
		public List<String> lineList = new ArrayList<String>();
	}

	private List<LineSeries> parseLineSeparatedSeries(BufferedReader bufferedReader, DecodingParam decodingParam)
			throws DataFileFormatException, IOException {

		List<LineSeries> seriesList = new ArrayList<LineSeries>();
		LineSeries series = new LineSeries();
		int emptyLineCount = 0;

		// Read the all lines in the file.
		while (true) {

			// Read the next line.
			String line = bufferedReader.readLine();
			boolean isEndOfFile = (line == null);

			// An empty data line or EOF: represents a separator of multiple "data series" or "sub data series".
			if(isEndOfFile || this.isEmptyDataLine(line, decodingParam)) {
				emptyLineCount++;

				// Double empty data lines or EOL: a separator of "data series".
				if (emptyLineCount == 2 || isEndOfFile) {
					if (series.lineList.size() != 0) {
						seriesList.add(series);
						series = new LineSeries();
					}
				}

				// If the stream has reached to EOL, end reading. Otherwise continue reading.
				if (isEndOfFile) {
					break;
				} else {
					continue;
				}

			} else {
				if (emptyLineCount == 1) {
					throw new DataFileFormatException(ErrorType.SINGLE_EMPTY_LINE_SEPARATED_MATRICES_IS_NOT_SUPPORTED);
				}
				emptyLineCount = 0;
			}

			series.lineList.add(line);

		} // The end of the reading loop.

		return seriesList;
	}


	/**
	 * Parses the string loaded from a data file.
	 *
	 * @param dataString The string loaded from the data file.
	 * @param format The format of the data file.
	 * @return The DataSeriesGroup instance storing all data series contained in the data file.
	 * @throw DataFileFormatException
	 *   Thrown if the specified format is unsupported, or the contents is syntactically incorrect.
	 */
	public DataSeriesGroup<ArrayDataSeries> parse(String dataString, RinearnGraph3DDataFileFormat format)
			throws DataFileFormatException {

		try (StringReader stringReader = new StringReader(dataString);
				BufferedReader bufferedReader = new BufferedReader(stringReader)) {

			DataSeriesGroup<ArrayDataSeries> result = this.parse(bufferedReader, format);
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
	 * @return The DataSeriesGroup instance storing all data series contained in the data file.
	 * @throw DataFileFormatException
	 *   Thrown if the specified format is unsupported, or the contents is syntactically incorrect.
	 */
	public DataSeriesGroup<ArrayDataSeries> parse(BufferedReader bufferedReader, RinearnGraph3DDataFileFormat format)
			throws DataFileFormatException, IOException {

		// Stores the parsed result to be returned.
		DataSeriesGroup<ArrayDataSeries> dataSeriesGroup = new DataSeriesGroup<ArrayDataSeries>();

		// Define the decoding parameters depending on the data file format.
		// If the format is unsupported by this parser, DataFileFormatException occurs here.
		DecodingParam decodingParam = this.determineDecodingParam(format);

		// Split the contents by dual-empty-lines, which are the separator between data series.
		List<LineSeries> lineSeriesList = this.parseLineSeparatedSeries(bufferedReader, decodingParam);

		// Parse each line-sector.
		for (LineSeries lineSeries: lineSeriesList) {

			// Split each line in a matrix into column values.
			List<String[]> tokenizedLineList = this.tokenizeLines(lineSeries.lineList, decodingParam);

			// Parse a matrix. It may contain multiple data series in HORIZONTAL_INDEX feature, so the result is a List.
			List<Matrix> matrixList = this.parseMatrix(tokenizedLineList, decodingParam);

			// Convert each matrix to ArrayDataSeries, and store it into the result.
			for (Matrix matrix: matrixList) {
				ArrayDataSeries dataSeries = this.convertMatrixToArrayDataSeries(matrix);
				dataSeriesGroup.addDataSeries(dataSeries);
			}
		}

		return dataSeriesGroup;
	}


	private ArrayDataSeries convertMatrixToArrayDataSeries(Matrix matrix)
			throws DataFileFormatException {

		boolean splitsVertical = matrix.optionList.contains(OPTION_VERTICAL_SPLIT);
		boolean splitsHorizontal = !splitsVertical; // true by default.
		boolean isHorizontalY = matrix.optionList.contains(OPTION_HORIZONTAL_Y);
		boolean isHorizontalX = !isHorizontalY; // true by default.

		int rowCount = matrix.zLineList.size();
		int colCount = matrix.zLineList.get(0).size();
		int rDimCount = splitsHorizontal ? colCount : rowCount;
		int lDimCount = splitsHorizontal ? rowCount : colCount;

		// The arrays in whicn coordinate values will be stored.
		double[][] xArray = new double[rDimCount][lDimCount];
		double[][] yArray = new double[rDimCount][lDimCount];
		double[][] zArray = new double[rDimCount][lDimCount];
		boolean[][] visibilities = new boolean[rDimCount][lDimCount];

		// Read coordinates corresponding to each cell in the matrix, convert them into numbers, and store them into the above arrays.
		for (int irow=0; irow<rowCount; irow++) {
			for (int icol=0; icol<colCount; icol++) {
				String xString = isHorizontalX ? matrix.horizontalCoordList.get(icol) : matrix.verticalCoordList.get(irow);
				String yString = isHorizontalY ? matrix.horizontalCoordList.get(icol) : matrix.verticalCoordList.get(irow);
				String zString = matrix.zLineList.get(irow).get(icol);

				int ir = splitsHorizontal ? icol : irow;
				int il = splitsHorizontal ? irow : icol;
				try {
					xArray[ir][il] = Double.parseDouble(xString);
				} catch (NumberFormatException nfe) {
					throw new DataFileFormatException(ErrorType.FAILED_TO_PARSE_NUMBER_IN_DATA_FILE, xString);
				}
				try {
					yArray[ir][il] = Double.parseDouble(yString);
				} catch (NumberFormatException nfe) {
					throw new DataFileFormatException(ErrorType.FAILED_TO_PARSE_NUMBER_IN_DATA_FILE, yString);
				}
				try {
					zArray[ir][il] = Double.parseDouble(zString);
				} catch (NumberFormatException nfe) {
					throw new DataFileFormatException(ErrorType.FAILED_TO_PARSE_NUMBER_IN_DATA_FILE, zString);
				}

				visibilities[ir][il] = true;
				if (Double.isNaN(xArray[ir][il]) || Double.isNaN(yArray[ir][il]) || Double.isNaN(zArray[ir][il])) {
					visibilities[ir][il] = false;
				}
			}
		}

		ArrayDataSeries arrayDataSeries = new ArrayDataSeries(xArray, yArray, zArray, visibilities);
		return arrayDataSeries;
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
			case MATRIX_CSV: {
				delimiter = ",";
				break;
			}
			case MATRIX_STSV: {
				delimiter = "\\s";
				break;
			}
			default: {
				throw new DataFileFormatException(
						ErrorType.UNSUPPORTED_DATA_FILE_FORMAT_FOR_THIS_PARSER, format.toString()
				);
			}
		}

		return new DecodingParam(delimiter);
	}

}
