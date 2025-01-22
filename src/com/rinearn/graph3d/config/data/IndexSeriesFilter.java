package com.rinearn.graph3d.config.data;

import java.util.Set;
import java.util.HashSet;

/**
 * The index-based series filter.
 */
public class IndexSeriesFilter extends SeriesFilter {

	/** The set of the series indices which should be included. */
	public volatile Set<Integer> includedSeriesIndexSet;

	/**
	 * Create a new instance.
	 */
	public IndexSeriesFilter () {
	}

	/**
	 * Sets the indices of the series to be included.
	 *
	 * @param includedSeriesIndices The indices of the series to be included.
	 */
	public void setIncludedSeriesIndices(int[] includedSeriesIndices) {
		this.includedSeriesIndexSet = new HashSet<Integer>();
		for (int seriesIndex: includedSeriesIndices) {
			this.includedSeriesIndexSet.add(seriesIndex);
		}
	}

	/**
	 * Determines whether the specified series is included in the result of this filter.
	 *
	 * @param seriesAttribute The container of attributes of the series.
	 * @return Returns true if the specified series is included.
	 */
	@Override
	public boolean isSeriesIncluded(SeriesAttribute seriesAttribute) {
		int seriesIndex = seriesAttribute.getGlobalSeriesIndex();
		return this.includedSeriesIndexSet.contains(seriesIndex);
	}

}
