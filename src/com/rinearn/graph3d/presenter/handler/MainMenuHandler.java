package com.rinearn.graph3d.presenter.handler;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.model.data.series.DataSeriesGroup;
import com.rinearn.graph3d.model.data.series.MathDataSeries;
import com.rinearn.graph3d.model.data.series.ZxyMathDataSeries;
import com.rinearn.graph3d.model.data.series.XtYtZtMathDataSeries;
import com.rinearn.graph3d.presenter.Presenter;
import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.view.MainWindow;
import com.rinearn.graph3d.def.ErrorMessage;
import com.rinearn.graph3d.def.ErrorType;
import com.rinearn.graph3d.def.CommunicationMessage;
import com.rinearn.graph3d.def.CommunicationType;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.Toolkit;
import java.awt.Color;

import javax.swing.JOptionPane;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;


/**
 * The class handling UI events and API requests related to the main menu.
 */
public final class MainMenuHandler {

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
	public MainMenuHandler(Model model, View view, Presenter presenter) {
		this.model = model;
		this.view = view;
		this.presenter = presenter;

		MainWindow window = this.view.mainWindow;

		// Add the action listeners to the sub menu items in "File" menu.
		window.mainMenu.openDataFileMenuItem.addActionListener(new OpenDataFileItemClickedEventListener());
		window.mainMenu.pasteDataTextMenuItem.addActionListener(new PasteDataTextItemClickedEventListener());
		window.mainMenu.saveImageFileMenuItem.addActionListener(new SaveImageFileItemClickedEventListener());
		window.mainMenu.copyImageMenuItem.addActionListener(new CopyImageItemClickedEventListener());
		window.mainMenu.clearDataFileMenuItem.addActionListener(new ClearDataFileItemClickedEventListener());

		// Add the action listeners to the sub menu items in "Math" menu.
		window.mainMenu.zxyMathMenuItem.addActionListener(new ZxyMathItemClickedEventListener());
		window.mainMenu.xtYtZtMathMenuItem.addActionListener(new XtYtZtMathItemClickedEventListener());
		window.mainMenu.modifyMathMenuItem.addActionListener(new ModifyMathItemClickedEventListener());
		window.mainMenu.removeMathMenuItem.addActionListener(new RemoveMathItemClickedEventListener());
		window.mainMenu.clearMathMenuItem.addActionListener(new ClearMathItemClickedEventListener());

		// Add the action listeners to the sub menu items in "Settings" menu.
		window.mainMenu.rangeSettingMenuItem.addActionListener(new RangeSettingItemClickedEventListener());
		window.mainMenu.labelSettingMenuItem.addActionListener(new LabelSettingItemClickedEventListener());
		window.mainMenu.fontSettingMenuItem.addActionListener(new FontSettingItemClickedEventListener());
		window.mainMenu.cameraSettingMenuItem.addActionListener(new CameraSettingItemClickedEventListener());
		window.mainMenu.lightSettingMenuItem.addActionListener(new LightSettingItemClickedEventListener());
		window.mainMenu.scaleSettingMenuItem.addActionListener(new ScaleSettingItemClickedEventListener());
		window.mainMenu.rendererSettingMenuItem.addActionListener(new RendererSettingItemClickedEventListener());
		window.mainMenu.resetSettingMenuItem.addActionListener(new ResetSettingItemClickedEventListener());
		window.mainMenu.clearAllMenuItem.addActionListener(new ClearAllItemClickedEventListener());

		// Add the action listeners to the sub menu items in "Option" menu.
		window.mainMenu.pointOptionMenuItem.addActionListener(new PointOptionMenuItemSelectedEventListener());
		window.mainMenu.lineOptionMenuItem.addActionListener(new LineOptionMenuItemSelectedEventListener());
		window.mainMenu.meshOptionMenuItem.addActionListener(new MeshOptionMenuItemSelectedEventListener());
		window.mainMenu.surfaceOptionMenuItem.addActionListener(new SurfaceOptionMenuItemSelectedEventListener());
		window.mainMenu.contourOptionMenuItem.addActionListener(new ContourOptionMenuItemSelectedEventListener());
		window.mainMenu.logXOptionMenuItem.addActionListener(new LogXOptionMenuItemSelectedEventListener());
		window.mainMenu.logYOptionMenuItem.addActionListener(new LogYOptionMenuItemSelectedEventListener());
		window.mainMenu.logZOptionMenuItem.addActionListener(new LogZOptionMenuItemSelectedEventListener());
		window.mainMenu.invertXOptionMenuItem.addActionListener(new InvertXOptionMenuItemSelectedEventListener());
		window.mainMenu.invertYOptionMenuItem.addActionListener(new InvertYOptionMenuItemSelectedEventListener());
		window.mainMenu.invertZOptionMenuItem.addActionListener(new InvertZOptionMenuItemSelectedEventListener());
		window.mainMenu.legendOptionMenuItem.addActionListener(new LegendOptionMenuItemSelectedEventListener());
		window.mainMenu.blackScreenOptionMenuItem.addActionListener(new BlackScreenOptionMenuItemSelectedEventListener());
		window.mainMenu.gradientOptionMenuItem.addActionListener(new GradientOptionMenuItemSelectedEventListener());
		window.mainMenu.gridLinesOptionMenuItem.addActionListener(new GridLinesOptionMenuItemSelectedEventListener());
		window.mainMenu.frameLinesOptionMenuItem.addActionListener(new FrameLinesOptionMenuItemSelectedEventListener());
		window.mainMenu.scaleTicksOptionMenuItem.addActionListener(new ScaleTicksOptionMenuItemSelectedEventListener());
		window.mainMenu.axisLabelsOptionMenuItem.addActionListener(new AxisLabelsOptionMenuItemSelectedEventListener());
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
	 * The listener handling the event that "File" > "Open File" menu item is clicked.
	 */
	private final class OpenDataFileItemClickedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			view.dataFileOpeningWindow.setWindowVisible(true);
		}
	}


	/**
	 * The listener handling the event that "File" > "Paste Data" menu item is clicked.
	 *
	 * This class is also instantiated in ScreenRightClickMenuListener
	 * to handle events of a right-click menu item "Paste Data".
	 */
	protected final class PasteDataTextItemClickedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}

			// Paste the data on the clipboard into the text area of the data-pasting window.
			try {
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				String clipboardText = String.class.cast(
						clipboard.getData(DataFlavor.stringFlavor)
				);
				view.dataTextPastingWindow.dataTextArea.setText(clipboardText);

			// If the data on the clipboard is not a text (e.g.: an image).
			} catch (UnsupportedFlavorException | ClassCastException | IOException e) {
				// Do nothing.
			}

			// Show the data-pasting window.
			view.dataTextPastingWindow.setWindowVisible(true);
		}
	}


	/**
	 * The listener handling the event that "File" > "Save Image" menu item is clicked.
	 */
	private final class SaveImageFileItemClickedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			view.imageSavingWindow.frame.setVisible(true);
		}
	}


	/**
	 * The event listener handling the event that the Right-click > "Copy Image" menu is clicked.
	 *
	 * This class is also instantiated in ScreenRightClickMenuListener
	 * to handle events of a right-click menu item "Paste Data".
	 */
	protected final class CopyImageItemClickedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}

			// Call the implementation of copyImage(int,boolean) API, provided by this class.
			// (It is processed on the event-dispatcher thread,
			//  so there is no need to wrap the followings by SwingUtilities.invokeAndWait(...))
			try {
				boolean transfersToClipboard = true;
				int bufferedImageType = BufferedImage.TYPE_INT_RGB;

				// Depending on the environment, if the copied image has the alpha-channel,
				// a warning (not exception) occurs, and we can not catch and handle it.
				//
				//   int bufferedImageType = BufferedImage.TYPE_INT_ARGB;

				presenter.imageIOHandler.copyImage(bufferedImageType, transfersToClipboard);

			} catch (IOException ioe) {
				ioe.printStackTrace();
				String errorMessage = ErrorMessage.generateErrorMessage(ErrorType.FAILED_TO_COPY_IMAGE_TO_CLIPBOARD);
				JOptionPane.showMessageDialog(view.mainWindow.frame, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
			}
		}
	}


	/**
	 * The listener handling the event that "File" > "Clear Data/Files" menu item is clicked.
	 */
	private final class ClearDataFileItemClickedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}

			// Cleare all currently-registered array data series.
			model.dataStore.clearArrayDataSeries();

			// Replot the graph.
			presenter.plot();
		}
	}


	/**
	 * The listener handling the event that "Math" > "Plot z(x,y)" menu item is clicked.
	 */
	private final class ZxyMathItemClickedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			presenter.zxyMathHandler.setMode(ZxyMathHandler.Mode.PLOT);
			view.zxyMathWindow.setWindowVisible(true);
		}
	}


	/**
	 * The listener handling the event that "Math" > "Plot x(t),y(t),z(t)" menu item is clicked.
	 */
	private final class XtYtZtMathItemClickedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			presenter.xtYtZtMathHandler.setMode(XtYtZtMathHandler.Mode.PLOT);
			view.xtYtZtMathWindow.setWindowVisible(true);
		}
	}


	/**
	 * Pop-ups the window to selects a math expression from the currently registered math expressions,
	 * used in RemoveMathItemClickedEventListener and ModifyMathItemClickedEventListener classes.
	 *
	 * @return The index of the selected math expression (or -1 if no expression is selected).
	 */
	private synchronized int selectMathExpression() {

		// Gets all the registered math data series.
		DataSeriesGroup<MathDataSeries> mathDataSeriesGroup = model.dataStore.getMathDataSeriesGroup();
		int seriesCount = mathDataSeriesGroup.getDataSeriesCount();
		if (seriesCount == 0) {
			return -1;
		}

		// Gets the display names of all the math data series.
		String[] seriesDisplayNames = new String[seriesCount];
		for (int iseries=0; iseries<seriesCount; iseries++) {
			seriesDisplayNames[iseries] = (iseries + 1) + ":  " + mathDataSeriesGroup.getDataSeriesAt(iseries).getFullDisplayName();
		}

		// Show the pop-up window to select the math data series to be removed.
		String message = CommunicationMessage.generateCommunicationMessage(CommunicationType.SELECT_MATH_EXPRESSION_TO_BE_REMOVED);
		Object selectedItem = JOptionPane.showInputDialog(
				view.mainWindow.frame, message, "", JOptionPane.PLAIN_MESSAGE, null,
				seriesDisplayNames, seriesDisplayNames[0]
		);

		// If the selected item is null, it means that "Cancel" is clicked.
		if (selectedItem == null) {
			return -1;
		}

		// Get the index of the selected item.
		String selectedText = String.class.cast(selectedItem);
		String selectedIndexString = selectedText.substring(0, selectedText.indexOf(":"));
		int selectedIndex = Integer.parseInt(selectedIndexString) - 1;
		return selectedIndex;
	}


	/**
	 * The listener handling the event that "Math" > "Remove Math Expression" menu item is clicked.
	 */
	private final class RemoveMathItemClickedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}

			// Pop-ups the window to selects a math expression from the currently registered math expressions.
			int selectedIndex = selectMathExpression();
			if (selectedIndex == -1) {
				return;
			}

			// Remove the selected math data series.
			model.dataStore.removeMathDataSeriesAt(selectedIndex);

			// Replot the graph.
			presenter.plot();
		}
	}


	/**
	 * The listener handling the event that "Math" > "Remove Math Expression" menu item is clicked.
	 */
	private final class ModifyMathItemClickedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}

			// Pop-ups the window to selects a math expression from the currently registered math expressions.
			int selectedIndex = selectMathExpression();
			if (selectedIndex == -1) {
				return;
			}

			// Get the selected math data series from the index.
			MathDataSeries selectedSeries = model.dataStore.getMathDataSeriesAt(selectedIndex);

			// Pop-up the window for modifying the selected math data series, depending on the type of the math data series.
			if (selectedSeries instanceof ZxyMathDataSeries) {
				presenter.zxyMathHandler.setUpdateTargetMathDataSeries(ZxyMathDataSeries.class.cast(selectedSeries));
				presenter.zxyMathHandler.setMode(ZxyMathHandler.Mode.UPDATE);
				view.zxyMathWindow.setWindowVisible(true);

			} else if (selectedSeries instanceof XtYtZtMathDataSeries) {
				presenter.xtYtZtMathHandler.setUpdateTargetMathDataSeries(XtYtZtMathDataSeries.class.cast(selectedSeries));
				presenter.xtYtZtMathHandler.setMode(XtYtZtMathHandler.Mode.UPDATE);
				view.xtYtZtMathWindow.setWindowVisible(true);

			} else {
				throw new IllegalStateException("Unexpected math data series type: " + selectedSeries.getClass().getTypeName());
			}
		}
	}


	/**
	 * The listener handling the event that "Math" > "Clear Math Expressions" menu item is clicked.
	 */
	private final class ClearMathItemClickedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}

			// Cleare all currently-registered math data series.
			model.dataStore.clearMathDataSeries();

			// Replot the graph.
			presenter.plot();
		}
	}


	/**
	 * The listener handling the event that "Settings" > "Set Ranges" menu item is clicked.
	 */
	private final class RangeSettingItemClickedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			view.rangeSettingWindow.setWindowVisible(true);
		}
	}


	/**
	 * The listener handling the event that "Settings" > "Set Labels" menu item is clicked.
	 */
	private final class LabelSettingItemClickedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			view.labelSettingWindow.setWindowVisible(true);
		}
	}


	/**
	 * The listener handling the event that "Settings" > "Set Fonts" menu item is clicked.
	 */
	private final class FontSettingItemClickedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			view.fontSettingWindow.setWindowVisible(true);
		}
	}


	/**
	 * The listener handling the event that "Settings" > "Set Camera" menu item is clicked.
	 */
	private final class CameraSettingItemClickedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			view.cameraSettingWindow.setWindowVisible(true);
		}
	}


	/**
	 * The listener handling the event that "Settings" > "Set Camera" menu item is clicked.
	 */
	private final class LightSettingItemClickedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			view.lightSettingWindow.setWindowVisible(true);
		}
	}


	/**
	 * The listener handling the event that "Settings" > "Set Scale" menu item is clicked.
	 */
	private final class ScaleSettingItemClickedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			view.scaleSettingWindow.setWindowVisible(true);
		}
	}


	/**
	 * The listener handling the event that "Settings" > "Set Renderer" menu item is clicked.
	 */
	private final class RendererSettingItemClickedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			view.rendererSettingWindow.setWindowVisible(true);
		}
	}


	/**
	 * The listener handling the event that "Settings" > "Reset Settings" menu item is clicked.
	 */
	private final class ResetSettingItemClickedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}

			// Create the configuration container storing default settings.
			RinearnGraph3DConfiguration defaultConfig = RinearnGraph3DConfiguration.createDefaultConfiguration();

			// Update the locale (language of UI and messages).
			defaultConfig.getEnvironmentConfiguration().setLocale(
					model.config.getEnvironmentConfiguration().getLocale()
			);

			// Update the screen size.
			defaultConfig.getScreenConfiguration().setScreenWidth(
					model.config.getScreenConfiguration().getScreenWidth()
			);
			defaultConfig.getScreenConfiguration().setScreenHeight(
					model.config.getScreenConfiguration().getScreenHeight()
			);

			// Reflect the default settings.
			model.config.merge(defaultConfig);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}


	/**
	 * The listener handling the event that "Edit" > "Clear All" menu item is clicked.
	 */
	private final class ClearAllItemClickedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}

			// Cleare all currently-registered data series.
			model.dataStore.clearArrayDataSeries();
			model.dataStore.clearMathDataSeries();

			// Replot the graph.
			presenter.plot();
		}
	}


	/**
	 * The listener handling the event that "Options" > "With Points" menu item is selected.
	 */
	private final class PointOptionMenuItemSelectedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			boolean isOptionSelected = view.mainWindow.mainMenu.pointOptionMenuItem.isSelected();

			// Show/hide the option settings window.
			view.pointOptionWindow.setWindowVisible(isOptionSelected);

			// Enable/disable the plotting option.
			model.config.getPlotterConfiguration().getPointPlotterConfiguration().setPlotterEnabled(isOptionSelected);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}


	/**
	 * The listener handling the event that "Options" > "With Lines" menu item is selected.
	 */
	private final class LineOptionMenuItemSelectedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			boolean isOptionSelected = view.mainWindow.mainMenu.lineOptionMenuItem.isSelected();

			// Show/hide the option settings window.
			view.lineOptionWindow.setWindowVisible(isOptionSelected);

			// Enable/disable the plotting option.
			model.config.getPlotterConfiguration().getLinePlotterConfiguration().setPlotterEnabled(isOptionSelected);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}


	/**
	 * The listener handling the event that "Options" > "With Meshes" menu item is selected.
	 */
	private final class MeshOptionMenuItemSelectedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			boolean isOptionSelected = view.mainWindow.mainMenu.meshOptionMenuItem.isSelected();

			// Show/hide the option settings window.
			view.meshOptionWindow.setWindowVisible(isOptionSelected);

			// Enable/disable the plotting option.
			model.config.getPlotterConfiguration().getMeshPlotterConfiguration().setPlotterEnabled(isOptionSelected);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}


	/**
	 * The listener handling the event that "Options" > "With Surfaces" menu item is selected.
	 */
	private final class SurfaceOptionMenuItemSelectedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			boolean isOptionSelected = view.mainWindow.mainMenu.surfaceOptionMenuItem.isSelected();

			// Show/hide the option settings window.
			view.surfaceOptionWindow.setWindowVisible(isOptionSelected);

			// Enable/disable the option.
			model.config.getPlotterConfiguration().getSurfacePlotterConfiguration().setPlotterEnabled(isOptionSelected);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}


	/**
	 * The listener handling the event that "Options" > "With Contours" menu item is selected.
	 */
	private final class ContourOptionMenuItemSelectedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			boolean isOptionSelected = view.mainWindow.mainMenu.contourOptionMenuItem.isSelected();

			// Show/hide the option settings window.
			// view.contourOptionWindow.setWindowVisible(isOptionSelected);

			// Enable/disable the option.
			model.config.getPlotterConfiguration().getContourPlotterConfiguration().setPlotterEnabled(isOptionSelected);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}


	/**
	 * The listener handling the event that "Options" > "Gradient Color" menu item is selected.
	 */
	private final class GradientOptionMenuItemSelectedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			boolean isOptionSelected = view.mainWindow.mainMenu.gradientOptionMenuItem.isSelected();

			// Show/hide the option settings window.
			view.gradientOptionWindow.setWindowVisible(isOptionSelected);

			// Enable/disable the option.
			model.config.getColorConfiguration().setDataGradientColorEnabled(isOptionSelected);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}


	/**
	 * The listener handling the event that "Options" > "Grid Lines" menu item is selected.
	 */
	private final class GridLinesOptionMenuItemSelectedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			boolean isOptionSelected = view.mainWindow.mainMenu.gridLinesOptionMenuItem.isSelected();

			// Update scale configuration.
			model.config.getScaleConfiguration().setGridLinesVisible(isOptionSelected);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}


	/**
	 * The listener handling the event that "Options" > "Frame Lines" menu item is selected.
	 */
	private final class FrameLinesOptionMenuItemSelectedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			boolean isOptionSelected = view.mainWindow.mainMenu.frameLinesOptionMenuItem.isSelected();

			// Update scale configuration.
			model.config.getFrameConfiguration().setFrameLinesVisible(isOptionSelected);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}


	/**
	 * The listener handling the event that "Options" > "Scale Ticks" menu item is selected.
	 */
	private final class ScaleTicksOptionMenuItemSelectedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			boolean isOptionSelected = view.mainWindow.mainMenu.scaleTicksOptionMenuItem.isSelected();

			// Update scale configuration.
			model.config.getScaleConfiguration().setTicksVisible(isOptionSelected);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}


	/**
	 * The listener handling the event that "Options" > "BlackScreen" menu item is selected.
	 */
	private final class BlackScreenOptionMenuItemSelectedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			boolean isOptionSelected = view.mainWindow.mainMenu.blackScreenOptionMenuItem.isSelected();

			// Update color configuration.
			if (isOptionSelected) {
				model.config.getColorConfiguration().setBackgroundColor(Color.BLACK);
				model.config.getColorConfiguration().setForegroundColor(Color.WHITE);
			} else {
				model.config.getColorConfiguration().setBackgroundColor(Color.WHITE);
				model.config.getColorConfiguration().setForegroundColor(Color.BLACK);
			}
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}


	/**
	 * The listener handling the event that "Options" > "Legends" menu item is selected.
	 */
	private final class LegendOptionMenuItemSelectedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			boolean isOptionSelected = view.mainWindow.mainMenu.legendOptionMenuItem.isSelected();

			// Enable/disable the option.
			model.config.getLabelConfiguration().setLegendLabelsVisible(isOptionSelected);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}


	/**
	 * The listener handling the event that "Options" > "Axis Labels" menu item is selected.
	 */
	private final class AxisLabelsOptionMenuItemSelectedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			boolean isOptionSelected = view.mainWindow.mainMenu.axisLabelsOptionMenuItem.isSelected();

			// Enable/disable the option.
			model.config.getLabelConfiguration().setAxisLabelsVisible(isOptionSelected);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}


	/**
	 * The listener handling the event that "Option" > "Log X" menu item is selected.
	 */
	public final class LogXOptionMenuItemSelectedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			boolean isOptionSelected = view.mainWindow.mainMenu.logXOptionMenuItem.isSelected();

			// Enable/disable the option.
			model.config.getScaleConfiguration().getXScaleConfiguration().setLogScaleEnabled(isOptionSelected);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}

	/**
	 * The listener handling the event that "Option" > "Log Y" menu item is selected.
	 */
	public final class LogYOptionMenuItemSelectedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			boolean isOptionSelected = view.mainWindow.mainMenu.logYOptionMenuItem.isSelected();

			// Enable/disable the option.
			model.config.getScaleConfiguration().getYScaleConfiguration().setLogScaleEnabled(isOptionSelected);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}

	/**
	 * The listener handling the event that "Option" > "Log Z" menu item is selected.
	 */
	public final class LogZOptionMenuItemSelectedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			boolean isOptionSelected = view.mainWindow.mainMenu.logZOptionMenuItem.isSelected();

			// Enable/disable the option.
			model.config.getScaleConfiguration().getZScaleConfiguration().setLogScaleEnabled(isOptionSelected);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}


	/**
	 * The listener handling the event that "Option" > "Invert X" menu item is selected.
	 */
	public final class InvertXOptionMenuItemSelectedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			boolean isOptionSelected = view.mainWindow.mainMenu.invertXOptionMenuItem.isSelected();

			// Enable/disable the option.
			model.config.getScaleConfiguration().getXScaleConfiguration().setInversionEnabled(isOptionSelected);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}

	/**
	 * The listener handling the event that "Option" > "Invert Y" menu item is selected.
	 */
	public final class InvertYOptionMenuItemSelectedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			boolean isOptionSelected = view.mainWindow.mainMenu.invertYOptionMenuItem.isSelected();

			// Enable/disable the option.
			model.config.getScaleConfiguration().getYScaleConfiguration().setInversionEnabled(isOptionSelected);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}

	/**
	 * The listener handling the event that "Option" > "Invert Z" menu item is selected.
	 */
	public final class InvertZOptionMenuItemSelectedEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			boolean isOptionSelected = view.mainWindow.mainMenu.invertZOptionMenuItem.isSelected();

			// Enable/disable the option.
			model.config.getScaleConfiguration().getZScaleConfiguration().setInversionEnabled(isOptionSelected);
			presenter.propagateConfiguration();
			presenter.plot();
		}
	}


	// ================================================================================
	//
	// - API Listeners -
	//
	// ================================================================================


	/**
	 * Clears all the currently plotted data and math expressions.
	 */
	public void clear() {

		// Handle the API request on the event-dispatcher thread.
		ClearAPIListener apiListener = new ClearAPIListener();
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
	 * The class handling API requests from clear() method, on event-dispatcher thread.
	 */
	private final class ClearAPIListener implements Runnable {
		@Override
		public void run() {
			model.dataStore.clearDataSeries();
			presenter.plot();
		}
	}


	/**
	 * Sets the visibility of the menu bar and the right click menus.
	 *
	 * @param visible Specify true for showing the menu bar and the right click menus.
	 */
	public void setMenuVisible(boolean visible) {

		// Handle the API request on the event-dispatcher thread.
		SetMenuVisibleAPIListener apiListener = new SetMenuVisibleAPIListener(visible);
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
	 * The class handling API requests from setMenuVisible(boolean visible) method,
	 * on event-dispatcher thread.
	 */
	private final class SetMenuVisibleAPIListener implements Runnable {

		/** Stores the visibility of the menus. */
		private volatile boolean visible;

		/**
		 * Create a new instance for handling setMenuVisible(-) API request with the specified argument.
		 *
		 * @param visible Specify the visibility of menus.
		 */
		public SetMenuVisibleAPIListener(boolean visible) {
			this.visible = visible;
		}

		@Override
		public void run() {
			view.mainWindow.setMenuVisible(this.visible);
			view.mainWindow.forceUpdateWindowLayout();
			presenter.renderingLoop.requestRendering();
		}
	}


	/**
	 * Replaces the menu bar of the graph window to the specified menu bar.
	 *
	 * @param menuBar The menu bar to be displayed on the graph window.
	 */
	public void setJMenuBar(JMenuBar menuBar) {

		// Handle the API request on the event-dispatcher thread.
		SetJMenuBarAPIListener apiListener = new SetJMenuBarAPIListener(menuBar);
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
	 * The class handling API requests from setJMenuBar(JMenuBar menuBar) method,
	 * on event-dispatcher thread.
	 */
	private final class SetJMenuBarAPIListener implements Runnable {

		/** The menu bar to be displayed on the graph window. */
		private volatile JMenuBar menuBar;

		/**
		 * Create a new instance for handling setJMenuBar(-) API request with the specified argument.
		 *
		 * @param menuBar The menu bar to be displayed on the graph window.
		 */
		public SetJMenuBarAPIListener(JMenuBar menuBar) {
			this.menuBar = menuBar;
		}

		@Override
		public void run() {
			boolean isMenuVisible = (view.mainWindow.frame.getJMenuBar() != null);

			view.mainWindow.mainMenu.menuBar = this.menuBar;
			if (isMenuVisible) {
				view.mainWindow.frame.setJMenuBar(this.menuBar);
				view.mainWindow.forceUpdateWindowLayout();
			}
		}
	}
}
