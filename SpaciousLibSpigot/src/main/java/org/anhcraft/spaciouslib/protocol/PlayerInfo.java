package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.ClassFinder;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;

public class PlayerInfo {
    public enum Type {
        ADD_PLAYER,
        UPDATE_GAME_MODE,
        UPDATE_LATENCY,
        UPDATE_DISPLAY_NAME,
        REMOVE_PLAYER
    }

    public static PacketSender create(Type type, Player player){
        Object craftPlayer = ReflectionUtils.cast(ClassFinder.CB.CraftPlayer, player);
        Object nmsEntityPlayer = ReflectionUtils.getMethod("getHandle", ClassFinder.CB.CraftPlayer, craftPlayer);
        Object enumPlayerInfoAction = ReflectionUtils.getEnum(type.toString(), ClassFinder.NMS.EnumPlayerInfoAction);
        Object[] x = (Object[]) Array.newInstance(ClassFinder.NMS.EntityPlayer, 1);
        x[0] = nmsEntityPlayer;
        return new PacketSender(ReflectionUtils.getConstructor(ClassFinder.NMS.PacketPlayOutPlayerInfo, new Group<>(
                new Class<?>[]{ClassFinder.NMS.EnumPlayerInfoAction, Array.newInstance(ClassFinder.NMS.EntityPlayer, 0).getClass()},
                new Object[]{enumPlayerInfoAction, x}
        )));
    }

    public static PacketSender create(Type type, Object nmsEntityPlayer){
        Object enumPlayerInfoAction = ReflectionUtils.getEnum(type.toString(), ClassFinder.NMS.EnumPlayerInfoAction);
        Object[] x = (Object[]) Array.newInstance(ClassFinder.NMS.EntityPlayer, 1);
        x[0] = nmsEntityPlayer;
        return new PacketSender(ReflectionUtils.getConstructor(ClassFinder.NMS.PacketPlayOutPlayerInfo, new Group<>(
                new Class<?>[]{ClassFinder.NMS.EnumPlayerInfoAction, Array.newInstance(ClassFinder.NMS.EntityPlayer, 0).getClass()},
                new Object[]{enumPlayerInfoAction, x}
        )));
    }
}
