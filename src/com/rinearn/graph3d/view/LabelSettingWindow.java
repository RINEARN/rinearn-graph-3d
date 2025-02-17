package com.rinearn.graph3d.view;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.font.FontConfiguration;
import com.rinearn.graph3d.config.label.LabelConfiguration;
import com.rinearn.graph3d.config.label.LegendLabelConfiguration;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import java.awt.Container;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import java.lang.reflect.InvocationTargetException;


/**
 * The window of "Set Labels" menu.
 */
public final class LabelSettingWindow {

	/** The default width [px] of this window. */
	public static final int DEFAULT_WINDOW_WIDTH = 480;

	/** The default height [px] of this window. */
	public static final int DEFAULT_WINDOW_HEIGHT = 600;

	/** The frame of this window. */
	public volatile JFrame frame;

	/** The title label of the text field for inputting the X label's text. */
	public volatile JLabel xLabelSectionLabel;

	/** The text field for inputting the X label's text. */
	public volatile JTextField xLabelField;

	/** The right-click menu of xLabelField. */
	public volatile TextRightClickMenu xLabelFieldRightClickMenu;

	/** The title label of the text field for inputting the Y label's text. */
	public volatile JLabel yLabelSectionLabel;

	/** The text field for inputting the Y label's text. */
	public volatile JTextField yLabelField;

	/** The right-click menu of yLabelField. */
	public volatile TextRightClickMenu yLabelFieldRightClickMenu;

	/** The title label of the text field for inputting the Z label's text. */
	public volatile JLabel zLabelSectionLabel;

	/** The text field for inputting the Z label's text. */
	public volatile JTextField zLabelTextField;

	/** The right-click menu of zLabelTextField. */
	public volatile TextRightClickMenu zLabelFieldRightClickMenu;

	/** The label of the legend section. */
	public volatile JLabel legendSectionLabel;

	/** The checkbox to enable/disable auto-legend-generation feature. */
	public volatile JCheckBox autoLegendGenerationBox;

	/** The checkbox to enable/disable gap-removal feature. */
	public volatile JCheckBox gapRemovalBox;

	/** The text area to input legends. */
	public volatile JTextArea legendArea;

	/** The right-click menu of legendArea. */
	public volatile TextRightClickMenu legendAreaRightClickMenu;

	/** The scroll pane of legendArea. */
	public volatile JScrollPane legendAreaScrollPane;


	/** The button to reflect settings. */
	public volatile JButton okButton;


