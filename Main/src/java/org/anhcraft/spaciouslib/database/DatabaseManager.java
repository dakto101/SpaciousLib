package org.anhcraft.spaciouslib.database;

import java.sql.SQLException;
import java.util.LinkedHashMap;

public class DatabaseManager {
    private static LinkedHashMap<String, Database> data = new LinkedHashMap<>();

    /**
     * Register a new database connection
     * @param id connection's id
     * @param db database
     */
    public static void register(String id, Database db){
        data.put(id, db);
    }

    /**
     * Unregister and close a specific database connection
     * @param id connection's id
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
