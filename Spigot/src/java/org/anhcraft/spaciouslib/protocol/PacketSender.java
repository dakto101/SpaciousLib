package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.GVersion;
import org.anhcraft.spaciouslib.utils.GameVersion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PacketSender {
    private Object packet;

    public PacketSender(Object packet){
        this.packet = packet;
    }

    public PacketSender sendPlayer(Player player){
        GVersion v = GameVersion.getVersion();
        try {
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + v.toString() + ".entity.CraftPlayer");
            Class<?> nmsEntityPlayerClass = Class.forName("net.minecraft.server." + v.toString() + ".EntityPlayer");
            Class<?> packetClass = Class.forName("net.minecraft.server." + v.toString() + ".Packet");
            Class<?> playerConnClass = Class.forName("net.minecraft.server." + v.toString() + ".PlayerConnection");
            Object craftPlayer = craftPlayerClass.cast(player);
            Method handle = craftPlayerClass.getDeclaredMethod("getHandle");
            Object nmsEntity = handle.invoke(craftPlayer);
            Object nmsEntityPlayer = nmsEntityPlayerClass.cast(nmsEntity);
            Field playerConnField = nmsEntityPlayerClass.getDeclaredField("playerConnection");
            playerConnField.setAccessible(true);
            Object playerConn = playerConnField.get(nmsEntityPlayer);
            Method sendPacket = playerConnClass.getDeclaredMethod("sendPacket", packetClass);
            sendPacket.invoke(playerConn, packet);
        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return this;
    }

    public PacketSender sendWorld(World world){
        for(Player p : world.getPlayers()){
            sendPlayer(p);
        }
        return this;
    }

    public PacketSender sendNearby(Location location, double distance){
        for(Player p : location.getWorld().getPlayers()){
            if(p.getLocation().distance(location) > distance){
                continue;
            }
            sendPlayer(p);
        }
        return this;
    }

    public PacketSender sendAll(){
        for(Player p : Bukkit.getServer().getOnlinePlayers()){
            sendPlayer(p);
        }
        return this;
    }

    public Object getPacket(){
        return this.packet;
    }
}
