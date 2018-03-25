package org.anhcraft.spaciouslib.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.io.DataInputStream;

public class BungeeForwardEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private String channel;
    private DataInputStream data;

    public BungeeForwardEvent(String channel, DataInputStream data){
        this.data = data;
        this.channel = channel;
    }

    public String getChannel(){
        return this.channel;
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