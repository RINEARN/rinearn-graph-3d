package com.rinearn.graph3d.model.io;

import com.rinearn.graph3d.RinearnGraph3DDataFileFormat;
import com.rinearn.graph3d.model.data.series.ArrayDataSeries;
import com.rinearn.graph3d.model.data.series.DataSeriesGroup;
import com.rinearn.graph3d.model.io.parser.ColumnDataParser;
import com.rinearn.graph3d.model.io.parser.MatrixDataParser;

import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.StringReader;


/**
 * The class that performs I/O operations on data files.
 */
public final class DataFileIO {

	/**
	 * Loads the data file.
	 *
	 * @param file The data file to be loaded.
	 * @param format The format of the data file. Specify AUTO to infer the format from the file content.
	 * @return The loaded result (multiple data series can be contained).
	 * @throws IOException Thrown if any I/O error is occurred when reading the data file.
	 * @throws DataFileFormatException Thrown if the content of the data file is syntactically incorrect, or unsupported format.
	 */
	public DataSeriesGroup<ArrayDataSeries> loadDataFile(File file, RinearnGraph3DDataFileFormat format)
			throws IOException, DataFileFormatException {

		// Load and decode the file content.
		DataSeriesGroup<ArrayDataSeries> dataSeriesGroup = null;
		try (FileReader fileReaderToInfer = new FileReader(file);
				BufferedReader bufferedReaderToInfer = new BufferedReader(fileReaderToInfer);
				FileReader fileReaderToLoad = new FileReader(file);
				BufferedReader bufferedReaderToLoad = new BufferedReader(fileReaderToLoad) ) {

			dataSeriesGroup = this.loadAndParseDataFromStreams(bufferedReaderToInfer, bufferedReaderToLoad, format);

		// Don't remove the following catch-and-rethrowing steps.
		// Maybe it seems to be meaningless, but is to close the resources.
		} catch (IOException | DataFileFormatException e) {
			throw e;
		}
		return dataSeriesGroup;
	}


	/**
	 * Parses the specified string, regarding it as the content of the data file.
	 *
	 * @param fileContent The string to be parsed as the content of a data file.
	 * @param format The format of the data file. Specify AUTO to infer the format from the file content.
	 * @return The parsed result (multiple data series can be contained).
	 * @throws DataFileFormatException Thrown if the content of the data file is syntactically incorrect, or unsupported format.
	 */
	public DataSeriesGroup<ArrayDataSeries> parseDataFileContent(String fileContent, RinearnGraph3DDataFileFormat format)
			throws DataFileFormatException {

		// Parse the file content.
		DataSeriesGroup<ArrayDataSeries> dataSeriesGroup = null;
		try (StringReader stringReaderToInfer = new StringReader(fileContent);
				BufferedReader bufferedReaderToInfer = new BufferedReader(stringReaderToInfer);
				StringReader stringReaderToLoad = new StringReader(fileContent);
				BufferedReader bufferedReaderToLoad = new BufferedReader(stringReaderToLoad) ) {

			dataSeriesGroup = this.loadAndParseDataFromStreams(bufferedReaderToInfer, bufferedReaderToLoad, format);

		// Don't remove the following catch-and-rethrowing steps.
		// Maybe it seems to be meaningless, but is to close the resources.
		} catch (DataFileFormatException dffe) {
			throw dffe;

		} catch (IOException ioe) {

			// We consider that, normally, IOException don't occur for reading data from ByteArrayInputStream.
			// It is irregular situation, so wrap it by IllegalStateException and re-throw.
			throw new IllegalStateException("Unexpected IOException occurred.", ioe);
		}
		return dataSeriesGroup;
	}


	/**
	 * Loads the content of a data file form the stream through the specified BufferedReader, and parse it.
	 *
	 * @param bufferedReaderToInfer The BufferedReader of the stream to infer the data file format.
	 * @param bufferedReaderToLoad The BufferedReader of the stream to load the data file content.
	 * @param format The format of the data file.
	 * @return The parsed data.
	 * @throws IOException Thrown if any I/O error occurred for loading the data file content.
	 * @throws DataFileFormatException Thrown if the content of the data file is syntactically incorrect, or unsupported format.
	 */
	private DataSeriesGroup<ArrayDataSeries> loadAndParseDataFromStreams(
			BufferedReader bufferedReaderToInfer, BufferedReader bufferedReaderToLoad, RinearnGraph3DDataFileFormat format)
					throws IOException, DataFileFormatException {

		// If the data file format is AUTO, infer the format from the content of the data file automatically.
		if (format == RinearnGraph3DDataFileFormat.AUTO) {
			format = new DataFileFormatInferencer().infer(bufferedReaderToInfer);
		}

		// Load the data file from the stream, depending on the format.
		switch (format) {
			case NONE : {
				DataSeriesGroup<ArrayDataSeries> dataSeriesGroup = new DataSeriesGroup<ArrayDataSeries>();
				return dataSeriesGroup;
			}
			case MATRIX_CSV :
			case MATRIX_STSV : {
				DataSeriesGroup<ArrayDataSeries> dataSeriesGroup = new MatrixDataParser().parse(bufferedReaderToLoad, format);
				return dataSeriesGroup;
			}
			default : {
				DataSeriesGroup<ArrayDataSeries> dataSeriesGroup = new ColumnDataParser().parse(bufferedReaderToLoad, format);
				return dataSeriesGroup;
			}
		}
	}
}
