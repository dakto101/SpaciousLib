package org.anhcraft.spaciouslib.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * An event triggers when player jumps
 * Doesn't trigger if player falls down
 */
public class PlayerJumpEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player p;
    private boolean b;

    public PlayerJumpEvent(Player p, boolean b){
        this.p = p;
        this.b = b;
    }

    public Player getPlayer(){
        return this.p;
    }

    public boolean isOnSpot(){
        return this.b;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
