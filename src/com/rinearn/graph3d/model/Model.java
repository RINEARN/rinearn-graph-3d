package com.rinearn.graph3d.model;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.model.data.series.AbstractDataSeries;
import com.rinearn.graph3d.model.data.series.ArrayDataSeries;
import com.rinearn.graph3d.model.data.series.DataSeriesGroup;
import com.rinearn.graph3d.model.data.series.MathDataSeries;

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

	// ↓ 以下の2つ、もう public にして外からそこに追加/削除していった方がシンプルなのでは？
	//    → しかし、系列登録時にフックしたい処理とか後々で生じない？
	//       → 思い浮かぶのは範囲更新とかだけど、それは View 層にも影響するから Presenter 層でやる必用があるし。
	//
	// → なんかそもそもデータ系列をこの階層で直で持つのがよくないような。
	//    データストア的なオブジェクトを作ってそれをここで public で持つのはあり。
	//    データ系列の setter / getter がこの階層に生えまくっているのが煩雑なので。
	//    データストアを介して各系列へのアクセスをどうやるかは（setterかpublicか）はまたデータストア側で考える。

	/** The group of math data series to be plotted. */
	private final DataSeriesGroup<MathDataSeries> mathDataSeriesGroup = new DataSeriesGroup<MathDataSeries>();

	/** The group of array data series to be plotted. */
	private final DataSeriesGroup<ArrayDataSeries> arrayDataSeriesGroup = new DataSeriesGroup<ArrayDataSeries>();


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
	 * Clear all the currently registered data series, without distinction of the type of the data series (math or array).
	 *
	 * The same operation can be performed by calling arrayDataSeriesList() and mathDataSeriesList() methods,
	 * but in some situation, we must perform them as an "atomic" operation. This method is provided for such situation.
	 */
	public synchronized void clearDataSeries() {
		this.arrayDataSeriesGroup.clearAllDataSeries();
		this.mathDataSeriesGroup.clearAllDataSeries();
	}


	/**
	 * Gets the List instance storing the currently registered data series,
	 * without distinction of the type of the data series (math or array).
	 *
	 * @return The (unmodifiable) List storing the currently registered data series.
	 */
	public synchronized List<AbstractDataSeries> getDataSeriesList() {
		List<AbstractDataSeries> dataSeriesList = new ArrayList<AbstractDataSeries>();
		for (ArrayDataSeries dataSeries: this.arrayDataSeriesGroup.getDataSeriesList()) {
			dataSeriesList.add(dataSeries);
		}
		for (MathDataSeries dataSeries: this.mathDataSeriesGroup.getDataSeriesList()) {
			dataSeriesList.add(dataSeries);
		}
		return Collections.unmodifiableList(dataSeriesList);
	}


	/**
	 * Adds (registers) a new math data series.
	 *
	 * @param mathDataSeries The math data series to be added.
	 */
	public synchronized void addMathDataSeries(MathDataSeries mathDataSeries) {
		this.mathDataSeriesGroup.addDataSeries(mathDataSeries);

		// Note: update the graph range here? if necessary.
		//   -> It is necessary for ArrayDataSeries, but it is not necessary for MathDataSeries, probably.
	}


	/**
	 * Remove the lastly registered math data series.
	 *
	 * If this method is called when no math data series is registered, nothing occurs.
	 */
	public synchronized void removeLastMathDataSeries() {
		if (this.mathDataSeriesGroup.getDataSeriesCount() == 0) {
			return;
		}
		this.mathDataSeriesGroup.removeLastDataSeries();
	}


	/**
	 * Clear all currently registered math data series.
	 */
	public synchronized void clearMathDataSeries() {
		this.mathDataSeriesGroup.clearAllDataSeries();
	}


	/**
	 * Gets the List instance storing the currently registered math data series.
	 *
	 * The returned List instance is unmodifiable. For adding/removing elements,
	 * use the methods addMathDataSeries(...), removeLastMathDataSeries(), etc.
	 *
	 * @return The (unmodifiable) List storing the currently registered math data series.
	 */
	public synchronized List<MathDataSeries> getMathDataSeriesList() {
		return this.mathDataSeriesGroup.getDataSeriesList();
	}


	/**
	 * Sets all the array data series to be plotted.
	 *
	 * At first glance, it seems that the completely same operation can performed
	 * by using multiple calls of clearArrayDataSeries() and addArrayDataSeries(ArrayDataSeries) methods.
	 *
	 * However, this operation must be performed as an "atomic operation" in some situation,
	 * especially when asynchronous-plotting feature is enabled.
	 * This method is provided for such situation.
	 *
	 * @param dataSeriesGroup The container in which all the array data series to be plotted are stored.
	 */
	public synchronized void setArrayDataSeriesGroup(DataSeriesGroup<ArrayDataSeries> dataSeriesGroup) {
		this.arrayDataSeriesGroup.clearAllDataSeries();
		for (ArrayDataSeries arrayDataSeries: dataSeriesGroup.getDataSeriesList()) {
			this.arrayDataSeriesGroup.addDataSeries(arrayDataSeries);
		}
	}


	/**
	 * Adds (registers) a new array data series.
	 *
	 * @param arrayDataSeries The array data series to be added.
	 */
	public synchronized void addArrayDataSeries(ArrayDataSeries arrayDataSeries) {
		this.arrayDataSeriesGroup.addDataSeries(arrayDataSeries);

		// TODO: Update the graph range here, before re-plotting.
		// -> いや範囲変更は View 層にも伝搬させる必要があるから Presenter 層でやるべき。ここじゃなくて。
System.out.println("!!! TODO @" + this);
	}


	/**
	 * Adds (registers) new multiple array data series.
	 *
	 * At first glance, it seems that the completely same operation can performed
	 * by using multiple calls of addArrayDataSeries(ArrayDataSeries) method for each data series.
	 *
	 * However, this operation must be performed as an "atomic operation" in some situation,
	 * especially when asynchronous-plotting feature is enabled.
	 * This method is provided for such situation.
	 *
	 * @param dataSeriesGroup The container in which all the array data series to be added are stored.
	 */
	public synchronized void addArrayDataSeriesGroup(DataSeriesGroup<ArrayDataSeries> dataSeriesGroup) {
		for (ArrayDataSeries arrayDataSeries: dataSeriesGroup.getDataSeriesList()) {
			this.arrayDataSeriesGroup.addDataSeries(arrayDataSeries);
		}
	}


	/**
	 * Remove the lastly registered array data series.
	 *
	 * If this method is called when no array data series is registered, nothing occurs.
	 */
	public synchronized void removeLastArrayDataSeries() {
		if (this.arrayDataSeriesGroup.getDataSeriesCount() == 0) {
			return;
		}
		this.arrayDataSeriesGroup.removeLastDataSeries();
	}


	/**
	 * Clear all currently registered array data series.
	 */
	public synchronized void clearArrayDataSeries() {
		this.arrayDataSeriesGroup.clearAllDataSeries();
	}


	/**
	 * Gets the List instance storing the currently registered array data series.
	 *
	 * The returned List is unmodifiable. For adding/removing elements,
	 * use the methods addArrayDataSeries(...), removeLastArrayDataSeries(), etc.
	 *
	 * @return The (unmodifiable) List storing the currently registered array data series.
	 */
	public synchronized List<ArrayDataSeries> getArrayDataSeriesList() {
		return this.arrayDataSeriesGroup.getDataSeriesList();
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
