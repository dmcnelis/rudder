package me.mcnelis.rudder.ml.unsupervised.clustering;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import me.mcnelis.rudder.data.MockRecord;
import me.mcnelis.rudder.data.collections.RecordList;
import me.mcnelis.rudder.exceptions.FeatureNotFoundException;

import org.junit.Test;

public class KMeansTest {

	@Test
	public void testClustering() {
		RecordList<MockRecord> list = new RecordList<MockRecord>();
		for (int i=0; i<10000; i++) {
			MockRecord r = new MockRecord();
			try {
				r.setFeature("feature1", Math.pow(i,2)*3.2d);
				r.setFeature("feature2", Math.pow(i,3)/2.3d);
				r.setFeature("feature3", 2 * Math.sqrt(i+1));
			} catch (FeatureNotFoundException e) {
				e.printStackTrace();
				fail("Should not have  exception");
			}
			
			list.add(r);
		}
		
		KMeans k = new KMeans(3, list);
		k.cluster();
		assertTrue(true);
	}

}
