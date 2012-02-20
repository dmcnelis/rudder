package me.mcnelis.rudder.ml.unsupervised.clustering;

import java.util.ArrayList;
import java.util.List;

import me.mcnelis.rudder.data.RecordInterface;

public class DBScan extends DensityBased
{

	protected DBScan(double epsilon, int minPts)
	{
		super(epsilon, minPts);
	}

	@Override
	protected List<Cluster> cluster()
	{
		if (this.clusters == null)
		{
			this.clusters = new ArrayList<Cluster>();
		}
		for (RecordInterface r : this.sourceData)
		{
			if (!r.isVisited())
			{
				r.setVisited(true);
				Cluster c = this.rangeQuery(r);

				if (c.getRecords().size() < this.minPts)
				{

					r.setNoise(true);

				}
				else
				{

					Cluster addCluster = this.expandCluster(r, c);
					this.clusters.add(addCluster);

				}
			}
		}
		return this.clusters;
	}

	protected Cluster expandCluster(RecordInterface r, Cluster c)
	{

		Cluster newCluster = new Cluster();

		newCluster.addRecord(r);

		for (Object o : c.getRecords())
		{
			RecordInterface rPrime = (RecordInterface) o;
			if (!rPrime.isVisited())
			{
				rPrime.setVisited(true);
				Cluster cluster = this.rangeQuery(rPrime);
				if (cluster.getRecords().size() >= this.minPts)
				{

					cluster.combineClusters(this.expandCluster(rPrime, cluster));

				}
			}

			if (!rPrime.isAssigned())
			{
				newCluster.addRecord(rPrime);
			}
		}

		return newCluster;
	}

}
