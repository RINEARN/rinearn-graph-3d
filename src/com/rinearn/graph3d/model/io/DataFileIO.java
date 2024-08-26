package com.rinearn.graph3d.model.io;

import com.rinearn.graph3d.RinearnGraph3DDataFileFormat;
import com.rinearn.graph3d.model.dataseries.DataSeriesGroup;
import com.rinearn.graph3d.model.io.decoder.TemporaryDataDecoder;

import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;


/**
 * The class that performs I/O operations on data files.
 */
public final class DataFileIO {

	/**
	 * Loads the data file by inferring the data file format automatically.
	 *
	 * @param file The data file to be loaded.
	 * @param format The data file format of the data file.
	 * @return The loaded result, in which multiple data series are contained.
	 * @throws IOException Thrown if any I/O error is occurred when reading the data file.
	 * @throws DataFileFormatException Thrown if the content of the data file is syntactically incorrect.
	 */
	public DataSeriesGroup loadDataFile(File file) throws IOException, DataFileFormatException {

		// Infer the data file format from the content of the data file.
		RinearnGraph3DDataFileFormat format = new DataFileFormatInferencer().infer(file);

		// Load the data file.
		DataSeriesGroup dataSeriesGroup = this.loadDataFile(file, format);
		return dataSeriesGroup;
	}


	/**
	 * Loads the data file.
	 *
	 * @param file The data file to be loaded.
	 * @param format The data file format of the data file.
	 * @return The loaded result, in which multiple data series are contained.
	 * @throws IOException Thrown if any I/O error is occurred when reading the data file.
	 * @throws DataFileFormatException Thrown if the content of the data file is syntactically incorrect.
	 */
	public DataSeriesGroup loadDataFile(File file, RinearnGraph3DDataFileFormat format) throws IOException, DataFileFormatException {

		// Load the data file.
		DataSeriesGroup dataSeriesGroup;
		try (FileReader fileReader = new FileReader(file);
				BufferedReader bufferedReader = new BufferedReader(fileReader)) {

			dataSeriesGroup = new TemporaryDataDecoder().decode(bufferedReader, format);

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
