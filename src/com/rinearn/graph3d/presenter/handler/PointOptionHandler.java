package com.rinearn.graph3d.presenter.handler;

import com.rinearn.graph3d.config.OptionConfiguration;
import com.rinearn.graph3d.config.OptionConfiguration.PointStyleMode;
import com.rinearn.graph3d.config.data.IndexSeriesFilter;
import com.rinearn.graph3d.config.data.SeriesFilterMode;
import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.view.PointOptionWindow;
import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.def.ErrorMessage;
import com.rinearn.graph3d.def.ErrorType;

import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * The class handling events and API requests related to "With Points" option.
 */
public final class PointOptionHandler {

	/** The front-end class of "Model" layer, which provides internal logic procedures and so on. */
	private final Model model;

	/** The front-end class of "View" layer, which provides visible part of GUI without event handling. */
	private final View view;

	/** The front-end class of "Presenter" layer, which invokes Model's procedures triggered by user's action on GUI. */
	private final Presenter presenter;

	/** The event handler of right-click menu of the circle-radius text field. */
	private final TextRightClickMenuHandler circleRadiusMenuHandler;

	/** The event handler of right-click menu of the marker-size text field. */
	private final TextRightClickMenuHandler markerSizeMenuHandler;

	/** The event handler of right-click menu of the vertical-offset-ratio text field. */
	private final TextRightClickMenuHandler markerOffsetRatioMenuHandler;

	/** The event handler of right-click menu of the marker-symbol text field. */
	private final TextRightClickMenuHandler markerSymbolMenuHandler;

	/** The event handler of UI components for series filter settings. */
	private final SeriesFilterHandler seriesFilterHandler;

	/** The flag for turning on/off the event handling feature of this instance. */
	private volatile boolean eventHandlingEnabled = true;


	/** The accessor class to get the series filters from the configuration of this option. */
	private final class SeriesFilterAccessor implements SeriesFilterHandler.SeriesFilterAccessorInterface {
		@Override
		public void setSeriesFilterMode(SeriesFilterMode seriesFilterMode) {
			model.config.getOptionConfiguration().getPointOptionConfiguration().setSeriesFilterMode(seriesFilterMode);
		}
		@Override
		public SeriesFilterMode getSeriesFilterMode() {
			return model.config.getOptionConfiguration().getPointOptionConfiguration().getSeriesFilterMode();
		}
		@Override
		public void setIndexSeriesFilter(IndexSeriesFilter indexSeriesFilter) {
			model.config.getOptionConfiguration().getPointOptionConfiguration().setIndexSeriesFilter(indexSeriesFilter);
		}
		@Override
		public IndexSeriesFilter getIndexSeriesFilter() {
			return model.config.getOptionConfiguration().getPointOptionConfiguration().getIndexSeriesFilter();
		}
	}


	/**
	 * Create a new instance handling events and API requests using the specified resources.
	 *
	 * @param model The front-end class of "Model" layer, which provides internal logic procedures and so on.
	 * @param view The front-end class of "View" layer, which provides visible part of GUI without event handling.
	 * @param presenter The front-end class of "Presenter" layer, which handles events occurred on GUI, and API requests.
	 */
	public PointOptionHandler(Model model, View view, Presenter presenter) {
		this.model = model;
		this.view = view;
		this.presenter = presenter;

		// Add the action listener defined in this class, to the SET button of label setting window.
		PointOptionWindow window = this.view.pointOptionWindow;
		window.setButton.addActionListener(new SetPressedEventListener());

		// Add the event listener handling the event that the selected item of the point-style-mode box is changed.
		window.styleModeBox.addActionListener(new StyleModeBoxChangedEventListener());

		// Add the event listeners to the right-click menus.
		circleRadiusMenuHandler = new TextRightClickMenuHandler(window.circleModeComponents.radiusFieldRightClickMenu, window.circleModeComponents.radiusField);
		markerSizeMenuHandler = new TextRightClickMenuHandler(window.markerModeComponents.sizeFieldRightClickMenu, window.markerModeComponents.sizeField);
		markerOffsetRatioMenuHandler = new TextRightClickMenuHandler(window.markerModeComponents.verticalOffsetRatioFieldRightClickMenu, window.markerModeComponents.verticalOffsetRatioField);
		markerSymbolMenuHandler = new TextRightClickMenuHandler(window.markerModeComponents.symbolFieldRightClickMenu, window.markerModeComponents.symbolField);

		// Add the event handler to UI components for series filter settings.
		seriesFilterHandler = new SeriesFilterHandler(window.seriesFilterComponents, new SeriesFilterAccessor());
	}


