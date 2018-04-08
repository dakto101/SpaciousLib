package org.anhcraft.spaciouslib.entity;

import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.entity.Player;

public class PlayerPing {
    public static int get(Player player){
        GameVersion v = GameVersion.getVersion();
        try {
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + v.toString() + ".entity.CraftPlayer");
            Class<?> nmsEntityPlayerClass = Class.forName("net.minecraft.server." + v.toString() + ".EntityPlayer");
            Object craftPlayer = ReflectionUtils.cast(craftPlayerClass, player);
            Object nmsEntity = ReflectionUtils.getMethod("getHandle", craftPlayerClass, craftPlayer);
            return (int) ReflectionUtils.getField("ping", nmsEntityPlayerClass, nmsEntity);
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
