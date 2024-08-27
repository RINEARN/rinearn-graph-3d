package com.rinearn.graph3d.model.dataseries;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


/**
 * The container class packing multiple data series.
 */
public final class DataSeriesGroup<DataSeriesType> {

	/** The list of the data series packed into this group. */
	private final List<DataSeriesType> dataSeriesList;


	/**
	 * Creates a new data series group.
	 */
	public DataSeriesGroup() {
		this.dataSeriesList = new ArrayList<DataSeriesType>();
	}


	/**
	 * Adds new data series to this group.
	 *
	 * @param dataSeries The data series to be added.
	 */
	public synchronized void addDataSeries(DataSeriesType dataSeries) {
		this.dataSeriesList.add(dataSeries);
	}


	/**
	 * Gets the number of the currently contained data series in this group.
	 *
	 * @return The number of the currently contained data series.
	 */
	public synchronized int getDataSeriesCount() {
		return this.dataSeriesList.size();
	}


	/**
	 * Gets the data series by the index.
	 *
	 * @param index The index of the data series.
	 * @return The all data series registered to this group.
	 */
	public synchronized DataSeriesType getDataSeriesAt(int index) {
		return this.dataSeriesList.get(index);
	}


	/**
	 * Gets the list of the currently registered data series.
	 *
	 * @return The list of the currently registered data series.
	 */
	public synchronized List<DataSeriesType> getDataSeriesList() {
		return Collections.unmodifiableList(this.dataSeriesList);
	}


	/**
	 * Removes the last registered data series.
	 */
	public synchronized void removeLastDataSeries() {
		this.dataSeriesList.remove(this.dataSeriesList.size() - 1);
	}


	/**
	 * Clears the all data series currently registered to this group.
	 */
	public synchronized void clearAllDataSeries() {
		this.dataSeriesList.clear();
	}
}
