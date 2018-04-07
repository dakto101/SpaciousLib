package org.anhcraft.spaciouslib.entity;

import org.anhcraft.spaciouslib.utils.GameVersion;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PlayerPing {
    public static int get(Player player){
        GameVersion v = GameVersion.getVersion();
        try {
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + v.toString() + ".entity.CraftPlayer");
            Class<?> nmsEntityPlayerClass = Class.forName("net.minecraft.server." + v.toString() + ".EntityPlayer");
            Object craftPlayer = craftPlayerClass.cast(player);
            Method handle = craftPlayerClass.getDeclaredMethod("getHandle");
            Object nmsEntity = handle.invoke(craftPlayer);
            Object nmsEntityPlayer = nmsEntityPlayerClass.cast(nmsEntity);
            Field pingField = nmsEntityPlayerClass.getDeclaredField("ping");
            pingField.setAccessible(true);
            return (int) pingField.get(nmsEntityPlayer);
        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
