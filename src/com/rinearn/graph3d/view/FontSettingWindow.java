package com.rinearn.graph3d.view;

import com.rinearn.graph3d.config.FontConfiguration;
import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;

import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.GraphicsEnvironment;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.SwingUtilities;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;


/**
 * The window of "Set Fonts" menu.
 */
public class FontSettingWindow {

	/** The default width [px] of this window. */
	public static final int DEFAULT_WINDOW_WIDTH = 650;

	/** The default height [px] of this window. */
	public static final int DEFAULT_WINDOW_HEIGHT = 320;

	/** The item of the combo boxes representing the default font, in English. */
	public static final String DEFAULT_FONT_ITEM_ENGLISH = "- Default -";

	/** The item of the combo boxes representing the default font, in Japanese. */
	public static final String DEFAULT_FONT_ITEM_JAPANESE = "- 標準 -";

	/** The frame of this window. */
	public volatile JFrame frame;


	/** The label of "UI Font". */
	public volatile JLabel uiFontLabel;

	/** The combo box of "UI Font". */
	public volatile JComboBox<String> uiFontNameBox;

	/** The combo box of "Size" label of "UI Font". */
	public volatile JLabel uiFontSizeLabel;

	/** The combo box of "Size" text field of "UI Font". */
	public volatile JTextField uiFontSizeField;

	/** The combo box of "Bold" check box of "UI Font". */
	public volatile JCheckBox uiFontBoldBox;


	/** The label of "Axis Label Font". */
	public volatile JLabel axisLabelFontLabel;

	/** The combo box of "Axis Label Font". */
	public volatile JComboBox<String> axisLabelFontNameBox;

	/** The combo box of "Size" label of "Axis Label Font". */
	public volatile JLabel axisLabelFontSizeLabel;

	/** The combo box of "Size" text field of "Axis Label Font". */
	public volatile JTextField axisLabelFontSizeField;

	/** The combo box of "Bold" check box of "Axis Label Font". */
	public volatile JCheckBox axisLabelFontBoldBox;


	/** The label of "Scale Tick Label". */
	public volatile JLabel tickLabelFontLabel;

	/** The combo box of "Scale Tick Label Font". */
	public volatile JComboBox<String> tickLabelFontNameBox;

	/** The combo box of "Size" label of "Tick Label Font". */
	public volatile JLabel tickLabelFontSizeLabel;

	/** The combo box of "Size" text field of "Tick Label Font". */
	public volatile JTextField tickLabelFontSizeField;

	/** The combo box of "Bold" check box of "Tick Label Font". */
	public volatile JCheckBox tickLabelFontBoldBox;


	/** "OK" button. */
	public volatile JButton okButton;


