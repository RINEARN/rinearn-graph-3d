package com.rinearn.graph3d.view;


/**
 * The front-end class of "View" layer (com.rinearn.graph3d.view package)
 * of RINEARN Graph 3D.
 * 
 * View layer provides visible part of GUI, without event handling.
 */
public class View {

	/** The main window of RINEARN Graph 3D (on which a 3D graph is displayed). */
	public volatile MainWindow mainWindow = new MainWindow();


	/**
	 * Creates new View layer of RINEARN Graph 3D.
	 */
	public View() {
	}
}
