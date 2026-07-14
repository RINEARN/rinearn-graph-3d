package com.rinearn.graph3d.presenter;

import com.rinearn.graph3d.config.data.SeriesAttribute;
import com.rinearn.graph3d.event.RinearnGraph3DPlottingDataAccessor;
import com.rinearn.graph3d.model.data.DataStore;
import com.rinearn.graph3d.model.data.series.AbstractDataSeries;
import com.rinearn.graph3d.model.data.series.DataSeriesGroup;

/**
 * The object for referring to the data to be plotted, from plotters.
 */
public class DataAccessor implements RinearnGraph3DPlottingDataAccessor {

	/** The generation of the API set of RinearnGraph3DPlottingDataAccessor, supported by this implementation. */
	private static final int API_GENERATION = 1;

	/** The data store in which the data to be plotted is stored. */
	private final DataSeriesGroup<AbstractDataSeries> dataSeriesGroup;

	/**
	 * Creates a new instance provides the access to the data in the specified data store object.
	 *
	 * @param dataStore The data store in which the data to be plotted is stored.
	 */
	public DataAccessor(DataStore dataStore) {
		this.dataSeriesGroup = dataStore.getCombinedDataSeriesGroup();
	}

	@Override
	public int getApiGeneration() {
		return API_GENERATION;
	}

	@Override
	public double getDataPointX(int seriesIndex, int lineIndex, int pointIndex) {
		return this.dataSeriesGroup.getDataSeriesAt(seriesIndex).getXCoordinates()[lineIndex][pointIndex];
	}

	@Override
	public double getDataPointY(int seriesIndex, int lineIndex, int pointIndex) {
		return this.dataSeriesGroup.getDataSeriesAt(seriesIndex).getYCoordinates()[lineIndex][pointIndex];
	}

	@Override
	public double getDataPointZ(int seriesIndex, int lineIndex, int pointIndex) {
		return this.dataSeriesGroup.getDataSeriesAt(seriesIndex).getZCoordinates()[lineIndex][pointIndex];
	}

	@Override
	public boolean isDataSeriesVisible(int seriesIndex) {
		// TODO: ここでアニメーションの領域絞り込みなどを実装
		return true;
	}

	@Override
	public boolean isDataLineVisible(int seriesIndex, int lineIndex) {
		// TODO: ここでアニメーションの領域絞り込みなどを実装
		return true;
	}

	@Override
	public boolean isDataPointVisible(int seriesIndex, int lineIndex, int pointIndex) {
		boolean isPointVisible = this.dataSeriesGroup.getDataSeriesAt(seriesIndex).getVisibilities()[lineIndex][pointIndex];

		// TODO: ここでアニメーションの領域絞り込みなどを実装

		return isPointVisible;
	}

	@Override
	public int getDataSeriesCount() {
		return this.dataSeriesGroup.getDataSeriesCount();
	}

	@Override
	public int getDataLineCount(int seriesIndex) {
		return this.dataSeriesGroup.getDataSeriesAt(seriesIndex).getXCoordinates().length;
	}

	@Override
	public int getDataPointCount(int seriesIndex, int lineIndex) {
		return this.dataSeriesGroup.getDataSeriesAt(seriesIndex).getXCoordinates()[lineIndex].length;
	}

	@Override
	public SeriesAttribute getDataSeriesAttribute(int seriesIndex) {
		return this.dataSeriesGroup.getDataSeriesAt(seriesIndex).getSeriesAttribute();
	}
}
