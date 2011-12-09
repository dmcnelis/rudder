package me.mcnelis.rudder.records;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import me.mcnelis.rudder.exceptions.IllegalRecordException;
import me.mcnelis.rudder.features.BinaryFeature;
import me.mcnelis.rudder.features.FeatureList;
import me.mcnelis.rudder.features.NumericFeature;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.junit.Test;

public class RecordSetTest {

	@Test
	public void testSetPrototypeNoRecords() {
		RecordSet rs= new RecordSet();
		DoubleRecord dr = new DoubleRecord();
		dr.setKey(12d);
		FeatureList list = new FeatureList();
		list.add(new BinaryFeature("test", true));
		
		dr.setFeatures(list);
		
		try {
			rs.setPrototype(dr);
			assertTrue(true);
		} catch (IllegalRecordException e) {
			fail("Should not have exception here");
		}
	}

	@Test
	public void testAddAndCheckNoPrototype() {
		RecordSet rs= new RecordSet();
		DoubleRecord dr = new DoubleRecord();
		dr.setKey(12d);
		FeatureList list = new FeatureList();
		list.add(new BinaryFeature("test", true));
		
		dr.setFeatures(list);
		
		try {
			
			assertTrue(rs.addAndCheck(dr));
		} catch (IllegalRecordException e) {
			fail("Should not have exception here");
		}
	}
	
	@Test
	public void testAddAndCheckWithPrototypeSuccess() {
		RecordSet rs= new RecordSet();
		DoubleRecord dr = new DoubleRecord();
		dr.setKey(12d);
		FeatureList list = new FeatureList();
		list.add(new BinaryFeature("test", true));
		
		DoubleRecord dr2 = new DoubleRecord();
		dr2.setKey(11d);
		FeatureList list2 = new FeatureList();
		list2.add(new BinaryFeature("test", true));
		
		dr2.setFeatures(list2);
		
		try {
			rs.setPrototype(dr);
			assertTrue(rs.addAndCheck(dr2));
			assertEquals(1, rs.size());
		} catch (IllegalRecordException e) {
			fail("Should not have exception here");
		}
	}
	
