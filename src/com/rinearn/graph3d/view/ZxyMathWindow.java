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
 * The window of "z(x,y)" plot in "Math" menu.
 */
public class ZxyMathWindow {

	/** The default width [px] of this window. */
	public static final int DEFAULT_WINDOW_WIDTH = 400;

	/** The default height [px] of this window. */
	public static final int DEFAULT_WINDOW_HEIGHT = 300;

	/** The default value of the text field of the math expression z(x,y). */
	private static final String DEFAULT_Z_MATH_EXPRESSION = "( sin(3.2 * x) + cos(2.7 * y) ) / 2";

	/** The default value of the text field of the X-resolution. */
	private static final String DEFAULT_X_RESOLUTION = "80";

	/** The default value of the text field of the Y-resolution. */
	private static final String DEFAULT_Y_RESOLUTION = "80";

	/** The frame of this window. */
	public volatile JFrame frame;

	/** The title label of the math expression section. */
	public volatile JLabel mathExpressionLabel;

	/** The title label of the math expression "z(x,y)". */
	public volatile JLabel zMathExpressionLabel;

	/** The text field of the math expression. */
	public volatile JTextField zMathExpressionField;

	/** The right-click menu of zMathExpressionField. */
	public volatile TextRightClickMenu zMathExpressionFieldRightClickMenu;

	/** The title label of the resolution section. */
	public volatile JLabel resolutionLabel;

	/** The title label of the X-resolution. */
	public volatile JLabel xResolutionLabel;

	/** The text field of the X-resolution. */
	public volatile JTextField xResolutionField;

	/** The right-click menu of xResolutionField. */
	public volatile TextRightClickMenu xResolutionFieldRightClickMenu;

	/** The title label of the Y-resolution. */
	public volatile JLabel yResolutionLabel;

	/** The text field of the Y-resolution. */
	public volatile JTextField yResolutionField;

	/** The right-click menu of yResolutionField. */
	public volatile TextRightClickMenu yResolutionFieldRightClickMenu;

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
	public ZxyMathWindow() {

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
			int bottomMarginUnderSectionTitile = 5;
			int bottomMarginUnderSection = 25;
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

				// Create the title label of the math expression z(x,y).
				zMathExpressionLabel = new JLabel("Unconfigured");
				zMathExpressionLabel.setHorizontalAlignment(JLabel.RIGHT);
				constraints.weightx = 0.05;
				constraints.gridx = 0;
				constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMarginUnderSection, rightMargin);
				layout.setConstraints(zMathExpressionLabel, constraints);
				basePanel.add(zMathExpressionLabel);

				// Create the title label of the text field for inputting the math expression.
				zMathExpressionField = new JTextField(DEFAULT_Z_MATH_EXPRESSION);
				constraints.weightx = 1.0;
				constraints.gridx = 1;
				constraints.insets = new Insets(topMargin, 0, bottomMarginUnderSection, rightMargin);
				layout.setConstraints(zMathExpressionField, constraints);
				basePanel.add(zMathExpressionField);

				// The right-click menu of the above text field.
				zMathExpressionFieldRightClickMenu = new TextRightClickMenu();

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

				// Create the title label of the X-resolution.
				xResolutionLabel = new JLabel("Unconfigured");
				xResolutionLabel.setHorizontalAlignment(JLabel.RIGHT);
				constraints.weightx = 0.05;
				constraints.gridx = 0;
				constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMarginInSection, marginBetweenLabelAndField);
				layout.setConstraints(xResolutionLabel, constraints);
				basePanel.add(xResolutionLabel);

				// Create the title label of the X-resolution.
				xResolutionField = new JTextField(DEFAULT_X_RESOLUTION);
				constraints.weightx = 1.0;
				constraints.gridx = 1;
				constraints.insets = new Insets(topMargin, 0, bottomMarginInSection, rightMargin);
				layout.setConstraints(xResolutionField, constraints);
				basePanel.add(xResolutionField);

				// The right-click menu of the above text field.
				xResolutionFieldRightClickMenu = new TextRightClickMenu();

				constraints.gridy++;
				constraints.gridwidth = 1;

				// Create the title label of the Y-resolution.
				yResolutionLabel = new JLabel("Unconfigured");
				yResolutionLabel.setHorizontalAlignment(JLabel.RIGHT);
				constraints.weightx = 0.05;
				constraints.gridx = 0;
				constraints.insets = new Insets(topMargin, leftMarginInSection, bottomMarginUnderSection, marginBetweenLabelAndField);
				layout.setConstraints(yResolutionLabel, constraints);
				basePanel.add(yResolutionLabel);

				// Create the title label of the Y-resolution.
				yResolutionField = new JTextField(DEFAULT_Y_RESOLUTION);
				constraints.weightx = 1.0;
				constraints.gridx = 1;
				constraints.insets = new Insets(topMargin, 0, bottomMarginUnderSection, rightMargin);
				layout.setConstraints(yResolutionField, constraints);
				basePanel.add(yResolutionField);

				// The right-click menu of the above text field.
				yResolutionFieldRightClickMenu = new TextRightClickMenu();

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
			zMathExpressionFieldRightClickMenu.configure(this.configuration);
			xResolutionFieldRightClickMenu.configure(this.configuration);
			yResolutionFieldRightClickMenu.configure(this.configuration);
		}

		/**
		 * Sets Japanese texts to the GUI components.
		 */
		private void setJapaneseTexts() {
			frame.setTitle("z(x,y) プロット");
			mathExpressionLabel.setText("- 数式 -");
			zMathExpressionLabel.setText("z(x,y) =");
			resolutionLabel.setText("- 解像度 -");
			xResolutionLabel.setText("Xメッシュ:");
			yResolutionLabel.setText("Yメッシュ:");

			// Update the text of PLOT/UPDATE button.
			updateUIForMode(currentMode, this.configuration.getEnvironmentConfiguration());
		}

		/**
		 * Sets English texts to the GUI components.
		 */
		private void setEnglishTexts() {
			frame.setTitle("z(x,y) Plot");
			mathExpressionLabel.setText("- Math Expression -");
			zMathExpressionLabel.setText("z(x,y) =");
			resolutionLabel.setText("- Resolution -");
			xResolutionLabel.setText("X Mesh:");
			yResolutionLabel.setText("Y Mesh:");

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

			zMathExpressionLabel.setFont(uiBoldFont);
			zMathExpressionField.setFont(uiPlainFont);

			resolutionLabel.setFont(uiBoldFont);

			xResolutionLabel.setFont(uiBoldFont);
			xResolutionField.setFont(uiPlainFont);
			yResolutionLabel.setFont(uiBoldFont);
			yResolutionField.setFont(uiPlainFont);

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
