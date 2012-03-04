package me.mcnelis.rudder.data.collections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math.util.ResizableDoubleArray;
import org.apache.log4j.Logger;

import me.mcnelis.rudder.data.FeatureType;
import me.mcnelis.rudder.data.Label;
import me.mcnelis.rudder.data.NumericFeature;
import me.mcnelis.rudder.data.TextFeature;

/**
 * RudderList is a collection to take any object that
 * is annotated with Feature or Label annotations.  These
 * annotations can be either methods or fields.  Methods
 * are preferable, as they allow the developer to better
 * customize how their data is passed into the ML algorithms.
 * 
 * This deprecates the earlier RecordList and Record objects.
 * 
 * This will require additional refactoring if/when Neural Networks
 * are added to the library
 * 
 * TODO: re-work to support NN
 * 
 * @author dmcnelis@gmail.com
 *
 * @param Class that is annotated for processing
 */
public class RudderList<T> extends ArrayList<T> implements IRudderList<T>
{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(RudderList.class);
	private int clusterId;
	
	//List of Objects considered to be noise
	private List<Integer> noise;
	
	//List of Objects that have been visited / processed
	private List<Integer> visited;
	
	/**
	 * Each list will allow us to cache the methods and fields
	 * that need to be accessed in order to process a record,
	 * thereby eliminating multiple iterations over an objects
	 * fields and methods.
	 */
	private List<String> setLabelMethods;
	private List<String> numericFeatureFields;
	private List<String> textFeatureFields;
	private List<String> numericLabelFields;
	private List<String> textLabelFields;
	private List<String> numericFeatureMethods;
	private List<String> textFeatureMethods;
	private List<String> numericLabelMethods;
	private List<String> textLabelMethods;

	protected double[] arrayMerge(double[] a1, double[] a2)
	{
		double[] r = new double[a1.length+a2.length];
		System.arraycopy(a1, 0, r, 0, a1.length);
		System.arraycopy(a2, 0, r, a1.length, a2.length);
		return r;
	}

	public int getClusterId()
	{
		return this.clusterId;
	}
	
	protected double getDoubleLabelFromFields(Object o)
	{

		double d = Double.NaN;
		
		Field[] fields = o.getClass().getDeclaredFields();
		for(Field f : fields) {
			Annotation annotation = f.getAnnotation(Label.class);
	
			if (annotation != null) {
				Label label = (Label) annotation;
				try {
					f.setAccessible(true);
					Object fo = f.get(o);
					if(fo instanceof Double)
					{
						if(label.type() == FeatureType.NUMERIC)
						{
							d = 0d;
						}
						d += (Double) fo;
					}
				} catch (IllegalArgumentException e) {
					
					LOG.error(e);
				} catch (IllegalAccessException e) {
					
					LOG.error(e);
				}
			}
		}
		
		return d;
	}
	
	protected List<Object> getFeatureArrayFromFields(Object o)
	{

		List<Object> fieldFeatures = new ArrayList<Object>();
		
		Field[] fields = o.getClass().getDeclaredFields();
		for(Field f : fields) {
			Annotation annotation = f.getAnnotation(NumericFeature.class);
			
			if(annotation == null)
			{
				annotation = f.getAnnotation(TextFeature.class);
			}
			
			if (annotation != null) {
				
				try {
					f.setAccessible(true);
					
					fieldFeatures.add(f.get(o));
				} catch (IllegalArgumentException e) {
					
					LOG.error(e);
				} catch (IllegalAccessException e) {
					
					LOG.error(e);
				}
			}
		}
		
		return fieldFeatures;
	}
	
