package org.anhcraft.spaciouslib.events;

import org.anhcraft.spaciouslib.inventory.EquipSlot;
import org.anhcraft.spaciouslib.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

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
