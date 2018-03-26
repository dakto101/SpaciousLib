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

/**
 * A class helps you to send packet
 */
public class PacketSender {
    private Object packet;

    /**
     * Creates PacketSender instance
     * @param packet the packet
     */
    public PacketSender(Object packet){
        this.packet = packet;
    }

    /**
     * Sends that packet to the given player
     * @param player the player
     * @return this object
     */
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

    /**
     * Sends that packet to all players in the given world
     * @param world the world
     * @return this object
     */
    public PacketSender sendWorld(World world){
        for(Player p : world.getPlayers()){
            sendPlayer(p);
        }
        return this;
    }

    /**
     * Sends that packet to all players who is nearby the given location
     * @param location the location
     * @param distance the maximum distance from the location
     * @return this object
     */
    public PacketSender sendNearby(Location location, double distance){
        for(Player p : location.getWorld().getPlayers()){
            if(p.getLocation().distance(location) > distance){
                continue;
            }
            sendPlayer(p);
        }
        return this;
    }

    /**
     * Sends that packet to all players
     * @return this object
     */
    public PacketSender sendAll(){
        for(Player p : Bukkit.getServer().getOnlinePlayers()){
            sendPlayer(p);
        }
        return this;
    }

    /**
     * Gets that packet
     * @return the packet
     */
    public Object getPacket(){
        return this.packet;
    }
}
