package com.rinearn.graph3d.view;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.data.IndexSeriesFilter;
import com.rinearn.graph3d.config.data.SeriesFilterMode;
import com.rinearn.graph3d.config.font.FontConfiguration;


/**
 * The container of UI components for series filter settings,
 * used on setting windows of plot options.
 */
public class SeriesFilterComponents {

	/** The panel on which UI components for series filter settings are mounted. */
	public volatile JPanel panel;

	/** The checkbox to enable/disable the series filter. */
	public volatile JCheckBox enabledBox;

	/** The label of the text field to input included series indices. */
	public volatile JLabel indexLabel;

	/** The text field to input included series indices. */
	public volatile JTextField indexField;

	/** The right-click menu of indexField. */
	public volatile TextRightClickMenu indexFieldRightClickMenu;


	/**
	 * Create a new instance.
	 */
	public SeriesFilterComponents() {

		// Initialize GUI components.
		this.initializeComponents();
	}


	// !!!!! IMPORTANT NOTE !!!!!
	//
	// Don't put "synchronized" modifier to UI-operation methods,
	// such as initializeComponents(), setConfiguration(configuration), etc.
	//
	// The internal processing of the UI operation methods are
	// always processed in serial, on the event-dispatcher thread.
	// So "synchronized" is not necessary for them.
	//
	// If we put "synchronized" to them,
	// it becomes impossible to call other synchronized methods of this instance,
	// from other tasks (varies depending on timing) stacked on the event dispatcher's queue.
	// IF WE ACCIDENTALLY CALL IT, THE EVENT-DISPATCHER THREAD MAY FAIL INTO A DEADLOCK.
	//
	// !!!!! IMPORTANT NOTE !!!!!

