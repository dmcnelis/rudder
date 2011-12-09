package me.mcnelis.ml.rudder.util;

import java.io.Serializable;
import java.util.HashMap;

import me.mcnelis.ml.rudder.exceptions.ConvertToFeaturesException;
import me.mcnelis.rudder.features.FeatureList;

import com.google.common.collect.BiMap;

public interface ConvertToFeatures extends Serializable {
	FeatureList run();
	HashMap<String, Integer> getCardinalities();
	boolean setData(Object data) throws ConvertToFeaturesException;
}
