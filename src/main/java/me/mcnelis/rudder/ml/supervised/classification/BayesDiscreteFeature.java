package me.mcnelis.rudder.ml.supervised.classification;

import java.util.HashMap;

/**
 * Place to hold information about a feature in classification
 * @author dmcnelis
 *
 */
public class BayesDiscreteFeature implements BayesFeature {
	protected int instances = 0;
	protected HashMap<String, Integer> valueCounter = new HashMap<String, Integer>();
	HashMap<Object, Double> probabilities; 
	/* (non-Javadoc)
	 * @see me.mcnelis.rudder.ml.supervised.classification.BayesFeature#add(java.lang.Object)
	 */
	public synchronized void add(Object newInstance) {
		String instance = ((String) newInstance).toLowerCase();
		if(!this.valueCounter.containsKey(instance))
			this.valueCounter.put(instance, 1);
		else {
			Integer i = this.valueCounter.get(instance);
			i++;
			this.valueCounter.put(instance, i);
			
		}
		this.instances++;
		this.probabilities = null;
	}

	public synchronized HashMap<Object, Double> getProbabilities() {
		if (this.probabilities == null)
			this.probabilities =  new HashMap<Object, Double>();
		
		for (String value : this.valueCounter.keySet()) {
			
			double probability = (this.valueCounter.get(value).doubleValue() / (double)this.instances);
			
			this.probabilities.put(value, probability);
			
		}
		
		return this.probabilities;
	}

	public double getClassScore(Object featureValue) {
		if(this.probabilities == null)
			if( this.valueCounter.containsKey(featureValue) ) {
				
				return (this.valueCounter.get(featureValue).doubleValue() / (double)this.instances);
			} else
				return 0d;
		else
			return this.probabilities.get(featureValue);
	}
	
	public boolean merge(BayesFeature f) {
		if(!(f instanceof BayesDiscreteFeature)) {
			return false;
		}
		
		//TODO: Add merge algorithm
		
		return true;
	}
}
