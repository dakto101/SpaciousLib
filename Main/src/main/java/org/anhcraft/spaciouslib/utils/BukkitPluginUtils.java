package org.anhcraft.spaciouslib.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BukkitPluginUtils {
    public static Plugin getPlugin(String p){
        for(Plugin x : Bukkit.getServer().getPluginManager().getPlugins()){
            if(x.getName().equals(p)){
                return x;
            }
        }
        return null;
    }

    public static File getPluginFile(String p) throws IOException {
        File c = null;
        File directory = new File("plugins/");
        if(directory.exists() && directory.isDirectory() && directory.listFiles() != null) {
            HashMap<String, File> files = new HashMap<>();
            for(File file : directory.listFiles()) {
                if(file.isFile()) {
                    String type = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                    if(-1 < file.getName().lastIndexOf(".") && type.equals("jar")) {
                        files.put(file.getName().replace("." + type, ""), file);
                    }
                }
            }
            if(0 < files.size()){
                if(files.containsKey(p)){
                    c = files.get(p);
                }
                else if(files.containsKey(p.toLowerCase())){
                    c = files.get(p.toLowerCase());
                }
                else if(files.containsKey(p.toUpperCase())){
                    c = files.get(p.toUpperCase());
                }
                p = p.split("\\.")[0];
                if(files.containsKey(p)){
                    c = files.get(p);
                }
                else if(files.containsKey(p.toLowerCase())){
                    c = files.get(p.toLowerCase());
                }
                else if(files.containsKey(p.toUpperCase())){
                    c = files.get(p.toUpperCase());
                }
            }
        }
        return c;
    }

    public static Boolean enablePlugin(Plugin p){
        if(!Bukkit.getServer().getPluginManager().isPluginEnabled(p)){
            Bukkit.getServer().getPluginManager().enablePlugin(p);
            return true;
        } else {
            return false;
        }
    }

    public static Boolean enablePlugin(String p){
        if(!Bukkit.getServer().getPluginManager().isPluginEnabled(p)){
            Bukkit.getServer().getPluginManager().enablePlugin(getPlugin(p));
            return true;
        } else {
            return false;
        }
    }

    public static Boolean disablePlugin(Plugin p){
        if(Bukkit.getServer().getPluginManager().isPluginEnabled(p)){
            Bukkit.getServer().getPluginManager().disablePlugin(p);
            return true;
        } else {
            return false;
        }
    }

    public static Boolean disablePlugin(String p){
        if(Bukkit.getServer().getPluginManager().isPluginEnabled(p)){
            Bukkit.getServer().getPluginManager().disablePlugin(getPlugin(p));
            return true;
        } else {
            return false;
        }
    }

    public static Plugin loadPlugin(String f) throws InvalidDescriptionException,
            InvalidPluginException, IOException {
        File fl = getPluginFile(f);
        if(fl != null){
            return Bukkit.getServer().getPluginManager().loadPlugin(fl);
        } else {
            return null;
        }
    }

    public static Boolean loadPlugin(File f) throws InvalidDescriptionException,
            InvalidPluginException {
        if(f.exists()){
            Bukkit.getServer().getPluginManager().loadPlugin(f);
            return true;
        } else {
            return false;
        }
    }

    public static boolean unloadPlugin(Plugin plugin) throws NoSuchFieldException,
            IllegalAccessException, IOException {
        String name = plugin.getName();
        PluginManager pluginManager = Bukkit.getPluginManager();
        SimpleCommandMap commandMap = null;
        List<Plugin> plugins = null;
        Map<String, Plugin> names = null;
        Map<String, Command> commands = null;

        disablePlugin(plugin);
        if (pluginManager != null) {
            Field pluginsField = Bukkit.getPluginManager().getClass()
                    .getDeclaredField("plugins");
            pluginsField.setAccessible(true);

            Field lookupNamesField = Bukkit.getPluginManager().getClass()
                    .getDeclaredField("lookupNames");

            lookupNamesField.setAccessible(true);
            Field commandMapField = Bukkit.getPluginManager().getClass()
                    .getDeclaredField("commandMap");

            commandMapField.setAccessible(true);
            Field commandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            commandsField.setAccessible(true);

            commandMap = (SimpleCommandMap) commandMapField.get(pluginManager);
            commands = (Map<String, Command>) commandsField.get(commandMap);
            plugins = (List<Plugin>) pluginsField.get(pluginManager);
            names = (Map<String, Plugin>) lookupNamesField.get(pluginManager);
        }

        if (plugins != null && plugins.contains(plugin)) {
            plugins.remove(plugin);
        }
        if (names != null && names.containsKey(name)) {
            names.remove(name);
        }
        if (commandMap != null) {
            for (Iterator<Map.Entry<String, Command>> it = commands
                    .entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Command> entry = it.next();
                if (entry.getValue() instanceof PluginCommand) {
                    PluginCommand c = (PluginCommand) entry.getValue();
                    if (c.getPlugin() == plugin) {
                        c.unregister(commandMap);
                        it.remove();
                    }
                }
            }
        }
        ClassLoader cl = plugin.getClass().getClassLoader();
        if (cl instanceof URLClassLoader) {
            ((URLClassLoader) cl).close();
        }
        System.gc();
        return true;
    }

    public static boolean unloadPlugin(String pl) throws NoSuchFieldException, IllegalAccessException, IOException {
        Plugin plugin = getPlugin(pl);
        return unloadPlugin(plugin);
    }

    public static void reload(Plugin plugin) throws IllegalAccessException, NoSuchFieldException,
            IOException, InvalidDescriptionException, InvalidPluginException {
        if (plugin != null) {
            BukkitPluginUtils.disablePlugin(plugin);
            unloadPlugin(plugin);
            BukkitPluginUtils.enablePlugin(BukkitPluginUtils.loadPlugin(plugin.getName()));
        }
    }

    public static void update(Plugin p, String fileurl, String newfile, File oldfile)
            throws IllegalAccessException, NoSuchFieldException, IOException,
            InvalidDescriptionException, InvalidPluginException {
        disablePlugin(p);
        unloadPlugin(p);
        if(URLUtils.download(fileurl, "plugins/" + newfile)) {
            if(oldfile.exists()){
                oldfile.delete();
            }
            enablePlugin(loadPlugin(newfile.replace(".jar","")));
        }
    }
}
