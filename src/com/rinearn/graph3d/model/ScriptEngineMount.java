package com.rinearn.graph3d.model;

import org.vcssl.connect.ConnectorPermissionName;
import org.vcssl.connect.ConnectorPermissionValue;
import org.vcssl.nano.VnanoEngine;
import org.vcssl.nano.VnanoException;
import org.vcssl.nano.interconnect.PluginLoader;

import java.util.Map;
import java.util.HashMap;
import java.util.Locale;


/**
 * The class initializing and retaining script engines, and wrapping I/O to/from script engines.
 */
public final class ScriptEngineMount {

	/** The script engine for calculating values of math expressions. */
	private volatile VnanoEngine mathExpressionEngine = null;

	/** The plug-in instance providing parameter variables (x, y, and t) available in math expressions. */
	private MathExpressionParameterPlugin mathExpressionParameterPlugin = null;

	/** The plug-in class providing parameter variables (x, y, and t) available in math expressions. */
	public final class MathExpressionParameterPlugin {

		// Caution: This class must be "public", to be accessed from the script engine.

		/** The parameter "x" of "f(x,y)" form math expressions. */
		public volatile double x = Double.NaN;

		/** The parameter "y" of "f(x,y)" form math expressions. */
		public volatile double y = Double.NaN;

		/** The parameter "t" of "f(t)" form math expressions. */
		public volatile double t = Double.NaN;
	}


	/**
	 * Create a new instance retaining a set of script engines, by the default settings.
	 * 
	 * @param locale The locale of the user's environment, which determines the language of error messages.
	 * @throws VnanoException Thrown when it failed to load/initialize plug-ins, etc.
	 */
	public ScriptEngineMount(Locale locale) throws VnanoException {
		this.initializeMathExpressionEngine(locale);
	}


	/**
	 * Activates the script engine which is used for calculating values of math expressions.
	 * 
	 * This activations is required to be processed before calculating any expressions,
	 * but it takes some overhead costs (required time) for initializing all plug-ins.
	 * 
	 * Hence, especially when calculate expressions repetitively in high frequency,
	 * to avoid degradation of processing speed caused by the above costs,
	 * the timing of this activation must be tuned appropriately.
	 * 
	 * @throws VnanoException Thrown when any error has occurred in the initialization procedure of any plug-in.
	 */
	public synchronized void activateMathExpressionEngine() throws VnanoException {
		this.mathExpressionEngine.activateEngine();
	}


	/**
	 * Deactivates the script engine which is used for calculating values of math expressions.
	 * 
	 * Finalization procedures of all connected plug-ins are invoked by this deactivation,
	 * but it takes some overhead costs (required time) for initializing all plug-ins.
	 * Hence, the timing of this deactivation must be tuned appropriately, same as the activation.
	 * 
	 * @throws VnanoException Thrown when any error has occurred in the finalization procedure of any plug-in.
	 */
	public synchronized void deactivateMathExpressionEngine() throws VnanoException {
		this.mathExpressionEngine.deactivateEngine();
	}

	/**
	 * Calculate the specified math expression having no parameters.
	 * 
	 * @param expression The math expression to be calculated.
	 * @return The calculated value.
	 * @throws VnanoException Throws if any syntax error is detected for the specified expression.
	 */
	public synchronized double calculateMathExpression(String expression) throws VnanoException {
		mathExpressionParameterPlugin.x = Double.NaN;
		mathExpressionParameterPlugin.y = Double.NaN;
		mathExpressionParameterPlugin.t = Double.NaN;
		double calculatedValue = (double)this.mathExpressionEngine.executeScript(expression + ";");
		return calculatedValue;
	}



