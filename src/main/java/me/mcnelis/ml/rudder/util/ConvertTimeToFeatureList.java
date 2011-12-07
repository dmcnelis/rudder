package me.mcnelis.ml.rudder.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.commons.math.util.ResizableDoubleArray;

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
public class ConvertTimeToFeatureList implements ConvertToFeatures {

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
	
	protected ResizableDoubleArray hoursDefault;
	protected ResizableDoubleArray dayOfWeekDefault;
	protected ResizableDoubleArray dayOfYearDefault;
	protected ResizableDoubleArray monthDefault;
	protected ResizableDoubleArray minutesDefault;
	
	public ConvertTimeToFeatureList() {
		this.init();
	}
	
	/**
	 * Constructor to build object with calendar
	 * @param Calendar object
	 */
	public ConvertTimeToFeatureList(Calendar c) {
		this.cal = c;
		this.init();
	}
	
	/**
	 * initializes the default data structures so that
	 * we can do it a single time.
	 */
	private synchronized void init() {
		if (this.hoursDefault == null) {
			this.hoursDefault = new ResizableDoubleArray();
			for (int i=0; i<23; i++) {
				this.hoursDefault.addElement(0);
			}
		}
		
		if(this.dayOfWeekDefault == null) {
			this.dayOfWeekDefault = new ResizableDoubleArray();
			for (int i=0; i<6; i++) {
				this.dayOfWeekDefault.addElement(0);
			}		
		}
		
		if(this.dayOfYearDefault == null) {
			this.dayOfYearDefault = new ResizableDoubleArray();
			for (int i=0; i<365; i++) {
				this.dayOfYearDefault.addElement(0);
			}
		}
		
		if(this.monthDefault == null) {
			this.monthDefault = new ResizableDoubleArray();
			for (int i=0; i<11; i++) {
				this.monthDefault.addElement(0);
			}
		}
		
		if(this.minutesDefault == null) {
			this.minutesDefault = new ResizableDoubleArray();
			for (int i=0; i<59; i++) {
				this.minutesDefault.addElement(0);
			}
		}
	}
	
	/**
	 * Runs based on options that are set
	 */
	public ResizableDoubleArray run() {
		ResizableDoubleArray result = new ResizableDoubleArray();
		for (TimeFeatures t : this.featureList) {
			switch (t) {
			case MONTH :
				result.addElements(this.getMonthList().getElements());
			case DAY_OF_YEAR:
				result.addElements(this.getDayOYearList().getElements());
			case DAY_OF_WEEK:
				result.addElements(this.getDayOfWeekList().getElements());
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
	 * 
	 * @return a binary feature list based on minutes in an hour
	 */
	public synchronized ResizableDoubleArray getMinuteList() {
		
		ResizableDoubleArray arr = new ResizableDoubleArray();
		
		int hour = this.cal.get(Calendar.MINUTE);
		
		arr = this.hoursDefault.copy();
		if(arr.getNumElements() > hour)
			arr.setElement(hour, 1);
		
		return arr;
	}
	
	/**
	 * 
	 * @return a binary feature list based on 24 hours in a day
	 */
	public synchronized ResizableDoubleArray getHourList() {
		
		ResizableDoubleArray arr = new ResizableDoubleArray();
		
		int hour = this.cal.get(Calendar.HOUR_OF_DAY);
		
		arr = this.hoursDefault.copy();
		if(arr.getNumElements() > hour)
			arr.setElement(hour, 1);
		
		return arr;
	}
	
	/**
	 * 
	 * @return a binary feature list based on 7 days in a week
	 */
	public synchronized ResizableDoubleArray getDayOfWeekList() {
		
		ResizableDoubleArray arr = new ResizableDoubleArray();
		
		int dayOfWeek = this.cal.get(Calendar.DAY_OF_WEEK) - 1;
		
		arr = this.dayOfWeekDefault.copy();
		if(arr.getNumElements() > dayOfWeek)
			arr.setElement(dayOfWeek, 1);
		
		return arr;
	}

	/**
	 * All results are based on 366 days in a year.  If it is a 
	 * leap year, a day is added to actual day number for any day
	 * after Feb. 28th (the 59th day)
	 * 
	 * @return day of the year binary feature list.
	 */
	public ResizableDoubleArray getDayOYearList() {
		ResizableDoubleArray arr = new ResizableDoubleArray();
		
		int dayOfYear = this.cal.get(Calendar.DAY_OF_YEAR) - 1;
		if (
				dayOfYear > 58 
				&& !(this.cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365) 
			)
			dayOfYear++;
		
		arr = this.dayOfYearDefault.copy();
		if(arr.getNumElements() > dayOfYear) {
			
			arr.setElement(dayOfYear, 1);
		}
		return arr;
	}

	/** 
	 * 
	 * @return Binary vector of months
	 */
	public ResizableDoubleArray getMonthList() {
		ResizableDoubleArray arr = new ResizableDoubleArray();
		
		int month = this.cal.get(Calendar.MONTH);
		
		arr = this.monthDefault.copy();
		if(arr.getNumElements() > month)
			arr.setElement(month, 1);
		
		return arr;
	}

	public synchronized HashMap<String, Integer> getCardinalities() {
		
		HashMap<String, Integer> cards = new HashMap<String, Integer>();
		
		cards.put("hours", hoursDefault.getNumElements());
		cards.put("days_of_week",dayOfWeekDefault.getNumElements());
		cards.put("days_of_year",dayOfYearDefault.getNumElements());
		cards.put("months",monthDefault.getNumElements());
		cards.put("minutes",minutesDefault.getNumElements());
		
		return cards;
	}
}
