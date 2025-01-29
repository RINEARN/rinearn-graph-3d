package com.rinearn.graph3d.config.data;

import java.util.Set;
import java.util.HashSet;

/**
 * The index-based series filter.
 */
public final class IndexSeriesFilter extends SeriesFilter {

	/** The series indices which should be included. */
	private volatile int[] includedSeriesIndices;

	/** The set of the series indices which should be included. */
	private volatile Set<Integer> includedSeriesIndexSet;

	/**
	 * Create a new instance.
	 */
	public IndexSeriesFilter () {
	}

	/**
	 * Create a new instance, to include the specified series indices.
	 *
	 * @param includedSeriesIndices The indices of the series to be included.
	 */
	public IndexSeriesFilter(int[] includedSeriesIndices) {
		this.setIncludedSeriesIndices(includedSeriesIndices);
	}

	/**
	 * Sets the indices of the series to be included.
	 *
	 * @param includedSeriesIndices The indices of the series to be included.
	 */
	public void setIncludedSeriesIndices(int[] includedSeriesIndices) {
		this.includedSeriesIndices = includedSeriesIndices;
		this.includedSeriesIndexSet = new HashSet<Integer>();
		for (int seriesIndex: includedSeriesIndices) {
			this.includedSeriesIndexSet.add(seriesIndex);
		}
	}

	/**
	 * Gets the indices of the series to be included.
	 *
	 * @return The indices of the series to be included.
	 */
	public int[] getIncludedSeriesIndices() {
		return this.includedSeriesIndices;
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
