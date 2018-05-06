package org.anhcraft.spaciouslib.events;

import io.netty.channel.Channel;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.lang.reflect.Field;

/**
 * An event triggers when a packet was sent from the server or received by the server.<br>
 * This event is not ran in the main thread, so there are some problems if you access Bukkit APIs.
 * Warning: when it was <b>sent from the server</b>, if you use this event to <b>send new packets</b> or access some <b>Bukkit APIs</b>, you may have the stack overflow error.
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
    private Channel c;

    public PacketHandleEvent(Player p, Object o, Type t, Channel c){
        this.p = p;
        this.o = o;
        this.t = t;
        this.c = c;
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

    public Channel getChannel(){
        return this.c;
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