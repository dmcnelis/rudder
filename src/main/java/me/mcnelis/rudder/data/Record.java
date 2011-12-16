package me.mcnelis.rudder.data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;

import me.mcnelis.rudder.exceptions.FeatureNotFoundException;

import org.apache.commons.math.util.MathUtils;
import org.apache.commons.math.util.ResizableDoubleArray;

/**
 * Base class that records in a dataset should extend.
 * 
 * This provides all the necessary functions to, assuming the 
 * features and  labels are properly annotated fields, to
 * perform any of the machine learning tasks included in 
 * this package.
 * 
 * 
 * @author dmcnelis
 *
 */
public abstract class Record {
	
	protected HashSet<RecordFlags> flags;
	
	/**
	 * Set a feature variable for a class, currently all
	 * features must be double
	 * @param featureName
	 * @param d
	 * @return
	 * @throws FeatureNotFoundException
	 */
	public boolean setFeature(String featureName, double d) 
			throws FeatureNotFoundException {
		
		Field f = null;
		try {
			f = this.getClass().getDeclaredField(featureName);
		} catch (SecurityException e) {
			throw new FeatureNotFoundException("Security exception");
		} catch (NoSuchFieldException e) {
			throw new FeatureNotFoundException(featureName + " is not a class member");
		}
		Annotation annotation = f.getAnnotation(Feature.class);
		if(annotation == null)
			throw new FeatureNotFoundException("There is no annotation here");

		if(annotation instanceof Feature) {
			try {
				f.setDouble(this, d);
			} catch (IllegalArgumentException e) {
				throw new FeatureNotFoundException("Illegal argument");
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				throw new FeatureNotFoundException("Illegal access");
			}
			return true;
		}			
		
		
		throw new FeatureNotFoundException("Field is not a feature");
	}
	
	public boolean setLabel(String labelName, Object o) throws Exception {
		
		Field f = null;
		try {
			f = this.getClass().getDeclaredField(labelName);
		} catch (SecurityException e) {
			throw e;
		} catch (NoSuchFieldException e) {
			throw e;
		}
		Annotation annotation = f.getAnnotation(Label.class);
		if(annotation == null)
			throw new FeatureNotFoundException("There is no annotation here");
		
		if(annotation instanceof Label) {
			
			
					f.set(this, o);
				
			
			return true;
		}			
		return false;
	}

