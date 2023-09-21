package com.rinearn.graph3d.view;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;


/**
 * The front-end class of "View" layer (com.rinearn.graph3d.view package)
 * of RINEARN Graph 3D.
 * 
 * View layer provides visible part of GUI, without event handling.
 */
public final class View {

	/** The main window of RINEARN Graph 3D (on which a 3D graph is displayed). */
	public final MainWindow mainWindow = new MainWindow();

	/** The container of configuration parameters. */
	RinearnGraph3DConfiguration configuration = null;

	/**
	 * Creates new View layer of RINEARN Graph 3D.
	 * 
	 * @param The container of configuration parameters.
	 */
	public View(RinearnGraph3DConfiguration configuration) {
		this.setConfiguration(configuration);
	}


	/**
	 * Reflects the configuration parameters related to the components in View layer.
	 * 
	 * @param configuration The container of configuration parameters.
	 */
	public synchronized void setConfiguration(RinearnGraph3DConfiguration configuration) {
		this.configuration = configuration;

		// Specify configuration to components in View layer here.
	}
}
