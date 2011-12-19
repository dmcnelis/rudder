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
public abstract class Record implements RecordInterface {
	
	protected HashSet<RecordFlags> flags;
	
	/* (non-Javadoc)
	 * @see me.mcnelis.rudder.data.RecordInterface#setFeature(java.lang.String, double)
	 */
	public synchronized boolean setFeature(String featureName, double d) 
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
	
	/* (non-Javadoc)
	 * @see me.mcnelis.rudder.data.RecordInterface#setLabel(java.lang.String, java.lang.Object)
	 */
	public synchronized boolean setLabel(String labelName, Object o) throws Exception {
		
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

	/* (non-Javadoc)
	 * @see me.mcnelis.rudder.data.RecordInterface#toString()
	 */
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
	
	/* (non-Javadoc)
	 * @see me.mcnelis.rudder.data.RecordInterface#getFeatureArray()
	 */
	public synchronized double[] getFeatureArray() {
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
	
	/* (non-Javadoc)
	 * @see me.mcnelis.rudder.data.RecordInterface#getFeatureAndLabelArray()
	 */
	public synchronized double[] getFeatureAndLabelArray() {
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
	
	/* (non-Javadoc)
	 * @see me.mcnelis.rudder.data.RecordInterface#getDoubleLabel()
	 */
	public synchronized double getDoubleLabel() {
		
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
	
	/* (non-Javadoc)
	 * @see me.mcnelis.rudder.data.RecordInterface#isNoise()
	 */
	public synchronized boolean isNoise() {
		if(this.flags == null)
			return false;
		else {
			if(this.flags.contains(RecordFlags.NOISE))
				return true;
			else
				return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see me.mcnelis.rudder.data.RecordInterface#setNoise(boolean)
	 */
	public synchronized void setNoise(boolean noise) {
		if(this.flags == null)
			this.flags = new HashSet<RecordFlags>();
		if(noise)
			this.flags.add(RecordFlags.NOISE);
		else if (!noise)
			this.flags.remove(RecordFlags.NOISE);
	}
	
	/* (non-Javadoc)
	 * @see me.mcnelis.rudder.data.RecordInterface#isVisited()
	 */
	public synchronized boolean isVisited() {
		if(this.flags == null)
			return false;
		else {
			if(this.flags.contains(RecordFlags.VISITED))
				return true;
			else
				return false;
		}
	}

	/* (non-Javadoc)
	 * @see me.mcnelis.rudder.data.RecordInterface#setVisited(boolean)
	 */
	public synchronized void setVisited(boolean visited) {
		if(this.flags == null)
			this.flags = new HashSet<RecordFlags>();
		if(visited)
			this.flags.add(RecordFlags.VISITED);
		else if (!visited)
			this.flags.remove(RecordFlags.VISITED);
	}
	
	/* (non-Javadoc)
	 * @see me.mcnelis.rudder.data.RecordInterface#setAssigned(boolean)
	 */
	public synchronized void setAssigned(boolean visited) {
		if(this.flags == null)
			this.flags = new HashSet<RecordFlags>();
		if(visited)
			this.flags.add(RecordFlags.ASSIGNED);
		else if (!visited)
			this.flags.remove(RecordFlags.ASSIGNED);
	}	
	
	/* (non-Javadoc)
	 * @see me.mcnelis.rudder.data.RecordInterface#isAssigned()
	 */
	public synchronized boolean isAssigned() {
		if(this.flags.contains(RecordFlags.ASSIGNED))
			return true;
		else
			return false;
	}

	/* (non-Javadoc)
	 * @see me.mcnelis.rudder.data.RecordInterface#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((flags == null) ? 0 : flags.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see me.mcnelis.rudder.data.RecordInterface#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RecordInterface other = (RecordInterface) obj;
		double[] orig = this.getFeatureAndLabelArray();
		double[] test = other.getFeatureAndLabelArray();
		return MathUtils.equals(orig, test);
	}
}
