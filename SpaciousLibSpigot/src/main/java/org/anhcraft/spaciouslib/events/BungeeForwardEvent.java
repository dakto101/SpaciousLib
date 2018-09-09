package org.anhcraft.spaciouslib.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.io.DataInputStream;

/**
 * An event triggers when a data was sent from the Bungeecord channel
 */
public class BungeeForwardEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private DataInputStream data;

    public BungeeForwardEvent(DataInputStream data){
        this.data = data;
    }

    public DataInputStream getData(){
        return this.data;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}