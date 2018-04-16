package org.anhcraft.spaciouslib.events;

import org.anhcraft.spaciouslib.entity.NPC;
import org.anhcraft.spaciouslib.inventory.EquipSlot;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * An event triggers when a player interact (left or right click) with an NPC which is created by this library. From 1.9 versions, this event will trigger twice, the first time for the main hand and the second time is for the off hand.
 */
public class NPCInteractEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private boolean leftClick;
    private EquipSlot slot;
    private Player player;
    private NPC npc;

    public NPCInteractEvent(Player player, NPC npc, boolean leftClick, EquipSlot slot){
        this.player = player;
        this.leftClick = leftClick;
        this.slot = slot;
        this.npc = npc;
    }

    public NPC getNPC(){
        return this.npc;
    }

    public EquipSlot getSlot(){
        return this.slot;
    }

    public Player getPlayer(){
        return this.player;
    }

    public boolean isLeftClick(){
        return this.leftClick;
    }

    public boolean isRightClick(){
        return !this.leftClick;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