	/**
	 * Creates a new window.
	 *
	 * @param configuration The configuration of this application.
	 */
	public LabelSettingWindow() {

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
			int leftMarginLong = 20;
			int rightMargin = 5;
			int marginBetweenLabelAndField = 10;

			constraints.gridwidth = 2;
			constraints.weightx = 1.0;

			// Create the title label of the text field for inputting the X label's text.
			xLabelSectionLabel = new JLabel("Unconfigured");
			xLabelSectionLabel.setVerticalAlignment(JLabel.BOTTOM);
			constraints.insets = new Insets(topMargin, leftMargin, marginBetweenLabelAndField, rightMargin);
			layout.setConstraints(xLabelSectionLabel, constraints);
			basePanel.add(xLabelSectionLabel);

			constraints.gridy++;

			// Create the text field for inputting the X label's text.
			xLabelField = new JTextField("Unconfigured");
			constraints.insets = new Insets(0, leftMarginLong, 0, rightMargin);
			layout.setConstraints(xLabelField, constraints);
			basePanel.add(xLabelField);

			// The right click menu for the above text field.
			xLabelFieldRightClickMenu = new TextRightClickMenu();

			constraints.gridy++;

			// Create the title label of the text field for inputting the Y label's text.
			yLabelSectionLabel = new JLabel("Unconfigured");
			yLabelSectionLabel.setVerticalAlignment(JLabel.BOTTOM);
			constraints.insets = new Insets(topMargin, leftMargin, marginBetweenLabelAndField, rightMargin);
			layout.setConstraints(yLabelSectionLabel, constraints);
			basePanel.add(yLabelSectionLabel);

			constraints.gridy++;

			// Create the text field for inputting the Y label's text.
			yLabelField = new JTextField("Unconfigured");
			constraints.insets = new Insets(0, leftMarginLong, 0, rightMargin);
			layout.setConstraints(yLabelField, constraints);
			basePanel.add(yLabelField);

			// The right click menu for the above text field.
			yLabelFieldRightClickMenu = new TextRightClickMenu();

			constraints.gridy++;

			// Create the title label of the text field for inputting the Z label's text.
			zLabelSectionLabel = new JLabel("Unconfigured");
			zLabelSectionLabel.setVerticalAlignment(JLabel.BOTTOM);
			constraints.insets = new Insets(topMargin, leftMargin, marginBetweenLabelAndField, rightMargin);
			layout.setConstraints(zLabelSectionLabel, constraints);
			basePanel.add(zLabelSectionLabel);

			constraints.gridy++;

			// Create the text field for inputting the Y label's text.
			zLabelTextField = new JTextField("Unconfigured");
			constraints.insets = new Insets(0, leftMarginLong, 0, rightMargin);
			layout.setConstraints(zLabelTextField, constraints);
			basePanel.add(zLabelTextField);

			// The right click menu for the above text field.
			zLabelFieldRightClickMenu = new TextRightClickMenu();

			constraints.gridy++;

			// Insert an empty row.
			JLabel emptyLabel = new JLabel(" ");
			constraints.insets = new Insets(0, 0, 0, 0);
			layout.setConstraints(emptyLabel, constraints);
			basePanel.add(emptyLabel);

			constraints.gridy++;

			// The label of the section to edit legends.
			legendSectionLabel = new JLabel("Unconfigured");
			constraints.insets = new Insets(topMargin, leftMargin, 0, rightMargin);
			layout.setConstraints(legendSectionLabel, constraints);
			basePanel.add(legendSectionLabel);

			constraints.gridy++;
			constraints.gridwidth = 1;

			// The checkbox to enable/disable the auto-legend-generation feature.
			autoLegendGenerationBox = new JCheckBox("Unconfigured");
			constraints.insets = new Insets(0, leftMarginLong, 0, rightMargin);
			layout.setConstraints(autoLegendGenerationBox, constraints);
			basePanel.add(autoLegendGenerationBox);

			constraints.gridx++;
			constraints.weightx = 30.0;

			// The checkbox to enable/disable the gap-removal feature.
			gapRemovalBox = new JCheckBox("Unconfigured");
			constraints.insets = new Insets(0, leftMarginLong, 0, rightMargin);
			layout.setConstraints(gapRemovalBox, constraints);
			basePanel.add(gapRemovalBox);

			constraints.gridx = 0;
			constraints.weightx = 1.0;
			constraints.gridwidth = 2;
			constraints.gridy++;

			// The text area to input legend.
			legendArea = new JTextArea();
			constraints.insets = new Insets(topMargin, leftMarginLong, bottomMargin, rightMargin);
			layout.setConstraints(legendArea, constraints);

			legendAreaRightClickMenu = new TextRightClickMenu();

			legendAreaScrollPane = new JScrollPane(legendArea);
			legendAreaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			legendAreaScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			constraints.insets = new Insets(topMargin, leftMarginLong, bottomMargin, rightMargin);
			constraints.weighty = 20.0;
			layout.setConstraints(legendAreaScrollPane, constraints);
			basePanel.add(legendAreaScrollPane);

			constraints.weighty = 1.0;
			constraints.gridy++;

			// Insert an empty row.
			emptyLabel = new JLabel(" ");
			constraints.insets = new Insets(0, 0, 0, 0);
			layout.setConstraints(emptyLabel, constraints);
			basePanel.add(emptyLabel);

			constraints.gridy++;

			// The button to reflect settings (OK button).
			okButton = new JButton("Unconfigured");
			constraints.insets = new Insets(0, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(okButton, constraints);
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
			} else {
				this.setEnglishTexts();
			}

			// Set fonts to the components.
			this.setFonts();

			// Updates the values of text fields, by the values stored in the configuration.
			this.updateValuesByConfiguration();

			// Update the right-click menus.
			xLabelFieldRightClickMenu.configure(this.configuration);
			yLabelFieldRightClickMenu.configure(this.configuration);
			zLabelFieldRightClickMenu.configure(this.configuration);
			legendAreaRightClickMenu.configure(this.configuration);
		}

