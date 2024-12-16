package com.rinearn.graph3d.view;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.FontConfiguration;
import com.rinearn.graph3d.config.ScaleConfiguration;
import com.rinearn.graph3d.config.FrameConfiguration;
import com.rinearn.graph3d.config.scale.NumericTickLabelFormatter;
import com.rinearn.graph3d.config.scale.ManualTicker;
import com.rinearn.graph3d.config.scale.EqualDivisionTicker;

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

import java.text.NumberFormat;
import java.text.DecimalFormat;

import java.io.File;
import java.math.BigDecimal;

import java.lang.reflect.InvocationTargetException;


/**
 * The window of "Set Ranges" menu.
 */
public class ScaleSettingWindow {

	/** The default width [px] of this window. */
	public static final int DEFAULT_WINDOW_WIDTH = 400;

	/** The default height [px] of this window. */
	public static final int DEFAULT_WINDOW_HEIGHT = 680;

	/** The displayed name of the TickerMode.MANUAL on UI in Japanese. */
	private static final String TICKER_MODE_MANUAL_JA = "手動";

	/** The displayed name of the TickerMode.MANUAL on UI in English. */
	private static final String TICKER_MODE_MANUAL_EN = "MANUAL";

	/** The displayed name of the TickerMode.EQUAL_DIVISION on UI in Japanese. */
	private static final String TICKER_MODE_EQUAL_DIVISION_JA = "等分割";

	/** The displayed name of the TickerMode.EQUAL_DIVISION on UI in English. */
	private static final String TICKER_MODE_EQUAL_DIVISION_EN = "EQUAL_DIVISION";

	/** The displayed name of the TickerMode.CUSTOM on UI in Japanese. */
	private static final String TICKER_MODE_CUSTOM_JA = "カスタム";

	/** The displayed name of the TickerMode.CUSTOM on UI in English. */
	private static final String TICKER_MODE_CUSTOM_EN = "CUSTOM";

	/** The displayed name of the ShapeMode.BOX on UI in Japanese. */
	private static final String SHAPE_MODE_BOX_JA = "箱型";

	/** The displayed name of the ShapeMode.BOX on UI in English. */
	private static final String SHAPE_MODE_BOX_EN = "BOX";

	/** The displayed name of the ShapeMode.BACKWALL on UI in Japanese. */
	private static final String SHAPE_MODE_BACKWALL_JA = "背面のみ";

	/** The displayed name of the ShapeMode.BACKWALL on UI in English. */
	private static final String SHAPE_MODE_BACKWALL_EN = "BACKWALL";

	/** The displayed name of the ShapeMode.FLOOR on UI in Japanese. */
	private static final String SHAPE_MODE_FLOOR_JA = "床面のみ";

	/** The displayed name of the ShapeMode.FLOOR on UI in Japanese. */
	private static final String SHAPE_MODE_FLOOR_EN = "FLOOR";

	/** The displayed name of the ShapeMode.NONE on UI in Japanese. */
	private static final String SHAPE_MODE_NONE_JA = "なし";

	/** The displayed name of the ShapeMode.NONE on UI in Japanese. */
	private static final String SHAPE_MODE_NONE_EN = "NONE";


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
		public volatile JLabel frameShapeModeLabel;

		/** The combo box to select the type of the outer frame. */
		public volatile JComboBox<MultilingualItem> frameShapeModeBox;

		/** The item of the frame shape box: BOX. */
		public volatile MultilingualItem boxModeItem;

		/** The item of the frame shape box: BACKWALL. */
		public volatile MultilingualItem backwallModeItem;

		/** The item of the frame shape box: FLOOR. */
		public volatile MultilingualItem floorModeItem;

		/** The item of the frame shape box: NONE. */
		public volatile MultilingualItem noneModeItem;

		/** The label of the text field of the line width of the outer frame. */
		public volatile JLabel frameLineWidthLabel;

		/** The text field of the line width of the outer frame. */
		public volatile JTextField frameLineWidthField;

		/** The label of the text field of the margin between tick lines and labels. */
		public volatile JLabel tickLabelMarginLabel;

		/** The text field of the margin between tick lines and labels. */
		public volatile JTextField tickLabelMarginField;

		/** The label of the text field of the length of the tick lines. */
		public volatile JLabel tickLineLengthLabel;

		/** The text field of the length of the tick lines. */
		public volatile JTextField tickLineLengthField;

		/** The check box of the option to draw the ticks towards inside of the outer frame. */
		public volatile JCheckBox tickInwardBox;

		/**
		 * Gets the selected item of frameShapeModeBox field as an element of FrameConfiguration.ShapeMode enum.
		 *
		 * @return The ShapeMode enum element corresponding to the selected item of frameShapeModeBox.
		 */
		public FrameConfiguration.ShapeMode getSelectedFrameShapeMode() {
			String selectedItemText = this.frameShapeModeBox.getSelectedItem().toString();
			switch (selectedItemText) {
				case SHAPE_MODE_BOX_EN :
				case SHAPE_MODE_BOX_JA : {
					return FrameConfiguration.ShapeMode.BOX;
				}
				case SHAPE_MODE_BACKWALL_EN :
				case SHAPE_MODE_BACKWALL_JA : {
					return FrameConfiguration.ShapeMode.BACKWALL;
				}
				case SHAPE_MODE_FLOOR_EN :
				case SHAPE_MODE_FLOOR_JA : {
					return FrameConfiguration.ShapeMode.FLOOR;
				}
				case SHAPE_MODE_NONE_EN :
				case SHAPE_MODE_NONE_JA : {
					return FrameConfiguration.ShapeMode.NONE;
				}
				default : {
					throw new IllegalStateException("Unexpected frame shape mode: " + selectedItemText);
				}
			}
		}
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
		public volatile JComboBox<MultilingualItem> xModeBox;

		/** The combo box to select the mode of Y-ticks. */
		public volatile JComboBox<MultilingualItem> yModeBox;

		/** The combo box to select the mode of Z-ticks. */
		public volatile JComboBox<MultilingualItem> zModeBox;

		/** The combo box to select the mode of color-bar-ticks. */
		public volatile JComboBox<MultilingualItem> colorBarModeBox;

		/** The item of the X-ticks mode box: EQUAL-DIVISION. */
		public volatile MultilingualItem xEqualDivisionModeItem;

		/** The item of the Y-ticks mode box: EQUAL-DIVISION. */
		public volatile MultilingualItem yEqualDivisionModeItem;

		/** The item of the Z-ticks mode box: EQUAL-DIVISION. */
		public volatile MultilingualItem zEqualDivisionModeItem;

		/** The item of the color-bar-ticks mode box: EQUAL-DIVISION. */
		public volatile MultilingualItem colorBarEqualDivisionModeItem;

		/** The item of the X-ticks mode box: MANUAL. */
		public volatile MultilingualItem xManualModeItem;

		/** The item of the Y-ticks mode box: MANUAL. */
		public volatile MultilingualItem yManualModeItem;

		/** The item of the Z-ticks mode box: MANUAL. */
		public volatile MultilingualItem zManualModeItem;

		/** The item of the color-bar-ticks mode box: MANUAL. */
		public volatile MultilingualItem colorBarManualModeItem;

