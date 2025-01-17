package com.rinearn.graph3d.view;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.EnvironmentConfiguration;
import com.rinearn.graph3d.config.FontConfiguration;

import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


/**
 * The window of "x(y), y(t), z(t)" plot in "Math" menu.
 */
public class XtYtZtMathWindow {

	/** The default width [px] of this window. */
	public static final int DEFAULT_WINDOW_WIDTH = 400;

	/** The default height [px] of this window. */
	public static final int DEFAULT_WINDOW_HEIGHT = 450;

	/** The default value of the text field of the math expression x(t). */
	private static final String DEFAULT_XT_MATH_EXPRESSION = "sin(3.2 * t)";

	/** The default value of the text field of the math expression y(t). */
	private static final String DEFAULT_YT_MATH_EXPRESSION = "cos(3.2 * t)";

	/** The default value of the text field of the math expression z(t). */
	private static final String DEFAULT_ZT_MATH_EXPRESSION = "5.6 * t";

	/** The default value of the text field of the starting time. */
	private static final String DEFAULT_STARTING_TIME = "0.0";

	/** The default value of the text field of the starting time. */
	private static final String DEFAULT_ENDING_TIME = "10.0";

	/** The default value of the text field of the time resolution. */
	private static final String DEFAULT_TIME_RESOLUTION = "1000";

	/** The frame of this window. */
	public volatile JFrame frame;

	/** The title label of the math expression section. */
	public volatile JLabel mathExpressionLabel;

	/** The title label of the math expression "x(t)". */
	public volatile JLabel xtMathExpressionLabel;

	/** The title label of the math expression "y(t)". */
	public volatile JLabel ytMathExpressionLabel;

	/** The title label of the math expression "z(t)". */
	public volatile JLabel ztMathExpressionLabel;

	/** The text field of the math expression "x(t)". */
	public volatile JTextField xtMathExpressionField;

	/** The text field of the math expression "y(t)". */
	public volatile JTextField ytMathExpressionField;

	/** The text field of the math expression "z(t)". */
	public volatile JTextField ztMathExpressionField;

	/** The right-click menu of xtMathExpressionField. */
	public volatile TextRightClickMenu xtMathExpressionFieldRightClickMenu;

	/** The right-click menu of ytMathExpressionField. */
	public volatile TextRightClickMenu ytMathExpressionFieldRightClickMenu;

	/** The right-click menu of ztMathExpressionField. */
	public volatile TextRightClickMenu ztMathExpressionFieldRightClickMenu;

	/** The title label of the resolution section. */
	public volatile JLabel resolutionLabel;

	/** The title label of the time resolution. */
	public volatile JLabel timeResolutionLabel;

	/** The text field of the time resolution. */
	public volatile JTextField timeResolutionField;

	/** The right-click menu of timeResolutionField. */
	public volatile TextRightClickMenu timeResolutionFieldRightClickMenu;

	/** The title label of the time-max. */
	public volatile JLabel timeMaxLabel;

	/** The text field of the time-max. */
	public volatile JTextField timeMaxField;

	/** The right-click menu of timeMaxField. */
	public volatile TextRightClickMenu timeMaxFieldRightClickMenu;

	/** The title label of the time-min. */
	public volatile JLabel timeMinLabel;

	/** The text field of the time-min. */
	public volatile JTextField timeMinField;

	/** The right-click menu of timeMinField. */
	public volatile TextRightClickMenu timeMinFieldRightClickMenu;


	/** PLOT/UPDATE button. */
	public volatile JButton plotButton;

	/** The currentMode of z(x,y) math tool. */
	public enum Mode {

		/** The currentMode to plot a new expression. */
		PLOT,

		/** The currentMode to update an existing expression. */
		UPDATE,

		/** Represents the currentMode is not set yet, or has been unset. */
		UNSET;
	}

	/** The current currentMode of z(x,y) math tool. */
	private volatile Mode currentMode = Mode.UNSET;


	/**
	 * Creates a new window.
	 */
	public XtYtZtMathWindow() {

		// Initialize GUI components.
		this.initializeComponents();
	}


