package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.entity.Entity;

/**
 * A class helps you to send camera packets
 */
public class Camera {
    /**
     * Creates a new camera packet
     * @param entity an entity
     * @return PacketSender object
     */
    public static PacketSender create(Entity entity) {
        return create(entity.getEntityId());
    }

    /**
     * Creates a new camera packet
     * @param entityId the id of an entity
     * @return PacketSender object
     */
    public static PacketSender create(int entityId) {
        String v = GameVersion.getVersion().toString();
        try {
            Class<?> packetClass = Class.forName("net.minecraft.server." + v + ".PacketPlayOutCamera");
            Object camera = ReflectionUtils.getConstructor(packetClass);
            ReflectionUtils.setField("a", packetClass, camera, entityId);
            return new PacketSender(camera);
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}