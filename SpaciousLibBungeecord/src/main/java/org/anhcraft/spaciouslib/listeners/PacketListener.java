package org.anhcraft.spaciouslib.listeners;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.netty.ChannelWrapper;
import org.anhcraft.spaciouslib.SpaciousLib;
import org.anhcraft.spaciouslib.annotations.AnnotationHandler;
import org.anhcraft.spaciouslib.annotations.PacketHandler;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PacketListener implements Listener {
    private static final HashMap<UUID, Integer> tasks = new HashMap<>();

    public enum BoundType{
        CLIENT_BOUND,
        SERVER_BOUND;
    }

    public class Handler{
        public Handler(UUID player, Object packet, BoundType bound) {
            this.player = player;
            this.packet = packet;
            this.bound = bound;
        }

        public BoundType getBound() {
            return bound;
        }

        private UUID player;
        private Object packet;
        private boolean cancelled = false;
        private BoundType bound;

        public void setCancelled(boolean cancel){
            this.cancelled = cancel;
        }

        public boolean isCancelled() {
            return cancelled;
        }

        public Object getPacket() {
            return packet;
        }

        public void setPacket(Object packet) {
            this.packet = packet;
        }

        public ProxiedPlayer getProxiedPlayer() {
            return BungeeCord.getInstance().getPlayer(player);
        }

        public boolean isAsynchronous() {
            return true;
        }

        public Object getPacketValue(String field){
            return ReflectionUtils.getField(field, packet.getClass(), packet);
        }

        public void setPacketValue(String field, Object value){
            ReflectionUtils.setField(field, packet.getClass(), packet, value);
        }
    }

    private static final String PACKET_HANDLER = "SpaciousLib";

    public PacketListener(){
        for (ProxiedPlayer p : BungeeCord.getInstance().getPlayers()) {
            init(p);
        }
    }

    public static Channel getChannel(ProxiedPlayer player) {
        UserConnection uc = (UserConnection)player;
        ChannelWrapper cw = (ChannelWrapper)ReflectionUtils.getField("ch", uc.getClass(), uc);
        return (Channel) ReflectionUtils.getField("ch", cw.getClass(), cw);
    }

    public static void remove(ProxiedPlayer player) {
        Channel channel = getChannel(player);
        BungeeCord.getInstance().getScheduler().cancel(tasks.get(player.getUniqueId()));
        if(channel.pipeline().get(PACKET_HANDLER) != null) {
            channel.pipeline().remove(PACKET_HANDLER);
        }
        tasks.remove(player.getUniqueId());
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
        UUID uuid = player.getUniqueId();
        Channel channel = getChannel(player);
        if (channel.pipeline().get(PACKET_HANDLER) == null) {
            int t = BungeeCord.getInstance().getScheduler().runAsync(SpaciousLib.instance, new Runnable() {
                @Override
                public void run() {
                    channel.pipeline().addBefore("legacy-kick", PACKET_HANDLER, new ChannelDuplexHandler() {
                        public void write(ChannelHandlerContext c, Object o, ChannelPromise p)
                                throws Exception {
                            Handler handler = new Handler(uuid, o, BoundType.CLIENT_BOUND);
                            for(Class clazz : AnnotationHandler.getClasses().keySet()) {
                                for(Method method : clazz.getDeclaredMethods()) {
                                    method.setAccessible(true);
                                    if(method.isAnnotationPresent(PacketHandler.class) &&
                                            method.getParameterTypes().length == 1 &&
                                            Handler.class.isAssignableFrom(method.getParameterTypes()[0])) {
                                        List<Object> x = AnnotationHandler.getClasses().get(clazz);
                                        for(Object obj : x) {
                                            method.invoke(obj, handler);
                                        }
                                    }
                                }
                            }
                            if(!handler.isCancelled()) {
                                super.write(c, handler.getPacket(), p);
                            }
                        }

                        public void channelRead(ChannelHandlerContext c, Object o)
                                throws Exception {
                            Handler handler = new Handler(uuid, o, BoundType.SERVER_BOUND);
                            for(Class clazz : AnnotationHandler.getClasses().keySet()) {
                                for(Method method : clazz.getDeclaredMethods()) {
                                    method.setAccessible(true);
                                    if(method.isAnnotationPresent(PacketHandler.class) &&
                                            method.getParameterTypes().length == 1 &&
                                            Handler.class.isAssignableFrom(method.getParameterTypes()[0])) {
                                        List<Object> x = AnnotationHandler.getClasses().get(clazz);
                                        for(Object obj : x) {
                                            method.invoke(obj, handler);
                                        }
                                    }
                                }
                            }
                            if(!handler.isCancelled()) {
                                super.channelRead(c, handler.getPacket());
                            }
                        }
                    });
                }
            }).getId();
            tasks.put(uuid, t);
        }
    }
}
