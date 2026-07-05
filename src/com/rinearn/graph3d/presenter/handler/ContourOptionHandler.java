package com.rinearn.graph3d.presenter.handler;

import com.rinearn.graph3d.config.data.IndexSeriesFilter;
import com.rinearn.graph3d.config.data.SeriesFilterMode;
import com.rinearn.graph3d.config.environment.EnvironmentConfiguration;
import com.rinearn.graph3d.config.plotter.PlotterConfiguration;
import com.rinearn.graph3d.config.plotter.ContourPlotterConfiguration;
import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.view.ContourOptionWindow;
import com.rinearn.graph3d.view.View;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;


/**
 * The class handling events and API requests related to "With Contours" option.
 */
public final class ContourOptionHandler {

	/** The front-end class of "Model" layer, which provides internal logic procedures and so on. */
	private final Model model;

	/** The front-end class of "View" layer, which provides visible part of GUI without event handling. */
	private final View view;

	/** The front-end class of "Presenter" layer, which invokes Model's procedures triggered by user's action on GUI. */
	private final Presenter presenter;

	/** The event handler of the right-click menu of the text field to input the line width. */
	private final TextRightClickMenuHandler lineWidthFieldMenuHandler;

	/** The event handler of the right-click menu of the text field to input the the minimum coordinate of the contours. */
	private final TextRightClickMenuHandler minFieldMenuHandler;

	/** The event handler of the right-click menu of the text field to input the the maximum coordinate of the contours. */
	private final TextRightClickMenuHandler maxFieldMenuHandler;

	/** The event handler of UI components for series filter settings. */
	private final SeriesFilterHandler seriesFilterHandler;

	/** The flag for turning on/off the event handling feature of this instance. */
	private volatile boolean eventHandlingEnabled = true;


	/** The accessor class to get the series filters from the configuration of this option. */
	private final class SeriesFilterAccessor implements SeriesFilterHandler.SeriesFilterAccessorInterface {
		@Override
		public void setSeriesFilterMode(SeriesFilterMode seriesFilterMode) {
			model.config.getPlotterConfiguration().getMeshPlotterConfiguration().setSeriesFilterMode(seriesFilterMode);
		}
		@Override
		public SeriesFilterMode getSeriesFilterMode() {
			return model.config.getPlotterConfiguration().getMeshPlotterConfiguration().getSeriesFilterMode();
		}
		@Override
		public void setIndexSeriesFilter(IndexSeriesFilter indexSeriesFilter) {
			model.config.getPlotterConfiguration().getMeshPlotterConfiguration().setIndexSeriesFilter(indexSeriesFilter);
		}
		@Override
		public IndexSeriesFilter getIndexSeriesFilter() {
			return model.config.getPlotterConfiguration().getMeshPlotterConfiguration().getIndexSeriesFilter();
		}
	}


	/**
	 * Create a new instance handling events and API requests using the specified resources.
	 *
	 * @param model The front-end class of "Model" layer, which provides internal logic procedures and so on.
	 * @param view The front-end class of "View" layer, which provides visible part of GUI without event handling.
	 * @param presenter The front-end class of "Presenter" layer, which handles events occurred on GUI, and API requests.
	 */
	public ContourOptionHandler(Model model, View view, Presenter presenter) {
		this.model = model;
		this.view = view;
		this.presenter = presenter;

		// Add the action listener defined in this class, to the SET button of label setting window.
		ContourOptionWindow window = this.view.contourOptionWindow;
		window.setButton.addActionListener(new SetPressedEventListener());

		// Add the event handler to the auto-range check box.
		window.autoRangeBox.addActionListener(new AutoRangeBoxSelectedEventListener());

		// Add the event handler to the right-click menus.
		this.lineWidthFieldMenuHandler = new TextRightClickMenuHandler(window.lineWidthFieldRightClickMenu, window.lineWidthField);
		this.minFieldMenuHandler = new TextRightClickMenuHandler(window.minFieldRightClickMenu, window.minField);
		this.maxFieldMenuHandler = new TextRightClickMenuHandler(window.maxFieldRightClickMenu, window.maxField);

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
		this.lineWidthFieldMenuHandler.setEventHandlingEnabled(enabled);
		this.minFieldMenuHandler.setEventHandlingEnabled(enabled);
		this.maxFieldMenuHandler.setEventHandlingEnabled(enabled);
		this.seriesFilterHandler.setEventHandlingEnabled(enabled);
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
			ContourOptionWindow window = view.contourOptionWindow;
			PlotterConfiguration plotterConfig = model.config.getPlotterConfiguration();
			ContourPlotterConfiguration contourPlotterConfig = plotterConfig.getContourPlotterConfiguration();
			EnvironmentConfiguration envConfig = model.config.getEnvironmentConfiguration();

			// Line width:
			try {
				String lineWidthText = window.lineWidthField.getText();
				double lineWidth = UIParameterParser.parseDoubleParameter(lineWidthText, "Line Width", "線の幅", 0.0, 10000.0, envConfig);
				contourPlotterConfig.setLineWidth(lineWidth);
			} catch (UIParameterParser.ParsingException e) {
				// The error message is already shown to the user by UIParameterParser.
				return;
			}

			// Division count:
			try {
				String divisionCountText = window.divisionCountField.getText();
				int divisionCount = UIParameterParser.parseIntParameter(divisionCountText, "Division Count", "区間数", 0, 1000000, envConfig);
				contourPlotterConfig.setDivisionCount(divisionCount);
			} catch (UIParameterParser.ParsingException e) {
				// The error message is already shown to the user by UIParameterParser.
				return;
			}

			// Max coordinate:
			try {
				String maxText = window.maxField.getText();
				double maxCoord = UIParameterParser.parseDoubleParameter(maxText, "Max", "上限", -Double.MAX_VALUE, Double.MAX_VALUE, envConfig);
				contourPlotterConfig.setMaximumCoordinate(BigDecimal.valueOf(maxCoord));
			} catch (UIParameterParser.ParsingException e) {
				// The error message is already shown to the user by UIParameterParser.
				return;
			}

			// Min coordinate:
			try {
				String minText = window.minField.getText();
				double minCoord = UIParameterParser.parseDoubleParameter(minText, "Min", "下限", -Double.MAX_VALUE, Double.MAX_VALUE, envConfig);
				contourPlotterConfig.setMinimumCoordinate(BigDecimal.valueOf(minCoord));
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
	 * The event listener handling the event that the series filter is enabled/disabled.
	 */
	private final class AutoRangeBoxSelectedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			ContourOptionWindow window = view.contourOptionWindow;
			PlotterConfiguration plotterConfig = model.config.getPlotterConfiguration();
			ContourPlotterConfiguration contourPlotterConfig = plotterConfig.getContourPlotterConfiguration();

			// Turn ON/OFF the flag of the auto range feature in the config container.
			boolean isEnabled = window.autoRangeBox.isSelected();
			contourPlotterConfig.setAutoRangeEnabled(isEnabled);

			// Enable/Disable the max/min text field.
			window.minField.setEditable(!isEnabled);
			window.maxField.setEditable(!isEnabled);
			if (isEnabled) {
				window.minField.setBackground(Color.LIGHT_GRAY);
				window.maxField.setBackground(Color.LIGHT_GRAY);
				window.minField.setForeground(Color.GRAY);
				window.maxField.setForeground(Color.GRAY);
			} else {
				window.minField.setBackground(Color.WHITE);
				window.maxField.setBackground(Color.WHITE);
				window.minField.setForeground(Color.BLACK);
				window.maxField.setForeground(Color.BLACK);
			}

		}
	}
}
