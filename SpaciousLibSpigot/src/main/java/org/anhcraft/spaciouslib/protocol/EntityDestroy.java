package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.ClassFinder;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;

public class EntityDestroy {
    public static PacketSender create(int entityID){
        return new PacketSender(ReflectionUtils.getConstructor(ClassFinder.NMS.PacketPlayOutEntityDestroy, new Group<>(
                new Class<?>[]{int[].class},
                new Object[]{new int[]{entityID}}
        )));
    }
}
