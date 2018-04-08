package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.entity.HumanEntity;

public class NamedEntitySpawn {
    public static PacketSender create(Object nmsEntityHuman){
        GameVersion v = GameVersion.getVersion();
        try {
            Class<?> nmsEntityHumanClass = Class.forName("net.minecraft.server." + GameVersion.getVersion().toString() + ".EntityHuman");
            Class<?> packetPlayOutNamedEntitySpawnClass = Class.forName("net.minecraft.server." + v.toString() + ".PacketPlayOutNamedEntitySpawn");
            return new PacketSender(ReflectionUtils.getConstructor(packetPlayOutNamedEntitySpawnClass, new Group<>(
                    new Class<?>[]{nmsEntityHumanClass},
                    new Object[]{nmsEntityHuman}
            )));
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PacketSender create(HumanEntity humanEntity){
        GameVersion v = GameVersion.getVersion();
        try {
            Class<?> craftHumanEntityClass = Class.forName("org.bukkit.craftbukkit." + GameVersion.getVersion().toString() + ".entity.CraftHumanEntity");
            Class<?> nmsEntityHumanClass = Class.forName("net.minecraft.server." + GameVersion.getVersion().toString() + ".EntityHuman");
            Class<?> packetPlayOutNamedEntitySpawnClass = Class.forName("net.minecraft.server." + v.toString() + ".PacketPlayOutNamedEntitySpawn");
            Object craftEntityHuman = ReflectionUtils.cast(craftHumanEntityClass, humanEntity);
            Object nmsEntityHuman = ReflectionUtils.getMethod("getHandle", craftHumanEntityClass, craftEntityHuman);
            return new PacketSender(ReflectionUtils.getConstructor(packetPlayOutNamedEntitySpawnClass, new Group<>(
                    new Class<?>[]{nmsEntityHumanClass},
                    new Object[]{nmsEntityHuman}
            )));
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