		/**
		 * Sets Japanese texts to the GUI components.
		 */
		private void setJapaneseTexts() {
			frame.setTitle("ラベルの設定");
			xLabelSectionLabel.setText("X軸ラベル:");
			yLabelSectionLabel.setText("Y軸ラベル:");
			zLabelSectionLabel.setText("Z軸ラベル:");
			legendSectionLabel.setText("凡例:");
			autoLegendGenerationBox.setText("自動生成");
			gapRemovalBox.setText("隙間を除去");
			okButton.setText("OK");
		}

		/**
		 * Sets English texts to the GUI components.
		 */
		private void setEnglishTexts() {
			frame.setTitle("Set Labels");
			xLabelSectionLabel.setText("X Axis Label:");
			yLabelSectionLabel.setText("Y Axis Label:");
			zLabelSectionLabel.setText("Z Axis Label:");
			legendSectionLabel.setText("Legend:");
			autoLegendGenerationBox.setText("Auto Generation");
			gapRemovalBox.setText("Remove Gaps");
			okButton.setText("OK");
		}

		/**
		 * Set fonts to the components.
		 */
		private void setFonts() {
			FontConfiguration fontConfig = configuration.getFontConfiguration();
			Font uiBoldFont = fontConfig.getUIBoldFont();
			Font uiPlainFont = fontConfig.getUIPlainFont();

			frame.setFont(uiBoldFont);
			xLabelSectionLabel.setFont(uiBoldFont);
			yLabelSectionLabel.setFont(uiBoldFont);
			zLabelSectionLabel.setFont(uiBoldFont);
			okButton.setFont(uiBoldFont);

			xLabelField.setFont(uiPlainFont);
			yLabelField.setFont(uiPlainFont);
			zLabelTextField.setFont(uiPlainFont);

			legendSectionLabel.setFont(uiBoldFont);
			autoLegendGenerationBox.setFont(uiBoldFont);
			gapRemovalBox.setFont(uiBoldFont);
			legendArea.setFont(uiPlainFont);
		}

		/**
		 * Updates the values of text fields, by the values stored in the configuration.
		 */
		private void updateValuesByConfiguration() {

			// Update axis labels:
			LabelConfiguration labelConfig = this.configuration.getLabelConfiguration();
			String xLabel = labelConfig.getXLabelConfiguration().getLabelText();
			String yLabel = labelConfig.getYLabelConfiguration().getLabelText();
			String zLabel = labelConfig.getZLabelConfiguration().getLabelText();
			xLabelField.setText(xLabel);
			yLabelField.setText(yLabel);
			zLabelTextField.setText(zLabel);

			// Update legends:
			updateLegendSection(labelConfig.getLegendLabelConfiguration());
		}
	}


	/**
	 * Updates the legend section of UI from the configuration.
	 *
	 * @param legendLabelConfig The legend label configuration.
	 */
	public void updateLegendSection(LegendLabelConfiguration legendLabelConfig) {
		if (!SwingUtilities.isEventDispatchThread()) {
			throw new IllegalStateException("This method is only invokable from the event dispatch thread.");
		}
		boolean isAutoLegendGenerationEnabled = legendLabelConfig.isAutoLegendGenerationEnabled();
		autoLegendGenerationBox.setSelected(isAutoLegendGenerationEnabled);
		if (isAutoLegendGenerationEnabled) {
			legendArea.setEditable(false);
			legendArea.setBackground(Color.LIGHT_GRAY);
			legendArea.setForeground(Color.GRAY);

			String[] legends = legendLabelConfig.getLabelTexts();
			StringBuilder legendAreaContentBuilder = new StringBuilder();
			for (String legend: legends) {
				legendAreaContentBuilder.append(legend);
				legendAreaContentBuilder.append("\n");
			}
			legendArea.setText(legendAreaContentBuilder.toString());

		} else {
			legendArea.setEditable(true);
			legendArea.setBackground(Color.WHITE);
			legendArea.setForeground(Color.BLACK);
		}

		boolean isGapRemovalEnabled = legendLabelConfig.isEmptyLegendExclusionEnabled();
		gapRemovalBox.setSelected(isGapRemovalEnabled);
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
