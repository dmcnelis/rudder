package me.mcnelis.rudder.records;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

import me.mcnelis.rudder.exceptions.IllegalRecordException;

/* 
 * Creates a set based implementation of the RecordCollection
 * (non-Javadoc)
 * @see me.mcnelis.rudder.records.RecordCollection(me.mcnelis.rudder.records.Record)
 */
public class RecordSet extends HashSet<Record> implements RecordCollection {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Prototype record
	 */
	protected Record prototype;
	
	/**
	 * Cache of statitics on the dataset.  This is nullified when new
	 * records are added because each record will change results and 
	 * I don't want to carry around the entire stats set all the time.
	 */
	protected HashMap<String, DescriptiveStatistics> statsCache = null;
	
	/* (non-Javadoc)
	 * @see me.mcnelis.rudder.records.RecordCollection#setPrototype(me.mcnelis.rudder.records.Record)
	 */
	public void setPrototype(Record r) throws IllegalRecordException{
		
		if (this.size()>0) {
			if(this.prototype.getFeatures().getFeatureList().equals(r.getFeatures().getFeatureList()))
				this.prototype = r;
			else
				throw new IllegalRecordException();
		}
	}
	
	/* (non-Javadoc)
	 * @see me.mcnelis.rudder.records.RecordCollection#addAndCheck(me.mcnelis.rudder.records.Record)
	 */
	public boolean addAndCheck(Record r) throws IllegalRecordException {
		if(this.prototype == null)
			this.prototype = r;
		if(this.prototype.getFeatures().getFeatureList().equals(r.getFeatures().getFeatureList())) {
			if(super.add(r)) {
				this.statsCache = null;
				return true;
			} else {
				throw new IllegalRecordException();
			}
		}
		else {
			throw new IllegalRecordException();
		}

	}

	/**
	 * Added to show that it shouldn't be used.
	 * @deprecated should use addAndCheck instead
	 */
	public boolean add(Record r) {
		
		return super.add(r);
	}

	/* (non-Javadoc)
	 * @see me.mcnelis.rudder.records.RecordCollection#getDoubleArray()
	 */
	public synchronized DoubleMatrix getDoubleMatrix() {
		DoubleMatrix matrix = new DoubleMatrix();
		
		for(Record r : this) {
			
			if (r instanceof DoubleRecord) {
				if(r.isValid())
					matrix.addValues((Double)r.getKey(), r.getFeatures().getFeatureArray());
			}
		}
		
		return matrix;
	}
	
	public synchronized DescriptiveStatistics getFeatureStatistics(String featureName) {
		if (this.prototype.getFeatures().getFeatureList().contains(featureName)) {
			if (this.statsCache != null && this.statsCache.containsKey(featureName)) {
				return this.statsCache.get(featureName);
			}
			
			DescriptiveStatistics stat = new DescriptiveStatistics();
			
			for (Record r : this) {
				if(r.isValid())
					stat.addValue(r.getFeatures().getFeature(featureName).getFeatureValue());
			}
			
			return stat;
			
		} else {
			return null;
		}
	}
	
	public synchronized DescriptiveStatistics getYStatistics() {
		DescriptiveStatistics d = new DescriptiveStatistics();
		for (Record r : this) {
			if(r.isValid())
				d.addValue((Double)r.getKey());
		}
		return d;
	}

	/**
	 * This is going to be a problem method with larger datasets, will 
	 * probably want to have a different class if you're expecting to do
	 * this sort of behavior regularly
	 * @param key
	 * @return records matching that key
	 */
	public synchronized ArrayList<Record> getRecordsByKey(Object key) {
		ArrayList<Record> records = new ArrayList<Record>();
		
		for(Record r : this) {
			if(r.getKey().equals(key))
				records.add(r);
		}
		
		return records;
	}
}
