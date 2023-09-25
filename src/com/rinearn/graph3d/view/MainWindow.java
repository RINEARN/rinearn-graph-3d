package com.rinearn.graph3d.view;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.FontConfiguration;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.ImageIcon;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.lang.reflect.InvocationTargetException;


/**
 * The main window of RINEARN Graph 3D.
 * On this window, a 3D graph is displayed.
 */
public final class MainWindow {

	/** The header (left) part of the main window's title. */
	public static final String WINDOW_TITLE_HEADER = "RINEARN Graph 3D 6.0";

	/** The default X position [px] of the left-top edge of the main window. */
	public static final int DEFAULT_WINDOW_X = 50;

	/** The default Y position [px] of the left-top edge of the main window. */
	public static final int DEFAULT_WINDOW_Y = 50;

	/** The default width [px] of the main window. */
	public static final int DEFAULT_WINDOW_WIDTH = 1000;

	/** The default height [px] of the main window. */
	public static final int DEFAULT_WINDOW_HEIGHT = 860;

	/** The width of the left-side UI panel. */
	public static final int LEFT_SIDE_UI_PANEL_WIDTH = 95;

	/** The approximate height of the window header. */
	public static final int APPROX_WINDOW_HEADER_HEIGHT = 35;

	/** The approximate height of the menu bar. */
	public static final int APPROX_MENU_BAR_HEIGHT = 30;

	/** The default width [px] of the 3D graph screen. */
	public static final int DEFAULT_SCREEN_WIDTH = DEFAULT_WINDOW_WIDTH - LEFT_SIDE_UI_PANEL_WIDTH;

	/** The default height [px] of the 3D graph screen. */
	public static final int DEFAULT_SCREEN_HEIGHT = DEFAULT_WINDOW_HEIGHT - APPROX_WINDOW_HEADER_HEIGHT - APPROX_MENU_BAR_HEIGHT;

	/** The the max value (integer count) of the dimension length bars. */
	public static final int DIMENSION_LENGTH_BAR_MAX_COUNT = 1000;

	/** The color of the dimension length bars. */
	public static final Color DIMENSION_LENGTH_BAR_COLOR = new Color(100, 120, 200);


	/** The frame of this window. */
	public volatile JFrame frame;

	/** The label of the screen, on which a 3D graph is displayed. */
	public volatile JLabel screenLabel;

	/** The icon for displaying a rendered graph image on "screenLabel". */
	public volatile ImageIcon screenIcon;

	/** The UI panel at the left-side of the screen. */
	public volatile JPanel leftSideUIPanel;

	/** The scroll bar for controlling the length of X dimension. */
	public volatile JScrollBar xDimensionLengthBar;

	/** The scroll bar for controlling the length of Y dimension. */
	public volatile JScrollBar yDimensionLengthBar;

	/** The scroll bar for controlling the length of Z dimension. */
	public volatile JScrollBar zDimensionLengthBar;

	/** The menu bar at the top of the window. */
	public volatile JMenuBar menuBar;

	/** "File" menu on the menu bar. */
	public volatile JMenu fileMenu;

	/** "Settings" > "Set Ranges" menu item on the menu bar. */
	public volatile JMenuItem rangeSettingMenuItem;

	/** "Settings" > "Set Labels" menu item on the menu bar. */
	public volatile JMenuItem labelSettingMenuItem;

	/** "Settings" menu on the menu bar. */
	public volatile JMenu settingsMenu;

	/** "Options" menu on the menu bar. */
	public volatile JMenu optionsMenu;

	/** The flag for switching the visibility of the UI panel at the screen side. */
	public volatile boolean screenSideUIVisible = true;

	/** The flag for switching the visibility of the menu bar and the right-click menus. */
	public volatile boolean menuVisible = true;


	/**
	 * Creates a new main window.
	 */
	public MainWindow() {

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
			frame.setTitle(WINDOW_TITLE_HEADER);
			frame.setBounds(
					DEFAULT_WINDOW_X, DEFAULT_WINDOW_Y,
					DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT
			);
			frame.setLayout(null);
			frame.setVisible(false);

			// The menu bar:
			menuBar = new JMenuBar();
			frame.setJMenuBar(menuBar);

			// "File" menu:
			fileMenu = new JMenu("Unconfigured");
			menuBar.add(fileMenu);

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
			}

