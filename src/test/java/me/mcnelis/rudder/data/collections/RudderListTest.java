package me.mcnelis.rudder.data.collections;

import static org.junit.Assert.*;

import java.util.List;

import me.mcnelis.rudder.data.MockAnnoTestRecord;

import org.junit.Before;
import org.junit.Test;

public class RudderListTest
{
	RudderList<MockAnnoTestRecord> list = new RudderList<MockAnnoTestRecord>();
	
	@Before
	public void setUp() throws Exception
	{
		this.list.add(new MockAnnoTestRecord(9, "Test", 3, 4, 6, "Home", "Run"));
		this.list.add(new MockAnnoTestRecord(1, "Second", 12, 33, 7, "Run", "Home"));
	}
	@Test
	public void testGetNumericFeatureFromMethodsArray()
	{
		double[] arr = this.list.getItemNumericFeatureArray(0);
		double[] testVals = new double[]{6,3,4};
		assertEquals(arr[0], testVals[0], .00001d);
		assertEquals(arr[1], testVals[1], .00001d);
		assertEquals(arr[2], testVals[2], .00001d);
		assertEquals(3, arr.length);
	}
	
	@Test
	public void testSetLabel()
	{
		MockAnnoTestRecord r = new MockAnnoTestRecord(9, "Test", 3, 4, 6, "Home", "Run");
		this.list.setLabel(r, "test label");
		
		assertEquals("test label", r.getStringLabel());
		
		this.list.setLabel(r, 1d);
		
		assertEquals(1d, r.getDoubleLabel(), .0001);
	}

	@Test
	public void testInitLabelMethods()
	{
		this.list.initLabelMethods(this.list.get(0));
		assertEquals("setTestLabel",this.list.getSetLabelMethods().get(0));
		assertEquals("setLabel",this.list.getSetLabelMethods().get(1));
		assertEquals("setLabel",this.list.getSetLabelMethods().get(2));
	}

	@Test
	public void testGetLabelMethodFromField()
	{
		this.list.getLabelMethodFromField(this.list.get(0));
		assertEquals("setTestLabel",this.list.getSetLabelMethods().get(0));
	}

	@Test
	public void testGetLabelMethodFromMethod()
	{
		this.list.getLabelMethodFromMethod(this.list.get(0));
		assertEquals("setLabel",this.list.getSetLabelMethods().get(1));
	}

	@Test
	public void testGetItemNumericFeatureArray()
	{
		double[] testDouble = this.list.getItemNumericFeatureArray(1);
		double[] testVals = new double[]{7, 12, 33};
		assertEquals(testDouble[0], testVals[0], .00001d);
		assertEquals(testDouble[1], testVals[1], .00001d);
		assertEquals(testDouble[2], testVals[2], .00001d);
		assertEquals(3, testDouble.length);
	}

	@Test
	public void testGetNumericFeatureArray()
	{
		double[] testDouble = this.list.getNumericFeatureArray(this.list.get(1));
		double[] testVals = new double[]{7, 12, 33};
		assertEquals(testDouble[0], testVals[0], .00001d);
		assertEquals(testDouble[1], testVals[1], .00001d);
		assertEquals(testDouble[2], testVals[2], .00001d);
		assertEquals(3, testDouble.length);
	}

	@Test
	public void testInitNumericFeatureFields()
	{
		this.list.initNumericFeatureFields(this.list.get(0));
		List<String> fields = this.list.getNumericFeatureFields();
		assertEquals("dv3", fields.get(0));
		assertEquals(1, fields.size());
	}

	@Test
	public void testGetNumericFeatureArrayFromFields()
	{
		double[] dArr = this.list.getNumericFeatureArrayFromFields(this.list.get(0));
		assertEquals(6, dArr[0], .00001);
		assertEquals(1, dArr.length);
	}

