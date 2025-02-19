package com.rinearn.graph3d.view;

import java.awt.Font;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;

import java.awt.Color;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.color.ColorConfiguration;
import com.rinearn.graph3d.config.scale.ScaleConfiguration;
import com.rinearn.graph3d.config.scale.AxisScaleConfiguration;
import com.rinearn.graph3d.config.font.FontConfiguration;
import com.rinearn.graph3d.config.frame.FrameConfiguration;
import com.rinearn.graph3d.config.label.LabelConfiguration;
import com.rinearn.graph3d.config.plotter.PlotterConfiguration;


/**
 * The main menu, displayed on the menu bar of the main window.
 */
public class MainMenu {

	/** The menu bar at the top of the window. */
	public volatile JMenuBar menuBar;


	/** "File" menu on the menu bar. */
	public volatile JMenu fileMenu;

	/** "File" > "Open File" menu on the menu bar. */
	public volatile JMenuItem openDataFileMenuItem;

	/** "File" > "Paste Data" menu on the menu bar. */
	public volatile JMenuItem pasteDataTextMenuItem;

	/** "File" > "Save Image" menu on the menu bar. */
	public volatile JMenuItem saveImageFileMenuItem;

	/** "File" > "Copy Image" menu on the menu bar. */
	public volatile JMenuItem copyImageMenuItem;

	/** "File" > "Clear Data/Files" */
	public volatile JMenuItem clearDataFileMenuItem;


	/** "Math" menu on the menu bar. */
	public volatile JMenu mathMenu;

	/** "Math" > "Modify Math Expression" menu on the menu bar. */
	public volatile JMenuItem modifyMathMenuItem;

	/** "Math" > "Remove Math Expression" menu on the menu bar. */
	public volatile JMenuItem removeMathMenuItem;

	/** "Math" > "Clear Math Expressions" menu on the menu bar. */
	public volatile JMenuItem clearMathMenuItem;

	/** "Math" > "Plot z(x,y)" menu on the menu bar. */
	public volatile JMenuItem zxyMathMenuItem;

	/** "Math" > "Plot x(t),y(t),z(t)" menu on the menu bar. */
	public volatile JMenuItem xtYtZtMathMenuItem;


	/** "Edit" menu on the menu bar. */
	public volatile JMenu editMenu;

	/** "Edit" > "Set Ranges" menu item on the menu bar. */
	public volatile JMenuItem rangeSettingMenuItem;

	/** "Edit" > "Set Labels" menu item on the menu bar. */
	public volatile JMenuItem labelSettingMenuItem;

	/** "Edit" > "Set Fonts" menu item on the menu bar. */
	public volatile JMenuItem fontSettingMenuItem;

	/** "Edit" > "Set Camera" menu item on the menu bar. */
	public volatile JMenuItem cameraSettingMenuItem;

	/** "Edit" > "Set Light" menu item on the menu bar. */
	public volatile JMenuItem lightSettingMenuItem;

	/** "Edit" > "Set Scale" menu item on the menu bar. */
	public volatile JMenuItem scaleSettingMenuItem;

	/** "Edit" > "Set Renderer" menu item on the menu bar. */
	public volatile JMenuItem rendererSettingMenuItem;

	/** "Edit" > "Reset Settings" */
	public volatile JMenuItem resetSettingMenuItem;

	/** "Edit" > "Clear All" */
	public volatile JMenuItem clearAllMenuItem;


	/** "Options" menu on the menu bar. */
	public volatile JMenu optionsMenu;

	/** "Options" > "With Points" menu item on the menu bar.  */
	public volatile JCheckBoxMenuItem pointOptionMenuItem;

	/** "Options" > "With Lines" menu item on the menu bar.  */
	public volatile JCheckBoxMenuItem lineOptionMenuItem;

	/** "Options" > "With Meshes" menu item on the menu bar.  */
	public volatile JCheckBoxMenuItem meshOptionMenuItem;

	/** "Options" > "With Surfaces" menu item on the menu bar.  */
	public volatile JCheckBoxMenuItem surfaceOptionMenuItem;


	/** "Options" > "Log X" menu item on the menu bar.  */
	public volatile JCheckBoxMenuItem logXOptionMenuItem;

