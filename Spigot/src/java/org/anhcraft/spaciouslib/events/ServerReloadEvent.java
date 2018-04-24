package org.anhcraft.spaciouslib.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * An event triggers when the server is going to reload
 */
public class ServerReloadEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
