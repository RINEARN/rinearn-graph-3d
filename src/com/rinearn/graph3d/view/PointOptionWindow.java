package com.rinearn.graph3d.view;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.data.IndexSeriesFilter;
import com.rinearn.graph3d.config.FontConfiguration;
import com.rinearn.graph3d.config.OptionConfiguration;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;


/**
 * The window of "With Points" option.
 */
public final class PointOptionWindow {

	/** The default width [px] of this window. */
	public static final int DEFAULT_WINDOW_WIDTH = 400;

	/** The default height [px] of this window. */
	public static final int DEFAULT_WINDOW_HEIGHT = 430;

	/** The display item of the point style mode "CIRCLE" in Japanese. */
	public static final String POINT_STYLE_MODE_CIRCLE_JA = "円形";

	/** The display item of the point style mode "CIRCLE" in English. */
	public static final String POINT_STYLE_MODE_CIRCLE_EN = "Circle";

	/** The display item of the point style mode "MARKER" in Japanese. */
	public static final String POINT_STYLE_MODE_MARKER_JA = "マーカー";

	/** The display item of the point style mode "MARKER" in English. */
	public static final String POINT_STYLE_MODE_MARKER_EN = "Marker";

	/** The frame of this window. */
	public volatile JFrame frame;

	/** The label of the combo box to select a point style mode. */
	public volatile JLabel styleModeLabel;

	/** The combo box to select a point style mode. */
	public volatile JComboBox<MultilingualItem> styleModeBox;

	/** The item of styleModeBox of CIRCLE mode. */
	public volatile MultilingualItem circleModeItem;

	/** The item of styleModeBox of MARKER mode. */
	public volatile MultilingualItem markerModeItem;

	/** The container of UI components in CIRCLE mode. */
	public volatile CircleModeItems circleModeComponents;

	/** The container of UI components in MARKER mode. */
	public volatile MarkerModeItems markerModeComponents;

	/** The checkbox to enable/disable the series filter. */
	public volatile JCheckBox seriesFilterBox;

	/** The title label of the text field of the series filter. */
	public volatile JLabel seriesFilterLabel;

	/** The text field of the series filter. */
	public volatile JTextField seriesFilterField;

	/** The right-click menu of seriesFilterField. */
	public volatile TextRightClickMenu seriesFilterFieldRightClickMenu;

	/** The panel on witch the panel for each style mode is mounted. */
	public volatile JPanel swappablePanel;

	/** The button to reflect settings. */
	public volatile JButton setButton;

	/** The container of the UI components in CIRCLE mode. */
	public static class CircleModeItems {

		/** The panel on which items in CIRCLE mode are mounted. */
		public volatile JPanel panel;

		/** The title label of the text field of the point radius in CIRCLE mode. */
		public volatile JLabel radiusLabel;

		/** The text field of the point radius in CIRCLE mode. */
		public volatile JTextField radiusField;

		/** The right-click menu of radiusField. */
		public volatile TextRightClickMenu radiusFieldRightClickMenu;
	}

	/** The container of the UI components in MARKER mode. */
	public static class MarkerModeItems {

		/** The panel on which items in MARKER mode are mounted. */
		public volatile JPanel panel;

		/** The title label of the text field to input the symbols in MARKER mode. */
		public volatile JLabel symbolLabel;

		/** The text field to input the symbols in MARKER mode. */
		public volatile JTextField symbolField;

		/** The check box to enable/disable the bold font. */
		public volatile JCheckBox boldBox;

		/** The title label of the text field of the marker's font size in MARKER mode. */
		public volatile JLabel fontSizeLabel;

		/** The text field of the marker's font size in MARKER mode. */
		public volatile JTextField sizeField;

		/** The title label of the text field to input the vertical offset ratio in MARKER mode. */
		public volatile JLabel verticalOffsetRatioLabel;

		/** The text field to input the vertical offset ratio in MARKER mode. */
		public volatile JTextField verticalOffsetRatioField;

		/** The upper-side label to explain the meaning of the vertical correction ratio. */
		public volatile JLabel noteUpperLabel;

		/** The lower-side label to explain the meaning of the vertical correction ratio. */
		public volatile JLabel noteLowerLabel;

		/** The right-click menu of symbolField. */
		public volatile TextRightClickMenu symbolFieldRightClickMenu;

		/** The right-click menu of sizeField. */
		public volatile TextRightClickMenu fontSizeFieldRightClickMenu;

