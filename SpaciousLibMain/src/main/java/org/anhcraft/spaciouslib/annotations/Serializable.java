package org.anhcraft.spaciouslib.annotations;

import java.lang.annotation.*;

@Inherited
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
/**
 * This is the annotation for defining serializable classes
 * This annotation don't need to be registered with AnnotationHandler
 */
public @interface Serializable {

}
