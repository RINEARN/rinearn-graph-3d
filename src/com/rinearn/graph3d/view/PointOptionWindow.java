package com.rinearn.graph3d.view;

import com.rinearn.graph3d.config.FontConfiguration;
import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;

import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import java.lang.reflect.InvocationTargetException;


/**
 * The window of "With Points" option.
 */
public final class PointOptionWindow {

	/** The default width [px] of this window. */
	public static final int DEFAULT_WINDOW_WIDTH = 400;

	/** The default height [px] of this window. */
	public static final int DEFAULT_WINDOW_HEIGHT = 430;

	/** The display name of the point style mode "CIRCLE" in Japanese. */
	public static final String POINT_STYLE_MODE_CIRCLE_JA = "円形";

	/** The display name of the point style mode "CIRCLE" in English. */
	public static final String POINT_STYLE_MODE_CIRCLE_EN = "Circle";

	/** The display name of the point style mode "MARKER" in Japanese. */
	public static final String POINT_STYLE_MODE_MARKER_JA = "マーカー";

	/** The display name of the point style mode "MARKER" in English. */
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
	public volatile CircleModeItems circleModeItems;

	/** The container of UI components in MARKER mode. */
	public volatile MarkerModeItems markerModeItems;

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
		public volatile JTextField fontSizeField;

		/** The title label of the text field to input the vertical correction ratio in MARKER mode. */
		public volatile JLabel correctionRatioLabel;

		/** The text field to input the vertical correction ratio in MARKER mode. */
		public volatile JTextField correctionRatioField;

		/** The upper-side label to explain the meaning of the vertical correction ratio. */
		public volatile JLabel noteUpperLabel;

		/** The lower-side label to explain the meaning of the vertical correction ratio. */
		public volatile JLabel noteLowerLabel;

		/** The right-click menu of symbolField. */
		public volatile TextRightClickMenu symbolFieldRightClickMenu;

		/** The right-click menu of fontSizeField. */
		public volatile TextRightClickMenu fontSizeFieldRightClickMenu;

		/** The right-click menu of correctionRatioField. */
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
			swappablePanel.add(circleModeItems.panel);
			//swappablePanel.add(markerModeItems.panel);

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
			circleModeItems = new CircleModeItems();
			circleModeItems.panel = new JPanel();

			// Prepare the layout manager and resources.
			JPanel basePanel = circleModeItems.panel;
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
			circleModeItems.radiusLabel = new JLabel();
			constraints.insets = new Insets(topMargin, leftMarginLong, bottomMargin, rightMargin);
			layout.setConstraints(circleModeItems.radiusLabel, constraints);
			basePanel.add(circleModeItems.radiusLabel);

			constraints.gridwidth = 1;
			constraints.gridx = 1;
			constraints.weightx = rightColumnWeight;

