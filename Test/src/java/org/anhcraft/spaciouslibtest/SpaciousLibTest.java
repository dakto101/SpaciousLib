package org.anhcraft.spaciouslibtest;

import org.anhcraft.spaciouslib.bungee.BungeeManager;
import org.anhcraft.spaciouslib.bungee.BungeePlayerIPResponse;
import org.anhcraft.spaciouslib.command.CommandArgument;
import org.anhcraft.spaciouslib.command.CommandBuilder;
import org.anhcraft.spaciouslib.command.CommandRunnable;
import org.anhcraft.spaciouslib.command.SubCommandBuilder;
import org.anhcraft.spaciouslib.events.BungeeForwardEvent;
import org.anhcraft.spaciouslib.events.PacketHandleEvent;
import org.anhcraft.spaciouslib.inventory.AttributeType;
import org.anhcraft.spaciouslib.inventory.BookManager;
import org.anhcraft.spaciouslib.inventory.EquipSlot;
import org.anhcraft.spaciouslib.protocol.Camera;
import org.anhcraft.spaciouslib.protocol.Particle;
import org.anhcraft.spaciouslib.protocol.PlayerList;
import org.anhcraft.spaciouslib.socket.*;
import org.anhcraft.spaciouslib.utils.RandomUtils;
import org.anhcraft.spaciouslib.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class SpaciousLibTest extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        try {
            new CommandBuilder("slt",
                    new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder sCommand, SubCommandBuilder subCommand, CommandSender commandSender, String[] strings, String s) {
                            for(String str : sCommand.getCommandsAsString(true)){
                                commandSender.sendMessage(str);
                            }
                        }
                    })

                    .addSubCommandBuilder(new SubCommandBuilder("particle spawn", "Spawn a specific type of particle at your location", new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder sCommand, SubCommandBuilder subCommand, CommandSender commandSender, String[] strings, String s) {
                            commandSender.sendMessage(sCommand.getCommandAsString(subCommand, true));
                        }
                    }).setArgument(
                            new CommandArgument("type", new CommandRunnable() {
                                @Override
                                public void run(CommandBuilder sCommand, SubCommandBuilder subCommand, CommandSender commandSender, String[] strings, String s) {
                                    if(commandSender instanceof Player) {
                                        Particle.Type type = StringUtils.get(strings[0].toUpperCase(), Particle.Type.values());
                                        if(type == null) {
                                            commandSender.sendMessage("Invalid particle type!");
                                        } else {
                                            Location location = ((Player) commandSender).getLocation();
                                            for (int degree = 0; degree < 360; degree++) {
                                                double radians = Math.toRadians(degree);
                                                double x = Math.cos(radians) * 3;
                                                double z = Math.sin(radians) * 3;
                                                location.add(x,0,z);
                                                Particle.create(type, location, 10).sendWorld(((Player) commandSender).getWorld());
                                                location.subtract(x,0,z);
                                            }
                                        }
                                    }
                                }
                            }, false),
                            CommandArgument.Type.CUSTOM
                    ).setArgument(
                            new CommandArgument("count", new CommandRunnable() {
                                @Override
                                public void run(CommandBuilder sCommand, SubCommandBuilder subCommand, CommandSender commandSender, String[] strings, String s) {
                                    if(commandSender instanceof Player) {
                                        Particle.Type type = StringUtils.get(strings[0], Particle.Type.values());
                                        if(type == null) {
                                            commandSender.sendMessage("Invalid particle type!");
                                        } else {
                                            Location location = ((Player) commandSender).getLocation();
                                            for (int degree = 0; degree < 360; degree++) {
                                                double radians = Math.toRadians(degree);
                                                double x = Math.cos(radians) * 3;
                                                double z = Math.sin(radians) * 3;
                                                location.add(x,0,z);
                                                Particle.create(type, location, StringUtils.toIntegerNumber(strings[1])).sendWorld(((Player) commandSender).getWorld());
                                                location.subtract(x,0,z);
                                            }
                                        }
                                    }
                                }
                            }, true),
                            CommandArgument.Type.INTEGER_NUMBER
                    ).hideTypeCommandString())

                    .addSubCommandBuilder(new SubCommandBuilder("particle list", "show all types of particle", new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder sCommand, SubCommandBuilder subCommand, CommandSender commandSender, String[] strings, String s) {
                            StringBuilder pls = new StringBuilder();
                            for(Particle.Type p : Particle.Type.values()){
                                pls.append(p.toString()).append(" ");
                            }
                            commandSender.sendMessage(pls.toString());
                        }
                    }))

                    .addSubCommandBuilder(
                            new SubCommandBuilder("playerlist set", null, new CommandRunnable() {
                                @Override
                                public void run(CommandBuilder sCommand, SubCommandBuilder subCommand, CommandSender commandSender, String[] strings, String s) {
                                    commandSender.sendMessage(sCommand.getCommandAsString(subCommand, true));
                                }
                            })
                                    .setArgument("header", new CommandRunnable() {
                                        @Override
                                        public void run(CommandBuilder sCommand, SubCommandBuilder subCommand, CommandSender commandSender, String[] strings, String s) {
                                            commandSender.sendMessage(sCommand.getCommandAsString(subCommand, true));
                                        }
                                    }, CommandArgument.Type.CUSTOM, false)
                                    .setArgument("footer", new CommandRunnable() {
                                        @Override
                                        public void run(CommandBuilder sCommand, SubCommandBuilder subCommand, CommandSender commandSender, String[] strings, String s) {
                                            PlayerList.create(strings[0], strings[1]).sendAll();
                                        }
                                    }, CommandArgument.Type.CUSTOM, false)
                    )

                    .addSubCommandBuilder(new SubCommandBuilder("playerlist remove", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder sCommand, SubCommandBuilder subCommand, CommandSender commandSender, String[] strings, String s) {
                            PlayerList.remove();
                        }
                    }))

                    .addSubCommandBuilder(new SubCommandBuilder("camera", "View as a random nearby entity", new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] strings, String s) {
                            if(sender instanceof Player){
                                Entity e = RandomUtils.pickRandom(((Player) sender).getNearbyEntities(5, 5, 5));
                                Camera.create(e).sendPlayer((Player) sender);
                            }
                        }
                    }))


                    .addSubCommandBuilder(new SubCommandBuilder("camera reset", "Reset your view to normal", new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] strings, String s) {
                            if(sender instanceof Player){
                                if(sender instanceof Player){
                                    Camera.create((Player) sender).sendPlayer((Player) sender);
                                }
                            }
                        }
                    }))

                    .addSubCommandBuilder(new SubCommandBuilder("bungee tp", "Teleport you or a specific player to a server in Bungee network", new CommandRunnable() {
                                @Override
                                public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                                    sender.sendMessage(cmd.getCommandAsString(subcmd, true));
                                }
                            }).setArgument("server", new CommandRunnable() {
                                @Override
                                public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                                    if(sender instanceof Player){
                                        BungeeManager.connect((Player) sender, value);
                                    }
                                }
                            }, CommandArgument.Type.CUSTOM, false)
                                    .setArgument("player", new CommandRunnable() {
                                        @Override
                                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                                            if(sender instanceof Player){
                                                BungeeManager.connect((Player) sender, value, args[0]);
                                            }
                                        }
                                    }, CommandArgument.Type.ONLINE_PLAYER, true)
                    )

                    .addSubCommandBuilder(new SubCommandBuilder("bungee ip", "Get your real IP", new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            if(sender instanceof Player){
                                BungeeManager.getIP((Player) sender, new BungeePlayerIPResponse() {
                                    @Override
                                    public void result(String ip, int port) {
                                        sender.sendMessage(ip+":"+port);
                                    }
                                });
                            }
                        }
                    }))
                    .buildExecutor(this)

                    .addSubCommandBuilder(new SubCommandBuilder("bungee data", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            sender.sendMessage("sent successfully!");
                            ByteArrayOutputStream bytedata = new ByteArrayOutputStream();
                            DataOutputStream data = new DataOutputStream(bytedata);
                            try {
                                data.writeUTF(sender.getName());
                            } catch(IOException e) {
                                e.printStackTrace();
                            }
                            BungeeManager.forwardData("slt", bytedata.toByteArray());
                        }
                    }))
                    .buildExecutor(this)
                    .newAlias("sl").buildExecutor(this)
                    .newAlias("spaciouslib").buildExecutor(this);
        } catch(Exception e) {
            e.printStackTrace();
        }
        getServer().getPluginManager().registerEvents(this, this);

        System.out.println("Starting socket server...");
        SocketManager.registerServer(25568, new ServerSocketRequestHandler() {
            @Override
            public void request(ServerSocketClientHandler client, String data) {
                System.out.println("Client >> " + data);
                if(data.equals("Hi server!")){
                    try {
                        client.send("Hi client!");
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
                if(data.equals("Bye server!")){
                    try {
                        client.close();
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        System.out.println("Starting socket client...");
        try {
            ClientSocketManager c = SocketManager.registerClient("localhost", 25568, new ClientSocketRequestHandler() {
                @Override
                public void request(ClientSocketManager manager, String data)  {
                    System.out.println("Server >> " + data);
                }
            }).send("Hi server!").send("Bye server!");
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        c.close();
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
            }.runTaskLaterAsynchronously(this, 40);
        } catch(IOException e) {

            e.printStackTrace();
        }
    }

    @EventHandler
    public void ev(PacketHandleEvent ev){
        if(ev.getType().equals(PacketHandleEvent.Type.SERVER_BOUND)){
            if(ev.getPacket().getClass().getSimpleName().equals("PacketPlayInChat")){
                if(ev.getPacketValue("a").toString().equalsIgnoreCase("tchat")){
                    ev.getPlayer().getInventory().addItem(
                            new BookManager("&aRule", 1)
                                    .setAuthor("anhcraft")
                                    .setTitle("RULE")
                                    .setBookGeneration(BookManager.BookGeneration.ORIGINAL)
                                    .addPage("First page")
                                    .addPage("Second page")
                                    .addPage("Third page")
                                    .setUnbreakable(true)
                                    .addAttribute(AttributeType.MOVEMENT_SPEED, 0.05, EquipSlot.MAINHAND)
                                    .addLore("This is the rule book of this server!")
                                    .addEnchant(Enchantment.DAMAGE_ALL, 1)
                                    .addFlag(ItemFlag.HIDE_ENCHANTS)
                                    .getItem());
                    ev.getPlayer().updateInventory();
                    ev.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void forward(BungeeForwardEvent ev){
        if(ev.getChannel().equals("slt")){
            try {
                Bukkit.getServer().broadcastMessage("Forward from: "+ev.getData().readUTF());
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDisable() {

    }
}