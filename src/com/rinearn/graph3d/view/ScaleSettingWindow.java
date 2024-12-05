package com.rinearn.graph3d.view;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.FontConfiguration;

import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JCheckBox;

import java.lang.reflect.InvocationTargetException;


/**
 * The window of "Set Ranges" menu.
 */
public class ScaleSettingWindow {

	/** The default width [px] of this window. */
	public static final int DEFAULT_WINDOW_WIDTH = 400;

	/** The default height [px] of this window. */
	public static final int DEFAULT_WINDOW_HEIGHT = 680;

	/** The frame of this window. */
	public volatile JFrame frame;

	/** The tabbed pane of this window. */
	public volatile JTabbedPane tabbedPane;


	/** The panel on which the contents of the "Design" tab are located. */
	public volatile JPanel designTabPanel;

	/** The container of the setting items on "Design" tab. */
	public volatile DesignTabItems designTabItems;

	/** The panel on which the contents of the "Ticks" tab are located. */
	public volatile JPanel ticksTabPanel;

	/** The container of the setting items on "Ticks" tab. */
	public volatile TicksTabItems ticksTabItems;

	/** The panel on which the contents of the "Formats" tab are located. */
	public volatile JPanel formatsTabPanel;

	/** The container of the setting items on "Formats" tab. */
	public volatile FormatsTabItems formatsTabItems;

	/** The panel on which the contents of the "Visibilities" tab are located. */
	public volatile JPanel visibilitiesTabPanel;

	/** The container of the setting items on "Visibilities" tab. */
	public volatile VisibilitiesTabItems visibilitiesTabItems;

	/** The panel on which the contents of the "Images" tab are located. */
	public volatile JPanel imagesTabPanel;

	/** The container of the setting items on "Images" tab. */
	public volatile ImagesTabItems imagesTabItems;

	/** The button to reflect settings. */
	public volatile JButton okButton;


	/**
	 * The container of the setting items of "Design" tab.
	 */
	public class DesignTabItems {

		/** The label of the combo box to select the type of the outer frame. */
		public volatile JLabel frameTypeLabel;

		/** The combo box to select the type of the outer frame. */
		public volatile JComboBox<String> frameTypeBox;

		/** The label of the text field of the line width of the outer frame. */
		public volatile JLabel frameLineWidthLabel;

		/** The text field of the line width of the outer frame. */
		public volatile JTextField frameLineWidthField;

		/** The label of the text field of the margin between tick lines and numbers. */
		public volatile JLabel tickLineMarginLabel;

		/** The text field of the margin between tick lines and numbers. */
		public volatile JTextField tickLineMarginField;

		/** The label of the text field of the length of the tick lines. */
		public volatile JLabel tickLineLengthLabel;

		/** The text field of the length of the tick lines. */
		public volatile JTextField tickLineLengthField;

		/** The check box of the option to draw the ticks towards inside of the outer frame. */
		public volatile JCheckBox tickInsideBox;
	}


	/**
	 * The container of the setting items of "Ticks" tab.
	 */
	public class TicksTabItems {

		/** The label of the X-axis section on "Ticks" tab. */
		public volatile JLabel xAxisLabel;

		/** The label of the Y-axis section on "Ticks" tab. */
		public volatile JLabel yAxisLabel;

		/** The label of the Z-axis section on "Ticks" tab. */
		public volatile JLabel zAxisLabel;

		/** The label of the color-bar section on "Ticks" tab. */
		public volatile JLabel colorBarLabel;

		/** The label of the combo box to select the mode of X-ticks. */
		public volatile JLabel xModeLabel;

		/** The label of the combo box to select the mode of Y-ticks. */
		public volatile JLabel yModeLabel;

		/** The label of the combo box to select the mode of Z-ticks. */
		public volatile JLabel zModeLabel;

		/** The label of the combo box to select the mode of color-bar-ticks. */
		public volatile JLabel colorBarModeLabel;

		/** The combo box to select the mode of X-ticks. */
		public volatile JComboBox<String> xModeBox;

		/** The combo box to select the mode of Y-ticks. */
		public volatile JComboBox<String> yModeBox;

		/** The combo box to select the mode of Z-ticks. */
		public volatile JComboBox<String> zModeBox;

		/** The combo box to select the mode of color-bar-ticks. */
		public volatile JComboBox<String> colorBarModeBox;

		/** The panel on which xEqualDivisionPanel or xEqualDivisionItems is mounted (swappable). */
		public volatile JPanel xSwappablePanel;

		/** The panel on which yEqualDivisionPanel or yEqualDivisionItems is mounted (swappable). */
		public volatile JPanel ySwappablePanel;

		/** The panel on which zEqualDivisionPanel or zEqualDivisionItems is mounted (swappable). */
		public volatile JPanel zSwappablePanel;

		/** The panel on which colorBarEqualDivisionPanel or colorBarEqualDivisionItems is mounted (swappable). */
		public volatile JPanel colorBarSwappablePanel;

		/** The container of the setting items of X-ticks in EQUAL-DIVISION mode. */
		public volatile TicksAxisEqualDivisionItems xEqualDivisionItems;

		/** The container of the setting items of Y-ticks in EQUAL-DIVISION mode. */
		public volatile TicksAxisEqualDivisionItems yEqualDivisionItems;

		/** The container of the setting items of Z-ticks in EQUAL-DIVISION mode. */
		public volatile TicksAxisEqualDivisionItems zEqualDivisionItems;

		/** The container of the setting items of color-bar-ticks in EQUAL-DIVISION mode. */
		public volatile TicksAxisEqualDivisionItems colorBarEqualDivisionItems;

		/** The container of the setting items of X-ticks in EQUAL-DIVISION mode. */
		public volatile TicksAxisManualItems xManualItems;

		/** The container of the setting items of Y-ticks in EQUAL-DIVISION mode. */
		public volatile TicksAxisManualItems yManualItems;

		/** The container of the setting items of Z-ticks in EQUAL-DIVISION mode. */
		public volatile TicksAxisManualItems zManualItems;

		/** The container of the setting items of color-bar-ticks in EQUAL-DIVISION mode. */
		public volatile TicksAxisManualItems colorBarManualItems;
	}

	/** The container class of the tick setting items of each axis in EQUAL-DIVISION mode. */
	public class TicksAxisEqualDivisionItems {

		/** The panel on which the setting items for EQUAL-DIVISION mode are mounted. */
		public volatile JPanel panel;

		/** The label of the text field of the division count. */
		public volatile JLabel divisionCountLabel;

		/** The text field of the division count. */
		public volatile JTextField divisionCountField;
	}

	/** The container class of the tick setting items of each axis in MANUAL mode. */
	public class TicksAxisManualItems {

		/** The panel on which the setting items for MANUAL mode are mounted. */
		public volatile JPanel panel;

		/** The label of the text field of the tick coordinates. */
		public volatile JLabel coordinatesLabel;

		/** The text field of the tick coordinates. */
		public volatile JTextField coordinatesField;

		/** The label of the text field of the tick labels. */
		public volatile JLabel labelsLabel;

		/** The text field of the tick labels. */
		public volatile JTextField labelsField;
	}


	/**
	 * The container of the setting items of "Formats" tab.
	 */
	public class FormatsTabItems {

		/** The label of the X-axis section on "Formats" tab. */
		public volatile JLabel xAxisLabel;

		/** The label of the Y-axis section on "Formats" tab. */
		public volatile JLabel yAxisLabel;

		/** The label of the Z-axis section on "Formats" tab. */
		public volatile JLabel zAxisLabel;

		/** The label of the color-bar section on "Ticks" tab. */
		public volatile JLabel colorBarLabel;

		/** The checkbox to ON/OFF the auto-formatting of X-ticks. */
		public volatile JCheckBox xAutoBox;

		/** The checkbox to ON/OFF the auto-formatting of Y-ticks. */
		public volatile JCheckBox yAutoBox;

		/** The checkbox to ON/OFF the auto-formatting of Z-ticks. */
		public volatile JCheckBox zAutoBox;

		/** The checkbox to ON/OFF the auto-formatting of color-bar-ticks. */
		public volatile JCheckBox colorBarAutoBox;


		/** The label of the text field of the medium-range format of X-ticks. */
		public volatile JLabel xMediumFormatLabel;

		/** The text field of the medium-range format of X-ticks. */
		public volatile JTextField xMediumFormatField;

		/** The label of the text field of the medium-range format of Y-ticks. */
		public volatile JLabel yMediumFormatLabel;

		/** The text field of the medium-range format of Y-ticks. */
		public volatile JTextField yMediumFormatField;

		/** The label of the text field of the medium-range format of Z-ticks. */
		public volatile JLabel zMediumFormatLabel;

		/** The text field of the medium-range format of Z-ticks. */
		public volatile JTextField zMediumFormatField;

		/** The label of the text field of the medium-range format of the color bar. */
		public volatile JLabel colorBarMediumFormatLabel;

		/** The text field of the medium-range format of the color bar. */
		public volatile JTextField colorBarMediumFormatField;


		/** The label of the text field of the short-range format of X-ticks. */
		public volatile JLabel xShortFormatLabel;

		/** The text field of the short-range format of X-ticks. */
		public volatile JTextField xShortFormatField;

		/** The label of the text field of the short-range format of Y-ticks. */
		public volatile JLabel yShortFormatLabel;

		/** The text field of the short-range format of Y-ticks. */
		public volatile JTextField yShortFormatField;

		/** The label of the text field of the short-range format of Z-ticks. */
		public volatile JLabel zShortFormatLabel;

		/** The text field of the short-range format of Z-ticks. */
		public volatile JTextField zShortFormatField;

		/** The label of the text field of the short-range format of the color bar. */
		public volatile JLabel colorBarShortFormatLabel;

		/** The text field of the short-range format of the color bar. */
		public volatile JTextField colorBarShortFormatField;


		/** The label of the text field of the long-range format of X-ticks. */
		public volatile JLabel xLongFormatLabel;

		/** The text field of the long-range format of X-ticks. */
		public volatile JTextField xLongFormatField;

		/** The label of the text field of the long-range format of Y-ticks. */
		public volatile JLabel yLongFormatLabel;

		/** The text field of the long-range format of Y-ticks. */
		public volatile JTextField yLongFormatField;

		/** The label of the text field of the long-range format of Z-ticks. */
		public volatile JLabel zLongFormatLabel;

		/** The text field of the long-range format of Z-ticks. */
		public volatile JTextField zLongFormatField;

		/** The label of the text field of the long-range format of the color bar. */
		public volatile JLabel colorBarLongFormatLabel;

		/** The text field of the long-range format of the color bar. */
		public volatile JTextField colorBarLongFormatField;
	}


	/**
	 * The container of the setting items of "Visibilities" tab.
	 */
	public class VisibilitiesTabItems {

		/** The label of the X-axis section on "Visibilities" tab. */
		public volatile JLabel xAxisLabel;

		/** The label of the Y-axis section on "Visibilities" tab. */
		public volatile JLabel yAxisLabel;

