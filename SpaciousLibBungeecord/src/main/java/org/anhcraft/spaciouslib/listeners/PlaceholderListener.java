package org.anhcraft.spaciouslib.listeners;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.anhcraft.spaciouslib.SpaciousLib;
import org.anhcraft.spaciouslib.placeholder.PlaceholderAPI;

public class PlaceholderListener implements Listener {
    @EventHandler
    public void join(ServerConnectedEvent event){
        BungeeCord.getInstance().getScheduler().runAsync(SpaciousLib.instance, () -> PlaceholderAPI.updateCache(event.getPlayer()));
    }
}