	@Test
	public void testInitNumericFeatureMethods()
	{
		this.list.initNumericFeatureMethods(this.list.get(0));
		List<String> fields = this.list.getNumericFeatureMethods();
		assertEquals("getdVal1", fields.get(0));
		assertEquals("getdVal2", fields.get(1));
		assertEquals(2, fields.size());
	}

	@Test
	public void testGetNumericFeatureArrayFromMethods()
	{
		double[] dArr = this.list.getNumericFeatureArrayFromMethods(this.list.get(0));
		assertEquals(3, dArr[0], .00001);
		assertEquals(4, dArr[1], .00001);
		assertEquals(2, dArr.length);
	}

	@Test
	public void testGetUnsupervisedDoubleDoubleArray()
	{
		double[][] dArr = this.list.getUnsupervisedDoubleDoubleArray();
		assertEquals(9, dArr[0][0], .00001);
		assertEquals(6, dArr[0][1], .00001);
		assertEquals(3, dArr[0][2], .00001);
		assertEquals(4, dArr[0][3], .00001);
		assertEquals(1, dArr[1][0], .00001);
		assertEquals(7, dArr[1][1], .00001);
		assertEquals(12, dArr[1][2], .00001);
		assertEquals(33, dArr[1][3], .00001);
	}

	@Test
	public void testGetSupervisedFeatures()
	{
		double[][] dArr = this.list.getSupervisedFeatures();
		
		assertEquals(6, dArr[0][0], .00001);
		assertEquals(3, dArr[0][1], .00001);
		assertEquals(4, dArr[0][2], .00001);
		
		assertEquals(7, dArr[1][0], .00001);
		assertEquals(12, dArr[1][1], .00001);
		assertEquals(33, dArr[1][2], .00001);
	}

	@Test
	public void testGetSupervisedLabels()
	{
		double[] dArr = this.list.getSupervisedLabels();
		assertEquals(9,dArr[0],.0001);
		assertEquals(1,dArr[1],.0001);
	}

	@Test
	public void testInitNumericLabelFields()
	{
		this.list.initNumericLabelFields();
		List<String> fields = this.list.getNumericLabelFields();
		assertEquals(0, fields.size());
	}

	@Test
	public void testInitNumericLabelMethods()
	{
		this.list.initNumericLabelMethods();
		List<String> fields = this.list.getNumericLabelMethods();
		assertEquals(1, fields.size());
		assertEquals("getDoubleLabel", fields.get(0));
	}

	@Test
	public void testGetDoubleLabelFromFields()
	{
		
		assertEquals(Double.NaN, this.list.getDoubleLabelFromFields(this.list.get(0)), .0001);
	}

	@Test
	public void testGetUnsupervisedSampleDoubleArray()
	{
		double[] dArr = this.list.getUnsupervisedSampleDoubleArray();
		assertEquals(9, dArr[0], .00001);
		assertEquals(6, dArr[1], .00001);
		assertEquals(3, dArr[2], .00001);
		assertEquals(4, dArr[3], .00001);
	}

	@Test
	public void testGetSupervisedSampleDoubleArray()
	{
		double[] dArr = this.list.getSupervisedSampleDoubleArray();
		
		assertEquals(6, dArr[0], .00001);
		assertEquals(3, dArr[1], .00001);
		assertEquals(4, dArr[2], .00001);
	}

	@Test
	public void testGetClusterId()
	{
		this.list.setClusterId(9);
		assertEquals(9, this.list.getClusterId());
	}


	@Test
	public void testGetStringLabel()
	{
		String s = this.list.getStringLabel(this.list.get(0));
		assertEquals("Test", s);
		
		s = this.list.getStringLabel(this.list.get(1));
		assertEquals("Second", s);
	}

	@Test
	public void testGetLabelFromFields()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetLabelFromMethods()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetRecordFeatures()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetFeatureArrayFromFields()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetFeatureArrayFromMethods()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetUnsupervisedDoubleArray()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetNumericLabelsForRecord()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testArrayMerge()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSetNoise()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testIsNoise()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testIsVisited()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSetVisited()
	{
		fail("Not yet implemented");
	}

}
