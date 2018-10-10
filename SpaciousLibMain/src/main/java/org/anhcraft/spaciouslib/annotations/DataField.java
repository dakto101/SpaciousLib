package org.anhcraft.spaciouslib.annotations;

import java.lang.annotation.*;

/**
 * DataField is the annotation for serializable declared fields in objects
 * This annotation don't need to be registered with AnnotationHandler
 */
@Inherited
@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataField {

}
