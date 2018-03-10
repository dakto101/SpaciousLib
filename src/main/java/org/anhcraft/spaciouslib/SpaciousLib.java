package org.anhcraft.spaciouslib;

import org.anhcraft.spaciouslib.listeners.*;
import org.anhcraft.spaciouslib.tasks.ArmorEquipEventTask;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpaciousLib extends JavaPlugin {
    public static SpaciousLib instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new PlayerJumpEventListener(), this);
        getServer().getPluginManager().registerEvents(new InteractItemListener(), this);
        getServer().getPluginManager().registerEvents(new AnvilCloseListener(), this);
        getServer().getPluginManager().registerEvents(new BowArrowHitEventListener(), this);
        getServer().getPluginManager().registerEvents(new AnvilFormListener(), this);

        new Updater1520156620("1520156620", this);
        new ArmorEquipEventTask().runTaskTimer(this, 0, 20);
    }

    @Override
    public void onDisable() {

    }
}
