package me.mcnelis.ml.rudder.util;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.apache.commons.math.util.ResizableDoubleArray;
import org.junit.Test;

public class ConvertTimeToFeatureListTest {

	@Test
	public void testAddFeatureSuccess() {
		ConvertTimeToFeatureList c = new ConvertTimeToFeatureList();
		assertTrue(c.addFeature(TimeFeatures.DAY_OF_WEEK));
	}
	
	@Test
	public void testAddFeatureFailure() {
		ConvertTimeToFeatureList c = new ConvertTimeToFeatureList();
		assertTrue(c.addFeature(TimeFeatures.DAY_OF_WEEK));
		assertFalse(c.addFeature(TimeFeatures.DAY_OF_WEEK));
	}
	
	@Test
	public void testHour0FeatureList() {
		Calendar c = Calendar.getInstance();
		c.set(2011, 12, 6, 0, 2);
		
		ConvertTimeToFeatureList convert = new ConvertTimeToFeatureList(c);
		convert.addFeature(TimeFeatures.HOUR);
		
		assertEquals(1d, convert.getHourList().getElement(0), .2);
		
	}
	
	@Test
	public void testHour6FeatureList() {
		Calendar c = Calendar.getInstance();
		c.set(2011, 12, 6, 6, 2);
		
		ConvertTimeToFeatureList convert = new ConvertTimeToFeatureList(c);
		convert.addFeature(TimeFeatures.HOUR);
		
		assertEquals(1d, convert.getHourList().getElement(6), .2);
		
	}
	
	@Test
	public void testHour16FeatureList() {
		Calendar c = Calendar.getInstance();
		c.set(2011, 12, 6, 16, 2);
		
		ConvertTimeToFeatureList convert = new ConvertTimeToFeatureList(c);
		convert.addFeature(TimeFeatures.HOUR);
		
		assertEquals(1d, convert.getHourList().getElement(16), .2);
		
	}
	
	@Test
	public void testHour23FeatureList() {
		Calendar c = Calendar.getInstance();
		c.set(2011, 12, 6, 23, 2);
		
		ConvertTimeToFeatureList convert = new ConvertTimeToFeatureList(c);
		convert.addFeature(TimeFeatures.HOUR);
		ResizableDoubleArray results = convert.getHourList();
		for(int i=0; i<results.getNumElements(); i++) {
			assertEquals(0d, results.getElement(i), .2);
		}
		
	}
	
	@Test
	public void testMinute12FeatureList() {
		Calendar c = Calendar.getInstance();
		c.set(2011, 12, 6, 23, 12);
		
		ConvertTimeToFeatureList convert = new ConvertTimeToFeatureList(c);
		convert.addFeature(TimeFeatures.HOUR);
		ResizableDoubleArray results = convert.getMinuteList();
		
		assertEquals(1d, results.getElement(12), .2);
		
		
	}
	
	@Test
	public void testMinute59FeatureList() {
		Calendar c = Calendar.getInstance();
		c.set(2011, 12, 6, 23, 59);
		
		ConvertTimeToFeatureList convert = new ConvertTimeToFeatureList(c);
		convert.addFeature(TimeFeatures.HOUR);
		ResizableDoubleArray results = convert.getMinuteList();
		for(int i=0; i<results.getNumElements(); i++) {
			assertEquals(0d, results.getElement(i), .2);
		}
		
	}
	
	@Test
	public void testDOW0FeatureList() {
		Calendar c = Calendar.getInstance();
		
		c.set(2011, 11,4, 12, 2);
		
		ConvertTimeToFeatureList convert = new ConvertTimeToFeatureList(c);
		convert.addFeature(TimeFeatures.HOUR);
		ResizableDoubleArray results = convert.getDayOfWeekList();
		
		assertEquals(0d, results.getElement(3), .2);
		
	}
	
	@Test
	public void testDOW7FeatureList() {
		Calendar c = Calendar.getInstance();
		
		c.set(2011, 11,3, 12, 2);
		
		ConvertTimeToFeatureList convert = new ConvertTimeToFeatureList(c);
		convert.addFeature(TimeFeatures.HOUR);
		ResizableDoubleArray results = convert.getDayOfWeekList();
		
		for(int i=0; i<results.getNumElements(); i++) {
			assertEquals(0d, results.getElement(i), .2);
		}
		
	}
	
