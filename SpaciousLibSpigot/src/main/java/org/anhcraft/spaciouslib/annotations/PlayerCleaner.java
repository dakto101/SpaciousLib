package org.anhcraft.spaciouslib.annotations;

import java.lang.annotation.*;

/**
 * Automatically remove any object related to offline players, e.g: UUID, Player object, etc<br>
 * Any object which is using this annotation must be registered with AnnotationHandler in order to work.
 */
@Inherited
@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PlayerCleaner {
}