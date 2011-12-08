package me.mcnelis.ml.rudder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import me.mcnelis.rudder.features.FeatureList;
import me.mcnelis.rudder.features.NumericFeature;

import org.junit.Test;

public class DoubleRecordTest {

	@Test
	public void testGetKey() {
		DoubleRecord r = new DoubleRecord();
		r.setKey(12d);
		assertTrue(r.getKey() instanceof Double);
		assertEquals(12, r.getKey(), .002);
	}

	@Test
	public void testCompareToDoubleRecordLessThan() {
		DoubleRecord r = new DoubleRecord();
		r.setKey(12d);
		
		DoubleRecord r2 = new DoubleRecord();
		r2.setKey(22d);
		
		assertEquals(-1, r.compareTo(r2));
	}
	
	@Test
	public void testCompareToDoubleRecordGreaterThan() {
		DoubleRecord r = new DoubleRecord();
		r.setKey(12d);
		
		DoubleRecord r2 = new DoubleRecord();
		r2.setKey(2d);
		
		assertEquals(1, r.compareTo(r2));
	}

	@Test
	public void testCompareToDoubleRecordEqual() {
		DoubleRecord r = new DoubleRecord();
		r.setKey(12d);
		
		DoubleRecord r2 = new DoubleRecord();
		r2.setKey(12d);
		
		assertEquals(0, r.compareTo(r2));
	}

	@Test
	public void testEqualsNoFeatures() {
		DoubleRecord r = new DoubleRecord();
		r.setKey(12d);
		
		DoubleRecord r2 = new DoubleRecord();
		r2.setKey(12d);
		
		assertTrue(r.equals(r2));
	}
	
	@Test
	public void testEqualsSameFeatures() {
		DoubleRecord r = new DoubleRecord();
		r.setKey(12d);
		FeatureList fl = new FeatureList();
		fl.add(new NumericFeature("one", 1));
		r.setFeatures(fl);
		
		DoubleRecord r2 = new DoubleRecord();
		r2.setKey(12d);
		FeatureList fl2 = new FeatureList();
		fl2.add(new NumericFeature("one", 1));
		r2.setFeatures(fl2);
		assertTrue(r.equals(r2));
	}
	
	@Test
	public void testNotEqualsSameKeyDiffFeatures() {
		DoubleRecord r = new DoubleRecord();
		r.setKey(15d);
		FeatureList fl = new FeatureList();
		fl.add(new NumericFeature("one", 1));
		r.setFeatures(fl);
		
		DoubleRecord r2 = new DoubleRecord();
		r2.setKey(15d);
		FeatureList fl2 = new FeatureList();
		fl2.add(new NumericFeature("two", 2));
		r2.setFeatures(fl2);
		
		assertFalse(r2.equals(r));
	}

}