	/**
	 * Calculate the specified math expression of "f(x,y)" form.
	 * 
	 * @param expression The math expression to be calculated.
	 * @param x The parameter "x" of "f(x,y)".
	 * @param y The parameter "y" of "f(x,y)".
	 * @return The calculated value.
	 * @throws VnanoException Throws if any syntax error is detected for the specified expression.
	 */
	public synchronized double calculateMathExpression(String expression, double x, double y) throws VnanoException {
		mathExpressionParameterPlugin.x = x;
		mathExpressionParameterPlugin.y = y;
		mathExpressionParameterPlugin.t = Double.NaN;
		double calculatedValue = (double)this.mathExpressionEngine.executeScript(expression + ";");
		return calculatedValue;
	}


	/**
	 * Calculate the specified math expression of "f(t)" form.
	 * 
	 * @param expression The math expression to be calculated.
	 * @param t The parameter "t" of "f(t)".
	 * @return The calculated value.
	 */
	public synchronized double calculateMathExpression(String expression, double t) throws VnanoException {
		mathExpressionParameterPlugin.x = Double.NaN;
		mathExpressionParameterPlugin.y = Double.NaN;
		mathExpressionParameterPlugin.t = t;
		double calculatedValue = (double)this.mathExpressionEngine.executeScript(expression + ";");
		return calculatedValue;
	}


	/**
	 * Create a new script engine of Vnano, and initialize it for calculating math expressions.
	 * 
	 * @param locale The locale of the user's environment, which determines the language of error messages.
	 * @return An initialized engine.
	 * @throws VnanoException Thrown when it failed to load/initialize plug-ins, etc.
	 */
	private final void initializeMathExpressionEngine(Locale locale) throws VnanoException {

		// See also the tutorial guide for using Vnano, if necessary:
		//     https://www.vcssl.org/en-us/vnano/doc/tutorial/

		// Create a script engine of Vnano.
		this.mathExpressionEngine = new VnanoEngine();

		// Enable options for calculating math expressions.
		// For details of each option, see:
		//     https://www.vcssl.org/en-us/vnano/spec/#options
		Map<String, Object> optionMap = new HashMap<String, Object>();
		optionMap.put("LOCALE", locale);
		optionMap.put("EVAL_INT_LITERAL_AS_FLOAT", true);
		optionMap.put("EVAL_ONLY_EXPRESSION", true);
		optionMap.put("EVAL_ONLY_FLOAT", true);
		optionMap.put("AUTOMATIC_ACTIVATION_ENABLED", false);
		optionMap.put("MAIN_SCRIPT_NAME", "Input_Expression");
		this.mathExpressionEngine.setOptionMap(optionMap);

		// Configure permission items.
		// Set "DENY" to the default, and set "ALLOW" to only items unnecessary for calculating math expression.
		// For details of each option, see:
		//     https://www.vcssl.org/en-us/vnano/spec/#permissions
		Map<String, String> permissionMap = new HashMap<String, String>();
		permissionMap.put(ConnectorPermissionName.DEFAULT, ConnectorPermissionValue.DENY);
		/* If there is any permission item necessary for this engine's usage, "ALLOW" it here. */
		this.mathExpressionEngine.setPermissionMap(permissionMap);

		// Load plug-ins listed in "./plugin/VnanoPluginList.txt".
		try {
			PluginLoader pluginLoader = new PluginLoader("UTF-8");
			pluginLoader.setPluginListPath("./plugin/VnanoPluginList.txt");
			pluginLoader.load();
			for (Object plugin: pluginLoader.getPluginInstances()) {
				this.mathExpressionEngine.connectPlugin("___VNANO_AUTO_KEY", plugin);

			}

		// The locale setting does not affect to error messages occurred in PluginLoader,
		// so set the locale to the exception explicitly.
		} catch (VnanoException vne) {
			vne.setLocale(locale);
			throw vne;
		}

		// Instantiate a plug-in providing parameter variables (x, y, and t) available in math expressions,
		// and connect it to the engine.
		this.mathExpressionParameterPlugin = new MathExpressionParameterPlugin();
		this.mathExpressionEngine.connectPlugin("MathExprParamPlugin", this.mathExpressionParameterPlugin);
	}
}
