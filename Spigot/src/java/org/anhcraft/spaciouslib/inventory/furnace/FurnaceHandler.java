package org.anhcraft.spaciouslib.inventory.furnace;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class FurnaceHandler {
    /**
     * This method will be called if a player clicks on an item of a specified furnace
     * @param player the player
     * @param item the clicked item
     * @param slot the slot which the clicked item was put on
     */
    public abstract void handle(Player player, ItemStack item, FurnaceSlot slot);
}
