package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.GVersion;
import org.anhcraft.spaciouslib.utils.GameVersion;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Animation {
    public static PacketSender create(Player player, Type type) {
        GVersion v = GameVersion.getVersion();
        try {
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + v.toString() + ".entity.CraftPlayer");
            Class<?> nmsEntityClass = Class.forName("net.minecraft.server." + v.toString() + ".Entity");
            Class<?> packetPlayOutAnimationClass = Class.forName("net.minecraft.server." + v.toString() + ".PacketPlayOutAnimation");
            Object craftPlayer = craftPlayerClass.cast(player);
            Method handle = craftPlayerClass.getDeclaredMethod("getHandle");
            Object nmsEntity = handle.invoke(craftPlayer);
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