		/** The label of the Z-axis section on "Visibilities" tab. */
		public volatile JLabel zAxisLabel;

		/** The checkbox to ON/OFF auto-controlling the visibility of X-ticks. */
		public volatile JCheckBox xAutoBox;

		/** The checkbox to ON/OFF auto-controlling the visibility of Y-ticks. */
		public volatile JCheckBox yAutoBox;

		/** The checkbox to ON/OFF auto-controlling the visibility of Z-ticks. */
		public volatile JCheckBox zAutoBox;

		/** The checkbox to ON/OFF the X-ticks A. */
		public volatile JCheckBox xABox;

		/** The checkbox to ON/OFF the X-ticks B. */
		public volatile JCheckBox xBBox;

		/** The checkbox to ON/OFF the X-ticks C. */
		public volatile JCheckBox xCBox;

		/** The checkbox to ON/OFF the X-ticks D. */
		public volatile JCheckBox xDBox;

		/** The checkbox to ON/OFF the Y-ticks A. */
		public volatile JCheckBox yABox;

		/** The checkbox to ON/OFF the Y-ticks B. */
		public volatile JCheckBox yBBox;

		/** The checkbox to ON/OFF the Y-ticks C. */
		public volatile JCheckBox yCBox;

		/** The checkbox to ON/OFF the Y-ticks D. */
		public volatile JCheckBox yDBox;

		/** The checkbox to ON/OFF the Z-ticks A. */
		public volatile JCheckBox zABox;

		/** The checkbox to ON/OFF the Z-ticks B. */
		public volatile JCheckBox zBBox;

		/** The checkbox to ON/OFF the Z-ticks C. */
		public volatile JCheckBox zCBox;

		/** The checkbox to ON/OFF the Z-ticks D. */
		public volatile JCheckBox zDBox;
	}


	/**
	 * The container of the setting items of "Images" tab.
	 */
	public class ImagesTabItems {

		/** The label of the X-Y plane (Floor, Z-min side) section. */
		public volatile JLabel xyFloorPlaneLabel;

		/** The container of the setting items of the X-Y plane (Floor, Z-min side) section. */
		public volatile ImagesPlaneItems xyFloorPlaneItems;

		/** The label of the X-Y plane (Ceil, Z-max side) section. */
		public volatile JLabel xyCeilPlaneLabel;

		/** The container of the setting items of the X-Y plane (Ceil, Z-max side) section. */
		public volatile ImagesPlaneItems xyCeilPlaneItems;


		/** The label of the Y-Z plane (Floor, X-min side) section. */
		public volatile JLabel yzFloorPlaneLabel;

		/** The container of the setting items of the Y-Z plane (Floor, X-min side) section. */
		public volatile ImagesPlaneItems yzFloorPlaneItems;

		/** The label of the Y-Z plane (Ceil, X-max side) section. */
		public volatile JLabel yzCeilPlaneLabel;

		/** The container of the setting items of the Y-Z plane (Ceil, X-max side) section. */
		public volatile ImagesPlaneItems yzCeilPlaneItems;


		/** The label of the Z-X plane (Floor, Y-min side) section. */
		public volatile JLabel zxFloorPlaneLabel;

		/** The container of the setting items of the Z-X plane (Floor, Y-min side) section. */
		public volatile ImagesPlaneItems zxFloorPlaneItems;

		/** The label of the Z-X plane (Ceil, Y-max side) section. */
		public volatile JLabel zxCeilPlaneLabel;

		/** The container of the setting items of the Z-X plane (Ceil, Y-max side) section. */
		public volatile ImagesPlaneItems zxCeilPlaneItems;

		/** The checkbox to draw images only inside of the graph. **/
		public volatile JCheckBox insideBox;
	}

	/**
	 * The container of the items on "Images" tab, for each plane.
	 */
	public class ImagesPlaneItems {

		/** The panel on which the setting items for each plane are mounted. */
		JPanel panel;

		/** The text field to input/display the image file path. */
		JTextField filePathField;

		/** The button to open the image file. */
		JButton openFileButton;

		/** The text field of the image width (pixels) displayed on the plane. */
		JTextField imageWidthField;

		/** The text field of the image height (pixels) displayed on the plane. */
		JTextField imageHeightField;
	}


