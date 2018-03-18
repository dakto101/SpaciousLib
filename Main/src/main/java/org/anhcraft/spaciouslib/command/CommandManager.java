package org.anhcraft.spaciouslib.command;

import org.anhcraft.spaciouslib.utils.GameVersion;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Map;

public class CommandManager {
    /**
     * Registers a command from a Bukkit plugin
     * @param plugin the plugin which has that command
     * @param command the command
     */
    public static void register(JavaPlugin plugin, PluginCommand command){
        try {
            Class<?> craftServerClass = Class.forName("org.bukkit.craftbukkit." + GameVersion.getVersion().toString() + ".CraftServer");
            Object craftServer = craftServerClass.cast(Bukkit.getServer());
            Field commandMapField = craftServerClass.getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            SimpleCommandMap commandMap = (SimpleCommandMap) commandMapField.get(craftServer);
            commandMap.register(plugin.getDescription().getName(), command);
            commandMapField.set(craftServer, commandMap);

            Field simplePluginManagerField = craftServerClass.getDeclaredField("pluginManager");
            simplePluginManagerField.setAccessible(true);
            SimplePluginManager simplePluginManager = (SimplePluginManager) simplePluginManagerField.get(craftServer);
            Field commandMapPluginManagerField = SimplePluginManager.class.getDeclaredField("commandMap");
            commandMapPluginManagerField.setAccessible(true);
            SimpleCommandMap commandPluginManagerMap = (SimpleCommandMap) commandMapPluginManagerField.get(simplePluginManager);
            commandPluginManagerMap.register(plugin.getDescription().getName(), command);
            commandMapPluginManagerField.set(simplePluginManager, commandPluginManagerMap);
            simplePluginManagerField.set(craftServer, simplePluginManager);
        } catch(IllegalAccessException | NoSuchFieldException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void unregister(JavaPlugin plugin, PluginCommand command){
        try {
            Class<?> craftServerClass = Class.forName("org.bukkit.craftbukkit." + GameVersion.getVersion().toString() + ".CraftServer");

            Object craftServer = craftServerClass.cast(Bukkit.getServer());
            Field commandMapField = craftServerClass.getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            SimpleCommandMap commandMap = (SimpleCommandMap) commandMapField.get(craftServer);
            Field knownCommandsField = commandMap.getClass().getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true);
            Map<String, Command> knownCommands = (Map<String, Command>) knownCommandsField.get(commandMap);
            knownCommands.remove(plugin.getName()+":"+command.getName());
            for (String alias : command.getAliases()){
                alias = plugin.getName()+":"+alias;
                if(knownCommands.containsKey(alias) &&
                        knownCommands.get(alias).toString().contains(command.getName())){
                    knownCommands.remove(alias);
                }
            }
            knownCommandsField.set(commandMap, knownCommands);
            commandMapField.set(craftServer, commandMap);

            Field simplePluginManagerField = craftServerClass.getDeclaredField("pluginManager");
            simplePluginManagerField.setAccessible(true);
            SimplePluginManager simplePluginManager = (SimplePluginManager) simplePluginManagerField.get(craftServer);
            Field commandMapPluginManagerField = SimplePluginManager.class.getDeclaredField("commandMap");
            commandMapPluginManagerField.setAccessible(true);
            SimpleCommandMap commandPluginManagerMap = (SimpleCommandMap) commandMapPluginManagerField.get(simplePluginManager);

            Field knownCommandsPluginManagerField = commandPluginManagerMap.getClass().getDeclaredField("knownCommands");
            knownCommandsPluginManagerField.setAccessible(true);
            Map<String, Command> knownCommandsPluginManager = (Map<String, Command>) knownCommandsPluginManagerField.get(commandPluginManagerMap);
            knownCommandsPluginManager.remove(plugin.getName()+":"+command.getName());
            for (String alias : command.getAliases()){
                alias = plugin.getName()+":"+alias;
                if(knownCommandsPluginManager.containsKey(alias) &&
                        knownCommandsPluginManager.get(alias).toString().contains(command.getName())){
                    knownCommandsPluginManager.remove(alias);
                }
            }
            knownCommandsPluginManagerField.set(commandPluginManagerMap, knownCommandsPluginManager);
            commandMapPluginManagerField.set(simplePluginManager, commandPluginManagerMap);
            simplePluginManagerField.set(craftServer, simplePluginManager);
        } catch(IllegalAccessException | NoSuchFieldException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
