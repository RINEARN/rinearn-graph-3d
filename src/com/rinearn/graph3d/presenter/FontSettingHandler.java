package com.rinearn.graph3d.presenter;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.view.FontSettingWindow;
import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.config.FontConfiguration;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;


/**
 * The class handling events and API requests for setting fonts.
 */
public class FontSettingHandler {

	/** The family name of the default font. */
	private final String DEFAULT_FONT_FAMILY_NAME = "Dialog";

	/** The front-end class of "Model" layer, which provides internal logic procedures and so on. */
	private final Model model;

	/** The front-end class of "View" layer, which provides visible part of GUI without event handling. */
	private final View view;

	/** The front-end class of "Presenter" layer, which invokes Model's procedures triggered by user's action on GUI. */
	private final Presenter presenter;

	/** The flag for turning on/off the event handling feature of this instance. */
	private volatile boolean eventHandlingEnabled = true;


	/**
	 * Create a new instance handling events and API requests using the specified resources.
	 * 
	 * @param model The front-end class of "Model" layer, which provides internal logic procedures and so on.
	 * @param view The front-end class of "View" layer, which provides visible part of GUI without event handling.
	 * @param presenter The front-end class of "Presenter" layer, which handles events occurred on GUI, and API requests.
	 */
	public FontSettingHandler(Model model, View view, Presenter presenter) {
		this.model = model;
		this.view = view;
		this.presenter = presenter;

		// Add the action listener defined in this class, to the OK button of label setting window.
		FontSettingWindow window = this.view.fontSettingWindow;
		window.okButton.addActionListener(new OkPressedEventListener());
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
	 * The event listener handling the event that OK button is pressed.
	 */
	private final class OkPressedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			FontConfiguration fontConfig = model.getConfiguration().getFontConfiguration();
			FontSettingWindow window = view.fontSettingWindow;
			String defaultNameJa = FontSettingWindow.DEFAULT_FONT_ITEM_JAPANESE;
			String defaultNameEn = FontSettingWindow.DEFAULT_FONT_ITEM_ENGLISH;

			// Detect whether the UI language is set to Japanese. (Necessary for generating error messages.)
			boolean isJapanese = model.getConfiguration().getEnvironmentConfiguration().isLocaleJapanese();

			// Update UI plain/bold fonts.
			{
				// Get the font name.
				String uiFontName = String.class.cast(window.uiFontNameBox.getSelectedItem());
				if (uiFontName.equals(defaultNameJa) || uiFontName.equals(defaultNameEn)) {
					uiFontName = DEFAULT_FONT_FAMILY_NAME;
				}

				// Get/parse the font size.
				int fontSize = 1;
				try {
					fontSize = Integer.parseInt(window.uiFontSizeField.getText());
				} catch (NumberFormatException nfe) {
					String errorMessage = isJapanese ?
							"UIフォントのサイズを解釈できません。\n正しい数値が入力されているか、確認してください。" :
							"Can not parse the size of UI font.\nPlease check that input value is a correct numeric value.";
					JOptionPane.showMessageDialog(window.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (fontSize < 1 || 20 < fontSize) {
					String errorMessage = isJapanese ?
							"UIフォントのサイズが許容範囲外です。\n1 から 20 までの範囲の値を入力してください。" :
							"The size of UI font is out of acceptable range.\nPlease input a number from 1 to 20.";
					JOptionPane.showMessageDialog(window.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
					return;					
				}

				// Get the on/off state of "Bold" check box.
				//boolean isBold = window.uiFontBoldBox.isSelected();
	
				// Create a Font instance from the above, and store it into the font configuration container.
				//Font uiFont = new Font(uiFontName, (isBold ? Font.BOLD : Font.PLAIN), fontSize);
				Font uiBoldFont = new Font(uiFontName, Font.BOLD, fontSize);
				Font uiPlainFont = new Font(uiFontName, Font.PLAIN, fontSize);
				fontConfig.setUIBoldFont(uiBoldFont);
				fontConfig.setUIPlainFont(uiPlainFont);
			}

			// Update axis-label font.
			{
				// Get the font name.
				String axisLabelFontName = String.class.cast(window.axisLabelFontNameBox.getSelectedItem());
				if (axisLabelFontName.equals(defaultNameJa) || axisLabelFontName.equals(defaultNameEn)) {
					axisLabelFontName = DEFAULT_FONT_FAMILY_NAME;
				}

				// Get/parse the font size.
				int fontSize = 1;
				try {
					fontSize = Integer.parseInt(window.axisLabelFontSizeField.getText());
				} catch (NumberFormatException nfe) {
					String errorMessage = isJapanese ?
							"軸ラベルフォントのサイズを解釈できません。\n正しい数値が入力されているか、確認してください。" :
							"Can not parse the size of axis label font.\nPlease check that input value is a correct numeric value.";
					JOptionPane.showMessageDialog(window.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (fontSize < 1 || 500 < fontSize) {
					String errorMessage = isJapanese ?
							"軸ラベルフォントのサイズが許容範囲外です。\n1 から 500 までの範囲の値を入力してください。" :
							"The size of axis label font is out of acceptable range.\nPlease input a number from 1 to 500.";
					JOptionPane.showMessageDialog(window.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
					return;					
				}


				// Get the on/off state of "Bold" check box.
				boolean isBold = window.axisLabelFontBoldBox.isSelected();
	
				// Create a Font instance from the above, and store it into the font configuration container.
				Font axisLabelFont = new Font(axisLabelFontName, (isBold ? Font.BOLD : Font.PLAIN), fontSize);
				fontConfig.setAxisLabelFont(axisLabelFont);
			}

			// Update tick-label font.
			{
				// Get the font name.
				String tickLabelFontName = String.class.cast(window.tickLabelFontNameBox.getSelectedItem());
				if (tickLabelFontName.equals(defaultNameJa) || tickLabelFontName.equals(defaultNameEn)) {
					tickLabelFontName = DEFAULT_FONT_FAMILY_NAME;
				}

				// Get/parse the font size.
				int fontSize = 1;
				try {
					fontSize = Integer.parseInt(window.tickLabelFontSizeField.getText());
				} catch (NumberFormatException nfe) {
					String errorMessage = isJapanese ?
							"目盛り数値フォントのサイズを解釈できません。\n正しい数値が入力されているか、確認してください。" :
							"Can not parse the size of scale tick font.\nPlease check that input value is a correct numeric value.";
					JOptionPane.showMessageDialog(window.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (fontSize < 1 || 500 < fontSize) {
					String errorMessage = isJapanese ?
							"目盛り数字フォントのサイズが許容範囲外です。\n1 から 500 までの範囲の値を入力してください。" :
							"The size of scale tick font is out of acceptable range.\nPlease input a number from 1 to 500.";
					JOptionPane.showMessageDialog(window.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
					return;					
				}

				// Get the on/off state of "Bold" check box.
				boolean isBold = window.tickLabelFontBoldBox.isSelected();
	
				// Create a Font instance from the above, and store it into the font configuration container.
				Font tickLabelFont = new Font(tickLabelFontName, (isBold ? Font.BOLD : Font.PLAIN), fontSize);
				fontConfig.setTickLabelFont(tickLabelFont);
			}

			// Propagate the above update of the configuration to the entire application.
			presenter.propagateConfiguration();

			// Replot the graph.
			presenter.plot();
		}
	}
}
