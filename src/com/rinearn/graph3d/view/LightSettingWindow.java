package com.rinearn.graph3d.view;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.LightConfiguration;
import com.rinearn.graph3d.config.FontConfiguration;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;

import static java.lang.Math.sqrt;
import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.acos;
import static java.lang.Math.atan2;
import static java.lang.Math.PI;


/**
 * The window of "Set Lighting Parameters" menu.
 */
public class LightSettingWindow {

	/** The default width [px] of the main window. */
	public static final int DEFAULT_WINDOW_WIDTH = 400;

	/** The default height [px] of the main window. */
	public static final int DEFAULT_WINDOW_HEIGHT = 570;

	/** The the max value (integer count) of the scroll bars in this window. */
	public static final int SCROLL_BAR_MAX_COUNT = 1000;

	/** The color of the scroll bars in this window. */
	public static final Color SCROLL_BAR_COLOR = new Color(100, 120, 200);

	/** The frame of this window. */
	public volatile JFrame frame;

	// The title label of "Reflection Parameters" section.
	public volatile JLabel reflectionSectionLabel;

	// The title label of "Ambient" parameter.
	public volatile JLabel ambientLabel;

	// The scroll bar of "Ambient" parameter.
	public volatile JScrollBar ambientBar;

	// The title label of "Diffuse" parameter.
	public volatile JLabel diffuseLabel;

	// The scroll bar of "Diffuse" parameter.
	public volatile JScrollBar diffuseBar;

	// The title label of "Diffractive" parameter.
	public volatile JLabel diffractiveLabel;

	// The scroll bar of "Diffractive" parameter.
	public volatile JScrollBar diffractiveBar;

	// The title label of "Specular Strength" parameter.
	public volatile JLabel specularStrengthLabel;

	// The scroll bar of "Specular Strength" parameter.
	public volatile JScrollBar specularStrengthBar;

	// The title label of "Specular Angle" parameter.
	public volatile JLabel specularAngleLabel;

	// The scroll bar of "Specular Angle" parameter.
	public volatile JScrollBar specularAngleBar;


	// The title label of "Light Angles" section.
	public volatile JLabel lightSectionLabel;

	// The title label of "Horizontal Angle" parameter.
	public volatile JLabel horizontalAngleLabel;

	// The scroll bar of "Horizontal Angle" parameter.
	public volatile JScrollBar horizontalAngleBar;

	// The title label of "Vertical Angle" parameter.
	public volatile JLabel verticalAngleLabel;

	// The scroll bar of "Vertical Angle" parameter.
	public volatile JScrollBar verticalAngleBar;


	/**
	 * Creates a new window.
	 * 
	 * @param configuration The configuration of this application.
	 */
	public LightSettingWindow() {

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
			int topMargin = 5;
			int bottomMargin = 15;
			int leftMargin = 5;
			int rightMargin = 5;
			int bottomMarginUnderSectionTitile = 20;
			int bottomMarginUnderSection = 25;
			int bottomMarginInSection = 5;
			int leftMarginLabelInSection = 20;
			int leftMarginBarInSection = 40;

			// Components for setting reflection parameters.
			{
				// Create the title label of "Reflection Parameters" section.
				reflectionSectionLabel = new JLabel("Unconfigured");
				reflectionSectionLabel.setVerticalAlignment(JLabel.BOTTOM);
				constraints.insets = new Insets(topMargin, leftMargin, bottomMarginUnderSectionTitile, rightMargin);
				layour.setConstraints(reflectionSectionLabel, constraints);
				basePanel.add(reflectionSectionLabel);

				constraints.gridy++;

				// Create the title label of "Ambient" section.
				ambientLabel = new JLabel("Unconfigured");
				constraints.insets = new Insets(0, leftMarginLabelInSection, 0, rightMargin);
				layour.setConstraints(ambientLabel, constraints);
				basePanel.add(ambientLabel);

				constraints.gridy++;

				// Create the scroll bar of "Ambient" section.
				ambientBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, SCROLL_BAR_MAX_COUNT);
				ambientBar.setBackground(SCROLL_BAR_COLOR);
				constraints.insets = new Insets(0, leftMarginBarInSection, bottomMarginInSection, rightMargin);
				layour.setConstraints(ambientBar, constraints);
				basePanel.add(ambientBar);

				constraints.gridy++;

				// Create the title label of "Diffuse" section.
				diffuseLabel = new JLabel("Unconfigured");
				constraints.insets = new Insets(0, leftMarginLabelInSection, 0, rightMargin);
				layour.setConstraints(diffuseLabel, constraints);
				basePanel.add(diffuseLabel);

				constraints.gridy++;

				// Create the scroll bar of "Diffuse" section.
				diffuseBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, SCROLL_BAR_MAX_COUNT);
				diffuseBar.setBackground(SCROLL_BAR_COLOR);
				constraints.insets = new Insets(0, leftMarginBarInSection, bottomMarginInSection, rightMargin);
				layour.setConstraints(diffuseBar, constraints);
				basePanel.add(diffuseBar);

				constraints.gridy++;

