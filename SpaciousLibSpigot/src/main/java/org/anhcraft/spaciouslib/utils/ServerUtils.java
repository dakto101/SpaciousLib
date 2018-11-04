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
     * Gets three recent TPS record (1 min, 10 min, 15 min ago) of the server
     * @return an array contains three recent TPS
     */
    public static double[] getTPS(){
        Object craftServer = ReflectionUtils.cast(ClassFinder.CB.CraftServer, Bukkit.getServer());
        Object nmsServer = ReflectionUtils.getMethod("getServer", ClassFinder.CB.CraftServer, craftServer);
        return (double[]) ReflectionUtils.getField("recentTps", ClassFinder.NMS.MinecraftServer, nmsServer);
    }
}
