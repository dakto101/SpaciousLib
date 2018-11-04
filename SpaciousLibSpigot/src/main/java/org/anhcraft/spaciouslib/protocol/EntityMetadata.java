package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.ClassFinder;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.entity.Entity;

public class EntityMetadata {
    public static PacketSender create(Entity entity){
        Object craftEntity = ReflectionUtils.cast(ClassFinder.CB.CraftEntity, entity);
        Object nmsEntity = ReflectionUtils.getMethod("getHandle", ClassFinder.CB.CraftEntity, craftEntity);
        Object dataWatcher = ReflectionUtils.getMethod(
                "getDataWatcher", ClassFinder.NMS.Entity, nmsEntity);
        return new PacketSender(ReflectionUtils.getConstructor(ClassFinder.NMS.PacketPlayOutEntityMetadata, new Group<>(
                new Class<?>[]{int.class, ClassFinder.NMS.DataWatcher, boolean.class},
                new Object[]{entity.getEntityId(), dataWatcher, true}
        )));
    }

    public static PacketSender create(Object nmsEntity){
        int id = (int) ReflectionUtils.getMethod("getId", ClassFinder.NMS.Entity, nmsEntity);
        Object dataWatcher = ReflectionUtils.getMethod(
                "getDataWatcher", ClassFinder.NMS.Entity, nmsEntity);
        return new PacketSender(ReflectionUtils.getConstructor(ClassFinder.NMS.PacketPlayOutEntityMetadata, new Group<>(
                new Class<?>[]{int.class, ClassFinder.NMS.DataWatcher, boolean.class},
                new Object[]{id, dataWatcher, true}
        )));
    }

    public static PacketSender create(int id, Object dataWatcher){
        return new PacketSender(ReflectionUtils.getConstructor(ClassFinder.NMS.PacketPlayOutEntityMetadata, new Group<>(
                new Class<?>[]{int.class, ClassFinder.NMS.DataWatcher, boolean.class},
                new Object[]{id, dataWatcher, true}
        )));
    }
}