	protected List<Object> getFeatureArrayFromMethods(Object o)
	{

		List<Object> features = new ArrayList<Object>();
		Method[] methods = o.getClass().getMethods();
		for(Method m : methods)
		{
			Annotation annotation = m.getAnnotation(NumericFeature.class);
			if(annotation == null)
			{
				annotation = m.getAnnotation(TextFeature.class);
			}
			
			if (annotation != null)
			{
				m.setAccessible(true);
				try
				{
					features.add(m.invoke(o));
				}
				catch (NumberFormatException e)
				{
					LOG.error(e);
				}
				catch (IllegalArgumentException e)
				{
					LOG.error(e);
				}
				catch (IllegalAccessException e)
				{
					LOG.error(e);
				}
				catch (InvocationTargetException e)
				{
					LOG.error(e);
				}
			}
		}
		return features;
	}
	
	/**
	 * Added to accomodate getting a data set based on an 
	 * array / list index
	 * @param index of record 
	 * @return double array of numeric features
	 */
	public double[] getItemNumericFeatureArray(int i)
	{
		return this.getNumericFeatureArray(this.get(i));
	}
	
	protected String getLabelFromFields(Object o)
	{

		StringBuffer recordLabels = new StringBuffer();
		
		Field[] fields = o.getClass().getDeclaredFields();
		for(Field f : fields) {
			Annotation annotation = f.getAnnotation(Label.class);
	
			if (annotation != null) {
				Label label = (Label) annotation;
				try {
					if(label.type() == FeatureType.TEXT)
					{
						f.setAccessible(true);
						
						recordLabels.append(f.get(o));
					}
				} catch (IllegalArgumentException e) {
					
					LOG.error(e);
				} catch (IllegalAccessException e) {
					
					LOG.error(e);
				}
			}
		}
		if(recordLabels.length()>0)
		{
			return recordLabels.toString();
		}
		else
		{
			return null;
		}
	}
	
