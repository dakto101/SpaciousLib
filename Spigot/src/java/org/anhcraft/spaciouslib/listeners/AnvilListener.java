package org.anhcraft.spaciouslib.listeners;

import org.anhcraft.spaciouslib.anvil.Anvil;
import org.anhcraft.spaciouslib.anvil.Anvil.Handler;
import org.anhcraft.spaciouslib.compatibility.CompatibilityInventoryClickEvent;
import org.anhcraft.spaciouslib.inventory.ItemManager;
import org.anhcraft.spaciouslib.utils.Group;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.UUID;

public class AnvilListener implements Listener {
    public static LinkedHashMap<UUID, Group<Inventory, Handler>> data = new LinkedHashMap<>();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if ((event.getWhoClicked() instanceof Player)) {
            Player player = (Player) event.getWhoClicked();
            Inventory inv = CompatibilityInventoryClickEvent.getInventory(event);
            if (inv != null && data.containsKey(player.getUniqueId())){
                ItemStack item = event.getCurrentItem();
                Group<Inventory, Anvil.Handler> anvil = data.get(player.getUniqueId());
                ItemStack output = event.getInventory().getItem(Anvil.Slot.OUTPUT.getID());
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
                                anvil.getB().result(player, input, item, Anvil.Slot.INPUT_LEFT);
                                break;
                            case 1:
                                anvil.getB().result(player, input, item, Anvil.Slot.INPUT_RIGHT);
                                break;
                            case 2:
                                anvil.getB().result(player, input, item, Anvil.Slot.OUTPUT);
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
                Group<Inventory, Anvil.Handler> anvil = data.get(player.getUniqueId());
                if(anvil.getA().equals(inv)) {
                    inv.setItem(Anvil.Slot.INPUT_LEFT.getID(), null);
                    inv.setItem(Anvil.Slot.INPUT_RIGHT.getID(), null);
                    inv.setItem(Anvil.Slot.OUTPUT.getID(), null);
                    data.remove(player.getUniqueId());
                }
            }
        }
    }
}
