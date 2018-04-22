package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.entity.Entity;

public class EntityMetadata {
    public static PacketSender create(Entity entity){
        String v = GameVersion.getVersion().toString();
        try {
            Class<?> nmsEntityClass = Class.forName("net.minecraft.server." + v + ".Entity");
            Class<?> packetPlayOutEntityMetadataClass = Class.forName("net.minecraft.server." + v + ".PacketPlayOutEntityMetadata");
            Class<?> dataWatcherClass = Class.forName("net.minecraft.server." + v + ".DataWatcher");
            Class<?> craftEntityClass = Class.forName("org.bukkit.craftbukkit." + v + ".entity.CraftEntity");
            Object craftEntity = ReflectionUtils.cast(craftEntityClass, entity);
            Object nmsEntity = ReflectionUtils.getMethod("getHandle", craftEntityClass, craftEntity);
            Object dataWatcher = ReflectionUtils.getMethod(
                    "getDataWatcher", nmsEntityClass, nmsEntity);
            return new PacketSender(ReflectionUtils.getConstructor(packetPlayOutEntityMetadataClass, new Group<>(
                    new Class<?>[]{int.class, dataWatcherClass, boolean.class},
                    new Object[]{entity.getEntityId(), dataWatcher, true}
            )));
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PacketSender create(Object nmsEntity){
        String v = GameVersion.getVersion().toString();
        try {
            Class<?> nmsEntityClass = Class.forName("net.minecraft.server." + v + ".Entity");
            Class<?> packetPlayOutEntityMetadataClass = Class.forName("net.minecraft.server." + v + ".PacketPlayOutEntityMetadata");
            Class<?> dataWatcherClass = Class.forName("net.minecraft.server." + v + ".DataWatcher");
            int id = (int) ReflectionUtils.getMethod("getId", nmsEntityClass, nmsEntity);
            Object dataWatcher = ReflectionUtils.getMethod(
                    "getDataWatcher", nmsEntityClass, nmsEntity);
            return new PacketSender(ReflectionUtils.getConstructor(packetPlayOutEntityMetadataClass, new Group<>(
                    new Class<?>[]{int.class, dataWatcherClass, boolean.class},
                    new Object[]{id, dataWatcher, true}
            )));
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PacketSender create(int id, Object dataWatcher){
        String v = GameVersion.getVersion().toString();
        try {
            Class<?> packetPlayOutEntityMetadataClass = Class.forName("net.minecraft.server." + v + ".PacketPlayOutEntityMetadata");
            Class<?> dataWatcherClass = Class.forName("net.minecraft.server." + v + ".DataWatcher");
            return new PacketSender(ReflectionUtils.getConstructor(packetPlayOutEntityMetadataClass, new Group<>(
                    new Class<?>[]{int.class, dataWatcherClass, boolean.class},
                    new Object[]{id, dataWatcher, true}
            )));
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
