package com.rinearn.graph3d.config.scale;

import com.rinearn.graph3d.config.RinearnGraph3DConfigurationException;

import java.math.BigDecimal;
import java.text.DecimalFormat;


/**
 * The formatter of displayed values of numeric tick labels.
 */
public final class NumericTickLabelFormatter extends TickLabelFormatter implements Cloneable {

	/** The format of numeric tick labels, applied when |coordinate| = 0. */
	private volatile DecimalFormat zeroRangeFormat = new DecimalFormat("0");
	// Note: Why we don't declare the above as DecimalFormat-type field is,
	//       some people may want to implement original format by extending NumberFormat.
	//       (NumberFormat is an abstract class, and DecimalFormat is one of its implementation.)

	/** The format of numeric tick labels, applied when |coordinate| < 0.1. */
	private volatile DecimalFormat shortRangeFormat = new DecimalFormat("0.0#E0");

	/** The format of numeric tick labels, applied when 0.1 <= |coordinate| <= 10. */
	private volatile DecimalFormat mediumRangeFormat = new DecimalFormat("#0.0#");

	/** The format of numeric tick labels, applied when 10 < |coordinate|. */
	private volatile DecimalFormat longRangeFormat = new DecimalFormat("0.0#E0");

	/** The threshold value between the 'short' range and the 'medium' range. */
	private volatile BigDecimal shortMediumRangeThreshold = new BigDecimal("0.1");

	/** The threshold value between the 'medium' range and the 'long' range. */
	private volatile BigDecimal mediumLongRangeThreshold = BigDecimal.TEN;

	/** The flag to create unmodifiable instance. */
	private volatile boolean modifiable = true;


	/**
	 * Creates a new formatter for formatting tick coordinates into numeric tick labels.
	 */
	public NumericTickLabelFormatter() {
	}


	/**
	 * Creates a clone of this instance;
	 *
	 * @return The clone of this instance.
	 */
	@Override
	public Object clone() {
		NumericTickLabelFormatter clonedInstance = new NumericTickLabelFormatter();
		clonedInstance.zeroRangeFormat = this.zeroRangeFormat;
		clonedInstance.shortRangeFormat = this.shortRangeFormat;
		clonedInstance.mediumRangeFormat = this.mediumRangeFormat;
		clonedInstance.longRangeFormat = this.longRangeFormat;
		clonedInstance.shortMediumRangeThreshold = this.shortMediumRangeThreshold;
		clonedInstance.mediumLongRangeThreshold = this.mediumLongRangeThreshold;
		clonedInstance.modifiable = this.modifiable;
		return clonedInstance;
	}

	/**
	 * Creates an unmodifiable clone of this instance.
	 *
	 * @return The unmodifiable clone of this instance.
	 */
	public NumericTickLabelFormatter createUnmodifiableClone() {
		NumericTickLabelFormatter clonedInstance = NumericTickLabelFormatter.class.cast(this.clone());
		clonedInstance.modifiable = false;
		return clonedInstance;
	}


	/**
	 * Sets the format for the coordinate zero (0).
	 *
	 * @param zeroRangeFormat The format for the coordinate zero (0).
	 */
	public void setZeroRangeFormat(DecimalFormat zeroRangeFormat) {
		this.zeroRangeFormat = zeroRangeFormat;
	}

	/**
	 * Gets the format for the coordinate zero (0).
	 *
	 * @return The format for the coordinate zero (0).
	 */
	public DecimalFormat getZeroRangeFormat() {
		return this.zeroRangeFormat;
	}


	/**
	 * Sets the format for coordinates in the short-range (|coordinate| < 0.1, by default).
	 *
	 * @param shortRangeFormat The format for coordinates in the short-range.
	 */
	public void setShortRangeFormat(DecimalFormat shortRangeFormat) {
		this.shortRangeFormat = shortRangeFormat;
	}

	/**
	 * Gets the format for coordinates in the short-range (|coordinate| < 0.1, by default).
	 *
	 * @return The format for coordinates in the short-range.
	 */
	public DecimalFormat getShortRangeFormat() {
		return this.shortRangeFormat;
	}


	/**
	 * Sets the format for coordinates in the medium-range (0.1 <= |coordinate| <= 10, by default).
	 *
	 * @param shortRangeFormat The format for coordinates in the medium-range.
	 */
	public void setMediumRangeFormat(DecimalFormat mediumRangeFormat) {
		this.mediumRangeFormat = mediumRangeFormat;
	}

	/**
	 * Gets the format for coordinates in the medium-range (0.1 <= |coordinate| <= 10, by default).
	 *
	 * @return The format for coordinates in the medium-range.
	 */
	public DecimalFormat getMediumRangeFormat() {
		return this.mediumRangeFormat;
	}



	/**
	 * Sets the format for coordinates in the long-range (10 < |coordinate|, by default).
	 *
	 * @param longRangeFormat The format for coordinates in the long-range.
	 */
	public void setLongRangeFormat(DecimalFormat longRangeFormat) {
		this.longRangeFormat = longRangeFormat;
	}

