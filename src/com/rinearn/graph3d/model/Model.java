package com.rinearn.graph3d.model;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.model.dataseries.MathDataSeries;

import org.vcssl.nano.VnanoException;

import javax.swing.JOptionPane;
import java.util.Locale;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;


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

	/** The list of math data series. */
	private final List<MathDataSeries> mathDataSeriesList = new ArrayList<MathDataSeries>();


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
	 * Adds (registers) a new math data series.
	 *
	 * @param mathDataSeries The math data series to be added.
	 */
	public synchronized void addMathDataSeries(MathDataSeries mathDataSeries) {
		this.mathDataSeriesList.add(mathDataSeries);

		// Note: update the graph range here? if necessary.
		//   -> It is necessary for ArrayDataSeries, but it is not necessary for MathDataSeries, probably.
	}


	/**
	 * Remove the lastly registered math data series.
	 *
	 * If this method is called when no math data series is registered, nothing occurs.
	 */
	public synchronized void removeLastMathDataSeries() {
		if (this.mathDataSeriesList.size() == 0) {
			return;
		}
		this.mathDataSeriesList.remove(this.mathDataSeriesList.size() - 1);
	}


	/**
	 * Clear all currently registered math data series.
	 */
	public synchronized void clearMathDataSeries() {
		this.mathDataSeriesList.clear();
	}


	/**
	 * Gets the list of the currently registered math data series.
	 *
	 * The returned list is unmodifiable. For adding/removing elements,
	 * use the methods addMathDataSeries(...), removeLastMathDataSeries(), etc.
	 *
	 * @return The (unmodifiable) list of the currently registered math data series.
	 */
	public synchronized List<MathDataSeries> getMathDataSeriesList() {
		return Collections.unmodifiableList(this.mathDataSeriesList);
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