	/** "Options" > "Log Y" menu item on the menu bar.  */
	public volatile JCheckBoxMenuItem logYOptionMenuItem;

	/** "Options" > "Log Z" menu item on the menu bar.  */
	public volatile JCheckBoxMenuItem logZOptionMenuItem;

	/** "Options" > "Invert X" menu item on the menu bar.  */
	public volatile JCheckBoxMenuItem invertXOptionMenuItem;

	/** "Options" > "Invert Y" menu item on the menu bar.  */
	public volatile JCheckBoxMenuItem invertYOptionMenuItem;

	/** "Options" > "Invert Z" menu item on the menu bar.  */
	public volatile JCheckBoxMenuItem invertZOptionMenuItem;


	/** "Options" > "Black Screen" menu item on the menu bar.  */
	public volatile JCheckBoxMenuItem blackScreenOptionMenuItem;

	/** "Options" > "Gradient Colors" menu item on the menu bar.  */
	public volatile JCheckBoxMenuItem gradientOptionMenuItem;

	/** "Options" > "Legends" menu item on the menu bar.  */
	public volatile JCheckBoxMenuItem legendOptionMenuItem;

	/** "Options" > "Grid Lines" menu item on the menu bar.  */
	public volatile JCheckBoxMenuItem gridLinesOptionMenuItem;

	/** "Options" > "Frame Lines" menu item on the menu bar.  */
	public volatile JCheckBoxMenuItem frameLinesOptionMenuItem;

	/** "Options" > "Scale Ticks" menu item on the menu bar.  */
	public volatile JCheckBoxMenuItem scaleTicksOptionMenuItem;

	/** "Options" > "Axis Labels" menu item on the menu bar.  */
	public volatile JCheckBoxMenuItem axisLabelsOptionMenuItem;


