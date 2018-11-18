package org.anhcraft.spaciouslib.annotations;

import java.lang.annotation.*;

/**
 * Any method have this annotation will be called when a packet is sent or is received.<br>
 * Any object which is using this annotation must be registered with AnnotationHandler in order to work.
 */
@Inherited
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PacketHandler {
}
