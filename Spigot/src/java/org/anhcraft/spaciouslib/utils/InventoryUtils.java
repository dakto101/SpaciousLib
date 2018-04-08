package org.anhcraft.spaciouslib.utils;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
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

    public static String materialData2Str(MaterialData md){
        return md.getItemType().toString()+":"+md.getData();
    }

    public static MaterialData str2MaterialData(String s){
        if(s == null || s.equalsIgnoreCase("null")){
            return new MaterialData(Material.AIR);
        }
        String[] x = s.split(":");
        Material mt;
        if(StringUtils.isNumeric(x[0])) {
            mt = Material.getMaterial(CommonUtils.toIntegerNumber(x[0]));
        } else {
            mt = Material.valueOf(x[0].toUpperCase());
        }
        byte data = (byte) Integer.parseInt(x[1]);
        return new MaterialData(mt, data);
    }

    /**
     * Gets all material types for armor
     * @return list of material types
     */
    public static List<Material> getArmors(){
        List<Material> m = new ArrayList<>();
        m.add(Material.LEATHER_HELMET);
        m.add(Material.LEATHER_CHESTPLATE);
        m.add(Material.LEATHER_LEGGINGS);
        m.add(Material.LEATHER_BOOTS);
        m.add(Material.CHAINMAIL_HELMET);
        m.add(Material.CHAINMAIL_CHESTPLATE);
        m.add(Material.CHAINMAIL_LEGGINGS);
        m.add(Material.CHAINMAIL_BOOTS);
        m.add(Material.IRON_HELMET);
        m.add(Material.IRON_CHESTPLATE);
        m.add(Material.IRON_LEGGINGS);
        m.add(Material.IRON_BOOTS);
        m.add(Material.DIAMOND_HELMET);
        m.add(Material.DIAMOND_CHESTPLATE);
        m.add(Material.DIAMOND_LEGGINGS);
        m.add(Material.DIAMOND_BOOTS);
        m.add(Material.GOLD_HELMET);
        m.add(Material.GOLD_CHESTPLATE);
        m.add(Material.GOLD_LEGGINGS);
        m.add(Material.GOLD_BOOTS);
        if(GameVersion.is1_9Above()) {
            m.add(Material.ELYTRA);
        }
        return m;
    }
}
