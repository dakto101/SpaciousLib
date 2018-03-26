package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.GameVersion;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A class helps you to send camera packets
 */
public class Camera {

    /**
     * Creates a new camera packet
     * @param entity the entity
     * @return PacketSender object
     */
    public static PacketSender create(Entity entity) {
        try {
            Class<?> packetClass = Class.forName("net.minecraft.server." + GameVersion.getVersion().toString() + ".PacketPlayOutCamera");
            Class<?> nmsEntityClass = Class.forName("net.minecraft.server." + GameVersion.getVersion().toString() + ".Entity");
            Class<?> craftEntityClass = Class.forName("org.bukkit.craftbukkit." + GameVersion.getVersion().toString() + ".entity.CraftEntity");
            Object craftEntity = craftEntityClass.cast(entity);
            Method nmsEntityHandle = craftEntityClass.getDeclaredMethod("getHandle");
            Object nmsEntity = nmsEntityHandle.invoke(craftEntity);
            Constructor cons = packetClass.getConstructor(nmsEntityClass);
            return new PacketSender(cons.newInstance(nmsEntity));
        } catch(ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}