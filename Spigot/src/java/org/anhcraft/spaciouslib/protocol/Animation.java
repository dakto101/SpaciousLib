package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.GameVersion;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
            Object craftEntity = craftEntityClass.cast(entity);
            Method handle = craftEntityClass.getDeclaredMethod("getHandle");
            Object nmsEntity = handle.invoke(craftEntity);
            Constructor<?> packetCons = packetPlayOutAnimationClass.getDeclaredConstructor(nmsEntityClass, int.class);
            Object packet = packetCons.newInstance(nmsEntity, type.getID());
            return new PacketSender(packet);
        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
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
