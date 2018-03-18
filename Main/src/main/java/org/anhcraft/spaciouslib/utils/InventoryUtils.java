package org.anhcraft.spaciouslib.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventoryUtils {

    public static boolean isNull(ItemStack item){
        return item == null || item.getType() == Material.AIR;
    }

    /**
     * Divides objects to each page of an inventory based on the index
     *
     * @param all all objects
     * @param index index of page (start from 0)
     * @param max maximum objects in each page
     * @return objects in that page
     */
    public static <X> List<X> getItemsAtPage(List<X> all, int index, int max){
        int first = max * index;
        int end = max * index + (max - 1);
        List<X> filter = new ArrayList<>();
        int i = 0;
        for(X v : all){
            if(first <= i && i <= end){
                filter.add(v);
            }
            i++;
        }
        return filter;
    }

    public static boolean compare(ItemStack a, ItemStack b) {
        if(isNull(a)){
            return isNull(b);
        }
        else if(isNull(b)){
            return isNull(a);
        }
        else {
            return a.hashCode() == b.hashCode();
        }
    }

    public static ItemStack clone(ItemStack item) {
        return isNull(item) ? null : item.clone();
    }
}