	/**
	 * Creates a new main menu.
	 */
	public MainMenu() {

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

			// The menu bar:
			menuBar = new JMenuBar();

			// "File" menu:
			{
				fileMenu = new JMenu("Unconfigured");
				menuBar.add(fileMenu);

				// "File" > "Open File"
				openDataFileMenuItem = new JMenuItem("Unconfigured");
				fileMenu.add(openDataFileMenuItem);

				// "File" > "Paste Data Text"
				pasteDataTextMenuItem = new JMenuItem("Unconfigured");
				fileMenu.add(pasteDataTextMenuItem);

				// ---
				fileMenu.addSeparator();

				// "File" > "Save Image"
				saveImageFileMenuItem = new JMenuItem("Unconfigured");
				fileMenu.add(saveImageFileMenuItem);

				// "File" > "Copy Image"
				copyImageMenuItem = new JMenuItem("Unconfigured");
				fileMenu.add(copyImageMenuItem);

				// ---
				fileMenu.addSeparator();

				// "File" > "Clear Data/Files"
				clearDataFileMenuItem = new JMenuItem("Unconfigured");
				fileMenu.add(clearDataFileMenuItem);
			}

			// "Math" menu:
			{
				mathMenu = new JMenu("Unconfigured");
				menuBar.add(mathMenu);

				// "Math" > "Plot z(x,y)" menu item:
				zxyMathMenuItem = new JMenuItem("Unconfigured");
				mathMenu.add(zxyMathMenuItem);

				// "Math" > "Plot x(t),y(t),z(t)" menu item:
				xtYtZtMathMenuItem = new JMenuItem("Unconfigured");
				mathMenu.add(xtYtZtMathMenuItem);

				// ---
				mathMenu.addSeparator();

				// "Math" > "Modify Math Expression" menu item.
				modifyMathMenuItem = new JMenuItem("Unconfigured");
				mathMenu.add(modifyMathMenuItem);

				// "Math" > "Remove Math Expression" menu item:
				removeMathMenuItem = new JMenuItem("Unconfigured");
				mathMenu.add(removeMathMenuItem);

				// ---
				mathMenu.addSeparator();

				// "Math" > "Clear Math Expressions" menu item:
				clearMathMenuItem = new JMenuItem("Unconfigured");
				mathMenu.add(clearMathMenuItem);
			}

			// "Edit" menu:
			{
				editMenu = new JMenu("Unconfigured");
				menuBar.add(editMenu);

				// "Edit" > "Set Ranges" menu item:
				rangeSettingMenuItem = new JMenuItem("Unconfigured");
				editMenu.add(rangeSettingMenuItem);

				// "Edit" > "Set Labels" menu item:
				labelSettingMenuItem = new JMenuItem("Unconfigured");
				editMenu.add(labelSettingMenuItem);

				// "Edit" > "Set Fonts" menu item:
				fontSettingMenuItem = new JMenuItem("Unconfigured");
				editMenu.add(fontSettingMenuItem);

				// "Edit" > "Set Camera" menu item:
				cameraSettingMenuItem = new JMenuItem("Unconfigured");
				editMenu.add(cameraSettingMenuItem);

				// "Edit" > "Set Light" menu item:
				lightSettingMenuItem = new JMenuItem("Unconfigured");
				editMenu.add(lightSettingMenuItem);

				// "Edit" > "Set Scale" menu item:
				scaleSettingMenuItem = new JMenuItem("Unconfigured");
				editMenu.add(scaleSettingMenuItem);

				// "Edit" > "Set Renderer" menu item:
				rendererSettingMenuItem = new JMenuItem("Unconfigured");
				editMenu.add(rendererSettingMenuItem);

				// ---
				editMenu.addSeparator();

				// "Edit" > "Reset Settings" menu item:
				resetSettingMenuItem = new JMenuItem("Unconfigured");
				editMenu.add(resetSettingMenuItem);

				// "Edit" > "Clear All" menu item:
				clearAllMenuItem = new JMenuItem("Unconfigured");
				editMenu.add(clearAllMenuItem);
			}

			// "Options" menu:
			{
				optionsMenu = new JMenu("Unconfigured");
				menuBar.add(optionsMenu);

				// "Options" > "With Points" menu item:
				pointOptionMenuItem = new JCheckBoxMenuItem("Unconfigured");
				optionsMenu.add(pointOptionMenuItem);

				// "Options" > "With Lines" menu item:
				lineOptionMenuItem = new JCheckBoxMenuItem("Unconfigured");
				optionsMenu.add(lineOptionMenuItem);

				// "Options" > "With Meshes" menu item:
				meshOptionMenuItem = new JCheckBoxMenuItem("Unconfigured");
				optionsMenu.add(meshOptionMenuItem);

				// "Options" > "With Surfaces" menu item:
				surfaceOptionMenuItem = new JCheckBoxMenuItem("Unconfigured");
				optionsMenu.add(surfaceOptionMenuItem);

				// --- Separator ---
				optionsMenu.add(new JSeparator());

				logXOptionMenuItem = new JCheckBoxMenuItem("Unconfigured");
				optionsMenu.add(logXOptionMenuItem);

				logYOptionMenuItem = new JCheckBoxMenuItem("Unconfigured");
				optionsMenu.add(logYOptionMenuItem);

				logZOptionMenuItem = new JCheckBoxMenuItem("Unconfigured");
				optionsMenu.add(logZOptionMenuItem);

				invertXOptionMenuItem = new JCheckBoxMenuItem("Unconfigured");
				optionsMenu.add(invertXOptionMenuItem);

				invertYOptionMenuItem = new JCheckBoxMenuItem("Unconfigured");
				optionsMenu.add(invertYOptionMenuItem);

				invertZOptionMenuItem = new JCheckBoxMenuItem("Unconfigured");
				optionsMenu.add(invertZOptionMenuItem);

				// --- Separator ---
				optionsMenu.add(new JSeparator());

				// "Options" > "Legends" menu item:
				legendOptionMenuItem = new JCheckBoxMenuItem("Unconfigured");
				optionsMenu.add(legendOptionMenuItem);

				// "Options" > "Black Screen" menu item:
				blackScreenOptionMenuItem = new JCheckBoxMenuItem("Unconfigured");
				optionsMenu.add(blackScreenOptionMenuItem);

				// "Options" > "Gradient Coloring" menu item:
				gradientOptionMenuItem = new JCheckBoxMenuItem("Unconfigured");
				optionsMenu.add(gradientOptionMenuItem);

				// "Options" > "Grid Lines" menu item:
				gridLinesOptionMenuItem = new JCheckBoxMenuItem("Grid Lines");
				optionsMenu.add(gridLinesOptionMenuItem);

				// "Options" > "Frame Lines" menu item:
				frameLinesOptionMenuItem = new JCheckBoxMenuItem("Frame Lines");
				optionsMenu.add(frameLinesOptionMenuItem);

				// "Options" > "Scale Ticks" menu item:
				scaleTicksOptionMenuItem = new JCheckBoxMenuItem("Scale Ticks");
				optionsMenu.add(scaleTicksOptionMenuItem);

				// "Options" > "Axis Labels" menu item:
				axisLabelsOptionMenuItem = new JCheckBoxMenuItem("Axis Labels");
				optionsMenu.add(axisLabelsOptionMenuItem);
			}
		}
	}


	// !!!!! IMPORTANT NOTE !!!!!
	//
	// Don't put "synchronized" modifier to UI-operation methods,
	// such as configure(), resize(), repaintScreen(), setScreenImage(image), etc.
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

			// Set the selection-states of the menu items in "Option" menu.
			this.setOptionStates();
		}

		/**
		 * Sets Japanese texts to the GUI components.
		 */
		private void setJapaneseTexts() {

			// "File" menu and sub menu items.
			{
				fileMenu.setText("ファイル");
				openDataFileMenuItem.setText("ファイルを開く");
				pasteDataTextMenuItem.setText("データを貼り付け");
				saveImageFileMenuItem.setText("画像を保存");
				copyImageMenuItem.setText("画像をコピー");
				clearDataFileMenuItem.setText("データ/ファイルをクリア");
			}

			// "Math" menu and sub menu items.
			{
				mathMenu.setText("数式");
				zxyMathMenuItem.setText("z(x,y) 形式の数式をプロット");
				xtYtZtMathMenuItem.setText("x(t),y(t),z(t) 形式の数式をプロット");
				modifyMathMenuItem.setText("数式を編集");
				removeMathMenuItem.setText("数式を削除");
				clearMathMenuItem.setText("数式をクリア");
			}

			// "Edit" menu and sub menu items.
			{
				editMenu.setText("編集");
				rangeSettingMenuItem.setText("範囲の設定");
				labelSettingMenuItem.setText("ラベルの設定");
				fontSettingMenuItem.setText("フォントの設定");
				cameraSettingMenuItem.setText("カメラの設定");
				lightSettingMenuItem.setText("光の設定");
				scaleSettingMenuItem.setText("目盛りの設定");
				rendererSettingMenuItem.setText("描画エンジンの設定");
				resetSettingMenuItem.setText("設定をリセット");
				clearAllMenuItem.setText("すべてクリア");
			}

			// "Options" menu and sub menu items.
			{
				optionsMenu.setText("オプション");
				pointOptionMenuItem.setText("点プロット");
				lineOptionMenuItem.setText("線プロット");
				meshOptionMenuItem.setText("メッシュプロット");
				surfaceOptionMenuItem.setText("曲面プロット");

				logXOptionMenuItem.setText("対数軸 X");
				logYOptionMenuItem.setText("対数軸 Y");
				logZOptionMenuItem.setText("対数軸 Z");

				invertXOptionMenuItem.setText("軸反転 X");
				invertYOptionMenuItem.setText("軸反転 Y");
				invertZOptionMenuItem.setText("軸反転 Z");

				legendOptionMenuItem.setText("凡例");
				blackScreenOptionMenuItem.setText("ブラックスクリーン");
				gradientOptionMenuItem.setText("グラデーション");
				gridLinesOptionMenuItem.setText("グリッド線");
				frameLinesOptionMenuItem.setText("フレーム");
				scaleTicksOptionMenuItem.setText("目盛り");
				axisLabelsOptionMenuItem.setText("軸ラベル");
			}
		}

		/**
		 * Sets English texts to the GUI components.
		 */
		private void setEnglishTexts() {

			// "File" menu and sub menu items.
			{
				fileMenu.setText("File");
				openDataFileMenuItem.setText("Open File");
				pasteDataTextMenuItem.setText("Paste Data");
				saveImageFileMenuItem.setText("Save Image");
				copyImageMenuItem.setText("Copy Image");
				clearDataFileMenuItem.setText("Clear Data/Files");
			}

			// "Math" menu and sub menu items.
			{
				mathMenu.setText("Math");
				zxyMathMenuItem.setText("Plot z(x,y)");
				xtYtZtMathMenuItem.setText("Plot x(t),y(t),z(t)");
				modifyMathMenuItem.setText("Modify Math Expression");
				removeMathMenuItem.setText("Remove Math Expression");
				clearMathMenuItem.setText("Clear Math Expressions");
			}

			// "Edit" menu and sub menu items.
			{
				editMenu.setText("Edit");
				rangeSettingMenuItem.setText("Set Ranges");
				labelSettingMenuItem.setText("Set Labels");
				fontSettingMenuItem.setText("Set Fonts");
				cameraSettingMenuItem.setText("Set Camera");
				lightSettingMenuItem.setText("Set Light");
				scaleSettingMenuItem.setText("Set Scale");
				rendererSettingMenuItem.setText("Set Renderer");
				resetSettingMenuItem.setText("Reset Settings");
				clearAllMenuItem.setText("Clear All");
			}

			// "Options" menu and sub menu items.
			{
				optionsMenu.setText("Options");
				pointOptionMenuItem.setText("With Points");
				lineOptionMenuItem.setText("With Lines");
				meshOptionMenuItem.setText("With Meshes");
				surfaceOptionMenuItem.setText("With Surfaces (Membranes)");

				logXOptionMenuItem.setText("Log X");
				logYOptionMenuItem.setText("Log Y");
				logZOptionMenuItem.setText("Log Z");

				invertXOptionMenuItem.setText("Invert X");
				invertYOptionMenuItem.setText("Invert Y");
				invertZOptionMenuItem.setText("Invert Z");

				legendOptionMenuItem.setText("Legends");
				gradientOptionMenuItem.setText("Gradient Colors");
				blackScreenOptionMenuItem.setText("Black Screen");
				gridLinesOptionMenuItem.setText("Grid Lines");
				frameLinesOptionMenuItem.setText("Frame Lines");
				scaleTicksOptionMenuItem.setText("Scale Ticks");
				axisLabelsOptionMenuItem.setText("Axis Labels");
			}
		}

		/**
		 * Set fonts to the components.
		 */
		private void setFonts() {
			FontConfiguration fontConfig = configuration.getFontConfiguration();
			Font uiBoldFont = fontConfig.getUIBoldFont();

			// "File" menu and sub menu items.
			{
				fileMenu.setFont(uiBoldFont);
				openDataFileMenuItem.setFont(uiBoldFont);
				pasteDataTextMenuItem.setFont(uiBoldFont);
				saveImageFileMenuItem.setFont(uiBoldFont);
				copyImageMenuItem.setFont(uiBoldFont);
				clearDataFileMenuItem.setFont(uiBoldFont);
			}

			// "Math" menu and sub menu items.
			{
				mathMenu.setFont(uiBoldFont);
				modifyMathMenuItem.setFont(uiBoldFont);
				removeMathMenuItem.setFont(uiBoldFont);
				clearMathMenuItem.setFont(uiBoldFont);
				zxyMathMenuItem.setFont(uiBoldFont);
				xtYtZtMathMenuItem.setFont(uiBoldFont);
			}

			// "Edit" menu and sub menu items.
			{
				editMenu.setFont(uiBoldFont);
				rangeSettingMenuItem.setFont(uiBoldFont);
				labelSettingMenuItem.setFont(uiBoldFont);
				fontSettingMenuItem.setFont(uiBoldFont);
				cameraSettingMenuItem.setFont(uiBoldFont);
				lightSettingMenuItem.setFont(uiBoldFont);
				scaleSettingMenuItem.setFont(uiBoldFont);
				rendererSettingMenuItem.setFont(uiBoldFont);
				resetSettingMenuItem.setFont(uiBoldFont);
				clearAllMenuItem.setFont(uiBoldFont);
			}

			// "Options" menu and sub menu items.
			{
				optionsMenu.setFont(uiBoldFont);
				pointOptionMenuItem.setFont(uiBoldFont);
				lineOptionMenuItem.setFont(uiBoldFont);
				meshOptionMenuItem.setFont(uiBoldFont);
				surfaceOptionMenuItem.setFont(uiBoldFont);

				logXOptionMenuItem.setFont(uiBoldFont);
				logYOptionMenuItem.setFont(uiBoldFont);
				logZOptionMenuItem.setFont(uiBoldFont);

				invertXOptionMenuItem.setFont(uiBoldFont);
				invertYOptionMenuItem.setFont(uiBoldFont);
				invertZOptionMenuItem.setFont(uiBoldFont);

				legendOptionMenuItem.setFont(uiBoldFont);
				blackScreenOptionMenuItem.setFont(uiBoldFont);
				gradientOptionMenuItem.setFont(uiBoldFont);
				gridLinesOptionMenuItem.setFont(uiBoldFont);
				frameLinesOptionMenuItem.setFont(uiBoldFont);
				scaleTicksOptionMenuItem.setFont(uiBoldFont);
				axisLabelsOptionMenuItem.setFont(uiBoldFont);
			}
		}

		/**
		 * Sets the selection-states of the menu items in "Option" menu.
		 */
		private void setOptionStates() {

			// Plot options:
			{
				PlotterConfiguration plotterConfig = configuration.getPlotterConfiguration();
				pointOptionMenuItem.setSelected(plotterConfig.getPointPlotterConfiguration().isPlotterEnabled());
				lineOptionMenuItem.setSelected(plotterConfig.getLinePlotterConfiguration().isPlotterEnabled());
				meshOptionMenuItem.setSelected(plotterConfig.getMeshPlotterConfiguration().isPlotterEnabled());
				surfaceOptionMenuItem.setSelected(plotterConfig.getSurfacePlotterConfiguration().isPlotterEnabled());
			}

			// Scale options:
			{
				ScaleConfiguration scaleConfig = configuration.getScaleConfiguration();
				AxisScaleConfiguration xScaleConfig = scaleConfig.getXScaleConfiguration();
				AxisScaleConfiguration yScaleConfig = scaleConfig.getYScaleConfiguration();
				AxisScaleConfiguration zScaleConfig = scaleConfig.getZScaleConfiguration();

				logXOptionMenuItem.setSelected(xScaleConfig.isLogScaleEnabled());
				logYOptionMenuItem.setSelected(yScaleConfig.isLogScaleEnabled());
				logZOptionMenuItem.setSelected(zScaleConfig.isLogScaleEnabled());

				invertXOptionMenuItem.setSelected(xScaleConfig.isInversionEnabled());
				invertYOptionMenuItem.setSelected(yScaleConfig.isInversionEnabled());
				invertZOptionMenuItem.setSelected(zScaleConfig.isInversionEnabled());

				gridLinesOptionMenuItem.setSelected(scaleConfig.isGridLinesVisible());
				scaleTicksOptionMenuItem.setSelected(scaleConfig.isTicksVisible());
			}

			// Frame options:
			{
				FrameConfiguration frameConfig = configuration.getFrameConfiguration();
				frameLinesOptionMenuItem.setSelected(frameConfig.isFrameLinesVisible());
			}

			// Label option:
			{
				LabelConfiguration labelConfig = configuration.getLabelConfiguration();
				legendOptionMenuItem.setSelected(labelConfig.isLegendLabelsVisible());
				axisLabelsOptionMenuItem.setSelected(labelConfig.isAxisLabelsVisible());
			}

			// Color options:
			{
				ColorConfiguration colorConfig = configuration.getColorConfiguration();
				gradientOptionMenuItem.setSelected(colorConfig.isDataGradientColorEnabled());

				Color bakcgroundColor = colorConfig.getBackgroundColor();
				boolean isBlackScreenEnabled =
						bakcgroundColor.getRed() == 0 &&
						bakcgroundColor.getGreen() == 0 &&
						bakcgroundColor.getBlue() == 0 &&
						bakcgroundColor.getAlpha() == 255; // Not transparent.
				blackScreenOptionMenuItem.setSelected(isBlackScreenEnabled);
			}
		}
	}

}
