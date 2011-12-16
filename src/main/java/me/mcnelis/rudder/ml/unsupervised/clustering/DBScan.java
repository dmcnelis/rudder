package me.mcnelis.rudder.ml.unsupervised.clustering;

import java.util.ArrayList;

import me.mcnelis.rudder.data.Record;

public class DBScan extends DensityBased {

	protected DBScan(double epsilon, int minPts) {
		super(epsilon, minPts);
	}

	@Override
	protected ArrayList<Cluster> cluster() {

		for (Record r : this.sourceData) {
			if (!r.isVisited()) {
				r.setVisited();
				Cluster c = this.rangeQuery(r);
				
				if (c.getRecords().size() < this.minPts) {
					
					r.setIsNoise();
					
				} else {
					
					Cluster addCluster = this.expandCluster(r, c);
					this.clusters.add(addCluster);
					
				}
			}
		}
		return this.clusters;
	}
	
	protected Cluster expandCluster(Record r, Cluster c) {
		
		Cluster newCluster = new Cluster();
		
		newCluster.addRecord(r);
		
		for (Record rPrime : c.getRecords()) {
			if (!rPrime.isVisited()) {
				rPrime.setVisited();
				Cluster cluster = this.rangeQuery(rPrime);
				if (cluster.getRecords().size() >= this.minPts) {
					
					cluster.combineClusters(this.expandCluster(rPrime, cluster));
					
				}
			}
			
			if (!rPrime.isAssigned())
				newCluster.addRecord(rPrime);
		}
		
		return newCluster;
	}

}
