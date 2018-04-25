package org.anhcraft.spaciouslibtest;

import org.anhcraft.spaciouslib.SpaciousLib;
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
import org.anhcraft.spaciouslib.entity.Hologram;
import org.anhcraft.spaciouslib.entity.NPC;
import org.anhcraft.spaciouslib.entity.PlayerManager;
import org.anhcraft.spaciouslib.entity.bossbar.BossBar;
import org.anhcraft.spaciouslib.events.*;
import org.anhcraft.spaciouslib.inventory.BookManager;
import org.anhcraft.spaciouslib.inventory.EquipSlot;
import org.anhcraft.spaciouslib.inventory.ItemManager;
import org.anhcraft.spaciouslib.inventory.RecipeManager;
import org.anhcraft.spaciouslib.io.FileManager;
import org.anhcraft.spaciouslib.mojang.GameProfileManager;
import org.anhcraft.spaciouslib.mojang.MojangAPI;
import org.anhcraft.spaciouslib.mojang.SkinAPI;
import org.anhcraft.spaciouslib.placeholder.FixedPlaceholder;
import org.anhcraft.spaciouslib.placeholder.PlaceholderAPI;
import org.anhcraft.spaciouslib.socket.ClientSocketHandler;
import org.anhcraft.spaciouslib.socket.ClientSocketManager;
import org.anhcraft.spaciouslib.utils.InventoryUtils;
import org.anhcraft.spaciouslib.utils.RandomUtils;
import org.anhcraft.spaciouslib.utils.TimedSet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class SpaciousLibTest extends JavaPlugin implements Listener {
    private static final File DB_FILE = new File("test.db");
    private static ClientSocketManager client;

    @Override
    public void onEnable() {
        if(!SpaciousLib.config.getBoolean("dev_mode")){
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED+"SpaciousLibTest only works in the dev mode.");
            try {
                Thread.sleep(1500);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            Bukkit.getServer().getPluginManager().disablePlugin(this);
            return;
        }

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

            // initializes the client socket
            client = new ClientSocketManager("localhost", 25568, new ClientSocketHandler() {
                @Override
                public void response(ClientSocketManager manager, byte[] data){
                    // prints the sent message
                    System.out.println("Server >> " + new String(data));
                }
            });
            // sends messages to the socket server
            client.send("Hi server!");

            //======================================================================================

            // the root command
            new CommandBuilder("slt", new CommandRunnable() {
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

            .addSubCommand(new SubCommandBuilder("hologram", null, new CommandRunnable() {
                @Override
                public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                    if(sender instanceof Player) {
                        Player player = (Player) sender;
                        new Hologram(((Player) sender).getLocation()).addViewer(player.getUniqueId()).addLine("test 1").addLine("test 2").addLine("test 3").spawn();
                    }
                }
            }))

            .addSubCommand(new SubCommandBuilder("recipe", null, new CommandRunnable() {
                @Override
                public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                    ShapedRecipe recipe = new ShapedRecipe(
                            new ItemManager("Emerald sword", Material.DIAMOND_SWORD, 1)
                                    .addEnchant(Enchantment.DAMAGE_ALL, 3)
                                    .addAttributeModifier(Attribute.Type.GENERIC_MAX_HEALTH,
                                            new AttributeModifier("attr", 5,
                                                    AttributeModifier.Operation.ADD), EquipSlot.MAINHAND)
                                    .getItem());
                    recipe.shape("b", "b", "a");
                    recipe.setIngredient('a', Material.STICK);
                    recipe.setIngredient('b', Material.EMERALD);
                    new RecipeManager(recipe).register();
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
                            new PlayerManager((Player) sender).changeSkin(
                                    SkinAPI.getSkin(MojangAPI.getUUID(value).getB()).getSkin());
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, CommandArgument.Type.CUSTOM, false))

            .addSubCommand(new SubCommandBuilder("placeholder", null, new CommandRunnable() {
                @Override
                public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                    if(sender instanceof Player) {
                        PlaceholderAPI.register(new FixedPlaceholder() {
                            @Override
                            public String getPlaceholder() {
                                return "{server_name}";
                            }

                            @Override
                            public String getValue(Player player) {
                                return Bukkit.getServer().getServerName();
                            }
                        });
                        sender.sendMessage(PlaceholderAPI.replace("{server_name}", (Player) sender));
                    }
                }
            }))

            .addSubCommand(new SubCommandBuilder("npc", null, new CommandRunnable() {
                @Override
                public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                    if(sender instanceof Player) {
                        try {
                            Player player = (Player) sender;
                            new NPC(new GameProfileManager("test")
                                            .setSkin(SkinAPI.getSkin(
                                                    MojangAPI.getUUID("anhcraft").getB()).getSkin())
                                            .getGameProfile(),
                                    ((Player) sender).getLocation()).addViewer(player.getUniqueId()).spawn();
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }))

            .addSubCommand(new SubCommandBuilder("bossbar", null, new CommandRunnable() {
                @Override
                public void run(CommandBuilder cmd, SubCommandBuilder subcmd, CommandSender sender, String[] args, String value) {
                    if(sender instanceof Player) {
                        Player player = (Player) sender;
                        new BossBar("test", BossBar.Color.GREEN, BossBar.Style.NOTCHED_6, 1, BossBar.Flag.CREATE_FOG, BossBar.Flag.DARKEN_SKY).addViewer(player.getUniqueId());
                    }
                }
            }))

            // build the command executor
            .buildExecutor(this);
        } catch(Exception e) {
            e.printStackTrace();
        }

        getServer().getPluginManager().registerEvents(this, this);
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

    @EventHandler
    public void packet(PacketHandleEvent event){
        if(event.getPacket().getClass().getSimpleName().equals("PacketPlayInChat")){
            if(event.getPacketValue("a").toString().equalsIgnoreCase("test")){
                event.setCancelled(true);
            }
        }
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

    @Override
    public void onDisable() {
        try {
            client.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}