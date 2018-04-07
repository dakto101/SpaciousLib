package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.GameVersion;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PlayerInfo {
    public enum Type {
        ADD_PLAYER,
        UPDATE_GAME_MODE,
        UPDATE_LATENCY,
        UPDATE_DISPLAY_NAME,
        REMOVE_PLAYER
    }

    public static PacketSender create(Type type, Player player){
        GameVersion v = GameVersion.getVersion();
        try {
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + v.toString() + ".entity.CraftPlayer");
            Class<?> nmsEntityPlayerClass = Class.forName("net.minecraft.server." + v.toString() + ".EntityPlayer");
            Class<?> packetPlayOutPlayerInfoClass = Class.forName("net.minecraft.server." + v.toString() + ".PacketPlayOutPlayerInfo");
            Class<?> enumPlayerInfoActionClass = Class.forName("net.minecraft.server." + v.toString() + ".PacketPlayOutPlayerInfo.EnumPlayerInfoAction");
            Object craftPlayer = craftPlayerClass.cast(player);
            Method handle = craftPlayerClass.getDeclaredMethod("getHandle");
            Object nmsEntityPlayer = nmsEntityPlayerClass.cast(handle.invoke(craftPlayer));
            Field enumPlayerInfoActionField = enumPlayerInfoActionClass.getDeclaredField(type.toString());
            enumPlayerInfoActionField.setAccessible(true);
            Object enumPlayerInfoAction = enumPlayerInfoActionField.get(null);
            Constructor<?> packetCons = packetPlayOutPlayerInfoClass.getDeclaredConstructor(enumPlayerInfoActionClass, nmsEntityPlayerClass);
            Object packet = packetCons.newInstance(enumPlayerInfoAction, nmsEntityPlayer);
            return new PacketSender(packet);
        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
