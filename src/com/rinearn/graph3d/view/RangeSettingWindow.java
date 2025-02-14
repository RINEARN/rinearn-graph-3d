package com.rinearn.graph3d.view;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.font.FontConfiguration;
import com.rinearn.graph3d.config.range.RangeConfiguration;
import com.rinearn.graph3d.config.range.AxisRangeConfiguration;

import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JCheckBox;

import java.lang.reflect.InvocationTargetException;


/**
 * The window of "Set Ranges" menu.
 */
public class RangeSettingWindow {

	/** The default width [px] of this window. */
	public static final int DEFAULT_WINDOW_WIDTH = 400;

	/** The default height [px] of this window. */
	public static final int DEFAULT_WINDOW_HEIGHT = 620;

	/** The frame of this window. */
	public volatile JFrame frame;


	/** The title label of the section of X axis. */
	public volatile JLabel xAxisLabel;

	/** The title label of the text field for inputting the maximum value of X range. */
	public volatile JLabel xMaxLabel;

	/** The text field for inputting the maximum value of X range. */
	public volatile JTextField xMaxField;

	/** The right-click menu of xMaxField. */
	public volatile TextRightClickMenu xMaxFieldRightClickMenu;

	/** The title label of the text field for inputting the minimum value of X range. */
	public volatile JLabel xMinLabel;

	/** The text field for inputting the minimum value of X range. */
	public volatile JTextField xMinField;

	/** The right-click menu of xMinField. */
	public volatile TextRightClickMenu xMinFieldRightClickMenu;

	/** The check box for turning on/off the auto range feature of X range. */
	public volatile JCheckBox xAutoRangeBox;


	/** The title label of the section of Y axis. */
	public volatile JLabel yAxisLabel;

	/** The title label of the text field for inputting the maximum value of Y range. */
	public volatile JLabel yMaxLabel;

	/** The text field for inputting the maximum value of Y range. */
	public volatile JTextField yMaxField;

	/** The right-click menu of yMaxField. */
	public volatile TextRightClickMenu yMaxFieldRightClickMenu;

	/** The title label of the text field for inputting the minimum value of Y range. */
	public volatile JLabel yMinLabel;

	/** The text field for inputting the minimum value of Y range. */
	public volatile JTextField yMinField;

	/** The right-click menu of yMinField. */
	public volatile TextRightClickMenu yMinFieldRightClickMenu;

	/** The check box for turning on/off the auto range feature of Y range. */
	public volatile JCheckBox yAutoRangeBox;


	/** The title label of the section of Z axis. */
	public volatile JLabel zAxisLabel;

	/** The title label of the text field for inputting the maximum value of Z range. */
	public volatile JLabel zMaxLabel;

	/** The text field for inputting the maximum value of Z range. */
	public volatile JTextField zMaxField;

	/** The right-click menu of zMaxField. */
	public volatile TextRightClickMenu zMaxFieldRightClickMenu;

	/** The title label of the text field for inputting the minimum value of Z range. */
	public volatile JLabel zMinLabel;

	/** The text field for inputting the minimum value of Z range. */
	public volatile JTextField zMinField;

	/** The right-click menu of zMinField. */
	public volatile TextRightClickMenu zMinFieldRightClickMenu;

	/** The check box for turning on/off the auto range feature of Z range. */
	public volatile JCheckBox zAutoRangeBox;


	/** The button to reflect settings. */
	public volatile JButton okButton;


