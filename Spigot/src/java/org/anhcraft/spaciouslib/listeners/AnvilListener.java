package org.anhcraft.spaciouslib.listeners;

import org.anhcraft.spaciouslib.anvil.AnvilBuilder;
import org.anhcraft.spaciouslib.anvil.Anvil.Handler;
import org.anhcraft.spaciouslib.anvil.Anvil.Slot;
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

public class AnvilListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if ((event.getWhoClicked() instanceof Player)) {
            Player player = (Player) event.getWhoClicked();
            Inventory inv = CompatibilityInventoryClickEvent.getInventory(event);
            if (inv != null && AnvilBuilder.data.containsKey(player)){
                ItemStack item = event.getCurrentItem();
                Group<Inventory, Anvil.Handler> anvil = AnvilBuilder.data.get(player);
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
            if (inv != null && AnvilBuilder.data.containsKey(player)){
                Group<Inventory, Anvil.Handler> anvil = AnvilBuilder.data.get(player);
                if(anvil.getA().equals(inv)) {
                    inv.setItem(Anvil.Slot.INPUT_LEFT.getID(), null);
                    inv.setItem(Anvil.Slot.INPUT_RIGHT.getID(), null);
                    inv.setItem(Anvil.Slot.OUTPUT.getID(), null);
                    AnvilBuilder.data.remove(player);
                }
            }
        }
    }
}
