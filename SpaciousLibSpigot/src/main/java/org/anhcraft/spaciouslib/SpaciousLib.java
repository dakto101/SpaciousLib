package org.anhcraft.spaciouslib;

import org.anhcraft.spaciouslib.annotations.AnnotationHandler;
import org.anhcraft.spaciouslib.bungee.BungeeAPI;
import org.anhcraft.spaciouslib.io.DirectoryManager;
import org.anhcraft.spaciouslib.io.FileManager;
import org.anhcraft.spaciouslib.listeners.*;
import org.anhcraft.spaciouslib.mojang.SkinAPI;
import org.anhcraft.spaciouslib.placeholder.PlaceholderAPI;
import org.anhcraft.spaciouslib.serialization.serializers.*;
import org.anhcraft.spaciouslib.tasks.ArmorEquipEventTask;
import org.anhcraft.spaciouslib.tasks.CachedSkinTask;
import org.anhcraft.spaciouslib.utils.*;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;

public final class SpaciousLib extends JavaPlugin {
    public final static String SL_CHANNEL = "spaciouslib:plugin";
    public final static File ROOT_FOLDER = new File("plugins/SpaciousLib/");
    public final static File SKINS_FOLDER = new File(ROOT_FOLDER, "skins/");
    public final static File CONFIG_FILE = new File(ROOT_FOLDER, "config.yml");
    public final static File CONFIG_FILE_OLD = new File(ROOT_FOLDER, "config.old.yml");
    public static SpaciousLib instance;
    public static FileConfiguration config;
    public static Chat chat;

    @Override
    public void onEnable(){
        try{
            Class.forName("org.spigotmc.SpigotConfig");
        } catch(ClassNotFoundException e) {
            getLogger().info("SpaciousLib only works with Spigot-based servers (Spigot, PaperSpigot, etc)");
            Bukkit.getServer().getPluginManager().disablePlugin(this);
        }

        instance = this;

        getLogger().info("Loading configuration...");
        try {
            new DirectoryManager(ROOT_FOLDER).mkdirs();
            new DirectoryManager(SKINS_FOLDER).mkdirs();
            new FileManager(CONFIG_FILE).initFile(IOUtils.toByteArray(getClass().getResourceAsStream("/config.yml")));
        } catch(IOException e) {
            e.printStackTrace();
        }

        config = YamlConfiguration.loadConfiguration(CONFIG_FILE);
        chat = new Chat("&f[&bSpaciousLib&f] ");
        if(!config.isSet("config_version") || config.getInt("config_version") < 3){
            try {
                chat.sendSender("&cAttempting to upgrade the old configuration...");
                chat.sendSender("&cCreating a backup for the old configuration....");
                new FileManager(CONFIG_FILE_OLD).delete().initFile(new FileManager(CONFIG_FILE).read());
                new FileManager(CONFIG_FILE).delete();
                chat.sendSender("&cDeleted the old config.yml file!");
                chat.sendSender("&cCreating the new config.yml file...");
                new FileManager(CONFIG_FILE).initFile(IOUtils.toByteArray(getClass().getResourceAsStream("/config.yml")));
                config = YamlConfiguration.loadConfiguration(CONFIG_FILE);
                chat.sendSender("&cReloaded the configuration! Enjoy <3");
            } catch(IOException e) {
                chat.sendSender("&cGot errors while trying to upgrade the configuration!");
                e.printStackTrace();
            }
        }

        ////////////////////////////////////////////////////////////////////////////////////////////

        chat.sendSender("&eInitializing library...");
        new ItemStackSerializer((byte) 30);
        new ItemMetaSerializer((byte) 31);
        new LocationSerializer((byte) 32);
        new VectorSerializer((byte) 33);
        new NBTCompoundSerializer((byte) 34);

        new PlaceholderAPI();
        new SkinAPI();
        new BungeeAPI();
        for(String proxy : config.getStringList("proxies")){
            String[] x = proxy.split(":");
            ProxyUtils.put(x[0], Integer.parseInt(x[1]));
        }
        AnnotationHandler.register(NPCInteractEventListener.class, null);
        AnnotationHandler.register(PlayerListener.class, null);

        chat.sendSender("&eLoading NMS/CB classes "+ GameVersion.getVersion().toString() +"...");
        try {
            Class.forName("org.anhcraft.spaciouslib.utils.ClassFinder$NMS");
            Class.forName("org.anhcraft.spaciouslib.utils.ClassFinder$CB");
        } catch(ClassNotFoundException e) {
            chat.sendSender("&cGot errors while trying to load NMS/CB classes!");
            e.printStackTrace();
        }

        chat.sendSender("&eStarting tasks...");
        //if(config.getBoolean("stats", true)){}
        if(config.getBoolean("check_update", true)){
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        if(UpdateChecker.predictLatest(getDescription().getVersion(), UpdateChecker.viaSpiget("39007"))){
                            chat.sendSender("&a[Updater] This version is latest!");
                        } else {
                            chat.sendSender("&c[Updater] Outdated version! Please update in order to receive bug fixes and much more.");
                        }
                    } catch(IOException e) {
                        e.printStackTrace();
                        chat.sendSender("&c[Updater] Failed to check update.");
                    }
                }
            }.runTaskAsynchronously(this);
        }

        // uses synchronous task because this task will call the ArmorEquipEvent event
        new ArmorEquipEventTask().runTaskTimer(this, 0, 20);
        new CachedSkinTask().runTaskTimerAsynchronously(this, 0, 1200);

        chat.sendSender("&eRegistering listeners...");
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new ServerListener(), this);
        if(config.getBoolean("packet_handler", true)) {
            getServer().getPluginManager().registerEvents(new PacketListener(), this);
        }

        chat.sendSender("&eRegistering messaging channel...");
        getServer().getMessenger().registerOutgoingPluginChannel(this, SL_CHANNEL);
        getServer().getMessenger().registerIncomingPluginChannel(this, SL_CHANNEL, new BungeeListener());

        if(Bukkit.getServer().getPluginManager().isPluginEnabled("Vault")){
            VaultUtils.init();
            if(VaultUtils.isInitialized()) {
                chat.sendSender("&aHooked to Vault!");
            }
        }
        if(Bukkit.getServer().getPluginManager().isPluginEnabled("PlayerPoints")){
            PlayerPointsUtils.init();
            if(PlayerPointsUtils.isInitialized()) {
                chat.sendSender("&aHooked to PlayerPoints!");
            }
        }

        if(config.getBoolean("dev_mode", false)){
            chat.sendSender("&aSwitched to the development mode!");
            getServer().getPluginManager().registerEvents(new SLDev(), this);
            getServer().getPluginManager().registerEvents(new SLDev2(), this);
        }
    }

    @Override
    public void onDisable() {
        AnnotationHandler.unregister(NPCInteractEventListener.class, null);
        AnnotationHandler.unregister(PlayerListener.class, null);
        for(Player player : getServer().getOnlinePlayers()){
            PacketListener.remove(player);
        }
    }
}