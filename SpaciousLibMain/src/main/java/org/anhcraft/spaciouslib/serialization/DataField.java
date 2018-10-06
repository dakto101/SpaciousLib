package org.anhcraft.spaciouslib.serialization;

import java.lang.annotation.*;

/**
 * DataField is the annotation for serializable declared fields in objects
 */
@Inherited
@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataField {

}
