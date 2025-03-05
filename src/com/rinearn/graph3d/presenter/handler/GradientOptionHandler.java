package com.rinearn.graph3d.presenter.handler;

import com.rinearn.graph3d.config.color.AxisGradientColor;
import com.rinearn.graph3d.config.color.ColorConfiguration;
import com.rinearn.graph3d.config.color.GradientAxis;
import com.rinearn.graph3d.config.color.GradientColor;
import com.rinearn.graph3d.config.data.IndexSeriesFilter;
import com.rinearn.graph3d.config.data.SeriesFilterMode;
import com.rinearn.graph3d.config.environment.EnvironmentConfiguration;
import com.rinearn.graph3d.config.range.RangeConfiguration;
import com.rinearn.graph3d.config.range.AxisRangeConfiguration;
import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.view.GradientOptionWindow;
import com.rinearn.graph3d.view.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;


/**
 * The class handling events and API requests related to "Gradient Coloring" option.
 */
public final class GradientOptionHandler {

	/** The front-end class of "Model" layer, which provides internal logic procedures and so on. */
	private final Model model;

	/** The front-end class of "View" layer, which provides visible part of GUI without event handling. */
	private final View view;

	/** The front-end class of "Presenter" layer, which invokes Model's procedures triggered by user's action on GUI. */
	private final Presenter presenter;

	/** The event handler of UI components for series filter settings. */
	private final SeriesFilterHandler seriesFilterHandler;

	/** The flag for turning on/off the event handling feature of this instance. */
	private volatile boolean eventHandlingEnabled = true;


	/** The accessor class to get the series filters from the configuration of this option. */
	private final class SeriesFilterAccessor implements SeriesFilterHandler.SeriesFilterAccessorInterface {
		private GradientColor getGradientColorFromConfiguration() {
			ColorConfiguration colorConfig = model.config.getColorConfiguration();
			if (colorConfig.getDataGradientColors().length != 1) {
				throw new IllegalStateException("Multiple gradient coloring is not supported on this version yet.");
			}
			return colorConfig.getDataGradientColors()[0];
		}
		@Override
		public void setSeriesFilterMode(SeriesFilterMode seriesFilterMode) {
			this.getGradientColorFromConfiguration().setSeriesFilterMode(seriesFilterMode);
		}
		@Override
		public SeriesFilterMode getSeriesFilterMode() {
			return this.getGradientColorFromConfiguration().getSeriesFilterMode();
		}
		@Override
		public void setIndexSeriesFilter(IndexSeriesFilter indexSeriesFilter) {
			this.getGradientColorFromConfiguration().setIndexSeriesFilter(indexSeriesFilter);
		}
		@Override
		public IndexSeriesFilter getIndexSeriesFilter() {
			return this.getGradientColorFromConfiguration().getIndexSeriesFilter();
		}
	}


