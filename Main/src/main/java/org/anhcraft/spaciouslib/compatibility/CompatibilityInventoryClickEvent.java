package org.anhcraft.spaciouslib.compatibility;

import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.inventory.Inventory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CompatibilityInventoryClickEvent {
    /**
     * Gets the inventory of the InventoryClickEvent event which is compatible with versions
     * @param event an InventoryClickEvent event
     * @return the inventory of that event
     */
    public static Inventory getInventory(InventoryClickEvent event){
        Class<?> c = event.getClass().getSuperclass().getSuperclass();
        if(event.getClass().equals(InventoryCreativeEvent.class)
        || event.getClass().equals(CraftItemEvent.class)) {
            c = c.getSuperclass();
        }
        try {
            Method m = c.getDeclaredMethod("getInventory");
            return (Inventory) m.invoke(event);
        } catch(NoSuchMethodException | IllegalAccessException
                | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}
