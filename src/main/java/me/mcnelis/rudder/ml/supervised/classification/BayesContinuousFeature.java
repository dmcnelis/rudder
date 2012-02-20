package me.mcnelis.rudder.ml.supervised.classification;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.apache.commons.math.stat.descriptive.SynchronizedSummaryStatistics;
import org.apache.log4j.Logger;

public class BayesContinuousFeature implements BayesFeature
{

	private static final Logger LOG = Logger.getLogger(BayesContinuousFeature.class);
	protected SynchronizedSummaryStatistics stats = new SynchronizedSummaryStatistics();

	public void add(Object newInstance)
	{

		this.stats.addValue((Double) newInstance);

	}

	public Map<Object, Double> getProbabilities()
	{
		Map<Object, Double> prob = new HashMap<Object, Double>();
		prob.put("mean", this.stats.getMean());
		prob.put("stdDev", this.stats.getStandardDeviation());
		return prob;
	}

	public double getClassScore(Object featureValue)
	{
		NormalDistributionImpl dist = new NormalDistributionImpl(
				this.stats.getMean(), this.stats.getStandardDeviation());
		try
		{
			return dist.cumulativeProbability((Double) featureValue);
		}
		catch (MathException e)
		{
			LOG.error(e.getLocalizedMessage());
		}
		return Double.NaN;
	}

	public boolean merge(BayesFeature f)
	{
		if (!(f instanceof BayesContinuousFeature))
		{
			return false;
		}
		// TODO: Add merge algorithm
		return true;
	}
}
