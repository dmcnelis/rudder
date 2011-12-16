package me.mcnelis.rudder.data.collections;

import java.util.ArrayList;

import me.mcnelis.rudder.data.Record;

public class RecordList extends ArrayList<Record> {

	protected int clusterIdx=-1;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Calls {@link me.mcnelis.rudder.data.Record#getFeatureAndLabelArray()} on
	 * each element and creates a full dataset.
	 * @return
	 */
	public double[][] getUnsupervisedDoubleDoubleArray() {
		double[][] d = new double[this.size()][];
		int cnt=0;
		for (Record r : this) {
			d[cnt] = r.getFeatureAndLabelArray();
			cnt++;
		}
		return d;
	}

	public double[][] getSupervisedFeatures() {
		double[][] d = new double[this.size()][];
		int cnt=0;
		for (Record r : this) {
			d[cnt] = r.getFeatureArray();
			cnt++;
		}
		return d;
	}
	
	public double[] getSupervisedLabels() {
		double[] d = new double[this.size()];
		int cnt=0;
		for (Record r : this) {
			d[cnt] = r.getDoubleLabel();
			cnt++;
		}
		
		return d;
	}
	
	public int getClusterId() {
		return clusterIdx;
	}
	public void setClusterId(int id) {
		this.clusterIdx = id;
	}
	
	public double[] getUnsupervisedSampleDoubleArray() {
		return this.get(0).getFeatureAndLabelArray();
	}
	public double[] getSupervisedSampleDoubleArray() {
		return this.get(0).getFeatureArray();
	}
}
