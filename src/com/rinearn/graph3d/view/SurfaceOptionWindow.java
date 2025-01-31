package com.rinearn.graph3d.view;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.data.IndexSeriesFilter;
import com.rinearn.graph3d.config.data.SeriesFilterMode;
import com.rinearn.graph3d.config.FontConfiguration;
import com.rinearn.graph3d.config.OptionConfiguration;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import java.lang.reflect.InvocationTargetException;


/**
 * The window of "With Surfaces" option.
 */
public final class SurfaceOptionWindow {

	/** The default width [px] of this window. */
	public static final int DEFAULT_WINDOW_WIDTH = 340;

	/** The default height [px] of this window. */
	public static final int DEFAULT_WINDOW_HEIGHT = 180;

	/** The frame of this window. */
	public volatile JFrame frame;

	/** The checkbox to enable/disable the series filter. */
	public volatile JCheckBox seriesFilterBox;

	/** The title label of the text field of the series filter. */
	public volatile JLabel seriesFilterLabel;

	/** The text field of the series filter. */
	public volatile JTextField seriesFilterField;

	/** The right-click menu of seriesFilterField. */
	public volatile TextRightClickMenu seriesFilterFieldRightClickMenu;

	/** The button to reflect settings. */
	public volatile JButton setButton;


	/**
	 * Creates a new window.
	 *
	 * @param configuration The configuration of this application.
	 */
	public SurfaceOptionWindow() {

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

			// The frame (window):
			frame = new JFrame();
			frame.setBounds(
					0, 0,
					DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT
			);
			frame.setLayout(null);
			frame.setVisible(false);
			frame.setVisible(true);

			// Prepare the layout manager and resources.
			Container basePanel = frame.getContentPane();
			GridBagLayout layout = new GridBagLayout();
			basePanel.setLayout(layout);
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
			int topMarginLong = 12;
			int bottomMargin = 5;
			int leftMargin = 5;
			int leftMarginLong = 25;
			int rightMargin = 5;
			double leftColumnWeight = 0.0;
			double rightColumnWeight = 1.0;

			constraints.gridwidth = 2;

			// The checkbox to enable/disable the series filter.
			seriesFilterBox = new JCheckBox();
			constraints.insets = new Insets(topMargin, leftMargin, 0, rightMargin);
			layout.setConstraints(seriesFilterBox, constraints);
			basePanel.add(seriesFilterBox);

			constraints.gridy++;
			constraints.gridx = 0;
			constraints.weightx = leftColumnWeight;
			constraints.gridwidth = 1;

			// The title label of the text field of the series filter.
			seriesFilterLabel = new JLabel();
			constraints.insets = new Insets(topMargin, leftMarginLong, bottomMargin, 0);
			layout.setConstraints(seriesFilterLabel, constraints);
			basePanel.add(seriesFilterLabel);

			constraints.gridx = 1;
			constraints.weightx = rightColumnWeight;
			constraints.gridwidth = 1;

			// The text field of the series filter.
			seriesFilterField = new JTextField();
			constraints.insets = new Insets(topMargin, 0, bottomMargin, rightMargin);
			layout.setConstraints(seriesFilterField, constraints);
			basePanel.add(seriesFilterField);

			seriesFilterFieldRightClickMenu = new TextRightClickMenu();

			constraints.gridy++;
			constraints.gridx = 0;
			constraints.gridwidth = 2;

			// The button to reflect settings (SET button).
			setButton = new JButton("Unconfigured");
			constraints.insets = new Insets(topMarginLong, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(setButton, constraints);
			basePanel.add(setButton);
		}
	}


