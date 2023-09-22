package com.rinearn.graph3d.model;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;

/**
 * The front-end class of "Model" layer (com.rinearn.graph3d.model package)
 * of RINEARN Graph 3D.
 * 
 * Model layer provides internal components of this software,
 * such as various logic procedures and so on.
 */
public final class Model {

	/** The container of configuration parameters. */
	private final RinearnGraph3DConfiguration config;


	/**
	 * Creates new Model layer of RINEARN Graph 3D.
	 * 
	 * @param configuration The container of configuration parameters.
	 */
	public Model(RinearnGraph3DConfiguration configuration) {
		this.config = configuration;
	}


	/**
	 * Returns the container of configuration parameters.
	 * 
	 * @return The container of configuration parameters.
	 */
	public synchronized RinearnGraph3DConfiguration getConfiguration() {
		return this.config;
	}
}
