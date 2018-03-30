package org.anhcraft.spaciouslib.anvil;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class AnvilHandler {
    public abstract void result(Player player, String input, ItemStack item, AnvilSlot slot);
}