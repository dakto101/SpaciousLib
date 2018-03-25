package org.anhcraft.spaciouslib.listeners;

import org.anhcraft.spaciouslib.events.AnvilCloseEvent;
import org.anhcraft.spaciouslib.anvil.Anvil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class AnvilCloseListener implements Listener {
    @EventHandler
    public void closeAnvil(AnvilCloseEvent event){
        if(Anvil.anvils.containsKey(event.getPlayer().getUniqueId())){
            Anvil.anvils.remove(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        if(Anvil.anvils.containsKey(event.getPlayer().getUniqueId())){
            Anvil.anvils.remove(event.getPlayer().getUniqueId());
        }
    }
}
