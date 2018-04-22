package org.anhcraft.spaciouslib.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

/**
 * A class helps you to remove an online player when his/her quits the game
 */
public class PlayerCleaner implements Listener {
    private static Set<Object> data = new HashSet<>();

    public static void add(Collection<UUID> obj){
        data.add(obj);
    }

    public static void add(Map<UUID, ?> obj){
        data.add(obj);
    }

    public static void remove(Object obj){
        data.remove(obj);
    }

    @EventHandler
    public void quit(PlayerQuitEvent event){
        for(Object obj: data){
            if(obj instanceof Collection){
                ((Collection<UUID>) obj).remove(event.getPlayer().getUniqueId());
            }
            if(obj instanceof Map){
                ((Map<UUID, ?>) obj).remove(event.getPlayer().getUniqueId());
            }
        }
    }
}
