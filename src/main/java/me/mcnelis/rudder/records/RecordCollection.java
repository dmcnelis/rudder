package me.mcnelis.rudder.records;

import me.mcnelis.rudder.exceptions.IllegalRecordException;

/**
 * Provide an interface for properly handling and validating records
 * prior to doing the processing as well as getting the data into 
 * the right format.
 * 
 * @author dmcnelis
 *
 */
public interface RecordCollection {

	/**
	 * Set a prototype record to compare other records to.  
	 * @param Record to serve as the prototype
	 * @throws IllegalRecordException 
	 */
	public abstract void setPrototype(Record r) throws IllegalRecordException;
	
	/**
	 * Checks against the prototype prior to trying to add
	 * @param Record to add
	 * @return success of adding record
	 * @throws IllegalRecordException
	 */
	public boolean addAndCheck(Record r) throws IllegalRecordException;
	
	/**
	 * 
	 * @return 2D double array with first dimension as the
	 * individual record keys / y-values, second dimension is 
	 * the double representation of the features.
	 */
	public DoubleMatrix getDoubleMatrix();

}