package me.mcnelis.ml.rudder.util;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.HashMap;

import me.mcnelis.ml.rudder.exceptions.ConvertTimeToFeaturesDataTypeException;

import org.apache.commons.math.util.ResizableDoubleArray;
import org.junit.Test;



public class ConvertTimeToFeatureListTest {

	@Test
	public void testAddFeatureSuccess() {
		ConvertTimeToFeatures c = new ConvertTimeToFeatures();
		assertTrue(c.addFeature(TimeFeatures.DAY_OF_WEEK));
	}
	
	@Test
	public void testAddFeatureFailure() {
		ConvertTimeToFeatures c = new ConvertTimeToFeatures();
		assertTrue(c.addFeature(TimeFeatures.DAY_OF_WEEK));
		assertFalse(c.addFeature(TimeFeatures.DAY_OF_WEEK));
	}
	
	@Test
	public void testHour0FeatureList() {
		Calendar c = Calendar.getInstance();
		c.set(2011, 12, 6, 0, 2);
		
		ConvertTimeToFeatures convert = new ConvertTimeToFeatures(c);
		convert.addFeature(TimeFeatures.HOUR);
		
		assertEquals(1d, convert.getHourList().get("0"), .2);
		
	}
	
	@Test
	public void testHour6FeatureList() {
		Calendar c = Calendar.getInstance();
		c.set(2011, 12, 6, 6, 2);
		
		ConvertTimeToFeatures convert = new ConvertTimeToFeatures(c);
		convert.addFeature(TimeFeatures.HOUR);
		
		assertEquals(1d, convert.getHourList().get("6"), .2);
		
	}
	
	@Test
	public void testHour16FeatureList() {
		Calendar c = Calendar.getInstance();
		c.set(2011, 12, 6, 16, 2);
		
		ConvertTimeToFeatures convert = new ConvertTimeToFeatures(c);
		convert.addFeature(TimeFeatures.HOUR);
		
		assertEquals(1d, convert.getHourList().get("16"), .2);
		
	}
	
	@Test
	public void testHour23FeatureList() {
		Calendar c = Calendar.getInstance();
		c.set(2011, 12, 6, 23, 2);
		
		ConvertTimeToFeatures convert = new ConvertTimeToFeatures(c);
		convert.addFeature(TimeFeatures.HOUR);
		HashMap<String, Double> results = convert.getHourList();
		for(String key : results.keySet()) {
			assertEquals(0d, results.get(key), .2);
		}
		
	}
	
	@Test
	public void testMinute12FeatureList() {
		Calendar c = Calendar.getInstance();
		c.set(2011, 12, 6, 23, 12);
		
		ConvertTimeToFeatures convert = new ConvertTimeToFeatures(c);
		convert.addFeature(TimeFeatures.HOUR);
		HashMap<String, Double> results = convert.getMinuteList();
		
		assertEquals(1d, results.get("12"), .2);
		
		
	}
	
	@Test
	public void testMinute59FeatureList() {
		Calendar c = Calendar.getInstance();
		c.set(2011, 12, 6, 23, 59);
		
		ConvertTimeToFeatures convert = new ConvertTimeToFeatures(c);
		convert.addFeature(TimeFeatures.HOUR);
		HashMap<String, Double> results = convert.getMinuteList();
		for(String key : results.keySet()) {
			assertEquals(0d, results.get(key), .2);
		}
		
	}
	
	@Test
	public void testDOW0FeatureList() {
		Calendar c = Calendar.getInstance();
		
		c.set(2011, 11,4, 12, 2);
		
		ConvertTimeToFeatures convert = new ConvertTimeToFeatures(c);
		convert.addFeature(TimeFeatures.HOUR);
		HashMap<String, Double> results = convert.getDayOfWeekList();
		
		assertEquals(0d, results.get("3"), .2);
		
	}
	
	@Test
	public void testDOW7FeatureList() {
		Calendar c = Calendar.getInstance();
		
		c.set(2011, 11,3, 12, 2);
		
		ConvertTimeToFeatures convert = new ConvertTimeToFeatures(c);
		convert.addFeature(TimeFeatures.HOUR);
		HashMap<String, Double> results = convert.getDayOfWeekList();
		
		for(String key : results.keySet()) {
			assertEquals(0d, results.get(key), .2);
		}
		
	}
	
	@Test
	public void testMonth1FeatureList() {
		Calendar c = Calendar.getInstance();
		
		c.set(2011, 0,3, 12, 2);
		
		ConvertTimeToFeatures convert = new ConvertTimeToFeatures(c);
		convert.addFeature(TimeFeatures.HOUR);
		HashMap<String, Double> results = convert.getMonthList();
	
		assertEquals(1d, results.get("0"), .2);
		
	}
	
