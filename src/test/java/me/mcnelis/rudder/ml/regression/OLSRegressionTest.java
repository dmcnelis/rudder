package me.mcnelis.rudder.ml.regression;

import static org.junit.Assert.*;

import me.mcnelis.rudder.exceptions.NoValidRegressionException;
import me.mcnelis.rudder.io.NumericDelimitedImporter;
import me.mcnelis.rudder.records.RecordSet;

import org.junit.Test;

public class OLSRegressionTest {

	@Test
	public void testGetCoefficients() {
		NumericDelimitedImporter io = new NumericDelimitedImporter();
		io.setFileUri("src/test/resources/testRegression.tab");
		io.setDelim('\t');
		io.setExampleField(0);
		RecordSet rs = null;
		try {
			rs = io.loadRecords();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error loading records");
		}
		
		OLSRegression o = new OLSRegression();
		o.runRegression(rs);
		
		try {
			double[] coefs = o.getCoefficients();
			assertEquals(12d, coefs[0], .00001);
			assertEquals(4d, coefs[1], .00001);
			assertEquals(6d, coefs[2], .00001);
			assertEquals(5d, coefs[3], .00001);
		} catch (NoValidRegressionException nvre) {
			fail("Should not be invalid");
		}
	}

	
	@Test
	public void testPredictValueRecord() {
		NumericDelimitedImporter io = new NumericDelimitedImporter();
		io.setFileUri("src/test/resources/testRegression.tab");
		io.setDelim('\t');
		io.setExampleField(0);
		RecordSet rs = null;
		try {
			rs = io.loadRecords();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error loading records");
		}
		
		OLSRegression o = new OLSRegression();
		o.runRegression(rs);
		
		assertEquals(69d,o.predictValue(rs.getRecordsByKey(69d).get(0)), .0001);
	}

	@Test
	public void testPredictValueDoubleArray() {
		NumericDelimitedImporter io = new NumericDelimitedImporter();
		io.setFileUri("src/test/resources/testRegression.tab");
		io.setDelim('\t');
		io.setExampleField(0);
		RecordSet rs = null;
		try {
			rs = io.loadRecords();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error loading records");
		}
		
		OLSRegression o = new OLSRegression();
		o.runRegression(rs);
		double[] xs = new double[]{6d,1d,1d};
		assertEquals(47d,o.predictValue(xs), .0001);
	}
	
	@Test
	public void testGetResiduals() {
		NumericDelimitedImporter io = new NumericDelimitedImporter();
		io.setFileUri("src/test/resources/testRegression.tab");
		io.setDelim('\t');
		io.setExampleField(0);
		RecordSet rs = null;
		try {
			rs = io.loadRecords();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error loading records");
		}
		
		OLSRegression o = new OLSRegression();
		o.runRegression(rs);

		try {
			for(double d : o.getResiduals()) {
				assertEquals(0d, d, .0000001);
			}
		} catch (NoValidRegressionException e) {
			fail("Should have been a valid regression");
		}
	}


}
