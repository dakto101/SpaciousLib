package org.anhcraft.spaciouslib.listeners;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.anhcraft.spaciouslib.SpaciousLib;
import org.anhcraft.spaciouslib.annotations.AnnotationHandler;
import org.anhcraft.spaciouslib.annotations.PacketHandler;
import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PacketListener implements Listener{
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

        public Player getPlayer() {
            return Bukkit.getServer().getPlayer(player);
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
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            init(p);
        }
    }

    public static Channel getChannel(Player player) {
        GameVersion v = GameVersion.getVersion();
        try {
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + v.toString() + ".entity.CraftPlayer");
            Class<?> nmsEntityPlayerClass = Class.forName("net.minecraft.server." + v.toString() + ".EntityPlayer");
            Class<?> playerConnClass = Class.forName("net.minecraft.server." + v.toString() + ".PlayerConnection");
            Class<?> networkManagerClass = Class.forName("net.minecraft.server." + v.toString() + ".NetworkManager");
            Object craftPlayer = ReflectionUtils.cast(craftPlayerClass, player);
            Object nmsEntityPlayer = ReflectionUtils.getMethod("getHandle", craftPlayerClass, craftPlayer);
            Object playerConn = ReflectionUtils.getField("playerConnection", nmsEntityPlayerClass, nmsEntityPlayer);
            Object networkManager = ReflectionUtils.getField("networkManager", playerConnClass, playerConn);
            return (Channel) ReflectionUtils.getField("channel", networkManagerClass, networkManager);
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }

    public static void remove(Player player) {
        synchronized(tasks) {
            Channel channel = getChannel(player);
            Bukkit.getServer().getScheduler().cancelTask(tasks.get(player.getUniqueId()));
            if(channel.pipeline().get(PACKET_HANDLER) != null) {
                channel.pipeline().remove(PACKET_HANDLER);
            }
            tasks.remove(player.getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void quit(PlayerQuitEvent event) {
        remove(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void join(PlayerJoinEvent event) {
        init(event.getPlayer());
    }

    private void init(Player player) {
        UUID uuid = player.getUniqueId();
        Channel channel = getChannel(player);
        if (channel.pipeline().get(PACKET_HANDLER) == null) {
            synchronized(tasks) {
                int id = new BukkitRunnable() {
                    @Override
                    public void run() {
                        channel.pipeline().addBefore("packet_handler", PACKET_HANDLER, new ChannelDuplexHandler() {
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
                }.runTaskAsynchronously(SpaciousLib.instance).getTaskId();
                tasks.put(uuid, id);
            }
        }
    }
}
