package org.anhcraft.spaciouslib.utils;

import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.inventory.Inventory;

public class CompatibilityUtils {
    /**
     * Gets the inventory of the InventoryClickEvent event.<br>
     * Because after 1.8 versions, the method "getClickedInventory" was renamed to "getInventory"
     * @param event an InventoryClickEvent event
     * @return the inventory of that event
     */
    public static Inventory getInventory(InventoryClickEvent event){
        if(GameVersion.is1_9Above()) {
            return event.getClickedInventory();
        } else {
            Class<?> c = event.getClass().getSuperclass().getSuperclass();
            if(event.getClass().equals(InventoryCreativeEvent.class)
                    || event.getClass().equals(CraftItemEvent.class)) {
                c = c.getSuperclass();
            }
            return (Inventory) ReflectionUtils.getMethod("getInventory", c, event);
        }
    }
}
