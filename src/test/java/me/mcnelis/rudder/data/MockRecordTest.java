package me.mcnelis.rudder.data;

import static org.junit.Assert.*;

import me.mcnelis.rudder.exceptions.FeatureNotFoundException;

import org.junit.Test;

public class MockRecordTest {

	@Test
	public void testSetFeature() {
		MockRecord r = new  MockRecord();
		try {
			assertTrue(r.setFeature("feature1", 4d));
		} catch (FeatureNotFoundException e) {
			e.printStackTrace();
			fail("Feature wasn't found!");
		}
	}
	
	@Test
	public void testSetFeatureFailureNonFeature() {
		MockRecord r = new  MockRecord();
		try {
			r.setFeature("nonFeature", 4d);
		} catch (FeatureNotFoundException e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testSetFeatureNoField() {
		MockRecord r = new  MockRecord();
		try {
			r.setFeature("bogusFeature", 4d);
		} catch (FeatureNotFoundException e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testSetLabel() {
		MockRecord r = new  MockRecord();
		
			try {
				assertTrue(r.setLabel("doubleLabel", 4d));
			} catch (Exception e) {
				fail("Should not have exception");
			}
	
	}

	@Test
	public void testGetFeatureArray() {
		MockRecord r = new MockRecord();
		try {
			r.setFeature("feature1", 1d);
			r.setFeature("feature2", 2d);
			r.setFeature("feature3", 3d);
		} catch (FeatureNotFoundException e) {
			fail("Should have executed cleanly");
		}
		
		double[] arr = r.getFeatureDoubleArray();
		assertEquals(1d, arr[0], .0001);
		assertEquals(2d, arr[1], .0001);
		assertEquals(3d, arr[2], .0001);
		
		
	}
}
