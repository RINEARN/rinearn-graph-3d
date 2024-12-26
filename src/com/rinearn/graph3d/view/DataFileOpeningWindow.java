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
 * The window of "File" > "Open Files" menu.
 */
public class DataFileOpeningWindow {

	/** The default width [px] of this window. */
	public static final int DEFAULT_WINDOW_WIDTH = 560;

	/** The default height [px] of this window. */
	public static final int DEFAULT_WINDOW_HEIGHT = 500;

	/** The dataFormatBox's item in English of: AUTO */
	public static final String FORMAT_AUTO_EN = "AUTO";

	/** The dataFormatBox's item in English of: AUTO */
	public static final String FORMAT_AUTO_JA = "自動識別";

	/** The dataFormatBox's item in English of: SPREADSHEET */
	public static final String FORMAT_SPREADSHEET_EN = "Copied from Spreadsheets";

	/** The dataFormatBox's item in English of: SPREADSHEET */
	public static final String FORMAT_SPREADSHEET_JA = "表計算ソフトからのコピー";

	/** The dataFormatBox's item in English of: 3-COLUMNS CSV */
	public static final String FORMAT_THREE_COLUMNS_CSV_EN = "3-COLUMNS, CSV (Comma Separated)";

	/** The dataFormatBox's item in Japanese of: 3-COLUMNS CSV */
	public static final String FORMAT_THREE_COLUMNS_CSV_JA = "3カラム, CSV (カンマ区切り)";

	/** The dataFormatBox's item in English of: 3-COLUMNS STSV.  */
	public static final String FORMAT_THREE_COLUMNS_STSV_EN = "3-COLUMNS, STSV (Space/Tab Separated)";

	/** The dataFormatBox's item in Japanese of: 3-COLUMNS STSV.  */
	public static final String FORMAT_THREE_COLUMNS_STSV_JA = "3カラム, STSV (スペース/タブ区切り)";

	/** The dataFormatBox's item in English of: 3-COLUMNS TSV.  */
	public static final String FORMAT_THREE_COLUMNS_TSV_EN = "3-COLUMNS, TSV (Tab Separated)";

	/** The dataFormatBox's item in Japanese of: 3-COLUMNS TSV.  */
	public static final String FORMAT_THREE_COLUMNS_TSV_JA = "3カラム, TSV (厳密なタブ区切り)";

	/** The dataFormatBox's item in English of: 4-COLUMNS CSV */
	public static final String FORMAT_FOUR_COLUMNS_CSV_EN = "4-COLUMNS, CSV (Comma Separated)";

	/** The dataFormatBox's item in Japanese of: 4-COLUMNS CSV */
	public static final String FORMAT_FOUR_COLUMNS_CSV_JA = "4カラム, CSV (カンマ区切り)";

	/** The dataFormatBox's item in English of: 4-COLUMNS STSV.  */
	public static final String FORMAT_FOUR_COLUMNS_STSV_EN = "4-COLUMNS, STSV (Space/Tab Separated)";

	/** The dataFormatBox's item in Japanese of: 4-COLUMNS STSV.  */
	public static final String FORMAT_FOUR_COLUMNS_STSV_JA = "4カラム, STSV (スペース/タブ区切り)";

	/** The dataFormatBox's item in English of: 4-COLUMNS TSV.  */
	public static final String FORMAT_FOUR_COLUMNS_TSV_EN = "4-COLUMNS, TSV (Tab Separated)";

	/** The dataFormatBox's item in Japanese of: 4-COLUMNS TSV.  */
	public static final String FORMAT_FOUR_COLUMNS_TSV_JA = "4カラム, TSV (厳密なタブ区切り)";

	/** The dataFormatBox's item in English of: MATRIX CSV */
	public static final String FORMAT_MATRIX_CSV_EN = "MATRIX, CSV (Comma Separated)";

	/** The dataFormatBox's item in Japanese of: MATRIX CSV */
	public static final String FORMAT_MATRIX_CSV_JA = "マトリックス, CSV (カンマ区切り)";

	/** The dataFormatBox's item in English of: MATRIX STSV.  */
	public static final String FORMAT_MATRIX_STSV_EN = "MATRIX, STSV (Space/Tab Separated)";