	/**
	 * Creates a new window.
	 *
	 * @param configuration The configuration of this application.
	 */
	public FontSettingWindow() {

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

			// Prepare the layout manager and resources.
			Container basePanel = frame.getContentPane();
			GridBagLayout layour = new GridBagLayout();
			basePanel.setLayout(layour);
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
			int rightMargin = 5;
			int bottomMarginInSection = 10;
			int leftMarginOfLabelsInSection = 5;
			int leftMarginOfComboBoxesInSection = 20;
			int leftMarginOfCheckBoxesInSection = 10;

			constraints.gridwidth = 4;

			// Create the label of "UI Font" section.
			uiFontLabel = new JLabel("Unconfigured");
			constraints.insets = new Insets(topMargin, leftMarginOfLabelsInSection, 0, rightMargin);
			layour.setConstraints(uiFontLabel, constraints);
			basePanel.add(uiFontLabel);

			constraints.gridy++;

			// Create the components of "UI Font".
			{
				constraints.gridx = 0;
				constraints.gridwidth = 1;

				uiFontNameBox = new JComboBox<String>();
				constraints.insets = new Insets(0, leftMarginOfComboBoxesInSection, bottomMarginInSection, 0);
				constraints.weightx = 10.0;
				layour.setConstraints(uiFontNameBox, constraints);
				basePanel.add(uiFontNameBox);

				constraints.gridx++;
				constraints.weightx = 1.0;

				uiFontSizeLabel = new JLabel("Unconfigured", JLabel.RIGHT);
				constraints.insets = new Insets(0, 0, bottomMarginInSection, 0);
				layour.setConstraints(uiFontSizeLabel, constraints);
				basePanel.add(uiFontSizeLabel);

				constraints.gridx++;

				uiFontSizeField = new JTextField("Unconfigured");
				constraints.insets = new Insets(0, 0, bottomMarginInSection, 0);
				layour.setConstraints(uiFontSizeField, constraints);
				basePanel.add(uiFontSizeField);

				constraints.gridx++;

				uiFontBoldBox = new JCheckBox("Unconfigured");
				constraints.insets = new Insets(0, leftMarginOfCheckBoxesInSection, bottomMarginInSection, rightMargin);
				layour.setConstraints(uiFontBoldBox, constraints);
				basePanel.add(uiFontBoldBox);

				// We don't use the following in this version.
				uiFontBoldBox.setVisible(false);

				constraints.gridx = 0;
				constraints.gridy++;
				constraints.gridwidth = 4;
			}

			// Create the label of "Axis Label Font" section.
			axisLabelFontLabel = new JLabel("Unconfigured");
			constraints.insets = new Insets(0, leftMarginOfLabelsInSection, 0, rightMargin);
			layour.setConstraints(axisLabelFontLabel, constraints);
			basePanel.add(axisLabelFontLabel);

			constraints.gridy++;

			// Create the components of "Axis Label Font".
			{
				constraints.gridx = 0;
				constraints.gridwidth = 1;

				axisLabelFontNameBox = new JComboBox<String>();
				constraints.insets = new Insets(0, leftMarginOfComboBoxesInSection, bottomMarginInSection, 0);
				constraints.weightx = 10.0;
				layour.setConstraints(axisLabelFontNameBox, constraints);
				basePanel.add(axisLabelFontNameBox);

				constraints.gridx++;
				constraints.weightx = 1.0;

				axisLabelFontSizeLabel = new JLabel("Unconfigured", JLabel.RIGHT);
				constraints.insets = new Insets(0, 0, bottomMarginInSection, 0);
				layour.setConstraints(axisLabelFontSizeLabel, constraints);
				basePanel.add(axisLabelFontSizeLabel);

				constraints.gridx++;

				axisLabelFontSizeField = new JTextField("Unconfigured");
				constraints.insets = new Insets(0, 0, bottomMarginInSection, 0);
				layour.setConstraints(axisLabelFontSizeField, constraints);
				basePanel.add(axisLabelFontSizeField);

				constraints.gridx++;

				axisLabelFontBoldBox = new JCheckBox("Unconfigured");
				constraints.insets = new Insets(0, leftMarginOfCheckBoxesInSection, bottomMarginInSection, rightMargin);
				layour.setConstraints(axisLabelFontBoldBox, constraints);
				basePanel.add(axisLabelFontBoldBox);

				constraints.gridx = 0;
				constraints.gridy++;
				constraints.gridwidth = 4;
			}

			// Create the label of "Tick Label Font" section.
			tickLabelFontLabel = new JLabel("Unconfigured");
			constraints.insets = new Insets(0, leftMarginOfLabelsInSection, 0, rightMargin);
			layour.setConstraints(tickLabelFontLabel, constraints);
			basePanel.add(tickLabelFontLabel);

			constraints.gridy++;

			// Create the components of "Tick Label Font".
			{
				constraints.gridx = 0;
				constraints.gridwidth = 1;

				tickLabelFontNameBox = new JComboBox<String>();
				constraints.insets = new Insets(0, leftMarginOfComboBoxesInSection, bottomMarginInSection, 0);
				constraints.weightx = 10.0;
				layour.setConstraints(tickLabelFontNameBox, constraints);
				basePanel.add(tickLabelFontNameBox);

				constraints.gridx++;
				constraints.weightx = 1.0;

				tickLabelFontSizeLabel = new JLabel("Unconfigured", JLabel.RIGHT);
				constraints.insets = new Insets(0, 0, bottomMarginInSection, 0);
				layour.setConstraints(tickLabelFontSizeLabel, constraints);
				basePanel.add(tickLabelFontSizeLabel);

				constraints.gridx++;

				tickLabelFontSizeField = new JTextField("Unconfigured");
				constraints.insets = new Insets(0, 0, bottomMarginInSection, 0);
				layour.setConstraints(tickLabelFontSizeField, constraints);
				basePanel.add(tickLabelFontSizeField);

				constraints.gridx++;

				tickLabelFontBoldBox = new JCheckBox("Unconfigured");
				constraints.insets = new Insets(0, leftMarginOfCheckBoxesInSection, bottomMarginInSection, rightMargin);
				layour.setConstraints(tickLabelFontBoldBox, constraints);
				basePanel.add(tickLabelFontBoldBox);

				constraints.gridx = 0;
				constraints.gridy++;
				constraints.gridwidth = 4;
			}

			// An empty line.
			JLabel emptyLine = new JLabel("");
			constraints.insets = new Insets(0, 0, 0, 0);
			layour.setConstraints(emptyLine, constraints);
			basePanel.add(emptyLine);
			constraints.gridy++;

			// "OK" button.
			okButton = new JButton("Unconfigured");
			constraints.insets = new Insets(0, leftMargin, bottomMargin, rightMargin);
			constraints.weighty = 1.8;
			layour.setConstraints(okButton, constraints);
			basePanel.add(okButton);
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
				//this.setEnglishTexts();
			} else {
				this.setEnglishTexts();
			}

