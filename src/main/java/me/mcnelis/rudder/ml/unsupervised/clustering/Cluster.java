package me.mcnelis.rudder.ml.unsupervised.clustering;

import java.util.ArrayList;
import java.util.Arrays;

import me.mcnelis.rudder.data.Record;
import me.mcnelis.rudder.data.collections.RecordList;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

public class Cluster {
	protected double[] centroid;
	protected RecordList elements = new RecordList();
	
	public Cluster() {}
	
	
	public void addRecord(Record r) {
		this.elements.add(r);
		
	}
	
	public void combineClusters(Cluster c) {
		for(Record r : c.getElements())
			if(!this.elements.contains(r))
				this.elements.add(r);
	}
	
	public RecordList getElements() {
		return this.elements;
	}
	
	public void setElements(RecordList elements) {
		this.elements = elements;
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
	
	protected synchronized double[] calculateCentroid() {
		
		this.centroid = new double[this.elements.get(0).getFeatureAndLabelArray().length];
		
		ArrayList<DescriptiveStatistics> stats = new  ArrayList<DescriptiveStatistics>();
		for (Record elem : this.elements) {
			double[] arr = elem.getFeatureAndLabelArray();
			for (int i=0; i<arr.length; i++) {
				
				DescriptiveStatistics stat;
				try {
					stat = stats.get(i);
				} catch (IndexOutOfBoundsException aiobe) {
					
					stat = new DescriptiveStatistics();
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
		result = prime * result
				+ ((elements == null) ? 0 : elements.hashCode());
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
		if (elements == null) {
			if (other.elements != null) {
				return false;
			}
		} else if (!elements.equals(other.elements)) {
			return false;
		}
		return true;
	}
}
