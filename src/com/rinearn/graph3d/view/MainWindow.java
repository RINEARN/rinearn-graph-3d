package com.rinearn.graph3d.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
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

	/** The approximate height of the header-area (window header + menu bar). */
	public static final int APPROX_HEADER_AREA_HEIGHT = 65;

	/** The default width [px] of the 3D graph screen. */
	public static final int DEFAULT_SCREEN_WIDTH = DEFAULT_WINDOW_WIDTH - LEFT_SIDE_UI_PANEL_WIDTH;

	/** The default height [px] of the 3D graph screen. */
	public static final int DEFAULT_SCREEN_HEIGHT = DEFAULT_WINDOW_HEIGHT - APPROX_HEADER_AREA_HEIGHT;

	/** The frame of this window. */
	public volatile JFrame frame;

	/** The label of the screen, on which a 3D graph is displayed. */
	public volatile JLabel screenLabel;

	/** The icon for displaying a rendered graph image on "screenLabel". */
	public volatile ImageIcon screenIcon;

	/** The UI panel at the left-side of the screen. */
	public volatile JPanel leftSideUIPanel;

	/** The menu bar at the top of the window. */
	public volatile JMenuBar menuBar;


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

			// Add dummy menus to the menu bar.
			Font tempMenuFont = new Font("Dialog", Font.BOLD, 16);
			JMenu menu;

			menu = new JMenu("Hello!");
			menu.setFont(tempMenuFont);
			menuBar.add(menu);

			menu = new JMenu("These");
			menu.setFont(tempMenuFont);
			menuBar.add(menu);

			menu = new JMenu("Are");
			menu.setFont(tempMenuFont);
			menuBar.add(menu);

			menu = new JMenu("Dummy");
			menu.setFont(tempMenuFont);
			menuBar.add(menu);

			menu = new JMenu("Menus");
			menu.setFont(tempMenuFont);
			menuBar.add(menu);

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
			leftSideUIPanel.setBackground(Color.GREEN);
			frame.getContentPane().add(leftSideUIPanel);
			leftSideUIPanel.setVisible(true);

			// The icon for displaying a rendered graph image on "screenLabel":
			screenIcon = new ImageIcon();
			screenLabel.setIcon(screenIcon);
		}
	}


	// !!!!! IMPORTANT NOTE !!!!!
	//
	// Don't put "synchronized" modifier to UI-operation methods,
	// such as resize(), repaintScreen(), setScreenImage(image), etc.
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

			// Resize the graph screen.
			int screenWidth = windowWidth - LEFT_SIDE_UI_PANEL_WIDTH;
			int screenHeight = windowHeight - APPROX_HEADER_AREA_HEIGHT;
			int screenX = LEFT_SIDE_UI_PANEL_WIDTH;
			int screenY = 0;
			screenLabel.setBounds(
					screenX, screenY, screenWidth, screenHeight
			);
			// The resizing event of the graph screen is fired in ScreenHandler, by the above setBounds(...).

			// Resize the UI-panel at the left side of the screen.
			leftSideUIPanel.setBounds(0, 0, LEFT_SIDE_UI_PANEL_WIDTH, screenHeight);
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
}
