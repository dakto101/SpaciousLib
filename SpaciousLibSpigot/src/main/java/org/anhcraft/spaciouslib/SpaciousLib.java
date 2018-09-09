package org.anhcraft.spaciouslib;

import org.anhcraft.spaciouslib.annotations.AnnotationHandler;
import org.anhcraft.spaciouslib.bungee.BungeeAPI;
import org.anhcraft.spaciouslib.io.DirectoryManager;
import org.anhcraft.spaciouslib.io.FileManager;
import org.anhcraft.spaciouslib.listeners.*;
import org.anhcraft.spaciouslib.mojang.SkinAPI;
import org.anhcraft.spaciouslib.placeholder.PlaceholderAPI;
import org.anhcraft.spaciouslib.tasks.ArmorEquipEventTask;
import org.anhcraft.spaciouslib.tasks.CachedSkinTask;
import org.anhcraft.spaciouslib.utils.Chat;
import org.anhcraft.spaciouslib.utils.ProxyUtils;
import org.anhcraft.spaciouslib.utils.VaultUtils;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class SpaciousLib extends JavaPlugin {
    public final static String CHANNEL = "spaciouslib:plugin";
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
            getLogger().info("SpaciousLib only works in Spigot-based servers (Spigot, PaperSpigot, etc)");
            Bukkit.getServer().getPluginManager().disablePlugin(this);
        }

        instance = this;

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

        chat.sendSender("&eInitializing the APIs...");
        new PlaceholderAPI();
        new SkinAPI();
        new BungeeAPI();
        for(String proxy : config.getStringList("proxies")){
            String[] x = proxy.split(":");
            ProxyUtils.put(x[0], Integer.parseInt(x[1]));
        }

        chat.sendSender("&eStarting the tasks...");
        if(config.getBoolean("stats", true)){
            new Updater1520156620("1520156620", this);
        }

        // uses synchronous task because this task will call the ArmorEquipEvent event
        new ArmorEquipEventTask().runTaskTimer(this, 0, 20);
        if(getConfig().getBoolean("auto_renew_skin", false)) {
            new CachedSkinTask().runTaskTimerAsynchronously(this, 0, 1200);
        }

        chat.sendSender("&eRegistering the listeners...");
        getServer().getPluginManager().registerEvents(new PlayerJumpEventListener(), this);
        getServer().getPluginManager().registerEvents(new ClickableItemListener(), this);
        getServer().getPluginManager().registerEvents(new BowArrowHitEventListener(), this);
        getServer().getPluginManager().registerEvents(new PlaceholderListener(), this);
        getServer().getPluginManager().registerEvents(new AnvilListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerCleanerListener(), this);
        getServer().getPluginManager().registerEvents(new ServerListener(), this);
        if(config.getBoolean("packet_handler", true)) {
            getServer().getPluginManager().registerEvents(new PacketListener(), this);
        }
        getServer().getMessenger().registerOutgoingPluginChannel(this, CHANNEL);
        getServer().getMessenger().registerIncomingPluginChannel(this, CHANNEL, new BungeeListener());

        AnnotationHandler.register(NPCInteractEventListener.class, null);
        AnnotationHandler.register(AnvilListener.class, null);
        if(Bukkit.getServer().getPluginManager().isPluginEnabled("Vault")){
            VaultUtils.init();
            if(VaultUtils.isInitialized()) {
                chat.sendSender("&aHooked to Vault!");
            }
        }

        if(config.getBoolean("dev_mode", false)){
            chat.sendSender("&aSwitched to the development mode!");
            getServer().getPluginManager().registerEvents(new SLDev(), this);
        }
    }

    @Override
    public void onDisable() {
        AnnotationHandler.unregister(NPCInteractEventListener.class, null);
        AnnotationHandler.unregister(AnvilListener.class, null);
        for(Player player : getServer().getOnlinePlayers()){
            PacketListener.remove(player);
        }
    }
}