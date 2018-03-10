package org.anhcraft.spaciouslib.listeners;

import org.anhcraft.spaciouslib.compatibility.CompatibilityInventoryClickEvent;
import org.anhcraft.spaciouslib.inventory.InteractItemRunnable;
import org.bukkit.Material;
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

public class InteractItemListener implements Listener {
    private static LinkedHashMap<Inventory, LinkedHashMap<ItemStack, InteractItemRunnable>> data = new LinkedHashMap<>();

    public static void a(Inventory inv, ItemStack item, InteractItemRunnable run){
        LinkedHashMap<ItemStack, InteractItemRunnable> items = new LinkedHashMap<>();
        if(data.containsKey(inv)){
            items = data.get(inv);
        }
        items.put(item, run);
        data.put(inv, items);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void interact(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = CompatibilityInventoryClickEvent.getInventory(event);
        ClickType type = event.getClick();
        if (inventory != null && data.containsKey(inventory)){
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
            ItemStack item = event.getCurrentItem();
            if(item != null && !item.getType().equals(Material.AIR) && data.get(inventory).containsKey(item)){
                LinkedHashMap<ItemStack, InteractItemRunnable> items = data.get(inventory);
                items.get(item).run(player, item, type, event.getSlot());
                data.put(inventory, items);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void close(InventoryCloseEvent event){
        if(data.containsKey(event.getInventory())){
            data.remove(event.getInventory());
        }
    }
}
