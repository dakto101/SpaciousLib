package org.anhcraft.spaciouslib;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.anhcraft.spaciouslib.io.DirectoryManager;
import org.anhcraft.spaciouslib.io.FileManager;
import org.anhcraft.spaciouslib.listeners.PlaceholderListener;
import org.anhcraft.spaciouslib.listeners.PlayerCleanerListener;
import org.anhcraft.spaciouslib.listeners.SpigotListener;
import org.anhcraft.spaciouslib.mojang.SkinAPI;
import org.anhcraft.spaciouslib.placeholder.PlaceholderAPI;
import org.anhcraft.spaciouslib.tasks.CachedSkinTask;
import org.anhcraft.spaciouslib.utils.Chat;
import org.anhcraft.spaciouslib.utils.IOUtils;
import org.anhcraft.spaciouslib.utils.ProxyUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public final class SpaciousLib extends Plugin {
    public final static String CHANNEL = "spaciouslib:plugin";
    public final static File ROOT_FOLDER = new File("plugins/SpaciousLib/");
    public final static File SKINS_FOLDER = new File(ROOT_FOLDER, "skins/");
    public final static File CONFIG_FILE = new File(ROOT_FOLDER, "config.yml");
    public final static File CONFIG_FILE_OLD = new File(ROOT_FOLDER, "config.old.yml");
    public static SpaciousLib instance;
    public static Configuration config;
    public static Chat chat;

    @Override
    public void onEnable() {
        instance = this;

        try {
            new DirectoryManager(ROOT_FOLDER).mkdirs();
            new DirectoryManager(SKINS_FOLDER).mkdirs();
            new FileManager(CONFIG_FILE).initFile(IOUtils.toByteArray(getClass().getResourceAsStream("/config.yml")));
        } catch(IOException e) {
            e.printStackTrace();
        }

        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(CONFIG_FILE);
        } catch(IOException e) {
            e.printStackTrace();
        }
        chat = new Chat("&f[&bSpaciousLib&f] ");
        if(!config.contains("config_version") || config.getInt("config_version") < 4){
            try {
                chat.sendSender("&cAttempting to upgrade the old configuration...");
                chat.sendSender("&cCreating a backup for the old configuration....");
                new FileManager(CONFIG_FILE_OLD).delete().initFile(new FileManager(CONFIG_FILE).read());
                new FileManager(CONFIG_FILE).delete();
                chat.sendSender("&cDeleted the old config.yml file!");
                chat.sendSender("&cCreating the new config.yml file...");
                new FileManager(CONFIG_FILE).initFile(IOUtils.toByteArray(getClass().getResourceAsStream("/config.yml")));
                config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(CONFIG_FILE);
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
        for(String proxy : config.getStringList("proxies")){
            String[] x = proxy.split(":");
            ProxyUtils.put(x[0], Integer.parseInt(x[1]));
        }

        chat.sendSender("&eStarting the tasks...");
        if(config.getBoolean("auto_renew_skin", false)) {
            getProxy().getScheduler().schedule(this, new CachedSkinTask(), 0, 60, TimeUnit.SECONDS);
        }
        if(config.getBoolean("stats", true)){
            new Updater1520156620("1520156620", this);
        }

        chat.sendSender("&eRegistering the listeners...");
        getProxy().getPluginManager().registerListener(this, new SpigotListener());
        getProxy().getPluginManager().registerListener(this, new PlaceholderListener());
        getProxy().getPluginManager().registerListener(this, new PlayerCleanerListener());

        chat.sendSender("&eRegistering the messaging channel...");
        getProxy().registerChannel(CHANNEL);

        if(config.getBoolean("dev_mode", false)){
            chat.sendSender("&aSwitched to the development mode!");
            getProxy().getPluginManager().registerListener(this, new SLDev());
        }
    }

    @Override
    public void onDisable() {
        if(PlaceholderAPI.asyncTask != null){
            PlaceholderAPI.asyncTask.stop();
        }
    }
}
