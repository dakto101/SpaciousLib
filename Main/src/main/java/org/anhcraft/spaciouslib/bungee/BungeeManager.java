package org.anhcraft.spaciouslib.bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.anhcraft.spaciouslib.SpaciousLib;
import org.anhcraft.spaciouslib.utils.Strings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class BungeeManager implements PluginMessageListener {
    private static final String CHANNEL = "BungeeCord";
    private static List<BungeeResponse> queue;

    public BungeeManager(){
        queue = new ArrayList<>();

        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(SpaciousLib.instance, CHANNEL);
        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(SpaciousLib.instance, CHANNEL, this);
    }

    public static void connect(Player player, String server){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage(SpaciousLib.instance, CHANNEL, out.toByteArray());
    }

    public static void connect(Player sender, String player, String server){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ConnectOther");
        out.writeUTF(player);
        out.writeUTF(server);
        sender.sendPluginMessage(SpaciousLib.instance, CHANNEL, out.toByteArray());
    }

    public static void getIP(Player player, BungeePlayerIPResponse response){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("IP");
        player.sendPluginMessage(SpaciousLib.instance, CHANNEL, out.toByteArray());
        queue.add(response);
    }

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

    public static void sendMessage(String player, String message){
        if(Bukkit.getServer().getOnlinePlayers().size() == 0){
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Message");
        out.writeUTF(player);
        out.writeUTF(Strings.color(message));
        Bukkit.getServer().getOnlinePlayers().iterator().next()
                .sendPluginMessage(SpaciousLib.instance, CHANNEL, out.toByteArray());
    }

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

    public static void getUUID(Player player, BungeePlayerUUIDResponse response){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("UUID");
        player.sendPluginMessage(SpaciousLib.instance, CHANNEL, out.toByteArray());
        queue.add(response);
    }

    public static void getUUIDOther(Player sender, String player, BungeeOtherPlayerUUIDResponse response){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("UUIDOther");
        out.writeUTF(player);
        sender.sendPluginMessage(SpaciousLib.instance, CHANNEL, out.toByteArray());
        queue.add(response);
    }

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

    public static void kickPlayer(String player, String reason){
        if(Bukkit.getServer().getOnlinePlayers().size() == 0){
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("KickPlayer");
        out.writeUTF(player);
        out.writeUTF(Strings.color(reason));
        Bukkit.getServer().getOnlinePlayers().iterator().next()
                .sendPluginMessage(SpaciousLib.instance, CHANNEL, out.toByteArray());
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals(CHANNEL)) {
            return;
        }
        ByteArrayDataInput i = ByteStreams.newDataInput(message);
        String sc = i.readUTF();
        boolean b = false;
        for(BungeeResponse br : queue){
            if (sc.equals("IP") && br instanceof BungeePlayerIPResponse) {
                ((BungeePlayerIPResponse) br).result(i.readUTF(), i.readUTF(), i.readInt());
                b = true;
            }
            if (sc.equals("PlayerCount") && br instanceof BungeePlayerAmountResponse) {
                ((BungeePlayerAmountResponse) br).result(i.readUTF(), i.readInt());
                b = true;
            }
            if (sc.equals("PlayerList") && br instanceof BungeePlayerListResponse) {
                ((BungeePlayerListResponse) br).result(i.readUTF(), new ArrayList<>(Arrays.asList(i.readUTF().split(", "))));
                b = true;
            }
            if (sc.equals("GetServers") && br instanceof BungeeServerListResponse) {
                ((BungeeServerListResponse) br).result(new ArrayList<>(Arrays.asList(i.readUTF().split(", "))));
                b = true;
            }
            if (sc.equals("GetServer") && br instanceof BungeeServerNameResponse) {
                ((BungeeServerNameResponse) br).result(i.readUTF());
                b = true;
            }
            if (sc.equals("UUID") && br instanceof BungeePlayerUUIDResponse) {
                ((BungeePlayerUUIDResponse) br).result(UUID.fromString(i.readUTF()));
                b = true;
            }
            if (sc.equals("UUIDOther") && br instanceof BungeeOtherPlayerUUIDResponse) {
                ((BungeeOtherPlayerUUIDResponse) br).result(i.readUTF(), UUID.fromString(i.readUTF()));
                b = true;
            }
            if (sc.equals("ServerIP") && br instanceof BungeeServerIPResponse) {
                ((BungeeServerIPResponse) br).result(i.readUTF(), i.readUTF(), i.readInt());
                b = true;
            }

            if(b){
                break;
            }
        }
        if(b){
            queue.remove(0);
        }
    }
}
