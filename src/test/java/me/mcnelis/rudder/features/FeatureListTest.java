package me.mcnelis.rudder.features;

import static org.junit.Assert.*;

import org.junit.Test;

public class FeatureListTest {

	@Test
	public void testGetFeatureArray() {
		FeatureList list = new FeatureList();
		list.add(new NumericFeature("c", 2));
		list.add(new NumericFeature("d", 6));
		list.add(new NumericFeature("alvin", 1));
		
		double[] arr = {1d, 2d, 6d};
		
		assertEquals(arr[0], list.getFeatureArray()[0], .002);
		assertEquals(arr[1], list.getFeatureArray()[1], .002);
		assertEquals(arr[2], list.getFeatureArray()[2], .002);
	}
	
	@Test
	public void addingDuplicateFeatureName() {
		FeatureList list = new FeatureList();
		list.add(new NumericFeature("c", 2));
		list.add(new NumericFeature("d", 6));
		list.add(new NumericFeature("alvin", 4));
		list.add(new NumericFeature("alvin", 1));
		
		for (Feature f : list) {
			
			if(f.getFeatureName().equalsIgnoreCase("alvin"))
				assertEquals(1, f.getFeatureValue(), .002);
		}
	}
	
	@Test
	public void addingNewFeatureNames() {
		FeatureList list = new FeatureList();
		list.add(new NumericFeature("c", 2));
		list.add(new NumericFeature("d", 6));
		list.add(new NumericFeature("alvin", 4));
		assertEquals(3, list.size());
	}
	
	@Test
	public void getFeatureIdxExists() {
		FeatureList list = new FeatureList();
		list.add(new NumericFeature("c", 2));
		assertEquals(0, list.getFeatureIndex("c"));
	}
	
	@Test
	public void getFeatureIdxNotExists() {
		FeatureList list = new FeatureList();
		list.add(new NumericFeature("c", 2));
		assertEquals(-1, list.getFeatureIndex("d"));
	}
	
	@Test
	public void testEqualsTrue() {
		FeatureList list = new FeatureList();
		list.add(new NumericFeature("c",2));
		
		FeatureList list2 = new FeatureList();
		list2.add(new NumericFeature("c",2));
		
		assertTrue(list.equals(list2));
	}

}
