package com.rinearn.graph3d.view;

import com.rinearn.graph3d.config.FontConfiguration;
import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import java.lang.reflect.InvocationTargetException;


/**
 * The window of "Set Labels" menu.
 */
public final class LabelSettingWindow {

	/** The default width [px] of the main window. */
	public static final int DEFAULT_WINDOW_WIDTH = 360;

	/** The default height [px] of the main window. */
	public static final int DEFAULT_WINDOW_HEIGHT = 430;

	/** The frame of this window. */
	public volatile JFrame frame;

	/** The title label of the text field for inputting the X label's text. */
	public volatile JLabel xLabelTextLabel;

	/** The text field for inputting the X label's text. */
	public volatile JTextField xLabelTextField;

	/** The title label of the text field for inputting the Y label's text. */
	public volatile JLabel yLabelTextLabel;

	/** The text field for inputting the Y label's text. */
	public volatile JTextField yLabelTextField;

	/** The title label of the text field for inputting the Z label's text. */
	public volatile JLabel zLabelTextLabel;

	/** The text field for inputting the Z label's text. */
	public volatile JTextField zLabelTextField;

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
			int marginBetweenLabelAndField = 10;

			// Create the title label of the text field for inputting the X label's text.
			xLabelTextLabel = new JLabel("Unconfigured");
			xLabelTextLabel.setVerticalAlignment(JLabel.BOTTOM);
			constraints.insets = new Insets(topMargin, leftMargin, marginBetweenLabelAndField, rightMargin);
			layour.setConstraints(xLabelTextLabel, constraints);
			basePanel.add(xLabelTextLabel);

			constraints.gridy++;

			// Create the text field for inputting the X label's text.
			xLabelTextField = new JTextField("Unconfigured");
			constraints.insets = new Insets(0, leftMargin, 0, rightMargin);
			layour.setConstraints(xLabelTextField, constraints);
			basePanel.add(xLabelTextField);

			constraints.gridy++;

			// Create the title label of the text field for inputting the Y label's text.
			yLabelTextLabel = new JLabel("Unconfigured");
			yLabelTextLabel.setVerticalAlignment(JLabel.BOTTOM);
			constraints.insets = new Insets(topMargin, leftMargin, marginBetweenLabelAndField, rightMargin);
			layour.setConstraints(yLabelTextLabel, constraints);
			basePanel.add(yLabelTextLabel);

			constraints.gridy++;

			// Create the text field for inputting the Y label's text.
			yLabelTextField = new JTextField("Unconfigured");
			constraints.insets = new Insets(0, leftMargin, 0, rightMargin);
			layour.setConstraints(yLabelTextField, constraints);
			basePanel.add(yLabelTextField);

			constraints.gridy++;

			// Create the title label of the text field for inputting the Z label's text.
			zLabelTextLabel = new JLabel("Unconfigured");
			zLabelTextLabel.setVerticalAlignment(JLabel.BOTTOM);
			constraints.insets = new Insets(topMargin, leftMargin, marginBetweenLabelAndField, rightMargin);
			layour.setConstraints(zLabelTextLabel, constraints);
			basePanel.add(zLabelTextLabel);

			constraints.gridy++;

			// Create the text field for inputting the Y label's text.
			zLabelTextField = new JTextField("Unconfigured");
			constraints.insets = new Insets(0, leftMargin, 0, rightMargin);
			layour.setConstraints(zLabelTextField, constraints);
			basePanel.add(zLabelTextField);

			constraints.gridy++;

			// Insert an empty row.
			JLabel emptyLabel = new JLabel(" ");
			constraints.insets = new Insets(0, 0, 0, 0);
			layour.setConstraints(emptyLabel, constraints);
			basePanel.add(emptyLabel);

			constraints.gridy++;

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
			frame.setTitle("ラベルの設定");
			xLabelTextLabel.setText("X軸ラベル:");
			yLabelTextLabel.setText("Y軸ラベル:");
			zLabelTextLabel.setText("Z軸ラベル:");
			okButton.setText("OK");
		}

		/**
		 * Sets English texts to the GUI components.
		 */
		private void setEnglishTexts() {
			frame.setTitle("Set Labels");
			xLabelTextLabel.setText("X Axis Label:");
			yLabelTextLabel.setText("Y Axis Label:");
			zLabelTextLabel.setText("Z Axis Label:");
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
			xLabelTextLabel.setFont(uiBoldFont);
			yLabelTextLabel.setFont(uiBoldFont);
			zLabelTextLabel.setFont(uiBoldFont);
			okButton.setFont(uiBoldFont);

			xLabelTextField.setFont(uiPlainFont);
			yLabelTextField.setFont(uiPlainFont);
			zLabelTextField.setFont(uiPlainFont);
		}

		/**
		 * Updates the values of text fields, by the values stored in the configuration.
		 */
		private void updateValuesByConfiguration() {
			String xLabel = this.configuration.getLabelConfiguration().getXLabelConfiguration().getText();
			String yLabel = this.configuration.getLabelConfiguration().getYLabelConfiguration().getText();
			String zLabel = this.configuration.getLabelConfiguration().getZLabelConfiguration().getText();
			xLabelTextField.setText(xLabel);
			yLabelTextField.setText(yLabel);
			zLabelTextField.setText(zLabel);
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
