package me.mcnelis.rudder.data;

import static org.junit.Assert.*;

import me.mcnelis.rudder.data.collections.RecordList;
import me.mcnelis.rudder.exceptions.FeatureNotFoundException;

import org.junit.Test;

public class RecordTest {

	@SuppressWarnings("unused")
	@Test
	public void testSetFeature() {
		RecordList<Record> list = new RecordList<Record>();
		for (int i=0; i<10000; i++) {
			Record r = new Record() {
				
				@Feature double feature1;
				@Feature double feature2;
				@Feature double feature3;
			};
			try {
				r.setFeature("feature1", Math.pow(i,2)*3.2d);
				r.setFeature("feature2", Math.pow(i,3)/2.3d);
				r.setFeature("feature3", 2 * Math.sqrt(i+1));
			} catch (FeatureNotFoundException e) {
				fail("Should not have  exception");
			}
			
			list.add(r);
		}
		
		assertTrue(true);
	}

}