	/**
	 * Creates a new window.
	 *
	 * @param configuration The configuration of this application.
	 */
	public ScaleSettingWindow() {

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

			// Create the tabbed pane of this window.
			tabbedPane = new JTabbedPane();
			constraints.insets = new Insets(topMargin, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(tabbedPane, constraints);
			basePanel.add(tabbedPane);

			// Tabs.
			{
				// The panel on which the contents of the "Design" tab are located.
				designTabPanel = new JPanel();
				mountDesignTabContents();
				tabbedPane.addTab("Unconfigured", designTabPanel);

				// The panel on which the contents of the "Ticks" tab are located.
				ticksTabPanel = new JPanel();
				mountTicksTabContents();
				tabbedPane.addTab("Unconfigured", ticksTabPanel);

				// The panel on which the contents of the "Formats" tab are located.
				formatsTabPanel = new JPanel();
				mountFormatsTabContents();
				tabbedPane.addTab("Unconfigured", formatsTabPanel);

				// The panel on which the contents of the "Visibilities" tab are located.
				visibilitiesTabPanel = new JPanel();
				mountVisibilitiesTabContents();
				tabbedPane.addTab("Unconfigured", visibilitiesTabPanel);

				// The panel on which the contents of the "Images" tab are located.
				imagesTabPanel = new JPanel();
				mountImagesTabContents();
				tabbedPane.addTab("Unconfigured", imagesTabPanel);
			}

			// The button to reflect settings (OK button).
			constraints.gridy++;
			okButton = new JButton();
			constraints.insets = new Insets(topMargin, leftMargin, bottomMargin, rightMargin);
			constraints.weighty = 0.0; // Set to the constant size (depends on the font size).
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
		}

		/**
		 * Sets Japanese texts to the GUI components.
		 */
		private void setJapaneseTexts() {
			frame.setTitle("目盛りの設定");

			tabbedPane.setTitleAt(0, "デザイン");
			{
				designTabItems.frameTypeLabel.setText("フレーム:");
				designTabItems.frameLineWidthLabel.setText("フレーム線の幅:");
				designTabItems.tickLineMarginLabel.setText("目盛り数値の余白:");
				designTabItems.tickLineLengthLabel.setText("目盛り線の長さ:");
				designTabItems.tickInsideBox.setText("内向きに描画");
			}

			tabbedPane.setTitleAt(1, "刻み");
			{
				ticksTabItems.xAxisLabel.setText("- X軸 -");
				ticksTabItems.xModeLabel.setText("モード: ");
				ticksTabItems.xEqualDivisionItems.divisionCountLabel.setText("区間の数: ");
				ticksTabItems.xManualItems.coordinatesLabel.setText("位置: ");
				ticksTabItems.xManualItems.labelsLabel.setText("表記: ");

				ticksTabItems.yAxisLabel.setText("- Y軸 -");
				ticksTabItems.yModeLabel.setText("モード: ");
				ticksTabItems.yEqualDivisionItems.divisionCountLabel.setText("区間の数: ");
				ticksTabItems.yManualItems.coordinatesLabel.setText("位置: ");
				ticksTabItems.yManualItems.labelsLabel.setText("表記: ");

				ticksTabItems.zAxisLabel.setText("- Z軸 -");
				ticksTabItems.zModeLabel.setText("モード: ");
				ticksTabItems.zEqualDivisionItems.divisionCountLabel.setText("区間の数: ");
				ticksTabItems.zManualItems.coordinatesLabel.setText("位置: ");
				ticksTabItems.zManualItems.labelsLabel.setText("表記: ");

				ticksTabItems.colorBarLabel.setText("- カラーバー -");
				ticksTabItems.colorBarModeLabel.setText("モード: ");
				ticksTabItems.colorBarEqualDivisionItems.divisionCountLabel.setText("区間の数: ");
				ticksTabItems.colorBarManualItems.coordinatesLabel.setText("位置: ");
				ticksTabItems.colorBarManualItems.labelsLabel.setText("表記: ");
			}

			tabbedPane.setTitleAt(2, "書式");
			{
				formatsTabItems.xAxisLabel.setText("- X軸 -");
				formatsTabItems.xAutoBox.setText("自動調整");
				formatsTabItems.xMediumFormatLabel.setText("0.1 ～ 10");
				formatsTabItems.xShortFormatLabel.setText("|x| < 0.1");
				formatsTabItems.xLongFormatLabel.setText("10 < |x|");

				formatsTabItems.yAxisLabel.setText("- Y軸 -");
				formatsTabItems.yAutoBox.setText("自動調整");
				formatsTabItems.yMediumFormatLabel.setText("0.1 ～ 10");
				formatsTabItems.yShortFormatLabel.setText("|y| < 0.1");
				formatsTabItems.yLongFormatLabel.setText("10 < |y|");

				formatsTabItems.zAxisLabel.setText("- Z軸 -");
				formatsTabItems.zAutoBox.setText("自動調整");
				formatsTabItems.zMediumFormatLabel.setText("0.1 ～ 10");
				formatsTabItems.zShortFormatLabel.setText("|z| < 0.1");
				formatsTabItems.zLongFormatLabel.setText("10 < |z|");
			}

			tabbedPane.setTitleAt(3, "表示");
			{
				visibilitiesTabItems.xAxisLabel.setText("- X軸 -");
				visibilitiesTabItems.xAutoBox.setText("自動調整");
				visibilitiesTabItems.xABox.setText("X-A");
				visibilitiesTabItems.xBBox.setText("X-B");
				visibilitiesTabItems.xCBox.setText("X-C");
				visibilitiesTabItems.xDBox.setText("X-D");

				visibilitiesTabItems.yAxisLabel.setText("- Y軸 -");
				visibilitiesTabItems.yAutoBox.setText("自動調整");
				visibilitiesTabItems.yABox.setText("Y-A");
				visibilitiesTabItems.yBBox.setText("Y-B");
				visibilitiesTabItems.yCBox.setText("Y-C");
				visibilitiesTabItems.yDBox.setText("Y-D");

				visibilitiesTabItems.zAxisLabel.setText("- Z軸 -");
				visibilitiesTabItems.zAutoBox.setText("自動調整");
				visibilitiesTabItems.zABox.setText("Z-A");
				visibilitiesTabItems.zBBox.setText("Z-B");
				visibilitiesTabItems.zCBox.setText("Z-C");
				visibilitiesTabItems.zDBox.setText("Z-D");
			}

			tabbedPane.setTitleAt(4, "画像");
			{
				imagesTabItems.xyFloorPlaneLabel.setText("- X-Y平面（床、Z最小側）-");
				imagesTabItems.xyCeilPlaneLabel.setText("- X-Y平面（天井、Z最大側）-");
				imagesTabItems.yzFloorPlaneLabel.setText("- Y-Z平面（X最小側）-");
				imagesTabItems.yzCeilPlaneLabel.setText("- Y-Z平面（X最大側）-");
				imagesTabItems.zxFloorPlaneLabel.setText("- Z-X平面（Y最小側）-");
				imagesTabItems.zxCeilPlaneLabel.setText("- Z-X平面（Y最大側）-");

				imagesTabItems.xyFloorPlaneItems.openFileButton.setText("開く");
				imagesTabItems.xyCeilPlaneItems.openFileButton.setText("開く");
				imagesTabItems.yzFloorPlaneItems.openFileButton.setText("開く");
				imagesTabItems.yzCeilPlaneItems.openFileButton.setText("開く");
				imagesTabItems.zxFloorPlaneItems.openFileButton.setText("開く");
				imagesTabItems.zxCeilPlaneItems.openFileButton.setText("開く");

				imagesTabItems.insideBox.setText("内側のみ表示");
			}

			okButton.setText("OK");
		}

		/**
		 * Sets English texts to the GUI components.
		 */
		private void setEnglishTexts() {
			frame.setTitle("Set Scale");

			tabbedPane.setTitleAt(0, "Design");
			{
				designTabItems.frameTypeLabel.setText("Frame:");
				designTabItems.frameLineWidthLabel.setText("Width of Frame Lines:");
				designTabItems.tickLineMarginLabel.setText("Margin of Tick Lines:");
				designTabItems.tickLineLengthLabel.setText("Length of Tick Lines:");
				designTabItems.tickInsideBox.setText("Draw Inside");
			}

			tabbedPane.setTitleAt(1, "Ticks");
			{
				ticksTabItems.xAxisLabel.setText("- X Axis -");
				ticksTabItems.xModeLabel.setText("Mode: ");
				ticksTabItems.xEqualDivisionItems.divisionCountLabel.setText("Number of Sections: ");
				ticksTabItems.xManualItems.coordinatesLabel.setText("Coords: ");
				ticksTabItems.xManualItems.labelsLabel.setText("Labels: ");

				ticksTabItems.yAxisLabel.setText("- Y Axis -");
				ticksTabItems.yModeLabel.setText("Mode: ");
				ticksTabItems.yEqualDivisionItems.divisionCountLabel.setText("Number of Sections: ");
				ticksTabItems.yManualItems.coordinatesLabel.setText("Coords: ");
				ticksTabItems.yManualItems.labelsLabel.setText("Labels: ");

				ticksTabItems.zAxisLabel.setText("- Z Axis -");
				ticksTabItems.zModeLabel.setText("Mode: ");
				ticksTabItems.zEqualDivisionItems.divisionCountLabel.setText("Number of Sections: ");
				ticksTabItems.zManualItems.coordinatesLabel.setText("Coords: ");
				ticksTabItems.zManualItems.labelsLabel.setText("Labels: ");

				ticksTabItems.colorBarLabel.setText("- Color Bar -");
				ticksTabItems.colorBarModeLabel.setText("Mode: ");
				ticksTabItems.colorBarEqualDivisionItems.divisionCountLabel.setText("Number of Sections: ");
				ticksTabItems.colorBarManualItems.coordinatesLabel.setText("Coords: ");
				ticksTabItems.colorBarManualItems.labelsLabel.setText("Labels: ");
			}

			tabbedPane.setTitleAt(2, "Formats");
			{
				formatsTabItems.xAxisLabel.setText("- X Axis -");
				formatsTabItems.xAutoBox.setText("Auto");
				formatsTabItems.xMediumFormatLabel.setText("0.1 - 10");
				formatsTabItems.xShortFormatLabel.setText("|x| < 0.1");
				formatsTabItems.xLongFormatLabel.setText("10 < |x|");

				formatsTabItems.yAxisLabel.setText("- Y Axis -");
				formatsTabItems.yAutoBox.setText("Auto");
				formatsTabItems.yMediumFormatLabel.setText("0.1 - 10");
				formatsTabItems.yShortFormatLabel.setText("|y| < 0.1");
				formatsTabItems.yLongFormatLabel.setText("10 < |y|");

				formatsTabItems.zAxisLabel.setText("- Z Axis -");
				formatsTabItems.zAutoBox.setText("Auto");
				formatsTabItems.zMediumFormatLabel.setText("0.1 - 10");
				formatsTabItems.zShortFormatLabel.setText("|z| < 0.1");
				formatsTabItems.zLongFormatLabel.setText("10 < |z|");
			}

			tabbedPane.setTitleAt(3, "Visibilities");
			{
				visibilitiesTabItems.xAxisLabel.setText("- X Axis -");
				visibilitiesTabItems.xAutoBox.setText("Auto");
				visibilitiesTabItems.xABox.setText("X-A");
				visibilitiesTabItems.xBBox.setText("X-B");
				visibilitiesTabItems.xCBox.setText("X-C");
				visibilitiesTabItems.xDBox.setText("X-D");

				visibilitiesTabItems.yAxisLabel.setText("- Y Axis -");
				visibilitiesTabItems.yAutoBox.setText("Auto");
				visibilitiesTabItems.yABox.setText("Y-A");
				visibilitiesTabItems.yBBox.setText("Y-B");
				visibilitiesTabItems.yCBox.setText("Y-C");
				visibilitiesTabItems.yDBox.setText("Y-D");

				visibilitiesTabItems.zAxisLabel.setText("- Z Axis -");
				visibilitiesTabItems.zAutoBox.setText("Auto");
				visibilitiesTabItems.zABox.setText("Z-A");
				visibilitiesTabItems.zBBox.setText("Z-B");
				visibilitiesTabItems.zCBox.setText("Z-C");
				visibilitiesTabItems.zDBox.setText("Z-D");
			}

			tabbedPane.setTitleAt(4, "Images");
			{
				imagesTabItems.xyFloorPlaneLabel.setText("- X-Y Plane (Floor, Z-min side) -");
				imagesTabItems.xyCeilPlaneLabel.setText("- X-Y Plane (Ceil, Z-max side) -");
				imagesTabItems.yzFloorPlaneLabel.setText("- Y-Z Plane (X-min) -");
				imagesTabItems.yzCeilPlaneLabel.setText("- Y-Z Plane (X-max) -");
				imagesTabItems.zxFloorPlaneLabel.setText("- Z-X Plane (Y-min) -");
				imagesTabItems.zxCeilPlaneLabel.setText("- Z-X Plane (Y-max) -");

				imagesTabItems.xyFloorPlaneItems.openFileButton.setText("Open");
				imagesTabItems.xyCeilPlaneItems.openFileButton.setText("Open");
				imagesTabItems.yzFloorPlaneItems.openFileButton.setText("Open");
				imagesTabItems.yzCeilPlaneItems.openFileButton.setText("Open");
				imagesTabItems.zxFloorPlaneItems.openFileButton.setText("Open");
				imagesTabItems.zxCeilPlaneItems.openFileButton.setText("Open");

				imagesTabItems.insideBox.setText("Inside Only");
			}

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

			{
				designTabItems.frameTypeLabel.setFont(uiBoldFont);
				designTabItems.frameLineWidthLabel.setFont(uiBoldFont);
				designTabItems.frameLineWidthField.setFont(uiPlainFont);
				designTabItems.tickLineMarginLabel.setFont(uiBoldFont);
				designTabItems.tickLineMarginField.setFont(uiPlainFont);
				designTabItems.tickLineLengthLabel.setFont(uiBoldFont);
				designTabItems.tickLineLengthField.setFont(uiPlainFont);
				designTabItems.tickInsideBox.setFont(uiBoldFont);
			}

			{
				ticksTabItems.xAxisLabel.setFont(uiBoldFont);
				ticksTabItems.xModeLabel.setFont(uiBoldFont);
				ticksTabItems.xEqualDivisionItems.divisionCountLabel.setFont(uiBoldFont);
				ticksTabItems.xEqualDivisionItems.divisionCountField.setFont(uiPlainFont);
				ticksTabItems.xManualItems.coordinatesLabel.setFont(uiBoldFont);
				ticksTabItems.xManualItems.coordinatesField.setFont(uiPlainFont);
				ticksTabItems.xManualItems.labelsLabel.setFont(uiBoldFont);
				ticksTabItems.xManualItems.labelsField.setFont(uiPlainFont);

				ticksTabItems.yAxisLabel.setFont(uiBoldFont);
				ticksTabItems.yModeLabel.setFont(uiBoldFont);
				ticksTabItems.yEqualDivisionItems.divisionCountLabel.setFont(uiBoldFont);
				ticksTabItems.yEqualDivisionItems.divisionCountField.setFont(uiPlainFont);
				ticksTabItems.yManualItems.coordinatesLabel.setFont(uiBoldFont);
				ticksTabItems.yManualItems.coordinatesField.setFont(uiPlainFont);
				ticksTabItems.yManualItems.labelsLabel.setFont(uiBoldFont);
				ticksTabItems.yManualItems.labelsField.setFont(uiPlainFont);

				ticksTabItems.zAxisLabel.setFont(uiBoldFont);
				ticksTabItems.zModeLabel.setFont(uiBoldFont);
				ticksTabItems.zEqualDivisionItems.divisionCountLabel.setFont(uiBoldFont);
				ticksTabItems.zEqualDivisionItems.divisionCountField.setFont(uiPlainFont);
				ticksTabItems.zManualItems.coordinatesLabel.setFont(uiBoldFont);
				ticksTabItems.zManualItems.coordinatesField.setFont(uiPlainFont);
				ticksTabItems.zManualItems.labelsLabel.setFont(uiBoldFont);
				ticksTabItems.zManualItems.labelsField.setFont(uiPlainFont);

				ticksTabItems.colorBarLabel.setFont(uiBoldFont);
				ticksTabItems.colorBarModeLabel.setFont(uiBoldFont);
				ticksTabItems.colorBarEqualDivisionItems.divisionCountLabel.setFont(uiBoldFont);
				ticksTabItems.colorBarEqualDivisionItems.divisionCountField.setFont(uiPlainFont);
				ticksTabItems.colorBarManualItems.coordinatesLabel.setFont(uiBoldFont);
				ticksTabItems.colorBarManualItems.coordinatesField.setFont(uiPlainFont);
				ticksTabItems.colorBarManualItems.labelsLabel.setFont(uiBoldFont);
				ticksTabItems.colorBarManualItems.labelsField.setFont(uiPlainFont);
			}

			{
				formatsTabItems.xAxisLabel.setFont(uiBoldFont);
				formatsTabItems.xAutoBox.setFont(uiBoldFont);
				formatsTabItems.xMediumFormatLabel.setFont(uiBoldFont);
				formatsTabItems.xMediumFormatField.setFont(uiPlainFont);
				formatsTabItems.xShortFormatLabel.setFont(uiBoldFont);
				formatsTabItems.xShortFormatField.setFont(uiPlainFont);
				formatsTabItems.xLongFormatLabel.setFont(uiBoldFont);
				formatsTabItems.xLongFormatField.setFont(uiPlainFont);

				formatsTabItems.yAxisLabel.setFont(uiBoldFont);
				formatsTabItems.yAutoBox.setFont(uiBoldFont);
				formatsTabItems.yMediumFormatLabel.setFont(uiBoldFont);
				formatsTabItems.yMediumFormatField.setFont(uiPlainFont);
				formatsTabItems.yShortFormatLabel.setFont(uiBoldFont);
				formatsTabItems.yShortFormatField.setFont(uiPlainFont);
				formatsTabItems.yLongFormatLabel.setFont(uiBoldFont);
				formatsTabItems.yLongFormatField.setFont(uiPlainFont);

				formatsTabItems.zAxisLabel.setFont(uiBoldFont);
				formatsTabItems.zAutoBox.setFont(uiBoldFont);
				formatsTabItems.zMediumFormatLabel.setFont(uiBoldFont);
				formatsTabItems.zMediumFormatField.setFont(uiPlainFont);
				formatsTabItems.zShortFormatLabel.setFont(uiBoldFont);
				formatsTabItems.zShortFormatField.setFont(uiPlainFont);
				formatsTabItems.zLongFormatLabel.setFont(uiBoldFont);
				formatsTabItems.zLongFormatField.setFont(uiPlainFont);
			}

			{
				visibilitiesTabItems.xAxisLabel.setFont(uiBoldFont);
				visibilitiesTabItems.xAutoBox.setFont(uiBoldFont);
				visibilitiesTabItems.xABox.setFont(uiBoldFont);
				visibilitiesTabItems.xBBox.setFont(uiBoldFont);
				visibilitiesTabItems.xCBox.setFont(uiBoldFont);
				visibilitiesTabItems.xDBox.setFont(uiBoldFont);

				visibilitiesTabItems.yAxisLabel.setFont(uiBoldFont);
				visibilitiesTabItems.yAutoBox.setFont(uiBoldFont);
				visibilitiesTabItems.yABox.setFont(uiBoldFont);
				visibilitiesTabItems.yBBox.setFont(uiBoldFont);
				visibilitiesTabItems.yCBox.setFont(uiBoldFont);
				visibilitiesTabItems.yDBox.setFont(uiBoldFont);

				visibilitiesTabItems.zAxisLabel.setFont(uiBoldFont);
				visibilitiesTabItems.zAutoBox.setFont(uiBoldFont);
				visibilitiesTabItems.zABox.setFont(uiBoldFont);
				visibilitiesTabItems.zBBox.setFont(uiBoldFont);
				visibilitiesTabItems.zCBox.setFont(uiBoldFont);
				visibilitiesTabItems.zDBox.setFont(uiBoldFont);
			}

			{
				imagesTabItems.xyFloorPlaneLabel.setFont(uiBoldFont);
				imagesTabItems.xyCeilPlaneLabel.setFont(uiBoldFont);
				imagesTabItems.yzFloorPlaneLabel.setFont(uiBoldFont);
				imagesTabItems.yzCeilPlaneLabel.setFont(uiBoldFont);
				imagesTabItems.zxFloorPlaneLabel.setFont(uiBoldFont);
				imagesTabItems.zxCeilPlaneLabel.setFont(uiBoldFont);

				imagesTabItems.xyFloorPlaneItems.filePathField.setFont(uiPlainFont);
				imagesTabItems.xyCeilPlaneItems.filePathField.setFont(uiPlainFont);
				imagesTabItems.yzFloorPlaneItems.filePathField.setFont(uiPlainFont);
				imagesTabItems.yzCeilPlaneItems.filePathField.setFont(uiPlainFont);
				imagesTabItems.zxFloorPlaneItems.filePathField.setFont(uiPlainFont);
				imagesTabItems.zxCeilPlaneItems.filePathField.setFont(uiPlainFont);

				imagesTabItems.xyFloorPlaneItems.openFileButton.setFont(uiBoldFont);
				imagesTabItems.xyCeilPlaneItems.openFileButton.setFont(uiBoldFont);
				imagesTabItems.yzFloorPlaneItems.openFileButton.setFont(uiBoldFont);
				imagesTabItems.yzCeilPlaneItems.openFileButton.setFont(uiBoldFont);
				imagesTabItems.zxFloorPlaneItems.openFileButton.setFont(uiBoldFont);
				imagesTabItems.zxCeilPlaneItems.openFileButton.setFont(uiBoldFont);

				imagesTabItems.insideBox.setFont(uiBoldFont);
			}

			okButton.setFont(uiBoldFont);
		}

		/**
		 * Updates the values of text fields, by the values stored in the configuration.
		 */
		private void updateValuesByConfiguration() {

			// To be implemented

			System.out.println("To be implemented: ScaleSettingWindow.ConfigurationReflector.updateValuesByConfiguration()");
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





	// ----------------------------------------------------------------------------------
	// Sub procedures constructing UI components, called from ComponentInitializer.run()
	// ----------------------------------------------------------------------------------



	/**
	 * Creates/mounts the contents on "Design" tab.
	 */
	private void mountDesignTabContents() {
		designTabItems = new DesignTabItems();

		// Prepare the layout manager and resources.
		GridBagLayout layout = new GridBagLayout();
		designTabPanel.setLayout(layout);
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
		int bottomMargin = 10;
		int leftMargin = 8;
		int leftMarginLong = 20;
		int rightMargin = 5;
		int topMarginBetweenLabelAndField = 2;
		int bottomMarginBetweenLabelAndField = 2;
		int bottomMarginOfEnd = 210;

		// The label of the combo box to select the type of the outer frame.
		designTabItems.frameTypeLabel = new JLabel("Unconfigured");
		constraints.gridy = 0;
		constraints.insets = new Insets(topMargin, leftMargin, bottomMarginBetweenLabelAndField, rightMargin);
		layout.setConstraints(designTabItems.frameTypeLabel, constraints);
		designTabPanel.add(designTabItems.frameTypeLabel);

		// The combo box to select the type of the outer frame.
		designTabItems.frameTypeBox = new JComboBox<String>();
		constraints.gridy++;
		constraints.insets = new Insets(topMarginBetweenLabelAndField, leftMarginLong, bottomMargin, rightMargin);
		layout.setConstraints(designTabItems.frameTypeBox, constraints);
		designTabPanel.add(designTabItems.frameTypeBox);


		// The label of the text field of the line width of the outer frame.
		designTabItems.frameLineWidthLabel = new JLabel("Unconfigured");
		constraints.gridy++;
		constraints.insets = new Insets(topMargin, leftMargin, bottomMarginBetweenLabelAndField, rightMargin);
		layout.setConstraints(designTabItems.frameLineWidthLabel, constraints);
		designTabPanel.add(designTabItems.frameLineWidthLabel);

		// The text field of the line width of the outer frame.
		designTabItems.frameLineWidthField = new JTextField();
		constraints.gridy++;
		constraints.insets = new Insets(topMarginBetweenLabelAndField, leftMarginLong, bottomMargin, rightMargin);
		layout.setConstraints(designTabItems.frameLineWidthField, constraints);
		designTabPanel.add(designTabItems.frameLineWidthField);


		// The label of the text field of the margin between tick lines and numbers.
		designTabItems.tickLineMarginLabel = new JLabel("Unconfigured");
		constraints.gridy++;
		constraints.insets = new Insets(topMargin, leftMargin, bottomMarginBetweenLabelAndField, rightMargin);
		layout.setConstraints(designTabItems.tickLineMarginLabel, constraints);
		designTabPanel.add(designTabItems.tickLineMarginLabel);

		// The text field of the margin between tick lines and numbers.
		designTabItems.tickLineMarginField = new JTextField();
		constraints.gridy++;
		constraints.insets = new Insets(topMarginBetweenLabelAndField, leftMarginLong, bottomMargin, rightMargin);
		layout.setConstraints(designTabItems.tickLineMarginField, constraints);
		designTabPanel.add(designTabItems.tickLineMarginField);


		// The label of the text field of the length of the tick lines.
		designTabItems.tickLineLengthLabel = new JLabel("Unconfigured");
		constraints.gridy++;
		constraints.insets = new Insets(topMargin, leftMargin, bottomMarginBetweenLabelAndField, rightMargin);
		layout.setConstraints(designTabItems.tickLineLengthLabel, constraints);
		designTabPanel.add(designTabItems.tickLineLengthLabel);

		// The text field of the length of the tick lines.
		designTabItems.tickLineLengthField = new JTextField();
		constraints.gridy++;
		constraints.insets = new Insets(topMarginBetweenLabelAndField, leftMarginLong, bottomMargin, rightMargin);
		layout.setConstraints(designTabItems.tickLineLengthField, constraints);
		designTabPanel.add(designTabItems.tickLineLengthField);


		// The check box of the option to draw the ticks towards inside of the outer frame.
		designTabItems.tickInsideBox = new JCheckBox("Unconfigured");
		constraints.gridy++;
		constraints.insets = new Insets(topMargin, leftMargin, bottomMarginOfEnd, rightMargin);
		layout.setConstraints(designTabItems.tickInsideBox, constraints);
		designTabPanel.add(designTabItems.tickInsideBox);
	}


	/**
	 * Creates/mounts the contents on "Tiks" tab.
	 */
	private void mountTicksTabContents() {
		ticksTabItems = new TicksTabItems();

		// Prepare the layout manager and resources.
		GridBagLayout layout = new GridBagLayout();
		ticksTabPanel.setLayout(layout);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1; // CAUTION: This is NOT the total number of columns of the grid. This is the number of the cells for one component.
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.fill = GridBagConstraints.BOTH;

		// Define margines.
		int topMargin = 5;
		int bottomMargin = 10;
		int leftMargin = 8;
		int rightMargin = 5;
		int topMarginInSection = 2;
		int bottomMarginInSection = 2;
		int leftMarginInSection = 20;
		double leftColumnWeight = 0.1;
		double rightColumnWeight = 0.9;
		double swappablePanelWeight = 10.0;

		// X-Axis
		{
			constraints.gridy = 0;
			constraints.gridx = 0;
			constraints.gridwidth = 2;

			// The title label of the section of X-axis.
			ticksTabItems.xAxisLabel = new JLabel("Unconfigured");
			constraints.insets = new Insets(topMargin, leftMargin, bottomMarginInSection, rightMargin);
			layout.setConstraints(ticksTabItems.xAxisLabel, constraints);
			ticksTabPanel.add(ticksTabItems.xAxisLabel);

			constraints.gridy++;
			constraints.gridx = 0;
			constraints.gridwidth = 1;

			// The label of the combo box to select the tick mode.
			ticksTabItems.xModeLabel = new JLabel("Unconfigured");
			constraints.weightx = leftColumnWeight;
			constraints.insets = new Insets(topMarginInSection, leftMarginInSection, bottomMarginInSection, 0);
			layout.setConstraints(ticksTabItems.xModeLabel, constraints);
			ticksTabPanel.add(ticksTabItems.xModeLabel);

			// The label of the combo box to select the tick mode.
			ticksTabItems.xModeBox = new JComboBox<String>();
			constraints.gridx++;
			constraints.weightx = rightColumnWeight;
			constraints.insets = new Insets(topMarginInSection, 0, bottomMarginInSection, rightMargin);
			layout.setConstraints(ticksTabItems.xModeBox, constraints);
			ticksTabPanel.add(ticksTabItems.xModeBox);

			constraints.gridy++;
			constraints.gridx = 0;
			constraints.gridwidth = 2;

			// The base panel on which a panel for each mode is mounted (swappable).
			ticksTabItems.xSwappablePanel = new JPanel();
			constraints.weighty = swappablePanelWeight;
			constraints.insets = new Insets(0, leftMarginInSection, bottomMargin, rightMargin);
			layout.setConstraints(ticksTabItems.xSwappablePanel, constraints);
			ticksTabItems.xSwappablePanel.setLayout(new GridLayout(1, 1));
			ticksTabPanel.add(ticksTabItems.xSwappablePanel);

			constraints.weighty = 1.0;
		}

		// Y-Axis
		{
			constraints.gridy++;
			constraints.gridx = 0;
			constraints.gridwidth = 2;

			// The title label of the section of Y-axis.
			ticksTabItems.yAxisLabel = new JLabel("Unconfigured");
			constraints.insets = new Insets(topMargin, leftMargin, bottomMarginInSection, rightMargin);
			layout.setConstraints(ticksTabItems.yAxisLabel, constraints);
			ticksTabPanel.add(ticksTabItems.yAxisLabel);

			constraints.gridy++;
			constraints.gridx = 0;
			constraints.gridwidth = 1;

			// The label of the combo box to select the tick mode.
			ticksTabItems.yModeLabel = new JLabel("Unconfigured");
			constraints.weightx = leftColumnWeight;
			constraints.insets = new Insets(topMarginInSection, leftMarginInSection, bottomMarginInSection, 0);
			layout.setConstraints(ticksTabItems.yModeLabel, constraints);
			ticksTabPanel.add(ticksTabItems.yModeLabel);

			// The label of the combo box to select the tick mode.
			ticksTabItems.yModeBox = new JComboBox<String>();
			constraints.gridx++;
			constraints.weightx = rightColumnWeight;
			constraints.insets = new Insets(topMarginInSection, 0, bottomMarginInSection, rightMargin);
			layout.setConstraints(ticksTabItems.yModeBox, constraints);
			ticksTabPanel.add(ticksTabItems.yModeBox);

			constraints.gridy++;
			constraints.gridx = 0;
			constraints.gridwidth = 2;

			// The base panel on which a panel for each mode is mounted (swappable).
			ticksTabItems.ySwappablePanel = new JPanel();
			constraints.weighty = swappablePanelWeight;
			constraints.insets = new Insets(0, leftMarginInSection, bottomMargin, rightMargin);
			layout.setConstraints(ticksTabItems.ySwappablePanel, constraints);
			ticksTabItems.ySwappablePanel.setLayout(new GridLayout(1, 1));
			ticksTabPanel.add(ticksTabItems.ySwappablePanel);

			constraints.weighty = 1.0;
		}

		// Z-Axis
		{
			constraints.gridy++;
			constraints.gridx = 0;
			constraints.gridwidth = 2;

			// The title label of the section of Y-axis.
			ticksTabItems.zAxisLabel = new JLabel("Unconfigured");
			constraints.insets = new Insets(topMargin, leftMargin, bottomMarginInSection, rightMargin);
			layout.setConstraints(ticksTabItems.zAxisLabel, constraints);
			ticksTabPanel.add(ticksTabItems.zAxisLabel);

			constraints.gridy++;
			constraints.gridx = 0;
			constraints.gridwidth = 1;

			// The label of the combo box to select the tick mode.
			ticksTabItems.zModeLabel = new JLabel("Unconfigured");
			constraints.weightx = leftColumnWeight;
			constraints.insets = new Insets(topMarginInSection, leftMarginInSection, bottomMarginInSection, 0);
			layout.setConstraints(ticksTabItems.zModeLabel, constraints);
			ticksTabPanel.add(ticksTabItems.zModeLabel);

			// The label of the combo box to select the tick mode.
			ticksTabItems.zModeBox = new JComboBox<String>();
			constraints.gridx++;
			constraints.weightx = rightColumnWeight;
			constraints.insets = new Insets(topMarginInSection, 0, bottomMarginInSection, rightMargin);
			layout.setConstraints(ticksTabItems.zModeBox, constraints);
			ticksTabPanel.add(ticksTabItems.zModeBox);

			constraints.gridy++;
			constraints.gridx = 0;
			constraints.gridwidth = 2;

			// The base panel on which a panel for each mode is mounted (swappable).
			ticksTabItems.zSwappablePanel = new JPanel();
			constraints.weighty = swappablePanelWeight;
			constraints.insets = new Insets(0, leftMarginInSection, bottomMargin, rightMargin);
			layout.setConstraints(ticksTabItems.zSwappablePanel, constraints);
			ticksTabItems.zSwappablePanel.setLayout(new GridLayout(1, 1));
			ticksTabPanel.add(ticksTabItems.zSwappablePanel);

			constraints.weighty = 1.0;
		}

		// Color-Bar
		{
			constraints.gridy++;
			constraints.gridx = 0;
			constraints.gridwidth = 2;

			// The title label of the section of the color-bar.
			ticksTabItems.colorBarLabel = new JLabel("Unconfigured");
			constraints.insets = new Insets(topMargin, leftMargin, bottomMarginInSection, rightMargin);
			layout.setConstraints(ticksTabItems.colorBarLabel, constraints);
			ticksTabPanel.add(ticksTabItems.colorBarLabel);

			constraints.gridy++;
			constraints.gridx = 0;
			constraints.gridwidth = 1;

			// The label of the combo box to select the tick mode.
			ticksTabItems.colorBarModeLabel = new JLabel("Unconfigured");
			constraints.weightx = leftColumnWeight;
			constraints.insets = new Insets(topMarginInSection, leftMarginInSection, bottomMarginInSection, 0);
			layout.setConstraints(ticksTabItems.colorBarModeLabel, constraints);
			ticksTabPanel.add(ticksTabItems.colorBarModeLabel);

			// The label of the combo box to select the tick mode.
			ticksTabItems.colorBarModeBox = new JComboBox<String>();
			constraints.gridx++;
			constraints.weightx = rightColumnWeight;
			constraints.insets = new Insets(topMarginInSection, 0, bottomMarginInSection, rightMargin);
			layout.setConstraints(ticksTabItems.colorBarModeBox, constraints);
			ticksTabPanel.add(ticksTabItems.colorBarModeBox);

			constraints.gridy++;
			constraints.gridx = 0;
			constraints.gridwidth = 2;

			// The base panel on which a panel for each mode is mounted (swappable).
			ticksTabItems.colorBarSwappablePanel = new JPanel();
			constraints.weighty = swappablePanelWeight;
			constraints.insets = new Insets(0, leftMarginInSection, bottomMargin, rightMargin);
			layout.setConstraints(ticksTabItems.colorBarSwappablePanel, constraints);
			ticksTabItems.colorBarSwappablePanel.setLayout(new GridLayout(1, 1));
			ticksTabPanel.add(ticksTabItems.colorBarSwappablePanel);

			constraints.weighty = 1.0;
		}

		// The contents for each mode.
		{
			this.createEqualDivisionPanels();
			this.createManualPanels();
			ticksTabItems.xSwappablePanel.add(ticksTabItems.xEqualDivisionItems.panel);
			ticksTabItems.ySwappablePanel.add(ticksTabItems.yEqualDivisionItems.panel);
			ticksTabItems.zSwappablePanel.add(ticksTabItems.zEqualDivisionItems.panel);
			ticksTabItems.colorBarSwappablePanel.add(ticksTabItems.colorBarEqualDivisionItems.panel);
			/*
			ticksTabItems.xSwappablePanel.add(ticksTabItems.xManualItems.panel);
			ticksTabItems.ySwappablePanel.add(ticksTabItems.yManualItems.panel);
			ticksTabItems.zSwappablePanel.add(ticksTabItems.zManualItems.panel);
			ticksTabItems.colorBarSwappablePanel.add(ticksTabItems.colorBarManualItems.panel);
			*/
		}
	}


	/**
	 * Creates the panels for EQUAL-DIVISION mode.
	 */
	private void createEqualDivisionPanels() {
		double leftColumnWeight = 0.1;
		double rightColumnWeight = 0.9;
		int topMargin = 2;
		int bottomMargin = 1;
		int leftMargin = 0;
		int rightMargin = 0;

		for (int iaxis=0; iaxis<=3; iaxis++) {
			TicksAxisEqualDivisionItems items = new TicksAxisEqualDivisionItems();
			if (iaxis == 0) {
				ticksTabItems.xEqualDivisionItems = items;
			} else if (iaxis == 1) {
				ticksTabItems.yEqualDivisionItems = items;
			} else if (iaxis == 2) {
				ticksTabItems.zEqualDivisionItems = items;
			} else if (iaxis == 3) {
				ticksTabItems.colorBarEqualDivisionItems = items;
			}
			items.panel = new JPanel();

			GridBagLayout layout = new GridBagLayout();
			items.panel.setLayout(layout);
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.gridx = 0;
			constraints.gridy = 0;
			constraints.gridwidth = 1; // CAUTION: This is NOT the total number of columns of the grid. This is the number of the cells for one component.
			constraints.gridheight = 1;
			constraints.weightx = 1.0;
			constraints.weighty = 1.0;
			constraints.fill = GridBagConstraints.BOTH;
			constraints.insets = new Insets(topMargin, leftMargin, bottomMargin, rightMargin);

			// The label of the text field of the number of divided sections in EQUAL-DIVISION mode.
			items.divisionCountLabel = new JLabel("Unconfigured");
			constraints.weightx = leftColumnWeight;
			layout.setConstraints(items.divisionCountLabel, constraints);
			items.panel.add(items.divisionCountLabel);

			// The text field of the number of divided sections in EQUAL-DIVISION mode.
			items.divisionCountField = new JTextField();
			constraints.gridx++;
			constraints.weightx = rightColumnWeight;
			layout.setConstraints(items.divisionCountField, constraints);
			items.panel.add(items.divisionCountField);

			// The empty line.
			constraints.gridy++;
			constraints.gridx = 0;
			constraints.gridwidth = 2;
			JLabel emptyLabel = new JLabel(" ");
			layout.setConstraints(emptyLabel, constraints);
			items.panel.add(emptyLabel);
		}
	}


	/**
	 * Creates the panels for MANUAL mode.
	 */
	private void createManualPanels() {
		double leftColumnWeight = 0.1;
		double rightColumnWeight = 0.9;
		int topMargin = 2;
		int bottomMargin = 1;
		int leftMargin = 0;
		int rightMargin = 0;

		for (int iaxis=0; iaxis<=3; iaxis++) {
			TicksAxisManualItems items = new TicksAxisManualItems();
			if (iaxis == 0) {
				ticksTabItems.xManualItems = items;
			} else if (iaxis == 1) {
				ticksTabItems.yManualItems = items;
			} else if (iaxis == 2) {
				ticksTabItems.zManualItems = items;
			} else if (iaxis == 3) {
				ticksTabItems.colorBarManualItems = items;
			}
			items.panel = new JPanel();

			GridBagLayout layout = new GridBagLayout();
			items.panel.setLayout(layout);
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.gridx = 0;
			constraints.gridy = 0;
			constraints.gridwidth = 1; // CAUTION: This is NOT the total number of columns of the grid. This is the number of the cells for one component.
			constraints.gridheight = 1;
			constraints.weightx = 1.0;
			constraints.weighty = 1.0;
			constraints.fill = GridBagConstraints.BOTH;
			constraints.insets = new Insets(topMargin, leftMargin, bottomMargin, rightMargin);

			// The label of the text field of the tick-coordinates.
			items.coordinatesLabel = new JLabel("Unconfigured");
			constraints.weightx = leftColumnWeight;
			layout.setConstraints(items.coordinatesLabel, constraints);
			items.panel.add(items.coordinatesLabel);

			// The text field of the tick-coordinates.
			items.coordinatesField = new JTextField();
			constraints.gridx++;
			constraints.weightx = rightColumnWeight;
			layout.setConstraints(items.coordinatesField, constraints);
			items.panel.add(items.coordinatesField);

			constraints.gridy++;
			constraints.gridx = 0;

			// The label of the text field of the tick-labels.
			items.labelsLabel = new JLabel("Unconfigured");
			constraints.weightx = leftColumnWeight;
			layout.setConstraints(items.labelsLabel, constraints);
			items.panel.add(items.labelsLabel);

			// The text field of the tick-labels.
			items.labelsField = new JTextField();
			constraints.gridx++;
			constraints.weightx = rightColumnWeight;
			layout.setConstraints(items.labelsField, constraints);
			items.panel.add(items.labelsField);
		}
	}


	/**
	 * Creates/mounts the contents on "Formats" tab.
	 */
	private void mountFormatsTabContents() {
		formatsTabItems = new FormatsTabItems();

		// Prepare the layout manager and resources.
		GridBagLayout layout = new GridBagLayout();
		formatsTabPanel.setLayout(layout);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.fill = GridBagConstraints.BOTH;

		// Define margines.
		int topMargin = 2;
		int bottomMargin = 2;
		int leftMargin = 8;
		int rightMargin = 5;
		int leftMarginInSection = 20;
		int topMarginLong = 20;
		double leftColumnWeight = 0.1;
		double rightColumnWeight = 0.9;

		// CAUTION: Do not modify the bottom-margin of the components at the end of each section,
		//   otherwise the center of the label of such components will shift downward.
		//   Instead, modify the top-margin of the header of each section.

		// X-Axis
		{
			constraints.gridy = 0;
			constraints.gridx = 0;
			constraints.gridwidth = 2;

			// The title label of the section of X-axis.
			formatsTabItems.xAxisLabel = new JLabel("Unconfigured");
			constraints.insets = new Insets(topMargin, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(formatsTabItems.xAxisLabel, constraints);
			formatsTabPanel.add(formatsTabItems.xAxisLabel);

			constraints.gridy++;
			constraints.gridx = 0;
			constraints.gridwidth = 2;

			// The checkbox to ON/OFF the auto-formatting of X-ticks.
			formatsTabItems.xAutoBox = new JCheckBox("Unconfigured");
			constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMargin, rightMargin);
			layout.setConstraints(formatsTabItems.xAutoBox, constraints);
			formatsTabPanel.add(formatsTabItems.xAutoBox);

			constraints.gridy++;
			constraints.gridx = 0;
			constraints.gridwidth = 1;

			// The label of the text field of the medium-range format of X-ticks.
			formatsTabItems.xMediumFormatLabel = new JLabel("Unconfigured");
			constraints.weightx = leftColumnWeight;
			constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMargin, 0);
			layout.setConstraints(formatsTabItems.xMediumFormatLabel, constraints);
			formatsTabPanel.add(formatsTabItems.xMediumFormatLabel);

			// The text field of the medium-range format of X-ticks.
			formatsTabItems.xMediumFormatField = new JTextField();
			constraints.gridx++;
			constraints.weightx = rightColumnWeight;
			constraints.insets = new Insets(topMargin, 0, bottomMargin, rightMargin);
			layout.setConstraints(formatsTabItems.xMediumFormatField, constraints);
			formatsTabPanel.add(formatsTabItems.xMediumFormatField);

			constraints.gridy++;
			constraints.gridx = 0;
			constraints.gridwidth = 1;

			// The label of the text field of the short-range format of X-ticks.
			formatsTabItems.xShortFormatLabel = new JLabel("Unconfigured");
			constraints.weightx = leftColumnWeight;
			constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMargin, 0);
			layout.setConstraints(formatsTabItems.xShortFormatLabel, constraints);
			formatsTabPanel.add(formatsTabItems.xShortFormatLabel);

			// The text field of the short-range format of X-ticks.
			formatsTabItems.xShortFormatField = new JTextField();
			constraints.gridx++;
			constraints.weightx = rightColumnWeight;
			constraints.insets = new Insets(topMargin, 0, bottomMargin, rightMargin);
			layout.setConstraints(formatsTabItems.xShortFormatField, constraints);
			formatsTabPanel.add(formatsTabItems.xShortFormatField);

			constraints.gridy++;
			constraints.gridx = 0;
			constraints.gridwidth = 1;

			// The label of the text field of the long-range format of X-ticks.
			formatsTabItems.xLongFormatLabel = new JLabel("Unconfigured");
			constraints.weightx = leftColumnWeight;
			constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMargin, 0);
			layout.setConstraints(formatsTabItems.xLongFormatLabel, constraints);
			formatsTabPanel.add(formatsTabItems.xLongFormatLabel);

			// The text field of the long-range format of X-ticks.
			formatsTabItems.xLongFormatField = new JTextField();
			constraints.gridx++;
			constraints.weightx = rightColumnWeight;
			constraints.insets = new Insets(topMargin, 0, bottomMargin, rightMargin);
			layout.setConstraints(formatsTabItems.xLongFormatField, constraints);
			formatsTabPanel.add(formatsTabItems.xLongFormatField);
		}

