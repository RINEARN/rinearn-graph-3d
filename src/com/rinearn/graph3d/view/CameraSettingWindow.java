package com.rinearn.graph3d.view;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.FontConfiguration;
import com.rinearn.graph3d.config.CameraConfiguration;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JScrollBar;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.SwingUtilities;


/**
 * The window of "Set Camera" menu.
 */
public class CameraSettingWindow {

	/** The default width [px] of the main window. */
	public static final int DEFAULT_WINDOW_WIDTH = 450;

	/** The default height [px] of the main window. */
	public static final int DEFAULT_WINDOW_HEIGHT = 620;

	/** The the max value (integer count) of the scroll bars in this window. */
	public static final int SCROLL_BAR_MAX_COUNT = 1000;

	/** The color of the scroll bars in this window. */
	public static final Color SCROLL_BAR_COLOR = new Color(100, 120, 200);

	/** The item of zenithAxisBox, representing X axis. */
	public static final String ZENITH_AXIS_BOX_ITEM_X = "X";

	/** The item of zenithAxisBox, representing Y axis. */
	public static final String ZENITH_AXIS_BOX_ITEM_Y = "Y";

	/** The item of zenithAxisBox, representing Z axis. */
	public static final String ZENITH_AXIS_BOX_ITEM_Z = "Z";

	/** The frame of this window. */
	public volatile JFrame frame;

	/** The title label of "Camera Angle" section. */
	public volatile JLabel angleSectionLabel;

	/** The label of "Zenith Axis" parameter. */
	public volatile JLabel zenithAxisLabel;

	/** The combo box of "Zenith Axis" parameter. */
	public volatile JComboBox<String> zenithAxisBox;

	/** The label of "Horizontal Angle" parameter. */
	public volatile JLabel horizontalAngleLabel;

	/** The scroll bar of "Horizontal Angle" parameter. */
	public volatile JScrollBar horizontalAngleBar;

	/** The label of "Vertical Angle" parameter. */
	public volatile JLabel verticalAngleLabel;

	/** The scroll bar of "Vertical Angle" parameter. */
	public volatile JScrollBar verticalAngleBar;

	/** The label of "Screw Angle" parameter. */
	public volatile JLabel screwAngleLabel;

	/** The scroll bar of "Screw Angle" parameter. */
	public volatile JScrollBar screwAngleBar;


	/** The title label of "Lens" section. */
	public volatile JLabel lensSectionLabel;

	/** The label of "Magnification" parameter. */
	public volatile JLabel magnificationLabel;

	/** The scroll bar of "Magnification" parameter. */
	public volatile JScrollBar magnificationBar;

	/** The label of "Distance" parameter. */
	public volatile JLabel distanceLabel;

	/** The scroll bar of "Distance" parameter. */
	public volatile JScrollBar distanceBar;

	/** The scroll bar of "Perspective" parameter. */
	public volatile JCheckBox perspectiveBox;

	/** The scroll bar of "Anti-aliasing" parameter. */
	public volatile JCheckBox antialiasingBox;


	/** The title label of "Screen" section. */
	public volatile JLabel screenSectionLabel;

	/** The title label of "Width" parameter. */
	public volatile JLabel widthLabel;

	/** The text field of "Width" parameter. */
	public volatile JTextField widthField;

	/** The title label of "Height" parameter. */
	public volatile JLabel heightLabel;

	/** The text field of "Height" parameter. */
	public volatile JTextField heightField;

	/** The title label of "Center Offset (X)" parameter. */
	public volatile JLabel centerOffsetXLabel;

	/** The scroll bar of "Center Offset (X)" parameter. */
	public volatile JScrollBar centerOffsetXBar;

	/** The title label of "Center Offset (Y)" parameter. */
	public volatile JLabel centerOffsetYLabel;

	/** The scroll bar of "Center Offset (Y)" parameter. */
	public volatile JScrollBar centerOffsetYBar;

	/** "OK" button. */
	public volatile JButton okButton;


