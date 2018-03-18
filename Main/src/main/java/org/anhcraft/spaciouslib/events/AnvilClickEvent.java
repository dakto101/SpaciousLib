package org.anhcraft.spaciouslib.events;

import org.anhcraft.spaciouslib.anvil.AnvilSlot;
import org.anhcraft.spaciouslib.anvil.AnvilWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * An event triggers when a player click on a specific item of an anvil which was created by this library
 */
public class AnvilClickEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private AnvilSlot slot;
    private String input;
    private Player p;
    private boolean destroy = false;
    private AnvilWrapper anvil;

    public AnvilClickEvent(AnvilSlot slot, String input, Player p, AnvilWrapper anvil){
        this.slot = slot;
        this.input = input;
        this.p = p;
        this.anvil = anvil;
    }

    public AnvilWrapper getAnvil(){
        return this.anvil;
    }

    public AnvilSlot getSlot(){
        return this.slot;
    }

    public String getInput(){
        return this.input;
    }

    public Player getPlayer(){
        return this.p;
    }

    public boolean isDestory() {
        return destroy;
    }

    public void setDestory(boolean destroy) {
        this.destroy = destroy;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
