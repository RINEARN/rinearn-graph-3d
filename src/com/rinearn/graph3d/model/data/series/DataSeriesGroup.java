package com.rinearn.graph3d.model.data.series;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;


/**
 * The container class packing multiple data series.
 *
 * @param <DataSeriesType> The type of the data series stored in this group.
 */
public final class DataSeriesGroup<DataSeriesType extends AbstractDataSeries>
		implements Iterable<DataSeriesType> {

	/** The list of the data series packed into this group. */
	private final List<DataSeriesType> dataSeriesList;


	/**
	 * Creates a new data series group.
	 */
	public DataSeriesGroup() {
		this.dataSeriesList = new ArrayList<DataSeriesType>();
	}


	/**
	 * Returns the iterator to access to each data series sequentially, used in enhanced for-statements.
	 *
	 * @return The iterator to access to each data series sequentially.
	 */
	@Override
	public synchronized Iterator<DataSeriesType> iterator() {
		return this.dataSeriesList.iterator();
	}


	/**
	 * Creates a new data series group in which all the data series in this and specified groups.
	 * The order of the elements from this and the specified group will be preserved in the created group.
	 * In the created group, the elements from this group will be located from the top,
	 * and then the elements from the specified group will be located.
	 *
	 * @param <NewDataSeriesType> The type of the data series of the new group.
	 * @param <ArgDataSeriesType> The type of the data series of the argument group.
	 * @param argGroup The group of the data series to be concatenated to this group.
	 * @return The new combined data group.
	 * @throws ClassCastException
	 *     Thrown if the types of the data series in this and argument group are incompatible with NewDataSeriesType.
	 */
	@SuppressWarnings("unchecked")
	public synchronized <NewDataSeriesType extends AbstractDataSeries, ArgDataSeriesType extends AbstractDataSeries>
			DataSeriesGroup<NewDataSeriesType> createCombinedGroup(DataSeriesGroup<ArgDataSeriesType> argGroup)
					throws ClassCastException {

		DataSeriesGroup<NewDataSeriesType> concatinatedGroup = new DataSeriesGroup<NewDataSeriesType>();
		for (DataSeriesType dataSeries: this.dataSeriesList) {
			concatinatedGroup.addDataSeries((NewDataSeriesType)dataSeries);  // Can throws ClassCastException
		}
		for (ArgDataSeriesType dataSeries: argGroup) {
			concatinatedGroup.addDataSeries((NewDataSeriesType)dataSeries);  // Can throws ClassCastException
		}
		return concatinatedGroup;
	}


	/**
	 * Adds all the data series stored in the specified group into this group.
	 * The order of the elements from the specified group will be preserved when added to this group.
	 *
	 * @param <ArgDataSeriesType> The type of the data series of the argument group.
	 * @param argGroup The data series group to be combined into this group.
	 * @throws ClassCastException
	 *     Thrown if the types of the data series in this and argument group are incompatible.
	 */
	@SuppressWarnings("unchecked")
	public synchronized <ArgDataSeriesType extends AbstractDataSeries> void combine(DataSeriesGroup<ArgDataSeriesType> argGroup) {
		for (ArgDataSeriesType dataSeries: argGroup) {
			this.addDataSeries((DataSeriesType)dataSeries);
		}
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


	/**
	 * Checks whether the minimum value of the X-coordinate values exists.
	 *
	 * @return Returns true if the minimum value exists.
	 */
	public synchronized boolean hasXMin() {
		for (DataSeriesType dataSeries: this.dataSeriesList) {
			if (dataSeries.hasXMin()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the minimum value of the X-coordinate values.
	 *
	 * @return The minimum value of the X-coordinate values.
	 */
	public synchronized BigDecimal getXMin() {

		// Stores the tentative minimum values in the following loop.
		BigDecimal xMinTentative = null;

		// Find the minimum value in all the data series.
		for (DataSeriesType dataSeries: this.dataSeriesList) {
			if (!dataSeries.hasXMin()) {
				continue;
			}

			// If the xMin value of this data series is smaller than xMinTentative,
			// assign it as a new value of xMinTentative.
			BigDecimal xMinOfThisSeries = dataSeries.getXMin();
			if (xMinTentative == null || xMinOfThisSeries.compareTo(xMinTentative) < 0) {
				xMinTentative = xMinOfThisSeries;
			}
		}
		return xMinTentative;
	}


	/**
	 * Checks whether the maximum value of the X-coordinate values exists.
	 *
	 * @return Returns true if the maximum value exists.
	 */
	public synchronized boolean hasXMax() {
		for (DataSeriesType dataSeries: this.dataSeriesList) {
			if (dataSeries.hasXMax()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the maximum value of the X-coordinate values.
	 *
	 * @return The maximum value of the X-coordinate values.
	 */
	public synchronized BigDecimal getXMax() {

		// Stores the tentative maximum values in the following loop.
		BigDecimal xMaxTentative = null;

		// Find the maximum value in all the data series.
		for (DataSeriesType dataSeries: this.dataSeriesList) {
			if (!dataSeries.hasXMax()) {
				continue;
			}

			// If the xMax value of this data series is smaller than xMaxTentative,
			// assign it as a new value of xMaxTentative.
			BigDecimal xMaxOfThisSeries = dataSeries.getXMax();
			if (xMaxTentative == null || 0 < xMaxOfThisSeries.compareTo(xMaxTentative)) {
				xMaxTentative = xMaxOfThisSeries;
			}
		}
		return xMaxTentative;
	}


	/**
	 * Checks whether the minimum value of the Y-coordinate values exists.
	 *
	 * @return Returns true if the minimum value exists.
	 */
	public synchronized boolean hasYMin() {
		for (DataSeriesType dataSeries: this.dataSeriesList) {
			if (dataSeries.hasYMin()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the minimum value of the Y-coordinate values.
	 *
	 * @return The minimum value of the Y-coordinate values.
	 */
	public synchronized BigDecimal getYMin() {

		// Stores the tentative minimum values in the following loop.
		BigDecimal yMinTentative = null;

		// Find the minimum value in all the data series.
		for (DataSeriesType dataSeries: this.dataSeriesList) {
			if (!dataSeries.hasYMin()) {
				continue;
			}

			// If the yMin value of this data series is smaller than yMinTentative,
			// assign it as a new value of yMinTentative.
			BigDecimal yMinOfThisSeries = dataSeries.getYMin();
			if (yMinTentative == null || yMinOfThisSeries.compareTo(yMinTentative) < 0) {
				yMinTentative = yMinOfThisSeries;
			}
		}
		return yMinTentative;
	}


	/**
	 * Checks whether the maximum value of the Y-coordinate values exists.
	 *
	 * @return Returns true if the maximum value exists.
	 */
	public synchronized boolean hasYMax() {
		for (DataSeriesType dataSeries: this.dataSeriesList) {
			if (dataSeries.hasYMax()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the maximum value of the Y-coordinate values.
	 *
	 * @return The maximum value of the Y-coordinate values.
	 */
	public synchronized BigDecimal getYMax() {

		// Stores the tentative maximum values in the following loop.
		BigDecimal yMaxTentative = null;

		// Find the maximum value in all the data series.
		for (DataSeriesType dataSeries: this.dataSeriesList) {
			if (!dataSeries.hasYMax()) {
				continue;
			}

			// If the yMax value of this data series is smaller than yMaxTentative,
			// assign it as a new value of yMaxTentative.
			BigDecimal yMaxOfThisSeries = dataSeries.getYMax();
			if (yMaxTentative == null || 0 < yMaxOfThisSeries.compareTo(yMaxTentative)) {
				yMaxTentative = yMaxOfThisSeries;
			}
		}
		return yMaxTentative;
	}


	/**
	 * Checks whether the minimum value of the Z-coordinate values exists.
	 *
	 * @return Returns true if the minimum value exists.
	 */
	public synchronized boolean hasZMin() {
		for (DataSeriesType dataSeries: this.dataSeriesList) {
			if (dataSeries.hasZMin()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the minimum value of the Z-coordinate values.
	 *
	 * @return The minimum value of the Z-coordinate values.
	 */
	public synchronized BigDecimal getZMin() {

		// Stores the tentative minimum values in the following loop.
		BigDecimal zMinTentative = null;

		// Find the minimum value in all the data series.
		for (DataSeriesType dataSeries: this.dataSeriesList) {
			if (!dataSeries.hasZMin()) {
				continue;
			}

			// If the zMin value of this data series is smaller than zMinTentative,
			// assign it as a new value of zMinTentative.
			BigDecimal zMinOfThisSeries = dataSeries.getZMin();
			if (zMinTentative == null || zMinOfThisSeries.compareTo(zMinTentative) < 0) {
				zMinTentative = zMinOfThisSeries;
			}
		}
		return zMinTentative;
	}


	/**
	 * Checks whether the maximum value of the Z-coordinate values exists.
	 *
	 * @return Returns true if the maximum value exists.
	 */
	public synchronized boolean hasZMax() {
		for (DataSeriesType dataSeries: this.dataSeriesList) {
			if (dataSeries.hasZMax()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the maximum value of the Z-coordinate values.
	 *
	 * @return The maximum value of the Z-coordinate values.
	 */
	public synchronized BigDecimal getZMax() {

		// Stores the tentative maximum values in the following loop.
		BigDecimal zMaxTentative = null;

		// Find the maximum value in all the data series.
		for (DataSeriesType dataSeries: this.dataSeriesList) {
			if (!dataSeries.hasZMax()) {
				continue;
			}

			// If the zMax value of this data series is smaller than zMaxTentative,
			// assign it as a new value of zMaxTentative.
			BigDecimal zMaxOfThisSeries = dataSeries.getZMax();
			if (zMaxTentative == null || 0 < zMaxOfThisSeries.compareTo(zMaxTentative)) {
				zMaxTentative = zMaxOfThisSeries;
			}
		}
		return zMaxTentative;
	}

}
