package me.mcnelis.rudder.ml.unsupervised.clustering;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import me.mcnelis.rudder.data.collections.IRudderList;
import me.mcnelis.rudder.data.collections.RudderList;

import org.apache.commons.math.stat.descriptive.SynchronizedSummaryStatistics;
import org.apache.commons.math.util.MathUtils;
import org.apache.log4j.Logger;

public class KMeans<T>
{

	private static final Logger LOG = Logger.getLogger(KMeans.class);
	
	protected int k;
	protected List<Cluster<T>> clusters;
	protected double[][] previousCenters;
	protected long maxIterations;
	protected long currentIteration = 0l;
	protected double minMovement = .0000001d;
	protected IRudderList<T> sourceData;

	public KMeans(int k, IRudderList<T> data)
	{
		this.k = k;
		this.sourceData = data;
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
	public List<Cluster<T>> cluster()
	{

		// Get the random centers
		this.chooseRandomCenters();

		do
		{
			this.currentIteration++;
			this.assignClusters();

			// This could be parallelized
			for (Cluster<?> c : this.clusters)
			{
				LOG.debug("Cluster size: " + c.size());
				if(c.size() > 0)
				{
					c.calculateCentroid();
				}
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

		LOG.debug("Source data size: " + this.sourceData.size());
		List<Cluster<T>> tempClusters = new ArrayList<Cluster<T>>();
		
		for(int i = 0; i < this.clusters.size(); i++)
		{
			tempClusters.add(new Cluster<T>());
		}
		for (T o : this.sourceData)
		{
			
			double min = Double.NaN;
			int clusterIdx = -1;
			for (int i = 0; i < this.clusters.size(); i++)
			{
				double distance = MathUtils.distance(this.clusters.get(i)
						.getCentroid(), this.sourceData.getUnsupervisedDoubleArray(o));
				LOG.trace("Record: " + Arrays.toString(this.sourceData.getUnsupervisedDoubleArray(o)));
				if (Double.isNaN(min) || distance < min)
				{
					clusterIdx = i;
					min = distance;
				}
			}

			tempClusters.get(clusterIdx).addRecord(o);
		}
		this.clusters = tempClusters;
	}

	/**
	 * Choose random K elements as initial center points.
	 * 
	 * Other KMeans implementations have better initial centroid selection. This
	 * could be overloaded with other implementations.
	 * 
	 * @return clusters with a center set
	 */
	protected List<Cluster<T>> chooseRandomCenters()
	{
		ArrayList<Cluster<T>> randomCluster = new ArrayList<Cluster<T>>();
		Random generator = new Random();

		for (int i = 0; i < this.k; i++)
		{

			Cluster<T> c = new Cluster<T>();

			c.setCentroid(
				this.sourceData.getUnsupervisedDoubleArray(
						this.sourceData.get(
								generator.nextInt(
										this.sourceData.size()
										)
								)
						)
				);
			
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

	public List<Cluster<T>> getClusters()
	{
		return clusters;
	}

	public void setClusters(List<Cluster<T>> clusters)
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

	public IRudderList<T> getSourceData()
	{
		return sourceData;
	}

	public void setSourceData(IRudderList<T> sourceData)
	{
		this.sourceData = sourceData;
	}

}
