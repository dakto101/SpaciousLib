package org.anhcraft.spaciouslib.listeners;

import org.anhcraft.spaciouslib.events.AnvilClickEvent;
import org.anhcraft.spaciouslib.events.AnvilCloseEvent;
import org.anhcraft.spaciouslib.anvil.AnvilForm;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;

public class AnvilFormListener implements Listener {
    @EventHandler
    public void click(AnvilClickEvent e){
        Map.Entry<Player, AnvilForm.AnvilInfo> x = null;
        for(Map.Entry<Player, AnvilForm.AnvilInfo> a : AnvilForm.anvils.entrySet()) {
            if(a.getValue().id().equals(e.getAnvil().getAnvilID())
                    && a.getKey().equals(e.getPlayer())
                    && e.getSlot() != null){
                x = a;
                break;
            }
        }
        if(x == null){
            return;
        }
        String i = e.getInput();
        if(i == null){
            i = "";
        }
        x.getValue().run().run(i);
        e.setDestory(true);
        AnvilForm.anvils.remove(e.getPlayer());
    }

    @EventHandler
    public void close(AnvilCloseEvent e){
        Map.Entry<Player, AnvilForm.AnvilInfo> x = null;
        for(Map.Entry<Player, AnvilForm.AnvilInfo> a : AnvilForm.anvils.entrySet()) {
            if(a.getValue().id().equals(e.getAnvil().getAnvilID())
                    && a.getKey().equals(e.getPlayer())){
                x = a;
                break;
            }
        }
        if(x == null){
            return;
        }
        AnvilForm.anvils.remove(e.getPlayer());
    }
}
