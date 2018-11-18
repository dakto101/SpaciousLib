package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.ClassFinder;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.entity.HumanEntity;

public class NamedEntitySpawn {
    public static PacketSender create(Object nmsEntityHuman){
        return new PacketSender(ReflectionUtils.getConstructor(ClassFinder.NMS.PacketPlayOutNamedEntitySpawn, new Group<>(
                new Class<?>[]{ClassFinder.NMS.EntityHuman},
                new Object[]{nmsEntityHuman}
        )));
    }

    public static PacketSender create(HumanEntity humanEntity){
        Object craftEntityHuman = ReflectionUtils.cast(ClassFinder.CB.CraftHumanEntity, humanEntity);
        Object nmsEntityHuman = ReflectionUtils.getMethod("getHandle", ClassFinder.CB.CraftHumanEntity, craftEntityHuman);
        return new PacketSender(ReflectionUtils.getConstructor(ClassFinder.NMS.PacketPlayOutNamedEntitySpawn, new Group<>(
                new Class<?>[]{ClassFinder.NMS.EntityHuman},
                new Object[]{nmsEntityHuman}
        )));
    }
}
