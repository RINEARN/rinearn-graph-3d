package com.rinearn.graph3d.presenter.handler;

import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.view.ScaleSettingWindow;
import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.view.MultilingualItem;
import com.rinearn.graph3d.config.ScaleConfiguration;
import com.rinearn.graph3d.config.FrameConfiguration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.SwingUtilities;


/**
 * The class handling events and API requests for setting scale-related parameters.
 */
public class ScaleSettingHandler {

	/** The front-end class of "Model" layer, which provides internal logic procedures and so on. */
	private final Model model;

	/** The front-end class of "View" layer, which provides visible part of GUI without event handling. */
	@SuppressWarnings("unused")
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
	public ScaleSettingHandler(Model model, View view, Presenter presenter) {
		this.model = model;
		this.view = view;
		this.presenter = presenter;

		// Add the action listener defined in this class, to the OK button of label setting window.
		ScaleSettingWindow window = this.view.scaleSettingWindow;
		window.okButton.addActionListener(new OkPressedEventListener());

		// Add the action listeners to the combo box of the ticker modes.
		window.ticksTabItems.xModeBox.addActionListener(new TickerModeBoxEventListener(
				window.ticksTabItems.xModeBox, window.ticksTabItems.xSwappablePanel,
				window.ticksTabItems.xEqualDivisionItems.panel, window.ticksTabItems.xManualItems.panel)
		);
		window.ticksTabItems.yModeBox.addActionListener(new TickerModeBoxEventListener(
				window.ticksTabItems.yModeBox, window.ticksTabItems.ySwappablePanel,
				window.ticksTabItems.yEqualDivisionItems.panel, window.ticksTabItems.yManualItems.panel)
		);
		window.ticksTabItems.zModeBox.addActionListener(new TickerModeBoxEventListener(
				window.ticksTabItems.zModeBox, window.ticksTabItems.zSwappablePanel,
				window.ticksTabItems.zEqualDivisionItems.panel, window.ticksTabItems.zManualItems.panel)
		);
		window.ticksTabItems.colorBarModeBox.addActionListener(new TickerModeBoxEventListener(
				window.ticksTabItems.colorBarModeBox, window.ticksTabItems.colorBarSwappablePanel,
				window.ticksTabItems.colorBarEqualDivisionItems.panel, window.ticksTabItems.colorBarManualItems.panel)
		);

		// Add the action listeners of the check boxes which switches ON/OFF states of auto-formatters.
		window.formatsTabItems.xAutoBox.addActionListener(new AutoFormatBoxEventListener(
				window.formatsTabItems.xAutoBox,
				window.formatsTabItems.xShortFormatLabel, window.formatsTabItems.xMediumFormatLabel, window.formatsTabItems.xLongFormatLabel,
				window.formatsTabItems.xShortFormatField, window.formatsTabItems.xMediumFormatField, window.formatsTabItems.xLongFormatField
		)) ;
		window.formatsTabItems.yAutoBox.addActionListener(new AutoFormatBoxEventListener(
				window.formatsTabItems.yAutoBox,
				window.formatsTabItems.yShortFormatLabel, window.formatsTabItems.yMediumFormatLabel, window.formatsTabItems.yLongFormatLabel,
				window.formatsTabItems.yShortFormatField, window.formatsTabItems.yMediumFormatField, window.formatsTabItems.yLongFormatField
		)) ;
		window.formatsTabItems.zAutoBox.addActionListener(new AutoFormatBoxEventListener(
				window.formatsTabItems.zAutoBox,
				window.formatsTabItems.zShortFormatLabel, window.formatsTabItems.zMediumFormatLabel, window.formatsTabItems.zLongFormatLabel,
				window.formatsTabItems.zShortFormatField, window.formatsTabItems.zMediumFormatField, window.formatsTabItems.zLongFormatField
		)) ;
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

			// !!!!! NOTE !!!!!
			// まずは正常系を固める。異常系での対処はその後に、どこでやるべきか検討しつつ実装する。
			// 候補としては各 config コンテナの validate() にポップアップエラー表示のオプションを付けるとか。
			// ここでやるべきではないと思う。ここはUI層なので、各パラメータの依存関係まで考慮を強いたくはない。

			// Get the configuration containers.
			ScaleConfiguration scaleConfig = model.config.getScaleConfiguration();
			FrameConfiguration frameConfig = model.config.getFrameConfiguration();
			ScaleConfiguration.AxisScaleConfiguration xScaleConfig = scaleConfig.getXScaleConfiguration();
			ScaleConfiguration.AxisScaleConfiguration yScaleConfig = scaleConfig.getYScaleConfiguration();
			ScaleConfiguration.AxisScaleConfiguration zScaleConfig = scaleConfig.getZScaleConfiguration();
			ScaleConfiguration.AxisScaleConfiguration cScaleConfig = scaleConfig.getColorBarScaleConfiguration();

			// Get the inputteds, and store them into the configuration containers.
			ScaleSettingWindow window = view.scaleSettingWindow;

			// "Frame" tab:
			{
				// Frame shape mode
				FrameConfiguration.ShapeMode frameShapeMode = window.designTabItems.getSelectedFrameShapeMode();
				frameConfig.setShapeMode(frameShapeMode);

				// Frame line length
				String frameLineWidthText = window.designTabItems.frameLineWidthField.getText();
				try {
					double frameLineWidth = Double.parseDouble(frameLineWidthText);
					frameConfig.setLineWidth(frameLineWidth);
				} catch (NumberFormatException nfe) {
				}

				// Tick label margin
				String tickLabelMarginText = window.designTabItems.tickLabelMarginField.getText();
				try {
					double tickLabelMargin = Double.parseDouble(tickLabelMarginText);
					xScaleConfig.setTickLabelMargin(tickLabelMargin);
					yScaleConfig.setTickLabelMargin(tickLabelMargin);
					zScaleConfig.setTickLabelMargin(tickLabelMargin);
				} catch (NumberFormatException nfe) {
				}

				// Tick line length
				String tickLineLengthText = window.designTabItems.tickLineLengthField.getText();
				try {
					double tickLineLength = Double.parseDouble(tickLineLengthText);
					xScaleConfig.setTickLineLength(tickLineLength);
					yScaleConfig.setTickLineLength(tickLineLength);
					zScaleConfig.setTickLineLength(tickLineLength);
				} catch (NumberFormatException nfe) {
				}

				// "Inward" box
				boolean isTickInward = window.designTabItems.tickInwardBox.isSelected();
				scaleConfig.setTickInward(isTickInward);
			}

			// "Ticks" tab:
			{
				// Ticker modes.
				ScaleConfiguration.TickerMode xTickerMode = window.ticksTabItems.getXTickerMode();
				ScaleConfiguration.TickerMode yTickerMode = window.ticksTabItems.getYTickerMode();
				ScaleConfiguration.TickerMode zTickerMode = window.ticksTabItems.getZTickerMode();
				ScaleConfiguration.TickerMode cTickerMode = window.ticksTabItems.getColorBarTickerMode();
				xScaleConfig.setTickerMode(xTickerMode);
				yScaleConfig.setTickerMode(yTickerMode);
				zScaleConfig.setTickerMode(zTickerMode);
				cScaleConfig.setTickerMode(cTickerMode);

				// X section count for the EQUAL-DIVISION mode.
				try {
					String xSectionCountText = window.ticksTabItems.xEqualDivisionItems.sectionCountField.getText();
					int xSectionCount = Integer.parseInt(xSectionCountText);
					xScaleConfig.getEqualDivisionTicker().setDividedSectionCount(xSectionCount);
				} catch (NumberFormatException nfe) {
				}

				// Y section count for the EQUAL-DIVISION mode.
				try {
					String ySectionCountText = window.ticksTabItems.yEqualDivisionItems.sectionCountField.getText();
					int ySectionCount = Integer.parseInt(ySectionCountText);
					yScaleConfig.getEqualDivisionTicker().setDividedSectionCount(ySectionCount);
				} catch (NumberFormatException nfe) {
				}

				// Z section count for the EQUAL-DIVISION mode.
				try {
					String zSectionCountText = window.ticksTabItems.zEqualDivisionItems.sectionCountField.getText();
					int zSectionCount = Integer.parseInt(zSectionCountText);
					zScaleConfig.getEqualDivisionTicker().setDividedSectionCount(zSectionCount);
				} catch (NumberFormatException nfe) {
				}

				// color-bar section count for the EQUAL-DIVISION mode.
				try {
					String cSectionCountText = window.ticksTabItems.colorBarEqualDivisionItems.sectionCountField.getText();
					int cSectionCount = Integer.parseInt(cSectionCountText);
					cScaleConfig.getEqualDivisionTicker().setDividedSectionCount(cSectionCount);
				} catch (NumberFormatException nfe) {
				}

				// X tick coordinates and label texts for the MANUAL mode.
				try {
					BigDecimal[] tickCoords = this.parseTickCoordinates(window.ticksTabItems.xManualItems.coordinatesField.getText());
					String[] tickLabelTexts = this.parseTickLabelTexts(window.ticksTabItems.xManualItems.labelsField.getText());
					xScaleConfig.getManualTicker().setTickCoordinates(tickCoords);
					xScaleConfig.getManualTicker().setTickLabelTexts(tickLabelTexts);
				} catch (NumberFormatException nfe) {
				}

				// Y tick coordinates and label texts for the MANUAL mode.
				try {
					BigDecimal[] tickCoords = this.parseTickCoordinates(window.ticksTabItems.yManualItems.coordinatesField.getText());
					String[] tickLabelTexts = this.parseTickLabelTexts(window.ticksTabItems.yManualItems.labelsField.getText());
					yScaleConfig.getManualTicker().setTickCoordinates(tickCoords);
					yScaleConfig.getManualTicker().setTickLabelTexts(tickLabelTexts);
				} catch (NumberFormatException nfe) {
				}

				// Z tick coordinates and label texts for the MANUAL mode.
				try {
					BigDecimal[] tickCoords = this.parseTickCoordinates(window.ticksTabItems.zManualItems.coordinatesField.getText());
					String[] tickLabelTexts = this.parseTickLabelTexts(window.ticksTabItems.zManualItems.labelsField.getText());
					zScaleConfig.getManualTicker().setTickCoordinates(tickCoords);
					zScaleConfig.getManualTicker().setTickLabelTexts(tickLabelTexts);
				} catch (NumberFormatException nfe) {
				}

				// Color-bar tick coordinates and label texts for the MANUAL mode.
				try {
					BigDecimal[] tickCoords = this.parseTickCoordinates(window.ticksTabItems.colorBarManualItems.coordinatesField.getText());
					String[] tickLabelTexts = this.parseTickLabelTexts(window.ticksTabItems.colorBarManualItems.labelsField.getText());
					cScaleConfig.getManualTicker().setTickCoordinates(tickCoords);
					cScaleConfig.getManualTicker().setTickLabelTexts(tickLabelTexts);
				} catch (NumberFormatException nfe) {
				}
			}

			// "Formatters" tab:
			{
				// Formatter modes.
				ScaleConfiguration.TickLabelFormatterMode xFormatterMode = window.formatsTabItems.getXTickLabelFormatterMode();
				ScaleConfiguration.TickLabelFormatterMode yFormatterMode = window.formatsTabItems.getYTickLabelFormatterMode();
				ScaleConfiguration.TickLabelFormatterMode zFormatterMode = window.formatsTabItems.getZTickLabelFormatterMode();
				ScaleConfiguration.TickLabelFormatterMode cFormatterMode = zFormatterMode;
				xScaleConfig.setTickLabelFormatterMode(xFormatterMode);
				yScaleConfig.setTickLabelFormatterMode(yFormatterMode);
				zScaleConfig.setTickLabelFormatterMode(zFormatterMode);
				cScaleConfig.setTickLabelFormatterMode(cFormatterMode);

				String xShortFormat = window.formatsTabItems.xShortFormatField.getText();
				String yShortFormat = window.formatsTabItems.yShortFormatField.getText();
				String zShortFormat = window.formatsTabItems.zShortFormatField.getText();
				String cShortFormat = zShortFormat;
				xScaleConfig.getNumericTickLabelFormatter().setShortRangeFormat(new DecimalFormat(xShortFormat));
				yScaleConfig.getNumericTickLabelFormatter().setShortRangeFormat(new DecimalFormat(yShortFormat));
				zScaleConfig.getNumericTickLabelFormatter().setShortRangeFormat(new DecimalFormat(zShortFormat));
				cScaleConfig.getNumericTickLabelFormatter().setShortRangeFormat(new DecimalFormat(cShortFormat));

				String xMediumFormat = window.formatsTabItems.xMediumFormatField.getText();
				String yMediumFormat = window.formatsTabItems.yMediumFormatField.getText();
				String zMediumFormat = window.formatsTabItems.zMediumFormatField.getText();
				String cMediumFormat = zMediumFormat;
				xScaleConfig.getNumericTickLabelFormatter().setMediumRangeFormat(new DecimalFormat(xMediumFormat));
				yScaleConfig.getNumericTickLabelFormatter().setMediumRangeFormat(new DecimalFormat(yMediumFormat));
				zScaleConfig.getNumericTickLabelFormatter().setMediumRangeFormat(new DecimalFormat(zMediumFormat));
				cScaleConfig.getNumericTickLabelFormatter().setMediumRangeFormat(new DecimalFormat(cMediumFormat));

				String xLongFormat = window.formatsTabItems.xLongFormatField.getText();
				String yLongFormat = window.formatsTabItems.yLongFormatField.getText();
				String zLongFormat = window.formatsTabItems.zLongFormatField.getText();
				String cLongFormat = zLongFormat;
				xScaleConfig.getNumericTickLabelFormatter().setLongRangeFormat(new DecimalFormat(xLongFormat));
				yScaleConfig.getNumericTickLabelFormatter().setLongRangeFormat(new DecimalFormat(yLongFormat));
				zScaleConfig.getNumericTickLabelFormatter().setLongRangeFormat(new DecimalFormat(zLongFormat));
				cScaleConfig.getNumericTickLabelFormatter().setLongRangeFormat(new DecimalFormat(cLongFormat));
			}

			// Propagate the above update of the configuration to the entire application.
			presenter.propagateConfiguration();

			// Replot the graph.
			presenter.plot();
		}

