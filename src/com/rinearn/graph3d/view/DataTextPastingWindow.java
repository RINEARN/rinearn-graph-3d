package com.rinearn.graph3d.view;

import com.rinearn.graph3d.RinearnGraph3DDataFileFormat;
import com.rinearn.graph3d.config.FontConfiguration;
import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;

import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import javax.swing.JComboBox;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.awt.Font;
import java.awt.Container;
import java.awt.Insets;

import java.lang.reflect.InvocationTargetException;


/**
 * The window of "File" > "Paste Data Text" menu.
 */
public class DataTextPastingWindow {

	/** The default width [px] of this window. */
	public static final int DEFAULT_WINDOW_WIDTH = 560;

	/** The default height [px] of this window. */
	public static final int DEFAULT_WINDOW_HEIGHT = 500;


	/** The frame of this window. */
	public volatile JFrame frame;

	/** The label of the text area to paste a data text. */
	public volatile JLabel dataTextLabel;

	/** The text area to paste the data text. */
	public volatile JTextArea dataTextArea;

	/** The label of the combo box for selecting the data format. */
	public volatile JLabel dataFormatLabel;

	/** The combo box for selecting the data format. */
	public volatile JComboBox<MultilingualItem> dataFormatBox;

	/** The item of dataFormatBox: "AUTO" */
	public volatile MultilingualItem autoFormatItem;

	/** The item of dataFormatBox: "Copied from Spreadsheets" */
	public volatile MultilingualItem spreadsheetFormatItem;

	/** The item of dataFormatBox: "3-COLUMNS, CSV" */
	public volatile MultilingualItem threeColumnsCSVFormatItem;

	/** The item of dataFormatBox: "3-COLUMNS, STSV (Space/Tab Separated)" */
	public volatile MultilingualItem threeColumnsSTSVFormatItem;

	/** The item of dataFormatBox: "3-COLUMNS, TSV (Strict)" */
	public volatile MultilingualItem threeColumnsTSVStrictFormatItem;

	/** The item of dataFormatBox: "4-COLUMNS, CSV" */
	public volatile MultilingualItem fourColumnsCSVFormatItem;

	/** The item of dataFormatBox: "4-COLUMNS, STSV (Space/Tab Separated)" */
	public volatile MultilingualItem fourColumnsSTSVFormatItem;

	/** The item of dataFormatBox: "4-COLUMNS, TSV (Strict)" */
	public volatile MultilingualItem fourColumnsTSVStrictFormatItem;

	/** The item of dataFormatBox: "MATRIX, CSV" */
	public volatile MultilingualItem matrixCSVFormatItem;

	/** The item of dataFormatBox: "MATRIX, STSV (Space/Tab Separated)" */
	public volatile MultilingualItem matrixSTSVFormatItem;

	/** The item of dataFormatBox: "MATRIX, TSV (Strict)" */
	public volatile MultilingualItem matrixTSVStrictFormatItem;


	/** The button to plot the currently pasted data text. */
	public volatile JButton plotButton;

	/** The button to clear the currently pasted data text. */
	public volatile JButton clearButton;


