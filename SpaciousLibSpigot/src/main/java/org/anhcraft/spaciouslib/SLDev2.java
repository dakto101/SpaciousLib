package org.anhcraft.spaciouslib;

import org.anhcraft.spaciouslib.annotations.AnnotationHandler;
import org.anhcraft.spaciouslib.annotations.ConfigOption;
import org.anhcraft.spaciouslib.annotations.SpaciousAnnotation;
import org.anhcraft.spaciouslib.builders.command.ArgumentType;
import org.anhcraft.spaciouslib.builders.command.ChildCommandBuilder;
import org.anhcraft.spaciouslib.builders.command.CommandBuilder;
import org.anhcraft.spaciouslib.builders.command.CommandCallback;
import org.anhcraft.spaciouslib.mojang.CachedSkin;
import org.anhcraft.spaciouslib.mojang.Skin;
import org.anhcraft.spaciouslib.protocol.ActionBar;
import org.anhcraft.spaciouslib.serialization.DataSerialization;
import org.anhcraft.spaciouslib.utils.CommonUtils;
import org.anhcraft.spaciouslib.utils.MathUtils;
import org.anhcraft.spaciouslib.utils.ServerUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class SLDev2 implements Listener {
    public static final String FILE = "plugins/SpaciousLib/config.yml";

    @ConfigOption(path = "stats", file = FILE)
    public static boolean stats;
    @ConfigOption(path = "dev_mode", file = FILE)
    public static boolean dev_mode;

    public static void main(String[] args){
        CachedSkin cs = new CachedSkin(Skin.STEVE, UUID.randomUUID(), 10);
        byte[] data = DataSerialization.serialize(CachedSkin.class, cs).getA();
        System.out.println(CommonUtils.compare(cs, DataSerialization.deserialize(CachedSkin.class, data)));
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

        new CommandBuilder("test", new CommandCallback() {
            @Override
            public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                for(int i = 0; i < builder.getCommands(); i++) {
                    sender.spigot().sendMessage(builder.toTextComponent(i, true, true));
                }
            }
        }, "testing command").addChild(new ChildCommandBuilder().path("dxffdtwmze", new CommandCallback() {
                    @Override
                    public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                        sender.sendMessage("dxffdtwmze");
                    }
                }).path("nhtthelcif", new CommandCallback() {
                    @Override
                    public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                        sender.sendMessage("dxffdtwmze nhtthelcif");
                    }
                }, "this is a test command").build()
        ).addChild(new ChildCommandBuilder().path("bweiaefxof", new CommandCallback() {
                    @Override
                    public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                        sender.sendMessage("bweiaefxof");
                    }
                }).var("aclavygsba", new CommandCallback() {
                    @Override
                    public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                        sender.sendMessage("bweiaefxof aclavygsba: "+value);
                    }
                }, ArgumentType.ANYTHING).build()
        ).addChild(new ChildCommandBuilder().path("8s5eq6z1rw", new CommandCallback() {
                    @Override
                    public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                        sender.sendMessage("8s5eq6z1rw");
                    }
                }).var("ob60yzdks8", new CommandCallback() {
                    @Override
                    public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                        sender.sendMessage("8s5eq6z1rw ob60yzdks8:"+value);
                    }
                }, ArgumentType.ONLINE_PLAYER).var("5ed63bdynt", new CommandCallback() {
                    @Override
                    public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                        sender.sendMessage("8s5eq6z1rw ob60yzdks8 5ed63bdynt:"+value);
                    }
                }, ArgumentType.INTEGER).build()
        ).addChild(new ChildCommandBuilder().path("rgikblwytx", new CommandCallback() {
                    @Override
                    public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                        sender.sendMessage("rgikblwytx");
                    }
                }).var("yeyhkjdxea", new CommandCallback() {
                    @Override
                    public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                        sender.sendMessage("rgikblwytx yeyhkjdxea:"+value);
                    }
                }, ArgumentType.EMAIL).var("bab", new CommandCallback() {
                    @Override
                    public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                        sender.sendMessage("rgikblwytx yeyhkjdxea hgleprjzzd:"+value);
                    }
                }, ArgumentType.WORLD).build()
        ).addChild(new ChildCommandBuilder().path("7465634628 0635284628", new CommandCallback() {
                    @Override
                    public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                        sender.sendMessage("7465634628 0635284628");
                    }
                }).path("skrbejbbkx yhopiryvbl", new CommandCallback() {
                    @Override
                    public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                        sender.sendMessage("7465634628 0635284628 skrbejbbkx yhopiryvbl");
                    }
                }).var("x3dk8e230u", new CommandCallback() {
                    @Override
                    public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                        sender.sendMessage("7465634628 0635284628 skrbejbbkx yhopiryvbl x3dk8e230u: "+value);
                    }
                }, ArgumentType.URL).build()
        ).addAlias("tezt").build(SpaciousLib.instance);


        new CommandBuilder("test2", new ChildCommandBuilder().root(new CommandCallback() {
            @Override
            public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                sender.sendMessage("root");
            }
        }).var("1fxc761i3u", new CommandCallback() {
            @Override
            public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                sender.sendMessage("root 1fxc761i3u: "+value);
            }
        }, ArgumentType.ANYTHING).var("iqq5rzyw7d", new CommandCallback() {
            @Override
            public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                sender.sendMessage("root 1fxc761i3u iqq5rzyw7d: "+value);
            }
        }, ArgumentType.NEGATIVE_REAL_NUMBER).build(), "testing command 2").addAlias("tezt2").build(SpaciousLib.instance);
    }
}
