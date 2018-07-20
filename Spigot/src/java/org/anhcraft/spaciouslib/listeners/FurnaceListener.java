package org.anhcraft.spaciouslib.listeners;

import org.anhcraft.spaciouslib.annotations.PlayerCleaner;
import org.anhcraft.spaciouslib.inventory.furnace.FurnaceHandler;
import org.anhcraft.spaciouslib.inventory.furnace.FurnaceSlot;
import org.anhcraft.spaciouslib.utils.CompatibilityUtils;
import org.anhcraft.spaciouslib.utils.Group;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.UUID;

public class FurnaceListener implements Listener {
    @PlayerCleaner
    public static LinkedHashMap<UUID, Group<Inventory, FurnaceHandler>> data = new LinkedHashMap<>();

    @EventHandler
    public void quit(PlayerQuitEvent event){
        data.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if ((event.getWhoClicked() instanceof Player)) {
            Player player = (Player) event.getWhoClicked();
            Inventory inv = CompatibilityUtils.getInventory(event);
            if (inv != null && data.containsKey(player.getUniqueId())){
                ItemStack item = event.getCurrentItem();
                Group<Inventory, FurnaceHandler> furnace = data.get(player.getUniqueId());
                if(furnace.getA().equals(inv)) {
                    event.setCancelled(true);
                    int slot = event.getRawSlot();
                    switch(slot) {
                        case 0:
                            furnace.getB().handle(player, item, FurnaceSlot.INGREDIENT);
                            break;
                        case 1:
                            furnace.getB().handle(player, item, FurnaceSlot.FUEL);
                            break;
                        case 2:
                            furnace.getB().handle(player, item, FurnaceSlot.OUTPUT);
                            break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if ((event.getPlayer() instanceof Player)) {
            Player player = (Player) event.getPlayer();
            Inventory inv = event.getInventory();
            if (inv != null && data.containsKey(player.getUniqueId())){
                Group<Inventory, FurnaceHandler> anvil = data.get(player.getUniqueId());
                if(anvil.getA().equals(inv)) {
                    inv.setItem(FurnaceSlot.INGREDIENT.getId(), null);
                    inv.setItem(FurnaceSlot.FUEL.getId(), null);
                    inv.setItem(FurnaceSlot.OUTPUT.getId(), null);
                    data.remove(player.getUniqueId());
                }
            }
        }
    }
}
