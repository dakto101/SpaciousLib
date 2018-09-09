package org.anhcraft.spaciouslib.listeners;

import org.anhcraft.spaciouslib.events.BowArrowHitEvent;
import org.anhcraft.spaciouslib.utils.Group;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;

public class BowArrowHitEventListener implements Listener{
    public static final LinkedHashMap<Entity, Group<LivingEntity, ItemStack>> data = new LinkedHashMap<>();

    @EventHandler
    public void shoot(EntityShootBowEvent e){
        data.put(e.getProjectile(), new Group<>(e.getEntity(), e.getBow()));
    }

    @EventHandler
    public void shoot(ProjectileHitEvent e){
        if(data.containsKey(e.getEntity()) && e.getEntity() instanceof Arrow){
            BowArrowHitEvent ev = new BowArrowHitEvent((Arrow) e.getEntity(), data.get(e.getEntity()), e);
            Bukkit.getServer().getPluginManager().callEvent(ev);
            data.remove(e.getEntity());
        }
    }

    @EventHandler
    public void quit(PlayerQuitEvent event){
        data.remove(event.getPlayer());
    }

    @EventHandler
    public void death(EntityDeathEvent event){
        data.remove(event.getEntity());
    }
}
