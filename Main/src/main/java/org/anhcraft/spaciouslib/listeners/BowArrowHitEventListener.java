package org.anhcraft.spaciouslib.listeners;

import org.anhcraft.spaciouslib.events.BowArrowHitEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;

public class BowArrowHitEventListener implements Listener{
    private static LinkedHashMap<Entity, BowShootSession> data = new LinkedHashMap<>();

    @EventHandler
    public void shoot(EntityShootBowEvent e){
        data.put(e.getProjectile(), new BowShootSession(e.getBow(), e.getEntity()));
    }

    @EventHandler
    public void shoot(ProjectileHitEvent e){
        if(data.containsKey(e.getEntity()) && e.getEntity() instanceof Arrow){
            BowArrowHitEvent ev = new BowArrowHitEvent((Arrow) e.getEntity(), data.get(e.getEntity()), e);
            Bukkit.getServer().getPluginManager().callEvent(ev);
            data.remove(e.getEntity());
        }
    }

    public static class BowShootSession {
        public ItemStack bow;
        public LivingEntity shooter;

        public BowShootSession(ItemStack bow, LivingEntity shooter){
            this.bow = bow;
            this.shooter = shooter;
        }
    }
}
