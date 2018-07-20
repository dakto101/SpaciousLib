package org.anhcraft.spaciouslib.inventory.anvil;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class AnvilHandler {
    /**
     * This method will be called if a player clicks on an item of a specific anvil
     * @param player the player
     * @param input the input, which is also the name of the first input item
     * @param item the clicked item
     * @param slot the slot which the clicked item was put on
     */
    public abstract void result(Player player, String input, ItemStack item, AnvilSlot slot);
}