				// Create the title label of "Diffractive" section.
				diffractiveLabel = new JLabel("Unconfigured");
				constraints.insets = new Insets(0, leftMarginLabelInSection, 0, rightMargin);
				layour.setConstraints(diffractiveLabel, constraints);
				basePanel.add(diffractiveLabel);

				constraints.gridy++;

				// Create the scroll bar of "Diffractive" section.
				diffractiveBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, SCROLL_BAR_MAX_COUNT);
				diffractiveBar.setBackground(SCROLL_BAR_COLOR);
				constraints.insets = new Insets(0, leftMarginBarInSection, bottomMarginInSection, rightMargin);
				layour.setConstraints(diffractiveBar, constraints);
				basePanel.add(diffractiveBar);

				constraints.gridy++;

				// Create the title label of "Specular Strength" section.
				specularStrengthLabel = new JLabel("Unconfigured");
				constraints.insets = new Insets(0, leftMarginLabelInSection, 0, rightMargin);
				layour.setConstraints(specularStrengthLabel, constraints);
				basePanel.add(specularStrengthLabel);

				constraints.gridy++;

				// Create the scroll bar of "Specular Strength" section.
				specularStrengthBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, SCROLL_BAR_MAX_COUNT);
				specularStrengthBar.setBackground(SCROLL_BAR_COLOR);
				constraints.insets = new Insets(0, leftMarginBarInSection, bottomMarginInSection, rightMargin);
				layour.setConstraints(specularStrengthBar, constraints);
				basePanel.add(specularStrengthBar);

				constraints.gridy++;

				// Create the title label of "Specular Angle" section.
				specularAngleLabel = new JLabel("Unconfigured");
				constraints.insets = new Insets(0, leftMarginLabelInSection, 0, rightMargin);
				layour.setConstraints(specularAngleLabel, constraints);
				basePanel.add(specularAngleLabel);

				constraints.gridy++;

				// Create the scroll bar of "Specular Angle" section.
				specularAngleBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, SCROLL_BAR_MAX_COUNT);
				specularAngleBar.setBackground(SCROLL_BAR_COLOR);
				constraints.insets = new Insets(0, leftMarginBarInSection, bottomMarginUnderSection, rightMargin);
				layour.setConstraints(specularAngleBar, constraints);
				basePanel.add(specularAngleBar);

				constraints.gridy++;
			}


			// Components for setting reflection parameters.
			{
				// Create the title label of "Light Parameters" section.
				lightSectionLabel = new JLabel("Unconfigured");
				lightSectionLabel.setVerticalAlignment(JLabel.BOTTOM);
				constraints.insets = new Insets(topMargin, leftMargin, bottomMarginUnderSectionTitile, rightMargin);
				layour.setConstraints(lightSectionLabel, constraints);
				basePanel.add(lightSectionLabel);

				constraints.gridy++;

				// Create the title label of "Horizontal Angle" section.
				horizontalAngleLabel = new JLabel("Unconfigured");
				constraints.insets = new Insets(0, leftMarginLabelInSection, 0, rightMargin);
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

				// Create the title label of "Vertical Angle" section.
				verticalAngleLabel = new JLabel("Unconfigured");
				constraints.insets = new Insets(0, leftMarginLabelInSection, 0, rightMargin);
				layour.setConstraints(verticalAngleLabel, constraints);
				basePanel.add(verticalAngleLabel);

				constraints.gridy++;

				// Create the scroll bar of "Vertical Angle" section.
				verticalAngleBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, SCROLL_BAR_MAX_COUNT);
				verticalAngleBar.setBackground(SCROLL_BAR_COLOR);
				constraints.insets = new Insets(0, leftMarginBarInSection, bottomMargin, rightMargin);
				layour.setConstraints(verticalAngleBar, constraints);
				basePanel.add(verticalAngleBar);

				constraints.gridy++;
			}
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
			frame.setTitle("光の設定");
			reflectionSectionLabel.setText("- 反射パラメータ -");
			ambientLabel.setText("環境光反射:");
			diffuseLabel.setText("拡散反射:");
			diffractiveLabel.setText("回折反射:");
			specularStrengthLabel.setText("光沢の強さ:");
			specularAngleLabel.setText("光沢の拡がり角:");
			lightSectionLabel.setText("- 光源の方向 -");
			horizontalAngleLabel.setText("水平角度:");
			verticalAngleLabel.setText("垂直角度:");
		}

		/**
		 * Sets English texts to the GUI components.
		 */
		private void setEnglishTexts() {
			frame.setTitle("Set Lighting Parameters");
			reflectionSectionLabel.setText("- Reflection Parameters -");
			ambientLabel.setText("Ambient:");
			diffuseLabel.setText("Diffuse:");
			diffractiveLabel.setText("Diffractive:");
			specularStrengthLabel.setText("Specular Strength:");
			specularAngleLabel.setText("Specular Angle:");
			lightSectionLabel.setText("- Light Source Direction -");
			horizontalAngleLabel.setText("Horizontal Angle:");
			verticalAngleLabel.setText("Vertical Angle:");
		}

		/**
		 * Set fonts to the components.
		 */
		private void setFonts() {
			FontConfiguration fontConfig = configuration.getFontConfiguration();
			Font uiBoldFont = fontConfig.getUIBoldFont();

			reflectionSectionLabel.setFont(uiBoldFont);
			ambientLabel.setFont(uiBoldFont);
			diffuseLabel.setFont(uiBoldFont);
			diffractiveLabel.setFont(uiBoldFont);
			specularStrengthLabel.setFont(uiBoldFont);
			specularAngleLabel.setFont(uiBoldFont);
			lightSectionLabel.setFont(uiBoldFont);
			horizontalAngleLabel.setFont(uiBoldFont);
			verticalAngleLabel.setFont(uiBoldFont);
		}

		/**
		 * Updates the values of text fields, by the values stored in the configuration.
		 */
		private void updateValuesByConfiguration() {
			LightConfiguration lightConfig = this.configuration.getLightConfiguration();

			// Extract the current values of the reflection parameters from the configuration container.
			double ambient = lightConfig.getAmbientReflectionStrength();
			double diffuse = lightConfig.getDiffuseReflectionStrength();
			double diffractive = lightConfig.getDiffractiveReflectionStrength();
			double specularStrength = lightConfig.getSpecularReflectionStrength();
			double specularAngle = lightConfig.getSpecularReflectionAngle();

			// Scale the angler parameter into the range [0.0, 1.0]
			double specularAngle01 = specularAngle / (0.5 * Math.PI);

			// Convert the above values to the counts of the scroll bars.
			int ambientScrollCount = (int)Math.round(ambient * LightSettingWindow.SCROLL_BAR_MAX_COUNT);
			int diffuseScrollCount = (int)Math.round(diffuse * LightSettingWindow.SCROLL_BAR_MAX_COUNT);
			int diffractiveScrollCount = (int)Math.round(diffractive * LightSettingWindow.SCROLL_BAR_MAX_COUNT);
			int specularStrengthScrollCount = (int)Math.round(specularStrength * LightSettingWindow.SCROLL_BAR_MAX_COUNT);
			int specularAngleScrollCount = (int)Math.round((specularAngle01) * LightSettingWindow.SCROLL_BAR_MAX_COUNT);

			// Set the aboves to the scroll bars.
			ambientBar.setValue(ambientScrollCount);
			diffuseBar.setValue(diffuseScrollCount);
			diffractiveBar.setValue(diffractiveScrollCount);
			specularStrengthBar.setValue(specularStrengthScrollCount);
			specularAngleBar.setValue(specularAngleScrollCount);

			// -----
			// Compute the horizontal/vertical angle of the light direction vector (facing to the light source).
			// Note that, on this application, the light direction vector is defined on the view coordinate system.
			// So the X axis faces to the right side of the screen, Y faces to the upper side, and Z faces to the front side.
			// The vertical angle is the angle between the vector and Y (NOT Z) axis.
			// The horizontal angle is the angle between Z axis and the projected vector to the X-Z plane.
			// -----
			double horizontalAngle = 0.0;
			double verticalAngle = 0.0;
			{
				// Get the components of the light direction vector.
				double lightX = lightConfig.getLightSourceDirectionX();
				double lightY = lightConfig.getLightSourceDirectionY();
				double lightZ = lightConfig.getLightSourceDirectionZ();
				double vectorLength = sqrt(lightX * lightX + lightY * lightY + lightZ * lightZ);

				if (vectorLength != 0.0) {

					// Normalize the light direction vector.
					lightX /= vectorLength;
					lightY /= vectorLength;
					lightZ /= vectorLength;

					// Compute the vertical angle, which is the angle between the light direction vector and Y (NOT Z) axis.
					verticalAngle = acos(lightY);

					// If sin(verticalAngle) is 0, the vector is parallel with Y axis,
					// so the dimension of horizontal angle is degenerated.
					if (sin(verticalAngle) == 0.0) {
						horizontalAngle = 0.0;

					// If sin(verticalAngle) is non-zero, we can compute the horizontal angle from
					// the direction of the light direction vector projected to the Z-X plane.
					} else {
						horizontalAngle = atan2(lightX, lightZ); // Be careful of the order of the arguments!
						if (horizontalAngle < 0.0) {
							horizontalAngle += 2.0 * PI;
						}
					}
				}
			}

			// Convert the above values to the counts of the scroll bars.
			double normalizedHorizontalAngle = horizontalAngle /= 2.0 * PI;
			double normalizedVerticalAngle = verticalAngle /= PI;
			int horizontalAngleScrollCount = (int)Math.round(normalizedHorizontalAngle * LightSettingWindow.SCROLL_BAR_MAX_COUNT);
			int verticalAngleScrollCount = (int)Math.round(normalizedVerticalAngle * LightSettingWindow.SCROLL_BAR_MAX_COUNT);

			// Set the aboves to the scroll bars.
			horizontalAngleBar.setValue(horizontalAngleScrollCount);
			verticalAngleBar.setValue(verticalAngleScrollCount);
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
