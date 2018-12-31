package org.anhcraft.spaciouslib;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import org.anhcraft.spaciouslib.builders.command.ArgumentType;
import org.anhcraft.spaciouslib.builders.command.ChildCommandBuilder;
import org.anhcraft.spaciouslib.builders.command.CommandBuilder;
import org.anhcraft.spaciouslib.builders.command.CommandCallback;
import org.anhcraft.spaciouslib.mojang.MojangAPI;
import org.anhcraft.spaciouslib.mojang.SkinAPI;
import org.anhcraft.spaciouslib.utils.PlayerUtils;

public class SLDev implements Listener {
    public SLDev(){
        new CommandBuilder("slb", new CommandCallback() {
            @Override
            public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                builder.sendHelpMessages(sender,true, true);
            }
        }).addChild(new ChildCommandBuilder().path("skin").var("player", new CommandCallback() {
            @Override
            public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                if(sender instanceof ProxiedPlayer) {
                    try {
                        PlayerUtils.changeSkin((ProxiedPlayer) sender,
                                SkinAPI.getSkin(MojangAPI.getUniqueId(value).getB()).getSkin());
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, ArgumentType.ONLINE_PLAYER).build()).build(SpaciousLib.instance);

        new CommandBuilder("test1", new ChildCommandBuilder().root().path("ping", new CommandCallback() {
            @Override
            public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                sender.sendMessage("Pong");
            }
        }).build()).build(SpaciousLib.instance);

        new CommandBuilder("test2", new ChildCommandBuilder().root().var("ping", new CommandCallback() {
            @Override
            public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                sender.sendMessage("Pong: "+value);
            }
        }, ArgumentType.ANYTHING).build()).build(SpaciousLib.instance);

        new CommandBuilder("test3", new CommandCallback() {
            @Override
            public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                builder.sendHelpMessages(sender,true, true);
            }
        }).addChild(new ChildCommandBuilder().var("ping", new CommandCallback() {
            @Override
            public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                sender.sendMessage("Pong: "+value);
            }
        }, ArgumentType.ANYTHING).build()).build(SpaciousLib.instance);

        new CommandBuilder("test4", new CommandCallback() {
            @Override
            public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                builder.sendHelpMessages(sender,true, true);
            }
        }).addChild(new ChildCommandBuilder().path("ping", new CommandCallback() {
            @Override
            public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                sender.sendMessage("Pong: "+value);
            }
        }).build()).build(SpaciousLib.instance);

        new CommandBuilder("test5", new CommandCallback() {
            @Override
            public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                builder.sendHelpMessages(sender,true, true);
            }
        }).addChild(new ChildCommandBuilder().path("check").path("ping", new CommandCallback() {
            @Override
            public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                sender.sendMessage("Pong: "+value);
            }
        }).build()).build(SpaciousLib.instance);

        new CommandBuilder("test6", new CommandCallback() {
            @Override
            public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                builder.sendHelpMessages(sender,true, true);
            }
        }).addChild(new ChildCommandBuilder().path("check").var("ping", new CommandCallback() {
            @Override
            public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                sender.sendMessage("Pong: "+value);
            }
        }, ArgumentType.ANYTHING).build()).build(SpaciousLib.instance);
    }
}
