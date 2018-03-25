package org.anhcraft.spaciouslib.events;

import org.anhcraft.spaciouslib.anvil.AnvilWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * An event triggers when a player close an anvil which was created by this library
 */
public class AnvilCloseEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private String inv;
    private Player p;
    private AnvilWrapper anvil;

    public AnvilCloseEvent(String inv, Player p, AnvilWrapper anvil){
        this.inv = inv;
        this.p = p;
        this.anvil = anvil;
    }

    public AnvilWrapper getAnvil(){
        return this.anvil;
    }

    public String getInventory(){
        return this.inv;
    }

    public Player getPlayer(){
        return this.p;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}