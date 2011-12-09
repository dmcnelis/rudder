package me.mcnelis.rudder.records;

import static org.junit.Assert.*;

import org.apache.commons.math.util.ResizableDoubleArray;
import org.junit.Test;

public class DoubleMatrixTest {

	@Test
	public void testGetYs() {
		DoubleMatrix m = new DoubleMatrix();
		ResizableDoubleArray arr = new ResizableDoubleArray();
		arr.addElement(0);
		arr.addElement(1);
		arr.addElement(3);
		
		ResizableDoubleArray arr2 = new ResizableDoubleArray();
		arr.addElement(4);
		arr.addElement(5);
		arr.addElement(6);
		
		ResizableDoubleArray arr3 = new ResizableDoubleArray();
		arr.addElement(7);
		arr.addElement(8);
		arr.addElement(9);
		
		m.addValues(1d, arr);
		m.addValues(2d, arr2);
		m.addValues(3d, arr3);
		
		double[] ys = m.getYs();
		assertEquals(1d, ys[0], .002);
		assertEquals(2d, ys[1], .002);
		assertEquals(3d, ys[2], .002);
		
	}

	@Test
	public void testGetXs() {
		DoubleMatrix m = new DoubleMatrix();
		ResizableDoubleArray arr = new ResizableDoubleArray();
		arr.addElement(0);
		arr.addElement(1);
		arr.addElement(3);
		
		ResizableDoubleArray arr2 = new ResizableDoubleArray();
		arr2.addElement(4);
		arr2.addElement(5);
		arr2.addElement(6);
		
		ResizableDoubleArray arr3 = new ResizableDoubleArray();
		arr3.addElement(7);
		arr3.addElement(8);
		arr3.addElement(9);
		
		m.addValues(1d, arr);
		m.addValues(2d, arr2);
		m.addValues(3d, arr3);
		
		double[][] xs = m.getXs();
		assertEquals(0d, xs[0][0], .002);
		assertEquals(1d, xs[0][1], .002);
		assertEquals(3d, xs[0][2], .002);
		
		assertEquals(4d, xs[1][0], .002);
		assertEquals(5d, xs[1][1], .002);
		assertEquals(6d, xs[1][2], .002);
		
		assertEquals(7d, xs[2][0], .002);
		assertEquals(8d, xs[2][1], .002);
		assertEquals(9d, xs[2][2], .002);
		
	}

}
