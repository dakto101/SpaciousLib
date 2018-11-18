package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.ClassFinder;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.entity.Entity;

public class EntityTeleport {
    public static PacketSender create(Object nmsEntity){
        return new PacketSender(ReflectionUtils.getConstructor(ClassFinder.NMS.PacketPlayOutEntityTeleport, new Group<>(
                new Class<?>[]{ClassFinder.NMS.Entity},
                new Object[]{nmsEntity}
        )));
    }

    public static PacketSender create(Entity entity){
        Object craftEntity = ReflectionUtils.cast(ClassFinder.CB.CraftEntity, entity);
        Object nmsEntity = ReflectionUtils.getMethod("getHandle", ClassFinder.CB.CraftEntity, craftEntity);
        return new PacketSender(ReflectionUtils.getConstructor(ClassFinder.NMS.PacketPlayOutEntityTeleport, new Group<>(
                new Class<?>[]{ClassFinder.NMS.Entity},
                new Object[]{nmsEntity}
        )));
    }
}