	/**
	 * Updates the UI depending on the currentMode of z(x,y) math tool.
	 * This method is invocable only from the event-dispatch thread.
	 *
	 * @param currentMode The currentMode of z(x,y) math tool.
	 * @param environmentConfig The environment configuration, containing the locale information.
	 */
	public synchronized void updateUIForMode(Mode mode, EnvironmentConfiguration environmentConfig) {
		if (!SwingUtilities.isEventDispatchThread()) {
			throw new IllegalStateException("This method is invocable only on the event-dispatch thread.");
		}
		this.currentMode = mode;

		// Update the label of PLOT/UPDATE button.
		if (environmentConfig.isLocaleJapanese()) {
			switch (mode) {
				case PLOT : {
					plotButton.setText("プロット");
					break;
				}
				case UPDATE : {
					plotButton.setText("更新");
					break;
				}
				case UNSET : {
					plotButton.setText("(未設定)");
					break;
				}
				default : {
					throw new IllegalStateException("Unexpected currentMode: " + mode);
				}
			}
		} else {
			switch (mode) {
				case PLOT : {
					plotButton.setText("PLOT");
					break;
				}
				case UPDATE : {
					plotButton.setText("UPDATE");
					break;
				}
				case UNSET : {
					plotButton.setText("(UNSET)");
					break;
				}
				default : {
					throw new IllegalStateException("Unexpected currentMode: " + mode);
				}
			}
		}
		plotButton.repaint();
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
			int bottomMarginUnderSectionTitile = 2;
			int bottomMarginUnderSection = 20;
			int bottomMarginInSection = 5;
			int leftMarginInSection = 20;
			int marginBetweenLabelAndField = 10;

			// Components for setting the math expression.
			{
				constraints.gridwidth = 2;

				// Create the title label of the math expression section.
				mathExpressionLabel = new JLabel("Unconfigured");
				mathExpressionLabel.setHorizontalAlignment(JLabel.LEFT);
				constraints.weightx = 0.05;
				constraints.gridx = 0;
				constraints.insets = new Insets(topMargin, leftMargin, bottomMarginUnderSectionTitile, rightMargin);
				layout.setConstraints(mathExpressionLabel, constraints);
				basePanel.add(mathExpressionLabel);

				constraints.gridy++;
				constraints.gridwidth = 1;

				// Create the title label of the math expression x(t).
				xtMathExpressionLabel = new JLabel("Unconfigured");
				xtMathExpressionLabel.setHorizontalAlignment(JLabel.RIGHT);
				constraints.weightx = 0.05;
				constraints.gridx = 0;
				constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMarginInSection, rightMargin);
				layout.setConstraints(xtMathExpressionLabel, constraints);
				basePanel.add(xtMathExpressionLabel);

				// Create the title label of the text field for inputting the math expression.
				xtMathExpressionField = new JTextField(DEFAULT_XT_MATH_EXPRESSION);
				constraints.weightx = 1.0;
				constraints.gridx = 1;
				constraints.insets = new Insets(topMargin, 0, bottomMarginInSection, rightMargin);
				layout.setConstraints(xtMathExpressionField, constraints);
				basePanel.add(xtMathExpressionField);

				// The right-click menu of the above text field.
				xtMathExpressionFieldRightClickMenu = new TextRightClickMenu();

				constraints.gridy++;

				// Create the title label of the math expression y(t).
				ytMathExpressionLabel = new JLabel("Unconfigured");
				ytMathExpressionLabel.setHorizontalAlignment(JLabel.RIGHT);
				constraints.weightx = 0.05;
				constraints.gridx = 0;
				constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMarginInSection, rightMargin);
				layout.setConstraints(ytMathExpressionLabel, constraints);
				basePanel.add(ytMathExpressionLabel);

				// Create the title label of the text field for inputting the math expression.
				ytMathExpressionField = new JTextField(DEFAULT_YT_MATH_EXPRESSION);
				constraints.weightx = 1.0;
				constraints.gridx = 1;
				constraints.insets = new Insets(topMargin, 0, bottomMarginInSection, rightMargin);
				layout.setConstraints(ytMathExpressionField, constraints);
				basePanel.add(ytMathExpressionField);

