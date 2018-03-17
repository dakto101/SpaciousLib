package org.anhcraft.spaciouslib.command;

import org.anhcraft.spaciouslib.utils.GameVersion;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;

import java.lang.reflect.Field;
import java.util.Map;

public class CommandManager {
    @Deprecated
    public static void register(Command command){
        try {
            Class<?> craftServerClass = Class.forName("org.bukkit.craftbukkit." + GameVersion.getVersion().toString() + ".CraftServer");
            Object craftServer = craftServerClass.cast(Bukkit.getServer());
            Field commandMapField = craftServerClass.getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            SimpleCommandMap commandMap = (SimpleCommandMap) commandMapField.get(craftServer);
            commandMap.register(command.getName(), command);
            commandMapField.set(craftServer, commandMap);
        } catch(IllegalAccessException | NoSuchFieldException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void register(PluginCommand command){
        try {
            Class<?> craftServerClass = Class.forName("org.bukkit.craftbukkit." + GameVersion.getVersion().toString() + ".CraftServer");
            Object craftServer = craftServerClass.cast(Bukkit.getServer());
            Field commandMapField = craftServerClass.getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            SimpleCommandMap commandMap = (SimpleCommandMap) commandMapField.get(craftServer);
            commandMap.register(command.getName(), command);
            commandMapField.set(craftServer, commandMap);
        } catch(IllegalAccessException | NoSuchFieldException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void unregister(Command command){
        try {
            Class<?> craftServerClass = Class.forName("org.bukkit.craftbukkit." + GameVersion.getVersion().toString() + ".CraftServer");
            Object craftServer = craftServerClass.cast(Bukkit.getServer());
            Field commandMapField = craftServerClass.getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            SimpleCommandMap commandMap = (SimpleCommandMap) commandMapField.get(craftServer);
            Field knownCommandsField = commandMap.getClass().getDeclaredField("knownCommands");
            Map<String, Command> knownCommands = (Map<String, Command>) knownCommandsField.get(commandMap);
            knownCommands.remove(command.getName());
            for (String alias : command.getAliases()){
                if(knownCommands.containsKey(alias) &&
                        knownCommands.get(alias).toString().contains(command.getName())){
                    knownCommands.remove(alias);
                }
            }
            commandMapField.set(craftServer, commandMap);
        } catch(IllegalAccessException | NoSuchFieldException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
