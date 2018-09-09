package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.entity.Entity;

public class EntityTeleport {
    public static PacketSender create(Object nmsEntity){
        String v = GameVersion.getVersion().toString();
        try {
            Class<?> nmsEntityClass = Class.forName("net.minecraft.server." + v + ".Entity");
            Class<?> packetPlayOutEntityTeleportClass = Class.forName("net.minecraft.server." + v + ".PacketPlayOutEntityTeleport");
            return new PacketSender(ReflectionUtils.getConstructor(packetPlayOutEntityTeleportClass, new Group<>(
                    new Class<?>[]{nmsEntityClass},
                    new Object[]{nmsEntity}
            )));
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PacketSender create(Entity entity){
        String v = GameVersion.getVersion().toString();
        try {
            Class<?> craftEntityClass = Class.forName("org.bukkit.craftbukkit." + v + ".entity.CraftEntity");
            Class<?> nmsEntityClass = Class.forName("net.minecraft.server." + v + ".Entity");
            Class<?> packetPlayOutEntityTeleportClass = Class.forName("net.minecraft.server." + v + ".PacketPlayOutEntityTeleport");
            Object craftEntity = ReflectionUtils.cast(craftEntityClass, entity);
            Object nmsEntity = ReflectionUtils.getMethod("getHandle", craftEntityClass, craftEntity);
            return new PacketSender(ReflectionUtils.getConstructor(packetPlayOutEntityTeleportClass, new Group<>(
                    new Class<?>[]{nmsEntityClass},
                    new Object[]{nmsEntity}
            )));
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
