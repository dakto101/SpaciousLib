package org.anhcraft.spaciouslib.listeners;

import org.anhcraft.spaciouslib.SpaciousLib;
import org.anhcraft.spaciouslib.annotations.AnnotationHandler;
import org.anhcraft.spaciouslib.annotations.PlayerCleaner;
import org.anhcraft.spaciouslib.events.BowArrowHitEvent;
import org.anhcraft.spaciouslib.events.PlayerJumpEvent;
import org.anhcraft.spaciouslib.inventory.ClickableItemHandler;
import org.anhcraft.spaciouslib.inventory.ItemManager;
import org.anhcraft.spaciouslib.inventory.anvil.AnvilHandler;
import org.anhcraft.spaciouslib.inventory.anvil.AnvilSlot;
import org.anhcraft.spaciouslib.placeholder.PlaceholderAPI;
import org.anhcraft.spaciouslib.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.*;

public class PlayerListener implements Listener {
    @PlayerCleaner
    public static final LinkedHashMap<UUID, Location> freezedPlayers = new LinkedHashMap<>();
    @PlayerCleaner
    public static final LinkedHashMap<UUID, Group<Inventory, AnvilHandler>> anvilPlayers = new LinkedHashMap<>();
    public static final LinkedHashMap<Entity, Group<LivingEntity, ItemStack>> bowArrows = new LinkedHashMap<>();
    public static final HashMap<Inventory, Table<ClickableItemHandler>> invTracker = new HashMap<>();

    @EventHandler
    public void join(PlayerJoinEvent event){
        new BukkitRunnable() {
            @Override
            public void run() {
                PlaceholderAPI.updateCache(event.getPlayer());
            }
        }.runTaskAsynchronously(SpaciousLib.instance);
    }

    @EventHandler
    public void death(EntityDeathEvent event){
        bowArrows.remove(event.getEntity());
    }
    
    @EventHandler
    public void shoot(EntityShootBowEvent e){
        bowArrows.put(e.getProjectile(), new Group<>(e.getEntity(), e.getBow()));
    }

    @EventHandler
    public void hit(ProjectileHitEvent e){
        if(bowArrows.containsKey(e.getEntity()) && e.getEntity() instanceof Arrow){
            BowArrowHitEvent ev = new BowArrowHitEvent((Arrow) e.getEntity(), bowArrows.get(e.getEntity()), e);
            Bukkit.getServer().getPluginManager().callEvent(ev);
            bowArrows.remove(e.getEntity());
        }
    }

