package org.anhcraft.spaciouslib.events;

import org.anhcraft.spaciouslib.utils.Group;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

/**
 * An event triggers when an arrow (which was shoot by a living entity with bow) hit something
 */
public class BowArrowHitEvent extends Event {
    public static final HandlerList handlers = new HandlerList();
    private Arrow arrow;
    private Group<LivingEntity, ItemStack> session;
    private ProjectileHitEvent event;

    public BowArrowHitEvent(Arrow arrow, Group<LivingEntity, ItemStack> session, ProjectileHitEvent event){
        this.arrow = arrow;
        this.session = session;
        this.event = event;
    }

    public Arrow getArrow(){
        return this.arrow;
    }

    public ProjectileHitEvent getEvent(){
        return this.event;
    }

    public LivingEntity getShooter(){
        return this.session.getA();
    }

    public ItemStack getBow(){
        return this.session.getB();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}