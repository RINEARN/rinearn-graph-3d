package com.rinearn.graph3d.view;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.RangeConfiguration;
import com.rinearn.graph3d.config.FontConfiguration;

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

	/** The default width [px] of the main window. */
	public static final int DEFAULT_WINDOW_WIDTH = 400;

	/** The default height [px] of the main window. */
	public static final int DEFAULT_WINDOW_HEIGHT = 620;

	/** The frame of this window. */
	public volatile JFrame frame;


	/** The title label of the section of X axis. */
	public volatile JLabel xAxisLabel;

	/** The title label of the text field for inputting the maximum value of X range. */
	public volatile JLabel xMaxLabel;

	/** The text field for inputting the maximum value of X range. */
	public volatile JTextField xMaxField;

	/** The title label of the text field for inputting the minimum value of X range. */
	public volatile JLabel xMinLabel;

	/** The text field for inputting the minimum value of X range. */
	public volatile JTextField xMinField;

	/** The check box for turning on/off the auto ranging feature of X range. */
	public volatile JCheckBox xAutoRangingBox;


	/** The title label of the section of Y axis. */
	public volatile JLabel yAxisLabel;

	/** The title label of the text field for inputting the maximum value of Y range. */
	public volatile JLabel yMaxLabel;

	/** The text field for inputting the maximum value of Y range. */
	public volatile JTextField yMaxField;

	/** The title label of the text field for inputting the minimum value of Y range. */
	public volatile JLabel yMinLabel;

	/** The text field for inputting the minimum value of Y range. */
	public volatile JTextField yMinField;

	/** The check box for turning on/off the auto ranging feature of Y range. */
	public volatile JCheckBox yAutoRangingBox;


	/** The title label of the section of Z axis. */
	public volatile JLabel zAxisLabel;

	/** The title label of the text field for inputting the maximum value of Z range. */
	public volatile JLabel zMaxLabel;

	/** The text field for inputting the maximum value of Z range. */
	public volatile JTextField zMaxField;

	/** The title label of the text field for inputting the minimum value of Z range. */
	public volatile JLabel zMinLabel;

	/** The text field for inputting the minimum value of Z range. */
	public volatile JTextField zMinField;

	/** The check box for turning on/off the auto ranging feature of Z range. */
	public volatile JCheckBox zAutoRangingBox;


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
			GridBagLayout layour = new GridBagLayout();
			basePanel.setLayout(layour);
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
				layour.setConstraints(xAxisLabel, constraints);
				basePanel.add(xAxisLabel);

				constraints.gridy++;
				constraints.gridwidth = 2;

				// Create the check box for turning on/off the auto-ranging feature of X axis.
				xAutoRangingBox = new JCheckBox("Unconfigured");
				constraints.gridx = 0;
				constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMarginInSection, rightMargin);
				layour.setConstraints(xAutoRangingBox, constraints);
				basePanel.add(xAutoRangingBox);

				constraints.gridy++;
				constraints.gridwidth = 1;

				// Create the title label of the text field for inputting the X-max value.
				xMaxLabel = new JLabel("Unconfigured");
				xMaxLabel.setHorizontalAlignment(JLabel.RIGHT);
				constraints.weightx = 0.05;
				constraints.gridx = 0;
				constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMarginInSection, marginBetweenLabelAndField);
				layour.setConstraints(xMaxLabel, constraints);
				basePanel.add(xMaxLabel);

				// Create the title label of the text field for inputting the X-max value.
				xMaxField = new JTextField("Unconfigured");
				constraints.weightx = 1.0;
				constraints.gridx = 1;
				constraints.insets = new Insets(topMargin, 0, bottomMarginInSection, rightMargin);
				layour.setConstraints(xMaxField, constraints);
				basePanel.add(xMaxField);

				constraints.gridy++;
				constraints.gridwidth = 1;

				// Create the title label of the text field for inputting the X-min value.
				xMinLabel = new JLabel("Unconfigured");
				xMinLabel.setHorizontalAlignment(JLabel.RIGHT);
				constraints.weightx = 0.05;
				constraints.gridx = 0;
				constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMarginUnderSection, marginBetweenLabelAndField);
				layour.setConstraints(xMinLabel, constraints);
				basePanel.add(xMinLabel);

				// Create the title label of the text field for inputting the X-min value.
				xMinField = new JTextField("Unconfigured");
				constraints.weightx = 1.0;
				constraints.gridx = 1;
				constraints.insets = new Insets(topMargin, 0, bottomMarginUnderSection, rightMargin);
				layour.setConstraints(xMinField, constraints);
				basePanel.add(xMinField);

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
				layour.setConstraints(yAxisLabel, constraints);
				basePanel.add(yAxisLabel);

				constraints.gridy++;
				constraints.gridwidth = 2;

				// Create the check box for turning on/off the auto-ranging feature of Y axis.
				yAutoRangingBox = new JCheckBox("Unconfigured");
				constraints.gridx = 0;
				constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMarginInSection, rightMargin);
				layour.setConstraints(yAutoRangingBox, constraints);
				basePanel.add(yAutoRangingBox);

				constraints.gridy++;
				constraints.gridwidth = 1;

				// Create the title label of the text field for inputting the Y-max value.
				yMaxLabel = new JLabel("Unconfigured");
				yMaxLabel.setHorizontalAlignment(JLabel.RIGHT);
				constraints.weightx = 0.05;
				constraints.gridx = 0;
				constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMarginInSection, marginBetweenLabelAndField);
				layour.setConstraints(yMaxLabel, constraints);
				basePanel.add(yMaxLabel);

				// Create the title label of the text field for inputting the Y-max value.
				yMaxField = new JTextField("Unconfigured");
				constraints.weightx = 1.0;
				constraints.gridx = 1;
				constraints.insets = new Insets(topMargin, 0, bottomMarginInSection, rightMargin);
				layour.setConstraints(yMaxField, constraints);
				basePanel.add(yMaxField);

				constraints.gridy++;
				constraints.gridwidth = 1;

				// Create the title label of the text field for inputting the Y-min value.
				yMinLabel = new JLabel("Unconfigured");
				yMinLabel.setHorizontalAlignment(JLabel.RIGHT);
				constraints.weightx = 0.05;
				constraints.gridx = 0;
				constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMarginUnderSection, marginBetweenLabelAndField);
				layour.setConstraints(yMinLabel, constraints);
				basePanel.add(yMinLabel);

				// Create the title label of the text field for inputting the Y-min value.
				yMinField = new JTextField("Unconfigured");
				constraints.weightx = 1.0;
				constraints.gridx = 1;
				constraints.insets = new Insets(topMargin, 0, bottomMarginUnderSection, rightMargin);
				layour.setConstraints(yMinField, constraints);
				basePanel.add(yMinField);

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
				layour.setConstraints(zAxisLabel, constraints);
				basePanel.add(zAxisLabel);

				constraints.gridy++;
				constraints.gridwidth = 2;

				// Create the check box for turning on/off the auto-ranging feature of Z axis.
				zAutoRangingBox = new JCheckBox("Unconfigured");
				constraints.gridx = 0;
				constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMarginInSection, rightMargin);
				layour.setConstraints(zAutoRangingBox, constraints);
				basePanel.add(zAutoRangingBox);

				constraints.gridy++;
				constraints.gridwidth = 1;

				// Create the title label of the text field for inputting the Z-max value.
				zMaxLabel = new JLabel("Unconfigured");
				zMaxLabel.setHorizontalAlignment(JLabel.RIGHT);
				constraints.weightx = 0.05;
				constraints.gridx = 0;
				constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMarginInSection, marginBetweenLabelAndField);
				layour.setConstraints(zMaxLabel, constraints);
				basePanel.add(zMaxLabel);

				// Create the title label of the text field for inputting the Z-max value.
				zMaxField = new JTextField("Unconfigured");
				constraints.weightx = 1.0;
				constraints.gridx = 1;
				constraints.insets = new Insets(topMargin, 0, bottomMarginInSection, rightMargin);
				layour.setConstraints(zMaxField, constraints);
				basePanel.add(zMaxField);

				constraints.gridy++;
				constraints.gridwidth = 1;

				// Create the title label of the text field for inputting the Z-min value.
				zMinLabel = new JLabel("Unconfigured");
				zMinLabel.setHorizontalAlignment(JLabel.RIGHT);
				constraints.weightx = 0.05;
				constraints.gridx = 0;
				constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMarginUnderSection, marginBetweenLabelAndField);
				layour.setConstraints(zMinLabel, constraints);
				basePanel.add(zMinLabel);

				// Create the title label of the text field for inputting the Z-min value.
				zMinField = new JTextField("Unconfigured");
				constraints.weightx = 1.0;
				constraints.gridx = 1;
				constraints.insets = new Insets(topMargin, 0, bottomMarginUnderSection, rightMargin);
				layour.setConstraints(zMinField, constraints);
				basePanel.add(zMinField);

				constraints.gridy++;
				constraints.gridwidth = 2;
			}

			constraints.gridy++;
			constraints.gridx = 0;

			// The button to reflect settings (OK button).
			okButton = new JButton("Unconfigured");
			constraints.insets = new Insets(0, leftMargin, bottomMargin, rightMargin);
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
			} else {
				this.setEnglishTexts();
			}

			// Set fonts to the components.
			this.setFonts();

			// Updates the values of text fields, by the values stored in the configuration.
			this.updateValuesByConfiguration();
		}

		/**
		 * Sets Japanese texts to the GUI components.
		 */
		private void setJapaneseTexts() {
			frame.setTitle("範囲の設定");
			xAxisLabel.setText("- X軸 -");
			yAxisLabel.setText("- Y軸 -");
			zAxisLabel.setText("- Z軸 -");
			xAutoRangingBox.setText("データ読み込み時に自動調整");
			yAutoRangingBox.setText("データ読み込み時に自動調整");
			zAutoRangingBox.setText("データ読み込み時に自動調整");
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
			xAutoRangingBox.setText("Auto-Set When Loading Data");
			yAutoRangingBox.setText("Auto-Set When Loading Data");
			zAutoRangingBox.setText("Auto-Set When Loading Data");
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

			xAutoRangingBox.setFont(uiBoldFont);
			yAutoRangingBox.setFont(uiBoldFont);
			zAutoRangingBox.setFont(uiBoldFont);

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
			RangeConfiguration.AxisRangeConfiguration xRangeConfig = rangeConfig.getXRangeConfiguration();
			RangeConfiguration.AxisRangeConfiguration yRangeConfig = rangeConfig.getYRangeConfiguration();
			RangeConfiguration.AxisRangeConfiguration zRangeConfig = rangeConfig.getZRangeConfiguration();

			xMaxField.setText(xRangeConfig.getMaximum().toString());
			xMinField.setText(xRangeConfig.getMinimum().toString());
			yMaxField.setText(yRangeConfig.getMaximum().toString());
			yMinField.setText(yRangeConfig.getMinimum().toString());
			zMaxField.setText(zRangeConfig.getMaximum().toString());
			zMinField.setText(zRangeConfig.getMinimum().toString());

			xAutoRangingBox.setSelected(xRangeConfig.isAutoRangingEnabled());
			yAutoRangingBox.setSelected(yRangeConfig.isAutoRangingEnabled());
			zAutoRangingBox.setSelected(zRangeConfig.isAutoRangingEnabled());
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
}
