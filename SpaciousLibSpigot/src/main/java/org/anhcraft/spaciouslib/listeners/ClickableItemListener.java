package org.anhcraft.spaciouslib.listeners;

import org.anhcraft.spaciouslib.inventory.ClickableItemHandler;
import org.anhcraft.spaciouslib.utils.CompatibilityUtils;
import org.anhcraft.spaciouslib.utils.InventoryUtils;
import org.anhcraft.spaciouslib.utils.Table;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class ClickableItemListener implements Listener {
    private static final HashMap<Inventory, Table<ClickableItemHandler>> data = new HashMap<>();

    public static void add(Inventory inv, Table<ClickableItemHandler> table){
        data.put(inv, table);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void click(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = CompatibilityUtils.getInventory(event);
        ClickType type = event.getClick();
        if (inventory != null && data.containsKey(inventory)){
            ItemStack item = event.getCurrentItem();
            if(!InventoryUtils.isNull(item)){
                ClickableItemHandler c = data.get(inventory).get(event.getRawSlot());
                if(c != null) {
                    event.setCancelled(true);
                    event.setResult(Event.Result.DENY);
                    c.run(player, item, type, event.getRawSlot(), event.getAction(), inventory);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void close(InventoryCloseEvent event){
        data.remove(event.getInventory());
    }
}