	/** The dataFormatBox's item in Japanese of: MATRIX STSV.  */
	public static final String FORMAT_MATRIX_STSV_JA = "マトリックス, STSV (スペース/タブ区切り)";

	/** The dataFormatBox's item in English of: MATRIX TSV.  */
	public static final String FORMAT_MATRIX_TSV_EN = "MATRIX, TSV (Tab Separated)";

	/** The dataFormatBox's item in English of: MATRIX TSV.  */
	public static final String FORMAT_MATRIX_TSV_JA = "マトリックス, TSV (厳密なタブ区切り)";


	/** The frame of this window. */
	public volatile JFrame frame;

	/** The label of the list of the files to be opened. */
	public volatile JLabel fileListLabel;

	/** The text area of the list of the files to be opened. */
	public volatile JTextArea fileListArea;

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


	/** The button to open data files. */
	public volatile JButton openButton;

	/** The button to plot the currently opened data files. */
	public volatile JButton plotButton;

	/** The button to clear the list of the currently opened data files. */
	public volatile JButton clearButton;


	/**
	 * Creates a new window.
	 *
	 * @param configuration The configuration of this application.
	 */
	public DataFileOpeningWindow() {

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
			int bottomMarginShort = 2;
			int topMarginShort = 2;
			int leftMarginLong = 20;

			constraints.gridwidth = 2;
			constraints.weighty = 0.03;

			// The label of the list of the files to be opened.
			fileListLabel = new JLabel("Unconfigured");
			constraints.insets = new Insets(topMargin, leftMargin, bottomMarginShort, rightMargin);
			layout.setConstraints(fileListLabel, constraints);
			basePanel.add(fileListLabel);

			constraints.gridy++;
			constraints.weighty = 1.2;

			// The text area of the list of the files to be opened.
			fileListArea = new JTextArea();
			constraints.insets = new Insets(topMarginShort, leftMarginLong, bottomMargin, rightMargin);
			JScrollPane fileListScrollPane = new JScrollPane(
					fileListArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
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
			constraints.weighty = 0.2;

			// The button to open data files.
			openButton = new JButton();
			constraints.insets = new Insets(topMargin, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(openButton, constraints);
			basePanel.add(openButton);

			constraints.gridy++;

			// The button to plot the currently opened data files.
			plotButton = new JButton();
			constraints.insets = new Insets(topMargin, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(plotButton, constraints);
			basePanel.add(plotButton);

			constraints.gridy++;

			// The button to clear the list of the currently opened data files.
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
			fileListLabel.setText("ファイルリスト:");
			openButton.setText("ファイルを開く");
			plotButton.setText("プロット");
			clearButton.setText("クリア");

			dataFormatLabel.setText("データ書式: ");
			{
				autoFormatItem.setText(FORMAT_AUTO_JA);
				spreadsheetFormatItem.setText(FORMAT_SPREADSHEET_JA);

				threeColumnsCSVFormatItem.setText(FORMAT_THREE_COLUMNS_CSV_JA);
				threeColumnsSTSVFormatItem.setText(FORMAT_THREE_COLUMNS_STSV_JA);
				threeColumnsTSVStrictFormatItem.setText(FORMAT_THREE_COLUMNS_TSV_JA);

				fourColumnsCSVFormatItem.setText(FORMAT_FOUR_COLUMNS_CSV_JA);
				fourColumnsSTSVFormatItem.setText(FORMAT_FOUR_COLUMNS_STSV_JA);
				fourColumnsTSVStrictFormatItem.setText(FORMAT_FOUR_COLUMNS_TSV_JA);

				matrixCSVFormatItem.setText(FORMAT_MATRIX_CSV_JA);
				matrixSTSVFormatItem.setText(FORMAT_MATRIX_STSV_JA);
				matrixTSVStrictFormatItem.setText(FORMAT_MATRIX_TSV_JA);
			}
		}

		/**
		 * Sets English texts to the GUI components.
		 */
		private void setEnglishTexts() {
			frame.setTitle("Open Files");
			fileListLabel.setText("File List:");
			openButton.setText("OPEN FILES");
			plotButton.setText("PLOT");
			clearButton.setText("CLEAR");

			dataFormatLabel.setText("Data Format: ");
			{
				autoFormatItem.setText(FORMAT_AUTO_EN);
				spreadsheetFormatItem.setText(FORMAT_SPREADSHEET_EN);

				threeColumnsCSVFormatItem.setText(FORMAT_THREE_COLUMNS_CSV_EN);
				threeColumnsSTSVFormatItem.setText(FORMAT_THREE_COLUMNS_STSV_EN);
				threeColumnsTSVStrictFormatItem.setText(FORMAT_THREE_COLUMNS_TSV_EN);

				fourColumnsCSVFormatItem.setText(FORMAT_FOUR_COLUMNS_CSV_EN);
				fourColumnsSTSVFormatItem.setText(FORMAT_FOUR_COLUMNS_STSV_EN);
				fourColumnsTSVStrictFormatItem.setText(FORMAT_FOUR_COLUMNS_TSV_EN);

				matrixCSVFormatItem.setText(FORMAT_MATRIX_CSV_EN);
				matrixSTSVFormatItem.setText(FORMAT_MATRIX_STSV_EN);
				matrixTSVStrictFormatItem.setText(FORMAT_MATRIX_TSV_EN);
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
			fileListLabel.setFont(uiBoldFont);
			fileListArea.setFont(uiPlainFont);
			dataFormatLabel.setFont(uiBoldFont);
			dataFormatBox.setFont(uiPlainFont);
			openButton.setFont(uiBoldFont);
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
		switch (selectedItem) {
			case FORMAT_AUTO_EN:
			case FORMAT_AUTO_JA: {
				return RinearnGraph3DDataFileFormat.AUTO;
			}
			case FORMAT_SPREADSHEET_EN:
			case FORMAT_SPREADSHEET_JA: {
				return RinearnGraph3DDataFileFormat.MATRIX_TSV;
			}
			case FORMAT_THREE_COLUMNS_CSV_EN:
			case FORMAT_THREE_COLUMNS_CSV_JA: {
				return RinearnGraph3DDataFileFormat.THREE_COLUMNS_CSV;
			}
			case FORMAT_THREE_COLUMNS_STSV_EN:
			case FORMAT_THREE_COLUMNS_STSV_JA: {
				return RinearnGraph3DDataFileFormat.THREE_COLUMNS_STSV;
			}
			case FORMAT_THREE_COLUMNS_TSV_EN:
			case FORMAT_THREE_COLUMNS_TSV_JA: {
				return RinearnGraph3DDataFileFormat.THREE_COLUMNS_TSV;
			}
			case FORMAT_FOUR_COLUMNS_CSV_EN:
			case FORMAT_FOUR_COLUMNS_CSV_JA: {
				return RinearnGraph3DDataFileFormat.FOUR_COLUMNS_CSV;
			}
			case FORMAT_FOUR_COLUMNS_STSV_EN:
			case FORMAT_FOUR_COLUMNS_STSV_JA: {
				return RinearnGraph3DDataFileFormat.FOUR_COLUMNS_STSV;
			}
			case FORMAT_FOUR_COLUMNS_TSV_EN:
			case FORMAT_FOUR_COLUMNS_TSV_JA: {
				return RinearnGraph3DDataFileFormat.FOUR_COLUMNS_TSV;
			}
			case FORMAT_MATRIX_CSV_EN:
			case FORMAT_MATRIX_CSV_JA: {
				return RinearnGraph3DDataFileFormat.MATRIX_CSV;
			}
			case FORMAT_MATRIX_STSV_EN:
			case FORMAT_MATRIX_STSV_JA: {
				return RinearnGraph3DDataFileFormat.MATRIX_STSV;
			}
			case FORMAT_MATRIX_TSV_EN:
			case FORMAT_MATRIX_TSV_JA: {
				return RinearnGraph3DDataFileFormat.MATRIX_TSV;
			}
			default : {
				throw new IllegalStateException("Unexpected data file format: " + selectedItem);
			}
		}
	}
}
