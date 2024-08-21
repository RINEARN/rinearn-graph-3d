package com.rinearn.graph3d.model.dataseries;

import java.util.List;
import java.util.ArrayList;


/**
 * The container class packing multiple data series.
 */
public abstract class DataSeriesGroup {

	/** The list of the data series packed into this group. */
	private final List<AbstractDataSeries> dataSeriesList;


	/**
	 * Creates a new data series group.
	 */
	public DataSeriesGroup() {
		this.dataSeriesList = new ArrayList<AbstractDataSeries>();
	}


	/**
	 * Adds new data series to this group.
	 *
	 * @param dataSeries The data series to be added.
	 */
	public void addDataSeries(AbstractDataSeries dataSeries) {
		this.dataSeriesList.add(dataSeries);
	}


	/**
	 * Gets the number of the currently contained data series in this group.
	 *
	 * @return The number of the currently contained data series.
	 */
	public int getDataSeriesCount() {
		return this.dataSeriesList.size();
	}


	/**
	 * Gets the all data series registered to this group.
	 *
	 * @return The all data series registered to this group.
	 */
	public AbstractDataSeries[] getAllDataSeries() {
		int length = this.dataSeriesList.size();
		AbstractDataSeries[] dataSeriesArray = new AbstractDataSeries[length];
		dataSeriesArray = this.dataSeriesList.toArray(dataSeriesArray);
		return dataSeriesArray;
	}


	/**
	 * Clears the all data series currently registered to this group.
	 */
	public void clearAllDataSeries() {
		this.dataSeriesList.clear();
	}
}
