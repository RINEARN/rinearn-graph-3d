package com.rinearn.graph3d.view;

import com.rinearn.graph3d.config.FontConfiguration;
import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;

import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import javax.swing.JComboBox;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.awt.Font;
import java.awt.Container;
import java.awt.Insets;

import java.lang.reflect.InvocationTargetException;


/**
 * The window of "File" > "Save Image" menu.
 */
public class ImageSavingWindow {

	/** The default width [px] of this window. */
	public static final int DEFAULT_WINDOW_WIDTH = 460;

	/** The default height [px] of this window. */
	public static final int DEFAULT_WINDOW_HEIGHT = 280;

	/** The item of fileFormatbox: "PNG". */
	public static final String FILE_FORMAT_PNG = "PNG";

	/** The item of fileFormatbox: "JPEG". */
	public static final String FILE_FORMAT_JPEG = "JPEG";

	/** The item of fileFormatbox: "BMP". */
	public static final String FILE_FORMAT_BMP = "BMP";

	/** The default value of the text filed to input the file name. */
	private static final String DEFAULT_FILE_NAME = "graph3d";

	/** The default value of the text filed to input the file location (folder). */
	private static final String DEFAULT_FILE_LOCATION = "./";

	/** The default value of the text filed to input the image quality. */
	private static final String DEFAULT_QUALITY = "100";


	/** The frame of this window. */
	public volatile JFrame frame;

	/** The label of the text field to input the file name. */
	public JLabel fileNameLabel;

	/** The text field to input the file name. */
	public JTextField fileNameField;

	/** The label of the text field to input the file location (folder). */
	public JLabel fileLocationLabel;

	/** The text field to input the file location (folder). */
	public JTextField fileLocationField;

	/** The button field to set the file location (folder). */
	public JButton fileLocationButton;

	/** The button to save the image file. */
	public JButton saveButton;

	/** The label of the check box to select the format of the image file. */
	public JLabel fileFormatLabel;

	/** The check box to select the format of the image file. */
	public JComboBox<String> fileFormatBox;

	/** The label of the text field to input the image quality. */
	public JLabel qualityLabel;

	/** The text field to input the image quality. */
	public JTextField qualityField;

	/** The label of "%" at the right of the text field to input the image quality.*/
	public JLabel percentLabel;


