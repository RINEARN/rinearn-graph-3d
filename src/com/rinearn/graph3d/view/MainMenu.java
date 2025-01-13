package com.rinearn.graph3d.view;

import java.awt.Font;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import com.rinearn.graph3d.config.FontConfiguration;
import com.rinearn.graph3d.config.OptionConfiguration;
import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;



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


	/** "Math" menu on the menu bar. */
	public volatile JMenu mathMenu;

	/** "Math" > "Clear Math Expressions" menu on the menu bar. */
	public volatile JMenuItem clearMathMenuItem;

	/** "Math" > "Remove Math Expression" menu on the menu bar. */
	public volatile JMenuItem removeMathMenuItem;

	/** "Math" > "Plot z(x,y)" menu on the menu bar. */
	public volatile JMenuItem zxyMathMenuItem;


	/** "Settings" menu on the menu bar. */
	public volatile JMenu settingsMenu;

	/** "Settings" > "Set Ranges" menu item on the menu bar. */
	public volatile JMenuItem rangeSettingMenuItem;

	/** "Settings" > "Set Labels" menu item on the menu bar. */
	public volatile JMenuItem labelSettingMenuItem;

	/** "Settings" > "Set Fonts" menu item on the menu bar. */
	public volatile JMenuItem fontSettingMenuItem;

	/** "Settings" > "Set Camera" menu item on the menu bar. */
	public volatile JMenuItem cameraSettingMenuItem;

	/** "Settings" > "Set Light" menu item on the menu bar. */
	public volatile JMenuItem lightSettingMenuItem;

	/** "Settings" > "Set Scale" menu item on the menu bar. */
	public volatile JMenuItem scaleSettingMenuItem;


	/** "Options" menu on the menu bar. */
	public volatile JMenu optionsMenu;

	/** "Options" > "With Points" menu item on the menu bar.  */
	public volatile JCheckBoxMenuItem pointOptionMenuItem;

	/** "Options" > "With Lines" menu item on the menu bar.  */
	public volatile JCheckBoxMenuItem lineOptionMenuItem;

	/** "Options" > "With Meshes" menu item on the menu bar.  */
	public volatile JCheckBoxMenuItem meshOptionMenuItem;

	/** "Options" > "With Membranes" menu item on the menu bar.  */
	public volatile JCheckBoxMenuItem membraneOptionMenuItem;


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

				// "File" > "Save Image"
				saveImageFileMenuItem = new JMenuItem("Unconfigured");
				fileMenu.add(saveImageFileMenuItem);

				// "File" > "Copy Image"
				copyImageMenuItem = new JMenuItem("Unconfigured");
				fileMenu.add(copyImageMenuItem);
			}

			// "Math" menu:
			{
				mathMenu = new JMenu("Unconfigured");
				menuBar.add(mathMenu);

				// "Math" > "Plot z(x,y)" menu item:
				zxyMathMenuItem = new JMenuItem("Unconfigured");
				mathMenu.add(zxyMathMenuItem);

				// ---
				mathMenu.addSeparator();

				// "Math" > "Remove Math Expression" menu item:
				removeMathMenuItem = new JMenuItem("Unconfigured");
				mathMenu.add(removeMathMenuItem);

				// "Math" > "Clear Math Expressions" menu item:
				clearMathMenuItem = new JMenuItem("Unconfigured");
				mathMenu.add(clearMathMenuItem);
			}

			// "Settings" menu:
			{
				settingsMenu = new JMenu("Unconfigured");
				menuBar.add(settingsMenu);

				// "Settings" > "Set Ranges" menu item:
				rangeSettingMenuItem = new JMenuItem("Unconfigured");
				settingsMenu.add(rangeSettingMenuItem);

				// "Settings" > "Set Labels" menu item:
				labelSettingMenuItem = new JMenuItem("Unconfigured");
				settingsMenu.add(labelSettingMenuItem);

				// "Settings" > "Set Fonts" menu item:
				fontSettingMenuItem = new JMenuItem("Unconfigured");
				settingsMenu.add(fontSettingMenuItem);

				// "Settings" > "Set Camera" menu item:
				cameraSettingMenuItem = new JMenuItem("Unconfigured");
				settingsMenu.add(cameraSettingMenuItem);

				// "Settings" > "Set Light" menu item:
				lightSettingMenuItem = new JMenuItem("Unconfigured");
				settingsMenu.add(lightSettingMenuItem);

				// "Settings" > "Set Scale" menu item:
				scaleSettingMenuItem = new JMenuItem("Unconfigured");
				settingsMenu.add(scaleSettingMenuItem);
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

				// "Options" > "With Membranes" menu item:
				membraneOptionMenuItem = new JCheckBoxMenuItem("Unconfigured");
				optionsMenu.add(membraneOptionMenuItem);
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
			}

			// "Math" menu and sub menu items.
			{
				mathMenu.setText("数式");
				zxyMathMenuItem.setText("z(x,y) 形式の数式をプロット");
				removeMathMenuItem.setText("数式を削除");
				clearMathMenuItem.setText("数式をクリア");
			}

			// "Settings" menu and sub menu items.
			{
				settingsMenu.setText("設定");
				rangeSettingMenuItem.setText("範囲の設定");
				labelSettingMenuItem.setText("ラベルの設定");
				fontSettingMenuItem.setText("フォントの設定");
				cameraSettingMenuItem.setText("カメラの設定");
				lightSettingMenuItem.setText("光の設定");
				scaleSettingMenuItem.setText("目盛りの設定");
			}

			// "Options" menu and sub menu items.
			{
				optionsMenu.setText("オプション");
				pointOptionMenuItem.setText("点プロット");
				lineOptionMenuItem.setText("線プロット");
				meshOptionMenuItem.setText("メッシュプロット");
				membraneOptionMenuItem.setText("曲面プロット");
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
			}

			// "Math" menu and sub menu items.
			{
				mathMenu.setText("Math");
				zxyMathMenuItem.setText("Plot z(x,y)");
				removeMathMenuItem.setText("Remove Math Expression");
				clearMathMenuItem.setText("Clear Math Expressions");
			}

			// "Settings" menu and sub menu items.
			{
				settingsMenu.setText("Settings");
				rangeSettingMenuItem.setText("Set Ranges");
				labelSettingMenuItem.setText("Set Labels");
				fontSettingMenuItem.setText("Set Fonts");
				cameraSettingMenuItem.setText("Set Camera");
				lightSettingMenuItem.setText("Set Light");
				scaleSettingMenuItem.setText("Set Scale");
			}

			// "Options" menu and sub menu items.
			{
				optionsMenu.setText("Options");
				pointOptionMenuItem.setText("With Points");
				lineOptionMenuItem.setText("With Lines");
				meshOptionMenuItem.setText("With Meshes");
				membraneOptionMenuItem.setText("With Membranes");
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
			}

			// "Math" menu and sub menu items.
			{
				mathMenu.setFont(uiBoldFont);
				removeMathMenuItem.setFont(uiBoldFont);
				clearMathMenuItem.setFont(uiBoldFont);
				zxyMathMenuItem.setFont(uiBoldFont);
			}

			// "Settings" menu and sub menu items.
			{
				settingsMenu.setFont(uiBoldFont);
				rangeSettingMenuItem.setFont(uiBoldFont);
				labelSettingMenuItem.setFont(uiBoldFont);
				fontSettingMenuItem.setFont(uiBoldFont);
				cameraSettingMenuItem.setFont(uiBoldFont);
				lightSettingMenuItem.setFont(uiBoldFont);
				scaleSettingMenuItem.setFont(uiBoldFont);
			}

			// "Options" menu and sub menu items.
			{
				optionsMenu.setFont(uiBoldFont);
				pointOptionMenuItem.setFont(uiBoldFont);
				lineOptionMenuItem.setFont(uiBoldFont);
				meshOptionMenuItem.setFont(uiBoldFont);
				membraneOptionMenuItem.setFont(uiBoldFont);
			}
		}

		/**
		 * Sets the selection-states of the menu items in "Option" menu.
		 */
		private void setOptionStates() {
			OptionConfiguration optionConfig = configuration.getOptionConfiguration();
			pointOptionMenuItem.setSelected(optionConfig.getPointOptionConfiguration().isSelected());
			lineOptionMenuItem.setSelected(optionConfig.getLineOptionConfiguration().isSelected());
			meshOptionMenuItem.setSelected(optionConfig.getMeshOptionConfiguration().isSelected());
			membraneOptionMenuItem.setSelected(optionConfig.getMembraneOptionConfiguration().isSelected());
		}
	}

}
