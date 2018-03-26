package org.anhcraft.spaciouslib.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public interface InteractItemRunnable {
    /**
     * A method will be called when a player click on a specific inventory
     * @param player the player who clicked
     * @param item the item which was clicked by that player
     * @param action the type of that click
     * @param slot the slot index of that item
     */
    void run(Player player, ItemStack item, ClickType action, int slot);
}
