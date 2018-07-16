package org.anhcraft.spaciouslib.utils;

import net.md_5.bungee.api.chat.TextComponent;
import org.anhcraft.spaciouslib.placeholder.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * A class helps you to send messages or color them
 */
public class Chat {
    /**
     * Colors the given string
     * @param text a text string
     * @return the colored message
     */
    public static String color(String text){
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    /**
     * Colors the given text component
     * @param text a text component
     * @return the colored text component
     */
    public static TextComponent color(TextComponent text){
        return new TextComponent(ChatColor.translateAlternateColorCodes('&', text.getText()));
    }

    private String prefix;
    private boolean placeholder;

    public Chat(String prefix){
        this.prefix = prefix;
        this.placeholder = false;
    }

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

    public void sendPlayer(TextComponent text, Player p) {
        p.spigot().sendMessage(replaceTC(buildChatPrefix(text), p));
    }

    public void sendPlayerNoPrefix(TextComponent text, Player p) {
        p.spigot().sendMessage(replaceTC(text, p));
    }

    public void sendSender(TextComponent text, CommandSender s) {
        if(s instanceof Player){
            ((Player) s).spigot().sendMessage(replaceTC(buildChatPrefix(text), (Player) s));
        } else {
            sendSender(text.toLegacyText());
        }
    }

    public void sendSenderNoPrefix(TextComponent text, CommandSender s) {
        if(s instanceof Player) {
            ((Player) s).spigot().sendMessage(replaceTC(text, (Player) s));
        }else {
            sendSenderNoPrefix(text.toLegacyText());
        }
    }

    public void sendAllPlayers(TextComponent text) {
        for(Player p: Bukkit.getServer().getOnlinePlayers()) {
            p.spigot().sendMessage(replaceTC(buildChatPrefix(text), p));
        }
    }

    public void sendAllPlayersNoPrefix(TextComponent text) {
        for(Player p: Bukkit.getServer().getOnlinePlayers()) {
            p.spigot().sendMessage(replaceTC(text, p));
        }
    }

    public void sendGlobal(TextComponent text, World w){
        for(Player p: w.getPlayers()){
            sendPlayer(text, p);
        }
    }

    public void sendGlobalNoPrefix(TextComponent text, World w) {
        for(Player p: w.getPlayers()){
            sendPlayerNoPrefix(text, p);
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

    private TextComponent replaceTC(TextComponent s) {
        return color(s);
    }

    private TextComponent replaceTC(TextComponent text, Player player) {
        if(this.placeholder){
            return new TextComponent(color(PlaceholderAPI.replace(text.toString(), player)));
        } else {
            return color(text);
        }
    }

    private TextComponent buildChatPrefix(TextComponent text) {
        TextComponent str = new TextComponent(prefix);
        str.addExtra(text);
        return str;
    }
}
