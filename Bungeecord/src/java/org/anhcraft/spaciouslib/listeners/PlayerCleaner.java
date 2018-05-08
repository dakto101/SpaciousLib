package org.anhcraft.spaciouslib.listeners;

import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

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
    public void quit(PlayerDisconnectEvent event){
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
