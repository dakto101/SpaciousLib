package org.anhcraft.spaciouslib.listeners;

import org.anhcraft.spaciouslib.placeholder.PlaceholderManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class FixedPlaceholderListener implements Listener {
    @EventHandler
    public void join(PlayerJoinEvent event){
        PlaceholderManager.updateCache(event.getPlayer());
    }
}
