package me.mcnelis.rudder.features;

import java.io.Serializable;

/**
 * All different feature types should extend this class and implement
 * the getFeatureValue method.
 * 
 * @author dmcnelis
 *
 */
public abstract class Feature 
	implements Serializable, Cloneable, Comparable<Feature> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String featureName;
	protected Object featureValue;
	
	public Feature() {
		super();
	}
	
	public Feature(String featureName) {
		super();
		this.featureName = featureName;
		this.featureValue = 0;
	}
	
	public Feature(String featureName, Object featureValue) {
		super();
		this.featureName = featureName;
		this.featureValue = featureValue;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}
	
	public String getFeatureName() {
		
		return this.featureName;
	}

	/**
	 * 
	 * @return a double representation of this feature
	 * 
	 */
	public abstract Double getFeatureValue();

	public void setFeatureValue(Object featureValue) {
		this.featureValue = featureValue;
	}

	@Override
	public BinaryFeature clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (BinaryFeature) super.clone();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.featureName + ": " + this.featureValue;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((featureName == null) ? 0 : featureName.hashCode());
		result = prime * result
				+ ((featureValue == null) ? 0 : featureValue.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Feature)) {
			return false;
		}
		Feature other = (Feature) obj;
		if (featureName == null) {
			if (other.featureName != null) {
				return false;
			}
		} else if (!featureName.equals(other.featureName)) {
			return false;
		}
		if (featureValue == null) {
			if (other.featureValue != null) {
				return false;
			}
		} else if (!featureValue.equals(other.featureValue)) {
			return false;
		}
		return true;
	}
	
	public int compareTo(Feature f) {
		return this.featureName.compareTo(f.getFeatureName());
	}

}