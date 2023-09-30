package com.rinearn.graph3d.model;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import org.vcssl.nano.VnanoEngine;
import org.vcssl.nano.VnanoException;
import org.vcssl.nano.interconnect.PluginLoader;

import java.util.Map;
import java.util.HashMap;


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


	/**
	 * Perform temporary code for development and debugging.
	 */
	public synchronized void temporaryExam() {

		// ======================================================================
		// Calculate math expressions using Vnano Engine.
		// ======================================================================

		System.out.println("- Vnano Math Calculation Exam -");

		try {

			// Create a scripting engine of Vnano.
			VnanoEngine engine = new VnanoEngine();

			// Enable the option to handle integers in expressions as floating-point numbers.
			Map<String, Object> optionMap = new HashMap<String, Object>();
	        optionMap.put("EVAL_INT_LITERAL_AS_FLOAT", true);
	        engine.setOptionMap(optionMap);

			// Load plug-ins.
			PluginLoader pluginLoader = new PluginLoader("UTF-8");
			pluginLoader.setPluginListPath("./plugin/VnanoPluginList.txt");
			pluginLoader.load();
			for (Object plugin: pluginLoader.getPluginInstances()) {
				engine.connectPlugin("___VNANO_AUTO_KEY", plugin);
			}

			// Calculate expressions.
			String expression;
			double result;

			expression = "1 + 2 * (3 - 4 / 5)";
			result = (double)engine.executeScript(expression + ";");
			System.out.println(expression + " = " + result);

			expression = "sin(1)";
			result = (double)engine.executeScript(expression + ";");
			System.out.println(expression + " = " + result);

			expression = "cos(1)";
			result = (double)engine.executeScript(expression + ";");
			System.out.println(expression + " = " + result);

			expression = "sin(1) * sin(1) + cos(1) * cos(1)";
			result = (double)engine.executeScript(expression + ";");
			System.out.println(expression + " = " + result);

		} catch (VnanoException vne) {
			vne.printStackTrace();
		}
	}
}