			// "Options" menu:
			optionsMenu = new JMenu("Unconfigured");
			menuBar.add(optionsMenu);

			// The label of the screen, on which a 3D graph is displayed:
			screenLabel = new JLabel();
			screenLabel.setBounds(
					LEFT_SIDE_UI_PANEL_WIDTH, 0,
					DEFAULT_SCREEN_WIDTH, DEFAULT_SCREEN_HEIGHT
			);
			frame.getContentPane().add(screenLabel);
			screenLabel.setVisible(true);

			// The UI-panel at the left side of the screen.
			leftSideUIPanel = new JPanel();
			leftSideUIPanel.setBounds(
					0, 0,
					LEFT_SIDE_UI_PANEL_WIDTH, DEFAULT_SCREEN_HEIGHT
			);
			frame.getContentPane().add(leftSideUIPanel);
			leftSideUIPanel.setVisible(true);

			// Creates the dimension length bars, and mount them on the left-side UI panel.
			mountDimensionLengthBars();

			// The icon for displaying a rendered graph image on "screenLabel":
			screenIcon = new ImageIcon();
			screenLabel.setIcon(screenIcon);
		}
	}


	/**
	 * Creates the dimension length bars, and mount them on the left-side UI panel.
	 */
	private void mountDimensionLengthBars() {
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		leftSideUIPanel.setLayout(layout);
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(5, 5, 5, 5);

		// The scroll bar for controlling the length of X dimension.
		constraints.gridx = 0;
		xDimensionLengthBar = new JScrollBar(
				JScrollBar.VERTICAL,
				DIMENSION_LENGTH_BAR_MAX_COUNT / 2, // default value
				0, // extent
				0, // min
				DIMENSION_LENGTH_BAR_MAX_COUNT // max
		);
		xDimensionLengthBar.setBackground(DIMENSION_LENGTH_BAR_COLOR);
		layout.setConstraints(xDimensionLengthBar, constraints);
		leftSideUIPanel.add(xDimensionLengthBar);
		xDimensionLengthBar.setVisible(true);

		// The scroll bar for controlling the length of Y dimension.
		constraints.gridx = 1;
		yDimensionLengthBar = new JScrollBar(
				JScrollBar.VERTICAL,
				DIMENSION_LENGTH_BAR_MAX_COUNT / 2, // default value
				0, // extent
				0, // min
				DIMENSION_LENGTH_BAR_MAX_COUNT // max
		);
		yDimensionLengthBar.setBackground(DIMENSION_LENGTH_BAR_COLOR);
		layout.setConstraints(yDimensionLengthBar, constraints);
		leftSideUIPanel.add(yDimensionLengthBar);
		yDimensionLengthBar.setVisible(true);

		// The scroll bar for controlling the length of Z dimension.
		constraints.gridx = 2;
		zDimensionLengthBar = new JScrollBar(
				JScrollBar.VERTICAL,
				DIMENSION_LENGTH_BAR_MAX_COUNT / 2, // default value
				0, // extent
				0, // min
				DIMENSION_LENGTH_BAR_MAX_COUNT // max
		);
		zDimensionLengthBar.setBackground(DIMENSION_LENGTH_BAR_COLOR);
		layout.setConstraints(zDimensionLengthBar, constraints);
		leftSideUIPanel.add(zDimensionLengthBar);
		zDimensionLengthBar.setVisible(true);
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
		}

		/**
		 * Sets Japanese texts to the GUI components.
		 */
		private void setJapaneseTexts() {

			// "File" menu and sub menu items.
			{
				fileMenu.setText("ファイル");
			}

			// "Settings" menu and sub menu items.
			{
				settingsMenu.setText("設定");
				rangeSettingMenuItem.setText("範囲の設定");
				labelSettingMenuItem.setText("ラベルの設定");
			}

			// "Options" menu and sub menu items.
			{
				optionsMenu.setText("オプション");
			}
		}

		/**
		 * Sets English texts to the GUI components.
		 */
		private void setEnglishTexts() {

			// "File" menu and sub menu items.
			{
				fileMenu.setText("File");
			}

			// "Settings" menu and sub menu items.
			{
				settingsMenu.setText("Settings");
				rangeSettingMenuItem.setText("Set Ranges");
				labelSettingMenuItem.setText("Set Labels");
			}

			// "Options" menu and sub menu items.
			{
				optionsMenu.setText("Options");
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
			}

			// "Settings" menu and sub menu items.
			{
				settingsMenu.setFont(uiBoldFont);
				rangeSettingMenuItem.setFont(uiBoldFont);
				labelSettingMenuItem.setFont(uiBoldFont);
			}

			// "Options" menu and sub menu items.
			{
				optionsMenu.setFont(uiBoldFont);
			}
		}
	}


	/**
	 * Resizes the components on the window.
	 */
	public void resize() {

		// Resizes the components on the window, on event-dispatcher thread.
		Resizer resizer = new Resizer();

		if (SwingUtilities.isEventDispatchThread()) {
			resizer.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(resizer);
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * The class for resizing the components on the window, on event-dispatcher thread.
	 */
	private final class Resizer implements Runnable {
		@Override
		public void run() {
			if (!SwingUtilities.isEventDispatchThread()) {
				throw new UnsupportedOperationException("This method is invokable only on the event-dispatcher thread.");
			}

			// Get the current size of the window.
			int windowWidth = (int)frame.getSize().getWidth();
			int windowHeight = (int)frame.getSize().getHeight();

			// Compute the layout of the graph screen.
			int screenWidth = windowWidth;
			int screenHeight = windowHeight - APPROX_WINDOW_HEADER_HEIGHT;
			int screenX = 0;
			int screenY = 0;
			if (menuVisible) {
				screenHeight -= APPROX_MENU_BAR_HEIGHT;
			}
			if (screenSideUIVisible) {
				screenX += LEFT_SIDE_UI_PANEL_WIDTH;
				screenWidth -= LEFT_SIDE_UI_PANEL_WIDTH;
			}

			// Resize the graph screen.
			screenLabel.setBounds(
					screenX, screenY, screenWidth, screenHeight
			);
			// The resizing event of the graph screen is fired in ScreenHandler, by the above setBounds(...).

			// Resize the UI-panel at the left side of the screen.
			if (screenSideUIVisible) {
				leftSideUIPanel.setBounds(0, 0, LEFT_SIDE_UI_PANEL_WIDTH, screenHeight);
			} else {
				leftSideUIPanel.setBounds(0, 0, 0, screenHeight);				
			}
		}
	}


	/**
	 * Sets the instance of the graph image, to be displayed on the screen.
	 * 
	 * It is not necessary to call this method every time after drawing something to the image.
	 * You can reflect the drawn contents by calling only repaint() method.
	 * 
	 * On the other hand, when the instance (reference) of the image has been changed,
	 * (e.g.: when the screen has been resized),
	 * it requires to call this method to update the reference to the image instance to be displayed.
	 * 
	 * @param image The instance of the graph image to be displayed on the screen.
	 */
	public void setScreenImage(Image screenImage) {

		// Set the specified graph image to "screenIcon", on event-dispatcher thread.
		ScreenImageSetter screenImageSetter = new ScreenImageSetter(screenImage);
		if (SwingUtilities.isEventDispatchThread()) {
			screenImageSetter.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(screenImageSetter);
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * The class for updating reference to the graph image from "screenIcon", on event-dispatcher thread.
	 */
	private final class ScreenImageSetter implements Runnable {

		/** The graph image to be set to "screenIcon". */
		private final Image screenImage;

		/**
		 * Creates an instance for setting the specified graph image to "screenIcon".
		 * 
		 * @param screenImage The graph image to be set to "screenIcon".
		 */
		public ScreenImageSetter(Image graphImage) {
			this.screenImage = graphImage;
		}

		@Override
		public void run() {
			if (!SwingUtilities.isEventDispatchThread()) {
				throw new UnsupportedOperationException("This method is invokable only on the event-dispatcher thread.");
			}
			screenIcon.setImage(this.screenImage);
		}
	}


	/**
	 * Repaints the graph screen.
	 */
	public void repaintScreen() {

		// Repaints "screenLabel", on event-dispatcher thread.
		ScreenRepainter screenRepainter = new ScreenRepainter();
		if (SwingUtilities.isEventDispatchThread()) {
			screenRepainter.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(screenRepainter);
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * The class for repainting the "screenLabel" and so on, on event-dispatcher thread.
	 */
	private final class ScreenRepainter implements Runnable {
		@Override
		public void run() {
			if (!SwingUtilities.isEventDispatchThread()) {
				throw new UnsupportedOperationException("This method is invokable only on the event-dispatcher thread.");
			}
			screenLabel.repaint();
		}
	}


	/**
	 * Sets the location and the size of this window.
	 * 
	 * @param x The X coordinate of the left-top edge of the window.
	 * @param y The Y coordinate of the left-top edge of the window.
	 * @param width The width of the window.
	 * @param height The height of the window.
	 */
	public void setWindowBounds(int x, int y, int width, int height) {

		// Sets the bounds of this window, on event-dispatcher thread.
		WindowBoundsSetter boundsSetter = new WindowBoundsSetter(x, y, width, height);
		if (SwingUtilities.isEventDispatchThread()) {
			boundsSetter.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(boundsSetter);
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * The class for setting the location and the size of this window, on event-dispatcher thread.
	 */
	private final class WindowBoundsSetter implements Runnable {

		/** The X coordinate of the left-top edge of the window, to be set. */
		private final int x;

		/** The Y coordinate of the left-top edge of the window, to be set. */
		private final int y;

		/** The width of the window, to be set. */
		private final int width;

		/** The height of the window, to be set. */
		private final int height;

		/**
		 * Creates a new instance for setting the window to the specified location/size.
		 * 
		 * @param x The X coordinate of the left-top edge of the window.
		 * @param y The Y coordinate of the left-top edge of the window.
		 * @param width The width of the window.
		 * @param height The height of the window.
		 */
		public WindowBoundsSetter(int x, int y, int width, int height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}

		@Override
		public void run() {
			if (!SwingUtilities.isEventDispatchThread()) {
				throw new UnsupportedOperationException("This method is invokable only on the event-dispatcher thread.");
			}
			frame.setBounds(this.x, this.y, this.width, this.height);
		}
	}


	/**
	 * Sets the size of the graph screen.
	 * 
	 * @param width The width of the graph screen.
	 * @param height The height of the graph screen.
	 */
	public void setScreenSize(int width, int height) {

		// Sets the bounds of this window, on event-dispatcher thread.
		ScreenSizeSetter sizeSetter = new ScreenSizeSetter(width, height);
		if (SwingUtilities.isEventDispatchThread()) {
			sizeSetter.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(sizeSetter);
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * The class for setting the size of the graph screen, on event-dispatcher thread.
	 */
	private final class ScreenSizeSetter implements Runnable {

		/** The width of the screen, to be set. */
		private final int width;

		/** The height of the screen, to be set. */
		private final int height;

		/**
		 * Creates a new instance for setting the screen to the specified size.
		 * 
		 * @param width The width of the screen.
		 * @param height The height of the screen.
		 */
		public ScreenSizeSetter(int width, int height) {
			this.width = width;
			this.height = height;
		}

		@Override
		public void run() {
			if (!SwingUtilities.isEventDispatchThread()) {
				throw new UnsupportedOperationException("This method is invokable only on the event-dispatcher thread.");
			}

			// Compute the window size corresponding to the specified screen size.
			int windowWidth = this.width;
			int windowHeight = this.height + APPROX_WINDOW_HEADER_HEIGHT;
			if (screenSideUIVisible) {
				windowWidth += LEFT_SIDE_UI_PANEL_WIDTH;
			}
			if (menuVisible) {
				windowHeight += APPROX_MENU_BAR_HEIGHT;
			}

			// Update the window size.
			// (Then the screen will be resized automatically.)
			frame.setSize(windowWidth, windowHeight);
		}
	}


	/**
	 * Sets the visibility of the window.
	 * 
	 * @param visible Specify true for showing the window, false for hiding the window.
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
	 * The class for switching visibility of the window, on event-dispatcher thread.
	 */
	private final class WindowVisiblitySwitcher implements Runnable {

		/** The flag representing whether the window is visible. */
		private volatile boolean visible;

		/**
		 * Create an instance for switching visibility of the window.
		 * 
		 * @param visible Specify true for showing the window, false for hiding the window.
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
	 * Sets the visibility of the menu bar and the right click menus.
	 * 
	 * @param visible Specify true for showing the menu bar and the right click menus.
	 */
	public void setMenuVisible(boolean visible) {

		// Switch visibility on event-dispatcher thread.
		MenuVisiblitySwitcher visibilitySwitcher = new MenuVisiblitySwitcher(visible);
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
	 * The class for switching visibility of the menu bar and the right click menus.
	 */
	private final class MenuVisiblitySwitcher implements Runnable {

		/** The flag representing whether the menu bar and the right click menus. */
		private volatile boolean visible;

		/**
		 * Create an instance for switching visibility of the menu bar and the right click menus.
		 * 
		 * @param visible Specify true for showing the menu bar and the right-click menus.
		 */
		public MenuVisiblitySwitcher(boolean visible) {
			this.visible = visible;
		}
		@Override
		public void run() {
			if (!SwingUtilities.isEventDispatchThread()) {
				throw new UnsupportedOperationException("This method is invokable only on the event-dispatcher thread.");
			}

			// Demount or re-mount the menu bar.
			menuVisible = visible;
			if (menuVisible) {
				frame.setJMenuBar(menuBar);
			} else {
				frame.setJMenuBar(null);				
			}

			// Update the location and the size of the screen.
			resize();
		}
	}


	/**
	 * Sets the visibility of the UI panel at the screen side.
	 * 
	 * @param visible Specify true for showing the UI panel at the screen side.
	 */
	public void setScreenSideUIVisible(boolean visible) {

		// Switch visibility on event-dispatcher thread.
		ScreenSideUIVisiblitySwitcher visibilitySwitcher = new ScreenSideUIVisiblitySwitcher(visible);
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
	 * The class for switching visibility of the UI panel at the screen side.
	 */
	private final class ScreenSideUIVisiblitySwitcher implements Runnable {

		/** The flag representing whether the UI panel at the screen side. */
		private volatile boolean visible;

		/**
		 * Create an instance for switching visibility of the menu bar and the right click menus.
		 * 
		 * @param visible Specify true for showing the menu bar and the right-click menus.
		 */
		public ScreenSideUIVisiblitySwitcher(boolean visible) {
			this.visible = visible;
		}
		@Override
		public void run() {
			if (!SwingUtilities.isEventDispatchThread()) {
				throw new UnsupportedOperationException("This method is invokable only on the event-dispatcher thread.");
			}

			// Switch visibility of the left-side UI panel.
			screenSideUIVisible = visible;
			leftSideUIPanel.setVisible(screenSideUIVisible);

			// Update the location and the size of the screen.
			resize();
		}
	}


	/**
	 * Force updates the layout of the components on the window.
	 */
	public void forceUpdateWindowLayout() {
		WindowLayoutForceUpdater windowLayoutUpdater = new WindowLayoutForceUpdater();
		if (SwingUtilities.isEventDispatchThread()) {
			windowLayoutUpdater.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(windowLayoutUpdater);
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * The class for force-updating the layout of the components, on event-dispatcher thread.
	 */
	private final class WindowLayoutForceUpdater implements Runnable {
		@Override
		public void run() {
			if (!SwingUtilities.isEventDispatchThread()) {
				throw new UnsupportedOperationException("This method is invokable only on the event-dispatcher thread.");
			}

			// Seems very tricky,
			// but most reliable way to force update the layout of the components on the window, for various situations.
			frame.setVisible(false);
			frame.setVisible(true);
		}
	}
}
