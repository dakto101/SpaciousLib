package org.anhcraft.spaciouslib.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class InventoryUtils {

    /**
     * Checks is the given item stack null or is air
     * @param item an item stack
     * @return true if yes
     */
    public static boolean isNull(ItemStack item){
        return item == null || item.getType() == Material.AIR || item.getType().toString().endsWith("_AIR");
    }

    /**
     * Checks is the given material null or is air
     * @param material material
     * @return true if yes
     */
    public static boolean isNull(Material material) {
        return material == null || material == Material.AIR || material.toString().endsWith("_AIR");
    }

    /**
     * Compares two given item stacks
     * @param a the first item stack
     * @param b the second item stack
     * @return true if they're equal
     */
    public static boolean compare(ItemStack a, ItemStack b) {
        if(isNull(a)){
            return isNull(b);
        }
        else if(isNull(b)){
            return isNull(a);
        }
        else {
            return a.equals(b);
        }
    }

    /**
     * Compares two given list of item stacks
     * @param a the first list
     * @param b the second list
     * @return true if they're equal
     */
    public static boolean compare(List<ItemStack> a, List<ItemStack> b) {
        if(a.size() == b.size()){
            int i = 0;
            for(ItemStack ai : a){
                if(!compare(ai, b.get(i))){
                    return false;
                }
                i++;
            }
            return true;
        }
        return false;
    }

    /**
     * Compares two given array of item stacks
     * @param a the first array
     * @param b the second array
     * @return true if they're equal
     */
    public static boolean compare(ItemStack[] a, ItemStack[] b) {
        if(a.length == b.length){
            int i = 0;
            for(ItemStack ai : a){
                if(!compare(ai, b[i])){
                    return false;
                }
                i++;
            }
            return true;
        }
        return false;
    }

    /**
     * Clones the given item safely
     * @param item an items tack
     * @return the clone of that item, returns null if the original is null
     */
    public static ItemStack clone(ItemStack item) {
        return isNull(item) ? null : item.clone();
    }
}
