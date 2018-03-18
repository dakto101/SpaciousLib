package org.anhcraft.spaciouslib.events;

import org.anhcraft.spaciouslib.listeners.BowArrowHitEventListener;
import org.bukkit.entity.Arrow;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ProjectileHitEvent;

/**
 * An event triggers when an arrow (which was shoot by a living entity with bow) hit something
 */
public class BowArrowHitEvent extends Event {
    public static final HandlerList handlers = new HandlerList();
    private Arrow arrow;
    private BowArrowHitEventListener.BowShootSession session;
    private ProjectileHitEvent event;

    public BowArrowHitEvent(Arrow arrow, BowArrowHitEventListener.BowShootSession session, ProjectileHitEvent event){
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

    public BowArrowHitEventListener.BowShootSession getSession(){
        return this.session;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}