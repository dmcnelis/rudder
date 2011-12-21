package me.mcnelis.rudder.data.collections;

import java.util.ArrayList;

import me.mcnelis.rudder.data.RecordInterface;

public class RecordList<T extends RecordInterface> extends ArrayList<T> {

	protected int clusterIdx=-1;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Calls {@link me.mcnelis.rudder.data.Record#getFeatureAndLabelDoubleArray()} on
	 * each element and creates a full dataset.
	 * @return
	 */
	public double[][] getUnsupervisedDoubleDoubleArray() {
		double[][] d = new double[this.size()][];
		int cnt=0;
		synchronized (this) {
			for (RecordInterface r : this) {
				d[cnt] = r.getFeatureAndLabelDoubleArray();
				cnt++;
			}
		}
		return d;
	}

	public double[][] getSupervisedFeatures() {
		
			double[][] d = new double[this.size()][];
			int cnt=0;
		synchronized (this) {
			for (RecordInterface r : this) {
				d[cnt] = r.getFeatureDoubleArray();
				cnt++;
			}
		}
		return d;
	}
	
	public double[] getSupervisedLabels() {
		double[] d = new double[this.size()];
		int cnt=0;
		synchronized (this) {
			for (RecordInterface r : this) {
				d[cnt] = r.getDoubleLabel();
				cnt++;
			}
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
		return this.get(0).getFeatureAndLabelDoubleArray();
	}
	public double[] getSupervisedSampleDoubleArray() {
		return this.get(0).getFeatureDoubleArray();
	}
}
