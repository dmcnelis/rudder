package me.mcnelis.ml.rudder;

import java.io.Serializable;

import me.mcnelis.rudder.features.FeatureList;

public abstract class Record implements Serializable, Cloneable, Comparable<Record>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected Object key;
	protected FeatureList features;
	/**
	 * @return the key
	 */
	public Object getKey() {
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(Object key) {
		this.key = key;
	}
	/**
	 * @return the features
	 */
	public FeatureList getFeatures() {
		return features;
	}
	
	/**
	 * @param features the features to set
	 */
	public void setFeatures(FeatureList features) {
		this.features = features;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((features == null) ? 0 : features.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
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
		if (!(obj instanceof Record)) {
			return false;
		}
		Record other = (Record) obj;
		if (features == null) {
			if (other.features != null) {
				return false;
			}
		} else if (!features.equals(other.features)) {
			return false;
		}
		if (key == null) {
			if (other.key != null) {
				return false;
			}
		} else if (!key.equals(other.key)) {
			return false;
		}

		return true;
	}
	
	
	
}
