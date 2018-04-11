package org.anhcraft.spaciouslib.utils;

import org.anhcraft.spaciouslib.placeholder.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * A class helps you to manage plugin's chat message
 */
public class Chat {
    public static String color(String a){
        return ChatColor.translateAlternateColorCodes('&', a);
    }

    private String prefix;
    private boolean placeholder;

    /**
     * Creates Chat instance
     * @param prefix chat prefix
     */
    public Chat(String prefix){
        this.prefix = prefix;
        this.placeholder = true;
    }

    /**
     * Creates Chat instance
     * @param prefix chat prefix
     * @param placeholder true if you want to auto replace placeholders in messages
     */
    public Chat(String prefix, boolean placeholder){
        this.prefix = prefix;
        this.placeholder = placeholder;
    }

	public void sendSender(String a) {
        Bukkit.getConsoleSender().sendMessage(replace(prefix + a));
    }

    public void sendSenderNoPrefix(String a) {
        Bukkit.getServer().getConsoleSender().sendMessage(replace(a));
    }

    public void sendPlayer(String a, Player p) {
        p.sendMessage(replace(prefix + a, p));
    }

    public void sendPlayerNoPrefix(String a, Player p) {
        p.sendMessage(replace(a, p));
    }

    public void sendSender(String a, CommandSender s) {
        if(s instanceof Player){
            s.sendMessage(replace(prefix + a, (Player) s));
        } else {
            s.sendMessage(replace(prefix + a));
        }
    }

    public void sendSenderNoPrefix(String a, CommandSender s) {
        if(s instanceof Player) {
            s.sendMessage(replace(a, (Player) s));
        }else {
            s.sendMessage(replace(a));
        }
    }

    public void sendAllPlayers(String a) {
        for(Player p: Bukkit.getServer().getOnlinePlayers()) {
             p.sendMessage(replace(prefix + a, p));
        }
    }

    private String replace(String s) {
        return color(s);
    }

    private String replace(String s, Player player) {
        if(this.placeholder){
            return color(PlaceholderAPI.replace(s, player));
        } else {
            return color(s);
        }
    }

    public void sendAllPlayersNoPrefix(String a) {
        for(Player p: Bukkit.getServer().getOnlinePlayers()) {
            p.sendMessage(replace(a, p));
        }
    }

    public void sendGlobal(String a) {
        Bukkit.getServer().broadcastMessage(replace(prefix + a));
    }

    public void sendGlobalNoPrefix(String a) {
        Bukkit.getServer().broadcastMessage(replace(a));
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
