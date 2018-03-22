package org.anhcraft.spaciouslib.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.lang.reflect.Field;

/**
 * An event triggers when a packet was sent from the server or received by the server.<br>
 * Warning when it was <b>sent from the server</b>, you may have stack overflow error if you <b>send a new packet</b> or access some <b>Bukkit APIs</b> by using that event.
 */
public class PacketHandleEvent extends Event implements Cancellable {
    public enum Type{
        CLIENT_BOUND,
        SERVER_BOUND
    }

    private static final HandlerList handlers = new HandlerList();
    private Player p;
    private Object o;
    private boolean b = false;
    private Type t;

    public PacketHandleEvent(Player p, Object o, Type t){
        this.p = p;
        this.o = o;
        this.t = t;
    }

    public Player getPlayer(){
        return this.p;
    }

    public Object getPacket(){
        return this.o;
    }

    public Type getType(){
        return this.t;
    }

    public void setPacket(Object o){
        this.o = o;
    }

    public void setPacketValue(String n, Object o){
        Field f = null;
        try {
            f = this.o.getClass().getDeclaredField(n);
        } catch(NoSuchFieldException e) {
            e.printStackTrace();
        }
        f.setAccessible(true);
        try {
            f.set(this.o, o);
        } catch(IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Object getPacketValue(String n){
        Field f = null;
        try {
            f = this.o.getClass().getDeclaredField(n);
        } catch(NoSuchFieldException e) {
            e.printStackTrace();
        }
        f.setAccessible(true);
        try {
            return f.get(this.o);
        } catch(IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return this.b;
    }

    @Override
    public void setCancelled(boolean b) {
        this.b = b;
    }
}