	/**
	 * Creates a new window.
	 *
	 * @param configuration The configuration of this application.
	 */
	public RangeSettingWindow() {

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
			constraints.gridwidth = 2;
			constraints.gridheight = 1;
			constraints.weightx = 1.0;
			constraints.weighty = 1.0;
			constraints.fill = GridBagConstraints.BOTH;

			// Define margines.
			int topMargin = 5;
			int bottomMargin = 5;
			int leftMargin = 5;
			int rightMargin = 5;
			int bottomMarginUnderAxisTitile = 5;
			int bottomMarginUnderSection = 25;
			int bottomMarginInSection = 5;
			int leftMarginInSection = 20;
			int marginBetweenLabelAndField = 10;

			// Components for setting X range.
			{
				// Create the title label of the section of X axis.
				xAxisLabel = new JLabel("Unconfigured");
				constraints.gridx = 0;
				xAxisLabel.setVerticalAlignment(JLabel.BOTTOM);
				constraints.insets = new Insets(topMargin, leftMargin, bottomMarginUnderAxisTitile, rightMargin);
				layout.setConstraints(xAxisLabel, constraints);
				basePanel.add(xAxisLabel);

				constraints.gridy++;
				constraints.gridwidth = 2;

				// Create the check box for turning on/off the auto-range feature of X axis.
				xAutoRangeBox = new JCheckBox("Unconfigured");
				constraints.gridx = 0;
				constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMarginInSection, rightMargin);
				layout.setConstraints(xAutoRangeBox, constraints);
				basePanel.add(xAutoRangeBox);

				constraints.gridy++;
				constraints.gridwidth = 1;

				// Create the title label of the text field for inputting the X-max value.
				xMaxLabel = new JLabel("Unconfigured");
				xMaxLabel.setHorizontalAlignment(JLabel.RIGHT);
				constraints.weightx = 0.05;
				constraints.gridx = 0;
				constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMarginInSection, marginBetweenLabelAndField);
				layout.setConstraints(xMaxLabel, constraints);
				basePanel.add(xMaxLabel);

				// Create the title label of the text field for inputting the X-max value.
				xMaxField = new JTextField("Unconfigured");
				constraints.weightx = 1.0;
				constraints.gridx = 1;
				constraints.insets = new Insets(topMargin, 0, bottomMarginInSection, rightMargin);
				layout.setConstraints(xMaxField, constraints);
				basePanel.add(xMaxField);

				// Create the right-click menu for the above text field.
				xMaxFieldRightClickMenu = new TextRightClickMenu();

				constraints.gridy++;
				constraints.gridwidth = 1;

