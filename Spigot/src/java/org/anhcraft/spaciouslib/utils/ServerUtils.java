package org.anhcraft.spaciouslib.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class ServerUtils {
    /**
     * Gets all entities over the server
     * @return list of entities
     */
    public static List<Entity> getAllEntities(){
        List<Entity> e = new ArrayList<>();
        for(World w : Bukkit.getServer().getWorlds()){
            e.addAll(w.getEntities());
        }
        return e;
    }

    /**
     * Gets all specified entities by their class over the server
     * @return list of entities
     */
    public static <E extends Entity> List<Entity> getAllEntitiesByClass(Class<E> c){
        List<Entity> e = new ArrayList<>();
        for(World w : Bukkit.getServer().getWorlds()){
            e.addAll(w.getEntitiesByClass(c));
        }
        return e;
    }

    /**
     * Gets the current TPS (tick-per-second) of this server
     * @return the TPS
     */
    public static double getTPS(){
        try{
            Class<?> craftServerClass = Class.forName("org.bukkit.craftbukkit." + GameVersion.getVersion().toString() + ".CraftServer");
            Class<?> nmsServerClass = Class.forName("net.minecraft.server." + GameVersion.getVersion().toString() + ".MinecraftServer");
            Object craftServer = ReflectionUtils.cast(craftServerClass, Bukkit.getServer());
            Object nmsServer = ReflectionUtils.getMethod("getServer", craftServerClass, craftServer);
            double[] d = (double[]) ReflectionUtils.getField("recentTps", nmsServerClass, nmsServer);
            return d[0];
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
