package me.mcnelis.ml.rudder.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import me.mcnelis.ml.rudder.exceptions.ConvertTimeToFeaturesDataTypeException;
import me.mcnelis.rudder.features.BinaryFeature;
import me.mcnelis.rudder.features.FeatureList;

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
	
	protected ImmutableMap<String, Boolean> hoursDefault;
	protected ImmutableMap<String, Boolean> dayOfWeekDefault;
	protected ImmutableMap<String, Boolean> dayOfYearDefault;
	protected ImmutableMap<String, Boolean> monthDefault;
	protected ImmutableMap<String, Boolean> minutesDefault;
	
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
		HashMap<String, Boolean> holder = null;
		
		if (this.hoursDefault == null) {
			holder = new HashMap<String, Boolean>();
			
			for (int i=0; i<23; i = i+1) {		
				Integer q = Integer.valueOf(i);
				holder.put(q.toString(), false);
				
			}
			this.hoursDefault = ImmutableMap.copyOf(holder);
		}
		
		if(this.dayOfWeekDefault == null) {
			holder = new HashMap<String, Boolean>();
			for (int i=0; i<6; i++) {
				holder.put(Integer.toString(i), false);
			}	
			this.dayOfWeekDefault = ImmutableMap.copyOf(holder);
		}
		
		if(this.dayOfYearDefault == null) {
			holder = new HashMap<String, Boolean>();
			for (int i=1; i<366; i++) {
				holder.put(Integer.toString(i), false);
			}
			this.dayOfYearDefault = ImmutableMap.copyOf(holder);
		}
		
		if(this.monthDefault == null) {
			holder = new HashMap<String, Boolean>();
			for (int i=0; i<11; i++) {
				holder.put(Integer.toString(i), false);
			}
			this.monthDefault = ImmutableMap.copyOf(holder);
		}
		
		if(this.minutesDefault == null) {
			holder = new HashMap<String, Boolean>();
			for (int i=0; i<59; i++) {
				holder.put(Integer.toString(i), false);
			}
			this.minutesDefault = ImmutableMap.copyOf(holder);
		}
	}
	
	/**
	 * Runs based on options that are set
	 * 
	 * @TODO update to return a FeatureList populated with Binary Features!
	 */
	public FeatureList run() {
		FeatureList result = new FeatureList();
		
		for (TimeFeatures t : this.featureList) {
			switch (t) {
			case MINUTE :
				result.addAll(this.buildFeatureSet(this.getMinuteList(), t));
			case HOUR :
				result.addAll(this.buildFeatureSet(this.getHourList(), t));
			case MONTH :
				result.addAll(this.buildFeatureSet(this.getMonthList(), t));
			case DAY_OF_YEAR:
				result.addAll(this.buildFeatureSet(this.getDayOYearList(), t));
			case DAY_OF_WEEK:
				result.addAll(this.buildFeatureSet(this.getDayOfWeekList(), t));
			}
		}

		return result;
	}
	
	private FeatureList buildFeatureSet(HashMap<String, Boolean> result, TimeFeatures t) {
		String keyBase = "";
		switch (t) {
		case MINUTE:
			keyBase = "Minute ";
		case HOUR:
			keyBase = "Hour ";
		case MONTH :
			keyBase = "Month ";
		case DAY_OF_YEAR: 
			keyBase = "Day of year ";
		case DAY_OF_WEEK : 
			keyBase = "Day of week ";
		}
		
		FeatureList list = new FeatureList();
		for (String key : result.keySet()) {
			list.add(new BinaryFeature(keyBase + key, result.get(key)));
		}
		
		return list;
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
	private HashMap<String, Boolean> updateMap(HashMap<String, Boolean> map, int type) {
		int keyVal = this.cal.get(type);
		
		if(
				type == Calendar.DAY_OF_YEAR
				&& keyVal > 58 
				&& !(this.cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365) 
			)
			keyVal++;
			
		
		String key = Integer.toString(keyVal);
		
		if (map.get(key) != null) {
			map.put(key, true);
			
		}
			 
		return map;
	}

	/**
	 * 
	 * @return a binary feature list based on minutes in an hour
	 */
	public synchronized HashMap<String, Boolean> getMinuteList() {
		HashMap<String, Boolean> arr = new HashMap<String, Boolean>();
		arr.putAll(this.minutesDefault);
		
		return this.updateMap(arr, Calendar.MINUTE);
	}
	
	/**
	 * 
	 * @return a binary feature list based on 24 hours in a day
	 */
	public synchronized HashMap<String, Boolean> getHourList() {
		HashMap<String, Boolean> arr = new HashMap<String, Boolean>();
		arr.putAll((this.hoursDefault));
		
		return this.updateMap(arr, Calendar.HOUR_OF_DAY);
	}
	
	/**
	 * 
	 * @return a binary feature list based on 7 days in a week
	 */
	public synchronized HashMap<String, Boolean> getDayOfWeekList() {
		HashMap<String, Boolean> arr = new HashMap<String, Boolean>();
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
	public HashMap<String, Boolean> getDayOYearList() {
		
		HashMap<String, Boolean> arr = new HashMap<String, Boolean>();
		arr.putAll((this.dayOfYearDefault));
		
		return this.updateMap(arr, Calendar.DAY_OF_YEAR);
	}

	/** 
	 * 
	 * @return Binary vector of months
	 */
	public HashMap<String, Boolean> getMonthList() {
		HashMap<String, Boolean> arr = new HashMap<String, Boolean>();
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
