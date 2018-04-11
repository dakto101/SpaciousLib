package org.anhcraft.spaciouslib.events;

import org.anhcraft.spaciouslib.inventory.EquipSlot;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * An event triggers when a player equip a new item
 * That item will be null if they unequipped an item
 */
public class ArmorEquipEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private ItemStack oldArmor;
    private ItemStack newArmor;
    private EquipSlot slot;
    private Player player;

    public ArmorEquipEvent(Player player, ItemStack oldArmor, ItemStack newArmor, EquipSlot slot){
        this.player = player;
        this.oldArmor = oldArmor;
        this.newArmor = newArmor;
        this.slot = slot;
    }

    public EquipSlot getSlot(){
        return this.slot;
    }

    public Player getPlayer(){
        return this.player;
    }

    public ItemStack getNewArmor(){
        return this.newArmor;
    }

    public ItemStack getOldArmor(){
        return this.oldArmor;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}