	@Override
	public String toString() {
		
		Field[] fields = this.getClass().getDeclaredFields();
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getName());
		sb.append(":\n");
		for(Field f : fields) {
			Annotation[] as = f.getAnnotations();
			for (Annotation a : as) {
				sb.append(a.toString());
				sb.append(" ");
			}
			sb.append(f.getName());
			sb.append(": ");
			try {
				sb.append(f.get(this));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			sb.append("\n");
		}
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * 
	 * @return double array of features for processing
	 */
	public double[] getFeatureArray() {
		ResizableDoubleArray arr = new ResizableDoubleArray();
		
		Field[] fields = this.getClass().getDeclaredFields();
		for(Field f : fields) {
			Annotation annotation = f.getAnnotation(Feature.class);
			if (annotation != null) {
				try {
					arr.addElement(f.getDouble(this));
				} catch (IllegalArgumentException e) {
					
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					
					e.printStackTrace();
				}
			}
		}
		
		return arr.getElements();
	}
	
	/**
	 * In unsupervised learning the order of your features is irrelevant
	 * so it doesn't matter what you're going through, as long as the label
	 * is a number.  
	 * 
	 * If your label is a string, you should handle labeling your data in a little
	 * different manner (i.e. give your label a double value type and have your
	 * label be another class member.  When you set that class member, it updates
	 * the label).  This is irrelevant if you're not planning on doing any 
	 * unsupervised learning on this  dataset
	 * 
	 * @return array of all your feature and labels  for unsupervised learning
	 */
	public double[] getFeatureAndLabelArray() {
		ResizableDoubleArray arr = new ResizableDoubleArray();
		
		Field[] fields = this.getClass().getDeclaredFields();
		for(Field f : fields) {
			Annotation annotation = f.getAnnotation(Feature.class);
			if(annotation == null)
				annotation = f.getAnnotation(Label.class);
			if (annotation != null) {
				try {
						arr.addElement(f.getDouble(this));
				} catch (IllegalArgumentException e) {
					//Field or label isn't a double, so ignore it
				} catch (IllegalAccessException e) {
					
					e.printStackTrace();
				}
			}
		}
		
		return arr.getElements();
	}
	
	public double getDoubleLabel() {
		
		Field[] fields = this.getClass().getDeclaredFields();
		for(Field f : fields) {
			Annotation annotation = f.getAnnotation(Label.class);
			
			if (annotation != null) {
				try {
					return (f.getDouble(this));
				} catch (IllegalArgumentException e) {
					//Not a double, so just move forward
				} catch (IllegalAccessException e) {
					
					e.printStackTrace();
				}
			}
		}
		return Double.NaN;
	}
	
	public boolean isNoise() {
		if(this.flags == null)
			return false;
		else {
			if(this.flags.contains(RecordFlags.NOISE))
				return true;
			else
				return false;
		}
	}
	
	public void setNoise(boolean noise) {
		if(this.flags == null)
			this.flags = new HashSet<RecordFlags>();
		if(noise)
			this.flags.add(RecordFlags.NOISE);
		else if (!noise)
			this.flags.remove(RecordFlags.NOISE);
	}
	
	public void setIsNoise() {
		if(this.flags == null)
			this.flags = new HashSet<RecordFlags>();

		this.flags.add(RecordFlags.NOISE);

	}
	
	public void setNotNoise() {
		if(this.flags == null)
			this.flags = new HashSet<RecordFlags>();

		this.flags.remove(RecordFlags.NOISE);

	}
	
	public boolean isVisited() {
		if(this.flags == null)
			return false;
		else {
			if(this.flags.contains(RecordFlags.VISITED))
				return true;
			else
				return false;
		}
	}

	public void setVisited(boolean visited) {
		if(this.flags == null)
			this.flags = new HashSet<RecordFlags>();
		if(visited)
			this.flags.add(RecordFlags.VISITED);
		else if (!visited)
			this.flags.remove(RecordFlags.VISITED);
	}
	
	public void setNotVisited() {
		if(this.flags == null)
			this.flags = new HashSet<RecordFlags>();
		if(this.flags.contains(RecordFlags.VISITED))
			this.flags.remove(RecordFlags.VISITED);
	}
	
	public void setVisited() {
		if(this.flags == null)
			this.flags = new HashSet<RecordFlags>();
		
		this.flags.add(RecordFlags.VISITED);
	}
	
	public void setAssigned(boolean visited) {
		if(this.flags == null)
			this.flags = new HashSet<RecordFlags>();
		if(visited)
			this.flags.add(RecordFlags.ASSIGNED);
		else if (!visited)
			this.flags.remove(RecordFlags.ASSIGNED);
	}
	
	public void setNotAssigned() {
		if(this.flags == null)
			this.flags = new HashSet<RecordFlags>();
		if(this.flags.contains(RecordFlags.ASSIGNED))
			this.flags.remove(RecordFlags.ASSIGNED);
	}
	
	public void setAssigned() {
		if(this.flags == null)
			this.flags = new HashSet<RecordFlags>();
		
		this.flags.add(RecordFlags.ASSIGNED);
	}
	
	public boolean isAssigned() {
		if(this.flags.contains(RecordFlags.ASSIGNED))
			return true;
		else
			return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((flags == null) ? 0 : flags.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Record other = (Record) obj;
		double[] orig = this.getFeatureAndLabelArray();
		double[] test = other.getFeatureAndLabelArray();
		return MathUtils.equals(orig, test);
	}
}
