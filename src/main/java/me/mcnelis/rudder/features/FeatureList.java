package me.mcnelis.rudder.features;

import java.util.ArrayList;
import java.util.Collections;

public class FeatureList extends ArrayList<Feature> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<String> features = new ArrayList<String>();
	
	public synchronized double[] getFeatureArray() {
		double[] arr = new double[this.size()];
		
		int idx = 0;
		for (Feature f : this) {
			arr[idx] = f.getFeatureValue();
			idx++;
		}
		
		return arr;
	}

	public synchronized boolean add(Feature f) {
		int fIdx = this.getFeatureIndex(f.getFeatureName());
		if(fIdx > -1) {
			this.get(fIdx).setFeatureValue(f.getFeatureValue());
		} else if (!this.contains(f)) {
			super.add(f);
			features.add(f.getFeatureName());
		}
		Collections.sort(this);
		return true;
	}
	
	public synchronized int getFeatureIndex(String featureName) {
		for(Feature feature : this) {

			if(feature.getFeatureName().equals(featureName)) {
				return this.indexOf(feature);

			}
		}
		return -1;
	}
	
	public synchronized boolean containsFeature(Feature f) {
		for(Feature feature : this) {
			if(f.getFeatureName().equals(feature.getFeatureName()))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((features == null) ? 0 : features.hashCode());
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
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof FeatureList)) {
			return false;
		}
		FeatureList other = (FeatureList) obj;
		if (features == null) {
			if (other.features != null) {
				return false;
			}
		} else {
			System.out.println("in featurelist equals");
			for(Feature f : this) {
				int idx = other.getFeatureIndex(f.getFeatureName());
				
				if(idx < -1 || !f.equals(other.get(idx)))
					return false;
			}
		}
		return true;
	}
}