    @EventHandler
    public void teleport(PlayerTeleportEvent e){
        if(freezedPlayers.containsKey(e.getPlayer().getUniqueId())){
            Location last = freezedPlayers.get(e.getPlayer().getUniqueId());
            if(last.getWorld().equals(e.getTo().getWorld())) {
                double offX = e.getTo().getX() - last.getX();
                double offY = e.getTo().getY() - last.getY();
                double offZ = e.getTo().getZ() - last.getZ();
                if(offX * offX + offY * offY + offZ * offZ >= 1) {
                    e.setCancelled(true);
                }
            } else {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void move(PlayerMoveEvent e){
        if(freezedPlayers.containsKey(e.getPlayer().getUniqueId())){
            Location last = freezedPlayers.get(e.getPlayer().getUniqueId());
            double offX = e.getTo().getX() - last.getX();
            double offY = e.getTo().getY() - last.getY();
            double offZ = e.getTo().getZ() - last.getZ();
            if(offX*offX+offY*offY+offZ*offZ >= 1) {
                e.setCancelled(true);
            }
        } else {
            double a = e.getTo().getY() - e.getFrom().getY();
            if(a == 0.33319999363422426) {
                PlayerJumpEvent ev = new PlayerJumpEvent(e.getPlayer(), e.getFrom().getX() == e.getTo().getX() && e.getFrom().getZ() == e.getTo().getZ());
                Bukkit.getServer().getPluginManager().callEvent(ev);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if ((event.getWhoClicked() instanceof Player)) {
            Player player = (Player) event.getWhoClicked();
            Inventory inv = CompatibilityUtils.getInventory(event);
            if (inv != null){
                if(anvilPlayers.containsKey(player.getUniqueId())){
                    ItemStack item = event.getCurrentItem();
                    Group<Inventory, AnvilHandler> anvil = anvilPlayers.get(player.getUniqueId());
                    ItemStack output = event.getInventory().getItem(AnvilSlot.OUTPUT.getId());
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
                                    anvil.getB().handle(player, input, item, AnvilSlot.INPUT_LEFT);
                                    break;
                                case 1:
                                    anvil.getB().handle(player, input, item, AnvilSlot.INPUT_RIGHT);
                                    break;
                                case 2:
                                    anvil.getB().handle(player, input, item, AnvilSlot.OUTPUT);
                                    break;
                            }
                        }
                    }
                } else if(invTracker.containsKey(inv) && event.getRawSlot() < inv.getSize()) {
                    ItemStack item = event.getCurrentItem();
                    if(!InventoryUtils.isNull(item)){
                        ClickableItemHandler c = invTracker.get(inv).get(event.getRawSlot());
                        if(c != null) {
                            event.setCancelled(true);
                            event.setResult(Event.Result.DENY);
                            c.run(player, item, event.getClick(), event.getRawSlot(), event.getAction(), inv);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        invTracker.remove(event.getInventory());
        if ((event.getPlayer() instanceof Player)) {
            Player player = (Player) event.getPlayer();
            Inventory inv = event.getInventory();
            if (inv != null && anvilPlayers.containsKey(player.getUniqueId())){
                Group<Inventory, AnvilHandler> anvil = anvilPlayers.get(player.getUniqueId());
                if(anvil.getA().equals(inv)) {
                    for(AnvilSlot slot : AnvilSlot.values()){
                        inv.setItem(slot.getId(), null);
                    }
                    anvilPlayers.remove(player.getUniqueId());
                }
            }
        }
    }
    
    @EventHandler
    public void quit(PlayerQuitEvent event){
        anvilPlayers.remove(event.getPlayer().getUniqueId());
        bowArrows.remove(event.getPlayer());
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Class clazz : AnnotationHandler.getClasses().keySet()) {
                    for(Field field : clazz.getDeclaredFields()) {
                        field.setAccessible(true);
                        if(field.isAnnotationPresent(PlayerCleaner.class)) {
                            List<Object> x = AnnotationHandler.getClasses().get(clazz);
                            for(Object obj : x) {
                                a(field, obj, event.getPlayer().getUniqueId());
                            }
                        }
                    }
                }
            }
        }.runTaskAsynchronously(SpaciousLib.instance);
    }

    private void a(Field field, Object obj, UUID uniqueId) {
        try {
            if(Collection.class.isAssignableFrom(field.getType())) {
                Collection v = (Collection) field.get(obj);
                v.remove(uniqueId);
            } else if(Map.class.isAssignableFrom(field.getType())) {
                Map<UUID, Object> v = (Map<UUID, Object>) field.get(obj);
                v.remove(uniqueId);
            } else if(TimedMap.class.isAssignableFrom(field.getType())) {
                TimedMap<UUID, Object> v = (TimedMap<UUID, Object>) field.get(obj);
                v.remove(uniqueId);
            } else if(TimedSet.class.isAssignableFrom(field.getType())) {
                TimedSet<UUID> v = (TimedSet<UUID>) field.get(obj);
                v.remove(uniqueId);
            } else if(TimedList.class.isAssignableFrom(field.getType())) {
                TimedList<UUID> v = (TimedList<UUID>) field.get(obj);
                v.remove(uniqueId);
            } else if(UUID.class.isAssignableFrom(field.getType())) {
                field.set(obj, null);
            }
        } catch(IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
