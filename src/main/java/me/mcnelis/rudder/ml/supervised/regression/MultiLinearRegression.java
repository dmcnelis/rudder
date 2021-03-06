package me.mcnelis.rudder.ml.supervised.regression;

import me.mcnelis.rudder.data.collections.IRudderList;
import me.mcnelis.rudder.data.collections.RudderList;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.stat.descriptive.SynchronizedMultivariateSummaryStatistics;
import org.apache.commons.math.stat.regression.GLSMultipleLinearRegression;
import org.apache.commons.math.stat.regression.MultipleLinearRegression;
import org.apache.commons.math.stat.regression.OLSMultipleLinearRegression;
import org.apache.log4j.Logger;

/**
 * Wrapper for @link
 * org.apache.commons.math.stat.regression.OLSMultipleLinearRegression and @link
 * org.apache.commons.math.stat.regression.GLSMultipleLinearRegression
 * 
 * @author dmcnelis
 * 
 */
public class MultiLinearRegression<T>
{
	private static final Logger LOG = Logger.getLogger(MultiLinearRegression.class);
	protected IRudderList<T> records;
	protected double[] betas;
	protected SynchronizedMultivariateSummaryStatistics stats;
	protected MultipleLinearRegression ols;
	protected RegressionTypes type;
	protected double[][] coMatrix;

	public MultiLinearRegression()
	{
	}

	@SuppressWarnings("unchecked")
	public MultiLinearRegression(IRudderList<T> records)
	{
		try
		{
			synchronized (this)
			{
				this.records = (IRudderList<T>) records;
				this.stats = new SynchronizedMultivariateSummaryStatistics(
						this.records.getSupervisedSampleDoubleArray().length,
						false);
			}
		}
		catch (ClassCastException cce)
		{
			return;
		}
	}

	@SuppressWarnings("unchecked")
	public MultiLinearRegression(IRudderList<T> records,
			RegressionTypes type)
	{

		try
		{
			synchronized (this)
			{
				this.type = type;
				this.records = (IRudderList<T>) records;
				this.stats = new SynchronizedMultivariateSummaryStatistics(
						this.records.getSupervisedSampleDoubleArray().length,
						false);
			}
		}
		catch (ClassCastException cce)
		{
			return;
		}
	}

	/**
	 * Add a record to the dataset
	 * 
	 * @param record
	 * @return success on adding record, negative if unable to add
	 */
	@SuppressWarnings("unchecked")
	public synchronized boolean addRecord(Object record)
	{
		if (this.records == null)
		{
			this.records = new RudderList<T>();
			
		}
		try
		{
			this.records.add((T) record);
			
			if(this.stats == null)
			{
				this.stats = new SynchronizedMultivariateSummaryStatistics(
					this.records.getSupervisedSampleDoubleArray().length, false);
			}
			this.stats.addValue(this.records.getNumericFeatureArray(record));
			
			return true;
		}
		catch (DimensionMismatchException e)
		{
			LOG.error(e);
			return false;
		}
		catch (@SuppressWarnings("deprecation") org.apache.commons.math.DimensionMismatchException e)
		{
			LOG.error(e);
			// Deprecated, will remove when we move to Commons Math 3.0
			return false;
		}
	}

	/**
	 * Run regression based on your sample data, preferably in a threadsafe
	 * manner
	 * 
	 * @return
	 */
	public double[] runRegression()
	{

		synchronized (this)
		{
			if (this.ols == null)
			{
				if (this.type == null || this.type == RegressionTypes.OLS)
				{
					this.type = RegressionTypes.OLS;
					this.ols = new OLSMultipleLinearRegression();
				}
				else if (this.type == RegressionTypes.GLS)
				{
					this.ols = new GLSMultipleLinearRegression();
				}
			}
		}

		synchronized (this)
		{
			if (this.type == RegressionTypes.OLS)
			{
				((OLSMultipleLinearRegression) this.ols).newSampleData(
						this.records.getSupervisedLabels(),
						this.records.getSupervisedFeatures());

			}
			else if (this.type == RegressionTypes.GLS)
			{
				((GLSMultipleLinearRegression) this.ols).newSampleData(
						this.records.getSupervisedLabels(),
						this.records.getSupervisedFeatures(), this.coMatrix);
			}

			synchronized (this)
			{
				this.betas = this.ols.estimateRegressionParameters();
			}
		}

		final double[] returnBetas = this.betas;

		return returnBetas;
	}

	/**
	 * 
	 * @return all parameters. Y-intercept is [0], coefs for rest of model (in
	 *         order of the class members)
	 */
	public double[] getBetas()
	{
		if (this.betas == null)
		{
			this.runRegression();
		}
		final double[] returnBetas = this.betas;

		return returnBetas;
	}

	/**
	 * 
	 * @return Y Intercept for model
	 */
	public double getIntercept()
	{
		if (this.betas == null)
		{
			this.runRegression();
		}
		return this.betas[0];
	}

	/**
	 * 
	 * @return coefficients for parameters starting at element 0
	 */
	public double[] getCoefficients()
	{
		if (this.betas == null)
		{
			this.runRegression();
		}

		double[] coefs = new double[this.betas.length - 1];
		for (int i = 1; i <= coefs.length; i++)
		{
			coefs[i - 1] = this.betas[i];
		}
		return coefs;
	}

	public synchronized void setRegressionType(RegressionTypes type)
	{
		this.type = type;
	}

}