	/**
	 * Creates a new window.
	 *
	 * @param configuration The configuration of this application.
	 */
	public ImageSavingWindow() {

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
			int rightMargin = 5;
			int topMarginLong = 15;
			int rightMarginShort = 2;
			int leftMarginShort = 2;

			constraints.gridwidth = 1;
			constraints.weightx = 0.0;

			fileNameLabel = new JLabel("Unconfigured", JLabel.RIGHT);
			constraints.insets = new Insets(topMargin, leftMargin, bottomMargin, rightMarginShort);
			layout.setConstraints(fileNameLabel, constraints);
			basePanel.add(fileNameLabel);

			constraints.gridwidth = 2;
			constraints.weightx = 1.0;
			constraints.gridx++;

			fileNameField = new JTextField(DEFAULT_FILE_NAME);
			constraints.insets = new Insets(topMargin, leftMarginShort, bottomMargin, rightMargin);
			layout.setConstraints(fileNameField, constraints);
			basePanel.add(fileNameField);

			constraints.gridwidth = 1;
			constraints.weightx = 0.0;
			constraints.gridy++;
			constraints.gridx = 0;

			fileLocationLabel = new JLabel("Unconfigured", JLabel.RIGHT);
			constraints.insets = new Insets(topMargin, leftMargin, bottomMargin, rightMarginShort);
			layout.setConstraints(fileLocationLabel, constraints);
			basePanel.add(fileLocationLabel);

			constraints.gridwidth = 1;
			constraints.weightx = 1.0;
			constraints.gridx++;

			fileLocationField = new JTextField(DEFAULT_FILE_LOCATION);
			constraints.insets = new Insets(topMargin, leftMarginShort, bottomMargin, rightMarginShort);
			layout.setConstraints(fileLocationField, constraints);
			basePanel.add(fileLocationField);

			constraints.gridwidth = 1;
			constraints.weightx = 0.0;
			constraints.gridx++;

			fileLocationButton = new JButton("Unconfigured");
			constraints.insets = new Insets(topMargin, leftMarginShort, bottomMargin, rightMargin);
			layout.setConstraints(fileLocationButton, constraints);
			basePanel.add(fileLocationButton);

			constraints.gridwidth = 1;
			constraints.weightx = 0.0;
			constraints.gridy++;
			constraints.gridx = 0;

			fileFormatLabel = new JLabel("Unconfigured", JLabel.RIGHT);
			constraints.insets = new Insets(topMargin, leftMargin, bottomMargin, rightMarginShort);
			layout.setConstraints(fileFormatLabel, constraints);
			basePanel.add(fileFormatLabel);

			constraints.gridwidth = 2;
			constraints.weightx = 1.0;
			constraints.gridx++;

			fileFormatBox = new JComboBox<String>();
			constraints.insets = new Insets(topMargin, leftMarginShort, bottomMargin, rightMargin);
			layout.setConstraints(fileFormatBox, constraints);
			basePanel.add(fileFormatBox);
			{
				fileFormatBox.addItem(FILE_FORMAT_PNG);
				fileFormatBox.addItem(FILE_FORMAT_JPEG);
				fileFormatBox.addItem(FILE_FORMAT_BMP);
			}

			constraints.gridwidth = 1;
			constraints.weightx = 0.0;
			constraints.gridy++;
			constraints.gridx = 0;

			qualityLabel = new JLabel("Unconfigured", JLabel.RIGHT);
			constraints.insets = new Insets(topMargin, leftMargin, bottomMargin, rightMarginShort);
			layout.setConstraints(qualityLabel, constraints);
			basePanel.add(qualityLabel);

			constraints.gridwidth = 1;
			constraints.weightx = 1.0;
			constraints.gridx++;

			qualityField = new JTextField(DEFAULT_QUALITY);
			constraints.insets = new Insets(topMargin, leftMarginShort, bottomMargin, rightMarginShort);
			layout.setConstraints(qualityField, constraints);
			basePanel.add(qualityField);

			constraints.gridwidth = 1;
			constraints.weightx = 0.0;
			constraints.gridx++;

			percentLabel = new JLabel(" %");
			constraints.insets = new Insets(topMargin, leftMarginShort, bottomMargin, rightMargin);
			layout.setConstraints(percentLabel, constraints);
			basePanel.add(percentLabel);

			constraints.gridwidth = 3;
			constraints.weightx = 1.0;
			constraints.weighty = 1.25;
			constraints.gridy++;
			constraints.gridx = 0;

			saveButton = new JButton("Unconfigured");
			constraints.insets = new Insets(topMarginLong, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(saveButton, constraints);
			basePanel.add(saveButton);
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
			frame.setTitle("画像の保存");

			fileNameLabel.setText("ファイル名: ");
			fileLocationLabel.setText("保存場所: ");
			fileLocationButton.setText("開く");
			fileFormatLabel.setText("画像形式: ");
			qualityLabel.setText("画質: ");
			saveButton.setText("保存");
		}

		/**
		 * Sets English texts to the GUI components.
		 */
		private void setEnglishTexts() {
			frame.setTitle("Save Image");

			fileNameLabel.setText("File Name: ");
			fileLocationLabel.setText("Location: ");
			fileLocationButton.setText("Open");
			fileFormatLabel.setText("Format: ");
			qualityLabel.setText("Quality: ");
			saveButton.setText("SAVE");
		}

		/**
		 * Set fonts to the components.
		 */
		private void setFonts() {
			FontConfiguration fontConfig = configuration.getFontConfiguration();
			Font uiBoldFont = fontConfig.getUIBoldFont();
			Font uiPlainFont = fontConfig.getUIPlainFont();

			frame.setFont(uiBoldFont);
			fileNameLabel.setFont(uiBoldFont);
			fileNameField.setFont(uiPlainFont);
			fileLocationLabel.setFont(uiBoldFont);
			fileLocationField.setFont(uiPlainFont);
			fileLocationButton.setFont(uiBoldFont);
			fileFormatLabel.setFont(uiBoldFont);
			fileFormatBox.setFont(uiPlainFont);
			qualityLabel.setFont(uiBoldFont);
			qualityField.setFont(uiPlainFont);
			percentLabel.setFont(uiBoldFont);
			saveButton.setFont(uiBoldFont);
		}

		/**
		 * Updates the values of text fields, by the values stored in the configuration.
		 */
		private void updateValuesByConfiguration() {
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
