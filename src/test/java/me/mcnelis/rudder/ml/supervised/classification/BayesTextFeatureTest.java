package me.mcnelis.rudder.ml.supervised.classification;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

public class BayesTextFeatureTest {

	@Test
	public void testGetProbabilities() {
		BayesDiscreteFeature f = new BayesDiscreteFeature();
		f.add("test1");
		f.add("string2");
		f.add("string2");
		f.add("chicken dinner");
		
		HashMap<Object, Double> probs = f.getProbabilities();
		assertEquals(.25d, probs.get("test1"), .0001);
		assertEquals(.25d, probs.get("chicken dinner"), .0001);
		assertEquals(.50d, probs.get("string2"), .0001);
	}

	@Test
	public void testGetClassScoreZero() {
		BayesDiscreteFeature f = new BayesDiscreteFeature();
		f.add("test1");
		f.add("string2");
		f.add("string2");
		f.add("chicken dinner");

		assertEquals(0d, f.getClassScore("na"), .0001);
		
	}

	@Test
	public void testGetClassScorePointFiveNoProbTestFirst() {
		BayesDiscreteFeature f = new BayesDiscreteFeature();
		f.add("test1");
		f.add("string2");
		f.add("string2");
		f.add("chicken dinner");

		assertEquals(.5d, f.getClassScore("string2"), .0001);
		
	}
	
	@Test
	public void testGetClassScorePointFiveProbTestFirst() {
		BayesDiscreteFeature f = new BayesDiscreteFeature();
		f.add("test1");
		f.add("string2");
		f.add("string2");
		f.add("chicken dinner");
		f.getProbabilities();
		assertEquals(.5d, f.getClassScore("string2"), .0001);
		
	}
}
