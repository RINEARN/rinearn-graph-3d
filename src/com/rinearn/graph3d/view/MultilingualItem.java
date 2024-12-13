package com.rinearn.graph3d.view;

/**
 * The item for some selectable UI (e.g. JComboBox)
 * of which text can be changed displayed for multilingual support.
 */
public class MultilingualItem {

	/** The displayed text of this item. */
	private volatile String text = "";

	/**
	 * Create a new item.
	 */
	public MultilingualItem() {
	}

	/**
	 * Sets the displayed text of this item.
	 *
	 * @param text The displayed text.
	 */
	public synchronized void setText(String text) {
		this.text = text;
	}

	/**
	 * Gets the displayed text of this item.
	 *
	 * @return The displayed text.
	 */
	public synchronized String getText() {
		return this.text;
	}

	/**
	 * Returns the displayed text of this item.
	 *
	 * @return The displayed text.
	 */
	@Override
	public synchronized String toString() {
		return this.text;
	}

	/**
	 * Returns the hash code of the text of this item.
	 *
	 * @return The hash code of the displayed text.
	 */
	@Override
    public synchronized int hashCode() {
        return this.text.hashCode();
    }

	/**
	 * Compares the displayed text of this instance to it of the specified instance.
	 *
	 * @param comparedItem The instance to be compared (must be MultilingualItem type).
	 * @return Returns true if the displayed text of this instance equals to it of the specified instance.
	 */
    @Override
    public synchronized boolean equals(Object comparedItem) {
    	if (!(comparedItem instanceof MultilingualItem)) {
    		throw new IllegalArgumentException(
    			"Must not compare a MultilingualItem instance to another class's instance, e.g. String."
    				+
    			"It is unclear (implementation dependent) whether a UI component equate 'comparedItem' instance with this instance "
    				+
    			"when their classes are different, even if equals(...) returns true."
    		);
    	}
    	MultilingualItem castedInstance = MultilingualItem.class.cast(comparedItem);
    	return castedInstance.text.equals(this.text);
    }
}
