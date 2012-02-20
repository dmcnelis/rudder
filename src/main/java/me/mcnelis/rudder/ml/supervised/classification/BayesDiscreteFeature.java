package me.mcnelis.rudder.ml.supervised.classification;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Place to hold information about a feature in classification
 * 
 * @author dmcnelis
 * 
 */
public class BayesDiscreteFeature implements BayesFeature
{
	protected int instances;
	protected Map<String, Integer> valueCounter = new HashMap<String, Integer>();
	protected Map<Object, Double> probabilities;

	public BayesDiscreteFeature()
	{
		synchronized (this)
		{
			this.instances = 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.mcnelis.rudder.ml.supervised.classification.BayesFeature#add(java.
	 * lang.Object)
	 */
	public synchronized void add(Object newInstance)
	{
		String instance = ((String) newInstance).toLowerCase();
		if (!this.valueCounter.containsKey(instance))
		{
			this.valueCounter.put(instance, 1);
		}
		else
		{
			Integer i = this.valueCounter.get(instance);
			i++;
			this.valueCounter.put(instance, i);

		}
		this.instances++;
		this.probabilities = null;
	}

	public synchronized Map<Object, Double> getProbabilities()
	{
		if (this.probabilities == null)
		{
			this.probabilities = new HashMap<Object, Double>();
		}

		for (Entry<String, Integer> value : this.valueCounter.entrySet())
		{

			double probability = (this.valueCounter.get(value.getKey())
					.doubleValue() / (double) this.instances);

			this.probabilities.put(value.getKey(), probability);

		}

		return this.probabilities;
	}

	public synchronized double getClassScore(Object featureValue)
	{
		if (this.probabilities == null)
			if (this.valueCounter.containsKey(featureValue))
			{

				return (this.valueCounter.get(featureValue).doubleValue() / (double) this.instances);
			}
			else
			{
				return 0d;
			}
		else
		{
			return this.probabilities.get(featureValue);
		}
	}

	public boolean merge(BayesFeature f)
	{
		if (!(f instanceof BayesDiscreteFeature))
		{
			return false;
		}

		// TODO: Add merge algorithm

		return true;
	}
}
