package org.anhcraft.spaciouslib.inventory.anvil;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class AnvilWrapper<T> {
    public Inventory inv;
    public abstract T open();
    public abstract T setItem(AnvilSlot slot, ItemStack item);
}
