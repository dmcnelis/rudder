package me.mcnelis.rudder.ml.unsupervised.clustering;

import static org.junit.Assert.*;

import java.util.ArrayList;

import me.mcnelis.rudder.data.MockRecord;
import me.mcnelis.rudder.data.Record;
import me.mcnelis.rudder.data.collections.RecordList;
import me.mcnelis.rudder.exceptions.FeatureNotFoundException;

import org.junit.Test;

public class DBScanTest {

	@Test
	public void testGetClusters() {
		RecordList list = new RecordList();
		for(int j=0; j<50; j=j+10) {
			for (int i=0; i<10; i++) {
				Record r = new MockRecord();
				try {
					r.setFeature("feature1",1*j + i*.0000001);
					r.setFeature("feature2", 1*j+ i*.0000001);
					r.setFeature("feature3", 1*j+ i*.0000001);
					r.setLabel("stringLabel", "label");
				} catch (FeatureNotFoundException e) {
					e.printStackTrace();
					fail("Should not have  exception");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				list.add(r);
			}
		}
		
		DBScan db = new DBScan(5d, 2);
		db.setSourceData(list);
		ArrayList<Cluster> clusters = db.getClusters();
		assertEquals(5, clusters.size());

	}

}
