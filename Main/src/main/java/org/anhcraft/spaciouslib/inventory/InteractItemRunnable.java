package org.anhcraft.spaciouslib.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public interface InteractItemRunnable {
    void run(Player player, ItemStack item, ClickType action, int slot);
}
