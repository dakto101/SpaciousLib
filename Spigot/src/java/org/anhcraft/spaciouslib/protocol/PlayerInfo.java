package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.entity.Player;

import java.lang.reflect.*;

public class PlayerInfo {
    public enum Type {
        ADD_PLAYER,
        UPDATE_GAME_MODE,
        UPDATE_LATENCY,
        UPDATE_DISPLAY_NAME,
        REMOVE_PLAYER
    }

    public static PacketSender create(Type type, Player player){
        String v = GameVersion.getVersion().toString();
        try {
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + v + ".entity.CraftPlayer");
            Class<?> nmsEntityPlayerClass = Class.forName("net.minecraft.server." + v + ".EntityPlayer");
            Class<?> packetPlayOutPlayerInfoClass = Class.forName("net.minecraft.server." + v + ".PacketPlayOutPlayerInfo");
            Class<?> enumPlayerInfoActionClass = Class.forName("net.minecraft.server." + v + "." + (v.equals(GameVersion.v1_8_R1.toString()) ? "" : "PacketPlayOutPlayerInfo$") + "EnumPlayerInfoAction");
            Object craftPlayer = ReflectionUtils.cast(craftPlayerClass, player);
            Object nmsEntityPlayer = ReflectionUtils.getMethod("getHandle", craftPlayerClass, craftPlayer);
            Object enumPlayerInfoAction = ReflectionUtils.getEnum(type.toString(), enumPlayerInfoActionClass);
            Object[] x = (Object[]) Array.newInstance(nmsEntityPlayerClass, 1);
            x[0] = nmsEntityPlayer;
            return new PacketSender(ReflectionUtils.getConstructor(packetPlayOutPlayerInfoClass, new Group<>(
                    new Class<?>[]{enumPlayerInfoActionClass, Array.newInstance(nmsEntityPlayerClass, 0).getClass()},
                    new Object[]{enumPlayerInfoAction, x}
            )));
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PacketSender create(Type type, Object nmsEntityPlayer){
        String v = GameVersion.getVersion().toString();
        try {
            Class<?> nmsEntityPlayerClass = Class.forName("net.minecraft.server." + v + ".EntityPlayer");
            Class<?> packetPlayOutPlayerInfoClass = Class.forName("net.minecraft.server." + v + ".PacketPlayOutPlayerInfo");
            Class<?> enumPlayerInfoActionClass = Class.forName("net.minecraft.server." + v + "." + (v.equals(GameVersion.v1_8_R1.toString()) ? "" : "PacketPlayOutPlayerInfo$") + "EnumPlayerInfoAction");
            Object enumPlayerInfoAction = ReflectionUtils.getEnum(type.toString(), enumPlayerInfoActionClass);
            Object[] x = (Object[]) Array.newInstance(nmsEntityPlayerClass, 1);
            x[0] = nmsEntityPlayer;
            return new PacketSender(ReflectionUtils.getConstructor(packetPlayOutPlayerInfoClass, new Group<>(
                    new Class<?>[]{enumPlayerInfoActionClass, Array.newInstance(nmsEntityPlayerClass, 0).getClass()},
                    new Object[]{enumPlayerInfoAction, x}
            )));
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
