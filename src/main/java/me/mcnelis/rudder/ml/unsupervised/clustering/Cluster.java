package me.mcnelis.rudder.ml.unsupervised.clustering;

import java.util.ArrayList;
import java.util.Arrays;

import me.mcnelis.rudder.data.collections.RudderList;

import org.apache.commons.math.stat.descriptive.SynchronizedSummaryStatistics;

/**
 * Specialized RecordList for housing and performing operations within a cluster
 * (i.e. holding a centroid
 * 
 * @author dmcnelis
 * 
 */
public class Cluster<T> extends RudderList<T>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected double[] centroid;

	public Cluster()
	{
	}

	public void addRecord(T r)
	{		
		this.add(r);

	}

	@SuppressWarnings("unchecked")
	public void combineClusters(Cluster<?> cluster)
	{
		for (Object o : cluster.getRecords())
		{
			
			if (!this.contains(o))
			{
				this.add((T) o);
			}
		}
	}

	public Cluster<T> getRecords()
	{
		return this;
	}

	public double[] getCentroid()
	{
		if (this.centroid != null)
		{
			return this.centroid.clone();
		}
	
		return this.calculateCentroid();
	}

	public void setCentroid(double[] d)
	{

		this.centroid = d.clone();

	}

	/**
	 * Cycles through elements in the list to average each element against the
	 * other records to create a centroid used in some forms of clustering
	 * algorithms.
	 * 
	 * @return double array representing centroid
	 */
	protected synchronized double[] calculateCentroid()
	{

		this.centroid = new double[this.getUnsupervisedDoubleArray(this.get(0)).length];

		ArrayList<SynchronizedSummaryStatistics> stats = new ArrayList<SynchronizedSummaryStatistics>();
		for (Object elem : this)
		{
			double[] arr = this.getUnsupervisedDoubleArray(elem);
			for (int i = 0; i < arr.length; i++)
			{

				SynchronizedSummaryStatistics stat;
				try
				{
					stat = stats.get(i);
				}
				catch (IndexOutOfBoundsException aiobe)
				{

					stat = new SynchronizedSummaryStatistics();
					stats.add(i, stat);
				}
				stat.addValue(arr[i]);
			}
		}

		for (int i = 0; i < this.centroid.length; i++)
		{
			this.centroid[i] = stats.get(i).getMean();
		}

		return this.centroid;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(centroid);
		result = prime * result;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (!(obj instanceof Cluster<?>))
		{
			return false;
		}
		@SuppressWarnings("unchecked")
		Cluster<T> other = (Cluster<T>) obj;
		if (!Arrays.equals(centroid, other.centroid))
		{
			return false;
		}

		return true;
	}

	public boolean isAssigned(Object o)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
