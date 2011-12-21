package me.mcnelis.rudder.ml.supervised.classification;

import java.util.HashMap;

public interface BayesFeature {

	/**
	 * Add a new instance from the training set
	 * 
	 * @param newInstance
	 */
	public abstract void add(Object newInstance);

	/**
	 * Build a hashmap of the probabilities of each attribute
	 * value in a class
	 * @return Hash of attribute value and probability within a class
	 */
	public abstract HashMap<Object, Double> getProbabilities();
	
	/**
	 * Get the score of an attribute value compared with the 
	 * known information
	 * 
	 * @param featureValue
	 * @return score for this feature  in the class
	 */
	public abstract double getClassScore(Object featureValue);
	
	public abstract boolean equals(Object o);
	
	/**
	 * More important once multithreaded support is  added /
	 * MapReduce is built in.
	 * 
	 * @param Feature to merge with this feature
	 * @return success of merger
	 */
	public abstract boolean merge(BayesFeature f);
}