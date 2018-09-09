package org.anhcraft.spaciouslib.protocol;

import org.anhcraft.spaciouslib.utils.CommonUtils;
import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.Group;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * A class helps you to send packet
 */
public class PacketSender {
    private Object[] packets;

    /**
     * Creates PacketSender instance
     * @param array this can be an array of packets, an array of packet senders, an array of collection which contains elements as packets or packet senders. You can also mix different types<br> Ex: #PacketSender(list<Packet>, Collection<PacketSender>, Packet, PacketSender, Packet[])
     */
    public PacketSender(Object... array){
        List<Object> packets = new ArrayList<>();
        for(Object o : array){
            if(o instanceof Iterable){
                for(Object obj : ((Iterable) o)) {
                    if(obj instanceof PacketSender) {
                        packets.addAll(CommonUtils.toList(((PacketSender) obj).packets));
                    } else {
                        packets.add(obj);
                    }
                }
            } else if(o instanceof PacketSender){
                packets.addAll(CommonUtils.toList(((PacketSender) o).packets));
            } else {
                packets.add(o);
            }
        }
        this.packets = CommonUtils.toArray(packets, Object.class);
    }

    /**
     * Creates PacketSender instance from the given packet sender array
     * @param packetSenderArray an array of packet senders
     */
    public PacketSender(PacketSender... packetSenderArray){
        List<Object> packets = new ArrayList<>();
        for(PacketSender ps : packetSenderArray){
            packets.addAll(CommonUtils.toList(ps.getPackets()));
        }
        this.packets = CommonUtils.toArray(packets, Object.class);
    }

    /**
     * Sends these packets to the given player
     * @param player the player
     * @return this object
     */
    public PacketSender sendPlayer(Player player){
        String v = GameVersion.getVersion().toString();
        try {
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + v + ".entity.CraftPlayer");
            Class<?> nmsEntityPlayerClass = Class.forName("net.minecraft.server." + v + ".EntityPlayer");
            Class<?> packetClass = Class.forName("net.minecraft.server." + v + ".Packet");
            Class<?> playerConnClass = Class.forName("net.minecraft.server." + v + ".PlayerConnection");
            Object craftPlayer = ReflectionUtils.cast(craftPlayerClass, player);
            Object nmsEntityPlayer = ReflectionUtils.getMethod("getHandle", craftPlayerClass, craftPlayer);
            Object playerConn = ReflectionUtils.getField("playerConnection", nmsEntityPlayerClass, nmsEntityPlayer);
            for(Object packet : this.packets){
                ReflectionUtils.getMethod("sendPacket", playerConnClass, playerConn, new Group<>(
                        new Class<?>[]{packetClass},
                        new Object[]{packet}
                ));
            }
        } catch(ClassNotFoundException e) {
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
