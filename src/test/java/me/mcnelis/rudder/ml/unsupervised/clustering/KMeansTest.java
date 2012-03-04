package me.mcnelis.rudder.ml.unsupervised.clustering;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import me.mcnelis.rudder.data.MockRecord;
import me.mcnelis.rudder.data.collections.IRudderList;
import me.mcnelis.rudder.data.collections.RudderList;
import me.mcnelis.rudder.exceptions.FeatureNotFoundException;

import org.junit.Test;

public class KMeansTest {

	@Test
	public void testClustering() {
		IRudderList<MockRecord> list = new RudderList<MockRecord>();
		for (int i=0; i<10000; i++) {
			MockRecord r = new MockRecord();
			
			r.setFeature("feature1", Math.pow(i,2)*3.2d);
			r.setFeature("feature2", Math.pow(i,3)/2.3d);
			r.setFeature("feature3", 2 * Math.sqrt(i+1));
			
			
			list.add(r);
		}
		
		KMeans<MockRecord> k = new KMeans<MockRecord>(3, list);
		k.cluster();
		assertTrue(true);
	}

}
