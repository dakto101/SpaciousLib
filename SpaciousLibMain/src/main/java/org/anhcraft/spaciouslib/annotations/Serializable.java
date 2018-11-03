package org.anhcraft.spaciouslib.annotations;

import java.lang.annotation.*;

/**
 * This is the annotation for defining serializable classes.<br>
 * This annotation don't need to be registered with AnnotationHandler
 */
@Inherited
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Serializable {

}
