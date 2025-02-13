package com.rinearn.graph3d.view;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.font.FontConfiguration;
import com.rinearn.graph3d.config.plotter.PlotterConfiguration;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;


/**
 * The window of "With Meshes" option.
 */
public final class MeshOptionWindow {

	/** The default width [px] of this window. */
	public static final int DEFAULT_WINDOW_WIDTH = 340;

	/** The default height [px] of this window. */
	public static final int DEFAULT_WINDOW_HEIGHT = 240;

	/** The frame of this window. */
	public volatile JFrame frame;

	/** The label of the text field to input the line width.  */
	public volatile JLabel lineWidthLabel;

	/** The text field to input the line width.  */
	public volatile JTextField lineWidthField;

	/** The right-click menu of the text field to input the line width. */
	public volatile TextRightClickMenu lineWidthFieldRightClickMenu;

	/** The container of UI components for setting the series filter. */
	public volatile SeriesFilterComponents seriesFilterComponents;

	/** The button to reflect settings. */
	public volatile JButton setButton;


	/**
	 * Creates a new window.
	 *
	 * @param configuration The configuration of this application.
	 */
	public MeshOptionWindow() {

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
			int bottomMargin = 5;
			int leftMargin = 5;
			int rightMargin = 5;

			double leftColumnWeight = 0.0;
			double rightColumnWeight = 1.0;

			constraints.gridwidth = 1;
			constraints.gridx = 0;
			constraints.weightx = leftColumnWeight;

			// The label of the text field to input the line width.
			lineWidthLabel = new JLabel();
			constraints.insets = new Insets(topMarginLong, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(lineWidthLabel, constraints);
			basePanel.add(lineWidthLabel);

			constraints.gridwidth = 1;
			constraints.gridx = 1;
			constraints.weightx = rightColumnWeight;

			// The text field to input the line width.
			lineWidthField = new JTextField();
			constraints.insets = new Insets(topMarginLong, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(lineWidthField, constraints);
			basePanel.add(lineWidthField);

			lineWidthFieldRightClickMenu = new TextRightClickMenu();

			constraints.gridwidth = 2;
			constraints.gridx = 0;
			constraints.gridy++;
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

			// The button to reflect settings (SET button).
			setButton = new JButton("Unconfigured");
			constraints.insets = new Insets(topMarginLong, leftMargin, bottomMargin, rightMargin);
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

			// Update the right-click menus.
			lineWidthFieldRightClickMenu.configure(this.configuration);

			// Update the series filter UI.
			PlotterConfiguration plotterConfig = this.configuration.getPlotterConfiguration();
			PlotterConfiguration.MeshPlotterConfiguration meshPlotterConfig = plotterConfig.getMeshPlotterConfiguration();
			seriesFilterComponents.configure(
					this.configuration, meshPlotterConfig.getSeriesFilterMode(), meshPlotterConfig.getIndexSeriesFilter()
			);
		}

		/**
		 * Sets Japanese texts to the GUI components.
		 */
		private void setJapaneseTexts() {
			frame.setTitle("オプション設定: メッシュプロット");
			lineWidthLabel.setText("線の幅: ");
			setButton.setText("SET");
		}

		/**
		 * Sets English texts to the GUI components.
		 */
		private void setEnglishTexts() {
			frame.setTitle("Option Settings: With Meshes");
			lineWidthLabel.setText("Line Width: ");
			setButton.setText("SET");
		}

		/**
		 * Set fonts to the components.
		 */
		private void setFonts() {
			FontConfiguration fontConfig = configuration.getFontConfiguration();
			Font uiBoldFont = fontConfig.getUIBoldFont();
			Font uiPlainFont = fontConfig.getUIPlainFont();

			lineWidthLabel.setFont(uiBoldFont);
			lineWidthField.setFont(uiPlainFont);

			frame.setFont(uiBoldFont);
			setButton.setFont(uiBoldFont);
		}

		/**
		 * Updates the values of text fields, by the values stored in the configuration.
		 */
		private void updateValuesByConfiguration() {
			PlotterConfiguration plotterConfig = this.configuration.getPlotterConfiguration();
			PlotterConfiguration.MeshPlotterConfiguration meshPlotterConfig = plotterConfig.getMeshPlotterConfiguration();
			DecimalFormat formatter = new DecimalFormat("#0.0#####");

			lineWidthField.setText(formatter.format(meshPlotterConfig.getLineWidth()));
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
