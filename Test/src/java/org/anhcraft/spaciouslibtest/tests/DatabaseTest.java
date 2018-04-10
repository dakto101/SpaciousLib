package org.anhcraft.spaciouslibtest.tests;

import org.anhcraft.spaciouslib.database.SQLiteDatabase;
import org.anhcraft.spaciouslib.io.FileManager;

import java.io.File;

public class DatabaseTest {
    private static final File DB_FILE = new File("test.db");

    public DatabaseTest(){
        try {
            // creates the database file
            new FileManager(DB_FILE).create();

            SQLiteDatabase database = new SQLiteDatabase();
            // connects to the database
            database.connect(DB_FILE);
            // creates a table
            database.update("CREATE TABLE IF NOT EXISTS `spaciouslib` (" +
                    " `key` varchar(255)," +
                    " `value` int" +
                    ")");
            // disconnects from the connection
            database.disconnect();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
