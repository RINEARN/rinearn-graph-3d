package com.rinearn.graph3d.view;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.color.AxisGradientColor;
import com.rinearn.graph3d.config.color.GradientAxis;
import com.rinearn.graph3d.config.color.GradientColor;
import com.rinearn.graph3d.config.ColorConfiguration;
import com.rinearn.graph3d.config.FontConfiguration;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import java.lang.reflect.InvocationTargetException;


/**
 * The window of "Gradient Coloring" option.
 */
public final class GradientOptionWindow {

	/** The default width [px] of this window. */
	public static final int DEFAULT_WINDOW_WIDTH = 340;

	/** The default height [px] of this window. */
	public static final int DEFAULT_WINDOW_HEIGHT = 430;

	/** The display item of the gradient axis "X". */
	public static final String GRADIENT_AXIS_X = "X";

	/** The display item of the gradient axis "Y". */
	public static final String GRADIENT_AXIS_Y = "Y";

	/** The display item of the gradient axis "Z". */
	public static final String GRADIENT_AXIS_Z = "Z";

	/** The display item of the gradient axis "Scalar" in Japanese. */
	public static final String GRADIENT_AXIS_SCALAR_JA = "スカラー（4列目の値）";

	/** The display item of the gradient axis "Scalar" in English. */
	public static final String GRADIENT_AXIS_SCALAR_EN = "Scalar (4th Column)";

	/** The frame of this window. */
	public volatile JFrame frame;

	/** The label of the combo box to select the axis of the gradient. */
	public volatile JLabel axisLabel;

	/** The combo box to select the axis of the gradient. */
	public volatile JComboBox<MultilingualItem> axisBox;

	/** The item of axisBox: X. */
	public volatile MultilingualItem xAxisItem;

	/** The item of axisBox: Y. */
	public volatile MultilingualItem yAxisItem;

	/** The item of axisBox: Z. */
	public volatile MultilingualItem zAxisItem;

	/** The item of axisBox: Scalar Dimension (4th column). */
	public volatile MultilingualItem scalarAxisItem;

	/** The checkbox to enable/disable the auto-ranging feature. */
	public volatile JCheckBox autoRangeBox;

	/** The label of the text field to input the minimum coordinate of the gradient. */
	public volatile JLabel minLabel;

	/** The text field to input the minimum coordinate of the gradient. */
	public volatile JTextField minField;

	/** The right click menu of minField. */
	public volatile TextRightClickMenu minFieldRightClickMenu;

	/** The label of the text field to input the maximum coordinate of the gradient. */
	public volatile JLabel maxLabel;

	/** The text field to input the maximum coordinate of the gradient. */
	public volatile JTextField maxField;

	/** The right click menu of maxField. */
	public volatile TextRightClickMenu maxFieldRightClickMenu;

	/** The container of UI components for setting the series filter. */
	public volatile SeriesFilterComponents seriesFilterComponents;

	/** The button to edit color. */
	public volatile JButton editColorButton;

	/** The button to reflect settings. */
	public volatile JButton setButton;


