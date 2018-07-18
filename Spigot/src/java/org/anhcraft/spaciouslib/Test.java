package org.anhcraft.spaciouslib;

import org.anhcraft.spaciouslib.anvil.Anvil;
import org.anhcraft.spaciouslib.attribute.Attribute;
import org.anhcraft.spaciouslib.attribute.AttributeModifier;
import org.anhcraft.spaciouslib.bungee.BungeeAPI;
import org.anhcraft.spaciouslib.bungee.BungeePlayerListResponse;
import org.anhcraft.spaciouslib.bungee.BungeeServerListResponse;
import org.anhcraft.spaciouslib.command.CommandArgument;
import org.anhcraft.spaciouslib.command.CommandBuilder;
import org.anhcraft.spaciouslib.command.CommandRunnable;
import org.anhcraft.spaciouslib.command.SubCommandBuilder;
import org.anhcraft.spaciouslib.database.SQLiteDatabase;
import org.anhcraft.spaciouslib.effects.*;
import org.anhcraft.spaciouslib.entity.ArmorStand;
import org.anhcraft.spaciouslib.entity.Hologram;
import org.anhcraft.spaciouslib.entity.NPC;
import org.anhcraft.spaciouslib.entity.PlayerManager;
import org.anhcraft.spaciouslib.entity.bossbar.BossBar;
import org.anhcraft.spaciouslib.events.*;
import org.anhcraft.spaciouslib.inventory.*;
import org.anhcraft.spaciouslib.io.FileManager;
import org.anhcraft.spaciouslib.mojang.GameProfileManager;
import org.anhcraft.spaciouslib.mojang.MojangAPI;
import org.anhcraft.spaciouslib.mojang.SkinAPI;
import org.anhcraft.spaciouslib.placeholder.PlaceholderAPI;
import org.anhcraft.spaciouslib.protocol.*;
import org.anhcraft.spaciouslib.scheduler.TimerTask;
import org.anhcraft.spaciouslib.socket.ClientSocketHandler;
import org.anhcraft.spaciouslib.socket.ClientSocketManager;
import org.anhcraft.spaciouslib.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.spigotmc.SpigotConfig;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Test implements Listener {
    private static final File DB_FILE = new File("test.db");
    private static ClientSocketManager client;
    private static double[] effectRotateAngle = new double[3];
    private static ShapedRecipe recipe = new ShapedRecipe(
            new ItemManager("Emerald sword", Material.DIAMOND_SWORD, 1)
                    .addEnchant(Enchantment.DAMAGE_ALL, 3)
                    .addAttributeModifier(Attribute.Type.GENERIC_MAX_HEALTH,
                            new AttributeModifier("attr", 5,
                                    AttributeModifier.Operation.ADD), EquipSlot.MAINHAND)
                    .getItem()).shape("b", "b", "a").setIngredient('a', Material.STICK)
            .setIngredient('b', Material.EMERALD);
    private static Hologram hologram;
    private static BossBar bossbar;
    private static ArmorStand armorstand;
    private static NPC npc;

    public Test(){

        try {
            // creates the database file
            new FileManager(DB_FILE).create();

            SQLiteDatabase database = new SQLiteDatabase();
            // connects to the database
            database.connect(DB_FILE);
            // creates a table
            database.update("CREATE TABLE IF NOT EXISTS `spaciouslib` (" +
                    " `key` varchar(255)," +
                    " `value` int" +
                    ")");
            // disconnects from the connection
            database.disconnect();

            //======================================================================================

            // the root command
            new CommandBuilder("sls", new CommandRunnable() {
                @Override
                public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                    for(SubCommandBuilder s : cmd.getSubCommands()){
                        sender.sendMessage(cmd.getCommandAsString(s, true));
                    }
                }
            })

                    // a sub command
                    .addSubCommand(new SubCommandBuilder("anvil", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            if(sender instanceof Player){
                                new Anvil((Player) sender, new Anvil.Handler() {
                                    @Override
                                    public void result(Player player, String input, ItemStack item, Anvil.Slot slot) {
                                        player.sendMessage("You've typed " + input);
                                    }
                                }).setItem(Anvil.Slot.INPUT_LEFT, new ItemStack(Material.DIAMOND, 1)).open();
                            }
                        }
                    }))

                    .addSubCommand(new SubCommandBuilder("inv", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            if(sender instanceof Player){
                                new InventoryManager("Test", 9).fill(new ItemStack(Material.APPLE, 1), new ClickableItemHandler() {
                                    @Override
                                    public void run(Player player, ItemStack item, ClickType click, int slot, InventoryAction action, Inventory inventory) {

                                    }
                                }).set(CenterPosition.CENTER_2_A, new ItemStack(Material.DIAMOND, 1), new ClickableItemHandler() {
                                    @Override
                                    public void run(Player player, ItemStack item, ClickType click, int slot, InventoryAction action, Inventory inventory) {

                                    }
                                }).set(CenterPosition.CENTER_2_B, new ItemStack(Material.EMERALD, 1), new ClickableItemHandler() {
                                    @Override
                                    public void run(Player player, ItemStack item, ClickType click, int slot, InventoryAction action, Inventory inventory) {

                                    }
                                }).open((Player) sender);
                            }
                        }
                    }))

                    .addSubCommand(new SubCommandBuilder("armorstand create", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            if(sender instanceof Player) {
                                Player player = (Player) sender;
                                armorstand = new ArmorStand(player.getLocation()).setArms(true)
                                        .setGravity(true).setCustomName("aaa").setMarker(true).setVisible(true).setHelmet(new ItemManager("&aaaa", Material.BEACON, 1).setUnbreakable(true).getItem());
                            }
                        }
                    }))

                    .addSubCommand(new SubCommandBuilder("armorstand build", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            armorstand.buildPackets();
                        }
                    }))

                    .addSubCommand(new SubCommandBuilder("armorstand addviewer", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                        }
                    }).addArgument("player", new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            armorstand.addViewer(Bukkit.getServer().getPlayer(value).getUniqueId());
                        }
                    }, CommandArgument.Type.ONLINE_PLAYER, false))

                    .addSubCommand(new SubCommandBuilder("tps", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            sender.sendMessage(Double.toString(ServerUtils.getTPS()));
                        }
                    }))

                    .addSubCommand(new SubCommandBuilder("animation", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            if(sender instanceof Player) {
                                Player player = (Player) sender;
                                Animation.create(player, Animation.Type.TAKE_DAMAGE).sendAll();
                            }
                        }
                    }))

                    .addSubCommand(new SubCommandBuilder("blockbreakanimation", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            if(sender instanceof Player) {
                                Player player = (Player) sender;
                                for(Location loc : LocationUtils.getNearbyLocations(player.getLocation(), 3, 3, 3)){
                                    BlockBreakAnimation.create(RandomUtils.randomInt(0, 10000), loc, RandomUtils.randomInt(0, 9)).sendAll();
                                }
                            }
                        }
                    }))

                    .addSubCommand(new SubCommandBuilder("camera", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            if(sender instanceof Player) {
                                Player player = (Player) sender;
                                Cow cow = player.getWorld().spawn(player.getLocation(), Cow.class);
                                Camera.create(cow).sendPlayer(player);
                            }
                        }
                    }))

                    .addSubCommand(new SubCommandBuilder("playerlist create", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            PlayerList.create("aaa","bbb").sendAll();
                        }
                    }))

                    .addSubCommand(new SubCommandBuilder("playerlist remove", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            PlayerList.remove().sendAll();
                        }
                    }))

                    .addSubCommand(new SubCommandBuilder("bungee", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            // gets list of servers
                            BungeeAPI.getServerList(new BungeeServerListResponse() {
                                @Override
                                public void result(List<String> servers) {
                                    if(0 < servers.size()){
                                        // selects a random server and gets list of its players
                                        BungeeAPI.getPlayerList(RandomUtils.pickRandom(servers), new BungeePlayerListResponse() {
                                            @Override
                                            public void result(String server, List<String> players) {
                                                for(String player : players){
                                                    // sends the message for each player
                                                    BungeeAPI.sendMessage(player, "Hi " + player + "!");
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }))

                    .addSubCommand(new SubCommandBuilder("title", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                        }
                    }).addArgument("text", new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            Title.create(String.join(" ", args), Title.Type.TITLE).sendAll();
                            Title.create(String.join(" ", args), Title.Type.SUBTITLE).sendAll();
                        }
                    }, CommandArgument.Type.CUSTOM, false))

                    .addSubCommand(new SubCommandBuilder("actionbar", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                        }
                    }).addArgument("text", new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            ActionBar.create(String.join(" ", args)).sendAll();
                        }
                    }, CommandArgument.Type.CUSTOM, false))

                    .addSubCommand(new SubCommandBuilder("uuid", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            try {
                                // gets the UUID from the Minecraft account "md_5"
                                String uuid = MojangAPI.getUUID("md_5").getB().toString();
                                sender.sendMessage(uuid);
                            } catch(Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }))

                    .addSubCommand(new SubCommandBuilder("hologram create", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            if(sender instanceof Player) {
                                Player player = (Player) sender;
                                hologram = new Hologram(player.getLocation());
                            }
                        }
                    }))

                    .addSubCommand(new SubCommandBuilder("hologram addline", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                        }
                    }).addArgument("text", new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            hologram.addLine(String.join(" ", args));
                        }
                    }, CommandArgument.Type.CUSTOM, false))

                    .addSubCommand(new SubCommandBuilder("hologram build", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            hologram.buildPackets();
                        }
                    }))

                    .addSubCommand(new SubCommandBuilder("hologram addviewer", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                        }
                    }).addArgument("player", new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            hologram.addViewer(Bukkit.getServer().getPlayer(value).getUniqueId());
                        }
                    }, CommandArgument.Type.ONLINE_PLAYER, false))

                    .addSubCommand(new SubCommandBuilder("hologram removeviewer", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                        }
                    }).addArgument("player", new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            hologram.removeViewer(Bukkit.getServer().getPlayer(value).getUniqueId());
                        }
                    }, CommandArgument.Type.ONLINE_PLAYER, false))

                    .addSubCommand(new SubCommandBuilder("recipe register", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            new RecipeManager(recipe).register();
                        }
                    }))

                    .addSubCommand(new SubCommandBuilder("recipe unregister", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            new RecipeManager(recipe).unregister();
                        }
                    }))

                    .addSubCommand(new SubCommandBuilder("socket", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            sender.sendMessage(cmd.getCommandAsString(subcmd, true));
                        }
                    }).addArgument("message", new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            try {
                                client.send(value);
                            } catch(IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }, CommandArgument.Type.CUSTOM, false))

                    .addSubCommand(new SubCommandBuilder("skin", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            sender.sendMessage(cmd.getCommandAsString(subcmd, true));
                        }
                    }).addArgument("player", new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            if(sender instanceof Player) {
                                try {
                                    if(SpigotConfig.bungee) {
                                        BungeeAPI.requestChangeSkin(sender.getName(),
                                                SkinAPI.getSkin(MojangAPI.getUUID(value).getB()).getSkin());
                                    } else {
                                        new PlayerManager((Player) sender).changeSkin(
                                                SkinAPI.getSkin(MojangAPI.getUUID(value).getB()).getSkin());
                                    }
                                } catch(Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, CommandArgument.Type.CUSTOM, false))

                    .addSubCommand(new SubCommandBuilder("placeholder", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                        }
                    }).addArgument("text", new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String s) {
                            if(sender instanceof Player) {
                                sender.sendMessage(PlaceholderAPI.replace(s, (Player) sender));
                            }
                        }
                    }, CommandArgument.Type.CUSTOM, false))

                    .addSubCommand(new SubCommandBuilder("npc create", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            if(sender instanceof Player) {
                                try {
                                    Player player = (Player) sender;
                                    npc = new NPC(new GameProfileManager("test").getGameProfile(),
                                            ((Player) sender).getLocation());
                                } catch(Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }))

                    .addSubCommand(new SubCommandBuilder("npc build", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            if(sender instanceof Player) {
                                try {
                                    npc.buildPackets();
                                } catch(Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }))

                    .addSubCommand(new SubCommandBuilder("npc addviewer", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                        }
                    }).addArgument("player", new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            npc.addViewer(Bukkit.getServer().getPlayer(value).getUniqueId());
                        }
                    }, CommandArgument.Type.ONLINE_PLAYER, false))

                    .addSubCommand(new SubCommandBuilder("npc removeviewer", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                        }
                    }).addArgument("player", new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            npc.removeViewer(Bukkit.getServer().getPlayer(value).getUniqueId());
                        }
                    }, CommandArgument.Type.ONLINE_PLAYER, false))

                    .addSubCommand(new SubCommandBuilder("bossbar create", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            if(sender instanceof Player) {
                                bossbar = new BossBar("test", BossBar.Color.GREEN, BossBar.Style.NOTCHED_6, 1, BossBar.Flag.CREATE_FOG, BossBar.Flag.DARKEN_SKY);
                            }
                        }
                    }))

                    .addSubCommand(new SubCommandBuilder("bossbar addviewer", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                        }
                    }).addArgument("player", new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            bossbar.addViewer(Bukkit.getServer().getPlayer(value).getUniqueId());
                        }
                    }, CommandArgument.Type.ONLINE_PLAYER, false))

                    .addSubCommand(new SubCommandBuilder("bossbar removeviewer", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                        }
                    }).addArgument("player", new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            bossbar.removeViewer(Bukkit.getServer().getPlayer(value).getUniqueId());
                        }
                    }, CommandArgument.Type.ONLINE_PLAYER, false))

                    .addSubCommand(new SubCommandBuilder("effect circle", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            if(sender instanceof Player) {
                                Player player = (Player) sender;
                                CircleEffect effect = new CircleEffect(player.getLocation());
                                effect.setRadius(5);
                                effect.addNearbyViewers(10);
                                new TimerTask(() -> {
                                    effect.setParticleColor(new Color(
                                            ThreadLocalRandom.current().nextInt(0, 256),
                                            ThreadLocalRandom.current().nextInt(0, 256),
                                            ThreadLocalRandom.current().nextInt(0, 256)
                                    ));
                                    effect.setAngleX(effectRotateAngle[0]);
                                    effect.setAngleY(effectRotateAngle[0]);
                                    effect.setAngleZ(effectRotateAngle[0]);
                                    effect.spawn();
                                    effectRotateAngle[0]++;
                                }, 0, 0.05, 60).run();
                            }
                        }
                    }))

                    .addSubCommand(new SubCommandBuilder("effect image", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            if(sender instanceof Player) {
                                Player player = (Player) sender;
                                ImageEffect effect = new ImageEffect(player.getLocation(), getClass().getResourceAsStream("/test.png"));
                                effect.setAngleX(90);
                                effect.addNearbyViewers(10);
                                effect.setImageSize(0.5);
                                effect.setParticleAmount(effect.getParticleAmount() * 20);
                                new TimerTask(() -> effect.spawn(), 0, 1, 120).run();
                            }
                        }
                    }))

                    .addSubCommand(new SubCommandBuilder("effect cuboid", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            if(sender instanceof Player) {
                                Player player = (Player) sender;
                                CuboidEffect effect = new CuboidEffect(player.getLocation(),
                                        10, 10, 10, true);
                                effect.addViewer(player.getUniqueId());
                                effect.setParticleType(Particle.Type.FLAME);
                                effect.setParticleAmount(effect.getParticleAmount() * 2);
                                new TimerTask(() -> {
                                    effect.setAngleX(effectRotateAngle[1]);
                                    effect.setAngleY(effectRotateAngle[1]);
                                    effect.setAngleZ(effectRotateAngle[1]);
                                    effect.spawn();
                                    effectRotateAngle[1]++;
                                }, 0, 0.05, 60).run();
                            }
                        }
                    }))

                    .addSubCommand(new SubCommandBuilder("effect vortex", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            if(sender instanceof Player) {
                                Player player = (Player) sender;
                                VortexEffect effect = new VortexEffect(player.getLocation());
                                effect.addViewer(player.getUniqueId());
                                effect.setParticleType(Particle.Type.FLAME);
                                effect.setParticleAmount(800);
                                effect.setVortexLineAmount(8);
                                effect.setVortexLineLength(2);
                                new TimerTask(() -> {
                                    effect.setAngleX(effectRotateAngle[2]);
                                    effect.setAngleY(effectRotateAngle[2]);
                                    effect.setAngleZ(effectRotateAngle[2]);
                                    effect.spawn();
                                    effectRotateAngle[2]++;
                                }, 0, 0.05, 60).run();
                            }
                        }
                    }))

                    .addSubCommand(new SubCommandBuilder("effect cone", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            if(sender instanceof Player) {
                                Player player = (Player) sender;
                                ConeEffect effect = new ConeEffect(player.getLocation());
                                effect.addViewer(player.getUniqueId());
                                effect.setParticleType(Particle.Type.FLAME);
                                effect.setParticleAmount(800);
                                effect.spawn();
                            }
                        }
                    }))

                    .addSubCommand(new SubCommandBuilder("effect line", null, new CommandRunnable() {
                        @Override
                        public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                            if(sender instanceof Player) {
                                Player player = (Player) sender;
                                LineEffect effect = new LineEffect(player.getLocation(), 10);
                                effect.addViewer(player.getUniqueId());
                                effect.setParticleType(Particle.Type.TOTEM);
                                effect.setParticleAmount(100);
                                effect.spawn();
                            }
                        }
                    }))

                    // build the command executor
                    .buildExecutor(SpaciousLib.instance);
        } catch(Exception e) {
            e.printStackTrace();
        }

        try{
            // initializes the client socket
            client = new ClientSocketManager("localhost", 25568, new ClientSocketHandler() {
                @Override
                public void response(ClientSocketManager manager, byte[] data){
                    // prints the sent message
                    System.out.println("Server >> " + new String(data));
                }
            });
        } catch(Exception ignored) {}
    }


    @EventHandler
    public void reload(ServerReloadEvent event){
        Bukkit.getServer().broadcastMessage(ChatColor.YELLOW+"Server is reloading...");
    }

    @EventHandler
    public void stop(ServerStopEvent event){
        Bukkit.getServer().broadcastMessage(ChatColor.RED+"Server is stopping...");
    }

    @EventHandler
    public void equip(ArmorEquipEvent event){
        if(!InventoryUtils.isNull(event.getNewArmor())){
            if(event.getNewArmor().getType().equals(Material.DIAMOND_HELMET)) {
                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 9999999, 1));
            }
            return;
        }
        if(!InventoryUtils.isNull(event.getOldArmor())) {
            if(event.getOldArmor().getType().equals(Material.DIAMOND_HELMET)) {
                event.getPlayer().removePotionEffect(PotionEffectType.LEVITATION);
            }
        }
    }

    @EventHandler
    public void bow(BowArrowHitEvent event){
        event.getShooter().teleport(event.getArrow().getLocation());
    }

    @EventHandler
    public void npc(NPCInteractEvent event){
        event.getPlayer().getInventory().addItem(
                new BookManager("&aBook", 1)
                        .addPage("Page 1")
                        .addPage("Page 2")
                        .addPage("Page 3")
                        .addPage("Page 4")
                        .addPage("Page 5")
                        .setAuthor("anhcraft")
                        .setBookGeneration(BookManager.BookGeneration.COPY_ORIGINAL)
                        .getItem());
        event.getPlayer().updateInventory();
    }

    public static TimedSet<UUID> jumpers = new TimedSet<>();

    @EventHandler
    public void jump(PlayerJumpEvent event){
        if(event.isOnSpot()){
            if(jumpers.contains(event.getPlayer().getUniqueId())){
                event.getPlayer().setVelocity(event.getPlayer().getVelocity().setY(2));
            }
            jumpers.add(event.getPlayer().getUniqueId(), 1);
        }
    }
}