	/**
	 * Gets the format for coordinates in the long-range (10 < |coordinate|, by default).
	 *
	 * @return The format for coordinates in the long-range.
	 */
	public DecimalFormat getLongRangeFormat() {
		return this.longRangeFormat;
	}


	/**
	 * Sets the threshold value between the 'short' range and the 'medium' range.
	 *
	 * @param shortMediumRangeThreshold The threshold value between the 'short' range and the 'medium' range.
	 */
	public void setShortMediumRangeThreshold(BigDecimal shortMediumRangeThreshold) {
		this.shortMediumRangeThreshold = shortMediumRangeThreshold;
	}

	/**
	 * Gets the threshold value between the 'short' range and the 'medium' range.
	 *
	 * @return shortMediumRangeThreshold The threshold value between the 'short' range and the 'medium' range.
	 */
	public BigDecimal getShortMediumRangeThreshold() {
		return this.shortMediumRangeThreshold;
	}


	/**
	 * Sets the threshold value between the 'medium' range and the 'long' range.
	 *
	 * @param mediumLongRangeThreshold The threshold value between the 'medium' range and the 'long' range.
	 */
	public void setMediumLongRangeThreshold(BigDecimal mediumLongRangeThreshold) {
		this.mediumLongRangeThreshold = mediumLongRangeThreshold;
	}

	/**
	 * Gets the threshold value between the 'medium' range and the 'long' range.
	 *
	 * @return shortMediumRangeThreshold The threshold value between the 'medium' range and the 'long' range.
	 */
	public BigDecimal getMediumLongRangeThreshold() {
		return this.mediumLongRangeThreshold;
	}


	/**
	 * Formats the specified coordinate value into a label text,
	 *
	 * @param tickCoordinate The coordinate of the numeric tick to be formatted.
	 * @return The formatted tick label.
	 */
	@Override
	public synchronized String format(BigDecimal tickCoordinate) {
		BigDecimal absCoordinate = tickCoordinate.abs();

		if (absCoordinate.compareTo(BigDecimal.ZERO) == 0) {
			return this.zeroRangeFormat.format(tickCoordinate);
		}
		if (absCoordinate.compareTo(this.shortMediumRangeThreshold) < 0) {
			return this.shortRangeFormat.format(tickCoordinate);
		}
		if (0 < absCoordinate.compareTo(this.mediumLongRangeThreshold)) {
			return this.longRangeFormat.format(tickCoordinate);
		}
		return this.mediumRangeFormat.format(tickCoordinate);
	}

	/**
	 * Returns whether the specified coordinate is formattable by this formatter.
	 *
	 * @param tickCoordinate Returns if the specified coordinate is formattable.
	 */
	@Override
	public synchronized boolean isFormattable(BigDecimal coordinate) {
		return true;
	}


	/**
	 * Validates correctness and consistency of parameters stored in this instance.
	 *
	 * This method is called when the ScaleConfiguration having this instance is specified to RinearnGraph3D or its renderer.
	 * If no issue is detected, nothing occurs.
	 * If any issue is detected, throws IllegalStateException.
	 *
	 * @throws RinearnGraph3DConfigurationException Thrown when incorrect or inconsistent settings are detected.
	 */
	@Override
	public void validate() throws RinearnGraph3DConfigurationException {
		if (this.zeroRangeFormat == null) {
			throw new RinearnGraph3DConfigurationException("zeroRangeFormat is null");
		}
		if (this.shortRangeFormat == null) {
			throw new RinearnGraph3DConfigurationException("shortRangeFormat is null");
		}
		if (this.mediumRangeFormat == null) {
			throw new RinearnGraph3DConfigurationException("mediumRangeFormat is null");
		}
		if (this.longRangeFormat == null) {
			throw new RinearnGraph3DConfigurationException("longRangeFormat is null");
		}
		if (this.shortMediumRangeThreshold == null) {
			throw new RinearnGraph3DConfigurationException("shortMediumRangeThreshold is null");
		}
		if (this.mediumLongRangeThreshold == null) {
			throw new RinearnGraph3DConfigurationException("mediumLongRangeThreshold is null");
		}
		if (0 <= this.shortMediumRangeThreshold.compareTo(this.mediumLongRangeThreshold)) {
			throw new RinearnGraph3DConfigurationException("mediumLongRangeThreshold must be grater than shortMediumRangeThreshold");
		}
		if (this.shortMediumRangeThreshold.compareTo(BigDecimal.ZERO) <= 0) {
			throw new RinearnGraph3DConfigurationException("shortMediumRangeThreshold must be a positive value (excluding zero).");
		}
		if (this.mediumLongRangeThreshold.compareTo(BigDecimal.ZERO) <= 0) {
			throw new RinearnGraph3DConfigurationException("mediumLongRangeThreshold must be a positive value (excluding zero).");
		}
	}
}