		/**
		 * Parses the text line inputted by users into the array of the tick label texts.
		 *
		 * @param tickLabelTextLine The text line inputted by users.
		 * @return The array of the tick label texts.
		 */
		private String[] parseTickLabelTexts(String tickLabelTextLine) {
			String escapedTextLine = tickLabelTextLine.replaceAll("\\\\,", "___ESCAPED_COMMA___");
			String[] tickLabelTexts = escapedTextLine.split(",");
			int tickLabelCount = tickLabelTexts.length;
			for (int itick=0; itick<tickLabelCount; itick++) {
				tickLabelTexts[itick] = tickLabelTexts[itick].replaceAll("___ESCAPED_COMMA___", "\\,").trim();
			}
			return tickLabelTexts;
		}

		/**
		 * Parses the text line inputted by users into the array of the tick coordinates.
		 *
		 * @param tickLabelTextLine The text line inputted by users.
		 * @return The array of the tick coordinates.
		 * @throw NumberFormatException Thrown if any value which can not be parsed as a number exists.
		 */
		private BigDecimal[] parseTickCoordinates(String tickLabelCoordLine) throws NumberFormatException {
			String[] tickCoordTexts = tickLabelCoordLine.split(",");
			int tickCoordCount = tickCoordTexts.length;
			BigDecimal[] tickCoords = new BigDecimal[tickCoordCount];
			for (int itick=0; itick<tickCoordCount; itick++) {
				try {
					tickCoords[itick] = new BigDecimal(tickCoordTexts[itick].trim());
				} catch (NumberFormatException nfe) {
					tickCoords[itick] = null;
				}
			}
			return tickCoords;
		}
	}


	/**
	 * The event listener handling the event that the selected items of the ticker mode boxes are changed.
	 */
	private final class TickerModeBoxEventListener implements ActionListener {

		/** The combo box to select the ticker mode. */
		private final JComboBox<MultilingualItem> modeBox;

		/** The panel on which the UI panel corresponding to the ticker mode will be mounted. */
		private final JPanel swappablePanel;

		/** The UI panel for EQUAL-DIVISION mode. */
		private final JPanel equalDivisionPanel;

		/** The UI panel for MANUAL mode. */
		private final JPanel manualPanel;

		/**
		 * Creates the instance handling the event on the specified combo box.
		 *
		 * @param modeBox The combo box to select the ticker mode.
		 * @param swappablePanel The panel on which the UI panel corresponding to the ticker mode will be mounted.
		 * @param equalDivisionPanel The UI panel for EQUAL-DIVISION mode.
		 * @param manualPanel The UI panel for MANUAL mode.
		 */
		public TickerModeBoxEventListener(JComboBox<MultilingualItem> modeBox,
				JPanel swappablePanel, JPanel equalDivisionPanel, JPanel manualPanel) {
			this.modeBox = modeBox;
			this.swappablePanel = swappablePanel;
			this.equalDivisionPanel = equalDivisionPanel;
			this.manualPanel = manualPanel;
		}

		@Override
		public void actionPerformed(ActionEvent ae) {
			ScaleSettingWindow window = view.scaleSettingWindow;
			ScaleConfiguration.TickerMode tickerMode = window.ticksTabItems.getSelectedTickerModeOf(this.modeBox);

			// Swaps the UI panel, depending on the ticker mode.
			this.swappablePanel.removeAll();
			switch (tickerMode) {
				case EQUAL_DIVISION : {
					this.swappablePanel.add(this.equalDivisionPanel);
					break;
				}
				case MANUAL : {
					this.swappablePanel.add(this.manualPanel);
					break;
				}
				case CUSTOM : {
					break;
				}
				default : {
					throw new IllegalStateException("Unexpected ticker mode: " + tickerMode);
				}
			}
			window.frame.repaint();
		}
	}



	/**
	 * The event listener handling the event that the selected items of the ticker mode boxes are changed.
	 */
	private final class AutoFormatBoxEventListener implements ActionListener {

		/** The check box to swich ON/OFF of the auto-formatter. */
		JCheckBox autoBox;

		/** The label of the text field to input the format of tick labels in short-range, applied when auto-formatter is OFF. */
		JLabel shortRangeLabel;

		/** The label of the text field to input the format of tick labels in medium-range, applied when auto-formatter is OFF. */
		JLabel mediumRangeLabel;

		/** The label of the text field to input the format of tick labels in long-range, applied when auto-formatter is OFF. */
		JLabel longRangeLabel;

		/** The text field to input the format of tick labels in short-range, applied when auto-formatter is OFF. */
		JTextField shortRangeField;

		/** The text field to input the format of tick labels in medium-range, applied when auto-formatter is OFF. */
		JTextField mediumRangeField;

		/** The text field to input the format of tick labels in long-range, applied when auto-formatter is OFF. */
		JTextField longRangeField;

		/**
		 * Creates the instance handling the event on the specified check box.
		 *
		 * @param autoBox The check box to swich ON/OFF of the auto-formatter.
		 * @param shortRangeLabel The label of the text field to input the format of tick labels in short-range, applied when auto-formatter is OFF.
		 * @param mediumRangeLabel The label of the text field to input the format of tick labels in medium-range, applied when auto-formatter is OFF.
		 * @param longRangeLabel The label of the text field to input the format of tick labels in long-range, applied when auto-formatter is OFF.
		 * @param shortRangeField The text field to input the format of tick labels in short-range, applied when auto-formatter is OFF.
		 * @param mediumRangeField The text field to input the format of tick labels in medium-range, applied when auto-formatter is OFF.
		 * @param longRangeField The text field to input the format of tick labels in long-range, applied when auto-formatter is OFF.
		 */
		public AutoFormatBoxEventListener(JCheckBox autoBox,
				JLabel shortRangeLabel, JLabel mediumRangeLabel, JLabel longRangeLabel,
				JTextField shortRangeField, JTextField mediumRangeField, JTextField longRangeField) {

			this.autoBox = autoBox;
			this.shortRangeLabel = shortRangeLabel;
			this.mediumRangeLabel = mediumRangeLabel;
			this.longRangeLabel = longRangeLabel;
			this.shortRangeField = shortRangeField;
			this.mediumRangeField = mediumRangeField;
			this.longRangeField = longRangeField;
		}

		@Override
		public void actionPerformed(ActionEvent ae) {
			boolean isAutoMode = this.autoBox.isSelected();
			this.shortRangeField.setEditable(!isAutoMode);
			this.mediumRangeField.setEditable(!isAutoMode);
			this.longRangeField.setEditable(!isAutoMode);

			if (isAutoMode) {
				this.shortRangeLabel.setForeground(Color.LIGHT_GRAY);
				this.mediumRangeLabel.setForeground(Color.LIGHT_GRAY);
				this.longRangeLabel.setForeground(Color.LIGHT_GRAY);
				this.shortRangeField.setForeground(Color.LIGHT_GRAY);
				this.mediumRangeField.setForeground(Color.LIGHT_GRAY);
				this.longRangeField.setForeground(Color.LIGHT_GRAY);
			} else {
				this.shortRangeLabel.setForeground(null);
				this.mediumRangeLabel.setForeground(null);
				this.longRangeLabel.setForeground(null);
				this.shortRangeField.setForeground(null);
				this.mediumRangeField.setForeground(null);
				this.longRangeField.setForeground(null);
			}
		}
	}


	// ================================================================================
	//
	// - API Listeners -
	//
	// ================================================================================


	/**
	 * Sets the coordinates (locations) and the labels (displayed texts) of the scale ticks on X axis.
	 *
	 * @param tickCoordinates The coordinates of the scale ticks.
	 * @param tickLabelTexts The label texts of the scale ticks.
	 */
	public void setXTicks(BigDecimal[] tickCoordinates, String[] tickLabelTexts) {

		// Handle the API on the event-dispatcher thread.
		SetXTicksAPIListener apiListener = new SetXTicksAPIListener(tickCoordinates, tickLabelTexts);
		if (SwingUtilities.isEventDispatchThread()) {
			apiListener.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(apiListener);
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * The class handling API requests from setXTicks(-) method,
	 * on event-dispatcher thread.
	 */
	private final class SetXTicksAPIListener implements Runnable {

		/** The coordinates of the scale ticks. */
		private final BigDecimal[] tickCoordinates;

		/** The label texts of the scale ticks. */
		private final String[] tickLabelTexts;

		/**
		 * Create an instance handling setXTicks(-) API with the specified argument.
		 *
		 * @param tickCoordinates The coordinates of the scale ticks.
		 * @param tickLabelTexts The label texts of the scale ticks.
		 */
		public SetXTicksAPIListener(BigDecimal[] tickCoordinates, String[] tickLabelTexts) {
			this.tickCoordinates = tickCoordinates;
			this.tickLabelTexts = tickLabelTexts;
		}

		@Override
		public void run() {
			ScaleConfiguration.AxisScaleConfiguration xScaleConfig
					= model.config.getScaleConfiguration().getXScaleConfiguration();

			xScaleConfig.setTickerMode(ScaleConfiguration.TickerMode.MANUAL);
			xScaleConfig.getManualTicker().setTickCoordinates(tickCoordinates);
			xScaleConfig.getManualTicker().setTickLabelTexts(tickLabelTexts);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}


	/**
	 * Sets the coordinates (locations) and the labels (displayed texts) of the scale ticks on Y axis.
	 *
	 * @param tickCoordinates The coordinates of the scale ticks.
	 * @param tickLabelTexts The label texts of the scale ticks.
	 */
	public void setYTicks(BigDecimal[] tickCoordinates, String[] tickLabelTexts) {

		// Handle the API on the event-dispatcher thread.
		SetYTicksAPIListener apiListener = new SetYTicksAPIListener(tickCoordinates, tickLabelTexts);
		if (SwingUtilities.isEventDispatchThread()) {
			apiListener.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(apiListener);
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * The class handling API requests from setYTicks(-) method,
	 * on event-dispatcher thread.
	 */
	private final class SetYTicksAPIListener implements Runnable {

		/** The coordinates of the scale ticks. */
		private final BigDecimal[] tickCoordinates;

		/** The label texts of the scale ticks. */
		private final String[] tickLabelTexts;

		/**
		 * Create an instance handling setYTicks(-) API with the specified argument.
		 *
		 * @param tickCoordinates The coordinates of the scale ticks.
		 * @param tickLabelTexts The labels of the scale ticks.
		 */
		public SetYTicksAPIListener(BigDecimal[] tickCoordinates, String[] tickLabelTexts) {
			this.tickCoordinates = tickCoordinates;
			this.tickLabelTexts = tickLabelTexts;
		}

		@Override
		public void run() {
			ScaleConfiguration.AxisScaleConfiguration yScaleConfig
					= model.config.getScaleConfiguration().getYScaleConfiguration();

			yScaleConfig.setTickerMode(ScaleConfiguration.TickerMode.MANUAL);
			yScaleConfig.getManualTicker().setTickCoordinates(tickCoordinates);
			yScaleConfig.getManualTicker().setTickLabelTexts(tickLabelTexts);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}


	/**
	 * Sets the coordinates (locations) and the labels (displayed texts) of the scale ticks on Y axis.
	 *
	 * @param tickCoordinates The coordinates of the scale ticks.
	 * @param tickLabelTexts The label texts of the scale ticks.
	 */
	public void setZTicks(BigDecimal[] tickCoordinates, String[] tickLabelTexts) {

		// Handle the API on the event-dispatcher thread.
		SetZTicksAPIListener apiListener = new SetZTicksAPIListener(tickCoordinates, tickLabelTexts);
		if (SwingUtilities.isEventDispatchThread()) {
			apiListener.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(apiListener);
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * The class handling API requests from setZTicks(-) method,
	 * on event-dispatcher thread.
	 */
	private final class SetZTicksAPIListener implements Runnable {

		/** The coordinates of the scale ticks. */
		private final BigDecimal[] tickCoordinates;

		/** The label texts of the scale ticks. */
		private final String[] tickLabelTexts;

		/**
		 * Create an instance handling setZTicks(-) API with the specified argument.
		 *
		 * @param tickCoordinates The coordinates of the scale ticks.
		 * @param tickLabelTexts The label texts of the scale ticks.
		 */
		public SetZTicksAPIListener(BigDecimal[] tickCoordinates, String[] tickLabelTexts) {
			this.tickCoordinates = tickCoordinates;
			this.tickLabelTexts = tickLabelTexts;
		}

		@Override
		public void run() {
			ScaleConfiguration.AxisScaleConfiguration zScaleConfig
					= model.config.getScaleConfiguration().getZScaleConfiguration();

			zScaleConfig.setTickerMode(ScaleConfiguration.TickerMode.MANUAL);
			zScaleConfig.getManualTicker().setTickCoordinates(tickCoordinates);
			zScaleConfig.getManualTicker().setTickLabelTexts(tickLabelTexts);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}

}
