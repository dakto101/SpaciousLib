package org.anhcraft.spaciouslib.bungee;

import org.anhcraft.spaciouslib.SpaciousLib;
import org.anhcraft.spaciouslib.events.BungeeForwardEvent;
import org.anhcraft.spaciouslib.mojang.Skin;
import org.anhcraft.spaciouslib.utils.Chat;
import org.anhcraft.spaciouslib.utils.CommonUtils;
import org.anhcraft.spaciouslib.utils.GameVersion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.*;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A class helps you to manage Bungeecord message channel
 */
public class BungeeAPI implements PluginMessageListener {
    public static final String BC_CHANNEL = GameVersion.is1_13Above() ? "" : "BungeeCord";
    private static final LinkedBlockingQueue<BungeeResponse> queue = new LinkedBlockingQueue<>();

    public BungeeAPI(){
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(SpaciousLib.instance, BC_CHANNEL);
        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(SpaciousLib.instance, BC_CHANNEL, this);
    }

    /**
     * Sends a connection request for the given player<br>
     * If success, that player will be connected to the given server automatically
     * @param player the player
     * @param server the server which you want the player connects to
     */
    public static void connect(Player player, String server){
        try {
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(array);
            out.writeUTF("Connect");
            out.writeUTF(server);
            out.flush();
            player.sendPluginMessage(SpaciousLib.instance, BC_CHANNEL, array.toByteArray());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a connection request for the given player who is in another server
     * @param player the player
     * @param server the server which you want the player connects to
     */
    public static void connect(String player, String server){
        if(Bukkit.getServer().getOnlinePlayers().size() == 0){
            return;
        }
        try {
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(array);
            out.writeUTF("ConnectOther");
            out.writeUTF(player);
            out.writeUTF(server);
            out.flush();
            Bukkit.getServer().getOnlinePlayers().iterator().next()
                    .sendPluginMessage(SpaciousLib.instance, BC_CHANNEL, array.toByteArray());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the IP of the given player
     * @param player the player
     * @param response BungeePlayerIPResponse object
     */
    public static void getIP(Player player, BungeePlayerIPResponse response){
        try {
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(array);
            out.writeUTF("IP");
            out.flush();
            player.sendPluginMessage(SpaciousLib.instance, BC_CHANNEL, array.toByteArray());
            queue.add(response);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the number of players in another server
     * @param server the server
     * @param response BungeePlayerAmountResponse object
     */
    public static void getPlayerAmount(String server, BungeePlayerAmountResponse response){
        if(Bukkit.getServer().getOnlinePlayers().size() == 0){
            return;
        }
        try {
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(array);
            out.writeUTF("PlayerCount");
            out.writeUTF(server);
            out.flush();
            Bukkit.getServer().getOnlinePlayers().iterator().next()
                    .sendPluginMessage(SpaciousLib.instance, BC_CHANNEL, array.toByteArray());
            queue.add(response);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the total number of players over the network
     * @param response BungeePlayerAmountResponse object
     */
    public static void getPlayerAmount(BungeePlayerAmountResponse response){
        if(Bukkit.getServer().getOnlinePlayers().size() == 0){
            return;
        }
        try {
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(array);
            out.writeUTF("PlayerCount");
            out.writeUTF("ALL");
            out.flush();
            Bukkit.getServer().getOnlinePlayers().iterator().next()
                    .sendPluginMessage(SpaciousLib.instance, BC_CHANNEL, array.toByteArray());
            queue.add(response);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the list of players in another server
     * @param server the server
     * @param response BungeePlayerListResponse object
     */
    public static void getPlayerNames(String server, BungeePlayerListResponse response){
        if(Bukkit.getServer().getOnlinePlayers().size() == 0){
            return;
        }
        try {
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(array);
            out.writeUTF("PlayerList");
            out.writeUTF(server);
            out.flush();
            Bukkit.getServer().getOnlinePlayers().iterator().next()
                    .sendPluginMessage(SpaciousLib.instance, BC_CHANNEL, array.toByteArray());
            queue.add(response);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Gets the list of players over the network
     * @param response BungeePlayerListResponse object
     */
    public static void getPlayerNames(BungeePlayerListResponse response){
        if(Bukkit.getServer().getOnlinePlayers().size() == 0){
            return;
        }
        try {
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(array);
            out.writeUTF("PlayerList");
            out.writeUTF("ALL");
            out.flush();
            Bukkit.getServer().getOnlinePlayers().iterator().next()
                    .sendPluginMessage(SpaciousLib.instance, BC_CHANNEL, array.toByteArray());
            queue.add(response);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets all servers under the network
     * @param response BungeeServerListResponse object
     */
    public static void getServerNames(BungeeServerListResponse response){
        if(Bukkit.getServer().getOnlinePlayers().size() == 0){
            return;
        }
        try {
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(array);
            out.writeUTF("GetServers");
            out.flush();
            Bukkit.getServer().getOnlinePlayers().iterator().next()
                    .sendPluginMessage(SpaciousLib.instance, BC_CHANNEL, array.toByteArray());
            queue.add(response);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a message to the given player who is in another server
     * @param player the name of that player
     * @param message the message
     */
    public static void sendMessage(String player, String message){
        if(Bukkit.getServer().getOnlinePlayers().size() == 0){
            return;
        }
        try {
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(array);
            out.writeUTF("Message");
            out.writeUTF(player);
            out.writeUTF(Chat.color(message));
            out.flush();
            Bukkit.getServer().getOnlinePlayers().iterator().next()
                    .sendPluginMessage(SpaciousLib.instance, BC_CHANNEL, array.toByteArray());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the name of the current server
     * @param response BungeeServerNameResponse object
     */
    public static void getServerName(BungeeServerNameResponse response){
        if(Bukkit.getServer().getOnlinePlayers().size() == 0){
            return;
        }
        try {
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(array);
            out.writeUTF("GetServer");
            out.flush();
            Bukkit.getServer().getOnlinePlayers().iterator().next()
                    .sendPluginMessage(SpaciousLib.instance, BC_CHANNEL, array.toByteArray());
            queue.add(response);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the UUID of a player
     * @param player the player
     * @param response BungeePlayerUUIDResponse object
     */
    public static void getUniqueId(Player player, BungeePlayerUUIDResponse response){
        try {
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(array);
            out.writeUTF("UUID");
            out.flush();
            player.sendPluginMessage(SpaciousLib.instance, BC_CHANNEL, array.toByteArray());
            queue.add(response);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the UUID of a player who is in another server
     * @param player the player
     * @param response BungeeOtherPlayerUUIDResponse object
     */
    public static void getUniqueId(String player, BungeeOtherPlayerUUIDResponse response){
        if(Bukkit.getServer().getOnlinePlayers().size() == 0){
            return;
        }
        try {
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(array);
            out.writeUTF("UUIDOther");
            out.writeUTF(player);
            out.flush();
            Bukkit.getServer().getOnlinePlayers().iterator().next().sendPluginMessage(SpaciousLib.instance, BC_CHANNEL, array.toByteArray());
            queue.add(response);
        } catch(IOException e) {
            e.printStackTrace();
        }
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
        try {
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(array);
            out.writeUTF("ServerIP");
            out.writeUTF(server);
            out.flush();
            Bukkit.getServer().getOnlinePlayers().iterator().next()
                    .sendPluginMessage(SpaciousLib.instance, BC_CHANNEL, array.toByteArray());
            queue.add(response);
        } catch(IOException e) {
            e.printStackTrace();
        }
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
        try {
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(array);
            out.writeUTF("KickPlayer");
            out.writeUTF(player);
            out.writeUTF(Chat.color(reason));
            out.flush();
            Bukkit.getServer().getOnlinePlayers().iterator().next()
                    .sendPluginMessage(SpaciousLib.instance, BC_CHANNEL, array.toByteArray());
        } catch(IOException e) {
            e.printStackTrace();
        }
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
        try {
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(array);
            out.writeUTF("Forward");
            out.writeUTF(server);
            out.writeUTF(channel);
            out.writeShort(bytedata.length);
            out.write(bytedata);
            out.flush();
            Bukkit.getServer().getOnlinePlayers().iterator().next()
                    .sendPluginMessage(SpaciousLib.instance, BC_CHANNEL, array.toByteArray());
        } catch(IOException e) {
            e.printStackTrace();
        }
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
        try {
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(array);
            out.writeUTF("Forward");
            out.writeUTF("ALL");
            out.writeUTF(channel);
            out.writeShort(bytedata.length);
            out.write(bytedata);
            out.flush();
            Bukkit.getServer().getOnlinePlayers().iterator().next()
                    .sendPluginMessage(SpaciousLib.instance, BC_CHANNEL, array.toByteArray());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void forwardDataPlayer(String player, String channel, byte[] bytedata){
        if(Bukkit.getServer().getOnlinePlayers().size() == 0){
            return;
        }
        try {
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(array);
            out.writeUTF("ForwardToPlayer");
            out.writeUTF(player);
            out.writeUTF(channel);
            out.writeShort(bytedata.length);
            out.write(bytedata);
            out.flush();
            Bukkit.getServer().getOnlinePlayers().iterator().next()
                    .sendPluginMessage(SpaciousLib.instance, BC_CHANNEL, array.toByteArray());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Requests the SpaciousLib plugin (on proxy-side) to change the skin of a player
     * @param player a player
     * @param skin a skin
     */
    public static void requestChangeSkin(String player, Skin skin){
        if(Bukkit.getServer().getOnlinePlayers().size() == 0){
            return;
        }
        try {
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(array);
            out.writeUTF("skin");
            out.writeUTF(player);
            out.writeUTF(skin.getValue());
            out.writeUTF(skin.getSignature());
            out.flush();
            Bukkit.getServer().getOnlinePlayers().iterator().next()
                    .sendPluginMessage(SpaciousLib.instance, SpaciousLib.SL_CHANNEL, array.toByteArray());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Requests the SpaciousLib plugin (on proxy-side) to execute a Bungeecord command as a console
     * @param command a command without "/" before
     */
    public static void requestExecuteCommand(String command){
        if(Bukkit.getServer().getOnlinePlayers().size() == 0){
            return;
        }
        try {
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(array);
            out.writeUTF("cmd");
            out.writeUTF(command);
            out.flush();
            Bukkit.getServer().getOnlinePlayers().iterator().next()
                    .sendPluginMessage(SpaciousLib.instance, SpaciousLib.SL_CHANNEL, array.toByteArray());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Requests the SpaciousLib plugin in a server to execute a command as a console
     * @param command a command without "/" before
     * @param server a server
     */
    public static void requestExecuteCommandServer(String command, String server){
        if(Bukkit.getServer().getOnlinePlayers().size() == 0){
            return;
        }
        try {
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(array);
            out.writeUTF("cmdsv");
            out.writeUTF(command);
            out.writeUTF(server);
            out.flush();
            Bukkit.getServer().getOnlinePlayers().iterator().next()
                    .sendPluginMessage(SpaciousLib.instance, SpaciousLib.SL_CHANNEL, array.toByteArray());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Requests the SpaciousLib plugin (on proxy-side) to execute a Bungeecord command as a player
     * @param command a command without "/" before
     * @param player a player
     */
    public static void requestExecutePlayerCommand(String command, String player){
        if(Bukkit.getServer().getOnlinePlayers().size() == 0){
            return;
        }
        try {
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(array);
            out.writeUTF("playercmd");
            out.writeUTF(command);
            out.writeUTF(player);
            out.flush();
            Bukkit.getServer().getOnlinePlayers().iterator().next()
                    .sendPluginMessage(SpaciousLib.instance, SpaciousLib.SL_CHANNEL, array.toByteArray());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Requests the SpaciousLib plugin in a server to execute a command as a player
     * @param command a command without "/" before
     * @param player a player
     */
    public static void requestExecutePlayerCommandServer(String command, String player){
        if(Bukkit.getServer().getOnlinePlayers().size() == 0){
            return;
        }
        try {
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(array);
            out.writeUTF("playercmdsv");
            out.writeUTF(command);
            out.writeUTF(player);
            out.flush();
            Bukkit.getServer().getOnlinePlayers().iterator().next()
                    .sendPluginMessage(SpaciousLib.instance, SpaciousLib.SL_CHANNEL, array.toByteArray());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        DataInputStream i = new DataInputStream(new ByteArrayInputStream(message));
        try {
            if(channel.equals(BC_CHANNEL)) {
                String sc = i.readUTF();
                BungeeResponse br = queue.poll();
                if(br != null) {
                    if(sc.equals("IP") && br instanceof BungeePlayerIPResponse) {
                        ((BungeePlayerIPResponse) br).result(i.readUTF(), i.readInt());
                    }
                    if(sc.equals("PlayerCount") && br instanceof BungeePlayerAmountResponse) {
                        ((BungeePlayerAmountResponse) br).result(i.readUTF(), i.readInt());
                    }
                    if(sc.equals("PlayerList") && br instanceof BungeePlayerListResponse) {
                        ((BungeePlayerListResponse) br).result(i.readUTF(), CommonUtils.toList(i.readUTF().split(", ")));
                    }
                    if(sc.equals("GetServers") && br instanceof BungeeServerListResponse) {
                        ((BungeeServerListResponse) br).result(CommonUtils.toList(i.readUTF().split(", ")));
                    }
                    if(sc.equals("GetServer") && br instanceof BungeeServerNameResponse) {
                        ((BungeeServerNameResponse) br).result(i.readUTF());
                    }
                    if(sc.equals("UUID") && br instanceof BungeePlayerUUIDResponse) {
                        ((BungeePlayerUUIDResponse) br).result(UUID.fromString(i.readUTF()));
                    }
                    if(sc.equals("UUIDOther") && br instanceof BungeeOtherPlayerUUIDResponse) {
                        ((BungeeOtherPlayerUUIDResponse) br).result(i.readUTF(), UUID.fromString(i.readUTF()));
                    }
                    if(sc.equals("ServerIP") && br instanceof BungeeServerIPResponse) {
                        ((BungeeServerIPResponse) br).result(i.readUTF(), i.readUTF(), i.readUnsignedShort());
                    }
                } else {
                    byte[] arr = new byte[i.readShort()];
                    i.readFully(arr);
                    DataInputStream data = new DataInputStream(new ByteArrayInputStream(arr));
                    BungeeForwardEvent ev = new BungeeForwardEvent(data);
                    Bukkit.getServer().getPluginManager().callEvent(ev);
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
