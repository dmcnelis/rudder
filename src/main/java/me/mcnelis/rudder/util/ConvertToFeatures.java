package me.mcnelis.rudder.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import me.mcnelis.rudder.exceptions.ConvertToFeaturesException;

public interface ConvertToFeatures extends Serializable {
	ArrayList<Double> run();
	HashMap<String, Integer> getCardinalities();
	boolean setData(Object data) throws ConvertToFeaturesException;
}
