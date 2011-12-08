package me.mcnelis.ml.rudder.util;

import java.io.Serializable;
import java.util.HashMap;

import me.mcnelis.ml.rudder.exceptions.ConvertToFeaturesException;

import com.google.common.collect.BiMap;

public interface ConvertToFeatures extends Serializable {
	HashMap<String, Double> run();
	HashMap<String, Integer> getCardinalities();
	boolean setData(Object data) throws ConvertToFeaturesException;
}
