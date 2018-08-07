package org.anhcraft.spaciouslib.utils;

import net.md_5.bungee.api.chat.BaseComponent;
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

    private String prefix;
    private BaseComponent prefixComponent;
    private boolean placeholder;

    public Chat(String prefix){
        this.prefix = prefix;
        this.placeholder = false;
    }

    public Chat(String prefix, boolean placeholder){
        this.prefix = prefix;
        this.placeholder = placeholder;
    }

    public Chat(BaseComponent prefix){
        this.prefixComponent = prefix;
    }

    public Chat(BaseComponent prefix, boolean placeholder){
        this.prefixComponent = prefix;
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

    public void sendPlayer(BaseComponent text, Player p) {
        p.spigot().sendMessage(replaceTC(buildChatPrefix(text), p));
    }

    public void sendPlayerNoPrefix(BaseComponent text, Player p) {
        p.spigot().sendMessage(replaceTC(text, p));
    }

    public void sendSender(BaseComponent text, CommandSender s) {
        if(s instanceof Player){
            ((Player) s).spigot().sendMessage(replaceTC(buildChatPrefix(text), (Player) s));
        } else {
            sendSender(text.toLegacyText());
        }
    }

    public void sendSenderNoPrefix(BaseComponent text, CommandSender s) {
        if(s instanceof Player) {
            ((Player) s).spigot().sendMessage(replaceTC(text, (Player) s));
        }else {
            sendSenderNoPrefix(text.toLegacyText());
        }
    }

    public void sendAllPlayers(BaseComponent text) {
        for(Player p: Bukkit.getServer().getOnlinePlayers()) {
            p.spigot().sendMessage(replaceTC(buildChatPrefix(text), p));
        }
    }

    public void sendAllPlayersNoPrefix(BaseComponent text) {
        for(Player p: Bukkit.getServer().getOnlinePlayers()) {
            p.spigot().sendMessage(replaceTC(text, p));
        }
    }

    public void sendGlobal(BaseComponent text, World w){
        for(Player p: w.getPlayers()){
            sendPlayer(text, p);
        }
    }

    public void sendGlobalNoPrefix(BaseComponent text, World w) {
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

    private BaseComponent replaceTC(BaseComponent text, Player player) {
        if(this.placeholder){
            return new TextComponent(PlaceholderAPI.replace(text.toPlainText(), player));
        } else {
            return text;
        }
    }

    private BaseComponent buildChatPrefix(BaseComponent text) {
        BaseComponent x = prefixComponent.duplicate();
        x.addExtra(text);
        return x;
    }
}