	/**
	 * Creates a new window.
	 * 
	 * @param configuration The configuration of this application.
	 */
	public CameraSettingWindow() {

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
			int topMargin = 0;
			int bottomMargin = 2;
			int leftMargin = 5;
			int rightMargin = 5;
			int bottomMarginUnderSectionTitile = 8;
			int bottomMarginUnderSection = 15;
			int bottomMarginInSection = 3;
			int leftMarginLabelInSection = 20;
			int leftMarginBarInSection = 40;

			// Components for setting camera angle parameters.
			{
				// Create the title label of "Camera Angle" section.
				angleSectionLabel = new JLabel("Unconfigured");
				angleSectionLabel.setVerticalAlignment(JLabel.BOTTOM);
				constraints.insets = new Insets(topMargin, leftMargin, bottomMarginUnderSectionTitile, rightMargin);
				layour.setConstraints(angleSectionLabel, constraints);
				basePanel.add(angleSectionLabel);

				constraints.gridy++;

				// Create the label of "Zenith Axis" section.
				zenithAxisLabel = new JLabel("Unconfigured");
				constraints.insets = new Insets(0, leftMarginLabelInSection, 0, rightMargin);
				layour.setConstraints(zenithAxisLabel, constraints);
				basePanel.add(zenithAxisLabel);

				constraints.gridy++;

				// Create the combo box of "Zenith Axis".
				zenithAxisBox = new JComboBox<String>();
				zenithAxisBox.addItem(ZENITH_AXIS_BOX_ITEM_X);
				zenithAxisBox.addItem(ZENITH_AXIS_BOX_ITEM_Y);
				zenithAxisBox.addItem(ZENITH_AXIS_BOX_ITEM_Z);
				constraints.insets = new Insets(0, leftMarginBarInSection, bottomMarginInSection, rightMargin);
				layour.setConstraints(zenithAxisBox, constraints);
				basePanel.add(zenithAxisBox);

				constraints.gridy++;

				// Create the label of "Horizontal Angle" section.
				horizontalAngleLabel = new JLabel("Unconfigured");
				constraints.insets = new Insets(5, leftMarginLabelInSection, 0, rightMargin);
				layour.setConstraints(horizontalAngleLabel, constraints);
				basePanel.add(horizontalAngleLabel);

				constraints.gridy++;

				// Create the scroll bar of "Horizontal Angle" section.
				horizontalAngleBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, SCROLL_BAR_MAX_COUNT);
				horizontalAngleBar.setBackground(SCROLL_BAR_COLOR);
				constraints.insets = new Insets(0, leftMarginBarInSection, bottomMarginInSection, rightMargin);
				layour.setConstraints(horizontalAngleBar, constraints);
				basePanel.add(horizontalAngleBar);

				constraints.gridy++;

				// Create the label of "Vertical Angle" section.
				verticalAngleLabel = new JLabel("Unconfigured");
				constraints.insets = new Insets(0, leftMarginLabelInSection, 0, rightMargin);
				layour.setConstraints(verticalAngleLabel, constraints);
				basePanel.add(verticalAngleLabel);

				constraints.gridy++;

				// Create the scroll bar of "Vertical Angle" section.
				verticalAngleBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, SCROLL_BAR_MAX_COUNT);
				verticalAngleBar.setBackground(SCROLL_BAR_COLOR);
				constraints.insets = new Insets(0, leftMarginBarInSection, bottomMarginInSection, rightMargin);
				layour.setConstraints(verticalAngleBar, constraints);
				basePanel.add(verticalAngleBar);

				constraints.gridy++;

				// Create the label of "Screw Angle" section.
				screwAngleLabel = new JLabel("Unconfigured");
				constraints.insets = new Insets(0, leftMarginLabelInSection, 0, rightMargin);
				layour.setConstraints(screwAngleLabel, constraints);
				basePanel.add(screwAngleLabel);

				constraints.gridy++;

