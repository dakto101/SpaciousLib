package org.anhcraft.spaciouslib.listeners;

import org.anhcraft.spaciouslib.SpaciousLib;
import org.anhcraft.spaciouslib.placeholder.PlaceholderAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlaceholderListener implements Listener {
    @EventHandler
    public void join(PlayerJoinEvent event){
        new BukkitRunnable() {
            @Override
            public void run() {
                PlaceholderAPI.updateCache(event.getPlayer());
            }
        }.runTaskAsynchronously(SpaciousLib.instance);
    }
}
