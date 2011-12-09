package me.mcnelis.rudder.features;


/**
 * Binary feature object
 * @author dmcnelis
 *
 */
public class BinaryFeature extends Feature {
	
	/**
	 * Serial id
	 */
	private static final long serialVersionUID = 1L;
	protected Boolean featureValue;
	
	public BinaryFeature() {
		super();
	}
	public BinaryFeature(String featureName, Boolean featureValue) {
		super();
		this.featureName = featureName;
		this.featureValue = featureValue;
	}


	public String getFeatureName() {
		return featureName;
	}


	public boolean isFeatureValue() {
		return featureValue;
	}


	@Override
	public Double getFeatureValue() {
		
		return this.isFeatureValue() ? (Double) 0d : 1d;
		
	}
}
