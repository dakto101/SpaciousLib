package org.anhcraft.spaciouslib.utils;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class MaterialUtils {
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
