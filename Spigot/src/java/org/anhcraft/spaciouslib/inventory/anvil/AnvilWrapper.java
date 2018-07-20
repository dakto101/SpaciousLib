package org.anhcraft.spaciouslib.inventory.anvil;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public abstract class AnvilWrapper<T> {
    protected HashMap<AnvilSlot, ItemStack> items = new HashMap<>();
    public Inventory inv;
    public abstract T open();

    protected void put(AnvilSlot slot, ItemStack item){
        items.put(slot, item);
    }
}
