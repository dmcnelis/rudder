package me.mcnelis.rudder.ml.unsupervised.clustering;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class DBScan extends DensityBased
{
	private static final Logger LOG = Logger.getLogger(DBScan.class);
	
	protected DBScan(double epsilon, int minPts)
	{
		super(epsilon, minPts);
	}

	@Override
	protected List<Cluster<?>> cluster()
	{
		if (this.clusters == null)
		{
			this.clusters = new ArrayList<Cluster<?>>();
		}
		for (Object r : this.sourceData)
		{
			if (!this.sourceData.isVisited(r))
			{
				this.sourceData.setVisited(r,true);
				Cluster<?> c = this.rangeQuery(r);

				if (c.getRecords().size() < this.minPts)
				{

					this.sourceData.setNoise(r,true);

				}
				else
				{

					Cluster<?> addCluster = this.expandCluster(r, c);
					this.clusters.add(addCluster);

				}
			}
		}
		return this.clusters;
	}

	protected Cluster<?> expandCluster(Object r, Cluster<?> c)
	{
		LOG.trace("Expanding cluster");
		Cluster<Object> newCluster = new Cluster<Object>();

		newCluster.addRecord(r);

		for (Object o : c.getRecords())
		{
			
			if (!c.isVisited(o))
			{
				c.setVisited(o,true);
				Cluster<?> cluster = this.rangeQuery(o);
				if (cluster.getRecords().size() >= this.minPts)
				{

					cluster.combineClusters(this.expandCluster(o, cluster));

				}
			}

			if (!c.isAssigned(o))
			{
				newCluster.addRecord(o);
			}
		}

		return newCluster;
	}

}
