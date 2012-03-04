package me.mcnelis.rudder.ml.unsupervised.clustering;

import java.util.List;

import me.mcnelis.rudder.data.collections.IRudderList;

import org.apache.commons.math.stat.descriptive.SynchronizedSummaryStatistics;
import org.apache.commons.math.util.MathUtils;

public abstract class DensityBased
{

	protected double epsilon;
	protected int minPts;
	protected int minClusters;
	protected SynchronizedSummaryStatistics distance = new SynchronizedSummaryStatistics();
	protected List<Cluster<?>> clusters;
	protected IRudderList<?> sourceData;

	public void setSourceData(IRudderList<?> rl)
	{
		this.sourceData = (IRudderList<?>) rl;
	}

	/**
	 * 
	 * @param epsilon
	 *            -- the max distance between elements in the cluster
	 * @param minPts
	 */
	protected DensityBased(double epsilon, int minPts)
	{
		this.epsilon = epsilon;
		this.minPts = minPts;
	}

	public List<Cluster<?>> getClusters()
	{
		if (this.clusters == null)
		{
			this.clusters = this.cluster();
		}
		return this.clusters;
	}

	protected abstract List<Cluster<?>> cluster();

	/**
	 * Find the neighbors of r within range (this.epsilon)
	 * 
	 * Create a cluster based on the neighbors.
	 * 
	 * @param Record
	 * @return Cluster of nearest neighbors to Record
	 */
	protected Cluster<?> rangeQuery(Object r)
	{
		Cluster<Object> c = new Cluster<Object>();
		c.addRecord(r);
	
		for (Object r2 : this.sourceData)
		{
			if (!r.equals(r2))
			{
				double mDistance = MathUtils.distance(
						this.sourceData.getUnsupervisedDoubleArray(r2),
						this.sourceData.getUnsupervisedDoubleArray(r));

				this.distance.addValue(mDistance);

				if (mDistance < this.epsilon)
				{
					this.sourceData.setNoise(r2, false);

					c.addRecord(r2);
				}
			}

		}

		return c;
	}

	public double getMinDistance()
	{
		return this.distance.getMin();
	}

	public double getMeanDistance()
	{
		return this.distance.getMean();
	}

	public SynchronizedSummaryStatistics getDistanceStats()
	{
		return this.distance;
	}

	public IRudderList<?> getSourceData()
	{
		return this.sourceData;
	}
}
