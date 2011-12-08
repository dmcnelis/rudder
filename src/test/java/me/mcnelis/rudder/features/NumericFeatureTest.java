package me.mcnelis.rudder.features;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class NumericFeatureTest {

	@Test
	public void testGetIntFeatureValue() {
		NumericFeature n = new NumericFeature();
		n.setFeatureValue(1);
		assertEquals(1d, n.getFeatureValue(), .001);
	}
	
	@Test
	public void testGetStringFeatureValue() {
		NumericFeature n = new NumericFeature();
		n.setFeatureValue("1");
		assertEquals(1d, n.getFeatureValue(), .001);
	}

	@Test
	public void testGetBigDecimalFeatureValue() {
		NumericFeature n = new NumericFeature();
		n.setFeatureValue(new BigDecimal(1));
		assertEquals(1d, n.getFeatureValue(), .001);
	}
	
	@Test
	public void testEquals() {
		NumericFeature bf1 = new NumericFeature();
		NumericFeature bf2 = new NumericFeature();
		bf1.setFeatureName("feature 1");
		bf2.setFeatureName("feature 1");
		bf1.setFeatureValue(1);
		bf2.setFeatureValue(1);
		assertTrue(bf1.equals(bf2));

	}
	
	@Test
	public void testNotEquals() {
		NumericFeature bf1 = new NumericFeature();
		NumericFeature bf2 = new NumericFeature();
		bf1.setFeatureName("feature 1");
		bf2.setFeatureName("feature 1");
		bf1.setFeatureValue(1);
		bf2.setFeatureValue(2);
		assertFalse(bf1.equals(bf2));

	}
	
	@Test
	public void testNameNotEquals() {
		NumericFeature bf1 = new NumericFeature();
		NumericFeature bf2 = new NumericFeature();
		bf1.setFeatureName("feature 1");
		bf2.setFeatureName("feature 2");
		bf1.setFeatureValue(1);
		bf2.setFeatureValue(1);
		assertFalse(bf1.equals(bf2));

	}
}
