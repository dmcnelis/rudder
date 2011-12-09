package me.mcnelis.ml.rudder.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.HashMap;

import me.mcnelis.ml.rudder.exceptions.ConvertTimeToFeaturesDataTypeException;

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
		
		assertTrue(convert.getHourList().get("0"));
		
		
	}
	
	@Test
	public void testHour6FeatureList() {
		Calendar c = Calendar.getInstance();
		c.set(2011, 12, 6, 6, 2);
		
		ConvertTimeToFeatures convert = new ConvertTimeToFeatures(c);
		convert.addFeature(TimeFeatures.HOUR);
		
		assertTrue(convert.getHourList().get("6"));
		
	}
	
	@Test
	public void testHour16FeatureList() {
		Calendar c = Calendar.getInstance();
		c.set(2011, 12, 6, 16, 2);
		
		ConvertTimeToFeatures convert = new ConvertTimeToFeatures(c);
		convert.addFeature(TimeFeatures.HOUR);
		
		assertTrue(convert.getHourList().get("16"));
		
	}
	
	@Test
	public void testHour23FeatureList() {
		Calendar c = Calendar.getInstance();
		c.set(2011, 12, 6, 23, 2);
		
		ConvertTimeToFeatures convert = new ConvertTimeToFeatures(c);
		convert.addFeature(TimeFeatures.HOUR);
		HashMap<String, Boolean> results = convert.getHourList();
		for(String key : results.keySet()) {
			assertFalse(results.get(key));
		}
		
	}
	
	@Test
	public void testMinute12FeatureList() {
		Calendar c = Calendar.getInstance();
		c.set(2011, 12, 6, 23, 12);
		
		ConvertTimeToFeatures convert = new ConvertTimeToFeatures(c);
		convert.addFeature(TimeFeatures.HOUR);
		HashMap<String, Boolean> results = convert.getMinuteList();
		
		assertTrue( results.get("12") );
		
		
	}
	
	@Test
	public void testMinute59FeatureList() {
		Calendar c = Calendar.getInstance();
		c.set(2011, 12, 6, 23, 59);
		
		ConvertTimeToFeatures convert = new ConvertTimeToFeatures(c);
		convert.addFeature(TimeFeatures.HOUR);
		HashMap<String, Boolean> results = convert.getMinuteList();
		for(String key : results.keySet()) {
			assertFalse(results.get(key));
		}
		
	}
	
	@Test
	public void testDOW0FeatureList() {
		Calendar c = Calendar.getInstance();
		
		c.set(2011, 11,4, 12, 2);
		
		ConvertTimeToFeatures convert = new ConvertTimeToFeatures(c);
		convert.addFeature(TimeFeatures.HOUR);
		HashMap<String, Boolean> results = convert.getDayOfWeekList();
		
		assertFalse(results.get("3"));
		
	}
	
	@Test
	public void testDOW7FeatureList() {
		Calendar c = Calendar.getInstance();
		
		c.set(2011, 11,3, 12, 2);
		
		ConvertTimeToFeatures convert = new ConvertTimeToFeatures(c);
		convert.addFeature(TimeFeatures.HOUR);
		HashMap<String, Boolean> results = convert.getDayOfWeekList();
		
		for(String key : results.keySet()) {
			assertFalse(results.get(key));
		}
		
	}
	
	@Test
	public void testMonth1FeatureList() {
		Calendar c = Calendar.getInstance();
		
		c.set(2011, 0,3, 12, 2);
		
		ConvertTimeToFeatures convert = new ConvertTimeToFeatures(c);
		convert.addFeature(TimeFeatures.HOUR);
		HashMap<String, Boolean> results = convert.getMonthList();
	
		assertTrue(results.get("0"));
		
	}
	
	@Test
	public void testMonth12FeatureList() {
		Calendar c = Calendar.getInstance();
		
		c.set(2011, 11,3, 12, 2);
		
		ConvertTimeToFeatures convert = new ConvertTimeToFeatures(c);
		convert.addFeature(TimeFeatures.HOUR);
		HashMap<String, Boolean> results = convert.getMonthList();
		
		for(String key : results.keySet()) {
			assertFalse(results.get(key));
		}
		
	}
	
	@Test
	public void testSecondToLastDayOfLeapYearFeatureList() {
		Calendar c = Calendar.getInstance();
		
		c.set(2004, 11,30, 12, 2);
		
		ConvertTimeToFeatures convert = new ConvertTimeToFeatures(c);
		convert.addFeature(TimeFeatures.HOUR);
		HashMap<String, Boolean> results = convert.getDayOYearList();
		
		assertTrue(results.get("365"));
		
		
	}
	
	@Test
	public void testLastDayOfLeapYearFeatureList() {
		Calendar c = Calendar.getInstance();
		
		c.set(2004, 11,31, 12, 2);
		
		ConvertTimeToFeatures convert = new ConvertTimeToFeatures(c);
		convert.addFeature(TimeFeatures.HOUR);
		HashMap<String, Boolean> results = convert.getDayOYearList();

		for(String key : results.keySet()) {
			
			assertFalse(results.get(key));
		}
		
	}
	
	@Test
	public void testSecondToLastDayOfNonLeapYearFeatureList() {
		Calendar c = Calendar.getInstance();
		
		c.set(2001, 11,30, 12, 2);
		
		ConvertTimeToFeatures convert = new ConvertTimeToFeatures(c);
		convert.addFeature(TimeFeatures.HOUR);
		HashMap<String, Boolean> results = convert.getDayOYearList();
		
		assertTrue(results.get("365"));
		
		
		
	}
	
	@Test
	public void testLastDayOfNonLeapYearFeatureList() {
		Calendar c = Calendar.getInstance();
		
		c.set(2001, 11,31, 12, 2);
		
		ConvertTimeToFeatures convert = new ConvertTimeToFeatures(c);
		convert.addFeature(TimeFeatures.HOUR);
		HashMap<String, Boolean> results = convert.getDayOYearList();

		for(String key : results.keySet()) {
			assertFalse(results.get(key));
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