	/**
	 * Turns on/off the event handling feature of this instance.
	 *
	 * @param enabled Specify false for turning off the event handling feature (enabled by default).
	 */
	public synchronized void setEventHandlingEnabled(boolean enabled) {
		this.eventHandlingEnabled = enabled;
		circleRadiusMenuHandler.setEventHandlingEnabled(enabled);
		markerSizeMenuHandler.setEventHandlingEnabled(enabled);
		markerOffsetRatioMenuHandler.setEventHandlingEnabled(enabled);
		markerSymbolMenuHandler.setEventHandlingEnabled(enabled);
		seriesFilterHandler.setEventHandlingEnabled(enabled);
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
	private final class SetPressedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			PointOptionWindow window = view.pointOptionWindow;
			OptionConfiguration optionConfig = model.config.getOptionConfiguration();
			OptionConfiguration.PointOptionConfiguration pointOptionConfig = optionConfig.getPointOptionConfiguration();
			boolean isJapanese = model.config.getEnvironmentConfiguration().isLocaleJapanese();

			// Point Style Mode:
			{
				PointStyleMode mode = window.getSelectedPointStyleMode();
				pointOptionConfig.setPointStyleMode(mode);

				if (mode != PointStyleMode.CIRCLE) {
					String errorMessage = isJapanese ? mode + "モードはまだ実装されていません" : mode + " mode is not supported yet.";
					JOptionPane.showMessageDialog(window.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}

			// Circle radius:
			{
				double circleRadius = Double.NaN;
				try {
					circleRadius = Double.parseDouble(window.circleModeComponents.radiusField.getText());
					pointOptionConfig.setCircleRadius(circleRadius);
				} catch (NumberFormatException nfe) {
					String[] errorWords = isJapanese ? new String[]{ "円の半径" } : new String[]{ "Circle Radius" };
					String errorMessage = ErrorMessage.generateErrorMessage(ErrorType.DOUBLE_PARAMETER_PARSING_FAILED, errorWords);
					JOptionPane.showMessageDialog(window.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (circleRadius < 0.0 || 10000.0 < circleRadius) {
					String[] errorWords = isJapanese ? new String[]{ "円の半径", "0.0", "10000.0" } : new String[]{ "Circle Radius", "0.0", "10000.0" };
					String errorMessage = ErrorMessage.generateErrorMessage(ErrorType.DOUBLE_PARAMETER_OUT_OF_RANGE, errorWords);
					JOptionPane.showMessageDialog(window.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				pointOptionConfig.setCircleRadius(circleRadius);
			}

			// Marker texts:
			{
				String markerText = window.markerModeComponents.symbolField.getText().trim();
				markerText = markerText.replaceAll("\\\\,", "___ESCAPED_COMMA___");
				String[] markerTexts = markerText.split(",");
				int textCount = markerTexts.length;
				for (int itext=0; itext<textCount; itext++) {
					markerTexts[itext] = markerTexts[itext].replaceAll("___ESCAPED_COMMA___", "\\,").trim();
				}
				pointOptionConfig.setMarkerTexts(markerTexts);
			}

			// Marker bold font:
			{
				boolean isMarkerBold = window.markerModeComponents.boldBox.isSelected();
				pointOptionConfig.setMarkerBold(isMarkerBold);
			}

			// Marker size:
			{
				double markerSize = Double.NaN;
				try {
					markerSize = Double.parseDouble(window.markerModeComponents.sizeField.getText());
					pointOptionConfig.setMarkerSize(markerSize);
				} catch (NumberFormatException nfe) {
					String[] errorWords = isJapanese ? new String[]{ "文字サイズ" } : new String[]{ "Font Size" };
					String errorMessage = ErrorMessage.generateErrorMessage(ErrorType.DOUBLE_PARAMETER_PARSING_FAILED, errorWords);
					JOptionPane.showMessageDialog(window.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (markerSize < 0.0 || 10000.0 < markerSize) {
					String[] errorWords = isJapanese ? new String[]{ "文字サイズ", "0.0", "10000.0" } : new String[]{ "Font Size", "0.0", "10000.0" };
					String errorMessage = ErrorMessage.generateErrorMessage(ErrorType.DOUBLE_PARAMETER_OUT_OF_RANGE, errorWords);
					JOptionPane.showMessageDialog(window.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				pointOptionConfig.setMarkerSize(markerSize);
			}

			// Marker offset ratio:
			{
				double markerOffsetRatio = Double.NaN;
				try {
					markerOffsetRatio = Double.parseDouble(window.markerModeComponents.verticalOffsetRatioField.getText());
					pointOptionConfig.setMarkerVerticalOffsetRatio(markerOffsetRatio);
				} catch (NumberFormatException nfe) {
					String[] errorWords = isJapanese ? new String[]{ "位置補正率" } : new String[]{ "Offset Ratio" };
					String errorMessage = ErrorMessage.generateErrorMessage(ErrorType.DOUBLE_PARAMETER_PARSING_FAILED, errorWords);
					JOptionPane.showMessageDialog(window.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (markerOffsetRatio < -10000.0 || 10000.0 < markerOffsetRatio) {
					String[] errorWords = isJapanese ? new String[]{ "位置補正率", "-10000.0", "10000.0" } : new String[]{ "Offset Ratio", "0.0", "10000.0" };
					String errorMessage = ErrorMessage.generateErrorMessage(ErrorType.DOUBLE_PARAMETER_OUT_OF_RANGE, errorWords);
					JOptionPane.showMessageDialog(window.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				pointOptionConfig.setMarkerVerticalOffsetRatio(markerOffsetRatio);
			}

			// Update the series filter from the current state of filer-settings UI.
			seriesFilterHandler.updateFilterFromUI(model.config.getEnvironmentConfiguration());

			// Propagate the above update of the configuration to the entire application.
			presenter.propagateConfiguration();

			// Replot the graph.
			presenter.plot();
		}
	}


	/**
	 * The event listener handling the event that the selected item of the point-style-mode box is changed.
	 */
	private final class StyleModeBoxChangedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			PointOptionWindow window = view.pointOptionWindow;
			OptionConfiguration.PointStyleMode pointStyleMode = window.getSelectedPointStyleMode();
			window.setSelectedPointStyleMode(pointStyleMode);
		}
	}
}
