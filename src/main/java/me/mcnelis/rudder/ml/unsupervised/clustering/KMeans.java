package me.mcnelis.rudder.ml.unsupervised.clustering;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.mcnelis.rudder.data.RecordInterface;
import me.mcnelis.rudder.data.collections.RecordList;

import org.apache.commons.math.stat.descriptive.SynchronizedSummaryStatistics;
import org.apache.commons.math.util.MathUtils;

public class KMeans
{

	protected int k;
	protected List<Cluster> clusters;
	protected double[][] previousCenters;
	protected long maxIterations;
	protected long currentIteration = 0l;
	protected double minMovement = .0000001d;
	protected RecordList<RecordInterface> sourceData;

	@SuppressWarnings("unchecked")
	public KMeans(int k, RecordList<? extends RecordInterface> data)
	{
		this.k = k;
		this.sourceData = (RecordList<RecordInterface>) data;
		this.init();
	}

	public KMeans(int k)
	{
		this.k = k;
		this.init();
	}

	public KMeans()
	{
		this.init();
	}

	private void init()
	{
		this.maxIterations = 1000000000000l;
		this.minMovement = .0000001d;
	}
	public List<Cluster> cluster()
	{

		// Get the random centers
		this.chooseRandomCenters();

		do
		{
			this.currentIteration++;
			this.assignClusters();

			// This could be parallelized
			for (Cluster c : this.clusters)
			{
				c.calculateCentroid();
			}

		}
		while (this.keepClustering());

		return this.clusters;
	}

	protected boolean keepClustering()
	{
		SynchronizedSummaryStatistics distanceStats = new SynchronizedSummaryStatistics();
		if (this.previousCenters == null)
		{
			this.previousCenters = new double[this.clusters.size()][];
			this.assignPreviousClusters();
			return true;
		}

		for (int i = 0; i < this.previousCenters.length; i++)
		{
			distanceStats.addValue(MathUtils.distance(this.previousCenters[i],
					this.clusters.get(i).getCentroid()));
		}
		this.assignPreviousClusters();
		
		return !(this.currentIteration > this.maxIterations
				|| this.minMovement <= distanceStats.getMean());


	}

	protected void assignPreviousClusters()
	{
		for (int i = 0; i < this.clusters.size(); i++)
		{
			this.previousCenters[i] = this.clusters.get(i).getCentroid();
		}
	}

	/**
	 * Assign records to the nearest cluster based on Euclidean distance
	 */
	protected void assignClusters()
	{

		for (Object o : this.sourceData)
		{
			RecordInterface r = (RecordInterface) o;
			double min = Double.NaN;
			int clusterIdx = -1;
			for (int i = 0; i < this.clusters.size(); i++)
			{
				double distance = MathUtils.distance(this.clusters.get(i)
						.getCentroid(), r.getFeatureAndLabelDoubleArray());
				if (Double.isNaN(min) || distance < min)
				{
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
	 * Other KMeans implementations have better initial centroid selection. This
	 * could be overloaded with other implementations.
	 * 
	 * @return clusters with a center set
	 */
	protected List<Cluster> chooseRandomCenters()
	{
		ArrayList<Cluster> randomCluster = new ArrayList<Cluster>();
		Random generator = new Random();

		for (int i = 0; i < this.k; i++)
		{

			Cluster c = new Cluster();

			c.setCentroid(((RecordInterface) this.sourceData.get(generator
					.nextInt(this.sourceData.size())))
					.getFeatureAndLabelDoubleArray());
			randomCluster.add(c);
		}
		this.clusters = randomCluster;
		return this.clusters;
	}

	public int getK()
	{
		return k;
	}

	public void setK(int k)
	{
		this.k = k;
	}

	public List<Cluster> getClusters()
	{
		return clusters;
	}

	public void setClusters(List<Cluster> clusters)
	{
		this.clusters = clusters;
	}

	public double getMinMovement()
	{
		return minMovement;
	}

	public void setMinMovement(double minMovement)
	{
		this.minMovement = minMovement;
	}

	public RecordList<RecordInterface> getSourceData()
	{
		return sourceData;
	}

	public void setSourceData(RecordList<RecordInterface> sourceData)
	{
		this.sourceData = sourceData;
	}

}
