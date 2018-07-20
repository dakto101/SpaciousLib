package org.anhcraft.spaciouslib.listeners;

import org.anhcraft.spaciouslib.annotations.PlayerCleaner;
import org.anhcraft.spaciouslib.inventory.ItemManager;
import org.anhcraft.spaciouslib.inventory.anvil.AnvilHandler;
import org.anhcraft.spaciouslib.inventory.anvil.AnvilSlot;
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

public class AnvilListener implements Listener {
    @PlayerCleaner
    public static LinkedHashMap<UUID, Group<Inventory, AnvilHandler>> data = new LinkedHashMap<>();

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
                Group<Inventory, AnvilHandler> anvil = data.get(player.getUniqueId());
                ItemStack output = event.getInventory().getItem(AnvilSlot.OUTPUT.getId());
                if(anvil.getA().equals(inv)) {
                    event.setCancelled(true);
                    if(output != null) {
                        int slot = event.getRawSlot();
                        String input = new ItemManager(output).getName();
                        if(input == null) {
                            input = "";
                        }
                        switch(slot) {
                            case 0:
                                anvil.getB().result(player, input, item, AnvilSlot.INPUT_LEFT);
                                break;
                            case 1:
                                anvil.getB().result(player, input, item, AnvilSlot.INPUT_RIGHT);
                                break;
                            case 2:
                                anvil.getB().result(player, input, item, AnvilSlot.OUTPUT);
                                break;
                        }
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
                Group<Inventory, AnvilHandler> anvil = data.get(player.getUniqueId());
                if(anvil.getA().equals(inv)) {
                    inv.setItem(AnvilSlot.INPUT_LEFT.getId(), null);
                    inv.setItem(AnvilSlot.INPUT_RIGHT.getId(), null);
                    inv.setItem(AnvilSlot.OUTPUT.getId(), null);
                    data.remove(player.getUniqueId());
                }
            }
        }
    }
}
