package me.mcnelis.rudder.ml.unsupervised.clustering;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math.util.MathUtils;

import me.mcnelis.rudder.data.Record;
import me.mcnelis.rudder.data.collections.RecordList;

public class KMeans implements Serializable{
	
	private static final long serialVersionUID = 1L;
	protected int k;
	protected ArrayList<Cluster> clusters;
	protected double[][] previousCenters;
	protected long maxIterations = 1000000000000l;
	protected long currentIteration = 0l;
	protected double minMovement = .0000001d;
	protected RecordList sourceData;
	
	public KMeans(int k, RecordList data) {
		this.k = k;
		this.sourceData = data;
	}
	
	public KMeans(int k) {
		this.k = k;
	}
	
	public KMeans() {}
	
	public ArrayList<Cluster> cluster() {
		
		//Get the random centers
		this.chooseRandomCenters();
		
		do {
			this.currentIteration++;
			this.assignClusters();
			
			//This could be parallelized 
			for (Cluster c : this.clusters) {
				c.calculateCentroid();
			}
			
		} while(this.keepClustering());
		
		return this.clusters;
	}
	
	protected boolean keepClustering() {
		DescriptiveStatistics distanceStats = new DescriptiveStatistics();
		if (this.previousCenters == null) {
			this.previousCenters = new double[this.clusters.size()][];
			this.assignPreviousClusters();
			return true;
		}
			
		for (int i=0; i<this.previousCenters.length; i++) {
			distanceStats.addValue(MathUtils.distance(this.previousCenters[i], this.clusters.get(i).getCentroid()));
		}
		this.assignPreviousClusters();
		if(this.currentIteration>this.maxIterations || this.minMovement <= distanceStats.getMean())
			return false;
		else
			return true;
		
	}
	
	protected void assignPreviousClusters() {
		for(int i=0; i<this.clusters.size(); i++) {
			this.previousCenters[i] = this.clusters.get(i).getCentroid();
		}
	}
	
	/**
	 * Assign records to  the nearest cluster
	 * based on Euclidean distance
	 */
	protected void assignClusters() {
		
		for (Record r : this.sourceData) {
			double min = Double.NaN;
			int clusterIdx = -1;
			for(int i=0; i<this.clusters.size(); i++) {
				double distance = 
						MathUtils.distance(
								this.clusters.get(i).getCentroid(), 
								r.getFeatureAndLabelArray()
								);
				if(Double.isNaN(min) || distance < min) {
					clusterIdx = i;
					min = distance;
				}
			}
			
			this.clusters.get(clusterIdx).addRecord(r);
		}
		
	}
	
	/**
	 * Choose random K elements as initial center points.
	 * 
	 * Other KMeans implementations have better initial centroid
	 * selection.  This could be  overloaded with other 
	 * implementations.
	 * 
	 * @return clusters with a center set
	 */
	protected ArrayList<Cluster> chooseRandomCenters() {
		ArrayList<Cluster> clusters = new ArrayList<Cluster>();
		Random generator = new Random();
		
		
		
		for (int i=0; i<this.k; i++) {
			
			
			Cluster c = new Cluster();
			
			c.setCentroid(this.sourceData.get(generator.nextInt(this.sourceData.size())).getFeatureAndLabelArray());
			clusters.add(c);
		}
		this.clusters = clusters;
		return this.clusters;
	}

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}

	public ArrayList<Cluster> getClusters() {
		return clusters;
	}

	public void setClusters(ArrayList<Cluster> clusters) {
		this.clusters = clusters;
	}

	public double getMinMovement() {
		return minMovement;
	}

	public void setMinMovement(double minMovement) {
		this.minMovement = minMovement;
	}

	public RecordList getSourceData() {
		return sourceData;
	}

	public void setSourceData(RecordList sourceData) {
		this.sourceData = sourceData;
	}
	
	
}
