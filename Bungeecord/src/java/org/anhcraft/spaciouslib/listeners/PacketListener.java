package org.anhcraft.spaciouslib.listeners;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.netty.ChannelWrapper;
import org.anhcraft.spaciouslib.events.PacketHandleEvent;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;

public class PacketListener implements Listener {
    private final static String PACKET_HANDLER = "SpaciousLib";

    public PacketListener(){
        for(ProxiedPlayer p : ProxyServer.getInstance().getPlayers()){
            init(p);
        }
    }

    public static Channel getChannel(ProxiedPlayer player){
        UserConnection uc = (UserConnection) player;
        ChannelWrapper cw = (ChannelWrapper) ReflectionUtils.getField("ch", uc.getClass(), uc);
        return (Channel) ReflectionUtils.getField("ch", cw.getClass(), cw);
    }

    public static void remove(ProxiedPlayer player){
        Channel channel = getChannel(player);
        if(channel.pipeline().get(PACKET_HANDLER) != null) {
            channel.pipeline().remove(PACKET_HANDLER);
        }
    }

    @EventHandler
    public void quit(ServerDisconnectEvent event){
        remove(event.getPlayer());
    }

    @EventHandler
    public void join(ServerConnectedEvent event){
        init(event.getPlayer());
    }

    private void init(ProxiedPlayer player) {
        Channel channel = getChannel(player);
        if(channel.pipeline().get(PACKET_HANDLER) == null) {
            channel.pipeline().addBefore("legacy-kick", PACKET_HANDLER,
                new ChannelDuplexHandler() {
                    @Override
                    public void write(ChannelHandlerContext c, Object o, ChannelPromise p) throws Exception {
                        PacketHandleEvent ev = new PacketHandleEvent(player, o, PacketHandleEvent.Type.CLIENT_BOUND, channel);
                        ProxyServer.getInstance().getPluginManager().callEvent(ev);
                        if(!ev.isCancelled()) {
                            super.write(c, ev.getPacket(), p);
                        }
                    }

                    @Override
                    public void channelRead(ChannelHandlerContext c, Object o) throws Exception {
                        PacketHandleEvent ev = new PacketHandleEvent(player, o,
                                PacketHandleEvent.Type.SERVER_BOUND, channel);
                        ProxyServer.getInstance().getPluginManager().callEvent(ev);
                        if(!ev.isCancelled()) {
                            super.channelRead(c, ev.getPacket());
                        }
                    }
                });
        }
    }
}