	protected String getLabelFromMethods(Object o)
	{

		StringBuffer methodLabels = new StringBuffer();
		if(this.textLabelMethods == null)
		{
			this.initLabelMethods(o);
			if(this.textLabelMethods == null)
			{
				return null;
			}
		}
		for(String methodName : this.textLabelMethods)
		{
			Method m = null;
			
			try
			{
				m = o.getClass().getMethod(methodName);
			}
			catch (SecurityException e1)
			{
				LOG.error(e1);
			}
			catch (NoSuchMethodException e1)
			{
				LOG.error(e1);
			}
			Annotation annotation = m.getAnnotation(Label.class);
			
			if (annotation != null)
			{
				Label label = (Label) annotation;
				try {
					if(label.type() == FeatureType.TEXT)
					{
						LOG.trace("Text label method: " + m.getName());
						methodLabels.append((m.invoke(o)));
					}
				}
				catch (NumberFormatException e)
				{
					LOG.error(e);
				}
				catch (IllegalArgumentException e)
				{
					LOG.error(e);
				}
				catch (IllegalAccessException e)
				{
					LOG.error(e);
				}
				catch (InvocationTargetException e)
				{
					LOG.error(e);
				}
			}
		}
		if(methodLabels.length()>0)
		{
			return methodLabels.toString();
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Pulls setlabel method for all fields labeled
	 * as a Label
	 * @param Object to find relevant methods for
	 */
	protected void getLabelMethodFromField(Object o)
	{
		
		Field[] fields = o.getClass().getDeclaredFields();
		for(Field f : fields) {
			Annotation annotation = f.getAnnotation(Label.class);
			
			if (annotation != null) {
				try {
					Label label = (Label) annotation;
					//Pull the setlabel value, if not provided, will default to setLabel
					if(this.setLabelMethods == null)
					{
						this.setLabelMethods = new ArrayList<String>();
					}
					this.setLabelMethods.add(label.setlabel());
					
					
				} catch (IllegalArgumentException e) {
					
					LOG.error(e);
				}
			}
		}

	}
	/**
	 * Pulls setlabel method for all methods labeled
	 * as a Label
	 * @param Object to find relevant methods for
	 */
	protected void getLabelMethodFromMethod(Object o)
	{
		Method[] methods = o.getClass().getMethods();
		for(Method m : methods)
		{
			Annotation annotation = m.getAnnotation(Label.class);
			
			if (annotation != null)
			{
				try {
					Label label = (Label) annotation;
					//Pull the setlabel value, if not provided, will default to setLabel
					if(this.setLabelMethods == null)
					{
						this.setLabelMethods = new ArrayList<String>();
					}
					this.setLabelMethods.add(label.setlabel());
				} catch (IllegalArgumentException e) {
					
					LOG.error(e);
				}
			}
		}
	}

	protected List<Integer> getNoise()
	{
		return noise;
	}
	
	/**
	 * Retrieve a numeric feature array for an individual object
	 * 
	 * This object does not NEED to exist in the collection.
	 * 
	 * @param Object to process
	 * @return double array of numeric features
	 */
	public double[] getNumericFeatureArray(Object o)
	{
		//ResizablbleDoubleArray is easier to use than normal double[]
		ResizableDoubleArray arr = new ResizableDoubleArray();
		arr.addElements(this.getNumericFeatureArrayFromFields(o));
		arr.addElements(this.getNumericFeatureArrayFromMethods(o));
		return arr.getElements();
	}
	
	/**
	 * Create a double[] of features only for a specific 
	 * object of the T of the collection from fields
	 * 
	 * @param Object of T for processing
	 * @return double[] of features for a single object
	 */
	protected double[] getNumericFeatureArrayFromFields(Object o)
	{

		ResizableDoubleArray arr = new ResizableDoubleArray();
		
		//Set up the fields if they've not been handled previously
		if(this.numericFeatureFields == null)
		{
			this.initNumericFeatureFields(o);
		}
		
		for(String field : this.numericFeatureFields) {
			Field f = null;
			try
			{
				f = o.getClass().getDeclaredField(field);
				
			}
			catch (SecurityException e1)
			{
				LOG.error(e1);
			}
			catch (NoSuchFieldException e1)
			{
				LOG.debug(Arrays.toString(o.getClass().getDeclaredFields()));
				LOG.error(e1);
			}
			
			try {
				f.setAccessible(true);
				//LOG.debug(f.get(o));
				arr.addElement(Double.parseDouble(f.get(o).toString()));
			} catch (IllegalArgumentException e) {
				
				LOG.error(e);
			} catch (IllegalAccessException e) {
				
				LOG.error(e);
			} catch(StackOverflowError s)
			{

				try
				{
					LOG.error(f.get(o));
				}
				catch (IllegalArgumentException e)
				{
					LOG.error(e);
				}
				catch (IllegalAccessException e)
				{
					LOG.error(e);
				}
			}
			
		}
		
		return arr.getElements();
	}
	
	/**
	 * Create a double[] of features only for a specific 
	 * object of the T of the collection from methods
	 * 
	 * @param Object of T for processing
	 * @return double[] of features for a single object
	 */
	protected double[] getNumericFeatureArrayFromMethods(Object o)
	{
		ResizableDoubleArray arr = new ResizableDoubleArray();
		
		if(this.numericFeatureMethods == null)
		{
			this.initNumericFeatureMethods(o);
		}
		for(String methodName : this.numericFeatureMethods)
		{
			Method m = null;
			try
			{
				m = o.getClass().getMethod(methodName);
			}
			catch (SecurityException e)
			{
				LOG.error(e);
			}
			catch (NoSuchMethodException e)
			{
				LOG.error(e);
			}
			Annotation annotation = m.getAnnotation(NumericFeature.class);
			
			if (annotation != null)
			{
				m.setAccessible(true);
				try
				{
					arr.addElement(Double.parseDouble(m.invoke(o).toString()));
				}
				catch (NumberFormatException e)
				{
					LOG.error(e);
				}
				catch (IllegalArgumentException e)
				{
					LOG.error(e);
				}
				catch (IllegalAccessException e)
				{
					LOG.error(e);
				}
				catch (InvocationTargetException e)
				{
					LOG.error(e);
				}
			}
		}

		return arr.getElements();

	}

	protected List<String> getNumericFeatureFields()
	{
		return numericFeatureFields;
	}
	
	protected List<String> getNumericFeatureMethods()
	{
		return numericFeatureMethods;
	}
	
	protected List<String> getNumericLabelFields()
	{
		return numericLabelFields;
	}
	
	protected List<String> getNumericLabelMethods()
	{
		return numericLabelMethods;
	}
	
	/**
	 * 
	 * @param Object to have label returned
	 * @return double of label(s)
	 */
	private double getNumericLabels(Object o)
	{
		return this.getNumericLabelsForRecord(o)[0];
	}
	public double[] getNumericLabelsForRecord(Object o)
	{
		
		ResizableDoubleArray arr = new ResizableDoubleArray();
		
		double[] fieldLabels = this.getNumericLabelsFromField(o);
		if (fieldLabels != null)
		{
			
			arr.addElements(fieldLabels);
		}
		
		double[] methodLabels = this.getNumericLabelsFromMethod(o);
		
		if(methodLabels != null)
		{
			
			arr.addElements(methodLabels);
		}
		
		return arr.getElements();
	}
	
	public double[] getNumericLabelsFromField(Object o)
	{
		LOG.trace("Retrieving double[] for field based labels");
		if(this.numericLabelFields == null)
		{
			this.initNumericLabelFields();
			
			if(this.numericLabelFields == null || this.numericLabelFields.size() == 0)
			{
				LOG.trace("No field based labels found");
				return null;
			}
		}
		
		ResizableDoubleArray arr = new ResizableDoubleArray();
		
		for(String field : this.numericLabelFields) {
			Field f = null;
			try
			{
				f = o.getClass().getDeclaredField(field);
				
			}
			catch (SecurityException e1)
			{
				LOG.error(e1);
			}
			catch (NoSuchFieldException e1)
			{
				LOG.debug(Arrays.toString(o.getClass().getDeclaredFields()));
				LOG.error(e1);
			}
			
			try {
				f.setAccessible(true);
				if(!(f.get(o) instanceof String) && (f.get(o) != null))
				{
					arr.addElement(Double.parseDouble(f.get(o).toString()));
				}
			} catch (IllegalArgumentException e) {
				
				LOG.error(e);
			} catch (IllegalAccessException e) {
				
				LOG.error(e);
			}
			
		}
		return arr.getElements();
	}
	
	public double[] getNumericLabelsFromMethod(Object o)
	{
		LOG.trace("Retrieving double[] for method based labels");
		if(this.numericLabelMethods == null)
		{
			this.initNumericLabelMethods();
			
			if(this.numericLabelMethods == null || this.numericLabelMethods.size() == 0)
			{
				LOG.trace("No numeric label methods found.");
				return null;
			}
		}
		ResizableDoubleArray arr = new ResizableDoubleArray();
		
		for(String methodName : this.numericLabelMethods) {
			LOG.trace("Entering " + methodName);
			Method m = null;
			try
			{
				LOG.trace("Retreiving data from " + methodName);
				m = o.getClass().getDeclaredMethod(methodName);
				
			}
			catch (SecurityException e1)
			{
				LOG.error(e1);
			}
			catch (NoSuchMethodException e)
			{
				LOG.error(e);
			}
			
			
			try {
				
				arr.addElement((Double)m.invoke(o));
			} catch (IllegalArgumentException e) {
				
				LOG.error(e);
			} catch (IllegalAccessException e) {
				
				LOG.error(e);
			}
			catch (InvocationTargetException e)
			{
				LOG.error(e);
			}
			
		}
		return arr.getElements();
	}
	
	public List<Object> getRecordFeatures(Object r)
	{
		List<Object> features = new ArrayList<Object>();
		
		features.addAll(this.getFeatureArrayFromFields(r));
		features.addAll(this.getFeatureArrayFromMethods(r));
		return features;
	}
	
	protected List<String> getSetLabelMethods()
	{
		return setLabelMethods;
	}
	
	/**
	 * @param Object to get label
	 */
	public String getStringLabel(Object r)
	{
		StringBuffer sb = new StringBuffer();
		String fs = this.getStringLabelFromField(r);
		if(fs != null)
		{
			sb.append(fs);
		}
		
		String ms = this.getStringLabelFromMethod(r);
		if(ms != null)
		{
			sb.append(ms);
		}
		LOG.debug("StringLabel: " + sb);
		return sb.toString().toUpperCase();
	}
	
	private String getStringLabelFromMethod(Object r)
	{
		StringBuffer sb = new StringBuffer();
		
		if(this.textLabelMethods == null)
		{
			this.initTextLabelMethods(r);
		}
		
		for(String methodName : this.textLabelMethods)
		{
			Method m = null;
			
			try
			{
				m = r.getClass().getMethod(methodName);
				sb.append(m.invoke(r));
			}
			catch (SecurityException e)
			{
				LOG.error(e);
			}
			catch (NoSuchMethodException e)
			{
				LOG.error(e);
			}
			catch (IllegalArgumentException e)
			{
				LOG.error(e);
			}
			catch (IllegalAccessException e)
			{
				LOG.error(e);
			}
			catch (InvocationTargetException e)
			{
				LOG.error(e);
			}
			
		}
		
		return sb.toString();
	}

	private void initTextLabelMethods(Object r)
	{
		if(this.textLabelMethods == null)
		{
			LOG.debug("Initializaing text label methods");
			this.textLabelMethods = new ArrayList<String>();
		}
		Method[] methods = this.get(0).getClass().getDeclaredMethods();
		for(Method m : methods)
		{
			Annotation annotation = m.getAnnotation(Label.class);
			
			if (annotation != null)
			{
				Label label = (Label) annotation;
				if(label.type() == FeatureType.TEXT)
				{
					
					this.textLabelMethods.add(m.getName());
				}
				
			}
		}
		
	}

	private String getStringLabelFromField(Object r)
	{
		StringBuffer sb = new StringBuffer();
		if(this.textLabelFields == null)
		{
			LOG.debug("Initializaing text label fields");
			this.initTextLabelFields(r);
		}
		
		for(String fieldName : this.textLabelFields)
		{
			Field f = null;
			
			try
			{
				f = r.getClass().getDeclaredField(fieldName);
				f.setAccessible(true);
				sb.append(f.get(r));
			}
			catch (SecurityException e)
			{
				LOG.error(e);
			}
			catch (NoSuchFieldException e)
			{
				LOG.error(e);
			}
			catch (IllegalArgumentException e)
			{
				LOG.error(e);
			}
			catch (IllegalAccessException e)
			{
				LOG.error(e);
			}
			
		}
		return sb.toString();
	}

	private void initTextLabelFields(Object r)
	{
		Field[] fields = this.get(0).getClass().getDeclaredFields();
		for(Field f : fields) {
			Annotation annotation = f.getAnnotation(Label.class);
			
			if (annotation != null) {
				LOG.debug("Has label annotation");
				try {	
					if(this.textLabelFields == null)
					{
						this.textLabelFields = new ArrayList<String>();
					}
					Label label = (Label) annotation;
					if(label.type() == FeatureType.TEXT)
					{
						LOG.trace("Label found: " + f.getName());
						this.textLabelFields.add(f.getName());
					}
					else
					{
						LOG.trace("Non numeric field: " + f.getName());
					}
					
				} catch (IllegalArgumentException e) {
					
					LOG.error(e);
				}
			}
		}
		
	}

	/**
	 * Returns a matrix of numeric features only for each
	 * object in the collection.
	 * 
	 * @return double[][] of features for supervised learning
	 */
	public double[][] getSupervisedFeatures()
	{
		double[][] featureArray = new double[this.size()][];
		int idx = 0;
		for(Object o : this)
		{
			double[] dArr = this.getNumericFeatureArray(o);
			featureArray[idx] = dArr;
			idx++;
		}
		
		return featureArray;
		
	}
	
	/**
	 * Supervised learning (until Neural Networks are implemented at least), require a single 
	 * dimensional set of data as the 'y' values.
	 * 
	 * @return one dimensional double array of numeric labels / object values
	 */
	public double[] getSupervisedLabels()
	{
		if(this.numericLabelFields == null)
		{
			this.initNumericLabelFields();
			
		}
		if(this.numericLabelMethods == null)
		{
			this.initNumericLabelMethods();
		}
		
		double[] dArr = new double[this.size()];
		int idx = 0;
		for(Object o : this)
		{
			dArr[idx] = this.getNumericLabels(o);
			idx++;
		}
		
		return dArr;
	}
	
	public double[] getSupervisedSampleDoubleArray()
	{
		return this.getItemNumericFeatureArray(0);
	}

	
	protected List<String> getTextFeatureFields()
	{
		return textFeatureFields;
	}

	protected List<String> getTextFeatureMethods()
	{
		return textFeatureMethods;
	}

	protected List<String> getTextLabelFields()
	{
		return textLabelFields;
	}
	protected List<String> getTextLabelMethods()
	{
		return textLabelMethods;
	}
	
	public double[] getUnsupervisedDoubleArray(Object o)
	{
		double[] labels = this.getNumericLabelsForRecord(o);
		double[] features = this.getNumericFeatureArray(o);

		if(labels != null && features != null)
		{
			return this.arrayMerge(labels, features);
		}
		
		if(labels == null)
		{
			return features;
		}
		
		if(features == null)
		{
			return labels;
		}
		return null;
	}

	/**
	 * Returns a matrix of all fields and labels for all objects 
	 * in the collection
	 */
	public double[][] getUnsupervisedDoubleDoubleArray()
	{
		double[][] dArr = new double[this.size()][];
		
		int idx = 0;
		for(Object o : this)
		{
			LOG.trace("Processing: " + o.toString());
			dArr[idx] = this.getUnsupervisedDoubleArray(o);
			idx++;
		}
		return dArr;
	}
	public double[] getUnsupervisedSampleDoubleArray()
	{
		return this.getUnsupervisedDoubleArray(this.get(0));
	}

	protected List<Integer> getVisited()
	{
		return visited;
	}

	/**
	 * Caches setLabelMethods, grabs both
	 * field and method defined method names
	 * @param Object to find relevant methods for
	 */
	protected void initLabelMethods(Object r)
	{
		
		this.getLabelMethodFromField(r);
		this.getLabelMethodFromMethod(r);
		
	}

	/**
	 * Caches the numeric feature field names for 
	 * later use.
	 * 
	 * @param Object to get field names from
	 */
	protected void initNumericFeatureFields(Object o)
	{
		
		Field[] fields = o.getClass().getDeclaredFields();
		for(Field f : fields) {
			Annotation annotation = f.getAnnotation(NumericFeature.class);
			
			if (annotation != null) {
				try {	
					if(this.numericFeatureFields == null)
					{
						this.numericFeatureFields = new ArrayList<String>();
					}
					this.numericFeatureFields.add(f.getName());
				} catch (IllegalArgumentException e) {
					
					LOG.error(e);
				}
			}
		}
	}

	protected void initNumericFeatureMethods(Object o)
	{
		if(this.numericFeatureMethods == null)
		{
			this.numericFeatureMethods = new ArrayList<String>();
		}
		
		Method[] methods = o.getClass().getMethods();
		for(Method m : methods)
		{
			Annotation annotation = m.getAnnotation(NumericFeature.class);
			
			if (annotation != null)
			{
				
				this.numericFeatureMethods.add(m.getName());
			}
		}
	}

	protected void initNumericLabelFields()
	{
		Field[] fields = this.get(0).getClass().getDeclaredFields();
		for(Field f : fields) {
			
			Annotation annotation = f.getAnnotation(Label.class);
			
			if (annotation != null) {
				
				try {	
					if(this.numericLabelFields == null)
					{
						this.numericLabelFields = new ArrayList<String>();
					}
					Label label = (Label) annotation;
					if(label.type() == FeatureType.NUMERIC)
					{
						LOG.trace("Label found: " + f.getName());
						this.numericLabelFields.add(f.getName());
					}
					else
					{
						LOG.trace("Non numeric field: " + f.getName());
					}
					
				} catch (IllegalArgumentException e) {
					
					LOG.error(e);
				}
			}
		}
	}

	protected void initNumericLabelMethods()
	{
		if(this.numericLabelMethods == null)
		{
			this.numericLabelMethods = new ArrayList<String>();
		}
		Method[] methods = this.get(0).getClass().getMethods();
		for(Method m : methods)
		{
			Annotation annotation = m.getAnnotation(Label.class);
			
			if (annotation != null)
			{
				Label label = (Label) annotation;
				if(label.type() == FeatureType.NUMERIC)
				{
					
					this.numericLabelMethods.add(m.getName());
				}
				
			}
		}
	}

	public boolean isNoise(Object o)
	{
		int idx = this.indexOf(o);
		if(idx>0)
		{
			return this.noise.contains((Integer)idx);
		}
		return false;
		
	}

	public boolean isVisited(Object o)
	{
		int idx = this.indexOf(o);
		if(idx>0)
		{
			return this.visited.contains((Integer)idx);
		}
		return false;
	}

	public void setClusterId(int id)
	{
		this.clusterId = id;
		
	}

	/**
	 * Applies a label to an object
	 * @param Object to apply label to
	 * @param Value of the label
	 * @return success of label application
	 */
	public boolean setLabel(Object r, Object value)
	{
		//Initialize the label methods if they are not all ready set
		if(this.setLabelMethods == null)
		{
			this.setLabelMethods = new ArrayList<String>();
			this.initLabelMethods(r);
		}
		
		for(String methodName : this.setLabelMethods)
		{
			try
			{
				/**
				 * Attempts to get the setLabel method with an argument that 
				 * matches the passed in value
				 */
				try{
					Method m = r.getClass().getMethod(methodName, value.getClass());
					
					m.invoke(r, value);
					
				}
				catch (NoSuchMethodException e)
				{
					LOG.trace(
						methodName 
						+ " does not exist with signature (" 
						+ value.getClass().getName() 
						+")"
						);
				}
			}
			catch (SecurityException e)
			{
				LOG.error(e);
				return false;
			}
			
			catch (IllegalArgumentException e)
			{
				LOG.error(e);
				return false;
			}
			catch (IllegalAccessException e)
			{
				LOG.error(e);
				return false;
			}
			catch (InvocationTargetException e)
			{
				LOG.error(e);
				return false;
			}
		}
		return true;
	}

	public void setNoise(Object o, boolean b)
	{
		if(this.noise == null)
		{
			this.noise = new ArrayList<Integer>();
		}
		int idx = this.indexOf(o);
		if(idx>0)
		{
			if(b)	
			{
				this.noise.add(idx);
			}
			else
			{
				if(this.noise.contains(idx))
				{
					this.noise.remove((Integer)idx);
				}
			}
		}
		
	}

	public void setVisited(Object o, boolean b)
	{
		int idx = this.indexOf(o);
		if(idx>0)
		{
			if(b)	
			{
				this.visited.add(idx);
			}
			else
			{
				if(this.visited.contains(idx))
				{
					this.visited.remove((Integer)idx);
				}
			}
		}
		
	}
}
