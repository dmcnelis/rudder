package me.mcnelis.rudder.io;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;

import me.mcnelis.rudder.records.Record;
import me.mcnelis.rudder.records.RecordSet;

import org.junit.Test;

public class NumericDelimitedImporterTest {

	@Test
	public void testLoadCsvRecords() {
	
		NumericDelimitedImporter io = new NumericDelimitedImporter();
		io.setFileUri("src/test/resources/test.csv");
		io.setExampleField(0);
		RecordSet rs = null;
		try {
			rs = io.loadRecords();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error loading records");
		}
		
		ArrayList<Record> r = rs.getRecordsByKey(4.0d);
		
		assertEquals(7d, r.get(0).getFeatures().getFeature("field1").getFeatureValue(), .002);
		
	}

	@Test
	public void testLoadTabRecords() {
	
		NumericDelimitedImporter io = new NumericDelimitedImporter();
		io.setFileUri("src/test/resources/test.tab");
		io.setExampleField(0);
		io.setDelim('\t');
		RecordSet rs = null;
		try {
			rs = io.loadRecords();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error loading records");
		}
		
		ArrayList<Record> r = rs.getRecordsByKey(4.0d);
		
		assertEquals(7d, r.get(0).getFeatures().getFeature("field1").getFeatureValue(), .002);
		
	}
}
