package me.mcnelis.rudder.ml.supervised.classification;

import static org.junit.Assert.*;

import java.util.HashMap;

import me.mcnelis.rudder.data.Label;
import me.mcnelis.rudder.data.Record;
import me.mcnelis.rudder.data.TextFeature;
import me.mcnelis.rudder.data.collections.IRudderList;
import me.mcnelis.rudder.data.collections.RecordList;
import me.mcnelis.rudder.data.collections.RudderList;
import me.mcnelis.rudder.exceptions.FeatureNotFoundException;

import org.junit.Test;

public class NaiveBayesClassificationTest {

	@Test
	public void testClassScores() {
		IRudderList<MockTextFeature> list = new RudderList<MockTextFeature>();
		
		list = getMockTextFeatures();
		
		NaiveBayesClassification bayes = new NaiveBayesClassification();
		bayes.setData(list);
		bayes.train();
		
		MockTextFeature m = new MockTextFeature();
		MockTextFeature m2 = new MockTextFeature();
		try {
			m.setFeature("text1", "up");
			m.setFeature("text1", "down");
			m.setFeature("text1", "left");
			m.setFeature("text1", "left");
			m.setFeature("text1", "up");
			
			m2.setFeature("text1", "up");
			m2.setFeature("text2", "down");
			m2.setFeature("text3", "b");
			m2.setFeature("text4", "b");
			m2.setFeature("text5", "b");
		} catch (FeatureNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals("contra", bayes.getLabel(m).toLowerCase());
		assertEquals("double dragon", bayes.getLabel(m2).toLowerCase());
	}

	private IRudderList<MockTextFeature> getMockTextFeatures() {
		IRudderList<MockTextFeature> list = new RudderList<MockTextFeature>();
		MockTextFeature mtf1 = new MockTextFeature();
		MockTextFeature mtf2 = new MockTextFeature();
		MockTextFeature mtf3 = new MockTextFeature();
		MockTextFeature mtf4 = new MockTextFeature();
		MockTextFeature mtf5 = new MockTextFeature();
		MockTextFeature mtf6 = new MockTextFeature();
		MockTextFeature mtf7 = new MockTextFeature();
		MockTextFeature mtf8 = new MockTextFeature();
		MockTextFeature mtf9 = new MockTextFeature();
		MockTextFeature mtf10 = new MockTextFeature();
		
		try {
			mtf1.setFeature("text1", "up");
			mtf1.setFeature("text2", "down");
			mtf1.setFeature("text3", "left");
			mtf1.setFeature("text4", "right");
			mtf1.setFeature("text5", "up");
			mtf1.setLabel("label", "contra");
			list.add(mtf1);
			
			mtf2.setFeature("text1", "left");
			mtf2.setFeature("text2", "right");
			mtf2.setFeature("text3", "up");
			mtf2.setFeature("text4", "down");
			mtf2.setFeature("text5", "left");
			mtf2.setLabel("label", "contra");
			list.add(mtf2);
			
			mtf3.setFeature("text1", "up");
			mtf3.setFeature("text2", "right");
			mtf3.setFeature("text3", "left");
			mtf3.setFeature("text4", "right");
			mtf3.setFeature("text5", "up");
			mtf3.setLabel("label", "contra");
			list.add(mtf3);
			
			mtf4.setFeature("text1", "left");
			mtf4.setFeature("text2", "down");
			mtf4.setFeature("text3", "left");
			mtf4.setFeature("text4", "right");
			mtf4.setFeature("text5", "up");
			mtf4.setLabel("label", "contra");
			list.add(mtf4);
			
			mtf5.setFeature("text1", "up");
			mtf5.setFeature("text2", "down");
			mtf5.setFeature("text3", "left");
			mtf5.setFeature("text4", "right");
			mtf5.setFeature("text5", "down");
			mtf5.setLabel("label", "contra");
			list.add(mtf5);
			
			mtf6.setFeature("text1", "A");
			mtf6.setFeature("text2", "b");
			mtf6.setFeature("text3", "left");
			mtf6.setFeature("text4", "c");
			mtf6.setFeature("text5", "up");
			mtf6.setLabel("label", "Double dragon");
			list.add(mtf6);
			
			mtf7.setFeature("text1", "b");
			mtf7.setFeature("text2", "b");
			mtf7.setFeature("text3", "left");
			mtf7.setFeature("text4", "right");
			mtf7.setFeature("text5", "c");
			mtf7.setLabel("label", "double dragon");
			list.add(mtf7);
			
			mtf8.setFeature("text1", "a");
			mtf8.setFeature("text2", "down");
			mtf8.setFeature("text3", "left");
			mtf8.setFeature("text4", "c");
			mtf8.setFeature("text5", "left");
			mtf8.setLabel("label", "double dragon");
			list.add(mtf8);
			
			mtf9.setFeature("text1", "a");
			mtf9.setFeature("text2", "down");
			mtf9.setFeature("text3", "right");
			mtf9.setFeature("text4", "c");
			mtf9.setFeature("text5", "up");
			mtf9.setLabel("label", "double dragon");
			list.add(mtf9);
			
			mtf10.setFeature("text1", "b");
			mtf10.setFeature("text2", "b");
			mtf10.setFeature("text3", "b");
			mtf10.setFeature("text4", "b");
			mtf10.setFeature("text5", "b");
			mtf10.setLabel("label", "double dragon");
			list.add(mtf10);
			
			
		} catch (FeatureNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}

	class MockTextFeature extends Record {
		@TextFeature
		public String text1;
		
		@TextFeature
		public String text2;
		
		@TextFeature
		public String text3;
		
		@TextFeature
		public String text4;
		
		@TextFeature
		public String text5;
		
		@Label
		public String label;
	}
}
