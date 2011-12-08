package me.mcnelis.ml.rudder.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import me.mcnelis.ml.rudder.exceptions.ConvertTimeToFeaturesDataTypeException;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap;

/**
 * Converts a time to a binary featureset.
 * 
 * User can specify the fields they want returned (i.e. year only, day of week, ect)
 * 
 * This class should have the time re-set instead of new instantiations if possible
 * 
 * Currently only supports up to minute granularity.
 * 
 * Example output for Day of the Week when the date passed in is a Monday:
 * {0,1,0,0,0,0}
 * 
 * When it is a Saturday:
 * {0,0,0,0,0,0}
 * 
 * @author dmcnelis
 *
 */
public class ConvertTimeToFeatures implements ConvertToFeatures {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * Holder to define the features we will use
	 */
	protected ArrayList<TimeFeatures> featureList = new ArrayList<TimeFeatures>();
	
	/**
	 * Calendar holder, only need one for each instance
	 */
	protected Calendar cal;
	
	protected ImmutableMap<String, Double> hoursDefault;
	protected ImmutableMap<String, Double> dayOfWeekDefault;
	protected ImmutableMap<String, Double> dayOfYearDefault;
	protected ImmutableMap<String, Double> monthDefault;
	protected ImmutableMap<String, Double> minutesDefault;
	
	public ConvertTimeToFeatures() {
		this.init();
	}
	
	/**
	 * Constructor to build object with calendar
	 * @param Calendar object
	 */
	public ConvertTimeToFeatures(Calendar c) {
		this.cal = c;
		this.init();
	}
	
	/**
	 * initializes the default data structures so that
	 * we can do it a single time.
	 */
	private synchronized void init() {
		HashMap<String, Double> holder = null;
		
		if (this.hoursDefault == null) {
			holder = new HashMap<String, Double>();
			
			for (int i=0; i<23; i = i+1) {		
				Integer q = Integer.valueOf(i);
				holder.put(q.toString(), 0d);
				
			}
			this.hoursDefault = ImmutableMap.copyOf(holder);
		}
		
		if(this.dayOfWeekDefault == null) {
			holder = new HashMap<String, Double>();
			for (int i=0; i<6; i++) {
				holder.put(Integer.toString(i), 0d);
			}	
			this.dayOfWeekDefault = ImmutableMap.copyOf(holder);
		}
		
		if(this.dayOfYearDefault == null) {
			holder = new HashMap<String, Double>();
			for (int i=0; i<366; i++) {
				holder.put(Integer.toString(i), 0d);
			}
			this.dayOfYearDefault = ImmutableMap.copyOf(holder);
		}
		
		if(this.monthDefault == null) {
			holder = new HashMap<String, Double>();
			for (int i=0; i<11; i++) {
				holder.put(Integer.toString(i), 0d);
			}
			this.monthDefault = ImmutableMap.copyOf(holder);
		}
		
		if(this.minutesDefault == null) {
			holder = new HashMap<String, Double>();
			for (int i=0; i<59; i++) {
				holder.put(Integer.toString(i), 0d);
			}
			this.minutesDefault = ImmutableMap.copyOf(holder);
		}
	}
	
	/**
	 * Runs based on options that are set
	 * 
	 * @TODO update to return a FeatureList populated with Binary Features!
	 */
	public HashMap<String, Double> run() {
		HashMap<String, Double> result = new HashMap<String, Double>();
		
		for (TimeFeatures t : this.featureList) {
			switch (t) {
			case MONTH :
				result.putAll(this.getMonthList());
			case DAY_OF_YEAR:
				result.putAll(this.getDayOYearList());
			case DAY_OF_WEEK:
				result.putAll(this.getDayOfWeekList());
			}
		}
		return result;
	}
	
