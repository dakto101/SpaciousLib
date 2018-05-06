package org.anhcraft.spaciouslib.bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.anhcraft.spaciouslib.SpaciousLib;
import org.anhcraft.spaciouslib.events.BungeeForwardEvent;
import org.anhcraft.spaciouslib.mojang.Skin;
import org.anhcraft.spaciouslib.utils.Chat;
import org.anhcraft.spaciouslib.utils.CommonUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A class helps you to manage Bungeecord message channel
 */
public class BungeeAPI implements PluginMessageListener {
    public static final String CHANNEL = "BungeeCord";
    private static List<BungeeResponse> queue = new ArrayList<>();

    public BungeeAPI(){
        queue = new ArrayList<>();

        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(SpaciousLib.instance, CHANNEL);
        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(SpaciousLib.instance, CHANNEL, this);
    }

    /**
     * Sends a connect request for the given player<br>
     * If success, that player will be connected to the given server automatically
     * @param player the player
     * @param server the server which you want the player connects to
     */
    public static void connect(Player player, String server){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage(SpaciousLib.instance, CHANNEL, out.toByteArray());
    }

    /**
     * Sends a connect request for the given player who is in another server
     * @param player the player
     * @param server the server which you want the player connects to
     */
    public static void connect(String player, String server){
        if(Bukkit.getServer().getOnlinePlayers().size() == 0){
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ConnectOther");
        out.writeUTF(player);
        out.writeUTF(server);
        Bukkit.getServer().getOnlinePlayers().iterator().next()
                .sendPluginMessage(SpaciousLib.instance, CHANNEL, out.toByteArray());
    }

    /**
     * Gets the IP of the given player
     * @param player the player
     * @param response BungeePlayerIPResponse object
     */
    public static void getIP(Player player, BungeePlayerIPResponse response){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("IP");
        player.sendPluginMessage(SpaciousLib.instance, CHANNEL, out.toByteArray());
        queue.add(response);
    }

    /**
     * Gets the player amount of another server
     * @param server the server
     * @param response BungeePlayerAmountResponse object
     */
    public static void getPlayerAmount(String server, BungeePlayerAmountResponse response){
        if(Bukkit.getServer().getOnlinePlayers().size() == 0){
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerCount");
        out.writeUTF(server);
        Bukkit.getServer().getOnlinePlayers().iterator().next()
                .sendPluginMessage(SpaciousLib.instance, CHANNEL, out.toByteArray());
        queue.add(response);
    }

    /**
     * Gets the player amount of all servers
     * @param response BungeePlayerAmountResponse object
     */
    public static void getPlayerAmount(BungeePlayerAmountResponse response){
        if(Bukkit.getServer().getOnlinePlayers().size() == 0){
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerCount");
        out.writeUTF("ALL");
        Bukkit.getServer().getOnlinePlayers().iterator().next()
                .sendPluginMessage(SpaciousLib.instance, CHANNEL, out.toByteArray());
        queue.add(response);
    }

    /**
     * Gets the list of players of another server
     * @param server the server
     * @param response BungeePlayerListResponse object
     */
    public static void getPlayerList(String server, BungeePlayerListResponse response){
        if(Bukkit.getServer().getOnlinePlayers().size() == 0){
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerList");
        out.writeUTF(server);
        Bukkit.getServer().getOnlinePlayers().iterator().next()
                .sendPluginMessage(SpaciousLib.instance, CHANNEL, out.toByteArray());
        queue.add(response);
    }


    /**
     * Gets the list of players of all servers
     * @param response BungeePlayerListResponse object
     */
    public static void getPlayerList(BungeePlayerListResponse response){
        if(Bukkit.getServer().getOnlinePlayers().size() == 0){
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerList");
        out.writeUTF("ALL");
        Bukkit.getServer().getOnlinePlayers().iterator().next()
                .sendPluginMessage(SpaciousLib.instance, CHANNEL, out.toByteArray());
        queue.add(response);
    }

    /**
     * Gets all servers in the network
     * @param response BungeeServerListResponse object
     */
    public static void getServerList(BungeeServerListResponse response){
        if(Bukkit.getServer().getOnlinePlayers().size() == 0){
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("GetServers");
        Bukkit.getServer().getOnlinePlayers().iterator().next()
                .sendPluginMessage(SpaciousLib.instance, CHANNEL, out.toByteArray());
        queue.add(response);
    }

    /**
     * Sends a message to a specific player who is in another server
     * @param player the name of that player
     * @param message the message
     */
    public static void sendMessage(String player, String message){
        if(Bukkit.getServer().getOnlinePlayers().size() == 0){
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Message");
        out.writeUTF(player);
        out.writeUTF(Chat.color(message));
        Bukkit.getServer().getOnlinePlayers().iterator().next()
                .sendPluginMessage(SpaciousLib.instance, CHANNEL, out.toByteArray());
    }

    /**
     * Gets the name of current server
     * @param response BungeeServerNameResponse object
     */
    public static void getServerName(BungeeServerNameResponse response){
        if(Bukkit.getServer().getOnlinePlayers().size() == 0){
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("GetServer");
        Bukkit.getServer().getOnlinePlayers().iterator().next()
                .sendPluginMessage(SpaciousLib.instance, CHANNEL, out.toByteArray());
        queue.add(response);
    }

    /**
     * Gets the UUID of a player
     * @param player the player
     * @param response BungeePlayerUUIDResponse object
     */
    public static void getUUID(Player player, BungeePlayerUUIDResponse response){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("UUID");
        player.sendPluginMessage(SpaciousLib.instance, CHANNEL, out.toByteArray());
        queue.add(response);
    }

    /**
     * Gets the UUID of a player who is in another server
     * @param player the player
     * @param response BungeeOtherPlayerUUIDResponse object
     */
    public static void getUUIDOther(String player, BungeeOtherPlayerUUIDResponse response){
        if(Bukkit.getServer().getOnlinePlayers().size() == 0){
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("UUIDOther");
        out.writeUTF(player);
        Bukkit.getServer().getOnlinePlayers().iterator().next().sendPluginMessage(SpaciousLib.instance, CHANNEL, out.toByteArray());
        queue.add(response);
    }

    /**
     * Gets the IP of the given server
     * @param server the server
     * @param response BungeeServerIPResponse object
     */
    public static void getServerIP(String server, BungeeServerIPResponse response){
        if(Bukkit.getServer().getOnlinePlayers().size() == 0){
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ServerIP");
        out.writeUTF(server);
        Bukkit.getServer().getOnlinePlayers().iterator().next()
                .sendPluginMessage(SpaciousLib.instance, CHANNEL, out.toByteArray());
        queue.add(response);
    }

    /**
     * Sends a kick request
     * @param player the player who you want to kick
     * @param reason the reason
     */
    public static void kickPlayer(String player, String reason){
        if(Bukkit.getServer().getOnlinePlayers().size() == 0){
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("KickPlayer");
        out.writeUTF(player);
        out.writeUTF(Chat.color(reason));
        Bukkit.getServer().getOnlinePlayers().iterator().next()
                .sendPluginMessage(SpaciousLib.instance, CHANNEL, out.toByteArray());
    }

    /**
     * Sends an array of bytes (binary data) to another server.<br>
     * These data can be received by BungeeForwardEvent event if this library has already installed on that server
     * @param server the server which youw ant to send to
     * @param channel the channel
     * @param bytedata the array of bytes
     */
    public static void forwardData(String server, String channel, byte[] bytedata){
        if(Bukkit.getServer().getOnlinePlayers().size() == 0){
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Forward");
        out.writeUTF(server);
        out.writeUTF(channel);
        out.writeShort(bytedata.length);
        out.write(bytedata);
        Bukkit.getServer().getOnlinePlayers().iterator().next()
                .sendPluginMessage(SpaciousLib.instance, CHANNEL, out.toByteArray());
    }

    /**
     * Sends an array of bytes (binary data) to all servers.<br>
     * These data can be received by BungeeForwardEvent event if this library has already installed on those server
     * @param channel the channel
     * @param bytedata the array of bytes
     */
    public static void forwardData(String channel, byte[] bytedata){
        if(Bukkit.getServer().getOnlinePlayers().size() == 0){
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Forward");
        out.writeUTF("ALL");
        out.writeUTF(channel);
        out.writeShort(bytedata.length);
        out.write(bytedata);
        Bukkit.getServer().getOnlinePlayers().iterator().next()
                .sendPluginMessage(SpaciousLib.instance, CHANNEL, out.toByteArray());
    }

    public static void forwardDataPlayer(String player, String channel, byte[] bytedata){
        if(Bukkit.getServer().getOnlinePlayers().size() == 0){
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ForwardToPlayer");
        out.writeUTF(player);
        out.writeUTF(channel);
        out.writeShort(bytedata.length);
        out.write(bytedata);
        Bukkit.getServer().getOnlinePlayers().iterator().next()
                .sendPluginMessage(SpaciousLib.instance, CHANNEL, out.toByteArray());
    }

    public static void requestChangeSkin(String player, Skin skin){
        if(Bukkit.getServer().getOnlinePlayers().size() == 0){
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("skin");
        out.writeUTF(player);
        out.writeUTF(skin.getValue());
        out.writeUTF(skin.getSignature());
        Bukkit.getServer().getOnlinePlayers().iterator().next()
                .sendPluginMessage(SpaciousLib.instance, SpaciousLib.CHANNEL, out.toByteArray());
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        ByteArrayDataInput i = ByteStreams.newDataInput(message);
        if (channel.equals(CHANNEL)) {
            String sc = i.readUTF();
            boolean b = false;
            for(BungeeResponse br : queue) {
                if(sc.equals("IP") && br instanceof BungeePlayerIPResponse) {
                    ((BungeePlayerIPResponse) br).result(i.readUTF(), i.readInt());
                    b = true;
                }
                if(sc.equals("PlayerCount") && br instanceof BungeePlayerAmountResponse) {
                    ((BungeePlayerAmountResponse) br).result(i.readUTF(), i.readInt());
                    b = true;
                }
                if(sc.equals("PlayerList") && br instanceof BungeePlayerListResponse) {
                    ((BungeePlayerListResponse) br).result(i.readUTF(), CommonUtils.toList(i.readUTF().split(", ")));
                    b = true;
                }
                if(sc.equals("GetServers") && br instanceof BungeeServerListResponse) {
                    ((BungeeServerListResponse) br).result(CommonUtils.toList(i.readUTF().split(", ")));
                    b = true;
                }
                if(sc.equals("GetServer") && br instanceof BungeeServerNameResponse) {
                    ((BungeeServerNameResponse) br).result(i.readUTF());
                    b = true;
                }
                if(sc.equals("UUID") && br instanceof BungeePlayerUUIDResponse) {
                    ((BungeePlayerUUIDResponse) br).result(UUID.fromString(i.readUTF()));
                    b = true;
                }
                if(sc.equals("UUIDOther") && br instanceof BungeeOtherPlayerUUIDResponse) {
                    ((BungeeOtherPlayerUUIDResponse) br).result(i.readUTF(), UUID.fromString(i.readUTF()));
                    b = true;
                }
                if(sc.equals("ServerIP") && br instanceof BungeeServerIPResponse) {
                    ((BungeeServerIPResponse) br).result(i.readUTF(), i.readUTF(), i.readUnsignedShort());
                    b = true;
                }

                if(b) {
                    break;
                }
            }
            if(b) {
                queue.remove(0);
            } else {
                byte[] bytedata = new byte[i.readShort()];
                i.readFully(bytedata);
                DataInputStream data = new DataInputStream(new ByteArrayInputStream(bytedata));
                BungeeForwardEvent ev = new BungeeForwardEvent(data);
                Bukkit.getServer().getPluginManager().callEvent(ev);
            }
        }
    }
}
