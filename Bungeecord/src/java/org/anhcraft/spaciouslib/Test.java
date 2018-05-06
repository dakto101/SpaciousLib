package org.anhcraft.spaciouslib;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import org.anhcraft.spaciouslib.command.CommandArgument;
import org.anhcraft.spaciouslib.command.CommandBuilder;
import org.anhcraft.spaciouslib.command.CommandRunnable;
import org.anhcraft.spaciouslib.command.SubCommandBuilder;
import org.anhcraft.spaciouslib.entity.PlayerManager;
import org.anhcraft.spaciouslib.mojang.MojangAPI;
import org.anhcraft.spaciouslib.mojang.SkinAPI;

public class Test implements Listener {
    public Test(){
        try {
            new CommandBuilder("slb", new CommandRunnable() {
                @Override
                public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                }
            })
                    .addSubCommand(new SubCommandBuilder("skin", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            sender.sendMessage(cmd.getCommandAsString(subcmd, true));
                        }
                    })
                            .addArgument("player", new CommandRunnable() {
                                @Override
                                public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                                    if(sender instanceof ProxiedPlayer) {
                                        try {
                                            new PlayerManager((ProxiedPlayer) sender).changeSkin(
                                                    SkinAPI.getSkin(MojangAPI.getUUID(value).getB()).getSkin());
                                        } catch(Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }, CommandArgument.Type.CUSTOM, false)).buildExecutor(SpaciousLib.instance);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
