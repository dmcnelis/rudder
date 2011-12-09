package me.mcnelis.rudder.util;

import java.io.Serializable;
import java.util.HashMap;

import me.mcnelis.rudder.exceptions.ConvertToFeaturesException;
import me.mcnelis.rudder.features.FeatureList;

import com.google.common.collect.BiMap;

public interface ConvertToFeatures extends Serializable {
	FeatureList run();
	HashMap<String, Integer> getCardinalities();
	boolean setData(Object data) throws ConvertToFeaturesException;
}
