package me.mcnelis.ml.rudder.util;

import java.io.Serializable;
import java.util.HashMap;

import org.apache.commons.math.util.ResizableDoubleArray;

public interface ConvertToFeatures extends Serializable {
	ResizableDoubleArray run();
	HashMap<String, Integer> getCardinalities();
}
