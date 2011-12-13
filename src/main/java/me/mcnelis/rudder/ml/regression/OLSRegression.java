package me.mcnelis.rudder.ml.regression;

import me.mcnelis.rudder.exceptions.NoValidRegressionException;
import me.mcnelis.rudder.records.DoubleMatrix;
import me.mcnelis.rudder.records.Record;
import me.mcnelis.rudder.records.RecordSet;

import org.apache.commons.math.stat.regression.OLSMultipleLinearRegression;

public class OLSRegression {
	protected OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
	protected double[] coefficients;
	
	protected double[] residuals;
	
	public synchronized void runRegression (double[] y, double[][] x) {
		this.residuals = null;
		this.regression.newSampleData(y, x);
		this.coefficients = this.regression.estimateRegressionParameters();
	}
	
	public synchronized void runRegression (DoubleMatrix matrix) {
		this.runRegression(matrix.getYs(), matrix.getXs());
	}
	
	public synchronized void runRegression (RecordSet records) {
		this.runRegression(records.getDoubleMatrix());	
	}
	
	public double[] getCoefficients() throws NoValidRegressionException {
		if(this.coefficients != null)
			return this.coefficients;
		else
			throw new NoValidRegressionException();
	}
	
	public double[] getResiduals() throws NoValidRegressionException {
		if(this.coefficients != null) {
			if(this.residuals == null)
				this.residuals = this.regression.estimateResiduals();
			return this.residuals;
		} else
			throw new NoValidRegressionException();
	}
	
	public double predictValue (Record r) {
		return this.predictValue(r.getFeatures().getFeatureArray());
	}
	
	public double predictValue (double[] xs) {
		double val = 0d;
		for (int i=0; i<this.coefficients.length; i++) {
			if(i!=0)
				val += xs[i-1] * this.coefficients[i];
			else val += this.coefficients[i];
		}
		return val;
	}
	
}
