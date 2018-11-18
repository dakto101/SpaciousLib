package org.anhcraft.spaciouslib.listeners;

import org.anhcraft.spaciouslib.events.ServerReloadEvent;
import org.anhcraft.spaciouslib.events.ServerStopEvent;
import org.anhcraft.spaciouslib.utils.ClassFinder;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

public class ServerListener implements Listener {
    private static int reloadCounter;
    private static boolean isStopping = false;

    public ServerListener(){
        Object craftServer = ReflectionUtils.cast(ClassFinder.CB.CraftServer, Bukkit.getServer());
        reloadCounter = (int) ReflectionUtils.getField("reloadCount", ClassFinder.CB.CraftServer, craftServer);
    }

    @EventHandler
    public void stop(PluginDisableEvent event){
        if(isStopping){
            return;
        }
        Object craftServer = ReflectionUtils.cast(ClassFinder.CB.CraftServer, Bukkit.getServer());
        int reloadCount = (int) ReflectionUtils.getField("reloadCount", ClassFinder.CB.CraftServer, craftServer);
        Object nmsServer = ReflectionUtils.getMethod("getServer", ClassFinder.CB.CraftServer, craftServer);
        boolean isRunning = (boolean) ReflectionUtils.getMethod("isRunning", ClassFinder.NMS.MinecraftServer, nmsServer);
        if(isRunning){
            if(reloadCounter < reloadCount){
                ServerReloadEvent ev = new ServerReloadEvent();
                Bukkit.getServer().getPluginManager().callEvent(ev);
                reloadCounter = reloadCount;
            }
        } else {
            ServerStopEvent ev = new ServerStopEvent();
            Bukkit.getServer().getPluginManager().callEvent(ev);
            isStopping = true;
        }
    }
}