		/** The item of the X-ticks mode box: CUSTOM. */
		public volatile MultilingualItem xCustomModeItem;

		/** The item of the Y-ticks mode box: CUSTOM. */
		public volatile MultilingualItem yCustomModeItem;

		/** The item of the Z-ticks mode box: CUSTOM. */
		public volatile MultilingualItem zCustomModeItem;

		/** The item of the color-bar-ticks mode box: CUSTOM. */
		public volatile MultilingualItem colorBarCustomModeItem;

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

		/** The label of the X-Y plane (Lower, Z-min side) section. */
		public volatile JLabel xyLowerPlaneLabel;

		/** The container of the setting items of the X-Y plane (Lower, Z-min side) section. */
		public volatile ImagesPlaneItems xyLowerPlaneItems;

		/** The label of the X-Y plane (Upper, Z-max side) section. */
		public volatile JLabel xyUpperPlaneLabel;

		/** The container of the setting items of the X-Y plane (Upper, Z-max side) section. */
		public volatile ImagesPlaneItems xyUpperPlaneItems;


		/** The label of the Y-Z plane (Lower, X-min side) section. */
		public volatile JLabel yzLowerPlaneLabel;

		/** The container of the setting items of the Y-Z plane (Lower, X-min side) section. */
		public volatile ImagesPlaneItems yzLowerPlaneItems;

		/** The label of the Y-Z plane (Upper, X-max side) section. */
		public volatile JLabel yzUpperPlaneLabel;

		/** The container of the setting items of the Y-Z plane (Upper, X-max side) section. */
		public volatile ImagesPlaneItems yzUpperPlaneItems;


		/** The label of the Z-X plane (Lower, Y-min side) section. */
		public volatile JLabel zxLowerPlaneLabel;

		/** The container of the setting items of the Z-X plane (Lower, Y-min side) section. */
		public volatile ImagesPlaneItems zxLowerPlaneItems;

		/** The label of the Z-X plane (Upper, Y-max side) section. */
		public volatile JLabel zxUpperPlaneLabel;

