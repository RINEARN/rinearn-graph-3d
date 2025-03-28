package com.rinearn.graph3d.view;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Image;
import java.awt.Dimension;
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
	public static final int DEFAULT_WINDOW_WIDTH = 770;

	/** The default height [px] of the main window. */
	public static final int DEFAULT_WINDOW_HEIGHT = 620;

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
	public static final int AXIS_LENGTH_BAR_MAX_COUNT = 2000;

	/** The color of the dimension length bars. */
	public static final Color AXIS_LENGTH_BAR_COLOR = new Color(100, 120, 200);


	/** The frame of this window. */
	public volatile JFrame frame;

	/** The label of the screen, on which a 3D graph is displayed. */
	public volatile JLabel screenLabel;

	/** The icon for displaying a rendered graph image on "screenLabel". */
	public volatile ImageIcon screenIcon;

	/** The UI panel at the left-side of the screen. */
	public volatile JPanel leftSideUIPanel;

	/** The scroll bar for controlling the length of X axis. */
	public volatile JScrollBar xAxisLengthBar;

	/** The scroll bar for controlling the length of Y axis. */
	public volatile JScrollBar yAxisLengthBar;

	/** The scroll bar for controlling the length of Z axis. */
	public volatile JScrollBar zAxisLengthBar;

	/** The main menu, displayed on the menu bar. */
	public volatile MainMenu mainMenu;

	/** The right-click menu of the graph screen. */
	public volatile ScreenRightClickMenu screenRightClickMenu;

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

			// The main menu (on the menu bar):
			mainMenu = new MainMenu();
			frame.setJMenuBar(mainMenu.menuBar);

			// Right-click menu of the graph screen:
			screenRightClickMenu = new ScreenRightClickMenu();

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

		// The scroll bar for controlling the length of X axis.
		constraints.gridx = 0;
		xAxisLengthBar = new JScrollBar(
				JScrollBar.VERTICAL,
				AXIS_LENGTH_BAR_MAX_COUNT / 2, // default value
				0, // extent
				0, // min
				AXIS_LENGTH_BAR_MAX_COUNT // max
		);
		xAxisLengthBar.setBackground(AXIS_LENGTH_BAR_COLOR);
		layout.setConstraints(xAxisLengthBar, constraints);
		leftSideUIPanel.add(xAxisLengthBar);
		xAxisLengthBar.setVisible(true);

		// The scroll bar for controlling the length of Y axis.
		constraints.gridx = 1;
		yAxisLengthBar = new JScrollBar(
				JScrollBar.VERTICAL,
				AXIS_LENGTH_BAR_MAX_COUNT / 2, // default value
				0, // extent
				0, // min
				AXIS_LENGTH_BAR_MAX_COUNT // max
		);
		yAxisLengthBar.setBackground(AXIS_LENGTH_BAR_COLOR);
		layout.setConstraints(yAxisLengthBar, constraints);
		leftSideUIPanel.add(yAxisLengthBar);
		yAxisLengthBar.setVisible(true);

		// The scroll bar for controlling the length of Z axis.
		constraints.gridx = 2;
		zAxisLengthBar = new JScrollBar(
				JScrollBar.VERTICAL,
				AXIS_LENGTH_BAR_MAX_COUNT / 2, // default value
				0, // extent
				0, // min
				AXIS_LENGTH_BAR_MAX_COUNT // max
		);
		zAxisLengthBar.setBackground(AXIS_LENGTH_BAR_COLOR);
		layout.setConstraints(zAxisLengthBar, constraints);
		leftSideUIPanel.add(zAxisLengthBar);
		zAxisLengthBar.setVisible(true);
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

			// Update the menu the menu bar, and the right-click menu of the graph screen.
			mainMenu.configure(configuration);
			screenRightClickMenu.configure(configuration);
		}
	}


	/**
	 * Resizes the components on this window, based on the current size of this window.
	 */
	public void resize() {
		int currentWindowWidth = this.frame.getWidth();
		int currentWindowHeight = this.frame.getHeight();
		this.resize(currentWindowWidth, currentWindowHeight);
	}

	/**
	 * Resizes the components on this window, based on the specified window size.
	 *
	 * Please note that, there is some time-lag between calling setBounds(...) method of the window
	 * and updating the window size actually.
	 *
	 * For avoiding the broken layout caused by the above time-lag,
	 * specify the window width/height which were passed to the argument of setBounds(...),
	 * to the arguments of this method.
	 *
	 * @param currentWindowWidth The current width of the window.
	 * @param currentWindowHeight The current height of the window.
	 */
	private void resize(int currentWindowWidth, int currentWindowHeight) {

		// Resizes the components on the window, on event-dispatcher thread.
		Resizer resizer = new Resizer(currentWindowWidth, currentWindowHeight);

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
	 * The class for resizing the components on the window based on the current size of the window, on event-dispatcher thread.
	 */
	private final class Resizer implements Runnable {

		/** The current width of the window. */
		private final int currentWindowWidth;

		/** The current height of the window. */
		private final int currentWindowHeight;

		/**
		 * Create a new instance resizing the component based on the specified window size.
		 *
		 * Please note that, there is some time-lag between calling setBounds(...) method of the window
		 * and updating the window size actually.
		 *
		 * For avoiding the broken layout caused by the above time-lag,
		 * specify the window width/height which were passed to the argument of setBounds(...),
		 * to the arguments of this constructor.
		 *
		 * @param currentWindowWidth The current width of the window.
		 * @param currentWindowHeight The current height of the window.
		 */
		public Resizer(int currentWindowWidth, int currentWindowHeight) {
			this.currentWindowWidth = currentWindowWidth;
			this.currentWindowHeight = currentWindowHeight;
		}

		@Override
		public void run() {
			if (!SwingUtilities.isEventDispatchThread()) {
				throw new UnsupportedOperationException("This method is invokable only on the event-dispatcher thread.");
			}

			// Compute the layout of the graph screen.
			int screenWidth = currentWindowWidth;
			int screenHeight = currentWindowHeight - APPROX_WINDOW_HEADER_HEIGHT;
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

			// Resize the UI-panel at the left side of the screen.
			if (screenSideUIVisible) {
				leftSideUIPanel.setBounds(0, 0, LEFT_SIDE_UI_PANEL_WIDTH, screenHeight);
			} else {
				leftSideUIPanel.setBounds(0, 0, 0, screenHeight);
			}

			// !!! DON'T REMOVE !!!
			// At the moment of maximizing the window, the above panel's size-change does not propagate to the scroll bars.
			// To propagate it, hide and re-show the panel. This is little strange code but necessary.
			leftSideUIPanel.setVisible(false);
			leftSideUIPanel.setVisible(true);
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

			// Resize components on the window.
			resize(this.width, this.height);
		}
	}


	/**
	 * Sets the size of the graph screen.
	 *
	 * @param width The width of the graph screen.
	 * @param height The height of the graph screen.
	 */
	public void setScreenSize(int width, int height) {

		// Sets the size of the screen, on event-dispatcher thread.
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
			frame.setSize(windowWidth, windowHeight);

			// Resize components on the window.
			resize(windowWidth, windowHeight);
		}
	}


	/**
	 * Gets the current size of the graph screen.
	 *
	 * @return The current size of the graph screen.
	 */
	public Dimension getScreenSize() {

		// Gets the current size of the screen, on event-dispatcher thread.
		ScreenSizeGetter sizeGetter = new ScreenSizeGetter();
		if (SwingUtilities.isEventDispatchThread()) {
			sizeGetter.run();
			return sizeGetter.getSize();
		} else {
			try {
				SwingUtilities.invokeAndWait(sizeGetter);
				return sizeGetter.getSize();
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * The class for getting the size of the graph screen, on event-dispatcher thread.
	 */
	private final class ScreenSizeGetter implements Runnable {

		/** The size of the screen. */
		private volatile Dimension size = null;

		@Override
		public void run() {
			if (!SwingUtilities.isEventDispatchThread()) {
				throw new UnsupportedOperationException("This method is invokable only on the event-dispatcher thread.");
			}

			// Gets the current screen size;
			this.size = screenLabel.getSize();
		}

		/**
		 * Returns the gotten screen size.
		 *
		 * @return The gotten screen size.
		 */
		public synchronized Dimension getSize() {
			return this.size;
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
				frame.setJMenuBar(mainMenu.menuBar);
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