	@Test
	public void testMonth12FeatureList() {
		Calendar c = Calendar.getInstance();
		
		c.set(2011, 11,3, 12, 2);
		
		ConvertTimeToFeatures convert = new ConvertTimeToFeatures(c);
		convert.addFeature(TimeFeatures.HOUR);
		HashMap<String, Double> results = convert.getMonthList();
		
		for(String key : results.keySet()) {
			assertEquals(0d, results.get(key), .2);
		}
		
	}
	
	@Test
	public void testSecondToLastDayOfLeapYearFeatureList() {
		Calendar c = Calendar.getInstance();
		
		c.set(2004, 11,30, 12, 2);
		
		ConvertTimeToFeatures convert = new ConvertTimeToFeatures(c);
		convert.addFeature(TimeFeatures.HOUR);
		HashMap<String, Double> results = convert.getDayOYearList();
		for(String key : results.keySet()) {
			if(results.get(key) > .5)
				System.out.println(key);
			assertEquals(0d, results.get(key), .2);
		}
		assertEquals(1d, results.get("363"), .2);
		
		
	}
	
	@Test
	public void testLastDayOfLeapYearFeatureList() {
		Calendar c = Calendar.getInstance();
		
		c.set(2004, 11,31, 12, 2);
		
		ConvertTimeToFeatures convert = new ConvertTimeToFeatures(c);
		convert.addFeature(TimeFeatures.HOUR);
		HashMap<String, Double> results = convert.getDayOYearList();

		for(String key : results.keySet()) {
			
			assertEquals(0d, results.get(key), .2);
		}
		
	}
	
	@Test
	public void testSecondToLastDayOfNonLeapYearFeatureList() {
		Calendar c = Calendar.getInstance();
		
		c.set(2001, 11,30, 12, 2);
		
		ConvertTimeToFeatures convert = new ConvertTimeToFeatures(c);
		convert.addFeature(TimeFeatures.HOUR);
		HashMap<String, Double> results = convert.getDayOYearList();
		
		assertEquals(1d, results.get("364"), .2);
		
		
		
	}
	
	@Test
	public void testLastDayOfNonLeapYearFeatureList() {
		Calendar c = Calendar.getInstance();
		
		c.set(2001, 11,31, 12, 2);
		
		ConvertTimeToFeatures convert = new ConvertTimeToFeatures(c);
		convert.addFeature(TimeFeatures.HOUR);
		HashMap<String, Double> results = convert.getDayOYearList();

		for(String key : results.keySet()) {
			assertEquals(0d, results.get(key), .2);
		}
		
	}
	
	@Test
	public void testMinuteCardinality() {
		Calendar c = Calendar.getInstance();
		
		c.set(2001, 11,31, 12, 2);
		ConvertTimeToFeatures convert = new ConvertTimeToFeatures(c);
		assertEquals(59, convert.getCardinalities().get("minutes").intValue());
	}
	
	@Test
	public void testHourCardinality() {
		Calendar c = Calendar.getInstance();
		
		c.set(2001, 11,31, 12, 2);
		ConvertTimeToFeatures convert = new ConvertTimeToFeatures(c);
		assertEquals(23, convert.getCardinalities().get("hours").intValue());
	}
	
	@Test
	public void testDayOfWeekCardinality() {
		Calendar c = Calendar.getInstance();
		
		c.set(2001, 11,31, 12, 2);
		ConvertTimeToFeatures convert = new ConvertTimeToFeatures(c);
		assertEquals(6, convert.getCardinalities().get("days_of_week").intValue());
	}
	
	@Test
	public void testDayOfYearCardinality() {
		Calendar c = Calendar.getInstance();
		
		c.set(2001, 11,31, 12, 2);
		ConvertTimeToFeatures convert = new ConvertTimeToFeatures(c);
		assertEquals(365, convert.getCardinalities().get("days_of_year").intValue());
	}
	
	@Test
	public void testMonthCardinality() {
		Calendar c = Calendar.getInstance();
		
		c.set(2001, 11,31, 12, 2);
		ConvertTimeToFeatures convert = new ConvertTimeToFeatures(c);
		assertEquals(11, convert.getCardinalities().get("months").intValue());
	}
	
	@Test
	public void failToSetData() {
		String test = "test";
		ConvertTimeToFeatures convert = new ConvertTimeToFeatures();
		boolean success = false;
		try{
			convert.setData(test);
		} catch (ConvertTimeToFeaturesDataTypeException e) {
			success = true;
		}
		assertTrue(success);
	}
	
	@Test
	public void succeedToSetData() {
		Calendar test = Calendar.getInstance();
		ConvertTimeToFeatures convert = new ConvertTimeToFeatures();
		boolean success = false;
		try{
			success = convert.setData(test);
		} catch (ConvertTimeToFeaturesDataTypeException e) {
			success = false;
		}
		assertTrue(success);
	}

}
