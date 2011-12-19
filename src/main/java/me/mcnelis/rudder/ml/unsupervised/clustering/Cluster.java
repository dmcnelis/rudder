package me.mcnelis.rudder.ml.unsupervised.clustering;

import java.util.ArrayList;
import java.util.Arrays;

import me.mcnelis.rudder.data.RecordInterface;
import me.mcnelis.rudder.data.collections.RecordList;

import org.apache.commons.math.stat.descriptive.SynchronizedSummaryStatistics;

/**
 * Specialized RecordList for housing and performing
 * operations within a cluster (i.e. holding a centroid
 * 
 * @author dmcnelis
 *
 */
public class Cluster extends RecordList<RecordInterface>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected double[] centroid;
	
	public Cluster() {}
	
	
	public void addRecord(RecordInterface r) {
		this.add(r);
		
	}
	
	public void combineClusters(Cluster c) {
		for (Object o : c.getRecords()) {
			RecordInterface r = (RecordInterface) o;
			if(!this.contains(r))
				this.add(r);
		}
	}
	
	public RecordList<RecordInterface> getRecords() {
		return this;
	}
	
	public double[] getCentroid() {
		if (this.centroid != null)
			return this.centroid;
		else
			return this.calculateCentroid();
	}
	
	public void setCentroid(double[] d) {
		this.centroid = d;
	}
	
	/**
	 * Cycles through elements in the list to average each element
	 * against the other records to create a centroid used in some
	 * forms of clustering algorithms.
	 * 
	 * @return double array representing centroid
	 */
	protected synchronized double[] calculateCentroid() {
		
		this.centroid = new double[this.get(0).getFeatureAndLabelArray().length];
		
		ArrayList<SynchronizedSummaryStatistics> stats = new  ArrayList<SynchronizedSummaryStatistics>();
		for (RecordInterface elem : this) {
			double[] arr = elem.getFeatureAndLabelArray();
			for (int i=0; i<arr.length; i++) {
				
				SynchronizedSummaryStatistics stat;
				try {
					stat = stats.get(i);
				} catch (IndexOutOfBoundsException aiobe) {
					
					stat = new SynchronizedSummaryStatistics();
					stats.add(i, stat);
				}
				stat.addValue(arr[i]);
			}
		}
		
		for (int i=0; i<this.centroid.length; i++) {
			this.centroid[i] = stats.get(i).getMean();
		}
		
		return this.centroid;
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(centroid);
		result = prime * result;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Cluster)) {
			return false;
		}
		Cluster other = (Cluster) obj;
		if (!Arrays.equals(centroid, other.centroid)) {
			return false;
		}
		
		return true;
	}
}