		/** The container of the setting items of the Z-X plane (Upper, Y-max side) section. */
		public volatile ImagesPlaneItems zxUpperPlaneItems;

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
				designTabItems.frameShapeModeLabel.setText("フレーム:");
				{
					designTabItems.boxModeItem.setText(SHAPE_MODE_BOX_JA);
					designTabItems.backwallModeItem.setText(SHAPE_MODE_BACKWALL_JA);
					designTabItems.floorModeItem.setText(SHAPE_MODE_FLOOR_JA);
					designTabItems.noneModeItem.setText(SHAPE_MODE_NONE_JA);;
				}
				designTabItems.frameLineWidthLabel.setText("フレーム線の幅:");
				designTabItems.tickLabelMarginLabel.setText("目盛り数値の余白:");
				designTabItems.tickLineLengthLabel.setText("目盛り線の長さ:");
				designTabItems.tickInwardBox.setText("目盛りを内向きに描画");
			}

			tabbedPane.setTitleAt(1, "刻み");
			{
				ticksTabItems.xAxisLabel.setText("- X軸 -");
				ticksTabItems.xModeLabel.setText("モード: ");
				ticksTabItems.xEqualDivisionModeItem.setText(TICKER_MODE_EQUAL_DIVISION_JA);
				ticksTabItems.xManualModeItem.setText(TICKER_MODE_MANUAL_JA);
				ticksTabItems.xCustomModeItem.setText(TICKER_MODE_CUSTOM_JA);
				ticksTabItems.xEqualDivisionItems.divisionCountLabel.setText("区間の数: ");
				ticksTabItems.xManualItems.coordinatesLabel.setText("位置: ");
				ticksTabItems.xManualItems.labelsLabel.setText("表記: ");

				ticksTabItems.yAxisLabel.setText("- Y軸 -");
				ticksTabItems.yModeLabel.setText("モード: ");
				ticksTabItems.yEqualDivisionModeItem.setText(TICKER_MODE_EQUAL_DIVISION_JA);
				ticksTabItems.yManualModeItem.setText(TICKER_MODE_MANUAL_JA);
				ticksTabItems.yCustomModeItem.setText(TICKER_MODE_CUSTOM_JA);
				ticksTabItems.yEqualDivisionItems.divisionCountLabel.setText("区間の数: ");
				ticksTabItems.yManualItems.coordinatesLabel.setText("位置: ");
				ticksTabItems.yManualItems.labelsLabel.setText("表記: ");

				ticksTabItems.zAxisLabel.setText("- Z軸 -");
				ticksTabItems.zModeLabel.setText("モード: ");
				ticksTabItems.zEqualDivisionModeItem.setText(TICKER_MODE_EQUAL_DIVISION_JA);
				ticksTabItems.zManualModeItem.setText(TICKER_MODE_MANUAL_JA);
				ticksTabItems.zCustomModeItem.setText(TICKER_MODE_CUSTOM_JA);
				ticksTabItems.zEqualDivisionItems.divisionCountLabel.setText("区間の数: ");
				ticksTabItems.zManualItems.coordinatesLabel.setText("位置: ");
				ticksTabItems.zManualItems.labelsLabel.setText("表記: ");

				ticksTabItems.colorBarLabel.setText("- カラーバー -");
				ticksTabItems.colorBarModeLabel.setText("モード: ");
				ticksTabItems.colorBarEqualDivisionModeItem.setText(TICKER_MODE_EQUAL_DIVISION_JA);
				ticksTabItems.colorBarManualModeItem.setText(TICKER_MODE_MANUAL_JA);
				ticksTabItems.colorBarCustomModeItem.setText(TICKER_MODE_CUSTOM_JA);
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
				imagesTabItems.xyLowerPlaneLabel.setText("- X-Y平面（床面、Z最小側）-");
				imagesTabItems.xyUpperPlaneLabel.setText("- X-Y平面（天井、Z最大側）-");
				imagesTabItems.yzLowerPlaneLabel.setText("- Y-Z平面（X最小側）-");
				imagesTabItems.yzUpperPlaneLabel.setText("- Y-Z平面（X最大側）-");
				imagesTabItems.zxLowerPlaneLabel.setText("- Z-X平面（Y最小側）-");
				imagesTabItems.zxUpperPlaneLabel.setText("- Z-X平面（Y最大側）-");

				imagesTabItems.xyLowerPlaneItems.openFileButton.setText("開く");
				imagesTabItems.xyUpperPlaneItems.openFileButton.setText("開く");
				imagesTabItems.yzLowerPlaneItems.openFileButton.setText("開く");
				imagesTabItems.yzUpperPlaneItems.openFileButton.setText("開く");
				imagesTabItems.zxLowerPlaneItems.openFileButton.setText("開く");
				imagesTabItems.zxUpperPlaneItems.openFileButton.setText("開く");

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
				designTabItems.frameShapeModeLabel.setText("Frame:");
				{
					designTabItems.boxModeItem.setText(SHAPE_MODE_BOX_EN);
					designTabItems.backwallModeItem.setText(SHAPE_MODE_BACKWALL_EN);
					designTabItems.floorModeItem.setText(SHAPE_MODE_FLOOR_EN);
					designTabItems.noneModeItem.setText(SHAPE_MODE_NONE_EN);;
				}
				designTabItems.frameLineWidthLabel.setText("Width of Frame Lines:");
				designTabItems.tickLabelMarginLabel.setText("Margin of Tick Lines:");
				designTabItems.tickLineLengthLabel.setText("Length of Tick Lines:");
				designTabItems.tickInwardBox.setText("Draw Ticks Inward");
			}

			tabbedPane.setTitleAt(1, "Ticks");
			{
				ticksTabItems.xAxisLabel.setText("- X Axis -");
				ticksTabItems.xModeLabel.setText("Mode: ");
				ticksTabItems.xEqualDivisionModeItem.setText(TICKER_MODE_EQUAL_DIVISION_EN);
				ticksTabItems.xManualModeItem.setText(TICKER_MODE_MANUAL_EN);
				ticksTabItems.xCustomModeItem.setText(TICKER_MODE_CUSTOM_EN);
				ticksTabItems.xEqualDivisionItems.divisionCountLabel.setText("Number of Sections: ");
				ticksTabItems.xManualItems.coordinatesLabel.setText("Coords: ");
				ticksTabItems.xManualItems.labelsLabel.setText("Labels: ");

				ticksTabItems.yAxisLabel.setText("- Y Axis -");
				ticksTabItems.yModeLabel.setText("Mode: ");
				ticksTabItems.yEqualDivisionModeItem.setText(TICKER_MODE_EQUAL_DIVISION_EN);
				ticksTabItems.yManualModeItem.setText(TICKER_MODE_MANUAL_EN);
				ticksTabItems.yCustomModeItem.setText(TICKER_MODE_CUSTOM_EN);
				ticksTabItems.yEqualDivisionItems.divisionCountLabel.setText("Number of Sections: ");
				ticksTabItems.yManualItems.coordinatesLabel.setText("Coords: ");
				ticksTabItems.yManualItems.labelsLabel.setText("Labels: ");

				ticksTabItems.zAxisLabel.setText("- Z Axis -");
				ticksTabItems.zModeLabel.setText("Mode: ");
				ticksTabItems.zEqualDivisionModeItem.setText(TICKER_MODE_EQUAL_DIVISION_EN);
				ticksTabItems.zManualModeItem.setText(TICKER_MODE_MANUAL_EN);
				ticksTabItems.zCustomModeItem.setText(TICKER_MODE_CUSTOM_EN);
				ticksTabItems.zEqualDivisionItems.divisionCountLabel.setText("Number of Sections: ");
				ticksTabItems.zManualItems.coordinatesLabel.setText("Coords: ");
				ticksTabItems.zManualItems.labelsLabel.setText("Labels: ");

				ticksTabItems.colorBarLabel.setText("- Color Bar -");
				ticksTabItems.colorBarModeLabel.setText("Mode: ");
				ticksTabItems.colorBarEqualDivisionModeItem.setText(TICKER_MODE_EQUAL_DIVISION_EN);
				ticksTabItems.colorBarManualModeItem.setText(TICKER_MODE_MANUAL_EN);
				ticksTabItems.colorBarCustomModeItem.setText(TICKER_MODE_CUSTOM_EN);
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
				imagesTabItems.xyLowerPlaneLabel.setText("- X-Y Plane (Lower, Z-min side) -");
				imagesTabItems.xyUpperPlaneLabel.setText("- X-Y Plane (Upper, Z-max side) -");
				imagesTabItems.yzLowerPlaneLabel.setText("- Y-Z Plane (X-min side) -");
				imagesTabItems.yzUpperPlaneLabel.setText("- Y-Z Plane (X-max side) -");
				imagesTabItems.zxLowerPlaneLabel.setText("- Z-X Plane (Y-min side) -");
				imagesTabItems.zxUpperPlaneLabel.setText("- Z-X Plane (Y-max side) -");

				imagesTabItems.xyLowerPlaneItems.openFileButton.setText("Open");
				imagesTabItems.xyUpperPlaneItems.openFileButton.setText("Open");
				imagesTabItems.yzLowerPlaneItems.openFileButton.setText("Open");
				imagesTabItems.yzUpperPlaneItems.openFileButton.setText("Open");
				imagesTabItems.zxLowerPlaneItems.openFileButton.setText("Open");
				imagesTabItems.zxUpperPlaneItems.openFileButton.setText("Open");

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
				designTabItems.frameShapeModeLabel.setFont(uiBoldFont);
				designTabItems.frameLineWidthLabel.setFont(uiBoldFont);
				designTabItems.frameLineWidthField.setFont(uiPlainFont);
				designTabItems.tickLabelMarginLabel.setFont(uiBoldFont);
				designTabItems.tickLabelMarginField.setFont(uiPlainFont);
				designTabItems.tickLineLengthLabel.setFont(uiBoldFont);
				designTabItems.tickLineLengthField.setFont(uiPlainFont);
				designTabItems.tickInwardBox.setFont(uiBoldFont);
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
				imagesTabItems.xyLowerPlaneLabel.setFont(uiBoldFont);
				imagesTabItems.xyUpperPlaneLabel.setFont(uiBoldFont);
				imagesTabItems.yzLowerPlaneLabel.setFont(uiBoldFont);
				imagesTabItems.yzUpperPlaneLabel.setFont(uiBoldFont);
				imagesTabItems.zxLowerPlaneLabel.setFont(uiBoldFont);
				imagesTabItems.zxUpperPlaneLabel.setFont(uiBoldFont);

				imagesTabItems.xyLowerPlaneItems.filePathField.setFont(uiPlainFont);
				imagesTabItems.xyUpperPlaneItems.filePathField.setFont(uiPlainFont);
				imagesTabItems.yzLowerPlaneItems.filePathField.setFont(uiPlainFont);
				imagesTabItems.yzUpperPlaneItems.filePathField.setFont(uiPlainFont);
				imagesTabItems.zxLowerPlaneItems.filePathField.setFont(uiPlainFont);
				imagesTabItems.zxUpperPlaneItems.filePathField.setFont(uiPlainFont);

				imagesTabItems.xyLowerPlaneItems.openFileButton.setFont(uiBoldFont);
				imagesTabItems.xyUpperPlaneItems.openFileButton.setFont(uiBoldFont);
				imagesTabItems.yzLowerPlaneItems.openFileButton.setFont(uiBoldFont);
				imagesTabItems.yzUpperPlaneItems.openFileButton.setFont(uiBoldFont);
				imagesTabItems.zxLowerPlaneItems.openFileButton.setFont(uiBoldFont);
				imagesTabItems.zxUpperPlaneItems.openFileButton.setFont(uiBoldFont);

				imagesTabItems.insideBox.setFont(uiBoldFont);
			}

			okButton.setFont(uiBoldFont);
		}

		/**
		 * Updates the values of text fields, by the values stored in the configuration.
		 */
		private void updateValuesByConfiguration() {
			ScaleConfiguration scaleConfig = this.configuration.getScaleConfiguration();
			FrameConfiguration frameConfig = this.configuration.getFrameConfiguration();
			ScaleConfiguration.AxisScaleConfiguration xScaleConfig = scaleConfig.getXScaleConfiguration();
			ScaleConfiguration.AxisScaleConfiguration yScaleConfig = scaleConfig.getYScaleConfiguration();
			ScaleConfiguration.AxisScaleConfiguration zScaleConfig = scaleConfig.getZScaleConfiguration();
			ScaleConfiguration.AxisScaleConfiguration cScaleConfig = scaleConfig.getColorBarScaleConfiguration();

			// "Design" tab:
			{
				switch (frameConfig.getShapeMode()) {
					case BOX : {
						designTabItems.frameShapeModeBox.setSelectedItem(designTabItems.boxModeItem);
						break;
					}
					case BACKWALL : {
						designTabItems.frameShapeModeBox.setSelectedItem(designTabItems.backwallModeItem);
						break;
					}
					case FLOOR : {
						designTabItems.frameShapeModeBox.setSelectedItem(designTabItems.floorModeItem);
						break;
					}
					case NONE : {
						designTabItems.frameShapeModeBox.setSelectedItem(designTabItems.noneModeItem);
						break;
					}
					default : {
						throw new IllegalStateException("Unexpected frame shape mode: " + frameConfig.getShapeMode());
					}
				}

				double frameLineWidth = frameConfig.getLineWidth();
				designTabItems.frameLineWidthField.setText(Double.toString(frameLineWidth));

				double scaleLabelMargin = scaleConfig.getXScaleConfiguration().getTickLabelMargin();
				designTabItems.tickLabelMarginField.setText(Double.toString(scaleLabelMargin));

				double scaleLineLength = scaleConfig.getXScaleConfiguration().getTickLineLength();
				designTabItems.tickLineLengthField.setText(Double.toString(scaleLineLength));

				boolean tickInward = scaleConfig.isTickInward();
				designTabItems.tickInwardBox.setSelected(tickInward);
			}

			// "Ticks" tab:
			{
				ticksTabItems.xSwappablePanel.removeAll();
				switch (xScaleConfig.getTickerMode()) {
					case EQUAL_DIVISION : {
						ticksTabItems.xModeBox.setSelectedItem(ticksTabItems.xEqualDivisionModeItem);
						ticksTabItems.xSwappablePanel.add(ticksTabItems.xEqualDivisionItems.panel);
						break;
					}
					case MANUAL : {
						ticksTabItems.xModeBox.setSelectedItem(ticksTabItems.xManualModeItem);
						ticksTabItems.xSwappablePanel.add(ticksTabItems.xManualItems.panel);
						break;
					}
					case CUSTOM : {
						ticksTabItems.xModeBox.setSelectedItem(ticksTabItems.xCustomModeItem);
						break;
					}
					default : {
						throw new IllegalStateException("Unexpected ticker mode (X): " + xScaleConfig.getTickerMode());
					}
				}

				ticksTabItems.ySwappablePanel.removeAll();
				switch (yScaleConfig.getTickerMode()) {
					case EQUAL_DIVISION : {
						ticksTabItems.yModeBox.setSelectedItem(ticksTabItems.yEqualDivisionModeItem);
						ticksTabItems.ySwappablePanel.add(ticksTabItems.yEqualDivisionItems.panel);
						break;
					}
					case MANUAL : {
						ticksTabItems.yModeBox.setSelectedItem(ticksTabItems.yManualModeItem);
						ticksTabItems.ySwappablePanel.add(ticksTabItems.yManualItems.panel);
						break;
					}
					case CUSTOM : {
						ticksTabItems.yModeBox.setSelectedItem(ticksTabItems.yCustomModeItem);
						break;
					}
					default : {
						throw new IllegalStateException("Unexpected ticker mode (Y): " + yScaleConfig.getTickerMode());
					}
				}

				ticksTabItems.zSwappablePanel.removeAll();
				switch (zScaleConfig.getTickerMode()) {
					case EQUAL_DIVISION : {
						ticksTabItems.zModeBox.setSelectedItem(ticksTabItems.zEqualDivisionModeItem);
						ticksTabItems.zSwappablePanel.add(ticksTabItems.zEqualDivisionItems.panel);
						break;
					}
					case MANUAL : {
						ticksTabItems.zModeBox.setSelectedItem(ticksTabItems.zManualModeItem);
						ticksTabItems.zSwappablePanel.add(ticksTabItems.zManualItems.panel);
						break;
					}
					case CUSTOM : {
						ticksTabItems.zModeBox.setSelectedItem(ticksTabItems.zCustomModeItem);
						break;
					}
					default : {
						throw new IllegalStateException("Unexpected ticker mode (Z): " + zScaleConfig.getTickerMode());
					}
				}

				ticksTabItems.colorBarSwappablePanel.removeAll();
				switch (cScaleConfig.getTickerMode()) {
					case EQUAL_DIVISION : {
						ticksTabItems.colorBarModeBox.setSelectedItem(ticksTabItems.colorBarEqualDivisionModeItem);
						ticksTabItems.colorBarSwappablePanel.add(ticksTabItems.colorBarEqualDivisionItems.panel);
						break;
					}
					case MANUAL : {
						ticksTabItems.colorBarModeBox.setSelectedItem(ticksTabItems.colorBarManualModeItem);
						ticksTabItems.colorBarSwappablePanel.add(ticksTabItems.colorBarManualItems.panel);
						break;
					}
					case CUSTOM : {
						ticksTabItems.colorBarModeBox.setSelectedItem(ticksTabItems.colorBarCustomModeItem);
						break;
					}
					default : {
						throw new IllegalStateException("Unexpected ticker mode (Color-bar): " + cScaleConfig.getTickerMode());
					}
				}

				EqualDivisionTicker xEqualDivisionTicker = xScaleConfig.getEqualDivisionTicker();
				EqualDivisionTicker yEqualDivisionTicker = yScaleConfig.getEqualDivisionTicker();
				EqualDivisionTicker zEqualDivisionTicker = zScaleConfig.getEqualDivisionTicker();
				EqualDivisionTicker cEqualDivisionTicker = cScaleConfig.getEqualDivisionTicker();

				ticksTabItems.xEqualDivisionItems.divisionCountField.setText(Integer.toString(xEqualDivisionTicker.getDividedSectionCount()));
				ticksTabItems.yEqualDivisionItems.divisionCountField.setText(Integer.toString(yEqualDivisionTicker.getDividedSectionCount()));
				ticksTabItems.zEqualDivisionItems.divisionCountField.setText(Integer.toString(zEqualDivisionTicker.getDividedSectionCount()));
				ticksTabItems.colorBarEqualDivisionItems.divisionCountField.setText(Integer.toString(cEqualDivisionTicker.getDividedSectionCount()));

				ManualTicker xManualTicker = xScaleConfig.getManualTicker();
				ManualTicker yManualTicker = yScaleConfig.getManualTicker();
				ManualTicker zManualTicker = zScaleConfig.getManualTicker();
				ManualTicker cManualTicker = cScaleConfig.getManualTicker();

				ticksTabItems.xManualItems.coordinatesField.setText(this.tickCoordinatesToUIText(xManualTicker.getTickCoordinates()));
				ticksTabItems.yManualItems.coordinatesField.setText(this.tickCoordinatesToUIText(yManualTicker.getTickCoordinates()));
				ticksTabItems.zManualItems.coordinatesField.setText(this.tickCoordinatesToUIText(zManualTicker.getTickCoordinates()));
				ticksTabItems.colorBarManualItems.coordinatesField.setText(this.tickCoordinatesToUIText(cManualTicker.getTickCoordinates()));

				ticksTabItems.xManualItems.labelsField.setText(this.tickLabelsToUIText(xManualTicker.getTickLabelTexts()));
				ticksTabItems.yManualItems.labelsField.setText(this.tickLabelsToUIText(yManualTicker.getTickLabelTexts()));
				ticksTabItems.zManualItems.labelsField.setText(this.tickLabelsToUIText(zManualTicker.getTickLabelTexts()));
				ticksTabItems.colorBarManualItems.labelsField.setText(this.tickLabelsToUIText(cManualTicker.getTickLabelTexts()));
			}

			// "Formats" tab:
			{
				ScaleConfiguration.TickLabelFormatterMode xFormatterMode = xScaleConfig.getTickLabelFormatterMode();
				ScaleConfiguration.TickLabelFormatterMode yFormatterMode = yScaleConfig.getTickLabelFormatterMode();
				ScaleConfiguration.TickLabelFormatterMode zFormatterMode = zScaleConfig.getTickLabelFormatterMode();
				formatsTabItems.xAutoBox.setSelected(xFormatterMode == ScaleConfiguration.TickLabelFormatterMode.AUTO);
				formatsTabItems.yAutoBox.setSelected(yFormatterMode == ScaleConfiguration.TickLabelFormatterMode.AUTO);
				formatsTabItems.zAutoBox.setSelected(zFormatterMode == ScaleConfiguration.TickLabelFormatterMode.AUTO);

				NumericTickLabelFormatter xFormatter = xScaleConfig.getNumericTickLabelFormatter();
				NumberFormat xShortRangeFormat = xFormatter.getShortRangeFormat();
				NumberFormat xMediumRangeFormat = xFormatter.getMediumRangeFormat();
				NumberFormat xLongRangeFormat = xFormatter.getLongRangeFormat();
				formatsTabItems.xShortFormatField.setText(DecimalFormat.class.cast(xShortRangeFormat).toPattern());
				formatsTabItems.xMediumFormatField.setText(DecimalFormat.class.cast(xMediumRangeFormat).toPattern());
				formatsTabItems.xLongFormatField.setText(DecimalFormat.class.cast(xLongRangeFormat).toPattern());

				NumericTickLabelFormatter yFormatter = yScaleConfig.getNumericTickLabelFormatter();
				NumberFormat yShortRangeFormat = yFormatter.getShortRangeFormat();
				NumberFormat yMediumRangeFormat = yFormatter.getMediumRangeFormat();
				NumberFormat yLongRangeFormat = yFormatter.getLongRangeFormat();
				formatsTabItems.yShortFormatField.setText(DecimalFormat.class.cast(yShortRangeFormat).toPattern());
				formatsTabItems.yMediumFormatField.setText(DecimalFormat.class.cast(yMediumRangeFormat).toPattern());
				formatsTabItems.yLongFormatField.setText(DecimalFormat.class.cast(yLongRangeFormat).toPattern());

				NumericTickLabelFormatter zFormatter = zScaleConfig.getNumericTickLabelFormatter();
				NumberFormat zShortRangeFormat = zFormatter.getShortRangeFormat();
				NumberFormat zMediumRangeFormat = zFormatter.getMediumRangeFormat();
				NumberFormat zLongRangeFormat = zFormatter.getLongRangeFormat();
				formatsTabItems.zShortFormatField.setText(DecimalFormat.class.cast(zShortRangeFormat).toPattern());
				formatsTabItems.zMediumFormatField.setText(DecimalFormat.class.cast(zMediumRangeFormat).toPattern());
				formatsTabItems.zLongFormatField.setText(DecimalFormat.class.cast(zLongRangeFormat).toPattern());
			}

			// "Visibilities" tab:
			{
				ScaleConfiguration.VisibilityMode xAVisibilityMode = xScaleConfig.getScaleAVisibilityMode();
				ScaleConfiguration.VisibilityMode xBVisibilityMode = xScaleConfig.getScaleBVisibilityMode();
				ScaleConfiguration.VisibilityMode xCVisibilityMode = xScaleConfig.getScaleCVisibilityMode();
				ScaleConfiguration.VisibilityMode xDVisibilityMode = xScaleConfig.getScaleDVisibilityMode();

				ScaleConfiguration.VisibilityMode yAVisibilityMode = yScaleConfig.getScaleAVisibilityMode();
				ScaleConfiguration.VisibilityMode yBVisibilityMode = yScaleConfig.getScaleBVisibilityMode();
				ScaleConfiguration.VisibilityMode yCVisibilityMode = yScaleConfig.getScaleCVisibilityMode();
				ScaleConfiguration.VisibilityMode yDVisibilityMode = yScaleConfig.getScaleDVisibilityMode();

				ScaleConfiguration.VisibilityMode zAVisibilityMode = zScaleConfig.getScaleAVisibilityMode();
				ScaleConfiguration.VisibilityMode zBVisibilityMode = zScaleConfig.getScaleBVisibilityMode();
				ScaleConfiguration.VisibilityMode zCVisibilityMode = zScaleConfig.getScaleCVisibilityMode();
				ScaleConfiguration.VisibilityMode zDVisibilityMode = zScaleConfig.getScaleDVisibilityMode();

				boolean isXVisibilityAuto = xAVisibilityMode == ScaleConfiguration.VisibilityMode.AUTO
						&& xBVisibilityMode == ScaleConfiguration.VisibilityMode.AUTO
						&& xCVisibilityMode == ScaleConfiguration.VisibilityMode.AUTO
						&& xDVisibilityMode == ScaleConfiguration.VisibilityMode.AUTO;

				boolean isYVisibilityAuto = yAVisibilityMode == ScaleConfiguration.VisibilityMode.AUTO
						&& yBVisibilityMode == ScaleConfiguration.VisibilityMode.AUTO
						&& yCVisibilityMode == ScaleConfiguration.VisibilityMode.AUTO
						&& yDVisibilityMode == ScaleConfiguration.VisibilityMode.AUTO;

				boolean isZVisibilityAuto = zAVisibilityMode == ScaleConfiguration.VisibilityMode.AUTO
						&& zBVisibilityMode == ScaleConfiguration.VisibilityMode.AUTO
						&& zCVisibilityMode == ScaleConfiguration.VisibilityMode.AUTO
						&& zDVisibilityMode == ScaleConfiguration.VisibilityMode.AUTO;

				visibilitiesTabItems.xAutoBox.setSelected(isXVisibilityAuto);
				visibilitiesTabItems.xABox.setEnabled(!isXVisibilityAuto);
				visibilitiesTabItems.xBBox.setEnabled(!isXVisibilityAuto);
				visibilitiesTabItems.xCBox.setEnabled(!isXVisibilityAuto);
				visibilitiesTabItems.xDBox.setEnabled(!isXVisibilityAuto);
				if (isXVisibilityAuto) {
					visibilitiesTabItems.xABox.setSelected(false);
					visibilitiesTabItems.xBBox.setSelected(false);
					visibilitiesTabItems.xCBox.setSelected(false);
					visibilitiesTabItems.xDBox.setSelected(false);
				} else {
					visibilitiesTabItems.xABox.setSelected(xAVisibilityMode == ScaleConfiguration.VisibilityMode.ALWAYS_VISIBLE);
					visibilitiesTabItems.xBBox.setSelected(xBVisibilityMode == ScaleConfiguration.VisibilityMode.ALWAYS_VISIBLE);
					visibilitiesTabItems.xCBox.setSelected(xCVisibilityMode == ScaleConfiguration.VisibilityMode.ALWAYS_VISIBLE);
					visibilitiesTabItems.xDBox.setSelected(xDVisibilityMode == ScaleConfiguration.VisibilityMode.ALWAYS_VISIBLE);
				}

				visibilitiesTabItems.yAutoBox.setSelected(isYVisibilityAuto);
				visibilitiesTabItems.yABox.setEnabled(!isYVisibilityAuto);
				visibilitiesTabItems.yBBox.setEnabled(!isYVisibilityAuto);
				visibilitiesTabItems.yCBox.setEnabled(!isYVisibilityAuto);
				visibilitiesTabItems.yDBox.setEnabled(!isYVisibilityAuto);
				if (isYVisibilityAuto) {
					visibilitiesTabItems.yABox.setSelected(false);
					visibilitiesTabItems.yBBox.setSelected(false);
					visibilitiesTabItems.yCBox.setSelected(false);
					visibilitiesTabItems.yDBox.setSelected(false);
				} else {
					visibilitiesTabItems.yABox.setSelected(yAVisibilityMode == ScaleConfiguration.VisibilityMode.ALWAYS_VISIBLE);
					visibilitiesTabItems.yBBox.setSelected(yBVisibilityMode == ScaleConfiguration.VisibilityMode.ALWAYS_VISIBLE);
					visibilitiesTabItems.yCBox.setSelected(yCVisibilityMode == ScaleConfiguration.VisibilityMode.ALWAYS_VISIBLE);
					visibilitiesTabItems.yDBox.setSelected(yDVisibilityMode == ScaleConfiguration.VisibilityMode.ALWAYS_VISIBLE);
				}

				visibilitiesTabItems.zAutoBox.setSelected(isZVisibilityAuto);
				visibilitiesTabItems.zABox.setEnabled(!isZVisibilityAuto);
				visibilitiesTabItems.zBBox.setEnabled(!isZVisibilityAuto);
				visibilitiesTabItems.zCBox.setEnabled(!isZVisibilityAuto);
				visibilitiesTabItems.zDBox.setEnabled(!isZVisibilityAuto);
				if (isZVisibilityAuto) {
					visibilitiesTabItems.zABox.setSelected(false);
					visibilitiesTabItems.zBBox.setSelected(false);
					visibilitiesTabItems.zCBox.setSelected(false);
					visibilitiesTabItems.zDBox.setSelected(false);
				} else {
					visibilitiesTabItems.zABox.setSelected(zAVisibilityMode == ScaleConfiguration.VisibilityMode.ALWAYS_VISIBLE);
					visibilitiesTabItems.zBBox.setSelected(zBVisibilityMode == ScaleConfiguration.VisibilityMode.ALWAYS_VISIBLE);
					visibilitiesTabItems.zCBox.setSelected(zCVisibilityMode == ScaleConfiguration.VisibilityMode.ALWAYS_VISIBLE);
					visibilitiesTabItems.zDBox.setSelected(zDVisibilityMode == ScaleConfiguration.VisibilityMode.ALWAYS_VISIBLE);
				}
			}

			// "Images" tab:
			{
				FrameConfiguration.PlaneFrameConfiguration xyLowerFrameConfig = frameConfig.getXyLowerFrameConfiguration();
				FrameConfiguration.PlaneFrameConfiguration xyUpperFrameConfig = frameConfig.getXyUpperFrameConfiguration();
				FrameConfiguration.PlaneFrameConfiguration yzLowerFrameConfig = frameConfig.getYzLowerFrameConfiguration();
				FrameConfiguration.PlaneFrameConfiguration yzUpperFrameConfig = frameConfig.getYzUpperFrameConfiguration();
				FrameConfiguration.PlaneFrameConfiguration zxLowerFrameConfig = frameConfig.getZxLowerFrameConfiguration();
				FrameConfiguration.PlaneFrameConfiguration zxUpperFrameConfig = frameConfig.getZxUpperFrameConfiguration();

				File xyLowerImageFile = xyLowerFrameConfig.getImageFile();
				File xyUpperImageFile = xyUpperFrameConfig.getImageFile();
				File yzLowerImageFile = yzLowerFrameConfig.getImageFile();
				File yzUpperImageFile = yzUpperFrameConfig.getImageFile();
				File zxLowerImageFile = zxLowerFrameConfig.getImageFile();
				File zxUpperImageFile = zxUpperFrameConfig.getImageFile();

				imagesTabItems.xyLowerPlaneItems.filePathField.setText(xyLowerImageFile == null ? "" : xyLowerImageFile.getPath());
				imagesTabItems.xyUpperPlaneItems.filePathField.setText(xyUpperImageFile == null ? "" : xyUpperImageFile.getPath());
				imagesTabItems.yzLowerPlaneItems.filePathField.setText(yzLowerImageFile == null ? "" : yzLowerImageFile.getPath());
				imagesTabItems.yzUpperPlaneItems.filePathField.setText(yzUpperImageFile == null ? "" : yzUpperImageFile.getPath());
				imagesTabItems.zxLowerPlaneItems.filePathField.setText(zxLowerImageFile == null ? "" : zxLowerImageFile.getPath());
				imagesTabItems.zxUpperPlaneItems.filePathField.setText(zxUpperImageFile == null ? "" : zxUpperImageFile.getPath());

				imagesTabItems.xyLowerPlaneItems.imageWidthField.setText(Integer.toString(xyLowerFrameConfig.getImageResolutionConversionWidth()));
				imagesTabItems.xyUpperPlaneItems.imageWidthField.setText(Integer.toString(xyUpperFrameConfig.getImageResolutionConversionWidth()));
				imagesTabItems.yzLowerPlaneItems.imageWidthField.setText(Integer.toString(yzLowerFrameConfig.getImageResolutionConversionWidth()));
				imagesTabItems.yzUpperPlaneItems.imageWidthField.setText(Integer.toString(yzUpperFrameConfig.getImageResolutionConversionWidth()));
				imagesTabItems.zxLowerPlaneItems.imageWidthField.setText(Integer.toString(zxLowerFrameConfig.getImageResolutionConversionWidth()));
				imagesTabItems.zxUpperPlaneItems.imageWidthField.setText(Integer.toString(zxUpperFrameConfig.getImageResolutionConversionWidth()));

				imagesTabItems.xyLowerPlaneItems.imageHeightField.setText(Integer.toString(xyLowerFrameConfig.getImageResolutionConversionHeight()));
				imagesTabItems.xyUpperPlaneItems.imageHeightField.setText(Integer.toString(xyUpperFrameConfig.getImageResolutionConversionHeight()));
				imagesTabItems.yzLowerPlaneItems.imageHeightField.setText(Integer.toString(yzLowerFrameConfig.getImageResolutionConversionHeight()));
				imagesTabItems.yzUpperPlaneItems.imageHeightField.setText(Integer.toString(yzUpperFrameConfig.getImageResolutionConversionHeight()));
				imagesTabItems.zxLowerPlaneItems.imageHeightField.setText(Integer.toString(zxLowerFrameConfig.getImageResolutionConversionHeight()));
				imagesTabItems.zxUpperPlaneItems.imageHeightField.setText(Integer.toString(zxUpperFrameConfig.getImageResolutionConversionHeight()));

				imagesTabItems.insideBox.setSelected(frameConfig.getImageDirectionMode() == FrameConfiguration.ImageDirectionMode.INSIDE);
			}
		}

		/**
		 * Converts the array storing the tick coordinates to a text value to be set to UI.
		 *
		 * @param tickCoords The array storing the tick coordinates.
		 * @return A text value to be set to UI.
		 */
		private String tickCoordinatesToUIText(BigDecimal[] tickCoordinates) {
			int tickCount = tickCoordinates.length;
			StringBuilder uiTextBuilder = new StringBuilder();
			for (int itick=0; itick<tickCount; itick++) {

				// Append the tick coordinate to the UI text, and also append a comma (,) as the delimiter between values.
				uiTextBuilder.append(tickCoordinates[itick].toString());
				if (itick != tickCount - 1) {
					uiTextBuilder.append(", ");
				}
			}
			return uiTextBuilder.toString();
		}

		/**
		 * Converts the array storing the tick labels to a text value to be set to UI.
		 *
		 * @param tickLabels The array storing the tick labels.
		 * @return A text value to be set to UI.
		 */
		private String tickLabelsToUIText(String[] tickLabels) {
			int tickCount = tickLabels.length;
			StringBuilder uiTextBuilder = new StringBuilder();
			for (int itick=0; itick<tickCount; itick++) {

				// If the tick label contains comma (,) characters, escape them.
				String escapedLabel = tickLabels[itick].contains(",") ? tickLabels[itick].replaceAll(",", "\\,") : tickLabels[itick];

				// Append the escaped tick label to the UI text, and also append a comma (,) as the delimiter between values.
				uiTextBuilder.append(escapedLabel);
				if (itick != tickCount - 1) {
					uiTextBuilder.append(", ");
				}
			}
			return uiTextBuilder.toString();
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
		designTabItems.frameShapeModeLabel = new JLabel("Unconfigured");
		constraints.gridy = 0;
		constraints.insets = new Insets(topMargin, leftMargin, bottomMarginBetweenLabelAndField, rightMargin);
		layout.setConstraints(designTabItems.frameShapeModeLabel, constraints);
		designTabPanel.add(designTabItems.frameShapeModeLabel);

		// The combo box to select the type of the outer frame.
		designTabItems.frameShapeModeBox = new JComboBox<MultilingualItem>();
		constraints.gridy++;
		constraints.insets = new Insets(topMarginBetweenLabelAndField, leftMarginLong, bottomMargin, rightMargin);
		layout.setConstraints(designTabItems.frameShapeModeBox, constraints);
		designTabPanel.add(designTabItems.frameShapeModeBox);
		{
			designTabItems.boxModeItem = new MultilingualItem();
			designTabItems.frameShapeModeBox.addItem(designTabItems.boxModeItem);
			designTabItems.backwallModeItem = new MultilingualItem();
			designTabItems.frameShapeModeBox.addItem(designTabItems.backwallModeItem);
			designTabItems.floorModeItem = new MultilingualItem();
			designTabItems.frameShapeModeBox.addItem(designTabItems.floorModeItem);
			designTabItems.noneModeItem = new MultilingualItem();
			designTabItems.frameShapeModeBox.addItem(designTabItems.noneModeItem);
		}

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


		// The label of the text field of the margin between tick lines and labels.
		designTabItems.tickLabelMarginLabel = new JLabel("Unconfigured");
		constraints.gridy++;
		constraints.insets = new Insets(topMargin, leftMargin, bottomMarginBetweenLabelAndField, rightMargin);
		layout.setConstraints(designTabItems.tickLabelMarginLabel, constraints);
		designTabPanel.add(designTabItems.tickLabelMarginLabel);

		// The text field of the margin between tick lines and labels.
		designTabItems.tickLabelMarginField = new JTextField();
		constraints.gridy++;
		constraints.insets = new Insets(topMarginBetweenLabelAndField, leftMarginLong, bottomMargin, rightMargin);
		layout.setConstraints(designTabItems.tickLabelMarginField, constraints);
		designTabPanel.add(designTabItems.tickLabelMarginField);


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
		designTabItems.tickInwardBox = new JCheckBox("Unconfigured");
		constraints.gridy++;
		constraints.insets = new Insets(topMargin, leftMargin, bottomMarginOfEnd, rightMargin);
		layout.setConstraints(designTabItems.tickInwardBox, constraints);
		designTabPanel.add(designTabItems.tickInwardBox);
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
			ticksTabItems.xModeBox = new JComboBox<MultilingualItem>();
			constraints.gridx++;
			constraints.weightx = rightColumnWeight;
			constraints.insets = new Insets(topMarginInSection, 0, bottomMarginInSection, rightMargin);
			layout.setConstraints(ticksTabItems.xModeBox, constraints);
			ticksTabPanel.add(ticksTabItems.xModeBox);
			{
				ticksTabItems.xEqualDivisionModeItem = new MultilingualItem();
				ticksTabItems.xModeBox.addItem(ticksTabItems.xEqualDivisionModeItem);
				ticksTabItems.xManualModeItem = new MultilingualItem();
				ticksTabItems.xModeBox.addItem(ticksTabItems.xManualModeItem);
				ticksTabItems.xCustomModeItem = new MultilingualItem();
				ticksTabItems.xModeBox.addItem(ticksTabItems.xCustomModeItem);
			}

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
			ticksTabItems.yModeBox = new JComboBox<MultilingualItem>();
			constraints.gridx++;
			constraints.weightx = rightColumnWeight;
			constraints.insets = new Insets(topMarginInSection, 0, bottomMarginInSection, rightMargin);
			layout.setConstraints(ticksTabItems.yModeBox, constraints);
			ticksTabPanel.add(ticksTabItems.yModeBox);
			{
				ticksTabItems.yEqualDivisionModeItem = new MultilingualItem();
				ticksTabItems.yModeBox.addItem(ticksTabItems.yEqualDivisionModeItem);
				ticksTabItems.yManualModeItem = new MultilingualItem();
				ticksTabItems.yModeBox.addItem(ticksTabItems.yManualModeItem);
				ticksTabItems.yCustomModeItem = new MultilingualItem();
				ticksTabItems.yModeBox.addItem(ticksTabItems.yCustomModeItem);
			}

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
			ticksTabItems.zModeBox = new JComboBox<MultilingualItem>();
			constraints.gridx++;
			constraints.weightx = rightColumnWeight;
			constraints.insets = new Insets(topMarginInSection, 0, bottomMarginInSection, rightMargin);
			layout.setConstraints(ticksTabItems.zModeBox, constraints);
			ticksTabPanel.add(ticksTabItems.zModeBox);
			{
				ticksTabItems.zEqualDivisionModeItem = new MultilingualItem();
				ticksTabItems.zModeBox.addItem(ticksTabItems.zEqualDivisionModeItem);
				ticksTabItems.zManualModeItem = new MultilingualItem();
				ticksTabItems.zModeBox.addItem(ticksTabItems.zManualModeItem);
				ticksTabItems.zCustomModeItem = new MultilingualItem();
				ticksTabItems.zModeBox.addItem(ticksTabItems.zCustomModeItem);
			}

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
			ticksTabItems.colorBarModeBox = new JComboBox<MultilingualItem>();
			constraints.gridx++;
			constraints.weightx = rightColumnWeight;
			constraints.insets = new Insets(topMarginInSection, 0, bottomMarginInSection, rightMargin);
			layout.setConstraints(ticksTabItems.colorBarModeBox, constraints);
			ticksTabPanel.add(ticksTabItems.colorBarModeBox);
			{
				ticksTabItems.colorBarEqualDivisionModeItem = new MultilingualItem();
				ticksTabItems.colorBarModeBox.addItem(ticksTabItems.colorBarEqualDivisionModeItem);
				ticksTabItems.colorBarManualModeItem = new MultilingualItem();
				ticksTabItems.colorBarModeBox.addItem(ticksTabItems.colorBarManualModeItem);
				ticksTabItems.colorBarCustomModeItem = new MultilingualItem();
				ticksTabItems.colorBarModeBox.addItem(ticksTabItems.colorBarCustomModeItem);
			}

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

		// X-Y Plane (Lower, Z-min side)
		{
			constraints.gridy = 0;

			// The title label of the section of this Plane.
			imagesTabItems.xyLowerPlaneLabel = new JLabel("Unconfigured");
			constraints.weighty = labelWeight;
			constraints.insets = new Insets(topMarginHead, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(imagesTabItems.xyLowerPlaneLabel, constraints);
			imagesTabPanel.add(imagesTabItems.xyLowerPlaneLabel);

			constraints.gridy++;

			// The panel on which the setting items for this Plane.
			constraints.weighty = panelWeight;
			constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMargin, rightMargin);
			layout.setConstraints(imagesTabItems.xyLowerPlaneItems.panel, constraints);
			imagesTabPanel.add(imagesTabItems.xyLowerPlaneItems.panel);
		}

		// X-Y Plane (Upper, Z-max side)
		{
			constraints.gridy++;

			// The title label of the section of this Plane.
			imagesTabItems.xyUpperPlaneLabel = new JLabel("Unconfigured");
			constraints.weighty = labelWeight;
			constraints.insets = new Insets(topMarginHead, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(imagesTabItems.xyUpperPlaneLabel, constraints);
			imagesTabPanel.add(imagesTabItems.xyUpperPlaneLabel);

			constraints.gridy++;

			// The panel on which the setting items for this Plane.
			constraints.weighty = panelWeight;
			constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMargin, rightMargin);
			layout.setConstraints(imagesTabItems.xyUpperPlaneItems.panel, constraints);
			imagesTabPanel.add(imagesTabItems.xyUpperPlaneItems.panel);
		}

		// Y-Z Plane (Lower, X-min side)
		{
			constraints.gridy++;

			// The title label of the section of this Plane.
			imagesTabItems.yzLowerPlaneLabel = new JLabel("Unconfigured");
			constraints.weighty = labelWeight;
			constraints.insets = new Insets(topMarginHead, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(imagesTabItems.yzLowerPlaneLabel, constraints);
			imagesTabPanel.add(imagesTabItems.yzLowerPlaneLabel);

			constraints.gridy++;

			// The panel on which the setting items for this Plane.
			constraints.weighty = panelWeight;
			constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMargin, rightMargin);
			layout.setConstraints(imagesTabItems.yzLowerPlaneItems.panel, constraints);
			imagesTabPanel.add(imagesTabItems.yzLowerPlaneItems.panel);
		}

		// Y-Z Plane (Upper, X-max side)
		{
			constraints.gridy++;

			// The title label of the section of this Plane.
			imagesTabItems.yzUpperPlaneLabel = new JLabel("Unconfigured");
			constraints.weighty = labelWeight;
			constraints.insets = new Insets(topMarginHead, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(imagesTabItems.yzUpperPlaneLabel, constraints);
			imagesTabPanel.add(imagesTabItems.yzUpperPlaneLabel);

			constraints.gridy++;

			// The panel on which the setting items for this Plane.
			constraints.weighty = panelWeight;
			constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMargin, rightMargin);
			layout.setConstraints(imagesTabItems.yzUpperPlaneItems.panel, constraints);
			imagesTabPanel.add(imagesTabItems.yzUpperPlaneItems.panel);
		}

		// Z-X Plane (Lower, Y-min side)
		{
			constraints.gridy++;

			// The title label of the section of this Plane.
			constraints.weighty = labelWeight;
			imagesTabItems.zxLowerPlaneLabel = new JLabel("Unconfigured");
			constraints.insets = new Insets(topMarginHead, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(imagesTabItems.zxLowerPlaneLabel, constraints);
			imagesTabPanel.add(imagesTabItems.zxLowerPlaneLabel);

			constraints.gridy++;

			// The panel on which the setting items for this Plane.
			constraints.weighty = panelWeight;
			constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMargin, rightMargin);
			layout.setConstraints(imagesTabItems.zxLowerPlaneItems.panel, constraints);
			imagesTabPanel.add(imagesTabItems.zxLowerPlaneItems.panel);
		}

		// Z-X Plane (Upper, Y-max side)
		{
			constraints.gridy++;

			// The title label of the section of this Plane.
			imagesTabItems.zxUpperPlaneLabel = new JLabel("Unconfigured");
			constraints.weighty = labelWeight;
			constraints.insets = new Insets(topMarginHead, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(imagesTabItems.zxUpperPlaneLabel, constraints);
			imagesTabPanel.add(imagesTabItems.zxUpperPlaneLabel);

			constraints.gridy++;

			// The panel on which the setting items for this Plane.
			constraints.weighty = panelWeight;
			constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMargin, rightMargin);
			layout.setConstraints(imagesTabItems.zxUpperPlaneItems.panel, constraints);
			imagesTabPanel.add(imagesTabItems.zxUpperPlaneItems.panel);
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
				imagesTabItems.xyLowerPlaneItems = items;
			} else if (iplane == 1) {
				imagesTabItems.xyUpperPlaneItems = items;
			} else if (iplane == 2) {
				imagesTabItems.yzLowerPlaneItems = items;
			} else if (iplane == 3) {
				imagesTabItems.yzUpperPlaneItems = items;
			} else if (iplane == 4) {
				imagesTabItems.zxLowerPlaneItems = items;
			} else if (iplane == 5) {
				imagesTabItems.zxUpperPlaneItems = items;
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
