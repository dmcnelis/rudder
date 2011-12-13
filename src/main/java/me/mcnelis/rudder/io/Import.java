package me.mcnelis.rudder.io;

import me.mcnelis.rudder.records.RecordCollection;

/** 
 * Define the methods around importing data
 * into record  sets from files or database resultsets
 * 
 * @author dmcnelis
 *
 */
public interface Import {
	
	/**
	 * 
	 * @return RecordCollection based on data available
	 */
	public RecordCollection loadRecords();
	
	/**
	 * set the data to load records from
	 * @param o
	 */
	public void setData(Object o);
	
}
