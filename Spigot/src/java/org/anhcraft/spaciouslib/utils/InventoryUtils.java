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
     * @return the clone of the item
     */
    public static ItemStack clone(ItemStack item) {
        return isNull(item) ? null : item.clone();
    }

    /**
     * Serializes the given material data to string
     * @param md MaterialData object
     * @return a string represents for the object
     */
    public static String materialData2Str(MaterialData md){
        return md.getItemType().toString()+":"+md.getData();
    }

    /**
     * Deserialize the given string to its material data
     * @param s a string represents for the object
     * @return the material data
     */
    public static MaterialData str2MaterialData(String s){
        if(s == null || s.equalsIgnoreCase("null")){
            return new MaterialData(Material.AIR);
        }
        String[] x = s.split(":");
        Material mt;
        if(!GameVersion.is1_13Above() && StringUtils.isNumeric(x[0])) {
            mt = (Material) ReflectionUtils.getStaticMethod("getMaterial",
                    Material.class, new Group<>(
                    new Class<?>[]{int.class},
                    new Object[]{CommonUtils.toIntegerNumber(x[0])}
            ));
        } else {
            mt = Material.valueOf(x[0].toUpperCase());
        }
        if(x.length == 2) {
            return new MaterialData(mt, (byte) Integer.parseInt(x[1]));
        } else {
            return new MaterialData(mt, (byte) 0);
        }
    }

    /**
     * Gets all material types for skulls
     */
    public static List<Material> getSkulls(){
        List<Material> m = new ArrayList<>();
        if(GameVersion.is1_13Above()){
            m.add(Material.CREEPER_HEAD);
            m.add(Material.DRAGON_HEAD);
            m.add(Material.PLAYER_HEAD);
            m.add(Material.ZOMBIE_HEAD);
            m.add(Material.SKELETON_SKULL);
            m.add(Material.WITHER_SKELETON_SKULL);
        } else {
            m.add(Material.valueOf("SKULL_ITEM"));
        }
        return m;
    }

    /**
     * Gets all material types for armor
     */
    public static List<Material> getArmors(){
        List<Material> m = new ArrayList<>();
        if(GameVersion.is1_9Above()) {
            m.add(Material.ELYTRA);
        }
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
        if(GameVersion.is1_13Above()){
            m.add(Material.GOLDEN_HELMET);
            m.add(Material.GOLDEN_CHESTPLATE);
            m.add(Material.GOLDEN_LEGGINGS);
            m.add(Material.GOLDEN_BOOTS);
        } else {
            m.add(Material.valueOf("GOLD_HELMET"));
            m.add(Material.valueOf("GOLD_CHESTPLATE"));
            m.add(Material.valueOf("GOLD_LEGGINGS"));
            m.add(Material.valueOf("GOLD_BOOTS"));
        }
        return m;
    }
}
