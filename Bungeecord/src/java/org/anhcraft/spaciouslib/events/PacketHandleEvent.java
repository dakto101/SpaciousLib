package org.anhcraft.spaciouslib.events;

import io.netty.channel.Channel;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

/**
 * An event triggers when a packet was sent from the proxy or received by the proxy.<br>
 * This event is not ran in the main thread.
 */
public class PacketHandleEvent extends Event implements Cancellable {
    public enum Type{
        CLIENT_BOUND,
        SERVER_BOUND
    }

    private ProxiedPlayer p;
    private Object o;
    private boolean b = false;
    private Type t;
    private Channel c;

    public PacketHandleEvent(ProxiedPlayer p, Object o, Type t, Channel c){
        this.p = p;
        this.o = o;
        this.t = t;
        this.c = c;
    }

    public ProxiedPlayer getPlayer(){
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

    @Override
    public boolean isCancelled() {
        return this.b;
    }

    @Override
    public void setCancelled(boolean b) {
        this.b = b;
    }
}