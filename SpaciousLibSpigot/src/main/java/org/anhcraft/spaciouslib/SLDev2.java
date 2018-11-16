package org.anhcraft.spaciouslib;

import org.anhcraft.spaciouslib.annotations.AnnotationHandler;
import org.anhcraft.spaciouslib.annotations.ConfigOption;
import org.anhcraft.spaciouslib.annotations.SpaciousAnnotation;
import org.anhcraft.spaciouslib.builders.command.*;
import org.anhcraft.spaciouslib.protocol.ActionBar;
import org.anhcraft.spaciouslib.utils.MathUtils;
import org.anhcraft.spaciouslib.utils.ServerUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class SLDev2 implements Listener {
    public static final String FILE = "plugins/SpaciousLib/config.yml";

    @ConfigOption(path = "stats", file = FILE)
    public static boolean stats;
    @ConfigOption(path = "dev_mode", file = FILE)
    public static boolean dev_mode;
    public static CommandBuilder cb = new CommandBuilder("test", new CommandCallback() {
        @Override
        public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
            for(int i = 0; i < builder.getCommands(); i++) {
                sender.spigot().sendMessage(builder.toTextComponent(i, true, true));
            }
        }
    }, "testing command").addChild(new ChildCommandBuilder().path("a", new CommandCallback() {
                @Override
                public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                    sender.sendMessage("a");
                }
            }).path("b", new CommandCallback() {
                @Override
                public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                    sender.sendMessage("a b");
                }
            }, "this is a test command").build()
    ).addChild(new ChildCommandBuilder().path("b", new CommandCallback() {
                @Override
                public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                    sender.sendMessage("b");
                }
            }).var("c", new CommandCallback() {
                @Override
                public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                    sender.sendMessage("b c: "+value);
                }
            }, ArgumentType.ANYTHING).build()
    ).addChild(new ChildCommandBuilder().path("c", new CommandCallback() {
                @Override
                public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                    sender.sendMessage("c");
                }
            }).var("a", new CommandCallback() {
                @Override
                public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                    sender.sendMessage("c a:"+value);
                }
            }, ArgumentType.ONLINE_PLAYER).var("b", new CommandCallback() {
                @Override
                public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                    sender.sendMessage("c a b:"+value);
                }
            }, ArgumentType.INTEGER).build()
    ).addChild(new ChildCommandBuilder().path("d", new CommandCallback() {
                @Override
                public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                    sender.sendMessage("d");
                }
            }).var("aaa", new CommandCallback() {
                @Override
                public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                    sender.sendMessage("d aaa:"+value);
                }
            }, ArgumentType.EMAIL).path("bab", new CommandCallback() {
                @Override
                public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                    sender.sendMessage("d aaa bab:"+value);
                }
            }).build()
    ).addChild(new ChildCommandBuilder().path("e", new CommandCallback() {
                @Override
                public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                    sender.sendMessage("e");
                }
            }).path("0122", new CommandCallback() {
                @Override
                public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                    sender.sendMessage("e 0122");
                }
            }).path("7870", new CommandCallback() {
                @Override
                public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                    sender.sendMessage("e 0122 7870");
                }
            }).path("2485", new CommandCallback() {
                @Override
                public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                    sender.sendMessage("e 0122 7870 2485");
                }
            }).path("1473", new CommandCallback() {
                @Override
                public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                    sender.sendMessage("e 0122 7870 2485 1473");
                }
            }).build()
    );

    public static void main(String[] args){
        for(int i = 0; i < cb.getCommands(); i++){
            System.out.println(cb.toString(i, true).replace("§", "&"));
            System.out.println(cb.toString(i, true, true).replace("§", "&"));
        }
    }

    public SLDev2(){
        AnnotationHandler.register(this.getClass(), this);
        SpaciousAnnotation.reloadConfig(FILE);
        System.out.println("STATS: "+stats);
        System.out.println("DEV MODE: "+dev_mode);
        new BukkitRunnable() {
            @Override
            public void run() {
                ActionBar.create("&aTPS: &f"+ MathUtils.round(ServerUtils.getTPS()[0])).sendAll();
            }
        }.runTaskTimerAsynchronously(SpaciousLib.instance, 0, 40);
        // /test
        // /test a b
        // /test b <c>
        // /test c [a <b>]
        cb.build(SpaciousLib.instance);
    }
}
