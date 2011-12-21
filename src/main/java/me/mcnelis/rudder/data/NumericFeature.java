package me.mcnelis.rudder.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes a numeric feature in a class
 * @author dmcnelis
 *
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD })
public @interface NumericFeature {

}
