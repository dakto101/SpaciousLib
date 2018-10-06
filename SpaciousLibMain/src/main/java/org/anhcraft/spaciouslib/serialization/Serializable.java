package org.anhcraft.spaciouslib.serialization;

import java.lang.annotation.*;

@Inherited
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
/**
 * This is the annotation for defining serializable classes
 */
public @interface Serializable {

}
