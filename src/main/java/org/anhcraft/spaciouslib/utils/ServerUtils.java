package org.anhcraft.spaciouslib.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class ServerUtils {
    public static List<Entity> getAllEntities(){
        List<Entity> e = new ArrayList<>();
        for(World w : Bukkit.getServer().getWorlds()){
            e.addAll(w.getEntities());
        }
        return e;
    }

    public static <E extends Entity> List<Entity> getAllEntitiesClass(Class<E> c){
        List<Entity> e = new ArrayList<>();
        for(World w : Bukkit.getServer().getWorlds()){
            e.addAll(w.getEntitiesByClass(c));
        }
        return e;
    }
}
