package me.mcnelis.rudder.ml.supervised.regression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import me.mcnelis.rudder.data.MockRecord;
import me.mcnelis.rudder.data.NumericFeature;
import me.mcnelis.rudder.data.collections.IRudderList;
import me.mcnelis.rudder.data.collections.RudderList;

import org.junit.Test;

import au.com.bytecode.opencsv.CSVReader;

public class MultiLinearRegressionTest {

	@Test
	public void testAddRecordSuccess() {
		MultiLinearRegression<MockRecord> ols = new MultiLinearRegression<MockRecord>();
		MockRecord r = new MockRecord(){
			@SuppressWarnings("unused")
			@NumericFeature
			double f2 = 0d;
		};
		assertTrue(ols.addRecord(r));
	}
	
	@Test
	public void testAddRecordFailure() {
		MultiLinearRegression<MockRecord> ols = new MultiLinearRegression<MockRecord>();
		MockRecord r = new MockRecord(){
			@SuppressWarnings("unused")
			@NumericFeature
			double f2 = 0d;
			@SuppressWarnings("unused")
			@NumericFeature
			double f3 = 0d;
		};
		assertTrue(ols.addRecord(r));
		
		MockRecord r2 = new MockRecord(){
			@SuppressWarnings("unused")
			@NumericFeature
			double f2 = 0d;
			
		};
		try
		{
			ols.addRecord(r2);
			fail("Added records with different dimensions");
		} catch(Exception e)
		{
			assertTrue(true);
		}
	}


	@Test
	public void testRunRegression() {
		CSVReader r = null;
		try {
			r = new CSVReader(new FileReader("src/test/resources/testRegression.tab"),'\t');
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		IRudderList<MockRecord> list = new RudderList<MockRecord>();
		
		String[] line = null;
		try {
			while((line = r.readNext())!=null) {
				MockRecord m = new MockRecord();
				try {
					m.setDoubleLabel(Double.parseDouble(line[0]));
					m.setFeature("feature1", Double.parseDouble(line[1]));
					m.setFeature("feature2", Double.parseDouble(line[2]));
					m.setFeature("feature3", Double.parseDouble(line[3]));
					list.add(m);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MultiLinearRegression ols = new MultiLinearRegression(list);
		double[] betas = ols.runRegression();
		
		assertEquals(12d, betas[0], .00001);
		assertEquals(4d, betas[1],.00001);
		assertEquals(6d, betas[2],.00001);
		assertEquals(5d, betas[3],.00001);
		
	}

}