	/**
	 * In a threadsafe way, add a new time feature definition
	 * 
	 * Output will be produced in the same order it is passed in.
	 * 
	 * @param TimeFeature
	 */
	public boolean addFeature(TimeFeatures feature) {
		synchronized (this) {
			if( ! this.featureList.contains(feature) ) {
				this.featureList.add(feature);
				return true;
			} else {
				return false;
			}
		}
	}
	
	/**
	 * Generic method to assign value to a map
	 * @param map of fields
	 * @param key of field to set to true
	 * @return updated map of key/values
	 */
	private HashMap<String, Double> updateMap(HashMap<String, Double> map, int type) {
		int keyVal = this.cal.get(type);
		
		if(
				type == Calendar.DAY_OF_YEAR
				&& keyVal > 58 
				&& !(this.cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365) 
			)
			keyVal++;
			
		
		String key = Integer.toString(keyVal);
		if (map.get(Integer.parseInt(key)) != null) 
			map.put(key, 1d);
		else {
			for(String keys : map.keySet())
			System.out.println("'" + key.hashCode() + "': '" + keys.hashCode() + "' - " + map.get(keys) );
		}
			 
		return map;
	}

	/**
	 * 
	 * @return a binary feature list based on minutes in an hour
	 */
	public synchronized HashMap<String, Double> getMinuteList() {
		HashMap<String, Double> arr = new HashMap<String, Double>();
		arr.putAll(this.minutesDefault);
		
		return this.updateMap(arr, Calendar.MINUTE);
	}
	
	/**
	 * 
	 * @return a binary feature list based on 24 hours in a day
	 */
	public synchronized HashMap<String, Double> getHourList() {
		HashMap<String, Double> arr = new HashMap<String, Double>();
		arr.putAll((this.hoursDefault));
		
		return this.updateMap(arr, Calendar.HOUR_OF_DAY);
	}
	
	/**
	 * 
	 * @return a binary feature list based on 7 days in a week
	 */
	public synchronized HashMap<String, Double> getDayOfWeekList() {
		HashMap<String, Double> arr = new HashMap<String, Double>();
		arr.putAll((this.dayOfWeekDefault));
		
		return this.updateMap(arr, Calendar.DAY_OF_WEEK);
	}

	/**
	 * All results are based on 366 days in a year.  If it is a 
	 * leap year, a day is added to actual day number for any day
	 * after Feb. 28th (the 59th day)
	 * 
	 * Because of the special behavior we can't use the default class from above
	 * 
	 * @return day of the year binary feature list.
	 */
	public HashMap<String, Double> getDayOYearList() {
		
		HashMap<String, Double> arr = new HashMap<String, Double>();
		arr.putAll((this.dayOfYearDefault));
		
		return this.updateMap(arr, Calendar.DAY_OF_YEAR);
	}

	/** 
	 * 
	 * @return Binary vector of months
	 */
	public HashMap<String, Double> getMonthList() {
		HashMap<String, Double> arr = new HashMap<String, Double>();
		arr.putAll((this.monthDefault));

		return this.updateMap(arr, Calendar.MONTH);
	}

	/**
	 * 
	 * @return Map of cardinalities from the data
	 */
	public synchronized HashMap<String, Integer> getCardinalities() {
		
		HashMap<String, Integer> cards = new HashMap<String, Integer>();
		
		cards.put("hours", hoursDefault.size());
		cards.put("days_of_week",dayOfWeekDefault.size());
		cards.put("days_of_year",dayOfYearDefault.size());
		cards.put("months",monthDefault.size());
		cards.put("minutes",minutesDefault.size());
		
		return cards;
	}

	/**
	 * @param Calendar object
	 * @return true if successful setting of data
	 * @throws ConverTimeToFeaturesDataTypeException if the wrong object type is passed in
	 */
	public boolean setData(Object data) throws ConvertTimeToFeaturesDataTypeException {
		if(data instanceof Calendar)
			this.cal = (Calendar) data;
		else
			throw new ConvertTimeToFeaturesDataTypeException();
		
		return true;
	}
}