				// Create the title label of the text field for inputting the X-min value.
				xMinLabel = new JLabel("Unconfigured");
				xMinLabel.setHorizontalAlignment(JLabel.RIGHT);
				constraints.weightx = 0.05;
				constraints.gridx = 0;
				constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMarginUnderSection, marginBetweenLabelAndField);
				layout.setConstraints(xMinLabel, constraints);
				basePanel.add(xMinLabel);

				// Create the title label of the text field for inputting the X-min value.
				xMinField = new JTextField("Unconfigured");
				constraints.weightx = 1.0;
				constraints.gridx = 1;
				constraints.insets = new Insets(topMargin, 0, bottomMarginUnderSection, rightMargin);
				layout.setConstraints(xMinField, constraints);
				basePanel.add(xMinField);

				// Create the right-click menu for the above text field.
				xMinFieldRightClickMenu = new TextRightClickMenu();

				constraints.gridy++;
				constraints.gridwidth = 2;
			}

			// Components for setting Y range.
			{
				// Create the title label of the section of Y axis.
				yAxisLabel = new JLabel("Unconfigured");
				constraints.gridx = 0;
				yAxisLabel.setVerticalAlignment(JLabel.BOTTOM);
				constraints.insets = new Insets(topMargin, leftMargin, bottomMarginUnderAxisTitile, rightMargin);
				layout.setConstraints(yAxisLabel, constraints);
				basePanel.add(yAxisLabel);

				constraints.gridy++;
				constraints.gridwidth = 2;

				// Create the check box for turning on/off the auto-range feature of Y axis.
				yAutoRangeBox = new JCheckBox("Unconfigured");
				constraints.gridx = 0;
				constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMarginInSection, rightMargin);
				layout.setConstraints(yAutoRangeBox, constraints);
				basePanel.add(yAutoRangeBox);

				constraints.gridy++;
				constraints.gridwidth = 1;

				// Create the title label of the text field for inputting the Y-max value.
				yMaxLabel = new JLabel("Unconfigured");
				yMaxLabel.setHorizontalAlignment(JLabel.RIGHT);
				constraints.weightx = 0.05;
				constraints.gridx = 0;
				constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMarginInSection, marginBetweenLabelAndField);
				layout.setConstraints(yMaxLabel, constraints);
				basePanel.add(yMaxLabel);

				// Create the title label of the text field for inputting the Y-max value.
				yMaxField = new JTextField("Unconfigured");
				constraints.weightx = 1.0;
				constraints.gridx = 1;
				constraints.insets = new Insets(topMargin, 0, bottomMarginInSection, rightMargin);
				layout.setConstraints(yMaxField, constraints);
				basePanel.add(yMaxField);

				// Create the right-click menu for the above text field.
				yMaxFieldRightClickMenu = new TextRightClickMenu();

				constraints.gridy++;
				constraints.gridwidth = 1;

				// Create the title label of the text field for inputting the Y-min value.
				yMinLabel = new JLabel("Unconfigured");
				yMinLabel.setHorizontalAlignment(JLabel.RIGHT);
				constraints.weightx = 0.05;
				constraints.gridx = 0;
				constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMarginUnderSection, marginBetweenLabelAndField);
				layout.setConstraints(yMinLabel, constraints);
				basePanel.add(yMinLabel);

				// Create the title label of the text field for inputting the Y-min value.
				yMinField = new JTextField("Unconfigured");
				constraints.weightx = 1.0;
				constraints.gridx = 1;
				constraints.insets = new Insets(topMargin, 0, bottomMarginUnderSection, rightMargin);
				layout.setConstraints(yMinField, constraints);
				basePanel.add(yMinField);

				// Create the right-click menu for the above text field.
				yMinFieldRightClickMenu = new TextRightClickMenu();

				constraints.gridy++;
				constraints.gridwidth = 2;
			}

			// Components for setting Z range.
			{
				// Create the title label of the section of Z axis.
				zAxisLabel = new JLabel("Unconfigured");
				constraints.gridx = 0;
				zAxisLabel.setVerticalAlignment(JLabel.BOTTOM);
				constraints.insets = new Insets(topMargin, leftMargin, bottomMarginUnderAxisTitile, rightMargin);
				layout.setConstraints(zAxisLabel, constraints);
				basePanel.add(zAxisLabel);

				constraints.gridy++;
				constraints.gridwidth = 2;

				// Create the check box for turning on/off the auto-range feature of Z axis.
				zAutoRangeBox = new JCheckBox("Unconfigured");
				constraints.gridx = 0;
				constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMarginInSection, rightMargin);
				layout.setConstraints(zAutoRangeBox, constraints);
				basePanel.add(zAutoRangeBox);

				constraints.gridy++;
				constraints.gridwidth = 1;

				// Create the title label of the text field for inputting the Z-max value.
				zMaxLabel = new JLabel("Unconfigured");
				zMaxLabel.setHorizontalAlignment(JLabel.RIGHT);
				constraints.weightx = 0.05;
				constraints.gridx = 0;
				constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMarginInSection, marginBetweenLabelAndField);
				layout.setConstraints(zMaxLabel, constraints);
				basePanel.add(zMaxLabel);

				// Create the right-click menu for the above text field.
				zMaxFieldRightClickMenu = new TextRightClickMenu();

				// Create the title label of the text field for inputting the Z-max value.
				zMaxField = new JTextField("Unconfigured");
				constraints.weightx = 1.0;
				constraints.gridx = 1;
				constraints.insets = new Insets(topMargin, 0, bottomMarginInSection, rightMargin);
				layout.setConstraints(zMaxField, constraints);
				basePanel.add(zMaxField);

				constraints.gridy++;
				constraints.gridwidth = 1;

				// Create the title label of the text field for inputting the Z-min value.
				zMinLabel = new JLabel("Unconfigured");
				zMinLabel.setHorizontalAlignment(JLabel.RIGHT);
				constraints.weightx = 0.05;
				constraints.gridx = 0;
				constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMarginUnderSection, marginBetweenLabelAndField);
				layout.setConstraints(zMinLabel, constraints);
				basePanel.add(zMinLabel);

				// Create the title label of the text field for inputting the Z-min value.
				zMinField = new JTextField("Unconfigured");
				constraints.weightx = 1.0;
				constraints.gridx = 1;
				constraints.insets = new Insets(topMargin, 0, bottomMarginUnderSection, rightMargin);
				layout.setConstraints(zMinField, constraints);
				basePanel.add(zMinField);

				// Create the right-click menu for the above text field.
				zMinFieldRightClickMenu = new TextRightClickMenu();

				constraints.gridy++;
				constraints.gridwidth = 2;
			}

			constraints.gridy++;
			constraints.gridx = 0;

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
			xMaxFieldRightClickMenu.configure(this.configuration);
			xMinFieldRightClickMenu.configure(this.configuration);
			yMaxFieldRightClickMenu.configure(this.configuration);
			yMinFieldRightClickMenu.configure(this.configuration);
			zMaxFieldRightClickMenu.configure(this.configuration);
			zMinFieldRightClickMenu.configure(this.configuration);
		}

		/**
		 * Sets Japanese texts to the GUI components.
		 */
		private void setJapaneseTexts() {
			frame.setTitle("範囲の設定");
			xAxisLabel.setText("- X軸 -");
			yAxisLabel.setText("- Y軸 -");
			zAxisLabel.setText("- Z軸 -");
			xAutoRangeBox.setText("データ読み込み時に自動調整");
			yAutoRangeBox.setText("データ読み込み時に自動調整");
			zAutoRangeBox.setText("データ読み込み時に自動調整");
			xMaxLabel.setText("最大:");
			xMinLabel.setText("最小:");
			yMaxLabel.setText("最大:");
			yMinLabel.setText("最小:");
			zMaxLabel.setText("最大:");
			zMinLabel.setText("最小:");
			okButton.setText("OK");
		}

		/**
		 * Sets English texts to the GUI components.
		 */
		private void setEnglishTexts() {
			frame.setTitle("Set Ranges");
			xAxisLabel.setText("- X Axis -");
			yAxisLabel.setText("- Y Axis -");
			zAxisLabel.setText("- Z Axis -");
			xAutoRangeBox.setText("Auto-Set When Loading Data");
			yAutoRangeBox.setText("Auto-Set When Loading Data");
			zAutoRangeBox.setText("Auto-Set When Loading Data");
			xMaxLabel.setText("Max:");
			xMinLabel.setText("Min:");
			yMaxLabel.setText("Max:");
			yMinLabel.setText("Min:");
			zMaxLabel.setText("Max:");
			zMinLabel.setText("Min:");
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

			xAxisLabel.setFont(uiBoldFont);
			yAxisLabel.setFont(uiBoldFont);
			zAxisLabel.setFont(uiBoldFont);

			xAutoRangeBox.setFont(uiBoldFont);
			yAutoRangeBox.setFont(uiBoldFont);
			zAutoRangeBox.setFont(uiBoldFont);

			xMaxLabel.setFont(uiBoldFont);
			xMinLabel.setFont(uiBoldFont);
			yMaxLabel.setFont(uiBoldFont);
			yMinLabel.setFont(uiBoldFont);
			zMaxLabel.setFont(uiBoldFont);
			zMinLabel.setFont(uiBoldFont);

			xMaxField.setFont(uiPlainFont);
			xMinField.setFont(uiPlainFont);
			yMaxField.setFont(uiPlainFont);
			yMinField.setFont(uiPlainFont);
			zMaxField.setFont(uiPlainFont);
			zMinField.setFont(uiPlainFont);

			okButton.setFont(uiBoldFont);
		}

		/**
		 * Updates the values of text fields, by the values stored in the configuration.
		 */
		private void updateValuesByConfiguration() {
			RangeConfiguration rangeConfig = this.configuration.getRangeConfiguration();
			AxisRangeConfiguration xRangeConfig = rangeConfig.getXRangeConfiguration();
			AxisRangeConfiguration yRangeConfig = rangeConfig.getYRangeConfiguration();
			AxisRangeConfiguration zRangeConfig = rangeConfig.getZRangeConfiguration();

			xMaxField.setText(xRangeConfig.getMaximum().toString());
			xMinField.setText(xRangeConfig.getMinimum().toString());
			yMaxField.setText(yRangeConfig.getMaximum().toString());
			yMinField.setText(yRangeConfig.getMinimum().toString());
			zMaxField.setText(zRangeConfig.getMaximum().toString());
			zMinField.setText(zRangeConfig.getMinimum().toString());

			xAutoRangeBox.setSelected(xRangeConfig.isAutoRangeEnabled());
			yAutoRangeBox.setSelected(yRangeConfig.isAutoRangeEnabled());
			zAutoRangeBox.setSelected(zRangeConfig.isAutoRangeEnabled());
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