	/**
	 * Create a new instance handling events and API requests using the specified resources.
	 *
	 * @param model The front-end class of "Model" layer, which provides internal logic procedures and so on.
	 * @param view The front-end class of "View" layer, which provides visible part of GUI without event handling.
	 * @param presenter The front-end class of "Presenter" layer, which handles events occurred on GUI, and API requests.
	 */
	public GradientOptionHandler(Model model, View view, Presenter presenter) {
		this.model = model;
		this.view = view;
		this.presenter = presenter;

		GradientOptionWindow window = this.view.gradientOptionWindow;

		// Add the event listener handling the event that the selected item of axisBox is changed.
		window.axisBox.addActionListener(new AxisBoxChangedEventListener());

		// Add the action listener to the auto range checkbox.
		window.autoRangeBox.addActionListener(new AutoRangeBoxClickedEventListener());

		// Add the action listener to the SET button.
		window.setButton.addActionListener(new SetPressedEventListener());

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
			GradientOptionWindow window = view.gradientOptionWindow;
			ColorConfiguration colorConfig = model.config.getColorConfiguration();
			EnvironmentConfiguration envConfig = model.config.getEnvironmentConfiguration();

			// Extract a gradient color.
			GradientColor[] gradientColors = colorConfig.getDataGradientColors();
			if (gradientColors.length != 1) {
				throw new IllegalStateException("Multiple gradient coloring is not supported on this version yet.");
			}
			GradientColor gradientColor = gradientColors[0];

			// Extract an axis's gradient color of the above.
			AxisGradientColor[] axisGradientColors = gradientColor.getAxisGradientColors();
			if (axisGradientColors.length != 1) {
				throw new IllegalStateException("Multi-axis gradient color is not supported on this version yet.");
			}
			AxisGradientColor axisGradientColor = axisGradientColors[0];

			// Auto range ON/OFF:
			axisGradientColor.setBoundaryAutoRangeEnabled(window.autoRangeBox.isSelected());

			// Range-min:
			try {
				String rangeMinText = window.minField.getText();
				BigDecimal rangeMin = UIParameterParser.parseBigDecimalParameter(rangeMinText, "下限", "Min", false, null, null, envConfig);
				axisGradientColor.setMinimumBoundaryCoordinate(rangeMin);
			} catch (UIParameterParser.ParsingException e) {
				// The error message is already shown to the user by UIParameterParser.
				return;
			}

			// Range-max:
			try {
				String rangeMaxText = window.maxField.getText();
				BigDecimal rangeMax = UIParameterParser.parseBigDecimalParameter(rangeMaxText, "上限", "Max", false, null, null, envConfig);
				axisGradientColor.setMaximumBoundaryCoordinate(rangeMax);
			} catch (UIParameterParser.ParsingException e) {
				// The error message is already shown to the user by UIParameterParser.
				return;
			}

			// Set the gradient axis.
			GradientAxis axis = window.getSelectedGradientAxis();
			axisGradientColor.setAxis(axis);

			// Update the series filter from the current state of filer-settings UI.
			seriesFilterHandler.updateFilterFromUI(model.config.getEnvironmentConfiguration());

			// Propagate the above update of the configuration to the entire application.
			presenter.propagateConfiguration();

			// Replot the graph.
			presenter.plot();
		}
	}


	/**
	 * The event listener handling the event that the selected item of axisBox is changed.
	 */
	private final class AxisBoxChangedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			updateRangeValues();
		}
	}


	/**
	 * The event listener handling the event that the auto range checkbox is clicked.
	 */
	private final class AutoRangeBoxClickedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			GradientOptionWindow window = view.gradientOptionWindow;
			boolean isSelected = window.autoRangeBox.isSelected();
			window.setAutoRangeEnabled(isSelected);

			if (isSelected) {
				updateRangeValues();
			}
		}
	}


	/**
	 * Updates the values of the text fields of the gradient range.
	 */
	private synchronized void updateRangeValues() {
		GradientOptionWindow window = view.gradientOptionWindow;
		if(!window.autoRangeBox.isSelected()) {
			return;
		}

		RangeConfiguration rangeConfig = model.config.getRangeConfiguration();
		GradientAxis axis = window.getSelectedGradientAxis();
		switch (axis) {
			case X : {
				AxisRangeConfiguration xRangeConfig = rangeConfig.getXRangeConfiguration();
				window.minField.setText(xRangeConfig.getMinimumCoordinate().toString());
				window.maxField.setText(xRangeConfig.getMaximumCoordinate().toString());
				break;
			}
			case Y : {
				AxisRangeConfiguration yRangeConfig = rangeConfig.getYRangeConfiguration();
				window.minField.setText(yRangeConfig.getMinimumCoordinate().toString());
				window.maxField.setText(yRangeConfig.getMaximumCoordinate().toString());
				break;
			}
			case Z : {
				AxisRangeConfiguration zRangeConfig = rangeConfig.getZRangeConfiguration();
				window.minField.setText(zRangeConfig.getMinimumCoordinate().toString());
				window.maxField.setText(zRangeConfig.getMaximumCoordinate().toString());
				break;
			}
			case SCALAR : {
				AxisRangeConfiguration[] extraRangeConfig = rangeConfig.getExtraDimensionRangeConfigurations();
				if (1 <= extraRangeConfig.length) {
					window.minField.setText(extraRangeConfig[0].getMinimumCoordinate().toString());
					window.maxField.setText(extraRangeConfig[0].getMaximumCoordinate().toString());
				}
				break;
			}
			default : {
				throw new UnsupportedOperationException("Unknown gradient axis: " + axis);
			}
		}
	}
}
