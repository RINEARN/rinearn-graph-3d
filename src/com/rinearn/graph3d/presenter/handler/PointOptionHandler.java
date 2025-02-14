package com.rinearn.graph3d.presenter.handler;

import com.rinearn.graph3d.config.data.IndexSeriesFilter;
import com.rinearn.graph3d.config.data.SeriesFilterMode;
import com.rinearn.graph3d.config.environment.EnvironmentConfiguration;
import com.rinearn.graph3d.config.plotter.PlotterConfiguration;
import com.rinearn.graph3d.config.plotter.PointPlotterConfiguration;
import com.rinearn.graph3d.config.plotter.PointStyleMode;
import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.view.PointOptionWindow;
import com.rinearn.graph3d.view.View;

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
			model.config.getPlotterConfiguration().getPointPlotterConfiguration().setSeriesFilterMode(seriesFilterMode);
		}
		@Override
		public SeriesFilterMode getSeriesFilterMode() {
			return model.config.getPlotterConfiguration().getPointPlotterConfiguration().getSeriesFilterMode();
		}
		@Override
		public void setIndexSeriesFilter(IndexSeriesFilter indexSeriesFilter) {
			model.config.getPlotterConfiguration().getPointPlotterConfiguration().setIndexSeriesFilter(indexSeriesFilter);
		}
		@Override
		public IndexSeriesFilter getIndexSeriesFilter() {
			return model.config.getPlotterConfiguration().getPointPlotterConfiguration().getIndexSeriesFilter();
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
		this.circleRadiusMenuHandler = new TextRightClickMenuHandler(window.circleModeComponents.radiusFieldRightClickMenu, window.circleModeComponents.radiusField);
		this.markerSizeMenuHandler = new TextRightClickMenuHandler(window.markerModeComponents.sizeFieldRightClickMenu, window.markerModeComponents.sizeField);
		this.markerOffsetRatioMenuHandler = new TextRightClickMenuHandler(window.markerModeComponents.verticalOffsetRatioFieldRightClickMenu, window.markerModeComponents.verticalOffsetRatioField);
		this.markerSymbolMenuHandler = new TextRightClickMenuHandler(window.markerModeComponents.symbolFieldRightClickMenu, window.markerModeComponents.symbolField);

		// Add the event handler to UI components for series filter settings.
		this.seriesFilterHandler = new SeriesFilterHandler(window.seriesFilterComponents, new SeriesFilterAccessor());
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
			PlotterConfiguration plotterConfig = model.config.getPlotterConfiguration();
			PointPlotterConfiguration pointPlotterConfig = plotterConfig.getPointPlotterConfiguration();
			EnvironmentConfiguration envConfig = model.config.getEnvironmentConfiguration();
			boolean isJapanese = envConfig.isLocaleJapanese();

			// Point Style Mode:
			{
				PointStyleMode mode = window.getSelectedPointStyleMode();
				pointPlotterConfig.setPointStyleMode(mode);

				if (mode != PointStyleMode.CIRCLE) {
					String errorMessage = isJapanese ? mode + "モードはまだ実装されていません" : mode + " mode is not supported yet.";
					JOptionPane.showMessageDialog(window.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}

			// Circle radius:
			try {
				String circleRadiusText = window.circleModeComponents.radiusField.getText();
				double circleRadius = UIParameterParser.parseDoubleParameter(circleRadiusText, "円の半径", "Circle Radius", 0.0, 10000.0, envConfig);
				pointPlotterConfig.setCircleRadius(circleRadius);
			} catch (UIParameterParser.ParsingException e) {
				// The error message is already shown to the user by UIParameterParser.
				return;
			}

			// Marker texts:
			try {
				String markerText = window.markerModeComponents.symbolField.getText().trim();
				String[] markerTexts = UIParameterParser.parseCommaSeparatedStringParameters(markerText, "マーカー記号", "Marker Symbols", envConfig);
				pointPlotterConfig.setMarkerTexts(markerTexts);
			} catch (UIParameterParser.ParsingException e) {
				// The error message is already shown to the user by UIParameterParser.
				return;
			}

			// Marker bold font:
			{
				boolean isMarkerBold = window.markerModeComponents.boldBox.isSelected();
				pointPlotterConfig.setMarkerBold(isMarkerBold);
			}

			// Marker size:
			try {
				String markerSizeText = window.markerModeComponents.sizeField.getText();
				double markerSize = UIParameterParser.parseDoubleParameter(markerSizeText, "文字サイズ", "Font Size", 0.0, 10000.0, envConfig);
				pointPlotterConfig.setMarkerSize(markerSize);
			} catch (UIParameterParser.ParsingException e) {
				// The error message is already shown to the user by UIParameterParser.
				return;
			}

			// Marker offset ratio:
			try {
				String markerOffsetRatioText = window.markerModeComponents.verticalOffsetRatioField.getText();
				double markerOffsetRatio = UIParameterParser.parseDoubleParameter(markerOffsetRatioText, "位置補正率", "Offset Ratio", -10000.0, 10000.0, envConfig);
				pointPlotterConfig.setMarkerVerticalOffsetRatio(markerOffsetRatio);
			} catch (UIParameterParser.ParsingException e) {
				// The error message is already shown to the user by UIParameterParser.
				return;
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
			PointStyleMode pointStyleMode = window.getSelectedPointStyleMode();
			window.setSelectedPointStyleMode(pointStyleMode);
		}
	}
}
