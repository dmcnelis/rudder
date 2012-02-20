package me.mcnelis.rudder.data.collections;

import java.util.ArrayList;
import java.util.List;

import me.mcnelis.rudder.data.RecordInterface;

/**
 * 
 * @author dmcnelis
 *
 * @param <T>
 */
@Deprecated
public class RecordList<T extends RecordInterface> extends ArrayList<T> implements IRudderList<T>
{

	protected int clusterIdx=-1;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/* (non-Javadoc)
	 * @see me.mcnelis.rudder.data.collections.RudderListInterface#getUnsupervisedDoubleDoubleArray()
	 */
	public double[][] getUnsupervisedDoubleDoubleArray() {
		double[][] d = new double[this.size()][];
		int cnt=0;
		synchronized (this) {
			for (RecordInterface r : this) {
				d[cnt] = r.getFeatureAndLabelDoubleArray();
				cnt++;
			}
		}
		return d;
	}

	/* (non-Javadoc)
	 * @see me.mcnelis.rudder.data.collections.RudderListInterface#getSupervisedFeatures()
	 */
	public double[][] getSupervisedFeatures() {
		
			double[][] d = new double[this.size()][];
			int cnt=0;
		synchronized (this) {
			for (RecordInterface r : this) {
				d[cnt] = r.getFeatureDoubleArray();
				cnt++;
			}
		}
		return d;
	}
	
	/* (non-Javadoc)
	 * @see me.mcnelis.rudder.data.collections.RudderListInterface#getSupervisedLabels()
	 */
	public double[] getSupervisedLabels() {
		double[] d = new double[this.size()];
		int cnt=0;
		synchronized (this) {
			for (RecordInterface r : this) {
				d[cnt] = r.getDoubleLabel();
				cnt++;
			}
		}
		
		return d;
	}
	
	public int getClusterId() {
		return clusterIdx;
	}
	public void setClusterId(int id) {
		this.clusterIdx = id;
	}
	
	/* (non-Javadoc)
	 * @see me.mcnelis.rudder.data.collections.RudderListInterface#getUnsupervisedSampleDoubleArray()
	 */
	public double[] getUnsupervisedSampleDoubleArray() {
		return this.get(0).getFeatureAndLabelDoubleArray();
	}
	/* (non-Javadoc)
	 * @see me.mcnelis.rudder.data.collections.RudderListInterface#getSupervisedSampleDoubleArray()
	 */
	public double[] getSupervisedSampleDoubleArray() {
		return this.get(0).getFeatureDoubleArray();
	}

	public String getStringLabel(Object r)
	{
		try 
		{
			RecordInterface rI = (RecordInterface) r;
			return rI.getLabel();
		}
		catch (ClassCastException cce)
		{
			cce.printStackTrace();
		}
		
		return "";
	}

	public List<Object> getRecordFeatures(Object r)
	{
		try 
		{
			RecordInterface rI = (RecordInterface) r;
			List<Object> list = new ArrayList<Object>();
			for(Object o : rI.getAllFeatures())
			{
				list.add(o);
			}
			
			return list;
		}
		catch (ClassCastException cce)
		{
			cce.printStackTrace();
		}
		
		return null;
	}
}