				// Create the scroll bar of "Screw Angle" section.
				screwAngleBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, SCROLL_BAR_MAX_COUNT);
				screwAngleBar.setBackground(SCROLL_BAR_COLOR);
				constraints.insets = new Insets(0, leftMarginBarInSection, bottomMarginUnderSection, rightMargin);
				layour.setConstraints(screwAngleBar, constraints);
				basePanel.add(screwAngleBar);

				constraints.gridy++;
			}


			// Components for setting lens parameters.
			{
				// Create the title label of "Lens" section.
				lensSectionLabel = new JLabel("Unconfigured");
				lensSectionLabel.setVerticalAlignment(JLabel.BOTTOM);
				constraints.insets = new Insets(0, leftMargin, bottomMarginUnderSectionTitile, rightMargin);
				layour.setConstraints(lensSectionLabel, constraints);
				basePanel.add(lensSectionLabel);

				constraints.gridy++;

				// Create the label of "Magnification" parameter.
				magnificationLabel = new JLabel("Unconfigured");
				constraints.insets = new Insets(0, leftMarginLabelInSection, 0, rightMargin);
				layour.setConstraints(magnificationLabel, constraints);
				basePanel.add(magnificationLabel);

				constraints.gridy++;

				// Create the scroll bar of "Magnification" parameter.
				magnificationBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, SCROLL_BAR_MAX_COUNT);
				magnificationBar.setBackground(SCROLL_BAR_COLOR);
				constraints.insets = new Insets(0, leftMarginBarInSection, bottomMarginInSection, rightMargin);
				layour.setConstraints(magnificationBar, constraints);
				basePanel.add(magnificationBar);

				constraints.gridy++;

				// Create the label of "Distance" parameter.
				distanceLabel = new JLabel("Unconfigured");
				constraints.insets = new Insets(0, leftMarginLabelInSection, 0, rightMargin);
				layour.setConstraints(distanceLabel, constraints);
				basePanel.add(distanceLabel);

				constraints.gridy++;

				// Create the scroll bar of "Distance" parameter.
				distanceBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, SCROLL_BAR_MAX_COUNT);
				distanceBar.setBackground(SCROLL_BAR_COLOR);
				constraints.insets = new Insets(0, leftMarginBarInSection, bottomMarginInSection, rightMargin);
				layour.setConstraints(distanceBar, constraints);
				basePanel.add(distanceBar);

				constraints.gridy++;

				// Panel on which check boxes are mounted.
				{
					JPanel checkBoxesPanel = new JPanel();
					checkBoxesPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
					constraints.insets = new Insets(5, leftMarginLabelInSection, 0, rightMargin);
					layour.setConstraints(checkBoxesPanel, constraints);
					basePanel.add(checkBoxesPanel);

					// Create the check box of "Perspective" parameter.
					perspectiveBox = new JCheckBox("Unconfigured");
					perspectiveBox.setSelected(true);
					checkBoxesPanel.add(perspectiveBox);

					// Create the check box of "Anti-aliasing" parameter.
					antialiasingBox = new JCheckBox("Unconfigured");
					antialiasingBox.setSelected(true);
					checkBoxesPanel.add(antialiasingBox);

					constraints.gridy++;
				}
			}


			// Components for setting screen parameters.
			{
				// Create the  label of "Screen" section.
				screenSectionLabel = new JLabel("Unconfigured");
				screenSectionLabel.setVerticalAlignment(JLabel.BOTTOM);
				constraints.insets = new Insets(0, leftMargin, bottomMarginUnderSectionTitile, rightMargin);
				layour.setConstraints(screenSectionLabel, constraints);
				basePanel.add(screenSectionLabel);

				constraints.gridy++;

				// Create the label of "Center Offset (X)" parameter.
				centerOffsetXLabel = new JLabel("Unconfigured");
				constraints.insets = new Insets(0, leftMarginLabelInSection, 0, rightMargin);
				layour.setConstraints(centerOffsetXLabel, constraints);
				basePanel.add(centerOffsetXLabel);

				constraints.gridy++;

				// Create the scroll bar of "Center Offset (X)" parameter.
				centerOffsetXBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, -SCROLL_BAR_MAX_COUNT, SCROLL_BAR_MAX_COUNT);
				centerOffsetXBar.setBackground(SCROLL_BAR_COLOR);
				constraints.insets = new Insets(0, leftMarginBarInSection, bottomMarginInSection, rightMargin);
				layour.setConstraints(centerOffsetXBar, constraints);
				basePanel.add(centerOffsetXBar);

				constraints.gridy++;

				// Create the label of "Center Offset (Y)" parameter.
				centerOffsetYLabel = new JLabel("Unconfigured");
				constraints.insets = new Insets(0, leftMarginLabelInSection, 0, rightMargin);
				layour.setConstraints(centerOffsetYLabel, constraints);
				basePanel.add(centerOffsetYLabel);

				constraints.gridy++;

				// Create the scroll bar of "Center Offset (Y)" parameter.
				centerOffsetYBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, -SCROLL_BAR_MAX_COUNT, SCROLL_BAR_MAX_COUNT);
				centerOffsetYBar.setBackground(SCROLL_BAR_COLOR);
				constraints.insets = new Insets(0, leftMarginBarInSection, bottomMarginInSection, rightMargin);
				layour.setConstraints(centerOffsetYBar, constraints);
				basePanel.add(centerOffsetYBar);

				constraints.gridy++;

				// Panel on which UI for setting "Width" and "Height" params are mounted.
				{
					JPanel sizeFieldsPanel = new JPanel();
					sizeFieldsPanel.setLayout(new GridLayout(1, 4));
					constraints.insets = new Insets(10, 0, bottomMarginUnderSection, rightMargin);
					layour.setConstraints(sizeFieldsPanel, constraints);
					basePanel.add(sizeFieldsPanel);

					constraints.gridy++;

					// Create the label of "Width" parameter.
					widthLabel = new JLabel("Unconfigured", JLabel.RIGHT);
					sizeFieldsPanel.add(widthLabel);

					// Create the text field of "Width" parameter.
					widthField = new JTextField("Unconfigured");
					sizeFieldsPanel.add(widthField);

					// Create the label of "Height" parameter.
					heightLabel = new JLabel("Unconfigured", JLabel.RIGHT);
					sizeFieldsPanel.add(heightLabel);

					// Create the text field of "Height" parameter.
					heightField = new JTextField("Unconfigured");
					sizeFieldsPanel.add(heightField);
				}

			}

			// Create "OK" button.
			okButton = new JButton("Unconfigured");
			constraints.insets = new Insets(0, leftMargin, bottomMargin, rightMargin);
			constraints.weighty = 12.0;
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

			// Updates the values of text fields, by the values stored in the configuration.
			this.updateValuesByConfiguration();
		}

		/**
		 * Sets Japanese texts to the GUI components.
		 */
		private void setJapaneseTexts() {
			frame.setTitle("カメラの設定");
			angleSectionLabel.setText("- カメラアングル -");
			zenithAxisLabel.setText("天頂軸:");
			horizontalAngleLabel.setText("水平方向の角度:");
			verticalAngleLabel.setText("垂直方向の角度:");
			screwAngleLabel.setText("きりもみ角度:");
			lensSectionLabel.setText("- レンズ -");
			magnificationLabel.setText("拡大率:");
			distanceLabel.setText("距離:");
			perspectiveBox.setText("遠近感");
			antialiasingBox.setText("アンチエイリアス");
			screenSectionLabel.setText("- スクリーン -");
			widthLabel.setText("画面幅: ");
			heightLabel.setText("画面高さ: ");
			centerOffsetXLabel.setText("中心オフセット(X):");
			centerOffsetYLabel.setText("中心オフセット(Y):");
			okButton.setText("OK");
		}

		/**
		 * Sets English texts to the GUI components.
		 */
		private void setEnglishTexts() {
			frame.setTitle("Set Camera");
			angleSectionLabel.setText("- Camera Angle -");
			zenithAxisLabel.setText("Zenith Axis:");
			screwAngleLabel.setText("Horizontal Angle:");
			horizontalAngleLabel.setText("Horizontal Angle:");
			verticalAngleLabel.setText("Vertical Angle:");
			screwAngleLabel.setText("Screw Angle:");
			lensSectionLabel.setText("- Lens -");
			magnificationLabel.setText("Magnification:");
			distanceLabel.setText("Distance:");
			perspectiveBox.setText("Perspective");
			antialiasingBox.setText("Anti-aliasing");
			screenSectionLabel.setText("- Screen -");
			widthLabel.setText("Width: ");
			heightLabel.setText("Height: ");
			centerOffsetXLabel.setText("Center Offset (X):");
			centerOffsetYLabel.setText("Center Offset (Y):");
			okButton.setText("OK");
		}

		/**
		 * Set fonts to the components.
		 */
		private void setFonts() {
			FontConfiguration fontConfig = configuration.getFontConfiguration();
			Font uiBoldFont = fontConfig.getUIBoldFont();
			Font uiPlainFont = fontConfig.getUIPlainFont();

			angleSectionLabel.setFont(uiBoldFont);
			zenithAxisLabel.setFont(uiBoldFont);
			screwAngleLabel.setFont(uiBoldFont);
			horizontalAngleLabel.setFont(uiBoldFont);
			verticalAngleLabel.setFont(uiBoldFont);
			screwAngleLabel.setFont(uiBoldFont);

			lensSectionLabel.setFont(uiBoldFont);
			magnificationLabel.setFont(uiBoldFont);
			distanceLabel.setFont(uiBoldFont);
			perspectiveBox.setFont(uiBoldFont);
			antialiasingBox.setFont(uiBoldFont);

			screenSectionLabel.setFont(uiBoldFont);
			widthLabel.setFont(uiBoldFont);
			heightLabel.setFont(uiBoldFont);
			centerOffsetXLabel.setFont(uiBoldFont);
			centerOffsetYLabel.setFont(uiBoldFont);

			widthField.setFont(uiPlainFont);
			heightField.setFont(uiPlainFont);

			okButton.setFont(uiBoldFont);
		}

		/**
		 * Updates the values of text fields, by the values stored in the configuration.
		 */
		private void updateValuesByConfiguration() {
			CameraConfiguration cameraConfig = this.configuration.getCameraConfiguration();

			// To be implemented.
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
