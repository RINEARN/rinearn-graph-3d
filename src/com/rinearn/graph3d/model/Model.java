package com.rinearn.graph3d.model;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.model.data.DataStore;

import org.vcssl.nano.VnanoException;

import javax.swing.JOptionPane;
import java.util.Locale;


/**
 * The front-end class of "Model" layer (com.rinearn.graph3d.model package)
 * of RINEARN Graph 3D.
 *
 * Model layer provides internal components of this software,
 * such as various logic procedures and so on.
 */
public final class Model {

	/** The container of configuration parameters. */
	public final RinearnGraph3DConfiguration config;

	/** The "engine-mount" object, retaining script engines in this application, and wrapping I/O to/from them. */
	public final ScriptEngineMount scriptEngineMount;

	/** The data store, storing and managing the data to be plotted. */
	public final DataStore dataStore = new DataStore();


	/**
	 * Creates new Model layer of RINEARN Graph 3D.
	 *
	 * @param configuration The container of configuration parameters.
	 * @throws IllegalStateException Thrown when it failed in initializing script engines, etc.
	 */
	public Model(RinearnGraph3DConfiguration configuration) {

		// Store the configuration container.
		this.config = configuration;

		// Extract some environment-dependent information from the configuration.
		Locale locale = this.config.getEnvironmentConfiguration().getLocale();
		boolean isJapanese = this.config.getEnvironmentConfiguration().isLocaleJapanese();

		// Initialize script engines.
		try {
			this.scriptEngineMount = new ScriptEngineMount(locale);
		} catch (VnanoException vne) {

			// Create the error message to be displayed on the pop-up window.
			String errorMessage = isJapanese ?
					"スクリプトエンジンの起動に失敗しました:\n\n" + vne.getMessage() :
					"Failed in starting scrpit engines:\n\n" + vne.getMessage();

			// If the error message is too long, crop it.
			int errorLength = errorMessage.length();
			if (150 < errorLength) {
				errorMessage = errorMessage.substring(0, 70) +
						" ... " + errorMessage.substring(errorLength - 70, errorLength) + "\n" +
						(isJapanese ? "(詳細はコンソールに出力)": "(Details are output to the console.)");
			}

			// Show the error message to the user, by popping-up the message window.
			JOptionPane.showMessageDialog(null, errorMessage, "!", JOptionPane.ERROR_MESSAGE);
			throw new IllegalStateException(vne);
		}
	}


	/**
	 * Perform temporary code for development and debugging.
	 */
	public synchronized void temporaryExam() {
		/*
		// ======================================================================
		// Calculate math expressions using Vnano Engine.
		// ======================================================================

		System.out.println("- Vnano Math Calculation Exam -");

		try {
			String expression;
			double x, y, t;
			double result;

			expression = "1 + 2 * (3 - 4 / 5)";
			result = this.scriptEngineMount.calculateMathExpression(expression);
			System.out.println(expression + " = " + result);

			expression = "sin(1)";
			result = this.scriptEngineMount.calculateMathExpression(expression);
			System.out.println(expression + " = " + result);

			expression = "cos(1)";
			result = this.scriptEngineMount.calculateMathExpression(expression);
			System.out.println(expression + " = " + result);

			expression = "sin(1) * sin(1) + cos(1) * cos(1)";
			result = this.scriptEngineMount.calculateMathExpression(expression);
			System.out.println(expression + " = " + result);

			expression = "7 * t";
			t = 3.0;
			result = this.scriptEngineMount.calculateMathExpression(expression, t);
			System.out.println(expression + " = " + result + ", where t=" + t);

			expression = "x*x + 2*x - 3*y + 15";
			x = 2.0;
			y = 3.0;
			result = this.scriptEngineMount.calculateMathExpression(expression, x, y);
			System.out.println(expression + " = " + result + ", where x=" + x + ", y=" + y);

		} catch (VnanoException vne) {
			vne.printStackTrace();
		}
		*/
	}
}
