package org.anhcraft.spaciouslib.database;

import java.sql.SQLException;
import java.util.LinkedHashMap;

/**
 * A class helps you to manage all database connections which were created by this library
 */
public class DatabaseManager {
    private static LinkedHashMap<String, Database> data = new LinkedHashMap<>();

    /**
     * Registers a new database connection
     * @param id connection's unique id
     * @param db database object
     */
    public static void register(String id, Database db){
        data.put(id, db);
    }

    /**
     * Unregisters a specific database connection
     * @param id connection id
     * @throws SQLException
     */
    public static void unregister(String id) throws SQLException {
        if(!data.containsKey(id)){
            return;
        }
        data.get(id).disconnect();
        data.remove(id);
    }
}
