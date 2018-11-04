package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.ClassFinder;
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
        Object camera = ReflectionUtils.getConstructor(ClassFinder.NMS.PacketPlayOutCamera);
        ReflectionUtils.setField("a", ClassFinder.NMS.PacketPlayOutCamera, camera, entityId);
        return null;
    }
}