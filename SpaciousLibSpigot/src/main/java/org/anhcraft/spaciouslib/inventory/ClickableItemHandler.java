package org.anhcraft.spaciouslib.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface ClickableItemHandler {
    /**
     * This method will be called when a player clicks on a item
     * @param player the player who clicked
     * @param item the item which was clicked by that player
     * @param click the type of that click
     * @param slot the slot index of that item
     * @param action the action
     * @param inventory the inventory which was clicked by that player
     */
    void run(Player player, ItemStack item, ClickType click, int slot, InventoryAction action, Inventory inventory);
}
