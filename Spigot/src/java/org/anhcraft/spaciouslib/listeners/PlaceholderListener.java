package org.anhcraft.spaciouslib.listeners;

import org.anhcraft.spaciouslib.placeholder.PlaceholderAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlaceholderListener implements Listener {
    @EventHandler
    public void join(PlayerJoinEvent event){
        PlaceholderAPI.updateCache(event.getPlayer());
    }
}
