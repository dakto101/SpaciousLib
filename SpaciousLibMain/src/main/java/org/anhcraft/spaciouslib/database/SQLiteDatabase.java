package org.anhcraft.spaciouslib.database;

import org.anhcraft.spaciouslib.utils.ExceptionThrower;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDatabase extends Database{
    /**
     * Creates a new connection to a SQLite database
     * @param db a database file
     */
    public void connect(File db) throws SQLException, ClassNotFoundException {
        ExceptionThrower.ifNull(db, new Exception("Database file must not null"));
        if(conn == null && state == null){
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + db.getAbsolutePath());
            state = conn.createStatement();
        }
    }
}
