package org.anhcraft.spaciouslib.annotations;

import java.lang.annotation.*;

/**
 * Getting the value of a configuration option.<br>
 * Any object which is using this annotation must be registered with AnnotationHandler in order to work.<br>
 * Load or reload a configuration by using method SpaciousAnnotation#reloadConfig, all registered objects will apply changes
 */
@Inherited
@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigOption {
    String path();
    String file();
}