	/**
	 * Creates a new window.
	 *
	 * @param configuration The configuration of this application.
	 */
	public GradientOptionWindow() {

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
			//int topMargin = 5;
			int topMarginLong = 12;
			int topMargin = 5;
			int bottomMargin = 5;
			int leftMargin = 5;
			int leftMarginLong = 25;
			int rightMargin = 5;

			double leftColumnWeight = 0.0;
			double rightColumnWeight = 1.0;

			constraints.gridwidth = 1;
			constraints.gridx = 0;
			constraints.weightx = leftColumnWeight;

			// The label of the combo box to select the gradient axis.
			axisLabel = new JLabel();
			constraints.insets = new Insets(topMarginLong, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(axisLabel, constraints);
			basePanel.add(axisLabel);

			constraints.gridwidth = 1;
			constraints.gridx = 1;
			constraints.weightx = rightColumnWeight;

			// The combo box to select the gradient axis.
			axisBox = new JComboBox<MultilingualItem>();
			constraints.insets = new Insets(topMarginLong, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(axisBox, constraints);
			basePanel.add(axisBox);

			xAxisItem = new MultilingualItem();
			yAxisItem = new MultilingualItem();
			zAxisItem = new MultilingualItem();
			scalarAxisItem = new MultilingualItem();
			axisBox.addItem(xAxisItem);
			axisBox.addItem(yAxisItem);
			axisBox.addItem(zAxisItem);
			axisBox.addItem(scalarAxisItem);

			constraints.gridwidth = 2;
			constraints.gridx = 0;
			constraints.gridy++;
			constraints.weighty = 1.0;

			// The checkbox to enable/disable the auto-ranging feature.
			autoRangeBox = new JCheckBox();
			constraints.insets = new Insets(topMarginLong, leftMargin, 0, rightMargin);
			layout.setConstraints(autoRangeBox, constraints);
			basePanel.add(autoRangeBox);

			constraints.gridy++;

			constraints.gridwidth = 1;
			constraints.gridx = 0;
			constraints.weightx = leftColumnWeight;

			// The label of the text field to input the maximum coordinate of the gradient.
			maxLabel = new JLabel("Unconfigured");
			constraints.insets = new Insets(topMargin, leftMarginLong, 0, rightMargin);
			layout.setConstraints(maxLabel, constraints);
			basePanel.add(maxLabel);

			constraints.gridwidth = 1;
			constraints.gridx = 1;
			constraints.weightx = rightColumnWeight;

			// The text field to input the maximum coordinate of the gradient.
			maxField = new JTextField("Unconfigured");
			constraints.insets = new Insets(topMargin, leftMargin, 0, rightMargin);
			layout.setConstraints(maxField, constraints);
			basePanel.add(maxField);

			maxFieldRightClickMenu = new TextRightClickMenu();

			constraints.gridy++;

			constraints.gridwidth = 1;
			constraints.gridx = 0;
			constraints.weightx = leftColumnWeight;

			// The label of the text field to input the minimum coordinate of the gradient.
			minLabel = new JLabel("Unconfigured");
			constraints.insets = new Insets(topMargin, leftMarginLong, bottomMargin, rightMargin);
			layout.setConstraints(minLabel, constraints);
			basePanel.add(minLabel);

			constraints.gridwidth = 1;
			constraints.gridx = 1;
			constraints.weightx = rightColumnWeight;

			// The text field to input the minimum coordinate of the gradient.
			minField = new JTextField("Unconfigured");
			constraints.insets = new Insets(topMargin, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(minField, constraints);
			basePanel.add(minField);

			minFieldRightClickMenu = new TextRightClickMenu();

			constraints.gridy++;

			constraints.gridwidth = 2;
			constraints.gridx = 0;
			constraints.weighty = 1.0;


			// A separator.
			JSeparator separator = new JSeparator();
			constraints.insets = new Insets(topMarginLong, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(separator, constraints);
			basePanel.add(separator);

			// The panel and UI components for setting the series filter.
			seriesFilterComponents = new SeriesFilterComponents();
			constraints.insets = new Insets(topMarginLong, 0, bottomMargin, 0);
			layout.setConstraints(seriesFilterComponents.panel, constraints);
			basePanel.add(seriesFilterComponents.panel);

			constraints.gridy++;
			constraints.weighty = 1.0;

			editColorButton = new JButton("Unconfigured");
			constraints.insets = new Insets(topMarginLong, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(editColorButton, constraints);
			basePanel.add(editColorButton);

			constraints.gridy++;
			constraints.weighty = 1.0;

			// The button to reflect settings (SET button).
			setButton = new JButton("Unconfigured");
			constraints.insets = new Insets(topMargin, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(setButton, constraints);
			basePanel.add(setButton);
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

			// Update the series filter UI.
			ColorConfiguration colorConfig = this.configuration.getColorConfiguration();
			GradientColor[] gradientColors = colorConfig.getDataGradientColors();
			if (gradientColors.length != 1) {
				throw new IllegalStateException("Multi-gradient coloring is not supported for this version yet.");
			}
			GradientColor gradientColor = gradientColors[0];
			seriesFilterComponents.configure(
					this.configuration, gradientColor.getSeriesFilterMode(), gradientColor.getIndexSeriesFilter()
			);
		}

		/**
		 * Sets Japanese texts to the GUI components.
		 */
		private void setJapaneseTexts() {
			frame.setTitle("オプション設定: グラデーション彩色");

			axisLabel.setText("基準軸: ");
			xAxisItem.setText(GRADIENT_AXIS_X);
			yAxisItem.setText(GRADIENT_AXIS_Y);
			zAxisItem.setText(GRADIENT_AXIS_Z);
			scalarAxisItem.setText(GRADIENT_AXIS_SCALAR_JA);

			autoRangeBox.setText("範囲の自動調整");
			maxLabel.setText("上限: ");
			minLabel.setText("下限: ");

			editColorButton.setText("色の編集");
			setButton.setText("SET");
		}

		/**
		 * Sets English texts to the GUI components.
		 */
		private void setEnglishTexts() {
			frame.setTitle("Option Settings: Gradient Coloring");

			axisLabel.setText("Axis: ");
			xAxisItem.setText(GRADIENT_AXIS_X);
			yAxisItem.setText(GRADIENT_AXIS_Y);
			zAxisItem.setText(GRADIENT_AXIS_Z);
			scalarAxisItem.setText(GRADIENT_AXIS_SCALAR_EN);

			autoRangeBox.setText("Auto Range");
			maxLabel.setText("Max: ");
			minLabel.setText("Min: ");

			editColorButton.setText("Edit Color");
			setButton.setText("SET");
		}

		/**
		 * Set fonts to the components.
		 */
		private void setFonts() {
			FontConfiguration fontConfig = configuration.getFontConfiguration();
			Font uiBoldFont = fontConfig.getUIBoldFont();
			Font uiPlainFont = fontConfig.getUIPlainFont();

			axisLabel.setFont(uiBoldFont);
			axisBox.setFont(uiPlainFont);

			autoRangeBox.setFont(uiBoldFont);
			maxLabel.setFont(uiBoldFont);
			minLabel.setFont(uiBoldFont);
			maxField.setFont(uiPlainFont);
			minField.setFont(uiPlainFont);

			frame.setFont(uiBoldFont);
			editColorButton.setFont(uiBoldFont);
			setButton.setFont(uiBoldFont);
		}

		/**
		 * Updates the values of text fields, by the values stored in the configuration.
		 */
		private void updateValuesByConfiguration() {
			ColorConfiguration colorConfig = this.configuration.getColorConfiguration();

			// Extract a gradient color.
			GradientColor[] gradientColors = colorConfig.getDataGradientColors();
			if (gradientColors.length != 1) {
				throw new IllegalStateException("Multiple gradient coloring is not supported on this version yet.");
			}
			GradientColor gradientColor = gradientColors[0];

			// Extract an axis's gradient color of the above.
			AxisGradientColor[] axisGradientColors = gradientColor.getAxisGradientColors();
			if (axisGradientColors.length != 1) {
				throw new IllegalStateException("Multi-axis gradient color is not supported on this version yet.");
			}
			AxisGradientColor axisGradientColor = axisGradientColors[0];

			// Select the axis of axisBox on UI.
			setSelectedGradientAxis(axisGradientColor.getAxis());

			// Probably rounding is necessary in future.
			minField.setText(axisGradientColor.getMaximumBoundaryCoordinate().toString());
			maxField.setText(axisGradientColor.getMaximumBoundaryCoordinate().toString());

			// Enable/disable the auto range feature of the gradient.
			setAutoRangeEnabled(axisGradientColor.isBoundaryAutoRangeEnabled());
		}
	}


	/**
	 * Sets the selected item of axisBox field by an element of GradientAxis enum.
	 * This method is invokable only on the event-dispatch thread.
	 *
	 * @param gradientAxis The GradientAxis enum element corresponding the item of axisBox to be selected.
	 */
	public void setSelectedGradientAxis(GradientAxis gradientAxis) {
		if (!SwingUtilities.isEventDispatchThread()) {
			throw new IllegalStateException("This method is invokable only on the event-dispatch thread.");
		}
		switch (gradientAxis) {
			case X: {
				this.axisBox.setSelectedItem(this.xAxisItem);
				return;
			}
			case Y: {
				this.axisBox.setSelectedItem(this.yAxisItem);
				return;
			}
			case Z: {
				this.axisBox.setSelectedItem(this.zAxisItem);
				return;
			}
			case SCALAR: {
				this.axisBox.setSelectedItem(this.scalarAxisItem);
				return;
			}
			default: {
				throw new IllegalStateException("Unexpected gradient axis: " + gradientAxis);
			}
		}
	}

	/**
	 * Gets the selected item of axisBox field as an element of GradientAxis enum.
	 * This method is invokable only on the event-dispatch thread.
	 *
	 * @return The GradientAxis enum element corresponding to the selected item of axisBox.
	 */
	public GradientAxis getSelectedGradientAxis() {
		if (!SwingUtilities.isEventDispatchThread()) {
			throw new IllegalStateException("This method is invokable only on the event-dispatch thread.");
		}
		String selectedItemText = this.axisBox.getSelectedItem().toString();
		switch (selectedItemText) {
			case GRADIENT_AXIS_X: {
				return GradientAxis.X;
			}
			case GRADIENT_AXIS_Y: {
				return GradientAxis.Y;
			}
			case GRADIENT_AXIS_Z: {
				return GradientAxis.Z;
			}
			case GRADIENT_AXIS_SCALAR_JA:
			case GRADIENT_AXIS_SCALAR_EN: {
				return GradientAxis.SCALAR;
			}
			default: {
				throw new IllegalStateException("Unexpected gradient axis: " + selectedItemText);
			}
		}
	}

	/**
	 * Enable/disable autoRangeBox and minField/maxField.
	 * This method is invokable only on the event-dispatch thread.
	 *
	 * @param enabled Specify true to enable autoRangeBox.
	 */
	public void setAutoRangeEnabled(boolean enabled) {
		if (!SwingUtilities.isEventDispatchThread()) {
			throw new IllegalStateException("This method is invokable only on the event-dispatch thread.");
		}

		// Enable/disable the auto range feature of the gradient.
		autoRangeBox.setSelected(enabled);

		// Enable/disable minField/maxField.
		if (enabled) {
			minField.setEditable(false);
			maxField.setEditable(false);
			minField.setBackground(Color.LIGHT_GRAY);
			maxField.setBackground(Color.LIGHT_GRAY);
			minField.setForeground(Color.GRAY);
			maxField.setForeground(Color.GRAY);

		} else {
			minField.setEditable(true);
			maxField.setEditable(true);
			minField.setBackground(Color.WHITE);
			maxField.setBackground(Color.WHITE);
			minField.setForeground(Color.BLACK);
			maxField.setForeground(Color.BLACK);
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
