package org.anhcraft.spaciouslib;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import org.anhcraft.spaciouslib.command.CommandArgument;
import org.anhcraft.spaciouslib.command.CommandBuilder;
import org.anhcraft.spaciouslib.command.CommandRunnable;
import org.anhcraft.spaciouslib.command.SubCommandBuilder;
import org.anhcraft.spaciouslib.mojang.MojangAPI;
import org.anhcraft.spaciouslib.mojang.SkinAPI;
import org.anhcraft.spaciouslib.utils.PlayerUtils;

public class SLDev implements Listener {
    public SLDev(){
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
                                            PlayerUtils.changeSkin((ProxiedPlayer) sender,
                                                    SkinAPI.getSkin(MojangAPI.getUniqueId(value).getB()).getSkin());
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
