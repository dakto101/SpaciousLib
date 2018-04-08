package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.entity.Entity;

/**
 * A class helps you to send animation packets
 */
public class Animation {
    /**
     * Creates an animation packet
     * @param entity the entity
     * @param type the type of the aniamtion
     * @return PacketSender object
     */
    public static PacketSender create(Entity entity, Type type) {
        GameVersion v = GameVersion.getVersion();
        try {
            Class<?> craftEntityClass = Class.forName("org.bukkit.craftbukkit." + v.toString() + ".entity.CraftEntity");
            Class<?> nmsEntityClass = Class.forName("net.minecraft.server." + v.toString() + ".Entity");
            Class<?> packetPlayOutAnimationClass = Class.forName("net.minecraft.server." + v.toString() + ".PacketPlayOutAnimation");
            Object craftEntity = ReflectionUtils.cast(craftEntityClass, entity);
            Object nmsEntity = ReflectionUtils.getMethod("getHandle", craftEntityClass, craftEntity);
            return new PacketSender(ReflectionUtils.getConstructor(packetPlayOutAnimationClass, new Group<>(
                   new Class<?>[]{nmsEntityClass, int.class},
                   new Object[]{nmsEntity, type.getID()}
            )));
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
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

        public int getID(){
            return i;
        }
    }
}