		// Y-Axis
		{
			constraints.gridy++;
			constraints.gridx = 0;
			constraints.gridwidth = 2;

			// The title label of the section of Y-axis.
			formatsTabItems.yAxisLabel = new JLabel("Unconfigured");
			constraints.insets = new Insets(topMarginLong, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(formatsTabItems.yAxisLabel, constraints);
			formatsTabPanel.add(formatsTabItems.yAxisLabel);

			constraints.gridy++;
			constraints.gridx = 0;
			constraints.gridwidth = 2;

			// The checkbox to ON/OFF the auto-formatting of Y-ticks.
			formatsTabItems.yAutoBox = new JCheckBox("Unconfigured");
			constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMargin, rightMargin);
			layout.setConstraints(formatsTabItems.yAutoBox, constraints);
			formatsTabPanel.add(formatsTabItems.yAutoBox);

			constraints.gridy++;
			constraints.gridx = 0;
			constraints.gridwidth = 1;

			// The label of the text field of the medium-range format of Y-ticks.
			formatsTabItems.yMediumFormatLabel = new JLabel("Unconfigured");
			constraints.weightx = leftColumnWeight;
			constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMargin, 0);
			layout.setConstraints(formatsTabItems.yMediumFormatLabel, constraints);
			formatsTabPanel.add(formatsTabItems.yMediumFormatLabel);

