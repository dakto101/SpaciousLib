package org.anhcraft.spaciouslib.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.anhcraft.spaciouslib.placeholder.PlaceholderAPI;

/**
 * A class helps you to manage plugin's chat message
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

    /**
     * Creates Chat instance
     * @param prefix chat prefix
     */
    public Chat(String prefix){
        this.prefix = prefix;
        this.placeholder = false;
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

    public void sendAllPlayersNoPrefix(String a) {
        for(ProxiedPlayer p: ProxyServer.getInstance().getPlayers()) {
            p.sendMessage(replace(a, p));
        }
    }

    public void sendPlayer(TextComponent text, ProxiedPlayer p) {
        p.sendMessage(replaceTC(buildChatPrefix(text), p));
    }

    public void sendPlayerNoPrefix(TextComponent text, ProxiedPlayer p) {
        p.sendMessage(replaceTC(text, p));
    }

    public void sendSender(TextComponent text, CommandSender s) {
        if(s instanceof ProxiedPlayer){
            ((ProxiedPlayer) s).sendMessage(replaceTC(buildChatPrefix(text), (ProxiedPlayer) s));
        } else {
            sendSender(text.toLegacyText());
        }
    }

    public void sendSenderNoPrefix(TextComponent text, CommandSender s) {
        if(s instanceof ProxiedPlayer) {
            ((ProxiedPlayer) s).sendMessage(replaceTC(text, (ProxiedPlayer) s));
        }else {
            sendSenderNoPrefix(text.toLegacyText());
        }
    }

    public void sendAllPlayers(TextComponent text) {
        for(ProxiedPlayer p: ProxyServer.getInstance().getPlayers()) {
            p.sendMessage(replaceTC(buildChatPrefix(text), p));
        }
    }

    public void sendAllPlayersNoPrefix(TextComponent text) {
        for(ProxiedPlayer p: ProxyServer.getInstance().getPlayers()) {
            p.sendMessage(replaceTC(text, p));
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

    private TextComponent replaceTC(TextComponent s) {
        return color(s);
    }

    private TextComponent replaceTC(TextComponent text, ProxiedPlayer player) {
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
