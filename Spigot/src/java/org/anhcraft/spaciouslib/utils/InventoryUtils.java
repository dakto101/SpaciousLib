package org.anhcraft.spaciouslib.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class InventoryUtils {

    /**
     * Checks is an item stack null
     * @param item itemStack object
     * @return true if yes
     */
    public static boolean isNull(ItemStack item){
        return item == null || item.getType() == Material.AIR;
    }

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

    public static ItemStack clone(ItemStack item) {
        return isNull(item) ? null : item.clone();
    }
}