		/** The right-click menu of verticalOffsetRatioField. */
		public volatile TextRightClickMenu correctionRatioFieldRightClickMenu;
	}


	/**
	 * Creates a new window.
	 *
	 * @param configuration The configuration of this application.
	 */
	public PointOptionWindow() {

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
			//frame.setVisible(false);
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

			/** The label of the combo box to select a point style mode. */
			styleModeLabel = new JLabel();
			constraints.insets = new Insets(topMargin, leftMargin, 0, rightMargin);
			layout.setConstraints(styleModeLabel, constraints);
			basePanel.add(styleModeLabel);

			constraints.gridy++;

			/** The combo box to select a point style mode. */
			styleModeBox = new JComboBox<MultilingualItem>();
			constraints.insets = new Insets(0, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(styleModeBox, constraints);
			basePanel.add(styleModeBox);
			circleModeItem = new MultilingualItem();
			markerModeItem = new MultilingualItem();
			styleModeBox.addItem(circleModeItem);
			styleModeBox.addItem(markerModeItem);

			constraints.gridy++;

			// The panel on witch the panel for each style mode is mounted. */
			swappablePanel = new JPanel();
			swappablePanel.setLayout(new GridLayout(1, 1));
			basePanel.add(swappablePanel);
			constraints.insets = new Insets(0, 0, 0, 0);
			constraints.weighty = 3.0;
			layout.setConstraints(swappablePanel, constraints);
			this.createCircleModeComponents();
			this.createMarkerModeComponents();
			swappablePanel.add(circleModeComponents.panel);
			//swappablePanel.add(markerModeComponents.panel);

			constraints.gridy++;
			constraints.weighty = 1.0;

			// A separator.
			JSeparator separator = new JSeparator();
			constraints.insets = new Insets(topMarginLong, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(separator, constraints);
			basePanel.add(separator);

			constraints.gridy++;

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

		/**
		 * Creates the UI components for CIRCLE mode.
		 */
		private void createCircleModeComponents() {
			circleModeComponents = new CircleModeItems();
			circleModeComponents.panel = new JPanel();

			// Prepare the layout manager and resources.
			JPanel basePanel = circleModeComponents.panel;
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
			int bottomMargin = 5;
			int leftMargin = 5;
			int leftMarginLong = 25;
			int rightMargin = 5;
			double leftColumnWeight = 0.0;
			double rightColumnWeight = 1.0;

			constraints.gridwidth = 1;
			constraints.gridx = 0;
			constraints.weightx = leftColumnWeight;

			// The title label of the text field of the point radius in CIRCLE mode.
			circleModeComponents.radiusLabel = new JLabel();
			constraints.insets = new Insets(topMargin, leftMarginLong, bottomMargin, rightMargin);
			layout.setConstraints(circleModeComponents.radiusLabel, constraints);
			basePanel.add(circleModeComponents.radiusLabel);

			constraints.gridwidth = 1;
			constraints.gridx = 1;
			constraints.weightx = rightColumnWeight;

			// The text field of the point radius in CIRCLE mode.
			circleModeComponents.radiusField = new JTextField();
			constraints.insets = new Insets(topMargin, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(circleModeComponents.radiusField, constraints);
			basePanel.add(circleModeComponents.radiusField);

			circleModeComponents.radiusFieldRightClickMenu = new TextRightClickMenu();

			constraints.gridy++;
			constraints.gridx = 0;
			constraints.gridwidth = 2;
			constraints.weightx = 1.0;

			// The empty line.
			JLabel emptyLabel = new JLabel(" ");
			constraints.insets = new Insets(topMargin, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(emptyLabel, constraints);
			basePanel.add(emptyLabel);

			constraints.gridy++;

			emptyLabel = new JLabel(" ");
			constraints.insets = new Insets(topMargin, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(emptyLabel, constraints);
			basePanel.add(emptyLabel);

			constraints.gridy++;

			emptyLabel = new JLabel(" ");
			constraints.insets = new Insets(topMargin, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(emptyLabel, constraints);
			basePanel.add(emptyLabel);

			constraints.gridy++;

			emptyLabel = new JLabel(" ");
			constraints.insets = new Insets(topMargin, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(emptyLabel, constraints);
			basePanel.add(emptyLabel);
		}

		/**
		 * Creates the UI components for MARKER mode.
		 */
		private void createMarkerModeComponents() {
			markerModeComponents = new MarkerModeItems();

			markerModeComponents.panel = new JPanel();

			// Prepare the layout manager and resources.
			JPanel basePanel = markerModeComponents.panel;
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
			int topMargin = 3;
			int bottomMargin = 2;
			int leftMargin = 5;
			int leftMarginLong = 25;
			int rightMargin = 5;
			double leftColumnWeight = 0.0;
			double rightColumnWeight = 1.0;

			constraints.gridwidth = 1;
			constraints.gridx = 0;
			constraints.weightx = leftColumnWeight;

			// The title label of the text field to input the symbols in MARKER mode.
			markerModeComponents.symbolLabel = new JLabel();
			constraints.insets = new Insets(topMargin, leftMarginLong, 0, rightMargin);
			layout.setConstraints(markerModeComponents.symbolLabel, constraints);
			basePanel.add(markerModeComponents.symbolLabel);

			constraints.gridwidth = 1;
			constraints.gridx = 1;
			constraints.weightx = rightColumnWeight;

			// The text field to input the symbols in MARKER mode.
			markerModeComponents.symbolField = new JTextField();
			constraints.insets = new Insets(topMargin, leftMargin, 0, rightMargin);
			layout.setConstraints(markerModeComponents.symbolField, constraints);
			basePanel.add(markerModeComponents.symbolField);

			markerModeComponents.symbolFieldRightClickMenu = new TextRightClickMenu();

			constraints.gridy++;
			constraints.gridx = 1;
			constraints.weightx = 1.0;

			// The check box to enable/disable bold font.

			markerModeComponents.boldBox = new JCheckBox();
			constraints.insets = new Insets(0, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(markerModeComponents.boldBox, constraints);
			basePanel.add(markerModeComponents.boldBox);

			constraints.gridy++;

			constraints.gridwidth = 1;
			constraints.gridx = 0;
			constraints.weightx = leftColumnWeight;

			// The title label of the text field of the marker's font size in MARKER mode.
			markerModeComponents.fontSizeLabel = new JLabel();
			constraints.insets = new Insets(topMargin, leftMarginLong, bottomMargin, rightMargin);
			layout.setConstraints(markerModeComponents.fontSizeLabel, constraints);
			basePanel.add(markerModeComponents.fontSizeLabel);

			constraints.gridwidth = 1;
			constraints.gridx = 1;
			constraints.weightx = rightColumnWeight;

			// The text field of the marker's font size in MARKER mode.
			markerModeComponents.sizeField = new JTextField();
			constraints.insets = new Insets(topMargin, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(markerModeComponents.sizeField, constraints);
			basePanel.add(markerModeComponents.sizeField);

			markerModeComponents.fontSizeFieldRightClickMenu = new TextRightClickMenu();

			constraints.gridy++;

			constraints.gridwidth = 1;
			constraints.gridx = 0;
			constraints.weightx = leftColumnWeight;

			// The title label of the text field to input the vertical offset ratio in MARKER mode.
			markerModeComponents.verticalOffsetRatioLabel = new JLabel();
			constraints.insets = new Insets(topMargin, leftMarginLong, bottomMargin, rightMargin);
			layout.setConstraints(markerModeComponents.verticalOffsetRatioLabel, constraints);
			basePanel.add(markerModeComponents.verticalOffsetRatioLabel);

			constraints.gridwidth = 1;
			constraints.gridx = 1;
			constraints.weightx = rightColumnWeight;

			// The text field to input the vertical offset ratio in MARKER mode.
			markerModeComponents.verticalOffsetRatioField = new JTextField();
			constraints.insets = new Insets(topMargin, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(markerModeComponents.verticalOffsetRatioField, constraints);
			basePanel.add(markerModeComponents.verticalOffsetRatioField);

			markerModeComponents.correctionRatioFieldRightClickMenu = new TextRightClickMenu();

			constraints.gridy++;
			constraints.gridwidth = 2;
			constraints.gridx = 0;
			constraints.weighty = 0.5;

			// The upper-side label to explain the meaning of the vertical correction ratio.
			markerModeComponents.noteUpperLabel = new JLabel();
			constraints.insets = new Insets(topMargin, leftMarginLong, 0, rightMargin);
			layout.setConstraints(markerModeComponents.noteUpperLabel, constraints);
			basePanel.add(markerModeComponents.noteUpperLabel);

			constraints.gridy++;

			// The lower-side label to explain the meaning of the vertical correction ratio.
			markerModeComponents.noteLowerLabel = new JLabel();
			constraints.insets = new Insets(0, leftMarginLong, bottomMargin, rightMargin);
			layout.setConstraints(markerModeComponents.noteLowerLabel, constraints);
			basePanel.add(markerModeComponents.noteLowerLabel);

			constraints.weighty = 1.0;
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
			circleModeComponents.radiusFieldRightClickMenu.configure(this.configuration);
			markerModeComponents.symbolFieldRightClickMenu.configure(this.configuration);
			markerModeComponents.fontSizeFieldRightClickMenu.configure(this.configuration);
			markerModeComponents.correctionRatioFieldRightClickMenu.configure(this.configuration);
		}

		/**
		 * Sets Japanese texts to the GUI components.
		 */
		private void setJapaneseTexts() {
			frame.setTitle("オプション設定: 点プロット");
			styleModeLabel.setText("モード:");
			setButton.setText("SET");

			circleModeItem.setText(POINT_STYLE_MODE_CIRCLE_JA);
			markerModeItem.setText(POINT_STYLE_MODE_MARKER_JA);

			circleModeComponents.radiusLabel.setText("円の半径: ");

			markerModeComponents.symbolLabel.setText("マーカー記号: ");
			markerModeComponents.boldBox.setText("太字");
			markerModeComponents.fontSizeLabel.setText("文字サイズ: ");
			markerModeComponents.verticalOffsetRatioLabel.setText("位置補正率: ");

			markerModeComponents.noteUpperLabel.setText("※ フォントのデザインにより、マーカーの上下中心");
			markerModeComponents.noteLowerLabel.setText("　 が多少ずれるため、上記の値で補正してください。");

			seriesFilterBox.setText("系列指定");
			seriesFilterLabel.setText("系列番号: ");
		}

		/**
		 * Sets English texts to the GUI components.
		 */
		private void setEnglishTexts() {
			frame.setTitle("Option Settings: With Points");
			styleModeLabel.setText("Mode:");
			setButton.setText("SET");

			circleModeItem.setText(POINT_STYLE_MODE_CIRCLE_EN);
			markerModeItem.setText(POINT_STYLE_MODE_MARKER_EN);

			circleModeComponents.radiusLabel.setText("Circle Radius: ");

			markerModeComponents.symbolLabel.setText("Marker Symbols: ");
			markerModeComponents.boldBox.setText("Bold Font");
			markerModeComponents.fontSizeLabel.setText("Font Size: ");
			markerModeComponents.verticalOffsetRatioLabel.setText("Position Correct.: ");

			markerModeComponents.noteUpperLabel.setText("* Depending on the font, the marker center may shift.");
			markerModeComponents.noteLowerLabel.setText("　Adjust it using the above parameter.");

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
			styleModeLabel.setFont(uiBoldFont);
			styleModeBox.setFont(uiPlainFont);
			setButton.setFont(uiBoldFont);

			circleModeComponents.radiusLabel.setFont(uiBoldFont);
			circleModeComponents.radiusField.setFont(uiPlainFont);

			markerModeComponents.symbolLabel.setFont(uiBoldFont);
			markerModeComponents.boldBox.setFont(uiBoldFont);
			markerModeComponents.fontSizeLabel.setFont(uiBoldFont);
			markerModeComponents.verticalOffsetRatioLabel.setFont(uiBoldFont);
			markerModeComponents.symbolField.setFont(uiPlainFont);
			markerModeComponents.sizeField.setFont(uiPlainFont);
			markerModeComponents.verticalOffsetRatioField.setFont(uiPlainFont);

			seriesFilterBox.setFont(uiBoldFont);
			seriesFilterLabel.setFont(uiBoldFont);
			seriesFilterField.setFont(uiPlainFont);

			Font smallFont = new Font(uiBoldFont.getName(), Font.BOLD, 11);
			markerModeComponents.noteUpperLabel.setFont(smallFont);
			markerModeComponents.noteLowerLabel.setFont(smallFont);
		}

		/**
		 * Updates the values of text fields, by the values stored in the configuration.
		 */
		private void updateValuesByConfiguration() {
			OptionConfiguration optionConfig = this.configuration.getOptionConfiguration();
			OptionConfiguration.PointOptionConfiguration pointOptionConfig = optionConfig.getPointOptionConfiguration();
			DecimalFormat formatter = new DecimalFormat("#0.0#####");

			setSelectedPointStyleMode(pointOptionConfig.getPointStyleMode());

			circleModeComponents.radiusField.setText(formatter.format(pointOptionConfig.getCircleRadius()));

			markerModeComponents.symbolField.setText(this.markerTextsToUIText(pointOptionConfig.getMarkerTexts()));
			markerModeComponents.boldBox.setSelected(pointOptionConfig.isMarkerBold());
			markerModeComponents.sizeField.setText(formatter.format(pointOptionConfig.getMarkerSize()));
			markerModeComponents.verticalOffsetRatioField.setText(formatter.format(pointOptionConfig.getMarkerVerticalOffsetRatio()));

			switch (pointOptionConfig.getSeriesFilterMode()) {
				case INDEX : {
					seriesFilterBox.setSelected(true);
					seriesFilterField.setEditable(true);
					seriesFilterField.setBackground(Color.WHITE);
					seriesFilterField.setForeground(Color.BLACK);

					// Converts the int[] type array of series indices to a single text value, and set it to seriesFilterField.
					IndexSeriesFilter filter = pointOptionConfig.getIndexSeriesFilter();
					int[] seriesIndices = filter.getIncludedSeriesIndices();
					StringBuilder seriesIndicesTextBuilder = new StringBuilder();
					for (int iseries = 0; iseries<seriesIndices.length; iseries++) {
						seriesIndicesTextBuilder.append(seriesIndices[iseries]);
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
					seriesFilterField.setEditable(false);
					seriesFilterField.setBackground(Color.LIGHT_GRAY);
					seriesFilterField.setForeground(Color.GRAY);
					break;
				}
				default : {
					throw new IllegalStateException("Unexpected series filter mode: " + pointOptionConfig.getSeriesFilterMode());
				}
			}
		}

		/**
		 * Converts the array storing the texts of markers to a single text value to be set to UI.
		 *
		 * @param markerTexts The array storing the texts of markers.
		 * @return A single text value to be set to UI.
		 */
		private String markerTextsToUIText(String[] markerTexts) {
			int textCount = markerTexts.length;
			StringBuilder uiTextBuilder = new StringBuilder();
			for (int itext=0; itext<textCount; itext++) {

				// If the text contains comma (,) characters, escape them.
				String escapedLabel = markerTexts[itext].contains(",") ? markerTexts[itext].replaceAll(",", "\\,") : markerTexts[itext];

				// Append the escaped text to the UI text, and also append a comma (,) as the delimiter between values.
				uiTextBuilder.append(escapedLabel);
				if (itext != textCount - 1) {
					uiTextBuilder.append(",");
				}
			}
			return uiTextBuilder.toString();
		}
	}


	/**
	 * Gets the selected item of styleModeBox field as an element of OptionConfiguration.PointStyleMode enum.
	 * This method is invokable only on the event-dispatch thread.
	 *
	 * @return The ShapeMode enum element corresponding to the selected item of styleModeBox.
	 */
	public OptionConfiguration.PointStyleMode getSelectedPointStyleMode() {
		if (!SwingUtilities.isEventDispatchThread()) {
			throw new IllegalStateException("This method is invokable only on the event-dispatch thread.");
		}
		String selectedItemText = this.styleModeBox.getSelectedItem().toString();
		switch (selectedItemText) {
			case POINT_STYLE_MODE_CIRCLE_EN:
			case POINT_STYLE_MODE_CIRCLE_JA: {
				return OptionConfiguration.PointStyleMode.CIRCLE;
			}
			case POINT_STYLE_MODE_MARKER_EN:
			case POINT_STYLE_MODE_MARKER_JA: {
				return OptionConfiguration.PointStyleMode.MARKER;
			}
			default: {
				throw new IllegalStateException("Unexpected point style mode: " + selectedItemText);
			}
		}
	}


	/**
	 * Sets the selected item of styleModeBox field by an element of OptionConfiguration.PointStyleMode enum.
	 * This method is invokable only on the event-dispatch thread.
	 *
	 * @return The ShapeMode enum element corresponding to the selected item of styleModeBox to be selected.
	 */
	public void setSelectedPointStyleMode(OptionConfiguration.PointStyleMode mode) {
		if (!SwingUtilities.isEventDispatchThread()) {
			throw new IllegalStateException("This method is invokable only on the event-dispatch thread.");
		}
		switch (mode) {
			case CIRCLE: {
				this.styleModeBox.setSelectedItem(this.circleModeItem);
				return;
			}
			case MARKER: {
				this.styleModeBox.setSelectedItem(this.markerModeItem);
				return;
			}
			default: {
				throw new IllegalStateException("Unexpected point style mode: " + mode);
			}
		}

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
