package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.ClassFinder;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;

public class EntityLook {
    public static PacketSender create(int id, byte yaw, byte pitch, boolean ground){
        return new PacketSender(ReflectionUtils.getConstructor(ClassFinder.NMS.PacketPlayOutEntityLook, new Group<>(
                new Class<?>[]{int.class, byte.class, byte.class, boolean.class},
                new Object[]{id, yaw, pitch, ground}
        )));
    }
}
