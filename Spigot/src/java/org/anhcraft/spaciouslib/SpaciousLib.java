package org.anhcraft.spaciouslib;

import org.anhcraft.spaciouslib.bungee.BungeeAPI;
import org.anhcraft.spaciouslib.io.DirectoryManager;
import org.anhcraft.spaciouslib.listeners.*;
import org.anhcraft.spaciouslib.mojang.SkinAPI;
import org.anhcraft.spaciouslib.npc.NPCManager;
import org.anhcraft.spaciouslib.placeholder.PlaceholderAPI;
import org.anhcraft.spaciouslib.tasks.ArmorEquipEventTask;
import org.anhcraft.spaciouslib.tasks.CachedSkinTask;
import org.anhcraft.spaciouslib.utils.Chat;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class SpaciousLib extends JavaPlugin {
    public static SpaciousLib instance;
    public final static File ROOT_FOLDER = new File("plugins/SpaciousLib/");
    public final static File SKINS_FOLDER = new File(ROOT_FOLDER, "skins/");
    public static Chat chat;

    @Override
    public void onEnable(){
        instance = this;

        new DirectoryManager(ROOT_FOLDER).mkdirs();
        new DirectoryManager(SKINS_FOLDER).mkdirs();

        chat = new Chat("&f[&bSpaciousLib&f] ");
        chat.sendSender("&aHello, world! I'm SpaciousLib library!");
        ////////////////////////////////////////////////////////////////////////////////////////////

        chat.sendSender("&eStarting the tasks...");
        new Updater1520156620("1520156620", this);
        // uses synchronous task because this task will call the ArmorEquipEvent event
        new ArmorEquipEventTask().runTaskTimer(this, 0, 20);
        new CachedSkinTask().runTaskTimerAsynchronously(this, 0, 200);

        chat.sendSender("&eInitializing the managers and the APIs...");
        new PlaceholderAPI();
        new SkinAPI();
        new BungeeAPI();

        chat.sendSender("&eRegistering the event listeners...");
        getServer().getPluginManager().registerEvents(new PlayerJumpEventListener(), this);
        getServer().getPluginManager().registerEvents(new InteractItemListener(), this);
        getServer().getPluginManager().registerEvents(new BowArrowHitEventListener(), this);
        getServer().getPluginManager().registerEvents(new PacketListener(), this);
        getServer().getPluginManager().registerEvents(new PlaceholderListener(), this);
        getServer().getPluginManager().registerEvents(new AnvilListener(), this);
        getServer().getPluginManager().registerEvents(new NPCManager(), this);
    }

    @Override
    public void onDisable() {
        chat.sendSender("&aUnregistering the managers and the APIs...");
        NPCManager.unregisterAll();
    }
}