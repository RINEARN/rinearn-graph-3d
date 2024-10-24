package com.rinearn.graph3d.model.io;

import com.rinearn.graph3d.RinearnGraph3DDataFileFormat;
import com.rinearn.graph3d.model.data.series.ArrayDataSeries;
import com.rinearn.graph3d.model.data.series.DataSeriesGroup;
import com.rinearn.graph3d.model.io.decoder.ColumnDataDecoder;

import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;


/**
 * The class that performs I/O operations on data files.
 */
public final class DataFileIO {

	/**
	 * Loads the data file.
	 *
	 * @param file The data file to be loaded.
	 * @param format The data file format of the data file. Specify AUTO to infer the format from the file content.
	 * @return The loaded result, in which multiple data series are contained.
	 * @throws IOException Thrown if any I/O error is occurred when reading the data file.
	 * @throws DataFileFormatException Thrown if the content of the data file is syntactically incorrect.
	 */
	public DataSeriesGroup<ArrayDataSeries> loadDataFile(File file, RinearnGraph3DDataFileFormat format)
			throws IOException, DataFileFormatException {

		// If the data file format is AUTO, infer the format from the content of the data file automatically.
		if (format == RinearnGraph3DDataFileFormat.AUTO) {
			format = new DataFileFormatInferencer().infer(file);
		}

		// Load the data file.
		DataSeriesGroup<ArrayDataSeries> dataSeriesGroup;
		try (FileReader fileReader = new FileReader(file);
				BufferedReader bufferedReader = new BufferedReader(fileReader)) {

			dataSeriesGroup = new ColumnDataDecoder().decode(bufferedReader, format);

		// Don't remove the following catch-and-rethrowing steps.
		// Maybe it seems to be meaningless, but is to close the resources.
		} catch (IOException ioe) {
			throw ioe;
		} catch (DataFileFormatException dffe) {
			throw dffe;
		}

		return dataSeriesGroup;
	}

}
