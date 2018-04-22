package org.anhcraft.spaciouslib.listeners;

import org.anhcraft.spaciouslib.placeholder.CachedPlaceholder;
import org.anhcraft.spaciouslib.placeholder.Placeholder;
import org.anhcraft.spaciouslib.placeholder.PlaceholderAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlaceholderListener implements Listener {
    @EventHandler
    public void join(PlayerJoinEvent event){
        PlaceholderAPI.updateCache(event.getPlayer());
    }

    @EventHandler
    public void quit(PlayerQuitEvent event){
        for(Placeholder placeholder : PlaceholderAPI.getPlaceholders()){
            if(placeholder instanceof CachedPlaceholder){
                ((CachedPlaceholder) placeholder).removeCache(event.getPlayer());
            }
        }
    }
}
