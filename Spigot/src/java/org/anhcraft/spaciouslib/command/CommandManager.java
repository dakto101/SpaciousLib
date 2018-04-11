package org.anhcraft.spaciouslib.command;

import org.anhcraft.spaciouslib.utils.GameVersion;
import org.anhcraft.spaciouslib.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

/**
 * A class helps you to manage plugin command
 */
public class CommandManager {
    private JavaPlugin plugin;
    private PluginCommand command;

    /**
     * Creates a new CommandManager instance
     * @param command the command
     * @param plugin the plugin which owned that command
     */
    public CommandManager(JavaPlugin plugin, PluginCommand command){
        this.plugin = plugin;
        this.command = command;
    }

    /**
     * Registers a command of a Bukkit plugin
     */
    public void register(){
        try {
            Class<?> craftServerClass = Class.forName("org.bukkit.craftbukkit." + GameVersion.getVersion().toString() + ".CraftServer");
            Object craftServer = ReflectionUtils.cast(craftServerClass, Bukkit.getServer());
            SimpleCommandMap commandMap = (SimpleCommandMap) ReflectionUtils.getField("commandMap", craftServerClass, craftServer);
            commandMap.register(this.plugin.getDescription().getName(), this.command);
            ReflectionUtils.setField("commandMap", craftServerClass, craftServer, commandMap);
            SimplePluginManager simplePluginManager = (SimplePluginManager) ReflectionUtils.getField("pluginManager", craftServerClass, craftServer);
            SimpleCommandMap commandPluginManagerMap = (SimpleCommandMap) ReflectionUtils.getField("commandMap", simplePluginManager.getClass(), simplePluginManager);
            commandPluginManagerMap.register(this.plugin.getDescription().getName(), this.command);
            ReflectionUtils.setField("commandMap", simplePluginManager.getClass(), simplePluginManager, commandPluginManagerMap);
            ReflectionUtils.setField("pluginManager", craftServerClass, craftServer, simplePluginManager);
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Unregisters a specific plugin command
     */
    public void unregister(){
        try {
            Class<?> craftServerClass = Class.forName("org.bukkit.craftbukkit." + GameVersion.getVersion().toString() + ".CraftServer");

            Object craftServer = ReflectionUtils.cast(craftServerClass, Bukkit.getServer());
            SimpleCommandMap commandMap = (SimpleCommandMap) ReflectionUtils.getField("commandMap", craftServerClass, craftServer);
            Map<String, Command> knownCommands = (Map<String, Command>) ReflectionUtils.getField("knownCommands", commandMap.getClass(), commandMap);
            knownCommands.remove(this.plugin.getName()+":"+this.command.getName());
            for (String alias : this.command.getAliases()){
                alias = this.plugin.getName()+":"+alias;
                if(knownCommands.containsKey(alias) &&
                        knownCommands.get(alias).toString().contains(this.command.getName())){
                    knownCommands.remove(alias);
                }
            }
            ReflectionUtils.setField("knownCommands", commandMap.getClass(), commandMap, knownCommands);
            ReflectionUtils.setField("commandMap", craftServerClass, craftServer, commandMap);

            SimplePluginManager simplePluginManager = (SimplePluginManager) ReflectionUtils.getField("pluginManager", craftServerClass, craftServer);
            SimpleCommandMap commandPluginManagerMap = (SimpleCommandMap) ReflectionUtils.getField("commandMap", simplePluginManager.getClass(), simplePluginManager);

            Map<String, Command> knownCommandsPluginManager = (Map<String, Command>) ReflectionUtils.getField("knownCommands", commandPluginManagerMap.getClass(), commandPluginManagerMap);
            knownCommandsPluginManager.remove(this.plugin.getName()+":"+this.command.getName());
            for (String alias : this.command.getAliases()){
                alias = this.plugin.getName()+":"+alias;
                if(knownCommandsPluginManager.containsKey(alias) &&
                        knownCommandsPluginManager.get(alias).toString().contains(this.command.getName())){
                    knownCommandsPluginManager.remove(alias);
                }
            }
            ReflectionUtils.setField("knownCommands", commandPluginManagerMap.getClass(), commandPluginManagerMap, knownCommandsPluginManager);
            ReflectionUtils.setField("commandMap", simplePluginManager.getClass(), simplePluginManager, commandPluginManagerMap);
            ReflectionUtils.setField("pluginManager", craftServerClass, craftServer, simplePluginManager);
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
