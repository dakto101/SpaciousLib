package org.anhcraft.spaciouslib.anvil;

import org.bukkit.inventory.ItemStack;

public interface AnvilWrapper {
    void open();
    void setSlot(AnvilSlot slot, ItemStack item);
    String getAnvilID();
}
