package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.CommonUtils;
import org.anhcraft.spaciouslib.utils.GameVersion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * A class helps you to send packet
 */
public class PacketSender {
    private Object[] packets;

    /**
     * Creates PacketSender instance from the given packet
     * @param packet the packet
     */
    public PacketSender(Object packet){
        this.packets = new Object[]{ packet };
    }

    /**
     * Creates PacketSender instance from the given packet array
     * @param packetArray the packet array
     */
    public PacketSender(Object... packetArray){
        this.packets = packetArray;
    }

    /**
     * Creates PacketSender instance from the given packet list
     * @param packetArray the packet list
     */
    public PacketSender(List<Object> packetArray){
        this.packets = CommonUtils.toArray(packetArray, Object.class);
    }

    /**
     * Sends these packets to the given player
     * @param player the player
     * @return this object
     */
    public PacketSender sendPlayer(Player player){
        GameVersion v = GameVersion.getVersion();
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
            for(Object packet : this.packets){
                sendPacket.invoke(playerConn, packet);
            }
        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Sends these packets to all given players
     * @param players players
     * @return this object
     */
    public PacketSender sendPlayers(Player... players){
        for(Player p : players){
            sendPlayer(p);
        }
        return this;
    }

    /**
     * Sends these packets to all given players
     * @param players the list of players
     * @return this object
     */
    public PacketSender sendPlayers(List<Player> players){
        for(Player p : players){
            sendPlayer(p);
        }
        return this;
    }

    /**
     * Sends these packets to all players in the given world
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
     * Sends these packets to all players who is nearby the given location
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
     * Sends these packets to all players
     * @return this object
     */
    public PacketSender sendAll(){
        for(Player p : Bukkit.getServer().getOnlinePlayers()){
            sendPlayer(p);
        }
        return this;
    }

    /**
     * Gets all packets
     * @return the packet array
     */
    public Object[] getPackets(){
        return this.packets;
    }
}
