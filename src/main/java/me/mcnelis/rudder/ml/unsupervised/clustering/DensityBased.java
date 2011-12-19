package me.mcnelis.rudder.ml.unsupervised.clustering;

import java.util.ArrayList;

import me.mcnelis.rudder.data.Record;
import me.mcnelis.rudder.data.collections.RecordList;

import org.apache.commons.math.stat.descriptive.SynchronizedSummaryStatistics;
import org.apache.commons.math.util.MathUtils;

public abstract class DensityBased {

	protected double epsilon;
	protected int minPts;
	protected int minClusters;
	protected SynchronizedSummaryStatistics distance = new SynchronizedSummaryStatistics();
	protected ArrayList<Cluster> clusters;
	protected RecordList sourceData;
	
	public void setSourceData(RecordList rl) {
		this.sourceData = rl;
	}
	
	/**
	 * 
	 * @param epsilon -- the max distance between elements in the cluster
	 * @param minPts
	 */
	protected DensityBased (double epsilon, int minPts) {
		this.epsilon = epsilon;
		this.minPts = minPts;
	}
	
	public ArrayList<Cluster> getClusters() {
		if(this.clusters == null){
			this.clusters = new ArrayList<Cluster>();
			this.clusters = this.cluster();
		}
		return this.clusters;
	}
	
	protected abstract ArrayList<Cluster> cluster();
	
	/**
	 * Find the neighbors of r within range (this.epsilon)
	 * 
	 * Create a cluster based on the neighbors.
	 * 
	 * @param Record
	 * @return Cluster of nearest neighbors to Record
	 */
	protected Cluster rangeQuery(Record r) {
		Cluster c = new Cluster();
		c.addRecord(r);
		
		for (Record r2 : this.sourceData) {
			if(!r.equals(r2)) {
				double distance = MathUtils.distance(
						r2.getFeatureAndLabelArray(), 
						r.getFeatureAndLabelArray()
						);
				
				this.distance.addValue(distance);
				
				if (distance < this.epsilon ) {	
					r2.setNoise(false);
					
					c.addRecord(r2);
				}
			}
			
		}
		
		return c;
	}
	
	public double getMinDistance() {
		return this.distance.getMin();
	}
	public double getMeanDistance() {
		return this.distance.getMean();
	}
	public SynchronizedSummaryStatistics getDistanceStats() {
		return this.distance;
	}
	public RecordList getSourceData() {
		return this.sourceData;
	}
}
