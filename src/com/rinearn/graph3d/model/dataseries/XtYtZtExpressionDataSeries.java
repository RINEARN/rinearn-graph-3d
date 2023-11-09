package com.rinearn.graph3d.model.dataseries;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.model.ScriptEngineMount;

/*
[Inheritance tree]

    AbstractDataSeries
      |
      +- ArrayDataSeries
      |
      +- ExpressionDataSeries
          |
          +- ZxyExpressionDataSeries
          |
          +- XtYtZtExpressionDataSeries < This Class
 */


public class XtYtZtExpressionDataSeries extends ExpressionDataSeries {

	/**
	 * Create an instance for generating data using the specified script engine, under the specified configuration.
	 * 
	 * @param scrioptEngineMount The "engine-mount" object, providing script engines for generating data from math expressions.
	 * @param config The configuration container (for referring the range configuration).
	 */
	public XtYtZtExpressionDataSeries(ScriptEngineMount scriptEngineMount, RinearnGraph3DConfiguration config) {
		super(scriptEngineMount, config);
	}
}
