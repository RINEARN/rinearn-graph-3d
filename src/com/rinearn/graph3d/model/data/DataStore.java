package com.rinearn.graph3d.model.data;

import com.rinearn.graph3d.model.data.series.AbstractDataSeries;
import com.rinearn.graph3d.model.data.series.ArrayDataSeries;
import com.rinearn.graph3d.model.data.series.DataSeriesGroup;
import com.rinearn.graph3d.model.data.series.MathDataSeries;


/**
 * The class storing and managing data to be plotted.
 */
public class DataStore {

	/** The group of math data series. */
	private final DataSeriesGroup<MathDataSeries> mathDataSeriesGroup = new DataSeriesGroup<MathDataSeries>();

	/** The group of array data series. */
	private final DataSeriesGroup<ArrayDataSeries> arrayDataSeriesGroup = new DataSeriesGroup<ArrayDataSeries>();


	/**
	 * Clear all the currently registered data series, without distinction of the type of the data series (math or array).
	 *
	 * The same operation can be performed by calling arrayDataSeriesList() and mathDataSeriesList() methods,
	 * but in some situation, we must perform them as an "atomic" operation. This method is provided for such situation.
	 */
	public synchronized void clearDataSeries() {
		this.arrayDataSeriesGroup.clearAllDataSeries();
		this.mathDataSeriesGroup.clearAllDataSeries();
	}


	/**
	 * Gets the group of all the currently registered data series,
	 * without distinction of the type of the data series (math or array).
	 *
	 * @return The group of all the currently registered data series.
	 */
	public synchronized DataSeriesGroup<AbstractDataSeries> getCombinedDataSeriesGroup() {
		DataSeriesGroup<AbstractDataSeries> allDataSeriesGroup = new DataSeriesGroup<AbstractDataSeries>();
		allDataSeriesGroup.combine(this.arrayDataSeriesGroup);
		allDataSeriesGroup.combine(this.mathDataSeriesGroup);
		return allDataSeriesGroup.createUnmodifiableClone();
	}


	/**
	 * Adds (registers) a new math data series.
	 *
	 * @param mathDataSeries The math data series to be added.
	 */
	public synchronized void addMathDataSeries(MathDataSeries mathDataSeries) {
		this.mathDataSeriesGroup.addDataSeries(mathDataSeries);

		// Note: update the graph range here? if necessary.
		//   -> It is necessary for ArrayDataSeries, but it is not necessary for MathDataSeries, probably.
	}


	/**
	 * Get the math data series at the specified index.
	 *
	 * @param index the index of the math data series.
	 * @return The math data series at the specified index.
	 */
	public synchronized MathDataSeries getMathDataSeriesAt(int index) {
		return this.mathDataSeriesGroup.getDataSeriesAt(index);
	}


	/**
	 * Remove the specified math data series.
	 *
	 * @param index the index of the math data series to be removed.
	 */
	public synchronized void removeMathDataSeriesAt(int index) {
		this.mathDataSeriesGroup.removeDataSeriesAt(index);
	}


	/**
	 * Remove the lastly registered math data series.
	 *
	 * If this method is called when no math data series is registered, nothing occurs.
	 */
	public synchronized void removeLastMathDataSeries() {
		if (this.mathDataSeriesGroup.getDataSeriesCount() == 0) {
			return;
		}
		this.mathDataSeriesGroup.removeLastDataSeries();
	}


	/**
	 * Clear all currently registered math data series.
	 */
	public synchronized void clearMathDataSeries() {
		this.mathDataSeriesGroup.clearAllDataSeries();
	}


	/**
	 * Gets the group of the currently registered math data series.
	 *
	 * The returned group instance is unmodifiable. For adding/removing elements,
	 * use the methods addMathDataSeries(...), removeLastMathDataSeries(), etc.
	 *
	 * @return The group of the currently registered math data series.
	 */
	public synchronized DataSeriesGroup<MathDataSeries> getMathDataSeriesGroup() {
		return this.mathDataSeriesGroup.createUnmodifiableClone();
	}


	/**
	 * Sets all the array data series to be plotted.
	 *
	 * At first glance, it seems that the completely same operation can performed
	 * by using multiple calls of clearArrayDataSeries() and addArrayDataSeries(ArrayDataSeries) methods.
	 *
	 * However, this operation must be performed as an "atomic operation" in some situation,
	 * especially when asynchronous-plotting feature is enabled.
	 * This method is provided for such situation.
	 *
	 * @param dataSeriesGroup The container in which all the array data series to be plotted are stored.
	 */
	public synchronized void setArrayDataSeriesGroup(DataSeriesGroup<ArrayDataSeries> dataSeriesGroup) {
		this.arrayDataSeriesGroup.clearAllDataSeries();
		for (ArrayDataSeries arrayDataSeries: dataSeriesGroup) {
			this.arrayDataSeriesGroup.addDataSeries(arrayDataSeries);
		}
	}


	/**
	 * Adds (registers) a new array data series.
	 *
	 * @param arrayDataSeries The array data series to be added.
	 */
	public synchronized void addArrayDataSeries(ArrayDataSeries arrayDataSeries) {
		this.arrayDataSeriesGroup.addDataSeries(arrayDataSeries);

		// TODO: Update the graph range here, before re-plotting.
		// -> いや範囲変更は View 層にも伝搬させる必要があるから Presenter 層でやるべき。ここじゃなくて。
System.out.println("!!! TODO @" + this);
	}


	/**
	 * Adds (registers) new multiple array data series.
	 *
	 * At first glance, it seems that the completely same operation can performed
	 * by using multiple calls of addArrayDataSeries(ArrayDataSeries) method for each data series.
	 *
	 * However, this operation must be performed as an "atomic operation" in some situation,
	 * especially when asynchronous-plotting feature is enabled.
	 * This method is provided for such situation.
	 *
	 * @param dataSeriesGroup The container in which all the array data series to be added are stored.
	 */
	public synchronized void addArrayDataSeriesGroup(DataSeriesGroup<ArrayDataSeries> dataSeriesGroup) {
		for (ArrayDataSeries arrayDataSeries: dataSeriesGroup) {
			this.arrayDataSeriesGroup.addDataSeries(arrayDataSeries);
		}
	}


	/**
	 * Remove the lastly registered array data series.
	 *
	 * If this method is called when no array data series is registered, nothing occurs.
	 */
	public synchronized void removeLastArrayDataSeries() {
		if (this.arrayDataSeriesGroup.getDataSeriesCount() == 0) {
			return;
		}
		this.arrayDataSeriesGroup.removeLastDataSeries();
	}


	/**
	 * Clear all currently registered array data series.
	 */
	public synchronized void clearArrayDataSeries() {
		this.arrayDataSeriesGroup.clearAllDataSeries();
	}


	/**
	 * Gets the group of the currently registered array data series.
	 *
	 * The returned group is unmodifiable. For adding/removing elements,
	 * use the methods addArrayDataSeries(...), removeLastArrayDataSeries(), etc.
	 *
	 * @return The group of the currently registered array data series.
	 */
	public synchronized DataSeriesGroup<ArrayDataSeries> getArrayDataSeriesGroup() {
		return this.arrayDataSeriesGroup.createUnmodifiableClone();
	}

}
