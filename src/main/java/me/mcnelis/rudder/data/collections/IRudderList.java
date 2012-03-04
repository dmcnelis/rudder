package me.mcnelis.rudder.data.collections;

import java.util.List;

public interface IRudderList<T> extends List<T>
{

	/**
	 * Calls {@link me.mcnelis.rudder.data.Record#getFeatureAndLabelDoubleArray()} on
	 * each element and creates a full dataset.
	 * @return
	 */
	double[][] getUnsupervisedDoubleDoubleArray();

	/**
	 * 
	 * @return double double array of numeric features for all records
	 */
	double[][] getSupervisedFeatures();

	double[] getSupervisedLabels();

	double[] getUnsupervisedSampleDoubleArray();

	double[] getSupervisedSampleDoubleArray();

	int getClusterId();
	
	void setClusterId(int id);

	String getStringLabel(Object r);

	List<Object> getRecordFeatures(Object r);

	double[] getUnsupervisedDoubleArray(Object r2);

	void setNoise(Object r2, boolean b);

	boolean isVisited(Object r);

	void setVisited(Object r, boolean b);
	
	double[] getNumericLabelsForRecord(Object o);
	
	 double[] getItemNumericFeatureArray(int i);
	 double[] getNumericFeatureArray(Object o);
}