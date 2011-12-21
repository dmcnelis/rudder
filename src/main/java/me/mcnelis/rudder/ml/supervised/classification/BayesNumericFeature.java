package me.mcnelis.rudder.ml.supervised.classification;

import java.util.HashMap;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.apache.commons.math.stat.descriptive.SynchronizedSummaryStatistics;

public class BayesNumericFeature implements BayesFeature {

	protected SynchronizedSummaryStatistics stats = new SynchronizedSummaryStatistics();
	public void add(Object newInstance) {

		this.stats.addValue((Double) newInstance);
		
	}

	public HashMap<Object, Double> getProbabilities() {
		HashMap<Object, Double> prob = new HashMap<Object, Double>();
		prob.put("mean", this.stats.getMean());
		prob.put("stdDev", this.stats.getStandardDeviation());
		return prob;
	}

	public double getClassScore(Object featureValue) {
		NormalDistributionImpl dist = new NormalDistributionImpl(
				this.stats.getMean(), 
				this.stats.getStandardDeviation()
				);
		try {
			return dist.cumulativeProbability((Double) featureValue);
		} catch (MathException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Double.NaN;
	}

	public boolean merge(BayesFeature f) {
		if(!(f instanceof BayesNumericFeature)) {
			return false;
		}
		//TODO: Add merge algorithm
		return true;
	}
}
