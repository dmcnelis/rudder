package me.mcnelis.rudder.features;

import java.math.BigDecimal;

public class NumericFeature extends Feature {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NumericFeature(String featureName, Object featureValue) {
		super(featureName, featureValue);
	}

	public NumericFeature() {
		super();
	}
	@Override
	public Double getFeatureValue() {
		if (this.featureValue instanceof String)
			return Double.valueOf( (String) this.featureValue);
		else if (this.featureValue instanceof Integer) 
			return Double.valueOf((Integer)this.featureValue);
		else if (this.featureValue instanceof BigDecimal)
			return ((BigDecimal) this.featureValue).doubleValue();
		else if(this.featureValue instanceof Double)
			return (Double) this.featureValue;
		else
			return Double.NaN;
	}

}
