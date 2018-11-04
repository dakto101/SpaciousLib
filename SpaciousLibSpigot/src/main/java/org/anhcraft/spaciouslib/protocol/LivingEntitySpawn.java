package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.ClassFinder;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.entity.LivingEntity;

public class LivingEntitySpawn {
    public static PacketSender create(Object nmsEntityLiving){
        return new PacketSender(ReflectionUtils.getConstructor(ClassFinder.NMS.PacketPlayOutSpawnEntityLiving, new Group<>(
                new Class<?>[]{ClassFinder.NMS.EntityLiving},
                new Object[]{nmsEntityLiving}
        )));
    }

    public static PacketSender create(LivingEntity livingEntity){
        Object craftEntityLiving = ReflectionUtils.cast(ClassFinder.CB.CraftLivingEntity, livingEntity);
        Object nmsEntityLiving = ReflectionUtils.getMethod("getHandle", ClassFinder.CB.CraftLivingEntity, craftEntityLiving);
        return new PacketSender(ReflectionUtils.getConstructor(ClassFinder.NMS.PacketPlayOutSpawnEntityLiving, new Group<>(
                new Class<?>[]{ClassFinder.NMS.EntityLiving},
                new Object[]{nmsEntityLiving}
        )));
    }
}