	@Test
	public void testMonth1FeatureList() {
		Calendar c = Calendar.getInstance();
		
		c.set(2011, 0,3, 12, 2);
		
		ConvertTimeToFeatureList convert = new ConvertTimeToFeatureList(c);
		convert.addFeature(TimeFeatures.HOUR);
		ResizableDoubleArray results = convert.getMonthList();
	
		assertEquals(1d, results.getElement(0), .2);
		
	}
	
	@Test
	public void testMonth12FeatureList() {
		Calendar c = Calendar.getInstance();
		
		c.set(2011, 11,3, 12, 2);
		
		ConvertTimeToFeatureList convert = new ConvertTimeToFeatureList(c);
		convert.addFeature(TimeFeatures.HOUR);
		ResizableDoubleArray results = convert.getMonthList();
		
		for(int i=0; i<results.getNumElements(); i++) {
			assertEquals(0d, results.getElement(i), .2);
		}
		
	}
	
	@Test
	public void testSecondToLastDayOfLeapYearFeatureList() {
		Calendar c = Calendar.getInstance();
		
		c.set(2004, 11,30, 12, 2);
		
		ConvertTimeToFeatureList convert = new ConvertTimeToFeatureList(c);
		convert.addFeature(TimeFeatures.HOUR);
		ResizableDoubleArray results = convert.getDayOYearList();
		assertEquals(1d, results.getElement(364), .2);
		
		
	}
	
	@Test
	public void testLastDayOfLeapYearFeatureList() {
		Calendar c = Calendar.getInstance();
		
		c.set(2004, 11,31, 12, 2);
		
		ConvertTimeToFeatureList convert = new ConvertTimeToFeatureList(c);
		convert.addFeature(TimeFeatures.HOUR);
		ResizableDoubleArray results = convert.getDayOYearList();

		for(int i=0; i<results.getNumElements(); i++) {
			assertEquals(0d, results.getElement(i), .2);
		}
		
	}
	
	@Test
	public void testSecondToLastDayOfNonLeapYearFeatureList() {
		Calendar c = Calendar.getInstance();
		
		c.set(2001, 11,30, 12, 2);
		
		ConvertTimeToFeatureList convert = new ConvertTimeToFeatureList(c);
		convert.addFeature(TimeFeatures.HOUR);
		ResizableDoubleArray results = convert.getDayOYearList();
		assertEquals(1d, results.getElement(364), .2);
		
		
	}
	
	@Test
	public void testLastDayOfNonLeapYearFeatureList() {
		Calendar c = Calendar.getInstance();
		
		c.set(2001, 11,31, 12, 2);
		
		ConvertTimeToFeatureList convert = new ConvertTimeToFeatureList(c);
		convert.addFeature(TimeFeatures.HOUR);
		ResizableDoubleArray results = convert.getDayOYearList();

		for(int i=0; i<results.getNumElements(); i++) {
			assertEquals(0d, results.getElement(i), .2);
		}
		
	}
	
	@Test
	public void testMinuteCardinality() {
		Calendar c = Calendar.getInstance();
		
		c.set(2001, 11,31, 12, 2);
		ConvertTimeToFeatureList convert = new ConvertTimeToFeatureList(c);
		assertEquals(59, convert.getCardinalities().get("minutes").intValue());
	}
	
	@Test
	public void testHourCardinality() {
		Calendar c = Calendar.getInstance();
		
		c.set(2001, 11,31, 12, 2);
		ConvertTimeToFeatureList convert = new ConvertTimeToFeatureList(c);
		assertEquals(23, convert.getCardinalities().get("hours").intValue());
	}
	
	@Test
	public void testDayOfWeekCardinality() {
		Calendar c = Calendar.getInstance();
		
		c.set(2001, 11,31, 12, 2);
		ConvertTimeToFeatureList convert = new ConvertTimeToFeatureList(c);
		assertEquals(6, convert.getCardinalities().get("days_of_week").intValue());
	}
	
	@Test
	public void testDayOfYearCardinality() {
		Calendar c = Calendar.getInstance();
		
		c.set(2001, 11,31, 12, 2);
		ConvertTimeToFeatureList convert = new ConvertTimeToFeatureList(c);
		assertEquals(365, convert.getCardinalities().get("days_of_year").intValue());
	}
	
	@Test
	public void testMonthCardinality() {
		Calendar c = Calendar.getInstance();
		
		c.set(2001, 11,31, 12, 2);
		ConvertTimeToFeatureList convert = new ConvertTimeToFeatureList(c);
		assertEquals(11, convert.getCardinalities().get("months").intValue());
	}

}