	private void initializeComponents() {

		// Initialize GUI components on the window, on event-dispatcher thread.
		ComponentInitializer componentInitializer = new ComponentInitializer();
		if (SwingUtilities.isEventDispatchThread()) {
			componentInitializer.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(componentInitializer);
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * The class for initializing GUI components on the window, on event-dispatcher thread.
	 */
	private final class ComponentInitializer implements Runnable {
		@Override
		public void run() {

			// Prepare the layout manager and resources.
			panel = new JPanel();
			GridBagLayout layout = new GridBagLayout();
			panel.setLayout(layout);
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.gridx = 0;
			constraints.gridy = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			constraints.weightx = 1.0;
			constraints.weighty = 1.0;
			constraints.fill = GridBagConstraints.BOTH;

			// Define margines.
			int topMargin = 5;
			int bottomMargin = 5;
			int leftMargin = 5;
			int leftMarginLong = 25;
			int rightMargin = 5;
			double leftColumnWeight = 0.0;
			double rightColumnWeight = 1.0;

			constraints.gridwidth = 2;

			// The checkbox to enable/disable the series filter.
			enabledBox = new JCheckBox();
			constraints.insets = new Insets(topMargin, leftMargin, 0, rightMargin);
			layout.setConstraints(enabledBox, constraints);
			panel.add(enabledBox);

			constraints.gridy++;
			constraints.gridx = 0;
			constraints.weightx = leftColumnWeight;
			constraints.gridwidth = 1;

			// The label of the text field to input included series indices.
			indexLabel = new JLabel();
			constraints.insets = new Insets(topMargin, leftMarginLong, bottomMargin, 0);
			layout.setConstraints(indexLabel, constraints);
			panel.add(indexLabel);

			constraints.gridx = 1;
			constraints.weightx = rightColumnWeight;
			constraints.gridwidth = 1;

			// The text field to input included series indices.
			indexField = new JTextField();
			constraints.insets = new Insets(topMargin, 0, bottomMargin, rightMargin);
			layout.setConstraints(indexField, constraints);
			panel.add(indexField);

			indexFieldRightClickMenu = new TextRightClickMenu();
		}
	}


	/**
	 * Reflects the configuration parameters related to this window, such as the language of UI, fonts, and so on.
	 *
	 * @param configuration The configuration container.
		 * @param seriesFilterMode The mode of the series filter of the target option configuration.
		 * @param indexSeriesFilter The index series filter of the target option configuration.
	 */
	public void configure(RinearnGraph3DConfiguration configuration,
			SeriesFilterMode seriesFilterMode, IndexSeriesFilter indexSeriesFilter) {

		// Reflect the configuration, on event-dispatcher thread.
		ConfigurationReflector configReflector = new ConfigurationReflector(configuration, seriesFilterMode, indexSeriesFilter);
		if (SwingUtilities.isEventDispatchThread()) {
			configReflector.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(configReflector);
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}


	/**
	 * The class for reflecting the updated configuration, on event-dispatcher thread.
	 */
	private final class ConfigurationReflector implements Runnable {

		/* The configuration to be reflected. */
		private volatile RinearnGraph3DConfiguration configuration;

		/** The mode of the series filter of the target option configuration. */
		private volatile SeriesFilterMode seriesFilterMode;

		/** The index series filter of the target option configuration. */
		private volatile IndexSeriesFilter indexSeriesFilter;

		/**
		 * Creates a new instance to reflect the specified configuration.
		 *
		 * @param configuration The configuration to be reflected.
		 * @param seriesFilterMode The mode of the series filter of the target option configuration.
		 * @param indexSeriesFilter The index series filter of the target option configuration.
		 */
		public ConfigurationReflector(RinearnGraph3DConfiguration configuration,
				SeriesFilterMode seriesFilterMode, IndexSeriesFilter indexSeriesFilter) {

			if (!configuration.hasEnvironmentConfiguration()) {
				throw new IllegalArgumentException("No environment configuration is stored in the specified configuration.");
			}
			if (!configuration.hasLabelConfiguration()) {
				throw new IllegalArgumentException("No indexLabel configuration is stored in the specified configuration.");
			}
			this.configuration = configuration;
			this.seriesFilterMode = seriesFilterMode;
			this.indexSeriesFilter = indexSeriesFilter;
		}

		@Override
		public void run() {
			if (!SwingUtilities.isEventDispatchThread()) {
				throw new UnsupportedOperationException("This method is invokable only on the event-dispatcher thread.");
			}

			// Set texts to the components, in the language specified by the configuration.
			if (this.configuration.getEnvironmentConfiguration().isLocaleJapanese()) {
				this.setJapaneseTexts();
			} else {
				this.setEnglishTexts();
			}

			// Set fonts to the components.
			this.setFonts();

			// Updates the values of text fields from the current series filter mode and the filter instance..
			setSeriesFilterMode(this.seriesFilterMode, this.indexSeriesFilter);

			// Update the right-click menus.
			indexFieldRightClickMenu.configure(this.configuration);
		}

		/**
		 * Sets Japanese texts to the GUI components.
		 */
		private void setJapaneseTexts() {
			enabledBox.setText("系列指定");
			indexLabel.setText("系列番号: ");
		}

		/**
		 * Sets English texts to the GUI components.
		 */
		private void setEnglishTexts() {
			enabledBox.setText("Specify Series");
			indexLabel.setText("Series Indices: ");
		}

		/**
		 * Set fonts to the components.
		 */
		private void setFonts() {
			FontConfiguration fontConfig = configuration.getFontConfiguration();
			Font uiBoldFont = fontConfig.getUIBoldFont();
			Font uiPlainFont = fontConfig.getUIPlainFont();

			enabledBox.setFont(uiBoldFont);
			indexLabel.setFont(uiBoldFont);
			indexField.setFont(uiPlainFont);
		}
	}


	/**
	 * Sets the mode of the series filter.
	 * This method is invokable only on the event-dispatch thread.
	 *
	 * @param seriesFilterMode The mode of the series filter.
	 * @param indexSeriesFilter The series filter in INDEX mode.
	 */
	public void setSeriesFilterMode(SeriesFilterMode seriesFilterMode, IndexSeriesFilter indexSeriesFilter) {
		if (!SwingUtilities.isEventDispatchThread()) {
			throw new IllegalStateException("This method is invokable only on the event-dispatch thread.");
		}

		switch (seriesFilterMode) {
			case INDEX : {
				enabledBox.setSelected(true);
				indexField.setEditable(true);
				indexField.setBackground(Color.WHITE);
				indexField.setForeground(Color.BLACK);

				// Converts the int[] type array of series indices to a single text value, and set it to indexField.
				int[] seriesIndices = indexSeriesFilter.getIncludedSeriesIndices();
				StringBuilder seriesIndicesTextBuilder = new StringBuilder();
				for (int iseries = 0; iseries<seriesIndices.length; iseries++) {

					// The series index "1" on UI corresponds to the internal series index "0" . So offset the index.
					int seriesIndexOnUI = seriesIndices[iseries] + 1;

					// Append the index at the tail of the text.
					seriesIndicesTextBuilder.append(seriesIndexOnUI);
					if (iseries != seriesIndices.length - 1) {
						seriesIndicesTextBuilder.append(", ");
					}
				}
				String seriesIndicesText = seriesIndicesTextBuilder.toString();
				indexField.setText(seriesIndicesText);
				break;
			}
			case NONE : {
				enabledBox.setSelected(false);
				indexField.setText("");
				indexField.setEditable(false);
				indexField.setBackground(Color.LIGHT_GRAY);
				indexField.setForeground(Color.GRAY);
				break;
			}
			default : {
				throw new IllegalStateException("Unexpected series filter mode: " + seriesFilterMode);
			}
		}
		indexField.repaint();
	}

}
