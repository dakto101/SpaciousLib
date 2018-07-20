package org.anhcraft.spaciouslib.inventory.anvil;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class AnvilWrapper {
    public Inventory inv;
    public abstract void open();
    public abstract void setItem(AnvilSlot slot, ItemStack item);
}
