package org.anhcraft.spaciouslib.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
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

    private String prefix;
    private BaseComponent prefixComponent;
    private boolean placeholder;

    public Chat(String prefix){
        this.prefix = prefix;
        this.prefixComponent = new TextComponent(TextComponent.fromLegacyText(Chat.color(prefix)));
        this.placeholder = false;
    }

    public Chat(String prefix, boolean placeholder){
        this.prefix = prefix;
        this.prefixComponent = new TextComponent(TextComponent.fromLegacyText(Chat.color(prefix)));
        this.placeholder = placeholder;
    }

    public Chat(BaseComponent prefix){
        this.prefix = prefix.toPlainText();
        this.prefixComponent = prefix;
    }

    public Chat(BaseComponent prefix, boolean placeholder){
        this.prefix = prefix.toPlainText();
        this.prefixComponent = prefix;
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

    public void sendPlayer(BaseComponent text, ProxiedPlayer p) {
        p.sendMessage(replaceTC(buildChatPrefix(text), p));
    }

    public void sendPlayerNoPrefix(BaseComponent text, ProxiedPlayer p) {
        p.sendMessage(replaceTC(text, p));
    }

    public void sendSender(BaseComponent text, CommandSender s) {
        s.sendMessage(replaceTC(buildChatPrefix(text), (ProxiedPlayer) s));
    }

    public void sendSenderNoPrefix(BaseComponent text, CommandSender s) {
        s.sendMessage(replaceTC(text, (ProxiedPlayer) s));
    }

    public void sendAllPlayers(BaseComponent text) {
        for(ProxiedPlayer p: ProxyServer.getInstance().getPlayers()) {
            p.sendMessage(replaceTC(buildChatPrefix(text), p));
        }
    }

    public void sendAllPlayersNoPrefix(BaseComponent text) {
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

    private BaseComponent replaceTC(BaseComponent text, ProxiedPlayer player) {
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
