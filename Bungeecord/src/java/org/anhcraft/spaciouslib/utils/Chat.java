package org.anhcraft.spaciouslib.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.anhcraft.spaciouslib.placeholder.PlaceholderAPI;

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
        ProxyServer.getInstance().getConsole().sendMessage(replace(prefix + a));
    }

    public void sendSenderNoPrefix(String a) {
        ProxyServer.getInstance().getConsole().sendMessage(replace(a));
    }

    public void sendPlayer(String a, ProxiedPlayer p) {
        p.sendMessage(replace(prefix + a, p));
    }

    public void sendPlayerNoPrefix(String a, ProxiedPlayer p) {
        p.sendMessage(replace(a, p));
    }

    public void sendSender(String a, CommandSender s) {
        if(s instanceof ProxiedPlayer){
            s.sendMessage(replace(prefix + a, (ProxiedPlayer) s));
        } else {
            s.sendMessage(replace(prefix + a));
        }
    }

    public void sendSenderNoPrefix(String a, CommandSender s) {
        if(s instanceof ProxiedPlayer) {
            s.sendMessage(replace(a, (ProxiedPlayer) s));
        }else {
            s.sendMessage(replace(a));
        }
    }

    public void sendAllPlayers(String a) {
        for(ProxiedPlayer p: ProxyServer.getInstance().getPlayers()) {
             p.sendMessage(replace(prefix + a, p));
        }
    }

    private String replace(String s) {
        return color(s);
    }

    private String replace(String s, ProxiedPlayer player) {
        if(this.placeholder){
            return color(PlaceholderAPI.replace(s, player));
        } else {
            return color(s);
        }
    }

    public void sendAllPlayersNoPrefix(String a) {
        for(ProxiedPlayer p: ProxyServer.getInstance().getPlayers()) {
            p.sendMessage(replace(a, p));
        }
    }
}
