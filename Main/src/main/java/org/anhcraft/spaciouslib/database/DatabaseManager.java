package org.anhcraft.spaciouslib.database;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.LinkedHashMap;

public class DatabaseManager {
    private static LinkedHashMap<JavaPlugin, LinkedHashMap<String, Database>> data = new LinkedHashMap<>();

    /**
     * Register a new database connection
     * @param plugin Bukkit plugin
     * @param id connection's id
     * @param db database
     */
    public static void register(JavaPlugin plugin, String id, Database db){
        LinkedHashMap<String, Database> a = new LinkedHashMap<>();
        if(data.containsKey(plugin)){
            a = data.get(plugin);
        }
        a.put(id, db);
        data.put(plugin, a);
    }

    /**
     * Unregister and close a specific database connection
     * @param plugin Bukkit plugin
     * @param id connection's id
     * @throws SQLException
     */
    public static void unregister(JavaPlugin plugin, String id) throws SQLException {
        LinkedHashMap<String, Database> a = new LinkedHashMap<>();
        if(data.containsKey(plugin)){
            a = data.get(plugin);
        }
        if(!a.containsKey(id)){
            return;
        }
        a.get(id).disconnect();
        a.remove(id);
        data.put(plugin, a);
    }
}