			// Set fonts to the components.
			this.setFonts();

			// Updates the current values on the window, by the values stored in the configuration.
			this.updateValuesByConfiguration();
		}

		/**
		 * Sets Japanese texts to the GUI components.
		 */
		private void setJapaneseTexts() {
			frame.setTitle("フォントの設定");
			uiFontLabel.setText("UIのフォント:");
			axisLabelFontLabel.setText("軸ラベルのフォント:");
			tickLabelFontLabel.setText("目盛り数値のフォント:");
			okButton.setText("OK");

			uiFontSizeLabel.setText("サイズ: ");
			axisLabelFontSizeLabel.setText("サイズ: ");
			tickLabelFontSizeLabel.setText("サイズ: ");

			uiFontBoldBox.setText("太字");
			axisLabelFontBoldBox.setText("太字");
			tickLabelFontBoldBox.setText("太字");
		}

		/**
		 * Sets English texts to the GUI components.
		 */
		private void setEnglishTexts() {
			frame.setTitle("Set Fonts");
			uiFontLabel.setText("UI Font:");
			axisLabelFontLabel.setText("Font of Axis Labels:");
			tickLabelFontLabel.setText("Font of Scale Tick Numbers:");
			okButton.setText("OK");

			uiFontSizeLabel.setText("Size: ");
			axisLabelFontSizeLabel.setText("Size: ");
			tickLabelFontSizeLabel.setText("Size: ");

			uiFontBoldBox.setText("Bold");
			axisLabelFontBoldBox.setText("Bold");
			tickLabelFontBoldBox.setText("Bold");
		}

		/**
		 * Set fonts to the components.
		 */
		private void setFonts() {
			FontConfiguration fontConfig = configuration.getFontConfiguration();
			Font uiBoldFont = fontConfig.getUIBoldFont();
			Font uiPlainFont = fontConfig.getUIPlainFont();

			uiFontLabel.setFont(uiBoldFont);
			axisLabelFontLabel.setFont(uiBoldFont);
			tickLabelFontLabel.setFont(uiBoldFont);
			okButton.setFont(uiBoldFont);

			uiFontNameBox.setFont(uiBoldFont);
			axisLabelFontNameBox.setFont(uiBoldFont);
			tickLabelFontNameBox.setFont(uiBoldFont);

			uiFontSizeLabel.setFont(uiBoldFont);
			axisLabelFontSizeLabel.setFont(uiBoldFont);
			tickLabelFontSizeLabel.setFont(uiBoldFont);

			uiFontSizeField.setFont(uiPlainFont);
			axisLabelFontSizeField.setFont(uiPlainFont);
			tickLabelFontSizeField.setFont(uiPlainFont);

			uiFontBoldBox.setFont(uiBoldFont);
			axisLabelFontBoldBox.setFont(uiBoldFont);
			tickLabelFontBoldBox.setFont(uiBoldFont);
		}


		private void updateValuesByConfiguration() {
			FontConfiguration fontConfig = this.configuration.getFontConfiguration();
			boolean isJapanese = this.configuration.getEnvironmentConfiguration().isLocaleJapanese();

			// Detect whether we should update the list of font names.
			// (it is required when they have not been initialized, or the language has been changed.)
			boolean fontUpdateNecessary = false;
			if (uiFontNameBox.getItemCount() == 0) {
				fontUpdateNecessary = true;
			} else {
				if (uiFontNameBox.getItemAt(0) == null) {
					fontUpdateNecessary = true;
				}
				if (isJapanese && uiFontNameBox.getItemAt(0).equals(DEFAULT_FONT_ITEM_ENGLISH)) {
					fontUpdateNecessary = true;
				}
				if (!isJapanese && uiFontNameBox.getItemAt(0).equals(DEFAULT_FONT_ITEM_JAPANESE)) {
					fontUpdateNecessary = true;
				}
			}

			// Update the list of font names, selectable on the combo boxes on this window.
			if (fontUpdateNecessary) {
				String[] fontNames = this.generateFontNames(isJapanese);
				uiFontNameBox.removeAllItems();
				axisLabelFontNameBox.removeAllItems();
				tickLabelFontNameBox.removeAllItems();
				for (String fontName: fontNames) {
					uiFontNameBox.addItem(fontName);
					axisLabelFontNameBox.addItem(fontName);
					tickLabelFontNameBox.addItem(fontName);
				}
			}

			// Get the current fonts stored in the font configuration container.
			Font uiFont = fontConfig.getUIPlainFont();
			Font axisLabelFont = fontConfig.getAxisLabelFont();
			Font tickLabelFont = fontConfig.getTickLabelFont();

			// Update the selected fonts on the combo boxes, if they don't match with the fonts specified by the config.
			if (!uiFont.getName().equals(uiFontNameBox.getSelectedItem())) {

				// Set to the default font at first.
				// - Don't use setSelectedIndex(0) here.
				//   Probably, there may be time-lag for reflecting the above UI update,
				//   so it might throw exceptions caused by "index out of bounds", sometimes.
				if (isJapanese) {
					uiFontNameBox.setSelectedItem(DEFAULT_FONT_ITEM_JAPANESE);
				} else {
					uiFontNameBox.setSelectedItem(DEFAULT_FONT_ITEM_ENGLISH);
				}

				// Then, select the specified font if it exists.
				uiFontNameBox.setSelectedItem(uiFont.getName());
			}
			if (!axisLabelFont.getName().equals(axisLabelFontNameBox.getSelectedItem())) {
				if (isJapanese) {
					axisLabelFontNameBox.setSelectedItem(DEFAULT_FONT_ITEM_JAPANESE);
				} else {
					axisLabelFontNameBox.setSelectedItem(DEFAULT_FONT_ITEM_ENGLISH);
				}
				axisLabelFontNameBox.setSelectedItem(axisLabelFont.getName());
			}
			if (!tickLabelFont.getName().equals(tickLabelFontNameBox.getSelectedItem())) {
				if (isJapanese) {
					tickLabelFontNameBox.setSelectedItem(DEFAULT_FONT_ITEM_JAPANESE);
				} else {
					tickLabelFontNameBox.setSelectedItem(DEFAULT_FONT_ITEM_ENGLISH);
				}
				tickLabelFontNameBox.setSelectedItem(tickLabelFont.getName());
			}

			// Update the sizes of the fonts.
			uiFontSizeField.setText(Integer.toString(uiFont.getSize()));
			axisLabelFontSizeField.setText(Integer.toString(axisLabelFont.getSize()));
			tickLabelFontSizeField.setText(Integer.toString(tickLabelFont.getSize()));

			// Update the on/off states of the "Bold" boxes.
			uiFontBoldBox.setSelected(uiFont.isBold());
			axisLabelFontBoldBox.setSelected(axisLabelFont.isBold());
			tickLabelFontBoldBox.setSelected(tickLabelFont.isBold());
		}

		/**
		 * Generates the list of the available font names,
		 * used as items of the combo boxes on this window.
		 *
		 * @param isJapanese Specifyt true if the locale of the environment is Japanese.
		 * @return The list of the font names.
		 */
		private String[] generateFontNames(boolean isJapanese) {
			List<String> fontNameList = new ArrayList<String>();

			// Add the virtual font name representing the default font, to the top of the list.
			if (isJapanese) {
				fontNameList.add(DEFAULT_FONT_ITEM_JAPANESE);
			} else {
				fontNameList.add(DEFAULT_FONT_ITEM_ENGLISH);
			}

			// Add the available fonts on this environment.
			Font[] environmentAvailableFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
			Set<String> containedSet = new HashSet<String>();
			for (Font font: environmentAvailableFonts) {
				String name = font.getFamily();

				if (!containedSet.contains(name)) {
					containedSet.add(name);
					fontNameList.add(name);
				}
			}

			// Convert the list to an array, and return it.
			String[] fontNames = new String[fontNameList.size()];
			fontNames = fontNameList.toArray(fontNames);
			return fontNames;
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
