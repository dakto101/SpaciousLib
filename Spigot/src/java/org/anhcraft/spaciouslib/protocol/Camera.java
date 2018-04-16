package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.entity.Entity;

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
        String v = GameVersion.getVersion().toString();
        try {
            Class<?> packetClass = Class.forName("net.minecraft.server." + v + ".PacketPlayOutCamera");
            Class<?> nmsEntityClass = Class.forName("net.minecraft.server." + v + ".Entity");
            Class<?> craftEntityClass = Class.forName("org.bukkit.craftbukkit." + v + ".entity.CraftEntity");
            Object craftEntity = ReflectionUtils.cast(craftEntityClass, entity);
            Object nmsEntity = ReflectionUtils.getMethod("getHandle", craftEntityClass, craftEntity);
            return new PacketSender(ReflectionUtils.getConstructor(packetClass, new Group<>(
                    new Class<?>[]{nmsEntityClass},
                    new Object[]{nmsEntity}
            )));
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}