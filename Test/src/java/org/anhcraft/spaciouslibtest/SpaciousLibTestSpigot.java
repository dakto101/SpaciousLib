package org.anhcraft.spaciouslibtest;

import org.anhcraft.spaciouslib.anvil.AnvilForm;
import org.anhcraft.spaciouslib.attribute.Attribute;
import org.anhcraft.spaciouslib.attribute.AttributeModifier;
import org.anhcraft.spaciouslib.bungee.BungeeManager;
import org.anhcraft.spaciouslib.bungee.BungeePlayerIPResponse;
import org.anhcraft.spaciouslib.command.CommandArgument;
import org.anhcraft.spaciouslib.command.CommandBuilder;
import org.anhcraft.spaciouslib.command.CommandRunnable;
import org.anhcraft.spaciouslib.command.SubCommandBuilder;
import org.anhcraft.spaciouslib.entity.EntityManager;
import org.anhcraft.spaciouslib.events.BungeeForwardEvent;
import org.anhcraft.spaciouslib.events.PacketHandleEvent;
import org.anhcraft.spaciouslib.inventory.BookManager;
import org.anhcraft.spaciouslib.inventory.EquipSlot;
import org.anhcraft.spaciouslib.inventory.ItemManager;
import org.anhcraft.spaciouslib.inventory.RecipeManager;
import org.anhcraft.spaciouslib.io.DirectoryManager;
import org.anhcraft.spaciouslib.io.FileManager;
import org.anhcraft.spaciouslib.placeholder.Placeholder;
import org.anhcraft.spaciouslib.placeholder.PlaceholderManager;
import org.anhcraft.spaciouslib.protocol.*;
import org.anhcraft.spaciouslib.socket.ServerSocketClientHandler;
import org.anhcraft.spaciouslib.socket.ServerSocketRequestHandler;
import org.anhcraft.spaciouslib.socket.SocketManager;
import org.anhcraft.spaciouslib.utils.RandomUtils;
import org.anhcraft.spaciouslib.utils.StringUtils;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SpaciousLibTestSpigot extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        new DirectoryManager("plugins/SpaciousLibTest/").mkdirs();
        try {
            new FileManager("plugins/SpaciousLibTest/test.txt").initFile(IOUtils.toByteArray(getClass().getResourceAsStream("/test.txt")));
        } catch(IOException e) {
            e.printStackTrace();
        }
        try {
            new CommandBuilder("slt",
                    new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder sCommand, SubCommandBuilder subCommand, CommandSender commandSender, String[] strings, String s) {
                            for(String str : sCommand.getCommandsAsString(true)) {
                                commandSender.sendMessage(str);
                            }
                        }
                    })

                    .addSubCommand(new SubCommandBuilder("particle spawn", "Spawn a specific type of particle at your location", new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder sCommand, SubCommandBuilder subCommand, CommandSender commandSender, String[] strings, String s) {
                            commandSender.sendMessage(sCommand.getCommandAsString(subCommand, true));
                        }
                    }).addArgument(
                            new CommandArgument("type", new CommandRunnable() {
                                @Override
                                public void run(CommandBuilder sCommand, SubCommandBuilder subCommand, CommandSender commandSender, String[] strings, String s) {
                                    if(commandSender instanceof Player) {
                                        Particle.Type type = StringUtils.get(strings[0].toUpperCase(), Particle.Type.values());
                                        if(type == null) {
                                            commandSender.sendMessage("Invalid particle type!");
                                        } else {
                                            Location location = ((Player) commandSender).getLocation();
                                            for(int degree = 0; degree < 360; degree++) {
                                                double radians = Math.toRadians(degree);
                                                double x = Math.cos(radians) * 3;
                                                double z = Math.sin(radians) * 3;
                                                location.add(x, 0, z);
                                                Particle.create(type, location, 10).sendWorld(((Player) commandSender).getWorld());
                                                location.subtract(x, 0, z);
                                            }
                                        }
                                    }
                                }
                            }, false),
                            CommandArgument.Type.CUSTOM
                    ).addArgument(
                            new CommandArgument("count", new CommandRunnable() {
                                @Override
                                public void run(CommandBuilder sCommand, SubCommandBuilder subCommand, CommandSender commandSender, String[] strings, String s) {
                                    if(commandSender instanceof Player) {
                                        Particle.Type type = StringUtils.get(strings[0], Particle.Type.values());
                                        if(type == null) {
                                            commandSender.sendMessage("Invalid particle type!");
                                        } else {
                                            Location location = ((Player) commandSender).getLocation();
                                            for(int degree = 0; degree < 360; degree++) {
                                                double radians = Math.toRadians(degree);
                                                double x = Math.cos(radians) * 3;
                                                double z = Math.sin(radians) * 3;
                                                location.add(x, 0, z);
                                                Particle.create(type, location, StringUtils.toIntegerNumber(strings[1])).sendWorld(((Player) commandSender).getWorld());
                                                location.subtract(x, 0, z);
                                            }
                                        }
                                    }
                                }
                            }, true),
                            CommandArgument.Type.INTEGER_NUMBER
                    ).hideTypeCommandString())

                    .addSubCommand(new SubCommandBuilder("particle list", "show all types of particle", new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder sCommand, SubCommandBuilder subCommand, CommandSender commandSender, String[] strings, String s) {
                            StringBuilder pls = new StringBuilder();
                            for(Particle.Type p : Particle.Type.values()) {
                                pls.append(p.toString()).append(" ");
                            }
                            commandSender.sendMessage(pls.toString());
                        }
                    }))

                    .addSubCommand(
                            new SubCommandBuilder("playerlist set", null, new CommandRunnable() {
                                @Override
                                public void run(CommandBuilder sCommand, SubCommandBuilder subCommand, CommandSender commandSender, String[] strings, String s) {
                                    commandSender.sendMessage(sCommand.getCommandAsString(subCommand, true));
                                }
                            })
                                    .addArgument("header", new CommandRunnable() {
                                        @Override
                                        public void run(CommandBuilder sCommand, SubCommandBuilder subCommand, CommandSender commandSender, String[] strings, String s) {
                                            commandSender.sendMessage(sCommand.getCommandAsString(subCommand, true));
                                        }
                                    }, CommandArgument.Type.CUSTOM, false)
                                    .addArgument("footer", new CommandRunnable() {
                                        @Override
                                        public void run(CommandBuilder sCommand, SubCommandBuilder subCommand, CommandSender commandSender, String[] strings, String s) {
                                            PlayerList.create(strings[0], strings[1]).sendAll();
                                        }
                                    }, CommandArgument.Type.CUSTOM, false)
                    )

                    .addSubCommand(new SubCommandBuilder("playerlist remove", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder sCommand, SubCommandBuilder subCommand, CommandSender commandSender, String[] strings, String s) {
                            PlayerList.remove().sendAll();
                        }
                    }))

                    .addSubCommand(new SubCommandBuilder("camera", "View as a random nearby entity", new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] strings, String s) {
                            if(sender instanceof Player) {
                                Entity e = RandomUtils.pickRandom(((Player) sender).getNearbyEntities(5, 5, 5));
                                Camera.create(e).sendPlayer((Player) sender);
                            }
                        }
                    }))


                    .addSubCommand(new SubCommandBuilder("camera reset", "Reset your view to normal", new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] strings, String s) {
                            if(sender instanceof Player) {
                                if(sender instanceof Player) {
                                    Camera.create((Player) sender).sendPlayer((Player) sender);
                                }
                            }
                        }
                    }))

                    .addSubCommand(new SubCommandBuilder("bungee tp", "Teleport you or a specific player to a server in Bungee network", new CommandRunnable() {
                                @Override
                                public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                                    sender.sendMessage(cmd.getCommandAsString(subcmd, true));
                                }
                            }).addArgument("server", new CommandRunnable() {
                                @Override
                                public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                                    if(sender instanceof Player) {
                                        BungeeManager.connect((Player) sender, value);
                                    }
                                }
                            }, CommandArgument.Type.CUSTOM, false)
                                    .addArgument("player", new CommandRunnable() {
                                        @Override
                                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                                            if(sender instanceof Player) {
                                                BungeeManager.connect(value, args[0]);
                                            }
                                        }
                                    }, CommandArgument.Type.ONLINE_PLAYER, true)
                    )

                    .addSubCommand(new SubCommandBuilder("bungee ip", "Get your real IP", new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            if(sender instanceof Player) {
                                BungeeManager.getIP((Player) sender, new BungeePlayerIPResponse() {
                                    @Override
                                    public void result(String ip, int port) {
                                        sender.sendMessage(ip + ":" + port);
                                    }
                                });
                            }
                        }
                    }))
                    .buildExecutor(this)

                    .addSubCommand(new SubCommandBuilder("bungee data", null, new CommandRunnable() {
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
                    .clone("sl").buildExecutor(this)
                    .clone("spaciouslib").buildExecutor(this);
        } catch(Exception e) {
            e.printStackTrace();
        }
        getServer().getPluginManager().registerEvents(this, this);

        System.out.println("Starting socket server...");
        SocketManager.registerServer(25568, new ServerSocketRequestHandler() {
            @Override
            public void request(ServerSocketClientHandler client, String data) {
                System.out.println("Client >> " + data);
            }
        });

        for(Placeholder placeholder : PlaceholderManager.getPlaceholders()){
            System.out.println(placeholder.getPlaceholder());
        }
    }

    @EventHandler
    public void ev(PacketHandleEvent ev) {
        if(ev.getType().equals(PacketHandleEvent.Type.SERVER_BOUND)) {
            if(ev.getPacket().getClass().getSimpleName().equals("PacketPlayInChat")) {
                if(ev.getPacketValue("a").toString().equalsIgnoreCase("testslt")) {
                    ev.getPlayer().getInventory().addItem(
                            new BookManager("&aRule", 1)
                                    .setAuthor("anhcraft")
                                    .setTitle("RULE")
                                    .setBookGeneration(BookManager.BookGeneration.ORIGINAL)
                                    .addPage(PlaceholderManager.replace(
                                               "Name: {player_name}\n"+
                                                    "Level: {player_level}\n" +
                                                    "Exp: {player_exp}\n" +
                                                    "Health: {player_health}/{player_max_health}",
                                            ev.getPlayer()))
                                    .addPage("Second page")
                                    .addPage("Third page")
                                    .setUnbreakable(true)
                                    .addAttributeModifier(
                                            Attribute.Type.GENERIC_MOVEMENT_SPEED,
                                            new AttributeModifier(
                                                    "test",
                                                    0.05,
                                                    AttributeModifier.Operation.ADD
                                            ),
                                            EquipSlot.MAINHAND)
                                    .addLore("This is the rule book of this server!")
                                    .addEnchant(Enchantment.DAMAGE_ALL, 1)
                                    .addFlag(ItemFlag.HIDE_ENCHANTS)
                                    .getItem());
                    ev.getPlayer().updateInventory();
                    ev.setCancelled(true);

                    new EntityManager(ev.getPlayer()).setAttribute(
                            new Attribute(Attribute.Type.GENERIC_ARMOR)
                            .addModifier(new AttributeModifier("1", 3, AttributeModifier.Operation.ADD)));

                    AnvilForm.create(new ItemManager("test", Material.DIAMOND, 1).getItem(),
                            new AnvilForm.AnvilRunnable() {
                                @Override
                                public void run(String input) {
                                    ev.getPlayer().sendMessage(input);
                                }
                            }, ev.getPlayer(), this);

                    ShapedRecipe r = new ShapedRecipe(new ItemManager("test111", Material.DIAMOND_SWORD, 1).setUnbreakable(true).addEnchant(Enchantment.DAMAGE_ALL, 5).addLore("tesssssss").addAttributeModifier(Attribute.Type.GENERIC_MAX_HEALTH, new AttributeModifier("1", 5, AttributeModifier.Operation.ADD), EquipSlot.MAINHAND).addAttributeModifier(Attribute.Type.GENERIC_ATTACK_SPEED, new AttributeModifier("2", 10, AttributeModifier.Operation.ADD), EquipSlot.MAINHAND).addAttributeModifier(Attribute.Type.GENERIC_ATTACK_DAMAGE, new AttributeModifier("3", 20, AttributeModifier.Operation.ADD), EquipSlot.MAINHAND).getItem());
                    r.shape("aaa", "aaa", "aaa");
                    r.setIngredient('a', Material.DIAMOND);
                    RecipeManager.register(r);

                    ActionBar.create("test").sendAll();
                    Animation.create(ev.getPlayer(), Animation.Type.SWING_MAIN_HAND).sendPlayer(ev.getPlayer());
                    BlockBreakAnimation.create(0, ev.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN), 5).sendWorld(ev.getPlayer().getWorld());
                    Particle.create(Particle.Type.BARRIER, ev.getPlayer().getLocation(), 5).sendWorld(ev.getPlayer().getWorld());
                    PlayerList.create("aaa", "bbb").sendAll();
                    Title.create("wwwwwwwww", Title.Type.TITLE).sendPlayer(ev.getPlayer());
                    Camera.create(ev.getPlayer()).sendPlayer(ev.getPlayer());
                }
            }
        }
    }

    @EventHandler
    public void forward(BungeeForwardEvent ev) {
        if(ev.getChannel().equals("slt")) {
            try {
                Bukkit.getServer().broadcastMessage("Forward from: " + ev.getData().readUTF());
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDisable() {
    }
}