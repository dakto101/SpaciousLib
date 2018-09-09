package org.anhcraft.spaciouslib.listeners;

import org.anhcraft.spaciouslib.events.PlayerJumpEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerJumpEventListener implements Listener{
    @EventHandler
    public void m(PlayerMoveEvent e){
        double a = e.getTo().getY() - e.getFrom().getY();
        if(a == 0.33319999363422426) {
            PlayerJumpEvent ev = new PlayerJumpEvent(e.getPlayer(), e.getFrom().getX() == e.getTo().getX() && e.getFrom().getZ() == e.getTo().getZ());
            Bukkit.getServer().getPluginManager().callEvent(ev);
        }
    }
}
