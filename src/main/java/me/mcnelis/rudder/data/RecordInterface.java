package me.mcnelis.rudder.data;

import me.mcnelis.rudder.exceptions.FeatureNotFoundException;

public interface RecordInterface {

	/**
	 * Set a feature variable for a class, currently all
	 * features must be double
	 * @param featureName
	 * @param d
	 * @return
	 * @throws FeatureNotFoundException
	 */
	public abstract boolean setFeature(String featureName, double d)
			throws FeatureNotFoundException;

	/**
	 * Set the label(s) for the record
	 * @param labelName
	 * @param Object to set label as
	 * @return success of lsetting the label
	 * @throws Exception, usually if you pass an object that can't be cast
	 */
	public abstract boolean setLabel(String labelName, Object o)
			throws Exception;

	/**
	 * Make it easy to see your records when you pass it out to a string
	 * @return
	 */
	public abstract String toString();

	/**
	 * 
	 * @return double array of features for processing
	 */
	public abstract double[] getFeatureArray();

	/**
	 * In unsupervised learning the order of your features is irrelevant
	 * so it doesn't matter what you're going through, as long as the label
	 * is a number.  
	 * 
	 * If your label is a string, you should handle labeling your data in a little
	 * different manner (i.e. give your label a double value type and have your
	 * label be another class member.  When you set that class member, it updates
	 * the label).  This is irrelevant if you're not planning on doing any 
	 * unsupervised learning on this  dataset
	 * 
	 * @return array of all your feature and labels  for unsupervised learning
	 */
	public abstract double[] getFeatureAndLabelArray();

	/**
	 * Get a double representation of the label.
	 * @return
	 */
	public abstract double getDoubleLabel();

	/**
	 * Some algorithms need to be able to test if 
	 * a record is to be considered noise
	 * @return
	 */
	public abstract boolean isNoise();

	/**
	 * Set noise flag
	 * @param noise
	 */
	public abstract void setNoise(boolean noise);

	/**
	 * Some algorithms need to know if the record
	 * has been visited / processed yet
	 * @return
	 */
	public abstract boolean isVisited();

	/**
	 * Set the visited flag
	 * @param visited
	 */
	public abstract void setVisited(boolean visited);

	/**
	 * Set the 'assigned' flag -- mainly for
	 * clustering and classification purposes
	 * @param visited
	 */
	public abstract void setAssigned(boolean visited);

	/**
	 * Some algorithms need to know if the record has
	 * already been assigned
	 * @return
	 */
	public abstract boolean isAssigned();

	/**
	 * Should be a part of your class
	 * @return
	 */
	public abstract int hashCode();

	/**
	 * Test if one record is a duplicate of another
	 * record
	 * @param obj
	 * @return
	 */
	public abstract boolean equals(Object obj);

}