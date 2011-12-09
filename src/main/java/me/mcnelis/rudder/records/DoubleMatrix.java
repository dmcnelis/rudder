package me.mcnelis.rudder.records;

import java.util.ArrayList;

import org.apache.commons.math.util.ResizableDoubleArray;

/**
 * Data structure to  hold  a matrix of X and Y data for
 * use in various modeling functions (i.e. OLS Regression)
 * @author dmcnelis
 *
 */
public class DoubleMatrix {

	protected ResizableDoubleArray y = new ResizableDoubleArray();
	protected ArrayList<ResizableDoubleArray> xs = new ArrayList<ResizableDoubleArray>();
	
	public void addValues(Double y, ResizableDoubleArray xs) {
		this.y.addElement(y);
		this.xs.add(xs);
	}
	
	public void addValues(Double y, double[] xs) {
		ResizableDoubleArray arr = new ResizableDoubleArray();
		arr.addElements(xs);
		this.addValues(y, arr);
	}
	
	public synchronized double[] getYs() {
		return this.y.getElements();
	}
	
	public synchronized double[][] getXs() {
		double[][] xs = new double[this.xs.size()][];
		int cnt = 0;
		for (ResizableDoubleArray i : this.xs){
			xs[cnt] = i.getElements();
			cnt++;
		}
		return xs;
	}
}