	/**
	 * Reflects the configuration parameters related to this window, such as the language of UI, fonts, and so on.
	 *
	 * @param configuration The configuration container.
	 */
	public void configure(RinearnGraph3DConfiguration configuration) {

		// Reflect the configuration, on event-dispatcher thread.
		ConfigurationReflector configReflector = new ConfigurationReflector(configuration);
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

		/**
		 * Creates a new instance to reflect the specified configuration.
		 *
		 * @param configuration The configuration to be reflected.
		 */
		public ConfigurationReflector(RinearnGraph3DConfiguration configuration) {
			if (!configuration.hasEnvironmentConfiguration()) {
				throw new IllegalArgumentException("No environment configuration is stored in the specified configuration.");
			}
			if (!configuration.hasLabelConfiguration()) {
				throw new IllegalArgumentException("No label configuration is stored in the specified configuration.");
			}
			this.configuration = configuration;
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

			// Updates the values of text fields, by the values stored in the configuration.
			this.updateValuesByConfiguration();

			// Update the right-click menus.
			seriesFilterFieldRightClickMenu.configure(this.configuration);
		}

		/**
		 * Sets Japanese texts to the GUI components.
		 */
		private void setJapaneseTexts() {
			frame.setTitle("オプション設定: 曲面プロット");
			setButton.setText("SET");

			seriesFilterBox.setText("系列指定");
			seriesFilterLabel.setText("系列番号: ");
		}

		/**
		 * Sets English texts to the GUI components.
		 */
		private void setEnglishTexts() {
			frame.setTitle("Option Settings: With Surfaces");
			setButton.setText("SET");

			seriesFilterBox.setText("Specify Series");
			seriesFilterLabel.setText("Series Indices: ");
		}

		/**
		 * Set fonts to the components.
		 */
		private void setFonts() {
			FontConfiguration fontConfig = configuration.getFontConfiguration();
			Font uiBoldFont = fontConfig.getUIBoldFont();
			Font uiPlainFont = fontConfig.getUIPlainFont();

			frame.setFont(uiBoldFont);
			setButton.setFont(uiBoldFont);

			seriesFilterBox.setFont(uiBoldFont);
			seriesFilterLabel.setFont(uiBoldFont);
			seriesFilterField.setFont(uiPlainFont);
		}

		/**
		 * Updates the values of text fields, by the values stored in the configuration.
		 */
		private void updateValuesByConfiguration() {
			OptionConfiguration optionConfig = this.configuration.getOptionConfiguration();
			OptionConfiguration.SurfaceOptionConfiguration surfaceOptionConfig = optionConfig.getSurfaceOptionConfiguration();
			setSeriesFilterMode(surfaceOptionConfig.getSeriesFilterMode(), surfaceOptionConfig.getIndexSeriesFilter());
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
				seriesFilterBox.setSelected(true);
				seriesFilterField.setEditable(true);
				seriesFilterField.setBackground(Color.WHITE);
				seriesFilterField.setForeground(Color.BLACK);

				// Converts the int[] type array of series indices to a single text value, and set it to seriesFilterField.
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
				seriesFilterField.setText(seriesIndicesText);
				break;
			}
			case NONE : {
				seriesFilterBox.setSelected(false);
				seriesFilterField.setText("");
				seriesFilterField.setEditable(false);
				seriesFilterField.setBackground(Color.LIGHT_GRAY);
				seriesFilterField.setForeground(Color.GRAY);
				break;
			}
			default : {
				throw new IllegalStateException("Unexpected series filter mode: " + seriesFilterMode);
			}
		}
		seriesFilterField.repaint();
	}


	/**
	 * Sets the visibility of this window.
	 *
	 * @param visible Specify true for showing this window, false for hiding the window.
	 */
	public void setWindowVisible(boolean visible) {

		// Set visibility of "frame", on event-dispatcher thread.
		WindowVisiblitySwitcher visibilitySwitcher = new WindowVisiblitySwitcher(visible);
		if (SwingUtilities.isEventDispatchThread()) {
			visibilitySwitcher.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(visibilitySwitcher);
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * The class for switching visibility of this window, on event-dispatcher thread.
	 */
	private final class WindowVisiblitySwitcher implements Runnable {

		/** The flag representing whether the window is visible. */
		private volatile boolean visible;

		/**
		 * Create an instance for switching visibility of this window.
		 *
		 * @param visible Specify true for showing this window, false for hiding the window.
		 */
		public WindowVisiblitySwitcher(boolean visible) {
			this.visible = visible;
		}
		@Override
		public void run() {
			if (!SwingUtilities.isEventDispatchThread()) {
				throw new UnsupportedOperationException("This method is invokable only on the event-dispatcher thread.");
			}
			frame.setVisible(visible);
		}
	}


	/**
	 * Disposes this window.
	 */
	public void dispose() {
		Disposer disposer = new Disposer();
		if (SwingUtilities.isEventDispatchThread()) {
			disposer.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(disposer);
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * The class for disposing GUI components on the window, on event-dispatcher thread.
	 */
	private final class Disposer implements Runnable {
		@Override
		public void run() {
			frame.setVisible(false);
			frame.dispose();
		}
	}
}
