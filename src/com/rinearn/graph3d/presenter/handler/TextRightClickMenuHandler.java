package com.rinearn.graph3d.presenter.handler;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.view.TextRightClickMenu;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.ActionMap;
import javax.swing.SwingUtilities;


/**
 * The class handling events of the right click menu (Cut, Copy, and Paste) of text input components.
 *
 * This class is instantiated as fields of other handler classes related to text input components,
 * not as a field of Presenter class.
 */
public final class TextRightClickMenuHandler {

	/** The front-end class of "Model" layer, which provides internal logic procedures and so on. */
	private final Model model;

	/** The front-end class of "View" layer, which provides visible part of GUI without event handling. */
	private final View view;

	/** The front-end class of "Presenter" layer, which invokes Model's procedures triggered by user's action on GUI. */
	private final Presenter presenter;

	/** The right-click menu. */
	private final TextRightClickMenu rightClickMenu;

	/** The text component to which the right click menu belongs. */
	private final JTextComponent textComponent;

	/** The flag for turning on/off the event handling feature of this instance. */
	private volatile boolean eventHandlingEnabled = true;


	/**
	 * Create a new instance handling events and API requests using the specified resources.
	 *
	 * @param model The front-end class of "Model" layer, which provides internal logic procedures and so on.
	 * @param view The front-end class of "View" layer, which provides visible part of GUI without event handling.
	 * @param presenter The front-end class of "Presenter" layer, which handles events occurred on GUI, and API requests.
	 * @param rightClickMenu The right-click menu.
	 * @param textComponent The text component to which the right click menu belongs.
	 */
	public TextRightClickMenuHandler(Model model, View view, Presenter presenter,
			TextRightClickMenu rightClickMenu, JTextComponent textComponent) {

		this.model = model;
		this.view = view;
		this.presenter = presenter;
		this.rightClickMenu = rightClickMenu;
		this.textComponent = textComponent;

		// Add the event listener defined in this class, to pop-up the right-click menu.
		this.textComponent.addMouseListener(new RightClickedEventListener());

		// Add the event listeners of Cut, Copy, and Paste menu, using default action listeners of the text componen.
		ActionMap actionMap = this.textComponent.getActionMap();
		this.rightClickMenu.cutItem.addActionListener(actionMap.get(DefaultEditorKit.cutAction));
		this.rightClickMenu.copyItem.addActionListener(actionMap.get(DefaultEditorKit.copyAction));
		this.rightClickMenu.pasteItem.addActionListener(actionMap.get(DefaultEditorKit.pasteAction));
	}


	/**
	 * Turns on/off the event handling feature of this instance.
	 *
	 * @param enabled Specify false for turning off the event handling feature (enabled by default).
	 */
	public synchronized void setEventHandlingEnabled(boolean enabled) {
		this.eventHandlingEnabled = enabled;
	}


	/**
	 * Gets whether the event handling feature of this instance is enabled.
	 *
	 * @return Returns true if the event handling feature is enabled.
	 */
	public synchronized boolean isEventHandlingEnabled() {
		return this.eventHandlingEnabled;
	}





	// ================================================================================
	//
	// - Event Listeners -
	//
	// ================================================================================


	/**
	 * The event listener handling the event that the text component is right-cliced.
	 */
	private final class RightClickedEventListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent me) {
			if (!eventHandlingEnabled) {
				return;
			}
			if (SwingUtilities.isRightMouseButton(me)) {
				rightClickMenu.popupMenu.show(textComponent, me.getX(), me.getY());
			}
		}
	}
}