	@Test
	public void testAddAndCheckWithPrototypeFailure() {
		RecordSet rs= new RecordSet();
		DoubleRecord dr = new DoubleRecord();
		dr.setKey(12d);
		FeatureList list = new FeatureList();
		list.add(new BinaryFeature("test", true));
		dr.setFeatures(list);
		
		DoubleRecord dr2 = new DoubleRecord();
		dr2.setKey(11d);
		FeatureList list2 = new FeatureList();
		list2.add(new BinaryFeature("test 2", true));
		
		dr2.setFeatures(list2);
		
		try {
			rs.setPrototype(dr);
			rs.addAndCheck(dr2);
			
		} catch (IllegalRecordException e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testAddAndCheckUniqueVerify() {
		RecordSet rs= new RecordSet();
		DoubleRecord dr = new DoubleRecord();
		dr.setKey(12d);
		FeatureList list = new FeatureList();
		list.add(new BinaryFeature("test", true));
		dr.setFeatures(list);
		
		DoubleRecord dr2 = new DoubleRecord();
		dr2.setKey(12d);
		FeatureList list2 = new FeatureList();
		list2.add(new BinaryFeature("test", true));
		
		dr2.setFeatures(list2);
		
		try {
			rs.setPrototype(dr);
			rs.addAndCheck(dr);
			rs.addAndCheck(dr2);
			assertEquals(1, rs.size());
		} catch (IllegalRecordException e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testAddAndCheckUniqueVerifyMulti() {
		RecordSet rs= new RecordSet();
		DoubleRecord dr = new DoubleRecord();
		dr.setKey(12d);
		FeatureList list = new FeatureList();
		list.add(new BinaryFeature("test", true));
		dr.setFeatures(list);
		
		DoubleRecord dr2 = new DoubleRecord();
		dr2.setKey(12d);
		FeatureList list2 = new FeatureList();
		list2.add(new BinaryFeature("test 2", true));
		
		dr2.setFeatures(list2);
		
		try {
			rs.setPrototype(dr);
			rs.addAndCheck(dr);
			rs.addAndCheck(dr2);
			assertEquals(2, rs.size());
		} catch (IllegalRecordException e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testGetDoubleArray() {
		
		RecordSet rs= new RecordSet();
		DoubleRecord dr = new DoubleRecord();
		dr.setKey(12d);
		FeatureList list = new FeatureList();
		list.add(new BinaryFeature("test", true));
		list.add(new NumericFeature("number", 12));
		dr.setFeatures(list);
		
		DoubleRecord dr2 = new DoubleRecord();
		dr2.setKey(11d);
		FeatureList list2 = new FeatureList();
		list2.add(new BinaryFeature("test", false));
		list2.add(new NumericFeature("number", 2));
		dr2.setFeatures(list2);
		
		try {
			rs.setPrototype(dr);	
		} catch (IllegalRecordException e) {
			fail("Should not have an exception");
		}
		
		try {
			rs.addAndCheck(dr);
		} catch (IllegalRecordException e) {
			fail("Should not have an exception");
		}
		
		
		try {
			rs.addAndCheck(dr2);
		} catch (IllegalRecordException e) {
			fail("Should not have an exception");
		}
		
		DoubleMatrix arr = rs.getDoubleMatrix();

		double[] ys = arr.getYs();
		assertEquals(12d, ys[0], .002);
		assertEquals(11d, ys[1], .002);
		
		double[][] xs = arr.getXs();
		
		assertEquals(0d, xs[1][1], .002);
		assertEquals(2d, xs[1][0], .002);
		assertEquals(1d, xs[0][1], .002);
		assertEquals(12d, xs[0][0], .002);
		
	}
	
	@Test
	public void getFeatureStatistics() {
		RecordSet rs= new RecordSet();
		DoubleRecord dr = new DoubleRecord();
		dr.setKey(12d);
		FeatureList list = new FeatureList();
		list.add(new BinaryFeature("test", true));
		list.add(new NumericFeature("number", 1));
		dr.setFeatures(list);
		
		DoubleRecord dr2 = new DoubleRecord();
		dr2.setKey(11d);
		FeatureList list2 = new FeatureList();
		list2.add(new BinaryFeature("test", false));
		list2.add(new NumericFeature("number", 5));
		dr2.setFeatures(list2);
		
		
		DoubleRecord dr3 = new DoubleRecord();
		dr3.setKey(12d);
		FeatureList list3 = new FeatureList();
		list3.add(new BinaryFeature("test", true));
		list3.add(new NumericFeature("number", 10));
		dr3.setFeatures(list3);
		
		DoubleRecord dr4 = new DoubleRecord();
		dr4.setKey(11d);
		FeatureList list4 = new FeatureList();
		list4.add(new BinaryFeature("test", false));
		list4.add(new NumericFeature("number", 20));
		dr4.setFeatures(list4);
		
		try {
			rs.addAndCheck(dr);
			rs.addAndCheck(dr2);
			rs.addAndCheck(dr3);
			rs.addAndCheck(dr4);
		} catch (IllegalRecordException e) {
			fail("Problem with the records!");
		}
		
		DescriptiveStatistics d = rs.getFeatureStatistics("number");
		assertEquals(1d, d.getMin(), .002);
		assertEquals(20d, d.getMax(), .002);
		assertEquals(4, d.getN());
		
	}
	
	@Test
	public void getYStatistics() {
		RecordSet rs= new RecordSet();
		DoubleRecord dr = new DoubleRecord();
		dr.setKey(12d);
		FeatureList list = new FeatureList();
		list.add(new BinaryFeature("test", true));
		list.add(new NumericFeature("number", 1));
		dr.setFeatures(list);
		
		DoubleRecord dr2 = new DoubleRecord();
		dr2.setKey(11d);
		FeatureList list2 = new FeatureList();
		list2.add(new BinaryFeature("test", false));
		list2.add(new NumericFeature("number", 5));
		dr2.setFeatures(list2);
		
		
		DoubleRecord dr3 = new DoubleRecord();
		dr3.setKey(12d);
		FeatureList list3 = new FeatureList();
		list3.add(new BinaryFeature("test", true));
		list3.add(new NumericFeature("number", 10));
		dr3.setFeatures(list3);
		
		DoubleRecord dr4 = new DoubleRecord();
		dr4.setKey(11d);
		FeatureList list4 = new FeatureList();
		list4.add(new BinaryFeature("test", false));
		list4.add(new NumericFeature("number", 20));
		dr4.setFeatures(list4);
		
		try {
			rs.addAndCheck(dr);
			rs.addAndCheck(dr2);
			rs.addAndCheck(dr3);
			rs.addAndCheck(dr4);
		} catch (IllegalRecordException e) {
			fail("Problem with the records!");
		}
		
		DescriptiveStatistics d = rs.getYStatistics();
		assertEquals(11d, d.getMin(), .002);
		assertEquals(12d, d.getMax(), .002);
		assertEquals(11.5, d.getMean(), .002);
		assertEquals(4, d.getN());
		
	}

}
