package me.mcnelis.rudder.ml.supervised.classification;

import java.util.Map;

public interface BayesFeature {

	/**
	 * Add a new instance from the training set
	 * 
	 * @param newInstance
	 */
	 void add(Object newInstance);

	/**
	 * Build a hashmap of the probabilities of each attribute
	 * value in a class
	 * @return Hash of attribute value and probability within a class
	 */
	 Map<Object, Double> getProbabilities();
	
	/**
	 * Get the score of an attribute value compared with the 
	 * known information
	 * 
	 * @param featureValue
	 * @return score for this feature  in the class
	 */
	 double getClassScore(Object featureValue);
	
	 boolean equals(Object o);
	
	 int hashCode();
	
	/**
	 * More important once multithreaded support is  added /
	 * MapReduce is built in.
	 * 
	 * @param Feature to merge with this feature
	 * @return success of merger
	 */
	 boolean merge(BayesFeature f);
}