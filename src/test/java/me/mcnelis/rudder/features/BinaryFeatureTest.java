package me.mcnelis.rudder.features;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.BeforeClass;
import org.junit.Test;

public class BinaryFeatureTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testEquals() {
		BinaryFeature bf1 = new BinaryFeature();
		BinaryFeature bf2 = new BinaryFeature();
		bf1.setFeatureName("feature 1");
		bf2.setFeatureName("feature 1");
		bf1.setFeatureValue(true);
		bf2.setFeatureValue(true);
		assertTrue(bf1.equals(bf2));

	}
	
	@Test
	public void testNotEquals() {
		BinaryFeature bf1 = new BinaryFeature();
		BinaryFeature bf2 = new BinaryFeature();
		bf1.setFeatureName("feature 1");
		bf2.setFeatureName("feature 1");
		bf1.setFeatureValue(true);
		bf2.setFeatureValue(false);
		assertFalse(bf1.equals(bf2));

	}
	
	@Test
	public void testNameNotEquals() {
		BinaryFeature bf1 = new BinaryFeature();
		BinaryFeature bf2 = new BinaryFeature();
		bf1.setFeatureName("feature 1");
		bf2.setFeatureName("feature 2");
		bf1.setFeatureValue(true);
		bf2.setFeatureValue(true);
		assertFalse(bf1.equals(bf2));

	}
	

	@Test
	public void testClone() {
		BinaryFeature bf1 = new BinaryFeature();
		bf1.setFeatureName("feature 1");
		bf1.setFeatureValue(true);
		try {
			BinaryFeature bf2 = bf1.clone();
			assertTrue(bf1.equals(bf2));
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			fail("Not CloneNotSupportedException");
		}
	}

	@Test
	public void testToString() {
		BinaryFeature b = new BinaryFeature();
		b.setFeatureName("test feature 1");
		b.setFeatureValue(true);
		assertEquals("test feature 1: true", b.toString());
	}
	
	@Test
	public void testCompareTo() {
		BinaryFeature a = new BinaryFeature();
		a.setFeatureName("a");
		
		BinaryFeature b = new BinaryFeature();
		b.setFeatureName("b");
		
		ArrayList<BinaryFeature> list = new ArrayList<BinaryFeature>();
		list.add(b);
		list.add(a);
		
		assertEquals("b", list.get(0).getFeatureName());
		assertEquals("a", list.get(1).getFeatureName());
		
		Collections.sort(list);
		
		assertEquals("a", list.get(0).getFeatureName());
		assertEquals("b", list.get(1).getFeatureName());
	}
}
