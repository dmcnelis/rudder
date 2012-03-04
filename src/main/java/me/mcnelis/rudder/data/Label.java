package me.mcnelis.rudder.data;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** denotes a label for a class feature in a class
 * @author dmcnelis
 *
 */
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Label {
	String setlabel() default "setLabel";
	FeatureType type() default FeatureType.NUMERIC;
}
