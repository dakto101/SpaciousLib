package org.anhcraft.spaciouslibtest;

import org.anhcraft.spaciouslib.anvil.AnvilBuilder;
import org.anhcraft.spaciouslib.anvil.AnvilHandler;
import org.anhcraft.spaciouslib.anvil.AnvilSlot;
import org.anhcraft.spaciouslib.attribute.Attribute;
import org.anhcraft.spaciouslib.attribute.AttributeModifier;
import org.anhcraft.spaciouslib.bungee.BungeeManager;
import org.anhcraft.spaciouslib.bungee.BungeePlayerIPResponse;
import org.anhcraft.spaciouslib.command.CommandArgument;
import org.anhcraft.spaciouslib.command.CommandBuilder;
import org.anhcraft.spaciouslib.command.CommandRunnable;
import org.anhcraft.spaciouslib.command.SubCommandBuilder;
import org.anhcraft.spaciouslib.entity.EntityManager;
import org.anhcraft.spaciouslib.entity.PlayerPing;
import org.anhcraft.spaciouslib.events.ArmorEquipEvent;
import org.anhcraft.spaciouslib.events.BungeeForwardEvent;
import org.anhcraft.spaciouslib.events.NPCInteractEvent;
import org.anhcraft.spaciouslib.events.PacketHandleEvent;
import org.anhcraft.spaciouslib.hologram.Hologram;
import org.anhcraft.spaciouslib.hologram.HologramManager;
import org.anhcraft.spaciouslib.inventory.*;
import org.anhcraft.spaciouslib.io.DirectoryManager;
import org.anhcraft.spaciouslib.io.FileManager;
import org.anhcraft.spaciouslib.mojang.CachedSkin;
import org.anhcraft.spaciouslib.mojang.GameProfileManager;
import org.anhcraft.spaciouslib.mojang.SkinManager;
import org.anhcraft.spaciouslib.nbt.NBTManager;
import org.anhcraft.spaciouslib.npc.NPC;
import org.anhcraft.spaciouslib.npc.NPCManager;
import org.anhcraft.spaciouslib.npc.NPCWrapper;
import org.anhcraft.spaciouslib.placeholder.Placeholder;
import org.anhcraft.spaciouslib.placeholder.PlaceholderManager;
import org.anhcraft.spaciouslib.protocol.*;
import org.anhcraft.spaciouslib.socket.ServerSocketClientHandler;
import org.anhcraft.spaciouslib.socket.ServerSocketRequestHandler;
import org.anhcraft.spaciouslib.socket.SocketManager;
import org.anhcraft.spaciouslib.utils.CommonUtils;
import org.anhcraft.spaciouslib.utils.InventoryUtils;
import org.anhcraft.spaciouslib.utils.RandomUtils;
import org.anhcraft.spaciouslib.utils.TimeUnit;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class SpaciousLibTestSpigot extends JavaPlugin implements Listener {
    private static File f = new File("plugins/SpaciousLibTest/test.txt");
    private static NPCWrapper npc;
    private static Hologram hg;

    @Override
    public void onEnable() {
        try {
            SkinManager.get(UUID.fromString("2c8d5050-eae7-438d-88c4-c29fbcebede9"), (int) TimeUnit.WEEK.getSeconds());
        } catch(Exception e) {
            e.printStackTrace();
        }

        new DirectoryManager("plugins/SpaciousLibTest/").mkdirs();
        try {
            new FileManager(f).initFile(IOUtils.toByteArray(getClass().getResourceAsStream("/test.txt")));
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
                                        Particle.Type type = CommonUtils.getObject(strings[0].toUpperCase(), Particle.Type.values());
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
                                        Particle.Type type = CommonUtils.getObject(strings[0], Particle.Type.values());
                                        if(type == null) {
                                            commandSender.sendMessage("Invalid particle type!");
                                        } else {
                                            Location location = ((Player) commandSender).getLocation();
                                            for(int degree = 0; degree < 360; degree++) {
                                                double radians = Math.toRadians(degree);
                                                double x = Math.cos(radians) * 3;
                                                double z = Math.sin(radians) * 3;
                                                location.add(x, 0, z);
                                                Particle.create(type, location, CommonUtils.toIntegerNumber(strings[1])).sendWorld(((Player) commandSender).getWorld());
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

                    .addSubCommand(new SubCommandBuilder("skin", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder sCommand, SubCommandBuilder subCommand, CommandSender commandSender, String[] strings, String s) {
                            commandSender.sendMessage(sCommand.getCommandAsString(subCommand, true));
                        }
                    }).addArgument("uuid", new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            try {
                                CachedSkin cs = SkinManager.get(UUID.fromString(value));
                                if(sender instanceof Player) {
                                    Player player = (Player) sender;
                                    SkinManager.changeSkin(player, cs.getSkin());
                                }
                            } catch(Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, CommandArgument.Type.UUID, false))

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
                                Camera.create((Player) sender).sendPlayer((Player) sender);
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
                    .addSubCommand(new SubCommandBuilder("test", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            if(sender instanceof Player) {
                                Player p = (Player) sender;
                                p.getInventory().addItem(
                                        new BookManager("&aGuide", 1)
                                                .setAuthor("anhcraft")
                                                .setTitle("GUIDE")
                                                .setBookGeneration(BookManager.BookGeneration.ORIGINAL)
                                                .addPage(PlaceholderManager.replace(
                                                        "Name: {player_name}\n" +
                                                                "Level: {player_level}\n" +
                                                                "Exp: {player_exp}\n" +
                                                                "Ping: {player_ping}\n" +
                                                                "Health: {player_health}/{player_max_health}",
                                                        p))
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
                                                .addLore("This is the guide book of this server!")
                                                .addEnchant(Enchantment.DAMAGE_ALL, 1)
                                                .addFlag(ItemFlag.HIDE_ENCHANTS)
                                                .getItem());
                                p.updateInventory();

                                new EntityManager(p).setAttribute(
                                        new Attribute(Attribute.Type.GENERIC_ARMOR)
                                                .addModifier(new AttributeModifier("1", 3, AttributeModifier.Operation.ADD)));

                                new AnvilBuilder(p, new AnvilHandler() {
                                    @Override
                                    public void result(Player player, String input, ItemStack item, AnvilSlot slot) {
                                        p.sendMessage(input);
                                        p.closeInventory();
                                    }
                                }).setItem(AnvilSlot.INPUT_LEFT, new ItemManager("test", Material.DIAMOND, 1).getItem()).open();

                                ShapedRecipe r = new ShapedRecipe(new ItemManager("test111", Material.DIAMOND_SWORD, 1).setUnbreakable(true).addEnchant(Enchantment.DAMAGE_ALL, 5).addLore("tesssssss").addAttributeModifier(Attribute.Type.GENERIC_MAX_HEALTH, new AttributeModifier("1", 5, AttributeModifier.Operation.ADD), EquipSlot.MAINHAND).addAttributeModifier(Attribute.Type.GENERIC_ATTACK_SPEED, new AttributeModifier("2", 10, AttributeModifier.Operation.ADD), EquipSlot.MAINHAND).addAttributeModifier(Attribute.Type.GENERIC_ATTACK_DAMAGE, new AttributeModifier("3", 20, AttributeModifier.Operation.ADD), EquipSlot.MAINHAND).getItem());
                                r.shape("aaa", "aaa", "aaa");
                                r.setIngredient('a', Material.DIAMOND);
                                RecipeManager.register(r);

                                ActionBar.create("test").sendAll();
                                Animation.create(p, Animation.Type.SWING_MAIN_HAND).sendPlayer(p);
                                BlockBreakAnimation.create(0, p.getLocation().getBlock().getRelative(BlockFace.DOWN), 5).sendWorld(p.getWorld());
                                Particle.create(Particle.Type.BARRIER, p.getLocation(), 5).sendWorld(p.getWorld());
                                PlayerList.create("aaa", "bbb").sendAll();
                                Title.create("wwwwwwwww", Title.Type.TITLE).sendPlayer(p);
                                Camera.create(p).sendPlayer(p);
                            }
                        }
                    }))
                    .addSubCommand(new SubCommandBuilder("npc spawn", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            if(sender instanceof Player && npc == null) {
                                Player p = (Player) sender;
                                try {
                                    npc = NPCManager.register("test", new NPC(
                                            new GameProfileManager("test")
                                                    .setSkin(SkinManager.get(UUID.fromString("2c8d5050-eae7-438d-88c4-c29fbcebede9")).getSkin()).getGameProfile(),
                                            p.getLocation(),
                                            NPC.Addition.INTERACT_HANDLER,
                                            NPC.Addition.NEARBY_RENDER).setNearbyRadius(20));
                                } catch(Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }))
                    .addSubCommand(new SubCommandBuilder("npc tphere", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            if(sender instanceof Player && npc != null) {
                                Player p = (Player) sender;
                                npc.teleport(p.getLocation());
                            }
                        }
                    }))
                    .addSubCommand(new SubCommandBuilder("npc remove", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            if(sender instanceof Player && npc != null) {
                                NPCManager.unregister("test");
                                npc = null;
                            }
                        }
                    }))
                    .addSubCommand(new SubCommandBuilder("npc rename", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            sender.sendMessage(cmd.getCommandAsString(subcmd, true));
                        }
                    }).addArgument("name", new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            if(sender instanceof Player && npc != null) {
                                npc.setName(value);
                            }
                        }
                    }, CommandArgument.Type.CUSTOM, false))

                    .addSubCommand(new SubCommandBuilder("hg add", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            if(sender instanceof Player) {
                                Player player = (Player) sender;
                                hg = new Hologram("test", player.getLocation());
                                hg.addLine("test 1");
                                hg.addLine("test 2");
                                hg.addLine("test 3");
                                hg.addViewer(player);
                                HologramManager.register(hg);
                            }
                        }
                    }))

                    .addSubCommand(new SubCommandBuilder("hg remove", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            if(sender instanceof Player) {
                                HologramManager.unregister(hg);
                            }
                        }
                    }))

                    .addSubCommand(new SubCommandBuilder("json", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            if(sender instanceof Player) {
                                Player player = (Player) sender;
                                try {
                                    new FileManager(f).writeIfExist(NBTManager.fromEntity(player).toJSON().getBytes(StandardCharsets.UTF_8));
                                } catch(IOException e) {
                                    e.printStackTrace();
                                }
                            }
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
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player player : Bukkit.getServer().getOnlinePlayers()){
                    ActionBar.create(Integer.toString(PlayerPing.get(player))+" ms", 60, 80, 60).sendPlayer(player);
                }
            }
        }.runTaskTimerAsynchronously(this, 40, 40);
    }

    @EventHandler
    public void npc(NPCInteractEvent event){
        if(event.getNPC().equals(npc.getNPC())){
            InventoryManager inv = new InventoryManager(npc.getNPC().getGameProfile().getName(), 54);
            int i = 0;
            while(i < 54){
                inv.set(i, new ItemManager("", RandomUtils.pickRandom(InventoryUtils.getArmors()), 1).getItem(), new InteractItemRunnable() {
                    @Override
                    public void run(Player player, ItemStack item, ClickType action, int slot) {

                    }
                });
                i++;
            }
            inv.open(event.getPlayer());
        }
    }

    @EventHandler
    public void ev(PacketHandleEvent ev) {
        if(ev.getType().equals(PacketHandleEvent.Type.SERVER_BOUND)) {
            if(ev.getPacket().getClass().getSimpleName().equals("PacketPlayInChat")) {
                if(ev.getPacketValue("a").toString().equalsIgnoreCase("savemydata")) {
                    ev.setCancelled(true);
                    FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
                    NBTManager.fromEntity(ev.getPlayer()).toConfigurationSection(fc);
                    try {
                        fc.save(f);
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
                if(ev.getPacketValue("a").toString().equalsIgnoreCase("applymydata")) {
                    ev.setCancelled(true);
                    FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
                    NBTManager.fromConfigurationSection(fc).toEntity(ev.getPlayer());
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

    @EventHandler
    public void equip(ArmorEquipEvent event){
        if(!InventoryUtils.isNull(event.getNewArmor())){
            if(event.getNewArmor().getType().equals(Material.DIAMOND_HELMET)){
                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 100000, 1));
            }
        }
    }

    @Override
    public void onDisable() {
    }
}