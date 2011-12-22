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
				
				@NumericFeature double feature1;
				@NumericFeature double feature2;
				@NumericFeature double feature3;
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

	@Test
	public void testGetLabelOneDoubleField() {
	
		Record r = new Record() {
			
			@NumericFeature double feature1;
			@NumericFeature double feature2;
			@Label double label;
		};
		try {
			r.setFeature("feature1", Math.pow(2,2)*3.2d);
			r.setFeature("feature2", Math.pow(2,3)/2.3d);
			r.setLabel("label", 12d);
		} catch (FeatureNotFoundException e) {
			fail("Should not have  exception");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals("12.0", r.getLabel());
	}
	
	@Test
	public void testGetLabelOneStringField() {
	
		Record r = new Record() {
			
			@NumericFeature double feature1;
			@NumericFeature double feature2;
			@Label String label;
		};
		try {
			r.setFeature("feature1", Math.pow(2,2)*3.2d);
			r.setFeature("feature2", Math.pow(2,3)/2.3d);
			r.setLabel("label", "success");
		} catch (FeatureNotFoundException e) {
			fail("Should not have  exception");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals("success", r.getLabel());
	}
	
	@Test
	public void testGetLabelTwoFields() {
	
		Record r = new Record() {
			
			@NumericFeature double feature1;
			@NumericFeature double feature2;
			@Label String label;
			@Label double label2;
			
		};
		try {
			r.setFeature("feature1", Math.pow(2,2)*3.2d);
			r.setFeature("feature2", Math.pow(2,3)/2.3d);
			r.setLabel("label", "success");
			r.setLabel("label2", 12d);
		} catch (FeatureNotFoundException e) {
			fail("Should not have  exception");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals("success12.0", r.getLabel());
	}
	
	@Test
	public void testSetStringFeature() {
	
		Record r = new Record() {
			
			@TextFeature String feature1;
			@NumericFeature double feature2;
			@Label String label;
			@Label double label2;
			
		};
		try {
			r.setFeature("feature1", "test");
			r.setFeature("feature2", Math.pow(2,3)/2.3d);
			r.setLabel("label", "success");
			r.setLabel("label2", 12d);
		} catch (FeatureNotFoundException e) {
			e.printStackTrace();
			fail("Should not have  exception");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals("test",(String)r.getAllFeatures()[0]);

	}
	
	@Test
	public void testUndefinedFeatureDropout() {
		Record r = new Record() {
			@NumericFeature double f1;
			@NumericFeature double f2 = Double.NaN;
			
		};
		try {
			r.setFeature("f1", 1d);
		} catch (FeatureNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(1, r.getFeatureDoubleArray().length);
	}
}
