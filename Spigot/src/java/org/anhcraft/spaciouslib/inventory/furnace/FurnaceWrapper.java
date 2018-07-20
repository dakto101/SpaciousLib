package org.anhcraft.spaciouslib.inventory.furnace;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class FurnaceWrapper<T> {
    protected Inventory inv;
    public abstract T open();
    public abstract T setItem(FurnaceSlot slot, ItemStack item);
    public abstract T setProgress(int current);
    public abstract T setFuelBurnTime(int current, int max);
}
