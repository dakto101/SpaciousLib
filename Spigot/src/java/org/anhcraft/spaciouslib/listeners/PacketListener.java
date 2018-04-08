package org.anhcraft.spaciouslib.listeners;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.anhcraft.spaciouslib.SpaciousLib;
import org.anhcraft.spaciouslib.events.PacketHandleEvent;
import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PacketListener implements Listener {
    private final static String PACKET_HANDLER = "SpaciousLib";

    public PacketListener(){
        for(Player p : Bukkit.getServer().getOnlinePlayers()){
            init(p);
        }
    }

    public static Channel getChannel(Player player){
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
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void remove(Player player){
        Channel channel = getChannel(player);
        if(channel.pipeline().get(PACKET_HANDLER) != null) {
            channel.pipeline().remove(PACKET_HANDLER);
        }
    }

    @EventHandler
    public void quit(PlayerQuitEvent event){
        remove(event.getPlayer());
    }

    @EventHandler
    public void join(PlayerJoinEvent event){
        init(event.getPlayer());
    }

    private void init(Player player) {
        Channel channel = getChannel(player);
        if(channel.pipeline().get(PACKET_HANDLER) == null) {
            channel.pipeline().addBefore("packet_handler", PACKET_HANDLER,
                new ChannelDuplexHandler() {
                    @Override
                    public void write(ChannelHandlerContext c, Object o, ChannelPromise p) throws Exception {
                        PacketHandleEvent ev = new PacketHandleEvent(player, o, PacketHandleEvent.Type.CLIENT_BOUND);
                        // switches to the main thread
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                Bukkit.getServer().getPluginManager().callEvent(ev);
                            }
                        }.runTaskLater(SpaciousLib.instance, 1);
                        if(!ev.isCancelled()) {
                            super.write(c, ev.getPacket(), p);
                        }
                    }

                    @Override
                    public void channelRead(ChannelHandlerContext c, Object o) throws Exception {
                        PacketHandleEvent ev = new PacketHandleEvent(player, o, PacketHandleEvent.Type.SERVER_BOUND);
                        // switches to the main thread
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                Bukkit.getServer().getPluginManager().callEvent(ev);
                            }
                        }.runTaskLater(SpaciousLib.instance, 1);
                        if(!ev.isCancelled()) {
                            super.channelRead(c, ev.getPacket());
                        }
                    }
                });
        }
    }
}