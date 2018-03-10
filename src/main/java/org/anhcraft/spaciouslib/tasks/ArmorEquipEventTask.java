package org.anhcraft.spaciouslib.tasks;

import org.anhcraft.spaciouslib.events.ArmorEquipEvent;
import org.anhcraft.spaciouslib.inventory.EquipSlot;
import org.anhcraft.spaciouslib.utils.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedHashMap;

public class ArmorEquipEventTask extends BukkitRunnable implements Listener {
    private static LinkedHashMap<Player,
            LinkedHashMap<EquipSlot, ItemStack>> data = new LinkedHashMap<>();

    @EventHandler
    public void quit(PlayerQuitEvent e){
        data.remove(e.getPlayer());
    }

    @Override
    public void run() {
        for(Player p : Bukkit.getServer().getOnlinePlayers()){
            ItemStack h = InventoryUtils.clone(p.getInventory().getHelmet());
            ItemStack c = InventoryUtils.clone(p.getInventory().getChestplate());
            ItemStack l = InventoryUtils.clone(p.getInventory().getLeggings());
            ItemStack b = InventoryUtils.clone(p.getInventory().getBoots());
            LinkedHashMap<EquipSlot, ItemStack> x = new LinkedHashMap<>();
            if(data.containsKey(p)){
                x = data.get(p);
                ItemStack oh = x.get(EquipSlot.HEAD);
                ItemStack oc = x.get(EquipSlot.CHEST);
                ItemStack ol = x.get(EquipSlot.LEGS);
                ItemStack ob = x.get(EquipSlot.FEET);
                if(!InventoryUtils.compare(h, oh)){
                    ArmorEquipEvent e = new ArmorEquipEvent(p, oh, h, EquipSlot.HEAD);
                    Bukkit.getServer().getPluginManager().callEvent(e);
                    x.put(EquipSlot.HEAD, e.getNewArmor());
                }
                if(!InventoryUtils.compare(c, oc)){
                    ArmorEquipEvent e = new ArmorEquipEvent(p, oc, c, EquipSlot.CHEST);
                    Bukkit.getServer().getPluginManager().callEvent(e);
                    x.put(EquipSlot.CHEST, e.getNewArmor());
                }
                if(!InventoryUtils.compare(l, ol)){
                    ArmorEquipEvent e = new ArmorEquipEvent(p, ol, l, EquipSlot.LEGS);
                    Bukkit.getServer().getPluginManager().callEvent(e);
                    x.put(EquipSlot.LEGS, e.getNewArmor());
                }
                if(!InventoryUtils.compare(b, ob)){
                    ArmorEquipEvent e = new ArmorEquipEvent(p, ob, b, EquipSlot.FEET);
                    Bukkit.getServer().getPluginManager().callEvent(e);
                    x.put(EquipSlot.FEET, e.getNewArmor());
                }
            } else {
                x.put(EquipSlot.HEAD, h);
                x.put(EquipSlot.CHEST, c);
                x.put(EquipSlot.LEGS, l);
                x.put(EquipSlot.FEET, b);
                for(EquipSlot r : x.keySet()){
                    ArmorEquipEvent e = new ArmorEquipEvent(p, null, x.get(r), r);
                    Bukkit.getServer().getPluginManager().callEvent(e);
                }
            }
            data.put(p, x);
        }
    }
}