	/**
	 * Creates a new window.
	 *
	 * @param configuration The configuration of this application.
	 */
	public DataTextPastingWindow() {

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
			// frame.setVisible(false);
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
			int bottomMargin = 5;
			int leftMargin = 5;
			int rightMargin = 5;
			int bottomMarginShort = 2;
			int topMarginShort = 2;
			int leftMarginLong = 20;

			constraints.gridwidth = 2;
			constraints.weighty = 0.03;

			// The label of the text area to paste a data text..
			dataTextLabel = new JLabel("Unconfigured");
			constraints.insets = new Insets(topMargin, leftMargin, bottomMarginShort, rightMargin);
			layout.setConstraints(dataTextLabel, constraints);
			basePanel.add(dataTextLabel);

			constraints.gridy++;
			constraints.weighty = 1.2;

			// The text area to paste a data text.
			dataTextArea = new JTextArea();
			constraints.insets = new Insets(topMarginShort, leftMarginLong, bottomMargin, rightMargin);
			JScrollPane fileListScrollPane = new JScrollPane(
					dataTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
			);
			layout.setConstraints(fileListScrollPane, constraints);
			basePanel.add(fileListScrollPane);

			constraints.gridy++;
			constraints.gridwidth = 1;
			constraints.weighty = 0.03;

			// The label of the combo box to selecting the data format.
			dataFormatLabel = new JLabel("Unconfigured", JLabel.RIGHT);
			constraints.insets = new Insets(topMargin, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(dataFormatLabel, constraints);
			basePanel.add(dataFormatLabel);

			constraints.gridx++;
			constraints.gridwidth = 1;

			// The combo box to selecting the data format.
			dataFormatBox = new JComboBox<MultilingualItem>();
			constraints.insets = new Insets(topMargin, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(dataFormatBox, constraints);
			basePanel.add(dataFormatBox);
			{
				autoFormatItem = new MultilingualItem();
				dataFormatBox.addItem(autoFormatItem);

				spreadsheetFormatItem = new MultilingualItem();
				dataFormatBox.addItem(spreadsheetFormatItem);

				threeColumnsCSVFormatItem = new MultilingualItem();
				dataFormatBox.addItem(threeColumnsCSVFormatItem);
				threeColumnsTSVStrictFormatItem = new MultilingualItem();
				dataFormatBox.addItem(threeColumnsTSVStrictFormatItem);
				threeColumnsSTSVFormatItem = new MultilingualItem();
				dataFormatBox.addItem(threeColumnsSTSVFormatItem);

				fourColumnsCSVFormatItem = new MultilingualItem();
				dataFormatBox.addItem(fourColumnsCSVFormatItem);
				fourColumnsTSVStrictFormatItem = new MultilingualItem();
				dataFormatBox.addItem(fourColumnsTSVStrictFormatItem);
				fourColumnsSTSVFormatItem = new MultilingualItem();
				dataFormatBox.addItem(fourColumnsSTSVFormatItem);

				matrixCSVFormatItem = new MultilingualItem();
				dataFormatBox.addItem(matrixCSVFormatItem);
				matrixTSVStrictFormatItem = new MultilingualItem();
				dataFormatBox.addItem(matrixTSVStrictFormatItem);
				matrixSTSVFormatItem = new MultilingualItem();
				dataFormatBox.addItem(matrixSTSVFormatItem);
			}

			constraints.gridx = 0;
			constraints.gridy++;
			constraints.gridwidth = 2;
			constraints.weighty = 0.14;

			// The button to plot the currently pasted data text.
			plotButton = new JButton();
			constraints.insets = new Insets(topMargin, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(plotButton, constraints);
			basePanel.add(plotButton);

			constraints.gridy++;

			// The button to clear the currently pasted data text.
			clearButton = new JButton();
			constraints.insets = new Insets(topMargin, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(clearButton, constraints);
			basePanel.add(clearButton);
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
			frame.setTitle("ファイルを開く");
			dataTextLabel.setText("データを貼り付けてください:");
			plotButton.setText("プロット");
			clearButton.setText("クリア");

			dataFormatLabel.setText("データ書式: ");
			{
				autoFormatItem.setText(FormatDisplayNames.AUTO_JA);
				spreadsheetFormatItem.setText(FormatDisplayNames.SPREADSHEET_JA);

				threeColumnsCSVFormatItem.setText(FormatDisplayNames.THREE_COLUMNS_CSV_JA);
				threeColumnsSTSVFormatItem.setText(FormatDisplayNames.THREE_COLUMNS_STSV_JA);
				threeColumnsTSVStrictFormatItem.setText(FormatDisplayNames.THREE_COLUMNS_TSV_JA);

				fourColumnsCSVFormatItem.setText(FormatDisplayNames.FOUR_COLUMNS_CSV_JA);
				fourColumnsSTSVFormatItem.setText(FormatDisplayNames.FOUR_COLUMNS_STSV_JA);
				fourColumnsTSVStrictFormatItem.setText(FormatDisplayNames.FOUR_COLUMNS_TSV_JA);

				matrixCSVFormatItem.setText(FormatDisplayNames.MATRIX_CSV_JA);
				matrixSTSVFormatItem.setText(FormatDisplayNames.MATRIX_STSV_JA);
				matrixTSVStrictFormatItem.setText(FormatDisplayNames.MATRIX_TSV_JA);
			}
		}

		/**
		 * Sets English texts to the GUI components.
		 */
		private void setEnglishTexts() {
			frame.setTitle("Open Files");
			dataTextLabel.setText("Paste the Data Text:");
			plotButton.setText("PLOT");
			clearButton.setText("CLEAR");

			dataFormatLabel.setText("Data Format: ");
			{
				autoFormatItem.setText(FormatDisplayNames.AUTO_EN);
				spreadsheetFormatItem.setText(FormatDisplayNames.SPREADSHEET_EN);

				threeColumnsCSVFormatItem.setText(FormatDisplayNames.THREE_COLUMNS_CSV_EN);
				threeColumnsSTSVFormatItem.setText(FormatDisplayNames.THREE_COLUMNS_STSV_EN);
				threeColumnsTSVStrictFormatItem.setText(FormatDisplayNames.THREE_COLUMNS_TSV_EN);

				fourColumnsCSVFormatItem.setText(FormatDisplayNames.FOUR_COLUMNS_CSV_EN);
				fourColumnsSTSVFormatItem.setText(FormatDisplayNames.FOUR_COLUMNS_STSV_EN);
				fourColumnsTSVStrictFormatItem.setText(FormatDisplayNames.FOUR_COLUMNS_TSV_EN);

				matrixCSVFormatItem.setText(FormatDisplayNames.MATRIX_CSV_EN);
				matrixSTSVFormatItem.setText(FormatDisplayNames.MATRIX_STSV_EN);
				matrixTSVStrictFormatItem.setText(FormatDisplayNames.MATRIX_TSV_EN);
			}
		}

		/**
		 * Set fonts to the components.
		 */
		private void setFonts() {
			FontConfiguration fontConfig = configuration.getFontConfiguration();
			Font uiBoldFont = fontConfig.getUIBoldFont();
			Font uiPlainFont = fontConfig.getUIPlainFont();

			frame.setFont(uiBoldFont);
			dataTextLabel.setFont(uiBoldFont);
			//dataTextArea.setFont(uiPlainFont);
			dataFormatLabel.setFont(uiBoldFont);
			dataFormatBox.setFont(uiPlainFont);
			plotButton.setFont(uiBoldFont);
			clearButton.setFont(uiBoldFont);
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


	/**
	 * Returns the selected data file format as an element of RinearnGraph3DDataFileFormat enum.
	 * This method can be invokable from only on the event-dispatch method.
	 *
	 * @return The selected data file format.
	 */
	public synchronized RinearnGraph3DDataFileFormat getSelectedDataFileFormat() {
		if (!SwingUtilities.isEventDispatchThread()) {
			throw new IllegalStateException("This method can be invokable from only on the event-dispatch method.");
		}
		String selectedItem = dataFormatBox.getSelectedItem().toString();
		RinearnGraph3DDataFileFormat enumElement = FormatDisplayNames.toRinearnGraph3DDataFileFormat(selectedItem);
		return enumElement;
	}
}