			// The text field of the point radius in CIRCLE mode.
			circleModeItems.radiusField = new JTextField();
			constraints.insets = new Insets(topMargin, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(circleModeItems.radiusField, constraints);
			basePanel.add(circleModeItems.radiusField);

			circleModeItems.radiusFieldRightClickMenu = new TextRightClickMenu();

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
			markerModeItems = new MarkerModeItems();

			markerModeItems.panel = new JPanel();

			// Prepare the layout manager and resources.
			JPanel basePanel = markerModeItems.panel;
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
			markerModeItems.symbolLabel = new JLabel();
			constraints.insets = new Insets(topMargin, leftMarginLong, 0, rightMargin);
			layout.setConstraints(markerModeItems.symbolLabel, constraints);
			basePanel.add(markerModeItems.symbolLabel);

			constraints.gridwidth = 1;
			constraints.gridx = 1;
			constraints.weightx = rightColumnWeight;

			// The text field to input the symbols in MARKER mode.
			markerModeItems.symbolField = new JTextField();
			constraints.insets = new Insets(topMargin, leftMargin, 0, rightMargin);
			layout.setConstraints(markerModeItems.symbolField, constraints);
			basePanel.add(markerModeItems.symbolField);

			markerModeItems.symbolFieldRightClickMenu = new TextRightClickMenu();

			constraints.gridy++;
			constraints.gridx = 1;
			constraints.weightx = 1.0;

			// The check box to enable/disable bold font.

			markerModeItems.boldBox = new JCheckBox();
			constraints.insets = new Insets(0, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(markerModeItems.boldBox, constraints);
			basePanel.add(markerModeItems.boldBox);

			constraints.gridy++;

			constraints.gridwidth = 1;
			constraints.gridx = 0;
			constraints.weightx = leftColumnWeight;

			// The title label of the text field of the marker's font size in MARKER mode.
			markerModeItems.fontSizeLabel = new JLabel();
			constraints.insets = new Insets(topMargin, leftMarginLong, bottomMargin, rightMargin);
			layout.setConstraints(markerModeItems.fontSizeLabel, constraints);
			basePanel.add(markerModeItems.fontSizeLabel);

			constraints.gridwidth = 1;
			constraints.gridx = 1;
			constraints.weightx = rightColumnWeight;

			// The text field of the marker's font size in MARKER mode.
			markerModeItems.fontSizeField = new JTextField();
			constraints.insets = new Insets(topMargin, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(markerModeItems.fontSizeField, constraints);
			basePanel.add(markerModeItems.fontSizeField);

			markerModeItems.fontSizeFieldRightClickMenu = new TextRightClickMenu();

			constraints.gridy++;

			constraints.gridwidth = 1;
			constraints.gridx = 0;
			constraints.weightx = leftColumnWeight;

			// The title label of the text field to input the vertical correction ratio in MARKER mode.
			markerModeItems.correctionRatioLabel = new JLabel();
			constraints.insets = new Insets(topMargin, leftMarginLong, bottomMargin, rightMargin);
			layout.setConstraints(markerModeItems.correctionRatioLabel, constraints);
			basePanel.add(markerModeItems.correctionRatioLabel);

			constraints.gridwidth = 1;
			constraints.gridx = 1;
			constraints.weightx = rightColumnWeight;

			// The text field to input the vertical correction ratio in MARKER mode.
			markerModeItems.correctionRatioField = new JTextField();
			constraints.insets = new Insets(topMargin, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(markerModeItems.correctionRatioField, constraints);
			basePanel.add(markerModeItems.correctionRatioField);

			markerModeItems.correctionRatioFieldRightClickMenu = new TextRightClickMenu();

			constraints.gridy++;
			constraints.gridwidth = 2;
			constraints.gridx = 0;
			constraints.weighty = 0.5;

			// The upper-side label to explain the meaning of the vertical correction ratio.
			markerModeItems.noteUpperLabel = new JLabel();
			constraints.insets = new Insets(topMargin, leftMarginLong, 0, rightMargin);
			layout.setConstraints(markerModeItems.noteUpperLabel, constraints);
			basePanel.add(markerModeItems.noteUpperLabel);

			constraints.gridy++;

			// The lower-side label to explain the meaning of the vertical correction ratio.
			markerModeItems.noteLowerLabel = new JLabel();
			constraints.insets = new Insets(0, leftMarginLong, bottomMargin, rightMargin);
			layout.setConstraints(markerModeItems.noteLowerLabel, constraints);
			basePanel.add(markerModeItems.noteLowerLabel);

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
			circleModeItems.radiusFieldRightClickMenu.configure(this.configuration);
			markerModeItems.symbolFieldRightClickMenu.configure(this.configuration);
			markerModeItems.fontSizeFieldRightClickMenu.configure(this.configuration);
			markerModeItems.correctionRatioFieldRightClickMenu.configure(this.configuration);
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

			circleModeItems.radiusLabel.setText("円の半径: ");

			markerModeItems.symbolLabel.setText("マーカー記号: ");
			markerModeItems.boldBox.setText("太字");
			markerModeItems.fontSizeLabel.setText("文字サイズ: ");
			markerModeItems.correctionRatioLabel.setText("位置補正率: ");

			markerModeItems.noteUpperLabel.setText("※ フォントのデザインにより、マーカーの上下中心");
			markerModeItems.noteLowerLabel.setText("　 が多少ずれるため、上記の値で補正してください。");

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

			circleModeItems.radiusLabel.setText("Circle Radius: ");

			markerModeItems.symbolLabel.setText("Marker Symbols: ");
			markerModeItems.boldBox.setText("Bold Font");
			markerModeItems.fontSizeLabel.setText("Font Size: ");
			markerModeItems.correctionRatioLabel.setText("Position Correct.: ");

			markerModeItems.noteUpperLabel.setText("* Depending on the font, the marker center may shift.");
			markerModeItems.noteLowerLabel.setText("　Adjust it using the above parameter.");

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

			circleModeItems.radiusLabel.setFont(uiBoldFont);
			circleModeItems.radiusField.setFont(uiPlainFont);

			markerModeItems.symbolLabel.setFont(uiBoldFont);
			markerModeItems.boldBox.setFont(uiBoldFont);
			markerModeItems.fontSizeLabel.setFont(uiBoldFont);
			markerModeItems.correctionRatioLabel.setFont(uiBoldFont);
			markerModeItems.symbolField.setFont(uiPlainFont);
			markerModeItems.fontSizeField.setFont(uiPlainFont);
			markerModeItems.correctionRatioField.setFont(uiPlainFont);

			seriesFilterBox.setFont(uiBoldFont);
			seriesFilterLabel.setFont(uiBoldFont);
			seriesFilterField.setFont(uiPlainFont);

			Font smallFont = new Font(uiBoldFont.getName(), Font.BOLD, 11);
			markerModeItems.noteUpperLabel.setFont(smallFont);
			markerModeItems.noteLowerLabel.setFont(smallFont);
		}

		/**
		 * Updates the values of text fields, by the values stored in the configuration.
		 */
		private void updateValuesByConfiguration() {
			/*
			String xLabel = this.configuration.getLabelConfiguration().getXLabelConfiguration().getText();
			String yLabel = this.configuration.getLabelConfiguration().getYLabelConfiguration().getText();
			String zLabel = this.configuration.getLabelConfiguration().getZLabelConfiguration().getText();
			xLabelTextField.setText(xLabel);
			yLabelTextField.setText(yLabel);
			zLabelTextField.setText(zLabel);
			*/
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
