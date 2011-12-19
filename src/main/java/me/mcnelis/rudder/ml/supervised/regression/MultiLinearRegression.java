package me.mcnelis.rudder.ml.supervised.regression;

import java.io.Serializable;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.stat.descriptive.SynchronizedMultivariateSummaryStatistics;
import org.apache.commons.math.stat.regression.GLSMultipleLinearRegression;
import org.apache.commons.math.stat.regression.MultipleLinearRegression;
import org.apache.commons.math.stat.regression.OLSMultipleLinearRegression;

import me.mcnelis.rudder.data.RecordInterface;
import me.mcnelis.rudder.data.collections.RecordList;

/**
 * Wrapper for @link org.apache.commons.math.stat.regression.OLSMultipleLinearRegression
 * and  @link org.apache.commons.math.stat.regression.GLSMultipleLinearRegression
 * 
 * @author dmcnelis
 *
 */
public class MultiLinearRegression implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected RecordList<RecordInterface> records;
	protected double[] betas;
	protected SynchronizedMultivariateSummaryStatistics stats;
	protected MultipleLinearRegression ols;
	protected RegressionTypes type = RegressionTypes.OLS;
	protected double[][] coMatrix;
	
	public MultiLinearRegression() {}
	
	@SuppressWarnings("unchecked")
	public MultiLinearRegression(RecordList<? extends RecordInterface> records) {
		try {
			
			this.records = (RecordList<RecordInterface>) records;
			this.stats = new SynchronizedMultivariateSummaryStatistics(
					this.records.getSupervisedSampleDoubleArray().length,
					false
					);
		} catch (ClassCastException cce) {
			return;
		}
	}
	
	@SuppressWarnings("unchecked")
	public MultiLinearRegression(RecordList<? extends RecordInterface> records, RegressionTypes type) {
		this.type = type;
		try {
			
			this.records = (RecordList<RecordInterface>) records;
			this.stats = new SynchronizedMultivariateSummaryStatistics(
					this.records.getSupervisedSampleDoubleArray().length,
					false
					);
		} catch (ClassCastException cce) {
			return;
		}
	}
	
	/**
	 * Add a record to the dataset
	 * @param record
	 * @return success on adding record, negative if unable to add
	 */
	public boolean addRecord(RecordInterface record) {
		if (this.records == null)  {
			this.records = new RecordList<RecordInterface>();
			this.stats = new SynchronizedMultivariateSummaryStatistics(record.getFeatureArray().length, false);
		}
		try {
			this.stats.addValue(record.getFeatureArray());
			return this.records.add(record);
		} catch (DimensionMismatchException e) {
			return false;
		} catch (
			@SuppressWarnings("deprecation") 
			org.apache.commons.math.DimensionMismatchException e
			) {
			//Deprecated, will remove when we move to Commons Math 3.0
			return false;
		}
	}
	
	/**
	 * Run regression based on your sample data, preferably in a 
	 * threadsafe manner
	 * @return
	 */
	public double[] runRegression() {

		if (this.ols == null) {
			if(this.type == RegressionTypes.OLS) {
				this.ols = new OLSMultipleLinearRegression();
			} else if(this.type == RegressionTypes.GLS){
				this.ols = new GLSMultipleLinearRegression();
			}
		}
		synchronized (this) {
			if(this.type == RegressionTypes.OLS) {
				((OLSMultipleLinearRegression) this.ols)
					.newSampleData(
							this.records.getSupervisedLabels(), 
							this.records.getSupervisedFeatures()
							);
				
			} else if(this.type == RegressionTypes.GLS){
				((GLSMultipleLinearRegression) this.ols)
					.newSampleData(
							this.records.getSupervisedLabels(), 
							this.records.getSupervisedFeatures(), 
							this.coMatrix
							);
			}
			
			
			this.betas = this.ols.estimateRegressionParameters();
		}
		
		return this.betas;
	}
	
	/**
	 * 
	 * @return all parameters.  Y-intercept is [0], coefs for 
	 * rest of model (in order of the class members)
	 */
	public double[] getBetas() {
		if (this.betas == null)
			this.runRegression();
		return this.betas;
	}
	
	/**
	 * 
	 * @return Y Intercept for model
	 */
	public double getIntercept() {
		if (this.betas == null)
			this.runRegression();
		return this.betas[0];
	}
	
	/**
	 * 
	 * @return coefficients for parameters starting at element 0
	 */
	public double[] getCoefficients() {
		if (this.betas == null)
			this.runRegression();
		double[] coefs = new double[this.betas.length-1];
		for(int i=1; i<=coefs.length; i++) {
			coefs[i-1] = this.betas[i];
		}
		return coefs;
	}
	
	public void setCovarianceMatrix(double[][] matrix) {
		this.coMatrix = matrix;
	}
	
	public void setRegressionType(RegressionTypes type) {
		this.type = type;
	}
	
}