				// The right-click menu of the above text field.
				ytMathExpressionFieldRightClickMenu = new TextRightClickMenu();

				constraints.gridy++;

				// Create the title label of the math expression z(t).
				ztMathExpressionLabel = new JLabel("Unconfigured");
				ztMathExpressionLabel.setHorizontalAlignment(JLabel.RIGHT);
				constraints.weightx = 0.05;
				constraints.gridx = 0;
				constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMarginUnderSection, rightMargin);
				layout.setConstraints(ztMathExpressionLabel, constraints);
				basePanel.add(ztMathExpressionLabel);

				// Create the title label of the text field for inputting the math expression.
				ztMathExpressionField = new JTextField(DEFAULT_ZT_MATH_EXPRESSION);
				constraints.weightx = 1.0;
				constraints.gridx = 1;
				constraints.insets = new Insets(topMargin, 0, bottomMarginUnderSection, rightMargin);
				layout.setConstraints(ztMathExpressionField, constraints);
				basePanel.add(ztMathExpressionField);

				// The right-click menu of the above text field.
				ztMathExpressionFieldRightClickMenu = new TextRightClickMenu();

				constraints.gridy++;
			}

			// Components for setting the resolution.
			{
				constraints.gridwidth = 2;

				// Create the title label of the resolution section.
				resolutionLabel = new JLabel("Unconfigured");
				constraints.gridx = 0;
				resolutionLabel.setVerticalAlignment(JLabel.BOTTOM);
				constraints.insets = new Insets(topMargin, leftMargin, bottomMarginUnderSectionTitile, rightMargin);
				layout.setConstraints(resolutionLabel, constraints);
				basePanel.add(resolutionLabel);

				constraints.gridy++;
				constraints.gridwidth = 1;

				// Create the title label of the time resolution.
				timeMinLabel = new JLabel("Unconfigured");
				timeMinLabel.setHorizontalAlignment(JLabel.RIGHT);
				constraints.weightx = 0.05;
				constraints.gridx = 0;
				constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMarginInSection, marginBetweenLabelAndField);
				layout.setConstraints(timeMinLabel, constraints);
				basePanel.add(timeMinLabel);

				// Create the title label of the time resolution.
				timeMinField = new JTextField(DEFAULT_STARTING_TIME);
				constraints.weightx = 1.0;
				constraints.gridx = 1;
				constraints.insets = new Insets(topMargin, 0, bottomMarginInSection, rightMargin);
				layout.setConstraints(timeMinField, constraints);
				basePanel.add(timeMinField);

				// The right-click menu of the above text field.
				timeMaxFieldRightClickMenu = new TextRightClickMenu();

				constraints.gridy++;

				// Create the title label of the time-max.
				timeMaxLabel = new JLabel("Unconfigured");
				timeMaxLabel.setHorizontalAlignment(JLabel.RIGHT);
				constraints.weightx = 0.05;
				constraints.gridx = 0;
				constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMarginInSection, marginBetweenLabelAndField);
				layout.setConstraints(timeMaxLabel, constraints);
				basePanel.add(timeMaxLabel);

				// Create the title label of the time resolution.
				timeMaxField = new JTextField(DEFAULT_ENDING_TIME);
				constraints.weightx = 1.0;
				constraints.gridx = 1;
				constraints.insets = new Insets(topMargin, 0, bottomMarginInSection, rightMargin);
				layout.setConstraints(timeMaxField, constraints);
				basePanel.add(timeMaxField);

				// The right-click menu of the above text field.
				timeMinFieldRightClickMenu = new TextRightClickMenu();

				constraints.gridy++;

				// Create the title label of the time-min.
				timeResolutionLabel = new JLabel("Unconfigured");
				timeResolutionLabel.setHorizontalAlignment(JLabel.RIGHT);
				constraints.weightx = 0.05;
				constraints.gridx = 0;
				constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMarginUnderSection, marginBetweenLabelAndField);
				layout.setConstraints(timeResolutionLabel, constraints);
				basePanel.add(timeResolutionLabel);

				// Create the title label of the time resolution.
				timeResolutionField = new JTextField(DEFAULT_TIME_RESOLUTION);
				constraints.weightx = 1.0;
				constraints.gridx = 1;
				constraints.insets = new Insets(topMargin, 0, bottomMarginUnderSection, rightMargin);
				layout.setConstraints(timeResolutionField, constraints);
				basePanel.add(timeResolutionField);

				// The right-click menu of the above text field.
				timeResolutionFieldRightClickMenu = new TextRightClickMenu();

				constraints.gridwidth = 1;
				constraints.gridy++;
			}

			constraints.gridwidth = 2;
			constraints.gridx = 0;

			// PLOT/UPDATE button.
			plotButton = new JButton("Unconfigured");
			constraints.insets = new Insets(0, leftMargin, bottomMargin, rightMargin);
			layout.setConstraints(plotButton, constraints);
			basePanel.add(plotButton);
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

			// Update the right-click menus.
			xtMathExpressionFieldRightClickMenu.configure(this.configuration);
			ytMathExpressionFieldRightClickMenu.configure(this.configuration);
			ztMathExpressionFieldRightClickMenu.configure(this.configuration);
			timeMinFieldRightClickMenu.configure(this.configuration);
			timeMaxFieldRightClickMenu.configure(this.configuration);
			timeResolutionFieldRightClickMenu.configure(this.configuration);
		}

		/**
		 * Sets Japanese texts to the GUI components.
		 */
		private void setJapaneseTexts() {
			frame.setTitle("x(t),y(t),z(t) プロット");
			mathExpressionLabel.setText("- 数式 -");
			xtMathExpressionLabel.setText("x(t) =");
			ytMathExpressionLabel.setText("y(t) =");
			ztMathExpressionLabel.setText("z(t) =");
			resolutionLabel.setText("- 離散化 -");
			timeMinLabel.setText("始点時刻:");
			timeMaxLabel.setText("終点時刻:");
			timeResolutionLabel.setText("時刻点数:");

			// Update the text of PLOT/UPDATE button.
			updateUIForMode(currentMode, this.configuration.getEnvironmentConfiguration());
		}

		/**
		 * Sets English texts to the GUI components.
		 */
		private void setEnglishTexts() {
			frame.setTitle("x(t),y(t),z(t) Plot");
			mathExpressionLabel.setText("- Math Expression -");
			xtMathExpressionLabel.setText("x(t) =");
			ytMathExpressionLabel.setText("y(t) =");
			ztMathExpressionLabel.setText("z(t) =");
			resolutionLabel.setText("- Discretization -");
			timeMinLabel.setText("Starting Time:");
			timeMaxLabel.setText("Ending Time:");
			timeResolutionLabel.setText("Number of Points:");

			// Update the text of PLOT/UPDATE button.
			updateUIForMode(currentMode, this.configuration.getEnvironmentConfiguration());
		}

		/**
		 * Set fonts to the components.
		 */
		private void setFonts() {
			FontConfiguration fontConfig = configuration.getFontConfiguration();
			Font uiBoldFont = fontConfig.getUIBoldFont();
			Font uiPlainFont = fontConfig.getUIPlainFont();

			frame.setFont(uiBoldFont);

			mathExpressionLabel.setFont(uiBoldFont);

			xtMathExpressionLabel.setFont(uiBoldFont);
			ytMathExpressionLabel.setFont(uiBoldFont);
			ztMathExpressionLabel.setFont(uiBoldFont);
			xtMathExpressionField.setFont(uiPlainFont);
			ytMathExpressionField.setFont(uiPlainFont);
			ztMathExpressionField.setFont(uiPlainFont);

			resolutionLabel.setFont(uiBoldFont);

			timeMinLabel.setFont(uiBoldFont);
			timeMinField.setFont(uiPlainFont);
			timeMaxLabel.setFont(uiBoldFont);
			timeMaxField.setFont(uiPlainFont);
			timeResolutionLabel.setFont(uiBoldFont);
			timeResolutionField.setFont(uiPlainFont);

			plotButton.setFont(uiBoldFont);
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
