package com.rinearn.graph3d.view;

import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import com.rinearn.graph3d.config.FontConfiguration;
import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;

import java.awt.Font;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JMenuItem;


/**
 * The right-click menu of text input components.
 *
 * This class is instantiated as fields of ...Window classes, not as a field of View class.
 */
public final class TextRightClickMenu {

	/** The pop-up menu (outer frame) of this right-click menu. */
	public volatile JPopupMenu popupMenu;

	/** The menu item "Cut". */
	public volatile JMenuItem cutItem;

	/** The menu item "Copy". */
	public volatile JMenuItem copyItem;

	/** The menu item "Paste". */
	public volatile JMenuItem pasteItem;

	/** The menu item "Clear". */
	public volatile JMenuItem clearItem;


	/**
	 * Creates a new menu instance.
	 */
	public TextRightClickMenu() {

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
			popupMenu = new JPopupMenu();

			cutItem = new JMenuItem("Unconfigured");
			popupMenu.add(cutItem);

			copyItem = new JMenuItem("Unconfigured");
			popupMenu.add(copyItem);

			pasteItem = new JMenuItem("Unconfigured");
			popupMenu.add(pasteItem);

			clearItem = new JMenuItem("Unconfigured");
			popupMenu.add(clearItem);
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
		}

		/**
		 * Sets Japanese texts to the GUI components.
		 */
		private void setJapaneseTexts() {
			cutItem.setText("切り取り");
			copyItem.setText("コピー");
			pasteItem.setText("ペースト");
			clearItem.setText("クリア");
		}

		/**
		 * Sets English texts to the GUI components.
		 */
		private void setEnglishTexts() {
			cutItem.setText("Cut");
			copyItem.setText("Copy");
			pasteItem.setText("Paste");
			clearItem.setText("Clear");
		}

		/**
		 * Set fonts to the components.
		 */
		private void setFonts() {
			FontConfiguration fontConfig = configuration.getFontConfiguration();
			Font uiBoldFont = fontConfig.getUIBoldFont();

			cutItem.setFont(uiBoldFont);
			copyItem.setFont(uiBoldFont);
			pasteItem.setFont(uiBoldFont);
			clearItem.setFont(uiBoldFont);
		}
	}
}
