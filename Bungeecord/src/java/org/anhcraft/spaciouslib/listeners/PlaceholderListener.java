package org.anhcraft.spaciouslib.listeners;

import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.anhcraft.spaciouslib.placeholder.PlaceholderAPI;
import org.anhcraft.spaciouslib.scheduler.DelayedTask;

public class PlaceholderListener implements Listener {
    @EventHandler
    public void join(ServerConnectedEvent event){
        new DelayedTask(() -> PlaceholderAPI.updateCache(event.getPlayer()), 1).run();
    }
}
