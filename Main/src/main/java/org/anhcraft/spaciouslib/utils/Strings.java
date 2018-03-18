package org.anhcraft.spaciouslib.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Strings {
    public static String color(String a){
        return ChatColor.translateAlternateColorCodes('&', a);
    }

    private String prefix;

    /**
     * Manage plugin's chat messages
     *
     * @param prefix chat prefix
     *
     */
    public Strings(String prefix){
        this.prefix = prefix;
    }

	public void sendSender(String a) {
        Bukkit.getConsoleSender().sendMessage(color(prefix + a));
    }

    public void sendSenderNoPrefix(String a) {
        Bukkit.getServer().getConsoleSender().sendMessage(color(a));
    }

    public void sendPlayer(String a, Player p) {
        p.sendMessage(color(prefix + a));
    }

    public void sendPlayerNoPrefix(String a, Player p) {
        p.sendMessage(color(a));
    }

    public void sendSender(String a, CommandSender s) {
        s.sendMessage(color(prefix + a));
    }

    public void sendSenderNoPrefix(String a, CommandSender s) {
        s.sendMessage(color(a));
    }

    public void sendAllPlayers(String a) {
        for(Player p: Bukkit.getServer().getOnlinePlayers()) {
             p.sendMessage(color(prefix + a));
        }
    }

    public void sendAllPlayersNoPrefix(String a) {
        for(Player p: Bukkit.getServer().getOnlinePlayers()) {
            p.sendMessage(color(a));
        }
    }

    public void sendGlobal(String a) {
        Bukkit.getServer().broadcastMessage(color(prefix + a));
    }

    public void sendGlobalNoPrefix(String a) {
        Bukkit.getServer().broadcastMessage(color(a));
    }

    public void sendGlobal(String a, World w){
        for(Player p: w.getPlayers()){
            sendPlayer(a, p);
        }
    }

    public void sendGlobalNoPrefix(String a, World w) {
        for(Player p: w.getPlayers()){
            sendPlayerNoPrefix(a, p);
        }
    }
}
