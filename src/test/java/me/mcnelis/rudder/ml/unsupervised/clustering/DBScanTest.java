package me.mcnelis.rudder.ml.unsupervised.clustering;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import me.mcnelis.rudder.data.MockRecord;
import me.mcnelis.rudder.data.collections.IRudderList;
import me.mcnelis.rudder.data.collections.RudderList;
import me.mcnelis.rudder.exceptions.FeatureNotFoundException;

import org.junit.Test;

public class DBScanTest
{

	@Test
	public void testGetClusters()
	{
		IRudderList<MockRecord> list = new RudderList<MockRecord>();
		for (int j = 0; j < 50; j = j + 10)
		{
			for (int i = 0; i < 10; i++)
			{
				MockRecord r = new MockRecord();
				try
				{
					r.setFeature("feature1", 1 * j + i * .0000001);
					r.setFeature("feature2", 1 * j + i * .0000001);
					r.setFeature("feature3", 1 * j + i * .0000001);
					r.setLabel("label");
				}
				
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				list.add(r);
			}
		}

		DBScan db = new DBScan(5d, 2);
		db.setSourceData(list);
		List<Cluster<?>> clusters = db.getClusters();
		assertEquals(5, clusters.size());

	}

}