			// The text field of the medium-range format of Y-ticks.
			formatsTabItems.yMediumFormatField = new JTextField();
			constraints.gridx++;
			constraints.weightx = rightColumnWeight;
			constraints.insets = new Insets(topMargin, 0, bottomMargin, rightMargin);
			layout.setConstraints(formatsTabItems.yMediumFormatField, constraints);
			formatsTabPanel.add(formatsTabItems.yMediumFormatField);

			constraints.gridy++;
			constraints.gridx = 0;
			constraints.gridwidth = 1;

			// The label of the text field of the short-range format of Y-ticks.
			formatsTabItems.yShortFormatLabel = new JLabel("Unconfigured");
			constraints.weightx = leftColumnWeight;
			constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMargin, 0);
			layout.setConstraints(formatsTabItems.yShortFormatLabel, constraints);
			formatsTabPanel.add(formatsTabItems.yShortFormatLabel);

			// The text field of the short-range format of Y-ticks.
			formatsTabItems.yShortFormatField = new JTextField();
			constraints.gridx++;
			constraints.weightx = rightColumnWeight;
			constraints.insets = new Insets(topMargin, 0, bottomMargin, rightMargin);
			layout.setConstraints(formatsTabItems.yShortFormatField, constraints);
			formatsTabPanel.add(formatsTabItems.yShortFormatField);

			constraints.gridy++;
			constraints.gridx = 0;
			constraints.gridwidth = 1;

			// The label of the text field of the long-range format of Y-ticks.
			formatsTabItems.yLongFormatLabel = new JLabel("Unconfigured");
			constraints.weightx = leftColumnWeight;
			constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMargin, 0);
			layout.setConstraints(formatsTabItems.yLongFormatLabel, constraints);
			formatsTabPanel.add(formatsTabItems.yLongFormatLabel);

			// The text field of the long-range format of Y-ticks.
			formatsTabItems.yLongFormatField = new JTextField();
			constraints.gridx++;
			constraints.weightx = rightColumnWeight;
			constraints.insets = new Insets(topMargin, 0, bottomMargin, rightMargin);
			layout.setConstraints(formatsTabItems.yLongFormatField, constraints);
			formatsTabPanel.add(formatsTabItems.yLongFormatField);

			constraints.gridy++;
			constraints.gridx = 0;
			constraints.gridwidth = 2;
		}

		// Z-Axis
		{
			constraints.gridy++;
			constraints.gridx = 0;
			constraints.gridwidth = 2;

			// The title label of the section of Z-axis.
			formatsTabItems.zAxisLabel = new JLabel("Unconfigured");
			constraints.insets = new Insets(topMarginLong, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(formatsTabItems.zAxisLabel, constraints);
			formatsTabPanel.add(formatsTabItems.zAxisLabel);

			constraints.gridy++;
			constraints.gridx = 0;
			constraints.gridwidth = 2;

			// The checkbox to ON/OFF the auto-formatting of Z-ticks.
			formatsTabItems.zAutoBox = new JCheckBox("Unconfigured");
			constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMargin, rightMargin);
			layout.setConstraints(formatsTabItems.zAutoBox, constraints);
			formatsTabPanel.add(formatsTabItems.zAutoBox);

			constraints.gridy++;
			constraints.gridx = 0;
			constraints.gridwidth = 1;

			// The label of the text field of the medium-range format of Z-ticks.
			formatsTabItems.zMediumFormatLabel = new JLabel("Unconfigured");
			constraints.weightx = leftColumnWeight;
			constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMargin, 0);
			layout.setConstraints(formatsTabItems.zMediumFormatLabel, constraints);
			formatsTabPanel.add(formatsTabItems.zMediumFormatLabel);

			// The text field of the medium-range format of Z-ticks.
			formatsTabItems.zMediumFormatField = new JTextField();
			constraints.gridx++;
			constraints.weightx = rightColumnWeight;
			constraints.insets = new Insets(topMargin, 0, bottomMargin, rightMargin);
			layout.setConstraints(formatsTabItems.zMediumFormatField, constraints);
			formatsTabPanel.add(formatsTabItems.zMediumFormatField);

			constraints.gridy++;
			constraints.gridx = 0;
			constraints.gridwidth = 1;

			// The label of the text field of the short-range format of Z-ticks.
			formatsTabItems.zShortFormatLabel = new JLabel("Unconfigured");
			constraints.weightx = leftColumnWeight;
			constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMargin, 0);
			layout.setConstraints(formatsTabItems.zShortFormatLabel, constraints);
			formatsTabPanel.add(formatsTabItems.zShortFormatLabel);

			// The text field of the short-range format of Z-ticks.
			formatsTabItems.zShortFormatField = new JTextField();
			constraints.gridx++;
			constraints.weightx = rightColumnWeight;
			constraints.insets = new Insets(topMargin, 0, bottomMargin, rightMargin);
			layout.setConstraints(formatsTabItems.zShortFormatField, constraints);
			formatsTabPanel.add(formatsTabItems.zShortFormatField);

			constraints.gridy++;
			constraints.gridx = 0;
			constraints.gridwidth = 1;

			// The label of the text field of the long-range format of Z-ticks.
			formatsTabItems.zLongFormatLabel = new JLabel("Unconfigured");
			constraints.weightx = leftColumnWeight;
			constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMargin, 0);
			layout.setConstraints(formatsTabItems.zLongFormatLabel, constraints);
			formatsTabPanel.add(formatsTabItems.zLongFormatLabel);

			// The text field of the long-range format of Z-ticks.
			formatsTabItems.zLongFormatField = new JTextField();
			constraints.gridx++;
			constraints.weightx = rightColumnWeight;
			constraints.insets = new Insets(topMargin, 0, bottomMargin, rightMargin);
			layout.setConstraints(formatsTabItems.zLongFormatField, constraints);
			formatsTabPanel.add(formatsTabItems.zLongFormatField);

			constraints.gridy++;
			constraints.gridx = 0;
			constraints.gridwidth = 2;
		}

		// Empty Line
		//   CAUTION: Do not modify the bottom-margin of the above components,
		//   otherwise the center of the label of the above components will shift downward.
		{
			constraints.gridy++;
			constraints.gridx = 0;
			constraints.gridwidth = 2;
			constraints.weighty = 0.01;

			JLabel emptyLabel = new JLabel(" ");
			constraints.insets = new Insets(0, 0, 0, 0);
			layout.setConstraints(emptyLabel, constraints);
			formatsTabPanel.add(emptyLabel);
		}
	}


	/**
	 * Creates/mounts the contents on "Visibilities" tab.
	 */
	private void mountVisibilitiesTabContents() {
		visibilitiesTabItems = new VisibilitiesTabItems();

		// Prepare the layout manager and resources.
		GridBagLayout layout = new GridBagLayout();
		visibilitiesTabPanel.setLayout(layout);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.fill = GridBagConstraints.BOTH;

		// Define margines.
		int topMargin = 2;
		int bottomMargin = 2;
		int leftMargin = 8;
		int rightMargin = 5;
		int leftMarginInSection = 20;
		int leftMarginInSectionLong = 40;
		int topMarginLong = 15;
		int topMarginHead = 10;

		// CAUTION: Do not modify the bottom-margin of the components at the end of each section,
		//   otherwise the center of the label of such components will shift downward.
		//   Instead, modify the top-margin of the header of each section.

		// X-Axis
		{
			constraints.gridy = 0;

			// The title label of the section of X-axis.
			visibilitiesTabItems.xAxisLabel = new JLabel("Unconfigured");
			constraints.insets = new Insets(topMarginHead, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(visibilitiesTabItems.xAxisLabel, constraints);
			visibilitiesTabPanel.add(visibilitiesTabItems.xAxisLabel);

			constraints.gridy++;

			// The checkbox to ON/OFF the auto-controlling the visibilities of X-ticks.
			visibilitiesTabItems.xAutoBox = new JCheckBox("Unconfigured");
			constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMargin, rightMargin);
			layout.setConstraints(visibilitiesTabItems.xAutoBox, constraints);
			visibilitiesTabPanel.add(visibilitiesTabItems.xAutoBox);

			constraints.gridy++;

			// The checkbox to ON/OFF the visibilities of X-ticks A.
			visibilitiesTabItems.xABox = new JCheckBox("Unconfigured");
			constraints.insets = new Insets(topMargin, leftMarginInSectionLong, bottomMargin, rightMargin);
			layout.setConstraints(visibilitiesTabItems.xABox, constraints);
			visibilitiesTabPanel.add(visibilitiesTabItems.xABox);

			constraints.gridy++;

			// The checkbox to ON/OFF the visibilities of X-ticks B.
			visibilitiesTabItems.xBBox = new JCheckBox("Unconfigured");
			constraints.insets = new Insets(topMargin, leftMarginInSectionLong, bottomMargin, rightMargin);
			layout.setConstraints(visibilitiesTabItems.xBBox, constraints);
			visibilitiesTabPanel.add(visibilitiesTabItems.xBBox);

			constraints.gridy++;

			// The checkbox to ON/OFF the visibilities of X-ticks C.
			visibilitiesTabItems.xCBox = new JCheckBox("Unconfigured");
			constraints.insets = new Insets(topMargin, leftMarginInSectionLong, bottomMargin, rightMargin);
			layout.setConstraints(visibilitiesTabItems.xCBox, constraints);
			visibilitiesTabPanel.add(visibilitiesTabItems.xCBox);

			constraints.gridy++;

			// The checkbox to ON/OFF the visibilities of X-ticks D.
			visibilitiesTabItems.xDBox = new JCheckBox("Unconfigured");
			constraints.insets = new Insets(topMargin, leftMarginInSectionLong, bottomMargin, rightMargin);
			layout.setConstraints(visibilitiesTabItems.xDBox, constraints);
			visibilitiesTabPanel.add(visibilitiesTabItems.xDBox);
		}

		// Y-Axis
		{
			constraints.gridy++;

			// The title label of the section of Y-axis.
			visibilitiesTabItems.yAxisLabel = new JLabel("Unconfigured");
			constraints.insets = new Insets(topMarginLong, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(visibilitiesTabItems.yAxisLabel, constraints);
			visibilitiesTabPanel.add(visibilitiesTabItems.yAxisLabel);

			constraints.gridy++;

			// The checkbox to ON/OFF the auto-controlling the visibilities of Y-ticks.
			visibilitiesTabItems.yAutoBox = new JCheckBox("Unconfigured");
			constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMargin, rightMargin);
			layout.setConstraints(visibilitiesTabItems.yAutoBox, constraints);
			visibilitiesTabPanel.add(visibilitiesTabItems.yAutoBox);

			constraints.gridy++;

			// The checkbox to ON/OFF the visibilities of Y-ticks A.
			visibilitiesTabItems.yABox = new JCheckBox("Unconfigured");
			constraints.insets = new Insets(topMargin, leftMarginInSectionLong, bottomMargin, rightMargin);
			layout.setConstraints(visibilitiesTabItems.yABox, constraints);
			visibilitiesTabPanel.add(visibilitiesTabItems.yABox);

			constraints.gridy++;

			// The checkbox to ON/OFF the visibilities of Y-ticks B.
			visibilitiesTabItems.yBBox = new JCheckBox("Unconfigured");
			constraints.insets = new Insets(topMargin, leftMarginInSectionLong, bottomMargin, rightMargin);
			layout.setConstraints(visibilitiesTabItems.yBBox, constraints);
			visibilitiesTabPanel.add(visibilitiesTabItems.yBBox);

			constraints.gridy++;

			// The checkbox to ON/OFF the visibilities of Y-ticks C.
			visibilitiesTabItems.yCBox = new JCheckBox("Unconfigured");
			constraints.insets = new Insets(topMargin, leftMarginInSectionLong, bottomMargin, rightMargin);
			layout.setConstraints(visibilitiesTabItems.yCBox, constraints);
			visibilitiesTabPanel.add(visibilitiesTabItems.yCBox);

			constraints.gridy++;

			// The checkbox to ON/OFF the visibilities of Y-ticks D.
			visibilitiesTabItems.yDBox = new JCheckBox("Unconfigured");
			constraints.insets = new Insets(topMargin, leftMarginInSectionLong, bottomMargin, rightMargin);
			layout.setConstraints(visibilitiesTabItems.yDBox, constraints);
			visibilitiesTabPanel.add(visibilitiesTabItems.yDBox);
		}

		// Z-Axis
		{
			constraints.gridy++;

			// The title label of the section of Z-axis.
			visibilitiesTabItems.zAxisLabel = new JLabel("Unconfigured");
			constraints.insets = new Insets(topMarginLong, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(visibilitiesTabItems.zAxisLabel, constraints);
			visibilitiesTabPanel.add(visibilitiesTabItems.zAxisLabel);

			constraints.gridy++;

			// The checkbox to ON/OFF the auto-controlling the visibilities of Z-ticks.
			visibilitiesTabItems.zAutoBox = new JCheckBox("Unconfigured");
			constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMargin, rightMargin);
			layout.setConstraints(visibilitiesTabItems.zAutoBox, constraints);
			visibilitiesTabPanel.add(visibilitiesTabItems.zAutoBox);

			constraints.gridy++;

			// The checkbox to ON/OFF the visibilities of Z-ticks A.
			visibilitiesTabItems.zABox = new JCheckBox("Unconfigured");
			constraints.insets = new Insets(topMargin, leftMarginInSectionLong, bottomMargin, rightMargin);
			layout.setConstraints(visibilitiesTabItems.zABox, constraints);
			visibilitiesTabPanel.add(visibilitiesTabItems.zABox);

			constraints.gridy++;

			// The checkbox to ON/OFF the visibilities of Z-ticks B.
			visibilitiesTabItems.zBBox = new JCheckBox("Unconfigured");
			constraints.insets = new Insets(topMargin, leftMarginInSectionLong, bottomMargin, rightMargin);
			layout.setConstraints(visibilitiesTabItems.zBBox, constraints);
			visibilitiesTabPanel.add(visibilitiesTabItems.zBBox);

			constraints.gridy++;

			// The checkbox to ON/OFF the visibilities of Z-ticks C.
			visibilitiesTabItems.zCBox = new JCheckBox("Unconfigured");
			constraints.insets = new Insets(topMargin, leftMarginInSectionLong, bottomMargin, rightMargin);
			layout.setConstraints(visibilitiesTabItems.zCBox, constraints);
			visibilitiesTabPanel.add(visibilitiesTabItems.zCBox);

			constraints.gridy++;

			// The checkbox to ON/OFF the visibilities of Z-ticks D.
			visibilitiesTabItems.zDBox = new JCheckBox("Unconfigured");
			constraints.insets = new Insets(topMargin, leftMarginInSectionLong, bottomMargin, rightMargin);
			layout.setConstraints(visibilitiesTabItems.zDBox, constraints);
			visibilitiesTabPanel.add(visibilitiesTabItems.zDBox);
		}

		// Empty Line
		//   CAUTION: Do not modify the bottom-margin of the above components,
		//   otherwise the center of the label of the above components will shift downward.
		{
			constraints.gridy++;
			constraints.gridx = 0;
			constraints.weighty = 0.01;

			JLabel emptyLabel = new JLabel(" ");
			constraints.insets = new Insets(0, 0, 0, 0);
			layout.setConstraints(emptyLabel, constraints);
			visibilitiesTabPanel.add(emptyLabel);
		}
	}


	/**
	 * Creates/mounts the contents on "Images" tab.
	 */
	private void mountImagesTabContents() {
		imagesTabItems = new ImagesTabItems();
		this.createImagesPlanePanels();

		// Prepare the layout manager and resources.
		GridBagLayout layout = new GridBagLayout();
		imagesTabPanel.setLayout(layout);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.fill = GridBagConstraints.BOTH;

		// Define margines.
		int topMargin = 2;
		int bottomMargin = 2;
		int leftMargin = 8;
		int rightMargin = 5;
		int leftMarginInSection = 20;
		int bottomMarginOfEnd = 8;
		int topMarginHead = 5;
		double labelWeight = 1.0;
		double panelWeight = 8.0;

		// X-Y Plane (Floor, Z-min side)
		{
			constraints.gridy = 0;

			// The title label of the section of this Plane.
			imagesTabItems.xyFloorPlaneLabel = new JLabel("Unconfigured");
			constraints.weighty = labelWeight;
			constraints.insets = new Insets(topMarginHead, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(imagesTabItems.xyFloorPlaneLabel, constraints);
			imagesTabPanel.add(imagesTabItems.xyFloorPlaneLabel);

			constraints.gridy++;

			// The panel on which the setting items for this Plane.
			constraints.weighty = panelWeight;
			constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMargin, rightMargin);
			layout.setConstraints(imagesTabItems.xyFloorPlaneItems.panel, constraints);
			imagesTabPanel.add(imagesTabItems.xyFloorPlaneItems.panel);
		}

		// X-Y Plane (Ceil, Z-max side)
		{
			constraints.gridy++;

			// The title label of the section of this Plane.
			imagesTabItems.xyCeilPlaneLabel = new JLabel("Unconfigured");
			constraints.weighty = labelWeight;
			constraints.insets = new Insets(topMarginHead, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(imagesTabItems.xyCeilPlaneLabel, constraints);
			imagesTabPanel.add(imagesTabItems.xyCeilPlaneLabel);

			constraints.gridy++;

			// The panel on which the setting items for this Plane.
			constraints.weighty = panelWeight;
			constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMargin, rightMargin);
			layout.setConstraints(imagesTabItems.xyCeilPlaneItems.panel, constraints);
			imagesTabPanel.add(imagesTabItems.xyCeilPlaneItems.panel);
		}

		// Y-Z Plane (Floor, X-min side)
		{
			constraints.gridy++;

			// The title label of the section of this Plane.
			imagesTabItems.yzFloorPlaneLabel = new JLabel("Unconfigured");
			constraints.weighty = labelWeight;
			constraints.insets = new Insets(topMarginHead, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(imagesTabItems.yzFloorPlaneLabel, constraints);
			imagesTabPanel.add(imagesTabItems.yzFloorPlaneLabel);

			constraints.gridy++;

			// The panel on which the setting items for this Plane.
			constraints.weighty = panelWeight;
			constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMargin, rightMargin);
			layout.setConstraints(imagesTabItems.yzFloorPlaneItems.panel, constraints);
			imagesTabPanel.add(imagesTabItems.yzFloorPlaneItems.panel);
		}

		// Y-Z Plane (Ceil, X-max side)
		{
			constraints.gridy++;

			// The title label of the section of this Plane.
			imagesTabItems.yzCeilPlaneLabel = new JLabel("Unconfigured");
			constraints.weighty = labelWeight;
			constraints.insets = new Insets(topMarginHead, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(imagesTabItems.yzCeilPlaneLabel, constraints);
			imagesTabPanel.add(imagesTabItems.yzCeilPlaneLabel);

			constraints.gridy++;

			// The panel on which the setting items for this Plane.
			constraints.weighty = panelWeight;
			constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMargin, rightMargin);
			layout.setConstraints(imagesTabItems.yzCeilPlaneItems.panel, constraints);
			imagesTabPanel.add(imagesTabItems.yzCeilPlaneItems.panel);
		}

		// Z-X Plane (Floor, Y-min side)
		{
			constraints.gridy++;

			// The title label of the section of this Plane.
			constraints.weighty = labelWeight;
			imagesTabItems.zxFloorPlaneLabel = new JLabel("Unconfigured");
			constraints.insets = new Insets(topMarginHead, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(imagesTabItems.zxFloorPlaneLabel, constraints);
			imagesTabPanel.add(imagesTabItems.zxFloorPlaneLabel);

			constraints.gridy++;

			// The panel on which the setting items for this Plane.
			constraints.weighty = panelWeight;
			constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMargin, rightMargin);
			layout.setConstraints(imagesTabItems.zxFloorPlaneItems.panel, constraints);
			imagesTabPanel.add(imagesTabItems.zxFloorPlaneItems.panel);
		}

		// Z-X Plane (Ceil, Y-max side)
		{
			constraints.gridy++;

			// The title label of the section of this Plane.
			imagesTabItems.zxCeilPlaneLabel = new JLabel("Unconfigured");
			constraints.weighty = labelWeight;
			constraints.insets = new Insets(topMarginHead, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(imagesTabItems.zxCeilPlaneLabel, constraints);
			imagesTabPanel.add(imagesTabItems.zxCeilPlaneLabel);

			constraints.gridy++;

			// The panel on which the setting items for this Plane.
			constraints.weighty = panelWeight;
			constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMargin, rightMargin);
			layout.setConstraints(imagesTabItems.zxCeilPlaneItems.panel, constraints);
			imagesTabPanel.add(imagesTabItems.zxCeilPlaneItems.panel);
		}

		// The checkbox to draw images only inside of the graph.
		{
			constraints.gridy++;

			imagesTabItems.insideBox = new JCheckBox();
			constraints.weighty = labelWeight;
			constraints.insets = new Insets(topMarginHead, leftMargin, bottomMarginOfEnd, rightMargin);
			layout.setConstraints(imagesTabItems.insideBox, constraints);
			imagesTabPanel.add(imagesTabItems.insideBox);
		}

	}


	/**
	 * Creates/mounts the contents on each plane's panel on "Images" tab.
	 */
	private void createImagesPlanePanels() {
		for (int iplane=0; iplane<=5; iplane++) {
			ImagesPlaneItems items = new ImagesPlaneItems();
			if (iplane == 0) {
				imagesTabItems.xyFloorPlaneItems = items;
			} else if (iplane == 1) {
				imagesTabItems.xyCeilPlaneItems = items;
			} else if (iplane == 2) {
				imagesTabItems.yzFloorPlaneItems = items;
			} else if (iplane == 3) {
				imagesTabItems.yzCeilPlaneItems = items;
			} else if (iplane == 4) {
				imagesTabItems.zxFloorPlaneItems = items;
			} else if (iplane == 5) {
				imagesTabItems.zxCeilPlaneItems = items;
			}
			items.panel = new JPanel();

			// Prepare the layout manager and resources.
			GridBagLayout layout = new GridBagLayout();
			items.panel.setLayout(layout);
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.gridx = 0;
			constraints.gridy = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			constraints.weightx = 1.0;
			constraints.weighty = 1.0;
			constraints.fill = GridBagConstraints.BOTH;

			int topMarginOfLowerLine = 5;
			int bottomMarginOfLowerLine = 5;

			// The text field of the image file path.
			constraints.gridwidth = 6;
			items.filePathField = new JTextField();
			constraints.insets = new Insets(0, 0, 0, 0);
			layout.setConstraints(items.filePathField, constraints);
			items.panel.add(items.filePathField);

			constraints.gridx = constraints.gridwidth;

			// The button to open the image file.
			constraints.gridwidth = 2;
			items.openFileButton = new JButton();
			constraints.insets = new Insets(0, 0, 0, 0);
			layout.setConstraints(items.openFileButton, constraints);
			items.panel.add(items.openFileButton);

			constraints.gridx = 0;
			constraints.gridy++;
			constraints.weightx = 0.0;

			// The label of "("
			constraints.gridwidth = 1;
			JLabel openParenLabel = new JLabel(" ( ", JLabel.RIGHT);
			constraints.insets = new Insets(topMarginOfLowerLine, 0, bottomMarginOfLowerLine, 0);
			layout.setConstraints(openParenLabel, constraints);
			items.panel.add(openParenLabel);

			constraints.gridx++;
			constraints.weightx = 1.0;

			// The text field of the image width (pixels) displayed on the graph.
			constraints.gridwidth = 1;
			items.imageWidthField = new JTextField();
			constraints.insets = new Insets(topMarginOfLowerLine, 0, bottomMarginOfLowerLine, 0);
			layout.setConstraints(items.imageWidthField, constraints);
			items.panel.add(items.imageWidthField);

			constraints.gridx++;
			constraints.weightx = 0.0;

			// The label of "x"
			constraints.gridwidth = 1;
			JLabel timesLabel = new JLabel(" x ", JLabel.CENTER);
			constraints.insets = new Insets(topMarginOfLowerLine, 0, bottomMarginOfLowerLine, 0);
			layout.setConstraints(timesLabel, constraints);
			items.panel.add(timesLabel);

			constraints.gridx++;
			constraints.weightx = 1.0;

			// The text field of the image height (pixels) displayed on the graph.
			constraints.gridwidth = 1;
			items.imageHeightField = new JTextField();
			constraints.insets = new Insets(topMarginOfLowerLine, 0, bottomMarginOfLowerLine, 0);
			layout.setConstraints(items.imageHeightField, constraints);
			items.panel.add(items.imageHeightField);

			constraints.gridx++;

			// The label of "px)"
			constraints.gridwidth = 1;
			JLabel closeParenLabel = new JLabel(" px ) ");
			constraints.insets = new Insets(topMarginOfLowerLine, 0, bottomMarginOfLowerLine, 0);
			layout.setConstraints(closeParenLabel, constraints);
			items.panel.add(closeParenLabel);
		}
	}
}
