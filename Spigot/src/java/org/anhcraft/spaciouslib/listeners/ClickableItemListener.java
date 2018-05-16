package org.anhcraft.spaciouslib.listeners;

import org.anhcraft.spaciouslib.compatibility.CompatibilityInventoryClickEvent;
import org.anhcraft.spaciouslib.inventory.ClickableItemHandler;
import org.anhcraft.spaciouslib.utils.InventoryUtils;
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

import java.util.LinkedHashMap;

public class ClickableItemListener implements Listener {
    private static LinkedHashMap<Inventory, LinkedHashMap<ItemStack, ClickableItemHandler>> data = new LinkedHashMap<>();

    public static void a(Inventory inv, ItemStack item, ClickableItemHandler run){
        LinkedHashMap<ItemStack, ClickableItemHandler> items = new LinkedHashMap<>();
        if(data.containsKey(inv)){
            items = data.get(inv);
        }
        items.put(item, run);
        data.put(inv, items);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void click(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = CompatibilityInventoryClickEvent.getInventory(event);
        ClickType type = event.getClick();
        if (inventory != null && data.containsKey(inventory)){
            ItemStack item = event.getCurrentItem();
            if(!InventoryUtils.isNull(item) && data.get(inventory).containsKey(item)){
                LinkedHashMap<ItemStack, ClickableItemHandler> items = data.get(inventory);
                items.get(item).run(player, item, type, event.getSlot(), event.getAction(), inventory);
                data.put(inventory, items);
                event.setCancelled(true);
                event.setResult(Event.Result.DENY);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void close(InventoryCloseEvent event){
        data.remove(event.getInventory());
    }
}
