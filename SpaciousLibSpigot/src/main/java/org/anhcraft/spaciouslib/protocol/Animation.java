package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.ClassFinder;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.entity.Entity;

/**
 * A class helps you to send animation packets
 */
public class Animation {
    /**
     * Creates an animation packet
     * @param entity an entity
     * @param type the type of the animation
     * @return PacketSender object
     */
    public static PacketSender create(Entity entity, Type type) {
        return create(entity.getEntityId(), type);
    }

    /**
     * Creates an animation packet
     * @param entityId the id of an entity
     * @param type the type of the animation
     * @return PacketSender object
     */
    public static PacketSender create(int entityId, Type type) {
        Object ani = ReflectionUtils.getConstructor(ClassFinder.NMS.PacketPlayOutAnimation);
        ReflectionUtils.setField("a", ClassFinder.NMS.PacketPlayOutAnimation, ani, entityId);
        ReflectionUtils.setField("b", ClassFinder.NMS.PacketPlayOutAnimation, ani, type.getId());
        return new PacketSender(ani);
    }

    public enum Type {
        SWING_MAIN_HAND(0),
        TAKE_DAMAGE(1),
        LEAVE_BED(2),
        SWING_OFF_HAND(3),
        CRITICAL_EFFECT(4),
        MAGIC_CRITICAL_EFFECT(5);

        private int i;

        Type(int i) {
            this.i = i;
        }

        public int getId(){
            return i;
        }
    }
}
