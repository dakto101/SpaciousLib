package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.GameVersion;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class Camera {
    public static PacketSender create(Entity entity) {
        try {
            Class<?> packetClass = Class.forName("net.minecraft.server." + GameVersion.getVersion().toString() + ".PacketPlayOutCamera");
            Class<?> nmsEntityClass = Class.forName("net.minecraft.server." + GameVersion.getVersion().toString() + ".Entity");
            Class<?> craftEntityClass = Class.forName("org.bukkit.craftbukkit." + GameVersion.getVersion().toString() + ".entity.CraftEntity");
            Object craftEntity = craftEntityClass.cast(entity);
            Field nmsEntityField = craftEntityClass.getDeclaredField("entity");
            nmsEntityField.setAccessible(true);
            Object nmsEntity = nmsEntityField.get(craftEntity);
            Constructor cons = packetClass.getConstructor(nmsEntityClass);
            return new PacketSender(cons.newInstance(nmsEntity));
        } catch(ClassNotFoundException | NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}